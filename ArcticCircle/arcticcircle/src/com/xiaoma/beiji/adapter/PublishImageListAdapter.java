/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.adapter
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.adapter;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.entity.FileUploadResultEntity;
import com.xiaoma.beiji.util.ImageLoaderUtil;

import java.io.File;
import java.util.List;

/**
 * 类名称： PublishImageListAdapter
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月01 23:42
 * 修改备注：
 *
 * @version 1.0.0
 */
public class PublishImageListAdapter extends LLBaseAdapter<FileUploadResultEntity> {
    private int maxImageSize = 0;
    private int imageSize;
    private DisplayImageOptions option;

    public PublishImageListAdapter(Activity activity, List<FileUploadResultEntity> dataList, int imageSize, int maxImageSize) {
        super(activity, dataList);
        this.imageSize = imageSize;
        this.maxImageSize = maxImageSize;
        option = ImageLoaderUtil.getDefaultImageLoaderOption();
    }

    @Override
    public int getCount() {
        int dataCount = getDataCount();
        if (dataCount < maxImageSize) {
            dataCount++;
        }
        return dataCount;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = getLayoutInflater().inflate(R.layout.lv_item_publish_image, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.rightSignImage = convertView.findViewById(R.id.rightSignImage);
            ViewGroup.LayoutParams lp = viewHolder.imageView.getLayoutParams();
            lp.width = lp.height = imageSize;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position == getDataCount()) {
            ImageLoader.getInstance().cancelDisplayTask(viewHolder.imageView);
            viewHolder.imageView.setImageResource(R.drawable.ic_add_photo);
            viewHolder.rightSignImage.setVisibility(View.GONE);
            viewHolder.rightSignImage.setOnClickListener(null);
        } else {
            FileUploadResultEntity entity = getItem(position);
            String data = entity.getLocalPath();
            String uri = null;
            if (data.startsWith("http")) {
                uri = data;
            } else {
                uri = Uri.fromFile(new File(data)).toString();
            }
            Log.d("getView", "getView "+uri);
            ImageLoader.getInstance().displayImage(uri, viewHolder.imageView, option);
            viewHolder.rightSignImage.setVisibility(View.VISIBLE);
            viewHolder.rightSignImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(position);
                    notifyDataSetChanged();
                }
            });
        }

        return convertView;
    }

    private static final class ViewHolder {
        ImageView imageView;
        View rightSignImage;
    }
}