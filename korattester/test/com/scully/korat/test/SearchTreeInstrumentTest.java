package com.scully.korat.test;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.scully.korat.KoratClient;
import com.scully.korat.map.TestStateSpaceDTO;

public class SearchTreeInstrumentTest
{
    
    TestStateSpaceDTO stateSpace;

    @Before
    public void setUp()
    {
        this.stateSpace = SearchTree_remove.createFiniteStateSpace(3);
    }
    
    @Ignore("Uses hard-coded path for local machine")
    @Test
    public void testCreateStateSpaceClasspath() throws Exception
    {
        String classpath = "C:\\Documents and Settings\\mscully\\My Documents\\UT\\Archive\\Verification and Validation\\workspace\\korattester\\classes";
        KoratClient.populateTestCandidates(this.stateSpace, new String[] {classpath});
    }
}
