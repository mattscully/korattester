/*
 * Created on Apr 15, 2005
 *
 */
package com.scully.korat.finitization;

/**
 * @author mscully
 *
 */
public class ShortSet extends FinSet
{

    /**
     * @param publicLong
     * @param publicLong2
     */
    public ShortSet(short min, short max)
    {
        for(short s=min; s<=max; s++)
        {
	        // create a class domain for each primitive value
            ClassDomain classDomain = new ClassDomain();
	        Object[] objects = new Object[] { new Short(s) };
	        classDomain.set(objects);
	        ClassDomainIndex classDomainIndex = new ClassDomainIndex(classDomain);
	        this.classDomainIndices.add(classDomainIndex);
        }
    }
}
