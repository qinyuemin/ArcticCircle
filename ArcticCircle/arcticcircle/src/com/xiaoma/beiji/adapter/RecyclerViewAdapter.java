package com.xiaoma.beiji.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.android.lib.util.TimeUtil;
import com.makeapp.javase.lang.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.acinterface.IActionInterFace;
import com.xiaoma.beiji.controls.sharesdk.ShareSdkUtil;
import com.xiaoma.beiji.controls.view.CircularImage;
import com.xiaoma.beiji.controls.view.ExpandListView;
import com.xiaoma.beiji.controls.view.ImgPagerView;
import com.xiaoma.beiji.controls.view.ShowMoreView;
import com.xiaoma.beiji.entity.CommentEntity;
import com.xiaoma.beiji.entity.FriendDynamicEntity;
import com.xiaoma.beiji.entity.PicEntity;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.util.IntentUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Author       : zhangqibo
 * Date         : 2015-04-02
 * Time         : 09:47
 * Description  :
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List list;
    private IActionInterFace actionListener;
    private Context mContext;

    //1 用户动态 2 用户求助  3 用户推荐店铺 4 店铺推广 5 朋友加入 6 转发点评 7长文 8转发长文

    private static final int TYPE_DIANPING = 1;
    private static final int TYPE_WENWEN = 2;
    private static final int TYPE_TUIJIAN_SHOP = 3;
    private static final int TYPE_GUANGGAO  = 4;
    private static final int TYPE_FRIEND_JOIN  = 5;
    private static final int TYPE_ZHUANFA_DIANPING = 6;
    private static final int TYPE_CHANGWEN = 7;
    private static final int TYPE_ZHUANFA_CHANGWEN  = 8;

//    private static final int TYPE_TUIJIAN_PINGLUN  = 7;


    public RecyclerViewAdapter(Context mContext,List things, IActionInterFace actionListener) {
        this.mContext = mContext;
        this.list = things;
        this.actionListener = actionListener;
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
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_wenwen, null, false);
                holder = new WenWenViewHolder(view);
                break;
            case TYPE_TUIJIAN_SHOP:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dynamic, null, false);
                holder = new DynamicViewHolder(view);
                break;
            case TYPE_GUANGGAO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dynamic, null, false);
                holder = new DynamicViewHolder(view);
                break;
            case TYPE_FRIEND_JOIN:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dynamic, null, false);
                holder = new DynamicViewHolder(view);
                break;
            case TYPE_ZHUANFA_DIANPING:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dynamic, null, false);
                holder = new DynamicViewHolder(view);
                break;
            case TYPE_CHANGWEN:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_article, null, false);
                holder = new ArticleViewHolder(view);
                break;
            case TYPE_ZHUANFA_CHANGWEN:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_article, null, false);
                holder = new ArticleViewHolder(view);
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
            //1 用户动态 2 用户求助  3 用户推荐店铺 4 店铺推广 5 朋友加入 6 转发点评 7长文 8转发长文
            return friendDynamicEntity.getReleaseType();
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
        }else if(holder instanceof  WenWenViewHolder){
            WenWenViewHolder wenWenViewHolder = (WenWenViewHolder) holder;
            FriendDynamicEntity entity = (FriendDynamicEntity) list.get(position);
            initWenWenViewHolder(wenWenViewHolder, entity);
        }

    }

    private void initWenWenViewHolder(final WenWenViewHolder holder,final FriendDynamicEntity entity){
        if(StringUtil.isValid(entity.getAvatar())){
            ImageLoader.getInstance().displayImage(entity.getAvatar(), holder.headImage);
        }
        holder.timeTextView.setText(TimeUtil.getDisplayTime(entity.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
        holder.headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.goProfileActivity(mContext,Integer.valueOf(entity.getUserId()));
            }
        });
        List<PicEntity> picLists = entity.getPic();
        if(picLists!=null&&picLists.size()>0){
            PicEntity picEntity = picLists.get(0);
            if(picEntity != null && StringUtil.isValid(picEntity.getPicUrl())){
                ImageLoader.getInstance().displayImage(picEntity.getPicUrl(), holder.imgPagerView);
            }

        }
        holder.titleTextView.setText(entity.getTitle());
        holder.nameTextView.setText(entity.getNickName());
        holder.descriptionTextView.setText(entity.getContent());

        final List<UserInfoEntity> lickUser = entity.getPraiseUsers() != null ? entity.getPraiseUsers() : new ArrayList<UserInfoEntity>();
        holder.likeLabel.setText(String.format("%d", lickUser.size()));

        List<String> shareUsers = entity.getShare_user_nickname()!= null ? entity.getShare_user_nickname() : new ArrayList<String>();
        holder.shareLabel.setText(String.format("%d", shareUsers.size()));
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.goFriendDynamicDetailActivity(mContext, entity);
            }
        });

        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AAA","moreBtn onClick");
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
                if (actionListener != null) {
                    actionListener.dynamicDoPraise(entity, new AbsHttpResultHandler() {
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
            }
        });
        holder.shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AAA", "shareLayout onClick");
            }
        });
        holder.addCommentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AAA", "addCommentLayout onClick");
            }
        });

        holder.rootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

