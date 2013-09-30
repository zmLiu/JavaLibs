package com.lzm.utils;

import org.apache.log4j.Logger;

public class LogError {
	
	public static void error(Exception e){
		Logger logger = Logger.getRootLogger();
		logger.error(e.getMessage());
		StackTraceElement[] error = e.getStackTrace();
	    for (StackTraceElement stackTraceElement : error) {  
	    	logger.error(stackTraceElement.toString());
	    }
	}
}
