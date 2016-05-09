/**
 * 项目名： simpleApp
 * 包名： com.company.simple.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015
 */
package com.xiaoma.beiji.activity;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.makeapp.javase.lang.StringUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.view.GetCodeButton;
import com.xiaoma.beiji.entity.ContactEntity;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.IntentUtil;
import com.makeapp.android.util.ViewUtil;
import com.xiaoma.beiji.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称： RegisterActivity
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年11月16 10:43
 * 修改时间： 2015年5月9 21:43  -zhangqibo
 * 修改备注：
 *
 * @version 1.0.0
 */
public class RegisterActivity extends SimpleBaseActivity implements View.OnClickListener {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    private EditText edtPhone,edtCode,edtPwd;

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
//        super.setTitleControlsInfo();
//        ViewUtil.setViewVisibility(this, R.id.title_bar_left_sub_txt, View.VISIBLE);
//        ViewUtil.setViewVisibility(this, R.id.title_bar_left_img, View.GONE);
//        TextViewUtil.setText(this,R.id.title_bar_left_sub_txt,"取消");
    }

    @Override
    protected void initComponents() {
        setTitleControlsInfo();
        edtPwd = (EditText) findViewById(R.id.edt_pwd);
        edtPhone = (EditText) findViewById(R.id.edt_mail);
        edtCode = (EditText) findViewById(R.id.edt_code);


        SpannableString str = new SpannableString("已有账户？赶快来登录吧！");
        str.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                finish();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                //设置文本的颜色
                ds.setColor(getResources().getColor(R.color.blue));
                //超链接形式的下划线，false 表示不显示下划线，true表示显示下划线
                ds.setUnderlineText(false);
            }
        }, 8, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView registerBtn = (TextView) findViewById(R.id.text_notice_register);
        registerBtn.setMovementMethod(LinkMovementMethod.getInstance());
        registerBtn.setText(str);

        SpannableString str2 = new SpannableString("继续表示你已同意《北极圈服务条款》");
        str2.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                IntentUtil.goClauseActivity(RegisterActivity.this);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                //设置文本的颜色
                ds.setColor(getResources().getColor(R.color.blue));
                //超链接形式的下划线，false 表示不显示下划线，true表示显示下划线
                ds.setUnderlineText(false);
            }
        }, 8, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView txt_tip = (TextView) findViewById(R.id.txt_tip);
        txt_tip.setMovementMethod(LinkMovementMethod.getInstance());
        txt_tip.setText(str2);

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
        String pwd = edtPwd.getText().toString();
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
                if(StringUtil.isInvalid(pwd)){
                    ToastUtil.showToast(this, "请输入密码");
                    return;
                }if(pwd.length() < 6){
                    ToastUtil.showToast(this, "请输入至少6位密码");
                    return;
                }

                HttpClientUtil.User.userRegister(phone, 1, "", pwd, code, smsKey, new AbsHttpResultHandler<UserInfoEntity>() {
                    @Override
                    public void onSuccess(int resultCode, String desc, UserInfoEntity data) {
                        ToastUtil.showToast(RegisterActivity.this,"注册成功");
                        // 保存数据，进入主页
                        if(data != null){
                            Global.setUserInfo(data);
                        }
                        attentionSelf();
                        IntentUtil.goPhoneContactsActivity(RegisterActivity.this, true);
                        finish();
                    }

                    @Override
                    public void onFailure(int resultCode, String desc) {

                    }
                });
                break;

        }
    }

    private void attentionSelf(){
        String phone = edtPhone.getText().toString();
        UserInfoEntity userInfoEntity = Global.getUserInfo();
        List<ContactEntity> list = new ArrayList<>();
        ContactEntity entity = new ContactEntity();
        entity.setPhone(phone);
        entity.setName(userInfoEntity.getNickname());
        list.add(entity);
        HttpClientUtil.vCardChange(list, new AbsHttpResultHandler() {
            @Override
            public void onSuccess(int resultCode, String desc, Object data) {

            }

            @Override
            public void onFailure(int resultCode, String desc) {

            }
        });
    }
}