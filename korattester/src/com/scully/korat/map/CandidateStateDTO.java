package com.scully.korat.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;

import com.scully.korat.CandidateState;
import com.scully.korat.finitization.ObjField;


public class CandidateStateDTO
{
    List candidateFields;

    public CandidateStateDTO()
    {
        this.candidateFields = new ArrayList();
    }

    /**
     * 
     * @param candidates
     *            List of Maps{ObjField, Integer}
     */
    public CandidateStateDTO(Map candidateMap)
    {
        this();
        
        for (Iterator fieldIter = candidateMap.keySet().iterator(); fieldIter.hasNext();)
        {
            ObjField objField = (ObjField) fieldIter.next();
            CandidateFieldDTO candidateField = new CandidateFieldDTO();
            candidateField.setFieldName(objField.getField().getName());
            candidateField.setFieldId(ObjectUtils.identityToString(objField.getObject()) + "."
                    + candidateField.getFieldName());
            candidateField.setFieldType(objField.getField().getType().getName());
            candidateField.setParentType(objField.getObject().getClass().getName());
            candidateField.setValueIndex((Integer) candidateMap.get(objField));
//            candidateField.setValue(ObjectUtils.toString(objField.getFieldValue()));
            this.candidateFields.add(candidateField);
        }
    }

    /**
     * 
     * @param candidates
     *            List of Maps{ObjField, Integer}
     */
    public CandidateStateDTO(CandidateState candidate)
    {
        this(candidate.getStateValueIndexes());
    }
    
    public boolean equals(Object obj)
    {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    public String toString()
    {
        StringBuffer buf = new StringBuffer(100);
        for (Iterator iter = this.candidateFields.iterator(); iter.hasNext();)
        {
            CandidateFieldDTO field = (CandidateFieldDTO) iter.next();
            buf.append(ClassUtils.getShortClassName(field.getParentType()) + "." + field.getFieldName() + "["
                    + field.getValueIndex() + "] | ");
//                    + field.getValueIndex() + "](" + field.getValue() + ") | ");
        }
        return buf.toString();
    }

    /**
     * @return the candidateFields
     */
    public List getCandidateFields()
    {
        return this.candidateFields;
    }

    /**
     * @param candidateFields
     *            the candidateFields to set
     */
    public void setCandidateFields(List candidateFields)
    {
        this.candidateFields = candidateFields;
    }

    public void addCandidateField(CandidateFieldDTO candidateFieldDTO)
    {
        this.candidateFields.add(candidateFieldDTO);
    }
}
