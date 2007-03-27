package com.scully.korat.map;

import java.util.List;

import com.scully.korat.map.StateFieldDTO;
import com.scully.korat.map.StateObjectDTO;
import com.scully.korat.map.TestStateSpaceDTO;

import junit.framework.TestCase;

public class StateSpaceBeanTest extends TestCase
{
    TestStateSpaceDTO testStateSpaceDTO;

    protected void setUp() throws Exception
    {
        super.setUp();
        this.testStateSpaceDTO = new TestStateSpaceDTO();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testAddStateObject()
    {
        StateObjectDTO stateObjectDTO = new StateObjectDTO();
        stateObjectDTO.setIncludeNullFlag(true);
        stateObjectDTO.setQuantity(3);
        stateObjectDTO.setType("com.scully.korat.test.SearchTree$Node");
        this.testStateSpaceDTO.addStateObject(stateObjectDTO);
//        StateObjectDTO[] stateObjects = this.testStateSpaceDTO.getStateObjects();
//        assertEquals("StateObjectDTO not properly added to TestStateSpaceDTO", stateObjectDTO, stateObjects[0]);
        List stateObjects = this.testStateSpaceDTO.getStateObjects();
        assertEquals("StateObjectDTO not properly added to TestStateSpaceDTO", stateObjectDTO, stateObjects.get(0));
    }

    public void testAddStateField()
    {
        StateFieldDTO stateField = new StateFieldDTO();
        stateField.setParentClass("com.scully.korat.test.SearchTree$Node");
        stateField.setType(int.class.getName());
        stateField.setName("value");
        stateField.setMin(1);
        stateField.setMax(3);
        this.testStateSpaceDTO.addStateField(stateField);
//        StateFieldDTO[] stateFields = this.testStateSpaceDTO.getStateFields();
//        assertEquals("StateFieldDTO not properly added to TestStateSpaceDTO", stateField, stateFields[0]);
        List stateFields = this.testStateSpaceDTO.getStateFields();
        assertEquals("StateFieldDTO not properly added to TestStateSpaceDTO", stateField, stateFields.get(0));
        
    }

}
