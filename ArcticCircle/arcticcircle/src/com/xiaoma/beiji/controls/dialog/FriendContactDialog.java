/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.controls.dialog
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.controls.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import com.makeapp.android.adapter.ArrayListAdapter;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ViewUtil;
import com.makeapp.javase.util.DataUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.entity.ShopEntity;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.manager.chatting.ChattingUtil;
import com.xiaoma.beiji.util.CommUtil;
import com.xiaoma.beiji.util.IntentUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称： FriendContactDialog
 * 类描述： 联系店铺
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月03 21:04
 * 修改备注：
 *
 * @version 1.0.0
 */
public class FriendContactDialog extends Dialog implements View.OnClickListener {
    private Activity context;
    private ShopEntity entity;

    public FriendContactDialog(Activity context, ShopEntity entity) {
        super(context, R.style.base_dialog);
        this.context = context;
        this.entity = entity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.5f;
//        lp.width = getWindow().getWindowManager().getDefaultDisplay().getWidth();
//        lp.x = 0;
//        lp.y = getWindow().getWindowManager().getDefaultDisplay().getHeight();
        // 设置显示位置
        this.onWindowAttributesChanged(lp);
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //这句必须加上，要不然dialog上会有一段空白（标题栏）
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_friend_contact);

        ViewUtil.setViewOnClickListener(this, R.id.txt_close, this);
        ViewUtil.setViewOnClickListener(this, R.id.rl_msg, this);
        ViewUtil.setViewOnClickListener(this, R.id.rl_location, this);
        ViewUtil.setViewOnClickListener(this, R.id.rl_phone, this);
        ViewUtil.setViewOnClickListener(this, R.id.rl_tel, this);

        ViewUtil.setViewOnClickListener(this, R.id.ll_dz, this);
        ViewUtil.setViewOnClickListener(this, R.id.ll_jd, this);
        ViewUtil.setViewOnClickListener(this, R.id.ll_tb, this);
        ViewUtil.setViewOnClickListener(this, R.id.ll_tm, this);
        ViewUtil.setViewOnClickListener(this, R.id.ll_wx, this);

        if (entity != null) {
            TextViewUtil.setText(this, R.id.txt2, entity.getAddress());
            TextViewUtil.setText(this, R.id.txt3, entity.getPhone());
            TextViewUtil.setText(this, R.id.txt4, entity.getTel());

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_msg:
                // 聊天
                UserInfoEntity userInfoEntity = new UserInfoEntity();
                userInfoEntity.setUserId(DataUtil.getInt(entity.getUserId()));
                userInfoEntity.setNickname(entity.getShowName());
                ChattingUtil.goChatting(context,userInfoEntity);
                break;
            case R.id.rl_location:

                break;
            case R.id.rl_phone:
                CommUtil.call(context, entity.getPhone());
                break;
            case R.id.rl_tel:
                CommUtil.call(context, entity.getTel());
                break;
            case R.id.ll_dz:
                CommUtil.openWebView(context,"http://www.dianping.com/");
                break;
            case R.id.ll_jd:
                CommUtil.openWebView(context,"http://www.jd.com/");
                break;
            case R.id.ll_tb:
                CommUtil.openWebView(context,entity.getShopOnlineEntity().getTbUrl());
                break;
            case R.id.ll_tm:
                CommUtil.openWebView(context,"http://www.tmall.com/");
                break;
            case R.id.ll_wx:
                CommUtil.openApp(context, "com.tencent.mm");
                break;
            case R.id.txt_close:
                dismiss();
                break;
        }
    }

    @Override
    public void show() {
        super.show();

        Display display = context.getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (display.getWidth()) - display.getWidth() / 10; //设置宽度
        getWindow().setAttributes(lp);
    }


}
