/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.database
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.common.android.lib.db.SqliteDatabaseAccess;

import java.util.Map;

/**
 *
 * 类名称： BaseDB
 * 类描述： 
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月17 22:47
 * 修改备注：
 * @version 1.0.0
 *
 */
public class SimpleBaseDB extends SqliteDatabaseAccess {

    public SimpleBaseDB(Context context) {
        super(context, TableMetadataDef.DATABASE_BASE_NAME, TableMetadataDef.DATABASE_BASE_VERSION);
    }

    @Override
    protected void postCreateOrUpgradeOperation(SQLiteDatabase db, String tableName, int operationType) {

    }

    @Override
    protected Map<String, String[]> getTableSchemaInfo() {
        return TableMetadataDef.simpleDBTableInfoMapping;
    }

    @Override
    protected Map<String, Integer> getDatabaseUpdateTags() {
        return null;
    }
}
