package com.scully.korat.test;


import com.scully.korat.KoratClient;
import com.scully.korat.finitization.*;
import com.scully.korat.map.BeanXmlMapper;
import com.scully.korat.map.StateFieldDTO;
import com.scully.korat.map.StateObjectDTO;
import com.scully.korat.map.StateSpaceBuilder;
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
        
        StateSpaceBuilder stateSpace = new StateSpaceBuilder();

        // set the root
        stateSpace.setRootClass("com.scully.korat.test.SearchTree");

        // ==> create StateObjects

        // Node
        stateSpace.addStateObject("com.scully.korat.test.SearchTree$Node", NUM_Node, true);

        // ==> create StateFields

        // SearchTree
        // root
        stateSpace.addStateField("root", "com.scully.korat.test.SearchTree$Node", "com.scully.korat.test.SearchTree");
        
        // size
        stateSpace.addStateField("size", int.class.getName(), "com.scully.korat.test.SearchTree", MIN_size, MAX_size);

        // SearchTree$Node
        // left
        stateSpace.addStateField("left", "com.scully.korat.test.SearchTree$Node", "com.scully.korat.test.SearchTree$Node");
        
        // right
        stateSpace.addStateField("right", "com.scully.korat.test.SearchTree$Node", "com.scully.korat.test.SearchTree$Node");
        
        // value
        stateSpace.addStateField("value", int.class.getName(), "com.scully.korat.test.SearchTree$Node", MIN_Node_value, MAX_Node_value);
        
        return stateSpace.getStateSpace();
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
