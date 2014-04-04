package com.example.voicealarm;

import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmRx extends BroadcastReceiver {
	private WakeLk lock = null;

	@Override
	public void onReceive(Context c, Intent i) {
		lock = WakeLk.getInstance();

		String playFile = i.getStringExtra(consts.AUD_FILE);
		int alarmDays = i.getIntExtra(consts.DTW, 0);
		Calendar todayCal = Calendar.getInstance();
		int today = todayCal.get(Calendar.DAY_OF_WEEK) - 1;

		// Check if the alarm is required to go off today
		if (isTodayAlarmDay(today, alarmDays) == 0)
			return;

		lock.acquireLock(c);
		Intent alrmIntent = new Intent(c, AlarmAlert.class);
		alrmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		alrmIntent.putExtra(consts.AUD_FILE, playFile);
		
		c.startActivity(alrmIntent);
	}

	public int isTodayAlarmDay(int today, int alarmBmsk) {
		// OR the day of the week with the alarm bit mask to determine
		//  if alarm needs to go off
		int res = ((1 << today) & alarmBmsk);
		Log.d("DBG", "today : " + Integer.toString(today) +
					 " bmsk :" + Integer.toHexString(alarmBmsk) +
					 " res : " + Integer.toString(res));
		return res;
	}
}
