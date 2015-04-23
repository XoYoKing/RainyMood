package com.wenmingvs.rainymood.seekbarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

@SuppressLint("NewApi")
public class SeekbarArea extends LinearLayout {

	
	
	public SeekbarArea(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public SeekbarArea(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		boolean ret = super.dispatchTouchEvent(ev);
        if(ret) //如果触摸事件是一直往下传递的话，我们阶段他，不让他继续往下传递
        {
          requestDisallowInterceptTouchEvent(true);//不允许此linelayout的父类和他的祖父类处理touchevent事件
        }
        return ret;
	}
	
}
