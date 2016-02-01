package com.xiaoma.beiji.util;

import com.xiaoma.beiji.common.AppConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class FileHelper {

    public static boolean isExists(String filePath) {
        File f = new File(filePath);
        return f.exists();
    }

    public static String getTmpImgFile() {
        return AppConstants.FileConstant.FILE_APK_IMG_CACHE;
    }

    // 从相册获取图片收存放临时文件
    public static String getTmpImgFileLoad() {
        return AppConstants.FileConstant.FILE_APK_IMG_LOAD_CACHE;
    }

    // 从相机获取图片收存放临时文件
    public static String getTmpImgFileCAPTURE() {
        return AppConstants.FileConstant.FILE_APK_IMG_CAPTURE_CACHE;
    }

    /**
     * 将imputstream转换成file保存
     *
     * @param ins
     * @param file
     */
    public static boolean inputstreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();
            os.close();
            ins.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static File clearTempPngFile(String mFile) {
        File file = new File(mFile);
        try {
            if (file.isFile() && file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}
