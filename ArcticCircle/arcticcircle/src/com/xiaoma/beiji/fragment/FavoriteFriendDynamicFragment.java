/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.common.android.lib.controls.view.pulltorefresh.PullToRefreshBase;
import com.common.android.lib.controls.view.pulltorefresh.PullToRefreshListView;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.adapter.FriendDynamicAdapter;
import com.xiaoma.beiji.base.SimpleFragment;
import com.xiaoma.beiji.entity.FriendDynamicEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.IntentUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 类名称： FavoriteFriendDynamicFragment
 * 类描述： 
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月04 0:10
 * 修改备注：
 * @version 1.0.0
 *
 */
public class FavoriteFriendDynamicFragment extends SimpleFragment implements AdapterView.OnItemClickListener {
    private PullToRefreshListView listView;
    private FriendDynamicAdapter adapter;
    private List<FriendDynamicEntity> entities;

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_friend_dynamic;
    }

    @Override
    protected void initComponents(View v) {
        listView = (PullToRefreshListView) v.findViewById(R.id.lst_friend);

        entities = new ArrayList<>();
        adapter = new FriendDynamicAdapter(getFragmentActivity(), entities);
        listView.setAdapter(adapter);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnItemClickListener(this);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData();
            }
        });
    }

    @Override
    protected void loadData() {
        HttpClientUtil.Dynamic.dynamicFavoriteList(1, "", "", new AbsHttpResultHandler<JSONObject>() {
            @Override
            public void onSuccess(int resultCode, String desc, JSONObject data) {
                if(data.containsKey("list")){
                    List<FriendDynamicEntity> entities1 = JSON.parseArray(data.getString("list"),FriendDynamicEntity.class);
                    if (data != null) {
                        entities.clear();
                        entities.addAll(entities1);
                        adapter.notifyDataSetChanged();
                    }
                }
                if (listView.isRefreshing()) {
                    listView.onRefreshComplete();
                }
            }

            @Override
            public void onSuccess(int resultCode, String desc, List<JSONObject> data) {

            }

            @Override
            public void onFailure(int resultCode, String desc) {
                if (listView.isRefreshing()) {
                    listView.onRefreshComplete();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        IntentUtil.goFriendDynamicDetailActivity(getFragmentActivity(), entities.get((int) l).getReleaseId());
        // todo
    }
}
