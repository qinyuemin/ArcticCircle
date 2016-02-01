/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.adapter
 * 版本信息： 1.0.0
 * Copyright (c) 2015
 */
package com.xiaoma.beiji.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.makeapp.android.util.AnimationUtil;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ViewUtil;
import com.makeapp.javase.lang.StringUtil;
import com.makeapp.javase.util.DataUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.controls.sharesdk.ShareSdkUtil;
import com.xiaoma.beiji.controls.view.ImgPagerView;
import com.xiaoma.beiji.entity.FriendDynamicEntity;
import com.xiaoma.beiji.manager.LocationManager;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.*;

import java.util.List;

/**
 * 类名称： FriendTrendsAdapter
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年11月21 14:52
 * 修改备注：
 *
 * @version 1.0.0
 */
public class FriendDynamicAdapter extends BaseAdapter {
    private Activity activity;
    private List<FriendDynamicEntity> entities;
    private LayoutInflater layoutInflater;

    private int [] viewType = {1,3,4,5,6,2,7,8,9};

    public FriendDynamicAdapter(Activity activity, List<FriendDynamicEntity> entities) {
        this.activity = activity;
        this.entities = entities;

        layoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return entities.size();
    }

    @Override
    public FriendDynamicEntity getItem(int i) {
        return entities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        int releaseType = getItem(position).getReleaseType();
        int type;
        switch (releaseType){
            case 3:
                type = 3;
                break;
            case 4:
                type = 4;
                break;
            case 5:
                type = 5;
                break;
            default:
                type = 1;
        }
        return type;
    }

    @Override
    public int getViewTypeCount() {
        return viewType.length;
    }

