package com.xiaoma.beiji.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.android.lib.base.BaseActivity;
import com.common.android.lib.util.TimeUtil;
import com.makeapp.android.util.ImageViewUtil;
import com.makeapp.android.util.ViewUtil;
import com.makeapp.javase.lang.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.acinterface.ICommentInterface;
import com.xiaoma.beiji.controls.acinterface.IDomoreInterface;
import com.xiaoma.beiji.controls.dialog.ActionSheetDialog;
import com.xiaoma.beiji.controls.dialog.CommonDialogsInBase;
import com.xiaoma.beiji.controls.dialog.InputDialog;
import com.xiaoma.beiji.controls.view.CircularImage;
import com.xiaoma.beiji.entity.CommentEntity;
import com.xiaoma.beiji.entity.FriendDynamicEntity;
import com.xiaoma.beiji.entity.PicEntity;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.CommUtil;
import com.xiaoma.beiji.util.IntentUtil;
import com.xiaoma.beiji.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhangqibo on 2016/5/21.
 */
public class ArticleDynamicDetailActivity extends BaseActivity implements View.OnClickListener, ImageLoadingListener {

    private FriendDynamicEntity entity;

    @Override
    protected String getActivityTitle() {
        return "长文详情";
    }

    @Override
    protected int getContentLayoutId() {
        entity = (FriendDynamicEntity) getIntent().getSerializableExtra("entity");
        return R.layout.activity_friend_article_detail;
    }

    @Override
    protected void initComponents() {
        setTitleControlsInfo();
        View view = findViewById(R.id.ll_root);
        ArticleViewHolder holder = new ArticleViewHolder(view);
        initArticleViewHolder(holder,entity);
    }

    @Override
    protected void loadData() {

    }

