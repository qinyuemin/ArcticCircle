/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015
 */
package com.xiaoma.beiji.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import com.makeapp.android.util.ImageViewUtil;
import com.makeapp.android.util.ViewUtil;
import com.makeapp.javase.util.DataUtil;
import com.makeapp.javase.util.DateUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.adapter.AdapterViewUtil;
import com.xiaoma.beiji.adapter.CommentAdapter;
import com.xiaoma.beiji.adapter.RecyclerView1Adapter;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.sharesdk.ShareSdkUtil;
import com.xiaoma.beiji.controls.view.CircularImage;
import com.xiaoma.beiji.controls.view.ExpandListView;
import com.xiaoma.beiji.controls.view.ImgPagerView;
import com.xiaoma.beiji.entity.CommentEntity;
import com.xiaoma.beiji.entity.FriendDynamicEntity;
import com.xiaoma.beiji.entity.PicEntity;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.CommUtil;
import com.xiaoma.beiji.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称： StoreDetailActivity
 * 类描述： 朋友动态详情
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年11月21 16:47
 * 修改备注：
 *
 * @version 1.0.0
 */
public class FriendDynamicDetailActivity extends SimpleBaseActivity implements View.OnClickListener {
    private static final String TAG = FriendDynamicDetailActivity.class.getSimpleName();

    public CircularImage headImage;
    public ImgPagerView imgPagerView;
    public TextView nameTextView;
    public TextView titleTextView;
    public TextView contentTextView;
    public RecyclerView mRecyclerView;
    public ImageView addComment;
    public TextView descriptionTextView;
    public ExpandListView mCommentRecyclerView;
    public ImageView showAllCommentBtn;
    public TextView likeLabel;
    public TextView commentLabel;
    public TextView shareUsers;

    private FriendDynamicEntity friendTrendsEntity;

    @Override
    protected String getActivityTitle() {
        return "点评详情";
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_friend_dynamic_detail;
    }

    @Override
    protected void initIntent() {
        super.initIntent();
        friendTrendsEntity = (FriendDynamicEntity) getIntent().getSerializableExtra("entity");
    }

    @Override
    protected void setTitleControlsInfo() {
        super.setTitleControlsInfo();
        ViewUtil.setViewVisibility(this, R.id.title_bar_right_img, View.VISIBLE);
        ImageViewUtil.setImageSrcId(this, R.id.title_bar_right_img, R.drawable.ic_share);
        ViewUtil.setViewOnClickListener(this,R.id.title_bar_right_layout,this);
    }

    @Override
    protected void initComponents() {
        setTitleControlsInfo();
        titleTextView = (TextView) findViewById(R.id.text_photo_title);
        contentTextView = (TextView) findViewById(R.id.text_photo_content);
        mRecyclerView = (RecyclerView) findViewById(R.id.item_recyclerView_lick);
        addComment = (ImageView) findViewById(R.id.item_btn_add_comment);
        descriptionTextView = (TextView)findViewById(R.id.text_description);
        nameTextView = (TextView) findViewById(R.id.text_item_name);
        headImage = (CircularImage) findViewById(R.id.img_head);
        imgPagerView = (ImgPagerView) findViewById(R.id.ipv_item_img);
        mCommentRecyclerView = (ExpandListView) findViewById(R.id.item_recyclerView_comment);
        showAllCommentBtn = (ImageView) findViewById(R.id.item_show_all_comment);
        likeLabel = (TextView) findViewById(R.id.item_text_label_like);
        commentLabel = (TextView) findViewById(R.id.item_text_label_comment);
        shareUsers = (TextView) findViewById(R.id.text_recommend_user);
        if(friendTrendsEntity != null){
            initView(friendTrendsEntity);
        }
    }

