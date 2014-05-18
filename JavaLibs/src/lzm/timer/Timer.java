package lzm.timer;

public class Timer {
	
	/**
	 * 调度延迟(秒)
	 * */
	private int delay;
	public int currentDelay;
	
	/**
	 * 循环次数
	 * */
	private int repeatCount;
	public int currentRepeatCount;
	
	private boolean isStart;
	
	/**
	 * @param	delay	多长时间调度一次
	 * @param	repeatCount	重复次数
	 * */
	public Timer(int delay,int repeatCount) {
		this.delay = delay;
		this.repeatCount = repeatCount;
		this.currentDelay = 0;
		this.currentRepeatCount = 0;
	}
	
	public void onTimer(){
		
	}
	
	public void onTimerOver(){
		
	}

	public void start(){
		isStart = true;
	}
	
	public void stop(){
		isStart = false;
	}
	
	public int getRepeatCount() {
		return repeatCount;
	}

	public int getDelay() {
		return delay;
	}

	public boolean isStart() {
		return isStart;
	}
	
}
