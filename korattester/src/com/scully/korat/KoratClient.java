/*
 * Created on May 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.scully.korat;

import java.util.List;

import com.scully.korat.map.CandidateStateDTO;
import com.scully.korat.map.TestStateSpaceDTO;



/**
 * @author mscully
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class KoratClient
{

    /**
     * 
     */
    
    /* ONLY THESE TWO METHODS ARE REALLY USEFUL FOR THE ECLIPSE PLUGIN
     * BUT THIS WAS IMPLEMENTED IN KoratMain
     */ 
    public static void populateTestCandidates(TestStateSpaceDTO stateSpace)
    {
	    populateTestCandidates(stateSpace, null);
    }
    public static void populateTestCandidates(TestStateSpaceDTO stateSpace, String[] codeClasspath)
    {
        KoratEngine korat = new KoratEngine(stateSpace, codeClasspath);
        List<CandidateStateDTO> validStates = korat.findAllValidStates();
        stateSpace.setCandidateStates(validStates);
//        System.out.println("Generated " + validStates.size() + " valid states.");
    }

    public static void setPruning(boolean flag)
    {
        KoratEngine.setPruning(flag);
    }

}
