package com.xiaoma.beiji.adapter;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.makeapp.javase.util.DateUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.entity.IMXMPPMessageEntity;
import com.xiaoma.beiji.entity.UserInfoEntity;
import com.xiaoma.beiji.manager.chatting.IMConfig;
import com.xiaoma.beiji.util.ImageLoaderUtil;
import com.xiaoma.beiji.util.IntentUtil;

public class IMChattingMsgAdapter extends LLBaseAdapter<IMXMPPMessageEntity>{

    private Activity activity;
    private LayoutInflater layoutInflater;
    /**
     * 图片的最大宽度为屏幕的60%。点击图片全屏查看
     */
    private int maxBmpWidth;
    private int maxBmpHeight;
    private int maxLocationWidth;
    private int maxLocationHeight;
    private static final int KEY_DATA = R.layout.activity_chatting;
    private Handler h = new Handler();
    private DisplayImageOptions avatarOptions;
    private DisplayImageOptions displayImageOptions;
    private int screenWidth;
    private int screenHeight;
    private float dp;

    private UserInfoEntity friendUser;
    private UserInfoEntity currentUser;

    public IMChattingMsgAdapter(Activity activity,UserInfoEntity currentUser,UserInfoEntity friendUser) {
        super(activity, null);
        this.activity = activity;
        this.currentUser = currentUser;
        this.friendUser = friendUser;

        screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
        screenHeight = activity.getResources().getDisplayMetrics().heightPixels;
        dp = activity.getResources().getDisplayMetrics().density;
        avatarOptions = ImageLoaderUtil.getDefaultAvatarOption();
        displayImageOptions = ImageLoaderUtil.getDefaultDownloadImageOption();
        layoutInflater = getLayoutInflater();
        maxBmpWidth = (int) (screenWidth * 0.4);
        maxBmpHeight = (int) (maxBmpWidth * 0.8);
        maxLocationWidth = (int) (screenWidth * 0.7);
        maxLocationHeight = (int) (maxLocationWidth * 0.5);
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        IMXMPPMessageEntity msgEntity = getItem(position);
        ViewHolderMsg vh = null;
        if (contentView == null) {
            vh = new ViewHolderMsg();
            int viewType = getItemViewType(position);
            int layoutId = getLayoutIdByViewType(viewType);
            contentView = layoutInflater.inflate(layoutId, null);
            vh.initHolderView(contentView);
            contentView.setTag(vh);
        }
        vh = (ViewHolderMsg) contentView.getTag();
        vh.initHolderData(msgEntity, position);
        return contentView;
    }

    private int getLayoutIdByViewType(int viewType) {
        switch (viewType) {
            case IMConfig.SHOW_TYPE_TEXT_SEND:
                return R.layout.lv_item_immsg_send_text;
            case IMConfig.SHOW_TYPE_TEXT_RECEIVE:
                return R.layout.lv_item_immsg_receive_text;
            default:
                break;
        }
        return R.layout.lv_item_immsg_receive_text;
    }


    @Override
    public int getItemViewType(int position) {
        return getItem(position).getMsgShowType(currentUser.getUserId());
    }

    @Override
    public int getViewTypeCount() {
        return 11;
    }

    /**
     * 每条消息的显示，只有内容不同
     *
     * @author hegao
     */
    class ViewHolderMsg {
        // 发送者身份相关
        public ImageView img_avatar;
        public TextView tv_nick_name;
        // 文本
        public TextView tv_content;
        public TextView tv_time;
        // 消息状态相关,只在发送出去的消息
        public ProgressBar progressBar_sending;
        public ImageView img_failed;

        public void initHolderView(View contentView) {
            img_avatar = (ImageView) contentView.findViewById(R.id.img_chat_avatar);
            tv_nick_name = (TextView) contentView.findViewById(R.id.tv_nick_name);
            tv_time = (TextView) contentView.findViewById(R.id.tv_send_time);
            progressBar_sending = (ProgressBar) contentView.findViewById(R.id.progress_sending);
            img_failed = (ImageView) contentView.findViewById(R.id.img_failed);
            tv_content = (TextView) contentView.findViewById(R.id.tv_content);

            img_avatar.setImageResource(R.drawable.ic_def_header);
        }

        public void initHolderData(final IMXMPPMessageEntity msgEntity, int position) {
            String avatarUrl = "";
            String nickname = "";
            final int viewType = getItemViewType(position);
            if(viewType == IMConfig.SHOW_TYPE_TEXT_SEND){
                avatarUrl = currentUser.getAvatar();
                nickname = currentUser.getNickname();
            }else if(viewType == IMConfig.SHOW_TYPE_TEXT_RECEIVE){
                avatarUrl = friendUser.getAvatar();
                nickname = friendUser.getNickname();
            }
            ImageLoader.getInstance().displayImage(avatarUrl,img_avatar,ImageLoaderUtil.getDefaultAvatarOption());

            tv_nick_name.setText(nickname);
            tv_content.setText(msgEntity.getContent());

            if (isTimeVisible(tv_time, position)) {
                tv_time.setText(AdapterViewUtil.getShowTime(msgEntity.getTime()));
            }

            if (img_avatar != null) {
                img_avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(viewType == IMConfig.SHOW_TYPE_TEXT_SEND){
                            // 跳转到个人中心
                        }else if(viewType == IMConfig.SHOW_TYPE_TEXT_RECEIVE){
                            IntentUtil.goFriendDetailActivity(activity,String.valueOf(friendUser.getUserId()));
                        }
                    }
                });
            }
            if (img_failed != null) {
                img_failed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showReSendDialog(msgEntity);

                    }
                });
            }
        }
    }

    public void showReSendDialog(final IMXMPPMessageEntity msg) {
//        final SimpleDialog resendDialog = new SimpleDialog(activity).setDialogTitle("提示").setDialogMessage("是否重新发送");
//        resendDialog.setDialogLeftButton("取消", new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                resendDialog.dismiss();
//            }
//        });
//        resendDialog.setDialogRightButton("确定", new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                if (msg.type == IMMessageEntity.TYPE_IMG) {
////					IMContextManager.getInstance(activity).sendMessage(msg, true, ImgMsgHandler.getInstance());
//                } else {
////					IMContextManager.getInstance(activity).sendMessage(msg, true, null);
//                }
//                // TODO其他非文本类型需要上传的，给对应的上传处理器
//            }
//        });
//        resendDialog.show();
    }

    private boolean isTimeVisible(TextView tv_chat_time, int position) {
        if (position == 0) {
            tv_chat_time.setVisibility(View.VISIBLE);
            return true;
        }
        if ((DateUtil.toDate(getItem(position).getTime()).getTime() - DateUtil.toDate(getItem(position - 1).getTime()).getTime()) > 1000 * 60 * 2) {
            tv_chat_time.setVisibility(View.VISIBLE);
            return true;
        } else {
            tv_chat_time.setVisibility(View.GONE);
        }
        tv_chat_time.setVisibility(View.GONE);
        return false;
    }
}
