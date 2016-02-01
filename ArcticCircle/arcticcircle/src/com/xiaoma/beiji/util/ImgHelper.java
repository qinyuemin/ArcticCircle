package com.xiaoma.beiji.util;

import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImgHelper {
    public static int getImageFileRotate(String srcPath) {
        ExifInterface exifInterface = null;
        int rotate = 0;
        try {
            exifInterface = new ExifInterface(srcPath);
            int tag = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (tag == ExifInterface.ORIENTATION_ROTATE_90)
                rotate = 90;
            else if (tag == ExifInterface.ORIENTATION_ROTATE_180)
                rotate = 180;
            else if (tag == ExifInterface.ORIENTATION_ROTATE_270)
                rotate = 270;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotate;
    }


    public static String saveImageToSize(Bitmap image, String savepath, int k_bytes) {
        FileOutputStream out = null;
        try {
            // File sdDir = Environment.getExternalStorageDirectory();
            // com.luluyou.android.lib.utils.Log.d("DFACE",sdDir.toString());
            // File f=new File(sdDir.getAbsolutePath()+"/image/"+"temp.jpg");
            byte[] data = compress(image, k_bytes, false);
            if (data != null) {
                File file = createFileIfNotExists(savepath);
                out = new FileOutputStream(file);
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
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
            out = null;
        }
    }

    /**
     * save bitmap
     *
     * @param bitmap
     * @param quality bitmap quality(1-100)
     * @return
     */
    public static String saveImage(Bitmap bitmap, String filePath, int quality) {
        String path = null;
        try {
            File file = createFileIfNotExists(filePath);
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
                Log.d("saveImage", "saveImage seccess: fileName= " + filePath + ", quality = " + quality);
                out.flush();
                out.close();
                path = filePath;
            } else {
                Log.d("saveImage", "saveImage fail: fileName= " + filePath);
            }
        } catch (Exception e) {
            Log.d("saveImage", "saveImage Exception: " + e);
            e.printStackTrace();
        }
        return path;
    }

    /**
     * The cycle compression specify meet the specified target size(100kb)
     *
     * @param target_size unit is kb
     * @return
     */
    public static byte[] compress(Bitmap bmp, int target_size, boolean recyle) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        int quality = 100;
        int length = baos.size() / 1024;
        // The cycle compression specify meet the specified size
        while (length >= target_size) {
            if (quality > 10) {
                quality -= 10;
            } else {
                quality -= 1;
            }
            if (quality <= 0) {
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

    public static File createFileIfNotExists(String filePath) {
        File file = new File(filePath);
        if (file.exists() == false) {
            File folder = file.getParentFile();
            if (folder.exists() == false || folder.isDirectory() == false) {
                folder.mkdirs();
            }

            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }
}
