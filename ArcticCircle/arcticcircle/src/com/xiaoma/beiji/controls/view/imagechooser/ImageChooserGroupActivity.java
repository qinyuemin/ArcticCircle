/**
 * MainActivity.java
 * ImageChooser
 * 
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

package com.xiaoma.beiji.controls.view.imagechooser;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.controls.view.imagechooser.task.ImageLoadTask;
import com.xiaoma.beiji.controls.view.imagechooser.task.OnTaskResultListener;

import java.util.ArrayList;

/**
 * 图片选择主界面，列出所有图片文件夹
 * 
 * @author likebamboo
 */
public class ImageChooserGroupActivity extends Activity implements OnItemClickListener {
    public static final int REQUEST_CODE_IMAGE_CHOOSER = 11;
    public static final String EXTRA_SELECTED_IMAGES = "extra_select_images"; // 选中的内容
    public static final String EXTRA_SELECTED_COUNT = "extra_select_count";// 限制选择的个数

    /**
     * loading布局
     */
    private LoadingLayout mLoadingLayout = null;

    /**
     * 图片组GridView
     */
    private GridView mGroupImagesGv = null;

    /**
     * 适配器
     */
    private ImageGroupAdapter mGroupAdapter = null;

    /**
     * 图片扫描一般任务
     */
    private ImageLoadTask mLoadTask = null;


    protected void setTitleControlsInfo() {
        //设置title的标题
        TextView title = (TextView) super.findViewById(com.common.android.lib.R.id.title_bar_title_txt);
        title.setText("相册");
        //设置左边按钮的返回操作
        View leftLayout = super.findViewById(com.common.android.lib.R.id.title_bar_left_layout);
        leftLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_chooser_group);

        setTitleControlsInfo();
        initView();
        loadImages();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_CODE_IMAGE_CHOOSER){
                setResult(Activity.RESULT_OK,data);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 初始化界面元素
     */
    private void initView() {
        mLoadingLayout = (LoadingLayout)findViewById(R.id.loading_layout);
        mGroupImagesGv = (GridView)findViewById(R.id.images_gv);
    }

    /**
     * 加载图片
     */
    private void loadImages() {
        mLoadingLayout.showLoading(true);
        if (!hasExternalStorage()) {
            mLoadingLayout.showEmpty(getString(R.string.donot_has_sdcard));
            return;
        }

        // 线程正在执行
        if (mLoadTask != null && mLoadTask.getStatus() == Status.RUNNING) {
            return;
        }

        mLoadTask = new ImageLoadTask(this, new OnTaskResultListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResult(boolean success, String error, Object result) {
                mLoadingLayout.showLoading(false);
                // 如果加载成功
                if (success && result != null && result instanceof ArrayList) {
                    setImageAdapter((ArrayList<ImageGroup>)result);
                } else {
                    // 加载失败，显示错误提示
                    mLoadingLayout.showFailed(getString(R.string.loaded_fail));
                }
            }
        });
        execute(mLoadTask);
    }

    /**
     * 构建GridView的适配器
     * 
     * @param data
     */
    private void setImageAdapter(ArrayList<ImageGroup> data) {
        if (data == null || data.size() == 0) {
            mLoadingLayout.showEmpty(getString(R.string.no_images));
        }
        mGroupAdapter = new ImageGroupAdapter(this, data, mGroupImagesGv);
        mGroupImagesGv.setAdapter(mGroupAdapter);
        mGroupImagesGv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
        ImageGroup imageGroup = mGroupAdapter.getItem(position);
        if (imageGroup == null) {
            return;
        }
        ArrayList<String> childList = imageGroup.getImages();
        Intent mIntent = getIntent();
        mIntent.setClass(this, ImageListActivity.class);
        mIntent.putExtra(ImageListActivity.EXTRA_TITLE, imageGroup.getDirName());
        mIntent.putStringArrayListExtra(ImageListActivity.EXTRA_IMAGES_DATAS, childList);
        startActivityForResult(mIntent,REQUEST_CODE_IMAGE_CHOOSER);
    }

    public static boolean hasExternalStorage() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static <Params, Progress, Result> void execute(AsyncTask<Params, Progress, Result> task,
                                                          Params... params) {
        if (Build.VERSION.SDK_INT >= 11) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }

}
