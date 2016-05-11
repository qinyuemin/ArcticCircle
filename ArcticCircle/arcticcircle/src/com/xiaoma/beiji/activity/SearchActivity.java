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

import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.entity.FriendDynamicEntity;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.fragment.InfoDetailsFragment;
import com.xiaoma.beiji.fragment.SearchFriendFragment;
import com.xiaoma.beiji.fragment.SearchShopFragment;
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

    private SearchShopFragment searchShopFragment;
    private SearchFriendFragment searchFriendFragment;
    private InfoDetailsFragment infoDetailsFragment;

    private EditText searchEdit;

    private List<Object> dataList = new ArrayList<>();

    private Spinner spinner;
//    private List<String> data_list;
//    private ArrayAdapter<String> arr_adapter;

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
        searchShopFragment = new SearchShopFragment();
        searchFriendFragment = new SearchFriendFragment();
        infoDetailsFragment = new InfoDetailsFragment();
//        dataList.add(new Title("热门店铺"));
//        List<ShopEntity> hotShops = new ArrayList<>();
//        ShopEntity shopEntity1 = new ShopEntity();
//        shopEntity1.setShowName("推荐1");
//        ShopEntity shopEntity2 = new ShopEntity();
//        shopEntity2.setShowName("推荐2");
//        hotShops.add(shopEntity1);
//        hotShops.add(shopEntity2);
//        HotShop hotShop = new HotShop(hotShops);
//        dataList.add(hotShop);
//        dataList.add(new Title("已收藏店铺"));
//        for(int i=0; i<20; i++){
//            ShopEntity shopEntity = new ShopEntity();
//            shopEntity.setShowName("收藏"+i);
//            dataList.add(shopEntity);
//        }

    }



    @Override
    protected void loadData() {

    }

    private void showFragment(int tag){
        Fragment fragment = null;
        switch (tag){
//            case DIANPU:
            case HAOYOU:
                searchShopFragment.setList(dataList);
                fragment = searchShopFragment;
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
        HttpClientUtil.Search.searchDynamic(keywords, new AbsHttpResultHandler<FriendDynamicEntity>() {


            @Override
            public void onSuccess(int resultCode, String desc, FriendDynamicEntity data) {

            }

            @Override
            public void onSuccess(int resultCode, String desc, List<FriendDynamicEntity> list) {
//                for(FriendDynamicEntity entity: dataList){
//                    HttpClientUtil.logger(entity.toString());
//                }
                if(list==null||list.size()<=0){
                    ToastUtil.showToast(SearchActivity.this,"暂未搜索到动态");
                }else{
                    infoDetailsFragment.setList(list);
                }

            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(SearchActivity.this,desc);
            }
        });
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
                        HttpClientUtil.logger(entity.toString());
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