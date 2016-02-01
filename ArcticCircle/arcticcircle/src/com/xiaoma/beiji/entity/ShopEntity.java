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
 * 类名称： ShopEntity
 * 类描述：  店铺对象
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月09 17:05
 * 修改备注：
 * @version 1.0.0
 *
 * "shop_id": "5",
"shop_name": "北极圈1",
"user_id": "1",
"primary_business": "土鸡蛋",
"latitude": "31.201364",
"longitude": "121.439308",
"province_name": "",
"city_name": "",
"district_name": "",
"address": "哦哦",
"phone": "18666663810",
"tel": "65656565",
"shop_pic": [
{
"pic_id": "89",
"pic_url": "http://testbjqcdn.xiaomakeji.com.cn/uploads/shop/7bheR2HlaBX4eMndJcjrAtovu7a0NDK44282LXWjWyozhOIpi9fmazsggx7O2M0n.jpg"
}
],
"shop_praise": 0
 */
public class ShopEntity implements Serializable{
    @JSONField(name = "shop_id")
    private String shopId;
    @JSONField(name = "shop_name")
    private String showName;
    @JSONField(name = "user_id")
    private String userId;
    @JSONField(name = "primary_business")
    private String primaryBusiness;
    @JSONField(name = "latitude")
    private String latitude;
    @JSONField(name = "longitude")
    private String longitude;
    @JSONField(name = "province_name")
    private String provinceName;
    @JSONField(name = "city_name")
    private String cityName;
    @JSONField(name = "district_name")
    private String districtName;
    @JSONField(name = "address")
    private String address;
    @JSONField(name = "phone")
    private String phone;
    @JSONField(name = "tel")
    private String tel;
    @JSONField(name = "shop_pic")
    private List<PicEntity> picEntities;
    @JSONField(name = "list")
    private List<ShopDynamicEntity> dynamicEntities;
    @JSONField(name = "comment_list")
    private CommentListEntity commentListEntity;
    @JSONField(name = "online")
    private ShopOnlineEntity shopOnlineEntity;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrimaryBusiness() {
        return primaryBusiness;
    }

    public void setPrimaryBusiness(String primaryBusiness) {
        this.primaryBusiness = primaryBusiness;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public List<PicEntity> getPicEntities() {
        return picEntities;
    }

    public void setPicEntities(List<PicEntity> picEntities) {
        this.picEntities = picEntities;
    }

    public List<ShopDynamicEntity> getDynamicEntities() {
        return dynamicEntities;
    }

    public void setDynamicEntities(List<ShopDynamicEntity> dynamicEntities) {
        this.dynamicEntities = dynamicEntities;
    }

    public CommentListEntity getCommentListEntity() {
        return commentListEntity;
    }

    public void setCommentListEntity(CommentListEntity commentListEntity) {
        this.commentListEntity = commentListEntity;
    }

    public ShopOnlineEntity getShopOnlineEntity() {
        return shopOnlineEntity;
    }

    public void setShopOnlineEntity(ShopOnlineEntity shopOnlineEntity) {
        this.shopOnlineEntity = shopOnlineEntity;
    }
}
