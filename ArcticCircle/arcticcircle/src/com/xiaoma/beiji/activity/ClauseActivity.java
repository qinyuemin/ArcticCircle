/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.activity;

import android.app.Activity;
import android.os.Bundle;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;

/**
 * 类名称： ClauseActivity
 * 类描述： 条款
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月18 15:44
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ClauseActivity extends SimpleBaseActivity {
    private static final String TAG = ClauseActivity.class.getSimpleName();

    @Override
    protected String getActivityTitle() {
        return "北极圈服务条款";
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_clause;
    }

    @Override
    protected void initComponents() {
        setTitleControlsInfo();
    }

    @Override
    protected void loadData() {

    }
}