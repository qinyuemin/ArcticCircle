package com.xiaoma.beiji.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.controls.view.CircularImage;
import com.xiaoma.beiji.controls.view.ImgPagerView;
import com.xiaoma.beiji.entity.FriendDynamicEntity;
import com.xiaoma.beiji.entity.PicEntity;
import com.xiaoma.beiji.network.HttpClientUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author       : yanbo
 * Date         : 2015-06-02
 * Time         : 09:47
 * Description  :
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    private int[] colors = {R.color.red, R.color.gray_1,R.color.color_blue,R.color.color_orange,R.color.color_yellow};
    private List list;

    private Context mContext;

    private static final int TYPE_DIANPING = 0;
    private static final int TYPE_WENWEN = 1;
    private static final int TYPE_CHANGWEN = 2;
    private static final int TYPE_ZHUANFA_DIANPING = 3;
    private static final int TYPE_ZHUANFA_WENWEN = 4;
    private static final int TYPE_ZHUANFA_CHANGWEN  = 5;
    private static final int TYPE_FRIEND_JOIN  = 6;

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
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null, false);
                holder = new DynamicViewHolder(view);
                break;
            case TYPE_WENWEN:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null, false);
                holder = new DynamicViewHolder(view);
                break;
            case TYPE_CHANGWEN:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null, false);
                holder = new DynamicViewHolder(view);
                break;
            case TYPE_ZHUANFA_DIANPING:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null, false);
                holder = new DynamicViewHolder(view);
                break;
            case TYPE_ZHUANFA_WENWEN:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null, false);
                holder = new DynamicViewHolder(view);
                break;
            case TYPE_ZHUANFA_CHANGWEN:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null, false);
                holder = new DynamicViewHolder(view);
                break;
            case TYPE_FRIEND_JOIN:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null, false);
                holder = new DynamicViewHolder(view);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null, false);
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
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.mRecyclerView.setLayoutManager(manager);
        holder.mRecyclerView.setAdapter(new RecyclerView1Adapter(mContext, entity.getShare_user_nickname()!=null?entity.getShare_user_nickname():new ArrayList<String>()));
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.layout_stting.getVisibility()==View.VISIBLE){
                    holder.layout_stting.setVisibility(View.GONE);
                }else{
                    holder.layout_stting.setVisibility(View.VISIBLE);
                }
            }
        });
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
        public RecyclerView mRecyclerView;
        public ImageButton btn;
        public TextView descriptionTextView;
        public LinearLayout layout_stting;

        public DynamicViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.text_photo_title);
            mRecyclerView = (RecyclerView) view.findViewById(R.id.item_recyclerView);
            btn = (ImageButton) view.findViewById(R.id.item_btn_hide_setting);
            layout_stting = (LinearLayout) view.findViewById(R.id.item_layout_setting);
            descriptionTextView = (TextView) view.findViewById(R.id.text_description);
            nameTextView = (TextView) view.findViewById(R.id.text_item_name);
            headImage = (CircularImage) view.findViewById(R.id.img_head);
            imgPagerView = (ImgPagerView) view.findViewById(R.id.ipv_item_img);
        }
    }


}
