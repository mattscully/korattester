package com.scully.korat.test;

import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.SystemUtils;

import com.scully.korat.KoratEngine;
import com.scully.korat.map.BeanXmlMapper;
import com.scully.korat.map.CandidateStateDTO;
import com.scully.korat.map.TestStateSpaceDTO;

public class KoratTester<E> implements Iterable<E>
{
    Object[] paramValues;

    Method method;

    Predicate<E> predicate;

    KoratEngine preState;

    KoratEngine postState;

    TestStateSpaceDTO stateSpace;

    List<CandidateStateDTO> failedCandidates;

    public KoratTester(Class testClass)
    {
        this(ClassUtils.getPackageName(testClass).replace('.', '/') + "/" + ClassUtils.getShortClassName(testClass)
                + ".xml");
    }

    public KoratTester(String stateSpacePath)
    {
        // prevent instrumentation for testing
        KoratEngine.setPruning(false);
        
        // normalize the path
        if (!stateSpacePath.startsWith("/"))
        {
            stateSpacePath = "/" + stateSpacePath;
        }

        // deserialize the state space
        InputStreamReader reader = new InputStreamReader(this.getClass().getResourceAsStream(stateSpacePath));
        this.stateSpace = (TestStateSpaceDTO) BeanXmlMapper.readBean(reader, "TestStateSpaceDTO",
                TestStateSpaceDTO.class);

        // initialize a korat engine for both pre and post states
        this.preState = new KoratEngine(stateSpace);
        this.postState = new KoratEngine(stateSpace);
    }

    // KoratEngine cannot use generics because it must remain object agnostic
    // during instrumentation.
    @SuppressWarnings("unchecked")
    public boolean execute()
    {
        // initialize failed candidates list
        this.failedCandidates = new ArrayList<CandidateStateDTO>();

        // set the pre/post objects
        predicate.setPreObject((E)preState.getRootObject());
        predicate.setPostObject((E)postState.getRootObject());
        predicate.setParameters(this.paramValues);

        // get the test candidates
        List candidateStates = this.stateSpace.getCandidateStates();

        // vars for tracking the results
        boolean result = true;
        boolean singleResult = false;

        int count = 0;
        for (Iterator iter = candidateStates.iterator(); iter.hasNext(); count++)
        {
            CandidateStateDTO candidateState = (CandidateStateDTO) iter.next();
            // initialize the root object
            preState.setCandidateState(candidateState);
            postState.setCandidateState(candidateState);
//            System.out.println("[" + count + "] = " + candidateState);
            singleResult = testPostStateCandidate();
            if (!singleResult)
            {
                this.failedCandidates.add(candidateState);
            }
            result &= singleResult;
        }
        return result;
    }

    public Iterator<E> iterator()
    {
        return new FailedObjectIterator<E>(this.postState, this.failedCandidates);
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
    private boolean testPostStateCandidate()
    {
        try
        {
            // invoke test subject
            this.predicate.setResult(this.method.invoke(this.predicate.getPostObject(), this.paramValues));
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
        return this.predicate.postCondition();
    }

    public void setMethod(String methodName, Class[] paramTypes)
    {
        this.method = getMethod(this.postState.getRootObject().getClass(), methodName, paramTypes);
    }

    public Method getMethod()
    {
        return this.method;
    }

    public String getMethodName()
    {
        return this.method.getName();
    }

    /**
     * @return the predicate
     */
    public Predicate<E> getMethodVerifier()
    {
        return predicate;
    }

    /**
     * @param predicate the predicate to set
     */
    public void setPredicate(Predicate<E> predicate)
    {
        this.predicate = predicate;
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

    /**
     * Return a String representation of the failed states separated by the
     * system's defined line separator.
     */
    @Override
    public String toString()
    {
        StringBuilder failedString = new StringBuilder(50);
        boolean isFirst = true;
        for (E obj : this)
        {
            // skip first entry for line separator
            if(!isFirst)
            {
	            failedString.append(SystemUtils.LINE_SEPARATOR);
            }
            
            failedString.append(obj.toString());
            isFirst = false;
        }
        return failedString.toString();

    }
}
