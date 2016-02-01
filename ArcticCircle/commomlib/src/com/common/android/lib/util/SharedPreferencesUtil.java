package com.common.android.lib.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SharedPreferencesUtil {
    /**
     * 获取应用环境参数
     * 
     * @param context
     * @param name
     *            参数名
     * @return
     */
    public static String getSetting(Context context, String prefName,String name) {
        SharedPreferences setting = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        return setting.getString(name, "");
    }

    public static int getSettingInt(Context context, String prefName,String name) {
        SharedPreferences setting = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        return setting.getInt(name, 0);
    }

    public static int getSettingInt(Context context, String prefName,String name, int defaultValue) {
        SharedPreferences setting = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        return setting.getInt(name, defaultValue);
    }

    public static float getSettingFloat(Context context, String prefName,String name) {
        SharedPreferences setting = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        return setting.getFloat(name, 0.0f);
    }

    public static long getSettingLong(Context context, String prefName,String name) {
        SharedPreferences setting = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        return setting.getLong(name, 0);
    }

    public static boolean getSettingBoolean(Context context, String prefName,String name) {
        return getSettingBoolean(context,prefName, name, false);
    }

    public static boolean getSettingBoolean(Context context, String prefName,String name, boolean defaultVal) {
        SharedPreferences setting = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        return setting.getBoolean(name, defaultVal);
    }

    /**
     * 设置应用程序环境参数
     * 
     * @param context
     * @param name
     *            参数名
     * @param value
     *            值
     */
    public static void setSetting(Context context, String prefName,String name, String value) {
        SharedPreferences setting = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static void setSetting(Context context, String prefName,String name, int value) {
        SharedPreferences setting = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.putInt(name, value);
        editor.commit();
    }

    public static void setSetting(Context context, String prefName,String name, float value) {
        SharedPreferences setting = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.putFloat(name, value);
        editor.commit();
    }

    public static void setSetting(Context context, String prefName,String name, long value) {
        SharedPreferences setting = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.putLong(name, value);
        editor.commit();
    }

    public static void setSetting(Context context, String prefName,String name, boolean value) {
        SharedPreferences setting = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.putBoolean(name, value);
        editor.commit();
    }

    public static void setBind(Context context, String prefName,boolean flag) {
        String flagStr = "not";
        if (flag) {
            flagStr = "ok";
        }
        SharedPreferences sp = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("bind_flag", flagStr);
        editor.commit();
    }

    // 用share preference来实现是否绑定的开关。在ionBind且成功时设置true，unBind且成功时设置false
    public static boolean hasBind(Context context,String prefName) {
        SharedPreferences sp = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        String flag = sp.getString("bind_flag", "");
        if ("ok".equalsIgnoreCase(flag)) {
            return true;
        }
        return false;
    }
}
