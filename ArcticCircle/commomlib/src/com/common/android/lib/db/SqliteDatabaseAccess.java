/**
 *
 *项目名：LuluYouLib
 *包名：com.luluyou.android.lib.db
 *文件名：SqliteDatabaseAccess.java
 *版本信息：1.0.0
 *创建日期：2013年12月9日-下午3:57:20
 *创建人：jerry.deng
 *Copyright (c) 2013上海路路由信息科技有限公司-版权所有
 * 
 */
package com.common.android.lib.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.text.TextUtils;

import android.util.Log;
import com.common.android.lib.util.ApacheLikeUtils;

/**
 *
 * 类名称：SqliteDatabaseAccess
 * 类描述：数据库操作工具类
 * 创建人： Jerry.deng
 * 修改人： Jerry.deng
 * 修改时间： 2013年12月9日 下午3:57:20
 * 修改备注：
 * @version 1.0.0
 *
 */
public abstract class SqliteDatabaseAccess extends SQLiteOpenHelper {
    
    //private static final Logger LOG = LoggerFactory.getLogger(SqliteDatabaseAccess.class);
    private static final String TAG = "SqliteDatabaseAccess";
    
    public static final QueryResultHandler<String[]> QSTRINGS = new StringArrayQuery();
    public static final QueryResultHandler<Integer> QINT = new IntegerQuery();
    public static final QueryResultHandler<String> QSTRING = new StringQuery();
    public static final QueryResultHandler<HashMap<String,String>> QHASHMAPS = new HashMapQuery();
    
    public static final String TABLE_TEMP = "_temp";
    public static final String SQLITE3_TEST_SQL = "select count(*) from Sqlite_master";
    public static final String SQLITE3_GET_TABLE_SQL = "select name from Sqlite_master where type='table'";
    
    
    protected static final int TYPE_DB_OPERATION_CREATE = 0;
    protected static final int TYPE_DB_OPERATION_UPGRADE = 1;
    
    public static final int TYPE_DB_UPGRADE_DELETE_CREATE = 0;
    public static final int TYPE_DB_UPGRADE_RESTORE_DATA = 1;

    protected Context context;

    /**
     * 创建一个新的实例 SqliteDatabaseAccess.
     */
    public SqliteDatabaseAccess(Context context, String dbName, int dbVersion) {
        super(context, dbName, null, dbVersion);
        this.context = context;
    }

