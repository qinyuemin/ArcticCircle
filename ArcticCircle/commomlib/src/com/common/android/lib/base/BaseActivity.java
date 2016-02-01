/**
 * 项目名：LuluYouLib
 * 包名：com.luluyou.android.lib.ui
 * 文件名：BaseActivity.java
 * 版本信息：1.0.0
 * 创建日期：2013年12月6日-下午4:13:56
 * 创建人：jerry.deng
 * Copyright (c) 2013上海路路由信息科技有限公司-版权所有
 */
package com.common.android.lib.base;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import android.widget.TextView;
import com.common.android.lib.R;
import com.common.android.lib.controls.dialog.CommonProgressDialog;
import com.common.android.lib.manager.ActivityManager;

/**
 * 类名称： BaseActivity
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年06月08 10:42
 * 修改备注：
 *
 * @version 1.0.0
 */
public abstract class BaseActivity extends FragmentActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    protected boolean bReloadData = false;
    protected boolean bHasNavigationBar = false;
    protected int nTitleBarId = -1;
    private Handler handler = null;

    //	private ProgressDialog loadingProgressDialog;
    private CommonProgressDialog loadingProgressDialog;

    private boolean isActived = true;

    protected ActivityManager activityManager = ActivityManager.getActivityManager();

    public BaseActivity() {
        this.handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                BaseActivity.this.handleMessage(msg);
            }
        };
    }

    /**
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TemplateApplication.instance.initApplication();
        activityManager.putActivity(this);
        getSavedDataFromBundle(savedInstanceState);
        this.setTitleBarId(this.getTitleBarId());
        this.setContentView(this.getContentLayoutId());
        this.bReloadData = this.IsReloadData();
        this.onPreInitUI();
        this.initIntent();
        this.initComponents();
        if (!bReloadData) {
            this.loadData();
        }
    }

    public void exit() {
        activityManager.exit();
    }

    public Handler getHandler() {
        return this.handler;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            this.finalizeBeforeDestroy();
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void initIntent(){}

    protected abstract String getActivityTitle();

    /**
     * getContentLayoutId(这里用一句话描述这个方法的作用）
     * （设置页面布局的id）
     *
     * @return int
     * @throws
     * @since 1.0.0
     */
    protected abstract int getContentLayoutId();

    /**
     * initComponents(这里用一句话描述这个方法的作用）
     * （初始化控件，设置处理函数）
     * void
     *
     * @throws
     * @since 1.0.0
     */
    protected abstract void initComponents();

    /**
     * loadData(这里用一句话描述这个方法的作用）
     * （从网络导入页面显示的数据）
     * void
     *
     * @throws
     * @since 1.0.0
     */
    protected abstract void loadData();

    protected boolean isHandleAppStatusChange() {
        return true;
    }


    /**
     * getTitleBarId(这里用一句话描述这个方法的作用）
     * （如果要自定义titlebar ，需要重写下面该方法）
     *
     * @return int
     * @throws
     * @since 1.0.0
     */
    protected int getTitleBarId() {
        return -1;
    }


    protected void setTitleControlsInfo() {
        //设置title的标题
        TextView title = (TextView) super.findViewById(R.id.title_bar_title_txt);
        title.setText(this.getActivityTitle());
        //设置左边按钮的返回操作
        View leftLayout = super.findViewById(R.id.title_bar_left_layout);
        leftLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finalizeBeforeDestroy();
                BaseActivity.this.finish();
            }
        });
    }

    /**
     * finalizeBeforeDestroy(这里用一句话描述这个方法的作用）
     * （activity退出前执行的操作）
     * void
     *
     * @throws
     * @since 1.0.0
     */
    protected void finalizeBeforeDestroy() {

    }

    /* (non-Javadoc)
     * @see android.app.Activity#finish()
     */
    @Override
    public void finish() {

        activityManager.removeActivity(this);
        super.finish();
    }

    protected void onPreInitUI() {

    }


    /**
     * IsReloadData(这里用一句话描述这个方法的作用）
     * （如果每次调用onresume重新加载数据，重写该函数，返回true）
     *
     * @return boolean
     * @throws
     * @since 1.0.0
     */
    protected boolean IsReloadData() {
        return false;
    }

    /**
     * setTitleBarId(这里用一句话描述这个方法的作用）
     * （设置titlebar的布局id）
     *
     * @param titleBarId void
     * @throws
     * @since 1.0.0
     */
    protected void setTitleBarId(int titleBarId) {
        if (titleBarId != -1) {
            this.nTitleBarId = titleBarId;
            this.bHasNavigationBar = true;
        } else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
    }

    protected void handleMessage(Message msg) {
        //nothing to do
    }

    protected void getSavedDataFromBundle(Bundle savedInstanceState) {

    }

    @Override
    public void setContentView(int layoutResID) {
        if (bHasNavigationBar && (this.nTitleBarId != -1)) {
            requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
            super.setContentView(layoutResID);
            super.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, this.nTitleBarId);
            this.setTitleControlsInfo();
        } else {
            super.setContentView(layoutResID);
        }
    }

    /**
     * isCheckNetwork(这里用一句话描述这个方法的作用）
     * （进入页面是否检查网络）
     *
     * @return boolean
     * @throws
     * @since 1.0.0
     */
    protected boolean isCheckNetwork() {
        return true;
    }

    public boolean isActivityShowing() {
        return isFinishing() == false && isActived == true;
    }

    @Override
    protected void onDestroy() {
        isActived = false;
        super.onDestroy();
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume() {
        Log.e(TAG, BaseActivity.this.getClass().getName());
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public SpannableStringBuilder getEditInputErrorPrompt(String errMsg) {
        ForegroundColorSpan fgcSpan = new ForegroundColorSpan(R.color.blue);
        SpannableStringBuilder ssb = new SpannableStringBuilder(errMsg);
        ssb.setSpan(fgcSpan, 0, errMsg.length(), 0);
        return ssb;
    }

    public SpannableStringBuilder getEditInputErrorPrompt(int msgResId) {
        return this.getEditInputErrorPrompt(getString(msgResId));
    }

    public CommonProgressDialog showProgressDialog(String title, String message) {
        try {
            if (isActivityShowing()) {
                if (loadingProgressDialog == null) {
                    loadingProgressDialog = CommonProgressDialog.show(this, title, message);
                    loadingProgressDialog.setCancelable(true);
                    loadingProgressDialog.setCanceledOnTouchOutside(true);
                    loadingProgressDialog.show();
                } else {
                    loadingProgressDialog.setTitle(title);
                    loadingProgressDialog.setMessage(message);
                    if (loadingProgressDialog.isShowing() == false) {
                        loadingProgressDialog.show();
                    }
                }
            }
        } catch (Exception e) {
        }
        return loadingProgressDialog;
    }

    /**
     * 隐藏键盘
     */
    protected void keyBoardCancle() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void dismissProgressDialog() {
        try {
            if (isActivityShowing() && loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) {
        try {
            if (receiver != null) {
                super.unregisterReceiver(receiver);
            }
        } catch (Exception e) {
        }
    }

}
