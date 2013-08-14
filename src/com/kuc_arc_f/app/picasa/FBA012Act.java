//------------------------------------
// @         : 
// @ purpose : Show
// @ date    : 
//------------------------------------
package com.kuc_arc_f.app.picasa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kuc_arc_f.app.picasa.db.PhotoDB;
import com.kuc_arc_f.app.picasa.db.ImageCacheDB.CacheColumn;
import com.kuc_arc_f.fw.*;

public class FBA012Act extends Activity {
	private static final String TAG="FBA012Act";
	
	private String m_ID ="";
	private String m_URL   ="";
	private String m_TITLE ="";
	private String STR_DIR="";
	String  mStrTotal ="";
	String m_STR_TYP_URL="";
	String m_SC_KEY     ="";
 	String m_DispSlide="";

    int  mOnePage_rec = 20;
	boolean bDispInfo = false;
	boolean mLAND_1280_800 = false;
	int     mLAND_width1200= 1200;
	int     mLAND_height740=  740;
	int     mPORT_width800  = 800;
	
	private ArrayList<ItemPT>     m_LIST = new ArrayList<ItemPT>();

	private  View mViewInfo;
	
	private com.kuc_arc_f.fw.HttpUtil m_Http  = new HttpUtil();
	private com.kuc_arc_f.fw.ComUtil  m_Util  = new ComUtil();
	private com.kuc_arc_f.fw.AppConst  m_Const = new AppConst();	
	private com.kuc_arc_f.fw.ComFunc2  m_Func2 = new ComFunc2();	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
        	requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.fba012 );
        	m_ID          = getIntent().getStringExtra( m_Const.STR_FM001_PT_ID );
            m_URL         = getIntent().getStringExtra( m_Const.STR_FM001_PT_URL );
            m_TITLE       =getIntent().getStringExtra( m_Const.STR_FM001_PT_TITLE );
            mStrTotal     = getIntent().getStringExtra( m_Const.STR_FM001_PT_Total );
            m_STR_TYP_URL = getIntent().getStringExtra( m_Const.STR_FM001_SCR_TYP );
            m_SC_KEY      = getIntent().getStringExtra(m_Const.STR_FM001_PT_SKEY );
            m_DispSlide   = getIntent().getStringExtra(m_Const.STR_FM110_SLIDE );
           
        	if(m_URL != null){
            	proc_dispView();
            	init_proc();
        	}
        	
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
    

    boolean Is_phoneSize(int i_num) throws Exception
    {
    	boolean ret=false;
    	try
    	{
        	WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
    		Display display = wm.getDefaultDisplay();
    		Point size = m_Util.get_pointByDensity(display, this.getResources() );
    		if(size.x <= i_num ){
    			ret= true;
    		}
    	}catch(Exception e){
    		throw e;
    	}
    	return ret;
    }

    boolean Is_land1280() throws Exception{
    	boolean ret=false;
    	try
    	{
        	WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
    		Display display = wm.getDefaultDisplay();
    		int width = display.getWidth();
    		int heigth = display.getHeight();

    		Resources r = this.getResources();
    		int i_w= (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mPORT_width800, r.getDisplayMetrics());
    		float f_den= r.getDisplayMetrics().density;
    		float f_wPy=(float)heigth / f_den;
    		if( width>=mLAND_width1200){
        		if( f_wPy >= m_Const.NUM_DIP600 ){
    				ret= true;
    			}
    		}

    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return ret;
    }
    
	public void onClick_slide(View v){
    }
	
	public void onClick_share(View v){
		try
		{
			int i_pos= DatArray.getPositonHsv();
			ItemPT item = m_LIST.get(i_pos);
			String s_href= item.getHref().toString();
//Log.d(TAG, "onClick_share.s_href=" + s_href);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, s_href );
            startActivity(intent);
		}catch(Exception e){
			e.printStackTrace();
		}
    }
	
    private void init_proc() throws Exception{
    	try
    	{
			int i_pid = get_tableId(m_ID);
    		m_LIST      = get_dbItems(i_pid, 0);
    		STR_DIR ="/data/data/" + this.getPackageName() + "/files/";

    		proc_dispHsv();
    	}catch(Exception e){
    		throw e;
    	}
    }
    
    
    int get_tableId(String s_pid) throws Exception
    {
    	int ret=0;
    	try
    	{
    		PhotoDB db = PhotoDB.getInstance(this);
   		    final Cursor c = db.find_tblId(s_pid);
   			if (c.moveToFirst()) {
                   do {
                   	final long l_id       = c.getLong(c.getColumnIndex(CacheColumn.ID));
       				ret=(int)l_id;
                   } while(c.moveToNext());
   			}
   			c.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
			
		return ret;
    }
    
    private void get_tumb() throws Exception{
		try
		{
			String s_fnm_dir = STR_DIR + m_ID + m_Const.EXT_JPG;
			if(m_Util.com_chkImageList(s_fnm_dir )== true){
				Bitmap bmp = m_Util.com_readBmp(s_fnm_dir);
				//m_Image.setImageBitmap(bmp);
			}
		} catch (Exception e) {
			throw e;
		}
    }
    
	 ArrayList<ItemPT> get_dbItems(int i_min, int i_page) throws Exception{
		 ArrayList<ItemPT> ret= new ArrayList<ItemPT>();
		 try
		 {
			 PhotoDB db = PhotoDB.getInstance(this);
			 final Cursor c = db.find_showItem(i_min , mOnePage_rec);
 			if (c.moveToFirst()) {
                do {
    				ItemPT item = m_Func2.set_itemPT(c, i_page);
    				ret.add(item);
                } while(c.moveToNext());
			}
			c.close();
		 }catch(Exception e){
			 throw e;
		 }
		 return ret;
	 }
	
	void proc_dispHsv() throws Exception
	{
    	try
    	{
            int width  = this.getWindowManager().getDefaultDisplay().getWidth();
            int height = this.getWindowManager().getDefaultDisplay().getHeight();
			int i_mxPage = this.m_LIST.size();
			ShowH_ScrollView horizontalScrollView
			= new ShowH_ScrollView(this, i_mxPage , width, height,  m_LIST ,STR_DIR, mViewInfo );

            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layer);
            linearLayout.addView(horizontalScrollView);

    	}catch(Exception e){
    		throw e;
    	}		
	}
	
    public void proc_dispView() throws Exception
    {
    	try
    	{
    		mViewInfo = this.getLayoutInflater().inflate(R.layout.theme2, null);
    		addContentView(mViewInfo, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));   
    		LinearLayout lay_body = (LinearLayout)mViewInfo.findViewById(R.id.Lay_theme );
    		lay_body.setBackgroundColor(Color.argb(128, 0, 0, 0));
    		TextView t_tit = (TextView)mViewInfo.findViewById(R.id.lbl_title );
    		t_tit.setText( m_TITLE );
    		
    		ImageButton btn_psn= (ImageButton)mViewInfo.findViewById(R.id.btn_slide);
			btn_psn.setVisibility(View.INVISIBLE);
			if(m_DispSlide !=null){
				if(m_DispSlide.equals(m_Const.OK_CODE)){
					btn_psn.setVisibility(View.VISIBLE);
				}
			}
			
            mViewInfo.setVisibility(View.INVISIBLE);
    	}catch(Exception e)
    	{
    		 throw e;
    	}
    }

    
}