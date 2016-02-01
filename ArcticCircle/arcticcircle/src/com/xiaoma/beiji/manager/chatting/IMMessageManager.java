package com.xiaoma.beiji.manager.chatting;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.makeapp.javase.util.DateUtil;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.database.SimpleDBManager;
import com.xiaoma.beiji.entity.IMSystemMessageEntity;
import com.xiaoma.beiji.entity.IMXMPPMessageEntity;
import com.xiaoma.beiji.entity.SystemMsgEntity;
import org.jivesoftware.smack.packet.Message;

/**
 * 类名称： IMMessageManager
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月18 0:48
 * 修改备注：
 *
 * @version 1.0.0
 */
public class IMMessageManager {
    private static final String TAG = IMMessageManager.class.getSimpleName();
    private static IMMessageManager ourInstance = new IMMessageManager();

    public static IMMessageManager getInstance() {
        if(ourInstance == null){
            ourInstance = new IMMessageManager();
        }
        return ourInstance;
    }

    private IMMessageManager() {
    }

    void logger(String log){
        Log.i(TAG,log);
    }

    // 处理收到的消息内容
    public void receiveMessage(Message message){
        String msgBody = message.getBody();
        try {
            JSONObject json = JSON.parseObject(msgBody);
            if(json.containsKey("type")){
                String type = json.getString("type");
                IMSystemMessageEntity sysMsg;
                switch (type){
                    case IMConfig.MessageType.ChatTypeMessage:
                        IMXMPPMessageEntity msg = JSON.parseObject(msgBody, IMXMPPMessageEntity.class);
                        msg.setBody(msgBody);
                        msg.setChatUserId(msg.getFromUserId());
                        msg.setHasRead(false);
                        msg.setUserId(Global.getUserId());
                        msg.setTimestamp(DateUtil.getStringDateTime());

                        SimpleDBManager.IMXMPPMessage.insertServerMsg(msg);

                        // 发出收到消息通知
                        IMBroadcastManager.sendNewMessage(msg);
                        break;
                    default:
//                    case IMConfig.MessageType.ChatTypeDynamicPraise:
//                    case IMConfig.MessageType.ChatTypeDynamicCollection:
//                    case IMConfig.MessageType.ChatTypeHelp:
//                    case IMConfig.MessageType.ChatTypeDynamicComment:
//                    case IMConfig.MessageType.ChatTypeDynamicReply:
//                    case IMConfig.MessageType.ChatTypeHelpComment:
//                    case IMConfig.MessageType.ChatTypeHelpReply:
//                    case IMConfig.MessageType.ChatTypeRecommendResult:
//                    case IMConfig.MessageType.ChatTypeRecommend:
                        sysMsg = new IMSystemMessageEntity();
                        sysMsg.setBody(msgBody);
                        sysMsg.setHasRead(false);
                        sysMsg.setType(type);
                        sysMsg.setUserId(Global.getUserId());
                        sysMsg.setTimestamp(DateUtil.getStringDateTime());
                        SimpleDBManager.IMSystemMessage.insertMsg(sysMsg);

                        // 发出收到消息通知
//                        IMBroadcastManager.sendNewMessage(msg);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
