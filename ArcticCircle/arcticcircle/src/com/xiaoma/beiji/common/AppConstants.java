package com.xiaoma.beiji.common;


import com.common.android.lib.util.SDCardUtil;
import com.xiaoma.beiji.BuildConfig;

import java.io.File;

public final class AppConstants {

    public final static boolean DEBUG = BuildConfig.DEBUG;

    public static final String SHARED_PREFERENCES_BASE_NAME = "beiji_pref";

    public static final String VERIFY_KEY = "beiji";

    public static final class SharedPreferencesConstant {
        public static final String SHARED_USER_ID = "prf_user_id";
        public static final String SHARED_USER_NICKNAME = "prf_user_nickname";
        public static final String SHARED_USER_PHONE = "prf_user_phone";
        public static final String SHARED_USER_SESSION = "prf_user_session";
        public static final String SHARED_USER_AVATAR = "prf_user_avatar";
        public static final String SHARED_USER_GENDER = "prf_user_gender";
        public static final String SHARED_USER_LABLE = "prf_user_label";
        public static final String SHARED_USER_ADDRESS = "prf_user_address";
        public static final String SHARED_USER_PROFILE = "prf_user_profile";
        public static final String SHARED_USER_LAST_LOGIN_TIME = "prf_user_last_login_time";
        public static final String SHARED_USER_SESSION_EXPIRE_TIME = "prf_user_session_expire_time";
        public static final String SHARED_USER_IS_ENABLED = "prf_user_is_enabled";
        public static final String SHARED_USER_CREATE_TIME = "prf_user_create_time";
    }

    public static final class IntentActionConstant{
        public static final String ACTION_LOGIN_TIMEOUT = "action_login_timeout";

    }

    public static final class IntentExtraConstant {
        public static final String EXTRA_USER_ENTITY = "extra_user_entity";
        public static final String EXTRA_DEVICE_ENTITY = "extra_device_entity";
    }

    public static final class FileConstant {
        private static final String ROOT = SDCardUtil.getSDCardPath();
        public static final String BEIJI_APP_BASE_PATH = ROOT + File.separator + "beiji";

        //DIR
        public static final String DIR_APK_UPGRADE = BEIJI_APP_BASE_PATH + File.separator + "upgrade"; //软件升级缓存
        public static final String DIR_APK_LOG = BEIJI_APP_BASE_PATH + File.separator + "log"; //全局日志
        public final static String DIR_APK_TEMP = BEIJI_APP_BASE_PATH + File.separator + "temp";//缓存文件
        public static final String DIR_APK_CACHE = BEIJI_APP_BASE_PATH + File.separator + "cache";
        public static final String DIR_APK_URL_CACHE = DIR_APK_CACHE + File.separator + "url";

        public static final String DIR_APK_IMG = BEIJI_APP_BASE_PATH + File.separator + "image"; //全局图片

        //FILE
        public static final String FILE_CRASH_LOG = DIR_APK_LOG + File.separator + "beiji.log";

        public static final String FILE_APK_IMG_CACHE = DIR_APK_TEMP + File.separator + "temp_img.cache";
        public static final String FILE_APK_IMG_LOAD_CACHE = DIR_APK_TEMP + File.separator + "temp_img_load.cache";
        public static final String FILE_APK_IMG_CAPTURE_CACHE = DIR_APK_TEMP + File.separator + "temp_img_capture.cache";
        public static final String FILE_APK_DOWNLOAD_PATH = BEIJI_APP_BASE_PATH + File.separator + "apk" + File.separator + "youdao";

        public static final String TEMP_FILE_EXT_NAME = ".tmp";

        public static final String CACHE_FILE_EXT_NAME = ".jpg";

        public static final String FILE_TEMP_IMG = "temp_img";

        public final static String SYSTEM_CAMERA_PATH = ROOT + File.separator + "DCIM" + File.separator + "Camera";

        public static String mkdirs(String path) {
            try {
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdirs();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return path;
        }

    }
}
