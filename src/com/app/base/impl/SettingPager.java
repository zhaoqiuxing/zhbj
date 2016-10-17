package com.app.base.impl;


import com.app.base.BasePager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * 设置
 * 
 * @author Kevin
 * @date 2015-10-18
 */
public class SettingPager extends BasePager {

	public SettingPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		System.out.println("设置初始化啦...");

		// 要给帧布局填充布局对象
		TextView view = new TextView(mActivity);
		view.setText("设置");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);

		flContent.addView(view);

	}

}
