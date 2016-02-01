/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.entity
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 *
 * 类名称： ChatRequestEntity
 * 类描述： 聊天请求 返回结果的 实体类
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月19 23:40
 * 修改备注：
 * @version 1.0.0
 *
 */
public class ChatRequestEntity implements Serializable{
    @JSONField(name = "success")
    private boolean success;
    @JSONField(name = "status")
    private int status;
    @JSONField(name = "content")
    private String content;
    @JSONField(name = "recommend_user_id")
    private int recommendUserId;
    @JSONField(name = "recommend_nick_name")
    private String recommendNickname;
    @JSONField(name = "recommend_avatar")
    private String recommendAvatar;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRecommendUserId() {
        return recommendUserId;
    }

    public void setRecommendUserId(int recommendUserId) {
        this.recommendUserId = recommendUserId;
    }

    public String getRecommendNickname() {
        return recommendNickname;
    }

    public void setRecommendNickname(String recommendNickname) {
        this.recommendNickname = recommendNickname;
    }

    public String getRecommendAvatar() {
        return recommendAvatar;
    }

    public void setRecommendAvatar(String recommendAvatar) {
        this.recommendAvatar = recommendAvatar;
    }
}
