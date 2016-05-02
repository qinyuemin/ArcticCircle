package com.xiaoma.beiji.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.makeapp.android.util.ViewUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.util.IntentUtil;

/**
 * Created by zhangqibo on 2016/5/2.
 */
public class AccountSafeActivity extends SimpleBaseActivity {
    @Override
    protected String getActivityTitle() {
        return "账号与安全";
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account_safe;
    }

    @Override
    protected void initComponents() {
        TextView phoneText = (TextView) findViewById(R.id.text_bindPhone);
        TextView idText = (TextView) findViewById(R.id.text_accountId);
        phoneText.setText(Global.getUserPhone());
        idText.setText(String.valueOf(Global.getUserId()));
        findViewById(R.id.layout_account_modifyPwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.goUserPwdChangeActivity(AccountSafeActivity.this);
            }
        });
    }

    @Override
    protected void loadData() {

    }
}
