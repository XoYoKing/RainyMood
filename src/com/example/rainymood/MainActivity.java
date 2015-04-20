package com.example.rainymood;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.example.receiver.AlarmReceiver;


public class MainActivity extends Activity implements OnClickListener{
	private ImageView playorstopButton;
	private ImageView counter;
	private ImageView about;
	private TextView clockTextView;
	private MediaPlayer thunder_mediaPlayer;
	private MediaPlayer rain_meMediaPlayer;
	private SeekBar rainBar;
	private SeekBar thunderBar;
	private AudioManager audioManager;
	private int maxVolume,currentVolume;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		init_Buttonand_other();
		setonClickListener();
		playThunder();
		playRain();
		
		
		
	}
	private void playThunder()
	{
		thunder_mediaPlayer = MediaPlayer.create(this, R.raw.thunder);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				thunder_mediaPlayer.start();
				thunder_mediaPlayer.setLooping(true);
			}
		}).start();
	}
	private void playRain()
	{
		rain_meMediaPlayer = MediaPlayer.create(this, R.raw.rain);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				rain_meMediaPlayer.start();
				rain_meMediaPlayer.setLooping(true);
			}
		}).start();
	}
	
	private void init_Buttonand_other()
	{
		playorstopButton = (ImageView)findViewById(R.id.playorstop);
		counter = (ImageView)findViewById(R.id.counter_imageview);
		about = (ImageView)findViewById(R.id.about);
		clockTextView = (TextView)findViewById(R.id.counter_text);
		rainBar = (SeekBar)findViewById(R.id.rain_seekbar);
		thunderBar = (SeekBar)findViewById(R.id.thunder_seekbar);
		audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		//seekbar设置最大音量
		maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//获取系统最大音量
		rainBar.setMax(maxVolume);
		thunderBar.setMax(maxVolume);
		//seekbar显示当前音量
		currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		rainBar.setProgress(currentVolume);
		thunderBar.setProgress(currentVolume);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.playorstop:
//			if (playorstopButton.getDrawable().equals(getResources().getDrawable(R.drawable.play))) {
//				playorstopButton.setImageResource(R.drawable.stop);
//			}
			if (playorstopButton.getTag().toString() == "play") {
				playorstopButton.setTag("stop");
				playorstopButton.setImageResource(R.drawable.stop);
				rain_meMediaPlayer.pause();
				thunder_mediaPlayer.pause();
			}else {
				playorstopButton.setTag("play");
				playorstopButton.setImageResource(R.drawable.play);
				rain_meMediaPlayer.start();
				thunder_mediaPlayer.start();
			}
			
		break;
		case R.id.counter_imageview:
			if (clockTextView.getText().toString() == "off") {
				clockTextView.setText("1h");
				AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				int anHour = 60 * 60 * 1000;
				long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
				Intent i = new Intent(this,AlarmReceiver.class);
				PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
				manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
			}else {
				clockTextView.setText("off");
			}
		break;
		case R.id.about:	
			
		break;
		default:
			break;
		}
	}
	
	private void setonClickListener()
	{
		playorstopButton.setOnClickListener(this);
		counter.setOnClickListener(this);
		about.setOnClickListener(this);
		thunderBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
				float percent = (float)(progress*1.0)/maxVolume;	
				thunder_mediaPlayer.setVolume(percent, percent);
				
			}
		});
		rainBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				float percent = (float)(progress*1.0)/maxVolume;	
				rain_meMediaPlayer.setVolume(percent, percent);
			}
		});
		
		
	}
	
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		thunder_mediaPlayer.stop();
		rain_meMediaPlayer.stop();
		thunder_mediaPlayer.release();
		rain_meMediaPlayer.release();
		super.onDestroy();
	}
	
}
