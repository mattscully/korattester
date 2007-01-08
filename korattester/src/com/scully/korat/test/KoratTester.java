package com.scully.korat.test;

import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ClassUtils;

import com.scully.korat.KoratEngine;
import com.scully.korat.MethodVerifier;
import com.scully.korat.map.BeanXmlMapper;
import com.scully.korat.map.CandidateStateDTO;
import com.scully.korat.map.TestStateSpaceDTO;


public class KoratTester
{
    Class[] paramTypes;

    Object[] paramValues;

    String methodName;

    MethodVerifier methodVerifier;

    TestStateSpaceDTO stateSpace;

    public KoratTester(Class testClass)
    {
        this(ClassUtils.getPackageName(testClass).replace('.', '/') + "/" + ClassUtils.getShortClassName(testClass)
                + ".xml");
    }

    public KoratTester(String stateSpacePath)
    {
        // normalize the path
        if (!stateSpacePath.startsWith("/"))
        {
            stateSpacePath = "/" + stateSpacePath;
        }
        InputStreamReader reader = new InputStreamReader(this.getClass().getResourceAsStream(stateSpacePath));
        this.stateSpace = (TestStateSpaceDTO) BeanXmlMapper.readBean(reader, "TestStateSpaceDTO",
                TestStateSpaceDTO.class);
    }

    public boolean execute()
    {
        // TODO: shouldn't need to set this when instrumentation is done
        KoratEngine.setPruning(false);

        // initialize a korat engine for both pre and post states
        KoratEngine preKorat = new KoratEngine(stateSpace);
        KoratEngine postKorat = new KoratEngine(stateSpace);

        // set the pre/post objects
        methodVerifier.setPreObject(preKorat.getRootObject());
        methodVerifier.setPostObject(postKorat.getRootObject());
        methodVerifier.setValues(this.paramValues);

        // get the test candidates
        List candidateStates = this.stateSpace.getCandidateStates();

        // vars for tracking the results
        boolean result = true;
        boolean singleResult = false;

        // get the test Method object
        Method method = getMethod(postKorat.getRootObject().getClass(), this.methodName, this.paramTypes);

        int count = 0;
        for (Iterator iter = candidateStates.iterator(); iter.hasNext(); count++)
        {
            CandidateStateDTO candidateState = (CandidateStateDTO) iter.next();
            // initialize the root object
            preKorat.setCandidateState(candidateState);
            postKorat.setCandidateState(candidateState);
            System.out.println("[" + count + "] = " + candidateState);
            singleResult = testCandidate(method);
            if (!singleResult)
            {
                System.out.println("\tCandidate Test FAILED: Invariant broken for parameter(s): "
                        + ArrayUtils.toString(this.paramValues));
            }
            result &= singleResult;
        }
        return result;
    }

    /**
     * @param fin
     * @param methodName
     * @param paramTypes
     * @param method
     * @return
     */
    private static Method getMethod(Class clazz, String methodName, Class[] paramTypes)
    {
        Method method = null;
        try
        {
            method = clazz.getDeclaredMethod(methodName, paramTypes);
            method.setAccessible(true);
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        return method;
    }

    /**
     * @param fin
     * @param space
     * @param root
     * @param cv
     */
    //            testCandidate(postKorat.getRootObject(), method, paramValues);
    private boolean testCandidate(Method testMethod)
    {
        // get objects needed to create candidate input
        //        Predicate pred = new Predicate(this.methodVerifier.getPostObject().getClass(), Predicate.REPOK);
        try
        {
            // invoke test subject
            this.methodVerifier.setResult(testMethod.invoke(this.methodVerifier.getPostObject(), this.paramValues));
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        // verify predicate is true
        return this.methodVerifier.postCondition();
    }

    /**
     * @return the methodName
     */
    public String getMethodName()
    {
        return methodName;
    }

    /**
     * @param methodName the methodName to set
     */
    public void setMethodName(String methodName)
    {
        this.methodName = methodName;
    }

    /**
     * @return the methodVerifier
     */
    public MethodVerifier getMethodVerifier()
    {
        return methodVerifier;
    }

    /**
     * @param methodVerifier the methodVerifier to set
     */
    public void setMethodVerifier(MethodVerifier methodVerifier)
    {
        this.methodVerifier = methodVerifier;
    }

    /**
     * @return the paramTypes
     */
    public Class[] getParamTypes()
    {
        return paramTypes;
    }

    /**
     * @param paramTypes the paramTypes to set
     */
    public void setParamTypes(Class[] paramTypes)
    {
        this.paramTypes = paramTypes;
    }

    /**
     * @return the paramValues
     */
    public Object[] getParamValues()
    {
        return paramValues;
    }

    /**
     * @param paramValues the paramValues to set
     */
    public void setParamValues(Object[] paramValues)
    {
        this.paramValues = paramValues;
    }
}
