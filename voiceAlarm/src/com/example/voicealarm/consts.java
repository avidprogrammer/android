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
	public static final String DTW = "days_of_the_week";
	public static final int MAX_REC_DUR = 10 * 1000; // in ms
	public static final String APPTAG = "voiceAlarm";
	public static final String SET_FILE = "voiceAlarm.json";

	public static final String MON = "Monday";
	public static final String TUE = "Tuesday";
	public static final String WED = "Wednesday";
	public static final String THU = "Thursday";
	public static final String FRI = "Friday";
	public static final String SAT = "Saturday";
	public static final String SUN = "Sunday";

	public static final String[] DAYS = {SUN, MON, TUE, WED, THU, FRI, SAT};
}
