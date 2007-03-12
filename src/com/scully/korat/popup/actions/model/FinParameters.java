/*
 * Created on Apr 13, 2005
 *
 */
package com.scully.korat.popup.actions.model;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;

/**
 * @author mscully
 *
 */
public class FinParameters
{
    private Set parameters = new LinkedHashSet();
    static class Parameter
    {
        String type;
        String name;
        
        public Parameter(String type, String name)
        {
            this.type = type;
            this.name = name;
        }
        
        public String toString()
        {
            return new StringBuffer(type).append(" ").append(name).toString();
        }
        
        public String getName()
        {
            return this.name;
        }
        
        
        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        public boolean equals(Object o)
        {
            if(o instanceof Parameter)
            {
                return this.toString().equals(o.toString());
            }
            return false;
        }
        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        public int hashCode()
        {
            return this.toString().hashCode();
        }
    }
    
    public void addParametersForField(IField field)
    {
        try
        {
	        String name = getName(field);
            String type = Signature.toString(field.getTypeSignature());
	        
	        // if native type, create range parameters
	        if(Util.isNativeType(field))
	        {
	            Parameter p1 = new Parameter(type, "MIN_"+name);
	            Parameter p2 = new Parameter(type, "MAX_"+name);
		        parameters.add(p1);
		        parameters.add(p2);
	        }
	        else
	        {
		        Parameter p = new Parameter("int", "NUM_"+type);
		        parameters.add(p);
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
        StringBuffer buf = new StringBuffer();
        
        if(!this.parameters.isEmpty())
        {
            buf.append("\t\t\t");
	        Iterator i = this.parameters.iterator();
	        while (i.hasNext())
	        {
	            Parameter param = (Parameter) i.next();
	            buf.append(param.toString()).append(",\n\t\t\t");
	        }
	        // don't return the last comma, newline, and tab
	        return buf.substring(0, buf.length() - 5);
        }
        return "";
    }
    
    /**
     * @return
     */
    public String toScopeString()
    {
        StringBuffer buf = new StringBuffer();
        
        if(!this.parameters.isEmpty())
        {
	        Iterator i = this.parameters.iterator();
	        while (i.hasNext())
	        {
	            Parameter param = (Parameter) i.next();
	            buf.append(getArgument(param, "scope")).append(", ");
	        }
	        // don't return the last comma, and space
	        return buf.substring(0, buf.length() - 2);
        }
        return "";
    }
    
    public String toMethodCallString()
    {
        StringBuffer buf = new StringBuffer();
        
        if(!this.parameters.isEmpty())
        {
	        Iterator i = this.parameters.iterator();
	        while (i.hasNext())
	        {
	            Parameter param = (Parameter) i.next();
	            buf.append(getArgument(param, "3")).append(", ");
	        }
	        // don't return the last comma, and space
	        return buf.substring(0, buf.length() - 2);
        }
        return "";
        
    }

    /**
     * @param param
     * @return
     */
    private String getArgument(Parameter param, String max)
    {
        if(param.getName().startsWith("MIN"))
        {
            return "0";
        }
        return max;
    }
    
    public boolean isEmpty()
    {
        return this.parameters.isEmpty();
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
            return parent.getElementName() + "_" + name;
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
            prefix.insert(0, parent.getElementName()+"_");
        }
        while(grandParent != null)
        {
            String grandName = grandParent.getElementName();
            prefix.insert(0, grandName+"_");
            grandParent = grandParent.getDeclaringType();
        }
        
        // delete eldest parent
        prefix.delete(0, prefix.indexOf("_")+1);
        
        return prefix.toString() + name;
        */
        
    }

}
