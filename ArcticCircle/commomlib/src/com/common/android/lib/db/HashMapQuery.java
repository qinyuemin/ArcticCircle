/**
 *
 *项目名：LuluYouLib
 *包名：com.luluyou.android.lib.db
 *文件名：HashMapQuery.java
 *版本信息：1.0.0
 *创建日期：2013年12月10日-上午11:22:30
 *创建人：jerry.deng
 *Copyright (c) 2013上海路路由信息科技有限公司-版权所有
 * 
 */
package com.common.android.lib.db;

import java.util.HashMap;

import android.database.Cursor;

/**
 *
 * 类名称：HashMapQuery
 * 类描述：
 * 创建人： Jerry.deng
 * 修改人： Jerry.deng
 * 修改时间： 2013年12月10日 上午11:22:30
 * 修改备注：
 * @version 1.0.0
 *
 */
public class HashMapQuery implements QueryResultHandler<HashMap<String, String>> {

    /* (non-Javadoc)
     * @see com.luluyou.android.lib.db.QueryResultHandler#handle(android.database.Cursor, int)
     */
    @Override
    public HashMap<String, String> handle(Cursor cursor, int numOfCols) {
        HashMap<String,String> result = new HashMap<String,String>();
        for(int i = 0; i < numOfCols; i++){
            result.put(cursor.getColumnName(i), cursor.getString(i));
        }
        return result;
    }

}
