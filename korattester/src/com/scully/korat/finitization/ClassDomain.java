/*
 * Created on Apr 17, 2005
 *
 */
package com.scully.korat.finitization;

/**
 * <p>
 * A collection of all the Objects for a particular class type.
 * </p>
 * 
 * <p>
 * For example, if we have 3 Node objects in our state space, this
 * class will have an array of those 3 Node objects.
 * </p>
 * 
 * @author mscully
 */
public class ClassDomain
{

    // ordered class domain
    public Object[] objects;
    
    public ClassDomain() {}
    
    /*
    public int getSize()
    {
        return objects.length;
    }
    */
    
    public Object get(int i)
    {
        return objects[i];
    }
    
    public void set(Object[] objects)
    {
        this.objects = objects;
    }
}
