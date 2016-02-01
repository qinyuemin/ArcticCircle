/**
 * 项目名： simpleApp
 * 包名： com.company.simple.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015
 */
package com.xiaoma.beiji.activity;

import android.view.View;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.util.IntentUtil;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ViewUtil;

/**
 *
 * 类名称： RegisterFinishActivity
 * 类描述： 
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年11月16 17:59
 * 修改备注：
 * @version 1.0.0
 *
 */
public class RegisterFinishActivity extends SimpleBaseActivity implements View.OnClickListener {
    private static final String TAG = RegisterFinishActivity.class.getSimpleName();

    @Override
    protected String getActivityTitle() {
        return "注册";
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_register_finish;
    }
    @Override
    protected void setTitleControlsInfo() {
        super.setTitleControlsInfo();
        ViewUtil.setViewVisibility(this, R.id.title_bar_left_sub_txt, View.VISIBLE);
        ViewUtil.setViewVisibility(this, R.id.title_bar_left_img, View.GONE);
        TextViewUtil.setText(this, R.id.title_bar_left_sub_txt, "取消");
    }

    @Override
    protected void initComponents() {
        setTitleControlsInfo();
        ViewUtil.setViewOnClickListener(this, R.id.btn_next, this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                IntentUtil.goMainActivity(this);
                finish();
                break;
        }
    }
}