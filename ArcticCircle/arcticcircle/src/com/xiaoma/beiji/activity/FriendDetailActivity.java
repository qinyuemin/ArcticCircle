/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.activity
 * 版本信息： 1.0.0
 * Copyright (c) -版权所有
 */
package com.xiaoma.beiji.activity;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ViewUtil;
import com.makeapp.javase.lang.StringUtil;
import com.makeapp.javase.util.DataUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.view.CircularImage;
import com.xiaoma.beiji.controls.view.SettingItemView;
import com.xiaoma.beiji.entity.FriendEntity;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.manager.chatting.ChattingUtil;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.CommUtil;
import com.xiaoma.beiji.util.IntentUtil;
import com.xiaoma.beiji.util.ToastUtil;

/**
 * 类名称： FriendDetailActivity
 * 类描述： 朋友详情
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月09 18:01
 * 修改备注：
 *
 * @version 1.0.0
 */
public class FriendDetailActivity extends SimpleBaseActivity implements View.OnClickListener, SettingItemView.OnCheckedChangeListener {
    private static final String TAG = FriendDetailActivity.class.getSimpleName();


    private SettingItemView stvFriend;

    private SettingItemView stv1,stv2,stv3;

    private RelativeLayout rlRelation;
    private LinearLayout llAction;
    private LinearLayout llAdd;
    private LinearLayout llCall;
    private LinearLayout llSameFriend;

    private FriendEntity.MiddleFriend middleFriend = null;
    private FriendEntity entity = null;
    private String userId;

    @Override
    protected String getActivityTitle() {
        return "朋友详情";
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_friend_detail;
    }

    @Override
    protected void initIntent() {
        super.initIntent();
        userId = getIntent().getStringExtra("userId");
    }

    @Override
    protected void initComponents() {
        setTitleControlsInfo();

        stvFriend = (SettingItemView) findViewById(R.id.stv_friend);
        stvFriend.setOnClickListener(this);

        stv1 = (SettingItemView) findViewById(R.id.stv_1);
        stv2 = (SettingItemView) findViewById(R.id.stv_2);
        stv3 = (SettingItemView) findViewById(R.id.stv_3);

        rlRelation = (RelativeLayout) findViewById(R.id.rl_relation);
        llAction = (LinearLayout) findViewById(R.id.ll_action);
        llAdd = (LinearLayout) findViewById(R.id.ll_add);
        llCall = (LinearLayout) findViewById(R.id.ll_call);
        llSameFriend = (LinearLayout) findViewById(R.id.ll_same_friend);

        ViewUtil.setViewOnClickListener(this, R.id.stv_dynamic, this);
        ViewUtil.setViewOnClickListener(this, R.id.stv_rquest, this);
        ViewUtil.setViewOnClickListener(this, R.id.stv_shop, this);
        ViewUtil.setViewOnClickListener(this, R.id.ll_chat, this);

        llAdd.setOnClickListener(this);
        llCall.setOnClickListener(this);
    }