//        StringBuffer stringBuffer = new StringBuffer().append("@").append(entity.getShare_user_nickname());
//        String content = stringBuffer.toString() + entity.get
    }

//    private void initShareArticleViewHolder(final ShareArticleViewHolder holder,final FriendDynamicEntity entity){
//        if(StringUtil.isValid(entity.getAvatar())){
//            ImageLoader.getInstance().displayImage(entity.getAvatar(), holder.headImage);
//        }
//        holder.headImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int userID = Integer.valueOf(entity.getUserId());
//                if(Global.getUserId() != userID){
//                    IntentUtil.goProfileActivity(mContext,userID);
//                }
//            }
//        });
//        List<PicEntity> picLists = entity.getPic();
//        List<String> picStrings = new ArrayList<>();
//        if(picLists!=null){
//            for (PicEntity picEntity:picLists) {
//                picStrings.add(picEntity.getPicUrl());
//            }
//        }
//        holder.imgPagerView.notifyData(picStrings);
//        holder.titleTextView.setText(entity.getTitle());
//        holder.nameTextView.setText(entity.getNickName());
//        holder.descriptionTextView.setText(entity.getDescription());
//
//        List<String> lickUser = entity.getPraise_avatar_user() != null ? entity.getPraise_avatar_user() : new ArrayList<String>();
//        holder.likeLabel.setText(String.format("%d", lickUser.size()));
//
//        List<String> shareUsers = entity.getShare_user_nickname()!= null ? entity.getShare_user_nickname() : new ArrayList<String>();
//        holder.shareLabel.setText(String.format("%d", shareUsers.size()));
////        StringBuffer stringBuffer = new StringBuffer().append("@").append(entity.getShare_user_nickname());
////        String content = stringBuffer.toString() + entity.get
//    }



    private void initArticleViewHolder(final ArticleViewHolder holder,final FriendDynamicEntity entity){
        if(StringUtil.isValid(entity.getAvatar())){
            ImageLoader.getInstance().displayImage(entity.getAvatar(), holder.headImage);
        }
        holder.timeTextView.setText(TimeUtil.getDisplayTime(entity.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
        holder.headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userID = Integer.valueOf(entity.getUserId());
                if(Global.getUserId() != userID){
                    IntentUtil.goProfileActivity(mContext,userID);
                }

            }
        });
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
        holder.descriptionTextView.setText(entity.getContent());

        final List<UserInfoEntity> lickUser = entity.getPraiseUsers() != null ? entity.getPraiseUsers() : new ArrayList<UserInfoEntity>();
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
                Log.d("AAA","moreBtn onClick");
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
                Log.d("AAA","likeBtn onClick");
                if(actionListener!=null){
                    actionListener.dynamicDoPraise(entity, new AbsHttpResultHandler() {
                        @Override
                        public void onSuccess(int resultCode, String desc, Object data) {
                            entity.setHavePraise(!entity.isHavePraise());
                            if(entity.isHavePraise()){
                                holder.likeBtn.setImageResource(R.drawable.ic_liked);
                                lickUser.add(Global.getUserInfo());
                            }else{
                                holder.likeBtn.setImageResource(R.drawable.icon_add_like);
                                Iterator<UserInfoEntity> iterator = lickUser.iterator();
                                while (iterator.hasNext()){
                                    UserInfoEntity entity1 = iterator.next();
                                    if(entity1.getUserId()==Global.getUserId()){
                                        iterator.remove();
                                    }
                                }
                            }
                            holder.likeLabel.setText(String.format("%d",lickUser.size()));
                        }
                        @Override
                        public void onFailure(int resultCode, String desc) {

                        }
                    });
                }
            }
        });
        holder.shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AAA", "shareLayout onClick");
            }
        });
        holder.addCommentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AAA", "addCommentLayout onClick");
            }
        });
    }

    private void initDynamicViewHolder(final DynamicViewHolder holder,final FriendDynamicEntity entity){
        String avatar = entity.getAvatar();
        holder.timeTextView.setText(TimeUtil.getDisplayTime(entity.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
        holder.headImage.setImageResource(R.drawable.ic_logo);
        if(!TextUtils.isEmpty(avatar)){
            ImageLoader.getInstance().displayImage(entity.getAvatar(), holder.headImage);
        }
        holder.headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userID = Integer.valueOf(entity.getUserId());
                if(Global.getUserId() != userID){
                    IntentUtil.goProfileActivity(mContext,userID);
                }
            }
        });
        List<PicEntity> picLists = entity.getPic();
        List<String> picStrings = new ArrayList<>();
        if(picLists!=null){
            for (PicEntity picEntity:picLists) {
                picStrings.add(picEntity.getPicUrl());
            }
        }
        holder.imgPagerView.notifyData(picStrings);
        if(!TextUtils.isEmpty(entity.getAssociated_shop_name())&&!"0".equals(entity.getAssociated_shop_name())){
            holder.titleTextView.setText(entity.getAssociated_shop_name());
        }else{
            holder.titleTextView.setText("111");
            holder.shopNameLayout.setVisibility(View.INVISIBLE);
        }
        if(!TextUtils.isEmpty(entity.getAssociated_price())&&!"0".equals(entity.getAssociated_price())){
            holder.contentTextView.setText(entity.getAssociated_price()+"RMB");
        }else{
            holder.contentTextView.setText(entity.getAssociated_price()+"RMB");
            holder. contentTextView.setVisibility(View.INVISIBLE);
        }

        holder.nameTextView.setText(entity.getNickName());
        holder.descriptionTextView.setText(entity.getDescription());
        LinearLayoutManager manager1 = new LinearLayoutManager(mContext);
        manager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.mRecyclerView.setLayoutManager(manager1);
        final List<UserInfoEntity> lickUser = entity.getPraiseUsers() != null ? entity.getPraiseUsers() : new ArrayList<UserInfoEntity>();
        holder.mRecyclerView.setAdapter(new RecyclerView1Adapter(mContext,lickUser));
        holder.likeLabel.setText(String.format("%d位已喜欢",lickUser.size()));

