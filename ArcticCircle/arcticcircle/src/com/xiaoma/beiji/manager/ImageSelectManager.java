/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.manager
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.manager;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import com.makeapp.javase.lang.StringUtil;
import com.makeapp.javase.util.DateUtil;
import com.xiaoma.beiji.activity.GetImageActivity;
import com.xiaoma.beiji.base.SimpleFragment;
import com.xiaoma.beiji.common.AppConstants;
import com.xiaoma.beiji.util.CommUtil;
import com.xiaoma.beiji.util.FileHelper;
import com.xiaoma.beiji.util.ImgHelper;
import com.xiaoma.beiji.util.ToastUtil;

import java.io.File;

/**
 * 类名称： ImageSelectManager
 * 类描述： 用户获取图片  （相册/拍照）
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月16 16:18
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ImageSelectManager {

    private Context context;

    private static ImageSelectManager ourInstance = new ImageSelectManager();

    public static ImageSelectManager getInstance() {
        return ourInstance;
    }

    private ImageSelectManager() {
    }

    public static final int MAX_IMAGE_SIZE = 9;
    public static final int INTENT_REQUEST_CODE = 10;
    public static final int INTENT_REQUEST_CODE_GET_IMAGE_ACTIVITY = 6;
    public static final int INTENT_REQUEST_CODE_CROP = 4;

    private GetImageActivity.GetImageConfig getImageConfig = null;
    private String mTempImagePath;
    private String mTempCropImagePath;// 裁剪图的缓存路径

    public void selectImage(Activity activity, GetImageActivity.GetImageType mGetImageType) {
        if (getImageConfig == null) {
            getImageConfig = new GetImageActivity.GetImageConfig();
        }
        getImageConfig.setGetImageType(mGetImageType);
        getImageConfig.setOutputImagePath(getTempImagePath());
        GetImageActivity.gotoGetImage(activity, getImageConfig, INTENT_REQUEST_CODE_GET_IMAGE_ACTIVITY);
    }

    public void selectImage(SimpleFragment fragment, GetImageActivity.GetImageType mGetImageType) {
        if (getImageConfig == null) {
            getImageConfig = new GetImageActivity.GetImageConfig();
        }
        getImageConfig.setGetImageType(mGetImageType);
        getImageConfig.setOutputImagePath(getTempImagePath());
        GetImageActivity.gotoGetImage(fragment, getImageConfig, INTENT_REQUEST_CODE_GET_IMAGE_ACTIVITY);
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
}
