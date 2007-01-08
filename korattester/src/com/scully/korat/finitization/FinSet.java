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
    List classDomainIndices = new ArrayList();
    
    
    public void addClassDomainIndex(ClassDomainIndex classDomainIndex)
    {
        this.classDomainIndices.add(classDomainIndex);
    }
    
    public ClassDomainIndex[] getClassDomainIndices()
    {
        return (ClassDomainIndex[]) this.classDomainIndices.toArray(new ClassDomainIndex[0]);
    }

}
