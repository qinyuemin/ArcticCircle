package com.xiaoma.beiji.fragment;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.xiaoma.beiji.R;
import com.xiaoma.beiji.activity.PublishActivity;
import com.xiaoma.beiji.util.IntentUtil;
import com.xiaoma.beiji.util.ToastUtil;

/**
 * Created by zhangqibo on 2016/4/10.
 */
public class MainPublishPopView extends PopupWindow implements View.OnClickListener{

    private Context context;

    public MainPublishPopView(Context context, int width, int height, boolean focusable) {
        super(LayoutInflater.from(context).inflate(R.layout.pop_layout_publish,null), width, height, focusable);
        this.context = context;
        getContentView().findViewById(R.id.publish_changwen).setOnClickListener(this);
        getContentView().findViewById(R.id.publish_dianping).setOnClickListener(this);
        getContentView().findViewById(R.id.publish_qiuzu).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()){
            case R.id.publish_changwen:
//                ToastUtil.showToast(context,"敬请期待");
                IntentUtil.goTrendsPublishActivity((Activity) context, PublishActivity.PUBLISH_CHANGWEN);
                break;
            case R.id.publish_dianping:
                IntentUtil.goTrendsPublishActivity((Activity) context,  PublishActivity.PUBLISH_DIANPING);
                break;
            case R.id.publish_qiuzu:
                IntentUtil.goTrendsPublishActivity((Activity) context, PublishActivity.PUBLISH_WENWEN);
                break;

        }
    }
}
