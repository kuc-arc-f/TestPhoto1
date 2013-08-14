package com.kuc_arc_f.app.picasa;

import android.support.v4.app.FragmentActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.kuc_arc_f.fw.AppConst;

public class MainActivity extends FragmentActivity {
	private static final String TAG ="MainActivity";
	
	private com.kuc_arc_f.fw.AppConst m_Const = new AppConst();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try
		{
			DatArray.setCountAd400(0);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			
			String s_land = m_Const.NG_CODE;
			Intent intent = new Intent( getApplicationContext(), FBA011Act.class );
			startActivity(intent);

			SharedPreferences prefs = Prefs.get( this );
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(m_Const.KEY_MODE_LAND, s_land);
			editor.commit();  
			finish();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}