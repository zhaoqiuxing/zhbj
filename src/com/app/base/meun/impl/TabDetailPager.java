package com.app.base.meun.impl;

import java.util.ArrayList;

import com.app.base.BaseMenuDetailPager;
import com.app.base.pojo.NewsMenu.NewsTabData;
import com.app.base.pojo.NewsTabBean;
import com.app.base.pojo.NewsTabBean.NewsData;
import com.app.base.pojo.NewsTabBean.TopNews;
import com.app.constants.GlobalConstants;
import com.app.utils.CacheUtils;
import com.app.view.TopNewsViewPager;
import com.app.zhbj.R;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 页签页面对象
 * 
 * @author Kevin
 * @date 2015-10-20
 */
public class TabDetailPager extends BaseMenuDetailPager {

	private NewsTabData mTabData;// 单个页签的网络数据
	// private TextView view;

	@ViewInject(R.id.vp_top_news)
	private TopNewsViewPager mViewPager;

	@ViewInject(R.id.indicator)
	private CirclePageIndicator mIndicator;

	@ViewInject(R.id.tv_title)
	private TextView tvTitle;

	@ViewInject(R.id.lv_list)
	private ListView lvList;

	private String mUrl;

	private ArrayList<TopNews> mTopNews;
	private ArrayList<NewsData> mNewsList;

	private NewsAdapter mNewsAdapter;

	public TabDetailPager(Activity activity, NewsTabData newsTabData) {
		super(activity);
		mTabData = newsTabData;

		mUrl = GlobalConstants.SERVER_URL + mTabData.url;
	}

	@Override
	public View initView() {
		// 要给帧布局填充布局对象
		// view = new TextView(mActivity);
		//
		// // view.setText(mTabData.title);//此处空指针
		//
		// view.setTextColor(Color.RED);
		// view.setTextSize(22);
		// view.setGravity(Gravity.CENTER);

		View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
		ViewUtils.inject(this, view);

		// 给listview添加头布局
		View mHeaderView = View.inflate(mActivity, R.layout.list_item_header,
				null);
		ViewUtils.inject(this, mHeaderView);// 此处必须将头布局也注入
		lvList.addHeaderView(mHeaderView);

		return view;
	}

	@Override
	public void initData() {
		// view.setText(mTabData.title);
		String cache = CacheUtils.getCache(mUrl, mActivity);
		if (!TextUtils.isEmpty(cache)) {
			processData(cache);
		}

		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				processData(result);

				CacheUtils.setCache(mUrl, result, mActivity);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// 请求失败
				error.printStackTrace();
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	protected void processData(String result) {
		Gson gson = new Gson();
		NewsTabBean newsTabBean = gson.fromJson(result, NewsTabBean.class);

		// 头条新闻填充数据
		mTopNews = newsTabBean.data.topnews;
		if (mTopNews != null) {
			mViewPager.setAdapter(new TopNewsAdapter());
			mIndicator.setViewPager(mViewPager);
			mIndicator.setSnap(true);// 快照方式展示

			// 事件要设置给Indicator
			mIndicator.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int position) {
					// 更新头条新闻标题
					TopNews topNews = mTopNews.get(position);
					tvTitle.setText(topNews.title);
				}

				@Override
				public void onPageScrolled(int position, float positionOffset,
						int positionOffsetPixels) {

				}

				@Override
				public void onPageScrollStateChanged(int state) {

				}
			});

			// 更新第一个头条新闻标题
			tvTitle.setText(mTopNews.get(0).title);
			mIndicator.onPageSelected(0);// 默认让第一个选中(解决页面销毁后重新初始化时,Indicator仍然保留上次圆点位置的bug)
		}

		// 列表新闻
		mNewsList = newsTabBean.data.news;
		if (mNewsList != null) {
			mNewsAdapter = new NewsAdapter();
			lvList.setAdapter(mNewsAdapter);
		}
	}

	// 头条新闻数据适配器
	class TopNewsAdapter extends PagerAdapter {

		private BitmapUtils mBitmapUtils;

		public TopNewsAdapter() {
			mBitmapUtils = new BitmapUtils(mActivity);
			mBitmapUtils
					.configDefaultLoadingImage(R.drawable.topnews_item_default);// 设置加载中的默认图片
		}

		@Override
		public int getCount() {
			return mTopNews.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = new ImageView(mActivity);
			// view.setImageResource(R.drawable.topnews_item_default);
			view.setScaleType(ScaleType.FIT_XY);// 设置图片缩放方式, 宽高填充父控件

			String imageUrl = mTopNews.get(position).topimage;// 图片下载链接

			// 下载图片-将图片设置给imageview-避免内存溢出-缓存
			// BitmapUtils-XUtils
			mBitmapUtils.display(view, imageUrl);

			container.addView(view);

			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	class NewsAdapter extends BaseAdapter {

		private BitmapUtils mBitmapUtils;

		public NewsAdapter() {
			mBitmapUtils = new BitmapUtils(mActivity);
			mBitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
		}

		@Override
		public int getCount() {
			return mNewsList.size();
		}

		@Override
		public NewsData getItem(int position) {
			return mNewsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.list_item_news,
						null);
				holder = new ViewHolder();
				holder.ivIcon = (ImageView) convertView
						.findViewById(R.id.iv_icon);
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.tvDate = (TextView) convertView
						.findViewById(R.id.tv_date);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			NewsData news = getItem(position);
			holder.tvTitle.setText(news.title);
			holder.tvDate.setText(news.pubdate);

			mBitmapUtils.display(holder.ivIcon, news.listimage);

			return convertView;
		}

	}

	static class ViewHolder {
		public ImageView ivIcon;
		public TextView tvTitle;
		public TextView tvDate;
	}
}
