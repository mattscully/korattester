/**
 * 
 */
package com.scully.korat;

import junit.framework.TestCase;

import com.scully.korat.map.CandidateFieldDTO;
import com.scully.korat.map.CandidateStateDTO;
import com.scully.korat.map.StateFieldDTO;
import com.scully.korat.map.StateObjectDTO;
import com.scully.korat.map.TestStateSpaceDTO;

/**
 * @author mscully
 * 
 */
public class KoratBaseTest extends TestCase
{

    KoratEngine korat;

    TestStateSpaceDTO stateSpace;

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        stateSpace = createFiniteStateSpace(3);
        KoratEngine.setPruning(false);
        korat = new KoratEngine(stateSpace);
        KoratEngine.setPruning(true);
    }

    public static TestStateSpaceDTO createFiniteStateSpace (
            int NUM_Node,
            int MIN_size,
            int MAX_size,
            int MIN_Node_value,
            int MAX_Node_value) {
        
        TestStateSpaceDTO testStateSpace = new TestStateSpaceDTO();

        // set the root
        testStateSpace.setRootClass("com.scully.korat.test.SearchTree");
        
        // set the repOk method
        testStateSpace.setRepOk("repOk");

        // ==> create StateObjects

        // Node
        StateObjectDTO stateObject = new StateObjectDTO();
        stateObject.setType("com.scully.korat.test.SearchTree$Node");
        stateObject.setQuantity(NUM_Node);
        stateObject.setNullable(true);
        testStateSpace.addStateObject(stateObject);

        // ==> create StateFields

        // SearchTree
        // root
        StateFieldDTO stateField = new StateFieldDTO();
        stateField.setName("root");
        stateField.setParentClass("com.scully.korat.test.SearchTree");
        stateField.setType("com.scully.korat.test.SearchTree$Node");
        testStateSpace.addStateField(stateField);
        // size
        stateField = new StateFieldDTO();
        stateField.setName("size");
        stateField.setParentClass("com.scully.korat.test.SearchTree");
        stateField.setType(int.class.getName());
        stateField.setMin(MIN_size);
        stateField.setMax(MAX_size);
        testStateSpace.addStateField(stateField);
        // bool
        stateField = new StateFieldDTO();
        stateField.setName("bool");
        stateField.setParentClass("com.scully.korat.test.SearchTree");
        stateField.setType(boolean.class.getName());
        stateField.setMin(0);
        stateField.setMax(1);
        testStateSpace.addStateField(stateField);

        // SearchTree$Node
        // left
        stateField = new StateFieldDTO();
        stateField.setName("left");
        stateField.setParentClass("com.scully.korat.test.SearchTree$Node");
        stateField.setType("com.scully.korat.test.SearchTree$Node");
        testStateSpace.addStateField(stateField);
        // right
        stateField = new StateFieldDTO();
        stateField.setName("right");
        stateField.setParentClass("com.scully.korat.test.SearchTree$Node");
        stateField.setType("com.scully.korat.test.SearchTree$Node");
        testStateSpace.addStateField(stateField);
        // value
        stateField = new StateFieldDTO();
        stateField.setName("value");
        stateField.setParentClass("com.scully.korat.test.SearchTree$Node");
        stateField.setType(int.class.getName());
        stateField.setMin(MIN_Node_value);
        stateField.setMax(MAX_Node_value);
        testStateSpace.addStateField(stateField);
        
        // CandidateStates
        // TODO: Complete candidate state
        CandidateStateDTO candidateState = new CandidateStateDTO();
        
        CandidateFieldDTO candidateField = new CandidateFieldDTO();
        candidateField.setFieldId("com.scully.korat.test.SearchTree$Node@5224ee.left");
        candidateField.setFieldName("left");
        candidateField.setFieldType("com.scully.korat.test.SearchTree$Node");
        candidateField.setParentType("com.scully.korat.test.SearchTree$Node");
        candidateField.setValueIndex(0);
        
        candidateField = new CandidateFieldDTO();
        candidateField.setFieldId("com.scully.korat.test.SearchTree$Node@5224ee.left");
        candidateField.setFieldName("left");
        candidateField.setFieldType("com.scully.korat.test.SearchTree$Node");
        candidateField.setParentType("com.scully.korat.test.SearchTree$Node");
        candidateField.setValueIndex(0);
        
        return testStateSpace;
    }

    public static TestStateSpaceDTO createFiniteStateSpace(int scope) {
        // TODO: Verify helper scope
        return createFiniteStateSpace(
            scope, 0, scope, 1, scope);
    }

    public KoratEngine getKorat()
    {
        return korat;
    }

    /**
     * @return the stateSpace
     */
    public TestStateSpaceDTO getStateSpace()
    {
        return stateSpace;
    }

}
