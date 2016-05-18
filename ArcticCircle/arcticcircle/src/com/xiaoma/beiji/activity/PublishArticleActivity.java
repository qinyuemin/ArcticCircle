/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.activity
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ViewUtil;
import com.makeapp.javase.lang.StringUtil;
import com.makeapp.javase.util.DateUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.common.AppConstants;
import com.xiaoma.beiji.entity.FileUploadResultEntity;
import com.xiaoma.beiji.manager.ImageSelectManager;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.BitmapDecodeTool;
import com.xiaoma.beiji.util.CommUtil;
import com.xiaoma.beiji.util.FileHelper;
import com.xiaoma.beiji.util.ImgHelper;
import com.xiaoma.beiji.util.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 类名称： PublishActivity
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月01 11:52
 * 修改备注：
 *
 * @version 1.0.0
 */
public class PublishArticleActivity extends SimpleBaseActivity implements View.OnClickListener {
    private static final String TAG = PublishArticleActivity.class.getSimpleName();
    public static final int MAX_IMAGE_SIZE = 9;
    public static final int INTENT_REQUEST_CODE = 10;
    private static final int INTENT_REQUEST_CODE_GET_IMAGE_ACTIVITY = 6;
    private static final int INTENT_REQUEST_CODE_CROP = 4;



    private GetImageActivity.GetImageConfig getImageConfig = null;
    private String mTempImagePath;
    private String mTempCropImagePath;// 裁剪图的缓存路径

    private EditText edtContent;
    private EditText edtTitle;
    private List<FileUploadResultEntity> uploadFialedEntities;

    @Override
    protected String getActivityTitle() {
        String title = "";
//        switch (releaseType){
//            case PUBLISH_DIANPING:
//                title = "点评";
//                break;
//            case PUBLISH_WENWEN:
//                title = "求助";
//                break;
//            case PUBLISH_CHANGWEN:
                title = "长文";
//                break;
//        }
        return title;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_publish_article;
    }

    @Override
    protected void initIntent() {
        super.initIntent();
    }

    @Override
    protected void setTitleControlsInfo() {
        super.setTitleControlsInfo();

        ViewUtil.setViewVisibility(this, R.id.title_bar_left_img, View.GONE);
        ViewUtil.setViewVisibility(this, R.id.title_bar_left_sub_txt, View.VISIBLE);
        TextViewUtil.setText(this, R.id.title_bar_left_sub_txt, "取消");


        TextView txtRight = (TextView) findViewById(R.id.title_bar_right_txt);
        txtRight.setText("发布");
        ViewUtil.setViewOnClickListener(this, R.id.title_bar_right_layout, this);
    }

    @Override
    protected void initComponents() {
        setTitleControlsInfo();
        uploadFialedEntities = new ArrayList<>();
        edtContent = (EditText) findViewById(R.id.edt_content);
        edtTitle = (EditText) findViewById(R.id.edt_article_title);
        ViewUtil.setViewOnClickListener(this,R.id.image_inset_img,this);
    }

