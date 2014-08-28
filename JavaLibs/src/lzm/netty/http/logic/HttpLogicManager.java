package lzm.netty.http.logic;

import lzm.executor.logic.LogicExecutorSystem;
import lzm.netty.http.config.HttpServerConfig;

/** 
 * @author zmliu
 *
 * @Create Date : 2014年8月28日 上午11:43:38 
 * 
 * @des http逻辑执行管理类
 */
public class HttpLogicManager {
	
	private static LogicExecutorSystem executorSystem;
	
	public static void execute(HttpLogicExecutor executor) throws InterruptedException{
		if(executorSystem == null){
			executorSystem = new LogicExecutorSystem(HttpServerConfig.logic_execute_thread);
		}
		executorSystem.addLogic(executor);
	}
	
}
