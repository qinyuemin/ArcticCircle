/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.activity;

import android.view.View;
import com.makeapp.android.util.EditTextUtil;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ViewUtil;
import com.makeapp.javase.lang.StringUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.entity.ChatRequestEntity;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.ToastUtil;

/**
 *
 * 类名称： ChatPrivateReferralActivity
 * 类描述： 聊天 引荐
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年11月24 23:25
 * 修改备注：
 * @version 1.0.0
 *
 */
public class ChatReferralActivity extends SimpleBaseActivity implements View.OnClickListener {
    private static final String TAG = ChatReferralActivity.class.getSimpleName();

    private ChatRequestEntity entity;
    private UserInfoEntity friendUser;

    @Override
    protected String getActivityTitle() {
        return "私聊";
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_chat_referral;
    }

    @Override
    protected void initIntent() {
        super.initIntent();
        entity = (ChatRequestEntity) getIntent().getSerializableExtra("entity");
        friendUser = (UserInfoEntity) getIntent().getSerializableExtra("friendUser");
    }

    @Override
    protected void initComponents() {
        setTitleControlsInfo();
        ViewUtil.setViewOnClickListener(this,R.id.btn_send,this);

        TextViewUtil.setText(this,R.id.txt_tip,"“"+friendUser.getNickname()+"”还不是你的好友，不能直接聊天，需要经过“"+entity.getRecommendNickname()+"”引荐");
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_send:
                String content = EditTextUtil.getTextString(this,R.id.edt_content);
                if(StringUtil.isInvalid(content)){
                    ToastUtil.showToast(this,"请输入引荐理由");
                    return;
                }
                HttpClientUtil.Friend.friendRecommend(friendUser.getUserId(), entity.getRecommendUserId(), content, new AbsHttpResultHandler() {
                    @Override
                    public void onSuccess(int resultCode, String desc, Object data) {
                        ToastUtil.showToast(ChatReferralActivity.this,"发送成功");
                        finish();
                    }

                    @Override
                    public void onFailure(int resultCode, String desc) {
                        ToastUtil.showToast(ChatReferralActivity.this,"发送失败:"+desc);
                    }
                });
                break;
        }
    }
}