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
 * 类名称： PicEntity
 * 类描述： 
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月02 17:06
 * 修改备注：
 * @version 1.0.0
 *
 *"pic_id": "80",
"dynamic_id": "70",
"pic_url": "http:\/\/testbjqcdn.xiaomakeji.com.cn\/uploads\/dynamic\/dynamic_70_1_1449046308.jpg"

 */
public class PicEntity implements Serializable{
    @JSONField (name = "pic_id")
    private String picId;
    @JSONField (name = "dynamic_id")
    private String dynamicId;
    @JSONField (name = "pic_url")
    private String picUrl;

    public String getPicId() {
        return picId;
    }

    public void setPicId(String picId) {
        this.picId = picId;
    }

    public String getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
