/*
 * Created on May 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.scully.korat.finitization;

/**
 * @author mscully
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IFinitizationMethod
{
	public Finitization getFinitization();
	
	public String getSerializePath();
	
	public Class[] getParamTypes();
	
	public Object[] getParamValues();
	
	public String getTestMethod();
    
}
