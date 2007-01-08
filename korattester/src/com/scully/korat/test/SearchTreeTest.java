package com.scully.korat.test;

import com.scully.korat.MethodVerifier;

import junit.framework.TestCase;

public class SearchTreeTest extends TestCase
{
    KoratTester tester;

    protected void setUp() throws Exception
    {
        this.tester = new KoratTester(SearchTree.class);
    }

    public void testRemove()
    {
        this.tester.setMethodName("remove");
        this.tester.setParamTypes(new Class[] { int.class });
        this.tester.setParamValues(new Object[] { new Integer(1) });
        this.tester.setMethodVerifier(new MethodVerifier() {
            public boolean postCondition()
            {
                SearchTree pre = (SearchTree) getPreObject();
                SearchTree post = (SearchTree) getPostObject();
                int value = ((Integer) ((Object[]) getValues())[0]).intValue();
                boolean result = ((Boolean) getResult()).booleanValue();
                return post.repOk() && !post.contains(value) && result == pre.contains(value);
            };
        });
        assertTrue(this.tester.execute());
    }
}
