package com.wenmingvs.rainymood.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class MySeekBar extends SeekBar {

	public MySeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.onTouchEvent(event);
	}
}
