/**
 * 项目名： eyah
 * 包名： com.sttri.eyah.adapter
 * 版本信息： 1.0.0
 * Copyright (c) -版权所有
 */
package com.xiaoma.beiji.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 *
 * 类名称： CommonPagerAdapter
 * 类描述： 
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年06月18 16:53
 * 修改备注：
 * @version 1.0.0
 *
 */
public class CommonPagerAdapter<T> extends PagerAdapter {

    private List<T> mDataSet ;
    private LayoutInflater mInflater;
    private ViewCreator<T> mCreator;
    private boolean mIsForceUpdateView = false;

    public CommonPagerAdapter(LayoutInflater inf,ViewCreator<T> creator){
        mCreator = creator;
        mInflater = inf;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
        int index = Math.max(0, Math.min(position, mDataSet.size() - 1));
        mCreator.releaseView(view, mDataSet.get(index));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mCreator.createView(mInflater, position, mDataSet.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }

    public T getDataItemAt(int position){
        return mDataSet == null ? null : mDataSet.get(position);
    }

    public void toggleForceUpdate(boolean isForce){
        mIsForceUpdateView = isForce;
    }

    @Override
    public int getItemPosition(Object object) {
        if(mIsForceUpdateView){
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    public void update(List<T> ds){
        mDataSet = ds;
        notifyDataSetChanged();
    }

    public void add(List<T> extraData){
        if(mDataSet == null){
            update(extraData);
            return;
        }
        mDataSet.addAll(extraData);
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public static interface ViewCreator<E> {
        View createView(LayoutInflater inflater, int position, E data);

        void updateView(View view, int position, E data);

        void releaseView(View view, E data);
    }
}
