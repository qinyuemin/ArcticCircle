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
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;
import com.makeapp.android.util.ViewUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.adapter.FragmentAdapter;
import com.xiaoma.beiji.base.SimpleFragment;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.view.PagerSlidingTabStrip;
import com.xiaoma.beiji.controls.view.zxing.CaptureActivity;
import com.xiaoma.beiji.manager.chatting.IMXmppManager;
import com.xiaoma.beiji.util.DisplayUtils;
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
//    private TextView mMsgSystemTextView;
//    private TextView mMsgPrivateTextView;
//    private TabLayout mTableLayout;
    private PagerSlidingTabStrip mTableLayout;
    private FragmentAdapter adapter;
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

//        adapter = new MessageCenterAdapter(getFragmentActivity().getSupportFragmentManager());
//        mMsgSystemTextView = (TextView) v.findViewById(R.id.message_system_txt);
//        mMsgPrivateTextView = (TextView) v.findViewById(R.id.message_private_txt);
        mTableLayout = (PagerSlidingTabStrip) v.findViewById(R.id.title_tab_layout);
        List<String> titles = new ArrayList<>();
        titles.add("点评");
        titles.add("问问");
        //初始化TabLayout的title
//        mTableLayout.addTab(mTableLayout.newTab().setText(titles.get(0)));
//        mTableLayout.addTab(mTableLayout.newTab().setText(titles.get(1)));
        //初始化ViewPager的数据集
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new FriendDynamicFragment());
        fragments.add(new FriendHelpFragment());
        //创建ViewPager的adapter
        adapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), fragments, titles);
        mViewPager = (ViewPager) v.findViewById(R.id.vPager);
        mViewPager.setAdapter(adapter);
        mTableLayout.setViewPager(mViewPager);
//        mTableLayout.setupWithViewPager(mViewPager);
//        mTableLayout.setTabsFromPagerAdapter(adapter);

        mViewPager.setOnPageChangeListener(this);

//        mMsgSystemTextView.setOnClickListener(this);
//        mMsgPrivateTextView.setOnClickListener(this);

        ViewUtil.setViewOnClickListener(v, R.id.img_publish, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                IntentUtil.goTrendsPublishActivity(getFragmentActivity(),currentPosition+1);
                PopupWindow popupWindow = new MainPublishPopView(getFragmentActivity(), ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_emotion));
                popupWindow.setOutsideTouchable(true);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        ViewUtil.setViewBackgroundResource(v, R.id.img_publish, R.drawable.ic_edit);
                    }
                });
                popupWindow.showAsDropDown(ViewUtil.findViewById(v, R.id.img_publish), -DisplayUtils.dp2px(getContext(),20f),0);
                ViewUtil.setViewBackgroundResource(v,R.id.img_publish,R.drawable.ic_scanning);
            }
        });

//        ViewUtil.setViewOnClickListener(v, R.id.img_scanning, new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                IntentUtil.goScanActivity(CircleFragment.this);
//            }
//        });

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


    @Override
    public void onPageScrollStateChanged(int position) {
    }

    @Override
    public void onPageScrolled(int position, float arg1, int arg2) {
        currentPosition = position;
        String rightTitleText = "";
        switch (currentPosition) {
            case 0:
                break;
            case 1:
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
