package lzm.timer;

import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import lzm.timer.Timer.TIMER_TYPE;
import lzm.utils.LogError;

public class TimerManager extends Thread {
	
	//倒计时的timer
	private static ConcurrentHashMap<Timer, Integer> countdownTimerMap = new ConcurrentHashMap<Timer, Integer>();
	//定时执行的timer
	private static ConcurrentHashMap<Timer, Integer> timeingTimerMap = new ConcurrentHashMap<Timer, Integer>();
	
	private static TimerManager instance;
	
	public static void addTimer(Timer timer,boolean isStart){
		
		if(timer.getType() == TIMER_TYPE.Countdown){
			countdownTimerMap.put(timer, 0);
		}else if(timer.getType() == TIMER_TYPE.Timing){
			timeingTimerMap.put(timer, 0);
		}
		
		if(isStart) timer.start();
		
		if(instance == null){
			instance = new TimerManager();
			instance.start();
		}
	}
	
	public static void removeTimer(Timer timer){
		countdownTimerMap.remove(timer);
		timeingTimerMap.remove(timer);
	}
	
	/**
	 * 执行定时任务
	 * */
	private void executeTimeing(){
		Iterator<Timer> it = timeingTimerMap.keySet().iterator();
		Timer timer;
		
		Calendar now = Calendar.getInstance();
		boolean isExecute;
		int executeTimeInts[] = new int[]{Calendar.YEAR,Calendar.MONTH,Calendar.DATE,Calendar.HOUR_OF_DAY,Calendar.MINUTE,Calendar.SECOND,Calendar.DAY_OF_WEEK};
		int timeValues[] = new int[7];
		for (int i = 0; i < 7; i++) {
			timeValues[i] = now.get(executeTimeInts[i]);
		}
		
		while (it.hasNext()) {
			timer = it.next();
			
			if(!timer.isStart()) continue;
			
			isExecute = true;
			for (int i = 0; i < 7; i++) {
				if(!timer.executeDate[i].equals("*") && Integer.valueOf(timer.executeDate[i]) != timeValues[i]){
					isExecute = false;
					continue;
				}
			}
			if(isExecute){
				try {timer.onTimer();} catch (Exception e) {LogError.error(e);}
			}
		}
	}
	
	/**
	 * 执行倒计时任务
	 * */
	private void executeCountDown(){
		Iterator<Timer> it = countdownTimerMap.keySet().iterator();
		Timer timer;
		
		while (it.hasNext()) {
			timer = it.next();
			
			if(!timer.isStart()) continue;
			
			timer.currentDelay += 1;
			if(timer.currentDelay == timer.getDelay()){
				timer.currentDelay = 0;
				try {timer.onTimer();} catch (Exception e) {LogError.error(e);}
				if(timer.getRepeatCount() > 0){
					timer.currentRepeatCount += 1;
					if(timer.currentRepeatCount == timer.getRepeatCount()){
						try {timer.onTimerOver();} catch (Exception e) {LogError.error(e);}
						TimerManager.removeTimer(timer);
					}
				}
			}
		}
	}
	
	@Override
	public void run() {
		
		while (true) {
			try {
				
				executeTimeing();
				
				executeCountDown();
				
				Thread.sleep(1000L);
			} catch (Exception e) {
				try {
					Thread.sleep(1000L);
				} catch (Exception e1) {
					LogError.error(e1);
				}
				LogError.error(e);
			}
		}
		
	}
}
