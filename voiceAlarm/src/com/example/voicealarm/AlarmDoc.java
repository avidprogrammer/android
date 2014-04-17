package com.example.voicealarm;

import java.util.ArrayList;

import android.util.Log;

public class AlarmDoc {
	private int hour = 0;
	private int minute = 0;
	private String tonePath = "";
	private int index = 0;
	private boolean status = false;
	private int daysBmsk = 0;
	private boolean oneShot = true;
	
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
		
		if (hr > 11) 
			aPm = "PM";

		if (hr > 12)
			hr -= 12;

			
		return String.valueOf(hr) + ":" + min + " " + aPm;
	}

	public void setDaysBmsk(int thisBmsk) {
		daysBmsk = thisBmsk;
	}

	public void setDaysAsBmsk(ArrayList<Integer> selected) {
		daysBmsk = 0;
		for(int i=0; i< selected.size(); i++)
			daysBmsk |= (1 << selected.get(i));
	}

	public int getDaysBmsk() {
		return daysBmsk;
	}
	
	public boolean[] getDaysBoolArr(){
		boolean days[] = {false, false, false, false, false, false, false};
		int bmsk = daysBmsk;
		for (int i=0; (i < 7) && (bmsk != 0); i++)
		{
			days[i] = ((bmsk & (1 << i)) > 0);
			bmsk &= ~(1 << i);
		}
		return days;
	}
	
	public ArrayList<Integer> getDaysArr() {
		ArrayList<Integer> days = new ArrayList<Integer>();
		int bmsk = daysBmsk;
		for(int i=0; (i<7) && (bmsk !=0); i++)
		{
			if ((bmsk & (1<<i)) > 0)
				days.add(i);
			bmsk &= ~(1<<i);
		}
		return days;
	}
	
	public void setOneShot(boolean val){
		oneShot = val;
		Log.d("DBG", "Set one shot" + val);
	}
	
	public boolean getOneShot(){
		return oneShot;		
	}
}
