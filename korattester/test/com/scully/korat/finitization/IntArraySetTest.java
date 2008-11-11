package com.scully.korat.finitization;

import org.junit.Test;
import static org.junit.Assert.*;

public class IntArraySetTest
{

    @Test
    public void testIntArraySetSize8()
    {
        IntArraySet arraySet = new IntArraySet(0, 1, 3);
        assertEquals(8, arraySet.getClassDomainIndices().length);
    }

    @Test
    public void testIntArraySetSize27()
    {
        IntArraySet arraySet = new IntArraySet(0, 2, 3);
        assertEquals(27, arraySet.getClassDomainIndices().length);
    }

    @Test
    public void testIntArraySetSize64()
    {
        IntArraySet arraySet = new IntArraySet(0, 3, 3);
        assertEquals(64, arraySet.getClassDomainIndices().length);
    }

    @Test
    public void testIntArraySetSize81()
    {
        IntArraySet arraySet = new IntArraySet(0, 2, 4);
        assertEquals(81, arraySet.getClassDomainIndices().length);
    }

}
