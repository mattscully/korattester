/*
 * Created on Apr 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.scully.korat.finitization;

/**
 * <p>
 * The domain of all possible values for a given field.
 * </p>
 * 
 * <p>
 * This actually holds an array of all the <code>ClassDomainIndex</code>es
 * which can be assigned to this field.  Typically, the only time you will
 * have multiple domains is when you add NULL to the ObjSet which is a
 * separate <code>ClassDomain</code>.
 * </p>
 * 
 * <p>
 * This class also provides an abstract way of accessing the field domain
 * by an index value that corresponds to the combination of all 
 * <code>ClassDomains</code>.
 * </p>
 * 
 * @author mscully
 */
public class FieldDomain
{
    ClassDomainIndex[] classDomainIndices = null;
    int size = 0;
    
    /**
     * @param classDomainIndices2
     */
    public FieldDomain(ClassDomainIndex[] classDomainIndices)
    {
        this.classDomainIndices = classDomainIndices;
        for (int i = 0; i < this.classDomainIndices.length; i++)
        {
            ClassDomainIndex index = this.classDomainIndices[i];
            this.size += index.domain.objects.length;
        }
    }
    
    /**
     * Get the value in the FieldDomain at the specified index
     * @param i index into the Field Domain
     * @return
     */
    public Object getValueAtIndex(int i)
    {
        int index = 0;
        for (int j = 0; j < this.classDomainIndices.length; j++)
        {
            ClassDomainIndex cdIndex = this.classDomainIndices[j];
            for(int k = 0; k < cdIndex.domain.objects.length; k++, index++)
            {
                if(index == i)
                {
                    return cdIndex.domain.objects[k];
                }
            }
        }
        return null;
    }
    
    /**
     * Get the value in the FieldDomain at the specified index
     * @param i index into the Field Domain
     * @return
     */
    public Object getValueAtIndex(Integer i)
    {
        return getValueAtIndex(i.intValue());
    }
    
    /**
     * Get the ClassDomain the value belongs to at the specified index
     * @param i index into the Field Domain
     * @return
     */
    public ClassDomain getClassDomainAtIndex(int i)
    {
        int index = 0;
        for (int j = 0; j < this.classDomainIndices.length; j++)
        {
            ClassDomainIndex cdIndex = this.classDomainIndices[j];
            for(int k = 0; k < cdIndex.domain.objects.length; k++, index++)
            {
                if(index == i)
                {
                    return cdIndex.domain;
                }
            }
        }
        return null;
    }
    
    /**
     * Get the ClassDomain the value belongs to at the specified index
     * @param i index into the Field Domain
     * @return
     */
    public ClassDomain getClassDomainAtIndex(Integer i)
    {
        return getClassDomainAtIndex(i.intValue());
    }
    
    /**
     * Get the ClassDomainIndex the value belongs to at the specified index
     * @param i index into the Field Domain
     * @return
     */
    public ClassDomainIndex getClassDomainIndexAtIndex(int i)
    {
        int index = 0;
        for (int j = 0; j < this.classDomainIndices.length; j++)
        {
            ClassDomainIndex cdIndex = this.classDomainIndices[j];
            for(int k = 0; k < cdIndex.domain.objects.length; k++, index++)
            {
                if(index == i)
                {
                    return cdIndex;
                }
            }
        }
        return null;
    }
    
    public int getSize()
    {
        return this.size;
    }
    
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o)
    {
        boolean retval = false;
        if(o instanceof FieldDomain)
        {
            FieldDomain fieldDomain = (FieldDomain) o;
            if(this.classDomainIndices.length == fieldDomain.classDomainIndices.length)
            {
                for (int i = 0; i < this.classDomainIndices.length; i++)
                {
                    ClassDomainIndex cdi = this.classDomainIndices[i];
                    ClassDomainIndex cdi2 = fieldDomain.classDomainIndices[i];
                    if(cdi == cdi2)
                    {
                        retval = true;
                    }
                }
            }
        }
        
        return retval;
    }
}
