/*
 * Created on Apr 15, 2005
 *
 */
package com.scully.korat.finitization;

/**
 * <p>
 * </p>
 * @author mscully
 */
public class ObjSet extends FinSet
{
    /**
     * @param object
     * @return true if Set changed, false if not
     */
    public void add(Object object)
    {
        ClassDomain classDomain = new ClassDomain();
        Object[] objects = new Object[] { object };
        classDomain.set(objects);
        ClassDomainIndex classDomainIndex = new ClassDomainIndex(classDomain);
        // insert NULLs at the beginning...not sure if necessary
        if(object == null)
        {
	        this.classDomainIndices.add(0, classDomainIndex);
        }
        else
        {
	        this.classDomainIndices.add(classDomainIndex);
        }
    }
    
}
