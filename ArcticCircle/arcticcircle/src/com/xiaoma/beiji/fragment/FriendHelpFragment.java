/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.fragment
 * 版本信息： 1.0.0
 * Copyright (c) 2015
 */
package com.xiaoma.beiji.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.common.android.lib.controls.view.pulltorefresh.PullToRefreshBase;
import com.common.android.lib.controls.view.pulltorefresh.PullToRefreshListView;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.adapter.FriendHelpAdapter;
import com.xiaoma.beiji.base.SimpleFragment;
import com.xiaoma.beiji.entity.FriendDynamicEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.IntentUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称： FriendHelpFragment
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年11月21 13:59
 * 修改备注：
 *
 * @version 1.0.0
 */
public class FriendHelpFragment extends SimpleFragment implements AdapterView.OnItemClickListener {

    private PullToRefreshListView lstHelp;
    private FriendHelpAdapter adapter;
    private List<FriendDynamicEntity> entities;
    private String releaseUserId = "";

    public static FriendHelpFragment newInstance(Bundle args) {
        FriendHelpFragment conversationFragment = new FriendHelpFragment();
        conversationFragment.setArguments(args);
        return conversationFragment;
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_friend_help;
    }

    @Override
    protected void initComponents(View v) {
        lstHelp = (PullToRefreshListView) v.findViewById(R.id.lst_help);

        entities = new ArrayList<>();
        adapter = new FriendHelpAdapter(getFragmentActivity(), entities);
        lstHelp.setAdapter(adapter);
        lstHelp.setMode(PullToRefreshBase.Mode.BOTH);
        lstHelp.setOnItemClickListener(this);
        lstHelp.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData();
            }
        });

        releaseUserId = getArguments().getString("releaseUserId");
    }

    @Override
    protected void loadData() {
        HttpClientUtil.Dynamic.dynamicGetList(releaseUserId,2, "", "", new AbsHttpResultHandler<FriendDynamicEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, FriendDynamicEntity data) {

            }

            @Override
            public void onSuccess(int resultCode, String desc, List<FriendDynamicEntity> data) {
                if (data != null) {
                    // todo 测试用 添加几张图片
//                    FriendTrendsEntity friendTrendsEntity = data.get(0);
//                    friendTrendsEntity.getPic().add("");
//                    friendTrendsEntity.getPic().add("");
//                    friendTrendsEntity.getPic().add("");
//                    friendTrendsEntity.getPic().add("");
//                    CommentEntity commentEntity = new CommentEntity();
//                    commentEntity.setNickname("周董");
//                    commentEntity.setContent("你确定你真的不来听我的演唱会？");
//                    friendTrendsEntity.getComment().add(commentEntity);
//                    commentEntity = new CommentEntity();
//                    commentEntity.setNickname("老汉");
//                    commentEntity.setContent("你怎么这么叼呢");
//                    friendTrendsEntity.getComment().add(commentEntity);
                    entities.clear();
                    entities.addAll(data);
                    adapter.notifyDataSetChanged();
                }

                if (lstHelp.isRefreshing()) {
                    lstHelp.onRefreshComplete();
                }
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                if (lstHelp.isRefreshing()) {
                    lstHelp.onRefreshComplete();
                }
            }
        });
    }

    boolean isFirst = true;
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            if (!isFirst) {
                loadData();
            }
            isFirst = false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        IntentUtil.goFriendHelpDetailActivity(getFragmentActivity(), entities.get((int) l).getReleaseId()); // todo
    }
}
