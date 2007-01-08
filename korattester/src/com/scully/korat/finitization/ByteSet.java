/*
 * Created on Apr 15, 2005
 *
 */
package com.scully.korat.finitization;

/**
 * @author mscully
 *
 */
public class ByteSet extends FinSet
{
    /**
     * @param publicLong
     * @param publicLong2
     */
    public ByteSet(byte min, byte max)
    {
        for(byte b=min; b<=max; b++)
        {
	        // create a class domain for each primitive value
            ClassDomain classDomain = new ClassDomain();
	        Object[] objects = new Object[] { new Byte(b) };
	        classDomain.set(objects);
	        ClassDomainIndex classDomainIndex = new ClassDomainIndex(classDomain);
	        this.classDomainIndices.add(classDomainIndex);
        }
    }
}
