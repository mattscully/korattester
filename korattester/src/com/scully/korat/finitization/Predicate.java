/*
 * Created on Apr 17, 2005
 *
 */
package com.scully.korat.finitization;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author mscully
 *
 */
public class Predicate
{
    Method repOk = null;
    
    public static final String REPOK = "repOk";

    /**
     * @param class1
     */
    public Predicate(Class rootClass, String methodName)
    {
        try
        {
            repOk = rootClass.getDeclaredMethod(methodName, null);
            repOk.setAccessible(true);
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
    }
    
    public boolean invoke(Object rootObject)
    {
          Boolean booleanObj = null;
        try
        {
            booleanObj = (Boolean) repOk.invoke(rootObject, null);
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return booleanObj.booleanValue();
    }
}
