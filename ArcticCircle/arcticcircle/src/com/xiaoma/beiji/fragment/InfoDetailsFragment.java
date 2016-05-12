package com.xiaoma.beiji.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoma.beiji.R;
import com.xiaoma.beiji.adapter.RecyclerViewAdapter;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.acinterface.IActionInterFace;
import com.xiaoma.beiji.controls.acinterface.ICommentInterface;
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
 * Author       : zhangqibo
 * Date         : 2015-04-01
 * Time         : 15:09
 * Description  :
 */
public class InfoDetailsFragment extends Fragment implements IActionInterFace {
    private RecyclerView mRecyclerView;
    private List<FriendDynamicEntity> dynamicEntities;
    private RecyclerViewAdapter adapter;
    protected CommonDialogsInBase commonDialogsInBase = new CommonDialogsInBase();
    private static final int REQUEST_CODE_COMMENT = 0;

    private void initDate(){
        if(dynamicEntities == null){
            dynamicEntities = new ArrayList<>();
        }
    }

    public void setList(List list){
        this.dynamicEntities = list;
        if(mRecyclerView!=null){
            adapter = new RecyclerViewAdapter(getActivity(), dynamicEntities,this);
            mRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_layout_list, container, false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        adapter = new RecyclerViewAdapter(getActivity(), dynamicEntities,this);
        mRecyclerView.setAdapter(adapter);
        return mRecyclerView;
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
                        ToastUtil.showToast(getContext(),"评论成功");
                    }

                    @Override
                    public void onFailure(int resultCode, String desc) {
                        closeProgressDialog();
                        ToastUtil.showToast(getContext(), "评论失败"+desc);
                    }
                });
            }
        });
    }

    @Override
    public void dynamicMore(FriendDynamicEntity entity) {

    }

    protected void showProgressDialog() {
        commonDialogsInBase.showProgressDialog(getActivity(),false,null);
    }


    protected void closeProgressDialog() {
        commonDialogsInBase.closeProgressDialog();
    }
}
