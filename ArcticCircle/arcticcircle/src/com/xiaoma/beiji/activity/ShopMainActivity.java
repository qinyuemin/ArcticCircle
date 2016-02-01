/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015
 */
package com.xiaoma.beiji.activity;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import com.baidu.location.BDLocation;
import com.makeapp.android.adapter.ArrayListAdapter;
import com.makeapp.android.util.ImageViewUtil;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ViewUtil;
import com.makeapp.javase.lang.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.adapter.ShopAdapter;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.controls.dialog.FriendContactDialog;
import com.xiaoma.beiji.entity.*;
import com.xiaoma.beiji.manager.LocationManager;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.CommUtil;
import com.xiaoma.beiji.util.IntentUtil;
import com.xiaoma.beiji.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称： StoreMainActivity
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年11月21 16:42
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ShopMainActivity extends SimpleBaseActivity implements View.OnClickListener {
    private static final String TAG = ShopMainActivity.class.getSimpleName();

    private String shopId;
    private ShopEntity entity;

    private int currentPosition = 0;
    private View line1, line2, line3;

    private GridView gvGoods, gvDynamic;
    private ListView lstComment;
    private ArrayListAdapter<GoodsEntity> goodsAdapter;
    private ArrayListAdapter<ShopDynamicEntity> dynamicAdapter;
    private ArrayListAdapter<CommentEntity> commentAdapter;

    private List<GoodsEntity> goodsEntities;
    private List<ShopDynamicEntity> dynamicEntities;
    private List<CommentEntity> commentEntities;

    private FriendContactDialog friendContactDialog;

    @Override
    protected String getActivityTitle() {
        return "店铺首页";
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_shop_main;
    }

    @Override
    protected void initIntent() {
        shopId = getIntent().getStringExtra("shopId");
    }

    @Override
    protected void setTitleControlsInfo() {
        super.setTitleControlsInfo();

//        ViewUtil.setViewVisibility(this,R.id.title_bar_right_img, View.VISIBLE);
//        ImageViewUtil.setImageSrcId(this,R.id.title_bar_right_img, R.drawable.ic_shop_collet);
//
//        ViewUtil.setViewVisibility(this,R.id.title_bar_right_sub_img, View.VISIBLE);
//        ImageViewUtil.setImageSrcId(this,R.id.title_bar_right_sub_img, R.drawable.ic_shop_share);


    }

    @Override
    protected void initComponents() {
        setTitleControlsInfo();

        ViewUtil.setViewOnClickListener(this,R.id.ll_praise,this);
        ViewUtil.setViewOnClickListener(this,R.id.ll_tell,this);

        goodsEntities = new ArrayList<>();
        dynamicEntities = new ArrayList<>();
        commentEntities = new ArrayList<>();

        ViewUtil.setViewOnClickListener(this, R.id.ll_dynamic, this);
        ViewUtil.setViewOnClickListener(this, R.id.ll_goods, this);
        ViewUtil.setViewOnClickListener(this, R.id.ll_comment, this);
        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);
        line3 = findViewById(R.id.line3);

        gvGoods = (GridView) findViewById(R.id.gv_goods);
        gvDynamic = (GridView) findViewById(R.id.gv_dynamic);
        lstComment = (ListView) findViewById(R.id.lst_comment);

        int screenWidth = CommUtil.getScreenWidth(this);
        int dip5 = CommUtil.dip2px(this.getApplicationContext(), 5);
        final int imageSize = (screenWidth - (dip5 * 4)) / 2;

        goodsAdapter = new ArrayListAdapter<GoodsEntity>(this, R.layout.gv_item_goods, goodsEntities) {
            @Override
            public void fillView(ViewGroup viewGroup, View view, GoodsEntity entity, int i) {
                String avatar = entity.getPic();
                if (StringUtil.isValid(avatar)) {
                    ImageView viewById = (ImageView) view.findViewById(R.id.img);
                    ImageLoader.getInstance().displayImage(avatar, viewById);
                    ViewGroup.LayoutParams lp = viewById.getLayoutParams();
                    lp.width = lp.height = imageSize;
                }

                TextViewUtil.setText(view, R.id.txt_name, entity.getGoodsName());
                TextViewUtil.setText(view, R.id.txt_tag, entity.getPrice());
            }
        };
        gvGoods.setAdapter(goodsAdapter);

        dynamicAdapter = new ArrayListAdapter<ShopDynamicEntity>(this, R.layout.gv_item_shop_dynamic, dynamicEntities) {
            @Override
            public void fillView(ViewGroup viewGroup, View view, ShopDynamicEntity entity, int i) {
                String avatar = entity.getPicList().get(0);
                if (StringUtil.isValid(avatar)) {
                    ImageView viewById = (ImageView) view.findViewById(R.id.img);
                    ImageLoader.getInstance().displayImage(avatar, viewById);
                    ViewGroup.LayoutParams lp = viewById.getLayoutParams();
                    lp.width = lp.height = imageSize;
                }

                TextViewUtil.setText(view, R.id.txt_name, entity.getContent());
                TextViewUtil.setText(view, R.id.txt_comment, entity.getCommentNum());
                TextViewUtil.setText(view, R.id.txt_like, entity.getPraiseNum());
            }
        };
        gvDynamic.setAdapter(dynamicAdapter);

        commentAdapter = new ArrayListAdapter<CommentEntity>(this, R.layout.ls_item_shop_comment, commentEntities) {
            @Override
            public void fillView(ViewGroup viewGroup, View view, CommentEntity entity, int i) {
                String avatar = entity.getAvatar();
                if (StringUtil.isValid(avatar)) {
                    ImageView viewById = (ImageView) view.findViewById(R.id.img_head);
                    ImageLoader.getInstance().displayImage(avatar, viewById);
                }
                TextViewUtil.setText(view, R.id.txt_name, entity.getNickname());
                TextViewUtil.setText(view, R.id.txt_time, entity.getCreateTimeTitle());

                String content = entity.getCommentContent();
                String toUserNickName = entity.getToUserNickName();
                if(StringUtil.isValid(toUserNickName)){
                    content = "回复"+toUserNickName +":"+entity.getCommentContent();
                }
                TextViewUtil.setText(view, R.id.txt_msg, content);
            }
        };
        lstComment.setAdapter(commentAdapter);


        gvGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                IntentUtil.goShopGoodsDetailActivity(ShopMainActivity.this,goodsEntities.get(i).getGoodsId());
            }
        });
        gvDynamic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShopDynamicEntity entity1 = dynamicEntities.get(i);
                IntentUtil.goShopDetailActivity(ShopMainActivity.this,shopId,entity1.getReleaseId());
            }
        });
        lstComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });

        setTabColor(0);

