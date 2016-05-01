package com.xiaoma.beiji.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.controls.view.CircularImage;
import com.xiaoma.beiji.controls.view.ImgPagerView;
import com.xiaoma.beiji.entity.CommentEntity;
import com.xiaoma.beiji.entity.FriendDynamicEntity;
import com.xiaoma.beiji.entity.PicEntity;
import com.xiaoma.beiji.entity.UserInfoEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author       : zhangqibo
 * Date         : 2015-04-02
 * Time         : 09:47
 * Description  :
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List list;

    private Context mContext;

    private static final int TYPE_DIANPING = 0;
    private static final int TYPE_WENWEN = 1;
    private static final int TYPE_CHANGWEN = 2;
    private static final int TYPE_ZHUANFA_DIANPING = 3;
    private static final int TYPE_ZHUANFA_WENWEN = 4;
    private static final int TYPE_ZHUANFA_CHANGWEN  = 5;
    private static final int TYPE_GUANGGAO  = 6;
    private static final int TYPE_TUIJIAN_PINGLUN  = 7;
    private static final int TYPE_FRIEND_JOIN  = 8;

    public RecyclerViewAdapter(Context mContext,List things) {
        this.mContext = mContext;
        this.list = things;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder;
        switch (viewType){
            case TYPE_DIANPING:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dynamic, null, false);
                holder = new DynamicViewHolder(view);
                break;
            case TYPE_WENWEN:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dynamic, null, false);
                holder = new DynamicViewHolder(view);
                break;
            case TYPE_CHANGWEN:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_article, null, false);
                holder = new ArticleViewHolder(view);
                break;
            case TYPE_ZHUANFA_DIANPING:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dynamic, null, false);
                holder = new DynamicViewHolder(view);
                break;
            case TYPE_ZHUANFA_WENWEN:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dynamic, null, false);
                holder = new DynamicViewHolder(view);
                break;
            case TYPE_ZHUANFA_CHANGWEN:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_article, null, false);
                holder = new ArticleViewHolder(view);
                break;
            case TYPE_GUANGGAO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dynamic, null, false);
                holder = new DynamicViewHolder(view);
                break;
            case TYPE_TUIJIAN_PINGLUN:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dynamic, null, false);
                holder = new DynamicViewHolder(view);
                break;
            case TYPE_FRIEND_JOIN:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dynamic, null, false);
                holder = new DynamicViewHolder(view);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dynamic, null, false);
                holder = new DynamicViewHolder(view);
                break;
        }
