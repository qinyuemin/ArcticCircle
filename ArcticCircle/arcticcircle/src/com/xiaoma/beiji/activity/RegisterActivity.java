/**
 * 项目名： simpleApp
 * 包名： com.company.simple.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015
 */
package com.xiaoma.beiji.activity;

import android.view.View;
import android.widget.EditText;
import com.alibaba.fastjson.JSONObject;
import com.makeapp.javase.lang.StringUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.controls.view.GetCodeButton;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.IntentUtil;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ViewUtil;
import com.xiaoma.beiji.util.ToastUtil;

/**
 * 类名称： RegisterActivity
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年11月16 10:43
 * 修改备注：
 *
 * @version 1.0.0
 */
public class RegisterActivity extends SimpleBaseActivity implements View.OnClickListener {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    private EditText edtPhone,edtCode;

    private String smsKey = "1512565d0b2cb3b41207566751";

    @Override
    protected String getActivityTitle() {
        return "注册";
    }

    @Override
    protected int getTitleBarId() {
        return -1;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void setTitleControlsInfo() {
        super.setTitleControlsInfo();
        ViewUtil.setViewVisibility(this, R.id.title_bar_left_sub_txt, View.VISIBLE);
        ViewUtil.setViewVisibility(this, R.id.title_bar_left_img, View.GONE);
        TextViewUtil.setText(this,R.id.title_bar_left_sub_txt,"取消");
    }

    @Override
    protected void initComponents() {
        setTitleControlsInfo();

        edtPhone = (EditText) findViewById(R.id.edt_mail);
        edtCode = (EditText) findViewById(R.id.edt_code);

        ViewUtil.setViewOnClickListener(this,R.id.btn_next,this);
        ((GetCodeButton) findViewById(R.id.btn_code)).setOnCodeListener(new GetCodeButton.OnCodeListener() {
            @Override
            public void onClick() {
                String phone = edtPhone.getText().toString();
                if (StringUtil.isInvalid(phone)) {
                    ToastUtil.showToast(RegisterActivity.this, "请输入手机号码");
                    edtPhone.requestFocus();
                    return;
                }
                HttpClientUtil.User.userSendLoginCode(phone, new AbsHttpResultHandler<JSONObject>() {
                    @Override
                    public void onSuccess(int resultCode, String desc, JSONObject data) {
                        ToastUtil.showToast(RegisterActivity.this, "验证码发送成功");
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
                        ToastUtil.showToast(RegisterActivity.this, "验证码发送失败:" + desc);
                    }
                });
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View view) {
        String phone = edtPhone.getText().toString();
        String code = edtCode.getText().toString();
        switch (view.getId()){
            case R.id.btn_next:
                if (StringUtil.isInvalid(phone)) {
                    ToastUtil.showToast(this, "请输入手机号码");
                    edtPhone.requestFocus();
                    return;
                }

                if (StringUtil.isInvalid(code)) {
                    ToastUtil.showToast(this, "请输入验证码");
                    edtCode.requestFocus();
                    return;
                }

                IntentUtil.goRegisterPwdActivity(this,phone,code,smsKey);
                break;

        }
    }
}