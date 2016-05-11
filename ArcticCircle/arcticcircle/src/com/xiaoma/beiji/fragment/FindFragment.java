package com.xiaoma.beiji.fragment;

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
import android.widget.TextView;

import com.common.android.lib.util.TimeUtil;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.adapter.CommentAdapter;
import com.xiaoma.beiji.adapter.RecyclerView1Adapter;
import com.xiaoma.beiji.adapter.RecyclerViewAdapter;
import com.xiaoma.beiji.base.SimpleFragment;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.acinterface.IActionInterFace;
import com.xiaoma.beiji.controls.dialog.CommonDialogsInBase;
import com.xiaoma.beiji.controls.view.CircularImage;
import com.xiaoma.beiji.controls.view.ExpandListView;
import com.xiaoma.beiji.controls.view.ImgPagerView;
import com.xiaoma.beiji.controls.view.ShowMoreView;
import com.xiaoma.beiji.entity.CommentEntity;
import com.xiaoma.beiji.entity.FriendDynamicEntity;
import com.xiaoma.beiji.entity.PicEntity;
import com.xiaoma.beiji.entity.SqureEntity;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.IntentUtil;
import com.xiaoma.beiji.util.ToastUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhangqibo on 2016/3/29.
 */
public class FindFragment extends SimpleFragment implements IActionInterFace {

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
        setTitleControlsInfo(v);
        recyclerView = (RecyclerView) v.findViewById(R.id.find_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void loadData() {

//        Title title1 = new Title();
//        title1.setTitle("推荐达人");
//        Title title2 = new Title();
//        title2.setTitle("推荐列表");
//
//        findEntityList = new ArrayList<>();
//        findEntityList.add(title1);
//        List<UserInfoEntity> list = new ArrayList<>(2);
//        list.add(new UserInfoEntity());
//        list.add(new UserInfoEntity());
//        FindEntity findEntity = new FindEntity();
//        findEntity.setEntities(list);
//        findEntityList.add(findEntity);
//        findEntityList.add(title2);
//        for(int i=0; i<18; i++){
//            FriendDynamicEntity thing = new FriendDynamicEntity();
//            thing.setTitle("第" + i + "个");
//            thing.setContent("这是第" + i + "个动态");
//            List<PicEntity> picEntities = new ArrayList<>();
//            PicEntity picEntity = new PicEntity();
//            picEntity.setPicUrl("http://i6.265g.com/images/201501/201501301413233383.jpg");
//            picEntities.add(picEntity);
//            thing.setPic(picEntities);
//            List<String> items = new ArrayList<>();
//            for(int j=0; j<18; j++){
//                items.add("第" + j + "个朋友");
//            }
//            thing.setShare_user_nickname(items);
//            findEntityList.add(thing);
//        }
        HttpClientUtil.squreList(new AbsHttpResultHandler<SqureEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, SqureEntity data) {
                List<UserInfoEntity> userInfoEntities = data.getUserInfoEntities();
                List<FriendDynamicEntity> dynamicEntities = data.getDynamicEntities();
                Title title1 = new Title();
                title1.setTitle("推荐达人");
                Title title2 = new Title();
                title2.setTitle("推荐列表");
                findEntityList = new ArrayList<>();
                findEntityList.add(title1);
                FindEntity entity = new FindEntity();
                entity.setEntities(userInfoEntities!=null?userInfoEntities:new ArrayList<UserInfoEntity>());
                findEntityList.add(entity);
                findEntityList.add(title2);
                if(dynamicEntities!=null){
                    for(FriendDynamicEntity entity1:dynamicEntities){
                        findEntityList.add(entity1);
                    }
                }
                recyclerView.setAdapter(new Adapter(getContext(), findEntityList,FindFragment.this));
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(getContext(),desc);
            }
        });

    }

    protected CommonDialogsInBase commonDialogsInBase = new CommonDialogsInBase();
    protected void showProgressDialog() {
        commonDialogsInBase.showProgressDialog(getActivity(), false, null);
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
                handler.onSuccess(resultCode,desc,data);
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
    public void dynamicDoShare(FriendDynamicEntity entity, AbsHttpResultHandler handler) {

    }

    @Override
    public void dynamicMore(FriendDynamicEntity entity) {

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

        CircularImage headView1,headView2;
        TextView nikeName1,nikeName2;
        ImageView darenImg1,darenImg2;
        ImageView hotImg1,hotImg2;
        TextView desView1,desView2;
        TextView noticeView1,noticeView2;
        TextView labelView1,labelView2;
        LinearLayout userLayout1,userLayout2;


        public DarenViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            headView1 = (CircularImage) rootView.findViewById(R.id.img_head1);
            headView2 = (CircularImage) rootView.findViewById(R.id.img_head2);
            nikeName1 = (TextView) rootView.findViewById(R.id.text_nickname1);
            nikeName2 = (TextView) rootView.findViewById(R.id.text_nickname2);
            darenImg1 = (ImageView) rootView.findViewById(R.id.icon_daren1);
            darenImg2 = (ImageView) rootView.findViewById(R.id.icon_daren2);
            hotImg1 = (ImageView) rootView.findViewById(R.id.icon_hot1);
            hotImg2 = (ImageView) rootView.findViewById(R.id.icon_hot2);
            desView1 = (TextView) rootView.findViewById(R.id.hot_des1);
            desView2 = (TextView) rootView.findViewById(R.id.hot_des2);
            noticeView1 = (TextView) rootView.findViewById(R.id.text_notice1);
            noticeView2 = (TextView) rootView.findViewById(R.id.text_notice2);
            labelView1 = (TextView) rootView.findViewById(R.id.text_label1);
            labelView2 = (TextView) rootView.findViewById(R.id.text_label2);
            userLayout1 = (LinearLayout) rootView.findViewById(R.id.layout_user1);
            userLayout2 = (LinearLayout) rootView.findViewById(R.id.layout_user2);
        }
    }

    public static class DynamicViewHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public CircularImage headImage;
        public TextView timeTextView;
        public ImgPagerView imgPagerView;
        public TextView nameTextView;
        public TextView titleTextView;
        public TextView contentTextView;
        public RecyclerView mRecyclerView;
        public ImageView addComment;
        public ShowMoreView descriptionTextView;
        public ExpandListView mCommentRecyclerView;
        public ImageView showAllCommentBtn;
        public TextView likeLabel;
        public TextView commentLabel;
        public TextView shareUsers;

        public ImageView moreBtn;
        public ImageView likeBtn;
        public ImageView shareBtn;
        public ImageView addCommentBtn;

        public DynamicViewHolder(View view) {
            super(view);
            rootView = view;
            titleTextView = (TextView) view.findViewById(R.id.text_photo_title);
            contentTextView = (TextView) view.findViewById(R.id.text_photo_content);
            mRecyclerView = (RecyclerView) view.findViewById(R.id.item_recyclerView_lick);
            addComment = (ImageView) view.findViewById(R.id.item_btn_add_comment);
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

    class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private static final int TYPE_TITLE = 0;
        private static final int TYPE_DATEN = 1;
        private static final int TYPE_DYNAMIC = 2;
        private List mFindEntityList;
        private Context mContext;
        private IActionInterFace actionListener;

        public Adapter(Context mContex ,List mFindEntityList,IActionInterFace actionListener) {
            this.mFindEntityList = mFindEntityList;
            this.mContext = mContex;
            this.actionListener = actionListener;
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
            if(list.size()<=0){
                holder.userLayout1.setVisibility(View.INVISIBLE);
                holder.userLayout2.setVisibility(View.INVISIBLE);
            }else if(list.size() == 1){
                holder.userLayout1.setVisibility(View.VISIBLE);
                holder.userLayout2.setVisibility(View.INVISIBLE);
                final UserInfoEntity userInfoEntity = list.get(0);
                initDaren(userInfoEntity,holder.nikeName1,holder.headView1,holder.darenImg1,holder.labelView1,holder.desView1,holder.noticeView1);
                holder.userLayout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentUtil.goProfileActivity(getActivity(), userInfoEntity.getUserId());
                    }
                });
            }else{
                holder.userLayout1.setVisibility(View.VISIBLE);
                holder.userLayout2.setVisibility(View.VISIBLE);
                final UserInfoEntity userInfoEntity = list.get(0);
                final UserInfoEntity userInfoEntity1 = list.get(1);
                initDaren(userInfoEntity,holder.nikeName1,holder.headView1,holder.darenImg1,holder.labelView1,holder.desView1,holder.noticeView1);
                initDaren(userInfoEntity1,holder.nikeName2,holder.headView2,holder.darenImg2,holder.labelView2,holder.desView2,holder.noticeView2);
                holder.userLayout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentUtil.goProfileActivity(getActivity(), userInfoEntity.getUserId());
                    }
                });
                holder.userLayout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentUtil.goProfileActivity(getActivity(), userInfoEntity1.getUserId());
                    }
                });
            }
        }

        private void initDaren(UserInfoEntity userInfoEntity, TextView nikeName,CircularImage headView,ImageView darenView,TextView labelView,TextView desView,TextView noticeView){
            nikeName.setText(userInfoEntity.getNickname());
            if(!TextUtils.isEmpty(userInfoEntity.getAvatar())){
                ImageLoader.getInstance().displayImage(userInfoEntity.getAvatar(), headView);
            }
            if("1".equals(userInfoEntity.getIs_talent())){
                darenView.setVisibility(View.VISIBLE);
            }else {
                darenView.setVisibility(View.INVISIBLE);
            }
            labelView.setText(userInfoEntity.getLabel());
            if(userInfoEntity.getFriendDynamicEntities()!=null&&userInfoEntity.getFriendDynamicEntities().size()>1){
                desView.setText(userInfoEntity.getFriendDynamicEntities().get(0).getContent());
            }
            if("1".equals(userInfoEntity.getIs_attention())){
                noticeView.setText("已关注");
            }else{
                noticeView.setText("关注Ta");
            }
        }
        private void initDynamicViewHolder(final DynamicViewHolder holder,final FriendDynamicEntity entity){
            String avatar = entity.getAvatar();
            holder.timeTextView.setText(TimeUtil.getDisplayTime(entity.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
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
                    Log.d("AAA", "moreBtn onClick");
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
                }
            });
            holder.addCommentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("AAA", "addCommentLayout onClick");
                }
            });
            holder.rootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        private void initTitleHodler(TitleViewHolder holder,Title title){

        }
    }
}