    @Override
    public View getView(int i, View contentView, ViewGroup viewGroup) {
        Holder1 holder1 = null;
        Holder2 holder2 = null;
        Holder3 holder3 = null;
        Holder4 holder4 = null;

        final FriendDynamicEntity entity = getItem(i);
        int releaseType = entity.getReleaseType();
        int type = getItemViewType(i);

        if (contentView == null) {
            if (type == 3) {
                // 朋友推荐店铺
                holder2 = new Holder2();
                contentView = layoutInflater.inflate(R.layout.lst_item_friend_dynamic3, null);
                holder2.txtShopName = (TextView) contentView.findViewById(R.id.txt_shop_name);
                holder2.txtDistance = (TextView) contentView.findViewById(R.id.txt_distance);
                holder2.txtDescription = (TextView) contentView.findViewById(R.id.txt_description);
                holder2.ipvImg = (ImgPagerView) contentView.findViewById(R.id.ipv_img);
                holder2.rlInfo = contentView.findViewById(R.id.rl_info);

                contentView.setTag(holder2);
            } else if (type == 4) {
                // 推广店铺
                holder3 = new Holder3();
                contentView = layoutInflater.inflate(R.layout.lst_item_friend_dynamic4, null);
                holder3.txtDescription = (TextView) contentView.findViewById(R.id.txt_description);
                holder3.gvPic = (GridView) contentView.findViewById(R.id.gv_pic);
                holder3.rlInfo = contentView.findViewById(R.id.rl_info);

                contentView.setTag(holder3);
            } else if (type == 5) {
                // 系统推荐消息
                holder4 = new Holder4();
                contentView = layoutInflater.inflate(R.layout.lst_item_friend_dynamic5, null);
                holder4.rlInfo = contentView.findViewById(R.id.rl_info);

                contentView.setTag(holder4);
            } else {
                // 店铺动态 1， 6， 其他
                holder1 = new Holder1();
                contentView = layoutInflater.inflate(R.layout.lst_item_friend_dynamic, null);
                holder1.txtCollect = (TextView) contentView.findViewById(R.id.txt_collect);
                holder1.txtLike = (TextView) contentView.findViewById(R.id.txt_like);
                holder1.imgA = (ImageView) contentView.findViewById(R.id.img_a);
                holder1.txtA = (TextView) contentView.findViewById(R.id.txt_a);
                holder1.llAction = (LinearLayout) contentView.findViewById(R.id.ll_action);
                holder1.txtComment = (TextView) contentView.findViewById(R.id.txt_comment);
                holder1.txtDescription = (TextView) contentView.findViewById(R.id.txt_description);
                holder1.txtShare = (TextView) contentView.findViewById(R.id.txt_share);
                holder1.gvPic = (GridView) contentView.findViewById(R.id.gv_pic);
                holder1.rlInfo = contentView.findViewById(R.id.rl_info);

                contentView.setTag(holder1);
            }
        } else {
            //有convertView，按样式，取得不用的布局
             if (type == 3) {
                // 朋友推荐店铺
                holder2 = (Holder2) contentView.getTag();
            } else if (type == 4) {
                // 推广店铺
                holder3 = (Holder3) contentView.getTag();
            } else if (type == 5) {
                // 系统推荐消息
                holder4 = (Holder4) contentView.getTag();
            } else {
                // 店铺动态  1， 6， 其他
                holder1 = (Holder1) contentView.getTag();
            }
        }

        View.OnClickListener infoListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.goFriendDetailActivity(activity, entity.getUserId());
            }
        };

        if (type == 3) {
            // 朋友推荐店铺
            AdapterViewUtil.initUserInfoView(activity, contentView, entity);

            holder2.txtShopName.setText(""+entity.getShopName());
            BDLocation currentLocation = LocationManager.getInstance().getCurrentLocation();
            try {
                holder2.txtDistance.setText("距离:" + LocationManager.getKMDistance(LocationManager.distanceOfTwoPoints("" + currentLocation.getLatitude(), "" + currentLocation.getLongitude(), entity.getLatitude(), entity.getLongitude())));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String description = entity.getDescription();
            holder2.txtDescription.setVisibility(StringUtil.isValid(description)?View.VISIBLE:View.GONE);
            holder2.txtDescription.setText(""+description);

            // 初始化 图片
            List<String> shopPic = entity.getShopPic();
            holder2.ipvImg.notifyData(shopPic);

            holder2.rlInfo.setOnClickListener(infoListener);
        } else if (type == 4) {
            // 推广店铺
            AdapterViewUtil.initUserInfoView(activity, contentView, entity);
            ImageGridAdapter.initPicGridView(activity, holder3.gvPic, entity);

            holder3.txtDescription.setText(""+entity.getContent());
            holder3.rlInfo.setOnClickListener(infoListener);
        } else if (type == 5) {
            // 系统推荐消息
            AdapterViewUtil.initUserInfoView(activity, contentView, entity);
            holder4.rlInfo.setOnClickListener(infoListener);
        } else {
            // 店铺动态
            AdapterViewUtil.initUserInfoView(activity, contentView, entity);
            ImageGridAdapter.initPicGridView(activity, holder1.gvPic, entity);

            CommUtil.textViewSetImg(activity, holder1.txtCollect, (entity.isHaveFavorite() ? R.drawable.ic_collected : R.drawable.ic_uncollect));
            CommUtil.textViewSetImg(activity, holder1.txtLike, (entity.isHavePraise() ? R.drawable.ic_liked : R.drawable.ic_unlike));

            AdapterViewUtil.initCommentView(contentView, entity);

            holder1.txtDescription.setText(""+entity.getContent());

            holder1.txtCollect.setText(DataUtil.getInt(entity.getFavoriteNum()) == 0 ? "收藏" : entity.getFavoriteNum());
            holder1.txtLike.setText(DataUtil.getInt(entity.getPraiseNum()) == 0 ? "喜欢" : entity.getPraiseNum());

            TextViewUtil.setText(contentView, R.id.txt_comment, DataUtil.getInt(entity.getCommentNum()) == 0 ? "评论" : entity.getCommentNum());

            final Holder1 finalHolder = holder1;
            holder1.txtCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 动画
                    if (!entity.isHaveFavorite()) {
                        finalHolder.imgA.setImageResource(R.drawable.ic_collected);
                        AdapterViewUtil.startScale(activity, view, finalHolder.txtA, finalHolder.llAction, finalHolder.imgA);
                    }

                    HttpClientUtil.Dynamic.dynamicDoFavorite(entity.getReleaseId(), entity.isHaveFavorite() ? 2 : 1, new AbsHttpResultHandler() {
                        @Override
                        public void onSuccess(int resultCode, String desc, Object data) {
                            entity.setHaveFavorite(!entity.isHaveFavorite());
                            CommUtil.textViewSetImg(activity, finalHolder.txtCollect, (entity.isHaveFavorite() ? R.drawable.ic_collected : R.drawable.ic_uncollect));

                            int anInt = DataUtil.getInt(entity.getFavoriteNum());
                            if (entity.isHaveFavorite()) {
                                ++anInt;
                            } else {
                                --anInt;
                            }
                            finalHolder.txtCollect.setText(anInt <= 0 ? "收藏" : "" + anInt);
                            entity.setFavoriteNum("" + anInt);
                        }

                        @Override
                        public void onFailure(int resultCode, String desc) {
                            ToastUtil.showToast(activity, "操作失败，请稍侯重试:" + desc);
                        }
                    });
                }
            });

            holder1.txtLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 动画
                    if (!entity.isHavePraise()) {
                        finalHolder.imgA.setImageResource(R.drawable.ic_liked);
                        AdapterViewUtil.startScale(activity, view, finalHolder.txtA, finalHolder.llAction, finalHolder.imgA);
                    }
                    HttpClientUtil.Dynamic.dynamicDoPraise(entity.getReleaseId(), entity.isHavePraise() ? 2 : 1, new AbsHttpResultHandler() {
                        @Override
                        public void onSuccess(int resultCode, String desc, Object data) {
                            entity.setHavePraise(!entity.isHavePraise());
                            CommUtil.textViewSetImg(activity, finalHolder.txtLike, (entity.isHavePraise() ? R.drawable.ic_liked : R.drawable.ic_unlike));

                            int anInt = DataUtil.getInt(entity.getPraiseNum());
                            if (entity.isHavePraise()) {
                                ++anInt;
                            } else {
                                --anInt;
                            }
                            finalHolder.txtLike.setText(anInt <= 0 ? "喜欢" : "" + anInt);
                            entity.setPraiseNum("" + anInt);
                        }

                        @Override
                        public void onFailure(int resultCode, String desc) {
                            ToastUtil.showToast(activity, "操作失败，请稍侯重试:" + desc);
                        }
                    });
                }
            });

            holder1.txtShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShareSdkUtil.showShare(activity, entity.getDynamicId());
                }
            });

            holder1.rlInfo.setOnClickListener(infoListener);
        }

        //region Description