//        LinearLayoutManager manager2 = new LinearLayoutManager(mContext);
//        manager2.setOrientation(LinearLayoutManager.VERTICAL);
//        holder.mCommentRecyclerView.setLayoutManager(manager2);
        final List<CommentEntity> commentEntityList = entity.getComment()!= null ? entity.getComment() : new ArrayList<CommentEntity>();
        holder.commentLabel.setText(String.format("%d条评论",commentEntityList.size()));
        if(commentEntityList.size()<=2){
            holder.mCommentRecyclerView.setAdapter(new CommentAdapter(mContext, commentEntityList));
            holder.showAllCommentBtn.setVisibility(View.GONE);
        }else{
            List<CommentEntity> commentEntities = new ArrayList<>(2);
            for(int i = 0; i<2; i++){
                commentEntities.add(commentEntityList.get(i));
            }
            holder.mCommentRecyclerView.setAdapter(new CommentAdapter(mContext, commentEntities));
            holder.showAllCommentBtn.setVisibility(View.VISIBLE);
            holder.showAllCommentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mCommentRecyclerView.setAdapter(new CommentAdapter(mContext, commentEntityList));
                    holder.showAllCommentBtn.setVisibility(View.GONE);
                }
            });
        }
        List<String> shareUsers = entity.getShare_user_nickname()!= null ? entity.getShare_user_nickname() : new ArrayList<String>();
        if(shareUsers.size()>0){
            StringBuffer contentBuffer = new StringBuffer();
            contentBuffer.append("已有 ");
            for(int i= 0; i< shareUsers.size(); i++){
                if(i== 0){
                    contentBuffer.append("@").append(shareUsers.get(i));
                }else {
                    contentBuffer.append("、@").append(shareUsers.get(i));
                }
            }
            contentBuffer.append(" 推荐");
            holder.shareUsers.setText(contentBuffer);
            holder.shareUsers.setVisibility(View.VISIBLE);
        }else {
//            holder.shareUsers.setVisibility(View.GONE);
            holder.shareUsers.setText("暂无人推荐");
        }

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.goFriendDynamicDetailActivity(mContext, entity);
            }
        });
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AAA","moreBtn onClick");
            }
        });
        if(entity.isHavePraise()){
            holder.likeBtn.setImageResource(R.drawable.ic_liked);
        }else{
            holder.likeBtn.setImageResource(R.drawable.icon_add_like);
        }
        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AAA","likeBtn onClick");
                if(actionListener!=null){
                    actionListener.dynamicDoPraise(entity, new AbsHttpResultHandler() {
                        @Override
                        public void onSuccess(int resultCode, String desc, Object data) {
                            entity.setHavePraise(!entity.isHavePraise());
                            if(entity.isHavePraise()){
                                holder.likeBtn.setImageResource(R.drawable.ic_liked);
                                lickUser.add(Global.getUserInfo());
                            }else{
                                holder.likeBtn.setImageResource(R.drawable.icon_add_like);
                                Iterator<UserInfoEntity> iterator = lickUser.iterator();
                                while (iterator.hasNext()){
                                    UserInfoEntity entity1 = iterator.next();
                                    if(entity1.getUserId()==Global.getUserId()){
                                        iterator.remove();
                                    }
                                }
                            }
                            holder.likeLabel.setText(String.format("%d位已喜欢",lickUser.size()));
                            holder.mRecyclerView.getAdapter().notifyDataSetChanged();
                        }
                        @Override
                        public void onFailure(int resultCode, String desc) {

                        }
                    });
                }
            }
        });
        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AAA","shareLayout onClick");
                if(actionListener!=null){
                    ShareSdkUtil.showShare(mContext, entity.getDynamicId());
//                    actionListener.dynamicDoFaavorite(entity, new AbsHttpResultHandler() {
//                        @Override
//                        public void onSuccess(int resultCode, String desc, Object data) {
//                            entity.setHavePraise(!entity.isHavePraise());
//                            if(entity.isHavePraise()){
//                                holder.likeBtn.setImageResource(R.drawable.ic_liked);
//                                lickUser.add(Global.getUserInfo());
//                            }else{
//                                holder.likeBtn.setImageResource(R.drawable.icon_add_like);
//                                Iterator<UserInfoEntity> iterator = lickUser.iterator();
//                                while (iterator.hasNext()){
//                                    UserInfoEntity entity1 = iterator.next();
//                                    if(entity1.getUserId()==Global.getUserId()){
//                                        iterator.remove();
//                                    }
//                                }
//                            }
//                            holder.likeLabel.setText(String.format("%d位已喜欢",lickUser.size()));
//                            holder.mRecyclerView.getAdapter().notifyDataSetChanged();
//                        }
//                        @Override
//                        public void onFailure(int resultCode, String desc) {
//
//                        }
//                    });
                }
            }
        });
        holder.addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AAA","addCommentLayout onClick");
            }
        });
        holder.rootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List list){
        this.list = list;
    }

    public static class DynamicViewHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public CircularImage headImage;
        public ImgPagerView imgPagerView;
        public TextView nameTextView;
        public TextView titleTextView;
        public LinearLayout shopNameLayout;
        public TextView contentTextView;
        public RecyclerView mRecyclerView;
        public ShowMoreView descriptionTextView;
        public ExpandListView mCommentRecyclerView;
        public ImageView showAllCommentBtn;
        public TextView likeLabel;
        public TextView commentLabel;
        public TextView shareUsers;
        public TextView timeTextView;

        public ImageView moreBtn;
        public ImageView likeBtn;
        public ImageView shareBtn;
        public ImageView addCommentBtn;

        public DynamicViewHolder(View view) {
            super(view);
            rootView = view;
            titleTextView = (TextView) view.findViewById(R.id.text_photo_title);
            shopNameLayout = (LinearLayout) view.findViewById(R.id.layout_shop_name);
            contentTextView = (TextView) view.findViewById(R.id.text_photo_content);
            mRecyclerView = (RecyclerView) view.findViewById(R.id.item_recyclerView_lick);
            descriptionTextView = (ShowMoreView) view.findViewById(R.id.text_description);
            nameTextView = (TextView) view.findViewById(R.id.text_item_name);
            headImage = (CircularImage) view.findViewById(R.id.img_head);
            imgPagerView = (ImgPagerView) view.findViewById(R.id.ipv_item_img);
            mCommentRecyclerView = (ExpandListView) view.findViewById(R.id.item_recyclerView_comment);
            showAllCommentBtn = (ImageView) view.findViewById(R.id.item_show_all_comment);
            likeLabel = (TextView) view.findViewById(R.id.item_text_label_like);
            commentLabel = (TextView) view.findViewById(R.id.item_text_label_comment);
            shareUsers = (TextView) view.findViewById(R.id.text_recommend_user);
            timeTextView = (TextView) view.findViewById(R.id.text_item_time);
            moreBtn = (ImageView) view.findViewById(R.id.btn_more);
            likeBtn = (ImageView) view.findViewById(R.id.btn_like);
            shareBtn = (ImageView) view.findViewById(R.id.btn_recommend);
            addCommentBtn = (ImageView) view.findViewById(R.id.btn_add_comment);
        }
    }


    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        public CircularImage headImage;
        public ImgPagerView imgPagerView;
        public TextView nameTextView;
        public TextView titleTextView;
        public TextView contentTextView;
        public ImageView addComment;
        public ShowMoreView descriptionTextView;
        public TextView likeLabel;
        public TextView shareLabel;
        public TextView shareUsers;
        public TextView timeTextView;

        public ImageView moreBtn;
        public ImageView likeBtn;
        public RelativeLayout likeLayout;
        public RelativeLayout shareLayout;
        public RelativeLayout addCommentLayout;

        public ArticleViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.text_photo_title);
            contentTextView = (TextView) view.findViewById(R.id.text_photo_content);
            addComment = (ImageView) view.findViewById(R.id.item_btn_add_comment);
            descriptionTextView = (ShowMoreView) view.findViewById(R.id.text_description);
            nameTextView = (TextView) view.findViewById(R.id.text_item_name);
            headImage = (CircularImage) view.findViewById(R.id.img_head);
            imgPagerView = (ImgPagerView) view.findViewById(R.id.ipv_item_img);
            likeLabel = (TextView) view.findViewById(R.id.item_text_label_like);
            shareLabel = (TextView) view.findViewById(R.id.item_text_label_share);
            shareUsers = (TextView) view.findViewById(R.id.text_recommend_user);
            timeTextView = (TextView) view.findViewById(R.id.text_item_time);
            moreBtn = (ImageView) view.findViewById(R.id.btn_more);
            likeBtn = (ImageView) view.findViewById(R.id.btn_like);
            likeLayout = (RelativeLayout) view.findViewById(R.id.layout_like);
            shareLayout = (RelativeLayout) view.findViewById(R.id.layout_recommend);
            addCommentLayout = (RelativeLayout) view.findViewById(R.id.layout_add_comment);
        }
    }

