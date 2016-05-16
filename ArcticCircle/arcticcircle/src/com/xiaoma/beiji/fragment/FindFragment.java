package com.xiaoma.beiji.fragment;

import android.content.Context;
import android.os.Handler;
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

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.common.android.lib.util.TimeUtil;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ViewUtil;
import com.makeapp.javase.lang.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.adapter.CommentAdapter;
import com.xiaoma.beiji.adapter.RecyclerView1Adapter;
import com.xiaoma.beiji.adapter.RecyclerViewAdapter;
import com.xiaoma.beiji.base.SimpleFragment;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.acinterface.IActionInterFace;
import com.xiaoma.beiji.controls.acinterface.ICommentInterface;
import com.xiaoma.beiji.controls.acinterface.IDomoreInterface;
import com.xiaoma.beiji.controls.dialog.ActionSheetDialog;
import com.xiaoma.beiji.controls.dialog.CommonDialogsInBase;
import com.xiaoma.beiji.controls.dialog.InputDialog;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhangqibo on 2016/3/29.
 */
public class FindFragment extends SimpleFragment implements IActionInterFace {

    private RecyclerView recyclerView;
    private List findEntityList = new ArrayList<>();
    ;
    private PtrClassicFrameLayout ptrClassicFrameLayout;
    RecyclerAdapterWithHF adapter;

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

