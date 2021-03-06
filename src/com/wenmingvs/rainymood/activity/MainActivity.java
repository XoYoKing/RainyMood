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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.wenmingvs.rainymood.R;
import com.wenmingvs.rainymood.service.CounterService;
import com.wenmingvs.rainymood.service.PlayService;
import com.wenmingvs.rainymood.slidingmenu.SlidingMenu;
import com.wenmingvs.rainymood.util.ActivityCollector;
import com.wenmingvs.rainymood.util.Const;
import com.wenmingvs.rainymood.util.MySeekBar;

public class MainActivity extends Activity implements OnClickListener {
	private ImageView playorstopButton;
	private ImageView counter;
	private ImageView about;
	private TextView clockTextView;
	private MySeekBar rainBar;
	private MySeekBar thunderBar;
	private AudioManager audioManager;
	private int maxVolume;

	private TextView weibo;
	private TextView zhihu;

	private Button adviceButton;
	// private String ShowAd = "";
	private SlidingMenu mLeftMenu;
	private FeedbackAgent agent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityCollector.addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		init_Buttonand_other();
		setonClickListener();
		playMusic();
		// 获取友盟的在线参数
		// ShowAd = MobclickAgent.getConfigParams( this, "ShowAd" );
		// 调用友盟的更新接口
		UmengUpdateAgent.update(this);
		// 初始化友盟的用户反馈界面,后台检查是否有新的来自开发者的回复
		agent = new FeedbackAgent(this);
		agent.sync();
		// 发送友盟的统计数据到服务器端
		MobclickAgent.updateOnlineConfig(this);
	}

	private void init_Buttonand_other() {

		playorstopButton = (ImageView) findViewById(R.id.playorstop);
		counter = (ImageView) findViewById(R.id.counter_imageview);
		about = (ImageView) findViewById(R.id.about);
		clockTextView = (TextView) findViewById(R.id.counter_text);
		rainBar = (MySeekBar) findViewById(R.id.rain_seekbar);
		thunderBar = (MySeekBar) findViewById(R.id.thunder_seekbar);
		mLeftMenu = (SlidingMenu) findViewById(R.id.id_menu);
		weibo = (TextView) findViewById(R.id.weibo);
		zhihu = (TextView) findViewById(R.id.zhihu);
		adviceButton = (Button) findViewById(R.id.advice);
		weibo.setMovementMethod(LinkMovementMethod.getInstance());
		zhihu.setMovementMethod(LinkMovementMethod.getInstance());

		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		// seekbar设置最大音量
		maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 获取系统最大音量
		rainBar.setMax(maxVolume);
		thunderBar.setMax(maxVolume);
		// seekbar显示当前音量

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
				intent.putExtra("tag", Const.STOP_MUSIC);
				startService(intent);

			} else {
				playorstopButton.setTag("play");
				playorstopButton.setImageResource(R.drawable.stop);
				Intent intent = new Intent(this, PlayService.class);
				intent.putExtra("tag", Const.PLAY_MUSIC);
				startService(intent);
			}
			break;
		case R.id.counter_imageview:
			vibrate_1s();
			if (clockTextView.getText().toString().equals("off")) {
				clockTextView.setText("1h");
				Intent intent = new Intent(this, CounterService.class);
				startService(intent);
			} else {

				clockTextView.setText("off");
				Intent intent = new Intent(this, CounterService.class);
				stopService(intent);

			}
			break;
		case R.id.about:
			vibrate_1s();
			mLeftMenu.toggle();
			break;

		case R.id.advice:
			vibrate_1s();
			agent.startFeedbackActivity();

		}
	}

	private void setonClickListener() {
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

				float percent = (float) (progress * 1.0) / maxVolume;
				Intent intent = new Intent(MainActivity.this, PlayService.class);
				intent.putExtra("tag", Const.THUNDER_VOLUME_CHANGE);
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

				float percent = (float) (progress * 1.0) / maxVolume;
				Intent intent = new Intent(MainActivity.this, PlayService.class);
				intent.putExtra("tag", Const.RAIN_VOLUME_CHANGE);
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

	private void playMusic() {
		Intent intent = new Intent(MainActivity.this, PlayService.class);
		intent.putExtra("tag", 0);
		startService(intent);
	}

	public void toggleMenu(View view) {
		Log.d("RainyMood", "执行了about的onClick方法");

	}

	public void vibrate_1s() {
		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(100); // 振动一次
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
