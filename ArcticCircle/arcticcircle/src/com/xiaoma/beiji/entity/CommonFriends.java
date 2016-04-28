package com.xiaoma.beiji.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by zhangqibo on 2016/4/28.
 */
public class CommonFriends {

    @JSONField(name = "total")
    private String total;
    @JSONField (name = "list")
    private List list;
}
