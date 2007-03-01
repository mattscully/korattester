/*
 * Created on May 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.scully.korat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.scully.korat.finitization.Predicate;
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
    public static void populateTestCandidates(TestStateSpaceDTO stateSpace)
    {
        KoratEngine korat = new KoratEngine(stateSpace);
        List<CandidateStateDTO> validStates = korat.findAllValidStates();
        stateSpace.setCandidateStates(validStates);
        System.out.println("Generated " + validStates.size() + " valid states.");
    }

    public static void testMethod(TestStateSpaceDTO stateSpace, String testMethod, Class[] paramTypes,
            Object[] paramValues)
    {
        KoratEngine korat = new KoratEngine(stateSpace);
        List<CandidateStateDTO> candidateStates = stateSpace.getCandidateStates();

        // get the test Method object
        Method method = getTestMethod(korat.getRootObject().getClass(), testMethod, paramTypes);

        int count = 0;
        for(CandidateStateDTO candidateState : candidateStates)
        {
            // initialize the root object
            korat.setCandidateState(candidateState);
            System.out.printf("[%d] = %s%n", count, candidateState);
            testCandidate(korat.getRootObject(), method, paramValues);
        }
    }

    /**
     * @param fin
     * @param testMethod
     * @param paramTypes
     * @param method
     * @return
     */
    private static Method getTestMethod(Class clazz, String testMethod, Class[] paramTypes)
    {
        Method method = null;
        try
        {
            method = clazz.getDeclaredMethod(testMethod, paramTypes);
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
    private static void testCandidate(Object cv, Method method, Object[] params)
    {
        // get objects needed to create candidate input
        Predicate pred = new Predicate(cv.getClass(), Predicate.REPOK);
        try
        {
            // invoke test subject
            method.invoke(cv, params);
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
        if (!pred.invoke(cv))
        {
            System.out.println("\tCandidate Test FAILED: Invariant broken for parameter(s): " + 1);
        }
    }

//    /**
//     * 
//     */
//    public static void serializeInputs(Finitization fin, String serializePath)
//    {
//        Predicate pred = new Predicate(fin.getRootClass(), Predicate.REPOK);
//        KoratEngine korat = new KoratEngine(fin, pred);
//        List inputs = korat.findAllValidStates();
//        System.out.println("Generated " + inputs.size() + " inputs.");
//
//        try
//        {
//            File dir = new File(serializePath);
//            if (dir.exists())
//            {
//                cleanDir(dir);
//            }
//            else
//            {
//                dir.mkdir();
//            }
//            KoratClient.serializeInputs(inputs, fin, dir);
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//    }

//    private static void serializeInputs(List inputs, Finitization fin, File dir) throws IOException
//    {
//        Map space = fin.getSpace();
//        Serializable root = (Serializable) fin.getRootObject();
//        ObjField[] objFields = fin.getObjFields();
//        StringBuffer fileName = new StringBuffer(64);
//
//        Iterator cvIterator = inputs.iterator();
//        for (int i = 1; cvIterator.hasNext(); i++)
//        {
//            // empty out name buffer
//            fileName.setLength(0);
//            fileName.append(i).append("_");
//            Map cv = (Map) cvIterator.next();
//            // System.out.println("[" + i + "] = " +
//            // fin.orderedCandidateString(cv));
//            for (int j = 0; j < objFields.length; j++)
//            {
//                ObjField objField = objFields[j];
//                FieldDomain fieldDomain = (FieldDomain) space.get(objField);
//                Integer index = (Integer) cv.get(objField);
//                objField.set(fieldDomain.getValueAtIndex(index));
//                fileName.append(index).append("-");
//            }
//            // remove last "-" and add the file extension
//            fileName.deleteCharAt(fileName.length() - 1).append(".cv");
//            File file = new File(dir, fileName.toString());
//            Serializer.store(root, file);
//            // System.out.println("Wrote file: " + file);
//        }
//    }

//    /**
//     * @param dir
//     */
//    private static void cleanDir(File dir)
//    {
//        File[] files = dir.listFiles();
//        for (int i = 0; i < files.length; i++)
//        {
//            File file = files[i];
//            file.delete();
//        }
//    }

//    /**
//     * @param testMethod
//     *            TODO
//     * @param paramTypes
//     *            TODO
//     * @param paramValues
//     *            TODO
//     * 
//     */
//    public static void testSerializedObjects(Finitization fin, String testMethod, Class[] paramTypes,
//            Object[] paramValues, String serializePath)
//    {
//        // Predicate pred = new Predicate(fin.getRootClass(), Predicate.REPOK);
//        File dir = new File(serializePath);
//        File[] cvObjects = dir.listFiles();
//        for (int i = 0; i < cvObjects.length; i++)
//        {
//            File file = cvObjects[i];
//            Object cv = null;
//            try
//            {
//                cv = Serializer.load(file);
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//            catch (ClassNotFoundException e)
//            {
//                e.printStackTrace();
//            }
//            Method method = getTestMethod(fin, testMethod, paramTypes);
//            System.out.println("Testing Candidate from file: " + file);
//            testCandidate(cv, method, paramValues);
//        }
//    }

    public static void setIsoMorphismBreaking(boolean flag)
    {
        KoratEngine.setIsoMorphismBreaking(flag);
    }
    
    public static void setPruning(boolean flag)
    {
        KoratEngine.setPruning(flag);
    }

}
