package com.common.android.lib.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhangqibo on 2016/5/7.
 */
public class TimeUtil {

    public static String getDisplayTime(String time,String pattern){
        String ret = " ";
        try{
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy.MM.dd");
            Date date = format.parse(time);
            Date currentDate = new Date();
            if(date.after(currentDate)){
                return " ";
            }
            long s = (currentDate.getTime() - date.getTime()) / 1000;
            long count = 0;
            if((count = s / 3600 * 24) > 30){
                ret = format2.format(date);
            }else if((count = s / 3600) > 0){
                ret =  count + "天前";
            }else if((count = s / 3600) > 0){
                ret = count + "小时前";
            }else if((count = s / 60) > 0){
                ret = count + "分钟前";
            }else{
                ret = "刚刚";
            }
        }catch (Exception e){

        }
        return ret;
    }
}
