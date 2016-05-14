package com.xiaoma.beiji.controls.acinterface;

import com.xiaoma.beiji.entity.FriendDynamicEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;

/**
 * Created by zhangqibo on 2016/5/11.
 */
public interface IActionInterFace {
    public void dynamicDoPraise(FriendDynamicEntity entity,AbsHttpResultHandler handler);
    public void dynamicDoFavorite(FriendDynamicEntity entity,AbsHttpResultHandler handler);
    public void dynamicDoComment(FriendDynamicEntity entity, ICommentInterface handler);
    public void dynamicMore(FriendDynamicEntity entity, IDomoreInterface handler);
//    public void dynamicDoComment();
//    public void dynamicMore(FriendDynamicEntity entity);
}
