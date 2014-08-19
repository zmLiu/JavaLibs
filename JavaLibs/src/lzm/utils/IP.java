package lzm.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IP {

	private static String ip = null;

	public static String getLocalIP() throws UnknownHostException {
		if(ip == null){
			InetAddress addr = InetAddress.getLocalHost();
			ip = addr.getHostAddress().toString();// 获得本机IP
			// String address=addr.getHostName().toString();//获得本机名称
		}
		return ip;
	}

}
