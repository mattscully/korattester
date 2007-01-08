/*
 * Created on Apr 15, 2005
 *
 */
package com.scully.korat.finitization;

/**
 * @author mscully
 *
 */
public class IntSet extends FinSet
{
    
    /**
     * @param publicLong
     * @param publicLong2
     */
    public IntSet(int min, int max)
    {
        for(int i=min; i<=max; i++)
        {
	        // create a class domain for each primitive value
            ClassDomain classDomain = new ClassDomain();
	        Object[] objects = new Object[] { new Integer(i) };
	        classDomain.set(objects);
	        ClassDomainIndex classDomainIndex = new ClassDomainIndex(classDomain);
	        this.classDomainIndices.add(classDomainIndex);
        }
    }
    
}
