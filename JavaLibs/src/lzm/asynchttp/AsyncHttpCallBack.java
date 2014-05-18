package lzm.asynchttp;

public interface AsyncHttpCallBack {
	void onComplete(String data);
	
	void onError(Throwable t);
}
