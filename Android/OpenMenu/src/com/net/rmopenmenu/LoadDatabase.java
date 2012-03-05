package com.net.rmopenmenu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

public class LoadDatabase extends AsyncTask<String, Integer, String> {
	
	private final int CONNECTION_TIMEOUT = 6000;
	private final int SOCKET_TIMEOUT = 6000;
	private Context context;
	private TextView tv;
	private boolean menu;
	private Activity activity;
	
	public LoadDatabase(Context context, TextView tv, boolean menu, Activity activity) {
		this.context = context;
		this.tv = tv;
		this.menu = menu;
		this.activity = activity;
	}

	@Override
	protected String doInBackground(String... params) {
		return load(params[0]);
	}
	
	protected void onProgressUpdate(Integer... progress) {
    }

    protected void onPostExecute(String result) {
    	tv.setText(context.getString(R.string.hello));
    	
		if (menu) ((ActionBarActivity) activity).getActionBarHelper().setRefreshActionItemState(false);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) activity.setProgressBarIndeterminateVisibility(false);
	}
    
    private HttpClient createHttpClient() {
		HttpParams params = new BasicHttpParams();

		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);

		HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);

		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);

		return new DefaultHttpClient(conMgr, params);
	}

	private String Post(String url, List<BasicNameValuePair> nameValuePairs) {

		// Initialize input stream and response variables
		InputStream iStream = null;
		String data = "";

		try {
			HttpPost httppost = new HttpPost("http://www.project-fin.org/openmenu/sync.php");

			// Process the response from the server 
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpClient httpClient = createHttpClient();
			HttpResponse httpResponse = httpClient.execute(httppost);

			HttpEntity entity = httpResponse.getEntity();
			iStream = entity.getContent();
		} catch(Exception e) {
			if (e != null) {
				Log.e("log_tag", "Error in http connection " + e.toString());
			}
		}

		// Convert server's response to a String
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(iStream,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			iStream.close();

			data = sb.toString();
		} catch(Exception e) {
			if (e != null) {
				Log.e("log_tag", "Error converting result " + e.toString());
			}
		}

		return data.trim();
	}
	
	public String load(String url) {
		Database myDbHelper = new Database(context);
 
        try {
 
        	myDbHelper.createDataBase();
 
 	} catch (IOException ioe) {
 
 		throw new Error("Unable to create database");
 
 	}
 
 	try {
 
 		myDbHelper.openDataBase();
 
 	}catch(SQLException sqle){
 
 		throw sqle;
 
	}
 	myDbHelper.close();
	return url;
	}

}
