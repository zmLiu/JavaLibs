package lzm.netty.http.service;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lzm.netty.http.handler.HttpServerHandler;

public abstract class AbstractHttpService implements IHttpService {
	
	/** 是哪一个handler受理的请求 */
	protected HttpServerHandler httpServerHandler;
	/** 用户链接 */
	protected ChannelHandlerContext ctx;
	/** request对象 */
	protected FullHttpRequest httpRequest;
	
	/** 用户访问的路径 */
	protected String path;
	/** 所有传递过来的参数 */
	protected Map<String, String> requestParams;
	/** 通过get方式传递的参数 */
	protected Map<String, String> getParams;
	/** 通过post方式传递的参数 */
	protected Map<String, String> postParams;
	
	/**
	 * 解析request
	 * @param	ctx
	 * @param	request
	 * @throws	IOException 
	 * */
	public void decoderRequest(HttpServerHandler httpServerHandler,ChannelHandlerContext ctx,FullHttpRequest httpRequest,QueryStringDecoder queryStringDecoder) throws IOException{
		this.ctx = ctx;
		this.httpRequest = httpRequest;
		
		requestParams = new HashMap<String, String>();
		path = queryStringDecoder.path();
		
		decoderQueryString(queryStringDecoder);
		
		if(httpRequest.content().isReadable()) decoderPostParams();
	}
	
	/**
	 * 解析Get Params
	 * */
	protected void decoderQueryString(QueryStringDecoder queryStringDecoder){
		Map<String, List<String>> params = queryStringDecoder.parameters();
        if (!params.isEmpty()) {
        	getParams = new HashMap<String, String>();
            for (Entry<String, List<String>> p: params.entrySet()) {
                String key = p.getKey();
                List<String> values = p.getValue();
                for (String value : values) {
                	getParams.put(key, value);
                	requestParams.put(key, value);
                }
            }
        }
	}
	
	/**
	 * 解析Post Params
	 * */
	protected void decoderPostParams() throws IOException{
		HttpPostRequestDecoder postRequestDecoder = new HttpPostRequestDecoder(httpRequest);
    	List<InterfaceHttpData> postDatas = postRequestDecoder.getBodyHttpDatas();
    	
    	if(!postDatas.isEmpty()){
    		postParams = new HashMap<String, String>();
    		
    		String key;
    		String value;
    		
    		for (InterfaceHttpData interfaceHttpData : postDatas) {
    			if(InterfaceHttpData.HttpDataType.Attribute == interfaceHttpData.getHttpDataType()){
    				Attribute attribute = (Attribute) interfaceHttpData;
    				attribute.setCharset(CharsetUtil.UTF_8);
    				key = attribute.getName();
    				value = attribute.getValue();
    				postParams.put(key, value);
    				requestParams.put(key, value);
    			}
    			/*
    			目前不实现
    			else if(InterfaceHttpData.HttpDataType.FileUpload == interfaceHttpData.getHttpDataType()){
    				System.out.println(interfaceHttpData);
    			}else if(InterfaceHttpData.HttpDataType.InternalAttribute == interfaceHttpData.getHttpDataType()){
    				System.out.println(interfaceHttpData);
    			}
    			*/
    		}
    	}
	}
	
	public void error(Throwable cause){
		if (ctx.channel().isActive()) {
            sendError(BAD_REQUEST);
        }
	}
	
	/** 向客户端发送消息 */
	public void sendResponse(String responseMsg){
		HttpServerHandler.sendResponse(responseMsg, ctx, httpRequest);
	}
	
	/** 重定向 */
	public void sendRedirect(String newUri) {
		HttpServerHandler.sendRedirect(ctx, newUri);
    }
	
	/** 向客户端发送错误代码 */
	public void sendError(HttpResponseStatus status){
		HttpServerHandler.sendError(ctx, status);
	}
	
	
	
	
}
