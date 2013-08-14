//------------------------------------
// @         : 
// @ purpose : hsv-portrait
// @ date    : 
//------------------------------------
package com.kuc_arc_f.app.picasa;

import android.app.Activity;
import android.app.AlertDialog;
//import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kuc_arc_f.app.picasa.db.ImageCacheDB;
import com.kuc_arc_f.app.picasa.db.ImageCacheDB.CacheColumn;
import com.kuc_arc_f.app.picasa.db.PhotoDB;
import com.kuc_arc_f.app.picasa.network.MultiTheadImageDownloader;
import com.kuc_arc_f.app.picasa.view.RemoteImageView;
import com.kuc_arc_f.fw.*;

public class FBA011Act extends Activity {
	private static final String TAG="FBA011Act";	
	
	private static final int m_WITH_600 = 600;
	private static final int MENU_Item01 =0;
	private static final int MENU_Item02 =1;
	private static final int MENU_Item03 =2;
	private static final int MENU_Item04 =3;
	private static final int MENU_Item05 =4;
	
	private ProgressDialog     mProgressDialog;
	private int               m_ResultCount=0;
	private int m_Page                     =1;
	private int m_TotalPage                =0;
	int          m_SLEEP_1000     =   1000;
	int        m_CountHdl =0;
	static int mOnePage_rec= 8;
    private static final int LAUNCHED_ACTIVITY = 1;
	
	private String STR_DIR = "";
	private String m_TM_VAL="";
	private String m_SC_KEY="";
	private String m_STR_TYP_URL ="";
	private String m_PERSON        ="";
    String mRet = "";
    
    private ArrayList<ItemPT>     m_LIST = new ArrayList<ItemPT>();
    
    Button   m_BTN_nxt;
	private AsyncTask<Void, Void, Void> mTask;
	final String CATEGORIES[] = { "Search", "My-Photo", "Slide-show", "Setting" ,"About" };
	
