package lzm.netty.http.disruptor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lzm.netty.http.handler.HttpServerHandler;

public class ServiceEvent {
	private FullHttpRequest request;
	private ChannelHandlerContext ctx;
	private HttpServerHandler httpServerHandler;
	
	
	public void execute() throws Exception {
		httpServerHandler.execute(ctx, request);
	}
	
	public FullHttpRequest getRequest() {
		return request;
	}
	public void setRequest(FullHttpRequest request) {
		this.request = request;
	}
	public ChannelHandlerContext getCtx() {
		return ctx;
	}
	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}
	public HttpServerHandler getHttpServerHandler() {
		return httpServerHandler;
	}
	public void setHttpServerHandler(HttpServerHandler httpServerHandler) {
		this.httpServerHandler = httpServerHandler;
	}
	
	
	
}
