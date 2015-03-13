package com.scully.korat.map;

public class CandidateFieldDTO
{
    String fieldId;

    String fieldName;

    String fieldType;

    String parentType;

    int valueIndex;

    //    String value;

    public CandidateFieldDTO()
    {
    }

    // TODO: excludes fieldID right now
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (!(obj instanceof CandidateFieldDTO))
            return false;

        CandidateFieldDTO field = (CandidateFieldDTO) obj;
        return this.fieldName.equals(field.fieldName) && this.fieldType.equals(field.fieldType)
                && this.parentType.equals(field.parentType) && this.valueIndex == field.valueIndex;
    }

    /**
     * @return the fieldId
     */
    public String getFieldId()
    {
        return fieldId;
    }

    /**
     * @param fieldId
     *            the fieldId to set
     */
    public void setFieldId(String fieldId)
    {
        this.fieldId = fieldId;
    }

    /**
     * @return the fieldName
     */
    public String getFieldName()
    {
        return fieldName;
    }

    /**
     * @param fieldName
     *            the fieldName to set
     */
    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName;
    }

    /**
     * @return the fieldType
     */
    // TODO: Is getFieldType() never used???
    public String getFieldType()
    {
        return fieldType;
    }

    /**
     * @param fieldType
     *            the fieldType to set
     */
    public void setFieldType(String fieldType)
    {
        this.fieldType = fieldType;
    }

    /**
     * @return the valueIndex
     */
    public int getValueIndex()
    {
        return valueIndex;
    }

    /**
     * @param valueIndex
     *            the valueIndex to set
     */
    public void setValueIndex(int valueIndex)
    {
        this.valueIndex = valueIndex;
    }

    /**
     * @param valueIndex
     *            the valueIndex to set
     */
    public void setValueIndex(Integer valueIndex)
    {
        this.valueIndex = valueIndex.intValue();
    }

    //    /**
    //     * @return the value
    //     */
    //    public String getValue()
    //    {
    //        return value;
    //    }

    //    /**
    //     * @param value the value to set
    //     */
    //    public void setValue(String value)
    //    {
    //        this.value = value;
    //    }

    /**
     * @return the parentType
     */
    public String getParentType()
    {
        return parentType;
    }

    /**
     * @param parentType the parentType to set
     */
    public void setParentType(String parentType)
    {
        this.parentType = parentType;
    }

}
