/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015
 */
package com.xiaoma.beiji.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.makeapp.android.util.EditTextUtil;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ToastUtil;
import com.makeapp.android.util.ViewUtil;
import com.makeapp.javase.lang.StringUtil;
import com.makeapp.javase.util.DataUtil;
import com.makeapp.javase.util.DateUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.common.AppConstants;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.view.CircularImage;
import com.xiaoma.beiji.controls.view.imagechooser.ImageChooserGroupActivity;
import com.xiaoma.beiji.entity.FileUploadResultEntity;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.manager.ImageSelectManager;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.BitmapDecodeTool;
import com.xiaoma.beiji.util.CommUtil;
import com.xiaoma.beiji.util.ImageLoaderUtil;
import com.xiaoma.beiji.util.ImageUtils;
import com.xiaoma.beiji.util.ImgHelper;
import com.xiaoma.beiji.util.IntentUtil;

import java.io.File;
import java.util.ArrayList;

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
public class UserInfoActivity extends SimpleBaseActivity implements View.OnClickListener {
    private static final String TAG = UserInfoActivity.class.getSimpleName();

    private UserInfoEntity infoEntity;

    private int gender;
    private CircularImage headView;



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
        findViewById(R.id.title_bar_right_txt).setOnClickListener(this);
    }

    @Override
    protected void initComponents() {
        setTitleControlsInfo();
        infoEntity = Global.getUserInfo();
        headView = (CircularImage) findViewById(R.id.img_head);
        if(StringUtil.isValid(infoEntity.getAvatar())){
            ImageLoader.getInstance().displayImage(infoEntity.getAvatar(), headView);
        }
        headView.setOnClickListener(this);
        EditTextUtil.setText(this, R.id.edit_nikename, infoEntity.getNickname());
        EditTextUtil.setText(this, R.id.edit_label, infoEntity.getLabel());
        EditTextUtil.setText(this, R.id.edit_address, infoEntity.getAddress());
        EditTextUtil.setText(this, R.id.edt_content, infoEntity.getProfile());
        gender = DataUtil.getInt(infoEntity.getGender(), 0);
        updateGenderView();
    }

    private void setUserProfile(){
        final String name = EditTextUtil.getTextString(this, R.id.edit_nikename);
        if (StringUtil.isInvalid(name)) {
            ToastUtil.show(this, "请输入昵称");
            return;
        }
        int newGender = DataUtil.getInt(infoEntity.getGender(), 0);

        EditText labelEdite = (EditText) findViewById(R.id.edit_label);
        EditText addressEdite = (EditText) findViewById(R.id.edit_address);
        EditText profileEdite = (EditText) findViewById(R.id.edt_content);
        final String lable = labelEdite.getText().toString().trim();
        final String address = addressEdite.getText().toString().trim();
        final String profile = profileEdite.getText().toString().trim();
        final int finalNewGender = newGender;
        HttpClientUtil.User.userEditV2(name, finalNewGender, lable, address, profile, new AbsHttpResultHandler() {
            @Override
            public void onSuccess(int resultCode, String desc, Object data) {
                ToastUtil.show(UserInfoActivity.this, "修改成功");
                infoEntity.setNickname(name);
                infoEntity.setGender(String.valueOf(finalNewGender));
                infoEntity.setLabel(lable);
                infoEntity.setAddress(address);
                infoEntity.setProfile(profile);
                Global.setUserInfo(infoEntity);
                finish();
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.show(UserInfoActivity.this, "修改失败:" + desc);
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ImageSelectManager.INTENT_REQUEST_CODE_GET_IMAGE_ACTIVITY:
                    if (data != null) {
                        String picturePath = data.getStringExtra(GetImageActivity.KEY_INTENT_IMAGE_PATH);
                        ImageUtils.getImageByUrl(headView, Uri.fromFile(new File(picturePath)).toString(), ImageLoaderUtil.getOptionDefaultUserInfo());

                        HttpClientUtil.imageUpload(picturePath, new AbsHttpResultHandler<FileUploadResultEntity>() {
                            @Override
                            public void onSuccess(int resultCode, String desc, final FileUploadResultEntity fileUploadResultEntity) {
                                HttpClientUtil.User.userAvatar(fileUploadResultEntity.getUploadPath(), new AbsHttpResultHandler() {
                                    @Override
                                    public void onSuccess(int resultCode, String desc, Object data) {
                                        com.xiaoma.beiji.util.ToastUtil.showToast(UserInfoActivity.this, "头像设置成功");
                                        String path = (String) data;
                                        infoEntity.setAvatar(path);
                                        Global.setUserInfo(infoEntity);
                                    }

                                    @Override
                                    public void onFailure(int resultCode, String desc) {
                                        com.xiaoma.beiji.util.ToastUtil.showToast(UserInfoActivity.this,"头像设置失败"+desc);
                                    }
                                });
                            }

                            @Override
                            public void onFailure(int resultCode, String desc) {

                            }
                        });



                    }
                    break;
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_head://头像
                ImageSelectManager.getInstance().selectImage(this, GetImageActivity.GetImageType.BOTH);
                break;
            case R.id.title_bar_right_txt: //保存
                setUserProfile();
                break;
        }
    }

    private void updateGenderView(){
        TextView textChooseNan = (TextView) findViewById(R.id.text_choose_nan);
        TextView textChooseNv = (TextView) findViewById(R.id.text_choose_nv);
        if (gender == 1) {
            TextViewUtil.setText(this,R.id.text_gender, "男");
            textChooseNan.setTextColor(getResources().getColor(R.color.blue));
            textChooseNv.setTextColor(getResources().getColor(R.color.gray_divider));
        } else {
            TextViewUtil.setText(this,R.id.text_gender, "女");
            textChooseNv.setTextColor(getResources().getColor(R.color.blue));
            textChooseNan.setTextColor(getResources().getColor(R.color.gray_divider));
        }
        textChooseNan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = 1;
                updateGenderView();
            }
        });

        textChooseNv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = 2;
                updateGenderView();
            }
        });
    }
}