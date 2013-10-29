package com.lzm.asynchttp;

public interface HttpCallBack {
	void onComplete(String data);
	
	void onError(Throwable t);
}
