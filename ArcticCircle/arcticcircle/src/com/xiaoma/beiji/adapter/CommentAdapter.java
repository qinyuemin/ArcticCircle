package com.xiaoma.beiji.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.android.lib.base.BaseActivity;
import com.common.android.lib.util.TimeUtil;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.javase.lang.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.entity.CommentEntity;
import com.xiaoma.beiji.util.IntentUtil;


import java.util.List;

/**
 * Created by zhangqibo on 2016/5/2.
 */
public class CommentAdapter extends BaseAdapter{

    private List<CommentEntity> commentEntityList ;
    private Context context;

    public CommentAdapter(Context context, List<CommentEntity> commentEntityList) {
        this.commentEntityList = commentEntityList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return commentEntityList.size();
    }


    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.lst_item_store_detail, null);
            holder = new  CommentHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (CommentHolder) convertView.getTag();
        }
        final CommentEntity commentEntity = commentEntityList.get(position);
        String avatar = commentEntity.getCommentUserAvatar();
        if (StringUtil.isValid(avatar)) {
            ImageLoader.getInstance().displayImage(avatar, holder.imgHead);
        }
        holder.nameText.setText(commentEntity.getCommentUserNickname());
        holder.timeText.setText(TimeUtil.getDisplayTime(commentEntity.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));

        String content = commentEntity.getCommentContent();
        String toUserNickName = commentEntity.getToUserNickName();
        if(StringUtil.isValid(toUserNickName)){
            content = "回复"+toUserNickName +":"+commentEntity.getCommentContent();
        }
        holder.commentText.setText(content);

        holder.imgHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    IntentUtil.goProfileActivity(context,Integer.valueOf(commentEntity.getUserId()));
                }catch (Exception e){

                }

            }
        });
        return convertView;
    }
}
class CommentHolder extends RecyclerView.ViewHolder {

    public ImageView imgHead;
    public TextView nameText;
    public TextView timeText;
    public TextView commentText;

    public CommentHolder(View itemView) {
        super(itemView);
        imgHead = (ImageView) itemView.findViewById(R.id.img_head);
        nameText = (TextView) itemView.findViewById(R.id.txt_name);
        timeText = (TextView) itemView.findViewById(R.id.txt_time);
        commentText = (TextView) itemView.findViewById(R.id.txt_comment);
    }
}
