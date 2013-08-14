package com.kuc_arc_f.app.picasa;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ItemSet {
	private static final  String TAG ="ItemSet";
	
    String text;  
    int imageResourceId;  
	private boolean m_TYP_TITLE=false;
	
	public ItemSet() {
		text      = "";
		imageResourceId    = 0;
		m_TYP_TITLE        =false;
	}
	public String getText() { return text; }
	public void setText(String src) { text = src; }

	public int getImageResourceId() { return imageResourceId; }
	public void setUmageResourceId(int src) {imageResourceId = src; }

	public boolean getTYP_TITLE() { return m_TYP_TITLE; }
	public void setTYP_TITLE(boolean src) { m_TYP_TITLE = src; }

}