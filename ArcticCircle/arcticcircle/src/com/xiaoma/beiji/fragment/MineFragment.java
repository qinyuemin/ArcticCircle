/**
 * 项目名： eyah
 * 包名： com.sttri.eyah.fragment
 * 版本信息： 1.0.0
 * Copyright (c) -版权所有
 */
package com.xiaoma.beiji.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ViewUtil;
import com.makeapp.javase.lang.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.activity.GetImageActivity;
import com.xiaoma.beiji.base.SimpleFragment;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.view.CircularImage;
import com.xiaoma.beiji.entity.FileUploadResultEntity;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.manager.ImageSelectManager;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.CommUtil;
import com.xiaoma.beiji.util.ImageLoaderUtil;
import com.xiaoma.beiji.util.ImageUtils;
import com.xiaoma.beiji.util.IntentUtil;

import java.io.File;

/**
 * 类名称： MineFragment
 * 类描述： 个人中心
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年06月08 15:57
 * 修改备注：
 *
 * @version 1.0.0
 */
public class MineFragment extends SimpleFragment implements View.OnClickListener {

    private CircularImage imgHead;
    private View rootView;

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_mine;
    }


    @Override
    protected void initComponents(View v) {
        rootView = v;
        UserInfoEntity entity = Global.getUserInfo();
        initInfo(entity);
        imgHead = (CircularImage) v.findViewById(R.id.img_head);
        String avatar = entity.getAvatar();
        if (StringUtil.isValid(avatar)) {
            ImageLoader.getInstance().displayImage(avatar, imgHead);
        }
        imgHead.setOnClickListener(this);

        ViewUtil.setViewOnClickListener(v, R.id.stv_dynamic, this);
        ViewUtil.setViewOnClickListener(v, R.id.stv_collect, this);
        ViewUtil.setViewOnClickListener(v, R.id.stv_like, this);
        ViewUtil.setViewOnClickListener(v, R.id.stv_msg, this);
        ViewUtil.setViewOnClickListener(v, R.id.stv_privacy, this);
        ViewUtil.setViewOnClickListener(v, R.id.stv_reply, this);
        ViewUtil.setViewOnClickListener(v, R.id.img_setting, this);
        ViewUtil.setViewOnClickListener(v, R.id.stv_logout, this);
    }

    boolean isFirst = true;
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            if(isFirst){
                initInfo(Global.getUserInfo());
            }
            isFirst = false;
        }
    }

    private void initInfo(UserInfoEntity entity) {
        TextViewUtil.setText(rootView, R.id.txt_name,entity.getNickname());
        TextViewUtil.setText(rootView,R.id.txt_phone, "手机号:"+ CommUtil.getPhoneEncrypt(entity.getUserPhone()));
        TextViewUtil.setText(rootView,R.id.txt_id,"北极圈号:"+entity.getUserId());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stv_msg:
                IntentUtil.goMessageListActivity(getFragmentActivity());
                break;
            case R.id.img_setting:
                IntentUtil.goUserInfoActivity(getFragmentActivity());
                break;
            case R.id.stv_privacy:
                IntentUtil.goSettingActivity(getFragmentActivity());
                break;
            case R.id.stv_logout:
                Global.logout();
                IntentUtil.goStartActivity(getFragmentActivity());
                getFragmentActivity().finish();
                break;
            case R.id.stv_collect:
                // 我的收藏
                IntentUtil.goFavoriteListActivity(getFragmentActivity());
                break;
           case R.id.stv_dynamic:
                // 我的动态
                IntentUtil.goFriendDynamicActivity(getFragmentActivity(),String.valueOf(Global.getUserId()),true);
                break;
           case R.id.stv_like:
                // 我的求助
                IntentUtil.goFriendHelpActivity(getFragmentActivity(),String.valueOf(Global.getUserId()), true);
                break;
            case R.id.img_head:
                ImageSelectManager.getInstance().selectImage(this, GetImageActivity.GetImageType.BOTH);
                break;
            case R.id.stv_reply:
                IntentUtil.goCommentMeActivity(getFragmentActivity());
                break;
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ImageSelectManager.INTENT_REQUEST_CODE_GET_IMAGE_ACTIVITY:
                    if (data != null) {
                        String picturePath = data.getStringExtra(GetImageActivity.KEY_INTENT_IMAGE_PATH);
                        ImageUtils.getImageByUrl(imgHead, Uri.fromFile(new File(picturePath)).toString(), ImageLoaderUtil.getOptionDefaultUserInfo());

                        HttpClientUtil.imageUpload(picturePath, new AbsHttpResultHandler<FileUploadResultEntity>() {
                            @Override
                            public void onSuccess(int resultCode, String desc, FileUploadResultEntity data) {
                                HttpClientUtil.User.userAvatar(data.getUploadPath(), new AbsHttpResultHandler() {
                                    @Override
                                    public void onSuccess(int resultCode, String desc, Object data) {

                                    }

                                    @Override
                                    public void onFailure(int resultCode, String desc) {

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
    protected void loadData() {
    }

}
