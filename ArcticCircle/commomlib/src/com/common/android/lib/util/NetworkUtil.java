/**
 *
 *项目名：LuluYouLib
 *包名：com.luluyou.android.lib.utils
 *文件名：NetworkUtils.java
 *版本信息：1.0.0
 *创建日期：2013年12月12日-下午3:14:27
 *创建人：jerry.deng
 *Copyright (c) 2013上海路路由信息科技有限公司-版权所有
 * 
 */
package com.common.android.lib.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 *
 * 类名称：NetworkUtils
 * 类描述：
 * 创建人： Jerry.deng
 * 修改人： Jerry.deng
 * 修改时间： 2013年12月12日 下午3:14:27
 * 修改备注：
 * @version 1.0.0
 *
 */
public class NetworkUtil {
    

    
    public static final String WIFI = "wifi";
    public static final String WIMAX = "wimax";
    // mobile
    public static final String MOBILE = "mobile";
    // 2G network types
    public static final String GSM = "gsm";
    public static final String GPRS = "gprs";
    public static final String EDGE = "edge";
    // 3G network types
    public static final String CDMA = "cdma";
    public static final String UMTS = "umts";
    public static final String HSPA = "hspa";
    public static final String HSUPA = "hsupa";
    public static final String HSDPA = "hsdpa";
    public static final String ONEXRTT = "1xrtt";
    public static final String EHRPD = "ehrpd";
    // 4G network types
    public static final String LTE = "lte";
    public static final String UMB = "umb";
    public static final String HSPA_PLUS = "hspa+";
    // return type
    public static final String TYPE_UNKNOWN = "unknown";
    public static final String TYPE_ETHERNET = "ethernet";
    public static final String TYPE_WIFI = "wifi";
    public static final String TYPE_2G = "2g";
    public static final String TYPE_3G = "3g";
    public static final String TYPE_4G = "4g";
    public static final String TYPE_NONE = "none";
    
    public static boolean isWifi(Context context){
        String typeInfo = getConnectionInfo(context);
        if(TYPE_WIFI.equals(typeInfo)){
            return true;
        }
        return false;
    }
    
    public static boolean is3G(Context context){
        String typeInfo = getConnectionInfo(context);
        if(TYPE_3G.equals(typeInfo)){
            return true;
        }
        return false;
    }
    
    public static String getConnectionInfo(Context context){
        ConnectivityManager sockMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = sockMan.getActiveNetworkInfo();
        return getConnectionInfo(info);
    }
    
    /** 
     * Get the latest network connection information
     * 
     * @param info the current active network info
     * @return a JSONObject that represents the network info
     */
    private static String getConnectionInfo(NetworkInfo info) {
        String type = TYPE_NONE;
        if (info != null) {
            // If we are not connected to any network set type to none
            if (!info.isConnected()) {
                type = TYPE_NONE;
            }
            else {
                type = getType(info);
            }
        }
        return type;
    }
    
    /**
     * Determine the type of connection
     * 
     * @param info the network info so we can determine connection type.
     * @return the type of mobile network we are on
     */
    private static String getType(NetworkInfo info) {
        if (info != null) {
            String type = info.getTypeName();

            if (type.toLowerCase().equals(WIFI)) {
                return TYPE_WIFI;
            }
            else if (type.toLowerCase().equals(MOBILE)) {
                type = info.getSubtypeName();
                if (type.toLowerCase().equals(GSM) || 
                        type.toLowerCase().equals(GPRS) ||
                        type.toLowerCase().equals(EDGE)) {
                    return TYPE_2G;
                }
                else if (type.toLowerCase().startsWith(CDMA) || 
                        type.toLowerCase().equals(UMTS)  ||
                        type.toLowerCase().equals(ONEXRTT) ||
                        type.toLowerCase().equals(EHRPD) ||
                        type.toLowerCase().equals(HSUPA) ||
                        type.toLowerCase().equals(HSDPA) ||
                        type.toLowerCase().equals(HSPA)) {
                    return TYPE_3G;
                }
                else if (type.toLowerCase().equals(LTE) || 
                        type.toLowerCase().equals(UMB) ||
                        type.toLowerCase().equals(HSPA_PLUS)) {
                    return TYPE_4G;
                }
            }
        } 
        else {
            return TYPE_NONE;
        }
        return TYPE_UNKNOWN;
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED
                            || info[i].getState() == NetworkInfo.State.CONNECTING) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 是否为 wifi 成功联网
     * @param context
     * @return
     */
    public static boolean isWifiAvailable(Context context) {
        return  isNetworkAvailable(context) && isWifi(context);
    }

    /**
     * check if network is connected
     *
     * @return
     */
    public static boolean isNetworkConnected(Context ctx) {
        ConnectivityManager connMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
