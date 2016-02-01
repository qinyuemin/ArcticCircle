/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.entity
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 类名称： XmppUserEntity
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月04 17:23
 * 修改备注：
 *
 * @version 1.0.0
 *          <p/>
 *          {"user_id":"18616349511@121.40.189.143","user_pw":"f8ec351911f9a89673cd72741bdf5c0a"}
 *          {"user_id":"15026729453@121.40.189.143","user_pw":"bb147bf6e72072d94f79a229ebc712e3"}
 */
public class XmppUserEntity {
    @JSONField(name = "user_id")
    private String userId;
    @JSONField(name = "user_pw")
    private String pwd;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
