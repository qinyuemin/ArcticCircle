package com.xiaoma.beiji.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
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


    private FriendDynamicListFragment dynamicFragment;
    private FriendDynamicListFragment seekHelpFragment;
    private FriendDynamicListFragment shouCangFragment;

    private List<FriendDynamicEntity> dynamicEntities = new ArrayList<>();
    private List<FriendDynamicEntity> seekHelpEntities = new ArrayList<>();
    private List<FriendDynamicEntity> favoriteEntities = new ArrayList<>();

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
        loadMySeekHelp();
        loadMyFavorite();
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
        dynamicFragment = new FriendDynamicListFragment();
        seekHelpFragment = new FriendDynamicListFragment();
        shouCangFragment = new FriendDynamicListFragment();
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

        dynamicFragment.setPtrHandlerListener(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                loadMyDynamic();
            }

        });
        dynamicFragment.setPtrLoadMore(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                loadMyDynamicMore();
            }
        });

        seekHelpFragment.setPtrHandlerListener(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                loadMySeekHelp();
            }

        });
        seekHelpFragment.setPtrLoadMore(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                loadMySeekHelpMore();
            }
        });

        shouCangFragment.setPtrHandlerListener(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                loadMyFavorite();
            }

        });
        shouCangFragment.setPtrLoadMore(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                loadMyFavoriteMore();
            }
        });
    }

    private void bindDataToView(){
        initInfo();
    }

    private void loadMySeekHelp(){
        HttpClientUtil.User.userHomeDynamic(2, "","", new AbsHttpResultHandler<UserInfoEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, UserInfoEntity data) {
                userInfoEntity = data;
                seekHelpEntities.clear();
                seekHelpEntities.addAll(data.getFriendDynamicEntities());
                bindDataToView();
                seekHelpFragment.loadSuccess(seekHelpEntities);
                tabs[1].setmCount(seekHelpEntities.size()+"");
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(getContext(), desc);
                seekHelpFragment.loadFaile();
            }
        });
    }
    private void loadMySeekHelpMore(){
        String newestId = "";
        String lastID = "";
        if(seekHelpEntities.size()>1){
            lastID = seekHelpEntities.get(seekHelpEntities.size()-1).getReleaseId();
            newestId = seekHelpEntities.get(0).getReleaseId();
        }
        HttpClientUtil.User.userHomeDynamic(2, lastID, newestId,new AbsHttpResultHandler<UserInfoEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, UserInfoEntity data) {
                userInfoEntity = data;
                seekHelpEntities.addAll(data.getFriendDynamicEntities());
                bindDataToView();
                seekHelpFragment.loadMore(true, true);
                tabs[1].setmCount(seekHelpEntities.size()+"");
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(getContext(), desc);
                seekHelpFragment.loadMore(false, true);
            }
        });
    }

    private void loadMyFavoriteMore(){
        String lastID = "";
        String newesetId = "";
        if(favoriteEntities.size()>1){
            lastID = favoriteEntities.get(favoriteEntities.size()-1).getReleaseId();
            newesetId = favoriteEntities.get(0).getReleaseId();
        }
        HttpClientUtil.User.homeFavoriteDynamic(1, 1, lastID, newesetId,new AbsHttpResultHandler<UserInfoEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, UserInfoEntity data) {
                userInfoEntity = data;
                if(data.getFriendFavoriteEntities()!=null){
                    favoriteEntities.addAll(data.getFriendFavoriteEntities());
                }
                bindDataToView();
                shouCangFragment.loadMore(true, true);
                tabs[2].setmCount(data.getFavorite_num());
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(getContext(), desc);
                shouCangFragment.loadMore(false, true);
            }
        });
    }

    private void loadMyFavorite(){
        HttpClientUtil.User.homeFavoriteDynamic(1, 1, "", "",new AbsHttpResultHandler<UserInfoEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, UserInfoEntity data) {
                userInfoEntity = data;
                favoriteEntities.clear();
                if (data.getFriendFavoriteEntities() != null) {
                    favoriteEntities.addAll(data.getFriendFavoriteEntities());
                }
                bindDataToView();
                shouCangFragment.loadSuccess(favoriteEntities);
                tabs[2].setmCount(data.getFavorite_num());
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(getContext(), desc);
                shouCangFragment.loadFaile();
            }
        });
    }

    private void loadMyDynamicMore(){
        String lastID = "";
        String newestId = "";
        if(dynamicEntities.size()>1){
            lastID = dynamicEntities.get(dynamicEntities.size()-1).getReleaseId();
            newestId = dynamicEntities.get(0).getReleaseId();
        }
        HttpClientUtil.User.userHomeDynamic(1, lastID,newestId, new AbsHttpResultHandler<UserInfoEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, UserInfoEntity data) {
                userInfoEntity = data;
                dynamicEntities.addAll(data.getFriendDynamicEntities());
                bindDataToView();
                dynamicFragment.loadMore(true, true);
                tabs[0].setmCount(dynamicEntities.size()+"");
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(getContext(), desc);
                dynamicFragment.loadFaile();
                dynamicFragment.loadMore(false, true);
            }
        });
    }
    private void loadMyDynamic(){
        HttpClientUtil.User.userHomeDynamic(1,"", "",new AbsHttpResultHandler<UserInfoEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, UserInfoEntity data) {
                userInfoEntity = data;
                dynamicEntities.clear();
                dynamicEntities.addAll(data.getFriendDynamicEntities());
                bindDataToView();
                dynamicFragment.loadSuccess(dynamicEntities);
                tabs[0].setmCount(dynamicEntities.size()+"");
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(getContext(), desc);
                dynamicFragment.loadFaile();
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
        TextViewUtil.setText(rootView, R.id.text_uesr_label, userInfoEntity.getLabel());
        TextViewUtil.setText(rootView, R.id.text_uesr_label_all, userInfoEntity.getProfile());
        if("1".equals(userInfoEntity.getGender())){
            ImageViewUtil.setImageSrcId(rootView,R.id.image_gender,R.drawable.icon_nan);
        }else{
            ImageViewUtil.setImageSrcId(rootView,R.id.image_gender,R.drawable.icon_nv);
        }
        TextViewUtil.setText(rootView, R.id.text_left_label_num, userInfoEntity.getAttention_friend_num());
        TextViewUtil.setText(rootView, R.id.text_right_label_num, userInfoEntity.getAttention_user_num());
        TextViewUtil.setViewOnClickListener(rootView, R.id.text_left_label_num, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TextViewUtil.setViewOnClickListener(rootView, R.id.text_right_label_num, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
