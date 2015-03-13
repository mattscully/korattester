/**
 * 
 */
package com.scully.korat.map;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * @author mscully
 * 
 */
public class TestStateSpaceDTO
{
    String rootClass;
    
    String repOk;

    List<StateObjectDTO> stateObjects;

    List<StateFieldDTO> stateFields;

    List<CandidateStateDTO> candidateStates;

    public TestStateSpaceDTO()
    {
        this.stateObjects = new ArrayList<StateObjectDTO>();
        this.stateFields = new ArrayList<StateFieldDTO>();
        this.candidateStates = new ArrayList<CandidateStateDTO>();
    }

    public void addStateObject(StateObjectDTO stateObjectDTO)
    {
        this.stateObjects.add(stateObjectDTO);
    }

    public void addStateField(StateFieldDTO stateField)
    {
        this.stateFields.add(stateField);
    }

    public boolean equals(Object obj)
    {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * @return the rootClass
     */
    public String getRootClass()
    {
        return rootClass;
    }

    /**
     * @param rootClass
     *            the rootClass to set
     */
    public void setRootClass(String rootClass)
    {
        this.rootClass = rootClass;
    }

    /**
     * @param rootClass
     *            the rootClass to set
     */
    public void setRootClass(Class rootClass)
    {
        this.rootClass = rootClass.getName();
    }

    /**
     * @return the candidateStates
     */
    public List<CandidateStateDTO> getCandidateStates()
    {
        return candidateStates;
    }

    /**
     * @param candidateStates the candidateStates to set
     */
    public void setCandidateStates(List<CandidateStateDTO> candidateStates)
    {
        this.candidateStates = candidateStates;
    }

    /**
     * @return the stateFields
     */
    public List<StateFieldDTO> getStateFields()
    {
        return stateFields;
    }

    /**
     * @param stateFields the stateFields to set
     */
    public void setStateFields(List<StateFieldDTO> stateFields)
    {
        this.stateFields = stateFields;
    }

    /**
     * @return the stateObjects
     */
    public List<StateObjectDTO> getStateObjects()
    {
        return stateObjects;
    }

    /**
     * @param stateObjects the stateObjects to set
     */
    public void setStateObjects(List<StateObjectDTO> stateObjects)
    {
        this.stateObjects = stateObjects;
    }

    public void addCandidateState(CandidateStateDTO candidateStateDTO)
    {
        this.candidateStates.add(candidateStateDTO);
    }

    /**
     * @return the repOk
     */
    public String getRepOk()
    {
        return repOk;
    }

    /**
     * @param repOk the repOk to set
     */
    public void setRepOk(String repOk)
    {
        this.repOk = repOk;
    }
}
