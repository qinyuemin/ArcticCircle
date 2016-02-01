/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ViewUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.controls.dialog.FriendlyLevelDialog;
import com.xiaoma.beiji.controls.view.SettingItemView;
import com.xiaoma.beiji.entity.DepthEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.IntentUtil;
import com.xiaoma.beiji.util.ToastUtil;

/**
 * 类名称： SettingActivity
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月02 23:21
 * 修改备注：
 *
 * @version 1.0.0
 */
public class SettingActivity extends SimpleBaseActivity implements View.OnClickListener {
    private static final String TAG = SettingActivity.class.getSimpleName();

    private SettingItemView stv1;
    private SettingItemView stv2;
    private SettingItemView stv3;

    private FriendlyLevelDialog friendlyLevelDialog = null;

    private DepthEntity depthEntity;

    @Override
    protected String getActivityTitle() {
        return "个人隐私设置";
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initComponents() {
        setTitleControlsInfo();

        ViewUtil.setViewOnClickListener(this, R.id.stv_blacklist, this);
        ViewUtil.setViewOnClickListener(this, R.id.stv_like, this);

        stv1 = (SettingItemView) findViewById(R.id.stv_attention1);
        stv2 = (SettingItemView) findViewById(R.id.stv_attention2);
        stv3 = (SettingItemView) findViewById(R.id.stv_attention3);

        stv1.setOnClickListener(this);
        stv2.setOnClickListener(this);
        stv3.setOnClickListener(this);
    }

    @Override
    protected void loadData() {
        HttpClientUtil.User.userGetReadDepth(new AbsHttpResultHandler<DepthEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, DepthEntity data) {
                depthEntity = data;
                stv1.setSubLabel(data.getDynamicDepthTitle());
                stv2.setSubLabel(data.getHelpDepthTitle());
                stv3.setSubLabel(data.getChatDepthTitle());
            }

            @Override
            public void onFailure(int resultCode, String desc) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stv_blacklist:
                IntentUtil.goBlacklistActivity(this,1);
                break;
            case R.id.stv_like:
                IntentUtil.goBlacklistActivity(this,2);
                break;
            case R.id.stv_attention1:
                // 我关注谁 动态
                friendlyLevelDialog = new FriendlyLevelDialog(this, new FriendlyLevelDialog.OnSelectListener() {
                    @Override
                    public void onItemClick(int i, String s) {
                        stv1.setSubLabel(s);
                        depthEntity.setDynamicDepth(i);
                        submit();
                    }
                });
                if (!friendlyLevelDialog.isShowing()) {
                    friendlyLevelDialog.show();
                }
                break;
            case R.id.stv_attention2:
                // 我关注谁 求助
                friendlyLevelDialog = new FriendlyLevelDialog(this, new FriendlyLevelDialog.OnSelectListener() {
                    @Override
                    public void onItemClick(int i, String s) {
                        stv2.setSubLabel(s);
                        depthEntity.setHelpDepth(i);
                        submit();
                    }
                });
                if (!friendlyLevelDialog.isShowing()) {
                    friendlyLevelDialog.show();
                }
                break;
            case R.id.stv_attention3:
                // 谁可以私聊我
                friendlyLevelDialog = new FriendlyLevelDialog(this, new FriendlyLevelDialog.OnSelectListener() {
                    @Override
                    public void onItemClick(int i, String s) {
                        stv3.setSubLabel(s);
                        depthEntity.setChatDepth(i);
                        submit();
                    }
                });
                if (!friendlyLevelDialog.isShowing()) {
                    friendlyLevelDialog.show();
                }
                break;
        }
    }

    private void submit() {
        HttpClientUtil.User.userEditReadDepth(depthEntity.getDynamicDepth(), depthEntity.getHelpDepth(), depthEntity.getChatDepth(), new AbsHttpResultHandler() {
            @Override
            public void onSuccess(int resultCode, String desc, Object data) {

            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(SettingActivity.this, "修改失败:" + desc);
            }
        });
    }
}