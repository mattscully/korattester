package com.scully.korat;

public abstract class MethodVerifier
{
    Object preObject;
    Object postObject;
    Object result;
    Object[] values;
    
    /**
     * @return the postObject
     */
    public Object getPostObject()
    {
        return postObject;
    }

    /**
     * @param postObject the postObject to set
     */
    public void setPostObject(Object postObject)
    {
        this.postObject = postObject;
    }

    /**
     * @return the preObject
     */
    public Object getPreObject()
    {
        return preObject;
    }

    /**
     * @param preObject the preObject to set
     */
    public void setPreObject(Object preObject)
    {
        this.preObject = preObject;
    }

    /**
     * @return the result
     */
    public Object getResult()
    {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(Object result)
    {
        this.result = result;
    }

    abstract public boolean postCondition();
    
    /**
     * @return the value
     */
    public Object getValues()
    {
        return values;
    }

    /**
     * @param value the value to set
     */
    public void setValues(Object[] values)
    {
        this.values = values;
    }

}
