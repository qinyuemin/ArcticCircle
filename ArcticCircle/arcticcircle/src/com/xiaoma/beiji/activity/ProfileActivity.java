package com.xiaoma.beiji.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.makeapp.android.util.TextViewUtil;
import com.makeapp.javase.lang.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.adapter.FragmentAdapter;
import com.xiaoma.beiji.controls.view.CircularImage;
import com.xiaoma.beiji.controls.view.MyTabLayoutItem;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.fragment.InfoDetailsFragment;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangqibo on 2016/5/6.
 */
public class ProfileActivity extends FragmentActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private InfoDetailsFragment dynamicFragment;
    private InfoDetailsFragment favoriteFragment;

    private CircularImage headView;
    private UserInfoEntity userInfoEntity;

    MyTabLayoutItem[] tabs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_layout_profile);
        initComponents();
        int id = getIntent().getIntExtra("user_id",-1);
        if(id<0){
            finish();
        }else{
            loadDynamic(id);
            loadFavorite(id);
        }
    }

    protected void initComponents() {
        View permissionSetting = findViewById(R.id.layout_permission_setting);
        permissionSetting.setVisibility(View.VISIBLE);
        View commomFriends = findViewById(R.id.layout_commom_friends);
        commomFriends.setVisibility(View.VISIBLE);
        findViewById(R.id.btn_account_setting).setVisibility(View.GONE);
        headView = (CircularImage) findViewById(R.id.img_user_head);
//        leftLabel = (TextView) rootView.findViewById(R.id.text_left_label);
//        rightLabel = (TextView) rootView.findViewById(R.id.text_right_label);
//        leftLabel.setCompoundDrawables(null,null,null,null);
//        rightLabel.setCompoundDrawables(null,null,null,null);
//        leftLabel.setText("我关注的人");
//        rightLabel.setText("关注我的人");
//        rootView.findViewById(R.id.btn_account_setting).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                IntentUtil.goAccountSettingActivity(getActivity());
//            }
//        });
        final LinearLayout settingLayout = (LinearLayout) findViewById(R.id.layout_setting);
        ImageView hideSettingBtn = (ImageView) findViewById(R.id.btn_hide_setting);
        hideSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(View.VISIBLE == settingLayout.getVisibility()){
                    settingLayout.setVisibility(View.GONE);
                }else{
                    settingLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
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
        favoriteFragment = new InfoDetailsFragment();
        fragments.add(dynamicFragment);
        fragments.add(favoriteFragment);
        //创建ViewPager的adapter
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
//        mTabLayout.setTabsFromPagerAdapter(adapter);

        tabs = new MyTabLayoutItem[mTabLayout.getTabCount()];

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tabs[i] = new MyTabLayoutItem(this);
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

    private void loadDynamic(int friendId){

        HttpClientUtil.User.friendHomeDynamic(1, friendId, new AbsHttpResultHandler<UserInfoEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, UserInfoEntity data) {
                userInfoEntity = data;
                initInfo();
                dynamicFragment.setList(data.getFriendDynamicEntities());
                tabs[0].setmCount(data.getFriendDynamicEntities().size() + "");
            }

            @Override
            public void onFailure(int resultCode, String desc) {

            }
        });
    }

    private void loadFavorite(int friendId){

        HttpClientUtil.User.friendFavoriteDynamic(1, 1, friendId, new AbsHttpResultHandler<UserInfoEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, UserInfoEntity data) {
                userInfoEntity = data;
                initInfo();
                favoriteFragment.setList(data.getFriendFavoriteEntities());
                tabs[1].setmCount(data.getFriendDynamicEntities().size() + "");
            }

            @Override
            public void onFailure(int resultCode, String desc) {

            }
        });
    }
    private void initInfo() {
        TextViewUtil.setText(this, R.id.text_user_name, userInfoEntity.getNickname());
        TextViewUtil.setText(this, R.id.text_user_id, "北极圈号:" + userInfoEntity.getUserId());
        if(StringUtil.isValid(userInfoEntity.getAvatar())){
            ImageLoader.getInstance().displayImage(userInfoEntity.getAvatar(), headView);
        }
    }
}
