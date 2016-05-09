/**
 * 项目名： simpleApp
 * 包名： com.company.simple.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015
 */
package com.xiaoma.beiji.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.makeapp.javase.lang.StringUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.IntentUtil;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ViewUtil;
import com.xiaoma.beiji.util.ToastUtil;

/**
 * 类名称： RegisterInfoActivity
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年11月16 16:01
 * 修改备注：
 *
 * @version 1.0.0
 */
public class RegisterInfoActivity extends SimpleBaseActivity implements View.OnClickListener {
    private static final String TAG = RegisterInfoActivity.class.getSimpleName();

    private EditText edtNickname;
    private RadioGroup rgSex;

    @Override
    protected String getActivityTitle() {
        return "注册";
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_register_info;
    }

    @Override
    protected void setTitleControlsInfo() {
        super.setTitleControlsInfo();
        ViewUtil.setViewVisibility(this, R.id.title_bar_left_sub_txt, View.VISIBLE);
        ViewUtil.setViewVisibility(this, R.id.title_bar_left_img, View.GONE);
        TextViewUtil.setText(this, R.id.title_bar_left_sub_txt, "取消");
    }

    @Override
    protected void initComponents() {
        setTitleControlsInfo();
        ViewUtil.setViewOnClickListener(this, R.id.btn_next, this);

        edtNickname = (EditText) findViewById(R.id.edt_nickname);
        rgSex = (RadioGroup) findViewById(R.id.rg_sex);

        String comment1 = "<font color=#a0aab3>注册会员即表示你同意</font><font color=#1ab6fe>《北极圈服务条款》</font>";
        TextViewUtil.setText(this, R.id.txt_clause, Html.fromHtml(comment1));
        ViewUtil.setViewOnClickListener(this,R.id.txt_clause,this);

    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_clause:
                IntentUtil.goClauseActivity(this);
                break;
            case R.id.btn_next:
                String nickname = edtNickname.getText().toString();
                if (StringUtil.isInvalid(nickname)) {
                    ToastUtil.showToast(this, "请输入昵称");
                    return;
                }

                int gender = 0;
                int checkedRadioButtonId = rgSex.getCheckedRadioButtonId();
                if(checkedRadioButtonId == R.id.rb_male){
                    gender = 1;
                }else if(checkedRadioButtonId == R.id.rb_female){
                    gender = 2;
                }

                Bundle bundle = getIntent().getExtras();
                bundle.putString("nickname", nickname);
                bundle.putString("gender", "" + gender);

                String phone = bundle.getString("phone");
                String code = bundle.getString("code");
                String smsKey = bundle.getString("smsKey");
                String pwd = bundle.getString("pwd");

                showProgressDialog("","正在为您注册，请稍侯...");
                HttpClientUtil.User.userRegister(phone, gender, nickname, pwd, code, smsKey, new AbsHttpResultHandler<UserInfoEntity>() {
                    @Override
                    public void onSuccess(int resultCode, String desc, UserInfoEntity data) {
                        dismissProgressDialog();
                        ToastUtil.showToast(RegisterInfoActivity.this,"注册成功");
                        // 保存数据，进入主页
                        if(data != null){
                            Global.setUserInfo(data);
                        }
                        IntentUtil.goPhoneContactsActivity(RegisterInfoActivity.this, true);
                        finish();
                    }

                    @Override
                    public void onFailure(int resultCode, String desc) {
                        dismissProgressDialog();
                        ToastUtil.showToast(RegisterInfoActivity.this,"注册失败:"+desc);
                    }
                });

                break;
        }
    }
}