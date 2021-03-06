package lzm.netty.socket.config;

public class SocketServerConfig {
	
	public static String host = "*";//访问地址
	
	public static int port = 8888;//服务器监听端口
	
	public static int idleTimeSeconds = 60;//连接空闲时间
	
	public static int bossGroupThreads = 0;//bossGroup的线程数
	
	public static int workerGroupThreads = 0;//workerGroup的线程数
	
	public static boolean log = false;//是否输出log信息
	
	public static int timeOut = 30;//socket超时时间
	
	public static int disruptor_buff_size = 4096;
	
}
