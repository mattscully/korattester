package com.scully.korat.map;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.xml.sax.SAXException;

public class BeanXmlMapper
{
    private BeanXmlMapper()
    {
    }

    public static void writeBean(Writer writer, Object bean)
    {
        // create write and set basic properties
        BeanWriter beanWriter = new BeanWriter(writer);
//        writer.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        // reading doesn't work with pretty print for some reason???
//        beanWriter.enablePrettyPrint();
        beanWriter.getBindingConfiguration().setMapIDs(false);

        // set a custom name mapper for attributes
//        writer.getXMLIntrospector().getConfiguration().setAttributeNameMapper(new HyphenatedNameMapper());
        // set a custom name mapper for elements
//        writer.getXMLIntrospector().getConfiguration().setElementNameMapper(new DecapitalizeNameMapper());

        // write out the bean
        try
        {
            beanWriter.write(bean);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (SAXException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IntrospectionException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("");
    }
    
    public static Object readBean(Reader reader, String path, Class beanClass)
    {
        Object bean = null;
        
        // Now convert this to a bean using betwixt
        // Create BeanReader
        BeanReader beanReader  = new BeanReader();
        
        // Configure the reader
        // If you're round-tripping, make sure that the configurations are compatible!
//        beanReader.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(false);
        beanReader.getBindingConfiguration().setMapIDs(false);
        
        // Register beans so that betwixt knows what the xml is to be converted to
        // Since the element mapped to a PersonBean isn't called the same 
        // as Betwixt would have guessed, need to register the path as well
        try
        {
            beanReader.registerBeanClass(path, beanClass);
        }
        catch (IntrospectionException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        // Now we parse the xml
        try
        {
            bean = beanReader.parse(reader);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (SAXException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return bean;
    }

    public static String beanToXml(Object bean)
    {
        StringWriter writer = new StringWriter(256);
        BeanXmlMapper.writeBean(writer, bean);
        return writer.toString();
    }
    
    public static Object xmlToBean(String xml, String path, Class beanClass)
    {
        StringReader xmlReader = new StringReader(xml);
        return BeanXmlMapper.readBean(xmlReader, path, beanClass); 
    }
}
