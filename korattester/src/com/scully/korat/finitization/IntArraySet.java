/*
 * Created on Apr 15, 2005
 *
 */
package com.scully.korat.finitization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * @author mscully
 *
 */
public class IntArraySet extends FinSet
{

    public IntArraySet(int min, int max, int size)
    {
        // try to create every possible array given
        // value bounds and array size.
        Integer ZERO = new Integer(0);
        int inclusiveRange = max - min + 1;
        Integer[] valueDomain = new Integer[inclusiveRange];
        for (int i = 0, value = min; i < inclusiveRange; i++, value++)
        {
            valueDomain[i] = value;
        }

        // Use the Korat algorithm to explore all possible arrays
        // given the said constraints

        List<Integer[]> possibleStates = new ArrayList<Integer[]>();
        Stack<Integer> arrayIndexesStack = new Stack<Integer>();

        // initialize the array
        Integer[] state = new Integer[size];
        Arrays.fill(state, ZERO);

        do
        {
            // track actual values for states
            possibleStates.add(valueOf(state, valueDomain));
            for (int i = 0; i < size; i++)
            {
                Integer arrayIndex = new Integer(i);
                if (!arrayIndexesStack.contains(arrayIndex))
                {
                    arrayIndexesStack.push(arrayIndex);
                }
            }
            while (!arrayIndexesStack.isEmpty())
            {
                Integer topArrayIndex = arrayIndexesStack.peek();
                if (state[topArrayIndex] < (inclusiveRange - 1))
                {
                    state[topArrayIndex] = state[topArrayIndex] + 1;
                    break;
                }
                else
                {
                    state[topArrayIndex] = ZERO;
                    arrayIndexesStack.pop();
                }
            }
        }
        while (!arrayIndexesStack.isEmpty());

        // create the class domain indexes
        for (Integer[] array : possibleStates)
        {
            // create a class domain for each array
            ClassDomain classDomain = new ClassDomain();
            Object[] objects = new Object[] { intArrayOf(array) };
            classDomain.set(objects);
            ClassDomainIndex classDomainIndex = new ClassDomainIndex(classDomain);
            this.classDomainIndices.add(classDomainIndex);
        }
    }

    private static Integer[] valueOf(Integer[] array, Integer[] valueDomain)
    {
        if (array == null || array.length == 0)
        {
            return new Integer[] {};
        }
        Integer[] newArray = new Integer[array.length];
        for (int i = 0; i < array.length; i++)
        {
            newArray[i] = valueDomain[array[i]];
        }
        return newArray;
    }

    private static int[] intArrayOf(Integer[] array)
    {
        if (array == null || array.length == 0)
        {
            return new int[] {};
        }
        int[] newArray = new int[array.length];
        for (int i = 0; i < array.length; i++)
        {
            newArray[i] = array[i];
        }
        return newArray;
    }
}
