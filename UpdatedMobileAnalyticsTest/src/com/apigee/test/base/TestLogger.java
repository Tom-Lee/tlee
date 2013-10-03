package com.apigee.test.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestLogger {
	 
	  static {
	    try {
	        org.apache.log4j.BasicConfigurator.configure();
	    }
	    catch (Exception ex) {
	      System.err.println("Exception initializing log4j " + ex.getMessage());
	      ex.printStackTrace(System.err);
	      throw new ExceptionInInitializerError(ex);
	    }   
	  }
	  
	  /**
	   * @param clazz
	   * @return org.apache.log4j.Logger
	   */
	  public static Log getLogger(Class clazz) {
	    Log logger = (Log) LogFactory.getFactory().getInstance(clazz);

	    return logger;   
	  }
	  
	  /**
	   * @param loggerName
	   * @return org.apache.commons.logging.Log
	    */
	  public static Log getLogger(String loggerName) {
	    Log logger = (Log) LogFactory.getFactory().getInstance(loggerName);

	    return logger;   
	  }

}
