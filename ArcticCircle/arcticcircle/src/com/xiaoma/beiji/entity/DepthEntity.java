/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.entity
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 类名称： DepthEntity
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月03 21:43
 * 修改备注：
 *
 * @version 1.0.0
 *          <p/>
 *          "user_id": "4",
 *          "dynamic_depth": "3",
 *          "help_depth": "3",
 *          "chat_depth": "1",
 *          "dynamic_depth_title": "三度好友",
 *          "help_depth_title": "三度好友",
 *          "chat_depth_title": "一度好友"
 */
public class DepthEntity {
    @JSONField(name = "user_id")
    private String userId;
    @JSONField(name = "dynamic_depth")
    private int dynamicDepth;
    @JSONField(name = "help_depth")
    private int helpDepth;
    @JSONField(name = "chat_depth")
    private int chatDepth;
    @JSONField(name = "dynamic_depth_title")
    private String dynamicDepthTitle;
    @JSONField(name = "help_depth_title")
    private String helpDepthTitle;
    @JSONField(name = "chat_depth_title")
    private String chatDepthTitle;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getDynamicDepth() {
        return dynamicDepth;
    }

    public void setDynamicDepth(int dynamicDepth) {
        this.dynamicDepth = dynamicDepth;
    }

    public int getHelpDepth() {
        return helpDepth;
    }

    public void setHelpDepth(int helpDepth) {
        this.helpDepth = helpDepth;
    }

    public int getChatDepth() {
        return chatDepth;
    }

    public void setChatDepth(int chatDepth) {
        this.chatDepth = chatDepth;
    }

    public String getDynamicDepthTitle() {
        return dynamicDepthTitle;
    }

    public void setDynamicDepthTitle(String dynamicDepthTitle) {
        this.dynamicDepthTitle = dynamicDepthTitle;
    }

    public String getHelpDepthTitle() {
        return helpDepthTitle;
    }

    public void setHelpDepthTitle(String helpDepthTitle) {
        this.helpDepthTitle = helpDepthTitle;
    }

    public String getChatDepthTitle() {
        return chatDepthTitle;
    }

    public void setChatDepthTitle(String chatDepthTitle) {
        this.chatDepthTitle = chatDepthTitle;
    }
}
