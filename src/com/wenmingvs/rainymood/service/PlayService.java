package com.wenmingvs.rainymood.service;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;


import com.wenmingvs.rainymood.R;
import com.wenmingvs.rainymood.util.AppConstant;

public class PlayService extends Service {

	private MediaPlayer thunder_mediaPlayer;
	private MediaPlayer rain_meMediaPlayer;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		flags = START_NOT_STICKY;

		if (intent != null) {
			int operation = intent.getIntExtra("tag", 5);
			switch (operation) {
			case AppConstant.PLAY_MUSIC:
				thunder_mediaPlayer.start();
				rain_meMediaPlayer.start(); 
				break;
			case AppConstant.STOP_MUSIC:
				rain_meMediaPlayer.pause();
				thunder_mediaPlayer.pause();
				break;
			case AppConstant.THUNDER_VOLUME_CHANGE:
				float thunder_percent = intent.getFloatExtra("percent", 0);
				thunder_mediaPlayer.setVolume(thunder_percent, thunder_percent);
				break;	
			case AppConstant.RAIN_VOLUME_CHANGE:
				float rain_percent = intent.getFloatExtra("percent", 0);
				rain_meMediaPlayer.setVolume(rain_percent, rain_percent);
				break;	
			default:
				break;
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}
	private void playThunder()
	{
		
		thunder_mediaPlayer.start();
		thunder_mediaPlayer.setLooping(true);
		
	}
	private void playRain()
	{
		
		rain_meMediaPlayer.start();
		rain_meMediaPlayer.setLooping(true);
		
	}

	@Override
	public void onCreate() {
		init_media();
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				playThunder();
				playRain();
			}
		}).start();
		super.onCreate();

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		rain_meMediaPlayer.stop();
		rain_meMediaPlayer.release();
		thunder_mediaPlayer.stop();
		thunder_mediaPlayer.release();
		super.onDestroy();
	}
	
	private void init_media()
	{
		thunder_mediaPlayer = MediaPlayer.create(this, R.raw.thunder);
		rain_meMediaPlayer = MediaPlayer.create(this, R.raw.rain);
	}
	
}
