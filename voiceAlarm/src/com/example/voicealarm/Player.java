package com.example.voicealarm;

import java.io.File;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;

public class Player implements playCompletion {
	private MediaPlayer plyr = null;
	private playCompletion pc_ptr;

	public Player(playCompletion fn_ptr) {
		pc_ptr = fn_ptr;

		plyr = new MediaPlayer();
		plyr.setOnCompletionListener(finishPlay);
		plyr.setOnPreparedListener(startPlay);
	}

	public void delPlayer() {
		clearPlayer();
		plyr.release();
	}

	public void play(File srcFile) throws Exception {
		if (srcFile == null)
			return;

		Log.d("DBG", "Player Src File" + srcFile.getAbsolutePath());
		try {
			plyr.setDataSource(srcFile.getAbsolutePath());
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}

		try {
			plyr.prepareAsync();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}

	}

	private OnPreparedListener startPlay = new OnPreparedListener() {
		public void onPrepared(MediaPlayer mp) {
			Log.d("DBG", "Finished Sync");
			mp.start();
		}
	};
	private OnCompletionListener finishPlay = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mp) {
			clearPlayer();

			pc_ptr.onPlayComplete();
		}
	};

	public void clearPlayer() {
		if (plyr != null) {
			plyr.stop();
			plyr.reset();
		}
	}

	@Override
	public void onPlayComplete() {

	}
}
