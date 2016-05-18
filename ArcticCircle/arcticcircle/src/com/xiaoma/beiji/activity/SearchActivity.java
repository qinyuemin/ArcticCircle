/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.entity.FriendDynamicEntity;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.fragment.FriendDynamicListFragment;
import com.xiaoma.beiji.fragment.SearchFriendFragment;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * create by zhangqibo 2016年4月12日23:11:34
 */
public class SearchActivity extends SimpleBaseActivity implements View.OnClickListener {
    private static final String TAG = SearchActivity.class.getSimpleName();

//    private static final int DIANPU = 0;
    private static final int DONGTAI = 0;
    private static final int HAOYOU = 1;

    private FragmentManager fragmentManager;
    private FragmentTransaction fmt;

//    private SearchShopFragment searchShopFragment;
    private SearchFriendFragment searchFriendFragment;
    private FriendDynamicListFragment infoDetailsFragment;
    private List<FriendDynamicEntity> dynamicEntities = new ArrayList<>();

    private EditText searchEdit;

    private List<UserInfoEntity> dataList = new ArrayList<>();

    private Spinner spinner;

    @Override
    protected String getActivityTitle() {
        return "";
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initComponents() {
        spinner = (Spinner) findViewById(R.id.spinner_search);
        searchEdit = (EditText) findViewById(R.id.edt_search);
        findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(spinner.getSelectedItemPosition());
            }
        });
        fragmentManager = getSupportFragmentManager();
        searchFriendFragment = new SearchFriendFragment();
        infoDetailsFragment = new FriendDynamicListFragment();
        infoDetailsFragment.setPtrHandlerListener(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                searchDynamic();
            }

        });
        infoDetailsFragment.setPtrLoadMore(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                searchDynamicNextPage();
            }
        });
    }



    @Override
    protected void loadData() {

    }

    private void showFragment(int tag){
        Fragment fragment = null;
        switch (tag){
//            case DIANPU:
            case HAOYOU:
                fragment = searchFriendFragment;
                searchUser();
                break;
            case DONGTAI:
                fragment =infoDetailsFragment;
                searchDynamic();
                break;
        }
        HttpClientUtil.logger(fragment.getClass().getSimpleName());
        if(fragment!=null){
            fmt = fragmentManager.beginTransaction();
            if (!fragment.isAdded()) {
                fmt.replace(R.id.layout_content, fragment);
            } else {
                fmt.show(fragment);
            }
            fmt.commitAllowingStateLoss();
        }
    }

    private void searchDynamic(){
        String keywords = searchEdit.getText().toString().trim();
        HttpClientUtil.Search.searchDynamic(keywords, "", "",new AbsHttpResultHandler<FriendDynamicEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, FriendDynamicEntity data) {

            }

            @Override
            public void onSuccess(int resultCode, String desc, List<FriendDynamicEntity> list) {
                dynamicEntities.clear();
                if (list == null || list.size() <= 0) {
                    ToastUtil.showToast(SearchActivity.this, "暂未搜索到动态");
                    infoDetailsFragment.loadFaile();
                } else {
                    dynamicEntities.addAll(list);
                    infoDetailsFragment.loadSuccess(dynamicEntities);
                }

            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(SearchActivity.this, desc);
                infoDetailsFragment.loadFaile();
            }
        });
    }

    private void searchDynamicNextPage(){
        String keywords = searchEdit.getText().toString().trim();
        if(dynamicEntities.size()>0){
            String lastId = dynamicEntities.get(dynamicEntities.size()-1).getReleaseId();
            String newestId = dynamicEntities.get(0).getReleaseId();
                    HttpClientUtil.Search.searchDynamic(keywords, lastId ,newestId,new AbsHttpResultHandler<FriendDynamicEntity>() {
                        @Override
                        public void onSuccess(int resultCode, String desc, FriendDynamicEntity data) {

                        }
                        @Override
                        public void onSuccess(int resultCode, String desc, List<FriendDynamicEntity> list) {
                            if(list==null||list.size()<=0){
                                infoDetailsFragment.loadMore(true,false);
                            }else{
                                dynamicEntities.addAll(list);
                                infoDetailsFragment.loadMore(true,false);
                            }
                        }

                        @Override
                        public void onFailure(int resultCode, String desc) {
                            ToastUtil.showToast(SearchActivity.this,desc);
                            infoDetailsFragment.loadFaile();
                        }
                    });
        }
    }

    private void searchUser(){
        String keywords = searchEdit.getText().toString().trim();
        HttpClientUtil.Search.searchFriends(keywords, new AbsHttpResultHandler<UserInfoEntity>() {


            @Override
            public void onSuccess(int resultCode, String desc, UserInfoEntity data) {

            }

            @Override
            public void onSuccess(int resultCode, String desc, List<UserInfoEntity> list) {
                if(list==null||list.size()<=0){
                    ToastUtil.showToast(SearchActivity.this,"暂未搜索到好友");
                }else{
                    for(UserInfoEntity entity: list){
                        HttpClientUtil.logger("user-->"+entity.toString());
                        dataList.add(entity);
                    }
                    searchFriendFragment.setList(dataList);
                }
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(SearchActivity.this,desc);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}