package lzm.netty.socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lzm.netty.socket.config.SocketServerConfig;

public class SocketServer {
	
	public void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup(SocketServerConfig.bossGroupThreads);
		EventLoopGroup workerGroup = new NioEventLoopGroup(SocketServerConfig.workerGroupThreads);
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.option(ChannelOption.SO_TIMEOUT, SocketServerConfig.timeOut)
				.childHandler(new SocketChannelInitializer())
				.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			if(SocketServerConfig.log) b.handler(new LoggingHandler(LogLevel.INFO));

			ChannelFuture f;
			if(SocketServerConfig.host.equals("*")){
				f = b.bind(SocketServerConfig.port).sync();
			}else{
				f = b.bind(SocketServerConfig.host,SocketServerConfig.port).sync();
			}
			f.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

	}

}
