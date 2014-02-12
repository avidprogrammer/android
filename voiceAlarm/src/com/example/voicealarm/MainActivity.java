package com.example.voicealarm;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {

	private ListView alarmListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		alarmListView = (ListView) findViewById(R.id.alarmList);
		initAlarmList();

	}

	private OnItemClickListener alarmClick = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> av, View v, int idx, long id) {
			Log.d("DBG", v.getContext().toString() + " idx:" + idx);
			Intent myIntent = new Intent(v.getContext(), Record.class);
			myIntent.putExtra("key", idx);
			startActivity(myIntent);
		}
	};

	private void initAlarmList() {
		String[] timeOpts = new String[] { "5s", "1min", "5min", "20min" };
		ArrayList<String> timeArr = new ArrayList<String>();
		for (int i = 0; i < timeOpts.length; i++) {
			timeArr.add(timeOpts[i]);
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.alarm_obj, R.id.alarmTv, timeArr);
		alarmListView.setAdapter(adapter);
		alarmListView.setOnItemClickListener(alarmClick);
	}

}
