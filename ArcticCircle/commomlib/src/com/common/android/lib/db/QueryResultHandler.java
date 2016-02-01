/**
 *
 *项目名：LuluYouLib
 *包名：com.luluyou.android.lib.db
 *文件名：QueryResultHandler.java
 *版本信息：1.0.0
 *创建日期：2013年12月9日-下午4:19:50
 *创建人：jerry.deng
 *Copyright (c) 2013上海路路由信息科技有限公司-版权所有
 * 
 */
package com.common.android.lib.db;

import android.database.Cursor;

/**
 *
 * 类名称：QueryResultHandler
 * 类描述：
 * 创建人： Jerry.deng
 * 修改人： Jerry.deng
 * 修改时间： 2013年12月9日 下午4:19:50
 * 修改备注：
 * @version 1.0.0
 *
 */
public interface QueryResultHandler<T> {
    T handle(Cursor cursor,int numOfCols);
}
