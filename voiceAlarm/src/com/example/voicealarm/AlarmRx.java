package com.example.voicealarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.Notification;
import android.app.NotificationManager;
import android.net.Uri;
import android.widget.Toast;

public class AlarmRx extends BroadcastReceiver {

	@Override
	public void onReceive(Context c, Intent i) {
		String playFile = i.getStringExtra(consts.AUD_FILE);
		int idx = i.getIntExtra(consts.IDX, 0);
		
		Toast.makeText(c, "Alarm Rx", Toast.LENGTH_LONG).show();

		NotificationManager mgr = (NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE);
	    
	    Notification noti = new Notification.Builder(c)
        .setContentTitle("Alarm")
        .setSmallIcon(R.drawable.ic_audio_alarm)
        .setSound(Uri.parse(playFile))
        .build();
	    
	    noti.flags |= Notification.FLAG_INSISTENT;
	    
	    mgr.notify(idx, noti);
  
	}
}
