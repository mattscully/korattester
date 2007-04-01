/*
 * Created on Apr 17, 2005
 *
 */
package com.scully.korat.finitization;

import java.lang.reflect.Field;

import com.scully.korat.Util;

/**
 * @author mscully
 *
 */
public class ObjField
{
    // field of an object from some domain
    Object object;

    Field field;

    // TODO: leave as transient?
    transient Field observableIndexField;

    /**
     * @param fieldName
     */
    public ObjField(Object object, Field field)
    {
        this.object = object;
        this.field = field;
    }

    public void set(Object o)
    {
        try
        {
            this.field.set(this.object, o);
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Set the value of the index field which corresponds to this field.
     * This is for observing executions
     * @param idx
     */
    public void setObservableIndexFieldValue(int idx)
    {
        // TODO: throw exception here?  If this happens, we've got trouble
        try
        {
            if (this.observableIndexField == null)
            {
                this.observableIndexField = this.object.getClass().getDeclaredField(Util.KORAT_PREFIX + this.field.getName());
                this.observableIndexField.setAccessible(true);
            }
            this.observableIndexField.setInt(this.object, idx);
        }
        catch (SecurityException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (NoSuchFieldException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean equals(Object o)
    {
        if (o instanceof ObjField)
        {
            ObjField objField = (ObjField) o;
            return (this.field.equals(objField.field) && this.object == objField.object);
        }
        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        int result = 17;
        result = 37 * result + this.field.hashCode();
        result = 37 * result + this.object.hashCode();
        return result;
    }

    public Object getObject()
    {
        return this.object;
    }

    /**
     * @return
     */
    public Field getField()
    {
        return this.field;
    }

    public Object getFieldValue()
    {
        Object o = null;
        try
        {
            o = field.get(this.object);
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return o;
    }

    public String toString()
    {
        return this.object.toString() + ":" + field.getName();
    }

}
