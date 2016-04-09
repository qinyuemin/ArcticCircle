/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.fragment
 * 版本信息： 1.0.0
 * Copyright (c) 2015
 */
package com.xiaoma.beiji.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.common.android.lib.controls.view.pulltorefresh.PullToRefreshBase;
import com.common.android.lib.controls.view.pulltorefresh.PullToRefreshListView;
import com.makeapp.javase.lang.StringUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.adapter.FriendDynamicAdapter;
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
public class FriendDynamicFragment extends SimpleFragment implements AdapterView.OnItemClickListener {
    private PullToRefreshListView lstFriend;
    private FriendDynamicAdapter adapter;
    private List<FriendDynamicEntity> entities;
    private String releaseUserId = "";

    private String lastKeyId = "";

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_friend_dynamic;
    }

    @Override
    protected void initComponents(View v) {
        lstFriend = (PullToRefreshListView) v.findViewById(R.id.lst_friend);

        entities = new ArrayList<>();
        adapter = new FriendDynamicAdapter(getFragmentActivity(), entities);
        lstFriend.setAdapter(adapter);
        lstFriend.setMode(PullToRefreshBase.Mode.BOTH);
        lstFriend.setOnItemClickListener(this);
        lstFriend.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                // 下拉 刷新
                lastKeyId = "";
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                // 上啦更多
                FriendDynamicEntity last = entities.get(entities.size() - 1);
                lastKeyId = last.getPageId();
                loadData();
            }
        });

//        releaseUserId = getArguments().getString("releaseUserId");
    }

    @Override
    protected void loadData() {
        HttpClientUtil.Dynamic.dynamicGetList(releaseUserId, -1, "", lastKeyId, new AbsHttpResultHandler<FriendDynamicEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, FriendDynamicEntity data) {

            }

            @Override
            public void onSuccess(int resultCode, String desc, List<FriendDynamicEntity> data) {
                if (data != null) {
                    if(StringUtil.isInvalid(lastKeyId)){
                        entities.clear();
                    }
                    entities.addAll(data);
                    adapter.notifyDataSetChanged();
                }

                if (lstFriend.isRefreshing()) {
                    lstFriend.onRefreshComplete();
                }
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(getFragmentActivity(),desc);
                if (lstFriend.isRefreshing()) {
                    lstFriend.onRefreshComplete();
                }
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        FriendDynamicEntity dynamicEntity = entities.get((int) l);
        int releaseType = dynamicEntity.getReleaseType();
        if(releaseType == 3){
            IntentUtil.goShopMainActivity(getFragmentActivity(), dynamicEntity.getShopId());
        }else if(releaseType == 4){
            IntentUtil.goShopMainActivity(getFragmentActivity(), dynamicEntity.getShopId());
        } if(releaseType == 5){
            IntentUtil.goFriendDetailActivity(getFragmentActivity(), dynamicEntity.getUserId());
        }else {
            IntentUtil.goFriendDynamicDetailActivity(getFragmentActivity(), dynamicEntity.getReleaseId());

        }
    }
}
