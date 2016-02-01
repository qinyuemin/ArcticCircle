/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.manager.chatting
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.manager.chatting;

import android.app.Activity;
import com.alibaba.fastjson.JSONObject;
import com.xiaoma.beiji.entity.ChatRequestEntity;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.IntentUtil;
import com.xiaoma.beiji.util.ToastUtil;

/**
 *
 * 类名称： ChattingUtil
 * 类描述： 
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月18 23:26
 * 修改备注：
 * @version 1.0.0
 *
 */
public class ChattingUtil {
    public static void goChatting(final Activity activity, final UserInfoEntity entity){
        HttpClientUtil.Message.messageRequest(String.valueOf(entity.getUserId()), new AbsHttpResultHandler<ChatRequestEntity>() {
            @Override
            public void onSuccess(int resultCode, String desc, ChatRequestEntity data) {
                try {
                    if (data != null) {
                        /**       # 1 等待引荐,
                        *       # 2 拒绝引荐 ,
                        *       # 5,2度好友需要引荐(需要发起引荐请求)
                        *       # 6 黑名单不能聊天
                        *       # 7他设置权限(他看不到我) 不能聊天,
                        *       # 8 推荐人设置权限(我的好友看不到他) 不能 聊天
                        *       # 9 不能聊天
                        *       # 100 可以聊天
                         */
                        int status = data.getStatus();
                        switch (status){
                            case  1:
                            case  2:
                            case  6:
                            case  7:
                            case  8:
                                IntentUtil.goChatStatusActivity(activity,data);
                                break;
                            case  5:
                                // 请求引荐
                                IntentUtil.goChatReferralActivity(activity,data,entity);
                                break;
                            case  9:
                                ToastUtil.showToast(activity,"亲，人家不想跟你聊哟...");
                                break;
                            case  100:
                                IntentUtil.goChattingActivity(activity,entity);
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int resultCode, String desc) {

            }
        });
    }
}
