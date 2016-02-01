/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.entity
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 类名称： SearchEntity
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月09 16:59
 * 修改备注：
 *
 * @version 1.0.0
 */
public class SearchEntity {
    @JSONField(name = "user")
    private List<FriendEntity> userEntities;

    @JSONField(name = "release_help")
    private String help;

    @JSONField(name = "release_dynamic")
    private List<FriendDynamicEntity> releaseDynamic;

    @JSONField(name = "shop")
    private List<ShopEntity> shopEntities;

    public List<FriendEntity> getUserEntities() {
        return userEntities;
    }

    public void setUserEntities(List<FriendEntity> userEntities) {
        this.userEntities = userEntities;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public List<FriendDynamicEntity> getReleaseDynamic() {
        return releaseDynamic;
    }

    public void setReleaseDynamic(List<FriendDynamicEntity> releaseDynamic) {
        this.releaseDynamic = releaseDynamic;
    }

    public List<ShopEntity> getShopEntities() {
        return shopEntities;
    }

    public void setShopEntities(List<ShopEntity> shopEntities) {
        this.shopEntities = shopEntities;
    }
}
