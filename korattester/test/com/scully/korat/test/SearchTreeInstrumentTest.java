package com.scully.korat.test;

import com.scully.korat.KoratClient;
import com.scully.korat.map.TestStateSpaceDTO;

import junit.framework.TestCase;

public class SearchTreeInstrumentTest extends TestCase
{
    
    TestStateSpaceDTO stateSpace;

    protected void setUp() throws Exception
    {
        super.setUp();
        this.stateSpace = SearchTree_remove.createFiniteStateSpace(3);
    }
    
    public void testCreateStateSpaceClasspath() throws Exception
    {
        String classpath = "C:\\Documents and Settings\\mscully\\My Documents\\UT\\Archive\\Verification and Validation\\workspace\\korattester\\classes";
        KoratClient.populateTestCandidates(this.stateSpace, new String[] {classpath});
    }
}
