/**
 *
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 *
 */
package com.xiaoma.beiji.activity;

import android.app.Activity;
import android.os.Bundle;
import com.makeapp.android.util.TextViewUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.controls.view.ImgPagerView;
import com.xiaoma.beiji.entity.GoodsEntity;
import com.xiaoma.beiji.entity.PicEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称： ShopGoodsDetailActivity
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月16 0:40
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ShopGoodsDetailActivity extends SimpleBaseActivity {
    private static final String TAG = ShopGoodsDetailActivity.class.getSimpleName();

    private String goodsId;

    private Activity activity;

    @Override
    protected String getActivityTitle() {
        return "商品详情";
    }

    @Override
    protected void initIntent() {
        goodsId = getIntent().getStringExtra("goodsId");
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_shop_goods_detail;
    }

    @Override
    protected void initComponents() {
        activity = this;
        setTitleControlsInfo();
    }

    @Override
    protected void loadData() {
        HttpClientUtil.Shop.shopGetGoodsDetail(goodsId, new AbsHttpResultHandler<GoodsEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, GoodsEntity data) {
                TextViewUtil.setText(activity,R.id.txt_name,data.getGoodsName());
                TextViewUtil.setText(activity,R.id.txt_tag,data.getPrice());
                TextViewUtil.setText(activity,R.id.txt_description,data.getDescription());
                // 初始化 图片
                List<String> shopPic = new ArrayList<>();
                List<PicEntity> picList = data.getPicEntities();
                for(PicEntity en : picList){
                    shopPic.add(en.getPicUrl());
                }
                ImgPagerView ipvImg = (ImgPagerView) activity.findViewById(R.id.ipv_img);
                ipvImg.notifyData(shopPic);
            }

            @Override
            public void onFailure(int resultCode, String desc) {

            }
        });

    }
}