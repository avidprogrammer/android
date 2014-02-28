package com.example.voicealarm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

	private ListView alarmListView;
	private Dbase db;
	private String JSON_FILE = "voiceAlarm.json";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Dbase is a singleton class and populates from the settings file.
		// The settings file is stored in JSON
		db = Dbase.getInstance();
		try {
			db.init(JSON_FILE);
		} catch (FileNotFoundException e) {
			Log.d("DBG", "file not found");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		alarmListView = (ListView) findViewById(R.id.alarmList);
		initAlarmList();
	}

	@Override
	protected void onPause() {
		super.onPause();
		db.commit();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initAlarmList();
	}

	// Initialize new alarm and reset params to default
	public void addAlarm() {
		AlarmDoc rec = new AlarmDoc();
		rec.setHour(0);
		rec.setMinute(0);
		rec.setStat(false);
		rec.setTone(null);
		rec.setIdx(db.getNumRecords());

		Log.d("DBG", "ADD new alarm! : " + rec.toString());
		db.addRecord(rec);
		initAlarmList();
	}

	// When an alarm row is clicked, open the Settings activity
	private OnItemClickListener alarmClick = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> av, View v, int idx, long id) {
			Log.d("DBG", v.getContext().toString() + " idx:" + idx);
			Intent myIntent = new Intent(v.getContext(), Settings.class);
			myIntent.putExtra(consts.IDX, idx);
			startActivity(myIntent);
		}
	};


	// The main screen is a list of alarms.
	// Populate them by iterating through dBase
	private void initAlarmList() {
		ArrayList<String> timeArr = new ArrayList<String>();
		for (int i = 0; i < db.getNumRecords(); i++) {
			AlarmDoc rec = db.getRecord(i);
			timeArr.add(rec.getDispText());
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.alarm_row, R.id.alarmTv, timeArr);
		alarmListView.setAdapter(adapter);
		alarmListView.setOnItemClickListener(alarmClick);
	}

	// Menu operation
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.add_alarm:
	            addAlarm();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
