package com.net.rmopenmenu;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.view.Window;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class OpenMenuActivity extends Activity {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);		

		setProgressBarIndeterminateVisibility(true);
		
		setContentView(R.layout.grid_view);
		
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean databaseLoaded = prefs.getBoolean("databaseLoaded", false);
		
		if (!databaseLoaded) {
			setProgressBarIndeterminateVisibility(true);

			TextView tv = (TextView)findViewById(R.id.myTextView);
			tv.setText("Building initial database.  This may take a minute.  Please wait...");
			
			LoadDatabase ld = new LoadDatabase(getBaseContext(), this, tv);
			ld.execute("http://www.project-fin.org/openmenu/sync.php");
		} else {
			setProgressBarIndeterminateVisibility(false);
		}
		
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
		    public void onStatusChanged(String provider, int status, Bundle extras) {}

		    public void onProviderEnabled(String provider) {}

		    public void onProviderDisabled(String provider) {}

			@Override
			public void onLocationChanged(Location location) {
				SharedPreferences.Editor editor = prefs.edit();
				
				Log.v("Location Update", "Got new lat " + (int)(location.getLatitude()*1000000));
				
				editor.putInt("lat", (int)(location.getLatitude()*1000000));
				editor.putInt("lon", (int)(location.getLongitude()*1000000));
				
				editor.commit();
			}
		  };

		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		
		GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(new ImageAdapter(this));
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.mainmenu, menu);
	    
	    // Get the SearchView and set the searchable configuration
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
	    
	    return super.onCreateOptionsMenu(menu);
    }
	
	private String[] mThumbStrs = {
            "Burger", "Pizza", "Bacon", "Cake"
    };
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    return true;
	}
	
	public class ImageAdapter extends BaseAdapter {
	    private Context mContext;

	    public ImageAdapter(Context c) {
	        mContext = c;
	    }

	    public int getCount() {
	        return 4;
	    }

	    public Object getItem(int position) {
	        return null;
	    }

	    public long getItemId(int position) {
	        return 0;
	    }

	    // create a new ImageView for each item referenced by the Adapter
	    @Override
	    public View getView(final int position, View convertView, ViewGroup parent) {
	        View myView;
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	        	LayoutInflater li = getLayoutInflater();
				myView = li.inflate(R.layout.grid_item, null);
	            
	            
	        } else {
	            myView = convertView;
	        }
	        ImageButton ib = (ImageButton) myView.findViewById(R.id.grid_item_button);
	        
	        ib.setImageResource(mThumbIds[position]);
	        
	        ib.setOnClickListener(new OnClickListener() {
		        public void onClick(View v) {
		        	Intent myIntent = new Intent(getBaseContext(), SearchActivity.class);

					myIntent.putExtra("query", mThumbStrs[position]);
					
					startActivity(myIntent);
		        }
		    });
	        
	        return myView;
	    }
	    
	    private Integer[] mThumbIds = {
	            R.drawable.burger, R.drawable.pizza,
	            R.drawable.bacon, R.drawable.cake
	    };
	}
}
