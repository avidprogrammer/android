package com.example.voicealarm;

import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmRx extends BroadcastReceiver {
	private WakeLk lock = null;
	private Dbase db;

	@Override
	public void onReceive(Context c, Intent i) {
		lock = WakeLk.getInstance();
		db = Dbase.getInstance();
		db.init(consts.SET_FILE);
		
		String playFile = i.getStringExtra(consts.AUD_FILE);
		AlarmDoc thisDoc = db.getRecordByAudFl(playFile);
		if (thisDoc == null)
			return;
		
		int alarmDays = thisDoc.getDaysBmsk(); 
		Calendar todayCal = Calendar.getInstance();
		int today = todayCal.get(Calendar.DAY_OF_WEEK) - 1;

		// Check if the alarm is required to go off today
		if ((isTodayAlarmDay(today, alarmDays) == 0) &&
			(thisDoc.getOneShot() == false))
			return;

		// Set one shot false after first invocation
		thisDoc.setOneShot(false);
		db.setRecord(thisDoc);
		db.commit();
		
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
		Log.d("DBG", "today : " + Integer.toHexString(1<<today) +
					 " bmsk :" + Integer.toHexString(alarmBmsk) +
					 " res : " + Integer.toString(res));
		return res;
	}
}
