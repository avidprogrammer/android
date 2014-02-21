package com.example.voicealarm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import android.util.Log;
import android.widget.Toast;

import com.example.voicealarm.myConsts;

public class Dbase {
	private ArrayList<HashMap<String, String>> records = new ArrayList<HashMap<String, String>>();
	private String settings;

	private static Dbase dbase = null;

	protected Dbase() {
	}

	public static Dbase getInstance() {
		if (dbase == null)
			dbase = new Dbase();
		return dbase;
	}

	private void initSettings() {
		File sttngs = new File(settings);
		Log.d("DBG", "FIle exists : "+sttngs.exists());
		if (sttngs.exists())
			return;
		
		JSONObject obj = new JSONObject();
		JSONArray alrms = new JSONArray();
		
		obj.put(myConsts.HDG, alrms);
	 
		try {
	 		FileWriter file = new FileWriter(settings);
			file.write(obj.toJSONString());
			file.flush();
			file.close();
	 	} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void init(String jsonF) throws FileNotFoundException, IOException,
			ParseException {

		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		JSONArray alrms = null;

		records.clear();
		settings = myConsts.BASEPATH + File.separator + jsonF;
		initSettings();

		
		Log.d("DBG", "settings : " + settings);

		obj = (JSONObject) parser.parse(new FileReader(settings));
		alrms = (JSONArray) obj.get(myConsts.HDG);
		Log.d("DBG", "alrms : " + alrms.toJSONString());

		for (int i = 0; i < alrms.size(); i++) {
			JSONObject recJson = (JSONObject) alrms.get(i);
			HashMap<String, String> rec = new HashMap<String, String>();

			rec.put(myConsts.IDX, Integer.toString(i));
			for (int j = 0; j < myConsts.REQD_FIELDS.length; j++) {
				String field = myConsts.REQD_FIELDS[j];
				if (recJson.containsKey(field))
					rec.put(field, (String) recJson.get(field));
				else
					rec.put(field, null);
			}
			records.add(rec);
		}
	}

	public void commit() {

		JSONObject obj = new JSONObject();
		JSONArray alrms = new JSONArray();
		for (int i = 0; i < getNumRecords(); i++) {
			JSONObject recJson = new JSONObject();
			HashMap<String, String> rec = records.get(i);
			for (Map.Entry<String, String> entry : rec.entrySet()) {
				String key = entry.getKey();
				String val = entry.getValue();
				if (key != myConsts.IDX)
					recJson.put(key, val);
			}
			alrms.add(recJson);
		}
		obj.put(myConsts.HDG, alrms);

		try {
			FileWriter file = new FileWriter(settings);
			file.write(obj.toJSONString());
			Log.d("DBG", "settings : " + settings.toString());
			Log.d("DBG", "commit : " + obj.toJSONString());
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getNumRecords() {
		Log.d("DBG", "getNumRecords : " + Integer.toString((records.size())));
		return records.size();
	}

	public HashMap<String, String> getRecord(int idx) {
		return records.get(idx);
	}

	public void setRecord(HashMap<String, String> rec) {
		records.set(getRecIdx(rec), rec);
	}

	public boolean addRecord(HashMap<String, String> rec) {
		boolean valid = true;
		for (int i = 0; i < myConsts.REQD_FIELDS.length; i++) {
			if (!rec.containsKey(myConsts.REQD_FIELDS[i])) {
				valid = false;
				break;
			}
		}
		
		Log.d("DBG", "Add Record : " + rec.toString());

		if (valid)
			records.add(rec);

		return valid;
	}

	public void deleteRecord(HashMap<String, String> rec) {
		int delIdx = getRecIdx(rec);
		int totIdx = getNumRecords() - 1;

		if (delIdx != totIdx)
			records.set(delIdx, getRecord(totIdx));

		records.remove(totIdx);
	}

	private int getRecIdx(HashMap<String, String> rec) {
		return Integer.valueOf(rec.get(myConsts.IDX));
	}

}
