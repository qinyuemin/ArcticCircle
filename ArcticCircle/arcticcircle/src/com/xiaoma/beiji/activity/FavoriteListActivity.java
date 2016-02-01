/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import com.makeapp.android.util.ViewUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.fragment.FavoriteFriendDynamicFragment;
import com.xiaoma.beiji.fragment.FavoriteFriendShopFragment;
import com.xiaoma.beiji.util.IntentUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 类名称： FavoriteListActivity
 * 类描述： 我的收藏
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月04 0:03
 * 修改备注：
 * @version 1.0.0
 *
 */
public class FavoriteListActivity extends SimpleBaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private static final String TAG = FavoriteListActivity.class.getSimpleName();

    private ViewPager mViewPager;
    private TextView txt1;
    private TextView txt2;
    private View line1,line2;

    private FragmentPagerAdapter adapter;
    private FavoriteFriendDynamicFragment dynamicFragment;
    private FavoriteFriendShopFragment shopFragment;
    private int currentPosition = 0;
    private List<String> CONTENT = new ArrayList<String>();

    @Override
    protected String getActivityTitle() {
        return null;
    }

    @Override
    protected void setTitleControlsInfo() {
        super.setTitleControlsInfo();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_favorite_list;
    }

    @Override
    protected void initComponents() {
        CONTENT.add("朋友动态");
        CONTENT.add("店铺");

        adapter = new MessageCenterAdapter(this.getSupportFragmentManager());
        txt1 = (TextView) findViewById(R.id.message_system_txt);
        txt2 = (TextView) findViewById(R.id.message_private_txt);
        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);

        mViewPager = (ViewPager) findViewById(R.id.vPager);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(this);

        txt1.setOnClickListener(this);
        txt2.setOnClickListener(this);

        ViewUtil.setViewOnClickListener(this, R.id.title_bar_title_txt, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void loadData() {
    }

    class MessageCenterAdapter extends FragmentPagerAdapter {
        public MessageCenterAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    dynamicFragment = new FavoriteFriendDynamicFragment();
                    return dynamicFragment;
                case 1:
                    return shopFragment = new FavoriteFriendShopFragment();
            }
            return dynamicFragment = new FavoriteFriendDynamicFragment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT.get(position);
        }

        @Override
        public int getCount() {
            return CONTENT.size();
        }
    }

    @Override
    public void onPageScrollStateChanged(int position) {
    }

    @Override
    public void onPageScrolled(int position, float arg1, int arg2) {
        setTabColor(position);
        currentPosition = position;
        String rightTitleText = "";
        switch (currentPosition) {
            case 0:
                break;
            case 1:
                break;
        }
    }

    private void setTabColor(int position) {
        switch (position) {
            case 0:
                txt1.setTextColor(getResources().getColor(R.color.white));
                txt2.setTextColor(getResources().getColor(R.color.white));
                line1.setVisibility(View.VISIBLE);
                line2.setVisibility(View.INVISIBLE);
                break;
            case 1:
                txt2.setTextColor(getResources().getColor(R.color.white));
                txt1.setTextColor(getResources().getColor(R.color.white));
                line1.setVisibility(View.INVISIBLE);
                line2.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
    }

    @Override
    public void onClick(View view) {
        int position = -1;
        boolean isPageChange = false;
        switch (view.getId()) {
            case R.id.message_system_txt:
                position = 0;
                isPageChange = true;
                break;
            case R.id.message_private_txt:
                position = 1;
                isPageChange = true;
                break;
        }

        if (isPageChange) {
            mViewPager.setCurrentItem(position);
        }

    }
}