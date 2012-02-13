/*
 * Copyright 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.net.rmopenmenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.net.rmopenmenu.R;

public class MainActivity extends ActionBarActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
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
	    gridview.setAdapter(new ImageAdapter());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                break;

            case R.id.menu_refresh:
                getActionBarHelper().setRefreshActionItemState(true);
                getWindow().getDecorView().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                getActionBarHelper().setRefreshActionItemState(false);
                            }
                        }, 1000);
                break;

            case R.id.menu_search:
            	onSearchRequested();
                break;

            case R.id.menu_share:
                Toast.makeText(this, "Tapped share", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private String[] mThumbStrs = {
            "Burger", "Pizza", "Bacon", "Cake"
    };
	
	public class ImageAdapter extends BaseAdapter {
	    public ImageAdapter() {
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
