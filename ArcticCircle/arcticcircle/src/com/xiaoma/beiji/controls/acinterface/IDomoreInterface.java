package com.xiaoma.beiji.controls.acinterface;

import com.xiaoma.beiji.entity.FriendDynamicEntity;

/**
 * Created by zhangqibo on 2016/5/14.
 */
public interface IDomoreInterface {
    public static final String TYPE_DELETE = "删除";
    public static final String TYPE_SHOUCANG = "收藏";
    public static final String TYPE_JUBAO = "举报";
    public static final String TYPE_PINGBI = "屏蔽";

    public void success(FriendDynamicEntity entity,String type);
}
