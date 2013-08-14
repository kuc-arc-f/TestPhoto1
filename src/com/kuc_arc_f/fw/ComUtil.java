package com.kuc_arc_f.fw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

//import com.kuc_arc_f.app.picasa.FBA001Activity;

import android.app.Activity;
import android.app.ProgressDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

public class ComUtil extends Activity {
	
	private static String TAG="ComUtil";

	//
	// @purpose : Calender -> String(YYYY-MM-DD HH:MI:SS)
	public String comConv_CalenderToString( GregorianCalendar c )
	{
		String sRet="";
		
		sRet = String.valueOf(c.get(Calendar.YEAR));
		sRet = sRet + "-" +String.valueOf(c.get(Calendar.MONTH)+1);
		sRet = sRet + "-" +String.valueOf(c.get(Calendar.DAY_OF_MONTH));

		sRet = sRet + " " +String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		sRet = sRet + ":" +String.valueOf(c.get(Calendar.MINUTE));
		sRet = sRet + ":" +String.valueOf(c.get(Calendar.SECOND));

		return sRet;
	}

	//
	// @purpose : Calender -> String(YYYY-MM-DD)
	public String comConv_DateToString( GregorianCalendar c )
	{
		String sRet="";
		
		sRet = String.valueOf(c.get(Calendar.YEAR));
		sRet = sRet + "-" +String.valueOf(c.get(Calendar.MONTH)+1);
		sRet = sRet + "-" +String.valueOf(c.get(Calendar.DAY_OF_MONTH));

		return sRet;
	}
	
    //
	public void com_MsgBox(String s_title, String s_msg, String s_msg2){
//        long msec = stopChronometer();
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle( s_title );
        b.setMessage(s_msg);
//        b.setIcon(R.drawable.congratulations);
        b.setPositiveButton(
        		s_msg2,
            new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog,int which) {
                    dialog.dismiss();
                }
            });
        b.show();
    }
	//	
    public  void showDialog(final Activity activity, String title, String text) {
        AlertDialog.Builder ad = new AlertDialog.Builder(activity);
        ad.setTitle(title);
        ad.setMessage(text);
        ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
               activity.setResult(Activity.RESULT_OK);
            }
        });
        ad.create();
        ad.show();
    }
    
    public void errorDialog(final Activity activity,  String text){
    	showDialog(activity, "Errror", text);
    }
    
    public String comGet_VersionName(final Activity activity, String s_pnm){
        PackageManager pm = activity.getPackageManager();
		String s_ver="";
		try{
	        PackageInfo pi =  
	            pm.getPackageInfo(s_pnm , 0);
	        s_ver = pi.versionName;
		}catch(Exception e){
			e.printStackTrace();
		}   	
    	return s_ver;
    } 
    //
    public void com_putStack(Throwable e){
        StackTraceElement[] stacks = e.getStackTrace();//スタックトレース
        StringBuilder sb = new StringBuilder();
        int len = stacks.length;
        for (int i = 0; i < len; i++) {
            StackTraceElement stack = stacks[i];
            sb.setLength(0);
            sb.append(stack.getClassName()).append("#");//クラス名
            sb.append(stack.getMethodName()).append(":");//メソッド名
            sb.append(stack.getLineNumber());//行番号
            Log.e(TAG, sb.toString());
//            pw.println(sb.toString());//ファイル書出し
        }
    }
    //
    public String comGet_VersionName02(final Activity activity){
    	PackageManager mgr = activity.getPackageManager();
    	String packageName = null;
    	String versionName = null;
    	try {
    	    PackageInfo info = mgr.getPackageInfo(activity.getPackageName(), 0);
    	    packageName = info.packageName;
    	    versionName = info.versionName;
    	} catch (NameNotFoundException e) {}
    	
    	return versionName;
    }  
    //
    public Bitmap com_readBmp(String fnm) throws Exception{
    	try
    	{
    		BitmapFactory.Options options = new BitmapFactory.Options();
    		options.inSampleSize = 1;
    		options.inJustDecodeBounds = false;

    		Bitmap image =BitmapFactory.decodeFile(fnm, options);
    		return image;
    	}catch(Exception e){
    		throw e;
    	}
    }
    //
    public boolean com_chkImageList(String fnm) throws Exception{
    	try
    	{
    		boolean ret=false;
	        File file = new File( fnm);  
	        if (file.exists()== false) {
	        	return ret;
	        }
	        return true;
    	}catch(Exception e){
    		throw e;
    	}
    }
    //
    public  boolean com_saveFile2SD(Activity act, String  fnm, byte[] w) {
        OutputStream out = null;
        try{
	        out = act.openFileOutput(fnm, act.MODE_WORLD_READABLE);
        	out.write(w); 
        	out.flush();
            return true;
        }
        catch (FileNotFoundException e) {
Log.e(TAG , e.toString() + e.getMessage());
			e.printStackTrace();
            return false;
        }
        catch (IndexOutOfBoundsException e) {
Log.w(TAG, "Failed to save file as inputed value was illegal");
            return false;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
    }    
    
    public  boolean com_saveFile03(Context con, String  fnm, byte[] w) {
        OutputStream out = null;
        try{
	        out = con.openFileOutput(fnm, con.MODE_WORLD_READABLE);
        	out.write(w); 
        	out.flush();
            return true;
        }
        catch (FileNotFoundException e) {
Log.e(TAG , e.toString() + e.getMessage());
            return false;
        }
        catch (IndexOutOfBoundsException e) {
Log.w(TAG, "Failed to save file as inputed value was illegal");
            return false;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
    }    
    public void overrideGetSize(Display display, Point outSize) {
        try {
          // test for new method to trigger exception
          Class pointClass = Class.forName("android.graphics.Point");
          Method newGetSize = Display.class.getMethod("getSize", new Class[]{ pointClass });

          // no exception, so new method is available, just use it
          newGetSize.invoke(display, outSize);
        } catch(NoSuchMethodException ex) {
          // new method is not available, use the old ones
          outSize.x = display.getWidth();
          outSize.y = display.getHeight();
        } catch(Exception e){
        	e.printStackTrace();
        }
    }
    
    public float get_widthByDensity(Display display, Resources r) throws Exception{
    	float ret=0;
    	try
    	{
			Point size = new Point();
			overrideGetSize(display, size);
			int width = size.x;
//Log.d(TAG , "init_proc.width=" +String.valueOf(width));
			float f_den= r.getDisplayMetrics().density;
			ret=(float)width / f_den;    		
    	}catch(Exception e){
    		throw e;
    	}
    	return ret;
    }
    
    public Point get_pointByDensity(Display display, Resources r) throws Exception{
    	Point ret = new Point();
    	try
    	{
			Point size = new Point();
			overrideGetSize(display, size);
			int width = size.x;
			int height = size.y;
//Log.d(TAG , "init_proc.width=" +String.valueOf(width));
			float f_den= r.getDisplayMetrics().density;
			float f_w =(float)width / f_den;    		
			float f_y =(float)height / f_den;  
			
			ret.x = (int)f_w;
			ret.y = (int)f_y;
    	}catch(Exception e){
    		throw e;
    	}
    	return ret;
    }
 
    public static int differenceDays(Date date1,Date date2) {
        long datetime1 = date1.getTime();
        long datetime2 = date2.getTime();
        long one_date_time = 1000 * 60 * 60 * 24;
        long diffDays = (datetime1 - datetime2) / one_date_time;
        return (int)diffDays; 
    }     

    
}
