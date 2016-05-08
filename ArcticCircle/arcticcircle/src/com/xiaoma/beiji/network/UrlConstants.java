/**
 * 项目名：
 * 包名：
 * 文件名：UrlConstants.java
 * 版本信息：1.0.0
 * 创建日期：2014年12月23日-上午11:30:20
 * 创建人：DingWentao
 *
 */
package com.xiaoma.beiji.network;


import com.common.android.lib.common.LibConstant;

/**
 *
 * 类名称：UrlConstants
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年05月10 15:24
 * 修改备注：
 *
 * @version 1.0.0
 */
public class UrlConstants {
    public static final String BASE_URL;//用static块来初始化
    public static final String XMPP_SERVER; // xmp 聊天服务器地址
    public static final int XMPP_PORT;// xmp 聊天服务器端口

    public static final String SHARE_URL;

    static {
        if (LibConstant.BASE_URL_TYPE == LibConstant.UrlType.RELEASE) {
            //正式环境地址
            BASE_URL = "http://bjqapi.xiaomakeji.com.cn/rest";
            XMPP_SERVER = "121.41.33.187";
            XMPP_PORT = 5222;
            SHARE_URL = "http://bjq2shop.xiaomakeji.com.cn/download";
        } else {
            //测试环境地址
            BASE_URL = "http://testbjq2api.xiaomakeji.com.cn/rest";
            XMPP_SERVER = "121.40.189.143";
            XMPP_PORT = 5222;
            SHARE_URL = "http://testbjqshop.xiaomakeji.com.cn/download";
        }
    }


    // user
    public static final String USER_REGISTER = "User.register";// 注册
    public static final String USER_LOGIN = "User.login";// 注册
    public static final String USER_GET_INFO = "User.get";// 获取用户信息
    public static final String USER_SEND_LOGIN_CODE = "User.send_login_code";// 发送登陆验证码
    public static final String USER_EDIT = "User.edit";// 设置用户信息(昵称/性别)
    public static final String USER_CHANGE_PWD = "User.change_password";// 修改密码
    public static final String USER_AVATAR = "User.avatar";// 上传头像
    public static final String USER_SEND_CHANGE_PASS_CODE = "User.send_change_pass_code";
    public static final String USER_GET_READ_DEPTH = "User.get_read_depth"; //
    public static final String USER_EDIT_READ_DEPTH = "User.edit_read_depth"; //
    public static final String USER_GET_FRIEND_INFO = "User.get_friend_info";
    public static final String USER_GET_DYNAMIC_COMMENTS = "User.get_dynamic_comments";
    public static final String USER_GET_SHOP_COMMENTS = "User.get_shop_comments";
    public static final String USER_HOME_DYNAMIC = "User.home_dynamic";
    public static final String USER_HOME_FAVORITE = "User.home_dynamic";
    public static final String USER_FRIEND_HOME_DYNAMIC = "User.friend_home_dynamic";
    public static final String USER_FRIEND_HOME_FAVORITE = "User.friend_home_favorite";

    // app
    public static final String APP_ADD = "App.add";// 接口应用添加
    public static final String APP_UPDATE = "App.update";// 接口应用信息更新
    public static final String APP_GET = "App.get";
    public static final String APP_GET_LIST = "App.get_list";

    //Dynamic
    public static final String DYNAMIC_DETAIL = "Dynamic.detail";
    public static final String DYNAMIC_GET_LIST = "Dynamic.get_list";//动态列表、更新、加载更多
    public static final String DYNAMIC_DO_PRAISE = "Dynamic.do_praise";
    public static final String DYNAMIC_DO_SHARE = "Dynamic.do_share";
    public static final String DYNAMIC_DO_FAVORITE = "Dynamic.do_favorite";
    public static final String DYNAMIC_FAVORITE_LIST = "Dynamic.favorite_list";
    public static final String DYNAMIC_DO_COMMENT = "Dynamic.do_comment";
    public static final String DYNAMIC_RELEASE_DYNAMIC = "Dynamic.release_dynamic";
    public static final String DYNAMIC_RELEASE_SEEK_HELP = "Dynamic.release_seek_help";
    public static final String DYNAMIC_RELEASE_SEEK_HELPV2 = "Dynamic.release_ask";
    public static final String DYNAMIC_RELEASE_DYNAMICV2 = "Dynamic.release_moment";
    public static final String DYNAMIC_RELEASE_DYNAMIC_LONG_TEXTV2 = "Dynamic.release_long_text";

    // Shop
    public static final String SHOP_GET = "Shop.get";
    public static final String SHOP_GET_INFO = "Shop.get_info";
    public static final String SHOP_DO_PRAISE = "Shop.do_praise";
    public static final String SHOP_DO_FAVORITE = "Shop.do_favorite";
    public static final String SHOP_DO_COMMENT = "Shop.do_comment";
    public static final String SHOP_GET_COMMENT_LIST = "Shop.get_comment_list";
    public static final String SHOP_RELEASE_LIST = "Shop.release_list";
    public static final String SHOP_PRAISE_LIST = "Shop.praise_list";
    public static final String SHOP_RELEASE_DETAIL = "Shop.release_detail";
    public static final String SHOP_GOODS_LIST = "Shop.get_goods_list";
    public static final String SHOP_GOODS_DETAIL = "Shop.get_goods_detail";
    public static final String SHOP_DYNAMIC = "Shop.dynamic";// 店铺动态
    public static final String SHOP_ADD = "Shop.add";//
    public static final String SHOP_GOODS_ADD = "Shop.goods_add";//
    public static final String SHOP_GOODS_EDIT = "Shop.goods_edit";//

    // VCARD
    public static final String VCARD_CHANGE = "Vcard.change";// 通讯录更新

    public static final String XMPP_GET_USER = "Xmpp.get_user";// 通讯录更新

    // Message
    public static final String MESSAGE_REQUEST = "Message.request";
    public static final String MESSAGE_SEND = "Message.send";
    public static final String MESSAGE_CONFIRM = "Message.confirm";

    // Friend
    public static final String FRIEND_GET = "Friend.get";
    public static final String FRIEND_GET_LIST = "Friend.get_list";
    public static final String FRIEND_RECOMMEND = "Friend.recommend";
    public static final String FRIEND_RECOMMEND_SUBMIT = "Friend.recommend_submit";
    public static final String FRIEND_GET_COMMON_FRIENDS = "Friend.get_common_friends";
    public static final String FRIEND_SET_BACKLIST = "Friend.set_backlist";
    public static final String FRIEND_GET_FILIATION = "Friend.get_filiation";
    public static final String FRIEND_SET_FILIATION = "Friend.set_filiation";
    public static final String FRIEND_SET_BLACKLIST = "Friend.set_backlist";
    public static final String FRIEND_DEL_BLACKLIST = "Friend.del_backlist";
    public static final String FRIEND_DO_ATTENTION = "Friend.do_attention";
    public static final String FRIEND_REMOVE_ATTENTION = "Friend.remove_attention";
    public static final String FRIEND_SET_PRIVACY = "Friend.set_privacy";

    public static final String IMAGES_UPLOAD = "/upload_image";

    public static final String SEARCH_GET_LIST = "Search.get_list";// 搜索
    public static final String SEARCH_DYNAMIC = "Search.dynamic";// 搜索动态
    public static final String SEARCH_USER = "Search.user";// 搜索用户

    public static final String V_CARD_CHANGE = "Vcard.change"; // 更新联系人
}
