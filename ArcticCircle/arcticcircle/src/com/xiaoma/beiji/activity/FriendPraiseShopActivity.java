/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.adapter.ShopAdapter;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.entity.ShopEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 类名称： FriendPraiseShopActivity
 * 类描述： 
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月22 17:25
 * 修改备注：
 * @version 1.0.0
 *
 */
public class FriendPraiseShopActivity extends SimpleBaseActivity {
    private static final String TAG = FriendPraiseShopActivity.class.getSimpleName();
    private ShopAdapter shopAdapter;
    private ListView lstShop;
    private List<ShopEntity> shopEntities;

    private String releaseUserId = "";

    @Override
    protected String getActivityTitle() {
        return "TA点赞的店铺";
    }

    @Override
    protected void initIntent() {
        super.initIntent();
        releaseUserId = getIntent().getStringExtra("releaseUserId");
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_friend_praise_shop;
    }

    @Override
    protected void initComponents() {
        setTitleControlsInfo();
        shopEntities = new ArrayList<>();

        lstShop = (ListView) findViewById(R.id.lst_shop);
        shopAdapter = new ShopAdapter(this, shopEntities);
        lstShop.setAdapter(shopAdapter);
    }

    @Override
    protected void loadData() {
        HttpClientUtil.Shop.shopPriseList(releaseUserId, "", new AbsHttpResultHandler<JSONObject>() {
            @Override
            public void onSuccess(int resultCode, String desc, JSONObject data) {
                List<ShopEntity > entities =  JSON.parseArray(data.getString("list"),ShopEntity.class) ;
                shopEntities.clear();
                shopEntities.addAll(entities);
                shopAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int resultCode, String desc) {

            }
        });
    }
}