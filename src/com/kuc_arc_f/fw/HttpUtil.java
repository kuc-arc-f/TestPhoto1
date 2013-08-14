package com.kuc_arc_f.fw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.*;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.params.HttpConnectionParams;
//import org.apache.http.params.HttpParams;
//import org.apache.http.protocol.BasicHttpContext;
//import org.apache.http.protocol.DefaultedHttpContext;
//import org.apache.http.protocol.HttpContext;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpUtil {
	
	private String TAG ="HttpUtil";
	
	private static final int mCONNECTION_TIMEOUT = 3000;
	private static final int mSOCKET_TIMEOUT     = 1000;
    //
    public String doGet( String url ) throws Exception{
        try
        {
        	
            HttpGet method = new HttpGet( url );

            DefaultHttpClient client = new DefaultHttpClient();

            // ヘッダを設定する
            method.setHeader( "Connection", "Keep-Alive" );
            
            HttpResponse response = client.execute( method );
            int status = response.getStatusLine().getStatusCode();
            if ( status != HttpStatus.SC_OK )
                throw new Exception( "" );
            
            return EntityUtils.toString( response.getEntity(), "UTF-8" );
        }
        catch ( Exception e )
        {
//        	Log.d( "doGet",  e.getMessage().toString() );
//            return null;
        	throw e;
        }
    }
    
    public String getHttp_byParam( String url ) throws Exception{
        try
        {
        	HttpParams httpParams = new BasicHttpParams();
    		HttpConnectionParams.setConnectionTimeout(httpParams, mCONNECTION_TIMEOUT);
    		HttpConnectionParams.setSoTimeout(httpParams, mSOCKET_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpParams);
            HttpGet method = new HttpGet( url );

            // ヘッダを設定する
            method.setHeader( "Connection", "Keep-Alive" );
            
            HttpResponse response = client.execute( method );
            int status = response.getStatusLine().getStatusCode();
            if ( status != HttpStatus.SC_OK )
                throw new Exception( "" );
            
            return EntityUtils.toString( response.getEntity(), "UTF-8" );
        }
        catch ( Exception e )
        {
        	throw e;
        }
    }
    
	public String doPost( String url, String params )
	{
	    try
	    {            
	        HttpPost method = new HttpPost( url );
	        
	        DefaultHttpClient client = new DefaultHttpClient();
	        
	        // POST データの設定
	        StringEntity paramEntity = new StringEntity( params );
	        paramEntity.setChunked( false );
	        paramEntity.setContentType( "application/x-www-form-urlencoded" );
	        method.setEntity( paramEntity );
	        
	        HttpResponse response = client.execute( method );
	        int status = response.getStatusLine().getStatusCode();
	        if ( status != HttpStatus.SC_OK )
	            throw new Exception( "" );
	        
	        return EntityUtils.toString( response.getEntity(), "UTF-8" );
	    }
	    catch ( Exception e )
	    {
	        return null;
	    }
	}
    //
    public String check_URL( String url ) throws Exception
    {
        try
        {
        	
            HttpGet method = new HttpGet( url );

            DefaultHttpClient client = new DefaultHttpClient();

            // ヘッダを設定する
            method.setHeader( "Connection", "Keep-Alive" );
            
            HttpResponse response = client.execute( method );
            int status = response.getStatusLine().getStatusCode();
//Log.d(TAG, "status="+ status );
            if ( status != HttpStatus.SC_OK ){
            	String s_buff="Http ErrorCode="+ String.valueOf(status);
                throw new Exception( s_buff );
            }
            
            return EntityUtils.toString( response.getEntity(), "UTF-8" );
        }
        catch ( Exception e )
        {
        	throw e;
        }
    }
    //
    public String postData(String sUrl, List<NameValuePair> objValuePairs) {
    	HttpClient objHttp = new DefaultHttpClient();
    	String sReturn = "";
        try {
        	HttpPost objPost   = new HttpPost(sUrl);
        	
            objPost.setEntity(new UrlEncodedFormEntity(objValuePairs, "UTF-8"));

            HttpResponse objResponse = objHttp.execute(objPost);
            if (objResponse.getStatusLine().getStatusCode() < 400){
                InputStream objStream = objResponse.getEntity().getContent();
                InputStreamReader objReader = new InputStreamReader(objStream);
                BufferedReader objBuf = new BufferedReader(objReader);
                StringBuilder objJson = new StringBuilder();
                String sLine;
                while((sLine = objBuf.readLine()) != null){
                	objJson.append(sLine);
                }
                sReturn = objJson.toString();
                objStream.close();
            }
        } catch (IOException e) {
        	return null;
        }	
        return sReturn;
    }
    //
    public byte[] getBmpFromURL(String requestURL) throws Exception {
        
        HttpClient c = new DefaultHttpClient();
        HttpParams p = c.getParams();
        HttpGet g = new HttpGet(requestURL);
        byte[] w = null;
         
        //接続のタイムアウト：5sec
        HttpConnectionParams.setConnectionTimeout(p, 5000);
        //データ取得のタイムアウト：8sec
        HttpConnectionParams.setSoTimeout(p, 8000);
         
        try {
            HttpResponse r = c.execute(g);
            int status = r.getStatusLine().getStatusCode();
            if (status != HttpStatus.SC_OK) {
Log.i(TAG, "httpRequest: NG=");
                throw new Exception("httpRequest: NG");
            } else if (status < 400) {
//                Log.i(TAG, "httpRequest: OK=" + requestURL);
                w = EntityUtils.toByteArray(r.getEntity());
                return w;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        Log.w(TAG, "httpRequest: False=" + requestURL);
        return null;
    }    
    
}
