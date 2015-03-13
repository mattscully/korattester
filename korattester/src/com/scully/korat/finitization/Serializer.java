/*
 * Created on May 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.scully.korat.finitization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author mscully
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Serializer
{

    /**
     * Serialize the object to a file
     * @param o object to be serialized
     * @param f file to write
     * @throws IOException
     */
    public static void store(Serializable o, File f) throws IOException
    {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
        out.writeObject(o);
        out.close();
    }
    
    /**
     * Deserialize an object from a file
     * @param f File of the serialized object
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object load(File f) throws IOException, ClassNotFoundException
    {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
        return in.readObject();
    }
}
