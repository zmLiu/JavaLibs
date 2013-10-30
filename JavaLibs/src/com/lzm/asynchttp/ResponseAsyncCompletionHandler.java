package com.lzm.asynchttp;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.Response;

import com.lzm.utils.LogError;


public class ResponseAsyncCompletionHandler extends AsyncCompletionHandler<Response> {
	
	private AsyncHttpCallBack callBack;
	
	public ResponseAsyncCompletionHandler(AsyncHttpCallBack callBack) {
		this.callBack = callBack;
	}

	@Override
    public Response onCompleted(Response response) throws Exception{
		if(this.callBack != null) 
			this.callBack.onComplete(response.getResponseBody("utf-8"));
        return response;
    }

    @Override
    public void onThrowable(Throwable t){
    	if(this.callBack == null){
    		LogError.throwable(t);
    	}else{
    		this.callBack.onError(t);
    	}
    }

}
