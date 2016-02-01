/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.activity;

import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.common.android.lib.controls.view.pulltorefresh.PullToRefreshBase;
import com.common.android.lib.controls.view.pulltorefresh.PullToRefreshScrollView;
import com.makeapp.android.adapter.ArrayListAdapter;
import com.makeapp.android.util.ImageViewUtil;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ViewUtil;
import com.makeapp.javase.lang.StringUtil;
import com.makeapp.javase.util.DateUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.adapter.CommentListAdapter;
import com.xiaoma.beiji.adapter.ImageGridAdapter;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.sharesdk.ShareSdkUtil;
import com.xiaoma.beiji.controls.view.InputCommentEditView;
import com.xiaoma.beiji.entity.CommentEntity;
import com.xiaoma.beiji.entity.FriendDynamicEntity;
import com.xiaoma.beiji.entity.PicEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.CommUtil;
import com.xiaoma.beiji.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称： FriendHelpDetailActivity
 * 类描述： 朋友 求助详情
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年11月24 23:55
 * 修改备注：
 *
 * @version 1.0.0
 */
public class FriendHelpDetailActivity extends SimpleBaseActivity implements View.OnClickListener {
    private static final String TAG = FriendHelpDetailActivity.class.getSimpleName();

    private static final int MAX_IMAGE_SIZE = 9;

    private ListView lstComment;
    private CommentListAdapter listAdapter;
    private List<CommentEntity> commentEntities;

    private GridView gvPic;
    private List<PicEntity> picEntities;
    private ImageGridAdapter gridAdapter;

    private String releaseId;

    private EditText edtContent;

    private FriendDynamicEntity friendDynamicEntity;

    private PullToRefreshScrollView svRoot;

    private CommentEntity currentReplyEntity = null;
    private InputCommentEditView inputCommentEditView;

    @Override
    protected String getActivityTitle() {
        return "朋友求助详情";
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_friend_help_detail;
    }

    @Override
    protected void setTitleControlsInfo() {
        super.setTitleControlsInfo();
        ViewUtil.setViewVisibility(this, R.id.title_bar_right_img, View.VISIBLE);
        ImageViewUtil.setImageSrcId(this, R.id.title_bar_right_img, R.drawable.ic_share);
        ViewUtil.setViewOnClickListener(this,R.id.title_bar_right_layout,this);
    }

    @Override
    protected void initIntent() {
        super.initIntent();
        releaseId = getIntent().getStringExtra("releaseId");
    }

    @Override
    protected void initComponents() {
        setTitleControlsInfo();

        svRoot = (PullToRefreshScrollView) findViewById(R.id.sv_root);
        svRoot.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        commentEntities = new ArrayList<>();

        lstComment = (ListView) findViewById(R.id.lst_comment);
        listAdapter = new CommentListAdapter(this,commentEntities);

        lstComment.setAdapter(listAdapter);

        gvPic = (GridView) findViewById(R.id.gv_pic);
        picEntities = new ArrayList<>();
        int screenWidth = CommUtil.getScreenWidth(this);
        int dip5 = CommUtil.dip2px(this.getApplicationContext(), 5);
        int imageSize = (screenWidth - (dip5 * 4)) / 3;
        gridAdapter = new ImageGridAdapter(this, picEntities, imageSize, MAX_IMAGE_SIZE);
        gvPic.setAdapter(gridAdapter);
        gvPic.setVisibility(View.GONE);


        inputCommentEditView = (InputCommentEditView) findViewById(R.id.icev_content);
        inputCommentEditView.setOnSendClickListener(new InputCommentEditView.OnSendClickListener() {
            @Override
            public void onSendMessage(String message) {
                sendComment(message);
            }
        });

        lstComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentReplyEntity = commentEntities.get(i);
                inputCommentEditView.setHint("回复"+currentReplyEntity.getNickname());
            }
        });
    }

    @Override
    protected void loadData() {
        HttpClientUtil.Dynamic.dynamicDetail(releaseId, new AbsHttpResultHandler<FriendDynamicEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, FriendDynamicEntity entity) {
                if (entity != null) {
                    friendDynamicEntity = entity;
                    View view = FriendHelpDetailActivity.this.findViewById(R.id.ll_root);
                    TextViewUtil.setText(view, R.id.txt_name, entity.getNickName());
                    TextViewUtil.setText(view, R.id.txt_tag_major, entity.getRelationalGrade());
                    TextViewUtil.setText(view, R.id.txt_address, entity.getArea());
                    TextViewUtil.setText(view, R.id.txt_time, entity.getCreateTime()); // todo 须根据与当前时间差计算 来显示 发布时间
                    TextViewUtil.setText(view, R.id.txt_description, entity.getContent());

                    notifyList(entity);
                }
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(FriendHelpDetailActivity.this, "数据加载失败:" + desc);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_bar_right_layout:
                ShareSdkUtil.showShare(this,releaseId);
                break;
        }
    }

    private void sendComment(String content) {
        final CommentEntity entity = new CommentEntity();
        entity.setNickname(Global.getUserInfo().getNickname());
        entity.setAvatar(Global.getUserInfo().getAvatar());
        entity.setCommentContent(content);
        entity.setCreateTime(DateUtil.getStringDateTime());
        entity.setCreateTimeTitle("刚刚");


        String toUserId = "";
        if(currentReplyEntity != null){
            toUserId = currentReplyEntity.getUserId();
            entity.setToUserId(toUserId);
            entity.setToUserNickName(currentReplyEntity.getNickname());
        }
        currentReplyEntity = null;
        inputCommentEditView.setHint("发布评论");

        HttpClientUtil.Dynamic.dynamicDoComment(releaseId, toUserId, content, new AbsHttpResultHandler() {
            @Override
            public void onSuccess(int resultCode, String desc, Object data) {
                commentEntities.add(0,entity);
                listAdapter.notifyDataSetChanged();
                lstComment.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int resultCode, String desc) {

            }
        });
    }

    private void notifyList(FriendDynamicEntity entity) {
        List<PicEntity> pic = entity.getPic();
        if (pic.size() > 0) {
            picEntities.clear();
            picEntities.addAll(pic);
            gridAdapter.notifyDataSetChanged();
            gvPic.setVisibility(View.VISIBLE);
        } else {
            gvPic.setVisibility(View.GONE);
        }

        List<CommentEntity> comment = entity.getComment();
        if (comment.size() > 0) {
            commentEntities.clear();
            commentEntities.addAll(comment);
            listAdapter.notifyDataSetChanged();
            lstComment.setVisibility(View.VISIBLE);
        } else {
            lstComment.setVisibility(View.GONE);
        }

    }
}