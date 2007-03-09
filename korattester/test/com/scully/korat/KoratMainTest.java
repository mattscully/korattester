package com.scully.korat;

import javassist.Loader;
import junit.framework.TestCase;

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
    
    public void testMainPrintsXml() throws Exception
    {
//        ClassPool pool = new ClassPool(true);
//        ClassPool pool = ClassPool.getDefault();
//        pool.insertClassPath(new ClassClassPath(ClassPool.class));
//        Loader loader = new Loader(pool);
        Loader loader = new Loader();
        try
        {
            loader.run("com.scully.korat.KoratMain", new String[] {BeanXmlMapper.beanToXml(stateSpace)});
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            fail("Failed to run KoratMain");
        }
    }

}
