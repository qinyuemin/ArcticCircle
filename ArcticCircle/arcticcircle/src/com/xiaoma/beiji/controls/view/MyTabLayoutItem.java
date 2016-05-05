package com.xiaoma.beiji.controls.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoma.beiji.R;

/**
 * Created by zhangqibo on 2016/4/27.
 */
public class MyTabLayoutItem {
    public View getTabView(String count,String lebel){
        View view = LayoutInflater.from(context).inflate(R.layout.item_tab_layout, null);
        mLabel = (TextView) view.findViewById(R.id.text_lable);
        mCount = (TextView) view.findViewById(R.id.text_count);
        mCount.setText(count);
        mLabel.setText(lebel);
        return view;
    }

    public MyTabLayoutItem(Context context){
        this.context = context;
    }

    public void setmCount(String mCount) {
        this.mCount.setText(mCount);
    }

    public void setmLabel(String mLabel) {
        this.mLabel.setText(mLabel);
    }

    public void setSelected(boolean isSelected){
        if(isSelected){
            mCount.setTextColor(context.getResources().getColor(R.color.blue));
            mLabel.setTextColor(context.getResources().getColor(R.color.blue));
        }else{
            mCount.setTextColor(context.getResources().getColor(R.color.gray_title));
            mLabel.setTextColor(context.getResources().getColor(R.color.gray_title));
        }
    }

    private Context context;

    TextView mCount;
    TextView mLabel;
}
