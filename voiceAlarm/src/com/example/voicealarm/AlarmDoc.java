package com.example.voicealarm;

public class AlarmDoc {
	private int hour = 0;
	private int minute = 0;
	private String tonePath = "";
	private int index = 0;
	private boolean status = false;
	
	public void setHour(int hr) {
		hour = hr;
	}
	
	public int getHour() {
		return hour;
	}
	
	public void setMinute(int min) {
		minute = min;
	}
	
	public int getMinute() {
		return minute;
	}
	
	public void setIdx(int idx) {
		index = idx;
	}
	
	public int getIdx() {
		return index;
	}

	public void setStat(boolean enable) {
		status = enable;
	}
	
	public boolean getStat() {
		return status;
	}
	
	public void setTone(String tone){
		tonePath = tone;
	}
	
	public String getTone() {
		return tonePath;
	}
	
	public String getDispText() {
		String min = String.valueOf(minute);
		String aPm = "AM";
		int hr = hour;
		
		if (minute < 10)
			min = "0".concat(min);
		
		if (hr > 11) {
			aPm = "PM";
			hr -= 12;
		}
			
		return String.valueOf(hr) + ":" + min + " " + aPm;
	}
}
