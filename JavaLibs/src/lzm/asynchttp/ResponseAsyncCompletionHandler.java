package lzm.asynchttp;
import org.apache.log4j.Logger;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.Response;


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
    		Logger logger = Logger.getRootLogger();
    		logger.error(t.getMessage());
    		StackTraceElement[] error = t.getStackTrace();
    	    for (StackTraceElement stackTraceElement : error) {  
    	    	logger.error(stackTraceElement.toString());
    	    }
    	}else{
    		this.callBack.onError(t);
    	}
    }

}
