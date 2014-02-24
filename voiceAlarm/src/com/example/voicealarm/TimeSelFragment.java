package com.example.voicealarm;

import android.os.Bundle;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

public class TimeSelFragment extends DialogFragment implements
		TimePickerDialog.OnTimeSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		int hour = getArguments().getInt(consts.TIME_H);
		int minute = getArguments().getInt(consts.TIME_M);

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute,
				DateFormat.is24HourFormat(getActivity()));
	}

	@Override
	public void onTimeSet(TimePicker arg0, int hr, int min) {
		Log.d("DBG", "Hour : " + String.valueOf(hr));
		Settings setAct = (Settings) getActivity();
		setAct.updateTime(hr, min);
		
	}
}
