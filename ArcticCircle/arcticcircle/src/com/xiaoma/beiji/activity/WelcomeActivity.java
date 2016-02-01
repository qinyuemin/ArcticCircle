/**
 * 项目名： consumer
 * 包名： com.android.consumer.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015
 */
package com.xiaoma.beiji.activity;

import android.os.AsyncTask;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.manager.LocationManager;
import com.xiaoma.beiji.util.IntentUtil;

/**
 *
 * 类名称： WelcomeActivity
 * 类描述： 
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年08月11 11:18
 * 修改备注：
 * @version 1.0.0
 *
 */
public class WelcomeActivity extends SimpleBaseActivity {
    private static final String TAG = WelcomeActivity.class.getSimpleName();

    @Override
    protected String getActivityTitle() {
        return null;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initComponents() {
    }

    @Override
    protected void loadData() {
        LocationManager.getInstance().startLocation();
        new AsyncTask(){
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    Thread.sleep(1 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                if (Global.isLogin()) {
                    IntentUtil.goMainActivity(WelcomeActivity.this);
                }else{
                    IntentUtil.goStartActivity(WelcomeActivity.this);
                }
                finish();
            }
        }.execute();
    }
}