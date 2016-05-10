package com.xiaoma.beiji.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangqibo on 2016/5/11.
 */
public class SqureEntity implements Serializable{

    @JSONField(name = "dynamic")
    List<FriendDynamicEntity> dynamicEntities;

    @JSONField(name = "talent")
    List<UserInfoEntity> userInfoEntities;

    public List<FriendDynamicEntity> getDynamicEntities() {
        return dynamicEntities;
    }

    public void setDynamicEntities(List<FriendDynamicEntity> dynamicEntities) {
        this.dynamicEntities = dynamicEntities;
    }

    public List<UserInfoEntity> getUserInfoEntities() {
        return userInfoEntities;
    }

    public void setUserInfoEntities(List<UserInfoEntity> userInfoEntities) {
        this.userInfoEntities = userInfoEntities;
    }
}
