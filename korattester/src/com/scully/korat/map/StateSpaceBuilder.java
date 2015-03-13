package com.scully.korat.map;

public class StateSpaceBuilder
{
    private TestStateSpaceDTO stateSpace;

    public StateSpaceBuilder()
    {
        this.stateSpace = new TestStateSpaceDTO();
    }

    public TestStateSpaceDTO getStateSpace()
    {
        return this.stateSpace;
    }

    public void setRootClass(String rootClass)
    {
        this.stateSpace.setRootClass(rootClass);
    }

    public void setRepOk(String repOk)
    {
        this.stateSpace.setRepOk(repOk);
    }

    /*
     * Add State Objects
     */
    public void addStateObject(String type, int quantity, boolean includeNullFlag)
    {
        StateObjectDTO stateObject = new StateObjectDTO();
        stateObject.setType(type);
        stateObject.setQuantity(quantity);
        stateObject.setNullable(includeNullFlag);
        this.stateSpace.addStateObject(stateObject);
    }

    /*
     * Add State Fields
     */
    public void addStateField(String name, String type, String parent)
    {
        addStateField(name, type, parent, 0, 0);
    }

    public void addStateField(String name, String type, String parent, int min, int max)
    {
        StateFieldDTO stateField = new StateFieldDTO();
        stateField.setName(name);
        stateField.setType(type);
        stateField.setParentClass(parent);
        stateField.setMin(min);
        stateField.setMax(max);
        this.stateSpace.addStateField(stateField);
    }

    public void addStateField(StateFieldDTO stateField)
    {
        if (stateField != null)
        {
            this.stateSpace.addStateField(stateField);
        }
    }
}
