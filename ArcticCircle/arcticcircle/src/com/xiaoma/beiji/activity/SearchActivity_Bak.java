/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.activity;

import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.makeapp.android.adapter.ArrayListAdapter;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ViewUtil;
import com.makeapp.javase.lang.StringUtil;
import com.makeapp.javase.util.DataUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.adapter.FriendDynamicAdapter;
import com.xiaoma.beiji.adapter.ShopAdapter;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.controls.view.CircularImage;
import com.xiaoma.beiji.entity.FriendDynamicEntity;
import com.xiaoma.beiji.entity.FriendEntity;
import com.xiaoma.beiji.entity.SearchEntity;
import com.xiaoma.beiji.entity.ShopEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.CommUtil;
import com.xiaoma.beiji.util.IntentUtil;
import com.xiaoma.beiji.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称： SearchActivity
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月09 15:40
 * 修改备注：
 *
 * @version 1.0.0
 */
public class SearchActivity_Bak extends SimpleBaseActivity implements View.OnClickListener {
    private static final String TAG = SearchActivity_Bak.class.getSimpleName();

    private EditText edtSearch;
    private int currentPosition = 0;
    private View line1, line2, line3;

    private ListView lstFriend, lstDynamic, lstShop;
    private ArrayListAdapter<FriendEntity> friendAdapter;
    private ShopAdapter shopAdapter;
    private FriendDynamicAdapter dynamicAdapter;

    private List<FriendEntity> friendEntities;
    private List<FriendDynamicEntity> dynamicEntities;
    private List<ShopEntity> shopEntities;

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
        friendEntities = new ArrayList<>();
        dynamicEntities = new ArrayList<>();
        shopEntities = new ArrayList<>();

        ViewUtil.setViewOnClickListener(this, R.id.ll_friend, this);
        ViewUtil.setViewOnClickListener(this, R.id.ll_dynamic, this);
        ViewUtil.setViewOnClickListener(this, R.id.ll_shop, this);
        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);
        line3 = findViewById(R.id.line3);

        lstDynamic = (ListView) findViewById(R.id.lst_dynamic);
        lstShop = (ListView) findViewById(R.id.lst_shop);
        lstFriend = (ListView) findViewById(R.id.lst_friend);

        edtSearch = (EditText) findViewById(R.id.edt_search);
        edtSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //do something;
                    search();
                    CommUtil.hideKeyBoard(SearchActivity_Bak.this);
                    v.setText("");
                    v.clearFocus();
                    return true;
                }
                return false;
            }
        });

        ViewUtil.setViewOnClickListener(this, R.id.title_bar_left_img, this);

        friendAdapter = new ArrayListAdapter<FriendEntity>(this, R.layout.ls_item_userinfo, friendEntities) {
            @Override
            public void fillView(ViewGroup viewGroup, View view, FriendEntity entity, int i) {
                String avatar = entity.getAvatar();
                if (StringUtil.isValid(avatar)) {
                    CircularImage imgHead = (CircularImage) view.findViewById(R.id.img_head);
                    ImageLoader.getInstance().displayImage(avatar, imgHead);
                }

                TextViewUtil.setText(view, R.id.txt_name, entity.getNickname());
                TextViewUtil.setText(view, R.id.txt_tag_major, CommUtil.getFriendTag(entity.getGender(), entity.getV()));
                TextViewUtil.setText(view, R.id.txt_time, entity.getTitle());
            }
        };
        lstFriend.setAdapter(friendAdapter);

        dynamicAdapter = new FriendDynamicAdapter(this, dynamicEntities);
        lstDynamic.setAdapter(dynamicAdapter);

        shopAdapter = new ShopAdapter(this, shopEntities);
        lstShop.setAdapter(shopAdapter);


        lstFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FriendEntity entity = (FriendEntity) friendAdapter.getItem(i);
                IntentUtil.goFriendDetailActivity(SearchActivity_Bak.this, entity.getUserId());
            }
        });
        lstDynamic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                IntentUtil.goFriendDynamicDetailActivity(SearchActivity_Bak.this, dynamicAdapter.getItem((int) l).getReleaseId());
            }
        });
        lstShop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                IntentUtil.goShopMainActivity(SearchActivity_Bak.this, shopAdapter.getItem(i).getShopId());
            }
        });

        setTabColor(0);
    }


    private void search() {
        String searchContent = DataUtil.getString(edtSearch.getText(), "");
        if (StringUtil.isInvalid(searchContent)) {
            ToastUtil.showToast(this, "请输入搜索内容");
            edtSearch.requestFocus();
            return;
        }

//        HttpClientUtil.searchGetList(searchContent, new AbsHttpResultHandler<SearchEntity>() {
//            @Override
//            public void onSuccess(int resultCode, String desc, SearchEntity data) {
//                friendEntities.clear();
//                friendEntities.addAll(data.getUserEntities());
//                friendAdapter.notifyDataSetChanged();
//
//                dynamicEntities.clear();
//                dynamicEntities.addAll(data.getReleaseDynamic());
//                dynamicAdapter.notifyDataSetChanged();
//
//                shopEntities.clear();
//                shopEntities.addAll(data.getShopEntities());
//                shopAdapter.notifyDataSetChanged();
//
//                setTabColor(currentPosition);
//            }
//
//            @Override
//            public void onFailure(int resultCode, String desc) {
//
//            }
//        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ll_dynamic:
                setTabColor(0);
                break;
            case R.id.ll_shop:
                setTabColor(1);
                break;
            case R.id.ll_friend:
                setTabColor(2);
                break;
            case R.id.title_bar_left_img:
                finish();
                break;
        }
    }

    private void setTabColor(int position) {
        currentPosition = position;
        switch (position) {
            case 0:
                line1.setBackgroundResource(R.color.color_blue);
                line2.setBackgroundResource(R.color.white);
                line3.setBackgroundResource(R.color.white);

                lstDynamic.setVisibility(View.VISIBLE);
                lstShop.setVisibility(View.GONE);
                lstFriend.setVisibility(View.GONE);
                break;
            case 1:
                line1.setBackgroundResource(R.color.white);
                line2.setBackgroundResource(R.color.color_blue);
                line3.setBackgroundResource(R.color.white);

                lstDynamic.setVisibility(View.GONE);
                lstShop.setVisibility(View.VISIBLE);
                lstFriend.setVisibility(View.GONE);
                break;
            case 2:
                line1.setBackgroundResource(R.color.white);
                line2.setBackgroundResource(R.color.white);
                line3.setBackgroundResource(R.color.color_blue);

                lstDynamic.setVisibility(View.GONE);
                lstShop.setVisibility(View.GONE);
                lstFriend.setVisibility(View.VISIBLE);
                break;
        }
    }
}