package com.xiaoma.beiji.manager.chatting;

public class IMConfig {
    public final static class Action {
        private static final String PACKAGE = "com.xiaoma.beiji.";
        public static final String ACTION_SYNCHRONIZE_OFFLINE_MSG = PACKAGE + "action_synchronize_offline_msg";
        public static final String ACTION_SYNCHRONIZE_SELF_MSG = PACKAGE + "action_synchronize_self_msg";
        public static final String ACTION_NEW_MSG = PACKAGE + ".chat.newmsg";
        public static final String ACTION_UPDATE_MSG = PACKAGE + ".chat.updatemsg";
        public static final String ACTION_CONVERSATION_CHANGED = PACKAGE + "action_conversation_changed";
    }

    public static final class MessageType {
        public static final String ChatTypeMessage = "message";
        public static final String ChatTypeRecommend = "recommend";
        public static final String ChatTypeRecommendResult = "recommend_result";
        public static final String ChatTypeDynamicPraise = "dynamic_praise";
        public static final String ChatTypeDynamicCollection = "dynamic_collection";
        public static final String ChatTypeDynamicComment = "dynamic_comment";
        public static final String ChatTypeDynamicReply = "dynamic_reply";
        public static final String ChatTypeHelp = "help";
        public static final String ChatTypeHelpComment = "help_comment";
        public static final String ChatTypeHelpReply = "help_reply";
    }


    public static final int SHOW_TYPE_TEXT_SEND = 0;
    public static final int SHOW_TYPE_TEXT_RECEIVE = 1;
    public static final int SHOW_MESSAGE_PAGE_SIZE = 20;
}
