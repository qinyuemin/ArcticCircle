/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.database
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.database;

import android.content.ContentValues;
import android.database.Cursor;
import com.common.android.lib.db.QueryResultHandler;
import com.xiaoma.beiji.base.BaseApplication;
import com.xiaoma.beiji.common.Global;
import com.xiaoma.beiji.entity.IMSystemMessageEntity;
import com.xiaoma.beiji.entity.IMXMPPMessageEntity;
import com.xiaoma.beiji.database.TableMetadataDef.ArcticXMPPMessage;
import com.xiaoma.beiji.database.TableMetadataDef.ArcticSystemMessage;
import com.xiaoma.beiji.manager.chatting.IMConfig;

import java.util.List;

/**
 * 类名称： SimpleDBManager
 * 类描述： 数据库操作工具类
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月17 23:17
 * 修改备注：
 *
 * @version 1.0.0
 */
public class SimpleDBManager {
    private static SimpleBaseDB simpleBaseDB = null;

    static {
        simpleBaseDB = BaseApplication.getInstance().getSimpleBaseDB();
    }

    public final static class IMSystemMessage {
        public static void insertMsg(IMSystemMessageEntity entity) {
            int num = simpleBaseDB.queryNumber(ArcticSystemMessage.SQL_QUERY_HAS_MSG, String.valueOf(entity.getId()));
            if (num <= 0) {
                ContentValues values = new ContentValues();
                values.put(ArcticSystemMessage._ID, entity.getId());
                values.put(ArcticSystemMessage.COLUMN_BODY, entity.getBody());
                values.put(ArcticSystemMessage.COLUMN_HAS_READ, entity.isHasRead() ? 1 : 0);
                values.put(ArcticSystemMessage.COLUMN_TYPE, entity.getType());
                values.put(ArcticSystemMessage.COLUMN_USER_ID, entity.getUserId());

                simpleBaseDB.insert(ArcticSystemMessage.TABLE_NAME, values);
            }
        }

        public static void deleteMsg(int msgId) {
            simpleBaseDB.queryNumber(ArcticSystemMessage.SQL_DELETE_MESSAGE, String.valueOf(msgId));
        }

        public static List<IMSystemMessageEntity> queryMessageList(int userId, int offset) {
            List<IMSystemMessageEntity> messageEntities = simpleBaseDB.queryList(ArcticSystemMessage.SQL_QUERY_MESSAGE, new QueryResultHandler<IMSystemMessageEntity>() {
                @Override
                public IMSystemMessageEntity handle(Cursor cursor, int numOfCols) {
                    return generateMessageEntity(cursor);
                }
            }, String.valueOf(userId), String.valueOf(offset), String.valueOf(IMConfig.SHOW_MESSAGE_PAGE_SIZE));
            return messageEntities;
        }

        private static IMSystemMessageEntity generateMessageEntity(Cursor cursor) {
            IMSystemMessageEntity entity = new IMSystemMessageEntity();

            entity.setId(cursor.getInt(cursor.getColumnIndex(ArcticSystemMessage._ID)));
            entity.setBody(cursor.getString(cursor.getColumnIndex(ArcticSystemMessage.COLUMN_BODY)));
            entity.setType(cursor.getString(cursor.getColumnIndex(ArcticSystemMessage.COLUMN_TYPE)));
            entity.setHasRead(cursor.getInt(cursor.getColumnIndex(ArcticSystemMessage.COLUMN_HAS_READ)) == 1);
            entity.setUserId(cursor.getInt(cursor.getColumnIndex(ArcticSystemMessage.COLUMN_USER_ID)));

            return entity;
        }
    }

    public final static class IMXMPPMessage {
        public static final String DEFALUT_ID = "0";

        public static void insertLocalMsg(IMXMPPMessageEntity entity) {
            ContentValues values = new ContentValues();
            values.put(ArcticXMPPMessage._ID, DEFALUT_ID);
            values.put(ArcticXMPPMessage.COLUMN_BODY, entity.getBody());
            values.put(ArcticXMPPMessage.COLUMN_CHAT_USER_ID, entity.getChatUserId());
            values.put(ArcticXMPPMessage.COLUMN_CHAT_USER_JID, entity.getChatUserJid());

            values.put(ArcticXMPPMessage.COLUMN_TYPE, entity.getType());
            values.put(ArcticXMPPMessage.COLUMN_TO_USER_ID, entity.getToUserId());
            values.put(ArcticXMPPMessage.COLUMN_FROM_USER_ID, entity.getFromUserId());
            values.put(ArcticXMPPMessage.COLUMN_CONTENT, entity.getContent());
            values.put(ArcticXMPPMessage.COLUMN_NICKNAME, entity.getNickname());
            values.put(ArcticXMPPMessage.COLUMN_AVATAR, entity.getAvatar());
            values.put(ArcticXMPPMessage.COLUMN_TIME, entity.getTime());

            values.put(ArcticXMPPMessage.COLUMN_HAS_READ, entity.isHasRead() ? 1 : 0);
            values.put(ArcticXMPPMessage.COLUMN_USER_ID, entity.getUserId());
            values.put(ArcticXMPPMessage.COLUMN_PID, entity.getPid());
            values.put(ArcticXMPPMessage.COLUMN_TIMESTAMP, entity.getTimestamp());
            simpleBaseDB.insert(ArcticXMPPMessage.TABLE_NAME, values);
        }

