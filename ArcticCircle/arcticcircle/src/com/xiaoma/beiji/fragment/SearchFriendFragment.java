package com.xiaoma.beiji.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeapp.android.util.ViewUtil;
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
import com.xiaoma.beiji.network.HttpClientUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by zhangqibo on 2016/4/12.
 */
public class SearchFriendFragment extends Fragment{

    private View rootView;
    private RecyclerView mRecyclerView;
    private List<Object> list;
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

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }



    class SearchShopAdpter extends RecyclerView.Adapter<ShopViewHolder>{

        private Context context;
        private List<Object> list;

        public SearchShopAdpter(Context context, List<Object> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public ShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_search_shop,null);
            return new ShopViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ShopViewHolder holder, int position) {
            Object o = list.get(position);
            holder.layout_title.setVisibility(View.GONE);
            holder.layout_tuijian.setVisibility(View.GONE);
            holder.layout_shop.setVisibility(View.GONE);
            if(o instanceof Title){
                Title title = (Title) o;
                holder.layout_title.setVisibility(View.VISIBLE);
                holder.title.setText(title.getTitleString());
            }else if(o instanceof HotShop){
                HotShop hotShop = (HotShop) o;
                List<ShopEntity> list = hotShop.getList();
                if(list!=null){
                    holder.layout_tuijian.setVisibility(View.VISIBLE);
                    holder.hotShopName1.setText(list.get(0).getShowName());
                    holder.hotShopName2.setText(list.get(1).getShowName());
                }
            }else if(o instanceof FriendEntity){
                FriendEntity friendEntity = (FriendEntity) o;
                holder.layout_shop.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(friendEntity.getAvatar(), holder.headImage);
                holder.itemShopName.setText(friendEntity.getNickname());
            }else if(o instanceof UserInfoEntity){
                UserInfoEntity userInfoEntity = (UserInfoEntity) o;
                holder.layout_shop.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(userInfoEntity.getAvatar(), holder.headImage);
                holder.itemShopName.setText(userInfoEntity.getNickname());
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    class ShopViewHolder extends RecyclerView.ViewHolder{

        ShopViewHolder(View view){
            super(view);
            layout_title = (LinearLayout) view.findViewById(R.id.item_layout_title);
            title = (TextView) view.findViewById(R.id.item_text_title);

            layout_tuijian = (LinearLayout) view.findViewById(R.id.item_layout_tuijian);
            hotShopName1 = (TextView) view.findViewById(R.id.item_hot_shopname1);
            hotShopName2 = (TextView) view.findViewById(R.id.item_hot_shopname2);

            layout_shop = (LinearLayout) view.findViewById(R.id.item_layout_shop);
            itemShopName = (TextView) view.findViewById(R.id.text_item_shop_name);
            headImage = (CircularImage) view.findViewById(R.id.img_head);
        }

        LinearLayout layout_title;
        TextView title;

        LinearLayout layout_tuijian;
        TextView hotShopName1;
//        TextView HotShopTuijian1;

        TextView hotShopName2;
//        TextView HotShopTuijian2;

        LinearLayout layout_shop;
        TextView itemShopName;

        CircularImage headImage;
    }

}