//        if (releaseType == 1 || releaseType == 6) {
//            // 店铺动态
//            contentView = layoutInflater.inflate(R.layout.lst_item_friend_dynamic, null);
//
//            AdapterViewUtil.initUserInfoView(activity, contentView, entity);
//
//            ImageGridAdapter.initPicGridView(activity, contentView, entity);
//
//            final TextView txtCollect = (TextView) contentView.findViewById(R.id.txt_collect);
//            final TextView txtLike = (TextView) contentView.findViewById(R.id.txt_like);
//            final ImageView imgA = (ImageView) contentView.findViewById(R.id.img_a);
//            final TextView txtA = (TextView) contentView.findViewById(R.id.txt_a);
//            final LinearLayout llAction = (LinearLayout) contentView.findViewById(R.id.ll_action);
//            CommUtil.textViewSetImg(activity, txtCollect, (entity.isHaveFavorite() ? R.drawable.ic_collected : R.drawable.ic_uncollect));
//            CommUtil.textViewSetImg(activity, txtLike, (entity.isHavePraise() ? R.drawable.ic_liked : R.drawable.ic_unlike));
//
//            AdapterViewUtil.initCommentView(contentView, entity);
//
//            TextViewUtil.setText(contentView, R.id.txt_description, entity.getContent());
//
//            txtCollect.setText(DataUtil.getInt(entity.getFavoriteNum()) == 0 ? "收藏" : entity.getFavoriteNum());
//            txtLike.setText(DataUtil.getInt(entity.getPraiseNum()) == 0 ? "喜欢" : entity.getPraiseNum());
//
//            TextViewUtil.setText(contentView, R.id.txt_comment, DataUtil.getInt(entity.getCommentNum()) == 0 ? "评论" : entity.getCommentNum());
//
//            txtCollect.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // 动画
//                    if (!entity.isHaveFavorite()) {
//                        imgA.setImageResource(R.drawable.ic_collected);
//                        AdapterViewUtil.startScale(activity, view, txtA, llAction, imgA);
//                    }
//
//                    HttpClientUtil.Dynamic.dynamicDoFavorite(entity.getReleaseId(), entity.isHaveFavorite() ? 2 : 1, new AbsHttpResultHandler() {
//                        @Override
//                        public void onSuccess(int resultCode, String desc, Object data) {
//                            entity.setHaveFavorite(!entity.isHaveFavorite());
//                            CommUtil.textViewSetImg(activity, txtCollect, (entity.isHaveFavorite() ? R.drawable.ic_collected : R.drawable.ic_uncollect));
//
//                            int anInt = DataUtil.getInt(entity.getFavoriteNum());
//                            if (entity.isHaveFavorite()) {
//                                ++anInt;
//                            } else {
//                                --anInt;
//                            }
//                            txtCollect.setText(anInt <= 0 ? "收藏" : "" + anInt);
//                            entity.setFavoriteNum("" + anInt);
//                        }
//
//                        @Override
//                        public void onFailure(int resultCode, String desc) {
//                            ToastUtil.showToast(activity, "操作失败，请稍侯重试:" + desc);
//                        }
//                    });
//                }
//            });
//
//            txtLike.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // 动画
//                    if (!entity.isHavePraise()) {
//                        imgA.setImageResource(R.drawable.ic_liked);
//                        AdapterViewUtil.startScale(activity, view, txtA, llAction, imgA);
//                    }
//                    HttpClientUtil.Dynamic.dynamicDoPraise(entity.getReleaseId(), entity.isHavePraise() ? 2 : 1, new AbsHttpResultHandler() {
//                        @Override
//                        public void onSuccess(int resultCode, String desc, Object data) {
//                            entity.setHavePraise(!entity.isHavePraise());
//                            CommUtil.textViewSetImg(activity, txtLike, (entity.isHavePraise() ? R.drawable.ic_liked : R.drawable.ic_unlike));
//
//                            int anInt = DataUtil.getInt(entity.getPraiseNum());
//                            if (entity.isHavePraise()) {
//                                ++anInt;
//                            } else {
//                                --anInt;
//                            }
//                            txtLike.setText(anInt <= 0 ? "喜欢" : "" + anInt);
//                            entity.setPraiseNum("" + anInt);
//                        }
//
//                        @Override
//                        public void onFailure(int resultCode, String desc) {
//                            ToastUtil.showToast(activity, "操作失败，请稍侯重试:" + desc);
//                        }
//                    });
//                }
//            });
//
//            contentView.findViewById(R.id.txt_share).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    ShareSdkUtil.showShare(activity, entity.getDynamicId());
//                }
//            });
//
//
//        } else if (releaseType == 3) {
//            // 朋友推荐店铺
//            contentView = layoutInflater.inflate(R.layout.lst_item_friend_dynamic3, null);
//
//            AdapterViewUtil.initUserInfoView(activity, contentView, entity);
//
//            TextViewUtil.setText(contentView, R.id.txt_shop_name, entity.getShopName());
//            BDLocation currentLocation = LocationManager.getInstance().getCurrentLocation();
//            try {
//                TextViewUtil.setText(contentView, R.id.txt_distance, "距离:" + LocationManager.getKMDistance(LocationManager.distanceOfTwoPoints("" + currentLocation.getLatitude(), "" + currentLocation.getLongitude(), entity.getLatitude(), entity.getLongitude())));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            String description = entity.getDescription();
//            if (StringUtil.isValid(description)) {
//                TextViewUtil.setText(contentView, R.id.txt_description, description);
//                ViewUtil.setViewVisibility(contentView, R.id.txt_description, View.VISIBLE);
//            } else {
//                ViewUtil.setViewVisibility(contentView, R.id.txt_description, View.GONE);
//            }
//
//            // 初始化 图片
//            List<String> shopPic = entity.getShopPic();
//            ImgPagerView ipvImg = (ImgPagerView) contentView.findViewById(R.id.ipv_img);
//            ipvImg.notifyData(shopPic);
//        } else if (releaseType == 4) {
//            // 推广店铺
//            contentView = layoutInflater.inflate(R.layout.lst_item_friend_dynamic4, null);
//
//            AdapterViewUtil.initUserInfoView(activity, contentView, entity);
//            ImageGridAdapter.initPicGridView(activity, contentView, entity);
//
//            TextViewUtil.setText(contentView, R.id.txt_description, entity.getContent());
//        } else if (releaseType == 5) {
//            // 系统推荐消息
//            contentView = layoutInflater.inflate(R.layout.lst_item_friend_dynamic5, null);
//            AdapterViewUtil.initUserInfoView(activity, contentView, entity);
//        } else {
//            contentView = layoutInflater.inflate(R.layout.lst_item_friend_dynamic6, null);
//        }
        //endregion

        return contentView;
    }

    public class Holder1 {
        TextView txtCollect;
        TextView txtLike;
        ImageView imgA;
        TextView txtA;
        LinearLayout llAction;
        TextView txtComment;
        TextView txtDescription;
        TextView txtShare;
        GridView gvPic;
        View rlInfo;
    }

    public class Holder2 {
        TextView txtShopName;
        TextView txtDistance;
        TextView txtDescription;
        ImgPagerView ipvImg;
        View rlInfo;
    }

    public class Holder3 {
        TextView txtDescription;
        GridView gvPic;
        View rlInfo;
    }

    public class Holder4 {
        View rlInfo;

    }


