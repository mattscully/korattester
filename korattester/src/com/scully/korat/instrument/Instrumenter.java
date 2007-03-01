package com.scully.korat.instrument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.scully.korat.map.StateFieldDTO;
import com.scully.korat.map.StateObjectDTO;
import com.scully.korat.map.TestStateSpaceDTO;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public class Instrumenter
{
    private static final String KORAT_PREFIX = "$kor_";

    TestStateSpaceDTO stateSpace;

    ClassPool classPool;

    public Instrumenter(TestStateSpaceDTO stateSpace)
    {
        this.stateSpace = stateSpace;
        this.classPool = ClassPool.getDefault();
    }

    /**
     * Steps to instrument code
     * 
     * <ol>
     * <li>Insert shadow fields</li>
     * <li>Insert Observer and registerObserver method, add Interface</li>
     * <li>Insert special getter/setter methods</li>
     * <li>Instrument field accesses in all methods</li>
     * </ol>
     * @throws NotFoundException if classes cannot be found
     * @throws CannotCompileException if instrumentation code could not compile
     *
     */
    public void instrument() throws NotFoundException, CannotCompileException
    {
        List<String> workList = new ArrayList<String>();
        workList.add(this.stateSpace.getRootClass());
        workList.addAll(this.getStateObjectNames());
        for(String className : workList)
        {
            CtClass cc = this.classPool.get(className);
            insertShadowFields(cc);
            insertObserver(cc);
            insertGettersSetters(cc);
        }
        for(String className : workList)
        {
            CtClass cc = this.classPool.get(className);
            instrumentFieldAccesses(cc);
            cc.toClass();
        }
    }

    public void insertShadowFields(CtClass cc) throws CannotCompileException
    {
        List<StateFieldDTO> fields = getFieldsForType(cc);
        for(StateFieldDTO fieldDTO : fields)
        {
            String fieldName = fieldDTO.getName();
            CtField field = CtField.make("int " + KORAT_PREFIX + fieldName + " = 0;", cc);
            cc.addField(field);
        }
    }

    public void insertObserver(CtClass cc) throws CannotCompileException, NotFoundException
    {
        CtClass iKoratObservable = this.classPool.get("com.scully.korat.IKoratObservable");
        cc.addInterface(iKoratObservable);
        // create $kor_observer field
        // com.scully.korat.IKoratObserver $kor_observer;
        CtClass iKoratObserver = this.classPool.get("com.scully.korat.IKoratObserver");
        CtField field = CtField.make(iKoratObserver.getName() + " " + KORAT_PREFIX + "observer = null;", cc);
        cc.addField(field);

        // create setObserver method
        // public void setObserver(com.scully.korat.IKoratObserver observer)
        // {
        //     this.$kor_observer = observer;
        // }
        CtMethod method = CtNewMethod.make("public void " + KORAT_PREFIX + "setObserver(" + iKoratObserver.getName()
                + " observer) {$0." + KORAT_PREFIX + "observer = observer; }", cc);
        cc.addMethod(method);
    }

    public void insertGettersSetters(CtClass cc) throws NotFoundException, CannotCompileException
    {
        List<StateFieldDTO> fieldNames = getFieldsForType(cc);
        for(StateFieldDTO field : fieldNames)
        {
            // getter
            //            public int $kor_getValue()
            //            {
            //                if(this.$kor_observer != null) 
            //                   this.$kor_observer.notify(this.$kor_value);
            //                return this.value;
            //            }
            String fieldName = field.getName();
            String shadowFieldName = KORAT_PREFIX + fieldName;
            String fieldType = field.getType();
            String getterName = KORAT_PREFIX + "get" + StringUtils.capitalize(fieldName);
            String observerName = KORAT_PREFIX + "observer";
            StringBuffer body = new StringBuffer(256);
            // public int $kor_getValue()
            body.append("public " + fieldType + " " + getterName + "(){");
            // if(this.$kor_observer != null) {
            body.append("if($0." + observerName + "!= null) {");
            // this.$kor_observer.notify(this.$kor_value); }
            body.append("$proceed($0." + shadowFieldName + "); }");
            // return this.value;
            body.append("return $0." + fieldName + "; }");
            CtMethod method = CtNewMethod.make(body.toString(), cc, "this." + KORAT_PREFIX + "observer", "notify");
            cc.addMethod(method);

            // setter
            //            public void $kor_setValue(int value)
            //            {
            //                if(this.$kor_observer != null) 
            //                    this.$kor_observer.notify(this.$kor_value);
            //                this.value = value;
            //            }
            String setterName = KORAT_PREFIX + "set" + StringUtils.capitalize(fieldName);
            // clear the buffer
            body.setLength(0);
            // public void $kor_setValue(int value)
            body.append("public void " + setterName + "(" + fieldType + " value){");
            // if(this.$kor_observer != null) {
            body.append("if($0." + observerName + "!= null) {");
            // this.$kor_observer.notify(this.$kor_value); }
            body.append("$proceed($0." + shadowFieldName + "); }");
            // this.value = value;
            body.append("$0." + fieldName + " = value; }");
            method = CtNewMethod.make(body.toString(), cc, "this." + KORAT_PREFIX + "observer", "notify");
            cc.addMethod(method);
        }
    }

    public void instrumentFieldAccesses(CtClass cc) throws CannotCompileException
    {
        cc.instrument(new ExprEditor() {
            public void edit(FieldAccess fieldAccess) throws CannotCompileException
            {
                // ignore instrumented fields
                if (fieldAccess.getFieldName().startsWith(KORAT_PREFIX))
                {
                    return;
                }
                else
                {
                    try
                    {
                        if (isStateSpaceField(fieldAccess) && !isInstrumentedMethod(fieldAccess))
                        {
                            //                            System.out.println("accessing field: " + fieldAccess.getFieldName() + " in \t"
                            //                                    + fieldAccess.where().getMethodInfo().getName() + "():"
                            //                                    + fieldAccess.getLineNumber());

                            // replace with getter
                            if (fieldAccess.isReader())
                            {
                                String getterName = KORAT_PREFIX + "get"
                                        + StringUtils.capitalize(fieldAccess.getFieldName());
                                if (fieldAccess.isStatic())
                                {
                                    fieldAccess.replace("{ $_ = " + getterName + "(); }");
                                }
                                else
                                {
                                    fieldAccess.replace("{ $_ = $0." + getterName + "(); }");
                                }
                            }
                            // replace with setter
                            else if (fieldAccess.isWriter())
                            {
                                String setterName = KORAT_PREFIX + "set"
                                        + StringUtils.capitalize(fieldAccess.getFieldName());
                                if (fieldAccess.isStatic())
                                {
                                    fieldAccess.replace("{ " + setterName + "($1); }");
                                }
                                else
                                {
                                    fieldAccess.replace("{ $0." + setterName + "($1); }");
                                }
                            }
                            else
                            {
                                new Exception("Should Not Get Here!").printStackTrace();
                            }
                        }
                    }
                    catch (NotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            protected boolean isInstrumentedMethod(FieldAccess fieldAccess)
            {
                return fieldAccess.where().getMethodInfo().getName().startsWith(KORAT_PREFIX);
            }

        });
    }

    boolean isStateSpaceField(FieldAccess fieldAccess) throws NotFoundException
    {
        CtField field = fieldAccess.getField();
        String parent = field.getDeclaringClass().getName();
        String name = field.getName();
        for (Iterator iter = this.stateSpace.getStateFields().iterator(); iter.hasNext();)
        {
            StateFieldDTO stateField = (StateFieldDTO) iter.next();
            if (parent.equals(stateField.getParentClass()) && name.equals(stateField.getName()))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * get  list of all field names for a given type
     * @param cc
     * @return
     */
    List<StateFieldDTO> getFieldsForType(CtClass cc)
    {
        List<StateFieldDTO> fieldNames = new ArrayList<StateFieldDTO>();
        List fields = this.stateSpace.getStateFields();
        String type = cc.getName();
        for (Iterator iter = fields.iterator(); iter.hasNext();)
        {
            StateFieldDTO field = (StateFieldDTO) iter.next();
            if (type.equals(field.getParentClass()))
            {
                fieldNames.add(field);
            }
        }
        return fieldNames;
    }

    Collection<String> getStateObjectNames()
    {
        List<String> objectNames = new ArrayList<String>();
        for (Iterator iter = this.stateSpace.getStateObjects().iterator(); iter.hasNext();)
        {
            StateObjectDTO stateObject = (StateObjectDTO) iter.next();
            objectNames.add(stateObject.getType());
        }
        return objectNames;
    }

}
