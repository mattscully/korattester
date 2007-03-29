package com.scully.korat.test;

import java.io.Serializable;
import java.util.*;

public class SearchTree implements Serializable
{
	Node root; // root node

	int size; // number of nodes in the tree
    
    boolean bool;

	public static class Node implements Serializable
	{
		Node left; // left child

		Node right; // right child

		int value;
		
		public String toString()
		{
		    return super.toString().substring(super.toString().indexOf('@')+1);
		}
	}
	
	public String toString()
	{
	    return super.toString().substring(super.toString().indexOf('@')+1);
	}
	
	/*@ normal_behavior // non-exceptional specification
	@ // precondition
	@ requires repOk();
	@ // postcondition
	@ ensures repOk() && !contains(value) &&
	@ \result == \old(contains(value));
	@*/
	public boolean remove(int value) {
	    /*/
	    if(root != null && root.left != null)
	    {
		    root.left = root.right;
	    }
	    return true;
	    /*/
	    Node parent = null;
		Node current = root;
//        if(size == 2) size = 3;
		while (current != null) {
//			int cmp = info.compareTo(current.info);
			if (value < current.value) {
				parent = current;
				current = current.left;
			} else if (value > current.value) {
				parent = current;
				current = current.right;
			} else {
				break;
			}
		}
		if (current == null)
			return false;
		Node change = removeNode(current);
		if (parent == null) {
			root = change;
		} else if (parent.left == current) {
			parent.left = change;
		} else {
			parent.right = change;
		}
		return true;
		//*/
	}
	
	Node removeNode(Node current) {
		size--;
		Node left = current.left, right = current.right;
		if (left == null)
			return right;
		if (right == null)
			return left;
		if (left.right == null) {
			current.value = left.value;
			current.left = left.left;
			return current;
		}
		Node temp = left;
		while (temp.right.right != null) {
			temp = temp.right;
		}
		current.value = temp.right.value;
		temp.right = temp.right.left;
		return current;
	}
	
	/*@ pure @*/ boolean contains(int value) {
		Node parent = null;
		Node current = root;
		while (current != null) {
			if (value < current.value) {
				parent = current;
				current = current.left;
			} else if (value > current.value) {
				parent = current;
				current = current.right;
			} else {
				break;
			}
		}
		if (current == null)
			return false;
		return true;
	}

	/*@ pure @*/ boolean repOk() {
        // test boolean flag
        if(!this.bool)
            return false;
		// checks that empty tree has size zero
		if (root == null)
			return size == 0;
		// checks that the input is a tree
		if (!isAcyclic())
			return false;
		// checks that size is consistent
		if (numNodes(root) != size)
			return false;
		// checks that data is ordered
		if (!isOrdered(root))
			return false;
		return true;
	}

	/*@ pure @*/ boolean isAcyclic() {
		Set visited = new HashSet();
		visited.add(root);
		LinkedList workList = new LinkedList();
		workList.add(root);
		while (!workList.isEmpty()) {
			Node current = (Node) workList.removeFirst();
			if (current.left != null) {
				// checks that the tree has no cycle
				if (!visited.add(current.left))
					return false;
				workList.add(current.left);
			}
			if (current.right != null) {
				// checks that the tree has no cycle
				if (!visited.add(current.right))
					return false;
				workList.add(current.right);
			}
		}
		return true;
	}

	/*@ pure @*/ int numNodes(Node n) {
		if (n == null)
			return 0;
		return 1 + numNodes(n.left) + numNodes(n.right);
	}

	/*@ pure @*/ boolean isOrdered(Node n) {
		return isOrdered(n, Integer.MAX_VALUE, Integer.MIN_VALUE);
	}

	/*@ pure @*/ boolean isOrdered(Node n, int min, int max) {
	    if ((min != Integer.MAX_VALUE && n.value <= min) 
				|| (max != Integer.MIN_VALUE && n.value >= max))
			return false;
		if (n.left != null)
			if (!isOrdered(n.left, min, n.value))
				return false;
		if (n.right != null)
			if (!isOrdered(n.right, n.value, max))
				return false;
		return true;
	}
}
