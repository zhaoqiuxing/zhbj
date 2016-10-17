package com.app.base.impl;

import com.app.base.BasePager;

import android.app.Activity;
import android.view.View;

/**
 * 新闻中心
 * 
 * @author Kevin
 * @date 2015-10-18
 */
public class NewsCenterPager extends BasePager {

	public NewsCenterPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		System.out.println("新闻中心初始化啦...");
		// 修改页面标题
		tvTitle.setText("新闻");
		// 显示菜单按钮
		btnMenu.setVisibility(View.VISIBLE);
	}


}
