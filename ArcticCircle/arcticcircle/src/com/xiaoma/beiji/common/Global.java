/**
 * 项目名： eyah
 * 包名： com.sttri.eyah.common
 * 版本信息： 1.0.0
 * Copyright (c) -版权所有
 */
package com.xiaoma.beiji.common;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.common.android.lib.util.SharedPreferencesUtil;
import com.makeapp.javase.lang.StringUtil;
import com.xiaoma.beiji.base.BaseApplication;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.entity.XmppUserEntity;

/**
 * 类名称： Global
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年06月20 10:25
 * 修改备注：
 *
 * @version 1.0.0
 */
public class Global {
    private static UserInfoEntity userInfo;
    private static XmppUserEntity xmppUserEntity;
    private static String appVersion;
    private static int appVersionCode;
    private static String phoneUniqueId;
    private static boolean isNeedRefreshIndex = false;

    public static String getUserPhone() {
        String phone = "";
        if (userInfo == null) {
            getUserInfo();
        }
        if (userInfo != null) {
            phone = userInfo.getUserPhone();
        }
        return phone;
    }

    public static void setUserInfo(UserInfoEntity entity) {
        userInfo = entity;
        // 存
        if (userInfo != null) {
            Context context = BaseApplication.getInstance().getApplicationContext();
            String prfName = AppConstants.SHARED_PREFERENCES_BASE_NAME;
            SharedPreferencesUtil.setSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_ID, entity.getUserId());
            SharedPreferencesUtil.setSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_NICKNAME, entity.getNickname());
            SharedPreferencesUtil.setSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_PHONE, entity.getUserPhone());
            SharedPreferencesUtil.setSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_SESSION, entity.getUserSession());
            SharedPreferencesUtil.setSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_AVATAR, entity.getAvatar());
            SharedPreferencesUtil.setSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_GENDER, entity.getGender());
            SharedPreferencesUtil.setSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_LABLE, entity.getLabel());
            SharedPreferencesUtil.setSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_ADDRESS, entity.getAddress());
            SharedPreferencesUtil.setSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_PROFILE, entity.getProfile());
            SharedPreferencesUtil.setSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_LAST_LOGIN_TIME, entity.getLastLoginTime());
            SharedPreferencesUtil.setSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_SESSION_EXPIRE_TIME, entity.getSessionExpireTime());
            SharedPreferencesUtil.setSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_IS_ENABLED, entity.getIsEnabled());
            SharedPreferencesUtil.setSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_CREATE_TIME, entity.getCreateTime());
        }
    }

    public static void cleanUser(){
        userInfo = null;
        Context context = BaseApplication.getInstance().getApplicationContext();
        String prfName = AppConstants.SHARED_PREFERENCES_BASE_NAME;
        SharedPreferencesUtil.removeSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_ID);
        SharedPreferencesUtil.removeSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_NICKNAME);
        SharedPreferencesUtil.removeSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_PHONE);
        SharedPreferencesUtil.removeSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_SESSION);
        SharedPreferencesUtil.removeSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_AVATAR);
        SharedPreferencesUtil.removeSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_GENDER);
        SharedPreferencesUtil.removeSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_LABLE);
        SharedPreferencesUtil.removeSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_ADDRESS);
        SharedPreferencesUtil.removeSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_PROFILE);
        SharedPreferencesUtil.removeSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_LAST_LOGIN_TIME);
        SharedPreferencesUtil.removeSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_SESSION_EXPIRE_TIME);
        SharedPreferencesUtil.removeSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_IS_ENABLED);
        SharedPreferencesUtil.removeSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_CREATE_TIME);
    }

    public static UserInfoEntity getUserInfo() {
        if (userInfo == null) {
            // 取
            userInfo = new UserInfoEntity();
            Context context = BaseApplication.getInstance().getApplicationContext();
            String prfName = AppConstants.SHARED_PREFERENCES_BASE_NAME;
            userInfo.setUserId(SharedPreferencesUtil.getSettingInt(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_ID));
            userInfo.setNickname(SharedPreferencesUtil.getSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_NICKNAME));
            userInfo.setUserPhone(SharedPreferencesUtil.getSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_PHONE));
            userInfo.setUserSession(SharedPreferencesUtil.getSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_SESSION));
            userInfo.setAvatar(SharedPreferencesUtil.getSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_AVATAR));
            userInfo.setGender(SharedPreferencesUtil.getSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_GENDER));
            userInfo.setLabel(SharedPreferencesUtil.getSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_LABLE));
            userInfo.setAddress(SharedPreferencesUtil.getSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_ADDRESS));
            userInfo.setProfile(SharedPreferencesUtil.getSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_PROFILE));
            userInfo.setLastLoginTime(SharedPreferencesUtil.getSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_LAST_LOGIN_TIME));
            userInfo.setSessionExpireTime(SharedPreferencesUtil.getSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_SESSION_EXPIRE_TIME));
            userInfo.setIsEnabled(SharedPreferencesUtil.getSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_IS_ENABLED));
            userInfo.setCreateTime(SharedPreferencesUtil.getSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_CREATE_TIME));
        }
        return userInfo;
    }

    public static int getUserId() {
        int userId = -1;
        if (userInfo == null) {
            getUserInfo();
        }
        if (userInfo != null) {
            userId = userInfo.getUserId();
        }
        return userId;
    }

    public static String getUserSession() {
        String userSession = "";
        if (userInfo == null) {
            getUserInfo();
        }
        if (userInfo != null) {
            userSession = userInfo.getUserSession();
        }
        return userSession;
    }

    public static XmppUserEntity getXmppUserEntity() {
        return xmppUserEntity;
    }

    public static void setXmppUserEntity(XmppUserEntity xmppUserEntity) {
        Global.xmppUserEntity = xmppUserEntity;
    }

    public static boolean isLogin() {
        if (userInfo == null) {
            userInfo = getUserInfo();
        }
        return userInfo != null && StringUtil.isValid(userInfo.getUserPhone());
    }

    public static void logout() {
        Context context = BaseApplication.getInstance().getApplicationContext();
        String prfName = AppConstants.SHARED_PREFERENCES_BASE_NAME;
        SharedPreferencesUtil.setSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_ID, -1);
        SharedPreferencesUtil.setSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_NICKNAME, "");
        SharedPreferencesUtil.setSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_PHONE, "");
        SharedPreferencesUtil.setSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_SESSION, "");
        SharedPreferencesUtil.setSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_AVATAR, "");
        SharedPreferencesUtil.setSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_GENDER, "");
        SharedPreferencesUtil.setSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_LAST_LOGIN_TIME, "");
        SharedPreferencesUtil.setSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_SESSION_EXPIRE_TIME, "");
        SharedPreferencesUtil.setSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_IS_ENABLED, "");
        SharedPreferencesUtil.setSetting(context, prfName, AppConstants.SharedPreferencesConstant.SHARED_USER_CREATE_TIME, "");
    }

    public static String getPhoneUniqueId() {
        if (phoneUniqueId == null) {
            phoneUniqueId = "sfafafaefaefaefasfeasef";
        }
        return phoneUniqueId;
    }

    public static String getAppVersion() {
        if (appVersion == null) {
            try {
                PackageManager manager = BaseApplication.getInstance().getPackageManager();
                PackageInfo info = manager.getPackageInfo(BaseApplication.getInstance().getPackageName(), 0);
                appVersion = info.versionName;
            } catch (Exception e) {
                return "UNKNOW";
            }
        }
        return appVersion;
    }

    public static int getAppVersionCode() {
        if (appVersionCode == 0) {
            try {
                PackageManager manager = BaseApplication.getInstance().getPackageManager();
                PackageInfo info = manager.getPackageInfo(BaseApplication.getInstance().getPackageName(), 0);
                appVersionCode = info.versionCode;
            } catch (Exception e) {
                return -1;
            }
        }
        return appVersionCode;
    }

    // 判定 首页 是否需要刷新
    public static boolean isNeedRefreshIndex(){
        return isNeedRefreshIndex;
    }

    public static void setIsNeedRefreshIndex(boolean b){
        isNeedRefreshIndex = b;
    }

}
