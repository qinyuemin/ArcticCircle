package com.xiaoma.beiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoma.beiji.R;
import com.xiaoma.beiji.entity.Thing;

import java.util.List;

/**
 * Author       : yanbo
 * Date         : 2015-06-02
 * Time         : 09:47
 * Description  :
 */
public class RecyclerView1Adapter extends RecyclerView.Adapter<RecyclerView1Adapter.ViewHolder> {
//    private int[] colors = {R.color.red, R.color.gray_1,R.color.color_blue,R.color.color_orange,R.color.color_yellow};
    private List<String> strings;

    private Context mContext;

    public RecyclerView1Adapter(Context mContext, List<String> things) {
        this.mContext = mContext;
        this.strings = things;
    }

    @Override
    public RecyclerView1Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView view = (ImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView1Adapter.ViewHolder holder, int position) {
//        holder.mTextView.setBackgroundColor(mContext.getResources().getColor(colors[position%(colors.length)]));
        String  str = strings.get(position);
        holder.mTextView.setImageResource(R.drawable.ic_def_header);
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mTextView;

        public ViewHolder(ImageView view) {
            super(view);
            mTextView = view;
        }
    }


}
