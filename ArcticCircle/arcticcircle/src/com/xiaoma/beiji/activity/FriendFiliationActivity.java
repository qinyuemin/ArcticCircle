/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.makeapp.android.adapter.ArrayListAdapter;
import com.makeapp.android.util.TextViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.entity.FriendEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称： FriendFiliationActivity
 * 类描述： 朋友关系设置
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月02 23:41
 * 修改备注：
 *
 * @version 1.0.0
 */
public class FriendFiliationActivity extends SimpleBaseActivity {
    private static final String TAG = FriendFiliationActivity.class.getSimpleName();

    private ListView lstBlacklist;
    private ArrayListAdapter<FriendEntity> adapter;
    private List<FriendEntity> entities;
    private List<FriendEntity> showEntities;

    private int type;

    private CheckBox cb;


    @Override
    protected String getActivityTitle() {
        String title = "我朋友看不到他们";
        if(type==1){
            title = "他们看不到我朋友";
        }
        return title;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_blacklist;
    }

    @Override
    protected void initIntent() {
        super.initIntent();
        type = getIntent().getIntExtra("type",1);
    }

    @Override
    protected void initComponents() {
        setTitleControlsInfo();


        String tag = "这里设置的人，你的朋友将不能看到TA及齐动态、求助、分享的所有公告信息";
        if(type == 1){
            tag = "这里设置的人，将不能看到你的朋友及其动态、求助、分享的所有公共信息";
        }
        TextViewUtil.setText(this,R.id.txt_tag,tag);

        lstBlacklist = (ListView) findViewById(R.id.lst_blacklist);
        entities = new ArrayList<>();
        showEntities = new ArrayList<>();
        adapter = new ArrayListAdapter<FriendEntity>(this, R.layout.lst_item_blacklist, showEntities) {
            @Override
            public void fillView(ViewGroup viewGroup, View view,final FriendEntity s, int i) {
                CheckBox rb = (CheckBox) view.findViewById(R.id.rb_female);
                rb.setOnCheckedChangeListener(null);
                boolean  b = s.isFriendNotLookHer();
                if(type == 1){
                     b = s.isLookMe();
                }
                rb.setChecked(b);
                TextViewUtil.setText(view,R.id.txt_name,s.getNickname());
                try{
                    ImageLoader.getInstance().displayImage(s.getAvatar(), (ImageView) view.findViewById(R.id.img));
                }catch (Exception e){

                }

                rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {
                        HttpClientUtil.Friend.friendSetFiliation(s.getUserId(), b, type, new AbsHttpResultHandler() {
                            @Override
                            public void onSuccess(int resultCode, String desc, Object data) {
                                if(type == 1){
                                    s.setLookMe(b);
                                }else{
                                    s.setFriendNotLookHer(b);
                                }
                                adapter.notifyDataSetChanged();
                                setCheckNum();
                            }

                            @Override
                            public void onFailure(int resultCode, String desc) {

                            }
                        });
                    }
                });

            }

        };
        lstBlacklist.setAdapter(adapter);


        cb = (CheckBox) findViewById(R.id.setting_item_checkbox);

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                showEntities.clear();
                if(b){
                    for (FriendEntity f : entities){
                        if(type == 1){
                            if(f.isLookMe()){
                                showEntities.add(f);
                            }
                        }else {
                            if(f.isFriendNotLookHer()){
                                showEntities.add(f);
                            }
                        }
                    }
                }else{
                    showEntities.addAll(entities);
                }

                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setCheckNum() {
        int i = 0;
        for (FriendEntity f : showEntities){
            if(type == 1){
                if(f.isLookMe()){
                    i ++;
                }
            }else{
                if(f.isFriendNotLookHer()){
                    i ++;
                }
            }
        }
        TextViewUtil.setText(FriendFiliationActivity.this, R.id.txt_num,"只显示已选中的"+i+"人");
    }

    @Override
    protected void loadData() {
        HttpClientUtil.Friend.friendGetList(new AbsHttpResultHandler<FriendEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, FriendEntity data) {

            }

            @Override
            public void onSuccess(int resultCode, String desc, List<FriendEntity> data) {
                super.onSuccess(resultCode, desc, data);
                entities.clear();
                entities.addAll(data);
                showEntities.clear();
                showEntities.addAll(entities);
                adapter.notifyDataSetChanged();

                setCheckNum();
            }

            @Override
            public void onFailure(int resultCode, String desc) {

            }
        });
    }
}