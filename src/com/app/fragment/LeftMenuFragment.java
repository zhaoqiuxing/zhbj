package com.app.fragment;


import com.app.zhbj.R;

import android.view.View;

/**
 * 侧边栏fragment
 * 
 * @author Kevin
 * @date 2015-10-18
 */
public class LeftMenuFragment extends BaseFragment {

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
		return view;
	}

	@Override
	public void initData() {
	}

}
