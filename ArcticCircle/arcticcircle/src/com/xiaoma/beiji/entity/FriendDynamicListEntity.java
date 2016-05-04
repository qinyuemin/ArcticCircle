/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.entity
 * 版本信息： 1.0.0
 * Copyright (c) 2015
 */
package com.xiaoma.beiji.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;


public class FriendDynamicListEntity implements Serializable{

    @JSONField(name = "list")
    private List<FriendDynamicEntity> friendDynamicEntities;
    @JSONField(name = "total")
    private int total;
    @JSONField(name = "pages")
    private int pages;
    @JSONField(name = "login_user_id")
    private int userId;

    public List<FriendDynamicEntity> getFriendDynamicEntities() {
        return friendDynamicEntities;
    }

    public void setFriendDynamicEntities(List<FriendDynamicEntity> friendDynamicEntities) {
        this.friendDynamicEntities = friendDynamicEntities;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
