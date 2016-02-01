/**
 *
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.entity
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 *
 */
package com.xiaoma.beiji.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 类名称： ShopOnlineEntity
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月17 1:05
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ShopOnlineEntity {
    @JSONField(name = "tb_url")
    private String tbUrl;
    @JSONField(name = "wx_url")
    private String wxUrl;

    public String getTbUrl() {
        return tbUrl;
    }

    public void setTbUrl(String tbUrl) {
        this.tbUrl = tbUrl;
    }

    public String getWxUrl() {
        return wxUrl;
    }

    public void setWxUrl(String wxUrl) {
        this.wxUrl = wxUrl;
    }
}
