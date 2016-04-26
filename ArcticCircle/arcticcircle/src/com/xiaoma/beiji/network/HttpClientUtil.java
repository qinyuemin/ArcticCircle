package com.xiaoma.beiji.network;

import com.alibaba.fastjson.JSONObject;
import com.xiaoma.beiji.BuildConfig;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.entity.*;
import com.xiaoma.beiji.util.MD5Util;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.makeapp.javase.util.DateUtil;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by gang.shi on 2014/12/19.
 */
public class HttpClientUtil {
    public static final String TAG = HttpClientUtil.class.getSimpleName();

    public static final boolean isDebug;

    public static final String HTTP_REQUEST_GET = "get";
    public static final String HTTP_REQUEST_POST = "post";
    public static final String HTTP_REQUEST_PUT = "put";
    public static final String HTTP_REQUEST_DELETE = "delete";

    private static final String HTTP_URl = UrlConstants.BASE_URL;

    private static final String APP_KEY = "6c5ea2ca96c165e86cd11f83875f0135";
    private static final String APP_SESSION = "f81eb851a320a29b7f792d99f85f4abe";
    private static final String APP_SECRET = "6deef98aae576547a5930876affe0056";

    private final static int timeOut = 15 * 1000;

    private static AsyncHttpClient client = new AsyncHttpClient();

    static {
        client.setTimeout(timeOut);
//        client.addHeader("Content-Type", "application/json");

        isDebug = BuildConfig.DEBUG;
    }

    public static void logger(String logger){
        if (isDebug) {
            System.out.println(logger);
        }
    }

