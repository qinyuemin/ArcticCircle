/**
 * ImageGroupAdapter.java
 * ImageChooser
 * 
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

package com.xiaoma.beiji.controls.view.imagechooser;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import com.makeapp.android.util.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;

import java.util.ArrayList;

/**
 * 某个图片组中图片列表适配器
 * 
 * @author likebamboo
 */
public class ImageListAdapter extends BaseAdapter {
    /**
     * 上下文对象
     */
    private Context mContext = null;
    private int selectMaxCount;

    /**
     * 图片列表
     */
    private ArrayList<String> mDataList = new ArrayList<String>();

    /**
     * 选中的图片列表
     */
    private ArrayList<String> mSelectedList = new ArrayList<String>();

    public ImageListAdapter(Context context, ArrayList<String> list,int selectMaxCount) {
        mDataList = list;
        mContext = context;
        this.selectMaxCount = selectMaxCount;
        mSelectedList = ImageManager.getInstance().getSelectedImages(context);
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public String getItem(int position) {
        if (position < 0 || position > mDataList.size()) {
            return null;
        }
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.lst_item_chooser_item, null);
            holder.mImageIv = (ChooserImageView)view.findViewById(R.id.list_item_iv);
            holder.mClickArea = view.findViewById(R.id.list_item_cb_click_area);
            holder.mSelectedCb = (CheckBox)view.findViewById(R.id.list_item_cb);
            holder.llRoot = view.findViewById(R.id.ll_root);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }

        final String path = getItem(position);
        // 加载图片
        holder.mImageIv.setTag(path);
        // 加载图片
        ImageLoader.getInstance().displayImage("file:///" +path,holder.mImageIv);

        holder.mSelectedCb.setChecked(false);
        // 该图片是否选中
        for (String selected : mSelectedList) {
            if (selected.equals(path)) {
                holder.mSelectedCb.setChecked(true);
            }
        }


        // 可点区域单击事件
//        holder.mClickArea.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean checked = holder.mSelectedCb.isChecked();
//                holder.mSelectedCb.setChecked(!checked);
//                if (!checked) {
//                    addImage(path);
//                } else {
//                    deleteImage(path);
//                }
//            }
//        });

        holder.llRoot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSelectedList.size() < selectMaxCount){
                    boolean checked = holder.mSelectedCb.isChecked();
                    holder.mSelectedCb.setChecked(!checked);
                    if (!checked) {
                        addImage(path);
                    } else {
                        deleteImage(path);
                    }
                }else {
                    ToastUtil.show(mContext,"最多选择" +selectMaxCount+"张图片");
                }
            }
        });

        return view;
    }

    /**
     * 将图片地址添加到已选择列表中
     * 
     * @param path
     */
    private void addImage(String path) {
        if (mSelectedList.contains(path)) {
            return;
        }
        mSelectedList.add(path);
    }

    /**
     * 将图片地址从已选择列表中删除
     * 
     * @param path
     */
    private void deleteImage(String path) {
        mSelectedList.remove(path);
    }

    /**
     * 获取已选中的图片列表
     * 
     * @return
     */
    public ArrayList<String> getSelectedImgs() {
        return mSelectedList;
    }

    static class ViewHolder {
        public ChooserImageView mImageIv;

        public View mClickArea;

        public CheckBox mSelectedCb;

        public View llRoot;
    }

}
