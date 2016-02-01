/**
 * 项目名： eyah
 * 包名： com.sttri.eyah.util
 * 版本信息： 1.0.0
 * Copyright (c) -版权所有
 */
package com.xiaoma.beiji.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import com.makeapp.javase.lang.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 类名称： CommUtil
 * 类描述： 通用工具类
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年06月09 14:25
 * 修改备注：
 *
 * @version 1.0.0
 */
public class CommUtil {
    private static int SCREEN_WIDTH = 0;
    private static int SCREEN_HEIGHT = 0;

    public static int getScreenWidth(Context context) {
        if (SCREEN_WIDTH == 0) {
            DisplayMetrics dm = new DisplayMetrics();
            dm = context.getResources().getDisplayMetrics();
            SCREEN_WIDTH = dm.widthPixels;
        }
        return SCREEN_WIDTH;
    }

    public static int getScreenHeight(Context context) {
        if (SCREEN_HEIGHT == 0) {
            DisplayMetrics dm = new DisplayMetrics();
            dm = context.getResources().getDisplayMetrics();
            SCREEN_HEIGHT = dm.heightPixels;
        }
        return SCREEN_HEIGHT;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static String getUUID(){
        return UUID.randomUUID().toString();
    }

    public static String getVersionName(Context context) {
        String versionName = "1.0.0.0";
        try {
            PackageInfo mPackageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            versionName = mPackageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static int getVersionCode(Context context) {
        int versionCode = 1;
        try {
            PackageInfo mPackageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            versionCode = mPackageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 判断手机号码是否正确
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        String mobileNumberReg = "^1[0-9]{10}$";
        if (phoneNumber == null || phoneNumber.length() == 0)
            return false;
        return phoneNumber.matches(mobileNumberReg);
    }

    public static Intent setAvatarCropIntent(Uri uri, Uri outUri) {
        return setImageCropIntent(uri, outUri, 256, 256, 1, 1);
    }

    public static Intent setImageCropIntent(Uri uri, Uri outUri, int outX, int outY, int aspectX, int aspectY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scaleUpIfNeeded", true);// 如果截图太小，则自动将选择的图片块放大填充整个高宽区域，避免出现黑色背景填充
        intent.putExtra("noFaceDetection", true);// 去掉人脸识别的功能
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", outX);
        intent.putExtra("outputY", outY);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);// 不返回data，intent会以路径的形式将截图文件带过来
        if (outUri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        }
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        return intent;
    }

    public static void textViewSetImg(Context context, TextView tv, int resId) {
        Drawable drawable = context.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv.setCompoundDrawables(drawable, null, null, null);
    }

    /**
     * 收起键盘
     *
     * @param activity
     */
    public static void hideKeyBoard(Activity activity) {
        try {
            ((InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
        }
    }

    /**
     * 将 手机号码 中间4位 设置为 *
     *
     * @param phone
     * @return
     */
    public static String getPhoneEncrypt(String phone) {
        String result = "";
        if (StringUtil.isValid(phone) && phone.length() > 7) {
            String s = phone.substring(3, 7);
            result = phone.replace(s, "****");
        }
        return result;
    }

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String formatChatDate(long ms) {
        Date today = new Date();
        Date date = new Date(ms);
        String str_today = df.format(today);
        String str_date = df.format(date);

        String year_today = str_today.substring(0, 4);
        String year_date = str_date.substring(0, 4);

        String month_today = str_today.substring(5, 10);
        String month_date = str_date.substring(5, 10);

        // String day_today = str_today.substring(8,10);
        // String day_date = str_date.substring(8,10);

        StringBuilder sb = new StringBuilder("");
        if (!year_date.equals(year_today)) {
            sb.append(year_date + "-");
        }
        if (!month_date.equals(month_today)) {
            sb.append(month_date + " ");
        }
        // if(!day_date.equals(day_today)){
        // sb.append(day_date+" ");
        // }
        sb.append(str_date.substring(11, 16));
        return sb.toString();
    }

    public static boolean isHttpUrl(String url){
        if(url!=null && url.trim().toLowerCase().startsWith("http")){
            return true;
        }
        return false;
    }

    public static String getFriendTag(int gender,int v) {
        String txt = "";
        if (v == 1) {
            txt = "好友";
        } else if (v == 2) {
            txt = "好友的好友";
        } else if (v == 3) {
            txt = "三度好友";
        }
        return getGenderTag(gender)+txt;
    }

    public static String getGenderTag(int gender){
        String gendertxt = "♂";
        if (gender == 1) {
            gendertxt = "♂";
        } else if (gender == 2) {
            gendertxt = "♀";
        }
        return gendertxt;
    }

    //播打电话
    public static void call(Context context, String number) {
        if (number != null) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("tel://" + number));
            context.startActivity(intent);
        }
    }

    public static void openWebView(Context context,String url){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(intent);
    }

    public static void openApp(Activity activity, String packageName){
        if(hasApplication(activity,packageName)){
            Intent intent = activity.getPackageManager().getLaunchIntentForPackage(packageName);
            activity.startActivity(intent);
        }
    }

    /**
     * 判断当前app 对象 对应app 是否已安装 并修改状态
     * @param context
     * @param packageName
     * @return
     */
    public static boolean hasApplication(Context context,String packageName) {
        if(TextUtils.isEmpty(packageName)){
            return false;
        }
        List<ResolveInfo> activities = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(packageName);
            if(intent != null){
                //查询是否有该Intent的Activity
                activities = packageManager.queryIntentActivities(intent, 0);
                if(activities != null){
                    //activities里面不为空就有，否则就没有
                    return activities.size() > 0 ? true : false;
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return false;
    }

}
