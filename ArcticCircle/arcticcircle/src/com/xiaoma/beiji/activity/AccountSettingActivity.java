package com.xiaoma.beiji.activity;

import android.view.View;

import com.makeapp.android.util.ViewUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.util.IntentUtil;

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
                break;
        }
    }
}
