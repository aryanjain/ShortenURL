package com.megadevs.bitlyzer;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.megadevs.bitlyzer.utils.Utils;

import javax.net.ssl.HttpsURLConnection;

public class Bitlyzer {

	private String formatString 	= "&format=";
	private String apiKeyString		= "&apiKey=";
	private String longUrlString	= "&longUrl=";
	private String ep			= "https://api-ssl.bitly.com/v3/shorten?access_token=";
	
	private String url;
	private String endpoint;

	private String accessToken;
	private String apiKey;

	private BitlyzerCallback mCallback;
	int flag=-1;
	
	public Bitlyzer(String accessToken, String apiKey) {
		this.accessToken = accessToken;
		this.apiKey = apiKey;
	}
	
	public void shorten(String url, BitlyzerCallback c) {
		this.url = url;
		mCallback = c;
		
		new BitlyThread().start();
	}



    private class BitlyThread extends Thread {

    	private JSONResponse mResponse;
    	
    	@Override
    	public void run() {

    		if (!url.contains("http://"))
    			url = "http://" + url;
    		
    		endpoint = ep + accessToken + apiKeyString + apiKey + formatString + "json" + longUrlString + url;
    		//System.out.println("endpoint="+endpoint);
			//System.out.println("url="+url);
			Log.d("endpoint","endpoint="+endpoint+"  \n "+"url="+url);

    		
			try {


				if(isReachable()==1)
					System.out.println("reachable");
				else
					System.out.println("not reachable");


				URL serviceUrl=new URL(endpoint);
				HttpsURLConnection conn= (HttpsURLConnection) serviceUrl.openConnection();
				conn.setConnectTimeout(10000);
				conn.connect();
				//System.out.println("code="+conn.getResponseCode());
				Log.d("code","response= "+conn.getResponseCode());
				//URLConnection conn = serviceUrl.openConnection();
				InputStream is = conn.getInputStream();
				String result = Utils.readInputStreamAsString(is);
				Log.d("res","response=");

				Gson g = new Gson();
				mResponse = g.fromJson(result, JSONResponse.class);
				System.out.println("Mresponse="+mResponse);

				if (mResponse.statusCode.equals("200")) {
					mCallback.onSuccess(mResponse.getURL());
					System.out.println(mResponse.getURL());
					System.out.println(mResponse.statusTXT);
				}
				else
					System.out.println(mResponse.statusTXT);
					//mCallback.onError(mResponse.statusTXT);
				
			} catch (Exception e) {
				Log.d("error","e="+e.getMessage());
				mCallback.onError(e.getMessage());
			}
    	}

    	public int isReachable()
		{
			try {
				InetAddress.getByName(endpoint).isReachable(10000); //Replace with your name
				flag=1;
			} catch (Exception e)
			{
				flag=0;
			}
			return flag;
		}
    	
		@SuppressWarnings("unused")
		public class JSONResponse {
			
			@SerializedName("status_code")
			public String statusCode;
			
			@SerializedName("data")
			public Data data = new Data();
			
			public String getURL() { return data.URL; }
			
			@SerializedName("status_txt")
			public String statusTXT;
			
			public class Data {
				
				@SerializedName("url")
				public String URL;
				
				@SerializedName("hash")
				public String hash;
				
				@SerializedName("global_hash")
				public String globalHash;
				
				@SerializedName("long_url")
				public String longURL;
				
				@SerializedName("new_hash")
				public String newHash;
			}
		}
    }
    
    public static abstract class BitlyzerCallback {
    	public abstract void onSuccess(String shortUrl);
    	public abstract void onError(String reason);
    }
}