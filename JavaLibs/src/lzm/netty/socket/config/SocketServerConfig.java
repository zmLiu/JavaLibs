package lzm.netty.socket.config;

public class SocketServerConfig {
	
	public int port = 8888;//服务器监听端口
	
	public int idleTimeSeconds = 60;//连接空闲时间
	
	public int executeCommandThreads = 2;//执行命令的线程数
	
	public int bossGroupThreads = 0;//bossGroup的线程数
	
	public int workerGroupThreads = 0;//workerGroup的线程数
	
}
