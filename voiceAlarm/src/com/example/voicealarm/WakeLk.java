package com.example.voicealarm;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

public class WakeLk {

	private static WakeLk Lock = null;
	private WakeLock wakeLock = null;

	protected WakeLk() {};

	public static WakeLk getInstance() {
		if (Lock == null)
			Lock = new WakeLk();
		return Lock;
	}

	public void acquireLock(Context ctx) {
		if (wakeLock == null)
		{
			int wakeLockFlags = PowerManager.FULL_WAKE_LOCK |
								PowerManager.ACQUIRE_CAUSES_WAKEUP;

			PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(wakeLockFlags, consts.APPTAG);
		}

		wakeLock.acquire();
		Log.d("DBG", "Acquire lock");
	}

	public void releaseLock() {
		if (wakeLock == null || !wakeLock.isHeld())
			return;
		Log.d("DBG", "releasing Lock");
		wakeLock.release();
	}

}
