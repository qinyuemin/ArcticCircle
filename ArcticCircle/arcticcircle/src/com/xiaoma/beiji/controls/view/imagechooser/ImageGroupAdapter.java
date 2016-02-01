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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.util.ImageLoaderUtil;

import java.util.List;

/**
 * 分组图片适配器
 * 
 * @author likebamboo
 */
public class ImageGroupAdapter extends BaseAdapter {
    /**
     * 上下文对象
     */
    private Context mContext = null;

    /**
     * 图片列表
     */
    private List<ImageGroup> mDataList = null;

    /**
     * 容器
     */
    private View mContainer = null;

    public ImageGroupAdapter(Context context, List<ImageGroup> list, View container) {
        mDataList = list;
        mContext = context;
        mContainer = container;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public ImageGroup getItem(int position) {
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
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.lst_item_chooser_group_item, null);
            holder.mImageIv = (ChooserImageView)view.findViewById(R.id.group_item_image_iv);
            holder.mTitleTv = (TextView)view.findViewById(R.id.group_item_title_tv);
            holder.mCountTv = (TextView)view.findViewById(R.id.group_item_count_tv);

            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }

        ImageGroup item = getItem(position);
        if (item != null) {
            // 图片路径
            String path = item.getFirstImgPath();
            // 标题
            holder.mTitleTv.setText(item.getDirName());
            // 计数
            holder.mCountTv.setText(mContext.getString(R.string.image_count, item.getImageCount()));
            holder.mImageIv.setTag(path);
            // 加载图片
            // 利用NativeImageLoader类加载本地图片
            ImageLoader.getInstance().displayImage("file:///" + path,holder.mImageIv);
        }
        return view;
    }

    static class ViewHolder {
        public ChooserImageView mImageIv;

        public TextView mTitleTv;

        public TextView mCountTv;
    }

}
