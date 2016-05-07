package com.xiaoma.beiji.fragment;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.adapter.CommentAdapter;
import com.xiaoma.beiji.adapter.RecyclerView1Adapter;
import com.xiaoma.beiji.adapter.RecyclerViewAdapter;
import com.xiaoma.beiji.base.SimpleFragment;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.view.CircularImage;
import com.xiaoma.beiji.controls.view.ExpandListView;
import com.xiaoma.beiji.controls.view.ImgPagerView;
import com.xiaoma.beiji.entity.CommentEntity;
import com.xiaoma.beiji.entity.FriendDynamicEntity;
import com.xiaoma.beiji.entity.PicEntity;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.util.IntentUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangqibo on 2016/3/29.
 */
public class FindFragment extends SimpleFragment{

    private RecyclerView recyclerView;
    private List findEntityList;

    protected void setTitleControlsInfo(View v) {
        TextViewUtil.setText(v, R.id.title_bar_title_txt, "发现");
        ViewUtil.setViewVisibility(v, R.id.title_bar_left_layout, View.GONE);
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initComponents(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.find_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void loadData() {

        Title title1 = new Title();
        title1.setTitle("推荐达人");
        Title title2 = new Title();
        title2.setTitle("推荐列表");

        findEntityList = new ArrayList<>();
        findEntityList.add(title1);
        List<UserInfoEntity> list = new ArrayList<>(2);
        list.add(new UserInfoEntity());
        list.add(new UserInfoEntity());
        FindEntity findEntity = new FindEntity();
        findEntity.setEntities(list);
        findEntityList.add(findEntity);
        findEntityList.add(title2);
        for(int i=0; i<18; i++){
            FriendDynamicEntity thing = new FriendDynamicEntity();
            thing.setTitle("第" + i + "个");
            thing.setContent("这是第" + i + "个动态");
            List<PicEntity> picEntities = new ArrayList<>();
            PicEntity picEntity = new PicEntity();
            picEntity.setPicUrl("http://i6.265g.com/images/201501/201501301413233383.jpg");
            picEntities.add(picEntity);
            thing.setPic(picEntities);
            List<String> items = new ArrayList<>();
            for(int j=0; j<18; j++){
                items.add("第" + j + "个朋友");
            }
            thing.setShare_user_nickname(items);
            findEntityList.add(thing);
        }
        recyclerView.setAdapter(new Adapter(getContext(), findEntityList));
    }

    class FindEntity{
        List<UserInfoEntity> entities;

        public List<UserInfoEntity> getEntities() {
            return entities;
        }

        public void setEntities(List<UserInfoEntity> entities) {
            this.entities = entities;
        }
    }

    class Title{
        String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    class TitleViewHolder extends RecyclerView.ViewHolder{
        View rootView;
        TextView titleView;

        public TitleViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            titleView = (TextView) itemView.findViewById(R.id.text_notice);
        }
    }

    class DarenViewHolder extends RecyclerView.ViewHolder{

        View rootView;

        public DarenViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
        }
    }

    public static class DynamicViewHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public CircularImage headImage;
        public ImgPagerView imgPagerView;
        public TextView nameTextView;
        public TextView titleTextView;
        public TextView contentTextView;
        public RecyclerView mRecyclerView;
        public ImageView addComment;
        public TextView descriptionTextView;
        public ExpandListView mCommentRecyclerView;
        public ImageView showAllCommentBtn;
        public TextView likeLabel;
        public TextView commentLabel;
        public TextView shareUsers;

        public DynamicViewHolder(View view) {
            super(view);
            rootView = view;
            titleTextView = (TextView) view.findViewById(R.id.text_photo_title);
            contentTextView = (TextView) view.findViewById(R.id.text_photo_content);
            mRecyclerView = (RecyclerView) view.findViewById(R.id.item_recyclerView_lick);
            addComment = (ImageView) view.findViewById(R.id.item_btn_add_comment);
            descriptionTextView = (TextView) view.findViewById(R.id.text_description);
            nameTextView = (TextView) view.findViewById(R.id.text_item_name);
            headImage = (CircularImage) view.findViewById(R.id.img_head);
            imgPagerView = (ImgPagerView) view.findViewById(R.id.ipv_item_img);
            mCommentRecyclerView = (ExpandListView) view.findViewById(R.id.item_recyclerView_comment);
            showAllCommentBtn = (ImageView) view.findViewById(R.id.item_show_all_comment);
            likeLabel = (TextView) view.findViewById(R.id.item_text_label_like);
            commentLabel = (TextView) view.findViewById(R.id.item_text_label_comment);
            shareUsers = (TextView) view.findViewById(R.id.text_recommend_user);
        }
    }