        public static void insertServerMsg(IMXMPPMessageEntity entity) {
            int num = simpleBaseDB.queryNumber(ArcticXMPPMessage.SQL_QUERY_HAS_MSG, String.valueOf(entity.getId()));
            if (num <= 0) {
                ContentValues values = new ContentValues();
                values.put(ArcticXMPPMessage._ID, entity.getId());
                values.put(ArcticXMPPMessage.COLUMN_BODY, entity.getBody());
                values.put(ArcticXMPPMessage.COLUMN_CHAT_USER_ID, entity.getChatUserId());
                values.put(ArcticXMPPMessage.COLUMN_CHAT_USER_JID, entity.getChatUserJid());

                values.put(ArcticXMPPMessage.COLUMN_TYPE, entity.getType());
                values.put(ArcticXMPPMessage.COLUMN_TO_USER_ID, entity.getToUserId());
                values.put(ArcticXMPPMessage.COLUMN_FROM_USER_ID, entity.getFromUserId());
                values.put(ArcticXMPPMessage.COLUMN_CONTENT, entity.getContent());
                values.put(ArcticXMPPMessage.COLUMN_NICKNAME, entity.getNickname());
                values.put(ArcticXMPPMessage.COLUMN_AVATAR, entity.getAvatar());
                values.put(ArcticXMPPMessage.COLUMN_TIME, entity.getTime());

                values.put(ArcticXMPPMessage.COLUMN_HAS_READ, entity.isHasRead() ? 1 : 0);
                values.put(ArcticXMPPMessage.COLUMN_USER_ID, entity.getUserId());
                values.put(ArcticXMPPMessage.COLUMN_PID, entity.getPid());
                values.put(ArcticXMPPMessage.COLUMN_TIMESTAMP, entity.getTimestamp());
                simpleBaseDB.insert(ArcticXMPPMessage.TABLE_NAME, values);
            }
        }

        public static List<IMXMPPMessageEntity> queryMessageList(int userId1, int friendUserId,  int offset) {
            String friendId = String.valueOf(friendUserId);
            String userId = String.valueOf(userId1);
            String offsetTag = String.valueOf(offset);
            List<IMXMPPMessageEntity> messageEntities = simpleBaseDB.queryList(ArcticXMPPMessage.SQL_QUERY_MESSAGE, new QueryResultHandler<IMXMPPMessageEntity>() {
                @Override
                public IMXMPPMessageEntity handle(Cursor cursor, int numOfCols) {
                    return generateMessageEntity(cursor);
                }
            }, friendId, userId, offsetTag, String.valueOf(IMConfig.SHOW_MESSAGE_PAGE_SIZE));
            simpleBaseDB.queryNumber(ArcticXMPPMessage.SQL_UPDATE_MSG_STATUS, friendId, userId);
            return messageEntities;
        }

        public static List<IMXMPPMessageEntity> querySessionList(int userId) {
            List<IMXMPPMessageEntity> sessionList = simpleBaseDB.queryList(ArcticXMPPMessage.SQL_QUERY_SESSION, new QueryResultHandler<IMXMPPMessageEntity>() {
                @Override
                public IMXMPPMessageEntity handle(Cursor cursor, int numOfCols) {
                    return generateMessageEntity(cursor);
                }
            }, String.valueOf(userId));
            return sessionList;
        }

        public static void deleteSession(int chatUserId) {
            simpleBaseDB.queryNumber(ArcticXMPPMessage.SQL_DELETE_SESSION, String.valueOf(Global.getUserId()), String.valueOf(chatUserId));
        }


        private static IMXMPPMessageEntity generateMessageEntity(Cursor cursor) {
            IMXMPPMessageEntity entity = new IMXMPPMessageEntity();

            entity.setBody(cursor.getString(cursor.getColumnIndex(ArcticXMPPMessage.COLUMN_BODY)));
            entity.setId(cursor.getInt(cursor.getColumnIndex(ArcticXMPPMessage._ID)));
            entity.setChatUserId(cursor.getInt(cursor.getColumnIndex(ArcticXMPPMessage.COLUMN_CHAT_USER_ID)));
            entity.setChatUserJid(cursor.getString(cursor.getColumnIndex(ArcticXMPPMessage.COLUMN_CHAT_USER_JID)));

            entity.setType(cursor.getString(cursor.getColumnIndex(ArcticXMPPMessage.COLUMN_TYPE)));
            entity.setToUserId(cursor.getInt(cursor.getColumnIndex(ArcticXMPPMessage.COLUMN_TO_USER_ID)));
            entity.setFromUserId(cursor.getInt(cursor.getColumnIndex(ArcticXMPPMessage.COLUMN_FROM_USER_ID)));
            entity.setContent(cursor.getString(cursor.getColumnIndex(ArcticXMPPMessage.COLUMN_CONTENT)));
            entity.setNickname(cursor.getString(cursor.getColumnIndex(ArcticXMPPMessage.COLUMN_NICKNAME)));
            entity.setAvatar(cursor.getString(cursor.getColumnIndex(ArcticXMPPMessage.COLUMN_AVATAR)));
            entity.setTime(cursor.getString(cursor.getColumnIndex(ArcticXMPPMessage.COLUMN_TIME)));

            entity.setHasRead(cursor.getInt(cursor.getColumnIndex(ArcticXMPPMessage.COLUMN_HAS_READ)) == 1);
            entity.setUserId(cursor.getInt(cursor.getColumnIndex(ArcticXMPPMessage.COLUMN_USER_ID)));
            entity.setPid(cursor.getInt(cursor.getColumnIndex(ArcticXMPPMessage.COLUMN_PID)));
            entity.setTimestamp(cursor.getString(cursor.getColumnIndex(ArcticXMPPMessage.COLUMN_TIMESTAMP)));

            return entity;
        }


    }

    public final static class IMLastXMPPMessage {
    }

}
