/*
 * Created on Apr 13, 2005
 *
 */
package com.scully.korat.popup.actions.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;

/**
 * @author mscully
 *
 */
public class FinBounds
{
    private Map nativeBounds = new HashMap();
    private Map objectBounds = new HashMap();
    private Set objectTypes = new HashSet();
    private String className = null;
    
    public FinBounds(String className)
    {
        this.className = className;
    }
    
    public void addBoundsForField(IField field)
    {
        try
        {
            String name = getName(field);
            String type = Signature.toString(field.getTypeSignature());
            
            if(Util.isNativeType(field))
            {
                nativeBounds.put(name, type);
            }
            else
            {
//	            System.out.println("CU: " + field.getCompilationUnit());
//	            System.out.println("Handle: " + field.getHandleIdentifier());
//	            System.out.println("Signature: " + field.getTypeSignature());
                objectTypes.add(type);
                objectBounds.put(name, type);
            }
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (JavaModelException e)
        {
            e.printStackTrace();
        }
    }
   
    public String toString()
    {
        StringBuffer buf = new StringBuffer(32);
        
        // add the ObjSet declarations
        Iterator i = objectTypes.iterator();
        while (i.hasNext())
        {
            String type = (String) i.next();
            String varName = getVarName(type);
            buf.append("\t\tObjSet "+varName+" = f.createObjects(\"<complete fully qualified name here>"+type+"\", NUM_"+type+");\n");
            buf.append("\t\t"+varName+".add(null);\n");
        }
        
        // add the object bounds
        i = objectBounds.keySet().iterator();
        while (i.hasNext())
        {
            String name = (String) i.next();
            String type = (String) this.objectBounds.get(name);
            String varName = getVarName(type);
            buf.append("\t\tf.set(\""+name+"\", "+varName+");\n");
        }
        
        // add the native bounds
        i = nativeBounds.keySet().iterator();
        while (i.hasNext())
        {
            String name = (String) i.next();
            String type = (String) this.nativeBounds.get(name);
            // capitalize the first letter
            type = type.substring(0,1).toUpperCase() + type.substring(1);
            String nameUnderscores = name.replaceAll("\\.", "_");
            buf.append("\t\tf.set(\""+name+"\", new "+type+"Set(MIN_"+nameUnderscores+", MAX_"+nameUnderscores+"));\n");
        }
        return buf.toString();
    }
    
    /**
     * @param type
     * @return
     */
    private String getVarName(String type)
    {
        StringBuffer buf = new StringBuffer(type);
        if(type.endsWith("s"))
        {
            buf.append("e");
        }
        buf.append("s");
        buf.replace(0,1,buf.substring(0, 1).toLowerCase());
        return buf.toString();
    }

    private String getName(IField field)
    {
        String name = field.getElementName();
        IType parent = field.getDeclaringType();
        
        // most cases, this will happen
        if(parent.getDeclaringType() == null)
        {
            return name;
        }
        else
        {
            return parent.getElementName() + "." + name;
        }
        
        /*
         * This is logic to get a name with all field type
         * ancestors in a dot-separated String except for
         * the eldest ancestor.  This was removed because
         * it was determined only the immediate parent was
         * necessary.  Not deleting in case this is needed
         * later.
         * 
        StringBuffer prefix = new StringBuffer();
        IType grandParent = parent.getDeclaringType();
        if(grandParent != null)
        {
            prefix.insert(0, parent.getElementName()+".");
        }
        while(grandParent != null)
        {
            String grandName = grandParent.getElementName();
            prefix.insert(0, grandName+".");
            grandParent = grandParent.getDeclaringType();
        }
        
        // delete eldest parent
        prefix.delete(0, prefix.indexOf(".")+1);
        
        return prefix.toString() + name;
        */
        
    }
    
    
}
