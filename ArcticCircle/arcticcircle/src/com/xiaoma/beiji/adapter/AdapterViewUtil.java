/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.adapter
 * 版本信息： 1.0.0
 * Copyright (c) 2015 版权所有
 */
package com.xiaoma.beiji.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ViewUtil;
import com.makeapp.javase.lang.StringUtil;
import com.makeapp.javase.util.DataUtil;
import com.makeapp.javase.util.DateUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.controls.view.CircularImage;
import com.xiaoma.beiji.entity.CommentEntity;
import com.xiaoma.beiji.entity.FriendDynamicEntity;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.manager.chatting.ChattingUtil;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.CommUtil;
import com.xiaoma.beiji.util.IntentUtil;

import java.util.List;

/**
 * 类名称： AdapterViewUtil
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月14 21:18
 * 修改备注：
 *
 * @version 1.0.0
 */
public class AdapterViewUtil {

    public static void initUserInfoView(final Activity activity, View contentView, final FriendDynamicEntity entity) {
        int releaseType = entity.getReleaseType();
        String avatar = entity.getAvatar();
        if (StringUtil.isValid(avatar)) {
            CircularImage imgHead = (CircularImage) contentView.findViewById(R.id.img_head);
            ImageLoader.getInstance().displayImage(avatar, imgHead);
        }

        TextViewUtil.setText(contentView, R.id.txt_name, entity.getNickName());
        if (releaseType == 1
                || releaseType == 2
                || releaseType == 3
                || releaseType == 6
                ) {
            // 系统 推荐信息 不修改
            TextViewUtil.setText(contentView, R.id.txt_tag_major, CommUtil.getGenderTag(entity.getGender()) + entity.getRelationalGrade());
        }
        TextViewUtil.setText(contentView, R.id.txt_address, entity.getArea());
        TextViewUtil.setText(contentView, R.id.txt_time, entity.getReleaseTypeTitle());

        if (releaseType == 1
                || releaseType == 2
                || releaseType == 3
                || releaseType == 6
                ) {
            // 系统 推荐信息 不聊天
            ViewUtil.setViewVisibility(contentView, R.id.img_chatting, (String.valueOf(Global.getUserId())).equals(entity.getUserId()) ? View.GONE : View.VISIBLE);
            ViewUtil.setViewOnClickListener(contentView, R.id.img_chatting, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserInfoEntity userEntity = new UserInfoEntity();
                    userEntity.setUserId(DataUtil.getInt(entity.getUserId()));
                    userEntity.setAvatar(entity.getAvatar());
                    userEntity.setNickname(entity.getNickName());

                    ChattingUtil.goChatting(activity,userEntity);
                }
            });
        }
    }

    public static void initCommentView(View contentView, FriendDynamicEntity entity) {
        View commentView = contentView.findViewById(R.id.ll_comment);
        List<CommentEntity> commentList = entity.getComment();
        if (commentList != null && commentList.size() > 0) {
            commentView.setVisibility(View.VISIBLE);
            CommentEntity commentEntity1 = commentList.get(0);
            String comment1 = "<font color=#2a2b2b>" + commentEntity1.getNickname() + "</font><font color=#b9b9b9> : " + commentEntity1.getCommentContent() + "</font>";
            TextViewUtil.setText(commentView, R.id.txt_comment1, Html.fromHtml(comment1));

            if (commentList.size() > 1) {
                CommentEntity commentEntity2 = commentList.get(1);
                String comment2 = "<font color=#2a2b2b>" + commentEntity2.getNickname() + "</font><font color=#b9b9b9> : " + commentEntity2.getCommentContent() + "</font>";
                TextViewUtil.setText(commentView, R.id.txt_comment2, Html.fromHtml(comment2));
                ViewUtil.setViewVisibility(commentView, R.id.txt_comment2, View.VISIBLE);

                ViewUtil.setViewVisibility(commentView, R.id.txt_comment2, commentList.size() > 2 ? View.VISIBLE : View.GONE);
            } else {
                ViewUtil.setViewVisibility(commentView, R.id.txt_comment2, View.GONE);
            }


        } else {
            commentView.setVisibility(View.GONE);
        }
    }


    public static void startScale(Activity activity,View view, final TextView txtA, LinearLayout llAction, final ImageView imgA) {
        int i1 = CommUtil.dip2px(activity, 35);
        setLayout(txtA,(int)view.getX() + i1,(int)llAction.getY() - CommUtil.dip2px(activity, 30));
        setLayout(imgA,(int)view.getX() + i1,(int)llAction.getY() + 20);
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.anim_collect);
        Animation animation1 = AnimationUtils.loadAnimation(activity, R.anim.anim_collect_txt);
        Animation.AnimationListener listener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                txtA.setVisibility(View.VISIBLE);
                imgA.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                txtA.setVisibility(View.GONE);
                imgA.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        animation.setAnimationListener(listener);
        animation1.setAnimationListener(listener);

        imgA.startAnimation(animation);
        txtA.startAnimation(animation1);
    }

    public static void setLayout(View view,int x,int y)
    {
        ViewGroup.MarginLayoutParams margin=new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(x,y, x+margin.width, y+margin.height);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }

    public static String getShowTime(String date){
        String showTime = date;
        long currentTime = System.currentTimeMillis();
        long msgTime = DateUtil.toDate(date).getTime();
        long l = Math.abs(msgTime - currentTime);
        if(l < 60 * 1000){// 1分钟
            showTime = "刚刚";
        }else if(l < 60 * 60 * 1000){
            showTime = (l / 60 / 1000) +"分钟前";
        }else if(l < 24 * 60 * 60 * 1000){
            showTime = (l / 60 / 60 / 1000) +"小时前";
        }else if(l < 2 * 24 * 60 * 60 * 1000){
            showTime = "昨天";
        } else if (l < 8 * 24 * 60 * 60 * 1000) {
            showTime = (l / 24 / 60 / 60 / 1000) +"天前";
        }
        return showTime;
    }
}
