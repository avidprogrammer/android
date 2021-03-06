package com.example.voicealarm;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.example.voicealarm.consts;

public class Settings extends Activity implements PlayComplete {

	private int id;
	private MediaRecorder rcdr = null;
	private AlarmDoc rec = null;
	private File recFile = null;
	private Player mplyr = null;
	private Dbase db;

	Button play_btn, rec_strt_btn, rec_stp_btn;
	Switch stat_switch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		Intent intent = getIntent();
		id = intent.getIntExtra(consts.IDX, 0);

		db = Dbase.getInstance();
		mplyr = new Player(this);
		play_btn = (Button) findViewById(R.id.play_btn);
		rec_strt_btn = (Button) findViewById(R.id.rec_start_btn);
		rec_stp_btn = (Button) findViewById(R.id.rec_stop_btn);
		stat_switch = (Switch) findViewById(R.id.alarm_status_switch);

		initSettingVals();
	}

	@Override
	protected void onPause() {
		super.onPause();
		stopRecording();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mplyr.delPlayer();

		// If alarm was
		// turned ON, register Alarm if possible
		// turned OFF, unregister Alarm if possible
		if (rec != null) {
			if (rec.getStat())
				registerAlrm();
			else
				unregisterAlrm();
		}
	}

	// On settings activity init, populate params from Dbase
	private void initSettingVals() {
		rec = db.getRecord(id);
		boolean alrmStat = rec.getStat();
		String recFileName = rec.getTone();

		if (recFileName == null)
			togglePlayBtn(false);
		else
			recFile = new File(consts.BASEPATH + File.separator + recFileName);

		stat_switch.setChecked(alrmStat);
	}

	// Delete this alarm from Dbase and also the associated audio file
	private void delAlarm() {
		String alrmPath = rec.getTone();
		Log.d("DBG", "Deleting record : " + rec.toString());

		if (alrmPath != null) {
			File alrmTune = new File(alrmPath);
			alrmTune.delete();
		}

		unregisterAlrm();
		db.deleteRecord(rec);
		rec = null;
		recFile = null;
		this.finish();
	}

	/* Media Record and Play Functions */
	public void startRecord(View v) throws Exception {
		String recFileName = rec.getTone();
		rcdr = new MediaRecorder();
		rcdr.setAudioSource(MediaRecorder.AudioSource.MIC);
		rcdr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		rcdr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		rcdr.setMaxDuration(consts.MAX_REC_DUR);
		rcdr.setOnInfoListener(new OnInfoListener() {
			@Override
			public void onInfo(MediaRecorder mr, int reason, int extra) {
				if (reason == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
					Toast.makeText(getBaseContext(),
							"Max Recording duration reached",
							Toast.LENGTH_SHORT).show();
					stopRecording();
				}
			}
		});

		// If there is no audio file associated, create new
		if (recFileName == null) {
			recFileName = "sound" + id + ".3gp";
			rec.setTone(recFileName);
			Log.d("DBG", "new audio file created : " + recFileName);
		}
		recFile = new File(consts.BASEPATH + File.separator + recFileName);
		Log.d("DBG", "audio file : " + recFile.getAbsolutePath());

		rcdr.setOutputFile(recFile.getAbsolutePath());
		rcdr.prepare();
		rcdr.start();

		toggleRecBtns();
	}

	public void stopRecord(View v) throws Exception {
		stopRecording();
	};

	private void stopRecording() {
		if (rcdr != null) {
			rcdr.stop();
			rcdr.reset();
			rcdr.release();
			rcdr = null;
		}
		toggleRecBtns();
		togglePlayBtn(true);
	}

	public void playRecord(View v) throws Exception {
		if (recFile == null)
			return;
		mplyr.play(recFile.getAbsolutePath());
		togglePlayBtn(false);
	}

	/* UI Button control */

	// When Recording, disable start record button
	// When Idle, disable stop record button
	private void toggleRecBtns() {
		boolean on = rec_strt_btn.isEnabled();
		rec_strt_btn.setEnabled(!on);
		rec_stp_btn.setEnabled(on);
	}

	// Set Play button status
	private void togglePlayBtn(boolean enable) {
		play_btn.setEnabled(enable);
	}

	public void onPlayComplete() {
		// After play finishes, enable play button again
		togglePlayBtn(true);
	}

	/* Alarm Control */
	public void onStatusChg(View v) {
		boolean enabled = stat_switch.isChecked();
		if (recFile == null) {
			Toast.makeText(getApplicationContext(),
					"Please record an audio message first", Toast.LENGTH_LONG)
					.show();
			stat_switch.setChecked(false);
		}
		rec.setStat(enabled);
	}

	// Get the alarm time info and register with AlarmManager
	private void registerAlrm() {
		int hour = rec.getHour();
		int min = rec.getMinute();

		Calendar calNow = Calendar.getInstance();
		Calendar calAlarm = (Calendar) calNow.clone();

		calAlarm.set(Calendar.HOUR_OF_DAY, hour);
		calAlarm.set(Calendar.MINUTE, min);
		calAlarm.set(Calendar.SECOND, 0);
		calAlarm.set(Calendar.MILLISECOND, 0);

		// Today Set time passed, count to tomorrow
		if (calAlarm.compareTo(calNow) <= 0)
			calAlarm.add(Calendar.DATE, 1);

		AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmMgr.set(AlarmManager.RTC_WAKEUP, calAlarm.getTimeInMillis(),
				getPendIntent());

		Log.d("DBG", "register alarm");
	}

	private void unregisterAlrm() {
		Log.d("DBG", "unregister alarm");
		AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmMgr.cancel(getPendIntent());
	}

	private PendingIntent getPendIntent() {
		if (recFile == null)
			return null;
		int alarm_idx = rec.getIdx();
		Intent alrmIntent = new Intent(getBaseContext(), AlarmRx.class);
		alrmIntent.putExtra(consts.AUD_FILE, recFile.getAbsolutePath());

		return PendingIntent.getBroadcast(getBaseContext(), alarm_idx,
				alrmIntent, 0);
	}

	/* Time Control */
	public void selTime(View v) throws Exception {
		int hr = rec.getHour();
		int min = rec.getMinute();
		DialogFragment timeDialog = new TimeSelFragment();
		Bundle dispArgs = new Bundle();
		dispArgs.putInt(consts.TIME_H, hr);
		dispArgs.putInt(consts.TIME_M, min);
		timeDialog.setArguments(dispArgs);
		timeDialog.show(getFragmentManager(), "time_sel");
	}

	// Callback for Time Picker Dialog
	public void updateTime(int hour, int min) {
		rec.setHour(hour);
		rec.setMinute(min);
	}


	/* Day Control */
	public void selDay(View v) {
		final ArrayList<Integer> selected = rec.getDaysArr();
		AlertDialog.Builder dayDialog = new AlertDialog.Builder(this);

		DialogInterface.OnMultiChoiceClickListener dayClicked = new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				if (isChecked) {
					selected.add(which);
				} else {
					int whichidx = selected.indexOf(which);
					if (whichidx != -1)
						selected.remove(whichidx);
				}
			}
		};

		DialogInterface.OnClickListener okClicked = new DialogInterface.OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				rec.setDaysAsBmsk(selected);
				Log.d("DBG", selected.toString());
			}
		};

		dayDialog.setTitle(R.string.sel_day)
				 .setMultiChoiceItems(consts.DAYS, rec.getDaysBoolArr(), dayClicked)
				 .setPositiveButton("OK", okClicked);

		dayDialog.show();
	}

	/* Menu Operation */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.settings, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.del_alarm:
			delAlarm();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
