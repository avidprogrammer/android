package com.example.voicealarm;

import java.io.File;

import android.os.Environment;

public class myConsts {

	private myConsts() {};
	
	public static final File BASEPATH = Environment.getExternalStorageDirectory();
	public static final String AUD_FILE = "audio_file_path";
	public static final String ALARM_ID = "id";
	public static final String TIME_H ="hour";
	public static final String TIME_M ="min";
	public static final String AM_PM ="am_pm";
	public static final String IDX = "idx";
	public static final String HDG = "alarms";
	public static final String[] REQD_FIELDS = new String[] {AUD_FILE,
															 ALARM_ID,
															 TIME_H,
															 TIME_M,
															 AM_PM};
}
