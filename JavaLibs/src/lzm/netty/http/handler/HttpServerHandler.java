package lzm.netty.http.handler;

import static io.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.Names.LOCATION;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
import lzm.netty.http.config.HttpServerConfig;
import lzm.netty.http.service.AbstractHttpService;
import lzm.netty.http.service.HttpServiceManager;
import lzm.netty.http.service.StaticFileService;

import org.apache.log4j.Logger;

public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	
	private AbstractHttpService httpService;
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request)throws Exception {
		if (is100ContinueExpected(request)) {
            send100Continue(ctx);
        }
		
		QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
		httpService = HttpServiceManager.getService(queryStringDecoder.path());
		if(httpService == null){
			httpService = new StaticFileService();
		}
		httpService.decoderRequest(this, ctx, request, queryStringDecoder);
		httpService.index();
	}
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	Logger logger = Logger.getRootLogger();
		logger.error(cause.getMessage());
		StackTraceElement[] error = cause.getStackTrace();
	    for (StackTraceElement stackTraceElement : error) {  
	    	logger.error(stackTraceElement.toString());
	    }
	    
	    if(httpService != null) httpService.error(cause);
	    
        ctx.close();
    }
	
	private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpServerConfig.http_version, CONTINUE);
        ctx.write(response);
    }

	/** 向客户端发送消息 */
	public static void sendResponse(String responseMsg,ChannelHandlerContext ctx,FullHttpRequest httpRequest){
		
		boolean keepAlive = isKeepAlive(httpRequest);
		
		FullHttpResponse response = new DefaultFullHttpResponse(HttpServerConfig.http_version, httpRequest.getDecoderResult().isSuccess()? OK : BAD_REQUEST,Unpooled.copiedBuffer(responseMsg, CharsetUtil.UTF_8));
		
		response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
		
		if (keepAlive) {
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
		
		ctx.writeAndFlush(response);
	}
	
	/** 重定向 */
	public static void sendRedirect(ChannelHandlerContext ctx, String newUri) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpServerConfig.http_version, FOUND);
        response.headers().set(LOCATION, newUri);

        // Close the connection as soon as the error message is sent.
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
	
	/** 向客户端发送错误代码 */
	public static void sendError(ChannelHandlerContext ctx,HttpResponseStatus status){
		
		FullHttpResponse response = new DefaultFullHttpResponse(HttpServerConfig.http_version, status, Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
		
		response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

}
