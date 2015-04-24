package com.wenmingvs.rainymood.activity;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import cn.waps.AppConnect;

import com.wenmingvs.rainymood.R;
import com.wenmingvs.rainymood.service.CounterService;
import com.wenmingvs.rainymood.service.PlayService;
import com.wenmingvs.rainymood.slidingmenu.SlidingMenu;
import com.wenmingvs.rainymood.util.ActivityCollector;
import com.wenmingvs.rainymood.util.AppConstant;


public class MainActivity extends Activity implements OnClickListener{
	private ImageView playorstopButton;
	private ImageView counter;
	private ImageView about;
	private TextView clockTextView;
	private SeekBar rainBar;
	private SeekBar thunderBar;
	private AudioManager audioManager;
	private int maxVolume;

	private TextView weibo;
	private TextView zhihu;
	
	private Button adviceButton;
	private String ShowAd = "";
	private SlidingMenu mLeftMenu ; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		AppConnect.getInstance("033b920ab379609d1297fda7bcfa8c41","default",this);
		
		ShowAd = AppConnect.getInstance(this).getConfig("ShowAd","false");
		
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
		mLeftMenu = (SlidingMenu) findViewById(R.id.id_menu);
		weibo = (TextView)findViewById(R.id.weibo);
		zhihu = (TextView)findViewById(R.id.zhihu);
		
		
		adviceButton = (Button)findViewById(R.id.advice);
		
		//显示迷你广告
		
		if (ShowAd.equals("true")) {
			LinearLayout miniLayout = (LinearLayout) findViewById(R.id.miniAdLinearLayout);
			AppConnect.getInstance(this).showMiniAd(this, miniLayout, 10);// 10秒刷新一次
		}
		
		
		
		
		weibo.setMovementMethod(LinkMovementMethod.getInstance());
		zhihu.setMovementMethod(LinkMovementMethod.getInstance());
		
		
		
		audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		//seekbar设置最大音量
		maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//获取系统最大音量
		rainBar.setMax(maxVolume);
		thunderBar.setMax(maxVolume);
		//seekbar显示当前音量
		
		rainBar.setProgress(maxVolume);
		thunderBar.setProgress(maxVolume);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.playorstop:
			vibrate_1s();
			if (playorstopButton.getTag().toString() == "play") {
				playorstopButton.setTag("stop");
				playorstopButton.setImageResource(R.drawable.play);
				Intent intent = new Intent(this, PlayService.class);
				intent.putExtra("tag", AppConstant.STOP_MUSIC);
				startService(intent);
				
			}else {
				playorstopButton.setTag("play");
				playorstopButton.setImageResource(R.drawable.stop);
				Intent intent = new Intent(this, PlayService.class);
				intent.putExtra("tag", AppConstant.PLAY_MUSIC);
				startService(intent);
			}		
		break;
		case R.id.counter_imageview:
			/**
			 * 熟悉C++的人对于两个字符串比较的代码一定很了解： (string1==string2) 
			      但在java中，这个代码即使在两个字符串完全相同的情况下也会返回false 
			      因为 “==” 比较的是内存地址是否相同！！！
			      必须使用java的equal来比较
			 */
			vibrate_1s();
			if (clockTextView.getText().toString().equals("off")) {
				
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
			
			if (ShowAd.equals("true")) {
				vibrate_1s();
				AppConnect.getInstance(this).showOffers(this);
			}else {
				vibrate_1s();
				mLeftMenu.toggle();
			}
		break;

		case R.id.advice:
			vibrate_1s();
			AppConnect.getInstance(this).showFeedback(this);
		
		}
	}
	
	private void setonClickListener()
	{
		playorstopButton.setOnClickListener(this);
		counter.setOnClickListener(this);
		about.setOnClickListener(this);
		adviceButton.setOnClickListener(this);
			
		
		
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
		AppConnect.getInstance(this).close();
		super.onDestroy();
	}
	private void playMusic()
	{
		Intent intent = new Intent(MainActivity.this, PlayService.class);
		intent.putExtra("tag", 0);
		startService(intent);
	}
	public void toggleMenu(View view)
	{
		Log.d("RainyMood", "执行了about的onClick方法");
		
	}
	public  void vibrate_1s()
	{
		Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);  
        vibrator.vibrate(100);  //振动一次
	}
	
}
