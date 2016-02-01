/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.entity
 * 版本信息： 1.0.0
 * Copyright (c) 2015
 */
package com.xiaoma.beiji.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 *
 * 类名称： CommentEntity
 * 类描述： 
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年11月21 17:17
 * 修改备注：
 * @version 1.0.0
 *
 *  "comment_id": "12",
"release_id": "59",
"user_id": "4",
"to_user_id": "0",
"comment_content": "\u4f60\u613f\u610f",
"create_time": "2015-12-02 16:52:44",
"nick_name": "Dfghjjjgh",
"avatar": null,
"gender": "1",
"relational_grade": 3

 *
 */
public class CommentEntity {
    @JSONField (name = "comment_id")
    private String commentId;
    @JSONField (name = "release_id")
    private String releaseId;
    @JSONField (name = "user_id")
    private String userId;
    @JSONField (name = "to_user_id")
    private String toUserId;
    @JSONField (name = "comment_content")
    private String commentContent;
    @JSONField (name = "create_time")
    private String createTime;
    @JSONField (name = "create_time_title")
    private String createTimeTitle;
    @JSONField (name = "nick_name")
    private String nickname;
    @JSONField (name = "avatar")
    private String avatar;
    @JSONField (name = "gender")
    private int gender;
    @JSONField (name = "relational_grade")
    private String relationalGrade;
    @JSONField (name = "to_user_nick_name")
    private String toUserNickName;
    @JSONField (name = "to_avatar")
    private String toAvatar;
    @JSONField (name = "release_user_name")
    private String releaseUsername;
    @JSONField (name = "description")
    private String description;
    @JSONField (name = "comment_title")
    private String comment_title;


    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(String releaseId) {
        this.releaseId = releaseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getRelationalGrade() {
        return relationalGrade;
    }

    public void setRelationalGrade(String relationalGrade) {
        this.relationalGrade = relationalGrade;
    }

    public String getCreateTimeTitle() {
        return createTimeTitle;
    }

    public void setCreateTimeTitle(String createTimeTitle) {
        this.createTimeTitle = createTimeTitle;
    }

    public String getToUserNickName() {
        return toUserNickName;
    }

    public void setToUserNickName(String toUserNickName) {
        this.toUserNickName = toUserNickName;
    }

    public String getToAvatar() {
        return toAvatar;
    }

    public void setToAvatar(String toAvatar) {
        this.toAvatar = toAvatar;
    }

    public String getReleaseUsername() {
        return releaseUsername;
    }

    public void setReleaseUsername(String releaseUsername) {
        this.releaseUsername = releaseUsername;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment_title() {
        return comment_title;
    }

    public void setComment_title(String comment_title) {
        this.comment_title = comment_title;
    }
}
