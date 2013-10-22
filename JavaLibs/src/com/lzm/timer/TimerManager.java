package com.lzm.timer;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import com.lzm.utils.LogError;

public class TimerManager extends Thread {
	
	private static ConcurrentHashMap<Timer, Integer> timerMap = new ConcurrentHashMap<Timer, Integer>();
	private static TimerManager instance;
	
	public static void addTimer(Timer timer,boolean isStart){
		timerMap.put(timer, 0);
		
		if(isStart) timer.start();
		
		if(instance == null){
			instance = new TimerManager();
			instance.start();
		}
	}
	
	public static void removeTimer(Timer timer){
		timerMap.remove(timer);
	}
	
	@Override
	public void run() {
		
		while (true) {
			try {
				Iterator<Timer> it = timerMap.keySet().iterator();
				Timer timer;
				while (it.hasNext()) {
					timer = it.next();
					
					if(!timer.isStart()) continue;
					
					timer.currentDelay += 1;
					if(timer.currentDelay == timer.getDelay()){
						timer.currentDelay = 0;
						timer.onTimer();
						timer.currentRepeatCount += 1;
						if(timer.currentRepeatCount == timer.getRepeatCount()){
							timer.onTimerOver();
							TimerManager.removeTimer(timer);
						}
					}
				}
				
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
