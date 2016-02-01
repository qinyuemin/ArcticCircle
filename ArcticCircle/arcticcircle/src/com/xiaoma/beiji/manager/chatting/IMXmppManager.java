package com.xiaoma.beiji.manager.chatting;

import android.os.Build;
import android.util.Log;
import com.xiaoma.beiji.network.UrlConstants;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.ping.PingManager;
import org.jivesoftware.smackx.ping.provider.PingProvider;

import java.io.File;

/**
 * 类名称： IMXmppManager
 * 类描述： xmpp 聊天 管理器
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月04 17:27
 * 修改备注：
 *
 * @version 1.0.0
 */
public class IMXmppManager {
    private static final String TAG = IMXmppManager.class.getSimpleName();

    private static IMXmppManager ourInstance = new IMXmppManager();

    public static IMXmppManager getInstance() {
        return ourInstance;
    }

    private static ConnectionConfiguration connConfig;
    private static XMPPConnection xmppConnection;

    public static final String XMPP_IDENTITY_TYPE = "phone";
    private static final int PACKET_TIMEOUT = 30000;

    private static final int PINT_INTERVALL = 10 * 1000 ;

    private RosterListener mRosterListener;// 联系人动态监听
    private PacketListener packetListener;// 消息动态监听
    private PacketListener mSendFailureListener;// 消息发送失败动态监听
    private PacketListener mPongListener;// ping pong服务器动态监听

    static {
        try{
            Class.forName("org.jivesoftware.smack.ReconnectionManager");
        }catch(Exception e){
            e.printStackTrace();
        }

        ProviderManager pm = ProviderManager.getInstance();
        pm.addIQProvider("ping", "urn:xmpp:ping", new PingProvider());
    }

    private IMXmppManager() {
        connConfig = new ConnectionConfiguration(UrlConstants.XMPP_SERVER, UrlConstants.XMPP_PORT);
        connConfig.setDebuggerEnabled(true);
        connConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        connConfig.setSASLAuthenticationEnabled(false);
        connConfig.setReconnectionAllowed(true);
        connConfig.setSelfSignedCertificateEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            connConfig.setTruststoreType("AndroidCAStore");
            connConfig.setTruststorePassword(null);
            connConfig.setTruststorePath(null);
        } else {
            connConfig.setTruststoreType("BKS");
            String path = System.getProperty("javax.net.ssl.trustStore");
            if (path == null) {
                path = System.getProperty("java.home") + File.separator + "etc" + File.separator + "security" + File.separator + "cacerts.bks";
            }
            connConfig.setTruststorePath(path);
        }

        SmackConfiguration.setDefaultPingInterval(PINT_INTERVALL);

        xmppConnection = new XMPPConnection(connConfig);
    }

    public void xmppConnect() {
        try {
            xmppConnection.connect();
            logger("xmpp connected");

        } catch (XMPPException e) {
            e.printStackTrace();
            logger("xmpp connect failed");
        }// 开启连接
        if (xmppConnection.isConnected()) {
            xmppConnection.addConnectionListener(new ConnectionListener() {
                //这里是正常关闭连接的事件
                public void connectionClosed() {
                    logger("xmpp connectionClosed");
                }

                //这里就是网络不正常断线激发的事件
                public void connectionClosedOnError(Exception arg0) {
                    logger("xmpp connectionClosedOnError");
                }

                //重新连接的动作正在进行的动作，里面的参数arg0是一个倒计时的数字，如果连接失败的次数增多，数字会越来越大，开始的时候是14
                public void reconnectingIn(int arg0) {
                    logger("xmpp reconnectingIn");
                }

                //重新连接失败
                public void reconnectionFailed(Exception arg0) {
                    logger("xmpp reconnectionFailed");
                }

                //当网络断线了，重新连接上服务器触发的事件
                public void reconnectionSuccessful() {
                    logger("xmpp reconnectionSuccessful");
                }
            });
        }

        ServiceDiscoveryManager sdm = ServiceDiscoveryManager.getInstanceFor(xmppConnection);
        if (sdm == null){
            sdm = new ServiceDiscoveryManager(xmppConnection);
        }
        sdm.addFeature("http://jabber.org/protocol/disco#info");
        PingManager.getInstanceFor(xmppConnection).setPingIntervall(PINT_INTERVALL);
        PingManager.getInstanceFor(xmppConnection).pingMyServer(PINT_INTERVALL);
    }

    public void xmppDisconnect() {
        xmppConnection.disconnect();
        logger("disconnect");
    }

    public boolean isAuthenticated() {// 是否与服务器连接上，供本类和外部服务调用
        if (xmppConnection != null) {
            return (xmppConnection.isConnected() && xmppConnection.isAuthenticated());
        }
        return false;
    }

    public boolean login(String userId, String password) throws XmppException {
        try {
//            SmackConfiguration.setPacketReplyTimeout(PACKET_TIMEOUT);
//            SmackConfiguration.setKeepAliveInterval(-1);
//            SmackConfiguration.setDefaultPingInterval(0);
//            registerRosterListener();// 监听联系人动态变化
//            initServiceDiscovery();// 与服务器交互消息监听,发送消息需要回执，判断是否发送成功
            xmppConnection.login(userId, password);
            logger("login(): success ");
//            setStatusFromConfig();// 更新在线状态
        } catch (Exception e) {
            logger("login(): failed " + Log.getStackTraceString(e));
        }
        registerAllListener();// 注册监听其他的事件，比如新消息
        return xmppConnection.isAuthenticated();
    }

    /**
     * 注册所有的监听
     */
    private void registerAllListener() {
        if (isAuthenticated()) {
            registerMessageListener();// 注册新消息监听
            registerPingListener();// 注册服务器回应ping消息监听
//            registerMessageSendFailureListener();// 注册消息发送失败监听
//            sendOfflineMessages();// 发送离线消息
        }
    }

    /************
     * start 新消息处理
     ********************/
    private void registerMessageListener() {
        if (packetListener != null) {
            xmppConnection.removePacketListener(packetListener);
        }

        PacketTypeFilter filter = new PacketTypeFilter(Message.class);
        packetListener = new PacketListener() {
            public void processPacket(Packet packet) {
                try {
                    if (packet instanceof Message) {// 如果是消息类型
                        Message msg = (Message) packet;
                        logger("processPacket msg:"+msg.getBody());
                        IMMessageManager.getInstance().receiveMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger("failed to process packet");
                }
            }
        };

        xmppConnection.addPacketListener(packetListener, filter);
    }


    /*****************
     * start 处理ping服务器消息
     ***********************/
    private void registerPingListener() {

    }

    void logger(String log){
        Log.d(TAG,log);
    }
}
