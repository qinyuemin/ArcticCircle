/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.entity
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 *
 * 类名称： RecommendMsgEntity
 * 类描述： 
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月30 11:37
 * 修改备注：
 * @version 1.0.0
 *
 * {
"to_user_id": 5,#被请求人
"to_user_nick_name": "aaa",#被请求人昵称
"to_user_avatar": "http://testbjqcdn.xiaomakeji.com.cn/uploads/avatar/user_5_1450265468.jpg",#被请求人头像
"from_user_id": 4,#请求人
"from_user_nick_name": "Dfghjjjgh",#请求人昵称
"from_user_avatar": "http://testbjqcdn.xiaomakeji.com.cn/uploads/avatar/user_4_1450258109.jpg",#请求人头像
"recommend_user_id": 2,#引荐人
"recommend_user_avatar": "http://testbjqcdn.xiaomakeji.com.cn/uploads/avatar/user_2_1449553143.jpg",#引荐人头像
"recommend_user_nick_name": "吴泉",#引荐人昵称
"type": "recommend",#xmpp消息类型
"content": "同意引荐",#结果文本
"status": 1,#引荐结果(1 同意 2不同意)
"time": "2015-12-17 11:09:41" #消息时间
}
 *
 */
public class RecommendMsgEntity {
    @JSONField(name = "to_user_id")
    private int toUserId;
    @JSONField(name = "to_user_nick_name")
    private String toUserNickname;
    @JSONField(name = "to_user_avatar")
    private String toUserAvatar;
    @JSONField(name = "from_user_id")
    private int fromUserId;
    @JSONField(name = "from_user_nick_name")
    private String fromUserNickname;
    @JSONField(name = "from_user_avatar")
    private String fromUserAvatar;
    @JSONField(name = "recommend_user_id")
    private int recommendUserId;
    @JSONField(name = "recommend_user_avatar")
    private String recommendUserAvatar;
    @JSONField(name = "recommend_user_nick_name")
    private String recommendUserNickname;
    @JSONField(name = "type")
    private String type;
    @JSONField(name = "content")
    private String content;
    @JSONField(name = "status")
    private int status;
    @JSONField(name = "time")
    private String time;

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public String getToUserNickname() {
        return toUserNickname;
    }

    public void setToUserNickname(String toUserNickname) {
        this.toUserNickname = toUserNickname;
    }

    public String getToUserAvatar() {
        return toUserAvatar;
    }

    public void setToUserAvatar(String toUserAvatar) {
        this.toUserAvatar = toUserAvatar;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUserNickname() {
        return fromUserNickname;
    }

    public void setFromUserNickname(String fromUserNickname) {
        this.fromUserNickname = fromUserNickname;
    }

    public String getFromUserAvatar() {
        return fromUserAvatar;
    }

    public void setFromUserAvatar(String fromUserAvatar) {
        this.fromUserAvatar = fromUserAvatar;
    }

    public int getRecommendUserId() {
        return recommendUserId;
    }

    public void setRecommendUserId(int recommendUserId) {
        this.recommendUserId = recommendUserId;
    }

    public String getRecommendUserAvatar() {
        return recommendUserAvatar;
    }

    public void setRecommendUserAvatar(String recommendUserAvatar) {
        this.recommendUserAvatar = recommendUserAvatar;
    }

    public String getRecommendUserNickname() {
        return recommendUserNickname;
    }

    public void setRecommendUserNickname(String recommendUserNickname) {
        this.recommendUserNickname = recommendUserNickname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
