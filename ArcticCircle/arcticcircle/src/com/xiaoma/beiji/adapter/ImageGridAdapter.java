/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.adapter
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.adapter;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.entity.FriendDynamicEntity;
import com.xiaoma.beiji.entity.PicEntity;
import com.xiaoma.beiji.util.CommUtil;
import com.xiaoma.beiji.util.ImageLoaderUtil;
import com.xiaoma.beiji.util.IntentUtil;

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
public class ImageGridAdapter extends LLBaseAdapter<PicEntity> {
    private final static int MAX_IMAGE_SIZE = 9;
    private int maxImageSize = 0;
    private int imageSize;
    private DisplayImageOptions option;

    public ImageGridAdapter(Activity activity, List<PicEntity> dataList, int imageSize, int maxImageSize) {
        super(activity, dataList);
        this.imageSize = imageSize;
        this.maxImageSize = maxImageSize;
        option = ImageLoaderUtil.getDefaultImageLoaderOption();
    }

    @Override
    public int getCount() {
        int dataCount = getDataCount();
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
            PicEntity data = getItem(position);

            String uri = null;
            String picUrl = data.getPicUrl();
            if (picUrl.startsWith("http")) {
                uri = picUrl;
            } else {
                uri = Uri.fromFile(new File(picUrl)).toString();
            }
            ImageLoader.getInstance().displayImage(uri, viewHolder.imageView, option);
            viewHolder.rightSignImage.setVisibility(View.GONE);
            viewHolder.rightSignImage.setOnClickListener(null);
        }

        return convertView;
    }

    private static final class ViewHolder {
        ImageView imageView;
        View rightSignImage;
    }

    public static void initPicGridView(final Activity activity, GridView gvPic, FriendDynamicEntity entity) {
        final List<PicEntity> picList = entity.getPic();
        if (picList != null && picList.size() > 0) {
            int screenWidth = CommUtil.getScreenWidth(activity);
            int dip5 = CommUtil.dip2px(activity.getApplicationContext(), 5);
            int imageSize = (screenWidth - (dip5 * 4)) / 3;
            ImageGridAdapter adapter = new ImageGridAdapter(activity, picList, imageSize, MAX_IMAGE_SIZE);
            gvPic.setAdapter(adapter);
            gvPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    IntentUtil.goPictureDetailActivity(activity, i, picList);
                }
            });

            gvPic.setVisibility(View.VISIBLE);
        } else {
            gvPic.setVisibility(View.GONE);
        }
    }
}