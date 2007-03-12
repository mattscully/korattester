package com.scully.korat.instrument;

import java.lang.reflect.Method;

import com.scully.korat.KoratBaseTest;
import com.scully.korat.instrument.Instrumenter;
import com.scully.korat.map.StateObjectDTO;
import com.scully.korat.map.TestStateSpaceDTO;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.Loader;
import junit.framework.TestCase;

public class InstrumenterTest extends TestCase
{
    TestStateSpaceDTO stateSpace;
    Instrumenter instrumenter;
    CtClass ctRootClass;
    CtClass ctNode;
    Loader classLoader;

    protected void setUp() throws Exception
    {
        super.setUp();
        this.stateSpace = KoratBaseTest.createFiniteStateSpace(3);
        this.instrumenter = new Instrumenter(this.stateSpace, null);
        ClassPool pool = new ClassPool(true);
        this.classLoader = new Loader(pool);
        this.ctRootClass = pool.get(this.stateSpace.getRootClass());
        this.ctRootClass.stopPruning(true);
        this.ctNode = pool.get(((StateObjectDTO)this.stateSpace.getStateObjects().get(0)).getType());
        this.ctNode.stopPruning(true);
    }
    
    protected void tearDown() throws Exception
    {
        super.tearDown();
        this.ctRootClass.defrost();
        this.ctNode.defrost();
    }
    
//    public void testInstrument()
//    {
//        try
//        {
//            this.instrumenter.instrument();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            fail("Instrumentation failed: " + e.getMessage());
//        }
//    }

    public void testInsertShadowFields()
    {
        try
        {
            this.instrumenter.insertShadowFields(this.ctRootClass);
            Class rootClass = this.classLoader.loadClass(this.ctRootClass.getName());
            rootClass.getDeclaredField("$kor_root"); // throws exception if not found
            rootClass.getDeclaredField("$kor_size"); // throws exception if not found
        }
        catch (Exception e)
        {
            fail("Instrumentation failed: " + e.getMessage());
        }
    }

    public void testInsertObserver()
    {
        try
        {
            this.instrumenter.insertObserver(this.ctRootClass);
            Class rootClass = this.classLoader.loadClass(this.ctRootClass.getName());
            rootClass.getDeclaredField("$kor_observer"); // throws exception if not found
//            System.out.println(ArrayUtils.toString(rootClass.getDeclaredMethods()));
            Method[] methods = rootClass.getDeclaredMethods();
            Method method = null;
            for (int i = 0; i < methods.length; i++)
            {
                if(methods[i].getName().equals("$kor_setObserver"))
                {
                    method = methods[i];
                }
            }
            assertNotNull("method not found", method);
            // for some reason these don't work???
//            rootClass.getMethod("$kor_setObserver", new Class[] {IKoratObserver.class}); // throws exception if not found
//            rootClass.getDeclaredMethod("$kor_setObserver", new Class[] {IKoratObserver.class}); // throws exception if not found
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Instrumentation failed: " + e.getMessage());
        }
    }

    public void testInsertGettersSetters()
    {
        try
        {
            // first two are prerequesites!
            this.instrumenter.insertShadowFields(this.ctRootClass);
            this.instrumenter.insertObserver(this.ctRootClass);
            this.instrumenter.insertGettersSetters(this.ctRootClass);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Instrumentation failed: " + e.getMessage());
        }
    }
    
    public void testInstrumentFieldAccesses() throws Exception
    {
        try
        {
            // these are prerequesites!
            this.instrumenter.insertShadowFields(this.ctRootClass);
            this.instrumenter.insertShadowFields(this.ctNode);
            this.instrumenter.insertObserver(this.ctRootClass);
            this.instrumenter.insertObserver(this.ctNode);
            this.instrumenter.insertGettersSetters(this.ctRootClass);
            this.instrumenter.insertGettersSetters(this.ctNode);
            
            this.instrumenter.instrumentFieldAccesses(this.ctRootClass);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Instrumentation failed: " + e.getMessage());
        }
    }
//
//    public void testGetFieldsForType()
//    {
//        fail("Not yet implemented");
//    }
//
//    public void testGetStateObjectNames()
//    {
//        fail("Not yet implemented");
//    }

}
