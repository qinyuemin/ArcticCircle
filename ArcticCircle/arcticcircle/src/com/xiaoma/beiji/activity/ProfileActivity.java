package com.xiaoma.beiji.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.javase.lang.StringUtil;
import com.makeapp.javase.util.DataUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.adapter.FragmentAdapter;
import com.xiaoma.beiji.adapter.RecyclerView1Adapter;
import com.xiaoma.beiji.controls.view.CircularImage;
import com.xiaoma.beiji.controls.view.MyTabLayoutItem;
import com.xiaoma.beiji.entity.CommonFriends;
import com.xiaoma.beiji.entity.FriendDynamicEntity;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.fragment.FriendDynamicListFragment;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.IntentUtil;
import com.xiaoma.beiji.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangqibo on 2016/5/6.
 */
public class ProfileActivity extends FragmentActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private FriendDynamicListFragment dynamicFragment;
    private FriendDynamicListFragment favoriteFragment;

    private List<FriendDynamicEntity> dynamicEntities = new ArrayList<>();
    private List<FriendDynamicEntity> favoriteEntities =  new ArrayList<>();

    private CircularImage headView;
    private UserInfoEntity userInfoEntity;

    private TextView commonFriendsLabel;

    private RecyclerView commonFriendsRecyclerView;

    private TextView leftLabel,rightLabel;
    private Switch hidemeSwitch;
    private Switch hidemeToHeFriendsSwitch;
    private Switch addBlackSwitch;

    MyTabLayoutItem[] tabs;
    private int friendId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_layout_profile);
        initComponents();
        friendId = getIntent().getIntExtra("user_id",-1);
        if(friendId<0){
            finish();
        }else{
            loadDynamic(friendId);
            loadFavorite(friendId);
        }
    }

    protected void initComponents() {
        View permissionSetting = findViewById(R.id.layout_permission_setting);
        permissionSetting.setVisibility(View.VISIBLE);
        View commomFriends = findViewById(R.id.layout_commom_friends);
        commomFriends.setVisibility(View.VISIBLE);
        findViewById(R.id.btn_account_setting).setVisibility(View.GONE);
        headView = (CircularImage) findViewById(R.id.img_user_head);
        commonFriendsLabel = (TextView) findViewById(R.id.text_commfriend_label);
        hidemeSwitch = (Switch) findViewById(R.id.switch_hide_me_to_he);
        hidemeToHeFriendsSwitch = (Switch) findViewById(R.id.switch_hide_me_to_his_friend);
        addBlackSwitch = (Switch) findViewById(R.id.switch_add_to_black);
        commonFriendsRecyclerView = (RecyclerView) findViewById(R.id.freinds_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        commonFriendsRecyclerView.setLayoutManager(layoutManager);
//        commonFriendsRecyclerView.setAdapter(new RecyclerView1Adapter(this,new ArrayList<UserInfoEntity>()));
        leftLabel = (TextView) findViewById(R.id.text_left_label);
        rightLabel = (TextView) findViewById(R.id.text_right_label);
        leftLabel.setOnClickListener(this);
        rightLabel.setOnClickListener(this);
        hidemeSwitch = (Switch) findViewById(R.id.switch_hide_me_to_he);
        hidemeToHeFriendsSwitch = (Switch) findViewById(R.id.switch_hide_me_to_his_friend);
        addBlackSwitch = (Switch) findViewById(R.id.switch_add_to_black);

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
        final ImageView hideSettingBtn = (ImageView) findViewById(R.id.btn_hide_setting);
        hideSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(View.VISIBLE == settingLayout.getVisibility()){
                    settingLayout.setVisibility(View.GONE);
                    hideSettingBtn.setImageResource(R.drawable.icon_open);
                }else{
                    settingLayout.setVisibility(View.VISIBLE);
                    hideSettingBtn.setImageResource(R.drawable.icon_hide);
                }
            }
        });
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        //初始化TabLayout的title数据集
        List<String> titles = new ArrayList<>();
        titles.add("动态");
        titles.add("收藏");
        //初始化TabLayout的title
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
        //初始化ViewPager的数据集
        List<Fragment> fragments = new ArrayList<>();
        dynamicFragment = new FriendDynamicListFragment();
        favoriteFragment = new FriendDynamicListFragment();
        fragments.add(dynamicFragment);
        fragments.add(favoriteFragment);
        //创建ViewPager的adapter
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(adapter);

        tabs = new MyTabLayoutItem[titles.size()];

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tabs[i] = new MyTabLayoutItem(this);
            Log.d("AAAAAAAAAAA","i = " +i);
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
                    if (position == i) {
                        item.setSelected(true);
                    } else {
                        item.setSelected(false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_back).setOnClickListener(this);

//        recyclerView = (RecyclerView) rootView.findViewById(R.id.freinds_recycler_view);
//        LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recyclerView.setLayoutManager(manager);
//        List<String> items = new ArrayList<>();
//        for(int j=0; j<18; j++){
//            items.add("第" + j + "个子元素");
//        }
//        recyclerView.setAdapter(new RecyclerView1Adapter(getContext(),items));
        dynamicFragment.setPtrHandlerListener(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                loadDynamic(friendId);
            }

        });
        dynamicFragment.setPtrLoadMore(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                loadDynamicMore(friendId);
            }
        });

        favoriteFragment.setPtrHandlerListener(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                loadFavorite(friendId);
            }

        });
        favoriteFragment.setPtrLoadMore(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                loadFavoriteMore(friendId);
            }
        });
    }

    private void loadDynamic(int friendId){
        HttpClientUtil.User.friendHomeDynamic(1, friendId, "",new AbsHttpResultHandler<UserInfoEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, UserInfoEntity data) {
                userInfoEntity = data;
                dynamicEntities.clear();
                initInfo();
                dynamicEntities.addAll(data.getFriendDynamicEntities());
                tabs[0].setmCount(data.getDynamic_num());
                dynamicFragment.loadSuccess(dynamicEntities);
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(ProfileActivity.this, desc);
                dynamicFragment.loadFaile();
            }
        });
    }

    private void loadDynamicMore(int friendId){
        String lastId = "";
        if(dynamicEntities.size()>=1){
            lastId = dynamicEntities.get(0).getReleaseId();
        }
        HttpClientUtil.User.friendHomeDynamic(1, friendId, lastId,new AbsHttpResultHandler<UserInfoEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, UserInfoEntity data) {
                userInfoEntity = data;
                initInfo();
                dynamicEntities.addAll(data.getFriendDynamicEntities());
                tabs[0].setmCount(data.getDynamic_num());
                dynamicFragment.loadMore(true, true);
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(ProfileActivity.this, desc);
                dynamicFragment.loadMore(false, true);
            }
        });
    }

    private void loadFavorite(int friendId){
        HttpClientUtil.User.friendFavoriteDynamic(1, 1, friendId, "", new AbsHttpResultHandler<UserInfoEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, UserInfoEntity data) {
                userInfoEntity = data;
                favoriteEntities.clear();
                initInfo();
                favoriteEntities.addAll(data.getFriendFavoriteEntities());
                tabs[1].setmCount(data.getFavorite_num());
                favoriteFragment.loadSuccess(favoriteEntities);
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                favoriteFragment.loadFaile();
            }
        });
    }
    private void loadFavoriteMore(int friendId){
        String lastId = "";
        if(favoriteEntities.size()>=1){
            lastId = favoriteEntities.get(0).getReleaseId();
        }
        HttpClientUtil.User.friendFavoriteDynamic(1, 1, friendId,lastId, new AbsHttpResultHandler<UserInfoEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, UserInfoEntity data) {
                userInfoEntity = data;
                initInfo();
                favoriteEntities.addAll(data.getFriendFavoriteEntities());
                tabs[1].setmCount(data.getFavorite_num());
                favoriteFragment.loadMore(true,true);
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                favoriteFragment.loadMore(false, true);
            }
        });
    }
    private void initInfo() {
        TextViewUtil.setText(this, R.id.text_user_name, userInfoEntity.getNickname());
        TextViewUtil.setText(this, R.id.text_user_id, "北极圈号:" + userInfoEntity.getUserId());
        if(StringUtil.isValid(userInfoEntity.getAvatar())){
            ImageLoader.getInstance().displayImage(userInfoEntity.getAvatar(), headView);
        }
        CommonFriends commonFriends = userInfoEntity.getCommon_friends();
        List<UserInfoEntity> userInfoEntities = new ArrayList<>();
        if(commonFriends != null){
            commonFriendsLabel.setText(userInfoEntity.getCommon_friends().getTotal()+"个共同好友");
            userInfoEntities = commonFriends.getList();
        }else{
            commonFriendsLabel.setText("0个共同好友");
        }
        if("1".equals(userInfoEntity.getIs_attention())){
            rightLabel.setText("已关注");
        }else{
            rightLabel.setText("关注TA");
        }
        commonFriendsRecyclerView.setAdapter(new RecyclerView1Adapter(this, userInfoEntities));

        hidemeSwitch.setChecked("1".equals(userInfoEntity.getCant_see_me()));
        hidemeToHeFriendsSwitch.setChecked("1".equals(userInfoEntity.getMy_friend_cant_see_his()));
        //TODO 黑名单初始状态
//        addBlackSwitch.setChecked(userInfoEntity.isBlackList());
        hidemeSwitch.setOnCheckedChangeListener(this);
        hidemeToHeFriendsSwitch.setOnCheckedChangeListener(this);
        addBlackSwitch.setOnCheckedChangeListener(this);
        TextViewUtil.setText(this, R.id.text_user_name, userInfoEntity.getNickname());
        TextViewUtil.setText(this, R.id.text_user_id, "北极圈号:" + userInfoEntity.getUserId());
        if(StringUtil.isValid(userInfoEntity.getAvatar())){
            ImageLoader.getInstance().displayImage(userInfoEntity.getAvatar(), headView);
        }
        TextViewUtil.setText(this, R.id.text_user_address, userInfoEntity.getAddress());
        TextViewUtil.setText(this, R.id.text_uesr_label, userInfoEntity.getLabel());
        TextViewUtil.setText(this, R.id.text_uesr_label_all, userInfoEntity.getProfile());
    }

    private void attention(final boolean isAttention){
        HttpClientUtil.Friend.friendAttention(isAttention, friendId, new AbsHttpResultHandler() {
            @Override
            public void onSuccess(int resultCode, String desc, Object data) {
                if (isAttention) {
                    ToastUtil.showToast(ProfileActivity.this, "关注成功");
                    userInfoEntity.setIs_attention("1");
                    rightLabel.setText("已关注");
                } else {
                    ToastUtil.showToast(ProfileActivity.this, "取消关注成功");
                    userInfoEntity.setIs_attention("0");
                    rightLabel.setText("关注TA");
                }
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(ProfileActivity.this, desc);
            }
        });
    }

    private void setPrivacy(final String canSeeMe, final String  my_friend_cant_see_his){

        HttpClientUtil.Friend.friendSetPrivacy(canSeeMe, my_friend_cant_see_his, friendId, new AbsHttpResultHandler() {
            @Override
            public void onSuccess(int resultCode, String desc, Object data) {
                userInfoEntity.setCant_see_me(canSeeMe);
                userInfoEntity.setMy_friend_cant_see_his(my_friend_cant_see_his);
                ToastUtil.showToast(ProfileActivity.this, "设置成功");
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(ProfileActivity.this, "设置成功"+desc);
            }
        });
    }

    private void updateBlackList(boolean isChecked){
        HttpClientUtil.Friend.friendUpdateBlacklist(DataUtil.getInt(userInfoEntity.getUserId()), isChecked, new AbsHttpResultHandler() {
            @Override
            public void onSuccess(int resultCode, String desc, Object data) {
                ToastUtil.showToast(ProfileActivity.this,"设置成功");
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(ProfileActivity.this,"设置成功:"+desc);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_right_label:
                if("已关注".equals(rightLabel.getText())){
                    attention(false);
                }else {
                    attention(true);
                }
                break;
            case R.id.text_left_label:
                IntentUtil.goChattingActivity(this,userInfoEntity);
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.switch_hide_me_to_he:
                String canSeeMe = isChecked ? "1" :"2";
                setPrivacy(canSeeMe, userInfoEntity.getMy_friend_cant_see_his());
                break;
            case R.id.switch_hide_me_to_his_friend:
                String my_friend_can_see_his  = isChecked ? "1" :"2";
                setPrivacy(userInfoEntity.getCant_see_me(), my_friend_can_see_his);
                break;
            case R.id.switch_add_to_black:
                updateBlackList(isChecked);
                break;
        }
    }
}
