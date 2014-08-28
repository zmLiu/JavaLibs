package lzm.executor.logic;

import java.util.concurrent.LinkedBlockingQueue;

import lzm.utils.LogError;

/** 
 * @author zmliu
 *
 * @Create Date : 2014年8月27日 上午3:46:06 
 * 
 * @des 逻辑执行线程
 * 
 */
public class LogicExecutorThread extends Thread {
	
	//等待执行逻辑的数量
	private int logicCount;
	private LinkedBlockingQueue<ILogic> logics;
	
	private int index;
	
	public LogicExecutorThread(int index) {
		this.logicCount = 0;
		this.logics = new LinkedBlockingQueue<ILogic>();
		this.index = index;
	}
	
	public int getIndex(){
		return this.index;
	}
	
	public void run() {
		ILogic iLogic;
		while(true){
			try {
				if(logicCount == 0){
					await();
				}
				iLogic = logics.poll();
				logicCount = logics.size();
				if(iLogic != null) iLogic.execute();
			} catch (Exception e) {
				LogError.throwable(e.getCause());
			}
		}
	}
	
	public void addLogic(ILogic iLogic) throws InterruptedException{
		this.logics.add(iLogic);
		this.logicCount = this.logics.size();
		if(this.logicCount == 1){
			assign();
		}
	}
	
	private synchronized void await() throws InterruptedException{
		this.wait();
	}
	
	private synchronized void assign() throws InterruptedException{
		this.notify();
	}
	
}
