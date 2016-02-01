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
import com.alibaba.fastjson.JSONObject;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ToastUtil;
import com.makeapp.android.util.ViewUtil;
import com.makeapp.javase.lang.StringUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.view.GetCodeButton;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;

/**
 * 类名称： UserPwdChangeActivity
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月16 17:41
 * 修改备注：
 *
 * @version 1.0.0
 */
public class UserPwdChangeActivity extends SimpleBaseActivity {
    private static final String TAG = UserPwdChangeActivity.class.getSimpleName();
    private String smsKey;
    private String userPhone;

    Activity activity;

    @Override
    protected String getActivityTitle() {
        return "修改密码";
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user_pwd_change;
    }

    @Override
    protected void initComponents() {
        setTitleControlsInfo();
        activity = this;
        userPhone = Global.getUserPhone();

        TextViewUtil.setText(this, R.id.txt_phone, userPhone);

        ((GetCodeButton) findViewById(R.id.btn_code)).setOnCodeListener(new GetCodeButton.OnCodeListener() {
            @Override
            public void onClick() {
                HttpClientUtil.User.userSendChangePassCode(new AbsHttpResultHandler<JSONObject>() {
                    @Override
                    public void onSuccess(int resultCode, String desc, JSONObject data) {
                        ToastUtil.show(activity, "发送成功");
                        try {
                            if (data != null) {
                                smsKey = data.getString("sms_key");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int resultCode, String desc) {
                        ToastUtil.show(activity, "发送失败:" + desc);
                    }
                });
            }
        });

        ViewUtil.setViewOnClickListener(this, R.id.btn_next, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = TextViewUtil.getTextString(activity, R.id.edt_code);
                if (StringUtil.isInvalid(code)) {
                    ToastUtil.show(activity, "请填写验证码");
                    return;
                }
                String pwd = TextViewUtil.getTextString(activity, R.id.edt_pwd);
                if (StringUtil.isInvalid(pwd)) {
                    ToastUtil.show(activity, "请填写密码");
                    return;
                }

                HttpClientUtil.User.userChangePwd(pwd, code, smsKey, new AbsHttpResultHandler() {
                    @Override
                    public void onSuccess(int resultCode, String desc, Object data) {
                        ToastUtil.show(activity, "修改成功");
                        finish();
                    }

                    @Override
                    public void onFailure(int resultCode, String desc) {
                        ToastUtil.show(activity, "修改失败:" + desc);
                    }
                });
            }
        });
    }

    @Override
    protected void loadData() {

    }
}