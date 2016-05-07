package com.xiaoma.beiji.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoma.beiji.R;
import com.xiaoma.beiji.adapter.RecyclerViewAdapter;
import com.xiaoma.beiji.entity.FriendDynamicEntity;
import com.xiaoma.beiji.entity.PicEntity;
import com.xiaoma.beiji.entity.Thing;
import com.xiaoma.beiji.network.HttpClientUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author       : zhangqibo
 * Date         : 2015-04-01
 * Time         : 15:09
 * Description  :
 */
public class InfoDetailsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<FriendDynamicEntity> dynamicEntities;
    private RecyclerViewAdapter adapter;

    private void initDate(){
        if(dynamicEntities == null){
            dynamicEntities = new ArrayList<>();
        }
    }

    public void setList(List list){
        this.dynamicEntities = list;
        if(mRecyclerView!=null){
            adapter = new RecyclerViewAdapter(getActivity(), dynamicEntities);
            mRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_layout_list, container, false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        adapter = new RecyclerViewAdapter(getActivity(), dynamicEntities);
        mRecyclerView.setAdapter(adapter);
        return mRecyclerView;
    }

}
