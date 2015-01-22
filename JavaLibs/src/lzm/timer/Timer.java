package lzm.timer;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Timer {
	
	public final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	enum TIMER_TYPE{
		/**
		 * 指定时间执行
		 * */
		Timing,
		/**
		 * 倒计时
		 * */
		Countdown
	}
	
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
	
	/**
	 * 倒计时类型
	 * */
	private TIMER_TYPE type;
	/**
	 * 是否已经开始
	 * */
	private boolean isStart;
	
	/**
	 * 执行日期
	 * */
	public String[] executeDate;
	
	/**
	 * 创建一个倒计时
	 * @param	delay	多长时间调度一次
	 * @param	repeatCount	重复次数
	 * */
	public Timer(int delay,int repeatCount) {
		this.delay = delay;
		this.repeatCount = repeatCount;
		this.currentDelay = 0;
		this.currentRepeatCount = 0;
		this.type = TIMER_TYPE.Countdown;
	}
	
	/**
	 * 创建一个指定时间执行的任务
	 * @param date 执行日期 -> 年 月 日 时 分 秒 星期(* * * * * * *)
	 * @throws ParseException 
	 * */
	public Timer(String date) {
		this.executeDate = new String[]{"*","*","*","*","*","*","*"};
		this.parseTime(date);
		this.type = TIMER_TYPE.Timing;
	}
	
	private void parseTime(String date){
		String tmpStr[] = date.split(" ");
		int len = tmpStr.length;
		String value;
		for (int i = 0; i < len; i++) {
			value = tmpStr[i];
			if(i == 1 && !value.equals("*")){
				executeDate[i] = (Integer.valueOf(tmpStr[i]) - 1) + "";
			}else if(i == 6 && !value.equals("*")){
				int intValue = Integer.valueOf(value);
				intValue = intValue == 7 ? 1 : intValue + 1;
				executeDate[i] = intValue + "";
			}else{
				executeDate[i] = value;
			}
		}
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

	public TIMER_TYPE getType() {
		return type;
	}
	
	
	
}
