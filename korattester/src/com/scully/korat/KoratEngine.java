/*
 * Created on Apr 17, 2005
 *
 */
package com.scully.korat;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.scully.korat.finitization.Finitization;
import com.scully.korat.finitization.ObjField;
import com.scully.korat.finitization.Predicate;
import com.scully.korat.map.CandidateFieldDTO;
import com.scully.korat.map.CandidateStateDTO;
import com.scully.korat.map.TestStateSpaceDTO;


/**
 * @author mscully
 * 
 */
public class KoratEngine
{
    private static boolean ISOMORPHISM_BREAKING = true;

    private static boolean PRUNING = true;

    private static NumberFormat nf = null;

    static
    {
        nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(3);
    }

    Predicate predicate;

    Object rootObject;

    CandidateState candidateState;

    boolean backtrackedRoot;
    
    KoratObserver observer;
    
    Finitization finitization;

    private Map dtoMap;

    public KoratEngine(TestStateSpaceDTO stateSpace)
    {
        this.finitization = new Finitization(stateSpace, PRUNING);
        this.predicate = new Predicate(finitization.getRootClass(), Predicate.REPOK);
        this.rootObject = finitization.getRootObject();
        this.candidateState = new CandidateState(finitization.getSpace(), finitization.getObjFields());
        this.observer = finitization.getObserver();
        this.backtrackedRoot = false;
        this.dtoMap = new HashMap();
    }

    /**
     * Produce possible valid states for the given predicate and finitization
     * 
     * @return List<CandidateStateDTO>
     */
    public List findAllValidStates()
    {
        List validStates = new ArrayList(); // Found CVs that are valid inputs
        Stack accessedFieldsStack = new Stack();
        
        // set the observer up with values needed for observing the execution
        this.observer.setObservedFields(accessedFieldsStack);
        this.observer.setObjFields(this.candidateState.getStateFields());
        
        int count = 0; // count of considered CVs

        long stime = System.currentTimeMillis();
        do
        {
            count++;

            // execute "pred(root)" and update stack
            boolean isValidState = observeExecution(this.predicate, this.rootObject, accessedFieldsStack);
//            System.out.println("Testing State: " + this.candidateState + " => " + (isValidState ? "ADDING" : ""));
            // if state is valid, add it to the list of valid inputs
            if (isValidState)
            {
                // validStates.add(candidateState.copyOfValueIndexes());
                validStates.add(new CandidateStateDTO(candidateState));
            }
            // if *not* optimizing, add other fields to the stack
            // 7/10/2006 - is this right?? -> Eliminate this since we're pushing every field to the stack
            // This means that:
            // * PRUNING ->
            //      if invalid, only backtrack along fields that were accessed in predicate
            //      if valid, we will need to include all fields accessible in this candidate
            // * NO PRUNING ->
            //      always check all reachable fields (this makes field monitoring useless
            //      since all accessible fields are always checked instead of the subset that
            //      caused the candidate to fail.
            if (!PRUNING || isValidState)
            {
                // add all reachable fields not already in stack
                ObjField[] reachableObjFields = getReachableObjFields(this.rootObject);
                for (int i = 0; i < reachableObjFields.length; i++)
                {
                    ObjField f = reachableObjFields[i];
                    if (!accessedFieldsStack.contains(f))
                    {
                        accessedFieldsStack.push(f);
                    }
                }
            }
            // backtrack
            while (!accessedFieldsStack.isEmpty())
            {
                // field on the top of stack
                ObjField topField = (ObjField) accessedFieldsStack.peek(); 

                if (ISOMORPHISM_BREAKING)
                {
                    skipIsomorphicCandidates(topField, accessedFieldsStack);
                }
                // if there are more values to be explored for this field, do it.
                if (candidateState.hasMoreValuesInFieldDomain(topField))
                {
                    // increment field
                    candidateState.setValueAtIndex(topField, candidateState.getValueIndex(topField) + 1);
                    break; // stop backtracking (or don't start)
                }
                else
                {
                    candidateState.setValueAtIndex(topField, CandidateState.ZERO);
                    accessedFieldsStack.pop(); // keep backtracking (or start)
                }
            }
        }
        while (!accessedFieldsStack.isEmpty()); // end do

        System.out.println("Total Considered Candidates: " + count);
        System.out.println("Total Time (sec):  " + nf.format((System.currentTimeMillis() - stime) / 1000.0D));

        return validStates;
    }

