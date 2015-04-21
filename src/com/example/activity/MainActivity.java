package com.example.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.rainymood.R;
import com.example.service.CounterService;
import com.example.service.PlayService;
import com.example.util.ActivityCollector;
import com.example.util.AppConstant;


public class MainActivity extends Activity implements OnClickListener{
	private ImageView playorstopButton;
	private ImageView counter;
	private ImageView about;
	private TextView clockTextView;
	private SeekBar rainBar;
	private SeekBar thunderBar;
	private AudioManager audioManager;
	private int maxVolume,currentVolume;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCollector.addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		init_Buttonand_other();
		setonClickListener();
		playMusic();
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
			if (playorstopButton.getTag().toString() == "play") {
				playorstopButton.setTag("stop");
				playorstopButton.setImageResource(R.drawable.stop);
				Intent intent = new Intent(this, PlayService.class);
				intent.putExtra("tag", AppConstant.STOP_MUSIC);
				startService(intent);
				
			}else {
				playorstopButton.setTag("play");
				playorstopButton.setImageResource(R.drawable.play);
				Intent intent = new Intent(this, PlayService.class);
				intent.putExtra("tag", AppConstant.PLAY_MUSIC);
				startService(intent);
			}
			
		break;
		case R.id.counter_imageview:
			if (clockTextView.getText().toString() == "off") {
				clockTextView.setText("1h");
				Intent intent = new Intent(this, CounterService.class);
				startService(intent);
				
			}else {
				clockTextView.setText("off");
				Intent intent = new Intent(this, CounterService.class);
				stopService(intent);
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
				Intent intent = new Intent(MainActivity.this, PlayService.class);
				intent.putExtra("tag", AppConstant.THUNDER_VOLUME_CHANGE);
				intent.putExtra("percent", percent);
				startService(intent);
				
				
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
				Intent intent = new Intent(MainActivity.this, PlayService.class);
				intent.putExtra("tag", AppConstant.RAIN_VOLUME_CHANGE);
				intent.putExtra("percent", percent);
				startService(intent);
				
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.d("RainyMood", "执行了activity的onDestroy");
		Intent intent = new Intent(MainActivity.this, PlayService.class);
		Intent intent2 = new Intent(this, CounterService.class);
		stopService(intent);
		stopService(intent2);
		super.onDestroy();
	}
	private void playMusic()
	{
		Intent intent = new Intent(MainActivity.this, PlayService.class);
		intent.putExtra("tag", 0);
		startService(intent);
	}
}
