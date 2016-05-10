package com.xiaoma.beiji.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeapp.android.util.ImageViewUtil;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.javase.lang.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.adapter.FragmentAdapter;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.view.CircularImage;
import com.xiaoma.beiji.controls.view.MyTabLayoutItem;
import com.xiaoma.beiji.entity.FriendDynamicEntity;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.IntentUtil;
import com.xiaoma.beiji.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangqibo on 2016/3/29.
 */
public class MyProfileFragment extends Fragment{
    private static final String TAG = MyProfileFragment.class.getSimpleName();

    private UserInfoEntity userInfoEntity;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private InfoDetailsFragment dynamicFragment;
    private InfoDetailsFragment seekHelpFragment;
    private InfoDetailsFragment shouCangFragment;

    private TextView leftLabel;
    private TextView rightLabel;

    private CircularImage headView;

    private View rootView;

    MyTabLayoutItem[] tabs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_layout_profile, null);
        userInfoEntity = Global.getUserInfo();
        initComponents(rootView);
        loadMyDynamic();
        return  rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initInfo();
    }

    protected void initComponents(View rootView) {
        View permissionSetting = rootView.findViewById(R.id.layout_permission_setting);
        permissionSetting.setVisibility(View.GONE);
        View commomFriends = rootView.findViewById(R.id.layout_commom_friends);
        commomFriends.setVisibility(View.GONE);
        headView = (CircularImage) rootView.findViewById(R.id.img_user_head);
        leftLabel = (TextView) rootView.findViewById(R.id.text_left_label);
        rightLabel = (TextView) rootView.findViewById(R.id.text_right_label);
        leftLabel.setCompoundDrawables(null, null, null, null);
        rightLabel.setCompoundDrawables(null, null, null, null);
        leftLabel.setText("我关注的人");
        rightLabel.setText("关注我的人");
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.goHeadPhotoEnlargeActivity(getActivity());
            }
        });

        rootView.findViewById(R.id.btn_account_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.goAccountSettingActivity(getActivity());
            }
        });
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
        titles.add("动态");
        titles.add("求助");
        titles.add("收藏");
        //初始化TabLayout的title
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(2)));
        //初始化ViewPager的数据集
        List<Fragment> fragments = new ArrayList<>();
        dynamicFragment = new InfoDetailsFragment();
        seekHelpFragment = new InfoDetailsFragment();
        shouCangFragment = new InfoDetailsFragment();
        fragments.add(dynamicFragment);
        fragments.add(seekHelpFragment);
        fragments.add(shouCangFragment);
        //创建ViewPager的adapter
        FragmentAdapter adapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
//        mTabLayout.setTabsFromPagerAdapter(adapter);
        rootView.findViewById(R.id.layout_other_users).setVisibility(View.GONE);

        tabs = new MyTabLayoutItem[mTabLayout.getTabCount()];

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tabs[i] = new MyTabLayoutItem(getContext());
            tab.setCustomView(tabs[i].getTabView("0",titles.get(i)));
        }
        tabs[0].setSelected(true);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                    MyTabLayoutItem item = tabs[i];
                    if(position == i){
                        item.setSelected(true);
                    }else{
                        item.setSelected(false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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

    private void bindDataToView(){
        initInfo();
    }

    private void loadMyDynamic(){
        HttpClientUtil.User.userHomeDynamic(1,new AbsHttpResultHandler<UserInfoEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, UserInfoEntity data) {
                    userInfoEntity = data;
                    bindDataToView();
                    dynamicFragment.setList(data.getFriendDynamicEntities());
                    tabs[0].setmCount(data.getFriendDynamicEntities().size()+"");
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(getContext(),desc);
            }
        });
        HttpClientUtil.User.userHomeDynamic(2, new AbsHttpResultHandler<UserInfoEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, UserInfoEntity data) {
                userInfoEntity = data;
                bindDataToView();
                seekHelpFragment.setList(data.getFriendDynamicEntities());
                tabs[1].setmCount(data.getFriendDynamicEntities().size() + "");
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(getContext(),desc);
            }
        });

        HttpClientUtil.User.homeFavoriteDynamic(1, 1, new AbsHttpResultHandler<UserInfoEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, UserInfoEntity data) {
                userInfoEntity = data;
                bindDataToView();
                List<FriendDynamicEntity> favoriteList = data.getFriendFavoriteEntities();
                if (favoriteList == null) {
                    favoriteList = new ArrayList<FriendDynamicEntity>();
                }
                shouCangFragment.setList(favoriteList);
                tabs[2].setmCount(favoriteList.size() + "");
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(getContext(), desc);
            }
        });
    }
    private void initInfo() {
        TextViewUtil.setText(rootView, R.id.text_user_name, userInfoEntity.getNickname());
        TextViewUtil.setText(rootView, R.id.text_user_id, "北极圈号:" + userInfoEntity.getUserId());
        if(StringUtil.isValid(userInfoEntity.getAvatar())){
            ImageLoader.getInstance().displayImage(userInfoEntity.getAvatar(), headView);
        }
        TextViewUtil.setText(rootView,R.id.text_user_address,userInfoEntity.getAddress());
        TextViewUtil.setText(rootView,R.id.text_uesr_label,userInfoEntity.getLabel());
        TextViewUtil.setText(rootView, R.id.text_uesr_label_all, userInfoEntity.getProfile());
    }

    private void attention(){

    }

}
