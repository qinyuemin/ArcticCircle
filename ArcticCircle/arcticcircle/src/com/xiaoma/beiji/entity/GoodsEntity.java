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
 * 类名称： GoodsEntity
 * 类描述： 商品
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月15 17:10
 * 修改备注：
 * @version 1.0.0
 *
 */
public class GoodsEntity  {
    @JSONField(name = "goods_id")
    private String goodsId;
    @JSONField(name = "goods_name")
    private String goodsName;
    @JSONField(name = "price")
    private String price;
    @JSONField(name = "description")
    private String description;
    @JSONField(name = "pic")
    private String pic;
    @JSONField(name = "shop_id")
    private String shopId;
    @JSONField(name = "create_time")
    private String createTime;
    @JSONField(name = "pic_list")
    private List<PicEntity> picEntities;


    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
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

    public List<PicEntity> getPicEntities() {
        return picEntities;
    }

    public void setPicEntities(List<PicEntity> picEntities) {
        this.picEntities = picEntities;
    }
}
