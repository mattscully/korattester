package com.scully.korat.instrument;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javassist.ClassPool;

import org.apache.commons.beanutils.MethodUtils;

/**
 * This class provides two features:
 * <ul>
 * <li>Debug information on which classes in total were loaded, which were
 * defined here, and which were delegated.</li>
 * <li>Exposes the invokeExactMethod method which allows the client to load
 * a class with this class loader and invoke a method on that class.
 * </ul>
 * @author mscully
 *
 */
public class Loader extends javassist.Loader
{
    private List<String> delegatedClasses = new ArrayList<String>();
    private List<String> definedHereClasses = new ArrayList<String>();
    private List<String> loadedClasses = new ArrayList<String>();
    private ClassPool classPool;
    
    public Loader()
    {
    }

    public Loader(ClassLoader classLoader, ClassPool pool)
    {
        super(classLoader, pool);
        this.classPool = pool;
    }
    
    public ClassPool getClassPool()
    {
        return this.classPool;
    }

    public Object invokeExactMethod(String classname, String methodName, Object[] args) throws Throwable
    {
        Class c = loadClass(classname);
        return MethodUtils.invokeExactMethod(c.newInstance(), methodName, args);
    }
    
    @Override
    protected Class findClass(String name) throws ClassNotFoundException
    {
        Class clazz = super.findClass(name);
        if(clazz != null)
        {
            definedHereClasses.add(name);
        }
        return clazz;
    }
    
    @Override
    protected Class delegateToParent(String classname) throws ClassNotFoundException
    {
        Class clazz = super.delegateToParent(classname);
        if(clazz != null)
        {
            delegatedClasses.add(classname);
        }
        return clazz;
    }
    
    @Override
    protected Class loadClass(String name, boolean resolve) throws ClassFormatError, ClassNotFoundException
    {
        Class clazz = super.loadClass(name, resolve);
        if(clazz != null)
        {
            loadedClasses.add(name);
        }
        return clazz;
    }
    
    public void printClasses(PrintStream out)
    {
        out.println("Classes Defined Here:");
        for (String classname : this.definedHereClasses)
        {
            out.println(classname);
        }
        out.println("\n\nClasses Delegated:");
        for (String classname : this.delegatedClasses)
        {
            out.println(classname);
        }
        out.println("\n\nClasses Loaded: " + this.loadedClasses.size());
//        for (String classname : this.loadedClasses)
//        {
//            out.println(classname);
//        }
    }

}
