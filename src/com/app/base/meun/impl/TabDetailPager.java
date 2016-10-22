package com.app.base.meun.impl;

import com.app.base.BaseMenuDetailPager;
import com.app.base.pojo.NewsMenu.NewsTabData;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * 页签页面对象
 * 
 * @author Kevin
 * @date 2015-10-20
 */
public class TabDetailPager extends BaseMenuDetailPager {
	private NewsTabData mTabData;// 单个页签的网络数据
	private TextView view;

	public TabDetailPager(Activity activity,NewsTabData newsTabData) {
		super(activity);
		mTabData = newsTabData;
	}

	@Override
	public View initView() {
		view = new TextView(mActivity);
		 //view.setText(mTabData.title);//此处空指针
		
		 view.setTextColor(Color.RED);
		 view.setTextSize(22);
		 view.setGravity(Gravity.CENTER);
		 return view;
	}
}
