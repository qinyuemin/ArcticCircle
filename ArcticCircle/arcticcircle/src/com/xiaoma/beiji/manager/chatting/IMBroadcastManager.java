package com.xiaoma.beiji.manager.chatting;

import android.content.Context;
import android.content.Intent;
import com.xiaoma.beiji.base.BaseApplication;
import com.xiaoma.beiji.entity.IMXMPPMessageEntity;

/**
 * 类名称： IMBroadcastManager
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月18 22:59
 * 修改备注：
 *
 * @version 1.0.0
 */
public class IMBroadcastManager {
    public static void sendNewMessage(IMXMPPMessageEntity msg){
        Intent intent = new Intent(IMConfig.Action.ACTION_NEW_MSG);
        intent.putExtra("msg",msg);
        BaseApplication.getInstance().getApplicationContext().sendBroadcast(intent);
    }
}
