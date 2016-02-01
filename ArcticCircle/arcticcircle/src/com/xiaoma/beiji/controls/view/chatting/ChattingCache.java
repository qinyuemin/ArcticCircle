/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.controls.view.chatting
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.controls.view.chatting;

import android.content.Context;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.controls.view.chatting.sensitiveword.SensitivewordFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 类名称： ChattingCache
 * 类描述： 
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月04 1:22
 * 修改备注：
 * @version 1.0.0
 *
 */
public class ChattingCache {
    private static ChattingCache _instance = new ChattingCache();
    public static List<String> mEmoticons = new ArrayList<String>();
    public static Map<String, Integer> mEmoticonsId = new HashMap<String, Integer>();
    private SensitivewordFilter sensitivewordFilter;

    public static ChattingCache getApplicationDataCache(){
        return _instance;
    }

    public boolean initDataCache(Context context){
        //初始化表情数据
        if(mEmoticons == null || mEmoticons.size() == 0){
            initEmotions(context);
        }
        //初始化敏感字数据
        if(null == sensitivewordFilter){
//            initSensitiveWord(context);
        }
        return true;
    }

    private void initEmotions(Context context) {
        String[] emotionMessages  = context.getResources().getStringArray(R.array.emotionmessages);
        for (int i = 0; i < emotionMessages.length; i++) {
            String emoticonsName = emotionMessages[i];
            int emoticonsId = context.getResources().getIdentifier("smiley_" + i, "drawable", context.getPackageName());
            mEmoticons.add(emoticonsName);
            mEmoticonsId.put(emoticonsName, emoticonsId);
        }
    }

    public void clearDataCache(){
        if(null != mEmoticons){
            mEmoticons.clear();
            mEmoticonsId = null;
        }

        if(null != sensitivewordFilter && sensitivewordFilter.getSensitiveWordMap() != null){
            sensitivewordFilter.getSensitiveWordMap().clear();
        }

        if(null != mEmoticonsId){
            mEmoticonsId.clear();
            mEmoticonsId = null;
        }
    }
}
