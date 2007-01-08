package com.scully.korat.finitization;

import com.scully.korat.KoratEngine;

public class TestClassFin {

	public static Finitization finTestClass (
			int MIN_myPrivateInt,
			int MAX_myPrivateInt,
			long MIN_myPrivateLong,
			long MAX_myPrivateLong,
			int NUM_Object,
			int MIN_myPublicInt,
			int MAX_myPublicInt,
			double MIN_myPublicDouble,
			double MAX_myPublicDouble,
			int MIN_myInt,
			int MAX_myInt,
			long MIN_myLong,
			long MAX_myLong,
			int NUM_Node,
			int MIN_Node_value,
			int MAX_Node_value,
			int NUM_InnerNode,
			int MIN_Node_InnerNode_innerInt,
			int MAX_Node_InnerNode_innerInt) {
		Finitization f = new Finitization(TestClass.class);
		ObjSet nodes = f.createObjects("com.scully.korat.finitization.TestClass$Node", NUM_Node);
		nodes.add(null);
		ObjSet innerNodes = f.createObjects("com.scully.korat.finitization.TestClass$Node$InnerNode", NUM_InnerNode);
		innerNodes.add(null);
		ObjSet objects = f.createObjects("java.lang.Object", NUM_Object);
		objects.add(null);
//		f.set("InnerNode.sideways", nodes);
//		f.set("Node.right", nodes);
//		f.set("InnerNode.down", innerNodes);
//		f.set("myPrivateObject", objects);
//		f.set("InnerNode.left", nodes);
//		f.set("InnerNode.up", innerNodes);
//		f.set("Node.left", nodes);
//		f.set("myPrivateLong", new LongSet(MIN_myPrivateLong, MAX_myPrivateLong));
//		f.set("Node.value", new IntSet(MIN_Node_value, MAX_Node_value));
//		f.set("myLong", new LongSet(MIN_myLong, MAX_myLong));
//		f.set("myInt", new IntSet(MIN_myInt, MAX_myInt));
//		f.set("myPrivateInt", new IntSet(MIN_myPrivateInt, MAX_myPrivateInt));
//		f.set("InnerNode.innerInt", new IntSet(MIN_Node_InnerNode_innerInt, MAX_Node_InnerNode_innerInt));
//		f.set("myPublicDouble", new DoubleSet(MIN_myPublicDouble, MAX_myPublicDouble));
//		f.set("myPublicInt", new IntSet(MIN_myPublicInt, MAX_myPublicInt));
		return f;
		
		// TODO: - DONE - only go one class deep
		// TODO: - DONE - need to do Wrappering like Khurshid discussed: this could be solved by using Lists instead of Sets
		// TODO: does root object need to have its own ClassDomain object?
		// TODO: - DONE - use LinkedTreeMap to maintain insertion order?
		// TODO: handel Comparables: this should handle all wrapper types and more
	}

	public static Finitization finTestClass(int scope) {
		return finTestClass(
			0, scope, 0, scope, scope, 0, scope, 0, scope, 0, scope, 0, scope, scope, 0, scope, scope, 0, scope);
	}
	
//	public static void main(String[] args) {
//		Finitization f = finTestClass(3);
//		Predicate p = new Predicate(TestClass.class, Predicate.REPOK);
//		KoratEngine k = new KoratEngine(f, p);
//		java.util.List inputs = k.findAllValidStates();
//		System.out.println("It worked!");
//	}
}