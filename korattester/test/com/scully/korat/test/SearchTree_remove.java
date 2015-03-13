package com.scully.korat.test;


import com.scully.korat.Util;
import com.scully.korat.map.StateSpaceBuilder;
import com.scully.korat.map.TestStateSpaceDTO;

public class SearchTree_remove {
    
//	public static Finitization finSearchTree (
//			int NUM_Node,
//			int MIN_size,
//			int MAX_size,
//			int MIN_Node_value,
//			int MAX_Node_value) {
//	    Finitization f = new Finitization(SearchTree.class);
//	    ObjSet nodes = f.createObjects("com.scully.korat.test.SearchTree$Node", NUM_Node);
//		nodes.add(null);
//		try
//        {
//			f.set(SearchTree.class.getDeclaredField("root"), nodes);
//			f.set(SearchTree.class.getDeclaredField("size"), new IntSet(MIN_size, MAX_size));
//			f.set(SearchTree.Node.class.getDeclaredField("right"), nodes);
//			f.set(SearchTree.Node.class.getDeclaredField("left"), nodes);
//            f.set(SearchTree.Node.class.getDeclaredField("value"), new IntSet(MIN_Node_value, MAX_Node_value));
//        }
//        catch (SecurityException e)
//        {
//            e.printStackTrace();
//        }
//        catch (NoSuchFieldException e)
//        {
//            e.printStackTrace();
//        }
//		return f;
//	}
    
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

        // bool
        stateSpace.addStateField("bool", boolean.class.getName(), "com.scully.korat.test.SearchTree", 0, 1);

        // SearchTree$Node
        // left
        stateSpace.addStateField("left", "com.scully.korat.test.SearchTree$Node", "com.scully.korat.test.SearchTree$Node");
        
        // right
        stateSpace.addStateField("right", "com.scully.korat.test.SearchTree$Node", "com.scully.korat.test.SearchTree$Node");
        
        // value
        stateSpace.addStateField("value", int.class.getName(), "com.scully.korat.test.SearchTree$Node", MIN_Node_value, MAX_Node_value);
        
        // add repOk method name.
        stateSpace.setRepOk(Util.REP_OK);
        
        return stateSpace.getStateSpace();
    }

	public static TestStateSpaceDTO createFiniteStateSpace(int scope) {
	    // TO DO: Verify helper scope
		return createFiniteStateSpace(
			scope, 0, scope, 1, scope);
	}
	
//	public static void main(String[] args) {
//	    // TO DO: Define Finitization Scope
//        TestStateSpaceDTO testStateSpace = createFiniteStateSpace(3);
////	    Finitization f = finSearchTree(3, 0, 3, 1, 3);
//	    
//	    // remove method signature
//	    String testMethod = "remove";
//	    Class[] paramTypes = new Class[] { int.class };
//	    // TO DO: Define test parameters for inputs
//	    Object[] paramValues = new Object[] { new Integer(1) };
//		
//	    // TO DO: Choose Korat feature execution
//	    KoratClient.setIsoMorphismBreaking(true);
//	    KoratClient.populateTestCandidates(testStateSpace);
//        System.out.println(BeanXmlMapper.beanToXml(testStateSpace));
//	    KoratClient.setPruning(false);
//        KoratClient.testMethod(testStateSpace, testMethod, paramTypes, paramValues);
////	    KoratClient.getFiniteStateSpace(stateSpace, testMethod, paramTypes, paramValues);
////		String serializePath = "c:/ut/korat-cvs/scope-4-noniso";
////	    KoratClient.serializeInputs(f, serializePath);
////		KoratClient.testSerializedObjects(f, testMethod, paramTypes, paramValues, serializePath);
//	}
    
}
