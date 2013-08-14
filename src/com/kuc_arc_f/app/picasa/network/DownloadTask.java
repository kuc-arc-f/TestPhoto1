package com.kuc_arc_f.app.picasa.network;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.kuc_arc_f.app.picasa.db.ImageCacheDB;
import com.kuc_arc_f.app.picasa.db.PhotoDB;
import com.kuc_arc_f.app.picasa.ItemPT;
import com.kuc_arc_f.fw.AppConst;

public class DownloadTask extends AsyncTask<ArrayList<ItemPT>, Void, Void> {
    
    private static final String TAG = "DownloadTask";

    private Context mContext;
    static com.kuc_arc_f.fw.AppConst m_Const = new AppConst();
    
    public DownloadTask(Context context) {
        mContext = context;
    }
    
    @Override
    protected Void doInBackground(ArrayList<ItemPT>... params) {
    	try
    	{
    		ArrayList<ItemPT> items = params[0];
            ImageCacheDB db = ImageCacheDB.getInstance(mContext);
            PhotoDB pdb = PhotoDB.getInstance(mContext );
            long id = 0;
            HttpClient httpClient = null;
            for (int i=0;i<items.size(); i++) {
		           	ItemPT  item    = items.get(i);
					String s_url    = item.getUrl_img_t().toString();
					String s_id     = item.getId().toString();
					String filename = s_id+ m_Const.EXT_JPG;
//Log.d(TAG, "getUrl_img_t="+ item.getUrl_img_t().toString());
					id = get_tblId(s_id);
//Log.d(TAG, "id=" +  String.valueOf(id));
//Log.d(TAG, "filename=" +  filename);
//Log.d(TAG, "s_url=" +  s_url);
//Log.d(TAG, "s_id=" +  s_id);
					if (Is_comleteDL(s_id)==false) {
					    try {
					        HttpGet httpRequest = new HttpGet(s_url);
					        httpClient = new DefaultHttpClient();
					        HttpResponse response;
					        response = (HttpResponse) httpClient.execute(httpRequest);
					        HttpEntity entity = response.getEntity();
					        BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
					        // ファイルに保存
					        String type = entity.getContentType().getValue();
					        FileOutputStream stream = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
					        InputStream is = bufHttpEntity.getContent();
					        byte[] data = new byte[4096];
					        int size;
					        while((size = is.read(data)) > 0) {
					            stream.write(data, 0, size);
					        }
					        stream.close();
					        is.close();
					        db.update(id, filename, type);
					        pdb.update_cFlg(s_id );
					    } catch (Exception e) {
					        Log.e(TAG, e.getClass().getSimpleName(), e);
					        if (id > 0) {
					            try {
					                db.delete(id);
					            } catch (Exception e2) {
					                Log.e(TAG, e2.getClass().getSimpleName(), e2);
					            }
					        }
					    } finally {
					        if (httpClient != null) {
					            httpClient.getConnectionManager().shutdown();
					        }
					    }
					}else{
						pdb.update_cFlg(s_id );
					}
            }
         }catch(Exception e){
            	e.printStackTrace();
    	 }
        return null;
    }
    
    long get_tblId(String s_id) throws Exception{
    	long ret=0;
    	try
    	{
    		ImageCacheDB db = ImageCacheDB.getInstance(mContext);
    		Cursor c= db.find_tblId(s_id);
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
    //final Cursor c = db.existsFile_byId(s_id);
    boolean Is_comleteDL(String s_id) throws Exception{
    	boolean ret=false;
    	try
    	{
    		ImageCacheDB db = ImageCacheDB.getInstance(mContext);
    		Cursor c= db.existsFile_byId(s_id);
    		if (c.moveToFirst()) {
    			ret=true;
    		}
			  c.close();
    	}catch(Exception e){
    		throw e;
    	}
    	return ret;
    }
    
    
}
