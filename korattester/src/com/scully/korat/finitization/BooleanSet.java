package com.scully.korat.finitization;

public class BooleanSet extends FinSet
{
    public BooleanSet(int min, int max)
    {
        for(int i=min; i<=max; i++)
        {
            // create a class domain for each primitive value
            ClassDomain classDomain = new ClassDomain();
            // 0 == false, 1 == true (really non-zero == true)
            Object[] objects = new Object[] { i == 0 ? false : true };
            classDomain.set(objects);
            ClassDomainIndex classDomainIndex = new ClassDomainIndex(classDomain);
            this.classDomainIndices.add(classDomainIndex);
        }
    }
}
