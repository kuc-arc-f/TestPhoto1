package com.kuc_arc_f.app.picasa.db;

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
import android.util.Log;

public class PhotoDB extends SQLiteOpenHelper {
	static String TAG="PhotoDB";

    private static final String DB_FILE   = "photo3.db";
    public  static final String TBL_PHOTO = "photo";
    private static final int    DB_VERSION = 1;
 
    static com.kuc_arc_f.fw.StringUtil m_String = new StringUtil();
    //com.kuc_arc_f.fw.ComUtil m_Util = new ComUtil();
    //
    public interface PhotoColumn {
        public static final String ID          = "_id";
        public static final String NAME        = "fileName";
        public static final String REGIST_DATE = "registDate";
        public static final String URL         = "url";
        public static final String TYPE        = "type";
        public static final String PID         = "pid";
        public static final String S_OWN       = "s_own";
        public static final String S_TITLE     = "s_title";
        public static final String S_URL       = "s_url";
        public static final String S_URL_T     = "s_url_t";
        public static final String P_NUM       = "p_num";
        public static final String D_NUM       = "d_num";
        public static final String HREF        = "href";
        public static final String C_FLG       = "c_flg";
        public static final String CREATE_AT   = "create_at";
    }
    
    private SQLiteDatabase mDB;
    
    public static PhotoDB instance;
    
    private PhotoDB(Context context) {
        super(context, DB_FILE, null, DB_VERSION);
    }
    
