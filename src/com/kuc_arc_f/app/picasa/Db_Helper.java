package com.kuc_arc_f.app.picasa;

import java.util.ArrayList;


import android.content.Context;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
public class Db_Helper extends SQLiteOpenHelper {
    static final String TAG = "Db_Helper";
    
    private SQLiteDatabase mDB;
    public static Db_Helper instance;
    
        static final String DB="pica.db";
        
        static final int DB_VERSION= 2;

        static final String CREATE_TABLE =
            "create table tr_photo (" +
            "_id      integer primary key autoincrement, " +
            " s_id    text    null," +
            " s_title text    null," +
            " s_own   text    null," +
            " s_url   text    null," +
            " s_fnm   text    null," +
            " p_num   integer null," +
            " valid   integer null," +
            " create_at text null" +
            " );";
        static final String CREATE_TABLE_02 =
                "create table ms_key (" +
                "_id integer primary key autoincrement, " +
                " s_key text null," +
                " k_typ text null," +
                " s_uid text null," +
                " create_at text null" +
                " );";
        static final String DROP_TABLE ="drop table tr_photo;";
        static final String DROP_TABLE_02 ="drop table ms_key;";        

        public Db_Helper(Context c) {
            super(c, DB, null, DB_VERSION);
        }
        public static Db_Helper getInstance(Context context) {
            if (instance == null) {
                instance = new Db_Helper(context);
            }
            return instance;
        }
        synchronized public SQLiteDatabase getDB() {
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
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
            db.execSQL(CREATE_TABLE_02 );
        }
        public void onUpgrade(SQLiteDatabase db, 
                              int oldVersion, int newVersion) {
Log.w(TAG, "Version mismatch :" + oldVersion + " to " + newVersion );
			try
			{
			    db.execSQL(DROP_TABLE);
			    db.execSQL(DROP_TABLE_02 );
			}catch(Exception e){
				e.printStackTrace();
			}
            onCreate(db);
        }
        //find_nextOne
        
        @Override
        protected void finalize() throws Throwable {
            if (mDB != null) {
                mDB.close();
            }
            this.close();
            super.finalize();
        }
        
}

