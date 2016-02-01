/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.alibaba.fastjson.JSON;
import com.makeapp.android.adapter.ArrayListAdapter;
import com.makeapp.android.util.ImageViewUtil;
import com.makeapp.android.util.TextViewUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.database.SimpleDBManager;
import com.xiaoma.beiji.entity.IMSystemMessageEntity;
import com.xiaoma.beiji.entity.IMXMPPMessageEntity;
import com.xiaoma.beiji.entity.SystemMsgEntity;
import com.xiaoma.beiji.manager.chatting.IMConfig;
import com.xiaoma.beiji.util.IntentUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称： MessageActivity
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月03 22:00
 * 修改备注：
 *
 * @version 1.0.0
 */
public class MessageListActivity extends SimpleBaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = MessageListActivity.class.getSimpleName();

    private ListView lstChat;
    private List<IMSystemMessageEntity> entities;
    private ArrayListAdapter<IMSystemMessageEntity> adapter;

    private int offset = 0;

    @Override
    protected String getActivityTitle() {
        return "我的消息";
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message_list;
    }

    @Override
    protected void initComponents() {
        setTitleControlsInfo();
        entities = new ArrayList<>();

        lstChat = (ListView) findViewById(R.id.lst_chat);
        adapter = new ArrayListAdapter<IMSystemMessageEntity>(this, R.layout.lv_item_chat_record, entities) {
            @Override
            public void fillView(ViewGroup viewGroup, View view, IMSystemMessageEntity chatRecordEntity, int i) {
                String type = chatRecordEntity.getType();
                SystemMsgEntity msg = JSON.parseObject(chatRecordEntity.getBody(),SystemMsgEntity.class);
                TextViewUtil.setText(view,R.id.txt_time,msg.getTime());
                TextViewUtil.setText(view,R.id.txt_name,msg.getTitle());
                TextViewUtil.setText(view,R.id.txt_msg,msg.getContent());
                int resId = R.drawable.ic_comment1;
                switch (type){
                    case IMConfig.MessageType.ChatTypeDynamicPraise:
                        resId = R.drawable.ic_praise1;
                        break;
                    case IMConfig.MessageType.ChatTypeDynamicCollection:
                        resId = R.drawable.ic_collection1;
                        break;
                    case IMConfig.MessageType.ChatTypeHelp:
                        resId = R.drawable.ic_request1;
                        break;
                    case IMConfig.MessageType.ChatTypeDynamicComment:
                    case IMConfig.MessageType.ChatTypeDynamicReply:
                    case IMConfig.MessageType.ChatTypeHelpComment:
                    case IMConfig.MessageType.ChatTypeHelpReply:
                        resId = R.drawable.ic_comment1;
                        break;
                    case IMConfig.MessageType.ChatTypeRecommendResult:

                        break;
                }
                ImageViewUtil.setImageSrcId(view,R.id.img_head,resId);
            }
        };
        lstChat.setAdapter(adapter);
        lstChat.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        IMSystemMessageEntity entity = (IMSystemMessageEntity)adapter.getItem(i);
        SystemMsgEntity msg = JSON.parseObject(entity.getBody(),SystemMsgEntity.class);
        String releaseId = String.valueOf(msg.getReleaseId());
        switch (entity.getType()){
            case IMConfig.MessageType.ChatTypeDynamicPraise:
            case IMConfig.MessageType.ChatTypeDynamicCollection:
            case IMConfig.MessageType.ChatTypeDynamicComment:
            case IMConfig.MessageType.ChatTypeDynamicReply:
                IntentUtil.goFriendDynamicDetailActivity(this, releaseId);
                break;
            case IMConfig.MessageType.ChatTypeHelp:
            case IMConfig.MessageType.ChatTypeHelpComment:
            case IMConfig.MessageType.ChatTypeHelpReply:
                IntentUtil.goFriendHelpDetailActivity(this,releaseId);
                break;
            case IMConfig.MessageType.ChatTypeRecommendResult:
                IntentUtil.goReferralCheckActivity(this,entity);
                break;
        }
    }

    @Override
    protected void loadData() {
        List<IMSystemMessageEntity> list = SimpleDBManager.IMSystemMessage.queryMessageList(Global.getUserId(), offset);
        if (list != null && list.size() > 0) {
            offset += 20;
            entities.addAll(list);
            adapter.notifyDataSetChanged();

        }
    }


}