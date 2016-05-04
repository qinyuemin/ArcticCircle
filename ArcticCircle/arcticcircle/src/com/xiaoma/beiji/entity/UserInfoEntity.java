/**
 * 项目名： simpleApp
 * 包名： com.company.simple.entity
 * 版本信息： 1.0.0
 * Copyright (c) 2015
 */
package com.xiaoma.beiji.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

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

    @JSONField (name = "address")
    private String address;

    @JSONField (name = "profile")
    private String profile;

    @JSONField (name = "label")
    private String label;

    @JSONField (name = "favorite_num")
    private String favorite_num;

    @JSONField (name = "dynamic_num")
    private String dynamic_num;

    @JSONField (name = "attention_user_num")
    private String attention_user_num;

    @JSONField (name = "attention_friend_num")
    private String attention_friend_num;

    @JSONField (name = "attention_num")
    private String attention_num;

    @JSONField (name = "is_register")
    private String is_register;

    @JSONField (name = "is_attention")
    private String is_attention;

    @JSONField (name = "last_dynamic_content")
    private String last_dynamic_content;

    @JSONField (name = "is_talent")
    private String is_talent;

    @JSONField (name = "cant_see_me")
    private String cant_see_me;

    @JSONField (name = "my_friend_cant_see_his")
    private String my_friend_cant_see_his;

    @JSONField (name = "dynamic_list")
    private FriendDynamicListEntity release_data;

    @JSONField (name = "common_friends")
    private CommonFriends common_friends;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFavorite_num() {
        return favorite_num;
    }

    public void setFavorite_num(String favorite_num) {
        this.favorite_num = favorite_num;
    }

    public String getDynamic_num() {
        return dynamic_num;
    }

    public void setDynamic_num(String dynamic_num) {
        this.dynamic_num = dynamic_num;
    }

    public String getAttention_user_num() {
        return attention_user_num;
    }

    public void setAttention_user_num(String attention_user_num) {
        this.attention_user_num = attention_user_num;
    }

    public String getAttention_friend_num() {
        return attention_friend_num;
    }

    public void setAttention_friend_num(String attention_friend_num) {
        this.attention_friend_num = attention_friend_num;
    }

    public String getAttention_num() {
        return attention_num;
    }

    public void setAttention_num(String attention_num) {
        this.attention_num = attention_num;
    }

    public String getIs_register() {
        return is_register;
    }

    public void setIs_register(String is_register) {
        this.is_register = is_register;
    }

    public String getIs_attention() {
        return is_attention;
    }

    public void setIs_attention(String is_attention) {
        this.is_attention = is_attention;
    }

    public String getLast_dynamic_content() {
        return last_dynamic_content;
    }

    public void setLast_dynamic_content(String last_dynamic_content) {
        this.last_dynamic_content = last_dynamic_content;
    }

    public String getIs_talent() {
        return is_talent;
    }

    public void setIs_talent(String is_talent) {
        this.is_talent = is_talent;
    }

    public String getCant_see_me() {
        return cant_see_me;
    }

    public void setCant_see_me(String cant_see_me) {
        this.cant_see_me = cant_see_me;
    }

    public String getMy_friend_cant_see_his() {
        return my_friend_cant_see_his;
    }

    public void setMy_friend_cant_see_his(String my_friend_cant_see_his) {
        this.my_friend_cant_see_his = my_friend_cant_see_his;
    }

    public FriendDynamicListEntity getRelease_data() {
        return release_data;
    }

    public void setRelease_data(FriendDynamicListEntity release_data) {
        this.release_data = release_data;
    }

    public CommonFriends getCommon_friends() {
        return common_friends;
    }

    public void setCommon_friends(CommonFriends common_friends) {
        this.common_friends = common_friends;
    }

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

    @Override
    public String toString() {
        return "UserInfoEntity{" +
                "userId=" + userId +
                ", userPhone='" + userPhone + '\'' +
                ", userPwd='" + userPwd + '\'' +
                ", userSession='" + userSession + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", gender='" + gender + '\'' +
                ", lastLoginTime='" + lastLoginTime + '\'' +
                ", sessionExpireTime='" + sessionExpireTime + '\'' +
                ", isEnabled='" + isEnabled + '\'' +
                ", createTime='" + createTime + '\'' +
                ", address='" + address + '\'' +
                ", profile='" + profile + '\'' +
                ", label='" + label + '\'' +
                ", favorite_num='" + favorite_num + '\'' +
                ", dynamic_num='" + dynamic_num + '\'' +
                ", attention_user_num='" + attention_user_num + '\'' +
                ", attention_friend_num='" + attention_friend_num + '\'' +
                ", attention_num='" + attention_num + '\'' +
                ", is_register='" + is_register + '\'' +
                ", is_attention='" + is_attention + '\'' +
                ", last_dynamic_content='" + last_dynamic_content + '\'' +
                ", is_talent='" + is_talent + '\'' +
                ", cant_see_me='" + cant_see_me + '\'' +
                ", my_friend_cant_see_his='" + my_friend_cant_see_his + '\'' +
                ", release_data=" + release_data +
                ", common_friends=" + common_friends +
                '}';
    }
}
