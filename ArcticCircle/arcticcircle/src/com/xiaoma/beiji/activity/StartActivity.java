/**
 * 项目名： simpleApp
 * 包名： com.company.simple.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015
 */
package com.xiaoma.beiji.activity;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.common.android.lib.controls.view.indicator.IconPageIndicator;
import com.common.android.lib.controls.view.indicator.IconPagerAdapter;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.IntentUtil;
import com.makeapp.android.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称： StartActivity
 * 类描述： 起始页
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年11月16 13:57
 * 修改备注：
 *
 * @version 1.0.0
 */
public class StartActivity extends SimpleBaseActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private MyPagerAdapter mPagerAdapter;
    private List<View> mListViews;
    private List<Integer> datas;
    private IconPageIndicator mIndicator;

    @Override
    protected String getActivityTitle() {
        return null;
    }

    @Override
    protected int getTitleBarId() {
        return -1;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_start;
    }

    @Override
    protected void initComponents() {
        ViewUtil.setViewOnClickListener(this, R.id.btn_login, this);
        ViewUtil.setViewOnClickListener(this, R.id.btn_register, this);

        mListViews = new ArrayList<>();
        datas = new ArrayList<>();
        datas.add(0);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mPagerAdapter = new MyPagerAdapter(mListViews);
        mViewPager.setAdapter(mPagerAdapter);
        mIndicator = (IconPageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mViewPager);
        mViewPager.setCurrentItem(0);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                mIndicator.setCurrentItem(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                // 注册
                IntentUtil.goUserRegisterActivity(this);
                break;
            case R.id.btn_login:
                // 登陆
                IntentUtil.goUserLoginActivity(this);
                break;
        }
    }

    public class MyPagerAdapter extends PagerAdapter implements IconPagerAdapter {
        public List<View> mListViews;

        public MyPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            //回收imageview占用的图片
             /* if (object instanceof ImageView) {
				BitmapDrawable bitmapDrawable = (BitmapDrawable) ((ImageView) object)
						.getDrawable();
				if (bitmapDrawable.getBitmap().isRecycled()){
					bitmapDrawable.getBitmap().recycle();
				}
			}*/
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            //return mListViews == null ? 0 : mListViews.size();
            return datas.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView img = new ImageView(StartActivity.this);
            img.setImageResource(R.drawable.ic_welcome);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ViewPager.LayoutParams params = new ViewPager.LayoutParams();
            params.height = ViewPager.LayoutParams.MATCH_PARENT;
            params.width = ViewPager.LayoutParams.MATCH_PARENT;
            container.addView(img, 0, params);
            return img;
        }


        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }

        @Override
        public int getIconResId(int index) {
            return R.drawable.sl_circle_indicator;
        }
    }
}