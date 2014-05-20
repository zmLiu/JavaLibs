package lzm.netty.http.service;

public interface IHttpService {
	
	void index() throws Exception;
	
	void error(Throwable cause);
	
}
