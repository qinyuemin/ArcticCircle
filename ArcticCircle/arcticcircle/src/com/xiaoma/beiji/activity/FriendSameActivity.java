/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015 版权所有
 */
package com.xiaoma.beiji.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.makeapp.android.adapter.ArrayListAdapter;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.javase.lang.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.controls.view.CircularImage;
import com.xiaoma.beiji.entity.FriendEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.CommUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 类名称： FriendSameActivity
 * 类描述： 
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月09 23:58
 * 修改备注：
 * @version 1.0.0
 *
 */
public class FriendSameActivity extends SimpleBaseActivity {
    private static final String TAG = FriendSameActivity.class.getSimpleName();

    private String friendUserId;
    private ArrayListAdapter<FriendEntity> adapter;
    private List<FriendEntity> entities;

    private ListView lst;

    @Override
    protected String getActivityTitle() {
        return "共同好友";
    }

    @Override
    protected void initIntent() {
        super.initIntent();
        friendUserId = getIntent().getStringExtra("friendUserId");
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_friend_same;
    }

    @Override
    protected void initComponents() {
        setTitleControlsInfo();
        entities = new ArrayList<>();

        lst = (ListView) findViewById(R.id.lst_friend);
        adapter = new ArrayListAdapter<FriendEntity>(this, R.layout.ls_item_userinfo, entities) {
            @Override
            public void fillView(ViewGroup viewGroup, View view, FriendEntity entity, int i) {
                String avatar = entity.getAvatar();
                if (StringUtil.isValid(avatar)) {
                    CircularImage imgHead = (CircularImage) view.findViewById(R.id.img_head);
                    ImageLoader.getInstance().displayImage(avatar, imgHead);
                }

                TextViewUtil.setText(view, R.id.txt_name, entity.getNickname());
                TextViewUtil.setText(view, R.id.txt_tag_major, CommUtil.getFriendTag(entity.getGender(), entity.getV()));
                TextViewUtil.setText(view, R.id.txt_time, entity.getTitle());
            }
        };

        lst.setAdapter(adapter);
    }

    @Override
    protected void loadData() {
        HttpClientUtil.Friend.friendGetCommonFriends(friendUserId, new AbsHttpResultHandler<FriendEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, FriendEntity data) {

            }

            @Override
            public void onSuccess(int resultCode, String desc, List<FriendEntity> data) {
                entities.clear();
                entities.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int resultCode, String desc) {

            }
        });
    }
}