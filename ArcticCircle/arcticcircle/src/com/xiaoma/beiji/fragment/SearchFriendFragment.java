package com.xiaoma.beiji.fragment;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeapp.android.util.ViewUtil;
import com.makeapp.javase.lang.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.adapter.RecyclerViewAdapter;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.view.CircularImage;
import com.xiaoma.beiji.entity.FriendEntity;
import com.xiaoma.beiji.entity.HotShop;
import com.xiaoma.beiji.entity.ShopEntity;
import com.xiaoma.beiji.entity.Title;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.IntentUtil;
import com.xiaoma.beiji.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by zhangqibo on 2016/4/12.
 */
public class SearchFriendFragment extends Fragment{

    private View rootView;
    private RecyclerView mRecyclerView;
    private List<UserInfoEntity> list = new ArrayList<>();
    private RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_layout_list,null);
        initComponents(rootView);
        list = new ArrayList<>();
        return  rootView;
    }

    private void initComponents(View rootView) {
        mRecyclerView = (RecyclerView) ViewUtil.findViewById(rootView,R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SearchShopAdpter(getContext(),list);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public List<UserInfoEntity> getList() {
        return list;
    }

    public void setList(List<UserInfoEntity> list) {
        this.list = list;
        if(mRecyclerView!=null){
            adapter = new SearchShopAdpter(getActivity(), list);
            mRecyclerView.setAdapter(adapter);
        }
    }



    class SearchShopAdpter extends RecyclerView.Adapter{

        private  final int TYPE_SHOP = 0;
        private  final int TYPE_FRIEND = 1;

        private Context context;
        private List<UserInfoEntity> list;

        public SearchShopAdpter(Context context, List<UserInfoEntity> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType){
                case TYPE_FRIEND:
                    view = LayoutInflater.from(context).inflate(R.layout.item_contacts,null);
                    return new FriendHolder(view);
                default:
                    view = LayoutInflater.from(context).inflate(R.layout.item_contacts,null);
                    return new FriendHolder(view);
            }
//            View v = LayoutInflater.from(context).inflate(R.layout.item_search_shop,null);
//            return new ShopViewHolder(v);
        }

        @Override
        public int getItemViewType(int position) {
            Object o = list.get(position);
            if(o instanceof UserInfoEntity) {
                return TYPE_FRIEND;
            }
            return TYPE_FRIEND;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Object o = list.get(position);
            if(o instanceof UserInfoEntity){
                UserInfoEntity entity = (UserInfoEntity) o;
                FriendHolder friendHolder = (FriendHolder) holder;
                initFriendHolder(entity,friendHolder);

            }
//            Object o = list.get(position);
//            holder.layout_title.setVisibility(View.GONE);
//            holder.layout_tuijian.setVisibility(View.GONE);
//            holder.layout_shop.setVisibility(View.GONE);
//            if(o instanceof Title){
//                Title title = (Title) o;
//                holder.layout_title.setVisibility(View.VISIBLE);
//                holder.title.setText(title.getTitleString());
//            }else if(o instanceof HotShop){
//                HotShop hotShop = (HotShop) o;
//                List<ShopEntity> list = hotShop.getList();
//                if(list!=null){
//                    holder.layout_tuijian.setVisibility(View.VISIBLE);
//                    holder.hotShopName1.setText(list.get(0).getShowName());
//                    holder.hotShopName2.setText(list.get(1).getShowName());
//                }
//            }else if(o instanceof FriendEntity){
//                FriendEntity friendEntity = (FriendEntity) o;
//                holder.layout_shop.setVisibility(View.VISIBLE);
//                if(StringUtil.isValid(friendEntity.getAvatar())){
//                    ImageLoader.getInstance().displayImage(friendEntity.getAvatar(), holder.headImage);
//                }
//                holder.itemShopName.setText(friendEntity.getNickname());
//            }else if(o instanceof UserInfoEntity){
//                UserInfoEntity userInfoEntity = (UserInfoEntity) o;
//                holder.layout_shop.setVisibility(View.VISIBLE);
//                if(StringUtil.isValid(userInfoEntity.getAvatar())){
//                    ImageLoader.getInstance().displayImage(userInfoEntity.getAvatar(), holder.headImage);
//                }
//                holder.itemShopName.setText(userInfoEntity.getNickname());
//            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

//    class ShopViewHolder extends RecyclerView.ViewHolder{
//
//        ShopViewHolder(View view){
//            super(view);
//            layout_title = (LinearLayout) view.findViewById(R.id.item_layout_title);
//            title = (TextView) view.findViewById(R.id.item_text_title);
//
//            layout_tuijian = (LinearLayout) view.findViewById(R.id.item_layout_tuijian);
//            hotShopName1 = (TextView) view.findViewById(R.id.item_hot_shopname1);
//            hotShopName2 = (TextView) view.findViewById(R.id.item_hot_shopname2);
//
//            layout_shop = (LinearLayout) view.findViewById(R.id.item_layout_shop);
//            itemShopName = (TextView) view.findViewById(R.id.text_item_shop_name);
//            headImage = (CircularImage) view.findViewById(R.id.img_head);
//        }
//
//        LinearLayout layout_title;
//        TextView title;
//
//        LinearLayout layout_tuijian;
//        TextView hotShopName1;
//        TextView hotShopName2;
//        LinearLayout layout_shop;
//        TextView itemShopName;
//
//        CircularImage headImage;
//    }

    class FriendHolder extends RecyclerView.ViewHolder{
        private CircularImage headView;
        private TextView nameText;
        private TextView descText;
        private ImageView imageView;

        public FriendHolder(View itemView) {
            super(itemView);
            headView = (CircularImage) itemView.findViewById(R.id.img_head);
            nameText = (TextView) itemView.findViewById(R.id.text_item_name);
            descText = (TextView) itemView.findViewById(R.id.text_description);
            imageView = (ImageView) itemView.findViewById(R.id.item_add);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    void initFriendHolder(final UserInfoEntity entity,final FriendHolder holder){
        final UserInfoEntity userInfoEntity = entity;
        holder.nameText.setText(userInfoEntity.getNickname());
        if(!"1".equals(userInfoEntity.getIs_attention())){
            holder.imageView.setImageResource(R.drawable.ic_publish);
        }else{
            holder.imageView.setImageDrawable(null);
        }
        if(StringUtil.isValid(userInfoEntity.getAvatar())){
            ImageLoader.getInstance().displayImage(userInfoEntity.getAvatar(), holder.headView);
        }
        holder.descText.setText("粉丝数："+ userInfoEntity.getAttention_num());
        holder.imageView.setVisibility(View.GONE);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean isAttention = !"1".equals(userInfoEntity.getIs_attention());
                HttpClientUtil.Friend.friendAttention(isAttention, userInfoEntity.getUserId(), new AbsHttpResultHandler() {
                    @Override
                    public void onSuccess(int resultCode, String desc, Object data) {
                        userInfoEntity.setIs_attention(isAttention ? "1" : "2");
                        if (isAttention) {
                            holder.imageView.setImageDrawable(null);
                            ToastUtil.showToast(getContext(), "关注成功");
                        } else {
                            holder.imageView.setImageResource(R.drawable.ic_publish);
                            ToastUtil.showToast(getContext(), "取消关注成功");
                        }
                    }

                    @Override
                    public void onFailure(int resultCode, String desc) {

                    }
                });
            }
        });
        holder.headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.goProfileActivity(getActivity(),entity.getUserId());
            }
        });
    }

}
