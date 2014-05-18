package lzm.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * 加载properties配置文件
 * */
public class PropertiesLoader {

	public static Properties loadProperties(String path) throws Exception {
		InputStream in = new BufferedInputStream(new FileInputStream(path));

		Properties pro = new Properties();
		pro.load(in);
		
		return pro;
	}

}
