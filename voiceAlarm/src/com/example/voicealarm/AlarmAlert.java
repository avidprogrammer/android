package com.example.voicealarm;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.app.Activity;

public class AlarmAlert extends Activity implements PlayComplete{
	private Player mplyr = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_alert);
		this.setFinishOnTouchOutside(false);

		String playFile = getIntent().getStringExtra(consts.AUD_FILE);
		mplyr = new Player(this);
		try {
			mplyr.playLooping(playFile);
		} catch (Exception e) {
			Log.d("DBG", "Error Playing Alarm file");
			e.printStackTrace();
		}
	}

	public void dismiss(View v) throws Exception {
		WakeLk lock = WakeLk.getInstance();
		lock.releaseLock();

		mplyr.delPlayer();
		this.finish();
	}

	@Override
	public void onPlayComplete() {}

}
