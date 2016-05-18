package com.xiaoma.beiji.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.View;

import com.makeapp.android.util.ViewUtil;
import com.makeapp.javase.lang.StringUtil;
import com.makeapp.javase.util.DateUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.common.AppConstants;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.view.imagechooser.ImageChooserGroupActivity;
import com.xiaoma.beiji.entity.FileUploadResultEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.BitmapDecodeTool;
import com.xiaoma.beiji.util.ImgHelper;
import com.xiaoma.beiji.util.IntentUtil;
import com.xiaoma.beiji.util.ToastUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by zhangqibo on 2016/5/2.
 */
public class AccountSettingActivity extends SimpleBaseActivity implements View.OnClickListener{



    @Override
    protected String getActivityTitle() {
        return "设置";
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account_setting;
    }

    @Override
    protected void initComponents() {
        ViewUtil.setViewOnClickListener(this,R.id.layout_account_safe,this);
        ViewUtil.setViewOnClickListener(this,R.id.layout_account_profile,this);
        ViewUtil.setViewOnClickListener(this,R.id.layout_account_daren,this);
        ViewUtil.setViewOnClickListener(this,R.id.layout_account_yinsi,this);

        ViewUtil.setViewOnClickListener(this, R.id.layout_account_notify, this);
        ViewUtil.setViewOnClickListener(this, R.id.layout_account_question, this);
        ViewUtil.setViewOnClickListener(this, R.id.layout_account_about, this);
        ViewUtil.setViewOnClickListener(this, R.id.layout_account_contact, this);
        ViewUtil.setViewOnClickListener(this, R.id.layout_account_clean, this);

        ViewUtil.setViewOnClickListener(this,R.id.layout_account_exit,this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_account_safe: //账户安全
                IntentUtil.goAccountSafeActivity(this);
                break;
            case R.id.layout_account_profile: //个人资料
                IntentUtil.goUserInfoActivity(this);
                break;
            case R.id.layout_account_daren: //申请达人
                break;
            case R.id.layout_account_yinsi: //隐私
                break;

            case R.id.layout_account_notify: //通知

                break;
            case R.id.layout_account_question: //常见问题
                break;
            case R.id.layout_account_about: //关于我们

                break;
            case R.id.layout_account_contact: //联系我们
                IntentUtil.goCommentMeActivity(this);
                break;
            case R.id.layout_account_clean: //清除缓存
                break;

            case R.id.layout_account_exit: //安全退出
                Global.cleanUser();
                IntentUtil.goUserLoginActivity(this);
                finish();
                break;
        }
    }
}
