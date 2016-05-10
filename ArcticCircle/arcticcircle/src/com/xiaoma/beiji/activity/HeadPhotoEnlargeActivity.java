package com.xiaoma.beiji.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import com.xiaoma.beiji.R;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.view.asyncImage.AsyncImage;


/**
 * 头像放大
 */
public class HeadPhotoEnlargeActivity extends Activity implements View.OnClickListener {

    private AsyncImage mIvHeadPhoto;
    private LinearLayout mLinearBg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headphotoenlarge_lay);
        initView();
        initAction();
        setEnlargeView();
    }

    protected void initView() {
        mIvHeadPhoto = (AsyncImage) findViewById(R.id.image_headphotoenlarge_photo);
        mLinearBg = (LinearLayout) findViewById(R.id.linear_enlarge_bg);
    }

    private void initAction(){
        mLinearBg.setOnClickListener(this);
    }
    private void  setEnlargeView(){

        if(!TextUtils.isEmpty(Global.getUserInfo().getAvatar())){
            mIvHeadPhoto.fail(R.drawable.ic_logo);
            mIvHeadPhoto.loadUrl(Global.getUserInfo().getAvatar(),getDisplayWith(),getDisplayWith());
        }else {
            mIvHeadPhoto.setLocalImageRes(R.drawable.ic_logo);
        }
    }

    private int  getDisplayWith(){
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        int ivWidth = width - 20; ;
        return ivWidth;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.linear_enlarge_bg){
            finish();
        }
    }
}
