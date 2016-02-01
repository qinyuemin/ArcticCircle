/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.activity;

import android.app.Activity;
import android.os.Bundle;
import com.makeapp.android.util.TextViewUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.entity.ChatRequestEntity;

/**
 * 类名称： ChatStatusActivity
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月19 23:29
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ChatStatusActivity extends SimpleBaseActivity {
    private static final String TAG = ChatStatusActivity.class.getSimpleName();

    private ChatRequestEntity entity;

    @Override
    protected String getActivityTitle() {
        return "聊天";
    }

    @Override
    protected void initIntent() {
        super.initIntent();
        entity = (ChatRequestEntity) getIntent().getSerializableExtra("entity");
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_chat_status;
    }

    @Override
    protected void initComponents() {
        setTitleControlsInfo();
        String tip = "";
        switch (entity.getStatus()){
            case 1:
                tip = "“"+entity.getRecommendNickname()+"”是你的2度朋友，你已经发出了引荐申请，请你耐心等待TA的回复";
                break;
            case 2:
                tip = "“"+entity.getRecommendNickname()+"”是你的2度朋友，但是拒绝为你引荐，很抱歉，你们不能进行私聊\n" +
                        " \n" +
                        "拒绝理由："+entity.getContent();
                break;
            case 6:
                tip = "抱歉，您被“"+ entity.getRecommendNickname() +"”拉入黑名单，你们不能进行私聊";
                break;
            case 7:
                tip = "抱歉，“"+ entity.getRecommendNickname() +"”设置了隐私保护，你们不能进行私聊";
                break;
            case 8:
                tip = "推荐人“"+entity.getRecommendUserId()+"”设置了权限，无法聊天";
                break;
        }

        TextViewUtil.setText(this, R.id.txt_tip, tip);
    }

    @Override
    protected void loadData() {

    }
}