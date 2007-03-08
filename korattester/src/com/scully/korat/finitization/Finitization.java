/*
 * Created on Apr 15, 2005
 *
 */
package com.scully.korat.finitization;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import com.scully.korat.IKoratObservable;
import com.scully.korat.KoratObserver;
import com.scully.korat.instrument.Instrumenter;
import com.scully.korat.map.StateFieldDTO;
import com.scully.korat.map.StateObjectDTO;
import com.scully.korat.map.TestStateSpaceDTO;

/**
 * This class sets the bounds for the state space.  
 * * @author mscully
 *
 */
public class Finitization
{

    /** The class of the object that owns the method to be tested. */
    Class rootClass = null;

    /** The object that owns the method to be tested. */
    Object rootObject = null;

    /**
     * <p>All of the registered fields mapped to their field domains.</p>
     */
    Map<ObjField, FieldDomain> space = new HashMap<ObjField, FieldDomain>();

    /**
     * <p>All of the registered fields mapped to their <code>ObjField</code>s.</p>
     * <p>Key = Field, Value = <code>ObjField</code></p>
     */
    Map<Field, List<ObjField>> objFieldsByName = new HashMap<Field, List<ObjField>>();

    List<ObjField> fieldOrdering = new ArrayList<ObjField>();

    ObjField[] fieldOrderingCache = null;

    KoratObserver observer = null;

    //    ClassLoader koratLoader = null;

    Instrumenter instrumenter;

    private boolean instrument;

