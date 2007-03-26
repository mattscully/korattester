package com.scully.korat;

import junit.framework.TestCase;

import com.scully.korat.instrument.Loader;
import com.scully.korat.map.BeanXmlMapper;
import com.scully.korat.map.TestStateSpaceDTO;
import com.scully.korat.test.SearchTree_remove;

public class KoratMainTest extends TestCase
{

    TestStateSpaceDTO stateSpace;

    protected void setUp() throws Exception
    {
        super.setUp();
        this.stateSpace = SearchTree_remove.createFiniteStateSpace(3);
    }

    public void testRun() throws Exception
    {
        //        ClassPool pool = new ClassPool(true);
        //        ClassPool pool = ClassPool.getDefault();
        //        pool.insertClassPath(new ClassClassPath(ClassPool.class));
        //        Loader loader = new Loader(pool);
        Loader loader = new Loader();
        String stateSpaceXml = null;
        try
        {
            //            loader.run("com.scully.korat.KoratMain", new String[] {BeanXmlMapper.beanToXml(this.stateSpace)});
            stateSpaceXml = (String) loader.invokeExactMethod("com.scully.korat.KoratMain", "run",
                    new Object[] { BeanXmlMapper.beanToXml(this.stateSpace) });
            assertNotNull("StateSpace XML returned NULL", stateSpaceXml);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            fail("Failed to run KoratMain");
        }
    }

}
