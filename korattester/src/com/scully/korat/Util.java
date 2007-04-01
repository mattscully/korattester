package com.scully.korat;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;

import javassist.expr.FieldAccess;

public class Util
{
    
    public static final String KORAT_PREFIX = "$kor_";
    
    public static final String JML_PREFIX = "rac$";
    
    public static final String REP_OK = "repOk";
    
    private static List<Object> supportedNonConcreteTypes;
    
    static
    {
        supportedNonConcreteTypes = new ArrayList<Object>();
        supportedNonConcreteTypes.add("Comparable");
        supportedNonConcreteTypes.add("java.lang.Comparable");
        supportedNonConcreteTypes.add("Object");
        supportedNonConcreteTypes.add("java.lang.Object");
        supportedNonConcreteTypes.add(Comparable.class);
        supportedNonConcreteTypes.add(Object.class);
    }
    
    public static boolean isSkippableField(Field field)
    {
        return field.getName().startsWith(JML_PREFIX) || field.getName().startsWith(KORAT_PREFIX)
                || Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers());
    }
    
    public static boolean isSkippableField(IField field) throws JavaModelException
    {
        return Flags.isStatic(field.getFlags()) || Flags.isTransient(field.getFlags());
    }
    
    public static boolean isSkippableFieldAccess(FieldAccess fieldAccess)
    {
        return fieldAccess.getFieldName().startsWith(KORAT_PREFIX) || isInstrumentedMethod(fieldAccess)
        || fieldAccess.isStatic();
    }
    
    public static boolean isSupportedNonConcreteClass(Class type)
    {
        return supportedNonConcreteTypes.contains(type);
    }
    
    public static boolean isSupportedNonConcreteClass(String type)
    {
        return supportedNonConcreteTypes.contains(type);
    }
    
    private static boolean isInstrumentedMethod(FieldAccess fieldAccess)
    {
        return fieldAccess.where().getMethodInfo().getName().startsWith(KORAT_PREFIX);
    }
}
