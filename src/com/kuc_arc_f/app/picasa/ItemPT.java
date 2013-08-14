package com.kuc_arc_f.app.picasa;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

//Item.java
public class ItemPT {
	private static final  String TAG ="ItemPT";
	
	private long mTblID=0;
	private int  mPnum =0;
	private int  mDnum =0;
	private int  mPkbn =0;
	private CharSequence m_ID;
	private CharSequence m_Title;
	
	private CharSequence m_Size;
	private CharSequence m_Url_img;
	private CharSequence m_Url_img_t;
	
	private CharSequence m_Categ;
	private Bitmap m_Image;
	private CharSequence m_TotalPage;
	private CharSequence m_Href;
	
	//slide
	private boolean m_Complete =false;
	private boolean m_StatNG   =false;  //retray=NG(10)
	
	public ItemPT() {
		mTblID     =0;
		mPnum =0;
		mDnum =0;
		mPkbn =0;
		m_ID       = "";
		m_Title ="";
		m_Categ     = "";
		m_Image     = null;
		m_Size ="";
		m_Url_img_t ="";
		m_Url_img ="";
		m_Title   ="";
		m_Complete    =false;
	}
	public long getTblID(){ return mTblID;}
	public void setTblID(long src){ mTblID = src; }
	
	public int getPkbn() {return mPkbn;}
	public void setPkbn(int src) {mPkbn = src;}
	
	public int getPnum() {return mPnum;}
	public void setPnum(int src) {mPnum = src;}
	
	public int getDnum() {return mDnum;}
	public void setDnum(int src) {mDnum = src;}

	public CharSequence getId() { return m_ID; }
	public void setId(CharSequence id) { m_ID = id; }

	public CharSequence getCateg() { return m_Categ; }
	public void setCateg(CharSequence src) { m_Categ = src; }

	public CharSequence getTotalPage() { return m_TotalPage; }
	public void setTotalPage(CharSequence src) { m_TotalPage = src; }
	
	public void setImage(String urlStr) {
		Bitmap image = null;
		try {
			URL url = new URL(urlStr);
			image = BitmapFactory.decodeStream((InputStream)url.getContent());
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
		this.m_Image = image;
	}
	public void setImage_file(Bitmap bmp) {
		this.m_Image = bmp;
	}
	public Bitmap getIcon() {
		return this.m_Image;
	}
	public Bitmap getIcon_file(String src) {
		return this.m_Image;
	}
	
	public CharSequence getSize() { return m_Size; }
	public void setSize(CharSequence src) { m_Size = src; }

	public CharSequence getTitle() { return m_Title; }
	public void setTitle(CharSequence src) { m_Title = src; }

	public CharSequence getUrl_img() { return m_Url_img; }
	public void setUrl_img(CharSequence src) { m_Url_img = src; }

	public CharSequence getUrl_img_t() { return m_Url_img_t; }
	public void setUrl_img_t(CharSequence src) { m_Url_img_t = src; }
	
	public CharSequence getHref() { return m_Href; }
	public void setHref(CharSequence src) { m_Href = src; }
	
	public boolean getComplete() { return m_Complete; }
	public void setComplete(boolean src) { m_Complete = src; }

	public boolean getStatNG() { return m_StatNG; }
	public void setStatNG(boolean src) { m_StatNG = src; }
	
}