//    public View getView(int i, View contentView, ViewGroup viewGroup) {
//
//        final FriendDynamicEntity entity = getItem(i);
//        int releaseType = entity.getReleaseType();
//        int type = getItemViewType(releaseType);
//        if (contentView == null) {
//            if(type){
//
//            }
//        }
//
//
//        if (releaseType == 1 || releaseType == 6) {
//            // 店铺动态
//            contentView = layoutInflater.inflate(R.layout.lst_item_friend_dynamic, null);
//
//            AdapterViewUtil.initUserInfoView(activity, contentView, entity);
//
//            ImageGridAdapter.initPicGridView(activity, contentView, entity);
//
//            final TextView txtCollect = (TextView) contentView.findViewById(R.id.txt_collect);
//            final TextView txtLike = (TextView) contentView.findViewById(R.id.txt_like);
//            final ImageView imgA = (ImageView) contentView.findViewById(R.id.img_a);
//            final TextView txtA = (TextView) contentView.findViewById(R.id.txt_a);
//            final LinearLayout llAction = (LinearLayout) contentView.findViewById(R.id.ll_action);
//            CommUtil.textViewSetImg(activity, txtCollect, (entity.isHaveFavorite() ? R.drawable.ic_collected : R.drawable.ic_uncollect));
//            CommUtil.textViewSetImg(activity, txtLike, (entity.isHavePraise() ? R.drawable.ic_liked : R.drawable.ic_unlike));
//
//            AdapterViewUtil.initCommentView(contentView, entity);
//
//            TextViewUtil.setText(contentView, R.id.txt_description, entity.getContent());
//
//            txtCollect.setText(DataUtil.getInt(entity.getFavoriteNum()) == 0 ? "收藏" : entity.getFavoriteNum());
//            txtLike.setText(DataUtil.getInt(entity.getPraiseNum()) == 0 ? "喜欢" : entity.getPraiseNum());
//
//            TextViewUtil.setText(contentView, R.id.txt_comment, DataUtil.getInt(entity.getCommentNum()) == 0 ? "评论" : entity.getCommentNum());
//
//            txtCollect.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // 动画
//                    if (!entity.isHaveFavorite()) {
//                        imgA.setImageResource(R.drawable.ic_collected);
//                        AdapterViewUtil.startScale(activity, view, txtA, llAction, imgA);
//                    }
//
//                    HttpClientUtil.Dynamic.dynamicDoFavorite(entity.getReleaseId(), entity.isHaveFavorite() ? 2 : 1, new AbsHttpResultHandler() {
//                        @Override
//                        public void onSuccess(int resultCode, String desc, Object data) {
//                            entity.setHaveFavorite(!entity.isHaveFavorite());
//                            CommUtil.textViewSetImg(activity, txtCollect, (entity.isHaveFavorite() ? R.drawable.ic_collected : R.drawable.ic_uncollect));
//
//                            int anInt = DataUtil.getInt(entity.getFavoriteNum());
//                            if (entity.isHaveFavorite()) {
//                                ++anInt;
//                            } else {
//                                --anInt;
//                            }
//                            txtCollect.setText(anInt <= 0 ? "收藏" : "" + anInt);
//                            entity.setFavoriteNum("" + anInt);
//                        }
//
//                        @Override
//                        public void onFailure(int resultCode, String desc) {
//                            ToastUtil.showToast(activity, "操作失败，请稍侯重试:" + desc);
//                        }
//                    });
//                }
//            });
//
//            txtLike.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // 动画
//                    if (!entity.isHavePraise()) {
//                        imgA.setImageResource(R.drawable.ic_liked);
//                        AdapterViewUtil.startScale(activity, view, txtA, llAction, imgA);
//                    }
//                    HttpClientUtil.Dynamic.dynamicDoPraise(entity.getReleaseId(), entity.isHavePraise() ? 2 : 1, new AbsHttpResultHandler() {
//                        @Override
//                        public void onSuccess(int resultCode, String desc, Object data) {
//                            entity.setHavePraise(!entity.isHavePraise());
//                            CommUtil.textViewSetImg(activity, txtLike, (entity.isHavePraise() ? R.drawable.ic_liked : R.drawable.ic_unlike));
//
//                            int anInt = DataUtil.getInt(entity.getPraiseNum());
//                            if (entity.isHavePraise()) {
//                                ++anInt;
//                            } else {
//                                --anInt;
//                            }
//                            txtLike.setText(anInt <= 0 ? "喜欢" : "" + anInt);
//                            entity.setPraiseNum("" + anInt);
//                        }
//
//                        @Override
//                        public void onFailure(int resultCode, String desc) {
//                            ToastUtil.showToast(activity, "操作失败，请稍侯重试:" + desc);
//                        }
//                    });
//                }
//            });
//
//            contentView.findViewById(R.id.txt_share).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    ShareSdkUtil.showShare(activity, entity.getDynamicId());
//                }
//            });
//
//
//        } else if (releaseType == 3) {
//            // 朋友推荐店铺
//            contentView = layoutInflater.inflate(R.layout.lst_item_friend_dynamic3, null);
//
//            AdapterViewUtil.initUserInfoView(activity, contentView, entity);
//
//            TextViewUtil.setText(contentView, R.id.txt_shop_name, entity.getShopName());
//            BDLocation currentLocation = LocationManager.getInstance().getCurrentLocation();
//            try {
//                TextViewUtil.setText(contentView, R.id.txt_distance, "距离:" + LocationManager.getKMDistance(LocationManager.distanceOfTwoPoints("" + currentLocation.getLatitude(), "" + currentLocation.getLongitude(), entity.getLatitude(), entity.getLongitude())));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            String description = entity.getDescription();
//            if (StringUtil.isValid(description)) {
//                TextViewUtil.setText(contentView, R.id.txt_description, description);
//                ViewUtil.setViewVisibility(contentView, R.id.txt_description, View.VISIBLE);
//            } else {
//                ViewUtil.setViewVisibility(contentView, R.id.txt_description, View.GONE);
//            }
//
//            // 初始化 图片
//            List<String> shopPic = entity.getShopPic();
//            ImgPagerView ipvImg = (ImgPagerView) contentView.findViewById(R.id.ipv_img);
//            ipvImg.notifyData(shopPic);
//        } else if (releaseType == 4) {
//            // 推广店铺
//            contentView = layoutInflater.inflate(R.layout.lst_item_friend_dynamic4, null);
//
//            AdapterViewUtil.initUserInfoView(activity, contentView, entity);
//            ImageGridAdapter.initPicGridView(activity, contentView, entity);
//
//            TextViewUtil.setText(contentView, R.id.txt_description, entity.getContent());
//        } else if (releaseType == 5) {
//            // 系统推荐消息
//            contentView = layoutInflater.inflate(R.layout.lst_item_friend_dynamic5, null);
//            AdapterViewUtil.initUserInfoView(activity, contentView, entity);
//        } else {
//            contentView = layoutInflater.inflate(R.layout.lst_item_friend_dynamic6, null);
//        }
//
//        contentView.findViewById(R.id.rl_info).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                IntentUtil.goFriendDetailActivity(activity, entity.getUserId());
//            }
//        });
//
//        return contentView;
//    }


}
