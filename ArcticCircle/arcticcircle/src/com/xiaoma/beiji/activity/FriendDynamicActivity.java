/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015 版权所有
 */
package com.xiaoma.beiji.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.fragment.FriendDynamicFragment;

/**
 * 类名称： FriendDynamicActivity
 * 类描述： 显示朋友的动态列表
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月09 23:06
 * 修改备注：
 *
 * @version 1.0.0
 */
public class FriendDynamicActivity extends SimpleBaseActivity {
    private static final String TAG = FriendDynamicActivity.class.getSimpleName();
    private String releaseUserId;
    private FragmentManager mFragmentManager;
    boolean b = false;

    @Override
    protected String getActivityTitle() {
        String title = "TA的动态";
        if(b){
            title = "我的动态";
        }
        return title;
    }

    @Override
    protected void initIntent() {
        super.initIntent();
        releaseUserId = getIntent().getStringExtra("releaseUserId");
        b = getIntent().getBooleanExtra("b",false);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_friend_dynamic;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFragmentManager = getSupportFragmentManager();

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initComponents() {
        setTitleControlsInfo();
        showFragment();
    }

    @Override
    protected void loadData() {

    }

    private void showFragment() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment fragment = Fragment.instantiate(this, FriendDynamicFragment.class.getName(), getIntent().getExtras());
        ft.add(R.id.container, fragment, fragment.getClass().getSimpleName());

        ft.show(fragment);
        ft.commitAllowingStateLoss();
    }
}