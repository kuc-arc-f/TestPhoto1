package com.kuc_arc_f.app.picasa;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.kuc_arc_f.fw.*;

public class SplashAct extends Activity implements AnimationListener {
	private static String  TAG="SplashAct";
	
	private static int m_SP_TM = 3000; //msec 
	private String m_MODE_LAND = "";
	
	private com.kuc_arc_f.fw.AppConst m_Const = new AppConst();
	//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash );
		try
		{

	    		SharedPreferences prefs = Prefs.get(SplashAct.this);
	    		m_MODE_LAND    = prefs.getString(m_Const.KEY_MODE_LAND,  "");

	        }catch(Exception e){
	        	e.printStackTrace();
	    }      
		Handler hdl = new Handler();
		hdl.postDelayed(new splashHandler(), 1200);
	}
	@Override
	public void onAnimationEnd(Animation animation) {
		try
		{
			this.finish();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public void onAnimationRepeat(Animation animation) {
	}
	@Override
	public void onAnimationStart(Animation animation) {
	}	

	class splashHandler implements Runnable {
		public void run() {
			SplashAct.this.finish();
		}
	}
	
	
}