//        TextView view = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_1, parent, false);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        Object object = list.get(position);
        int type = TYPE_DIANPING;
        if(object instanceof FriendDynamicEntity){
            FriendDynamicEntity friendDynamicEntity = (FriendDynamicEntity) object;
            switch (friendDynamicEntity.getReleaseType()){
                case 6: //转发点评
                    type = TYPE_ZHUANFA_DIANPING;
                    break;
                case 7: //长文
                    type = TYPE_CHANGWEN;
                    break;
                case 8: //转发长文
                    type = TYPE_ZHUANFA_CHANGWEN;
                    break;
                default:
                    type = TYPE_DIANPING;
                    break;
            }
        }
        return  type;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof DynamicViewHolder){
            DynamicViewHolder dynamicViewHolder = (DynamicViewHolder) holder;
            FriendDynamicEntity entity = (FriendDynamicEntity) list.get(position);
            initDynamicViewHolder(dynamicViewHolder,entity);
        }else if(holder instanceof  ArticleViewHolder){
            ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
            FriendDynamicEntity entity = (FriendDynamicEntity) list.get(position);
            initArticleViewHolder(articleViewHolder, entity);
        }

    }

    private void initShareArticleViewHolder(final ShareArticleViewHolder holder,FriendDynamicEntity entity){
        ImageLoader.getInstance().displayImage(entity.getAvatar(), holder.headImage);
        List<PicEntity> picLists = entity.getPic();
        List<String> picStrings = new ArrayList<>();
        if(picLists!=null){
            for (PicEntity picEntity:picLists) {
                picStrings.add(picEntity.getPicUrl());
            }
        }
        holder.imgPagerView.notifyData(picStrings);
        holder.titleTextView.setText(entity.getTitle());
        holder.nameTextView.setText(entity.getNickName());
        holder.descriptionTextView.setText(entity.getDescription());

        List<String> lickUser = entity.getPraise_avatar_user() != null ? entity.getPraise_avatar_user() : new ArrayList<String>();
        holder.likeLabel.setText(String.format("%d", lickUser.size()));

        List<String> shareUsers = entity.getShare_user_nickname()!= null ? entity.getShare_user_nickname() : new ArrayList<String>();
        holder.shareLabel.setText(String.format("%d", shareUsers.size()));
//        StringBuffer stringBuffer = new StringBuffer().append("@").append(entity.getShare_user_nickname());
//        String content = stringBuffer.toString() + entity.get
    }



    private void initArticleViewHolder(final ArticleViewHolder holder,FriendDynamicEntity entity){
        ImageLoader.getInstance().displayImage(entity.getAvatar(), holder.headImage);
        List<PicEntity> picLists = entity.getPic();
        List<String> picStrings = new ArrayList<>();
        if(picLists!=null){
            for (PicEntity picEntity:picLists) {
                picStrings.add(picEntity.getPicUrl());
            }
        }
        holder.imgPagerView.notifyData(picStrings);
        holder.titleTextView.setText(entity.getTitle());
        holder.nameTextView.setText(entity.getNickName());
        holder.descriptionTextView.setText(entity.getDescription());

        List<String> lickUser = entity.getPraise_avatar_user() != null ? entity.getPraise_avatar_user() : new ArrayList<String>();
        holder.likeLabel.setText(String.format("%d",lickUser.size()));

        List<String> shareUsers = entity.getShare_user_nickname()!= null ? entity.getShare_user_nickname() : new ArrayList<String>();
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
                contentBuffer.append("推荐");
            }
            holder.shareUsers.setText(contentBuffer);
            holder.shareUsers.setVisibility(View.VISIBLE);
        }else {
            holder.shareUsers.setVisibility(View.GONE);
        }
    }

    private void initDynamicViewHolder(final DynamicViewHolder holder,FriendDynamicEntity entity){
        ImageLoader.getInstance().displayImage(entity.getAvatar(), holder.headImage);
        List<PicEntity> picLists = entity.getPic();
        List<String> picStrings = new ArrayList<>();
        if(picLists!=null){
            for (PicEntity picEntity:picLists) {
                picStrings.add(picEntity.getPicUrl());
            }
        }
        holder.imgPagerView.notifyData(picStrings);
        holder.titleTextView.setText(entity.getTitle());
        holder.nameTextView.setText(entity.getNickName());
        holder.descriptionTextView.setText(entity.getDescription());
        LinearLayoutManager manager1 = new LinearLayoutManager(mContext);
        manager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.mRecyclerView.setLayoutManager(manager1);
        List<String> lickUser = entity.getPraise_avatar_user() != null ? entity.getPraise_avatar_user() : new ArrayList<String>();
        holder.mRecyclerView.setAdapter(new RecyclerView1Adapter(mContext,lickUser));
        holder.likeLabel.setText(String.format("%d位已喜欢",lickUser.size()));

        LinearLayoutManager manager2 = new LinearLayoutManager(mContext);
        manager2.setOrientation(LinearLayoutManager.VERTICAL);
        holder.mRecyclerView.setLayoutManager(manager2);
        final List<CommentEntity> commentEntityList = entity.getComment()!= null ? entity.getComment() : new ArrayList<CommentEntity>();
        holder.commentLabel.setText(String.format("%d条评论",commentEntityList.size()));
        if(commentEntityList.size()<=2){
            holder.mRecyclerView.setAdapter(new CommentAdapter(mContext, commentEntityList));
            holder.showAllCommentBtn.setVisibility(View.GONE);
        }else{
            List<CommentEntity> commentEntities = new ArrayList<>(2);
            for(int i = 0; i<2; i++){
                commentEntities.add(commentEntityList.get(i));
            }
            holder.mRecyclerView.setAdapter(new CommentAdapter(mContext, commentEntities));
            holder.showAllCommentBtn.setVisibility(View.VISIBLE);
            holder.showAllCommentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mRecyclerView.setAdapter(new CommentAdapter(mContext, commentEntityList));
                    holder.showAllCommentBtn.setVisibility(View.GONE);
                }
            });
        }
        List<String> shareUsers = entity.getShare_user_nickname()!= null ? entity.getShare_user_nickname() : new ArrayList<String>();
        if(shareUsers.size()>0){
            StringBuffer contentBuffer = new StringBuffer();
            contentBuffer.append("已有");
            for(int i= 0; i< shareUsers.size(); i++){
                if(i== 0){
                    contentBuffer.append("@").append(shareUsers.get(i));
                }else {
                    contentBuffer.append("、@").append(shareUsers.get(i));
                }
                contentBuffer.append("推荐");
            }
            holder.shareUsers.setText(contentBuffer);
            holder.shareUsers.setVisibility(View.VISIBLE);
        }else {
            holder.shareUsers.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List list){
        this.list = list;
    }

    public static class DynamicViewHolder extends RecyclerView.ViewHolder {
        public CircularImage headImage;
        public ImgPagerView imgPagerView;
        public TextView nameTextView;
        public TextView titleTextView;
        public TextView contentTextView;
        public RecyclerView mRecyclerView;
        public ImageView addComment;
        public TextView descriptionTextView;
        public RecyclerView mCommentRecyclerView;
        public ImageView showAllCommentBtn;
        public TextView likeLabel;
        public TextView commentLabel;
        public TextView shareUsers;

        public DynamicViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.text_photo_title);
            contentTextView = (TextView) view.findViewById(R.id.text_photo_content);
            mRecyclerView = (RecyclerView) view.findViewById(R.id.item_recyclerView_lick);
            addComment = (ImageView) view.findViewById(R.id.item_btn_add_comment);
            descriptionTextView = (TextView) view.findViewById(R.id.text_description);
            nameTextView = (TextView) view.findViewById(R.id.text_item_name);
            headImage = (CircularImage) view.findViewById(R.id.img_head);
            imgPagerView = (ImgPagerView) view.findViewById(R.id.ipv_item_img);
            mCommentRecyclerView = (RecyclerView) view.findViewById(R.id.item_recyclerView_comment);
            showAllCommentBtn = (ImageView) view.findViewById(R.id.item_show_all_comment);
            likeLabel = (TextView) view.findViewById(R.id.item_text_label_like);
            commentLabel = (TextView) view.findViewById(R.id.item_text_label_comment);
            shareUsers = (TextView) view.findViewById(R.id.text_recommend_user);
        }
    }


    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        public CircularImage headImage;
        public ImgPagerView imgPagerView;
        public TextView nameTextView;
        public TextView titleTextView;
        public TextView contentTextView;
        public ImageView addComment;
        public TextView descriptionTextView;
        public TextView likeLabel;
        public TextView shareLabel;
        public TextView shareUsers;

        public ArticleViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.text_photo_title);
            contentTextView = (TextView) view.findViewById(R.id.text_photo_content);
            addComment = (ImageView) view.findViewById(R.id.item_btn_add_comment);
            descriptionTextView = (TextView) view.findViewById(R.id.text_description);
            nameTextView = (TextView) view.findViewById(R.id.text_item_name);
            headImage = (CircularImage) view.findViewById(R.id.img_head);
            imgPagerView = (ImgPagerView) view.findViewById(R.id.ipv_item_img);
            likeLabel = (TextView) view.findViewById(R.id.item_text_label_like);
            shareLabel = (TextView) view.findViewById(R.id.item_text_label_share);
            shareUsers = (TextView) view.findViewById(R.id.text_recommend_user);
        }
    }

    public static class ShareArticleViewHolder extends RecyclerView.ViewHolder {
        public CircularImage headImage;
        public ImgPagerView imgPagerView;
        public TextView nameTextView;
        public TextView titleTextView;
        public TextView contentTextView;
        public ImageView addComment;
        public TextView descriptionTextView;
        public TextView likeLabel;
        public TextView shareLabel;
        public TextView shareNotice;

        public ShareArticleViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.text_photo_title);
            contentTextView = (TextView) view.findViewById(R.id.text_photo_content);
            addComment = (ImageView) view.findViewById(R.id.item_btn_add_comment);
            descriptionTextView = (TextView) view.findViewById(R.id.text_description);
            nameTextView = (TextView) view.findViewById(R.id.text_item_name);
            headImage = (CircularImage) view.findViewById(R.id.img_head);
            imgPagerView = (ImgPagerView) view.findViewById(R.id.ipv_item_img);
            likeLabel = (TextView) view.findViewById(R.id.item_text_label_like);
            shareLabel = (TextView) view.findViewById(R.id.item_text_label_share);
            shareNotice = (TextView) view.findViewById(R.id.text_share_notice);
        }
    }

}