    /* (non-Javadoc)
     * 
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "创建数据库，path=" + db.getPath() + ",Version=" + db.getVersion());
        try{
            this.createDatabase(db);
        }catch(RuntimeException e){
            Log.e(TAG, "创建数据库失败");
            throw e;
        }
    }

    /* (non-Javadoc)
     * 升级数据库使用
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "数据库升级，path=" + db.getPath() + ",Version=" + db.getVersion());
        try{
            this.cleanupDatabase(db);
        }catch(RuntimeException e){
            Log.e(TAG, "数据库升级失败");
        }
    }
    
    protected void databaseCreateOrUpdateOperation(SQLiteDatabase db, int operationType){
        Map<String,String[]>  tableSchemaInfoMapping = this.getTableSchemaInfo();
        Map<String,Integer>  tableUpgradeTags = this.getDatabaseUpdateTags();
        //首先查询数据库中所有的表
        List<String>  tableNameList = this.getTableNameListFromSqliteMaster(db);
        
        if(null != tableSchemaInfoMapping && tableSchemaInfoMapping.size() > 0){
            Set<String>  keys = tableSchemaInfoMapping.keySet();
            for(String key : keys){
                if(TextUtils.isEmpty(key)){
                    continue;
                }
                String[]  tableSechma = tableSchemaInfoMapping.get(key);
                if(null == tableSechma || tableSechma.length == 0){
                    continue;
                }
                int upgradeType = (tableUpgradeTags != null && tableUpgradeTags.containsKey(key))?tableUpgradeTags.get(key):TYPE_DB_UPGRADE_RESTORE_DATA;
                boolean bHandled = true;
                switch(operationType){
                case TYPE_DB_OPERATION_CREATE://创建表，数据库创建的时候调用
                    //创建表
                    createTableIfNotExisted(db, key, tableSechma);
                    Log.i(TAG, "创建表("  + key + ")成功");
                    break;
                case TYPE_DB_OPERATION_UPGRADE://数据库升级的使用使用该方法
                    if(TYPE_DB_UPGRADE_RESTORE_DATA == upgradeType){
                        databaseUpgradeRestoreData(db,key,tableSechma);
                    }else if(TYPE_DB_UPGRADE_DELETE_CREATE == upgradeType){
                        databaseUpgradeDeleteCreate(db,key,tableSechma);
                    }
                    deleteExistedTableFromList(tableNameList,key);
                    Log.i(TAG, "升级表("  + key + ")成功");
                    break;
                default:
                    bHandled = false;
                    break;
                }
                if(bHandled){//表创建或者升级成功后的其他操作
                    postCreateOrUpgradeOperation(db, key, operationType);
                }
            }
            //删除已经失效的表
            if(tableNameList != null && tableNameList.size() > 1){
                for(String item : tableNameList){
                    if(!TextUtils.isEmpty(item)){
                        Log.i(TAG, "-------删除不使用的表的名称是:" + item);
                        execSQL(db,"DROP TABLE IF EXISTS " + item);
                    }
                }
            }
        }
    }
    
    private List<String>  getTableNameListFromSqliteMaster(SQLiteDatabase db){
        List<String>  tableNameList = null;
        Cursor c = null;
        try{
            c = db.rawQuery(SQLITE3_GET_TABLE_SQL, null);
            if(c != null && c.getColumnCount() > 0){
                c.moveToFirst();
                tableNameList = new ArrayList<String>();
                while(!c.isAfterLast()){
                    String tableName = c.getString(0);
                    //过滤掉系统生成的表
                    if(!TextUtils.isEmpty(tableName) && !tableName.equalsIgnoreCase("sqlite_master")
                            && !tableName.equalsIgnoreCase("android_metadata") && !tableName.equalsIgnoreCase("sqlite_sequence")){
                        tableNameList.add(c.getString(0));
                    }                    
                    c.moveToNext();
                }
            }
        }finally {
            if(c != null && !c.isClosed()){
                c.close();
            }
        }
        return tableNameList;
    }
    
    private void deleteExistedTableFromList(List<String> tableList,String tableName){
        if(null == tableList || tableList.size()  == 0){
            Log.i(TAG, "数据库中没YOU任何的表");
            return;
        }
        //查找并且已经已经创建的表
        for(String  item :tableList ){
            if(!TextUtils.isEmpty(item)  && item.equals(tableName)){
                tableList.remove(item);
                break;
            }
        }
        
    }
    /**
     * 
     *databaseUpgradeRestoreData(这里用一句话描述这个方法的作用）
     *（恢复所有的数据）
     *void
     * @exception
     * @since 1.0.0
     */
    private void databaseUpgradeRestoreData(SQLiteDatabase db,String tableName,String[] tableSchema){
        
      //1.删除所有的临时表
        execSQL(db,"DROP TABLE IF EXISTS " + tableName + TABLE_TEMP);
        //2.创建表
        createTableIfNotExisted(db, tableName, tableSchema);
        //3. 将第二步创建的表重命名
        execSQL(db, "ALTER TABLE " + tableName + " RENAME TO " + tableName + TABLE_TEMP);
        //4.再次创建表
        createTableIfNotExisted(db, tableName, tableSchema);
        //5. 将临时表的数据插入再次创建的表中
        insertFromTemp(db, tableName + TABLE_TEMP, tableName);
        //6.将第3步创建的临时表删除
        execSQL(db,"DROP TABLE IF EXISTS " + tableName + TABLE_TEMP);
    }
    /**
     * 
     *databaseUpgradeDeleteCreate(这里用一句话描述这个方法的作用）
     *（删除重新建表，不恢复数据）
     *void
     * @exception
     * @since 1.0.0
     */
    private void databaseUpgradeDeleteCreate(SQLiteDatabase db,String tableName,String[] tableSchema){
        //1.删除目前所有的表
        execSQL(db,"DROP TABLE IF EXISTS " + tableName);
        //2.重新创建所有的表
        createTableIfNotExisted(db, tableName, tableSchema);
    }
    
    protected  void createDatabase(final SQLiteDatabase db) {
        try {
            db.beginTransaction();
            databaseCreateOrUpdateOperation(db,TYPE_DB_OPERATION_CREATE);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            db.endTransaction();
        }
    }
    