    class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private static final int TYPE_TITLE = 0;
        private static final int TYPE_DATEN = 1;
        private static final int TYPE_DYNAMIC = 2;
        private List mFindEntityList;
        private Context mContext;

        public Adapter(Context mContex ,List mFindEntityList) {
            this.mFindEntityList = mFindEntityList;
            this.mContext = mContex;
        }

        @Override
        public int getItemViewType(int position) {
            Object object = mFindEntityList.get(position);
            if(object instanceof Title){
                return TYPE_TITLE;
            }else if(object instanceof FindEntity){
                return TYPE_DATEN;
            }else {
                return TYPE_DYNAMIC;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            RecyclerView.ViewHolder holder;
            switch (viewType){
                case TYPE_TITLE:
                    view = LayoutInflater.from(mContext).inflate(R.layout.find_item_title,null);
                    holder = new TitleViewHolder(view);
                    break;
                case TYPE_DATEN:
                    view = LayoutInflater.from(mContext).inflate(R.layout.find_item_daren,null);
                    holder = new DarenViewHolder(view);
                    break;
                case TYPE_DYNAMIC:
                    view = LayoutInflater.from(mContext).inflate(R.layout.list_item_dynamic,null);
                    holder = new DynamicViewHolder(view);
                    break;
                default:
                    view = LayoutInflater.from(mContext).inflate(R.layout.find_item_title,null);
                    holder = new TitleViewHolder(view);
                    break;
            }
            return  holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Object entity = mFindEntityList.get(position);
            if(holder instanceof DarenViewHolder){
                FindEntity findEntity = (FindEntity) entity;
                DarenViewHolder darenViewHolder = (DarenViewHolder) holder;
                List<UserInfoEntity> userInfoEntities = findEntity.getEntities()!=null?findEntity.getEntities():new ArrayList<UserInfoEntity>();
                initDarenHolder(darenViewHolder, userInfoEntities);
            }else if(holder instanceof TitleViewHolder){
                Title title = (Title) entity;
                TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
                titleViewHolder.titleView.setText(title.getTitle());
            }else if(holder instanceof DynamicViewHolder){
                FriendDynamicEntity friendDynamicEntity = (FriendDynamicEntity) entity;
                DynamicViewHolder dynamicViewHolder = (DynamicViewHolder) holder;
                initDynamicViewHolder(dynamicViewHolder, friendDynamicEntity);
            }
        }

        @Override
        public int getItemCount() {
            return mFindEntityList.size();
        }

        private void initDarenHolder(DarenViewHolder holder,List<UserInfoEntity> list){
            holder.rootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        private void initDynamicViewHolder(final DynamicViewHolder holder,final FriendDynamicEntity entity){
            String avatar = entity.getAvatar();
            holder.headImage.setImageResource(R.drawable.ic_logo);
            if(!TextUtils.isEmpty(avatar)){
                ImageLoader.getInstance().displayImage(entity.getAvatar(), holder.headImage);
            }
            holder.headImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int userID = Integer.valueOf(entity.getUserId());
                    if(Global.getUserId() != userID){
                        IntentUtil.goProfileActivity(mContext, userID);
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
            holder.titleTextView.setText(entity.getArea());
            holder.contentTextView.setText(entity.getAssociated_price()+"RMB");
            holder.nameTextView.setText(entity.getNickName());
            holder.descriptionTextView.setText(entity.getDescription());
            LinearLayoutManager manager1 = new LinearLayoutManager(mContext);
            manager1.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.mRecyclerView.setLayoutManager(manager1);
            List<UserInfoEntity> lickUser = entity.getPraiseUsers() != null ? entity.getPraiseUsers() : new ArrayList<UserInfoEntity>();
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
            holder.rootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        private void initTitleHodler(TitleViewHolder holder,Title title){

        }
    }
}
