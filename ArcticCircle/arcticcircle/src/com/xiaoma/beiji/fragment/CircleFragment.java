/**
 * 项目名： eyah
 * 包名： com.sttri.eyah.fragment
 * 版本信息： 1.0.0
 * Copyright (c) -版权所有
 */
package com.xiaoma.beiji.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import com.makeapp.android.util.ViewUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleFragment;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.view.zxing.CaptureActivity;
import com.xiaoma.beiji.manager.chatting.IMXmppManager;
import com.xiaoma.beiji.util.IntentUtil;
import com.xiaoma.beiji.util.ToastUtil;
import org.jivesoftware.smack.XMPPException;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称： CircleFragment
 * 类描述：   首页
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年06月08 11:54
 * 修改备注：
 *
 * @version 1.0.0
 */
public class CircleFragment extends SimpleFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private TextView mMsgSystemTextView;
    private TextView mMsgPrivateTextView;
    private View line1,line2;

    private FragmentPagerAdapter adapter;
    private FriendDynamicFragment friendTrendsFragment;
    private int currentPosition = 0;
    private List<String> CONTENT = new ArrayList<>();

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_circle;
    }

    protected void setTitleControlsInfo(View v) {

    }

    @Override
    protected void initComponents(final View v) {
        CONTENT.add("朋友求助");
        CONTENT.add("朋友动态");

        adapter = new MessageCenterAdapter(getFragmentActivity().getSupportFragmentManager());
        mMsgSystemTextView = (TextView) v.findViewById(R.id.message_system_txt);
        mMsgPrivateTextView = (TextView) v.findViewById(R.id.message_private_txt);
        line1 = v.findViewById(R.id.line1);
        line2 = v.findViewById(R.id.line2);

        mViewPager = (ViewPager) v.findViewById(R.id.vPager);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(this);

        mMsgSystemTextView.setOnClickListener(this);
        mMsgPrivateTextView.setOnClickListener(this);

        ViewUtil.setViewOnClickListener(v, R.id.img_publish, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.goTrendsPublishActivity(getFragmentActivity(),currentPosition+1);
            }
        });

        ViewUtil.setViewOnClickListener(v, R.id.img_scanning, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.goScanActivity(CircleFragment.this);
            }
        });

        ViewUtil.setViewOnClickListener(v, R.id.img_search, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.goSearchActivity(getFragmentActivity());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        mViewPager.setCurrentItem(0);
//        setTabColor(0);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        try {
            adapter.getItem(currentPosition).onHiddenChanged(hidden);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            Bundle bundle = new Bundle();
//            bundle.putString("releaseUserId", ""+Global.getUserId());
            switch (position) {
                case 0:
                    friendTrendsFragment = new FriendDynamicFragment();
                    friendTrendsFragment.setArguments(bundle);
                    return friendTrendsFragment;
                case 1:
                    return FriendHelpFragment.newInstance(bundle);
            }
            return friendTrendsFragment = new FriendDynamicFragment();
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
                mMsgSystemTextView.setTextColor(getResources().getColor(R.color.white));
                mMsgPrivateTextView.setTextColor(getResources().getColor(R.color.white));
                line1.setVisibility(View.VISIBLE);
                line2.setVisibility(View.INVISIBLE);
                break;
            case 1:
                mMsgPrivateTextView.setTextColor(getResources().getColor(R.color.white));
                mMsgSystemTextView.setTextColor(getResources().getColor(R.color.white));
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
//            setTabColor(position);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK ) {
            if(requestCode == CaptureActivity.INTENT_REQUEST_CODE){
                String codeStart = "bjqshop";
                String code = data.getStringExtra("result");
                int codeStart1 = code.indexOf(codeStart);
                if(codeStart1 == -1){
                    ToastUtil.showToast(getFragmentActivity(),"非法店铺");
                    return;
                }
                String shopId = code.substring(codeStart1 + codeStart.length());
                IntentUtil.goShopMainActivity(getFragmentActivity(),shopId);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
