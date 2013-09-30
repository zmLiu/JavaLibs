package com.lzm.utils;

import org.apache.log4j.Logger;

public class LogError {
	
	@SuppressWarnings("rawtypes")
	public static void error(Class clazz,Exception e){
		Logger logger = Logger.getLogger(clazz);
		logger.error(e.getMessage());
		StackTraceElement[] error = e.getStackTrace();
	    for (StackTraceElement stackTraceElement : error) {  
	    	logger.error(stackTraceElement.toString());
	    }
	}
}
