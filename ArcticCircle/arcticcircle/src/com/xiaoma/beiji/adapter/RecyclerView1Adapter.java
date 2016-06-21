package com.xiaoma.beiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeapp.javase.lang.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.view.CircularImage;
import com.xiaoma.beiji.entity.Thing;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.util.IntentUtil;

import java.util.List;

/**
 * Author       : zhangqibo
 * Date         : 2016-04-02
 * Time         : 09:47
 * Description  :
 */
public class RecyclerView1Adapter extends RecyclerView.Adapter<RecyclerView1Adapter.ViewHolder> {
//    private int[] colors = {R.color.red, R.color.gray_1,R.color.color_blue,R.color.color_orange,R.color.color_yellow};
    private List<UserInfoEntity> strings;

    private Context mContext;

    public RecyclerView1Adapter(Context mContext, List<UserInfoEntity> things) {
        this.mContext = mContext;
        this.strings = things;
    }

    @Override
    public RecyclerView1Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CircularImage view = (CircularImage) LayoutInflater.from(mContext).inflate(R.layout.list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView1Adapter.ViewHolder holder, int position) {
//        holder.titleTextView.setBackgroundColor(mContext.getResources().getColor(colors[position%(colors.length)]));
        final UserInfoEntity  entity = strings.get(position);
        if (StringUtil.isValid(entity.getAvatar())) {
            ImageLoader.getInstance().displayImage(entity.getAvatar(), holder.mTextView);
        }
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    IntentUtil.goProfileActivity(mContext,String.valueOf(entity.getUserId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CircularImage mTextView;

        public ViewHolder(CircularImage view) {
            super(view);
            mTextView = view;
        }
    }


}
