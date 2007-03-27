package com.scully.korat;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import com.scully.korat.map.BeanXmlMapper;
import com.scully.korat.map.CandidateStateDTO;
import com.scully.korat.map.TestStateSpaceDTO;

public class KoratMain
{

    /**
     * @param args
     */
    public static String run(String classpath)
    {
        return run(classpath, null);
    }
    
    public static void main(String[] args)
    {
        if(args.length < 1)
        {
            throw new IllegalArgumentException("must have at least one argument");
        }
        run(args[0], (String[]) ArrayUtils.subarray(args, 1, 2));
    }
    
    public static String run(String stateSpaceXml, String[] classpath)
    {
        if (stateSpaceXml == null)
        {
            throw new IllegalArgumentException("Korat requires XML String passed to KoratMain");
        }

        // read in the state space
        TestStateSpaceDTO stateSpace = (TestStateSpaceDTO) BeanXmlMapper.xmlToBean(stateSpaceXml, "TestStateSpaceDTO",
                TestStateSpaceDTO.class);
        
        // Find all valid states
        KoratEngine korat = new KoratEngine(stateSpace, classpath);
        List<CandidateStateDTO> validStates = korat.findAllValidStates();
        stateSpace.setCandidateStates(validStates);
//        System.out.println("Generated States = " + validStates.size());
        
        return BeanXmlMapper.beanToXml(stateSpace);
    }
}
