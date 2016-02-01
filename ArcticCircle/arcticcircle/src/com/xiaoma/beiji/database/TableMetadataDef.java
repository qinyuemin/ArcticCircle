package com.xiaoma.beiji.database;

import java.util.HashMap;
import java.util.Map;

public final class TableMetadataDef {
    public static final String DATABASE_BASE_NAME = "simple_base.db";
    public static final int DATABASE_BASE_VERSION = 1;
    public static final String DATABASE_BASE_VERSION_KEY = "database_base_version";

    public static final Map<String, String[]> simpleDBTableInfoMapping;

    static {
        simpleDBTableInfoMapping = new HashMap<>();
        //将所有的表具体信息加入到该map中
        simpleDBTableInfoMapping.put(ArcticSystemMessage.TABLE_NAME, ArcticSystemMessage.TABLE_SCHEMA);
        simpleDBTableInfoMapping.put(ArcticXMPPMessage.TABLE_NAME, ArcticXMPPMessage.TABLE_SCHEMA);
//        simpleDBTableInfoMapping.put(ArcticLastXMPPMessage.TABLE_NAME, ArcticLastXMPPMessage.TABLE_SCHEMA);
    }

    public static final class ArcticSystemMessage {
        public static final String TABLE_NAME = "arctic_system_message";
        public static final String[] TABLE_SCHEMA = new String[]{
                "_id INTEGER",
                "_id_local INTEGER PRIMARY KEY AUTOINCREMENT",
                "body text",
                "hasRead integer",
                "pid integer",
                "timestamp datetime",
                "type text",
                "userId integer"
        };

        public static final String _ID = "_id";
        public static final String _ID_LOCAL = "_id_local";
        public static final String COLUMN_BODY = "body";
        public static final String COLUMN_HAS_READ = "hasRead";
        public static final String COLUMN_PID = "pid";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_USER_ID = "userId";

        public static final String SQL_QUERY_MESSAGE = "select * from " + TABLE_NAME + " where " + COLUMN_USER_ID + " = ?  order by "+ _ID_LOCAL + " limit ?,?";
        public static final String SQL_DELETE_MESSAGE = "delete  from " + TABLE_NAME + " where " + _ID + " = ? ";
        public static final String SQL_QUERY_HAS_MSG = "select * from " + TABLE_NAME + " where " + _ID + " = ? ";
    }

    public static final class ArcticXMPPMessage {
        public static final String TABLE_NAME = "arctic_xmpp_message";
        public static final String[] TABLE_SCHEMA = new String[]{
                "_id INTEGER ",
                "_id_local INTEGER PRIMARY KEY AUTOINCREMENT",
                "body TEXT",
                "chatUserId integer",
                "chatUserJid text",

                "type TEXT",
                "toUserId integer",
                "fromUserId integer",
                "content text",
                "nickname text",
                "avatar text",
                "time text",
                "hasRead integer",
                "userId integer",
                "pid integer",
                "timestamp datetime"
        };

        public static final String _ID = "_id";
        public static final String _ID_LOCAL = "_id_local";
        public static final String COLUMN_BODY = "body";
        public static final String COLUMN_CHAT_USER_ID = "chatUserId";
        public static final String COLUMN_CHAT_USER_JID = "chatUserJid";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_TO_USER_ID = "toUserId";
        public static final String COLUMN_FROM_USER_ID = "fromUserId";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_NICKNAME = "nickname";
        public static final String COLUMN_AVATAR = "avatar";
        public static final String COLUMN_TIME = "time";

        public static final String COLUMN_HAS_READ = "hasRead";
        public static final String COLUMN_PID = "pid";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_USER_ID = "userId";

        public static final String SQL_QUERY_MESSAGE = "select * from " + TABLE_NAME + " where " + COLUMN_CHAT_USER_ID + " = ? and " + COLUMN_USER_ID + " = ? order by "+_ID_LOCAL + " limit ?,?";
        public static final String SQL_QUERY_HAS_MSG = "select * from " + TABLE_NAME + " where " + _ID + " = ? ";
        public static final String SQL_QUERY_SESSION = "select * from " + TABLE_NAME + " where " + COLUMN_USER_ID + " = ? group by " + COLUMN_CHAT_USER_ID;
        public static final String SQL_UPDATE_MSG_STATUS = "update "+ TABLE_NAME +" set " + COLUMN_HAS_READ +" = 1 where "  + COLUMN_CHAT_USER_ID + " = ? and " + COLUMN_USER_ID + " = ?";
        public static final String SQL_DELETE_SESSION = "delete  from " + TABLE_NAME + " where " + COLUMN_USER_ID + " = ? and "+ COLUMN_CHAT_USER_ID + " = ? ";
    }

    public static final class ArcticLastXMPPMessage {
        public static final String TABLE_NAME = "arctic_last_xmpp_message";
        public static final String[] TABLE_SCHEMA = new String[]{
                "_id INTEGER PRIMARY KEY AUTOINCREMENT",
                "body TEXT",
                "chatUserId integer",
                "chatUserJid text",
                "content text",
                "fromUserId integer",
                "hasRead integer",
                "pid integer",
                "timestamp TEXT",
                "toUserId integer",
                "type TEXT",
                "userId integer"
        };

        public static final String _ID = "_id";
        public static final String COLUMN_BODY = "body";
        public static final String COLUMN_CHAT_USER_ID = "chatUserId";
        public static final String COLUMN_CHAT_USER_JID = "chatUserJid";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_FROM_USER_ID = "fromUserId";
        public static final String COLUMN_HAS_READ = "hasRead";
        public static final String COLUMN_PID = "pid";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_TO_USER_ID = "toUserId";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_USER_ID = "userId";
    }
}