	private com.kuc_arc_f.fw.HttpUtil m_Http     = new HttpUtil();
	private com.kuc_arc_f.fw.ComUtil  m_Util     = new ComUtil();
	private com.kuc_arc_f.fw.AppConst  m_Const   = new AppConst();
	private com.kuc_arc_f.fw.ComFunc2   m_Func2  = new ComFunc2();
   
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fba011  );
        try
        {
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
       	
        	init_proc();
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
	@Override
	protected void onResume(){
        super.onResume();
        try
        {
			get_prefs();
			m_Func2.setTM_VAL(m_TM_VAL);
        }catch(Exception e){
        	e.printStackTrace();
        }
    }      
    
    void dest_proc() throws Exception{
    	try
    	{
    		PhotoDB db = PhotoDB.getInstance(this);
    		db.delete_all();
			m_Func2.com_deleteDb_day(this, m_Const.STR_FM006_Del_01 );
			m_Func2.com_deleteShowDB(this , m_Const.STR_FM006_Del_01);
    	}catch(Exception e){
    		throw e;
    	}
    }
    
    private void init_proc() throws Exception{
    	try
    	{
    		DatArray.initialize();
    		DatArray.setTYP_PORT_800(m_Const.NG_CODE);
    		DatArray.setTYP_PORT_600(m_Const.NG_CODE);
			WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			int width = display.getWidth();
			Point size = new Point();
			m_Util.overrideGetSize(display, size);
			float f_wPx= m_Util.get_widthByDensity(display, this.getResources());
			int   i_wPx = (int)f_wPx;
			
    		if(f_wPx >= m_Const.NUM_DIP800 ){
				DatArray.setTYP_PORT_800(m_Const.OK_CODE);
				mOnePage_rec = m_Const.NUM_REC_12;
			}

    		int i_w = width / m_Const.NUM_FM001_ColMX;

    		DatArray.set_ColWidth(i_w);
    		
    		m_STR_TYP_URL= m_Const.STR_FM001_STAT_Normal;
    		STR_DIR ="/data/data/" + this.getPackageName() + "/files/";
    		m_Page=1;
    		new MtShowTask(this).execute( );
    		
			//Intent intent = new Intent( getApplicationContext(),  SplashAct.class );
			//startActivity(intent);
    	}catch(Exception e){
    		throw e;
    	}
    	
    }
    void delete_scView()
    {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layer);
        linearLayout.removeAllViews();
    }
    
    public void onClick_home(View v){
    	try
    	{
    		m_SC_KEY="";
			m_STR_TYP_URL= m_Const.STR_FM001_STAT_Normal;
			m_LIST.clear();
    		m_Page=1;
    		delete_scView();
    		proc_reload();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    //
    public void onClick_menu(View v){
    }

    void proc_reload() throws Exception
    {
    	try
    	{
    		m_Page=1;
    		PhotoDB db = PhotoDB.getInstance(this);
    		db.delete_all();
    		
    		delete_scView();
            
    		new MtShowTask(this).execute();    			
    	}catch(Exception e)
    	{
    		throw e;
    	}
    }

    private void get_prefs() throws Exception{
    	try
    	{
    		SharedPreferences prefs = Prefs.get( this);
    		m_TM_VAL    = prefs.getString(m_Const.KEY_TM_VAL,  "");
    		m_PERSON  = prefs.getString(m_Const.KEY_USR_NM,  "");
    	}catch(Exception e){
    		throw e;
    	}
    }    
    
    void proc_disp() throws Exception{
    	try
    	{
        	CustomPortlScrollView horizontalScrollView;
            int width  = this.getWindowManager().getDefaultDisplay().getWidth();
    		Resources r = FBA011Act.this.getResources();
    		int i_wpy= (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, m_Const.NUM_FM110_SideWidth, r.getDisplayMetrics());
            
            int height = this.getWindowManager().getDefaultDisplay().getHeight();
            height = height - i_wpy;

			int i_mxPage=m_Const.NUM_FM101_PageMX;;
			if((m_STR_TYP_URL.equals(m_Const.STR_FM001_STAT_SC )== true) ||
					(m_STR_TYP_URL.equals(m_Const.STR_FM001_STAT_MyPgae  )== true))
			{
				if(this.m_TotalPage < 1){
					return;
				}
				i_mxPage = this.m_TotalPage;
				if(this.m_TotalPage > m_Const.NUM_FM101_PageMX){
					i_mxPage = m_Const.NUM_FM101_PageMX;
				}
			}
			if(m_STR_TYP_URL.equals(m_Const.STR_FM001_STAT_MyPgae  )){
				horizontalScrollView = new CustomPortlScrollView(this, i_mxPage , width, height, m_STR_TYP_URL, this.m_PERSON );
			}else{
	            horizontalScrollView = new CustomPortlScrollView(this, i_mxPage , width, height, m_STR_TYP_URL, m_SC_KEY);
			}

            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layer);
            linearLayout.addView(horizontalScrollView);
    	}catch(Exception e){
    		throw e;
    	}
    }
    
    private class MtShowTask extends AsyncTask<Void, Void, Void> {
        protected Context mContext;
        protected ProgressDialog mProgress;

        public MtShowTask(Context context) {
            this.mContext = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress = new ProgressDialog(mContext);
            mProgress.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                	MtShowTask.this.cancel(true);
                }
            });
            mProgress.setMessage(mContext.getString(R.string.now_loading));
            mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgress.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
        	mRet= m_Const.NG_CODE;
        	try
        	{
            	dest_proc();
        	}catch(Exception e){
        		e.printStackTrace();
        	}
            try
            {
            	m_LIST= m_Func2.get_photo(m_Page, m_STR_TYP_URL, m_SC_KEY);
                m_Func2.proc_regPhoto(m_LIST, FBA011Act.this, m_Page);
                ImageCacheDB db= ImageCacheDB.getInstance(mContext);
                db.insert_byTran(m_LIST);
                MultiTheadImageDownloader.execute(mContext, m_LIST );
                
                m_ResultCount = m_LIST.size();
            	mRet= m_Const.OK_CODE;
            }catch(Exception e){
            	e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
        	try
        	{
                mProgress.dismiss();
                if (isCancelled()) {
                    return;
                }

            	if(mRet.equals(m_Const.OK_CODE)){
                	m_CountHdl=0;
        			Handler hdl = new Handler();
        			hdl.postDelayed(new checkHandler(), m_Const.NUM_TIME_THREAD_500 );
            	}else{
        			String s_msg= FBA011Act.this.getResources().getString(R.string.err_msg_01);
        			m_Util.errorDialog(FBA011Act.this, s_msg);            		
            	}
                if (isCancelled()) {
                    return;
                }
                super.onPostExecute(null);
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        }
    }
        
	class checkHandler implements Runnable {
		public void run() {
	    	try
	    	{
	    		int i_ct =0;
				if(m_CountHdl <  m_Const.NUM_MAX_THREAD_COUNT ){
	        		i_ct= m_Func2.get_dlNum_ById ( FBA011Act.this, 0, 0 );
//Log.d(TAG, "checkHandler.i_ct="+ String.valueOf(i_ct) + " , m_count="+ String.valueOf(m_CountHdl));
	        		if(i_ct <  mOnePage_rec){
	        			m_CountHdl ++;
		    			Handler hdl = new Handler();
		    			hdl.postDelayed(new checkHandler(), m_Const.NUM_TIME_THREAD_500 );
	        		}else{
		    	        proc_disp();					
	        		}
				}
				else{
	    	        proc_disp();					
				}
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}			
		}
	}
	
    
}