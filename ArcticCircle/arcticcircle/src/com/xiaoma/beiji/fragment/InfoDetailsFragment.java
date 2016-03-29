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
import com.xiaoma.beiji.entity.Thing;

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
    private List<Thing> things;

    private void initDate(){
        things = new ArrayList<>();
        for(int i=0; i<18; i++){
            Thing thing = new Thing();
            thing.setTitle("第" + i + "个");
            thing.setContent("这是第" + i + "个元素");
            thing.setImgUrl("http://i6.265g.com/images/201501/201501301413233383.jpg");
            List<String> items = new ArrayList<>();
            for(int j=0; j<18; j++){
                items.add("第" + j + "个子元素");
            }
            thing.setFreinds(items);
            things.add(thing);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initDate();
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_layout_list, container, false);
        return mRecyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        mRecyclerView.setAdapter(new RecyclerViewAdapter(getActivity(),things));
    }
}
