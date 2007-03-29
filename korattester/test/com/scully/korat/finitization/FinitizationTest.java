/**
 * 
 */
package com.scully.korat.finitization;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.scully.korat.finitization.ClassDomain;
import com.scully.korat.finitization.FieldDomain;
import com.scully.korat.finitization.Finitization;
import com.scully.korat.finitization.IntSet;
import com.scully.korat.finitization.ObjField;
import com.scully.korat.finitization.ObjSet;
import com.scully.korat.test.SearchTree;


import junit.framework.TestCase;

/**
 * @author mscully
 * 
 */
public class FinitizationTest extends TestCase
{
    private Finitization finitization;

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        this.finitization = new Finitization(SearchTree.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /**
     * Test method for
     * {@link com.scully.korat.finitization.Finitization#Finitization(java.lang.Class)}.
     * Make sure the rootClass is set correctly.
     */
    public void testFinitizationRootClass()
    {
        assertSame("Conflicting root class for Finitization", SearchTree.class, this.finitization.rootClass);
    }

    /**
     * Test method for
     * {@link com.scully.korat.finitization.Finitization#Finitization(java.lang.Class)}.
     * Make sure the rootObject is set correctly.
     */
    public void testFinitizationRootObject()
    {
        assertTrue("Conflicting root object for Finitization", this.finitization.rootObject instanceof SearchTree);
    }

    /**
     * Test method for
     * {@link com.scully.korat.finitization.Finitization#Finitization(java.lang.Class)}.
     * Make sure the objFieldsByName gets populated.
     */
    public void testFinitizationObjFieldsCreated()
    {
        int expectedSize = 3;
        assertNotNull("objFields for Finitization are null", this.finitization.objFieldsByName);
        assertEquals("objFields size does not match for Finitization.", expectedSize, this.finitization.objFieldsByName
                .size());
    }

    /**
     * Test method for
     * {@link com.scully.korat.finitization.Finitization#Finitization(java.lang.Class)}.
     * Make sure the objFieldsByName gets populated correctly.
     */
    public void testFinitizationObjFieldsCorrect()
    {
        Map objFields = this.finitization.objFieldsByName;

        // test for root
        Field key;
        try
        {
            key = SearchTree.class.getDeclaredField("root");
            List objFieldList = (ArrayList) objFields.get(key);
            assertNotNull("ObjField doesn't exist.", objFieldList);
            assertEquals("ObjField doesn't exist.", 1, objFieldList.size());
            ObjField objField = (ObjField) objFieldList.get(0);
            assertTrue("ObjField accessibility incorrect.", objField.getField().isAccessible());
            assertSame("ObjField mapped to wrong object", this.finitization.getRootObject(), objField.getObject());

            // test for size
            key = SearchTree.class.getDeclaredField("size");
            objFieldList = (ArrayList) objFields.get(key);
            assertNotNull("ObjField doesn't exist.", objFieldList);
            assertEquals("ObjField doesn't exist.", 1, objFieldList.size());
            objField = (ObjField) objFieldList.get(0);
            assertTrue("ObjField accessibility incorrect.", objField.getField().isAccessible());
            assertSame("ObjField mapped to wrong object", this.finitization.getRootObject(), objField.getObject());
        }
        catch (SecurityException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (NoSuchFieldException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Test method for
     * {@link com.scully.korat.finitization.Finitization#getSimpleName(String)}.
     * 
     * sample class name: test.reflect.Node$InnerNode
     */
    public void testGetSimpleName()
    {
        String name = "test.reflect.Node$InnerNode";
        String expectedSimpleName = "InnerNode";

        String simpleName = this.finitization.getSimpleName(name);
        assertEquals("Simple name failed", expectedSimpleName, simpleName);

        name = "Node$InnerNode";
        simpleName = this.finitization.getSimpleName(name);
        assertEquals("Simple name failed", expectedSimpleName, simpleName);

        name = "test.reflect.InnerNode";
        simpleName = this.finitization.getSimpleName(name);
        assertEquals("Simple name failed", expectedSimpleName, simpleName);

        name = "InnerNode";
        simpleName = this.finitization.getSimpleName(name);
        assertEquals("Simple name failed", expectedSimpleName, simpleName);
    }

    /**
     * Test method for
     * {@link com.scully.korat.finitization.Finitization#createObjects(java.lang.String, int)}.
     * Verify that all fields get registered correctly when createObjects() is
     * called.
     */
    public void testCreateObjectsRegistersFields()
    {
        int numObjects = 3;
        Class nodeClass = SearchTree.Node.class;
        this.finitization.createObjects(nodeClass, numObjects);

        Map objFields = this.finitization.objFieldsByName;

        // test for Node.left
        List objFieldList;
        try
        {
            objFieldList = (ArrayList) objFields.get(SearchTree.Node.class.getDeclaredField("left"));
            verifyObjFieldList(objFieldList, nodeClass, numObjects);

            // test for Node.right
            objFieldList = (ArrayList) objFields.get(SearchTree.Node.class.getDeclaredField("right"));
            verifyObjFieldList(objFieldList, nodeClass, numObjects);

            // test for Node.value
            objFieldList = (ArrayList) objFields.get(SearchTree.Node.class.getDeclaredField("value"));
            verifyObjFieldList(objFieldList, nodeClass, numObjects);
        }
        catch (SecurityException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (NoSuchFieldException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param objFieldList
     * @param nodeClass
     * @param numObjects
     */
    private void verifyObjFieldList(List objFieldList, Class nodeClass, int numObjects)
    {
        assertNotNull("ObjField doesn't exist.", objFieldList);
        assertEquals("Incorrect number of ObjFields created.", numObjects, objFieldList.size());
        for (Iterator iter = objFieldList.iterator(); iter.hasNext();)
        {
            ObjField objField = (ObjField) iter.next();
            assertTrue("ObjField accessibility incorrect.", objField.getField().isAccessible());
            assertSame("ObjField is the wrong Class", nodeClass, objField.getObject().getClass());
        }
    }

    /**
     * Verify that the FieldDomain gets created correctly. Test method for
     * {@link com.scully.korat.finitization.Finitization#createObjects(java.lang.String, int)}.
     */
    public void testCreateObjectsFieldDomain()
    {
        int numObjects = 3;
        ObjSet objSet = this.finitization.createObjects(SearchTree.Node.class, numObjects);

        assertNotNull("ObjSet wasn't found.", objSet);
        ClassDomain classDomain = objSet.getClassDomainIndices()[0].domain;
        assertEquals("Incorrect number of ObjFields created.", numObjects, classDomain.objects.length);
        for (int i = 0; i < classDomain.objects.length; i++)
        {
            Object o = classDomain.objects[i];
            assertTrue("Domain object of wrong type.", o instanceof SearchTree.Node);
        }
    }

    /**
     * Test method for
     * {@link com.scully.korat.finitization.Finitization#set(java.lang.String, com.scully.korat.finitization.FinSet)}.
     * Verify the state space is constructed correctly.
     */
    public void testSet()
    {
        int numObjects = 3, expectedSpaceSize = 0, expectedDomainSize = 0;
        Map space = this.finitization.space;

        ObjSet nodes = this.finitization.createObjects(SearchTree.Node.class, numObjects);
        nodes.add(null);

        try
        {
            // test Node.right
            Field field;
            field = SearchTree.Node.class.getDeclaredField("right");
            expectedSpaceSize = 3; // 3 right fields
            expectedDomainSize = 4;
            this.finitization.set(field, nodes);
            verifyFieldDomain(space, field, expectedSpaceSize, expectedDomainSize, SearchTree.Node.class);

            // test root
            field = SearchTree.class.getDeclaredField("root");
            expectedSpaceSize += 1; // only one root field
            expectedDomainSize = 4;
            this.finitization.set(field, nodes);
            verifyFieldDomain(space, field, expectedSpaceSize, expectedDomainSize, SearchTree.Node.class);

            // test Node.right
            field = SearchTree.Node.class.getDeclaredField("left");
            expectedSpaceSize += 3; // 3 left fields
            expectedDomainSize = 4;
            this.finitization.set(field, nodes);
            verifyFieldDomain(space, field, expectedSpaceSize, expectedDomainSize, SearchTree.Node.class);

            // test Node.value
            field = SearchTree.Node.class.getDeclaredField("value");
            expectedSpaceSize += 3; // 3 value fields
            expectedDomainSize = 4;
            this.finitization.set(field, new IntSet(0, numObjects));
            verifyFieldDomain(space, field, expectedSpaceSize, expectedDomainSize, Integer.class);

            // test size
            field = SearchTree.class.getDeclaredField("size");
            expectedSpaceSize += 1; // 1 size field
            expectedDomainSize = 4;
            this.finitization.set(field, new IntSet(0, numObjects));
            verifyFieldDomain(space, field, expectedSpaceSize, expectedDomainSize, Integer.class);
        }
        catch (SecurityException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (NoSuchFieldException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param space
     * @param field
     * @param expectedSpaceSize
     * @param expectedDomainSize
     * @param type
     */
    private void verifyFieldDomain(Map space, Field field, int expectedSpaceSize, int expectedDomainSize,
            Class fieldType)
    {
        List objFieldList;
        assertEquals("State Space is incorrect size.", expectedSpaceSize, space.size());
        objFieldList = (List) this.finitization.objFieldsByName.get(field);
        for (Iterator iter = objFieldList.iterator(); iter.hasNext();)
        {
            ObjField objField = (ObjField) iter.next();
            FieldDomain fieldDomain = (FieldDomain) space.get(objField);
            assertEquals("FieldDomain incorrect size", expectedDomainSize, fieldDomain.getSize());
            for (int i = 0; i < fieldDomain.getSize(); i++)
            {
                Object o = fieldDomain.getValueAtIndex(i);
                if (o != null)
                {
                    assertSame("Object in FieldDomain incorrect type", o.getClass(), fieldType);
                }
            }
        }
    }

}