    public static PhotoDB getInstance(Context context) {
        if (instance == null) {
            instance = new PhotoDB(context);
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
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TBL_PHOTO + " ("
                + PhotoColumn.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PhotoColumn.REGIST_DATE + " INTEGER,"
                + PhotoColumn.URL +  " text,"
                + PhotoColumn.TYPE + " VARCHAR(100),"
                + PhotoColumn.NAME + " text,"
                + PhotoColumn.PID     +  "  text,"
                + PhotoColumn.S_TITLE +  "  text,"
                + PhotoColumn.S_OWN   +  "  text,"
                + PhotoColumn.S_URL   +  "  text,"
                + PhotoColumn.S_URL_T +  "  text,"
                + PhotoColumn.P_NUM   +  "  INTEGER,"
                + PhotoColumn.D_NUM   +  "  INTEGER,"
                + PhotoColumn.HREF    +  "  text,"
                + PhotoColumn.C_FLG   +  "  INTEGER,"
                + PhotoColumn.CREATE_AT + " text )"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public long insert_byItem(ItemPT item) {
    	long id=0;
    	try
    	{
        	String s_id = item.getId().toString();
        	if(Is_existId(s_id)==true){
        		return 0;
        	}
        	Calendar cal1 = Calendar.getInstance();
        	String s_tm= conv_DateToString(cal1);
            ContentValues values = new ContentValues();
            values.put(PhotoColumn.REGIST_DATE, new Date().getTime());
            values.put(PhotoColumn.URL   , item.getUrl_img().toString() );
            values.put(PhotoColumn.PID   ,  item.getId().toString() );
            values.put(PhotoColumn.S_TITLE , item.getTitle().toString() );
            values.put(PhotoColumn.S_URL , item.getUrl_img().toString() );
            values.put(PhotoColumn.S_URL_T, item.getUrl_img_t().toString() );
            values.put(PhotoColumn.P_NUM,   item.getPnum() );
            values.put(PhotoColumn.HREF,    item.getHref().toString() );
            values.put(PhotoColumn.CREATE_AT  , s_tm);
             id = getDB().insertOrThrow(TBL_PHOTO, null, values);    		
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
        return id;
    }
    
    public int update(long id, String filename, String type) {
        ContentValues values = new ContentValues();
        values.put(PhotoColumn.TYPE, type);
        values.put(PhotoColumn.NAME, filename);
        return getDB().update(TBL_PHOTO, values, PhotoColumn.ID + " = ?", new String[]{String.valueOf(id)});
    }
    public int update_dNum(ItemPT item) {
        ContentValues values = new ContentValues();
        values.put(PhotoColumn.D_NUM,  item.getDnum());
        return getDB().update(TBL_PHOTO, values, PhotoColumn.ID + " = ?", new String[]{String.valueOf(item.getTblID() )});
    }
    
    public int update_cFlg(String s_id) {
        ContentValues values = new ContentValues();
        values.put(PhotoColumn.C_FLG , 1);
        return getDB().update(TBL_PHOTO, values, PhotoColumn.PID  + " = ?", new String[]{ s_id });
    }
    
    public int delete(long id) {
        return getDB().delete(TBL_PHOTO, PhotoColumn.ID + " = ?", new String[]{String.valueOf(id)});
    }
    public void delete_byDay(String s_num) throws Exception {
    	try
    	{
    		String sql ="delete from " +TBL_PHOTO+ " where ";
    		       sql+= PhotoColumn.CREATE_AT +"< datetime('now', 'utc', '-"+s_num+" days')";
	        getDB().execSQL(sql);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void delete_all() throws Exception {
    	try
    	{
    		String sql ="delete from " +TBL_PHOTO+ ";";
	        getDB().execSQL(sql);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    public Cursor find_tblId( String s_pid) {
    	String sql="select "
    			 + PhotoColumn.ID+" "
    			 +" from photo where "
    			 + PhotoColumn.PID +" = "+ s_pid +  " limit 1;";
    	Cursor c= getDB().rawQuery(sql, null );
    	return c;
    }
    
    public Cursor exists_byId(String s_id) {
        return getDB().query(TBL_PHOTO, null, PhotoColumn.PID + " = ?", new String[]{s_id}, null, null, null);
    }
    
    public Cursor existsFile_byId(String s_id) {
        return getDB().query(TBL_PHOTO, null, PhotoColumn.PID + " = ? AND " + PhotoColumn.NAME + " IS NOT NULL", new String[]{s_id}, null, null, null);
    }
    
    // true:  record ari
    private boolean Is_existId(String s_id) throws Exception{
    	boolean ret=false;
    	try
    	{
    		int i_ct=0;
    		String sql="select count(*) as ct_num from "+TBL_PHOTO + " where "+PhotoColumn.PID+"="+ s_id+";";
    		Cursor c= getDB().rawQuery(sql, null );
	   		if (c.moveToFirst()) {
	   			do {
	   				i_ct       = c.getInt(0);
	   			}while(c.moveToNext());
	   		}
	   		c.close();
	   		if(i_ct  > 0){
	   			ret= true;
	   		}
    	}catch(Exception e){
    		throw e;
    	}
    	return ret;
    } 

    public Cursor find_old_byDay(String s_num) throws Exception {
    	try
    	{
        	String sql = "select "+PhotoColumn.NAME+"," +PhotoColumn.ID +" from "+TBL_PHOTO+" where ";
        	       sql+= PhotoColumn.CREATE_AT +"< datetime('now', 'utc', '-"+s_num+" days')";
        	return  getDB().rawQuery(sql, null );
    	}catch(Exception e){
    		throw e;
    	}
    }
    public Cursor find_showItem(int i_min , int limit) throws Exception {
    	try
    	{
        	String sql = "select "+PhotoColumn.NAME+"," +PhotoColumn.ID+",";
        	       sql += PhotoColumn.PID  +",";
        	       sql += PhotoColumn.S_OWN  +",";
        	       sql += PhotoColumn.S_URL  +",";
        	       sql += PhotoColumn.S_URL_T+",";
        	       sql += PhotoColumn.HREF   +",";
        	       sql += PhotoColumn.S_TITLE;
        	       sql+= " from "+TBL_PHOTO+" where ";
        	       sql+= PhotoColumn.ID +">= "+ String.valueOf(i_min)+ " limit "+ String.valueOf(limit);
        	       
 //Log.d(TAG, "sql="+sql);
        	return  getDB().rawQuery(sql, null );
    	}catch(Exception e){
    		throw e;
    	}
    }
    public Cursor find_minId(int i_min , int limit) throws Exception {
    	try
    	{
        	String sql = "select "+PhotoColumn.NAME+"," +PhotoColumn.ID+",";
        	       sql += PhotoColumn.PID  +",";
        	       sql += PhotoColumn.S_OWN  +",";
        	       sql += PhotoColumn.S_URL  +",";
        	       sql += PhotoColumn.S_URL_T+",";
        	       sql += PhotoColumn.HREF   +",";
        	       sql += PhotoColumn.S_TITLE;
        	       sql+= " from "+TBL_PHOTO+" where ";
        	       sql+= PhotoColumn.ID +"> "+ String.valueOf(i_min)+ " limit "+ String.valueOf(limit);
        	       
 //Log.d(TAG, "sql="+sql);
        	return  getDB().rawQuery(sql, null );
    	}catch(Exception e){
    		throw e;
    	}
    }
    public Cursor find_byDnum(int dNum ) throws Exception {
    	try
    	{
        	String sql = "select "+PhotoColumn.NAME+"," +PhotoColumn.ID+",";
        	       sql += PhotoColumn.PID  +",";
        	       sql += PhotoColumn.S_OWN  +",";
        	       sql += PhotoColumn.S_URL  +",";
        	       sql += PhotoColumn.S_URL_T+",";
        	       sql += PhotoColumn.HREF   +",";
        	       sql += PhotoColumn.S_TITLE;
        	       sql+= " from "+TBL_PHOTO+" where ";
        	       sql+= PhotoColumn.D_NUM +"= "+ String.valueOf(dNum);
//Log.d(TAG, "find_byDnum.sql=" +sql);
        	return  getDB().rawQuery(sql, null );
    	}catch(Exception e){
    		throw e;
    	}
    }
	public Cursor count_CompleteByKbn(int i_kbn) throws Exception {
    	try
    	{
        	String sql = "select count(*) ";
        	       sql+= " from "+TBL_PHOTO+" where ";
        	       //sql+= PhotoColumn.P_KBN+"="+ String.valueOf(i_kbn) + " AND ";
        	       sql+= PhotoColumn.C_FLG +"=1";
 //Log.d(TAG,  "sql="+ sql);
        	return  getDB().rawQuery(sql, null );
    	}catch(Exception e){
    		throw e;
    	}
    }
	public Cursor count_minIdComplete(int i_min ,int i_kbn) throws Exception {
    	try
    	{
        	String sql = "select count(*) ";
        	       sql+= " from "+TBL_PHOTO+" where ";
        	       //sql+= PhotoColumn.P_KBN+"="+ String.valueOf(i_kbn) + " AND ";
        	       sql+= PhotoColumn.C_FLG +"=1 AND ";
        	       sql+= PhotoColumn.ID +"> "+ String.valueOf(i_min);
 //Log.d(TAG,  "sql="+ sql);
        	return  getDB().rawQuery(sql, null );
    	}catch(Exception e){
    		throw e;
    	}
    }
    public Cursor count_minId(int i_min ) throws Exception {
    	try
    	{
        	String sql = "select count(*) ";
        	       sql+= " from "+TBL_PHOTO+" where ";
        	       sql+= PhotoColumn.ID +"> "+ String.valueOf(i_min);
        	return  getDB().rawQuery(sql, null );
    	}catch(Exception e){
    		throw e;
    	}
    }
    public int count_byPnum(int i_page) throws Exception
    {
    	int ret=0;
    	try
    	{
           String sql = "select count(*) ";
 	       sql+= " from "+TBL_PHOTO+" where ";
 	       sql+= PhotoColumn.P_NUM +"= "+ String.valueOf(i_page);
	   		Cursor c= getDB().rawQuery(sql, null );
	   		if (c.moveToFirst()) {
	   			do {
	   				ret       = c.getInt(0);
	   			}while(c.moveToNext());
	   		}
	   		c.close();
    	}catch(Exception e){
    		throw e;
    	}
    	return ret;
    }
    public Cursor findAll() {
        return getDB().query(TBL_PHOTO,
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
