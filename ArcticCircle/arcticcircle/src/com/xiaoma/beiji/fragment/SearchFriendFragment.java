package com.xiaoma.beiji.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoma.beiji.R;

/**
 * Created by zhangqibo on 2016/4/12.
 */
public class SearchFriendFragment extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_layout_profile,null);
        initComponents(rootView);
        return  rootView;
    }

    private void initComponents(View rootView) {

    }

}
