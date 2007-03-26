package com.scully.korat.finitization;

/*
 * Created on Apr 10, 2005
 *
 */

/**
 * @author mscully
 *
 */
public class TestClass {
	
	private int myPrivateInt = 0;
	private long myPrivateLong = 0L;
	private Object myPrivateObject = null;
	public int myPublicInt = 0;
	public double myPublicDouble = 0D;
	int myInt = 0;
	long myLong = 0L;
	
	public static void main(String[] args) {
	}
	
	static class Node {
		Node left;
		Node right;
		int value;
		
		static class InnerNode {
			InnerNode up;
			InnerNode down;
			Node left;
			Node sideways;
			int innerInt;
		}
	}
}
