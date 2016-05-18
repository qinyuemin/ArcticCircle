/**
 * 项目名： eyah
 * 包名： com.sttri.eyah.network
 * 版本信息： 1.0.0
 * Copyright (c) -版权所有
 */
package com.xiaoma.beiji.network;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.makeapp.android.util.DialogUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.BaseApplication;
import com.xiaoma.beiji.common.AppConstants;
import com.xiaoma.beiji.controls.dialog.LoginTimeoutDialog;
import com.xiaoma.beiji.controls.dialog.SimpleDialog;
import org.apache.http.Header;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 类名称： JsonHttpResponseHandler
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年06月10 11:41
 * 修改备注：
 *
 * @version 1.0.0
 */
public class HttpResultConverter<T> extends BaseJsonHttpResponseHandler {
    public static final int RESULT_CODE_SUCCESS = 0;
    public static final int RESULT_CODE_FAILED = 1;

    private static final String RESPONSE_SUCCESS = "success";
    private static final String RESPONSE_CODE = "errcode";
    private static final String RESPONSE_MESSAGE = "message";
    private static final String RESPONSE_DATA = "data";

    protected Class<T> clazz;
    private AbsHttpResultHandler handler;
    private String logTag;


    @Deprecated
    public HttpResultConverter(Class<T> cls, AbsHttpResultHandler<T> handler, String logTag) {
        this.handler = handler;
        this.logTag = logTag;
        clazz = cls;
    }

    public HttpResultConverter(AbsHttpResultHandler<T> handler, String logTag) {
        this.handler = handler;
        this.logTag = logTag;

        Class clazz = getClass();

        getMyClass(clazz);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
        try {
            HttpClientUtil.logger(HttpClientUtil.TAG + " " + logTag + " onSuccess result:" + rawJsonResponse);
            if (handler == null) {
                return;
            }

            JSONObject jsonObject = JSON.parseObject(rawJsonResponse);

            boolean successStatus = jsonObject.getBoolean(RESPONSE_SUCCESS);
            int errorCode = jsonObject.getIntValue(RESPONSE_CODE);
            String message = jsonObject.getString(RESPONSE_MESSAGE);

            if (errorCode == 20027 || errorCode == 200026) {
                // 登录超时
                BaseApplication.getInstance().sendBroadcast(new Intent(AppConstants.IntentActionConstant.ACTION_LOGIN_TIMEOUT));
            }
            try {
                if (successStatus) {
                    T obj = null;
                    List<T> objList = null;
                    if (jsonObject.containsKey(RESPONSE_DATA)) {
                        Object object = jsonObject.get(RESPONSE_DATA);
                        if(object instanceof  JSONArray){
                            JSONArray array = (JSONArray)object;
                            objList = JSON.parseArray(array.toJSONString(), clazz);
                            handler.onSuccess(errorCode, message, objList);
                        }else if(object instanceof  JSONObject){
                            JSONObject json = (JSONObject)object;
                            obj = JSON.toJavaObject(json, clazz);
                            handler.onSuccess(errorCode, message, obj);
                        }else{
//                        obj = clazz.newInstance();
                            handler.onSuccess(errorCode, message, object);
                        }
                    }
                } else {
                    handler.onFailure(errorCode, message);
                }
            }catch (Exception e){
                handler.onFailure(errorCode, message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
        HttpClientUtil.logger(HttpClientUtil.TAG + " " + logTag + " onFailure result:" + rawJsonData);
        if (handler != null) {
            handler.onFailure(statusCode, rawJsonData);
        }
    }

    @Override
    protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
        return null;
    }

    private void getMyClass(Class clazz) {
        while (clazz != Object.class) {
            Type t = clazz.getGenericSuperclass();
            if (t instanceof ParameterizedType) {
                Type[] args = ((ParameterizedType) t).getActualTypeArguments();
                if (args[0] instanceof Class) {
                    this.clazz = (Class<T>) args[0];
                    break;
                }
            }
            clazz = clazz.getSuperclass();
        }
    }
}
