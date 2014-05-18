package lzm.netty.socket;

import lzm.netty.socket.command.CommandExecuteThread;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettySocketServer {
	// 服务器监听端口
	public int port;
	// 连接空闲时间
	public int idleTimeSeconds = 60;
	// 执行命令的线程数
	public int executeCommandThreadCount = 1;

	private ServerBootstrap boot;
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;

	/**
	 * 创建服务器
	 * 
	 * @param port
	 *            监听端口
	 * */
	public NettySocketServer(int port) {
		this.port = port;
	}

	public void run() throws Exception {
		CommandExecuteThread.initExecuteCommandThread(executeCommandThreadCount);

		boot = new ServerBootstrap();
		bossGroup = new NioEventLoopGroup(0);
		workerGroup = new NioEventLoopGroup(0);
		try {
			boot.option(ChannelOption.TCP_NODELAY, true).childOption(ChannelOption.TCP_NODELAY, true);

			boot.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.handler(new LoggingHandler(LogLevel.INFO))
				.childOption(ChannelOption.TCP_NODELAY, false)
				.childHandler(new NettySocketChannelInitializer(idleTimeSeconds));

			// ChannelFuture f = b.bind(port).sync();
			// f.channel().closeFuture().sync();
			ChannelFuture f = boot.bind(port);
			f.sync();
		} catch (Exception err) {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

	}

}
