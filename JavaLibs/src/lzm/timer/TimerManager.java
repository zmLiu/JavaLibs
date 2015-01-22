package lzm.timer;

import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import lzm.timer.Timer.TIMER_TYPE;
import lzm.utils.LogError;

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
				
				Calendar now;
				boolean isExecute;
				int executeTimeInts[] = new int[]{Calendar.YEAR,Calendar.MONTH,Calendar.DATE,Calendar.HOUR_OF_DAY,Calendar.MINUTE,Calendar.SECOND,Calendar.DAY_OF_WEEK};
				
				while (it.hasNext()) {
					timer = it.next();
					
					if(!timer.isStart()) continue;
					
					if(timer.getType() == TIMER_TYPE.Countdown){
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
					}else if(timer.getType() == TIMER_TYPE.Timing){
						now = Calendar.getInstance();
						isExecute = true;
						for (int i = 0; i < 7; i++) {
							if(!timer.executeDate[i].equals("*") && Integer.valueOf(timer.executeDate[i]) != now.get(executeTimeInts[i])){
								isExecute = false;
								continue;
							}
						}
						if(isExecute){
							try {timer.onTimer();} catch (Exception e) {LogError.error(e);}
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
