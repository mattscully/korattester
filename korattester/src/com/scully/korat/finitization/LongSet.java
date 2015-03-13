/*
 * Created on Apr 15, 2005
 *
 */
package com.scully.korat.finitization;


/**
 * @author mscully
 *
 */
public class LongSet extends FinSet
{

    /**
     * @param publicLong
     * @param publicLong2
     */
    public LongSet(long min, long max)
    {
        for(long l=min; l<=max; l++)
        {
	        // create a class domain for each primitive value
            ClassDomain classDomain = new ClassDomain();
	        Object[] objects = new Object[] { new Long(l) };
	        classDomain.set(objects);
	        ClassDomainIndex classDomainIndex = new ClassDomainIndex(classDomain);
	        this.classDomainIndices.add(classDomainIndex);
        }
    }
}
