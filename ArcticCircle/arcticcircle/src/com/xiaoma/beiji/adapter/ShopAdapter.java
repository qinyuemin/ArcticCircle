/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.adapter
 * 版本信息： 1.0.0
 * Copyright (c) 2015 版权所有
 */
package com.xiaoma.beiji.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ViewUtil;
import com.makeapp.javase.lang.StringUtil;
import com.makeapp.javase.util.DataUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.view.CircularImage;
import com.xiaoma.beiji.entity.*;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.CommUtil;
import com.xiaoma.beiji.util.IntentUtil;
import com.xiaoma.beiji.util.ToastUtil;

import java.util.List;

/**
 * 类名称： ShopAdapter
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月10 22:56
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ShopAdapter extends BaseAdapter {
    private final int MAX_IMAGE_SIZE = 9;

    private Activity activity;
    private List<ShopEntity> shopEntities;
    private LayoutInflater layoutInflater;

    public ShopAdapter(Activity activity, List<ShopEntity> shopEntities) {
        this.activity = activity;
        this.shopEntities = shopEntities;
        layoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return shopEntities.size();
    }

    @Override
    public ShopEntity getItem(int i) {
        return shopEntities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View contentView = layoutInflater.inflate(R.layout.lst_item_shop, null);
        final ShopEntity entity = getItem(i);
        // 店铺动态

//        String avatar = entity.getAvatar();
//        if (StringUtil.isValid(avatar)) {
//            CircularImage imgHead = (CircularImage) contentView.findViewById(R.id.img_head);
//            ImageLoader.getInstance().displayImage(avatar, imgHead);
//        }

        TextViewUtil.setText(contentView, R.id.txt_name, entity.getShowName());
        TextViewUtil.setText(contentView, R.id.txt_address, entity.getPrimaryBusiness());
        TextViewUtil.setText(contentView, R.id.txt_time, "距离:"+ entity.getDistrictName());

        GridView gvPic = (GridView) contentView.findViewById(R.id.gv_pic);
        final List<PicEntity> picList = entity.getPicEntities();
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

        return contentView;
    }
}