//    public static class ShareArticleViewHolder extends RecyclerView.ViewHolder {
//        public CircularImage headImage;
//        public ImgPagerView imgPagerView;
//        public TextView nameTextView;
//        public TextView titleTextView;
//        public TextView contentTextView;
//        public ImageView addComment;
//        public ShowMoreView descriptionTextView;
//        public TextView likeLabel;
//        public TextView shareLabel;
//        public TextView shareNotice;
//
//        public ShareArticleViewHolder(View view) {
//            super(view);
//            titleTextView = (TextView) view.findViewById(R.id.text_photo_title);
//            contentTextView = (TextView) view.findViewById(R.id.text_photo_content);
//            addComment = (ImageView) view.findViewById(R.id.item_btn_add_comment);
//            descriptionTextView = (ShowMoreView) view.findViewById(R.id.text_description);
//            nameTextView = (TextView) view.findViewById(R.id.text_item_name);
//            headImage = (CircularImage) view.findViewById(R.id.img_head);
//            imgPagerView = (ImgPagerView) view.findViewById(R.id.ipv_item_img);
//            likeLabel = (TextView) view.findViewById(R.id.item_text_label_like);
//            shareLabel = (TextView) view.findViewById(R.id.item_text_label_share);
//            shareNotice = (TextView) view.findViewById(R.id.text_share_notice);
//        }
//    }

    public static class WenWenViewHolder extends RecyclerView.ViewHolder {

        public View rootView;

        public CircularImage headImage;
        public ImageView imgPagerView;
        public TextView nameTextView;
        public TextView titleTextView;
        public TextView contentTextView;
        public ShowMoreView descriptionTextView;
        public TextView likeLabel;
        public TextView shareLabel;
        public TextView timeTextView;

        public ImageView moreBtn;
        public ImageView likeBtn;
        public RelativeLayout likeLayout;
        public RelativeLayout shareLayout;
        public RelativeLayout addCommentLayout;

        public WenWenViewHolder(View view) {
            super(view);
            rootView = view;
            titleTextView = (TextView) view.findViewById(R.id.text_photo_title);
            contentTextView = (TextView) view.findViewById(R.id.text_photo_content);
            descriptionTextView = (ShowMoreView) view.findViewById(R.id.text_description);
            nameTextView = (TextView) view.findViewById(R.id.text_item_name);
            headImage = (CircularImage) view.findViewById(R.id.img_head);
            imgPagerView = (ImageView) view.findViewById(R.id.ipv_item_img);
            likeLabel = (TextView) view.findViewById(R.id.item_text_label_like);
            shareLabel = (TextView) view.findViewById(R.id.item_text_label_share);
            timeTextView = (TextView) view.findViewById(R.id.text_item_time);
            moreBtn = (ImageView) view.findViewById(R.id.btn_more);
            likeBtn = (ImageView) view.findViewById(R.id.btn_like);
            moreBtn = (ImageView) view.findViewById(R.id.btn_more);
            likeBtn = (ImageView) view.findViewById(R.id.btn_like);
            likeLayout = (RelativeLayout) view.findViewById(R.id.layout_like);
            shareLayout = (RelativeLayout) view.findViewById(R.id.layout_recommend);
            addCommentLayout = (RelativeLayout) view.findViewById(R.id.layout_add_comment);

        }
    }

}
