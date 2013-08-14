package com.kuc_arc_f.app.picasa.db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.kuc_arc_f.app.picasa.ItemPT;
import com.kuc_arc_f.fw.ComUtil;
import com.kuc_arc_f.fw.StringUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class ImageCacheDB extends SQLiteOpenHelper {

    private static final String DB_FILE = "imagecache.db";
    public  static final String TBL_CACHE = "ImageCache";
    private static final int    DB_VERSION = 1;
 
    static com.kuc_arc_f.fw.StringUtil m_String = new StringUtil();
    //com.kuc_arc_f.fw.ComUtil m_Util = new ComUtil();
    //
    public interface CacheColumn {
        public static final String ID = "_id";
        public static final String NAME = "fileName";
        public static final String REGIST_DATE = "registDate";
        public static final String URL         = "url";
        public static final String TYPE        = "type";
        public static final String PID         = "pid";
        public static final String CREATE_AT   = "create_at";
    }
    
    private SQLiteDatabase mDB;
    
    public static ImageCacheDB instance;
    
    private ImageCacheDB(Context context) {
        super(context, DB_FILE, null, DB_VERSION);
    }
    
    public static ImageCacheDB getInstance(Context context) {
        if (instance == null) {
            instance = new ImageCacheDB(context);
        }
        return instance;
    }
    
    synchronized private SQLiteDatabase getDB() {
        if (mDB == null) {
            mDB = getWritableDatabase();
        }
        return mDB;
    }
    
    public void close() {
        if (mDB != null) {
            getDB().close();
        }
        super.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TBL_CACHE + " ("
                + CacheColumn.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CacheColumn.REGIST_DATE + " INTEGER,"
                + CacheColumn.URL +  " text,"
                + CacheColumn.TYPE + " VARCHAR(100),"
                + CacheColumn.NAME + " text,"
                + CacheColumn.PID +  "  text,"
                + CacheColumn.CREATE_AT + " text )"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert_byTran(ArrayList<ItemPT> items) throws Exception{
    	SQLiteDatabase db=null;
    	try
    	{
    		db= getDB();
    		db.beginTransaction();
	    	Calendar cal1 = Calendar.getInstance();
	    	String s_tm   = conv_DateToString(cal1);
	    	long l_tm     = new Date().getTime();
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO "+ TBL_CACHE +"(");
			sb.append(CacheColumn.REGIST_DATE);
			sb.append("," + CacheColumn.URL);
			sb.append("," + CacheColumn.PID);
			sb.append("," + CacheColumn.CREATE_AT + ") VALUES (? ,? ,? ,? );");
			String s_sql =sb.toString();
		    SQLiteStatement stmt= db.compileStatement( s_sql );
			
    		for(int i=0; i <items.size(); i++){
    			ItemPT item = items.get(i);
				String s_url    = item.getUrl_img_t().toString();
				String s_id     = item.getId().toString();
	    		Cursor c= find_tblId(s_id);
				if (!c.moveToFirst()) {
					stmt.bindLong(  1, l_tm);
					stmt.bindString(2, s_url);
					stmt.bindString(3, s_id);
					stmt.bindString(4, s_tm);
					stmt.executeInsert();
				}
				c.close();
    		}
    		db.setTransactionSuccessful();
    	}catch(Exception e){
    		throw e;
		}finally {
			db.endTransaction();
		}    	
    }
    
    public int update(long id, String filename, String type) {
        ContentValues values = new ContentValues();
        values.put(CacheColumn.TYPE, type);
        values.put(CacheColumn.NAME, filename);
        return getDB().update(TBL_CACHE, values, CacheColumn.ID + " = ?", new String[]{String.valueOf(id)});
    }
    
    public int delete(long id) {
        return getDB().delete(TBL_CACHE, CacheColumn.ID + " = ?", new String[]{String.valueOf(id)});
    }
    public void delete_byDay(String s_num) throws Exception {
    	try
    	{
    		String sql ="delete from " +TBL_CACHE+ " where ";
    		       sql+= CacheColumn.CREATE_AT +"< datetime('now', 'utc', '-"+s_num+" days')";
	        getDB().execSQL(sql);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public Cursor exists_byId(String s_id) {
        return getDB().query(TBL_CACHE, null, CacheColumn.PID + " = ?", new String[]{s_id}, null, null, null);
    }
    
    public Cursor existsFile_byId(String s_id) {
        return getDB().query(TBL_CACHE, null, CacheColumn.PID + " = ? AND " + CacheColumn.NAME + " IS NOT NULL", new String[]{s_id}, null, null, null);
    }

    public Cursor find_tblId(String s_id ) throws Exception {
    	try
    	{
        	String sql = "select " +CacheColumn.ID +" from "+TBL_CACHE+" where ";
        	       sql+= CacheColumn.PID  +"=" + s_id;
        	return  getDB().rawQuery(sql, null );
    	}catch(Exception e){
    		throw e;
    	}
    }
    
    public Cursor find_old_byDay(String s_num) throws Exception {
    	try
    	{
        	String sql = "select "+CacheColumn.NAME+"," +CacheColumn.ID +" from "+TBL_CACHE+" where ";
        	       sql+= CacheColumn.CREATE_AT +"< datetime('now', 'utc', '-"+s_num+" days')";
        	return  getDB().rawQuery(sql, null );
    	}catch(Exception e){
    		throw e;
    	}
    }
    
    public Cursor findAll() {
        return getDB().query(TBL_CACHE,
                null,
                null,
                null,
                null, null, null);
    }
    
    @Override
    protected void finalize() throws Throwable {
        if (mDB != null) {
            mDB.close();
        }
        this.close();
        super.finalize();
    }
    //
	String conv_DateToString( Calendar cal1 )
	{
	    int year = cal1.get(Calendar.YEAR);        //(2)現在の年を取得
	    int month = cal1.get(Calendar.MONTH) + 1;  //(3)現在の月を取得
	    int day = cal1.get(Calendar.DATE);         //(4)現在の日を取得
	    int hour = cal1.get(Calendar.HOUR_OF_DAY); //(5)現在の時を取得
	    int minute = cal1.get(Calendar.MINUTE);    //(6)現在の分を取得
	    int second = cal1.get(Calendar.SECOND);    //(7)現在の秒を取得

		String sRet="";
		sRet = String.valueOf(  year );
		sRet = sRet + "-" +m_String.comConv_ZeroToStr(String.valueOf( month), 2) ;
		sRet = sRet + "-" +m_String.comConv_ZeroToStr(String.valueOf(day) ,2);
		sRet = sRet + " " +m_String.comConv_ZeroToStr(String.valueOf(hour),2);
		sRet = sRet + ":" +m_String.comConv_ZeroToStr(String.valueOf(minute ), 2);
		sRet = sRet + ":" +m_String.comConv_ZeroToStr(String.valueOf(second),2);
		return sRet;
	}
}
