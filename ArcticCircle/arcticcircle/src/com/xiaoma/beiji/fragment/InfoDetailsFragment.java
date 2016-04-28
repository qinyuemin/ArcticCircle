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
 * Author       : yanbo
 * Date         : 2015-06-01
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
            for(int i=0; i<18; i++){
                FriendDynamicEntity thing = new FriendDynamicEntity();
                thing.setTitle("第" + i + "个");
                thing.setContent("这是第" + i + "个动态");
                List<PicEntity> picEntities = new ArrayList<>();
                PicEntity picEntity = new PicEntity();
                picEntity.setPicUrl("http://i6.265g.com/images/201501/201501301413233383.jpg");
                picEntities.add(picEntity);
                thing.setPic(picEntities);
                List<String> items = new ArrayList<>();
                for(int j=0; j<18; j++){
                    items.add("第" + j + "个朋友");
                }
                thing.setShare_user_nickname(items);
                dynamicEntities.add(thing);
            }
        }
    }

    public void setList(List list){
        this.dynamicEntities = list;
        if(adapter!=null){
            adapter.setData(list);
            adapter.notifyDataSetChanged();
            HttpClientUtil.logger("setList  and  notifyDataSetChanged");
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
