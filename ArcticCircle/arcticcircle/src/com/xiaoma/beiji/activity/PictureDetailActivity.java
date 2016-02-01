package com.xiaoma.beiji.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.controls.view.photoview.HackyViewPager;
import com.xiaoma.beiji.controls.view.photoview.PhotoView;
import com.xiaoma.beiji.controls.view.photoview.PhotoViewAttacher;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * 类名称： PictureDetailActivity
 * 类描述： 图片详情
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年11月21 14:52
 * 修改备注：
 *
 * @version 1.0.0
 */
public class PictureDetailActivity extends SimpleBaseActivity {
    private TextView pictureIndexTv;
    private ViewPager picGr;
    private int picIndex;
    private int picCount;
    protected List<String> picUrlList;

    public static final String PICTURE_URL_LIST = "picList";
    public static final String PICTURE_INDEX = "picIndex";

    @Override
    protected int getTitleBarId() {
        return -1;
    }

    @Override
    protected String getActivityTitle() {
        return null;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_picture_detail;
    }

    @Override
    protected void initComponents() {
        picGr = (HackyViewPager) this.findViewById(R.id.pic_vp);
        pictureIndexTv = (TextView) findViewById(R.id.picture_index_tv);
    }

    @Override
    protected void loadData() {
        picUrlList = getIntent().getStringArrayListExtra(PICTURE_URL_LIST);
        picIndex = getIntent().getIntExtra(PICTURE_INDEX, 0);
        picCount = picUrlList.size();
        PictureDetailAdapter friendPicDetailAdapter = new PictureDetailAdapter(picUrlList, PictureDetailActivity.this);
        picGr.setAdapter(friendPicDetailAdapter);
        picGr.setOnPageChangeListener(new PicturePageChangeListener());
        picGr.setCurrentItem(picIndex);
        pictureIndexTv.setText((picIndex + 1) + "/" + picCount + "");
    }

    public class PicturePageChangeListener implements OnPageChangeListener {
        public void onPageSelected(int arg0) {
            picIndex = arg0;
            pictureIndexTv.setText((picIndex + 1) + "/" + picCount + "");
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageScrollStateChanged(int arg0) {
        }
    }

    private class PictureDetailAdapter extends PagerAdapter {
        private Context mContext;
        private LayoutInflater inflater;
        private List<String> picList;

        public PictureDetailAdapter(List<String> picList, Context context) {
            this.mContext = context;
            inflater = LayoutInflater.from(mContext);
            this.picList = picList;
        }

        public int getCount() {
            return picList.size();
        }

        public View instantiateItem(ViewGroup container, final int position) {
            //View convertView = viewMap.get(position);
            View convertView = inflater.inflate(R.layout.viewpager_item_picture_detail, null);
            final PhotoView picIv = (PhotoView) convertView.findViewById(R.id.picture_iv);
            final String picUrl = picList.get(position);
            if (picUrl == null)
                return null;
            picIv.setImageURI(picUrl);
            picIv.setPictureClickListener(new PhotoViewAttacher.PictureClickListener() {
                public void click(View v) {
                    PictureDetailActivity.this.finish();
                }
            });
            picIv.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final File file = ImageLoader.getInstance().getDiscCache().get(picUrl);
                    if (file.exists() && file.isFile()) {
//						AlertUtil.showAlert(PictureDetailActivity.this, new String[]{"保存到本地"}, "取消", new OnAlertSelectId() {
//							public void onClick(int whichButton) {
//								switch (whichButton) {
//								case 0:
//									String path = FileUtils.copyFile(file, LianlianAppConstants.FileConstant.SYSTEM_CAMERA_PATH + File.separator + StringUtils.getTime(new Date(), "yyyy-MM-dd-HH-mm-ss-SSS") + ".jpg");
//									if(StringUtils.isNotEmpty(path)){
//										ToastUtil.showToast(getApplicationContext(), "保存图片成功，存储路径为：" + path, Toast.LENGTH_LONG);
//										scanPhotos(path, getApplicationContext());
//									}
//									break;
//								}
//							}
//						}, null);
                    }
                    return true;
                }
            });
            container.addView(convertView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            return convertView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    public void scanPhotos(String filePath, Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(new File(filePath));
            intent.setData(uri);
            context.sendBroadcast(intent);
        } catch (Exception e) {
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_nochange, R.anim.anim_out_alpha);
    }

}
