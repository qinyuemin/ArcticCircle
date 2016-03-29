package com.xiaoma.beiji.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.makeapp.android.util.ImageViewUtil;
import com.makeapp.android.util.TextViewUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.adapter.FragmentAdapter;
import com.xiaoma.beiji.adapter.RecyclerView1Adapter;
import com.xiaoma.beiji.base.SimpleFragment;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.util.CommUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangqibo on 2016/3/29.
 */
public class MyProfileFragment extends Fragment{
    private static final String TAG = MyProfileFragment.class.getSimpleName();
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private RecyclerView recyclerView;
    private ImageButton hideSettingBtn;
    private LinearLayout settingLayout;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_layout_profile,null);
        initComponents(rootView);
        initInfo(Global.getUserInfo());
        return  rootView;
    }


    protected void initComponents(View rootView) {
        View permissionSetting = rootView.findViewById(R.id.layout_permission_setting);
        permissionSetting.setVisibility(View.GONE);
        View commomFriends = rootView.findViewById(R.id.layout_commom_friends);
        commomFriends.setVisibility(View.GONE);
//        settingLayout = (LinearLayout) rootView.findViewById(R.id.layout_setting);
//        hideSettingBtn = (ImageButton) rootView.findViewById(R.id.btn_hide_setting);
//        hideSettingBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(View.VISIBLE == settingLayout.getVisibility()){
//                    settingLayout.setVisibility(View.GONE);
//                }else{
//                    settingLayout.setVisibility(View.VISIBLE);
//                }
//            }
//        });
        mTabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        //初始化TabLayout的title数据集
        List<String> titles = new ArrayList<>();
        titles.add("details");
        titles.add("share");
        titles.add("agenda");
        //初始化TabLayout的title
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(2)));
        //初始化ViewPager的数据集
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new InfoDetailsFragment());
        fragments.add(new InfoDetailsFragment());
        fragments.add(new InfoDetailsFragment());
        //创建ViewPager的adapter
        FragmentAdapter adapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(adapter);

//        recyclerView = (RecyclerView) rootView.findViewById(R.id.freinds_recycler_view);
//        LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recyclerView.setLayoutManager(manager);
//        List<String> items = new ArrayList<>();
//        for(int j=0; j<18; j++){
//            items.add("第" + j + "个子元素");
//        }
//        recyclerView.setAdapter(new RecyclerView1Adapter(getContext(),items));
    }

    private void initInfo(UserInfoEntity entity) {
        TextViewUtil.setText(rootView, R.id.text_user_name, entity.getNickname());
        TextViewUtil.setText(rootView, R.id.text_user_id, "北极圈号:" + entity.getUserId());
        ImageViewUtil.setImageSrcUrl(rootView,R.id.img_user_head,entity.getAvatar());
    }
}
