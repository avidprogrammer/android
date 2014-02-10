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


public class Record extends Activity {

	private int id;
	private MediaRecorder rcdr;
	private File basePath = Environment.getExternalStorageDirectory();
	private File recFile = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		Intent intent = getIntent();
		id = intent.getIntExtra("key", 0);
	}

	public void startRecord(View v) throws Exception {
		rcdr = new MediaRecorder();
		rcdr.setAudioSource(MediaRecorder.AudioSource.MIC);
		rcdr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		rcdr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recFile = File.createTempFile("sound" + id, ".3gp", basePath);
		rcdr.setOutputFile(recFile.getAbsolutePath());

		rcdr.prepare();
		rcdr.start();
	}

	public void stopRecord(View v) throws Exception {	
		rcdr.stop();
		rcdr.release();
	};
}
