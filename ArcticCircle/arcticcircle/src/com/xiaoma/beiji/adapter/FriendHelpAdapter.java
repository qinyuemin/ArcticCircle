/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.adapter
 * 版本信息： 1.0.0
 * Copyright (c) 2015
 */
package com.xiaoma.beiji.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ViewUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.entity.CommentEntity;
import com.xiaoma.beiji.entity.FriendDynamicEntity;
import com.xiaoma.beiji.entity.PicEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.CommUtil;
import com.xiaoma.beiji.util.IntentUtil;
import com.xiaoma.beiji.util.ToastUtil;

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
public class FriendHelpAdapter extends BaseAdapter {
    private final int MAX_IMAGE_SIZE = 9;
    private Activity activity;
    private List<FriendDynamicEntity> entities;
    private LayoutInflater layoutInflater;

    public FriendHelpAdapter(Activity activity, List<FriendDynamicEntity> entities) {
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        View contentView = null;
        final FriendDynamicEntity entity = getItem(i);

        if (entity.getReleaseType() == 2) {
            // 店铺动态
            contentView = layoutInflater.inflate(R.layout.lst_item_friend_help, null);
            AdapterViewUtil.initUserInfoView(activity, contentView, entity);
            TextViewUtil.setText(contentView, R.id.txt_description, entity.getContent());

            ImageGridAdapter.initPicGridView(activity, (GridView) contentView.findViewById(R.id.gv_pic),entity);

            AdapterViewUtil.initCommentView(contentView, entity);

            ViewUtil.setViewOnClickListener(contentView, R.id.txt_all_comment, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });

            contentView.findViewById(R.id.rl_info).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IntentUtil.goFriendDetailActivity(activity, entity.getUserId());
                }
            });
        }
        return contentView;
    }

    class Holder {
        public ImageView imgHead;
        public TextView txtName;
    }
}
