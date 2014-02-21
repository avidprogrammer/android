package com.example.voicealarm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
	String tag = "DBG";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
		Log.d("DBG", "onPause");
		db.commit();
	}

	public void addAlarm() {
		HashMap <String, String> rec = new HashMap <String, String>();
		rec.put(myConsts.AUD_FILE, "abc");
		rec.put(myConsts.TIME_H, "1");
		rec.put(myConsts.TIME_M, "0");
		rec.put(myConsts.AM_PM, "PM");
		rec.put(myConsts.ALARM_ID, Integer.toString((int) (Math.random()*5)));

		Log.d("DBG", "ADD new alarm! : " + rec.toString());
		db.addRecord(rec);
	}

	private OnItemClickListener alarmClick = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> av, View v, int idx, long id) {
			Log.d("DBG", v.getContext().toString() + " idx:" + idx);
			Intent myIntent = new Intent(v.getContext(), Settings.class);
			myIntent.putExtra("key", idx);
			startActivity(myIntent);
		}
	};

	private void initAlarmList() {
		String[] timeOpts = new String[] { "5s", "1min", "5min", "20min" };
		ArrayList<String> timeArr = new ArrayList<String>();
		for (int i = 0; i < db.getNumRecords(); i++) {
			//HashMap<String, String> rec = db.getRecord(i);
			timeArr.add(timeOpts[i]);// + ' ' + rec.get(myConsts.IDX));
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.alarm_obj, R.id.alarmTv, timeArr);
		alarmListView.setAdapter(adapter);
		alarmListView.setOnItemClickListener(alarmClick);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
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
