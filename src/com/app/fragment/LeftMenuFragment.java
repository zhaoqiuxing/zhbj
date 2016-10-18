package com.app.fragment;

import java.util.ArrayList;

import com.app.base.pojo.NewsMenu.NewsMenuData;
import com.app.zhbj.MainActivity;
import com.app.zhbj.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 侧边栏fragment
 * 
 * @author Kevin
 * @date 2015-10-18
 */
public class LeftMenuFragment extends BaseFragment {
	@ViewInject(R.id.lv_list)
	private ListView lvList;

	private ArrayList<NewsMenuData> mNewsMenuData;// 侧边栏网络数据对象

	private int mCurrentPos;// 当前被选中的item的位置

	private LeftMenuAdapter mAdapter;
	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
		return view;
	}

	@Override
	public void initData() {
	}

	public void setMenuData(ArrayList<NewsMenuData> data) {
		mCurrentPos = 0;// 当前选中的位置归零

		// 更新页面
		mNewsMenuData = data;

		mAdapter = new LeftMenuAdapter();
		lvList.setAdapter(mAdapter);

		lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mCurrentPos = position;// 更新当前被选中的位置
				mAdapter.notifyDataSetChanged();// 刷新listview

				// 收起侧边栏
				toggle();

				// 侧边栏点击之后, 要修改新闻中心的FrameLayout中的内容
				//setCurrentDetailPager(position);
			}
		});
	}

	/**
	 * 打开或者关闭侧边栏
	 */
	protected void toggle() {
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		slidingMenu.toggle();// 如果当前状态是开, 调用后就关; 反之亦然
	}

	class LeftMenuAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mNewsMenuData.size();
		}

		@Override
		public NewsMenuData getItem(int position) {
			return mNewsMenuData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mActivity, R.layout.list_item_left_menu, null);
			TextView tvMenu = (TextView) view.findViewById(R.id.tv_menu);

			NewsMenuData item = getItem(position);
			tvMenu.setText(item.title);

			if (position == mCurrentPos) {
				// 被选中
				tvMenu.setEnabled(true);// 文字变为红色
			} else {
				// 未选中
				tvMenu.setEnabled(false);// 文字变为白色
			}

			return view;
		}

	}
}
