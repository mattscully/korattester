/**
 * 
 */
package com.scully.korat;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.math.RandomUtils;

import com.scully.korat.CandidateState;
import com.scully.korat.finitization.FieldDomain;
import com.scully.korat.finitization.Finitization;
import com.scully.korat.finitization.ObjField;


/**
 * @author scully
 * 
 */
public class CandidateStateTest extends KoratBaseTest
{
    CandidateState candidateState;

    protected void setUp() throws Exception
    {
        super.setUp();
        this.candidateState = this.korat.getCandidateState();
    }

    /**
     * Test method for {@link com.scully.korat.CandidateState#initStateValueIndexes()}.
     */
    public void testInitStateValueIndexes()
    {
        // assert correct size
        assertEquals("Wrong number of fields in map.", 12, this.candidateState.getStateValueIndexes().size());

        // assert correct values
        Collection values = this.candidateState.getStateValueIndexes().values();
        Integer zero = new Integer(0);
        for (Iterator iter = values.iterator(); iter.hasNext();)
        {
            Integer index = (Integer) iter.next();
            assertEquals("Field value index not set to zero.", zero, index);
        }
    }

    // /**
    // * Test method for
    // * {@link com.scully.korat.CandidateState#getClassDomainForField(com.scully.korat.finitization.ObjField)}.
    // */
    // public void testGetClassDomainForField()
    // {
    // fail("Not yet implemented");
    // }
    //
    // /**
    // * Test method for
    // * {@link com.scully.korat.CandidateState#hasEqualDomainsForFields(com.scully.korat.finitization.ObjField,
    // com.scully.korat.finitization.ObjField)}.
    // */
    // public void testHasEqualDomainsForFields()
    // {
    // fail("Not yet implemented");
    // }
    //
    // /**
    // * Test method for {@link com.scully.korat.CandidateState#getValueIndex(com.scully.korat.finitization.ObjField)}.
    // */
    // public void testGetValueIndex()
    // {
    // fail("Not yet implemented");
    // }

    /**
     * Test method for
     * {@link com.scully.korat.CandidateState#setValueAtIndex(com.scully.korat.finitization.ObjField, int)}.
     */
    public void testSetValueAtIndexObjFieldInt()
    {
        Map stateValueIndexes = new LinkedHashMap();
        // set random indexes
        // indexes from 0 to 2 will be valid for all fields (since 'value' only
        // has 3)
        // indexes for booleans must be 0 or 1
        ObjField field = null;
        int index = 0;
        for (int i = 0; i < this.candidateState.stateFields.length; i++)
        {
            field = this.candidateState.stateFields[i];
            if(boolean.class.equals(field.getField().getType()))
            {
	            index = RandomUtils.nextInt(2);
            }
            else
            {
	            index = RandomUtils.nextInt(3);
            }
            // store indexes for later comparison
            stateValueIndexes.put(field, new Integer(index));
            // call the method
            this.candidateState.setValueAtIndex(field, index);
        }

        // verify fields set properly
        for (int i = 0; i < this.candidateState.stateFields.length; i++)
        {
            field = this.candidateState.stateFields[i];
            FieldDomain fieldDomain = (FieldDomain) this.candidateState.stateSpace.get(field);
            Object indexValue = fieldDomain.getValueAtIndex((Integer) stateValueIndexes.get(field));
            assertEquals("Field value set to wrong index.", indexValue, field.getFieldValue());
        }
    }

    // /**
    // * Test method for
    // * {@link com.scully.korat.CandidateState#hasMoreValuesInFieldDomain(com.scully.korat.finitization.ObjField)}.
    // */
    // public void testHasMoreValuesInFieldDomain()
    // {
    // fail("Not yet implemented");
    // }
    //
    // /**
    // * Test method for
    // * {@link com.scully.korat.CandidateState#hasMoreValuesInClassDomain(com.scully.korat.finitization.ObjField)}.
    // */
    // public void testHasMoreValuesInClassDomain()
    // {
    // fail("Not yet implemented");
    // }
    //
//    /**
//     * Assert maps are equal but keys are different objects.
//     * 
//     * Test method for {@link com.scully.korat.CandidateState#copyOfValueIndexes()}.
//     */
//    public void testCopyOfValueIndexes()
//    {
//        Map stateValueIndexes = null;
//        ObjField field1 = null;
//        ObjField field2 = null;
//        int index = 0;
//        
//        // set random indexes
//        // indexes from 0 to 2 will be valid for all fields (since 'value' only
//        // has 3)
//        for (int i = 0; i < this.candidateState.stateFields.length; i++)
//        {
//            field1 = this.candidateState.stateFields[i];
//            index = RandomUtils.nextInt(3);
//            // call the method
//            this.candidateState.setValueAtIndex(field1, index);
//        }
//        
//        // call method to test
//        stateValueIndexes = this.candidateState.copyOfValueIndexes();
//        
//        // We will assert that the maps are equal, but that the ObjField objects
//        // are different objects.
//        assertTrue("Maps are not equal.", stateValueIndexes.equals(this.candidateState.stateValueIndexes));
//        
//        // verify fields set properly
//        Iterator orig = null;
//        Iterator copy = null;
//        for (orig = this.candidateState.stateValueIndexes.keySet().iterator(),
//             copy = stateValueIndexes.keySet().iterator(); orig.hasNext();)
//        {
//            field1 = (ObjField) orig.next();
//            field2 = (ObjField) copy.next();
//            assertTrue("ObjField's not equal.", field1.equals(field2));
//            assertFalse("ObjField's are identical.", field1 == field2);
//        }
//    }
}
