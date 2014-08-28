package lzm.netty.http.config;

import io.netty.handler.codec.http.HttpVersion;

import java.util.Locale;

public class HttpServerConfig {
	public static int port = 80;// 服务器监听端口

	public static int bossGroupThreads = 0;// bossGroup的线程数

	public static int workerGroupThreads = 0;// workerGroup的线程数
	
	public static String server_path = "./";//服务器路径
	
	public static Locale locale = Locale.US;
	
	public static String http_date_format = "EEE, dd MMM yyyy HH:mm:ss zzz";
	
	public static String time_zone = "PRC";
	
	public static int http_cache_seconds = 3600;
	
	public static HttpVersion http_version = HttpVersion.HTTP_1_1;
	
	public static int time_out = 30;
	
	public static boolean send_file = true;//是否可以请求文件
	
	public static boolean send_file_list = true;//是否可以请求文件列表
	
	public static String index_file = "index.html";//默认页
	
	public static boolean log = false;//是否输出log信息
	
	public static int logic_execute_thread = 0;
}
