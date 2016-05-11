package com.xiaoma.beiji.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangqibo on 2016/4/28.
 */
public class CommonFriends implements Serializable{

    @JSONField(name = "total")
    private String total;
    @JSONField (name = "list")
    private List<UserInfoEntity> list;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<UserInfoEntity> getList() {
        return list;
    }

    public void setList(List<UserInfoEntity> list) {
        this.list = list;
    }
}
