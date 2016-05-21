/**
 * 项目名： eyah
 * 包名： com.sttri.eyah.util
 * 版本信息： 1.0.0
 * Copyright (c) -版权所有
 */
package com.xiaoma.beiji.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.xiaoma.beiji.R;
import com.xiaoma.beiji.activity.*;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.view.BottomBarView;
import com.xiaoma.beiji.controls.view.zxing.CaptureActivity;
import com.xiaoma.beiji.entity.ChatRequestEntity;
import com.xiaoma.beiji.entity.FriendDynamicEntity;
import com.xiaoma.beiji.entity.IMSystemMessageEntity;
import com.xiaoma.beiji.entity.PicEntity;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.fragment.CircleFragment;
import com.xiaoma.beiji.manager.chatting.IMChatService;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称： IntentUtil
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年06月10 10:25
 * 修改备注：
 *
 * @version 1.0.0
 */
public class IntentUtil {

    public static void startIMChatService(Activity activity){
        Intent intent = new Intent();
        intent.setAction(IMChatService.INTENT_IM_CHAT_SERVICE);
        activity.startService(intent);
    }

    public static void stopIMChatService(Context activity){
        Intent intent = new Intent();
        intent.setAction(IMChatService.INTENT_IM_CHAT_SERVICE);
        activity.stopService(intent);
    }

    /**
     * 跳转到注册页面
     *
     * @param from
     */
    public static void goUserRegisterActivity(Context from) {
        Intent intent = new Intent();
        intent.setClass(from, RegisterActivity.class);
        from.startActivity(intent);
    }

    /**
     * 跳转到登陆
     *
     * @param from
     */
    public static void goUserLoginActivity(Context from) {
        Intent intent = new Intent();
        intent.setClass(from, LoginActivity.class);
        from.startActivity(intent);
    }

    public static void goMainActivity(Activity from) {
        Intent intent = new Intent();
        intent.setClass(from, MainActivity.class);
        from.startActivity(intent);
    }

    public static void goMainActivity(Activity from, BottomBarView.PageItem item) {
        Intent intent = new Intent();
        intent.setClass(from, MainActivity.class);
        intent.putExtra("item", item);
        from.startActivity(intent);
    }

    public static void goProfileActivity(Context from, int userId) {
        Intent intent = new Intent();
        intent.setClass(from, ProfileActivity.class);
        intent.putExtra("user_id", userId);
        from.startActivity(intent);
    }