//        try {
//            Typeface typeFace = Typeface.createFromAsset(getResources().getAssets(),"fonts/AndroidEmoji.ttf");
//            title.setTypeface(typeFace);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void loadData() {
        HttpClientUtil.Shop.shopGetInfo(shopId, new AbsHttpResultHandler<ShopEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, ShopEntity data) {
                setViewData(data);
            }

            @Override
            public void onFailure(int resultCode, String desc) {

            }
        });

        // 商品
        HttpClientUtil.Shop.shopGetGoodsList(shopId, "", "", new AbsHttpResultHandler<GoodsEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, GoodsEntity data) {
            }

            @Override
            public void onSuccess(int resultCode, String desc, List<GoodsEntity> data) {
                goodsEntities.addAll(data);
                goodsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int resultCode, String desc) {

            }
        });

        // 动态
        HttpClientUtil.Shop.shopReleaseList(shopId, "", "", new AbsHttpResultHandler<ShopEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, ShopEntity data) {
                dynamicEntities.addAll(data.getDynamicEntities());
                dynamicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int resultCode, String desc) {

            }
        });

        // 评论
        HttpClientUtil.Shop.shopGetCommentList(shopId, new AbsHttpResultHandler<ShopEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, ShopEntity data) {
                CommentListEntity listEntity = data.getCommentListEntity();
                commentEntities.addAll(listEntity.getCommentEntities());
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int resultCode, String desc) {

            }
        });

    }

    private void setViewData(ShopEntity entity) {
        this.entity = entity;
        TextViewUtil.setText(this,R.id.txt_name,entity.getShowName());
        TextViewUtil.setText(this,R.id.txt_address,entity.getAddress());
        BDLocation currentLocation = LocationManager.getInstance().getCurrentLocation();
        try {
            TextViewUtil.setText(this,R.id.txt_distance,"距离:"+ LocationManager.getKMDistance(LocationManager.distanceOfTwoPoints("" + currentLocation.getLatitude(), "" + currentLocation.getLongitude(), entity.getLatitude(), entity.getLongitude())));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_tell:
                if(entity != null){
                    if(friendContactDialog == null){
                        friendContactDialog = new FriendContactDialog(this,entity);
                    }
                    if(!friendContactDialog.isShowing()){
                        friendContactDialog.show();
                    }
                }
                break;
            case R.id.ll_praise:
                if(entity != null){
                    HttpClientUtil.Shop.shopDoPraise(shopId, new AbsHttpResultHandler() {
                        @Override
                        public void onSuccess(int resultCode, String desc, Object data) {
                            ToastUtil.showToast(ShopMainActivity.this,"推荐成功");
                        }

                        @Override
                        public void onFailure(int resultCode, String desc) {
                            ToastUtil.showToast(ShopMainActivity.this,"店铺已推荐过");
                        }
                    });
                }
                break;

            case R.id.ll_dynamic:
                setTabColor(0);
                break;
            case R.id.ll_goods:
                setTabColor(1);
                break;
            case R.id.ll_comment:
                setTabColor(2);
                break;
        }
    }

    private void setTabColor(int position) {
        currentPosition = position;
        switch (position) {
            case 0:
                line1.setBackgroundResource(R.color.color_blue);
                line2.setBackgroundResource(R.color.white);
                line3.setBackgroundResource(R.color.white);

                gvDynamic.setVisibility(View.VISIBLE);
                gvGoods.setVisibility(View.GONE);
                lstComment.setVisibility(View.GONE);
                break;
            case 1:
                line1.setBackgroundResource(R.color.white);
                line2.setBackgroundResource(R.color.color_blue);
                line3.setBackgroundResource(R.color.white);

                gvDynamic.setVisibility(View.GONE);
                gvGoods.setVisibility(View.VISIBLE);
                lstComment.setVisibility(View.GONE);
                break;
            case 2:
                line1.setBackgroundResource(R.color.white);
                line2.setBackgroundResource(R.color.white);
                line3.setBackgroundResource(R.color.color_blue);

                gvDynamic.setVisibility(View.GONE);
                gvGoods.setVisibility(View.GONE);
                lstComment.setVisibility(View.VISIBLE);
                break;
        }
    }
}