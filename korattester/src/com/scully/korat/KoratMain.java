package com.scully.korat;

import com.scully.korat.map.BeanXmlMapper;
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
    
    public static String run(String stateSpaceXml, String[] classpath)
    {
        if (stateSpaceXml == null)
        {
            throw new IllegalArgumentException("Korat requires XML String passed to KoratMain");
        }

        TestStateSpaceDTO stateSpace = (TestStateSpaceDTO) BeanXmlMapper.xmlToBean(stateSpaceXml, "TestStateSpaceDTO",
                TestStateSpaceDTO.class);
        
        KoratClient.populateTestCandidates(stateSpace, classpath);
        return BeanXmlMapper.beanToXml(stateSpace);
    }
}
