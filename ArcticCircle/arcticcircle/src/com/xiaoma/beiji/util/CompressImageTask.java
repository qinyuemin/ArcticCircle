package com.xiaoma.beiji.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import com.makeapp.javase.lang.StringUtil;
import com.xiaoma.beiji.common.AppConstants;

import java.io.File;

/**
 * 图片压缩算法任务
 *
 * @author
 * @Date 2014-11-11 15:54
 */
public class CompressImageTask extends AsyncTask<String, Void, String> {

    private int resultImageWidth = 0;
    private int resultImageHeight = 0;
    private CompressImageTaskParam mParam;
    private OnCompressImageListener mOnCompressImageListener;

    public CompressImageTask(CompressImageTaskParam param, OnCompressImageListener onCompressImageListener) {
        if (param != null) {
            this.mParam = param;
        } else {
            this.mParam = new CompressImageTaskParam();
        }
        if (this.mParam.config == null) {
            this.mParam.config = Bitmap.Config.ARGB_8888;
        }
        this.mOnCompressImageListener = onCompressImageListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mOnCompressImageListener != null) {
            mOnCompressImageListener.onCompressStart();
        }
    }

    public static BitmapFactory.Options getMaxSampleSize(String filePath, float maxWidth, float maxHeight) {
        int inSampleSize = 1;
        if (maxWidth > 0 || maxHeight > 0) {
            BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
            bmpFactoryOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, bmpFactoryOptions);
            float widthSampleSize = 1;
            if (maxWidth > 0) {
                widthSampleSize = bmpFactoryOptions.outWidth / maxWidth;
                if (widthSampleSize < 1) {
                    widthSampleSize = 1;
                }
            }
            float heightSampleSize = 1;
            if (maxHeight > 0) {
                heightSampleSize = bmpFactoryOptions.outHeight / maxHeight;
                if (heightSampleSize < 1) {
                    heightSampleSize = 1;
                }
            }

            if (maxWidth == 0) {
                inSampleSize = (int) heightSampleSize;
            } else if (maxHeight == 0) {
                inSampleSize = (int) widthSampleSize;
            } else {
                if (widthSampleSize <= heightSampleSize) {
                    inSampleSize = (int) widthSampleSize;
                } else {
                    inSampleSize = (int) heightSampleSize;
                }
            }
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
//      options.inPreferredConfig = Bitmap.Config.ARGB_4444;//默认是Bitmap.Config.ARGB_8888
        options.inPurgeable = true;
        options.inInputShareable = true;
        return options;
    }

    public static Bitmap getBitmapFromFile(String path) {
        try {
            return BitmapFactory.decodeFile(path, getMaxSampleSize(path, 0, 0));
        } catch (OutOfMemoryError e) {
            System.gc();
            return null;
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String imagePath = null;
        try {
            String srcPath = params[0];

            Bitmap bmpImage = null;
            if (mParam.useOriginalImage) {
                bmpImage = getBitmapFromFile(srcPath);
            } else {
                bmpImage = BitmapDecodeTool.decodeBitmap(srcPath, mParam.imageContainerWidth, mParam.imageContainerHeight, mParam.config, mParam.outerImageContainer);
            }

            int rotate = ImgHelper.getImageFileRotate(srcPath);
            if (bmpImage != null) {
                int width = bmpImage.getWidth();
                int height = bmpImage.getHeight();
                float scale = 1;
                if (mParam.useOriginalImage == false) {//如果不使用原图，才需要缩放
                    scale = BitmapDecodeTool.calculateMaxScale(width, height, mParam.imageContainerWidth, mParam.imageContainerHeight, rotate, mParam.outerImageContainer);
                }
                if (rotate != 0 || scale < 1) {
                    // Log.i(this.getClass().getSimpleName(), "onCreate 4");
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
                if (StringUtil.isInvalid(mParam.outputImagePath)) {
                    imagePath = FileHelper.getTmpImgFileLoad();
                } else {
                    imagePath = mParam.outputImagePath;
                    if (imagePath.indexOf("/") == -1 && imagePath.indexOf("\\") == -1) {
                        //如果传递过来的文件名只是一个name，则做兼容保存至temp目录下
                        imagePath = AppConstants.FileConstant.DIR_APK_TEMP + File.separator + imagePath;
                        File f = new File(imagePath);
                        File folder = f.getParentFile();
                        if (folder.exists() == false || folder.isDirectory() == false) {
                            folder.mkdirs();
                        }
                    }
                }
                resultImageWidth = bmpImage.getWidth();
                resultImageHeight = bmpImage.getHeight();
                int targetSize = 512;
                if (mParam.useOriginalImage) {
                    targetSize = 4096;
                }
                imagePath = ImgHelper.saveImageToSize(bmpImage, imagePath, targetSize);
                if (bmpImage != null && bmpImage.isRecycled() == false) {
                    bmpImage.recycle();
                }
            }
        } catch (Exception e) {
        }
        return imagePath;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (mOnCompressImageListener != null) {
            mOnCompressImageListener.onCompressFinish(result, resultImageWidth, resultImageHeight);
        }
    }

    public static class CompressImageTaskParam {
        public Bitmap.Config config;
        public int imageContainerWidth;
        public int imageContainerHeight;
        public boolean outerImageContainer;
        public String outputImagePath;
        public boolean useOriginalImage;
    }

    public static interface OnCompressImageListener {
        public void onCompressStart();

        public void onCompressFinish(String imagePath, int imageWidth, int imageHeight);
    }
}