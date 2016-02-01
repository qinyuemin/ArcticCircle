package com.xiaoma.beiji.database;

import android.content.Context;
import android.os.Environment;
import com.common.android.lib.util.SharedPreferencesUtil;
import com.xiaoma.beiji.common.AppConstants;

import java.io.*;

/**
 * 类名称： SQLiteDBManager
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月17 23:12
 * 修改备注：
 *
 * @version 1.0.0
 */
public class SQLiteDBManager {
    private static SQLiteDBManager ourInstance = null;

    public static SQLiteDBManager getInstance(Context context) {
        if(ourInstance == null){
            ourInstance = new SQLiteDBManager(context);
        }
        return ourInstance;
    }

    private SQLiteDBManager(Context context) {
        this.context = context;
    }

    private Context context;
    public static final String PACKAGE_NAME = "com.xiaoma.beiji";
    public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME + "/"; // 在手机里存放数据库的位置

    /**
     * 判断 数据库的版本
     *
     * @return
     */
    public boolean isUpgrade() {
        int oldVersion = SharedPreferencesUtil.getSettingInt(context, AppConstants.SHARED_PREFERENCES_BASE_NAME, TableMetadataDef.DATABASE_BASE_VERSION_KEY);
        if (TableMetadataDef.DATABASE_BASE_VERSION > oldVersion || oldVersion == 0) {
            return true;
        }
        return false;
    }

    public boolean copyDatabase() {
        try {
            // 首先创建目录
            File simpleDbPath = new File(DB_PATH);
            if (!simpleDbPath.exists()) {
                simpleDbPath.mkdirs();
            }
            String simpleDBFilename = DB_PATH + File.separator + TableMetadataDef.DATABASE_BASE_NAME;
            File simpleDBFile = new File(simpleDBFilename);
            // 判断数据库文件是否存在，如果存在直接返回
            if (!isUpgrade()) {
                if (simpleDBFile.exists()) {
                    System.out.println("数据库已经存在");
                    return true;
                }
            } else {
                if (simpleDBFile.exists()) {
                    simpleDBFile.delete();
                }
            }
            InputStream is = context.getAssets().open(TableMetadataDef.DATABASE_BASE_NAME); // 欲导入的数据库

            FileOutputStream fos = new FileOutputStream(simpleDBFilename);
            byte[] buffer = new byte[1024 * 4];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            fos.close();
            is.close();
            System.out.println("数据库拷贝成功");
            SharedPreferencesUtil.setSetting(context, AppConstants.SHARED_PREFERENCES_BASE_NAME, TableMetadataDef.DATABASE_BASE_VERSION_KEY, TableMetadataDef.DATABASE_BASE_VERSION);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
