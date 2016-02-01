/**
 * 项目名： eyah
 * 包名： com.sttri.eyah.util
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.util;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.io.*;

/**
 *
 * 类名称： ImageUtils
 * 类描述： 
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年07月03 15:35
 * 修改备注：
 * @version 1.0.0
 *
 */

public class ImageUtils {

    public static Bitmap readBitmap(Context context, int resId) {
        return readBitmap(context, resId, Bitmap.Config.RGB_565);
    }

    public static Bitmap readBitmap(Context context, int resId, Bitmap.Config config) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = config;
        opt.inPurgeable = true;
        opt.inInputShareable = true; // 获取资源图片
        InputStream is = null;
        for(int i = 0; i < 4; i++){
            try{
                if(i == 0){
                    opt.inSampleSize = 1;
                }else{
                    opt.inSampleSize = opt.inSampleSize * 2;
                }
                is = context.getResources().openRawResource(resId);
                return BitmapFactory.decodeStream(is, null, opt);
            }catch(OutOfMemoryError error){
                System.gc();
                if(is != null){
                    try {
                        Thread.sleep(200);
                        is.close();
                    } catch (Exception e) {}
                }
            } finally {
                if(is != null){
                    try {
                        is.close();
                    } catch (IOException e) {}
                }
                is = null;
            }
        }
        return null;
    }

    public static Bitmap readBitmap(String filePath, Bitmap.Config config) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = config;
        opt.inPurgeable = true;
        opt.inInputShareable = true; // 获取资源图片
        InputStream is = null;
        for(int i = 0; i < 4; i++){
            try{
                if(i == 0){
                    opt.inSampleSize = 1;
                }else{
                    opt.inSampleSize = opt.inSampleSize * 2;
                }
                return BitmapFactory.decodeFile(filePath, opt);
            }catch(OutOfMemoryError error){
                System.gc();
                if(is != null){
                    try {
                        Thread.sleep(200);
                        is.close();
                    } catch (Exception e) {}
                }
            } finally {
                if(is != null){
                    try {
                        is.close();
                    } catch (IOException e) {}
                }
                is = null;
            }
        }
        return null;
    }

    public static Bitmap readBitmap(String filePath) {
        return readBitmap(filePath, Bitmap.Config.RGB_565);
    }


    /**
     * 将bitmap 转换为 带 指定宽度白色边框的 圆形图片
     * @param bitmap
     * @param borderWidth
     * @return
     */
    public static Bitmap toCircleBitmapWithWhiteBorder(Bitmap bitmap,int borderWidth) {
        if(null==bitmap){
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if(!(width>0&&height>0)){
            return null;
        }


        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int radius = Math.min(h / 2, w / 2);
        Bitmap output = Bitmap.createBitmap(w + 8, h + 8, Bitmap.Config.ARGB_8888);

        Paint p = new Paint();
        p.setAntiAlias(true);

        Canvas c = new Canvas(output);
        c.drawARGB(0, 0, 0, 0);
        p.setStyle(Paint.Style.FILL);

        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        c.drawBitmap(bitmap, 4, 4, p);
        p.setXfermode(null);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.WHITE);
        p.setStrokeWidth(borderWidth);
        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

        return output;
//      Rect src = new Rect((width-ovalLen)/2, (height-ovalLen)/2,ovalLen, ovalLen);
//      Bitmap output = Bitmap.createBitmap(ovalLen,ovalLen,Config.ARGB_8888);
//      Canvas canvas = new Canvas(output);
//      bitmap = Bitmap.createBitmap(bitmap,src.left, src.top, ovalLen, ovalLen, null, true);
//
//      Paint paintBorder = new Paint();
//      paintBorder.setAntiAlias(true);
//      paintBorder.setColor(android.R.color.white);
//      Path pathBorder = new Path();
//      pathBorder = new Path();
//      pathBorder.addOval(new RectF(0, 0,ovalLen+2, ovalLen+2), Path.Direction.CW);
//
//      Path path = new Path();
//      path.addOval(new RectF(0, 0,ovalLen, ovalLen), Path.Direction.CW);
//      BitmapShader tempShader = new BitmapShader(bitmap,BitmapShader.TileMode.CLAMP,BitmapShader.TileMode.CLAMP);
//      Paint paint = new Paint();
//      paint.setAntiAlias(true);
//      paint.setShader(tempShader);
//      canvas.drawPath(path, paint);
//      return output;
    }

    public static Bitmap toCircleBitmapWithWhiteBorder(Bitmap bitmap) {
        return toCircleBitmapWithWhiteBorder(bitmap,3);
    }

    public static Bitmap getBitmapByUrl(String imageUrl) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        Bitmap m = imageLoader.getMemoryCache().get(imageUrl);
        if(m == null) {
            File file = imageLoader.getDiscCache().get(imageUrl);
            if(file != null && file.exists() && file.isFile()) {
                FileInputStream fis = null;
                try {
                    m = BitmapFactory.decodeStream(fis = new FileInputStream(file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (OutOfMemoryError error){
                    error.printStackTrace();
                    System.gc();
                } finally {
                    if(fis != null){
                        try {
                            fis.close();
                        } catch (IOException e) {}
                        fis = null;
                    }
                }
            }
        }
        return m;
    }

    public static Bitmap toCircleBitmap(Bitmap bitmap) {
        if(null==bitmap){
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if(!(width>0&&height>0)){
            return null;
        }
        int ovalLen=Math.min(width, height);
        Rect src = new Rect((width-ovalLen)/2, (height-ovalLen)/2,ovalLen, ovalLen);
        Bitmap output = Bitmap.createBitmap(ovalLen,ovalLen, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        bitmap = Bitmap.createBitmap(bitmap,src.left, src.top, ovalLen, ovalLen, null, true);
        Path path = new Path();
        path.addOval(new RectF(0, 0,ovalLen, ovalLen), Path.Direction.CW);
        BitmapShader tempShader = new BitmapShader(bitmap,BitmapShader.TileMode.CLAMP,BitmapShader.TileMode.CLAMP);
        paint.setShader(tempShader);
        canvas.drawPath(path, paint);
        return output;
    }

    public static void getImageByUrl(ImageView imgView, String url, DisplayImageOptions options) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(url, imgView, options);
    }

    public static void getImageByUrl(ImageView imgView, String url, DisplayImageOptions options, ImageLoadingListener listener) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(url, imgView, options, listener);
    }

    public static void getImageByUrlWithCache(ImageView imageView, String imageUrl, DisplayImageOptions options, int width, int height) {
        ImageLoader imageLoader = ImageLoader.getInstance();

        Bitmap m = imageLoader.getMemoryCache().get(imageUrl);
        boolean hasBmp = false;
        if (m != null) {
            imageView.setImageBitmap(m);
            hasBmp = true;
        } else {
            File file = imageLoader.getDiscCache().get(imageUrl);
            if (file != null && file.exists()) {
                String imagePath = file.getPath();
                try {
                    m = ImageUtils.getBitmapFromFile(imagePath, ImageUtils.getMaxSampleSize(imagePath, width, height));
                    imageView.setImageBitmap(m);
                    hasBmp = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (hasBmp == false) {
            imageLoader.displayImage(imageUrl, imageView, options);
        }
    }

    public static String saveImageToSize(Bitmap image, String savepath, int k_bytes) {
        FileOutputStream out = null;
        try {
            // File sdDir = Environment.getExternalStorageDirectory();
            // com.luluyou.android.lib.utils.Log.d("DFACE",sdDir.toString());
            // File f=new File(sdDir.getAbsolutePath()+"/image/"+"temp.jpg");
            byte[] data = compress(image, k_bytes, false);
            if (data != null) {
                out = new FileOutputStream(savepath);
                if (out != null) {
                    out.write(data);
                    out.flush();
                }
            }
            return savepath;// f.getAbsolutePath();
        } catch (Exception e) {
            Log.e("ImgHelper.saveImageToSize()", "", e);
            return "";
        } finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {}
            }
            out = null;
        }
    }

    /**
     * The cycle compression specify meet the specified target size(100kb)
     *
     * @param bmp
     * @param target_size
     *            unit is kb
     * @return
     */
    public static byte[] compress(Bitmap bmp, int target_size, boolean recyle) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        int quality = 100;
        int length = baos.size() / 1024;
        // The cycle compression specify meet the specified size
        while (length >= target_size) {
            if(quality > 10){
                quality -= 10;
            }else{
                quality -= 1;
            }
            if(quality <= 0){
                break;
            }
            baos.reset();
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            length = baos.toByteArray().length / 1024;
            Log.v("compress ------", "循环压缩  压缩率：" + quality + " 压缩后大小: " + baos.size() + "要求压缩到(K)：" + target_size);
        }
        if (recyle)
            bmp.recycle();
        Log.v("compress ------", "压缩率：" + quality + " 压缩后大小: " + baos.size() + "要求压缩到(K)：" + target_size);
        return baos.toByteArray();
    }

    public static void displayImage(ImageView imageView,String imageUrl) {
        displayImage(imageView, imageUrl, null, null);
    }

    public static void displayImage(ImageView imageView,String imageUrl,DisplayImageOptions option) {
        displayImage(imageView, imageUrl, option, null);
    }

    public static void displayImage(ImageView imageView,String imageUrl,DisplayImageOptions option, ImageLoadingListener listener) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        Bitmap bitmap = displayImageFromLocal(imageUrl);
        if(bitmap != null){
            //取消设置imageView的任务
            imageLoader.cancelDisplayTask(imageView);
            imageView.setImageBitmap(bitmap);
            if(listener != null){
                listener.onLoadingComplete(imageUrl, imageView, bitmap);
            }
        }else{
            imageLoader.displayImage(imageUrl, imageView, option, listener);
        }
    }

    /**
     * 从本地文件或本地内存查找图片并显示
     * @return Bitmap 如果为null，表示本地没有图片
     */
    public static Bitmap displayImageFromLocal(String imageUrl){
        ImageLoader imageLoader = ImageLoader.getInstance();
        Bitmap m = null;
        if(imageUrl != null){
            m = imageLoader.getMemoryCache().get(imageUrl);
            if(m == null){
                File file = imageLoader.getDiscCache().get(imageUrl);
                if(file != null && file.exists()) {
                    FileInputStream is = null;
                    try {
                        m = BitmapFactory.decodeStream(is = new FileInputStream(file));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (OutOfMemoryError error){
                        System.gc();
                        error.printStackTrace();
                    } finally {
                        if(is != null){
                            try {
                                is.close();
                            } catch (IOException e) {}
                            is = null;
                        }
                    }
                }
            }
        }
        return m;
    }

    /**
     * 获取大于一定范围内的区域内图片的最小缩放比例
     * 例如范围为100 * 100，图片大小为2000*1000，那么计算出来的图片大小应该是200 * 100
     * @param filePath
     * @param maxWidth 图片范围的最大宽
     * @param maxHeight 图片范围的最大高
     * @return
     */
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

    /**
     * 从文件中获取图片
     *
     * @param path
     *            图片的路径
     * @return
     */
    public static Bitmap getBitmapFromFile(String path) {
        try {
            return BitmapFactory.decodeFile(path, ImageUtils.getMaxSampleSize(path, 0, 0));
        } catch (OutOfMemoryError e) {
            System.gc();
            return null;
        }
    }

    public static Bitmap getBitmapFromFile(String path, BitmapFactory.Options options) {
        try {
            return BitmapFactory.decodeFile(path, options);
        } catch (OutOfMemoryError e) {
            System.gc();
            return null;
        }
    }

    public static Drawable getGrayDrawable(Drawable drawable){
        drawable.mutate();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(cm);
        drawable.setColorFilter(cf);
        return drawable;
    }
}
