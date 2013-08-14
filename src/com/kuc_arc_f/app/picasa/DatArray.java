package com.kuc_arc_f.app.picasa;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kuc_arc_f.fw.*;

import android.app.Activity;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatArray {
	private static String TAG="DatArray";
	
	private static final String m_STAT_OK ="ok";
	
	private static String                           m_TYP_PORT_600="";
	private static String                           m_TYP_PORT_800="";
	private static ArrayList<ItemPT>                 mItems= new ArrayList<ItemPT>();
    private static int                              m_Total;
    private static int                              m_TotalPage=0;
    private static int                              m_ColWidth =0;
    private static int                              m_PositonHsv =0;
    private static int                              m_CountAd400 =0;
    
    public static void initialize() throws Exception {
    	try
    	{
    		m_TotalPage =0;
    		mItems= new ArrayList<ItemPT>();
    		m_Total  = 0;
    		m_ColWidth =0;
    		m_TYP_PORT_800="";
    	}catch(Exception e){
    		throw e;
    	}
    }
    public static void destroy() throws Exception {
    	try
    	{
    		mItems.clear();
    		mItems =null;
    	}catch(Exception e){
    		throw e;
    	}
    }

    public static void set_ColWidth(int src){ m_ColWidth=src; }
    public static int get_ColWidth() { return m_ColWidth; }
    
    public static void setCountAd400(int src){ m_CountAd400=src; }
    public static int getCountAd400() { return m_CountAd400; }
    
    public static ArrayList<ItemPT> getItems(){ return mItems; }
    
    public static void setTotal(int src){ m_Total=src; }
    public static int getTotal() { return m_Total; }
    
    public static int  getTotalPage(){return m_TotalPage; }
    public static void setTotalPage(int src){m_TotalPage= src;}
    
    public static int  getPositonHsv(){return m_PositonHsv; }
    public static void setPositonHsv(int src){m_PositonHsv= src;}
    //
    
    public static void   setTYP_PORT_600(String src){m_TYP_PORT_600= src;}
    public static String  getTYP_PORT_600(){ return m_TYP_PORT_600 ;}
    
    public static void    setTYP_PORT_800(String src){m_TYP_PORT_800= src;}
    public static String  getTYP_PORT_800(){ return m_TYP_PORT_800 ;}
}
