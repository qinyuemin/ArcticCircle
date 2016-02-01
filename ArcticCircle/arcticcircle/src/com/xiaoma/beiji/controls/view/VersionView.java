/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.controls.view
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.controls.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.common.android.lib.common.LibConstant;
import com.xiaoma.beiji.BuildConfig;

/**
 *
 * 类名称： VersionView
 * 类描述： 显示 版本信息
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月29 11:41
 * 修改备注：
 * @version 1.0.0
 *
 */
public class VersionView {

    public static View showVersionSign(Activity activity){
        boolean debug = BuildConfig.DEBUG;
        if(LibConstant.BASE_URL_TYPE != LibConstant.UrlType.RELEASE || debug){
            try{
                WindowManager mWindowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
                WindowManager.LayoutParams mWindowLayoutParams = new WindowManager.LayoutParams();
                mWindowLayoutParams.format = PixelFormat.TRANSLUCENT; //图片之外的其他地方透明
                mWindowLayoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
                mWindowLayoutParams.x = 0;
                mWindowLayoutParams.y = 0;
                mWindowLayoutParams.alpha = 0.5f; // 透明度
                mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
                mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

                TextView textView = new TextView(activity);
                textView.setTextColor(Color.RED);
                textView.setIncludeFontPadding(false);
                textView.setTextSize(10);
                textView.setText(LibConstant.BASE_URL_TYPE.getValue());
                if(debug){
                    textView.append("[DEBUG]");
                }
                mWindowManager.addView(textView, mWindowLayoutParams);
                setBold(textView);
                return textView;
            }catch(Exception e){}
        }
        return null;
    }

    public static void removeVersionSign(Activity activity, View view){
        boolean debug = BuildConfig.DEBUG;
        if (view != null && (LibConstant.BASE_URL_TYPE != LibConstant.UrlType.RELEASE || debug)) {
            try{
                WindowManager mWindowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
                mWindowManager.removeView(view);
            }catch(Exception e){}
        }
    }

    public static void setBold(TextView textView) {
        TextPaint tp = textView.getPaint();
        tp.setFakeBoldText(true);
    }
}