    @Override
    protected void loadData() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_bar_right_layout:
                publishArticle();
                break;
            case R.id.image_inset_img:
                addImage();
                break;
        }
    }


    private void publishArticle() {
        EditText titleEdite = (EditText) findViewById(R.id.edt_article_title);
        String title = String.valueOf(titleEdite.getText());

        final String content = String.valueOf(edtContent.getText());

        if (TextUtils.isEmpty(content)) {
            ToastUtil.showToast(this, "请输入标题");
            return;
        }

        if (TextUtils.isEmpty(content)) {
            ToastUtil.showToast(this, "请输入内容");
            return;
        }
        JSONArray picList = new JSONArray();

        for(int i=0; i<uploadFialedEntities.size();i++){
            FileUploadResultEntity entity = uploadFialedEntities.get(i);
            if(entity.isUpload){
                JSONObject picObject = new JSONObject();
                picObject.put("path",entity.getUploadPath());
                picObject.put("seq_no",i);
                picObject.put("pic_loc",entity.getLocation());
                picList.add(picObject);
            }else{
                ToastUtil.showToast(this, "图片还未上传完成");
                return;
            }
        }
        HttpClientUtil.Dynamic.dynamicReleasLongText(title, content, picList, new AbsHttpResultHandler() {
            @Override
            public void onSuccess(int resultCode, String desc, Object data) {
                ToastUtil.showToast(PublishArticleActivity.this, "发布成功");
                PublishArticleActivity.this.finish();
            }

            @Override
            public void onFailure(int resultCode, String desc) {
                ToastUtil.showToast(PublishArticleActivity.this, "发布失败:" + desc);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ImageSelectManager.INTENT_REQUEST_CODE_GET_IMAGE_ACTIVITY:
                    if (data != null) {
                        String picturePath = data.getStringExtra(GetImageActivity.KEY_INTENT_IMAGE_PATH);
                        picturePath = compressFile(picturePath);
                        final FileUploadResultEntity entity = new FileUploadResultEntity();
                        entity.setLocalPath(picturePath);
                        HttpClientUtil.imageUpload(picturePath, new AbsHttpResultHandler<FileUploadResultEntity>() {
                            @Override
                            public void onSuccess(int resultCode, String desc, FileUploadResultEntity data) {
                                entity.setServerPath(data.getServerPath());
                                entity.setUploadPath(data.getUploadPath());
                                entity.setUpload(true);
                                int location = edtContent.getSelectionStart();
                                entity.setLocation(location);
                                insertImageToEdit(entity,location);
                            }

                            @Override
                            public void onFailure(int resultCode, String desc) {
                                ToastUtil.showToast(PublishArticleActivity.this, desc);

                            }
                        });
                        uploadFialedEntities.add(entity);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void insertImageToEdit(FileUploadResultEntity entity, int location){
        Editable content = edtContent.getText();
        char obj = 65532;
        String objstr = ""+obj;
        content.insert(location,objstr);
        SpannableString string = new SpannableString(content);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.outWidth = CommUtil.getScreenWidth(this);
        options.inScaled = true;
        Bitmap bitmap = BitmapFactory.decodeFile(entity.getLocalPath(),new BitmapFactory.Options());
        ImageSpan imageSpan = new ImageSpan(bitmap);
        string.setSpan(imageSpan,location,location+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        edtContent.setText(string);
        edtContent.setSelection(location+1);
    }


    private void addImage() {
        if (uploadFialedEntities.size() < MAX_IMAGE_SIZE) {
//            if (getImageConfig == null) {
//                getImageConfig = new GetImageActivity.GetImageConfig();
//            }
//            getImageConfig.setOutputImagePath(getTempImagePath());
//            GetImageActivity.gotoGetImageSimple(this, getImageConfig, (MAX_IMAGE_SIZE - uploadFialedEntities.size()));
            ImageSelectManager.getInstance().selectImage(this, GetImageActivity.GetImageType.BOTH);
        } else {
            ToastUtil.showToast(getApplicationContext(), String.format("最多上传 %d 张图片", MAX_IMAGE_SIZE));
        }
    }

    private String getTempImagePath() {
        deleteTempImage();// 如果该文件存在，先删除该文件（为了确保文件的准确性，如果不删除，可能会得到以前创建的文件）
        mTempImagePath = AppConstants.FileConstant.DIR_APK_TEMP + File.separator + "temp_img_" + DateUtil.getStringDate("yyyy-MM-dd-HH-mm-ss-SSS") + AppConstants.FileConstant.CACHE_FILE_EXT_NAME;
        File file = new File(mTempImagePath);
        File f = file.getParentFile();
        if (f.exists() == false || f.isDirectory() == false) {
            f.mkdirs();
        }
        return mTempImagePath;
    }

    private void deleteTempImage() {
        if (isImageExists(mTempImagePath)) {
            File file = new File(mTempImagePath);
            file.delete();
            mTempCropImagePath = null;
        }
    }

    private boolean isImageExists(String path) {
        if (StringUtil.isValid(path)) {
            File file = new File(path);
            return file.exists();
        }
        return false;
    }

    private void startCropActivity(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "false");
        intent.putExtra("scaleUpIfNeeded", true);// 如果截图太小，则自动将选择的图片块放大填充整个高宽区域，避免出现黑色背景填充
        intent.putExtra("noFaceDetection", true);// 去掉人脸识别的功能
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", CommUtil.getScreenWidth(this));
        intent.putExtra("outputY", CommUtil.getScreenWidth(this));
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);// 不返回data，intent会以路径的形式将截图文件带过来
        Uri outUri = getCropImageUri();
        if (outUri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        }
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, INTENT_REQUEST_CODE_CROP);
    }

    private Uri getCropImageUri() {
        deleteCropTempImage();// 如果该文件存在，先删除该文件（为了确保文件的准确性，如果不删除，可能会得到以前创建的文件）
        mTempCropImagePath = AppConstants.FileConstant.DIR_APK_TEMP + File.separator + "temp_img_crop_" + DateUtil.getStringDate("yyyy-MM-dd-HH-mm-ss-SSS") + AppConstants.FileConstant.CACHE_FILE_EXT_NAME;
        File file = new File(mTempCropImagePath);
        File f = file.getParentFile();
        if (f.exists() == false || f.isDirectory() == false) {
            f.mkdirs();
        }
        return Uri.fromFile(file);
    }

    private void deleteCropTempImage() {
        if (isImageExists(mTempCropImagePath)) {
            File file = new File(mTempCropImagePath);
            file.delete();
            mTempCropImagePath = null;
        }
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param data
     */
    private String getImageToView(Intent data) {
        String resultpath = null;
        if (null == data) {
            return resultpath;
        }
        if (isImageExists(mTempCropImagePath)) {
            resultpath = mTempCropImagePath;
        } else {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Object obj = extras.get("data");
                if (obj == null) {
                    obj = data.getData();
                }
                if (obj instanceof String) {
                    resultpath = (String) obj;
                }
                if (obj instanceof Uri) {
                    resultpath = getPathFromUri((Uri) obj);
                } else if (obj instanceof Bitmap) {
                    Bitmap bitmap = extras.getParcelable("data");
                    resultpath = ImgHelper.saveImage(bitmap, FileHelper.getTmpImgFile(), 100);
                }
            }
        }
        // if(StringUtils.isNotEmpty(resultpath)){
        // ImageUtil.getImageByUrl(imageView, Uri.fromFile(new
        // File(resultpath)).toString(),
        // ImageLoaderUtil.getOptionDefaultUserInfo() ,new
        // DefaultImageLoadingListener(imageView,DefaultImageLoadingListener.TYPE_AVATAR_IMG,false));
        // }
        return resultpath;
    }

    private String getPathFromUri(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        String img_path = actualimagecursor.getString(actual_image_column_index);
        return img_path;
    }


    public String compressFile(String srcPath){
        String imagePath = getTempImagePath();
        try {
            Bitmap bmpImage  = BitmapDecodeTool.decodeBitmap(srcPath, 640, 1136, Bitmap.Config.ARGB_8888 , false);
            int rotate = ImgHelper.getImageFileRotate(srcPath);
            if (bmpImage != null) {
                int width = bmpImage.getWidth();
                int height = bmpImage.getHeight();
                float scale = 1;
                scale = BitmapDecodeTool.calculateMaxScale(width, height, 640, 1136, rotate, false);
                if (rotate != 0 || scale < 1) {
                    Matrix matrix = new Matrix();
                    if (rotate != 0) {
                        matrix.setRotate(rotate);
                    }
                    if (scale < 1) {
//						matrix.setScale(scale, scale);
                        matrix.postScale(scale, scale);
                    }
                    Bitmap rotateBitmap = Bitmap.createBitmap(bmpImage, 0, 0, bmpImage.getWidth(), bmpImage.getHeight(), matrix, true);
                    if (rotateBitmap != null && bmpImage != rotateBitmap) {
                        bmpImage.recycle();
                        bmpImage = rotateBitmap;
                        System.gc();
                    }
                }
            }

            if (bmpImage != null) {
                //如果传递过来的文件名只是一个name，则做兼容保存至temp目录下
                File f = new File(imagePath);
                File folder = f.getParentFile();
                if (folder.exists() == false || folder.isDirectory() == false) {
                    folder.mkdirs();
                }

                int targetSize = 512;
                imagePath = ImgHelper.saveImageToSize(bmpImage, imagePath, targetSize);
                if (bmpImage != null && bmpImage.isRecycled() == false) {
                    bmpImage.recycle();
                }
            }
        } catch (Exception e) {
        }
        return imagePath;
    }
}