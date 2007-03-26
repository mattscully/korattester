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
    
//    public void testCreateStateSpaceXml() throws Exception
//    {
//        // TODO: Choose Korat feature execution
//        KoratClient.setIsoMorphismBreaking(true);
//        KoratClient.populateTestCandidates(this.stateSpace);
////        System.out.println(BeanXmlMapper.beanToXml(this.stateSpace));
//    }
    
    public void testCreateStateSpaceClasspath() throws Exception
    {
        String classpath = "C:\\Documents and Settings\\mscully\\My Documents\\UT\\Archive\\Verification and Validation\\workspace\\korattester\\classes";
        // TODO: Choose Korat feature execution
        KoratClient.setIsoMorphismBreaking(true);
        KoratClient.populateTestCandidates(this.stateSpace, new String[] {classpath});
//        System.out.println(BeanXmlMapper.beanToXml(this.stateSpace));
    }
    
//    public void testTestingRemoveMethod() throws Exception
//    {
//        // remove method signature
//        String testMethod = "remove";
//        Class[] paramTypes = new Class[] { int.class };
//        // TODO: Define test parameters for inputs
//        Object[] paramValues = new Object[] { new Integer(1) };
//        
//        // TODO: Choose Korat feature execution
//        KoratClient.setIsoMorphismBreaking(true);
//        KoratClient.populateTestCandidates(this.stateSpace);
//        System.out.println(BeanXmlMapper.beanToXml(this.stateSpace));
//        KoratClient.testMethod(this.stateSpace, testMethod, paramTypes, paramValues);
//    }

}
