package com.scully.korat.test;

public abstract class Predicate<T>
{
    T preObject;
    T postObject;
    Object result;
    Object[] parameters;
    
    /**
     * @return the postObject
     */
    public T getPostObject()
    {
        return postObject;
    }

    /**
     * @param postObject the postObject to set
     */
    public void setPostObject(T postObject)
    {
        this.postObject = postObject;
    }

    /**
     * @return the preObject
     */
    public T getPreObject()
    {
        return preObject;
    }

    /**
     * @param preObject the preObject to set
     */
    public void setPreObject(T preObject)
    {
        this.preObject = preObject;
    }

    /**
     * @return the result as an Object
     */
    public Object getResult()
    {
        return result;
    }
    
    /**
     * @return the result as an Object
     */
    public boolean getBooleanResult()
    {
        return ((Boolean) result).booleanValue();
    }
    
    /**
     * @return the result as a short
     */
    public short getShortResult()
    {
        return ((Short) result).shortValue();
    }

    /**
     * @return the result as an int
     */
    public int getIntResult()
    {
        return ((Integer) result).intValue();
    }

    /**
     * @return the result as a long
     */
    public long getLongResult()
    {
        return ((Long) result).longValue();
    }

    /**
     * @return the result as a double
     */
    public double getDoubleResult()
    {
        return ((Double) result).doubleValue();
    }

    /**
     * @return the result as a float
     */
    public float getFloatResult()
    {
        return ((Float) result).floatValue();
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
    public Object[] getParameters()
    {
        return parameters;
    }
    
    public Object getObjectParam(int index)
    {
        return this.parameters[index];
    }
    
    public byte getByteParam(int index)
    {
        return ((Byte) this.parameters[index]).byteValue();
    }
    
    public short getShortParam(int index)
    {
        return ((Short) this.parameters[index]).shortValue();
    }

    public int getIntParam(int index)
    {
        return ((Integer) this.parameters[index]).intValue();
    }

    public long getLongParam(int index)
    {
        return ((Long) this.parameters[index]).longValue();
    }

    public double getDoubleParam(int index)
    {
        return ((Double) this.parameters[index]).doubleValue();
    }

    public float getFloatParam(int index)
    {
        return ((Float) this.parameters[index]).floatValue();
    }

    /**
     * @param value the value to set
     */
    public void setParameters(Object[] parameters)
    {
        this.parameters = parameters;
    }

}
