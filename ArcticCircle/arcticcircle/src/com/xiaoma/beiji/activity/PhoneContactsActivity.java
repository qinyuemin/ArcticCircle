/**
 * 项目名： simpleApp
 * 包名： com.company.simple.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015
 */
package com.xiaoma.beiji.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeapp.javase.lang.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.controls.view.CircularImage;
import com.xiaoma.beiji.entity.ContactEntity;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.manager.ContactManager;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.IntentUtil;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ViewUtil;
import com.xiaoma.beiji.util.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 *
 * 类名称： PhoneContactsActivity
 * 类描述： 
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年11月16 17:59
 * 修改备注：
 * @version 1.0.0
 *
 */
public class PhoneContactsActivity extends SimpleBaseActivity {
    private static final String TAG = PhoneContactsActivity.class.getSimpleName();

    private List<ContactEntity> contactList;
    private List<UserInfoEntity> displayList;
    private List<ContactEntity> attendList;

    private RecyclerView recyclerView;

    private boolean isFromRegister;

    @Override
    protected String getActivityTitle() {
        return "关注通讯录";
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_register_finish;
    }
    @Override
    protected void setTitleControlsInfo() {
        super.setTitleControlsInfo();
        isFromRegister = getIntent().getBooleanExtra("isFromRegister",false);
        ViewUtil.setViewVisibility(this, R.id.title_bar_left_sub_txt, View.GONE);
        ViewUtil.setViewVisibility(this, R.id.title_bar_left_img, View.GONE);

        TextView txtRight = (TextView) findViewById(R.id.title_bar_right_txt);
        txtRight.setText("确定");
        ViewUtil.setViewOnClickListener(this, R.id.title_bar_right_layout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncContacts();
            }
        });
    }

    @Override
    protected void initComponents() {
        setTitleControlsInfo();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void loadData() {
        attendList = new ArrayList<>();
        contactList = ContactManager.getInstance(this).getContactEntities();
        HttpClientUtil.friendVCardList(contactList, new AbsHttpResultHandler<UserInfoEntity>() {

            @Override
            public void onSuccess(int resultCode, String desc, List<UserInfoEntity> data) {
                super.onSuccess(resultCode, desc, data);

                if (data != null) {
                    displayList = data;
                    Iterator<ContactEntity> iterator = contactList.iterator();
                    while (iterator.hasNext()) {
                        ContactEntity contactEntity = iterator.next();
                        for (UserInfoEntity entity : displayList) {
                            if (contactEntity.getPhone().equals(entity.getPhone())) {
                                if ("1".equals(entity.getIs_register())) {
                                    entity.setName(entity.getNickname() + "(" + contactEntity.getName() + ")");
                                }
                            }
                        }
                    }
                    if(displayList.size()>1){
                        Collections.sort(displayList, new Comparator<UserInfoEntity>() {
                            @Override
                            public int compare(UserInfoEntity lhs, UserInfoEntity rhs) {
                                try {
                                    int lhs_register = Integer.valueOf(lhs.getIs_register());
                                    int rhs_register = Integer.valueOf(rhs.getIs_register());
                                    return  lhs_register - rhs_register;
                                }catch (Exception e){
                                    return 0;
                                }
                            }
                        });
                    }

                }else{
                    displayList = new ArrayList<UserInfoEntity>();
                }

                recyclerView.setAdapter(new Adpter(PhoneContactsActivity.this,displayList));
            }

            @Override
            public void onSuccess(int resultCode, String desc, UserInfoEntity data) {

            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(PhoneContactsActivity.this, desc);
            }
        });
    }

    private void syncContacts() {
        HttpClientUtil.vCardChange(attendList, new AbsHttpResultHandler() {
            @Override
            public void onSuccess(int resultCode, String desc, Object data) {
                Log.i(TAG, "vCardChange success");
                if(isFromRegister){
                    IntentUtil.goMainActivity(PhoneContactsActivity.this);
                }else{
                    finish();
                }
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                Log.i(TAG, "vCardChange failure :" + desc);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private CircularImage headView;
        private TextView nameText;
        private TextView descText;
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            headView = (CircularImage) itemView.findViewById(R.id.img_head);
            nameText = (TextView) itemView.findViewById(R.id.text_item_name);
            descText = (TextView) itemView.findViewById(R.id.text_description);
            imageView = (ImageView) itemView.findViewById(R.id.item_add);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    class Adpter extends RecyclerView.Adapter<ViewHolder>{

        private Context context;
        private List<UserInfoEntity> list;

        public Adpter(Context context, List<UserInfoEntity> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_contacts,null);
            return  new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final UserInfoEntity userInfoEntity = displayList.get(position);
            holder.nameText.setText(userInfoEntity.getName());
            if(!"1".equals(userInfoEntity.getIs_attention())){
                holder.imageView.setImageResource(R.drawable.ic_publish);
            }else{
                holder.imageView.setImageDrawable(null);
            }
            if(StringUtil.isValid(userInfoEntity.getAvatar())){
                ImageLoader.getInstance().displayImage(userInfoEntity.getAvatar(), holder.headView);
            }
            holder.descText.setText(userInfoEntity.getPhone());
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if("1".equals(userInfoEntity.getIs_register())){
                        final boolean isAttention = !"1".equals(userInfoEntity.getIs_attention());
                        HttpClientUtil.Friend.friendAttention(isAttention, userInfoEntity.getUserId(), new AbsHttpResultHandler() {
                            @Override
                            public void onSuccess(int resultCode, String desc, Object data) {
                                userInfoEntity.setIs_attention(isAttention?"1":"2");
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(int resultCode, String desc) {

                            }
                        });
                    }else{
                        boolean isAttention = !"1".equals(userInfoEntity.getIs_attention());
                        userInfoEntity.setIs_attention(isAttention ? "1" : "2");
                        notifyDataSetChanged();
                        if(isAttention){
                            attendList.add(userInfoEntity);
                        }else{
                            attendList.remove(userInfoEntity);
                        }

                    }


                }
            });
        }

        @Override
        public int getItemCount() {
            return displayList.size();
        }
    }

}