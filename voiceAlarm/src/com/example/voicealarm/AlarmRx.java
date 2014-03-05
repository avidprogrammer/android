package com.example.voicealarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmRx extends BroadcastReceiver {
	private WakeLk lock = null;

	@Override
	public void onReceive(Context c, Intent i) {
		lock = WakeLk.getInstance();
		lock.acquireLock(c);

		String playFile = i.getStringExtra(consts.AUD_FILE);
		Intent alrmIntent = new Intent(c, AlarmAlert.class);
		alrmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		alrmIntent.putExtra(consts.AUD_FILE, playFile);
		
		c.startActivity(alrmIntent);
	}
}
