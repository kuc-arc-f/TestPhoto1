//共通

package com.kuc_arc_f.fw;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.TypedValue;
import android.widget.SimpleCursorAdapter;

//import com.kuc_arc_f.app.flick.DatArray;
import com.kuc_arc_f.app.picasa.ItemPT;
import com.kuc_arc_f.app.picasa.Prefs;
import com.kuc_arc_f.app.picasa.db.ImageCacheDB;
import com.kuc_arc_f.app.picasa.db.PhotoDB;
import com.kuc_arc_f.app.picasa.db.ImageCacheDB.CacheColumn;
import com.kuc_arc_f.app.picasa.db.PhotoDB.PhotoColumn;
import com.kuc_arc_f.app.picasa.db.ShowCacheDB;

public class ComFunc2 {
	private static final String TAG="ComFunc2";
	
	static String mDir912 ="/s912/";
    private Activity m_Activity;
    
    private static String m_TM_VAL   ="";
    private static String m_TYP_NEXT ="";
    private static int m_TotalPage= 0;
    
    private static com.kuc_arc_f.fw.AppConst m_Const = new AppConst();
	private com.kuc_arc_f.fw.ComUtil m_Util = new ComUtil();
	private com.kuc_arc_f.fw.HttpUtil m_Http = new HttpUtil();
	private com.kuc_arc_f.fw.StringUtil m_String = new StringUtil();
    //
    public String com_get_strUrl(String s_farm, String s_server, String s_id, String s_secret, String s_size) throws Exception{
    	String ret="";
    	
    	ret="http://farm" + s_farm +".static.flickr.com/" + s_server +"/" + s_id + "_"+ s_secret+ "_" + s_size+".jpg" ;
    	
    	return ret;
    }
    //
    public void show_timerSet(final Activity ac, final String s_val) throws Exception
    {
    	try
    	{
    		m_TM_VAL = s_val;
//    		String s_min ="min";
    		String s_30 = ac.getResources().getString( com.kuc_arc_f.app.picasa.R.string.timer_30);
    		String s_60 = ac.getResources().getString( com.kuc_arc_f.app.picasa.R.string.timer_60);
    		String s_120 = ac.getResources().getString( com.kuc_arc_f.app.picasa.R.string.timer_120);
    		String s_300 = ac.getResources().getString( com.kuc_arc_f.app.picasa.R.string.timer_300);
    		
            int def_index =2;
            if(s_val.length() >0){
            	if(s_val.equals( String.valueOf(m_Const.NUM_FM001_TM030)) ==true  ){
            		def_index =0;
            	}
            	else
            	if(  s_val.equals( String.valueOf(m_Const.NUM_FM001_TM060)) ==true  ){
            		def_index =1;
            	}
            	if(s_val.equals( String.valueOf( m_Const.NUM_FM001_TM120 )) ==true){
            		def_index =2;
            	}
            	else 
            	if(  s_val.equals( String.valueOf(m_Const.NUM_FM001_TM300)) ==true  ){
            		def_index =3;
            	}
            }
    	    final String[] str_items = { s_30 , s_60, s_120, s_300 };
    	    final String[] num_items = { String.valueOf( m_Const.NUM_FM001_TM030), String.valueOf( m_Const.NUM_FM001_TM060) , String.valueOf( m_Const.NUM_FM001_TM120) , String.valueOf( m_Const.NUM_FM001_TM300)};
        new AlertDialog.Builder( ac)
        .setTitle("Timer Setting (min)")
        .setSingleChoiceItems(str_items, def_index,  new DialogInterface.OnClickListener(){
        	public void onClick(DialogInterface dialog, int which) {
        		m_TM_VAL = num_items[which];
//Log.d(TAG,  "show_timerSet.m_TM_VAL="+ m_TM_VAL);
                dialog.dismiss();
                //
                SharedPreferences prefs = Prefs.get( ac );
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(m_Const.KEY_TM_VAL, m_TM_VAL );
                editor.commit();                
        	}
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
        	}
        })
        .show();
    	}catch(Exception e){ throw  e; }
    }
    //
    public void show_typNext(final Activity ac, final String s_val) throws Exception
    {
    	try
    	{
    		m_TYP_NEXT = s_val;
    		String s_normal = ac.getResources().getString( com.kuc_arc_f.app.picasa.R.string.fm003_nxt_01);
    		String s_rand   = ac.getResources().getString( com.kuc_arc_f.app.picasa.R.string.fm003_nxt_02 );
    		String s_tit    = ac.getResources().getString( com.kuc_arc_f.app.picasa.R.string.fm003_nxt_tit );
    		
            int def_index =0;
            if(s_val.length() >0){
            	if(s_val.equals( m_Const.OK_CODE) ==true  ){
            		def_index =1;
            	}
            }
    	    final String[] str_items = { s_normal , s_rand };
    	    final String[] num_items = { m_Const.NG_CODE ,  m_Const.OK_CODE };
        new AlertDialog.Builder( ac)
        .setTitle( s_tit )
        .setSingleChoiceItems(str_items, def_index,  new DialogInterface.OnClickListener(){
        	public void onClick(DialogInterface dialog, int which) {
        		m_TYP_NEXT = num_items[which];
                dialog.dismiss();
                //
                SharedPreferences prefs = Prefs.get( ac );
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(m_Const.KEY_TYP_NEXT , m_TYP_NEXT );
                editor.commit();                
        	}
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
        	}
        })
        .show();
    	}catch(Exception e){ throw  e; }
    } 
    //
    public ArrayList<ItemPT> get_photoMy(String s_uid ,int i_page ) throws Exception{
    	ArrayList<ItemPT> ret = new ArrayList<ItemPT>();
 		try
 		{
 			int i_st =  ((i_page -1) * m_Const.NUM_FM001_PAGE_One) +1;
 			String s_page="&start-index="+ String.valueOf(i_st) +"&max-results="+String.valueOf(m_Const.NUM_FM001_PAGE_One);
 			String url= m_Const.URL_PERSON +s_uid +"?kind=photo&alt=json&imgmax=912"+s_page;
 //String url= m_Const.URL_PERSON +s_uid +"?kind=photo&alt=json"+s_page;
 			String s = m_Http.getHttp_byParam(url);
 			JSONObject jsons = new JSONObject( s );
 			JSONObject jsonObj = jsons.getJSONObject("feed");	
			JSONObject o_tota  = jsonObj.getJSONObject("openSearch$totalResults");
			String s_tota = o_tota.getString("$t");
 			if(i_page==1){
				if(s_tota !=null){
					if(s_tota.length()  >0){
						try{
							int i_tota= Integer.parseInt(s_tota);
							m_TotalPage= com_getTotal(i_tota);
						}catch(Exception e){}
					}
				}
 			}
 			JSONArray dim       = jsonObj.getJSONArray( "entry");
 //Log.d(TAG, "dim.length="+ String.valueOf(dim.length()));
 			for(int j =0; j< dim.length(); j++)
 			{
				ItemPT item = new ItemPT();
// Log.d(TAG, "j="  + String.valueOf(j) );
 				JSONObject obj = dim.getJSONObject(j);	
 				JSONObject o_id  = obj.getJSONObject("gphoto$id");
 				String s_id = o_id.getString("$t");
//Log.d(TAG, "s_id=" +s_id);		
//			JSONObject o_al_id  = obj.getJSONObject("gphoto$albumid");
//			String s_al_id = o_al_id.getString("$t");
//Log.d(TAG, "s_Alubumid=" +s_al_id);		
 				JSONObject o_tit = obj.getJSONObject("title");
 				String s_tit     = o_tit.getString("$t");
//Log.d(TAG, "s_tit=" + s_tit );
 				JSONObject o_cont = obj.getJSONObject("content");
 				String s_cont     = o_cont.getString("src");
 //Log.d(TAG, "s_cont=" + s_cont );
 				JSONObject o_mediaGrg = obj.getJSONObject("media$group");
 				JSONArray d_thub      = o_mediaGrg.getJSONArray( "media$thumbnail");
 				String s_thub_url="";
 				for(int k=0; k < d_thub.length(); k++){
 					JSONObject o_thub_url = d_thub.getJSONObject(k);	
 					s_thub_url = o_thub_url.getString("url");
 //Log.d(TAG, "s_thub_url=" +s_thub_url);
 				}
				String s_href="";
				JSONArray d_link      = obj.getJSONArray( "link");
				for(int m=0; m < d_link.length(); m++){
					if(m==2){
						JSONObject o_link = d_link.getJSONObject(m);
						s_href            = o_link.getString("href");
					}
				}
 //Log.d(TAG, "get_photo.s_href=" + s_href);
				item.setId(s_id);
				item.setTitle(s_tit);
				item.setUrl_img(s_cont);
				item.setUrl_img_t(s_thub_url);
				item.setHref(s_href );
				
				ret.add(item);
 			}
 		} catch (Exception e) {
 			throw e;
 		}
 		return ret;
     }
    public ArrayList<ItemPT> get_photo(int i_page, String s_typUrl, String s_scKey) throws Exception{
    	ArrayList<ItemPT>     lst_pt = new ArrayList<ItemPT>();
		try
		{
			int i_st =  ((i_page -1) * m_Const.NUM_FM001_PAGE_One) +1;
			String url = m_Const.URL_LIST + "&start-index="+String.valueOf(i_st)+"&max-results="+String.valueOf(m_Const.NUM_FM001_PAGE_One);
			if(s_typUrl.equals( m_Const.STR_FM001_STAT_SC)==true){
				 s_scKey= URLEncoder.encode(s_scKey, "UTF-8");
				 url = m_Const.URL_SC +"&q=" + s_scKey+ "&start-index="+String.valueOf(i_st)+"&max-results="+String.valueOf(m_Const.NUM_FM001_PAGE_One);
			}
//Log.d(TAG, "url=" + url);
 
			String s = m_Http.getHttp_byParam(url);
			JSONObject jsons = new JSONObject( s );
			JSONObject jsonObj = jsons.getJSONObject("feed");
			JSONObject o_tota  = jsonObj.getJSONObject("openSearch$totalResults");
			String s_tota = o_tota.getString("$t");
			if(i_page==1){
//Log.d(TAG, "s_tota="+  s_tota);
				if(s_tota !=null){
					if(s_tota.length()  >0){
						try{
							int i_tota= Integer.parseInt(s_tota);
							m_TotalPage=  com_getTotal(i_tota);
//Log.d(TAG,  "m_TotalPage=" +String.valueOf(m_TotalPage));
						}catch(Exception e){}
					}
				}
			}			
			JSONArray dim       = jsonObj.getJSONArray( "entry");
			int i_ct= 1;
			for(int j =0; j< dim.length(); j++)
			{
				ItemPT item = new ItemPT();
//Log.d(TAG, "j="  + String.valueOf(j) );
				String s_id="";
				String s_tit="";
				String s_cont="";
				String s_thub_url="";
				String s_href="";
				try
				{
					JSONObject obj = dim.getJSONObject(j);	
					JSONObject o_id  = obj.getJSONObject("gphoto$id");				
					s_id = o_id.getString("$t");
	//Log.d(TAG, s_id);
					JSONObject o_tit = obj.getJSONObject("title");
					s_tit     = o_tit.getString("$t");
	//Log.d(TAG, "s_tit=" + s_tit );
					JSONObject o_cont = obj.getJSONObject("content");
					s_cont     = o_cont.getString("src");
 //Log.d(TAG, "s_cont=" + s_cont );
					JSONObject o_mediaGrg = obj.getJSONObject("media$group");
					JSONArray d_thub      = o_mediaGrg.getJSONArray( "media$thumbnail");
					s_thub_url="";
					for(int k=0; k < d_thub.length(); k++){
						JSONObject o_thub_url = d_thub.getJSONObject(k);	
							s_thub_url = o_thub_url.getString("url");
					}
					
					JSONArray d_link      = obj.getJSONArray( "link");
					for(int m=0; m < d_link.length(); m++){
						if(m==2){
							JSONObject o_link = d_link.getJSONObject(m);
							s_href            = o_link.getString("href");
						}
					}
				}catch(Exception e){
				}
 //Log.d(TAG, "get_photo.s_href=" + s_href);
				
				item.setId(s_id);
				item.setTitle(s_tit);
				item.setUrl_img(s_cont);
				//item.setUrl_img_t(s_thub_url);
				item.setPnum(i_page);
				item.setHref(s_href);
				
				if(m_String.Is_nullOrEmpty( s_thub_url)==false){
					if(s_cont.indexOf(mDir912) >=0 ){
						s_thub_url = s_cont.replaceAll(mDir912 , "/s144/" );
					}
					else
					{
 //Log.d(TAG, "mDir912 is nothing, s_cont="+ s_cont);
					}
				}
//Log.d(TAG, "s_thub_url=" +s_thub_url);
				item.setUrl_img_t(s_thub_url);
				if(m_String.Is_nullOrEmpty( s_thub_url)==true){
					lst_pt.add(item);
				}
				
				i_ct++;
			}
		} catch (Exception e) {
			throw e;
		}
		return lst_pt;
    }	

    public void proc_regPhoto(ArrayList<ItemPT> items, final Context cont, int i_page){
    	PhotoDB db = PhotoDB.getInstance(cont);

    	for(int i=0; i< items.size() ; i++){
    		ItemPT item = items.get(i);
    		item.setPnum(i_page);
    		db.insert_byItem(item);
    	}
    }
	 public int get_dlNum_ById(Context con  , int i_min, int i_kbn) throws Exception
	 {
		 int ret=0;
		 try
		 {
			 PhotoDB db= PhotoDB.getInstance( con );
			 final Cursor c = db.count_minIdComplete(i_min, i_kbn);
			if (c.moveToFirst()) {
              do {
  				ret       = c.getInt(0);
              } while(c.moveToNext());
			}
			c.close();
		 }catch(Exception e){
			 throw e;
		 }
		 return ret;
	 }

    public int com_getTotal(int i_num) throws Exception{
    	int ret=0;
    	try
    	{
    		int i_div = i_num / m_Const.NUM_FM001_PAGE_One;
    		int i_d2  = i_num % m_Const.NUM_FM001_PAGE_One;
    		if( i_d2 > 0 ){
    			i_div =i_div + 1;
    		}
    		ret= i_div;
    	}catch(Exception e){
    		throw e;
    	}
    	return ret;
    }

    public void com_deleteDb_day(final Activity ac, String s_num) throws Exception{
    	try
    	{
    		if(s_num.length() >0){
    			ImageCacheDB db = ImageCacheDB.getInstance( ac);
    			final Cursor c = db.find_old_byDay(s_num);
    			if (c.moveToFirst()) {
                    do {
        				final String filename = c.getString(c.getColumnIndex(CacheColumn.NAME));
        				final long l_id       = c.getLong(c.getColumnIndex(CacheColumn.ID));
//Log.d(TAG, "filename="+filename);
    					try
    					{
    						ac.deleteFile(filename);
    					}catch(Exception e){
    						e.printStackTrace();
    					}
                    } while(c.moveToNext());
    			}
    			c.close();
    			
    			db.delete_byDay(s_num);
    		}
    	}catch(Exception e){
    		throw e;
    	}
    }
    public void com_deleteShowDB(final Activity ac, String s_num) throws Exception{
    	try
    	{
    		if(s_num.length() >0){
    			ShowCacheDB db = ShowCacheDB.getInstance( ac);
    			final Cursor c = db.find_old_byDay(s_num);
    			if (c.moveToFirst()) {
                    do {
        				final String filename = c.getString(c.getColumnIndex(CacheColumn.NAME));
//Log.d(TAG, "filename="+filename);
    					try
    					{
    						ac.deleteFile(filename);
    					}catch(Exception e){
    						e.printStackTrace();
    					}
                    } while(c.moveToNext());
    			}
    			c.close();
    			
    			db.delete_byDay(s_num);
    		}
    	}catch(Exception e){
    		throw e;
    	}
    }

	 public int get_dlNum_ByKbn(Context con  , int i_kbn) throws Exception
	 {
		 int ret=0;
		 try
		 {
			 PhotoDB db= PhotoDB.getInstance( con );
			 final Cursor c = db.count_CompleteByKbn(i_kbn);
			if (c.moveToFirst()) {
              do {
  				ret       = c.getInt(0);
              } while(c.moveToNext());
			}
			c.close();
		 }catch(Exception e){
			 throw e;
		 }
		 return ret;
	 }
    public ItemPT set_itemPT(Cursor c, int i_page) throws Exception{
		 ItemPT item= new ItemPT();
		 try
		 {
				final long l_id       = c.getLong(c.getColumnIndex(CacheColumn.ID));
				final String pid      = c.getString(c.getColumnIndex(PhotoColumn.PID));
				final String s_url_t   = c.getString(c.getColumnIndex(PhotoColumn.S_URL_T));
				final String s_url     = c.getString(c.getColumnIndex(PhotoColumn.S_URL ));
				final String s_tit     = c.getString(c.getColumnIndex(PhotoColumn.S_TITLE  ));
				final String s_href    = c.getString(c.getColumnIndex(PhotoColumn.HREF  ));
 //Log.d(TAG, "filename="+filename);
				item.setTblID(l_id);
				item.setId(pid);
				item.setUrl_img(s_url);
				item.setUrl_img_t(s_url_t);
				item.setTitle(s_tit);
				item.setDnum(i_page);
				item.setHref(s_href);
		 }catch(Exception e)
		 {
			 throw e;
		 }
		 return item;    	
    }
    //
    public int getPixels(final Activity ac, int dipValue){
        Resources r = ac.getResources();
        int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
        return px;
	}
    //
    public static String getTM_VAL(){ return m_TM_VAL; };
    public static void   setTM_VAL(String src){ m_TM_VAL = src; };    
    
    public static String getTYP_NEXT(){ return m_TYP_NEXT; };
    public static void   setTYP_NEXT(String src){ m_TYP_NEXT = src; };    

    public static int  getTotalPage(){ return m_TotalPage; };
    public static void setTotalPage(int src){ m_TotalPage= src; };

}
