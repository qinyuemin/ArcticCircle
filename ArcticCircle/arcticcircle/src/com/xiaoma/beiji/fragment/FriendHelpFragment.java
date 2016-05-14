/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.fragment
 * 版本信息： 1.0.0
 * Copyright (c) 2015
 */
package com.xiaoma.beiji.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.makeapp.javase.lang.StringUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.adapter.RecyclerViewAdapter;
import com.xiaoma.beiji.base.SimpleFragment;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.acinterface.IActionInterFace;
import com.xiaoma.beiji.controls.acinterface.ICommentInterface;
import com.xiaoma.beiji.controls.acinterface.IDomoreInterface;
import com.xiaoma.beiji.controls.dialog.ActionSheetDialog;
import com.xiaoma.beiji.controls.dialog.CommonDialogsInBase;
import com.xiaoma.beiji.controls.dialog.InputDialog;
import com.xiaoma.beiji.entity.CommentEntity;
import com.xiaoma.beiji.entity.FriendDynamicEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 类名称： FriendTrendsFragment
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年11月21 13:58
 * 修改备注：
 *
 * @version 1.0.0
 */
public class FriendHelpFragment extends SimpleFragment implements IActionInterFace {
    private RecyclerView lstFriend;
    private RecyclerViewAdapter adapter;
    private List<FriendDynamicEntity> entities;


