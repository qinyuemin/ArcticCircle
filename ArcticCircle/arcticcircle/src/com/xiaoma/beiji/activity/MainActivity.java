package com.xiaoma.beiji.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import com.common.android.lib.util.SDCardUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.dialog.SimpleDialog;
import com.xiaoma.beiji.controls.view.BottomBarView;
import com.xiaoma.beiji.controls.view.BottomBarView.PageItem;
import com.xiaoma.beiji.controls.view.VersionView;
import com.xiaoma.beiji.database.TableMetadataDef;
import com.xiaoma.beiji.entity.XmppUserEntity;
import com.xiaoma.beiji.event.LogoutEvent;
import com.xiaoma.beiji.fragment.CircleFragment;
import com.xiaoma.beiji.fragment.FindFragment;
import com.xiaoma.beiji.fragment.MineFragment;
import com.xiaoma.beiji.fragment.ChatFragment;
import com.xiaoma.beiji.fragment.MyProfileFragment;
import com.xiaoma.beiji.manager.ContactManager;
import com.xiaoma.beiji.manager.chatting.IMXmppManager;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.IntentUtil;
import com.xiaoma.beiji.util.ToastUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 *
 * 类名称： MainActivity
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年06月08 10:42
 * 修改备注：
 * @version 1.0.0
 *
 */
public class MainActivity extends SimpleBaseActivity implements BottomBarView.OnPageItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private BottomBarView bbv;

    private Fragment indexFragment;
    private Fragment findFragment;
    private Fragment statisticsFragment;
    private Fragment mineFragment;
    private FragmentManager mFragmentManager;

    private PageItem mPageItem = null;
    private Fragment mCurrentFragment;

    private List<Fragment> fragmentList;

    @Override
    protected String getActivityTitle() {
        return null;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFragmentManager = getSupportFragmentManager();

        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mPageItem = (PageItem) intent.getSerializableExtra("item");

    }

    @Override
    protected void onResume() {
        super.onResume();

        signView = VersionView.showVersionSign(this);// 显示版本标识（测试期间使用）
        try {
            dbExport(TableMetadataDef.DATABASE_BASE_NAME);
//            syncContacts();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (mPageItem != null) {
            onClick(mPageItem);
            bbv.setCurrentPage(mPageItem);
            mPageItem = null;
        }
    }

    private View signView;

    @Override
    protected void onPause() {
        super.onPause();
        VersionView.removeVersionSign(this, signView);// 去掉版本标识（测试期间使用）
    }



    private void dbExport(String dbName) {
        FileInputStream in = null;
        FileOutputStream fos = null;
        try {
            File dir = new File(SDCardUtil.getSDCardPath() + "/beiji/databases/");
            if (!dir.exists()) {
                try {
                    dir.mkdir();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            File outFile = new File(dir, dbName);
            outFile.deleteOnExit();

            File inFile = new File(getApplicationContext().getFilesDir().getParentFile().getAbsolutePath() + "/databases/" + dbName);
            if (inFile.exists()) {
                in = new FileInputStream(inFile);
                fos = new FileOutputStream(outFile);
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = in.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
            }
            Log.i("dbExport", "export db from " + inFile.getAbsolutePath() + " to " + outFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void initComponents() {
        fragmentList = new ArrayList<>();
        bbv = (BottomBarView) findViewById(R.id.bottomBar);
        bbv.setOnPageItemClickListener(this);

        showFragment(PageItem.INDEX);

        HttpClientUtil.Chatting.XmppGetUser(new AbsHttpResultHandler<XmppUserEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, XmppUserEntity data) {
                Global.setXmppUserEntity(data);
                IntentUtil.startIMChatService(MainActivity.this);
            }

            @Override
            public void onFailure(int resultCode, String desc) {

            }
        });
    }

    @Override
    public void onClick(PageItem pageItem) {
        showFragment(pageItem);
    }

    @Override
    protected void loadData() {
        try {
            super.activityManager.exitPreActivity(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFragment(BottomBarView.PageItem pageItem) {
        if (pageItem == null) {
            return;
        }

        Fragment fragment = null;
        mPageItem = pageItem;
        switch (pageItem){
            case INDEX:
                if(indexFragment == null){
                    indexFragment = Fragment.instantiate(this, CircleFragment.class.getName(), null);
                }
                fragment = indexFragment;
                break;
            case FIND:
                if(findFragment == null){
                    findFragment = Fragment.instantiate(this, FindFragment.class.getName(), null);
                }
                fragment = findFragment;
                break;
            case STATISTICS:
                if(statisticsFragment == null){
                    statisticsFragment = Fragment.instantiate(this, ChatFragment.class.getName(), null);
                }
                fragment = statisticsFragment;
                break;
            case MINE:
                if(mineFragment == null){
                    mineFragment = Fragment.instantiate(this, MyProfileFragment.class.getName(), null);
                }
                fragment = mineFragment;
                break;

        }
        addAndShowFragment(fragment);
        bbv.setCurrentPage(mPageItem);
    }

    private void addAndShowFragment(Fragment fragment){
        mCurrentFragment = fragment;
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if(!fragment.isAdded()){
            ft.add(R.id.homePageContainer, fragment, fragment.getClass().getSimpleName());
        }

        hideFragment(ft,indexFragment,fragment);
        hideFragment(ft,findFragment,fragment);
        hideFragment(ft,statisticsFragment,fragment);
        hideFragment(ft, mineFragment, fragment);

        ft.show(fragment);
        ft.commitAllowingStateLoss();
    }

    private void hideFragment(FragmentTransaction ft, Fragment fragment,Fragment fragment1){
        if(fragment != fragment1){
            if(fragment != null){
                ft.hide(fragment);
            }
        }
    }

    private long backClickTime = 0;

    private boolean tipExit() {
        if (System.currentTimeMillis() - backClickTime > 3000) {
            backClickTime = System.currentTimeMillis(); // 第一次不弹出提示框
            ToastUtil.showToast(getApplicationContext(), "再按一次返回键退出" + getString(R.string.app_name));
        } else {
            // 显示退出客户端Dialog的时候，右侧按钮显示的是“取消”，所以左侧按钮（即Cancel按钮）显示的才是退出
            backClickTime = 0;
            new SimpleDialog(this).setDialogTitle("提示").setDialogMessage("是否要退出北极圈吗?").setDialogLeftButton("退出", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activityManager.exit();
                }
            }).setDialogRightButton("取消", null).show();
            ToastUtil.hideToast();
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (tipExit()) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onEventMainThread(LogoutEvent event) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