    protected final void cleanupDatabase(SQLiteDatabase db){
        Log.i(TAG, "执行数据库升级");

        try {
            db.beginTransaction();
            databaseCreateOrUpdateOperation(db,TYPE_DB_OPERATION_UPGRADE);
            db.setTransactionSuccessful();          
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            db.endTransaction();
        }
    }
   
    /**
     * 
     *refreshUpgradeData(这里用一句话描述这个方法的作用）
     *（升级完成后其他数据的处理）
     * @param db
     *void
     * @exception
     * @since 1.0.0
     */
    protected abstract  void postCreateOrUpgradeOperation(SQLiteDatabase db,String tableName,int operationType);
    protected abstract Map<String,String[]>  getTableSchemaInfo();
    protected abstract Map<String,Integer>  getDatabaseUpdateTags();
    
    
    protected void insertFromTemp(SQLiteDatabase db, String oldTableName, String newTableName){
        insertFromTemp(db, oldTableName, newTableName, false);
    }
    
    /**
     * insert from temp table
     * 
     * @param db
     */
    protected void insertFromTemp(SQLiteDatabase db, String oldTableName, String newTableName, boolean includeUpgradeIdColumn) {
        Log.i(TAG, "insertFromTemp");

        String columns = getCommonColumns(db, oldTableName, newTableName, includeUpgradeIdColumn);
        db.execSQL("INSERT INTO " + newTableName + " (" + columns + ") SELECT " + columns + " FROM " + oldTableName);
    }
    
    /**
     * get all common columns from table old and new
     * 
     * @param db
     * @param
     * @return
     */
    protected String getCommonColumns(SQLiteDatabase db, String oldTableName, String newTableName, boolean includeUpgradeIdColumn) {
        List<String> oldTableColumns = null;
        List<String> newTableColumns = null;

        oldTableColumns = getTableColumns(db, oldTableName);
        newTableColumns = getTableColumns(db, newTableName);

        Map<String, Integer> newColumns = new HashMap<String, Integer>();
        for (String oColumn : oldTableColumns) {
            newColumns.put(oColumn, 1);
        }

        for (String nColumn : newTableColumns) {
            if (newColumns.containsKey(nColumn)) {
                newColumns.put(nColumn, 2);
            }
        }

        if(includeUpgradeIdColumn == false){
            //ID不参与数据插入
            newColumns.remove(BaseColumns._ID);
        }
        
        List<String> columns = new ArrayList<String>();
        for (Iterator<String> iter = newColumns.keySet().iterator(); iter.hasNext();) {
            String c = iter.next();
            if (newColumns.get(c) == 2) {
                columns.add(c);
            }
        }

        String columnsSeperated = TextUtils.join(",", columns);
        return columnsSeperated;
    }
    