    private void initView(final FriendDynamicEntity entity){
        String avatar = entity.getAvatar();
        headImage.setImageResource(R.drawable.ic_logo);
        if(!TextUtils.isEmpty(avatar)){
            ImageLoader.getInstance().displayImage(entity.getAvatar(), headImage);
        }
        List<PicEntity> picLists = entity.getPic();
        List<String> picStrings = new ArrayList<>();
        if(picLists!=null){
            for (PicEntity picEntity:picLists) {
                picStrings.add(picEntity.getPicUrl());
            }
        }
        imgPagerView.notifyData(picStrings);
        titleTextView.setText(entity.getArea());
        contentTextView.setText(entity.getAssociated_price()+"RMB");
        nameTextView.setText(entity.getNickName());
        descriptionTextView.setText(entity.getDescription());
        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        manager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager1);
        List<UserInfoEntity> lickUser = entity.getPraiseUsers() != null ? entity.getPraiseUsers() : new ArrayList<UserInfoEntity>();
        mRecyclerView.setAdapter(new RecyclerView1Adapter(this,lickUser));
        likeLabel.setText(String.format("%d位已喜欢",lickUser.size()));

//        LinearLayoutManager manager2 = new LinearLayoutManager(mContext);
//        manager2.setOrientation(LinearLayoutManager.VERTICAL);
//        holder.mCommentRecyclerView.setLayoutManager(manager2);
        final List<CommentEntity> commentEntityList = entity.getComment()!= null ? entity.getComment() : new ArrayList<CommentEntity>();
        commentLabel.setText(String.format("%d条评论",commentEntityList.size()));
        if(commentEntityList.size()<=2){
            mCommentRecyclerView.setAdapter(new CommentAdapter(this, commentEntityList));
            showAllCommentBtn.setVisibility(View.GONE);
        }else{
            List<CommentEntity> commentEntities = new ArrayList<>(2);
            for(int i = 0; i<2; i++){
                commentEntities.add(commentEntityList.get(i));
            }
            mCommentRecyclerView.setAdapter(new CommentAdapter(this, commentEntities));
            showAllCommentBtn.setVisibility(View.VISIBLE);
            showAllCommentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCommentRecyclerView.setAdapter(new CommentAdapter(FriendDynamicDetailActivity.this, commentEntityList));
                    showAllCommentBtn.setVisibility(View.GONE);
                }
            });
        }
        List<String> shareUserLists = entity.getShare_user_nickname()!= null ? entity.getShare_user_nickname() : new ArrayList<String>();
        if(shareUserLists.size()>0){
            StringBuffer contentBuffer = new StringBuffer();
            contentBuffer.append("已有 ");
            for(int i= 0; i< shareUserLists.size(); i++){
                if(i== 0){
                    contentBuffer.append("@").append(shareUserLists.get(i));
                }else {
                    contentBuffer.append("、@").append(shareUserLists.get(i));
                }
            }
            contentBuffer.append(" 推荐");
            shareUsers.setText(contentBuffer);
            shareUsers.setVisibility(View.VISIBLE);
        }else {
//            holder.shareUsers.setVisibility(View.GONE);
            shareUsers.setText("暂无人推荐");
        }


    }

    @Override
    protected void loadData() {
//        HttpClientUtil.Dynamic.dynamicDetail(releaseId, new AbsHttpResultHandler<FriendDynamicEntity>() {
//            @Override
//            public void onSuccess(int resultCode, String desc, FriendDynamicEntity entity) {
//                if (entity != null) {
//                    friendTrendsEntity = entity;
//                    initView(entity);
//                }
//            }
//
//            @Override
//            public void onFailure(int resultCode, String desc) {
//                ToastUtil.showToast(FriendDynamicDetailActivity.this, "数据加载失败:" + desc);
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_collect:
//                if (!friendTrendsEntity.isHaveFavorite()) {
//                    imgA.setImageResource(R.drawable.ic_collected);
//                    AdapterViewUtil.startScale(this,view, txtA, llAction, imgA);
//                }
//                HttpClientUtil.Dynamic.dynamicDoFavorite(releaseId, friendTrendsEntity.isHaveFavorite() ? 2 : 1, new AbsHttpResultHandler() {
//                    @Override
//                    public void onSuccess(int resultCode, String desc, Object data) {
//                        friendTrendsEntity.setHaveFavorite(!friendTrendsEntity.isHaveFavorite());
//                        CommUtil.textViewSetImg(FriendDynamicDetailActivity.this, txtCollect, (friendTrendsEntity.isHaveFavorite() ? R.drawable.ic_collected : R.drawable.ic_uncollect));
//
//                        int anInt = DataUtil.getInt(friendTrendsEntity.getFavoriteNum());
//                        if (friendTrendsEntity.isHaveFavorite()) {
//                            ++ anInt;
//                        }else{
//                            -- anInt;
//                        }
//                        txtCollect.setText(anInt <= 0 ? "收藏":""+anInt);
//                        friendTrendsEntity.setFavoriteNum(""+anInt);
//                        Global.setIsNeedRefreshIndex(true);
//                    }
//
//                    @Override
//                    public void onFailure(int resultCode, String desc) {
//                        ToastUtil.showToast(FriendDynamicDetailActivity.this, "操作失败，请稍侯重试:" + desc);
//                    }
//                });
                break;
            case R.id.txt_like:
//                if (!friendTrendsEntity.isHavePraise()) {
//                    imgA.setImageResource(R.drawable.ic_liked);
//                    AdapterViewUtil.startScale(this,view, txtA, llAction, imgA);
//                }
//                HttpClientUtil.Dynamic.dynamicDoPraise(releaseId, friendTrendsEntity.isHavePraise() ? 2 : 1, new AbsHttpResultHandler() {
//                    @Override
//                    public void onSuccess(int resultCode, String desc, Object data) {
//                        friendTrendsEntity.setHavePraise(!friendTrendsEntity.isHavePraise());
//                        CommUtil.textViewSetImg(FriendDynamicDetailActivity.this, txtLike, (friendTrendsEntity.isHavePraise() ? R.drawable.ic_liked : R.drawable.ic_unlike));
//                        int anInt = DataUtil.getInt(friendTrendsEntity.getPraiseNum());
//                        if (friendTrendsEntity.isHavePraise()) {
//                            ++anInt;
//                        }else{
//                            --anInt;
//                        }
//                        txtLike.setText(anInt <= 0 ? "喜欢":""+anInt);
//                        friendTrendsEntity.setPraiseNum(""+anInt);
//                        Global.setIsNeedRefreshIndex(true);
//                    }
//
//                    @Override
//                    public void onFailure(int resultCode, String desc) {
//                        ToastUtil.showToast(FriendDynamicDetailActivity.this, "操作失败，请稍侯重试:" + desc);
//                    }
//                });
                break;
            case R.id.title_bar_right_layout:
                ShareSdkUtil.showShare(this,friendTrendsEntity.getReleaseId());
                break;
        }
    }

//    private void sendComment(String content) {
//        final CommentEntity entity = new CommentEntity();
//        entity.setNickname(Global.getUserInfo().getNickname());
//        entity.setAvatar(Global.getUserInfo().getAvatar());
//        entity.setCommentContent(content);
//        entity.setCreateTime(DateUtil.getStringDateTime());
//        entity.setCreateTimeTitle("刚刚");
//
//        String toUserId = "";
//        if(currentReplyEntity != null){
//            toUserId = currentReplyEntity.getUserId();
//            entity.setToUserId(toUserId);
//            entity.setToUserNickName(currentReplyEntity.getNickname());
//        }
//        currentReplyEntity = null;
//        inputCommentEditView.setHint("发布评论");
//        HttpClientUtil.Dynamic.dynamicDoComment(releaseId, toUserId, content, new AbsHttpResultHandler() {
//            @Override
//            public void onSuccess(int resultCode, String desc, Object data) {
//                commentEntities.add(0,entity);
//                listAdapter.notifyDataSetChanged();
//                lstComment.setVisibility(View.VISIBLE);
//                Global.setIsNeedRefreshIndex(true);
//            }
//
//            @Override
//            public void onFailure(int resultCode, String desc) {
//
//            }
//        });
//    }


}