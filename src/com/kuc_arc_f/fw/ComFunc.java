package com.kuc_arc_f.fw;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

public class ComFunc {
	private static final String TAG="ComFunc";
	

    private Activity m_Activity;
    private int m_LastID=0;
    private int m_UP_FLG=0;  //1=update
    
    private static com.kuc_arc_f.fw.AppConst m_Const = new AppConst();
	private com.kuc_arc_f.fw.ComUtil m_Util = new ComUtil();
	private com.kuc_arc_f.fw.HttpUtil m_Http = new HttpUtil();
	private com.kuc_arc_f.fw.StringUtil m_String = new StringUtil();
    //
    public ComFunc(Activity ac){
    	m_Activity = ac;
    }
	//




}
