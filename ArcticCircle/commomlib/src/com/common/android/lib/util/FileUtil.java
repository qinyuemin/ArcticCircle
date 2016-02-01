/**
 *
 *项目名：LuluYouLib
 *包名：com.luluyou.android.lib.cache
 *文件名：FileUtils.java
 *版本信息：1.0.0
 *创建日期：2014年03月18日-下午2:43:17
 *创建人：jerry.deng
 *Copyright (c) 2013上海路路由信息科技有限公司-版权所有
 * 
 */
package com.common.android.lib.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

/**
*
* 类名称：FileUtils
* 类描述：
* 创建人： Jerry.deng
* 修改人： Jerry.deng
* 修改时间： 2013年12月6日 下午2:43:17
* 修改备注：
* @version 1.0.0
*
*/
public class FileUtil {

    public static final long B = 1;
    public static final long KB = B * 1024;
    public static final long MB = KB * 1024;
    public static final long GB = MB * 1024;
    private static final int BUFFER = 8192;

    private static final String DATA_DIR = "data/";

    /**
     * 格式化文件大小<b> 带有单位
     * 
     * @param size
     * @return
     */
    public static String formatFileSize(long size) {
        StringBuilder sb = new StringBuilder();
        String u = null;
        double tmpSize = 0;
        if (size < KB) {
            sb.append(size).append("B");
            return sb.toString();
        } else if (size < MB) {
            tmpSize = getSize(size, KB);
            u = "KB";
        } else if (size < GB) {
            tmpSize = getSize(size, MB);
            u = "MB";
        } else {
            tmpSize = getSize(size, GB);
            u = "GB";
        }
        return sb.append(twodot(tmpSize)).append(u).toString();
    }

    /**
     * 保留两位小数
     * 
     * @param d
     * @return
     */
    public static String twodot(double d) {
        return String.format("%.2f", d);
    }

    public static double getSize(long size, long u) {
        return (double) size / (double) u;
    }

    /**
     * sd卡挂载且可用
     * 
     * @return
     */
    public static boolean isSdCardMounted() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 递归创建文件目录
     * 
     * @param path
     * */
    public static void CreateDir(String path) {
        if (!isSdCardMounted())
            return;
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取文件
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public static String readTextFile(File file) throws IOException {
        String text = null;
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            text = readTextInputStream(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return text;
    }

    /**
     * 从流中读取文件
     * 
     * @param is
     * @return
     * @throws IOException
     */
    public static String readTextInputStream(InputStream is) throws IOException {
        StringBuffer strbuffer = new StringBuffer();
        String line;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            while ((line = reader.readLine()) != null) {
                strbuffer.append(line).append("\r\n");
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return strbuffer.toString();
    }

    /**
     * 将文本内容写入文件
     * 
     * @param file
     * @param str
     * @throws IOException
     */
    public static void writeTextFile(File file, String str) throws IOException {
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(new FileOutputStream(file));
            out.write(str.getBytes());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 读取表情配置文件
     * 
     * @param context
     * @return
     */
    public static List<String> getEmojiFile(Context context) {
        try {
            List<String> list = new ArrayList<String>();
            InputStream in = context.getResources().getAssets().open("emoji");// 文件名字为rose.txt
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String str = null;
            while ((str = br.readLine()) != null) {
                list.add(str);
            }

            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取一个文件夹大小
     * 
     * @param f
     * @return
     * @throws Exception
     */
    public static long getFileSize(File f) {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    /**
     * 删除文件
     * 
     * @param file
     */
    public static void deleteFile(File file) {

        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        }
    }

    public static String getDataCacheDir(Context context) {
        File dataDir;
        if (AndroidVersionCheckUtils.hasFroyo()) {
            dataDir = AndroidExternalOverFroyoUtils.getDiskCacheDir(context, DATA_DIR);
        } else {
            dataDir = AndroidExternalUnderFroyoUtils.getDiskCacheDir(context, DATA_DIR);
        }
        if (!dataDir.exists()) {
            dataDir.mkdirs();
            // do not allow media scan
            try {
                new File(dataDir, ".nomedia").createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String dataFilepath = dataDir.getAbsolutePath();
        if (!dataFilepath.trim().endsWith(File.separator)) {
            dataFilepath = dataFilepath.trim() + File.separator;
        }
        return dataFilepath;

    }
    public static void makeDir(String fileFullpath) {
        File f = new File(fileFullpath);
        if(!f.exists()){
            if(!f.mkdirs()){
                throw new RuntimeException("无法创建目录[" + fileFullpath + "]");
            }
        }
    }
}
