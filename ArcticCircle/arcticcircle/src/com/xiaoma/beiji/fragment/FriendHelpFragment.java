/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.fragment
 * 版本信息： 1.0.0
 * Copyright (c) 2015
 */
package com.xiaoma.beiji.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.common.android.lib.controls.view.pulltorefresh.PullToRefreshBase;
import com.common.android.lib.controls.view.pulltorefresh.PullToRefreshListView;
import com.makeapp.javase.lang.StringUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.adapter.FriendDynamicAdapter;
import com.xiaoma.beiji.adapter.RecyclerViewAdapter;
import com.xiaoma.beiji.base.SimpleFragment;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.entity.FriendDynamicEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.IntentUtil;
import com.xiaoma.beiji.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称： FriendTrendsFragment
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年11月21 13:58
 * 修改备注：
 *
 * @version 1.0.0
 */
public class FriendHelpFragment extends SimpleFragment{
    private RecyclerView lstFriend;
    private RecyclerViewAdapter adapter;
    private List<FriendDynamicEntity> entities;

    private String lastKeyId = "";

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_friend_help;
    }

    @Override
    protected void initComponents(View v) {
        lstFriend = (RecyclerView) v.findViewById(R.id.dynamic_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lstFriend.setLayoutManager(layoutManager);
        entities = new ArrayList<>();
        adapter = new RecyclerViewAdapter(getFragmentActivity(), entities);
        lstFriend.setAdapter(adapter);
//        lstFriend.setMode(PullToRefreshBase.Mode.BOTH);
//        lstFriend.setOnItemClickListener(this);
//        lstFriend.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                // 下拉 刷新
//                lastKeyId = "";
//                loadData();
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                // 上啦更多
//                FriendDynamicEntity last = entities.get(entities.size() - 1);
//                lastKeyId = last.getPageId();
//                loadData();
//            }
//        });

//        releaseUserId = getArguments().getString("releaseUserId");
    }

    @Override
    protected void loadData() {
        HttpClientUtil.Dynamic.dynamicGetList("", 2, "", lastKeyId, new AbsHttpResultHandler<FriendDynamicEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, FriendDynamicEntity data) {

            }

            @Override
            public void onSuccess(int resultCode, String desc, List<FriendDynamicEntity> data) {
                if (data != null) {
                    if (StringUtil.isInvalid(lastKeyId)) {
                        entities.clear();
                    }
                    entities.addAll(data);
                    adapter.notifyDataSetChanged();
                }

//                if (lstFriend.isRefreshing()) {
//                    lstFriend.onRefreshComplete();
//                }
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(getFragmentActivity(), desc);
//                if (lstFriend.isRefreshing()) {
//                    lstFriend.onRefreshComplete();
//                }
            }
        });
    }

    boolean isFirst = true;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            if(Global.isNeedRefreshIndex()){
                loadData();
                Global.setIsNeedRefreshIndex(false);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Global.isNeedRefreshIndex()){
            loadData();
            Global.setIsNeedRefreshIndex(false);
        }
    }
}
