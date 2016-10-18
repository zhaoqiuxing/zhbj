package com.app.base.impl;

import com.app.base.BasePager;
import com.app.base.pojo.NewsMenu;
import com.app.constants.GlobalConstants;
import com.app.fragment.LeftMenuFragment;
import com.app.utils.CacheUtils;
import com.app.zhbj.MainActivity;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

/**
 * 新闻中心
 * 
 * @author Kevin
 * @date 2015-10-18
 */
public class NewsCenterPager extends BasePager {
	private NewsMenu mNewsData;// 分类信息网络数据

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
		// 先判断有没有缓存,如果有的话,就加载缓存
		String cache = CacheUtils.getCache(GlobalConstants.CATEGORY_URL, mActivity);
		if (!TextUtils.isEmpty(cache)) {
			System.out.println("发现缓存啦...");
			processData(cache);
		}

		// 请求服务器,获取数据
		// 开源框架: XUtils
		getDataFromServer();
	}

	private void processData(String json) {
		// Gson: Google Json
		Gson gson = new Gson();
		mNewsData = gson.fromJson(json, NewsMenu.class);
		System.out.println("解析结果:" + mNewsData);

		// 获取侧边栏对象
		MainActivity mainUI = (MainActivity) mActivity;
		LeftMenuFragment fragment = mainUI.getLeftMenuFragment();

		// 给侧边栏设置数据
		fragment.setMenuData(mNewsData.data);

	}

	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, GlobalConstants.CATEGORY_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// 请求成功
				String result = responseInfo.result;// 获取服务器返回结果
				System.out.println("服务器返回结果:" + result);
				// JsonObject, Gson
				processData(result);

				// 写缓存
				CacheUtils.setCache(GlobalConstants.CATEGORY_URL, result, mActivity);

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// 请求失败
				error.printStackTrace();
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
			}

		});

	}

}
