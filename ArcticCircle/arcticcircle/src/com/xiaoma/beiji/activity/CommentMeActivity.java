/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.makeapp.android.adapter.ArrayListAdapter;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ViewUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.entity.CommentEntity;
import com.xiaoma.beiji.entity.FriendEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 类名称： CommentMeActivity
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月20 18:10
 * 修改备注：
 * @version 1.0.0
 *
 */
public class CommentMeActivity extends SimpleBaseActivity implements View.OnClickListener {
    private static final String TAG = CommentMeActivity.class.getSimpleName();

    private int currentPosition = 0;
    private View line1, line2;

    private ListView lstComment1, lstComment2;
    private ArrayListAdapter<CommentEntity> adapter1;
    private ArrayListAdapter<CommentEntity> adapter2;

    private List<CommentEntity> entities1;
    private List<CommentEntity> entities2;

    @Override
    protected String getActivityTitle() {
        return "我的评论/回复";
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_comment_me;
    }

    @Override
    protected void initComponents() {
        setTitleControlsInfo();

        entities1 = new ArrayList<>();
        entities2 = new ArrayList<>();

        ViewUtil.setViewOnClickListener(this, R.id.ll_comment1, this);
        ViewUtil.setViewOnClickListener(this, R.id.ll_comment2, this);
        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);

        lstComment1 = (ListView) findViewById(R.id.lst_comment1);
        lstComment2 = (ListView) findViewById(R.id.lst_comment2);

        adapter1 = new ArrayListAdapter<CommentEntity>(this,R.layout.lst_item_me_comment,entities1) {
            @Override
            public void fillView(ViewGroup viewGroup, View view, CommentEntity commentEntity, int i) {
                TextViewUtil.setText(view,R.id.txt_time,commentEntity.getCreateTimeTitle());
                TextViewUtil.setText(view,R.id.txt_tip,commentEntity.getComment_title());
                TextViewUtil.setText(view,R.id.txt_content,commentEntity.getCommentContent());
            }
        };

        adapter2 = new ArrayListAdapter<CommentEntity>(this,R.layout.lst_item_me_comment,entities2) {
            @Override
            public void fillView(ViewGroup viewGroup, View view, CommentEntity commentEntity, int i) {
                TextViewUtil.setText(view,R.id.txt_time,commentEntity.getCreateTimeTitle());
                TextViewUtil.setText(view,R.id.txt_tip,commentEntity.getComment_title());
                TextViewUtil.setText(view,R.id.txt_content,commentEntity.getCommentContent());
            }
        };

        lstComment1.setAdapter(adapter1);
        lstComment2.setAdapter(adapter2);
        setTabColor(0);
    }

    @Override
    protected void loadData() {
        HttpClientUtil.User.userGetDynamicComments("", new AbsHttpResultHandler<JSONObject>() {
            @Override
            public void onSuccess(int resultCode, String desc, JSONObject data) {
                if(data.containsKey("list")){
                    List<CommentEntity> entities = JSON.parseArray(data.getString("list"),CommentEntity.class);
                    entities1.clear();
                    entities1.addAll(entities);
                    adapter1.notifyDataSetChanged();
                }
            }


            @Override
            public void onFailure(int resultCode, String desc) {

            }
        });

        HttpClientUtil.User.userGetShopComments("", new AbsHttpResultHandler<JSONObject>() {
            @Override
            public void onSuccess(int resultCode, String desc, JSONObject data) {
                if(data.containsKey("list")){
                    List<CommentEntity> entities = JSON.parseArray(data.getString("list"),CommentEntity.class);
                    entities2.clear();
                    entities2.addAll(entities);
                    adapter2.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int resultCode, String desc) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_comment1:
                setTabColor(0);
                break;
            case R.id.ll_comment2:
                setTabColor(1);
                break;
        }
    }

    private void setTabColor(int position) {
        currentPosition = position;
        switch (position) {
            case 0:
                line1.setBackgroundResource(R.color.color_blue);
                line2.setBackgroundResource(R.color.white);

                lstComment1.setVisibility(View.VISIBLE);
                lstComment2.setVisibility(View.GONE);
                break;
            case 1:
                line1.setBackgroundResource(R.color.white);
                line2.setBackgroundResource(R.color.color_blue);

                lstComment1.setVisibility(View.GONE);
                lstComment2.setVisibility(View.VISIBLE);
                break;
        }
    }

}