    @Override
    protected void loadData() {
        HttpClientUtil.Friend.friendGet(userId, new AbsHttpResultHandler<FriendEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, FriendEntity data) {
                entity = data;

                String avatar = entity.getAvatar();
                if (StringUtil.isValid(avatar)) {
                    CircularImage imgHead = (CircularImage) findViewById(R.id.img_head);
                    ImageLoader.getInstance().displayImage(avatar, imgHead);
                }

                TextViewUtil.setText(FriendDetailActivity.this, R.id.txt_name, entity.getNickname());
                int v = entity.getV();
                TextViewUtil.setText(FriendDetailActivity.this, R.id.txt_tag_major, CommUtil.getFriendTag(entity.getGender(), v));
                TextViewUtil.setText(FriendDetailActivity.this, R.id.txt_time, entity.getTitle());


                stv1.setChecked(entity.isLookMe());
                stv2.setChecked(entity.isFriendNotLookHer());
                boolean blackList = entity.isBlackList();
                String txt = "";
                if(blackList){
                    txt = "把TA移出黑名单";
                }else{
                    txt = "把TA加入黑名单";
                }
                stv3.setLabel(txt);
                stv3.setChecked(blackList);

                stvFriend.setLabel("我们有" + entity.getSameFriend() + "个共同好友");
                String txtRelationTip = "和我的关系:";
                if (v == 1) {
                    // 一度好友
                    rlRelation.setVisibility(View.GONE);
                    llAction.setVisibility(View.VISIBLE);
                    llAdd.setVisibility(View.GONE);
                    llCall.setVisibility(View.VISIBLE);

                    txtRelationTip+="一度好友";
                }else if(v == 2){
                    // 二度好友
                    rlRelation.setVisibility(View.VISIBLE);
                    llAction.setVisibility(View.VISIBLE);

                    //<editor-fold desc="设置 好友关系图">
                    // 自己
                    UserInfoEntity userInfo = Global.getUserInfo();
                    String avatarMe = userInfo.getAvatar();
                    if (StringUtil.isValid(avatarMe)) {
                        CircularImage imgHead = (CircularImage) findViewById(R.id.img_friend1);
                        ImageLoader.getInstance().displayImage(avatarMe, imgHead);
                    }
                    TextViewUtil.setText(FriendDetailActivity.this,R.id.txt_friend1_name,userInfo.getNickname());

                    // 中间朋友
                    middleFriend = entity.getMiddleFriend();
                    String avatarMiddle = middleFriend.getAvatar();
                    if (StringUtil.isValid(avatarMiddle)) {
                        CircularImage imgHead = (CircularImage) findViewById(R.id.img_friend2);
                        ImageLoader.getInstance().displayImage(avatarMiddle, imgHead);
                        imgHead.setOnClickListener(FriendDetailActivity.this);
                    }
                    TextViewUtil.setText(FriendDetailActivity.this,R.id.txt_friend2_name,middleFriend.getNickname());

                    // 当前朋友
                    String avatarCurrent = entity.getAvatar();
                    if (StringUtil.isValid(avatarCurrent)) {
                        CircularImage imgHead = (CircularImage) findViewById(R.id.img_friend3);
                        ImageLoader.getInstance().displayImage(avatarCurrent, imgHead);
                    }
                    TextViewUtil.setText(FriendDetailActivity.this,R.id.txt_friend3_name,entity.getNickname());
                    //</editor-fold>

                    llAdd.setVisibility(View.VISIBLE);
                    llAdd.setEnabled(false);
                    llCall.setVisibility(View.GONE);
                    txtRelationTip+="二度好友";
                }else if(v == 3){
                    // 三度好友
                    rlRelation.setVisibility(View.GONE);
                    llAction.setVisibility(View.GONE);
                    llSameFriend.setVisibility(View.GONE);
                    txtRelationTip+="三度好友";
                    TextViewUtil.setText(FriendDetailActivity.this,R.id.txt_relation_tip,txtRelationTip);
                }

                stv1.setOnCheckedChangeListener(FriendDetailActivity.this);
                stv2.setOnCheckedChangeListener(FriendDetailActivity.this);
                stv3.setOnCheckedChangeListener(FriendDetailActivity.this);
            }

            @Override
            public void onFailure(int resultCode, String desc) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stv_friend:
                IntentUtil.goFriendSameActivity(this,entity.getUserId());
                break;
            case R.id.stv_dynamic:
                IntentUtil.goFriendDynamicActivity(this,entity.getUserId(), false);
                break;
            case R.id.stv_rquest:
                IntentUtil.goFriendHelpActivity(this,entity.getUserId(),false);
                break;
            case R.id.stv_shop:
                IntentUtil.goFriendPraiseShopActivity(this,entity.getUserId());
                break;
            case R.id.ll_chat:
                UserInfoEntity userEntity = new UserInfoEntity();
                userEntity.setUserId(DataUtil.getInt(this.entity.getUserId()));
                userEntity.setAvatar(this.entity.getAvatar());
                userEntity.setNickname(this.entity.getNickname());

                ChattingUtil.goChatting(this,userEntity);
                break;
            case R.id.ll_add:
                break;
            case R.id.ll_call:
                String phone = this.entity.getPhone();
                if(StringUtil.isInvalid(phone)){
                    ToastUtil.showToast(this,"暂无电话号码");
                    return;
                }
                CommUtil.call(this, phone);
                break;
            case R.id.img_friend2:
                FriendEntity entity1 = new FriendEntity();
                entity1.setUserId(middleFriend.getUserId());
                IntentUtil.goFriendDetailActivity(this,middleFriend.getUserId());
                break;


        }
    }

    @Override
    public void onCheckedChanged(SettingItemView settingItem, CompoundButton buttonView, boolean isChecked) {
        switch (settingItem.getId()){
            case R.id.stv_1:
                HttpClientUtil.Friend.friendSetFiliation(entity.getUserId(), isChecked, 1, new AbsHttpResultHandler() {
                    @Override
                    public void onSuccess(int resultCode, String desc, Object data) {
                        ToastUtil.showToast(FriendDetailActivity.this,"修改成功");
//                        loadData();
                    }

                    @Override
                    public void onFailure(int resultCode, String desc) {
                        ToastUtil.showToast(FriendDetailActivity.this,"修改失败:"+desc);
                    }
                });
                break;
            case R.id.stv_2:
                HttpClientUtil.Friend.friendSetFiliation(entity.getUserId(), isChecked, 2, new AbsHttpResultHandler() {
                    @Override
                    public void onSuccess(int resultCode, String desc, Object data) {
                        ToastUtil.showToast(FriendDetailActivity.this,"修改成功");
//                        loadData();
                    }

                    @Override
                    public void onFailure(int resultCode, String desc) {
                        ToastUtil.showToast(FriendDetailActivity.this,"修改失败:"+desc);
                    }
                });
                break;
            case R.id.stv_3:
                boolean isBlacklist = entity.isBlacklist();
                HttpClientUtil.Friend.friendUpdateBlacklist(DataUtil.getInt(entity.getUserId()), isChecked, new AbsHttpResultHandler() {
                    @Override
                    public void onSuccess(int resultCode, String desc, Object data) {
                        ToastUtil.showToast(FriendDetailActivity.this,"修改成功");
//                        loadData();
                    }

                    @Override
                    public void onFailure(int resultCode, String desc) {
                        ToastUtil.showToast(FriendDetailActivity.this,"修改失败:"+desc);
                    }
                });
                break;
        }
    }
}