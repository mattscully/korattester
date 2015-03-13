package com.scully.korat.map;

import org.apache.commons.lang.builder.EqualsBuilder;

public class StateFieldDTO
{

    String name;

    String parentClass;

    String type;

    int min;

    int max;
    
    int arraySize;
    
    // for Comparables and Objects
    boolean isNullable;

    public boolean equals(Object obj)
    {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * @return the max
     */
    public int getMax()
    {
        return max;
    }

    /**
     * @param max
     *            the max to set
     */
    public void setMax(int max)
    {
        this.max = max;
    }

    /**
     * @return the min
     */
    public int getMin()
    {
        return min;
    }

    /**
     * @param min
     *            the min to set
     */
    public void setMin(int min)
    {
        this.min = min;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the parentClass
     */
    public String getParentClass()
    {
        return parentClass;
    }

    /**
     * @param parentClass
     *            the parentClass to set
     */
    public void setParentClass(String parentClass)
    {
        this.parentClass = parentClass;
    }

    /**
     * @param parentClass
     *            the parentClass to set
     */
    public void setParentClass(Class parentClass)
    {
        this.parentClass = parentClass.getName();
    }

    /**
     * @return the type
     */
    public String getType()
    {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type)
    {
        this.type = type;
    }
    
    /**
     * @param type
     *            the type to set
     */
    public void setType(Class type)
    {
        this.type = type.getName();
    }

    /**
     * @return the arraySize
     */
    public int getArraySize()
    {
        return arraySize;
    }

    /**
     * @param arraySize the arraySize to set
     */
    public void setArraySize(int arraySize)
    {
        this.arraySize = arraySize;
    }

    /**
     * @return the isNullable
     */
    public boolean isNullable()
    {
        return isNullable;
    }

    /**
     * @param isNullable the isNullable to set
     */
    public void setNullable(boolean isNullable)
    {
        this.isNullable = isNullable;
    }
}
