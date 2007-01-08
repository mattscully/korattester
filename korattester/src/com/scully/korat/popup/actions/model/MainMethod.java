/*
 * Created on May 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.scully.korat.popup.actions.model;

import org.eclipse.jdt.core.IMethod;

/**
 * @author mscully
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MainMethod
{
    IMethod testMethod = null;
    FinParameters parameters = null;
    
    static String DECLARATION = "\tpublic static void main(String[] args) {";
    
    /**
     * @param testMethod
     * @param parameters
     */
    public MainMethod(IMethod testMethod, FinParameters parameters)
    {
        this.testMethod = testMethod;
        this.parameters = parameters;
    }
    
    public String toString()
    {
        StringBuffer body = new StringBuffer(256);
        
	    // create method signature
	    body.append(DECLARATION);
        
        // create comment
        body.append("\n\n\t\t// TODO: Define Finitization Scope");
        body.append("\n\t\tFinitization f = finSearchTree(3);");
        body.append("\n\t\t//Finitization f = finSearchTree(");
        body.append(this.parameters.toMethodCallString());
  		body.append(");\n");
        
        // create test method signature and define parameters
        body.append("\n\t\t// ").append(this.testMethod.getElementName()).append(" method signature");
        body.append("\n\t\tString testMethod = \"").append(this.testMethod.getElementName()).append("\";");
        body.append("\n\t\tClass[] paramTypes = new Class[] { ").append(getParamTypes()).append(" };");
        body.append("\n\n\t\t// TODO: Define test parameters for inputs");
        body.append("\n\t\tObject[] paramValues = new Object[] { ").append(getValueDeclarations()).append(" };");
        
        // create Korat execution options
        body.append("\n\n\t\t// TODO: Set Iso-Morphism Breaking Flag");
        body.append("\n\t\tKoratClient.setIsoMorphismBreaking(true);");
        body.append("\n\n\t\t// TODO: Choose Korat feature execution");
        body.append("\n\t\tKoratClient.generateCvsAndTest(f, testMethod, paramTypes, paramValues);");
        body.append("\n\t\t// TODO: Define serialized CV directory");
        body.append("\n\t\tString serializePath = \"c:/korat-cvs\";");
        body.append("\n\t\t//KoratClient.serializeInputs(f, serializePath);");
        body.append("\n\t\t//KoratClient.testSerializedObjects(f, testMethod, paramTypes, paramValues, serializePath);");
        
        body.append("\n\n\t}\n\n");
        
        return body.toString();
    }

    /**
     * @return
     */
    private String getValueDeclarations()
    {
        if(!this.parameters.isEmpty())
        {
	        StringBuffer buf = new StringBuffer();
	        
	        String[] types = this.testMethod.getParameterTypes();
	        for (int i = 0; i < types.length; i++)
	        {
	            String declaration = Util.getParameterNewDeclaration(types[i]);
	            buf.append(declaration).append(", ");
	        }
	        return buf.substring(0, buf.length() - 2);
        }
        return "";
    }

    /**
     * @return
     */
    private String getParamTypes()
    {
        if(!this.parameters.isEmpty())
        {
	        StringBuffer buf = new StringBuffer();
	        
	        String[] types = this.testMethod.getParameterTypes();
	        for (int i = 0; i < types.length; i++)
	        {
	            String type = Util.getSignatureType(types[i]);
	            buf.append(type).append(".class, ");
	        }
	        return buf.substring(0, buf.length() - 2);
        }
        return "";
    }
    
}
