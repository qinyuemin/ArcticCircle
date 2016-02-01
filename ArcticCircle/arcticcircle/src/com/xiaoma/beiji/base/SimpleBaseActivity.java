/**
 * 项目名： eyah
 * 包名： com.sttri.eyah.base
 * 版本信息： 1.0.0
 * Copyright (c) -版权所有
 */
package com.xiaoma.beiji.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import com.common.android.lib.base.BaseActivity;
import com.xiaoma.beiji.common.AppConstants;
import com.xiaoma.beiji.controls.dialog.LoginTimeoutDialog;

/**
 *
 * 类名称： SimpleBaseActivity
 * 类描述： 
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年06月08 10:42
 * 修改备注：
 * @version 1.0.0
 *
 */
public abstract class SimpleBaseActivity extends BaseActivity {
    private GlobalReceiver mGlobalReceiver;
    private LoginTimeoutDialog timeoutDialog;

    @Override
    protected void onPause() {
        super.onPause();
        unregisterGlobalReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerGlobalListener();
    }

    private void registerGlobalListener() {
        if (null == mGlobalReceiver) {
            mGlobalReceiver = new GlobalReceiver();
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(AppConstants.IntentActionConstant.ACTION_LOGIN_TIMEOUT);
        this.registerReceiver(mGlobalReceiver, filter);
    }

    private void unregisterGlobalReceiver() {
        if (null != mGlobalReceiver) {
            this.unregisterReceiver(mGlobalReceiver);
        }
    }

    private class GlobalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(AppConstants.IntentActionConstant.ACTION_LOGIN_TIMEOUT.equals(action)) {
                // 登录超时
                if (timeoutDialog == null) {
                    timeoutDialog = new LoginTimeoutDialog(SimpleBaseActivity.this);
                }
                if (!timeoutDialog.isShowing()) {
                    timeoutDialog.show();
                }
            }
        }
    }
}