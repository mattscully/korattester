/**
 * 
 */
package com.scully.korat;

import java.util.LinkedHashMap;
import java.util.Map;

import com.scully.korat.finitization.ClassDomain;
import com.scully.korat.finitization.FieldDomain;
import com.scully.korat.finitization.ObjField;


/**
 * This class represents a single state given a state space and fields.
 * 
 * @author scully
 * 
 */
public class CandidateState
{
    // {objField, fieldDomain}
    Map stateSpace;

    // {objField, valueIndex}
    Map stateValueIndexes;

    // all the fields
    ObjField[] stateFields;

    public static Integer ZERO = new Integer(0);

    public CandidateState(Map stateSpace, ObjField[] stateFields)
    {
        this.stateSpace = stateSpace;
        this.stateFields = stateFields;
        initStateValueIndexes();
    }

    /**
     * Initialize all fields to have the value at their ZERO field domain index
     */
    void initStateValueIndexes()
    {
        this.stateValueIndexes = new LinkedHashMap();

        // initialize state to a zero index of all field values
        for (int i = 0; i < stateFields.length; i++)
        {
            ObjField f = stateFields[i];
            this.setValueAtIndex(f, ZERO);
        }
    }

    public Map getStateValueIndexes()
    {
        return stateValueIndexes;
    }

    /**
     * Get the ClassDomain the specified field is a member of.
     * 
     * @param field
     * @return
     */
    public ClassDomain getClassDomainForField(ObjField field)
    {
        FieldDomain fieldDomain = (FieldDomain) this.stateSpace.get(field);
        return fieldDomain.getClassDomainAtIndex((Integer) this.stateValueIndexes.get(field));
    }

    public boolean hasEqualDomainsForFields(ObjField field1, ObjField field2)
    {
        FieldDomain fieldDomain1 = (FieldDomain) this.stateSpace.get(field1);
        ClassDomain classDomain1 = fieldDomain1.getClassDomainAtIndex((Integer) this.stateValueIndexes.get(field1));
        FieldDomain fieldDomain2 = (FieldDomain) this.stateSpace.get(field2);
        ClassDomain classDomain2 = fieldDomain2.getClassDomainAtIndex((Integer) this.stateValueIndexes.get(field2));

        return fieldDomain1.equals(fieldDomain2) && classDomain1.equals(classDomain2);
    }

    public int getValueIndex(ObjField field)
    {
        return ((Integer) this.stateValueIndexes.get(field)).intValue();
    }

    public void setValueAtIndex(ObjField field, int index)
    {
        setValueAtIndex(field, new Integer(index));
    }

    public void setValueAtIndex(ObjField field, Integer index)
    {
        this.stateValueIndexes.put(field, index);
        FieldDomain fieldDomain = (FieldDomain) this.stateSpace.get(field);
        field.set(fieldDomain.getValueAtIndex(index));
    }

    public boolean hasMoreValuesInFieldDomain(ObjField field)
    {
        int stateValueIndex = ((Integer) this.stateValueIndexes.get(field)).intValue();
        int maxStateValueIndex = ((FieldDomain) this.stateSpace.get(field)).getSize() - 1;
        return stateValueIndex < maxStateValueIndex;
    }

    public boolean hasMoreValuesInClassDomain(ObjField field)
    {
        // includes logic from hasMoreValues(), didn't want to repeat so many
        // casts, etc...
        FieldDomain fieldDomain = (FieldDomain) this.stateSpace.get(field);
        int stateValueIndex = ((Integer) this.stateValueIndexes.get(field)).intValue();
        int maxStateValueIndex = fieldDomain.getSize() - 1;
        int fieldIndex = getValueIndex(field);

        // there are more left in field's field domain and the
        // next value is still in the same class domain
        return stateValueIndex < maxStateValueIndex
                && fieldDomain.getClassDomainAtIndex(fieldIndex).equals(
                        fieldDomain.getClassDomainAtIndex(fieldIndex + 1));
    }

//    /**
//     * @param stateValueIndexes
//     * @return
//     */
//    public Map copyOfValueIndexes()
//    {
//        // copies Map structure without the keys and values
//        // a "shallow" clone
//        Map stateCopy = new LinkedHashMap();
//        Iterator keys = this.stateValueIndexes.keySet().iterator();
//        while (keys.hasNext())
//        {
//            ObjField objField = (ObjField) keys.next();
//            ObjField objFieldCopy = new ObjField(objField.getObject(), objField.getField());
//            stateCopy.put(objFieldCopy, stateValueIndexes.get(objField));
//        }
//        return stateCopy;
//    }

    public String toString()
    {
        StringBuffer buf = new StringBuffer(64);
        for (int i = 0; i < this.stateFields.length; i++)
        {
            ObjField objField = this.stateFields[i];
            buf.append(objField).append("=").append(this.stateValueIndexes.get(objField)).append(", ");
        }
        return buf.toString();
    }

    /**
     * @return the stateFields
     */
    public ObjField[] getStateFields()
    {
        return stateFields;
    }

    /**
     * Set the instrumented observable index fields to their
     * corresponding index into the stateFields array.
     *
     */
    public void setObservableIndexes()
    {
        for (int i = 0; i < this.stateFields.length; i++)
        {
            this.stateFields[i].setObservableIndexFieldValue(i);
        }
    }
}
