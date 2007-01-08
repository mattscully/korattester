/*
 * Created on Apr 13, 2005
 *
 */
package com.scully.korat.popup.actions.model;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;

/**
 * @author mscully
 *
 */
public class Util
{
    public static boolean isNativeType(IField field) 
    {
        boolean returnVal = false;
        char[] sig;
        try
        {
            sig = field.getTypeSignature().toCharArray();
	        
            switch (sig[0])
	        {
	        case 'B': // byte
	        case 'C': // char
	        case 'D': // double
	        case 'F': // float
	        case 'I': // int
	        case 'J': // long
	        case 'S': // short
	        // TODO: Z is boolean, how do we handle these?
	        // case 'Z': // boolean
	            returnVal = true;
	            break;
	
	        default:
	            returnVal = false;
	            break;
	        }
        }
        catch (JavaModelException e)
        {
            e.printStackTrace();
        }
        
        return returnVal;
    }
    
    public static String getSignatureType(String sig) 
    {
        String type = "";
        
        if("B".equals(sig))
        {
            type = "byte";
        }
        else if("C".equals(sig))
        {
            type = "char";
        }
        else if("D".equals(sig))
        {
            type = "double";
        }
        else if("F".equals(sig))
        {
            type = "float";
        }
        else if("I".equals(sig))
        {
            type = "int";
        }
        else if("J".equals(sig))
        {
            type = "long";
        }
        else if("S".equals(sig))
        {
            type = "short";
        }
        else if("Z".equals(sig))
        {
            type = "boolean";
        }
        else if(sig.startsWith("Q"))
        {
            int idx = sig.indexOf(';');
            if(idx > 0)
            {
	            type = sig.substring(1, idx);
            }
            else
            {
	            type = sig.substring(1);
            }
        }
        
        return type;
    }

    /**
     * @param string
     * @return
     */
    public static String getParameterNewDeclaration(String sig)
    {
        String declaration = "";
        
        if("B".equals(sig))
        {
            declaration = "new Byte(1)";
        }
        else if("C".equals(sig))
        {
            declaration = "new Character(1)";
        }
        else if("D".equals(sig))
        {
            declaration = "new Double(1D)";
        }
        else if("F".equals(sig))
        {
            declaration = "new Float(1.0F";
        }
        else if("I".equals(sig))
        {
            declaration = "new Integer(1)";
        }
        else if("J".equals(sig))
        {
            declaration = "new Long(1L)";
        }
        else if("S".equals(sig))
        {
            declaration = "new Short(1)";
        }
        else if("Z".equals(sig))
        {
            declaration = "new Boolean(true)";
        }
        else if(sig.startsWith("Q"))
        {
            int idx = sig.indexOf(';');
            if(idx > 0)
            {
	            declaration = "new " + sig.substring(1, idx) + "()";
            }
            else
            {
	            declaration = sig.substring(1);
	            declaration = "new " + sig.substring(1) + "()";
            }
        }
        
        return declaration;
    }
}