        ptrClassicFrameLayout = (PtrClassicFrameLayout) v.findViewById(R.id.test_recycler_view_frame);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapterWithHF(new Adapter(getContext(), findEntityList, FindFragment.this));
        recyclerView.setAdapter(adapter);
        ptrClassicFrameLayout.disableWhenHorizontalMove(true);
//        ptrClassicFrameLayout.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                ptrClassicFrameLayout.autoRefresh(true);
//            }
//        }, 150);

        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                loadData();
            }
        });

        ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                loadMoreDate();
            }
        });
    }

    private void prepareDate() {
        HttpClientUtil.squreList(new AbsHttpResultHandler<SqureEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, SqureEntity data) {
                List<UserInfoEntity> userInfoEntities = data.getUserInfoEntities();
                List<FriendDynamicEntity> dynamicEntities = data.getDynamicEntities();
                Title title1 = new Title();
                title1.setTitle("推荐达人");
                Title title2 = new Title();
                title2.setTitle("推荐列表");
                findEntityList.clear();
                findEntityList.add(title1);
                FindEntity entity = new FindEntity();
                entity.setEntities(userInfoEntities != null ? userInfoEntities : new ArrayList<UserInfoEntity>());
                findEntityList.add(entity);
                findEntityList.add(title2);
                if (dynamicEntities != null) {
                    for (FriendDynamicEntity entity1 : dynamicEntities) {
                        findEntityList.add(entity1);
                    }
                }
//                ptrClassicFrameLayout.autoRefresh(true);
                ptrClassicFrameLayout.refreshComplete();
                ptrClassicFrameLayout.setLoadMoreEnable(true);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(getContext(), desc);
                ptrClassicFrameLayout.refreshComplete();
            }
        });
    }

    private void loadMoreDate() {
        HttpClientUtil.squreList(new AbsHttpResultHandler<SqureEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, SqureEntity data) {
                List<FriendDynamicEntity> dynamicEntities = data.getDynamicEntities();
                if (dynamicEntities != null) {
                    for (FriendDynamicEntity entity1 : dynamicEntities) {
                        findEntityList.add(entity1);
                    }
                }
                adapter.notifyDataSetChanged();
                ptrClassicFrameLayout.loadMoreComplete(true);
//                ptrClassicFrameLayout.setLoadMoreEnable(true);
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ptrClassicFrameLayout.loadMoreComplete(true);
                ptrClassicFrameLayout.setLoadMoreEnable(true);
                ToastUtil.showToast(getContext(), desc);
            }
        });
    }

    @Override
    protected void loadData() {
        prepareDate();
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
    public void dynamicDoShare(FriendDynamicEntity entity, AbsHttpResultHandler handler) {
        commonDialogsInBase.shoShareDialog(getActivity(), entity, handler);
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

    public void dynamicMore(final FriendDynamicEntity entity, final IDomoreInterface callBack) {
        try {
            final List<String> items = new ArrayList<>();
            if (Global.getUserId() == Integer.valueOf(entity.getUserId())) {
                items.add(IDomoreInterface.TYPE_DELETE);
                items.add(IDomoreInterface.TYPE_JUBAO);
            } else {
                items.add(IDomoreInterface.TYPE_PINGBI);
                items.add(IDomoreInterface.TYPE_JUBAO);
                items.add(IDomoreInterface.TYPE_SHOUCANG);
            }
            commonDialogsInBase.showChooseDialog(getActivity(), items, new ActionSheetDialog.OnSheetItemClickListener() {
                @Override
                public void onClick(int which) {
                    String opreate = items.get(which - 1);
                    switch (opreate) {
                        case IDomoreInterface.TYPE_DELETE:
                            deleteDynamic(entity, callBack);
                            break;
                        case IDomoreInterface.TYPE_SHOUCANG:
                            doFavorite(entity, callBack);
                            break;
                        case IDomoreInterface.TYPE_PINGBI:
                            hideDynamic(entity, callBack);
                            break;
                        case IDomoreInterface.TYPE_JUBAO:
                            reportDynamic(entity, callBack);
                            break;
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    private void doFavorite(final FriendDynamicEntity entity, final IDomoreInterface callBack) {
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

    private void deleteDynamic(final FriendDynamicEntity entity, final IDomoreInterface callBack) {
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

    private void reportDynamic(final FriendDynamicEntity entity, final IDomoreInterface callBack) {
        if (Global.getUserId() == Integer.valueOf(entity.getUserId())) //自己不能举报自己吧
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

    private void hideDynamic(final FriendDynamicEntity entity, final IDomoreInterface callBack) {
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
                ToastUtil.showToast(getActivity(), "屏蔽失败" + desc);
            }
        });
    }

    class FindEntity {
        List<UserInfoEntity> entities;

        public List<UserInfoEntity> getEntities() {
            return entities;
        }

        public void setEntities(List<UserInfoEntity> entities) {
            this.entities = entities;
        }
    }

    class Title {
        String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView titleView;

        public TitleViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            titleView = (TextView) itemView.findViewById(R.id.text_notice);
        }
    }

    class DarenViewHolder extends RecyclerView.ViewHolder {

        View rootView;

        CircularImage headView1, headView2;
        TextView nikeName1, nikeName2;
        ImageView darenImg1, darenImg2;
        ImageView hotImg1, hotImg2;
        TextView desView1, desView2;
        TextView noticeView1, noticeView2;
        TextView labelView1, labelView2;
        LinearLayout userLayout1, userLayout2;


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

    class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_TITLE = -1;
        private static final int TYPE_DATEN = -2;
        private List mFindEntityList;
        private Context mContext;
        private IActionInterFace actionListener;
        private RecyclerViewAdapter recyclerViewAdapter;

        public Adapter(Context mContex, List mFindEntityList, IActionInterFace actionListener) {
            this.mFindEntityList = mFindEntityList;
            this.mContext = mContex;
            this.actionListener = actionListener;
            recyclerViewAdapter = new RecyclerViewAdapter(mContex,mFindEntityList,actionListener);
        }

        @Override
        public int getItemViewType(int position) {
            Object object = mFindEntityList.get(position);
            if (object instanceof Title) {
                return TYPE_TITLE;
            } else if (object instanceof FindEntity) {
                return TYPE_DATEN;
            } else if(object instanceof  FriendDynamicEntity){
                FriendDynamicEntity entity = (FriendDynamicEntity) object;
                return recyclerViewAdapter.getItemViewType(position);
            }else{
                return TYPE_TITLE;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            RecyclerView.ViewHolder holder;
            switch (viewType) {
                case TYPE_TITLE:
                    view = LayoutInflater.from(mContext).inflate(R.layout.find_item_title, null);
                    holder = new TitleViewHolder(view);
                    break;
                case TYPE_DATEN:
                    view = LayoutInflater.from(mContext).inflate(R.layout.find_item_daren, null);
                    holder = new DarenViewHolder(view);
                    break;
                default:
                    holder = recyclerViewAdapter.onCreateViewHolder(parent,viewType);
                    break;
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Object entity = mFindEntityList.get(position);
            if (holder instanceof DarenViewHolder) {
                FindEntity findEntity = (FindEntity) entity;
                DarenViewHolder darenViewHolder = (DarenViewHolder) holder;
                List<UserInfoEntity> userInfoEntities = findEntity.getEntities() != null ? findEntity.getEntities() : new ArrayList<UserInfoEntity>();
                initDarenHolder(darenViewHolder, userInfoEntities);
            } else if (holder instanceof TitleViewHolder) {
                Title title = (Title) entity;
                TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
                titleViewHolder.titleView.setText(title.getTitle());
            } else {
                recyclerViewAdapter.onBindViewHolder(holder,position);
            }
        }

        @Override
        public int getItemCount() {
            return mFindEntityList.size();
        }

        private void initDarenHolder(DarenViewHolder holder, List<UserInfoEntity> list) {
            holder.rootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            if (list.size() <= 0) {
                holder.userLayout1.setVisibility(View.INVISIBLE);
                holder.userLayout2.setVisibility(View.INVISIBLE);
            } else if (list.size() == 1) {
                holder.userLayout1.setVisibility(View.VISIBLE);
                holder.userLayout2.setVisibility(View.INVISIBLE);
                final UserInfoEntity userInfoEntity = list.get(0);
                initDaren(userInfoEntity, holder.nikeName1, holder.headView1, holder.darenImg1, holder.labelView1, holder.desView1, holder.noticeView1);
                holder.userLayout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentUtil.goProfileActivity(getActivity(), userInfoEntity.getUserId());
                    }
                });
            } else {
                holder.userLayout1.setVisibility(View.VISIBLE);
                holder.userLayout2.setVisibility(View.VISIBLE);
                final UserInfoEntity userInfoEntity = list.get(0);
                final UserInfoEntity userInfoEntity1 = list.get(1);
                initDaren(userInfoEntity, holder.nikeName1, holder.headView1, holder.darenImg1, holder.labelView1, holder.desView1, holder.noticeView1);
                initDaren(userInfoEntity1, holder.nikeName2, holder.headView2, holder.darenImg2, holder.labelView2, holder.desView2, holder.noticeView2);
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

        private void initDaren(final UserInfoEntity userInfoEntity, TextView nikeName, CircularImage headView, ImageView darenView, TextView labelView, TextView desView, final TextView noticeView) {
            nikeName.setText(userInfoEntity.getNickname());
            if (!TextUtils.isEmpty(userInfoEntity.getAvatar())) {
                ImageLoader.getInstance().displayImage(userInfoEntity.getAvatar(), headView);
            }
            if ("1".equals(userInfoEntity.getIs_talent())) {
                darenView.setVisibility(View.VISIBLE);
            } else {
                darenView.setVisibility(View.INVISIBLE);
            }
            labelView.setText(userInfoEntity.getLabel());
            if (userInfoEntity.getFriendDynamicEntities() != null && userInfoEntity.getFriendDynamicEntities().size() > 1) {
                desView.setText(userInfoEntity.getFriendDynamicEntities().get(0).getContent());
            }
            if ("1".equals(userInfoEntity.getIs_attention())) {
                noticeView.setText("已关注");
            } else {
                noticeView.setText("关注TA");
            }
            noticeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final boolean isAttention = !"1".equals(userInfoEntity.getIs_attention());
                    HttpClientUtil.Friend.friendAttention(isAttention, userInfoEntity.getUserId(), new AbsHttpResultHandler() {
                        @Override
                        public void onSuccess(int resultCode, String desc, Object data) {
                            if (isAttention) {
                                ToastUtil.showToast(getActivity(), "关注成功");
                                userInfoEntity.setIs_attention("1");
                                noticeView.setText("已关注");
                            } else {
                                ToastUtil.showToast(getActivity(), "取消关注成功");
                                userInfoEntity.setIs_attention("0");
                                noticeView.setText("关注TA");
                            }
                        }

                        @Override
                        public void onFailure(int resultCode, String desc) {
                            ToastUtil.showToast(getActivity(), desc);
                        }
                    });
                }
            });
        }
    }
}
