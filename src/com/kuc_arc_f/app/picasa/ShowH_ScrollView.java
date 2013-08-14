package com.kuc_arc_f.app.picasa;

import java.util.ArrayList;

import com.kuc_arc_f.app.picasa.db.PhotoDB;
//import com.kuc_arc_f.app.picasa.db.ImageCacheDB.CacheColumn;
//import com.kuc_arc_f.app.picasa.db.PhotoDB.PhotoColumn;
import com.kuc_arc_f.app.picasa.network.LargeDownloadTask;
//import com.kuc_arc_f.app.picasa.network.MultiTheadImageDownloader;
import com.kuc_arc_f.app.picasa.view.LargeImageView;
import com.kuc_arc_f.fw.AppConst;
import com.kuc_arc_f.fw.ComFunc2;
import com.kuc_arc_f.fw.ComUtil;
import com.kuc_arc_f.fw.HttpUtil;

import android.app.AlertDialog;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
	
 public class ShowH_ScrollView extends HorizontalScrollView implements OnTouchListener, OnGestureListener 
 {
	 static final String TAG="ShowH_ScrollView";
	 private static final int SWIPE_MIN_DISTANCE       = 300;
	 private static final int SWIPE_THRESHOLD_VELOCITY = 300;
	 private static final int SWIPE_PAGE_ON_FACTOR     = 10;
     int mColMax      =4;
     int mColHeight   =3;
     int mOnePage_rec = 12;
     
     private Handler _handlerAnimation = null;
	 private GestureDetector gestureDetector;
	 private int scrollTo    = 0;
	 private int maxItem     = 0;
	 private int activeItem  = 0;
	 private float prevScrollX = 0;
	 private boolean start     = true;
	 boolean bDispInfo = false;
	 
	 private int itemWidth     = 0;
	 private int itemHeight    = 0;
	 private int mMinId        =0;
	 
	 private String m_STR_TYP_URL= "";
	 private String m_SC_KEY="";
	 String  STR_DIR="";
	 
	 private float currentScrollX;
	 private boolean flingDisable = true;
	 Context         mContext;
	 ImageView       m_ImageView;
	 View mViewInfo;
	 ArrayList<ItemPT> m_LIST =new ArrayList<ItemPT>();
	 ItemPT m_ITEM            =  new ItemPT();
	 
	 com.kuc_arc_f.fw.AppConst m_Const = new AppConst();
	 com.kuc_arc_f.fw.ComUtil  m_Util  = new ComUtil();
	 com.kuc_arc_f.fw.ComFunc2 m_Func2 = new ComFunc2();
	 com.kuc_arc_f.fw.HttpUtil m_Http  = new HttpUtil();
	 
	 private final Runnable _runAnimationThread = new Runnable(){
	     public void run(){
	         updateAutoScroll();
	     }
	 };
	 
	 //
	 public ShowH_ScrollView(Context context) {
		  super(context);
		  setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		    LayoutParams.FILL_PARENT));
	 }
	
	 public ShowH_ScrollView(Context context, int maxItem,
			   int itemWidth, int itemHeight, ArrayList<ItemPT> lst, String s_dir, View info_view) 
	 {
		 this(context);
		 try
		 {
			  mViewInfo = info_view;
			  STR_DIR= s_dir;
			  this.maxItem = maxItem;
			  this.itemWidth = itemWidth;
			  this.itemHeight =itemHeight;
			  gestureDetector = new GestureDetector(this);
			  this.setOnTouchListener(this);
			  mContext = context;
			  m_LIST= lst;
			  
			  init_proc();
			  int i_start= DatArray.getPositonHsv();
			  if(i_start > 0){
			      loadView(i_start, true);
				  this.activeItem = i_start;
			      startAutoScroll();
			  }else{
			      loadView(0, true);
			  }
		 }catch(Exception e )
		 {
			 e.printStackTrace();
		 }
	 }
	 /**
	  * アニメーション用のハンドラ
	  * @return
	  */
	 private Handler getHandlerAnimation(){
	     if(_handlerAnimation == null){
	         _handlerAnimation = new Handler();
	     }
	     return _handlerAnimation;
	 }
	 /**
	  * 自動スクロールを開始する
	  */
	 public void startAutoScroll(){
	     //監視を開始
	     getHandlerAnimation().postDelayed(_runAnimationThread, 100 );
	 }
	 /**
	  * 自動スクロールの状態更新
	  */
	 public void updateAutoScroll(){
		 scrollTo(itemWidth * activeItem , getScrollY());
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
	 ArrayList<ItemPT> get_nextArrays(ArrayList<ItemPT> lst , int i_pos) throws Exception
	 {
		 ArrayList<ItemPT> ret= new  ArrayList<ItemPT>();
		 try
		 {
			 for (int i=0; i <2; i++){
				 int i_ct = i_pos+ i;
 //Log.d(TAG,  "get_nextArrays.i_ct=" +String.valueOf(i_ct) + ", lst.size()="+String.valueOf(lst.size()) + ",i="+ String.valueOf(i) );
				 if(lst.size() > i_ct ){
					  ItemPT item = lst.get(i_ct);
					  ret.add(item);
				 }
			 }
		 }catch(Exception e){
			 throw e;
		 }
		 return ret;
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
				  ArrayList<ItemPT> d_next = get_nextArrays(m_LIST, i_page);
				  new LargeDownloadTask(mContext).execute(d_next);		    	 

		    	  final ItemPT item= m_LIST.get(i_page );
                  
                  PhotoImageView siv= new PhotoImageView(mContext, item, STR_DIR);
 			  	  LinearLayout.LayoutParams layoutParams 
		  	  	   =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
 			  	  siv.setLayoutParams(layoutParams);
 			  	  siv.setScaleType(ScaleType.FIT_CENTER  );
				  siv.setOnClickListener(new View.OnClickListener(){
			            public void onClick(View v) {
							click_image(item);
			              }
					}
				   );
 			  	  Bitmap bmp = get_tumb(item.getId().toString() );
 			  	  siv.setImageBitmap(bmp) ;
				  siv.start_progress();
			      Message msg =  siv.mHandler.obtainMessage(LargeImageView.IMG_DOWNLOADING,
		                  1, 0, item);
		          msg.sendToTarget();
		          
                  onel.addView(siv);
		     }
		 }catch(Exception e)
		 {
			 throw e;
		 }

	 }
	 
	 void click_image(ItemPT item){
		 try
		 {
//Log.d(TAG, "#_click_image");
				if(bDispInfo== false){
					mViewInfo.setVisibility(View.VISIBLE);
		    		TextView t_tit = (TextView)mViewInfo.findViewById(R.id.lbl_title );
		    		t_tit.setText( item.getTitle().toString() );
				    bDispInfo= true;
				}else{
					mViewInfo.setVisibility(View.INVISIBLE);
			        bDispInfo =false;
				}			 
		 }catch(Exception e){
			 e.printStackTrace();
		 }
	 }
	 
	 Bitmap get_tumb(String s_id) throws Exception{
		 Bitmap ret=null;
			try
			{
				String s_fnm_dir = STR_DIR + s_id + m_Const.EXT_JPG;
				if(m_Util.com_chkImageList(s_fnm_dir )== true){
					ret = m_Util.com_readBmp(s_fnm_dir);
				}
			} catch (Exception e) {
				throw e;
			}
			return ret;
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
	 
	 void init_proc()
	 {
		 activeItem =0;
//Log.d(TAG ,  "mOneWidth=" +String.valueOf(mOneWidth));
		  LinearLayout container = new LinearLayout(mContext);
		  container.setLayoutParams(new LayoutParams(itemWidth, itemHeight));
		  container.setOrientation(LinearLayout.HORIZONTAL);
	
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
				        DatArray.setPositonHsv(activeItem);
				        this.loadView(activeItem, true);
				        this.deleteView(activeItem -1);
				        mViewInfo.setVisibility(View.INVISIBLE);
				        this.bDispInfo=false;
				    }
			   } else if ((this.currentScrollX - this.prevScrollX) > minFactor) {
				    if (activeItem > 0){
					     activeItem = activeItem - 1;
					     DatArray.setPositonHsv(activeItem);
					     this.loadView(activeItem, false);
					     this.deleteView(activeItem +1);
					     mViewInfo.setVisibility(View.INVISIBLE);
					     this.bDispInfo=false;
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
	;
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
	 public class PhotoImageView extends LargeImageView {
		 protected Context mContext;
		 protected ProgressDialog mProgress;
	
	     public PhotoImageView(Context context, ItemPT item, String s_dir) {
	         super(context);
	         this.mContext = context;
	         
	         //execute(item, s_dir);
	     }
	     @Override
	     public void setImageNotFound() {
	         setImageResource(R.drawable.ic_delete );
	     }
	     @Override
	     public void setImageNowLoading() {
	         setImageResource(R.drawable.ic_menu_rotate );
	     }
	     @Override
	     public void start_progress(){
	         mProgress = new ProgressDialog(mContext);
	         mProgress.setMessage(mContext.getString(R.string.now_loading));
	         mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	         mProgress.show(); 	    	 
	     }
	     @Override
	     public void stop_progress(){
	    	 mProgress.dismiss(); 
	     }
	 }


}