    public static void goStartActivity(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, StartActivity.class);
        activity.startActivity(intent);
    }

    public static void goRegisterPwdActivity(Activity activity, String phone, String code, String smsKey) {
        Intent intent = new Intent();
        intent.setClass(activity, RegisterPwdActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("code", code);
        intent.putExtra("smsKey", smsKey);
        activity.startActivity(intent);
    }

    public static void goRegisterInfoActivity(Activity activity, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(activity, RegisterInfoActivity.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    public static void goPhoneContactsActivity(Activity activity, boolean isFromRegister) {
        Intent intent = new Intent();
        intent.setClass(activity, PhoneContactsActivity.class);
        intent.putExtra("isFromRegister", isFromRegister);
        activity.startActivity(intent);
    }

    public static void goUserInfoActivity(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, UserInfoActivity.class);
        activity.startActivity(intent);
    }

    //店铺首页
    public static void goShopMainActivity(Activity activity, String shopId) {
        Intent intent = new Intent();
        intent.setClass(activity, ShopMainActivity.class);
        intent.putExtra("shopId", shopId);
        activity.startActivity(intent);
    }

    //店铺详情
    public static void goFriendDynamicDetailActivity(Context activity, FriendDynamicEntity entity) {
        Intent intent = new Intent();
        intent.setClass(activity, FriendDynamicDetailActivity.class);
        intent.putExtra("entity", entity);
        activity.startActivity(intent);
    }

    //头像放大
    public static void goHeadPhotoEnlargeActivity(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, HeadPhotoEnlargeActivity.class);
        activity.startActivity(intent);
    }

    //求助详情
    public static void goFriendHelpDetailActivity(Activity activity, String releaseId) {
        Intent intent = new Intent();
        intent.setClass(activity, FriendHelpDetailActivity.class);
        intent.putExtra("releaseId", releaseId);
        activity.startActivity(intent);
    }

    // 发布动态
    public static void goTrendsPublishActivity(Activity activity, int i) {
        Intent intent = new Intent(activity, PublishActivity.class);
        intent.putExtra("releaseType",i);
        activity.startActivity(intent);
    }

    // 发布长文
    public static void goTrendsPublishArticleActivity(Activity activity) {
        Intent intent = new Intent(activity, PublishArticleActivity.class);
        activity.startActivity(intent);
    }

    // 扫描
    public static void goScanActivity(CircleFragment context) {
        Intent intent = new Intent();
        intent.setClass(context.getFragmentActivity(), CaptureActivity.class);
        context.startActivityForResult(intent, CaptureActivity.INTENT_REQUEST_CODE);
    }

    // 个人设置
    public static void goSettingActivity(Activity activity) {
        Intent intent = new Intent(activity, SettingActivity.class);
        activity.startActivity(intent);
    }

    // 他们看不到我的朋友
    public static void goBlacklistActivity(Activity activity,int type) {
        Intent intent = new Intent(activity, FriendFiliationActivity.class);
        intent.putExtra("type",type);
        activity.startActivity(intent);
    }

    // 我的消息
    public static void goMessageListActivity(Activity activity) {
        Intent intent = new Intent(activity, MessageListActivity.class);
        activity.startActivity(intent);
    }

    // 收藏列表
    public static void goFavoriteListActivity(Activity activity) {
        Intent intent = new Intent(activity, FavoriteListActivity.class);
        activity.startActivity(intent);
    }

    // 聊天页面
    public static void goChattingActivity(Activity activity, UserInfoEntity userEntity) {
        Intent intent = new Intent(activity, ChattingActivity.class);
        intent.putExtra("entity",userEntity);
        activity.startActivity(intent);
    }

    public static void goSearchActivity(Activity activity) {
        Intent intent = new Intent(activity, SearchActivity.class);
        activity.startActivity(intent);
    }

    public static void goFriendDetailActivity(Activity activity, String userId) {
        if(userId.equals(String.valueOf(Global.getUserId()))){
            goMainActivity(activity, BottomBarView.PageItem.MINE);
        }else{
            Intent intent = new Intent(activity, FriendDetailActivity.class);
            intent.putExtra("userId",userId);
            activity.startActivity(intent);
        }
    }

    public static void goFriendArticleDetailActivity(Context activity, FriendDynamicEntity entity) {
        Intent intent = new Intent();
        intent.setClass(activity, ArticleDynamicDetailActivity.class);
        intent.putExtra("entity", entity);
        activity.startActivity(intent);
    }

    public static void goFriendDynamicActivity(Activity activity, String releaseUserId, boolean b) {
        Intent intent = new Intent(activity, FriendDynamicActivity.class);
        intent.putExtra("releaseUserId",releaseUserId);
        intent.putExtra("b",b);
        activity.startActivity(intent);
    }

    public static void goFriendHelpActivity(Activity activity, String releaseUserId, boolean b) {
        Intent intent = new Intent(activity, FriendHelpActivity.class);
        intent.putExtra("releaseUserId",releaseUserId);
        intent.putExtra("b",b);
        activity.startActivity(intent);
    }

    public static void goFriendSameActivity(Activity activity, String friendUserId) {
        Intent intent = new Intent(activity, FriendSameActivity.class);
        intent.putExtra("friendUserId",friendUserId);
        activity.startActivity(intent);
    }

    public static void goPictureDetailActivity(Activity activity, int picIndex, List<PicEntity> picList) {
        Intent intent = new Intent(activity, PictureDetailActivity.class);
        intent.putExtra("picIndex",picIndex);
        ArrayList<String> pics = new ArrayList<>();
        for(PicEntity pic : picList){
            pics.add(pic.getPicUrl());
        }
        intent.putStringArrayListExtra("picList",pics);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.anim_in_alpha, R.anim.anim_nochange);
    }

    public static void goShopDetailActivity(Activity activity, String shopId, String releaseId) {
        Intent intent = new Intent(activity, ShopDynamicDetailActivity.class);
        intent.putExtra("shopId", shopId);
        intent.putExtra("releaseId", releaseId);
        activity.startActivity(intent);
    }

    public static void goShopGoodsDetailActivity(Activity activity, String goodsId) {
        Intent intent = new Intent(activity, ShopGoodsDetailActivity.class);
        intent.putExtra("goodsId",goodsId);
        activity.startActivity(intent);
    }

    public static void goUserPwdChangeActivity(Activity activity) {
        Intent intent = new Intent(activity, UserPwdChangeActivity.class);
        activity.startActivity(intent);
    }

    public static void goAccountSafeActivity(Activity activity) {
        Intent intent = new Intent(activity, AccountSafeActivity.class);
        activity.startActivity(intent);
    }

    public static void goAccountSettingActivity(Activity activity) {
        Intent intent = new Intent(activity, AccountSettingActivity.class);
        activity.startActivity(intent);
    }

    public static void goClauseActivity(Activity activity) {
        Intent intent = new Intent(activity, ClauseActivity.class);
        activity.startActivity(intent);
    }

    public static void goChatReferralActivity(Activity activity, ChatRequestEntity data, UserInfoEntity friendUser) {
        Intent intent = new Intent(activity, ChatReferralActivity.class);
        intent.putExtra("entity",data);
        intent.putExtra("friendUser",friendUser);
        activity.startActivity(intent);
    }

    public static void goChatStatusActivity(Activity activity, ChatRequestEntity data) {
        Intent intent = new Intent(activity, ChatStatusActivity.class);
        intent.putExtra("entity",data);
        activity.startActivity(intent);
    }

    public static void goCommentMeActivity(Activity activity) {
        Intent intent = new Intent(activity, CommentMeActivity.class);
        activity.startActivity(intent);
    }

    public static void goFriendPraiseShopActivity(Activity activity, String userId) {
        Intent intent = new Intent(activity, FriendPraiseShopActivity.class);
        intent.putExtra("releaseUserId",userId);
        activity.startActivity(intent);
    }

    public static void goReferralCheckActivity(Activity activity, IMSystemMessageEntity entity) {
        Intent intent = new Intent(activity, ReferralCheckActivity.class);
        intent.putExtra("msg",entity);
        activity.startActivity(intent);
    }
}