    /**
     * @param topField
     * @param accessedFieldsStack
     */
    private void skipIsomorphicCandidates(ObjField topField, Stack accessedFieldsStack)
    {
        // the max value index of all fields in the same field/class domain
        // excluding the topField of the stack.
        int maxFieldValueIndex = -1; // $m_f$ from the first Korat paper

        // a straightforward way to computer 'mf'
        ObjField[] stackWithoutTop = (ObjField[]) accessedFieldsStack.toArray(new ObjField[0]);
        // search the stack backwards to get the field right before this one
        // -- do "-2" to skip the top
        for (int i = stackWithoutTop.length - 2; i >= 0; i--)
        {
            ObjField field = stackWithoutTop[i];
            if (candidateState.hasEqualDomainsForFields(topField, field))
            {
                maxFieldValueIndex = Math.max(maxFieldValueIndex, candidateState.getValueIndex(field));
            }
        }
        // if an isomorphic candidate would be next...
        int topFieldIndex = candidateState.getValueIndex(topField);
        if (topFieldIndex > maxFieldValueIndex)
        {
            // ...skip to the end of domain
            while (candidateState.hasMoreValuesInClassDomain(topField))
            {
                candidateState.setValueAtIndex(topField, ++topFieldIndex);
            }
        }
    }

    /**
     * Execute the predicate. In order to do pruning, this method would do field
     * monitoring and add accessed fields to the stack in the order they were
     * accessed
     * 
     * @param pred
     *            Preditcate to be invoked
     * @param root
     *            The structure root
     * @param stack
     *            The stack of ObjFields - not used without pruning
     * @return
     */
    private boolean observeExecution(Predicate pred, Object root, Stack stack)
    {
        // only observe executions if PRUNING is enabled
        if(PRUNING)
        {
	        this.candidateState.setObservableIndexes();
        }
        return pred.invoke(root);
    }

    /**
     * Recursively traverse the data structure from the root
     * 
     * @param root
     * @return
     */
    private ObjField[] getReachableObjFields(Object root)
    {
        ArrayList objFields = new ArrayList();
        addFieldsForObject(root, objFields);
        return (ObjField[]) objFields.toArray(new ObjField[0]);
    }

    /**
     * @param rootClass
     * @param objFields
     */
    private void addFieldsForObject(Object rootObject, ArrayList objFields)
    {
        if (rootObject == null)
        {
            return;
        }
        Class rootClass = rootObject.getClass();
        Field[] fields = rootClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++)
        {
            Field field = fields[i];
            // ignore JML instrumented fields
            if (field.getName().startsWith("rac$") || field.getName().startsWith("$kor_"))
            {
                continue;
            }
            // we may need to access this field if it's an Object
            field.setAccessible(true);
            ObjField objField = new ObjField(rootObject, field);
            // if encountered a loop, skip it
            if (objFields.contains(objField))
            {
                continue;
            }
            objFields.add(objField);
            // we need to recurse down fields that are NOT wrappers
            // TODO: How will wrapper objects be handled??
            if (!objField.getField().getType().isPrimitive())
            {
                Object o = objField.getFieldValue();
                if (!(o instanceof Number))
                {
                    addFieldsForObject(o, objFields);
                }
            }
        }
    }

    public static void setIsoMorphismBreaking(boolean flag)
    {
        ISOMORPHISM_BREAKING = flag;
    }
    
    public static void setPruning(boolean flag)
    {
        PRUNING = flag;
    }

    /**
     * @param candidateState
     *            the candidateState to set
     */
    public void setCandidateState(CandidateStateDTO candidateState)
    {
        List fields = candidateState.getCandidateFields();
        for (Iterator iter = fields.iterator(); iter.hasNext();)
        {
            CandidateFieldDTO field = (CandidateFieldDTO) iter.next();
            ObjField objField = (ObjField) this.dtoMap.get(field.getFieldId());
            if (objField == null)
            {
                try
                {
                    Class parent = Class.forName(field.getParentType());
                    objField = getNextMatchingObjField(parent, field.getFieldName());
                    // TODO: Check for null and throw error
                    this.dtoMap.put(field.getFieldId(), objField);
                }
                catch (ClassNotFoundException e)
                {
                    // TODO this is an important error to handle
                    e.printStackTrace();
                }
            }
            this.candidateState.setValueAtIndex(objField, field.getValueIndex());
        }
    }

    private ObjField getNextMatchingObjField(Class parentClass, String fieldName)
    {
        ObjField[] objFields = this.candidateState.getStateFields();
        for (int i = 0; i < objFields.length; i++)
        {
            ObjField objField = objFields[i];
            Class clazz = objField.getObject().getClass();
            if (clazz.equals(parentClass) && fieldName.equals(objField.getField().getName())
                    && !this.dtoMap.containsValue(objField))
            {
                return objField;
            }
        }
        return null;
    }

    /**
     * @return the predicate
     */
    public Predicate getPredicate()
    {
        return predicate;
    }

    /**
     * @return the rootObject
     */
    public Object getRootObject()
    {
        return rootObject;
    }

    /**
     * @return the candidateState
     */
    public CandidateState getCandidateState()
    {
        return candidateState;
    }

    /**
     * @return the finitization
     */
    public Finitization getFinitization()
    {
        return finitization;
    }
}
