/**
 * 项目名： eyah
 * 包名： com.sttri.eyah.fragment
 * 版本信息： 1.0.0
 * Copyright (c) -版权所有
 */
package com.xiaoma.beiji.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import com.alibaba.fastjson.JSON;
import com.makeapp.android.adapter.ArrayListAdapter;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ToastUtil;
import com.makeapp.android.util.ViewUtil;
import com.makeapp.javase.lang.StringUtil;
import com.makeapp.javase.util.DataUtil;
import com.makeapp.javase.util.DateUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.adapter.AdapterViewUtil;
import com.xiaoma.beiji.base.SimpleFragment;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.view.swipemenulistview.SwipeMenu;
import com.xiaoma.beiji.controls.view.swipemenulistview.SwipeMenuCreator;
import com.xiaoma.beiji.controls.view.swipemenulistview.SwipeMenuItem;
import com.xiaoma.beiji.controls.view.swipemenulistview.SwipeMenuListView;
import com.xiaoma.beiji.database.SimpleDBManager;
import com.xiaoma.beiji.entity.IMXMPPMessageEntity;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.manager.chatting.IMConfig;
import com.xiaoma.beiji.util.CommUtil;
import com.xiaoma.beiji.util.IntentUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称： StatisticsFragment
 * 类描述： 私聊
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年06月08 11:57
 * 修改备注：
 *
 * @version 1.0.0'
 */
public class ChatFragment extends SimpleFragment{

    private SwipeMenuListView lstChat;
    private List<IMXMPPMessageEntity> entities;
    private ArrayListAdapter<IMXMPPMessageEntity> adapter;

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_chat;
    }

    protected void setTitleControlsInfo(View v) {
        TextViewUtil.setText(v, R.id.title_bar_title_txt, "私聊");
        ViewUtil.setViewVisibility(v,R.id.title_bar_left_layout, View.GONE);
    }

    @Override
    protected void initComponents(final View v) {
        setTitleControlsInfo(v);
        entities = new ArrayList<>();

        lstChat = (SwipeMenuListView) v.findViewById(R.id.lst_chat);
        adapter = new ArrayListAdapter<IMXMPPMessageEntity>(getFragmentActivity(), R.layout.lv_item_chat_record, entities) {
            @Override
            public void fillView(ViewGroup viewGroup, View view, IMXMPPMessageEntity chatRecordEntity, int i) {
                try {
                    final IMXMPPMessageEntity entity = entities.get(i);
                    String avatar = entity.getAvatar();
                    if (StringUtil.isValid(avatar)) {
                        ImageView img = (ImageView) view.findViewById(R.id.img_head);
                        ImageLoader.getInstance().displayImage(avatar, img);
                        img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                IntentUtil.goProfileActivity(getActivity(), String.valueOf(entity.getFromUserId()));
                            }
                        });
                    }
                    ViewUtil.setViewVisibility(view,R.id.red_point,entity.isHasRead() ? View.GONE: View.VISIBLE);
                    TextViewUtil.setText(view, R.id.txt_name, entity.getNickname());
                    TextViewUtil.setText(view, R.id.txt_time, AdapterViewUtil.getShowTime(entity.getTime()));
                    TextViewUtil.setText(view, R.id.txt_msg, entity.getContent());
                }catch (Exception ex){

                }
            }
        };
        lstChat.setAdapter(adapter);

        lstChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final IMXMPPMessageEntity entity = entities.get(i);
                UserInfoEntity friendUser= new UserInfoEntity();
                friendUser.setUserId(DataUtil.getInt(entity.getChatUserId()));
                friendUser.setAvatar(entity.getAvatar());
                friendUser.setNickname(entity.getNickname());
                IntentUtil.goChattingActivity(getFragmentActivity(),friendUser);

            }
        });

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem( getFragmentActivity());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(CommUtil.dip2px(getFragmentActivity(),90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        lstChat.setMenuCreator(creator);
        lstChat.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                IMXMPPMessageEntity msg = entities.get(position);
                SimpleDBManager.IMXMPPMessage.deleteSession(msg.getChatUserId());
                loadData();
            }
        });
    }

    @Override
    protected boolean IsReloadData() {
        return true;
    }

    @Override
    protected void loadData() {
        // 消息会话
        List<IMXMPPMessageEntity> sessionList = SimpleDBManager.IMXMPPMessage.querySessionList(Global.getUserId());
        entities.clear();
        entities.addAll(sessionList);
        adapter.notifyDataSetChanged();

        // 引荐消息

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            loadData();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置intent过滤器处理的事件
        IntentFilter filter = new IntentFilter();
        filter.addAction(IMConfig.Action.ACTION_NEW_MSG);
        filter.addAction(IMConfig.Action.ACTION_UPDATE_MSG);
        filter.addAction(IMConfig.Action.ACTION_SYNCHRONIZE_OFFLINE_MSG);
        filter.addAction(IMConfig.Action.ACTION_SYNCHRONIZE_SELF_MSG);
      getFragmentActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getFragmentActivity().unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (IMConfig.Action.ACTION_NEW_MSG.equals(action)) {
                IMXMPPMessageEntity msg = (IMXMPPMessageEntity) intent.getSerializableExtra("msg");
                // 判断是否该用户消息
                if (msg.getToUserId() == Global.getUserId()) {
                    loadData();
                }
            }
        }
    };
}