    private String lastKeyId = "";

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_friend_help;
    }

    @Override
    protected void initComponents(View v) {
        lstFriend = (RecyclerView) v.findViewById(R.id.dynamic_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lstFriend.setLayoutManager(layoutManager);
        entities = new ArrayList<>();
        adapter = new RecyclerViewAdapter(getFragmentActivity(), entities,this);
        lstFriend.setAdapter(adapter);
//        lstFriend.setMode(PullToRefreshBase.Mode.BOTH);
//        lstFriend.setOnItemClickListener(this);
//        lstFriend.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                // 下拉 刷新
//                lastKeyId = "";
//                loadData();
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                // 上啦更多
//                FriendDynamicEntity last = entities.get(entities.size() - 1);
//                lastKeyId = last.getPageId();
//                loadData();
//            }
//        });

//        releaseUserId = getArguments().getString("releaseUserId");
    }

    @Override
    protected void loadData() {
        HttpClientUtil.Dynamic.dynamicGetList("", 2, "", lastKeyId, new AbsHttpResultHandler<FriendDynamicEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, FriendDynamicEntity data) {

            }

            @Override
            public void onSuccess(int resultCode, String desc, List<FriendDynamicEntity> data) {
                if (data != null) {
                    if (StringUtil.isInvalid(lastKeyId)) {
                        entities.clear();
                    }
                    entities.addAll(data);
                    adapter.notifyDataSetChanged();
                }

//                if (lstFriend.isRefreshing()) {
//                    lstFriend.onRefreshComplete();
//                }
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(getFragmentActivity(), desc);
//                if (lstFriend.isRefreshing()) {
//                    lstFriend.onRefreshComplete();
//                }
            }
        });
    }

    boolean isFirst = true;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            if(Global.isNeedRefreshIndex()){
                loadData();
                Global.setIsNeedRefreshIndex(false);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Global.isNeedRefreshIndex()){
            loadData();
            Global.setIsNeedRefreshIndex(false);
        }
    }
    protected CommonDialogsInBase commonDialogsInBase = new CommonDialogsInBase();
    protected void showProgressDialog() {
        commonDialogsInBase.showProgressDialog(getActivity(), true, null);
    }

    protected void closeProgressDialog() {
        commonDialogsInBase.closeProgressDialog();
    }

    @Override
    public void dynamicDoPraise(FriendDynamicEntity entity, final AbsHttpResultHandler handler) {
        showProgressDialog();
        HttpClientUtil.Dynamic.dynamicDoPraise(entity.getReleaseId(), entity.isHaveFavorite() ? 2 : 1, new AbsHttpResultHandler() {
            @Override
            public void onSuccess(int resultCode, String desc, Object data) {
                handler.onSuccess(resultCode, desc, data);
                closeProgressDialog();
                ToastUtil.showToast(getContext(), "成功");
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                handler.onFailure(resultCode, desc);
                closeProgressDialog();
                ToastUtil.showToast(getContext(), "失败" + desc);
            }
        });
    }


    @Override
    public void dynamicDoFavorite(FriendDynamicEntity entity, AbsHttpResultHandler handler) {

    }

    @Override
    public void dynamicDoComment(final FriendDynamicEntity entity, final ICommentInterface handler) {
        commonDialogsInBase.showInputDialog(getActivity(), new InputDialog.InputCallBack() {
            @Override
            public void success(final String content) {
                showProgressDialog();
                HttpClientUtil.Dynamic.dynamicDoComment(entity.getReleaseId(), entity.getUserId(), content, new AbsHttpResultHandler() {
                    @Override
                    public void onSuccess(int resultCode, String desc, Object data) {
                        closeProgressDialog();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = format.format(new Date());
                        CommentEntity commentEntity = new CommentEntity();
                        commentEntity.setCommentUserNickname(Global.getUserInfo().getNickname());
                        commentEntity.setCommentUserAvatar(Global.getUserInfo().getAvatar());
                        commentEntity.setCreateTime(time);
                        commentEntity.setCommentContent(content);
                        handler.success(commentEntity);
                        ToastUtil.showToast(getContext(), "评论成功");
                    }

                    @Override
                    public void onFailure(int resultCode, String desc) {
                        closeProgressDialog();
                        ToastUtil.showToast(getContext(), "评论失败" + desc);
                    }
                });
            }
        });
    }

    @Override
    public void dynamicMore(final FriendDynamicEntity entity, final IDomoreInterface callBack) {
        try{
            final List<String> items = new ArrayList<>();
            if(Global.getUserId() == Integer.valueOf(entity.getUserId())){
                items.add(IDomoreInterface.TYPE_DELETE);
                items.add(IDomoreInterface.TYPE_JUBAO);
            }else{
                items.add(IDomoreInterface.TYPE_PINGBI);
                items.add(IDomoreInterface.TYPE_JUBAO);
                items.add(IDomoreInterface.TYPE_SHOUCANG);
            }
            commonDialogsInBase.showChooseDialog(getActivity(), items, new ActionSheetDialog.OnSheetItemClickListener() {
                @Override
                public void onClick(int which) {
                    String opreate = items.get(which-1);
                    switch (opreate){
                        case IDomoreInterface.TYPE_DELETE:
                            deleteDynamic(entity,callBack);
                            break;
                        case IDomoreInterface.TYPE_SHOUCANG:
                            doFavorite(entity,callBack);
                            break;
                        case IDomoreInterface.TYPE_PINGBI:
                            hideDynamic(entity,callBack);
                            break;
                        case IDomoreInterface.TYPE_JUBAO:
                            reportDynamic(entity,callBack);
                            break;
                    }
                }
            });
        }catch (Exception e){

        }
    }

    private void doFavorite(final FriendDynamicEntity entity,final IDomoreInterface callBack){
//        int type = entity.isHaveFavorite()?2:1;
        showProgressDialog();
        HttpClientUtil.Dynamic.dynamicDoFavorite(entity.getReleaseId(), 1, new AbsHttpResultHandler() {
            @Override
            public void onSuccess(int resultCode, String desc, Object data) {
                callBack.success(entity, IDomoreInterface.TYPE_SHOUCANG);
                ToastUtil.showToast(getActivity(), "收藏成功");
                closeProgressDialog();
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                closeProgressDialog();
                ToastUtil.showToast(getActivity(), "收藏失败" + desc);
            }
        });
    }

    private void deleteDynamic(final FriendDynamicEntity entity,final IDomoreInterface callBack){
        showProgressDialog();
        HttpClientUtil.Dynamic.dynamicDoDelete(entity.getReleaseId(), new AbsHttpResultHandler() {
            @Override
            public void onSuccess(int resultCode, String desc, Object data) {
                callBack.success(entity, IDomoreInterface.TYPE_DELETE);
                ToastUtil.showToast(getActivity(), "删除成功");
                closeProgressDialog();
            }

            @Override
            public void onSuccess(int resultCode, String desc, List data) {
                super.onSuccess(resultCode, desc, data);
                callBack.success(entity, IDomoreInterface.TYPE_DELETE);
                ToastUtil.showToast(getActivity(), "删除成功");
                closeProgressDialog();
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                closeProgressDialog();
                ToastUtil.showToast(getActivity(), "删除失败" + desc);
            }
        });
    }

    private void reportDynamic(final FriendDynamicEntity entity,final IDomoreInterface callBack){
        if(Global.getUserId() == Integer.valueOf(entity.getUserId())) //自己不能举报自己吧
            return;
        showProgressDialog();
        HttpClientUtil.Dynamic.dynamicDoReport(entity.getReleaseId(), new AbsHttpResultHandler() {
            @Override
            public void onSuccess(int resultCode, String desc, Object data) {
                callBack.success(entity, IDomoreInterface.TYPE_JUBAO);
                ToastUtil.showToast(getActivity(), "举报成功");
                closeProgressDialog();
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                closeProgressDialog();
                ToastUtil.showToast(getActivity(), "举报失败" + desc);
            }
        });
    }

    private void hideDynamic(final FriendDynamicEntity entity,final IDomoreInterface callBack){
        showProgressDialog();
        HttpClientUtil.Dynamic.dynamicDoShield(entity.getReleaseId(), new AbsHttpResultHandler() {
            @Override
            public void onSuccess(int resultCode, String desc, Object data) {
                callBack.success(entity, IDomoreInterface.TYPE_PINGBI);
                ToastUtil.showToast(getActivity(), "屏蔽成功");
                closeProgressDialog();
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                closeProgressDialog();
                ToastUtil.showToast(getActivity(), "屏蔽失败"+desc);
            }
        });
    }
}
