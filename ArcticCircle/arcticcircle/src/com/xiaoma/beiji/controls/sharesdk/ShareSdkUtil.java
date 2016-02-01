/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.util
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.controls.sharesdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import com.makeapp.android.util.ToastUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.controls.view.zxing.decoding.Intents;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.network.UrlConstants;

/**
 * 类名称： ShareSdkUtil
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月21 15:42
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ShareSdkUtil {
    public static void showShare(final Context context,final String dynamicId) {
        ShareSDK.initSDK(context);

        final String shareTxt = "我是来自北极圈的，来看看吧";

        final OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(context.getString(R.string.app_name));
        // text是分享文本，所有平台都需要这个字段
        oks.setText(shareTxt);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(UrlConstants.SHARE_URL);


        // 构造一个图标
        Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_logo_share);
        // 定义图标的标签
        String label = context.getResources().getString(R.string.app_name);
        // 图标点击后会通过Toast提示消息
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                HttpClientUtil.Dynamic.dynamicDoShare(dynamicId, shareTxt, new AbsHttpResultHandler() {
                    @Override
                    public void onSuccess(int resultCode, String desc, Object data) {
                        ToastUtil.show(context, "成功分享到北极圈");
                    }

                    @Override
                    public void onFailure(int resultCode, String desc) {
                        ToastUtil.show(context, "分享失败:"+desc);
                    }
                });
            }
        };
        oks.setCustomerLogo(logo, logo, label, listener);

        // 启动分享GUI
        oks.show(context);
    }
}
