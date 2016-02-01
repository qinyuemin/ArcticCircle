/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import com.alibaba.fastjson.JSON;
import com.makeapp.android.util.EditTextUtil;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ToastUtil;
import com.makeapp.android.util.ViewUtil;
import com.makeapp.javase.lang.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.database.SimpleBaseDB;
import com.xiaoma.beiji.database.SimpleDBManager;
import com.xiaoma.beiji.entity.IMSystemMessageEntity;
import com.xiaoma.beiji.entity.RecommendMsgEntity;
import com.xiaoma.beiji.entity.SystemMsgEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;

/**
 *
 * 类名称： ReferralCheckActivity
 * 类描述： 处理引荐 消息
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月30 15:40
 * 修改备注：
 * @version 1.0.0
 *
 */
public class ReferralCheckActivity extends SimpleBaseActivity {
    private static final String TAG = ReferralCheckActivity.class.getSimpleName();

    private IMSystemMessageEntity msg = null;

    private EditText edtContent;

    @Override
    protected String getActivityTitle() {
        return "请求引荐";
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_referral_check ;
    }

    @Override
    protected void initIntent() {
        super.initIntent();
        msg = (IMSystemMessageEntity)getIntent().getSerializableExtra("msg");
    }

    @Override
    protected void initComponents() {
        setTitleControlsInfo();

        final RecommendMsgEntity msgEntity = JSON.parseObject(msg.getBody(),RecommendMsgEntity.class);

        ImageLoader.getInstance().displayImage(msgEntity.getRecommendUserAvatar(), (ImageView) findViewById(R.id.img_head));
        String tip = msgEntity.getRecommendUserNickname() +"向您推荐" + msgEntity.getFromUserNickname();
        TextViewUtil.setText(this,R.id.txt_tip,tip);
        TextViewUtil.setText(this,R.id.txt_cause,msgEntity.getContent());
        TextViewUtil.setText(this,R.id.txt_time,msgEntity.getTime());

        ViewUtil.setViewOnClickListener(this, R.id.btn_ok, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit(msgEntity,1);
            }
        });

        ViewUtil.setViewOnClickListener(this, R.id.btn_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit(msgEntity,2);
            }
        });

    }

    private void submit(RecommendMsgEntity msgEntity,int status) {
        String content = "";
        if(status == 2){
            content = EditTextUtil.getTextString(this,R.id.edt_content);
            if(StringUtil.isInvalid(content)){
                RadioGroup group = (RadioGroup) findViewById(R.id.rg_1);

                if(group.getCheckedRadioButtonId() == R.id.rg_1){
                    content = "我跟TA不熟";
                }else{
                    content = "不太方便";
                }
            }
        }
        HttpClientUtil.Friend.friendRecommendSubmit(msgEntity.getFromUserId(), msgEntity.getRecommendUserId(), status,content , new AbsHttpResultHandler() {
            @Override
            public void onSuccess(int resultCode, String desc, Object data) {
                ToastUtil.show(ReferralCheckActivity.this,"处理成功");
                SimpleDBManager.IMSystemMessage.deleteMsg(msg.getId());
                finish();
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.show(ReferralCheckActivity.this,"提交失败:"+desc);
            }
        });
    }

    @Override
    protected void loadData() {

    }
}