/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015
 */
package com.xiaoma.beiji.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.makeapp.android.util.EditTextUtil;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ToastUtil;
import com.makeapp.android.util.ViewUtil;
import com.makeapp.javase.lang.StringUtil;
import com.makeapp.javase.util.DataUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.CommUtil;
import com.xiaoma.beiji.util.IntentUtil;

/**
 * 类名称： UserInfoActivity
 * 类描述： 个人资料
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年11月18 10:27
 * 修改备注：
 *
 * @version 1.0.0
 */
public class UserInfoActivity extends SimpleBaseActivity {
    private static final String TAG = UserInfoActivity.class.getSimpleName();

    private UserInfoEntity infoEntity;
    private Activity activity;

    private RadioGroup rgSex;

    @Override
    protected String getActivityTitle() {
        return "个人资料";
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void setTitleControlsInfo() {
        super.setTitleControlsInfo();

        ViewUtil.setViewVisibility(this, R.id.title_bar_right_layout, View.VISIBLE);
        TextViewUtil.setViewVisibility(this, R.id.title_bar_right_txt, View.VISIBLE);
        TextViewUtil.setText(this, R.id.title_bar_right_txt, "保存");
    }

    @Override
    protected void initComponents() {
        setTitleControlsInfo();
        activity = this;
        infoEntity = Global.getUserInfo();
        EditTextUtil.setText(this, R.id.edit_nikename, infoEntity.getNickname());
        rgSex = (RadioGroup) findViewById(R.id.rg_sex);
        final int gender = DataUtil.getInt(infoEntity.getGender(), 0);
        String genderText = "";
        if (gender == 1) {
//            ((RadioButton) findViewById(R.id.rb_male)).setChecked(true);
            genderText = "男";
        } else {
            genderText = "女";
//            ((RadioButton) findViewById(R.id.rb_male)).setChecked(false);
        }
        TextViewUtil.setText(this,R.id.text_gender,genderText);
//        ViewUtil.setViewOnClickListener(this, R.id.txt_update_pwd, new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                IntentUtil.goUserPwdChangeActivity(UserInfoActivity.this);
//            }
//        });

//        ViewUtil.setViewOnClickListener(this, R.id.title_bar_right_layout, new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final String name = EditTextUtil.getTextString(activity, R.id.edt_name);
//                if (StringUtil.isInvalid(name)) {
//                    ToastUtil.show(activity, "请输入昵称");
//                    return;
//                }
//                int newGender = 0;
//                int checkedRadioButtonId = rgSex.getCheckedRadioButtonId();
//                if (checkedRadioButtonId == R.id.rb_male) {
//                    newGender = 1;
//                } else if (checkedRadioButtonId == R.id.rb_female) {
//                    newGender = 2;
//                }
//                if (name.equals(infoEntity.getNickname()) && newGender == gender) {
//                    return;
//                }
//                final int finalNewGender = newGender;
//                HttpClientUtil.User.userEdit(name, gender, new AbsHttpResultHandler() {
//                    @Override
//                    public void onSuccess(int resultCode, String desc, Object data) {
//                        ToastUtil.show(activity, "修改成功");
//                        infoEntity.setNickname(name);
//                        infoEntity.setGender(String.valueOf(finalNewGender));
//                        Global.setUserInfo(infoEntity);
//                    }
//
//                    @Override
//                    public void onFailure(int resultCode, String desc) {
//                        ToastUtil.show(activity, "修改失败:" + desc);
//                    }
//                });
//            }
//        });
    }

    @Override
    protected void loadData() {

    }
}