    protected List<String> getTableColumns(SQLiteDatabase db, String tableName) {
        ArrayList<String> columns = new ArrayList<String>();
        Cursor c = null;
        try {
            c = db.rawQuery("select * from " + tableName + " limit 1", null);
            if (c != null) {
                columns = new ArrayList<String>(Arrays.asList(c.getColumnNames()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null)
                c.close();
        }
        return columns;
    }
    
    /**
     * 
     *insert(这里用一句话描述这个方法的作用）
     *（插入一条记录到数据库中）
     * @param tableName
     * @param values
     * @return
     *long
     * @exception
     * @since 1.0.0
     */
    public synchronized long insert(String tableName,ContentValues values){
        return this.getWritableDatabase().insertOrThrow(tableName, null, values);
    }
    
    public synchronized long delete(String tableName){
    	return delete(tableName, null, null);
    }
    
    public synchronized long delete(String tableName, String whereClause, String[] whereArgs){
    	return this.getWritableDatabase().delete(tableName, whereClause, whereArgs);
    }
    
    public synchronized int update(String tableName,ContentValues values,String whereClause,String... whereArgs){
        return this.getWritableDatabase().update(tableName, values, whereClause, whereArgs);
    }
    
    public void execSql(String sql){
        this.getWritableDatabase().execSQL(sql);
    }
    /**
     * 
     *execSQL(这里用一句话描述这个方法的作用）
     *（如果传递了db，则直接使用该db执行sql语句）
     * @param db
     * @param sql
     *void
     * @exception
     * @since 1.0.0
     */
    public void execSQL(SQLiteDatabase db,String sql){
        db.execSQL(sql);
    }
    
    public void createTableIfNotExisted(SQLiteDatabase db,String tableName,String[] columns){
        String sql = String.format("create table if not exists %s(%s)", tableName,ApacheLikeUtils.join(columns,","));
        db.execSQL(sql);
    }
    
    public final String newUUID(){
        return UUID.randomUUID().toString();
    }
    
    public void dropTableIfExists(SQLiteDatabase db,String tablename){
        String sql = String.format("DROP TABLE IF EXISTS %s", tablename);
        db.execSQL(sql);
    }
    
    public <T> T queryObject(String sql, QueryResultHandler<T> objq,String ... args){
        Cursor c = null;
        T obj = null;
        try{
            c = this.getReadableDatabase().rawQuery(sql, args);
            if(c != null){
	            if(c.getCount() == 0 || c.isAfterLast()){
	            	obj = null;
	            }else{
	            	c.moveToFirst();
	            	obj = objq.handle(c, c.getColumnCount());
	            }
            }
        }finally {
            if(c != null && !c.isClosed()){
                c.close();
            }
        }
        return obj;
    }
    
    public <T> List<T> queryList(String sql, QueryResultHandler<T> objq,String ...args){
        Cursor c = null;
        List<T> results = Collections.emptyList();
        try{
            c = this.getReadableDatabase().rawQuery(sql, args);
            if(c != null){
	            int n = c.getColumnCount();
	            c.moveToFirst();
	            results = new ArrayList<T>();
	            while(!c.isAfterLast()){
	                results.add(objq.handle(c, n));
	                c.moveToNext();
	            }
            }
        }finally {
            if(c != null && !c.isClosed()){
                c.close();
            }
        }
        return results;
    }
    
    public void queryWithHandler(String sql,QueryResultHandler<Void> rh,String ...args){
        Cursor c = null;
        try{
            c = this.getReadableDatabase().rawQuery(sql, args);
            int n = c.getColumnCount();
            c.moveToFirst();
            while(!c.isAfterLast()){
                rh.handle(c, n);
                c.moveToNext();
            }
        }finally {
            if(c!= null && !c.isClosed()){
                c.close();
            }
        }
    }
    /**
     * 
     *queryStringsList(这里用一句话描述这个方法的作用）
     *（获取一个字符串数组列表）
     * @param sql
     * @param args
     * @return
     *List<String[]>
     * @exception
     * @since 1.0.0
     */
    public List<String[]> queryStringsList(String sql,String...args){
        return this.queryList(sql, QSTRINGS, args);
    }
    /**
     * 
     *queryStrings(这里用一句话描述这个方法的作用）
     *（得到一个字符串数据，其中的值为一条记录的值）
     * @param sql
     * @param args
     * @return
     *String[]
     * @exception
     * @since 1.0.0
     */
    public String[] queryStrings(String sql, String...args){
        return this.queryObject(sql, QSTRINGS, args);
    }
    /**
     * 
     *queryNumber(这里用一句话描述这个方法的作用）
     *（得到一个整型值）
     * @param sql
     * @param args
     * @return
     *int
     * @exception
     * @since 1.0.0
     */
    public int queryNumber(String sql, String...args){
        Integer result = queryObject(sql, QINT, args);
        return (result  == null)?0:result.intValue();
    }
    
    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
    		return super.getWritableDatabase();
    }
    
    /**
     * 
     *queryHashMapList(这里用一句话描述这个方法的作用）
     *（返回一个HashMap的list）
     * @param sql
     * @param args
     * @return
     *List<HashMap<String,String>>
     * @exception
     * @since 1.0.0
     */
    public List<HashMap<String,String>> queryHashMapList(String sql, String...args){
        return this.queryList(sql, QHASHMAPS, args);
    }
    
    public void beginTransaction(){
        this.getWritableDatabase().beginTransaction();
    }
    
    public void endTransaction(){
        this.getWritableDatabase().endTransaction();
    }
    
    public void setTransactionSuccessful(){
        this.getWritableDatabase().setTransactionSuccessful();
    }
    
    public void closeDB(){
        super.close();
    }
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        closeDB();
    }

}
