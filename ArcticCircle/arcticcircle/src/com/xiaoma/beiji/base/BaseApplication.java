/**
 * 项目名： eyah
 * 包名： com.sttri.eyah.base
 * 版本信息： 1.0.0
 * Copyright (c) -版权所有
 */
package com.xiaoma.beiji.base;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import com.baidu.location.BDLocation;
import com.common.android.lib.db.SqliteDatabaseAccess;
import com.common.android.lib.util.FileUtil;
import com.common.android.lib.util.SDCardUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.xiaoma.beiji.common.AppConstants;
import com.xiaoma.beiji.controls.dialog.LoginTimeoutDialog;
import com.xiaoma.beiji.controls.view.chatting.ChattingCache;
import com.xiaoma.beiji.database.SQLiteDBManager;
import com.xiaoma.beiji.database.SimpleBaseDB;
import com.xiaoma.beiji.util.ImageLoaderUtil;
import com.xiaoma.beiji.util.IntentUtil;

/**
 * 类名称： BaseApplication
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年06月08 14:17
 * 修改备注：
 *
 * @version 1.0.0
 */
public class BaseApplication extends Application {

    private static final String TAG = BaseApplication.class.getSimpleName();
    private static BaseApplication _instance;
    private boolean applicationInited = false;
    private CrashHandler crashHandler;
    private BDLocation location;
    private SimpleBaseDB simpleBaseDB = null;


    public static BaseApplication getInstance() {
        return _instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.setProperty("http.keepAlive", "false");
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        System.setProperty("java.net.preferIPv6Addresses", "false");
        _instance = this;
        initApplication();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        finalApplication();
    }

    /**
     * 初始化应用的全局变量
     *
     * @return
     */
    public boolean initApplication() {
        Log.i(TAG, "执行应用程序的初始化操作");
        if (applicationInited) {
            return true;
        }
        //初始化增强数值
        applicationInited = true;
        //设置联连的工作目录
        String sdCardPath = SDCardUtil.getSDCardPath();
        if (TextUtils.isEmpty(sdCardPath)) {
            Log.i(TAG, "无法设置工作目录");
        } else {
            FileUtil.makeDir(AppConstants.FileConstant.BEIJI_APP_BASE_PATH);
        }
        //初始化crashhandler，这个要放到工作目录创建之后
        this.crashHandler = new CrashHandler();
        this.crashHandler.init();

        initImageLoader();
        initDatabase();
        ChattingCache.getApplicationDataCache().initDataCache(this);

        return true;
    }

    /**
     * 销毁 application 维持的内容
     */
    public void finalApplication() {
        Log.i(TAG, "执行应用程序的销毁操作");
        if (applicationInited) {
            applicationInited = false;
            //执行程序停止前的其他资源释放操作
            finalDatabase();
            ChattingCache.getApplicationDataCache().initDataCache(this);
            IntentUtil.stopIMChatService(this);
        }
    }

    private void initDatabase() {
        // 初始化数据库连接对象
        if(null == this.simpleBaseDB){
            this.simpleBaseDB = new SimpleBaseDB(this);
            doSimpleQuery();
        }
    }

    private void doSimpleQuery(){
        if(null != simpleBaseDB){
            int count = simpleBaseDB.queryNumber(SqliteDatabaseAccess.SQLITE3_TEST_SQL, new String[]{});
            Log.i(TAG, "表(Sqlite_master )中得记录数是:" + count);
        }
    }

    private void finalDatabase() {
        // 关闭数据库连接
        if (this.simpleBaseDB != null) {
            this.simpleBaseDB.closeDB();
            this.simpleBaseDB = null;
        }
    }

    private void initImageLoader() {
        //初始化图片加载器
        DisplayImageOptions defaultOptions = ImageLoaderUtil.getDefaultImageLoaderOptionBuilder().build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                        //设置线程的优先级
                .denyCacheImageMultipleSizesInMemory()
                        //当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                        //设置缓存文件的名字
                .discCacheFileCount(1000)
                        //缓存文件的最大个数
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                        // 设置图片下载和显示的工作队列排序
                        //.memoryCache(new WeakMemoryCache())
                .memoryCache(new LruMemoryCache((int) (Runtime.getRuntime().maxMemory() / 8))).threadPoolSize(3).discCacheExtraOptions(1024, 1024, Bitmap.CompressFormat.PNG, 75, null).defaultDisplayImageOptions(defaultOptions)
                .memoryCacheExtraOptions(512, 512).build();

        //Initialize ImageLoader with configuration
        ImageLoader.getInstance().init(config);
    }

    public SimpleBaseDB getSimpleBaseDB() {
        return simpleBaseDB;
    }

    public BDLocation getLocation() {
        return location;
    }

    public void setLocation(BDLocation location) {
        this.location = location;
    }
}