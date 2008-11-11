package com.scully.korat.map;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.scully.korat.map.StateFieldDTO;
import com.scully.korat.map.StateObjectDTO;
import com.scully.korat.map.TestStateSpaceDTO;

public class StateSpaceBeanTest
{
    TestStateSpaceDTO testStateSpaceDTO;

    @Before
    public void setUp()
    {
        this.testStateSpaceDTO = new TestStateSpaceDTO();
    }

    @Test
    public void testAddStateObject()
    {
        StateObjectDTO stateObjectDTO = new StateObjectDTO();
        stateObjectDTO.setNullable(true);
        stateObjectDTO.setQuantity(3);
        stateObjectDTO.setType("com.scully.korat.test.SearchTree$Node");
        this.testStateSpaceDTO.addStateObject(stateObjectDTO);
//        StateObjectDTO[] stateObjects = this.testStateSpaceDTO.getStateObjects();
//        assertEquals("StateObjectDTO not properly added to TestStateSpaceDTO", stateObjectDTO, stateObjects[0]);
        List<StateObjectDTO> stateObjects = this.testStateSpaceDTO.getStateObjects();
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
        List<StateFieldDTO> stateFields = this.testStateSpaceDTO.getStateFields();
        assertEquals("StateFieldDTO not properly added to TestStateSpaceDTO", stateField, stateFields.get(0));
        
    }

}