    /**
     * Creates a new Finitization for the following class.
     * 
     * <ul>
     * <li>assign class to rootObject</li> 
     * <li>create new instance of rootClass as rootObject</li> 
     * <li>create field objects</li>
     * </ul>
     * @param testClass Class to create finitization for.
     */
    public Finitization(Class testClass)
    {
        this.rootClass = testClass;
        this.observer = new KoratObserver();
        try
        {
            this.rootObject = this.rootClass.newInstance();
            // register the root object's fields
            createObjFields(this.rootObject);
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    public Finitization(TestStateSpaceDTO testStateSpaceDTO)
    {
        this(testStateSpaceDTO, null, true);
    }

    /**
     * Completely initialize this Finitization object from a testStateSpaceDTO.
     * @param testStateSpaceDTO
     */
    public Finitization(TestStateSpaceDTO testStateSpaceDTO, String[] codeClasspath, boolean instrument)
    {
        try
        {
            // create root object for state space
            this.instrument = instrument;

            if (instrument)
            {
                //                // create the class loader
                //                if (codeClasspath != null && codeClasspath.length > 0)
                //                {
                //                    try
                //                    {
                //                        List<URL> paths = new ArrayList<URL>();
                //                        for (String path : codeClasspath)
                //                        {
                //                            paths.add(new File(path).toURL());
                //                        }
                //                        URL[] classpathURLs = paths.toArray(new URL[]{});
                //                        this.koratLoader = new URLClassLoader(classpathURLs, this.getClass().getClassLoader());
                //                    }
                //                    catch (MalformedURLException e)
                //                    {
                //                        // TODO Auto-generated catch block
                //                        e.printStackTrace();
                //                    }
                //                }

                // instrument the state space
                instrumenter = new Instrumenter(testStateSpaceDTO, codeClasspath);
                instrumenter.instrument();
                //                this.rootClass = Class.forName(testStateSpaceDTO.getRootClass());
                this.rootClass = this.instrumenter.lookup(testStateSpaceDTO.getRootClass());
                this.rootObject = this.rootClass.newInstance();
                // register the observer with the new object
                IKoratObservable observable = (IKoratObservable) this.rootObject;
                
                // this.observer = new KoratObserver();
                this.observer = (KoratObserver) this.instrumenter.lookup("com.scully.korat.KoratObserver").newInstance();
                observable.$kor_setObserver(observer);
            }
            else
            {
                this.rootClass = Class.forName(testStateSpaceDTO.getRootClass());
                this.rootObject = this.rootClass.newInstance();
            }

            // register the root object's fields
            createObjFields(this.rootObject);

            // create Object types in state space
            //            StateObjectDTO[] stateObjects = testStateSpaceDTO.getStateObjects();
            List<StateObjectDTO> stateObjects = testStateSpaceDTO.getStateObjects();
            Map<String, ObjSet> objSets = new HashMap<String, ObjSet>();
            for (StateObjectDTO stateObjectDTO : stateObjects)
            {
                ObjSet objSet = this.createObjects(stateObjectDTO.getType(), stateObjectDTO.getQuantity());
                if (stateObjectDTO.isIncludeNullFlag())
                {
                    objSet.add(null);
                }
                objSets.put(stateObjectDTO.getType(), objSet);
            }
            //            for (int i = 0; i < stateObjects.length; i++)
            //            {
            //                StateObjectDTO stateObjectDTO = stateObjects[i];
            //                ObjSet objSet = this.createObjects(stateObjectDTO.getType(), stateObjectDTO.getQuantity());
            //                if(stateObjectDTO.isIncludeNullFlag())
            //                {
            //                    objSet.add(null);
            //                }
            //                objSets.put(stateObjectDTO.getType(), objSet);
            //            }

            // map fields to FinSets
            //            StateFieldDTO[] stateFields = testStateSpaceDTO.getStateFields();
            List<StateFieldDTO> stateFields = testStateSpaceDTO.getStateFields();
            for (StateFieldDTO stateField : stateFields)
            {
                Class parent = Class.forName(stateField.getParentClass());
                Field field = parent.getDeclaredField(stateField.getName());
                Class type = field.getType();
                if (type.equals(int.class))
                {
                    this.set(field, new IntSet(stateField.getMin(), stateField.getMax()));
                }
                else if (objSets.containsKey(type.getName()))
                {
                    this.set(field, objSets.get(type.getName()));
                }
                // TODO: Implement boolean
                else if (type.equals(boolean.class))
                {
                }
                else if (type.equals(byte.class))
                {
                    this.set(field, new ByteSet((byte) stateField.getMin(), (byte) stateField.getMax()));
                }
                // TODO: Implement char
                else if (type.equals(char.class))
                {
                }
                else if (type.equals(short.class))
                {
                    this.set(field, new ShortSet((short) stateField.getMin(), (short) stateField.getMax()));
                }
                else if (type.equals(long.class))
                {
                    // TODO: handle long type
                    this.set(field, new LongSet(stateField.getMin(), stateField.getMax()));
                }
                // TODO: Implement FloatSet
                else if (type.equals(float.class))
                {
                }
                else if (type.equals(double.class))
                {
                    // TODO: handle double type
                    this.set(field, new DoubleSet((double) stateField.getMin(), (double) stateField.getMax()));
                }
            }
            //            for (int i = 0; i < stateFields.length; i++)
            //            {
            //                StateFieldDTO stateField = stateFields[i];
            //                Class parent = Class.forName(stateField.getParentClass());
            //                Field field = parent.getDeclaredField(stateField.getName());
            //                Class type = field.getType();
            //                if(type.equals(int.class))
            //                {
            //                    this.set(field, new IntSet(stateField.getMin(), stateField.getMax()));
            //                }
            //                else if(objSets.containsKey(type.getName()))
            //                {
            //                    this.set(field, (FinSet) objSets.get(type.getName()));
            //                }
            //                // TODO: Implement boolean
            //                else if(type.equals(boolean.class))
            //                {
            //                }
            //                else if(type.equals(byte.class))
            //                {
            //                    this.set(field, new ByteSet((byte) stateField.getMin(), (byte) stateField.getMax()));
            //                }
            //                // TODO: Implement char
            //                else if(type.equals(char.class))
            //                {
            //                }
            //                else if(type.equals(short.class))
            //                {
            //                    this.set(field, new ShortSet((short) stateField.getMin(), (short) stateField.getMax()));
            //                }
            //                else if(type.equals(long.class))
            //                {
            //                    // TODO: handle long type
            //                    this.set(field, new LongSet(stateField.getMin(), stateField.getMax()));
            //                }
            //                // TODO: Implement FloatSet
            //                else if(type.equals(float.class))
            //                {
            //                }
            //                else if(type.equals(double.class))
            //                {
            //                    // TODO: handle double type
            //                    this.set(field, new DoubleSet((double) stateField.getMin(),(double) stateField.getMax()));
            //                }
            //            }
        }
        catch (ClassNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (SecurityException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (NoSuchFieldException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (NotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (CannotCompileException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * <p>
     * Create a mapping of field names as Strings to a list of ObjFields
     * as objects are created.
     * </p>
     * <p>
     * Essentially, this is registering this object's fields so that their
     * values can later be set using reflection.
     * </p>
     * @param object The object whose fields will be registered.
     */
    void createObjFields(Object object)
    {
        Class c = object.getClass();
        //        String name = null;
        //        if(c.equals(this.rootClass))
        //        {
        //            name = "";
        //        }
        //        else
        //        {
        //            // need '.' for appending field name below
        //            name = getSimpleName(c.getName()) + ".";
        //        }
        for (Field field : c.getDeclaredFields())
        {
            // ignore JML & Korat instrumented fields
            if (field.getName().startsWith("rac$") || field.getName().startsWith("$kor_"))
            {
                continue;
            }
            field.setAccessible(true);
            ObjField objField = new ObjField(object, field);
            System.out.println("Finitization.createObjFields ->  fieldName: " + field.getName());
            addObjFieldByName(field, objField);
            this.fieldOrdering.add(objField);
        }
    }

    /**
     * @param field
     * @param objField
     */
    void addObjFieldByName(Field field, ObjField objField)
    {
        List<ObjField> objFieldList = (ArrayList<ObjField>) this.objFieldsByName.get(field);
        if (objFieldList == null)
        {
            objFieldList = new ArrayList<ObjField>();
        }
        objFieldList.add(objField);
        this.objFieldsByName.put(field, objFieldList);
    }

    /**
     * @param name
     * @return
     */
    String getSimpleName(String name)
    {
        // sample class name:  test.reflect.Node$InnerNode
        String simpleName = name;
        int lastDotIndex = name.lastIndexOf('.');
        int lastDollarIndex = name.lastIndexOf('$');
        int lastPartIndex = Math.max(lastDotIndex, lastDollarIndex);
        if (lastPartIndex > 0)
        {
            simpleName = name.substring(lastPartIndex + 1);
        }
        return simpleName;
    }

    /**
     * @param string
     * @param node
     * @return
     */
    public ObjSet createObjects(String objName, int numObjects)
    {
        Class c = null;
        try
        {
            //            c = Class.forName(objName);
            if (this.instrument)
            {
                c = this.instrumenter.lookup(objName);
            }
            else
            {
                c = Class.forName(objName);
            }
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return createObjects(c, numObjects);
    }

    /**
     * <p>
     * Creates and registers <code>numObjects</code> number of objects,
     * adds them to a <code>ClassDomain</code>, and returns an <code>ObjSet</code>
     * containing the <code>ClassDomainIndex</code>.
     * </p>
     * 
     * @param objClass The class from which to create objects to register.
     * @param numObjects The number of objects to register.
     * @return an ObjSet containing the <code>ClassDomainIndex</code> for the objects.
     */
    public ObjSet createObjects(Class objClass, int numObjects)
    {
        ArrayList<Object> objects = new ArrayList<Object>();
        try
        {
            for (int i = 0; i < numObjects; i++)
            {
                Object o = objClass.newInstance();

                if (this.instrument)
                {
                    // register the observer with the new object
                    IKoratObservable observable = (IKoratObservable) o;
                    observable.$kor_setObserver(observer);
                }

                createObjFields(o);
                objects.add(o);
                System.out.println("Finitization.createObjects: " + objClass + "[" + i + "] = " + o);
            }
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        Object[] objArray = objects.toArray();
        ClassDomain classDomain = new ClassDomain();
        classDomain.set(objArray);
        ClassDomainIndex classDomainIndex = new ClassDomainIndex(classDomain);
        ObjSet objSet = new ObjSet();
        objSet.addClassDomainIndex(classDomainIndex);

        return objSet;
    }

    /**
     * <p>
     * Populate the state space for a specified field. The <code>FieldDomain</code>
     * is created from the <code>FinSet</code>.
     * </p>
     * 
     * <p>
     * Note: the state space is a map in the format: 
     * <code>&lt;ObjField, FieldDomain&gt;</code>.
     * </p>
     * @param string
     * @param nodes
     */
    public void set(Field field, FinSet finSet)
    {
        System.out.println("Finitization.set ->  fieldName: " + field.getName());
        // get the class domain indices for this field
        ClassDomainIndex[] classDomainIndices = finSet.getClassDomainIndices();
        FieldDomain fieldDomain = new FieldDomain(classDomainIndices);

        // get the ObjFields that have been created for this field
        List<ObjField> objFieldList = this.objFieldsByName.get(field);
        for (ObjField objField : objFieldList)
        {
            // add each ObjField to the state space
            this.space.put(objField, fieldDomain);
        }
    }

    /**
     * @return All of the registered fields mapped to their field domains.
     * Key = <code>ObjField</code>, Value = <code>FieldDomain</code>
     */
    public Map<ObjField, FieldDomain> getSpace()
    {
        return this.space;
    }

    /**
     * @return
     */
    public Object getRootObject()
    {
        return this.rootObject;
    }

    public Class getRootClass()
    {
        return this.rootClass;
    }

    /**
     * @return
     */
    public ObjField[] getObjFields()
    {
        if (this.space.isEmpty())
        {
            return new ObjField[0];
        }
        return this.fieldOrdering.toArray(new ObjField[0]);
    }

    /**
     * @return
     */
    public List<ObjField> getFieldOrdering()
    {
        return this.fieldOrdering;
    }

    /**
     * @param cv
     * @return
     */
    public String orderedCandidateString(Map cv)
    {
        if (this.fieldOrderingCache == null)
        {
            this.fieldOrderingCache = this.fieldOrdering.toArray(new ObjField[0]);
        }
        StringBuffer buf = new StringBuffer(64);
        for (ObjField objField : this.fieldOrderingCache)
        {
            buf.append(objField).append("=").append(cv.get(objField)).append(", ");
        }
        return buf.toString();
    }

    /**
     * @return the observer
     */
    public KoratObserver getObserver()
    {
        return observer;
    }
}
