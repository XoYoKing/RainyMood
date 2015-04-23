package com.wenmingvs.rainymood.slidingmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;

public class SlidingMenu extends HorizontalScrollView{
	private LinearLayout mWapper;
	private ViewGroup mMenu;
	private ViewGroup mContent;
	private int mScreenWidth;
	private int mMenuWidth;
	private int mMenuRightPadding = 70;//单位dp
	private boolean once = false;
	private boolean isOpen;
	
	/*
	 * 没有使用自定义属性就调用2个参数的构造方法
	 */
			
	public SlidingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		/*
		 * 拿到屏幕的宽度
		 */
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		mScreenWidth = outMetrics.widthPixels;
		
		/*
		 * 把50dp转换成像素值
		 */
		
		mMenuRightPadding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, context.getResources()
				.getDisplayMetrics());
		
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		
		if (!once) {
			mWapper = (LinearLayout)getChildAt(0);
			mMenu = (ViewGroup)mWapper.getChildAt(0);
			mContent = (ViewGroup)mWapper.getChildAt(1);		
			mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth- mMenuRightPadding;
			mContent.getLayoutParams().width = mScreenWidth;
			once = true;
		}
			
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_UP:
			// 隐藏在左边的宽度
			int scrollX = getScrollX();
			if (scrollX >= mMenuWidth *0.5)
			{
				this.smoothScrollTo(mMenuWidth, 0);
				isOpen = false;
			} else
			{
				this.smoothScrollTo(0, 0);
				isOpen = true;
			}
			
			if (getScrollX() == 0) {
				this.smoothScrollTo(mMenuWidth, 0);
				isOpen = false;
			}
			
			
			return true;			
		}
		
		return super.onTouchEvent(ev);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			this.scrollTo(mMenuWidth, 0);
		}
		
	}
	/**
	 * 打开菜单
	 */
	public void openMenu()
	{
		if (isOpen)
			return;
		this.smoothScrollTo(0, 0);
		isOpen = true;
	}

	public void closeMenu()
	{
		if (!isOpen)
			return;
		this.smoothScrollTo(mMenuWidth, 0);
		isOpen = false;
	}

	/**
	 * 切换菜单
	 */
	public void toggle()
	{
		if (isOpen)
		{
			closeMenu();
		} else
		{
			openMenu();
		}
	}
	/**
	 * 滚动发生时
	 */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		
		/**
		 * 实现抽屉效果（1）
		 */
		
		float scale = l * 1.0f /mMenuWidth;
		ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.8f);
		/**
		 * 实现更复杂的抽屉效果（2）
		 * 
		 * 区别1：内容区域1.0~0.7 缩放的效果 
		 * 所以scale调整为 : 1.0~0.0 0.7 + 0.3 * scale
		 * 
		 * 区别2：菜单的偏移量需要修改
		 * 
		 * 区别3：菜单的显示时有缩放以及透明度变化 缩放：0.7 ~1.0 1.0 - scale * 0.3 透明度 0.6 ~ 1.0 
		 * 所以scale调整为 :0.6+ 0.4 * (1- scale) ;
		 */
		
		float rightScale = 0.7f + 0.3f * scale;
		
		// 1.设置内容区域的缩放效果
		ViewHelper.setPivotX(mContent, 0);
		ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
		ViewHelper.setScaleX(mContent, rightScale);
		ViewHelper.setScaleY(mContent, rightScale);		
		
		//2.实现菜单的透明度
		float leftAlpha = 0.6f + 0.4f * (1 - scale);
		ViewHelper.setAlpha(mMenu, leftAlpha);
		
		//3.实现菜单的缩放效果
		float leftScale = 1.0f - scale * 0.3f;
		ViewHelper.setScaleX(mMenu, leftScale);
		ViewHelper.setScaleY(mMenu, leftScale);
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
