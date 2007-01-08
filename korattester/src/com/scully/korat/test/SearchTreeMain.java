/*
 * Created on May 4, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.scully.korat.test;

import java.io.File;
import java.io.IOException;

import com.scully.korat.finitization.Serializer;
import com.scully.korat.test.SearchTree.Node;


/**
 * @author mscully
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SearchTreeMain
{

    public static void main(String[] args)
    {
        testManual();
//        testSerialized();
        
    }
    
    public static void testSerialized()
    {
	    File dir = new File("c:/ut/korat-cv");
	    File[] cvObjects = dir.listFiles();
	    for (int i = 0; i < cvObjects.length; i++)
        {
            File file = cvObjects[i];
            SearchTree searchTree = null;
            try
            {
                searchTree = (SearchTree) Serializer.load(file);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
            searchTree.remove(1);
        }
    }

    public static void testManual()
    {
        SearchTree st = new SearchTree();
        Node n1 = new SearchTree.Node();
        Node n2 = new SearchTree.Node();
        st.root = n1;
        st.size = 2;
        n1.value = 1;
        n1.right = n2;
        n2.value = 2;
        
        st.remove(1);
    }
}
