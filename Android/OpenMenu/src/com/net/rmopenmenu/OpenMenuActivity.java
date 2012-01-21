package com.net.rmopenmenu;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class OpenMenuActivity extends Activity {

	private final String MENU_ROOT = "http://www.project-fin.org/openmenu/";
	private final int CONNECTION_TIMEOUT = 10000;
	private final int SOCKET_TIMEOUT = 10000;
	private String result = "";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		networkThread.start();
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

	private String Post(String root, String suffix, List<BasicNameValuePair> nameValuePairs) {

		// Initialize input stream and response variables
		InputStream iStream = null;
		String data = "";
		Log.v("Server", root + suffix);

		try {
			HttpPost httppost = new HttpPost(root + suffix);

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
			return getBaseContext().getString(R.string.timeout);
		}

		return data.trim();
	}
	
	Thread networkThread = new Thread() {
		public void run() {
			// Initialize the array of name value pairs
			List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
			result = Post(MENU_ROOT, "sync.php", nameValuePairs);

			textHandler.sendEmptyMessage(0);
		}
	};
	
	private Handler textHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			TextView tv = (TextView) findViewById(R.id.myTextView);
			
			parseJson(result);
			
			SQLiteDatabase db = new Database(getBaseContext()).getReadableDatabase();
			Cursor cursor = db.query("items", null, "name LIKE '%caesar salad%'", null, null, null, null);
			cursor.moveToFirst();

			String s = "";
			while (!cursor.isAfterLast()) {
				s += ("iid = " + cursor.getInt(cursor.getColumnIndex("iid")) + " OR ");
				cursor.moveToNext();
			}
			
			s = s.substring(0, s.length() - 4);
						
			cursor = db.query("restaurants_items", null, s, null, null, null, null);
			cursor.moveToFirst();

			s = "";
			while (!cursor.isAfterLast()) {
				s += ("rid = " + cursor.getInt(cursor.getColumnIndex("rid")) + " OR ");
				cursor.moveToNext();
			}
			
			s = s.substring(0, s.length() - 4);
			
			cursor = db.query("restaurants", null, s, null, null, null, null);
			cursor.moveToFirst();

			ArrayList<String> restaurant_names = new ArrayList<String>();
			while (!cursor.isAfterLast()) {
				restaurant_names.add(cursor.getString(cursor.getColumnIndex("name")));
				cursor.moveToNext();
			}
			
			tv.setText(restaurant_names.toString());
			
			db.close();
		}
	};
	
	public void parseJson(String json) {
		//used for parsing the JSON object
		JsonStreamParser parser = new JsonStreamParser(json);
		SQLiteDatabase db = new Database(getBaseContext()).getWritableDatabase();

		if (!json.equals("null")) {
			JsonArray arr = parser.next().getAsJsonArray();
	
			for (int i = 0; i < arr.size(); i++)
			{
				if (arr.get(i).isJsonObject())
				{
					//Since the JsonArray contains whole bunch json array, we can get each one out
					JsonObject ob = arr.get(i).getAsJsonObject();
	
					// Grab the stuff
					int rid = ob.get("rid").getAsInt();
					String name = ob.get("name").getAsString();
					String address = ob.get("address").getAsString();
					String city = ob.get("city").getAsString();
					String state = ob.get("state").getAsString();
					String country = ob.get("country").getAsString();
					
					db.execSQL("INSERT OR REPLACE INTO restaurants (rid, name, address, city, state, country) VALUES (" + 
													  rid + ", '" + name + "', '" + address + "', '" + city + "', '" + state + "', '" + country + "')");
				}
			}
			
			arr = parser.next().getAsJsonArray();
			
			for (int i = 0; i < arr.size(); i++)
			{
				if (arr.get(i).isJsonObject())
				{
					//Since the JsonArray contains whole bunch json array, we can get each one out
					JsonObject ob = arr.get(i).getAsJsonObject();
	
					// Grab the stuff
					int iid = ob.get("iid").getAsInt();
					String name = ob.get("name").getAsString();
					String description = ob.get("description").getAsString();
					String price = ob.get("price").getAsString();
					
					db.execSQL("INSERT OR REPLACE INTO items (iid, name, description, price) VALUES (" + 
													  iid + ", '" + name + "', '" + description + "', '" + price + "')");
				}
			}
			
			arr = parser.next().getAsJsonArray();
			
			for (int i = 0; i < arr.size(); i++)
			{
				if (arr.get(i).isJsonObject())
				{
					//Since the JsonArray contains whole bunch json array, we can get each one out
					JsonObject ob = arr.get(i).getAsJsonObject();
	
					// Grab the stuff
					int rid = ob.get("rid").getAsInt();
					int iid = ob.get("iid").getAsInt();
					
					db.execSQL("INSERT OR REPLACE INTO restaurants_items (rid, iid) VALUES (" + 
													  rid + ", " + iid + ")");
				}
			}
		}
		
		db.close();
	}
}
