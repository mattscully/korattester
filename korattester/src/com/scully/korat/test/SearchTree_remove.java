package com.scully.korat.test;


import com.scully.korat.KoratClient;
import com.scully.korat.finitization.*;
import com.scully.korat.map.BeanXmlMapper;
import com.scully.korat.map.StateFieldDTO;
import com.scully.korat.map.StateObjectDTO;
import com.scully.korat.map.TestStateSpaceDTO;

public class SearchTree_remove {
    
	public static Finitization finSearchTree (
			int NUM_Node,
			int MIN_size,
			int MAX_size,
			int MIN_Node_value,
			int MAX_Node_value) {
		// TODO: Verify Finitization Skeleton
	    Finitization f = new Finitization(SearchTree.class);
		// TODO: Update createObjects method with fully qualified name
	    ObjSet nodes = f.createObjects("com.scully.korat.test.SearchTree$Node", NUM_Node);
		nodes.add(null);
		try
        {
			f.set(SearchTree.class.getDeclaredField("root"), nodes);
			f.set(SearchTree.class.getDeclaredField("size"), new IntSet(MIN_size, MAX_size));
			f.set(SearchTree.Node.class.getDeclaredField("right"), nodes);
			f.set(SearchTree.Node.class.getDeclaredField("left"), nodes);
            f.set(SearchTree.Node.class.getDeclaredField("value"), new IntSet(MIN_Node_value, MAX_Node_value));
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
		return f;
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

        // ==> create StateObjects

        // Node
        StateObjectDTO stateObject = new StateObjectDTO();
        stateObject.setType("com.scully.korat.test.SearchTree$Node");
        stateObject.setQuantity(NUM_Node);
        stateObject.setIncludeNullFlag(true);
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
        
        return testStateSpace;
    }

	public static TestStateSpaceDTO createFiniteStateSpace(int scope) {
	    // TODO: Verify helper scope
		return createFiniteStateSpace(
			scope, 0, scope, 1, scope);
	}
	
	public static void main(String[] args) {
	    // TODO: Define Finitization Scope
        TestStateSpaceDTO testStateSpace = createFiniteStateSpace(3);
//	    Finitization f = finSearchTree(3, 0, 3, 1, 3);
	    
	    // remove method signature
	    String testMethod = "remove";
	    Class[] paramTypes = new Class[] { int.class };
	    // TODO: Define test parameters for inputs
	    Object[] paramValues = new Object[] { new Integer(1) };
		
	    // TODO: Choose Korat feature execution
	    KoratClient.setIsoMorphismBreaking(true);
	    KoratClient.populateTestCandidates(testStateSpace);
        System.out.println(BeanXmlMapper.beanToXml(testStateSpace));
	    KoratClient.setPruning(false);
        KoratClient.testMethod(testStateSpace, testMethod, paramTypes, paramValues);
//	    KoratClient.getFiniteStateSpace(stateSpace, testMethod, paramTypes, paramValues);
//		String serializePath = "c:/ut/korat-cvs/scope-4-noniso";
//	    KoratClient.serializeInputs(f, serializePath);
//		KoratClient.testSerializedObjects(f, testMethod, paramTypes, paramValues, serializePath);
	}
    
}
