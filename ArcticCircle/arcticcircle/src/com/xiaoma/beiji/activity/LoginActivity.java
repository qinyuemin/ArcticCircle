/**
 * 项目名： simpleApp
 * 包名： com.company.simple.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015
 */
package com.xiaoma.beiji.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSONObject;
import com.makeapp.javase.lang.StringUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.makeapp.android.util.ViewUtil;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.view.GetCodeButton;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.IntentUtil;
import com.xiaoma.beiji.util.ToastUtil;

/**
 * 类名称： LoginActivity
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年11月16 10:43
 * 修改备注：
 *
 * @version 1.0.0
 */
public class LoginActivity extends SimpleBaseActivity implements View.OnClickListener {

    private TextView txtLoginModel;
    private RelativeLayout rlMobile, rlPwd;
    private EditText edtMobile, edtCode, edtEmail, edtPwd;
    private Button btnLogin;

    private String smsKey = "";

    @Override
    protected String getActivityTitle() {
        return null;
    }

    @Override
    protected int getTitleBarId() {
        return -1;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initComponents() {
        rlMobile = (RelativeLayout) findViewById(R.id.rl_mobile);
        rlPwd = (RelativeLayout) findViewById(R.id.rl_pwd);
        txtLoginModel = (TextView) findViewById(R.id.txt_login_model);


        txtLoginModel.setOnClickListener(this);

        ViewUtil.setViewOnClickListener(this, R.id.btn_login, this);
        ((GetCodeButton) findViewById(R.id.btn_code)).setOnCodeListener(new GetCodeButton.OnCodeListener() {
            @Override
            public void onClick() {
                String mobile = edtMobile.getText().toString();
                // 密码登录
                if (StringUtil.isInvalid(mobile)) {
                    ToastUtil.showToast(LoginActivity.this, "请输入手机号码");
                    edtEmail.requestFocus();
                    return;
                }
                HttpClientUtil.User.userSendLoginCode(mobile, new AbsHttpResultHandler<JSONObject>() {
                    @Override
                    public void onSuccess(int resultCode, String desc, JSONObject data) {
                        ToastUtil.showToast(LoginActivity.this, "验证码发送成功");
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
                        ToastUtil.showToast(LoginActivity.this, "验证码发送失败:" + desc);
                    }
                });
            }
        });

        setLoginModel(true);

        edtMobile = (EditText) findViewById(R.id.edt_mobile);
        edtCode = (EditText) findViewById(R.id.edt_code);
        edtEmail = (EditText) findViewById(R.id.edt_mail);
        edtPwd = (EditText) findViewById(R.id.edt_pwd);

        ViewUtil.setViewOnClickListener(this, R.id.img_logo, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                IntentUtil.goMainActivity(LoginActivity.this);
            }
        });
    }

    private void setLoginModel(boolean loginModel) {
        rlMobile.setVisibility(loginModel ? View.VISIBLE : View.GONE);
        rlPwd.setVisibility(loginModel ? View.GONE : View.VISIBLE);
        txtLoginModel.setText(loginModel ? "密码登陆>>" : "验证码登陆>>");
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_login_model:
                setLoginModel(!(rlMobile.getVisibility() == View.VISIBLE));
                break;
            case R.id.btn_login:
                login();
                break;
        }
    }

    void login() {
        String mobile = edtMobile.getText().toString();
        String code = edtCode.getText().toString();
        String email = edtEmail.getText().toString();
        String pwd = edtPwd.getText().toString();

        boolean loginModel = rlMobile.getVisibility() == View.VISIBLE;
        // true 验证码登录  false 密码登录
        if (loginModel) {
            // 密码登录
            if (StringUtil.isInvalid(mobile)) {
                ToastUtil.showToast(this, "请输入手机号码");
                edtEmail.requestFocus();
                return;
            }

            if (StringUtil.isInvalid(code)) {
                ToastUtil.showToast(this, "亲输入验证码");
                edtCode.requestFocus();
                return;
            }
        } else {
            // 密码登录
            if (StringUtil.isInvalid(email)) {
                ToastUtil.showToast(this, "请输入邮箱/手机");
                edtEmail.requestFocus();
                return;
            }

            if (StringUtil.isInvalid(pwd)) {
                ToastUtil.showToast(this, "亲输入密码");
                edtPwd.requestFocus();
                return;
            }
        }

        HttpClientUtil.User.userLogin(loginModel ? 1 : 2, loginModel ? mobile : email, pwd, code, smsKey, new AbsHttpResultHandler<UserInfoEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, UserInfoEntity data) {
                ToastUtil.showToast(LoginActivity.this, "登录成功");
                // 设置 登录成功信息 然后跳转
                if(data != null){
                    Global.setUserInfo(data);
                    IntentUtil.goMainActivity(LoginActivity.this);
                }
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(LoginActivity.this, "登录失败:" + desc);
            }
        });
    }
}