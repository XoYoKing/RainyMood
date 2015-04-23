package com.wenmingvs.rainymood.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wenmingvs.rainymood.util.ActivityCollector;


public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		ActivityCollector.finishAll();
		
	}
	
}
