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

import com.nostra13.universalimageloader.utils.L;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.entity.Thing;

import java.util.List;

/**
 * Author       : yanbo
 * Date         : 2015-06-02
 * Time         : 09:47
 * Description  :
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
//    private int[] colors = {R.color.red, R.color.gray_1,R.color.color_blue,R.color.color_orange,R.color.color_yellow};
    private List<Thing> things;

    private Context mContext;

    public RecyclerViewAdapter(Context mContext,List<Thing> things) {
        this.mContext = mContext;
        this.things = things;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
//        TextView view = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder holder, int position) {
//        holder.mTextView.setBackgroundColor(mContext.getResources().getColor(colors[position%(colors.length)]));
        Thing thing = things.get(position);
        holder.mTextView.setText(thing.getTitle());
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.mRecyclerView.setLayoutManager(manager);
        holder.mRecyclerView.setAdapter(new RecyclerView1Adapter(mContext, thing.getFreinds()));
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
        return things.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public RecyclerView mRecyclerView;
        public ImageButton btn;
        public LinearLayout layout_stting;

        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.text_photo_title);
            mRecyclerView = (RecyclerView) view.findViewById(R.id.item_recyclerView);
            btn = (ImageButton) view.findViewById(R.id.item_btn_hide_setting);
            layout_stting = (LinearLayout) view.findViewById(R.id.item_layout_setting);
        }
    }


}
