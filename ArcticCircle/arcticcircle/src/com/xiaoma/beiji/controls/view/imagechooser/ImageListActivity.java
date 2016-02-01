
package com.xiaoma.beiji.controls.view.imagechooser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ViewUtil;
import com.xiaoma.beiji.R;

import java.util.ArrayList;

/**
 * 某个文件夹下的所有图片列表
 * 
 * @author
 */
public class ImageListActivity extends Activity implements OnItemClickListener {

    /**
     * 选中的图片列表配置项名称
     */
    public static String C_SELECTED_IMAGES = "c_selected_images";

    /**
     * title
     */
    public static final String EXTRA_TITLE = "extra_title";

    /**
     * 图片列表extra
     */
    public static final String EXTRA_IMAGES_DATAS = "extra_images";

    /**
     * 图片列表GridView
     */
    private GridView mImagesGv = null;

    /**
     * 图片地址数据源
     */
    private ArrayList<String> mImages = new ArrayList<String>();

    /**
     * 适配器
     */
    private ImageListAdapter mImageAdapter = null;

    private int selectMaxCount = Integer.MAX_VALUE;

    protected void setTitleControlsInfo() {
        //设置title的标题
        String titleStr = getIntent().getStringExtra(EXTRA_TITLE);
        TextView title = (TextView) super.findViewById(com.common.android.lib.R.id.title_bar_title_txt);
        if (!TextUtils.isEmpty(titleStr)) {
            title.setText(titleStr);
        }
        //设置左边按钮的返回操作
        View leftLayout = super.findViewById(com.common.android.lib.R.id.title_bar_left_layout);
        leftLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ViewUtil.setViewVisibility(this,R.id.title_bar_right_layout, View.VISIBLE);
        TextViewUtil.setText(this,R.id.title_bar_right_txt,"完成");
        ViewUtil.setViewOnClickListener(this, R.id.title_bar_right_layout, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageManager.getInstance().clear();
                Intent intent = new Intent();
                intent.putStringArrayListExtra(ImageChooserGroupActivity.EXTRA_SELECTED_IMAGES,mImageAdapter.getSelectedImgs());
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_chooser_list);
        selectMaxCount = getIntent().getIntExtra(ImageChooserGroupActivity.EXTRA_SELECTED_COUNT,Integer.MAX_VALUE);

        setTitleControlsInfo();

        initView();
        if (getIntent().hasExtra(EXTRA_IMAGES_DATAS)) {
            mImages = getIntent().getStringArrayListExtra(EXTRA_IMAGES_DATAS);
            setAdapter(mImages);
        }
    }

    /**
     * 初始化界面元素
     */
    private void initView() {
        mImagesGv = (GridView)findViewById(R.id.images_gv);
    }

    /**
     * 构建并初始化适配器
     * 
     * @param datas
     */
    private void setAdapter(ArrayList<String> datas) {
        mImageAdapter = new ImageListAdapter(this, datas,selectMaxCount);
        mImagesGv.setAdapter(mImageAdapter);
        mImagesGv.setOnItemClickListener(this);
    }

    /*
     * (non-Javadoc)
     * @see
     * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
     * .AdapterView, android.view.View, int, long)
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

    }

    @Override
    public void onBackPressed() {
        if (mImageAdapter != null) {
            ImageManager.getInstance().saveSelectedImages(this, mImageAdapter.getSelectedImgs());
        }
        super.onBackPressed();
    }
}
