/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.entity
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.xiaoma.beiji.manager.chatting.IMConfig;

import java.io.Serializable;

/**
 * 类名称： XMPPMessage
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月18 1:47
 * 修改备注：
 *
 * @version 1.0.0
 */
public class IMXMPPMessageEntity implements Serializable{


    @JSONField(name = "_id")
    private int id;

    private String body;

    private int chatUserId;
    private String chatUserJid;

    @JSONField(name = "type")
    private String type;
    @JSONField(name = "to_user_id")
    private int toUserId;
    @JSONField(name = "from_user_id")
    private int fromUserId;
    @JSONField(name = "content")
    private String content;
    @JSONField(name = "nick_name")
    private String nickname;
    @JSONField(name = "avatar")
    private String avatar;
    @JSONField(name = "time")
    private String time;

    public boolean hasRead; // 0 false; 1 true
    private int userId;
    private int pid;
    private String timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getChatUserId() {
        return chatUserId;
    }

    public void setChatUserId(int chatUserId) {
        this.chatUserId = chatUserId;
    }

    public String getChatUserJid() {
        return chatUserJid;
    }

    public void setChatUserJid(String chatUserJid) {
        this.chatUserJid = chatUserJid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public boolean isHasRead() {
        return hasRead;
    }

    public void setHasRead(boolean hasRead) {
        this.hasRead = hasRead;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMsgShowType(int currentUserId) {
        return currentUserId == getFromUserId() ? IMConfig.SHOW_TYPE_TEXT_SEND : IMConfig.SHOW_TYPE_TEXT_RECEIVE; // 0 发送的；1接收到的
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
