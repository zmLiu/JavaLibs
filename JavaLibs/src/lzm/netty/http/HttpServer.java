package lzm.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lzm.netty.http.config.HttpServerConfig;
import lzm.netty.http.service.HttpServiceManager;
import lzm.netty.http.test.TestService;

public class HttpServer {
	
	public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(HttpServerConfig.bossGroupThreads);
        EventLoopGroup workerGroup = new NioEventLoopGroup(HttpServerConfig.workerGroupThreads);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .option(ChannelOption.TCP_NODELAY, true)
             .option(ChannelOption.SO_TIMEOUT, HttpServerConfig.time_out)
             .childHandler(new HttpServerInitializer())
             .childOption(ChannelOption.SO_KEEPALIVE, true);
            
            if(HttpServerConfig.log) b.handler(new LoggingHandler(LogLevel.INFO));
            
            ChannelFuture f;
            if(HttpServerConfig.host.equals("*")){
            	f = b.bind(HttpServerConfig.port).sync();
            }else{
            	f = b.bind(HttpServerConfig.host,HttpServerConfig.port).sync();
            }
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
	
	public static void main(String[] args) throws Exception {
		HttpServerConfig.port = 9001;
		HttpServerConfig.server_path = "./";
//		HttpServerConfig.log = false;
		
		HttpServiceManager.registerService("test", new TestService());
		HttpServiceManager.registerService("test1", "lzm.netty.http.test.Test1Service");
		
		new HttpServer().run();
	}
	
}
