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

/**
 * 类名称： FriendTrendsEntity
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年11月21 14:59
 * 修改备注：
 *
 * @version 1.0.0
 *          <p/>
 *          {
 *          "release_id": "56",
 *          "release_type": "1",
 *          "dynamic_id": "67",
 *          "area": "",
 *          "user_id": "4",
 *          "description": "Cjxjxjxjxj",
 *          "shop_id": "0",
 *          "create_time": "2015-12-01 22:45:38",
 *          "release_type_title": "发布动态",
 *          "login_user_id": null,
 *          "comment": [],
 *          "nick_name": "Dfghjjjgh",
 *          "avatar": "",
 *          "gender": "1",
 *          "gender_title": "男",
 *          "relational_grade": "三度好友",
 *          "is_have_favorite": false,
 *          "is_have_praise": false,
 *          "content": "Cjxjxjxjxj",s
 *          "pic": [],
 *          "praise_num": null
 *          }
 *
 *          动态type字段
1 用户动态 2 用户求助  3 用户推荐店铺 4 店铺推广 5 朋友加入
 */
public class FriendDynamicEntity  implements Serializable{
    @JSONField(name = "release_id")
    private String releaseId;
    @JSONField(name = "release_type")
    private int releaseType;
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
    @JSONField(name = "create_time_title")
    private String createTimeTitle;
    @JSONField(name = "release_type_title")
    private String releaseTypeTitle;
    @JSONField(name = "login_user_id")
    private String loginUserId;
    @JSONField(name = "comment")
    private List<CommentEntity> comment;
    @JSONField(name = "nick_name")
    private String nickName;
    @JSONField(name = "avatar")
    private String avatar;
    @JSONField(name = "gender")
    private int gender;
    @JSONField(name = "gender_title")
    private String genderTitle;
    @JSONField(name = "relational_grade")
    private String relationalGrade;
    @JSONField(name = "is_have_favorite")
    public boolean isHaveFavorite; // 设置 public 原因：fastJson 转换时 先直接使用，私有变量是通过set方式找，is开头变量生成 set方法时 为 is。。。所以直接修改
    @JSONField(name = "is_have_praise")
    public boolean isHavePraise;
    @JSONField(name = "content")
    private String content;
    @JSONField(name = "pic")
    private List<PicEntity> pic;
    @JSONField(name = "praise_num")
    private String praiseNum;
    @JSONField(name = "page_id")
    private String pageId;

    // 店铺属性
    @JSONField(name = "shop_name")
    private String shopName;
    @JSONField(name = "shop_pic")
    private List<String> shopPic;
    @JSONField(name = "shop_praise")
    private String shopPraise;

    @JSONField(name = "latitude")
    private String latitude;
    @JSONField(name = "longitude")
    private String longitude;

    @JSONField(name = "comment_num")
    private String commentNum;
    @JSONField(name = "favorite_num")
    private String favoriteNum;

    @JSONField(name = "praise_avatar_user")
    private List<String> praise_avatar_user;

    @JSONField(name = "share_user_nickname")
    private List<String> share_user_nickname;

    @JSONField(name = "title")
    private String title;

    @JSONField(name = "associated_shop_name")
    private String associated_shop_name;

    @JSONField(name = "associated_price")
    private String associated_price;

    @JSONField(name = "primary_business")
    private String primary_business;

    @JSONField(name = "original_user_data")
    private UserInfoEntity original_user_data;

    @JSONField(name = "release_favorite_id")
    private int release_favorite_id;

    @JSONField(name = "showAll1")
    private boolean showAll1;

    @JSONField(name = "showAll2")
    private boolean showAll2;

    @JSONField(name = "praise_data")
    private List<UserInfoEntity> praiseUsers;

    public List<UserInfoEntity> getPraiseUsers() {
        return praiseUsers;
    }

    public void setPraiseUsers(List<UserInfoEntity> praiseUsers) {
        this.praiseUsers = praiseUsers;
    }

    public void setIsHaveFavorite(boolean isHaveFavorite) {
        this.isHaveFavorite = isHaveFavorite;
    }

    public void setIsHavePraise(boolean isHavePraise) {
        this.isHavePraise = isHavePraise;
    }

    public List<String> getPraise_avatar_user() {
        return praise_avatar_user;
    }

    public void setPraise_avatar_user(List<String> praise_avatar_user) {
        this.praise_avatar_user = praise_avatar_user;
    }

    public List<String> getShare_user_nickname() {
        return share_user_nickname;
    }

    public void setShare_user_nickname(List<String> share_user_nickname) {
        this.share_user_nickname = share_user_nickname;
    }

    public String getAssociated_shop_name() {
        return associated_shop_name;
    }