    private void initContent(){
        TextView textView = (TextView) findViewById(R.id.text_content);
        String content = entity.getContent();
        SpannableString string = new SpannableString(content);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.outWidth = CommUtil.getScreenWidth(this);
        options.inScaled = true;
        List<PicEntity> list = entity.getPic();
        for(PicEntity picEntity :list ){
            if(picEntity.getBitmap()!=null){
                ImageSpan imageSpan = new ImageSpan(picEntity.getBitmap());
                string.setSpan(imageSpan, picEntity.getLocation(), picEntity.getLocation() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        textView.setText(string);
    }

    @Override
    protected void setTitleControlsInfo() {
        super.setTitleControlsInfo();
//        ViewUtil.setViewVisibility(this, R.id.title_bar_right_img, View.VISIBLE);
//        ImageViewUtil.setImageSrcId(this, R.id.title_bar_right_img, R.drawable.ic_share);
//        ViewUtil.setViewOnClickListener(this, R.id.title_bar_right_layout, this);
    }

    @Override
    public void onClick(View v) {

    }

    protected CommonDialogsInBase commonDialogsInBase = new CommonDialogsInBase();
    protected void showProgressDialog() {
        commonDialogsInBase.showProgressDialog(this, true, null);
    }

    protected void closeProgressDialog() {
        commonDialogsInBase.closeProgressDialog();
    }

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
            commonDialogsInBase.showChooseDialog(ArticleDynamicDetailActivity.this, items, new ActionSheetDialog.OnSheetItemClickListener() {
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
                ToastUtil.showToast(ArticleDynamicDetailActivity.this, "收藏成功");
                closeProgressDialog();
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                closeProgressDialog();
                ToastUtil.showToast(ArticleDynamicDetailActivity.this, "收藏失败" + desc);
            }
        });
    }

    private void deleteDynamic(final FriendDynamicEntity entity,final IDomoreInterface callBack){
        showProgressDialog();
        HttpClientUtil.Dynamic.dynamicDoDelete(entity.getReleaseId(), new AbsHttpResultHandler() {
            @Override
            public void onSuccess(int resultCode, String desc, Object data) {
                callBack.success(entity, IDomoreInterface.TYPE_DELETE);
                ToastUtil.showToast(ArticleDynamicDetailActivity.this, "删除成功");
                closeProgressDialog();
            }

            @Override
            public void onSuccess(int resultCode, String desc, List data) {
                super.onSuccess(resultCode, desc, data);
                callBack.success(entity, IDomoreInterface.TYPE_DELETE);
                ToastUtil.showToast(ArticleDynamicDetailActivity.this, "删除成功");
                closeProgressDialog();
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                closeProgressDialog();
                ToastUtil.showToast(ArticleDynamicDetailActivity.this, "删除失败" + desc);
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
                ToastUtil.showToast(ArticleDynamicDetailActivity.this, "举报成功");
                closeProgressDialog();
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                closeProgressDialog();
                ToastUtil.showToast(ArticleDynamicDetailActivity.this, "举报失败" + desc);
            }
        });
    }

    private void hideDynamic(final FriendDynamicEntity entity,final IDomoreInterface callBack){
        showProgressDialog();
        HttpClientUtil.Dynamic.dynamicDoShield(entity.getReleaseId(), new AbsHttpResultHandler() {
            @Override
            public void onSuccess(int resultCode, String desc, Object data) {
                callBack.success(entity, IDomoreInterface.TYPE_PINGBI);
                ToastUtil.showToast(ArticleDynamicDetailActivity.this, "屏蔽成功");
                closeProgressDialog();
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                closeProgressDialog();
                ToastUtil.showToast(ArticleDynamicDetailActivity.this, "屏蔽失败" + desc);
            }
        });
    }

    public void dynamicDoComment(final FriendDynamicEntity entity, final ICommentInterface handler) {
        commonDialogsInBase.showInputDialog(ArticleDynamicDetailActivity.this, new InputDialog.InputCallBack() {
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
                        ToastUtil.showToast(ArticleDynamicDetailActivity.this, "评论成功");
                    }

                    @Override
                    public void onFailure(int resultCode, String desc) {
                        closeProgressDialog();
                        ToastUtil.showToast(ArticleDynamicDetailActivity.this, "评论失败" + desc);
                    }
                });
            }
        });
    }

    public void dynamicDoPraise(FriendDynamicEntity entity, final AbsHttpResultHandler handler) {
        showProgressDialog();
        HttpClientUtil.Dynamic.dynamicDoPraise(entity.getReleaseId(), entity.isHaveFavorite() ? 2 : 1, new AbsHttpResultHandler() {
            @Override
            public void onSuccess(int resultCode, String desc, Object data) {
                handler.onSuccess(resultCode, desc, data);
                closeProgressDialog();
                ToastUtil.showToast(ArticleDynamicDetailActivity.this, "成功");
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                handler.onFailure(resultCode, desc);
                closeProgressDialog();
                ToastUtil.showToast(ArticleDynamicDetailActivity.this, "失败" + desc);
            }
        });
    }

    private void doShare(AbsHttpResultHandler absHttpResultHandler){
        commonDialogsInBase.shoShareDialog(this,entity,absHttpResultHandler);
    }

    private void initArticleViewHolder(final ArticleViewHolder holder,final FriendDynamicEntity entity){
        holder.flagText.setText(entity.getReleaseTypeTitle());
        if(StringUtil.isValid(entity.getAvatar())){
            ImageLoader.getInstance().displayImage(entity.getAvatar(), holder.headImage);
        }
        holder.timeTextView.setText(TimeUtil.getDisplayTime(entity.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        holder.headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userID = Integer.valueOf(entity.getUserId());
                if(Global.getUserId() != userID){
                    IntentUtil.goProfileActivity(ArticleDynamicDetailActivity.this, userID);
                }

            }
        });
        List<PicEntity> picLists = entity.getPic();
        if(picLists!=null){
            for (PicEntity picEntity:picLists) {
                ImageLoader.getInstance().loadImage(picEntity.getPicUrl(),ArticleDynamicDetailActivity.this);
            }
        }
        holder.nameTextView.setText(entity.getNickName());
        holder.descriptionTextView.setText(entity.getContent());

        final List<UserInfoEntity> lickUser = entity.getPraiseUsers() != null ? entity.getPraiseUsers() : new ArrayList<UserInfoEntity>();
        holder.likeLabel.setText(String.format("%d",lickUser.size()));

        final List<String> shareUsers = entity.getShare_user_nickname()!= null ? entity.getShare_user_nickname() : new ArrayList<String>();
        holder.shareLabel.setText(String.format("%d",shareUsers.size()));
        if(shareUsers.size()>0){
            StringBuffer contentBuffer = new StringBuffer();
            contentBuffer.append("已有");
            for(int i= 0; i< shareUsers.size(); i++){
                if(i== 0){
                    contentBuffer.append("@").append(shareUsers.get(i));
                }else {
                    contentBuffer.append("、@").append(shareUsers.get(i));
                }
            }
            contentBuffer.append("推荐");
            holder.shareUsers.setText(contentBuffer);
            holder.shareUsers.setVisibility(View.VISIBLE);
        }else {
            holder.shareUsers.setVisibility(View.GONE);
        }


        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AAA", "moreBtn onClick");
                dynamicMore(entity, new IDomoreInterface() {
                    @Override
                    public void success(FriendDynamicEntity entity, String type) {
                        switch (type){
                            case IDomoreInterface.TYPE_DELETE:
//                                    list.remove(entity);
//                                    notifyDataSetChanged();
                                break;
                            case IDomoreInterface.TYPE_PINGBI:
//                                    list.remove(entity);
//                                    notifyDataSetChanged();
                                break;
                            case IDomoreInterface.TYPE_SHOUCANG:
                                break;
                            case IDomoreInterface.TYPE_JUBAO:
                                break;
                        }
                    }
                });
                }
        });
        if(entity.isHavePraise()){
            holder.likeBtn.setImageResource(R.drawable.ic_liked);
        }else{
            holder.likeBtn.setImageResource(R.drawable.icon_add_like);
        }
        holder.likeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AAA", "likeBtn onClick");

                dynamicDoPraise(entity, new AbsHttpResultHandler() {
                    @Override
                    public void onSuccess(int resultCode, String desc, Object data) {
                        entity.setHavePraise(!entity.isHavePraise());
                        if (entity.isHavePraise()) {
                            holder.likeBtn.setImageResource(R.drawable.ic_liked);
                            lickUser.add(Global.getUserInfo());
                        } else {
                            holder.likeBtn.setImageResource(R.drawable.icon_add_like);
                            Iterator<UserInfoEntity> iterator = lickUser.iterator();
                            while (iterator.hasNext()) {
                                UserInfoEntity entity1 = iterator.next();
                                if (entity1.getUserId() == Global.getUserId()) {
                                    iterator.remove();
                                }
                            }
                        }
                        holder.likeLabel.setText(String.format("%d", lickUser.size()));
                    }

                    @Override
                    public void onFailure(int resultCode, String desc) {

                    }
                });
            }
        });
        holder.shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AAA", "shareLayout onClick");
                doShare(new AbsHttpResultHandler() {
                    @Override
                    public void onSuccess(int resultCode, String desc, Object data) {
                        shareUsers.add(Global.getUserInfo().getNickname());
                        if (shareUsers.size() > 0) {
                            StringBuffer contentBuffer = new StringBuffer();
                            contentBuffer.append("已有");
                            for (int i = 0; i < shareUsers.size(); i++) {
                                if (i == 0) {
                                    contentBuffer.append("@").append(shareUsers.get(i));
                                } else {
                                    contentBuffer.append("、@").append(shareUsers.get(i));
                                }
                            }
                            contentBuffer.append("推荐");
                            holder.shareUsers.setText(contentBuffer);
                            holder.shareUsers.setVisibility(View.VISIBLE);
                        } else {
                            holder.shareUsers.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(int resultCode, String desc) {

                    }
                });
            }
        });
        holder.addCommentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AAA", "addCommentLayout onClick");
                dynamicDoComment(entity, new ICommentInterface() {
                    @Override
                    public void success(CommentEntity entity) {
                    }
                });
            }
        });
    }

    @Override
    public void onLoadingStarted(String s, View view) {

    }

    @Override
    public void onLoadingFailed(String s, View view, FailReason failReason) {

    }

    @Override
    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
        List<PicEntity> picEntities = entity.getPic();
        for(PicEntity entity : picEntities){
            if(!TextUtils.isEmpty(s)&&s.equals(entity.getPicUrl())){
                entity.setBitmap(bitmap);
                initContent();
            }
        }
    }

    @Override
    public void onLoadingCancelled(String s, View view) {

    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        public CircularImage headImage;
        public TextView nameTextView;
        public ImageView addComment;
        public TextView descriptionTextView;
        public TextView likeLabel;
        public TextView shareLabel;
        public TextView shareUsers;
        public TextView timeTextView;
        public TextView flagText;
        public ImageView moreBtn;
        public ImageView likeBtn;
        public RelativeLayout likeLayout;
        public RelativeLayout shareLayout;
        public RelativeLayout addCommentLayout;


        public ArticleViewHolder(View view) {
            super(view);
            addComment = (ImageView) view.findViewById(R.id.item_btn_add_comment);
            descriptionTextView = (TextView) view.findViewById(R.id.text_content);
            nameTextView = (TextView) view.findViewById(R.id.text_item_name);
            headImage = (CircularImage) view.findViewById(R.id.img_head);
            likeLabel = (TextView) view.findViewById(R.id.item_text_label_like);
            shareLabel = (TextView) view.findViewById(R.id.item_text_label_share);
            shareUsers = (TextView) view.findViewById(R.id.text_recommend_user);
            timeTextView = (TextView) view.findViewById(R.id.text_item_time);
            moreBtn = (ImageView) view.findViewById(R.id.btn_more);
            likeBtn = (ImageView) view.findViewById(R.id.btn_like);
            likeLayout = (RelativeLayout) view.findViewById(R.id.layout_like);
            shareLayout = (RelativeLayout) view.findViewById(R.id.layout_recommend);
            addCommentLayout = (RelativeLayout) view.findViewById(R.id.layout_add_comment);
            flagText = (TextView) view.findViewById(R.id.text_item_flag);
        }
    }
}
