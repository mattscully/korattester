package com.scully.korat.map;

import org.apache.commons.lang.builder.EqualsBuilder;

public class StateObjectDTO
{
    String type;

    boolean includeNullFlag;

    int quantity;
    
    public boolean equals(Object obj)
    {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * @return the includeNullFlag
     */
    public boolean isIncludeNullFlag()
    {
        return includeNullFlag;
    }

    /**
     * @param includeNullFlag
     *            the includeNullFlag to set
     */
    public void setIncludeNullFlag(boolean includeNullFlag)
    {
        this.includeNullFlag = includeNullFlag;
    }

    /**
     * @return the quantity
     */
    public int getQuantity()
    {
        return quantity;
    }

    /**
     * @param quantity
     *            the quantity to set
     */
    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
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
}
