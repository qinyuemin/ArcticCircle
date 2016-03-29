/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.service
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.manager.chatting;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.entity.XmppUserEntity;
import com.xiaoma.beiji.manager.ContactManager;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;

/**
 *
 * 类名称： IMChatService
 * 类描述： 
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月17 21:48
 * 修改备注：
 * @version 1.0.0
 *
 */
public class IMChatService extends Service {
    private static final String TAG = IMChatService.class.getSimpleName();
    public static final String INTENT_IM_CHAT_SERVICE = "com.xiaoma.beiji.imchatservice"; // 启动 intent， 与 manifest.xml 里面对应

    private IMXmppManager imXmppManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        imXmppManager = IMXmppManager.getInstance();
        new Thread(new Runnable() {
            @Override
            public void run() {
                imXmppManager.xmppConnect();
            }
        }).start();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        initXmppLogin();


        HttpClientUtil.vCardChange(ContactManager.getInstance(this).getContactEntities(), new AbsHttpResultHandler() {
            @Override
            public void onSuccess(int resultCode, String desc, Object data) {
                Log.i(TAG,"vCardChange success");
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                Log.i(TAG,"vCardChange failure :"+desc);
            }
        });
    }

    private void initXmppLogin() {
        HttpClientUtil.Chatting.XmppGetUser(new AbsHttpResultHandler<XmppUserEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, XmppUserEntity data) {
                Global.setXmppUserEntity(data);
                try {
                    String account = splitAndSaveServer(data.getUserId());
                    String password = data.getPwd();
                    boolean isLogin = IMXmppManager.getInstance().login(account,password);
                } catch (XmppException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int resultCode, String desc) {

            }
        });
    }

    private String splitAndSaveServer(String account) {
        if (!account.contains("@"))
            return account;
        String[] res = account.split("@");
        String userName = res[0];
        return userName;
    }

    @Override
    public void onDestroy() {
        imXmppManager.xmppDisconnect();
        super.onDestroy();
    }
}
