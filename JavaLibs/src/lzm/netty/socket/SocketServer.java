package lzm.netty.socket;

import lzm.netty.socket.command.CommandExecuteThread;
import lzm.netty.socket.config.SocketServerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class SocketServer {
	
	private ServerBootstrap boot;
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	
	private SocketServerConfig config;

	/**
	 * 创建服务器
	 * 
	 * @param port
	 *            监听端口
	 * */
	public SocketServer(SocketServerConfig config) {
		this.config = config;
	}

	public void run() throws Exception {
		CommandExecuteThread.initExecuteCommandThread(config.executeCommandThreads);

		boot = new ServerBootstrap();
		bossGroup = new NioEventLoopGroup(config.bossGroupThreads);
		workerGroup = new NioEventLoopGroup(config.workerGroupThreads);
		try {
			boot.option(ChannelOption.TCP_NODELAY, true).childOption(ChannelOption.TCP_NODELAY, true);

			boot.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.handler(new LoggingHandler(LogLevel.INFO))
				.childOption(ChannelOption.TCP_NODELAY, false)
				.childHandler(new SocketChannelInitializer(config.idleTimeSeconds));

			ChannelFuture f = boot.bind(config.port).sync();
			f.channel().closeFuture().sync();
		} catch (Exception err) {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

	}

}
