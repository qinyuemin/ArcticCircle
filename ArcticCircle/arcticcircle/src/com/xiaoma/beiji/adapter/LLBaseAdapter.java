/**
 * 项目名： eyah
 * 包名： com.sttri.eyah.adapter
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 类名称： LLBaseAdapter
 * 类描述： 
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年07月03 15:13
 * 修改备注：
 * @version 1.0.0
 *
 */

public abstract class LLBaseAdapter<T> extends BaseAdapter {

    private List<T> mDataList;
    private Activity mActivity;
    private LayoutInflater mLayoutInflater;

    public LLBaseAdapter(Activity activity, List<T> dataList){
        if(dataList == null){
            dataList = new ArrayList<T>();
        }
        this.mActivity = activity;
        this.mDataList = dataList;
        this.mLayoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return getDataCount();
    }

    public int getDataCount() {
        return mDataList.size();
    }

    public void setDataList(List<T> dataList){
        if(dataList == null){
            dataList = new ArrayList<T>();
        }
        this.mDataList = dataList;
    }

    public void addDataList(List<T> dataList){
        if(dataList == null){
            dataList = new ArrayList<T>();
        }

        if(mDataList != null){
            this.mDataList.addAll(dataList);
        }else{
            this.mDataList = dataList;
        }
    }

    public void addDataList(int position, List<T> dataList){
        if(dataList != null && dataList.isEmpty() == false){
            if(mDataList != null){
                this.mDataList.addAll(position, dataList);
            }else{
                this.mDataList = dataList;
            }
        }
    }

    public void addData(T data){
        this.mDataList.add(data);
    }

    public void addData(int position, T data){
        this.mDataList.add(position, data);
    }

    public void setData(int position, T data){
        this.mDataList.set(position, data);
    }



    public List<T> getmDataList() {
        return mDataList;
    }

    @Override
    public T getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public Activity getActivity() {
        return mActivity;
    }

    public LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
    }

    public T remove(int position){
        return mDataList.remove(position);
    }

    public boolean remove(T data){
        return mDataList.remove(data);
    }

}