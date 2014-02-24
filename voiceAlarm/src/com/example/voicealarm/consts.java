package com.example.voicealarm;

import java.io.File;

import android.os.Environment;

public class consts {

	private consts() {
	};

	public static final File BASEPATH = Environment.getExternalStorageDirectory();
	public static final String AUD_FILE = "audio_file_path";
	public static final String ALRM_STAT = "status";
	public static final String TIME_H = "hour";
	public static final String TIME_M = "min";
	public static final String IDX = "idx";
	public static final String HDG = "alarms";
	public static final int MAX_REC_DUR = 10 * 1000; // in ms

}
