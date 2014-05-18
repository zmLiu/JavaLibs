package lzm.utils;

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
	
	public static void throwable(Throwable t){
		Logger logger = Logger.getRootLogger();
		logger.error(t.getMessage());
		StackTraceElement[] error = t.getStackTrace();
	    for (StackTraceElement stackTraceElement : error) {  
	    	logger.error(stackTraceElement.toString());
	    }
	}
}
