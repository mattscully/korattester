/*
 * Created on Apr 17, 2005
 *
 */
package com.scully.korat.finitization;

/**
 * <p>
 * Right now, this class just wrappers ClassDomain.
 * </p>
 * <p>
 * <b>This may not need to be used.</b>
 * </p>
 * 
 * @author mscully
 */
public class ClassDomainIndex
{
    ClassDomain domain;
//    int index = 0; // index into domain.objects array
    
    /**
     * @param classDomain
     */
    public ClassDomainIndex(ClassDomain classDomain)
    {
        this.domain = classDomain;
    }
    
//    public Object get()
//    {
//        return domain.get(this.index);
//    }
}
