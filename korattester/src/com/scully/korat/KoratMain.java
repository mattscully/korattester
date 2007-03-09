package com.scully.korat;

import org.apache.commons.lang.ArrayUtils;

import com.scully.korat.map.BeanXmlMapper;
import com.scully.korat.map.TestStateSpaceDTO;

public class KoratMain
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        if (args == null || args.length < 1)
        {
            throw new IllegalArgumentException("Korat requires XML String passed to KoratMain");
        }

        TestStateSpaceDTO stateSpace = (TestStateSpaceDTO) BeanXmlMapper.xmlToBean(args[0], "TestStateSpaceDTO",
                TestStateSpaceDTO.class);
        
        // XXX: Shouldn't even need the classpath. It can be set by the loader.
        // the classpath can be passed as additional String args
        String[] classpath = null;
        if (args.length > 1)
        {
            classpath = (String[]) ArrayUtils.subarray(args, 1, args.length);
        }
        
        KoratClient.populateTestCandidates(stateSpace, classpath);
        System.out.println(BeanXmlMapper.beanToXml(stateSpace));
    }
}
