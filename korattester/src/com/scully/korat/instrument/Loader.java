package com.scully.korat.instrument;

import javassist.ClassPool;

import org.apache.commons.beanutils.MethodUtils;

public class Loader extends javassist.Loader
{
    public Loader()
    {
    }

    public Loader(ClassLoader classLoader, ClassPool pool)
    {
        super(classLoader, pool);
    }

    public Object invokeExactMethod(String classname, String methodName, Object[] args) throws Throwable
    {
        Class c = loadClass(classname);
        return MethodUtils.invokeExactMethod(c.newInstance(), methodName, args);
    }

}
