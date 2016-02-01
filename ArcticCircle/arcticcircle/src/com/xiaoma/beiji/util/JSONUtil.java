package com.xiaoma.beiji.util;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;


public class JSONUtil {
	
	private static final String TAG = "JSONUtil";
	
	public static int JSON_VALUE_INT_NULL = Integer.MIN_VALUE;
	public static String JSON_VALUE_OBJECT_NULL = null;
	public static String JSON_VALUE_STRING_NULL = null;
	
	public static final Object get(JSONObject json, String key) {
		try {
			if (json.has(key)) {
				return json.get(key);
			}
		} catch (Exception e) {}
		return JSON_VALUE_OBJECT_NULL;
	}
	
	public static final Object get(Object json, String key) {
		json = getJSONObject(json);
		if (json instanceof JSONObject) {
			return get((JSONObject)json, key);
		}
		return JSON_VALUE_OBJECT_NULL;
	}
	
	public static final int getInt(JSONObject json, String key) {
		try {
			if (json.has(key)) {
				return json.getInt(key);
			}
		} catch (Exception e) {}
		return JSON_VALUE_INT_NULL;
	}
	
	public static final long getLong(JSONObject json,String key){
		try {
			if(json.has(key)){
				return json.getLong(key);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return JSON_VALUE_INT_NULL;
	}
	
	public static final int getInt(Object json, String key) {
		json = getJSONObject(json);
		if (json instanceof JSONObject) {
			return getInt((JSONObject)json, key);
		}
		return JSON_VALUE_INT_NULL;
	}
	
	public static final String getString(JSONObject json, String key) {
		try {
			if (json.has(key)) {
				return json.getString(key);
			}
		} catch (Exception e) {}
		return JSON_VALUE_STRING_NULL;
	}
	
	public static final String getString(Object json, String key) {
		json = getJSONObject(json);
		if (json instanceof JSONObject) {
			return getString((JSONObject)json, key);
		}
		return JSON_VALUE_STRING_NULL;
	}
	
	public static JSONObject getJSONObject(String jsonString){
		try {
			return new JSONObject(jsonString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static JSONObject getJSONObject(Object obj){
		if(obj instanceof JSONObject){
			return (JSONObject) obj;
		}else if(obj instanceof String){
			return getJSONObject(String.valueOf(obj));
		}
		return null;
	}
	
    /**
     * 将现有的参数构建为 json 格式的参数
     * 部分接口需要 以 application/json 的方式
     * @param params
     * @return
     */
    public static final Map<String,Object> getJSONRequestParam(Map<String,Object> params ){
        List<Map<String,Object>> paramsList = new ArrayList<Map<String,Object>>();
        Map<String,Object> requestParams = new HashMap<String, Object>();
        paramsList.add(params);
        try {
            JSONArray jsonContent = JSONUtil.getJSONArrayByMap(paramsList);
            requestParams.put("jsonContent", jsonContent);
            return requestParams;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    /**
     * 对构造好的 参数进行 jsonContent包装
     * @param data
     * @return
     */
    public static final String getJSONRequestParam(String data){
        Map<String,String> paramsList = new HashMap<String, String>();
        paramsList.put("jsonContent",data);
        return com.alibaba.fastjson.JSONObject.toJSONString(paramsList);
    }

	public static final JSONArray getJSONArrayByMap(List<Map<String,Object>> paramList) throws JSONException {
		JSONArray jsonArray = new JSONArray();
		if(null != paramList && paramList.size() > 0){
			int nSize = paramList.size();
			for(int i = 0;i < nSize ; i++){
				JSONObject j = new JSONObject();
				Map<String,Object> params = paramList.get(i);
				Set<String> keys = params.keySet();
				for(String key : keys){
					Object obj = params.get(key);
					j.put(key, obj);
				}
				jsonArray.put(j);
			}
		}
		return jsonArray;
	}
	
	public static final JSONObject getJSONObjectByMap(Map<String,Object> params) throws JSONException {
		return new JSONObject(getJSONStringByMap(params));
	}
	
	public static final String getJSONStringByMap(Map<String,Object> params) throws JSONException {
		StringBuilder sb = new StringBuilder("{");
		if(null != params){
			Set<String> keys = params.keySet();
			for(String key : keys){
				Object obj = params.get(key);
				//根据不同的类型传入不同的值
				if(obj instanceof Integer || obj instanceof Float || obj instanceof Double){
					sb.append("\"" + key + "\":" + params.get(key) + ",");
				}else if(obj instanceof Boolean){
					sb.append("\"" + key + "\":" + params.get(key).toString() + ",");
				}else {
					if(null != params.get(key)){
						sb.append("\"" + key + "\":\"" + params.get(key).toString() + "\",");
					}else{
						sb.append("\"" + key + "\":" + "null" + ",");
					}
				}
			}
			//去掉多余的逗号
			if (sb.toString().endsWith(",")) {
				sb.deleteCharAt(sb.length() - 1);
			}
			
		}
		sb.append("}");
		Log.i(TAG, "JSONObject content = " + sb.toString());
		return sb.toString();
	}
}
