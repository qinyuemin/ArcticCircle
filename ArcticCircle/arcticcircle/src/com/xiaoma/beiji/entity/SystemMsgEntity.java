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
 * 类名称： SystemMsgEntity
 * 类描述： 系统 消息通知过来时 的对象
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月21 22:42
 * 修改备注：
 * @version 1.0.0
 *
 * {
"releasep_id": 81,
"content": "必须要添加胜多负少",
"type": "help",
"title": "国成 发布了求助信息",
"time": "2015-11-24 20:05:09"
}
 */
public class SystemMsgEntity {

    @JSONField(name = "_id")
    private int id;
    @JSONField(name = "release_id")
    private int releaseId;
    @JSONField(name = "content")
    private String content;
    @JSONField(name = "type")
    private String type;
    @JSONField(name = "title")
    private String title;
    @JSONField(name = "time")
    private String time;

    public int getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(int releaseId) {
        this.releaseId = releaseId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
