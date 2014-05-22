package lzm.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import lzm.netty.http.handler.HttpServerHandler;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
//		pipeline.addLast("readTimeoutHandler", new ReadTimeoutHandler(HttpServerConfig.time_out));
//     	pipeline.addLast("writeTimeoutHandler", new WriteTimeoutHandler(HttpServerConfig.time_out));
		
		pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));//用于将解析出来的数据封装成http对象，httprequest什么的
        pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
        
        pipeline.addLast("handler", new HttpServerHandler());
	}

}