    // 默认 post 方式 请求
    private static <T> void executeAction(String method, Class<T> cls, Map<String, Object> params, AbsHttpResultHandler<T> handler) {
        try {
            RequestParams requestParams = new RequestParams();

            JSONObject statsJson = new JSONObject();
            statsJson.put("app_version", Global.getAppVersion());
            statsJson.put("app_udid", Global.getPhoneUniqueId());
            params.put("app_stats", statsJson);
            String paramJson = new JSONObject(params).toJSONString();


            //1.构建参数  2.参数按照顺序添加
            Map<String, Object> paramsMap = new LinkedHashMap<>();
            paramsMap.put("app_key", APP_KEY);
            paramsMap.put("app_session", APP_SESSION);
            paramsMap.put("charset", "UTF-8");
            paramsMap.put("format", "json");
            paramsMap.put("method", method);
            paramsMap.put("params", paramJson);
            paramsMap.put("sign_method", "md5");
            paramsMap.put("timestamp", DateUtil.getStringDate());
            paramsMap.put("version", "2015101501");
            paramsMap.put("sign", getParamsSign(paramsMap));

            String requestJson = new JSONObject(paramsMap).toJSONString();
            requestParams.put("json", requestJson);

            requestParams.setContentEncoding("UTF-8");
            String tag = getHttpRequestTag();
            logger(TAG + " " + tag + " url:" + HTTP_URl);
            logger(TAG + " " + tag + " requestParams:" + requestParams.toString());

            HttpEntity httpEntity = new StringEntity(requestJson, "UTF-8");

            client.post(null, HTTP_URl, httpEntity, "application/json", new HttpResultConverter<T>(cls, handler, tag));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static <T> void executeAction(String method, Class<T> cls, JSONObject params, AbsHttpResultHandler<T> handler) {
        try {
            RequestParams requestParams = new RequestParams();

            JSONObject statsJson = new JSONObject();
            statsJson.put("app_version", Global.getAppVersion());
            statsJson.put("app_udid", Global.getPhoneUniqueId());
            params.put("app_stats", statsJson);

            //1.构建参数  2.参数按照顺序添加
            Map<String, Object> paramsMap = new LinkedHashMap<>();
            paramsMap.put("app_key", APP_KEY);
            paramsMap.put("app_session", APP_SESSION);
            paramsMap.put("charset", "UTF-8");
            paramsMap.put("format", "json");
            paramsMap.put("method", method);
            paramsMap.put("params", params.toJSONString());
            paramsMap.put("sign_method", "md5");
            paramsMap.put("timestamp", DateUtil.getStringDate());
            paramsMap.put("version", "2015101501");
            paramsMap.put("sign", getParamsSign(paramsMap));

            String requestJson = new JSONObject(paramsMap).toJSONString();
            requestParams.put("json", requestJson);

            requestParams.setContentEncoding("UTF-8");
            String tag = getHttpRequestTag();
            logger(TAG + " " + tag + " url:" + HTTP_URl);
            logger(TAG + " " + tag + " requestParams:" + requestParams.toString());

            HttpEntity httpEntity = new StringEntity(requestJson, "UTF-8");

            client.post(null, HTTP_URl, httpEntity, "application/json", new HttpResultConverter<T>(cls, handler, tag));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // post 表单方式
    private static <T> void executeActionForm(String method, Class<T> cls, RequestParams requestParams, AbsHttpResultHandler<T> handler) {
        try {

            requestParams.setContentEncoding("UTF-8");
            String tag = getHttpRequestTag();
            logger(TAG + " " + tag + " url:" + HTTP_URl);
            logger(TAG + " " + tag + " requestParams:" + requestParams.toString());

            client.post(HTTP_URl + method, requestParams, new HttpResultConverter<T>(cls, handler, tag));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getHttpRequestTag() {
        return DateUtil.getStringDate("sss") + " " + new Random().nextInt(10);
    }

    private static String getParamsSign(Map<String, Object> paramsMap) {
        // 3.拼接字符串
        StringBuilder builder = new StringBuilder();
        for (String key : paramsMap.keySet()) {
            builder.append(key).append(paramsMap.get(key));
        }
        //4.首尾加上密钥 secret
        builder.insert(0, APP_SECRET);
        builder.append(APP_SECRET);
        //5.md5两次 得到 sign
        String str = builder.toString();
        String md5 = null;
        try {
            md5 = MD5Util.getMD5(MD5Util.getMD5(str));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }

    public static class User {
        /**
         * 获取用户信息
         * <p/>
         * user_id                         # 用户ID
         */
        public static void userGetInfo(String userId, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("user_id", userId);
            executeAction(UrlConstants.USER_GET_INFO, null, params, handler);
        }

        /**
         * 用户注册
         * <p/>
         * user_phone       # 用户手机号
         * login_type       # 登陆类型        1 手机验证码登陆 2 密码登陆
         * user_pwd                        # 登陆密码
         * captcha                         # 验证码
         * sms_key                         # 短信Key
         */
        public static void userLogin(int loginType, String userPhone, String userPwd, String captcha, String smsKey, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("user_phone", userPhone);
            params.put("login_type", loginType);
            params.put("user_pwd", userPwd);
            params.put("captcha", captcha);
            params.put("sms_key", smsKey);
            executeAction(UrlConstants.USER_LOGIN, UserInfoEntity.class, params, handler);
        }

        public static void userRegister(String userPhone, int gender, String nickname, String pwd, String captcha, String smsKey, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("user_phone", userPhone);
            params.put("gender", gender);
            params.put("nick_name", nickname);
            params.put("user_pwd", pwd);
            params.put("captcha", captcha);
            params.put("sms_key", smsKey);
            executeAction(UrlConstants.USER_REGISTER, null, params, handler);
        }

        /**
         * 发送登陆验证码
         *
         * @param userPhone phone_num                       # 用户手机号
         * @param handler
         */
        public static void userSendLoginCode(String userPhone, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("phone_num", userPhone);
            executeAction(UrlConstants.USER_SEND_LOGIN_CODE, JSONObject.class, params, handler);
        }

        /**
         * 设置用户信息(昵称/性别)
         * <p/>
         * user_id                         # 用户id
         * nickname                        # 用户昵称
         * gender                          # 用户性别
         */
        public static void userEdit(String nickname, int gender, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("nickname", nickname);
            params.put("gender", gender);
            executeAction(UrlConstants.USER_EDIT, JSONObject.class, params, handler);
        }

        /**
         * 修改密码
         * user_id                         # 用户id
         * user_pwd                        # 登陆密码
         * captcha                         # 验证码
         * sms_key                         # 短信Key
         */
        public static void userChangePwd(String userPwd, String captcha, String smsKey, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("user_pwd", userPwd);
            params.put("captcha", captcha);
            params.put("sms_key", smsKey);
            executeAction(UrlConstants.USER_CHANGE_PWD, JSONObject.class, params, handler);
        }

        /**
         * 上传头像
         *
         * @param imgPath
         * @param handler
         */
        public static void userAvatar(String imgPath, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("image", imgPath);
            executeAction(UrlConstants.USER_AVATAR, null, params, handler);
        }

        /**
         * 发送修改密码验证码
         *
         * @param handler
         */
        public static void userSendChangePassCode(AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("phone_num", Global.getUserPhone());
            executeAction(UrlConstants.USER_SEND_CHANGE_PASS_CODE, null, params, handler);
        }

        /**
         * dynamic_depth                   # 查看几度好友的动态
         * help_depth                      # 查看几度好友的求助
         * chat_depth                      # 几度好友可以私聊
         */
        public static void userGetReadDepth(AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            executeAction(UrlConstants.USER_GET_READ_DEPTH, DepthEntity.class, params, handler);
        }

        /**
         * dynamic_depth                   # 查看几度好友的动态
         * help_depth                      # 查看几度好友的求助
         * chat_depth                      # 几度好友可以私聊
         */
        public static void userEditReadDepth(int dynamicDepth, int helpDepth, int chatDepth, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("dynamic_depth", dynamicDepth);
            params.put("help_depth", helpDepth);
            params.put("chat_depth", chatDepth);
            executeAction(UrlConstants.USER_EDIT_READ_DEPTH, null, params, handler);
        }

        public static void userGetFriendInfo(String friendId, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("friend_user_id", friendId);
            executeAction(UrlConstants.USER_GET_FRIEND_INFO, FriendEntity.class, params, handler);
        }

        public static void userGetDynamicComments(String bottomCommentId, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("bottom_comment_id", bottomCommentId);
            executeAction(UrlConstants.USER_GET_DYNAMIC_COMMENTS, JSONObject.class, params, handler);
        }

        public static void userGetShopComments(String bottomCommentId, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("bottom_comment_id", bottomCommentId);
            executeAction(UrlConstants.USER_GET_SHOP_COMMENTS, JSONObject.class, params, handler);
        }
    }

    public static class App {
        /**
         * 接口应用添加
         * app_key                         # 应用App_key
         * app_name                        # 接口应用名称
         * app_secret                      # 应用密码
         * app_session                     # app_session
         * app_type                        # 接口应用类型  10 APP应用 20 网站
         * is_internal                     # 是否为内部应用 1 是 2 否
         * is_enabled                      # 是否可用 1是 2 否
         * session_expire_time             # session 有效期
         */
        public static void appAdd(String appKey,
                                  String appName,
                                  String appSecret,
                                  String appSession,
                                  String appType,
                                  String appInternal,
                                  String appEnabled,
                                  String appSessionExpireTime,
                                  AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("app_key", appKey);
            params.put("app_name", appName);
            params.put("app_secret", appSecret);
            params.put("app_session", appSession);
            params.put("app_type", appType);
            params.put("is_internal", appInternal);
            params.put("is_enabled", appEnabled);
            params.put("session_expire_time", appSessionExpireTime);
            executeAction(UrlConstants.APP_ADD, null, params, handler);
        }

        /**
         * 接口应用信息更新
         * app_key                         # 应用App_key
         * app_name                        # 接口应用名称
         * app_secret                      # 应用密码
         * app_session                     # app_session
         * app_type                        # 接口应用类型  10 APP应用 20 网站
         * is_internal                     # 是否为内部应用 1 是 2 否
         * is_enabled                      # 是否可用 1是 2 否
         * session_expire_time             # session 有效期
         */
        public static void appUpdate(String appKey,
                                     String appName,
                                     String appSecret,
                                     String appSession,
                                     String appType,
                                     String appInternal,
                                     String appEnabled,
                                     String appSessionExpireTime,
                                     AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("app_key", appKey);
            params.put("app_name", appName);
            params.put("app_secret", appSecret);
            params.put("app_session", appSession);
            params.put("app_type", appType);
            params.put("is_internal", appInternal);
            params.put("is_enabled", appEnabled);
            params.put("session_expire_time", appSessionExpireTime);
            executeAction(UrlConstants.APP_UPDATE, null, params, handler);
        }

        /**
         * 获取接口应用信息
         * app_key                         # 应用App_key
         */
        public static void appGet(String appKey, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("app_key", appKey);
            executeAction(UrlConstants.APP_GET, null, params, handler);
        }

        /**
         * app_key                         # 应用App_key
         * app_name                        # 接口应用名称
         * app_type                        # 接口应用类型  10 APP应用 20 网站
         * is_internal                     # 是否为内部应用 1 是 2 否
         * is_enabled                      # 是否可用 1是 2 否
         * is_pages                        # 是否分页
         * page_size                       # 每页显示多少条
         * page_no                         # 第几页
         */
        public static void appGetList() {

        }
    }

    public static class Dynamic {
        /**
         * 获得动态信息(单条)
         * user_id                         # 用户ID
         * release_id                      # 动态发布ID
         */
        public static void dynamicDetail(String dynamicId, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("release_id", dynamicId);
            executeAction(UrlConstants.DYNAMIC_DETAIL, FriendDynamicEntity.class, params, handler);
        }

        /**
         * 获取自己动态列表/获得指定好友动态 (Dynamic动态 , Help求助)
         * user_id                         # 用户ID
         * release_user_id                 # 发布者ID
         * dynamic_type                    # 动态类型     1 用户动态 2 用户求助  3 用户推荐店铺 4 店铺推广 5 朋友加入
         * newest_key_id                   # 最新一条动态的数组键值
         * last_key_id                     # 最后一条动态的数组键值
         */
        public static void dynamicGetList(String releaseUserId, int type, String newestKeyId, String lastKeyId, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("release_user_id", releaseUserId);
            if (type != -1) {
                params.put("dynamic_type", type);
            }
            params.put("newest_key_id", newestKeyId);
            params.put("last_key_id", lastKeyId);
            executeAction(UrlConstants.DYNAMIC_GET_LIST, FriendDynamicEntity.class, params, handler);
        }

        /**
         * 动态点赞
         * user_id                         # 用户ID
         * release_id                      # 动态发布ID
         * praise_type                     # 点赞类型 1 点赞 2 取消点赞
         */
        public static void dynamicDoPraise(String releaseId, int type, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("release_id", releaseId);
            params.put("praise_type", type);
            executeAction(UrlConstants.DYNAMIC_DO_PRAISE, JSONObject.class, params, handler);
        }

        /**
         * user_id                         # 用户ID
         * dynamic_id                      # 动态ID
         * description                     # 分享描述
         */
        public static void dynamicDoShare(String dynamicId, String description, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("dynamic_id", dynamicId);
            params.put("description", description);
            executeAction(UrlConstants.DYNAMIC_DO_SHARE, JSONObject.class, params, handler);
        }

        /**
         * 动态收藏
         * user_id                         # 用户ID
         * release_id                      # 动态发布ID
         * favorite_type                   # 收藏类型     1 收藏 2 取消收藏
         */
        public static void dynamicDoFavorite(String releaseId, int type, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("release_id", releaseId);
            params.put("favorite_type", type);
            executeAction(UrlConstants.DYNAMIC_DO_FAVORITE, Boolean.class, params, handler);
        }

        /**
         * 收藏列表
         * do_type                         # 店铺收藏/动态收藏    1 分享动态收藏 2 店铺收藏
         */
        public static void dynamicFavoriteList(int type, String top, String bottom, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("do_type", type);
            params.put("top_favorite_id", top);
            params.put("bottom_favorite_id", bottom);
            executeAction(UrlConstants.DYNAMIC_FAVORITE_LIST, JSONObject.class, params, handler);
        }

        /**
         * 动态评论
         * user_id                         # 用户ID
         * dynamic_id                      # 动态发布ID
         * to_user_id                      # 对谁评论
         * comment_content                 # 评论内容
         */
        public static void dynamicDoComment(String releaseId, String toUserId, String content, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("release_id", releaseId);
            params.put("to_user_id", toUserId);
            params.put("comment_content", content);
            executeAction(UrlConstants.DYNAMIC_DO_COMMENT, null, params, handler);
        }

        /**
         * 发布动态
         * user_id                         # 用户ID
         * dynamic_content                 # 动态内容
         * pic_list                        # 图片列表（图片数组，图片路径）
         * {
         * "user_id":"1",
         * "user_session":"f095730cd3c2968d5b8694a75b0267c3",
         * "dynamic_content":"必须要添加胜多负少",
         * "pic_list":[
         * "uploads/temp/a03fcacb32f6e11dc18fd22eff0ab8db.jpg",
         * "uploads/temp/a03fcacb32f6e11dc18fd22eff0ab8db.jpg",
         * "uploads/temp/a03fcacb32f6e11dc18fd22eff0ab8db.jpg"
         * ]
         * }
         */
        public static void dynamicReleaseDynamic(int releaseType, String dynamicContent, List<String> picList, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("area", "");// 放入地址
            params.put("dynamic_content", dynamicContent);
            params.put("pic_list", picList);//todo 须检查特殊处理
            // 讲发布合并，除接口地址不同，其他内容都想吐
            executeAction(releaseType == 1 ? UrlConstants.DYNAMIC_RELEASE_DYNAMIC : UrlConstants.DYNAMIC_RELEASE_SEEK_HELP, null, params, handler);
        }

        /**
         * 发布评论 第二版
         * @param dynamicContent            # 动态内容
         * @param shopId                    # 店铺ID
         * @param shopName                  # 店铺名称
         * @param area                       # 地点
         * @param price                      # 消费金额
         * @param description               # 转发描述
         * @param picList                   # 图片列表（图片数组，图片路径）
         * @param handler                   # 回调处理
         */
        public static void dynamicReleaseDynamicV2(String dynamicContent, String shopId, String shopName, String area,
                                                   String price, String description,List<String> picList, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("area", area);
            params.put("content", dynamicContent);
            params.put("associated_shop_name", shopName);
            params.put("associated_shop_id", shopId);
            params.put("description", description);
            params.put("associated_price", price);
            params.put("pic_list", picList);
            params.put("dynamic_type", 1);
            executeAction(UrlConstants.DYNAMIC_RELEASE_DYNAMICV2, null, params, handler);
        }

        /**
         * 发布求助
         */
        public static void dynamicReleaseSeekHelp(String dynamicContent, List<String> picList, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("dynamic_type", 2);
            params.put("area", "");// 放入地址
            params.put("content", dynamicContent);
            params.put("pic_list", picList);
            executeAction(UrlConstants.DYNAMIC_RELEASE_SEEK_HELPV2, null, params, handler);
        }

        /**
         * 发布长文
         */
        public static void dynamicReleasLongText(String title,String content, List<String> picList, AbsHttpResultHandler handler) {
//            Map<String, Object> params = new HashMap<>();
//            params.put("user_id", Global.getUserId());
//            params.put("user_session", Global.getUserSession());
//            params.put("dynamic_type", 2);
//            params.put("area", "");// 放入地址
//            params.put("content", dynamicContent);
//            params.put("pic_list", picList);//todo 须检查特殊处理
//            executeAction(UrlConstants.DYNAMIC_RELEASE_SEEK_HELPV2, null, params, handler);
        }
    }

    public static class Shop {
        /**
         * 获取店铺相关信息
         */
        public static void shopGet(String shopId, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("shop_id", shopId);
            executeAction(UrlConstants.SHOP_GET, null, params, handler);
        }

        /**
         * 获取店铺详细信息
         *
         * @param shopId
         * @param handler
         */
        public static void shopGetInfo(String shopId, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("shop_id", shopId);
            executeAction(UrlConstants.SHOP_GET_INFO, ShopEntity.class, params, handler);
        }

        /**
         * 店铺点赞
         *
         * @param shopId
         * @param handler
         */
        public static void shopDoPraise(String shopId, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("shop_id", shopId);
            executeAction(UrlConstants.SHOP_DO_PRAISE, null, params, handler);
        }

        /**
         * 店铺评论
         * user_id                         # 用户ID
         * shop_id                         # 店铺ID
         * favorite_type                   # 收藏类型 1 收藏 2取消收藏
         */
        public static void shopDoFavorite(String shopId, String favoriteType, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", "");
            params.put("user_session", "");
            params.put("shop_id", shopId);
            params.put("favorite_type", favoriteType);
            executeAction(UrlConstants.SHOP_DO_FAVORITE, null, params, handler);
        }

        /**
         * 店铺评论
         * user_id                         # 用户ID
         * shop_id                         # 店铺ID
         * to_user_id                      # 对谁评论
         * comment_content                 # 评论内容
         */
        public static void shopDoComment(String shopId, String toUserId, String content, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("shop_id", shopId);
            params.put("to_user_id", toUserId);
            params.put("comment_content", content);
            executeAction(UrlConstants.SHOP_DO_COMMENT, null, params, handler);
        }

        public static void shopGetGoodsList(String shopId, String topGoodsId, String bottomGoodsId, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("shop_id", shopId);
            params.put("top_goods_id", topGoodsId);
            params.put("bottom_goods_id", bottomGoodsId);
            executeAction(UrlConstants.SHOP_GOODS_LIST, GoodsEntity.class, params, handler);
        }

        public static void shopGetGoodsDetail(String goodsId, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("goods_id", goodsId);
            executeAction(UrlConstants.SHOP_GOODS_DETAIL, GoodsEntity.class, params, handler);
        }

        public static void shopGetCommentList(String shopId, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("shop_id", shopId);
            executeAction(UrlConstants.SHOP_GET_COMMENT_LIST, ShopEntity.class, params, handler);
        }

        public static void shopReleaseList(String shopId, String topGoodsId, String bottomGoodsId, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("shop_id", shopId);
            params.put("top_goods_id", topGoodsId);
            params.put("bottom_goods_id", bottomGoodsId);
            executeAction(UrlConstants.SHOP_RELEASE_LIST, ShopEntity.class, params, handler);
        }

        public static void shopReleaseDetail(String shopId, String releaseId, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("shop_id", shopId);
            params.put("release_id", releaseId);
            executeAction(UrlConstants.SHOP_RELEASE_DETAIL, ShopDynamicDetailEntity.class, params, handler);
        }

        public static void shopPriseList(String releaseUserId, String bottomPriseId, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("release_user_id", releaseUserId);
            params.put("bottom_prise_id", bottomPriseId);
            executeAction(UrlConstants.SHOP_PRAISE_LIST, JSONObject.class, params, handler);
        }
    }

    public static class Chatting {
        /**
         * 获取 xmpp 聊天 账号
         */
        public static void XmppGetUser(AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            executeAction(UrlConstants.XMPP_GET_USER, XmppUserEntity.class, params, handler);
        }
    }

    public static class Friend {
        public static void friendGet(String friendId, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("friend_user_id", friendId);
            executeAction(UrlConstants.FRIEND_GET, FriendEntity.class, params, handler);
        }

        public static void friendGetList(AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            executeAction(UrlConstants.FRIEND_GET_LIST, FriendEntity.class, params, handler);
        }

        public static void friendGetFiliation(AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            executeAction(UrlConstants.FRIEND_GET_FILIATION, FriendEntity.class, params, handler);
        }

        /**
         * {
         "user_id": "1",
         "user_session": "f095730cd3c2968d5b8694a75b0267c3",
         "friend_user_id": "2",
         "is_look_me": true,
         "is_friend_not_look_her": true
         }
         */
        public static void friendSetFiliation(String friendUserId,boolean b,int type,AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("friend_user_id", friendUserId);
            if(type == 1){
                params.put("is_look_me", b);
            }else{
                params.put("is_friend_not_look_her", b);
            }
            executeAction(UrlConstants.FRIEND_SET_FILIATION, JSONObject.class, params, handler);
        }
        // 共同好友
        public static void friendGetCommonFriends(String friendUserId,AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("friend_user_id", friendUserId);
            executeAction(UrlConstants.FRIEND_GET_COMMON_FRIENDS, FriendEntity.class, params, handler);
        }

        // 引荐 审核  status 1 同意 0 不同意
        public static void friendRecommendSubmit(int fromUserId,int toUserId,int status,String content,AbsHttpResultHandler handler){
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("from_user_id", fromUserId);
            params.put("to_user_id", toUserId);
            params.put("status", status);
            params.put("content", content);
            executeAction(UrlConstants.FRIEND_RECOMMEND_SUBMIT, FriendEntity.class, params, handler);
        }

        public static void friendRecommend(int friendUserId,int recommendUserId,String recommendContent,AbsHttpResultHandler handler){
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("friend_user_id", friendUserId);
            params.put("recommend_user_id", recommendUserId);
            params.put("recommend_content", recommendContent);
            executeAction(UrlConstants.FRIEND_RECOMMEND, FriendEntity.class, params, handler);
        }

        public static void friendUpdateBlacklist(int friendUserId,boolean b,AbsHttpResultHandler handler){
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("friend_user_id", friendUserId);
            String url = "";
            if(b){
                url = UrlConstants.FRIEND_SET_BACKLIST;
            }else{
                url = UrlConstants.FRIEND_DEL_BLACKLIST;
            }
            executeAction(url, FriendEntity.class, params, handler);
        }
    }

    public static class Message{
        /**
         * 请求 聊天
         * @param friendId
         * @param handler
         */
        public static void messageRequest(String friendId, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("friend_user_id", friendId);
            executeAction(UrlConstants.MESSAGE_REQUEST, ChatRequestEntity.class, params, handler);
        }

        // 发送聊天消息
        public static void messageSend(int friendId,String message, AbsHttpResultHandler handler) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Global.getUserId());
            params.put("user_session", Global.getUserSession());
            params.put("friend_user_id", friendId);
            params.put("message", message);
            executeAction(UrlConstants.MESSAGE_SEND, JSONObject.class, params, handler);
        }
    }

    /**
     * 上传图片
     * user_id, user_session, userfile[文件上传name]
     *
     * @param imgPath
     * @param handler
     */
    public static void imageUpload(String imgPath, AbsHttpResultHandler handler) {
        RequestParams params = new RequestParams();
        params.put("user_id", Global.getUserId());
        params.put("user_session", Global.getUserSession());
        try {
            params.put("userfile", new File(imgPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        executeActionForm(UrlConstants.IMAGES_UPLOAD, FileUploadResultEntity.class, params, handler);
    }

    public static void searchGetList(String searchContent, AbsHttpResultHandler handler) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", Global.getUserId());
        params.put("user_session", Global.getUserSession());
        params.put("search_text", searchContent);
        executeAction(UrlConstants.SEARCH_GET_LIST, SearchEntity.class, params, handler);
    }

    public static void vCardChange(List<ContactEntity> entities,AbsHttpResultHandler handler){
        JSONObject params = new JSONObject();
        params.put("user_id", Global.getUserId());
        params.put("user_session", Global.getUserSession());
        params.put("list", entities);
        executeAction(UrlConstants.V_CARD_CHANGE, SearchEntity.class, params, handler);
    }
}

