package com.example.voicealarm;

import java.io.File;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Record extends Activity implements playCompletion {

	private int id;
	private MediaRecorder rcdr = null;
	private File basePath = Environment.getExternalStorageDirectory();
	private File recFile = null;
	private Player mplyr = null;

	Button play_btn, rec_strt_btn, rec_stp_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		Intent intent = getIntent();
		id = intent.getIntExtra("key", 0);

		mplyr = new Player(this);

		play_btn = (Button) findViewById(R.id.play_btn);
		rec_strt_btn = (Button) findViewById(R.id.rec_start_btn);
		rec_stp_btn = (Button) findViewById(R.id.rec_stop_btn);
	}

	protected void onDestroy(Bundle savedInstanceState) {
		mplyr.delPlayer();
		clrRecorder();
	}

	private void clrRecorder() {
		if (rcdr != null) {
			rcdr.stop();
			rcdr.reset();
			rcdr.release();
		}
	}

	public void startRecord(View v) throws Exception {
		rcdr = new MediaRecorder();
		rcdr.setAudioSource(MediaRecorder.AudioSource.MIC);
		rcdr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		rcdr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recFile = File.createTempFile("sound" + id, ".3gp", basePath);
		Log.d("DBG", "audio file : " + recFile.getAbsolutePath());
		rcdr.setOutputFile(recFile.getAbsolutePath());

		rcdr.prepare();
		rcdr.start();

		toggle_rec_btns();
	}

	public void stopRecord(View v) throws Exception {
		clrRecorder();
		toggle_rec_btns();
	};

	public void playRecord(View v) throws Exception {
		if (recFile == null)
			return;
		mplyr.play(recFile);
		play_btn.setEnabled(false);
	}

	private void toggle_rec_btns() {
		boolean on = rec_strt_btn.isEnabled();
		rec_strt_btn.setEnabled(!on);
		rec_stp_btn.setEnabled(on);
	}

	public void onPlayComplete() {
		play_btn.setEnabled(true);
	}
}
