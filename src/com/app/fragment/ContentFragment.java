package com.app.fragment;

import com.app.zhbj.R;

import android.view.View;

/**
 * 主页面fragment
 * 
 * @author Kevin
 * @date 2015-10-18
 */
public class ContentFragment extends BaseFragment {

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_content, null);
		return view;
	}

	@Override
	public void initData() {
	
	}

}