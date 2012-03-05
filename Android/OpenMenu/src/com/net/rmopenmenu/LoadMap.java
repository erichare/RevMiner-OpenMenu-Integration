package com.net.rmopenmenu;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class LoadMap extends AsyncTask<String, Integer, Bundle> {
	
	private Bundle b;
	private Activity activity;
	
	public LoadMap(Bundle b, Activity activity) {
		this.activity = activity;
		this.b = b;
	}

	@Override
	protected Bundle doInBackground(String... params) {
		return load(params[0]);
	}
	
	protected void onProgressUpdate(Integer... progress) {
    }
	
	@Override
	protected void onPreExecute() {
		MapFragment.overlays.clear();
	}

    protected void onPostExecute(Bundle b) {
		MapFragment.overlays.add(MapFragment.locOverlay);
		MapFragment.overlays.add(MapFragment.itemizedOverlay);

    	MapFragment.mapView.invalidate();
    	
    	((ActionBarActivity) activity).getActionBarHelper().setRefreshActionItemState(false);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) activity.setProgressBarIndeterminateVisibility(false);
	}
    
  
	
	public Bundle load(String query) {
		ArrayList<String> restaurant_names = b.getStringArrayList("restaurant_names");
		ArrayList<Integer> restaurant_lats = b.getIntegerArrayList("restaurant_lats");
		ArrayList<Integer> restaurant_lons = b.getIntegerArrayList("restaurant_lons");
				
		for (int i = 0; i < restaurant_names.size(); i++) {				
			GeoPoint point = new GeoPoint(restaurant_lats.get(i), restaurant_lons.get(i));
			OverlayItem overlayitem = new OverlayItem(point, restaurant_names.get(i), "");
			
			MapFragment.itemizedOverlay.addOverlay(overlayitem);
		}
		
		return null;
	}
		
}
