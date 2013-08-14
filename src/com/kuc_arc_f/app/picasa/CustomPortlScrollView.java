package com.kuc_arc_f.app.picasa;

import java.util.ArrayList;

import com.kuc_arc_f.app.picasa.db.ImageCacheDB;
import com.kuc_arc_f.app.picasa.db.PhotoDB;
import com.kuc_arc_f.app.picasa.db.ImageCacheDB.CacheColumn;
import com.kuc_arc_f.app.picasa.db.PhotoDB.PhotoColumn;
import com.kuc_arc_f.app.picasa.network.MultiTheadImageDownloader;
import com.kuc_arc_f.app.picasa.view.RemoteImageView;
import com.kuc_arc_f.fw.AppConst;
import com.kuc_arc_f.fw.ComFunc2;

import android.app.AlertDialog;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
	
 public class CustomPortlScrollView extends HorizontalScrollView implements OnTouchListener, OnGestureListener 
 {
	 static final String TAG="CustomPortlScrollView";
	 private static final int SWIPE_MIN_DISTANCE       = 300;
	 private static final int SWIPE_THRESHOLD_VELOCITY = 300;
	 private static final int SWIPE_PAGE_ON_FACTOR     = 10;
      static int mOnePage_rec = 8;
//      int mColMax      =3;
      int mColMax      =2;
      int mColHeight   =4;
      int m_CountHdl =0;
      
	 private GestureDetector gestureDetector;
	 private int scrollTo    = 0;
	 private int maxItem     = 0;
	 private int activeItem  = 0;
	 private float prevScrollX = 0;
	 private boolean start     = true;
	 private int itemWidth     = 0;
	 private int itemHeight    = 0;
	 private int mOneWidth     =0;
	 private int mOneHeight    =0;
	 private int mMinId        =0;
	 private int m_Page         =1;
	 private String m_STR_TYP_URL= "";
	 private String m_SC_KEY="";
	 
	 private float currentScrollX;
	 private boolean flingDisable = true;
	 Context mContext;

	 com.kuc_arc_f.fw.AppConst m_Const = new AppConst();
	 com.kuc_arc_f.fw.ComFunc2 m_Func2 = new ComFunc2();
	 //
	 public CustomPortlScrollView(Context context) {
		  super(context);
		  setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		    LayoutParams.FILL_PARENT));
	 }
	
	 public CustomPortlScrollView(Context context, int maxItem,
			   int itemWidth, int itemHeight ,String s_typUrl, String s_scKey) 
	 {
		 this(context);
		 try
		 {
			  this.maxItem = maxItem;
			  this.itemWidth = itemWidth;
			  this.itemHeight =itemHeight;
			  gestureDetector = new GestureDetector(this);
			  this.setOnTouchListener(this);
			  mContext = context;
			  
			  m_STR_TYP_URL =  s_typUrl;
			  m_SC_KEY      =s_scKey;
			  init_proc();
		      loadView(0, true);
		 }catch(Exception e )
		 {
			 e.printStackTrace();
		 }
	 }
	 
	 void deleteView(int i_page) throws Exception
	 {
		 try
		 {
			 if(i_page < 0){
				 return;
			 }
			 if(i_page >= maxItem){
				 return;
			 }
		     LinearLayout llmain = (LinearLayout)this.getChildAt(0);
		     LinearLayout onel = (LinearLayout)llmain.getChildAt(i_page);
		     int i_one = onel.getChildCount();
		     if(i_one < 1){
		    	 return;
		     }
		     onel.removeAllViews();
		 }catch(Exception e){
			 throw e;
		 }
	 }
	 
	 void loadView(int i_page , boolean b_next) throws Exception
	 {
//Log.d(TAG ,"#_loadView");
		 try
		 {
		     LinearLayout llmain = (LinearLayout)this.getChildAt(0);
		     
		     LinearLayout onel = (LinearLayout)llmain.getChildAt(i_page);
		     int i_one = onel.getChildCount();
		     if(i_one < 1){
				  LinearLayout ll_01 = new LinearLayout(mContext);
				  ll_01.setLayoutParams(new LayoutParams(itemWidth ,mOneHeight ));
				  ll_01.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
				  
				  LinearLayout ll_02 = new LinearLayout(mContext);
				  ll_02.setLayoutParams(new LayoutParams(itemWidth ,mOneHeight));
				  ll_02.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
				  
				  LinearLayout ll_03 = new LinearLayout(mContext);
				  ll_03.setLayoutParams(new LayoutParams(itemWidth ,mOneHeight ));
				  ll_03.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
				  
				  LinearLayout ll_04 = new LinearLayout(mContext);
				  ll_04.setLayoutParams(new LayoutParams(itemWidth ,mOneHeight ));
				  ll_04.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
				  
				  onel.addView(ll_01);
				  onel.addView(ll_02);
				  onel.addView(ll_03);
				  onel.addView(ll_04);
		
				  if(b_next==true){
					  m_CountHdl =0;
					//  Log.d(TAG,  "loadView.i_next="+String.valueOf(i_next)+ ",mMinId="+String.valueOf(mMinId));
		    			Handler hdl = new Handler();
		    			hdl.postDelayed(new checkHandler(), m_Const.NUM_TIME_THREAD_500 );

				  }
				  
				  int i_row=0;
				  int ict=0;
				  ArrayList<ItemPT> items= new ArrayList<ItemPT>();
				  if(b_next==true){
					  items= this.get_dbItems(this.mMinId ,i_page);
  //Log.d(TAG,  "loadView.(items.size="+String.valueOf(items.size()) + ",i_page="+String.valueOf(i_page) );
				  }else{
					  items= this.get_dNumPage(i_page);
				  }

				 Resources r = mContext.getResources();
				 int i_px05= (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,  5 , r.getDisplayMetrics());
				 int i_px10= (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,  10, r.getDisplayMetrics());
				 int i_widthOne =mOneWidth -i_px10;
//Log.d(TAG,  "mOneWidth="+String.valueOf(mOneWidth)+ ", i_widthOne=" +String.valueOf(i_widthOne));
		         for(int i=0; i< items.size(); i++){
		        	    final ItemPT item= items.get(i);
			 			FrameLayout ll_frame = new FrameLayout(mContext);
			 			ll_frame.setBackgroundColor( Color.WHITE);
					  	LinearLayout.LayoutParams para_fra =new LinearLayout.LayoutParams(i_widthOne,  mOneHeight-i_px05);
//					  	para_fra.setMargins(i_px05, 0, 0, i_px05);
					  	para_fra.setMargins(i_px05, i_px05, 0, 0);
					  	ll_frame.setLayoutParams(para_fra);
				 		//ll_img.addView(ll_frame );
				 		
				       	PhotoImageView iv = new PhotoImageView(mContext );
				  	  	//LinearLayout.LayoutParams layoutParams =new LinearLayout.LayoutParams(i_widthOne -i_px10,  mOneHeight -i_px10);
				  	    LinearLayout.LayoutParams layoutParams =new LinearLayout.LayoutParams(i_widthOne ,  mOneHeight -i_px05 );
				  	    iv.setPadding(i_px05, i_px05, i_px05, i_px05);
				  	  	iv.setLayoutParams(layoutParams);
				  	  	//LinearLayout.LayoutParams layoutParams =new LinearLayout.LayoutParams(mOneWidth, mOneWidth);
				  	  	//layoutParams.weight=1;
				  	  	//iv.setLayoutParams(layoutParams);
				  	  	iv.setScaleType(ScaleType.CENTER );
				  	  	iv.setImageNowLoading();
						iv.setOnClickListener(new View.OnClickListener(){
				            public void onClick(View v) {
								click_image(item);
				              }
							}
						);
						ll_frame.addView(iv);
						
				        Message msg =  iv.mHandler.obtainMessage(RemoteImageView.IMG_DOWNLOADING,
				                  1, 0, item);
				         msg.sendToTarget();
				        this.mMinId = (int)item.getTblID();
				  	  	if(ict >= this.mColMax){
				  	  	  i_row++;
				  	  	  ict =0;
				  	  	}
				  	    ict++;
				  	  	if(i_row ==0){
//				  	  		ll_01.addView(iv);
				  	  		ll_01.addView(ll_frame);
				  	  		//ll_frame
				  	  	}
				  	  	else if(i_row ==1){
				  	  		ll_02.addView(ll_frame);
				  	  	}
				  	  	else if(i_row ==2){
				  	  		ll_03.addView(ll_frame);
				  	  	}
				  	  	else if(i_row ==3){
				  	  		ll_04.addView(ll_frame);
				  	  	}
				  	  	
		         }
		     }
		 }catch(Exception e)
		 {
			 throw e;
		 }

	 }
	 void click_image(ItemPT item){
		 try
		 {
      	   String s_id= item.getId().toString();
//Log.d(TAG, "click_image.getId=" +item.getId().toString() );
			Intent intent;
	      	String s_url = item.getUrl_img().toString();
	      	String s_tit = item.getTitle().toString();
	      	DatArray.setPositonHsv(0);
	  		intent = new Intent( mContext.getApplicationContext(), FBA012Act.class );
	  		intent.putExtra( m_Const.STR_FM001_PT_ID ,   s_id);
	  		intent.putExtra( m_Const.STR_FM001_PT_URL,   s_url);
	  		intent.putExtra( m_Const.STR_FM001_PT_TITLE, s_tit);
	  		intent.putExtra( m_Const.STR_FM001_PT_Total, String.valueOf(this.maxItem ));
	  		intent.putExtra( m_Const.STR_FM001_SCR_TYP , m_STR_TYP_URL );
	  		intent.putExtra( m_Const.STR_FM001_PT_SKEY , m_SC_KEY );
	  		intent.putExtra( m_Const.STR_FM110_SLIDE ,  m_Const.OK_CODE );
	  		mContext.startActivity(intent);			 
		 }catch(Exception e){
			 e.printStackTrace();
		 }
	 }
	 int get_countById(int i_min) throws Exception
	 {
		 int ret=0;
		 try
		 {
			 PhotoDB db= PhotoDB.getInstance(mContext);
			 final Cursor c = db.count_minId(i_min);
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
	 ArrayList<ItemPT> get_dbItems(int i_min, int i_page) throws Exception{
		 ArrayList<ItemPT> ret= new ArrayList<ItemPT>();
		 try
		 {
			 PhotoDB db = PhotoDB.getInstance(mContext);
			 final Cursor c = db.find_minId(i_min , mOnePage_rec);
 			if (c.moveToFirst()) {
                do {
    				ItemPT item = set_itemPT(c, i_page);
    				ret.add(item);
    				db.update_dNum(item);
                } while(c.moveToNext());
			}
			c.close();
		 }catch(Exception e){
			 throw e;
		 }
		 return ret;
	 }
	 ArrayList<ItemPT> get_dNumPage(int i_page) throws Exception{
		 ArrayList<ItemPT> ret= new ArrayList<ItemPT>();
		 try
		 {
			 PhotoDB db = PhotoDB.getInstance(mContext);
			 final Cursor c = db.find_byDnum(i_page);
 			if (c.moveToFirst()) {
                do {
    				ItemPT item = set_itemPT(c, i_page);
    				ret.add(item);
                } while(c.moveToNext());
			}
			c.close();
		 }catch(Exception e){
			 throw e;
		 }
		 return ret;
	 }	 
	 
	 ItemPT set_itemPT(Cursor c, int i_page) throws Exception{
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
	 
	 void init_proc()
	 {
		 m_Page =1;
		 int i_w=0;
		 String s800 = DatArray.getTYP_PORT_800();
		  if(s800.equals(m_Const.OK_CODE) == true ){
			  mColMax      = 3;
			  mOnePage_rec = m_Const.NUM_REC_12;
			  i_w = itemWidth   / mColMax;
		  }else{
			  i_w = itemWidth   / mColMax;
		  }
		  
		  int i_h = itemHeight  / mColHeight;
		  
		  mOneWidth  = i_w;
		  mOneHeight = i_h;
//Log.d(TAG ,  "mOneWidth=" +String.valueOf(mOneWidth));
	  
		  LinearLayout container = new LinearLayout(mContext);
		  container.setLayoutParams(new LayoutParams(itemWidth, itemHeight));
	
		  for(int i=0; i< maxItem; i++){
			  LinearLayout onel = new LinearLayout(mContext);
			  onel.setLayoutParams(new LayoutParams(itemWidth, itemHeight));
			  onel.setOrientation(LinearLayout.VERTICAL);
			    container.addView( onel );
		  }
		  addView(container);
	 }
 
	 @Override
	 public boolean onTouch(View v, MotionEvent event) {
		 Boolean returnValue = gestureDetector.onTouchEvent(event);
		 try
		 {
//Log.d(TAG , "#_onTouch");
			 
			  if (gestureDetector.onTouchEvent(event)) {
			   return true;
			  }
			
			  int x = (int) event.getRawX();
			
			  switch (event.getAction()) {
			  case MotionEvent.ACTION_MOVE:
			   if (start) {
			    this.prevScrollX = x;
			    start = false;
			   }
			   break;
			  case MotionEvent.ACTION_UP:
			   start = true;
			   this.currentScrollX = x;
			   int minFactor = itemWidth / SWIPE_PAGE_ON_FACTOR;
			
			   if ((this.prevScrollX - this.currentScrollX) > minFactor) {
				    if (activeItem < maxItem - 1){
				        activeItem = activeItem + 1;
				        this.loadView(activeItem, true);
				        this.deleteView(activeItem -1);
				    }
			   } else if ((this.currentScrollX - this.prevScrollX) > minFactor) {
				    if (activeItem > 0){
					     activeItem = activeItem - 1;
					     this.loadView(activeItem, false);
					     this.deleteView(activeItem +1);
				    }
			   }
  System.out.println("horizontal : " + activeItem);
			   scrollTo = activeItem * itemWidth;
			   this.smoothScrollTo(scrollTo, 0);
			   returnValue = true;
			   break;
			  }
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		  return returnValue;			 
	 }

	 @Override
	 public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
	   float velocityY) 
	 {
//Log.d(TAG, "#_onFling");
	
	  if (flingDisable)
	   return false;
	  boolean returnValue = false;
	  
	  float ptx1 = 0, ptx2 = 0;
	  if (e1 == null || e2 == null)
	   return false;
	  ptx1 = e1.getX();
	  ptx2 = e2.getX();
	  // right to left
	
	  if (ptx1 - ptx2 > SWIPE_MIN_DISTANCE
	    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	   if (activeItem < maxItem - 1)
	    activeItem = activeItem + 1;
	
	   returnValue = true;
	
	  } else if (ptx2 - ptx1 > SWIPE_MIN_DISTANCE
	    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	   if (activeItem > 0)
	    activeItem = activeItem - 1;
	
	   returnValue = true;
	  }
	  scrollTo = activeItem * itemWidth;
	
	  this.smoothScrollTo( scrollTo, 0);
	  return returnValue;
	 }
	
	 @Override
	 public boolean onDown(MotionEvent e) {
	  return false;
	 }
	
	 @Override
	 public void onLongPress(MotionEvent e) {
	 }
	
	 @Override
	 public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
	   float distanceY) {
	  return false;
	 }
	
	 @Override
	 public void onShowPress(MotionEvent e) {
	 }
	
	 @Override
	 public boolean onSingleTapUp(MotionEvent e) {
	  return false;
	 }
	 
	 //View
	 public class PhotoImageView extends RemoteImageView {
	
	     public PhotoImageView(Context context) {
	         super(context);
	     }
	     @Override
	     public void setImageNotFound() {
	         setImageResource(R.drawable.ic_delete );
	     }
	     @Override
	     public void setImageNowLoading() {
	         setImageResource(R.drawable.ic_menu_rotate );
	     }
	 }

	    //
	    // Background
	    //
	    private class NextTask extends AsyncTask<Void, Void, Void> {
	        protected Context mContext;

	        public NextTask(Context context) {
	            this.mContext = context;
	        }
	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            m_Page++;
	        }
	        @Override
	        protected Void doInBackground(Void... params) {
	            try
	            {
	            	ArrayList<ItemPT> lst =new ArrayList<ItemPT>();
	            	if(m_STR_TYP_URL.equals(m_Const.STR_FM001_STAT_MyPgae)){
	            		 lst  = m_Func2.get_photoMy( m_SC_KEY , m_Page  );
	            	}else{
		            	 lst =m_Func2.get_photo(m_Page, m_STR_TYP_URL , m_SC_KEY );
	            	}
	            	m_Func2.proc_regPhoto(lst, mContext, m_Page);
	                ImageCacheDB db= ImageCacheDB.getInstance(mContext);
	                db.insert_byTran(lst);
	                
	            	MultiTheadImageDownloader.execute(mContext, lst );
	            }catch(Exception e){
	            	e.printStackTrace();
	            }
	            return null;
	        }
	        @Override
	        protected void onPostExecute(Void result) {
	            super.onPostExecute(null);
	        }
	    }
	    
		class checkHandler implements Runnable {
			public void run() {
		    	try
		    	{
		    		int i_ct =0;
		    		int i_max = m_Page * m_Const.NUM_FM003_PAGE_One;
					if(m_CountHdl <  m_Const.NUM_MAX_THREAD_COUNT ){
		        		i_ct= m_Func2.get_dlNum_ByKbn ( mContext, 0 );
//Log.d(TAG, "HSC.checkHandler.i_ct="+ String.valueOf(i_ct) + " , m_count="+ String.valueOf(m_CountHdl) +", i_max="+String.valueOf(i_max));
		        		if(i_ct <  i_max){
		        			m_CountHdl ++;
			    			Handler hdl = new Handler();
			    			hdl.postDelayed(new checkHandler(), m_Const.NUM_TIME_THREAD_500 );
		        		}else{
//Log.d(TAG, "#NextTask");    	        
							new NextTask(mContext).execute();					
		        		}
					}
					else{
//Log.d(TAG, "#NextTask");    	        
						new NextTask(mContext).execute();					
					}
		    	}catch(Exception e){
		    		e.printStackTrace();
		    	}			
			}
		}
 
}