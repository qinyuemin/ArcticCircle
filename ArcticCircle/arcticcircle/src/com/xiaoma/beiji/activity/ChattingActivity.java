/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.common.android.lib.controls.view.pulltorefresh.PullToRefreshBase;
import com.common.android.lib.controls.view.pulltorefresh.PullToRefreshListView;
import com.makeapp.javase.util.DateUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.adapter.IMChattingMsgAdapter;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.view.InputCommentEditView;
import com.xiaoma.beiji.database.SimpleDBManager;
import com.xiaoma.beiji.manager.chatting.IMConfig;
import com.xiaoma.beiji.entity.IMXMPPMessageEntity;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;

import java.util.List;

/**
 * 类名称： ChatActivity
 * 类描述： 聊天UI
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月04 0:46
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ChattingActivity extends SimpleBaseActivity implements View.OnClickListener {
    private static final String TAG = ChattingActivity.class.getSimpleName();

    private UserInfoEntity friendUserEntity;
    private UserInfoEntity currentUserEntity;

    private Handler h = new Handler();
    private int offset = 0;
    // UI相关
    private PullToRefreshListView pullToRefreshListView;
    private IMChattingMsgAdapter imMsgAdapter;
    private TextView tv_title;
    private View ll_net_bad;
    private Animation animation;

    private InputCommentEditView inputCommentEditView;

    @Override
    protected String getActivityTitle() {
        return friendUserEntity.getNickname();
    }

    @Override
    protected void initIntent() {
        super.initIntent();
        friendUserEntity = (UserInfoEntity) getIntent().getSerializableExtra("entity");
        currentUserEntity = Global.getUserInfo();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_chatting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置intent过滤器处理的事件
        IntentFilter filter = new IntentFilter();
        filter.addAction(IMConfig.Action.ACTION_NEW_MSG);
        filter.addAction(IMConfig.Action.ACTION_UPDATE_MSG);
        filter.addAction(IMConfig.Action.ACTION_SYNCHRONIZE_OFFLINE_MSG);
        filter.addAction(IMConfig.Action.ACTION_SYNCHRONIZE_SELF_MSG);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void initComponents() {
        setTitleControlsInfo();

        animation = AnimationUtils.loadAnimation(this, R.anim.anim_in_from_bottom);
        ll_net_bad = findViewById(R.id.ll_net_bad);
        ll_net_bad.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.title_bar_title_txt);

        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.ptr_listview);
        pullToRefreshListView.getRefreshableView().setCacheColorHint(0);
        pullToRefreshListView.setEnabled(false);
        imMsgAdapter = new IMChattingMsgAdapter(this,currentUserEntity,friendUserEntity);
        pullToRefreshListView.setAdapter(imMsgAdapter);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                List<IMXMPPMessageEntity> list = SimpleDBManager.IMXMPPMessage.queryMessageList(currentUserEntity.getUserId(),friendUserEntity.getUserId(),offset);
                if (list != null && list.size() > 0) {
                    offset += 20;
                    imMsgAdapter.getDataList().addAll(0, list);
                    imMsgAdapter.notifyDataSetChanged();
                }
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshListView.onRefreshComplete();
                    }
                });
            }
        });
        inputCommentEditView = (InputCommentEditView) findViewById(R.id.icev_content);
        inputCommentEditView.setHint("请输入消息内容");
        inputCommentEditView.setOnSendClickListener(new InputCommentEditView.OnSendClickListener() {
            @Override
            public void onSendMessage(String message) {
                sendMessage(message);
            }
        });
    }

    private void sendMessage(String content) {
        // 消息入库
        IMXMPPMessageEntity newMsg = new IMXMPPMessageEntity();

        newMsg.setContent(content);
        newMsg.setType(IMConfig.MessageType.ChatTypeMessage);
        newMsg.setToUserId(friendUserEntity.getUserId());
        newMsg.setFromUserId(currentUserEntity.getUserId());
        newMsg.setContent(content);
        newMsg.setAvatar(friendUserEntity.getAvatar());
        newMsg.setNickname(friendUserEntity.getNickname());
        newMsg.setTime(DateUtil.getStringDateTime());
        newMsg.setHasRead(true);

        newMsg.setUserId(currentUserEntity.getUserId());
        newMsg.setChatUserId(friendUserEntity.getUserId());
        newMsg.setContent(content);
        newMsg.setBody(JSON.toJSONString(newMsg));

        SimpleDBManager.IMXMPPMessage.insertLocalMsg(newMsg);

        // 消息刷新到UI

        addMsgAndNotify(newMsg);

        // 消息发送到服务器
        HttpClientUtil.Message.messageSend(friendUserEntity.getUserId(), content, new AbsHttpResultHandler() {
            @Override
            public void onSuccess(int resultCode, String desc, Object data) {

            }

            @Override
            public void onFailure(int resultCode, String desc) {

            }
        });
    }

    @Override
    protected void loadData() {
        tv_title.setText(friendUserEntity.getNickname());
        offset = 0;
        List<IMXMPPMessageEntity> list = SimpleDBManager.IMXMPPMessage.queryMessageList(currentUserEntity.getUserId(),friendUserEntity.getUserId(),offset);
        // 添加模拟数据
        if (list != null && list.size() > 0) {
            offset += 20;
            imMsgAdapter.setDataList(list);
            imMsgAdapter.notifyDataSetChanged();
            h.post(new Runnable() {
                @Override
                public void run() {
                    pullToRefreshListView.getRefreshableView().setSelection(imMsgAdapter.getCount() - 1);
                    animListView(pullToRefreshListView.getRefreshableView());
                }
            });
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return super.dispatchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    /**
     * 更新界面显示
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (IMConfig.Action.ACTION_NEW_MSG.equals(action)) {
                IMXMPPMessageEntity msg = (IMXMPPMessageEntity) intent.getSerializableExtra("msg");
                // 判断是否该用户消息
                if (msg.getToUserId() == currentUserEntity.getUserId()) {
                    // 消息更新为已读

                    // 显示
                    addMsgAndNotify(msg);
                }
            }
        }
    };

    private void addMsgAndNotify(IMXMPPMessageEntity msg) {
        imMsgAdapter.getDataList().add(msg);
        offset++;
        imMsgAdapter.notifyDataSetChanged();
        pullToRefreshListView.getRefreshableView().setSelection(imMsgAdapter.getCount() - 1);
        animListView(pullToRefreshListView.getRefreshableView());
    }

    private void animListView(ListView listView) {
        int size = listView.getChildCount();
        for (int i = size - 1; i >= 0; i--) {
            listView.getChildAt(i).startAnimation(animation);
        }
    }

    @Override
    public void onClick(View view) {

    }
}