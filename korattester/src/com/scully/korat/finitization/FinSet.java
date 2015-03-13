/*
 * Created on Apr 23, 2005
 *
 */
package com.scully.korat.finitization;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds a list of all the ClassDomains for a Finitization.
 * 
 * @author mscully
 */
public abstract class FinSet
{
    List<ClassDomainIndex> classDomainIndices = new ArrayList<ClassDomainIndex>();
    
    
    public void addClassDomainIndex(ClassDomainIndex classDomainIndex)
    {
        this.classDomainIndices.add(classDomainIndex);
    }
    
    public ClassDomainIndex[] getClassDomainIndices()
    {
        return this.classDomainIndices.toArray(new ClassDomainIndex[0]);
    }

}
