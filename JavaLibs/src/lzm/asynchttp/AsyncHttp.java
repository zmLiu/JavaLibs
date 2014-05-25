package lzm.asynchttp;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import lzm.utils.LogError;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.FluentStringsMap;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.providers.netty.NettyAsyncHttpProvider;

/**
 * 异步http请求
 * */
public class AsyncHttp extends Thread {

	private ConcurrentLinkedQueue<Object[]> requestList;
	private AsyncHttpClient httpClient;

	public AsyncHttp() {
		AsyncHttpClientConfig config = new AsyncHttpClientConfig.Builder().build();
		httpClient = new DefaultAsyncHttpClient(new NettyAsyncHttpProvider(config), config);
		requestList = new ConcurrentLinkedQueue<Object[]>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		Object []objects;
		int objectsLen;

		String url;
		Map<String, String> params;
		String method;
		AsyncHttpCallBack callBack;

		while(true){
			try {
				objects = requestList.poll();
				if(objects != null){
					objectsLen = objects.length;
					url = (String) objects[0];
					method = (String) objects[1];
					callBack = (AsyncHttpCallBack) objects[2];
					if(objectsLen == 3){
						if(method.equals(POST)){
							httpClient.preparePost(url).execute(new ResponseAsyncCompletionHandler(callBack));
						}else if(method.equals(GET)){
							httpClient.prepareGet(url).execute(new ResponseAsyncCompletionHandler(callBack));
						}
					}else if(objectsLen == 4){
						params = (Map<String, String>) objects[3];
						executeRequest(url, params, method, callBack);
					}
				}else{
					Thread.sleep(500L);
				}

			} catch (Exception e) {
				LogError.error(e);
			}
		}
	}


	private void executeRequest(String url,Map<String, String> params,String method,AsyncHttpCallBack callBack) throws Exception{
		FluentStringsMap map = params == null ? null : new FluentStringsMap();
		if(map != null){
			Iterator<String> it = params.keySet().iterator();
			String key;
			while(it.hasNext()){
				key = it.next();
				map.add(key,params.get(key));
			}
		}

		Request request = new RequestBuilder().setUrl(url).setQueryParameters(map).setMethod(method).build();

		httpClient.executeRequest(request, new ResponseAsyncCompletionHandler(callBack));
	}

	private void addRequest(String url,String method,AsyncHttpCallBack callBack){
		requestList.add(new Object[]{url,method,callBack});
	}

	private void addRequest(String url,Map<String, String> params,String method,AsyncHttpCallBack callBack){
		requestList.add(new Object[]{url,method,callBack,params});
	}

	private static final String GET = "GET";
	private static final String POST = "POST";

	private static AsyncHttp instance;
	private static AsyncHttp getInstance(){
		if(instance == null){
			instance = new AsyncHttp();
			instance.start();
		}
		return instance;
	}

	public static void post(String url,AsyncHttpCallBack callBack) throws Exception{
		getInstance().addRequest(url, POST, callBack);
	}

	public static void post(String url,Map<String, String> params,AsyncHttpCallBack callBack) throws Exception{
		getInstance().addRequest(url, params, POST, callBack);
	}

	public static void get(String url,AsyncHttpCallBack callBack) throws Exception{
		getInstance().addRequest(url, GET, callBack);
	}

	public static void get(String url,Map<String, String> params,AsyncHttpCallBack callBack) throws Exception{
		getInstance().addRequest(url, params, GET, callBack);
	}
}