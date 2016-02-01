/**
 * 项目名： simpleApp
 * 包名： com.company.simple.entity
 * 版本信息： 1.0.0
 * Copyright (c) 2015
 */
package com.xiaoma.beiji.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 *
 * 类名称： UserInfoEntity
 * 类描述： 
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年11月16 14:20
 * 修改备注：
 * @version 1.0.0
 *
 *
 *  "user_id": 4,
"user_phone": "18616349511",
"user_pwd": "36488272ca3c34aca928f7bda33f97dc",
"user_session": "0d459cf3721a0225b97e6d01a4dc9031",
"nick_name": "Dfghjjjgh",
"avatar": "",
"gender": "1",
"last_login_time": "2015-12-01 10:56:50",
"session_expire_time": "2015-12-08 10:56:50",
"is_enabled": "1",
"create_time": "0000-00-00 00:00:00"

 */
public class UserInfoEntity implements Serializable{
    @JSONField (name = "user_id")
    private int userId;
    @JSONField (name = "user_phone")
    private String userPhone;
    @JSONField (name = "user_pwd")
    private String userPwd;
    @JSONField (name = "user_session")
    private String userSession;
    @JSONField (name = "nick_name")
    private String nickname;
    @JSONField (name = "avatar")
    private String avatar;
    @JSONField (name = "gender")
    private String gender;
    @JSONField (name = "last_login_time")
    private String lastLoginTime;
    @JSONField (name = "session_expire_time")
    private String sessionExpireTime;
    @JSONField (name = "is_enabled")
    private String isEnabled;
    @JSONField (name = "create_time")
    private String createTime;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserSession() {
        return userSession;
    }

    public void setUserSession(String userSession) {
        this.userSession = userSession;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getSessionExpireTime() {
        return sessionExpireTime;
    }

    public void setSessionExpireTime(String sessionExpireTime) {
        this.sessionExpireTime = sessionExpireTime;
    }

    public String getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(String isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
