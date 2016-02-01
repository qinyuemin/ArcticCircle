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
 *
 * 类名称： ShopDynamicEntity
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月15 17:10
 * 修改备注：
 * @version 1.0.0
 *
 */
public class ShopDynamicDetailEntity {
    @JSONField(name = "release_id")
    private String releaseId;
    @JSONField(name = "release_type")
    private String releaseType;
    @JSONField(name = "dynamic_id")
    private String dynamicId;
    @JSONField(name = "area")
    private String area;
    @JSONField(name = "user_id")
    private String userId;
    @JSONField(name = "description")
    private String description;
    @JSONField(name = "shop_id")
    private String shopId;
    @JSONField(name = "create_time")
    private String createTime;
    @JSONField(name = "release_type_title")
    private String releaseTypeTitle;
    @JSONField(name = "create_time_title")
    private String createTimeTitle;
    @JSONField(name = "content")
    private String content;
    @JSONField(name = "praise_num")
    private String praiseNum;
    @JSONField(name = "is_have_praise")
    private String isHavePraise;
    @JSONField(name = "comment_num")
    private String commentNum;
    @JSONField(name = "shop_name")
    private String shopName;
    @JSONField(name = "address")
    private String address;
    @JSONField(name = "pic")
    private List<PicEntity> picList;
    @JSONField(name = "comment")
    private List<CommentEntity> commentEntities;



    public String getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(String releaseId) {
        this.releaseId = releaseId;
    }

    public String getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(String releaseType) {
        this.releaseType = releaseType;
    }

    public String getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getReleaseTypeTitle() {
        return releaseTypeTitle;
    }

    public void setReleaseTypeTitle(String releaseTypeTitle) {
        this.releaseTypeTitle = releaseTypeTitle;
    }

    public String getCreateTimeTitle() {
        return createTimeTitle;
    }

    public void setCreateTimeTitle(String createTimeTitle) {
        this.createTimeTitle = createTimeTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(String praiseNum) {
        this.praiseNum = praiseNum;
    }

    public String getIsHavePraise() {
        return isHavePraise;
    }

    public void setIsHavePraise(String isHavePraise) {
        this.isHavePraise = isHavePraise;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public List<PicEntity> getPicList() {
        return picList;
    }

    public void setPicList(List<PicEntity> picList) {
        this.picList = picList;
    }

    public List<CommentEntity> getCommentEntities() {
        return commentEntities;
    }

    public void setCommentEntities(List<CommentEntity> commentEntities) {
        this.commentEntities = commentEntities;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
