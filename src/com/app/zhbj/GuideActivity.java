package com.app.zhbj;

import java.util.ArrayList;

import com.app.utils.DensityUtils;
import com.app.utils.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class GuideActivity extends Activity {
	private ViewPager mViewPager;
	private LinearLayout llContainer;
	private ImageView ivRedPoint;// 小红点
	private Button btnStart;
	// 小红点移动距离
	private int mPointDis;
	private ArrayList<ImageView> mImageViewList; // imageView集合

	// 引导页图片id数组
	private int[] mImageIds = new int[] { R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题,
		setContentView(R.layout.activity_guide);
		mViewPager = (ViewPager) findViewById(R.id.vp_guide);
		llContainer = (LinearLayout) findViewById(R.id.ll_container);
		ivRedPoint = (ImageView) findViewById(R.id.iv_red_point);
		btnStart = (Button) findViewById(R.id.btn_start);
		initData();// 先初始化数据
		mViewPager.setAdapter(new GuideAdapter());// 设置数据
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// 某个页面被选中
				if (position == mImageViewList.size() - 1) {// 最后一个页面显示开始体验的按钮
					btnStart.setVisibility(View.VISIBLE);
				} else {
					btnStart.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// 当页面滑动过程中的回调
				System.out.println("当前位置:" + position + ";移动偏移百分比:" + positionOffset);
				// 更新小红点距离
				int leftMargin = (int) (mPointDis * positionOffset) + position * mPointDis;// 计算小红点当前的左边距
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
				params.leftMargin = leftMargin;// 修改左边距

				// 重新设置布局参数
				ivRedPoint.setLayoutParams(params);

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				// 移除监听,避免重复回调
				ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				mPointDis = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0).getLeft();
				System.out.println("圆点距离:" + mPointDis);
			}
		});
		
		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 更新sp, 已经不是第一次进入了
				PrefUtils.setBoolean(getApplicationContext(), "is_first_enter", false);

				// 跳到主页面
				startActivity(new Intent(getApplicationContext(), MainActivity.class));
				finish();
			}
		});
	}

	// 初始化数据
	private void initData() {
		mImageViewList = new ArrayList<ImageView>();
		for (int i = 0; i < mImageIds.length; i++) {
			ImageView view = new ImageView(this);
			view.setBackgroundResource(mImageIds[i]);// 通过设置背景,可以让宽高填充布局
			// view.setImageResource(resId)
			mImageViewList.add(view);

			// 初始化小圆点
			ImageView point = new ImageView(this);
			point.setImageResource(R.drawable.shape_point_gray);// 设置图片(shape形状)

			// 初始化布局参数, 宽高包裹内容,父控件是谁,就是谁声明的布局参数
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			if (i > 0) {
				// 从第二个点开始设置左边距
				params.leftMargin = DensityUtils.dip2px(10, this);
			}

			point.setLayoutParams(params);// 设置布局参数

			llContainer.addView(point);// 给容器添加圆点
		}
	}

	class GuideAdapter extends PagerAdapter {
		// item的个数
		@Override
		public int getCount() {
			return mImageViewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		// 初始化item布局
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = mImageViewList.get(position);
			container.addView(view);
			return view;
		}

		// 销毁item
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

}
