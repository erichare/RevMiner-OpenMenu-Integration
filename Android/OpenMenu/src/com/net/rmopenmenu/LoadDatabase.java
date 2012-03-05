package com.net.rmopenmenu;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.TextView;

public class LoadDatabase extends AsyncTask<String, Integer, String> {
	
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
		
		if (menu) {
			UpdateDatabase ud = new UpdateDatabase(activity);
			ud.execute("http://www.project-fin.org/openmenu/sync.php");
		}
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
