/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.entity
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**
 *
 * 类名称： FriendEntity
 * 类描述： 好友对象
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月09 17:37
 * 修改备注：
 * @version 1.0.0
 *
 *"user_id": "2",
"phone": "18502116250",
"nick_name": "吴传彧",
"v": 1,
"avatar": "http://testbjqcdn.xiaomakeji.com.cn/uploads/avatar/user_2_1449553143.jpg",
"gender": "2",
"title": "我有TA手机号码"

"middle_friend": {
"user_id": "2",
"avatar": "http://testbjqcdn.xiaomakeji.com.cn/uploads/avatar/user_2_1449553143.jpg",
"nick_name": "斯嘉丽"
},

 */
public class FriendEntity implements Serializable{
    @JSONField(name = "user_id")
    private String userId;
    @JSONField(name = "phone")
    private String phone;
    @JSONField(name = "nick_name")
    private String nickname;
    @JSONField(name = "v")
    private int v;
    @JSONField(name = "avatar")
    private String avatar;
    @JSONField(name = "gender")
    private int gender;
    @JSONField(name = "title")
    private String title;
   @JSONField(name = "user_phone")
    private String userPhone;
    @JSONField(name = "same_friend")
    private int sameFriend;
    @JSONField(name = "is_blacklist")
    public boolean isBlacklist;
    @JSONField(name = "is_look_me")
    public boolean isLookMe;
    @JSONField(name = "is_friend_not_look_her")
    public boolean isFriendNotLookHer;
    @JSONField(name = "middle_friend")
    private MiddleFriend middleFriend;
    @JSONField(name = "is_blacklist")
    public boolean isBlackList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public int getSameFriend() {
        return sameFriend;
    }

    public void setSameFriend(int sameFriend) {
        this.sameFriend = sameFriend;
    }

    public boolean isBlacklist() {
        return isBlacklist;
    }

    public void setBlacklist(boolean blacklist) {
        isBlacklist = blacklist;
    }

    public boolean isLookMe() {
        return isLookMe;
    }

    public void setLookMe(boolean lookMe) {
        isLookMe = lookMe;
    }

    public boolean isFriendNotLookHer() {
        return isFriendNotLookHer;
    }

    public void setFriendNotLookHer(boolean friendNotLookHer) {
        isFriendNotLookHer = friendNotLookHer;
    }

    public MiddleFriend getMiddleFriend() {
        return middleFriend;
    }

    public void setMiddleFriend(MiddleFriend middleFriend) {
        this.middleFriend = middleFriend;
    }

    public boolean isBlackList() {
        return isBlackList;
    }

    public void setBlackList(boolean isBlackList) {
        this.isBlackList = isBlackList;
    }

    public static class MiddleFriend implements Serializable{
//        "user_id": "2",
//                "avatar": "http://testbjqcdn.xiaomakeji.com.cn/uploads/avatar/user_2_1449553143.jpg",
//                "nick_name": "斯嘉丽"

        @JSONField(name = "user_id")
        private String userId;
        @JSONField(name = "avatar")
        private String avatar;
        @JSONField(name = "nick_name")
        private String nickname;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
