package com.example.voicealarm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import android.util.Log;

public class Dbase {
	private ArrayList<AlarmDoc> records = new ArrayList<AlarmDoc>();
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
		
		obj.put(consts.HDG, alrms);
	 
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
		settings = consts.BASEPATH + File.separator + jsonF;
		initSettings();

		
		Log.d("DBG", "settings : " + settings);

		obj = (JSONObject) parser.parse(new FileReader(settings));
		alrms = (JSONArray) obj.get(consts.HDG);
		Log.d("DBG", "alrms : " + alrms.toJSONString());

		for (int i = 0; i < alrms.size(); i++) {
			JSONObject recJson = (JSONObject) alrms.get(i);
			AlarmDoc rec = new AlarmDoc();

			rec.setIdx(i);
			rec.setHour(((Long)(recJson.get(consts.TIME_H))).intValue());
			rec.setMinute(((Long)(recJson.get(consts.TIME_M))).intValue());
			rec.setStat((Boolean) recJson.get(consts.ALRM_STAT));
			rec.setTone((String) recJson.get(consts.AUD_FILE));
			records.add(rec);
		}
	}

	public void commit() {

		JSONObject obj = new JSONObject();
		JSONArray alrms = new JSONArray();
		for (int i = 0; i < getNumRecords(); i++) {
			JSONObject recJson = new JSONObject();
			AlarmDoc rec = records.get(i);
			recJson.put(consts.ALRM_STAT, rec.getStat());
			recJson.put(consts.AUD_FILE, rec.getTone());
			recJson.put(consts.TIME_H, rec.getHour());
			recJson.put(consts.TIME_M, rec.getMinute());
			recJson.put(consts.IDX, rec.getIdx());
			alrms.add(recJson);
		}
		obj.put(consts.HDG, alrms);

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

	public AlarmDoc getRecord(int idx) {
		return records.get(idx);
	}

	public void setRecord(AlarmDoc rec) {
		records.set(rec.getIdx(), rec);
	}

	public void addRecord(AlarmDoc rec) {
		Log.d("DBG", "Add Record : " + rec.toString());
		records.add(rec);
	}

	public void deleteRecord(AlarmDoc rec) {
		int delIdx = rec.getIdx();
		int totIdx = getNumRecords() - 1;
		AlarmDoc totRec = getRecord(totIdx);

		// When deleting from the array, replace last record
		// with the one to be deleted to prevent shifting multiple
		// elements on element deletion
		if (delIdx != totIdx)
		{
			records.set(delIdx, totRec);
			totRec.setIdx(delIdx);
		}

		records.remove(totIdx);
		Log.d("DBG", "After delete : " + records.toString());
	}
}
