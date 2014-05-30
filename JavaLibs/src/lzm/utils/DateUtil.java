package lzm.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static String dateString(){
		return dateFormat.format(new Date());
	}
	
	
	
}
