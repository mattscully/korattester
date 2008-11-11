package com.scully.korat;

import java.util.Stack;

import com.scully.korat.finitization.ObjField;


public class KoratObserver implements IKoratObserver
{
    Stack<ObjField> observedFields;

    ObjField[] objFields;

    public KoratObserver()
    {
    }

    public void notify(int idx)
    {
        // TODO: remove first IF statement when instrumentation done
        // this is just to handle case when testing the methods and this
        // class doesn't get initialized (w/ instrumentation, it won't
        // even be in the test subject's class definition).
        if (this.objFields != null)
        {
            if (!this.observedFields.contains(this.objFields[idx]))
            {
                this.observedFields.push(this.objFields[idx]);
            }

        }
    }

    /**
     * @return the objFields
     */
    public ObjField[] getObjFields()
    {
        return objFields;
    }

    /**
     * @param objFields the objFields to set
     */
    public void setObjFields(ObjField[] objFields)
    {
        this.objFields = objFields;
    }

    /**
     * @return the observedFields
     */
    public Stack<ObjField> getObservedFields()
    {
        return observedFields;
    }

    /**
     * @param observedFields the observedFields to set
     */
    public void setObservedFields(Stack<ObjField> observedFields)
    {
        this.observedFields = observedFields;
    }

}
