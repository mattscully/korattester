/*
 * Created on Apr 13, 2005
 *
 */
package com.scully.korat.popup.actions.model;

import org.eclipse.jdt.core.IField;

/**
 * @author mscully
 *
 */
public class FinMethod
{
    static String DECLARATION = "\tpublic static Finitization fin";
    
    String className = null;
    FinParameters parameters = null;
	FinBounds bounds = null;
    
    public FinMethod(String className)
    {
        this.className = className;
        this.parameters = new FinParameters();
        this.bounds = new FinBounds(className);
    }
    
    public void addField(IField field)
    {
        this.parameters.addParametersForField(field);
        this.bounds.addBoundsForField(field);
    }
    
    public String toString()
    {
	    StringBuffer body = new StringBuffer(512);
        
	    // create method signature
	    body.append(DECLARATION).append(className).append(" (\n");
        body.append(this.parameters.toString()).append(") {\n");
        
        // create comment
        body.append("\n\t\t// TODO: Verify Finitization Skeleton\n");
        
        // create finitization object
        body.append("\t\tFinitization f = new Finitization(").append(this.className).append(".class);\n");
        
        // list field bounds
        // create comment
        body.append("\n\t\t// TODO: Update createObjects method with fully qualified name\n");
        body.append(this.bounds.toString());
        
        // return finitization object
        body.append("\t\treturn f;\n\t}\n\n");
        
        // create helper fin method
	    body.append(DECLARATION).append(className).append("(");
        body.append("int scope) {\n");
        // create comment
        body.append("\t\t// TODO: Verify helper scope\n");
        body.append("\t\treturn fin").append(className).append("(\n");
        body.append("\t\t\t").append(this.parameters.toScopeString());
        body.append(");\n\t}\n");
        
        return body.toString();
    }
    
    /**
     * @return Returns the bounds.
     */
    public FinBounds getBounds()
    {
        return bounds;
    }
    /**
     * @return Returns the parameters.
     */
    public FinParameters getParameters()
    {
        return parameters;
    }
}
