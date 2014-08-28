package lzm.netty.http.logic;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lzm.executor.logic.ILogic;
import lzm.netty.http.handler.HttpServerHandler;

/** 
 * @author zmliu
 *
 * @Create Date : 2014年8月28日 上午11:06:34 
 * 
 * @des http逻辑执行
 */
public class HttpLogicExecutor implements ILogic {
	
	private FullHttpRequest request;
	private ChannelHandlerContext ctx;
	private HttpServerHandler httpServerHandler;
	
	public HttpLogicExecutor(HttpServerHandler httpServerHandler,ChannelHandlerContext ctx,FullHttpRequest request) {
		this.httpServerHandler = httpServerHandler;
		this.ctx = ctx;
		this.request = request;
	}
	
	@Override
	public void execute() throws Exception {
		httpServerHandler.execute(ctx, request);
	}
	
	public HttpServerHandler getHttpServerHandler() {
		return httpServerHandler;
	}
	public void setHttpServerHandler(HttpServerHandler httpServerHandler) {
		this.httpServerHandler = httpServerHandler;
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}
	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	public FullHttpRequest getRequest() {
		return request;
	}
	public void setRequest(FullHttpRequest request) {
		this.request = request;
	}

}
