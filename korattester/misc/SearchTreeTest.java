package com.scully.korat.test;


import junit.framework.TestCase;

public class SearchTreeTest extends TestCase
{
    KoratTester<SearchTree> tester;

    protected void setUp() throws Exception
    {
        this.tester = new KoratTester<SearchTree>(SearchTree.class);
    }

    public void testRemove()
    {
        this.tester.setMethod("remove", new Class[] { int.class });
        this.tester.setPredicate(new Predicate<SearchTree>() {
            public boolean postCondition()
            {
                SearchTree pre = getPreObject();
                SearchTree post = getPostObject();
                // this gets the parameter that was passed to the remove() method
                int value = getIntParam(0);
                // this is the value returned from the remove() method
                boolean result = getBooleanResult();
                // tests whether post condition was true
                return post.repOk() && !post.contains(value) && result == pre.contains(value);
            };
        });
        
        this.tester.setParamValues(new Object[] { new Integer(1) });
        assertTrue(this.tester.execute());
        
        this.tester.setParamValues(new Object[] { new Integer(2) });
        assertTrue(this.tester.execute());
        
        this.tester.setParamValues(new Object[] { new Integer(3) });
        assertTrue(this.tester.execute());
    }
}
