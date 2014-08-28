package lzm.executor.logic;


/** 
 * @author zmliu
 *
 * @Create Date : 2014年8月27日 上午3:41:51 
 * 
 * @des 多线程逻辑执行系统
 */
public class LogicExecutorSystem {
	
	//当前适用的哪一个线程
	private int threadUseIndex;
	
	//线程数量
	protected int threadCount;
	//线程数组
	protected LogicExecutorThread executorThreads[];
	
	public LogicExecutorSystem(int threadCount) {
		this.threadCount = threadCount <= 0 ? (Runtime.getRuntime().availableProcessors() * 2) : threadCount;
		this.executorThreads = new LogicExecutorThread[this.threadCount];
		this.threadUseIndex = 0;
		for (int i = 0; i < this.threadCount; i++) {
			this.executorThreads[i] = new LogicExecutorThread(i);
			this.executorThreads[i].start();
		}
	}
	
	//增加一个逻辑
	public void addLogic(ILogic iLogic) throws InterruptedException{
		getExecutorThread(null).addLogic(iLogic);
	}
	
	//获取合适的逻辑执行线程
	protected LogicExecutorThread getExecutorThread(Object []args){
		LogicExecutorThread executorThread = this.executorThreads[threadUseIndex];
		this.threadUseIndex = ((this.threadUseIndex + 1) == this.threadCount) ? 0 : (this.threadUseIndex + 1);
		return executorThread;
	}
	
}
