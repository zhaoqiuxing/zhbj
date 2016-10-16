package com.app.zhbj;

import com.app.utils.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

public class SplashActivity extends Activity {

	private RelativeLayout rlRoot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
		RotateAnimation animRotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		animRotate.setDuration(1000);// ����ʱ��
		animRotate.setFillAfter(true);// ���ֶ�������״̬
		// ���Ŷ���
		ScaleAnimation animScale = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		animScale.setDuration(1000);
		animScale.setFillAfter(true);// ���ֶ�������״̬

		// ���䶯��
		AlphaAnimation animAlpha = new AlphaAnimation(0, 1);
		animAlpha.setDuration(2000);// ����ʱ��
		animAlpha.setFillAfter(true);// ���ֶ�������״̬

		// ��������
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(animRotate);
		set.addAnimation(animScale);
		set.addAnimation(animAlpha);
		
		rlRoot.startAnimation(set);
		
		set.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				boolean isFirst = PrefUtils.getBoolean(SplashActivity.this, "is_first_enter", true);
				Intent intent;
				if(isFirst){
					intent = new Intent(getApplicationContext(),
							GuideActivity.class);
				}else{
					intent = new Intent(getApplicationContext(),
							MainActivity.class);
				}
				startActivity(intent);
				finish();
			}
		});
	}
}
