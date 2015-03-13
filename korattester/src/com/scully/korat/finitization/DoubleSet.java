/*
 * Created on Apr 15, 2005
 *
 */
package com.scully.korat.finitization;

/**
 * @author mscully
 *
 */
public class DoubleSet extends FinSet
{
    /**
     * @param publicLong
     * @param publicLong2
     */
    public DoubleSet(double min, double max)
    {
        for(double d=min; d<=max; d++)
        {
	        // create a class domain for each primitive value
            ClassDomain classDomain = new ClassDomain();
	        Object[] objects = new Object[] { new Double(d) };
	        classDomain.set(objects);
	        ClassDomainIndex classDomainIndex = new ClassDomainIndex(classDomain);
	        this.classDomainIndices.add(classDomainIndex);
        }
    }
}
