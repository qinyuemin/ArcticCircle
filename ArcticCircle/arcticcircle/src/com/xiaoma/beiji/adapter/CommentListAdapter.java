/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.adapter
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.javase.lang.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.entity.CommentEntity;
import com.xiaoma.beiji.util.IntentUtil;

import java.util.List;

/**
 *
 * 类名称： CommentListAdapter
 * 类描述： 评论列表
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月03 23:24
 * 修改备注：
 * @version 1.0.0
 *
 */
public class CommentListAdapter extends BaseAdapter {
    private Activity activity;
    private List<CommentEntity> entities;
    private LayoutInflater from;

    public CommentListAdapter(Activity activity, List<CommentEntity> entities) {
        this.activity = activity;
        this.entities = entities;
        from = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return entities.size();
    }

    @Override
    public CommentEntity getItem(int i) {
        return entities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = from.inflate(R.layout.lst_item_store_detail,null);
        final CommentEntity commentEntity = getItem(i);
        String avatar = commentEntity.getAvatar();
        ImageView imgHead = (ImageView) view.findViewById(R.id.img_head);
        if (StringUtil.isValid(avatar)) {
            ImageLoader.getInstance().displayImage(avatar, imgHead);
        }
        TextViewUtil.setText(view, R.id.txt_name, commentEntity.getNickname());
        TextViewUtil.setText(view, R.id.txt_time, commentEntity.getCreateTimeTitle());

        String content = commentEntity.getCommentContent();
        String toUserNickName = commentEntity.getToUserNickName();
        if(StringUtil.isValid(toUserNickName)){
            content = "回复"+toUserNickName +":"+commentEntity.getCommentContent();
        }
        TextViewUtil.setText(view, R.id.txt_comment, content);

        imgHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.goFriendDetailActivity(activity,commentEntity.getUserId());
            }
        });
        return view;
    }
}