    public void setAssociated_shop_name(String associated_shop_name) {
        this.associated_shop_name = associated_shop_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAssociated_price() {
        return associated_price;
    }

    public void setAssociated_price(String associated_price) {
        this.associated_price = associated_price;
    }

    public String getPrimary_business() {
        return primary_business;
    }

    public void setPrimary_business(String primary_business) {
        this.primary_business = primary_business;
    }

    public UserInfoEntity getOriginal_user_data() {
        return original_user_data;
    }

    public void setOriginal_user_data(UserInfoEntity original_user_data) {
        this.original_user_data = original_user_data;
    }

    public int getRelease_favorite_id() {
        return release_favorite_id;
    }

    public void setRelease_favorite_id(int release_favorite_id) {
        this.release_favorite_id = release_favorite_id;
    }

    public boolean isShowAll1() {
        return showAll1;
    }

    public void setShowAll1(boolean showAll1) {
        this.showAll1 = showAll1;
    }

    public boolean isShowAll2() {
        return showAll2;
    }

    public void setShowAll2(boolean showAll2) {
        this.showAll2 = showAll2;
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

    public String getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(String releaseId) {
        this.releaseId = releaseId;
    }

    public int getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(int releaseType) {
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

    public String getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(String loginUserId) {
        this.loginUserId = loginUserId;
    }

    public List<CommentEntity> getComment() {
        return comment;
    }

    public void setComment(List<CommentEntity> comment) {
        this.comment = comment;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public String getGenderTitle() {
        return genderTitle;
    }

    public void setGenderTitle(String genderTitle) {
        this.genderTitle = genderTitle;
    }

    public String getRelationalGrade() {
        return relationalGrade;
    }

    public void setRelationalGrade(String relationalGrade) {
        this.relationalGrade = relationalGrade;
    }

    public boolean isHaveFavorite() {
        return isHaveFavorite;
    }

    public void setHaveFavorite(boolean haveFavorite) {
        isHaveFavorite = haveFavorite;
    }

    public boolean isHavePraise() {
        return isHavePraise;
    }

    public void setHavePraise(boolean havePraise) {
        isHavePraise = havePraise;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<PicEntity> getPic() {
        return pic;
    }

    public void setPic(List<PicEntity> pic) {
        this.pic = pic;
    }

    public String getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(String praiseNum) {
        this.praiseNum = praiseNum;
    }

    public String getCreateTimeTitle() {
        return createTimeTitle;
    }

    public void setCreateTimeTitle(String createTimeTitle) {
        this.createTimeTitle = createTimeTitle;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<String> getShopPic() {
        return shopPic;
    }

    public void setShopPic(List<String> shopPic) {
        this.shopPic = shopPic;
    }

    public String getShopPraise() {
        return shopPraise;
    }

    public void setShopPraise(String shopPraise) {
        this.shopPraise = shopPraise;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public String getFavoriteNum() {
        return favoriteNum;
    }

    public void setFavoriteNum(String favoriteNum) {
        this.favoriteNum = favoriteNum;
    }

    @Override
    public String toString() {
        return "FriendDynamicEntity{" +
                "releaseId='" + releaseId + '\'' +
                ", releaseType=" + releaseType +
                ", dynamicId='" + dynamicId + '\'' +
                ", area='" + area + '\'' +
                ", userId='" + userId + '\'' +
                ", description='" + description + '\'' +
                ", shopId='" + shopId + '\'' +
                ", createTime='" + createTime + '\'' +
                ", createTimeTitle='" + createTimeTitle + '\'' +
                ", releaseTypeTitle='" + releaseTypeTitle + '\'' +
                ", loginUserId='" + loginUserId + '\'' +
                ", comment=" + comment +
                ", nickName='" + nickName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", gender=" + gender +
                ", genderTitle='" + genderTitle + '\'' +
                ", relationalGrade='" + relationalGrade + '\'' +
                ", isHaveFavorite=" + isHaveFavorite +
                ", isHavePraise=" + isHavePraise +
                ", content='" + content + '\'' +
                ", pic=" + pic +
                ", praiseNum='" + praiseNum + '\'' +
                ", pageId='" + pageId + '\'' +
                ", shopName='" + shopName + '\'' +
                ", shopPic=" + shopPic +
                ", shopPraise='" + shopPraise + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", commentNum='" + commentNum + '\'' +
                ", favoriteNum='" + favoriteNum + '\'' +
                ", praise_avatar_user=" + praise_avatar_user +
                ", share_user_nickname=" + share_user_nickname +
                ", title='" + title + '\'' +
                ", associated_price='" + associated_price + '\'' +
                ", primary_business='" + primary_business + '\'' +
                ", original_user_data=" + original_user_data +
                ", release_favorite_id=" + release_favorite_id +
                ", showAll1=" + showAll1 +
                ", showAll2=" + showAll2 +
                '}';
    }
}
