package com.net.rmopenmenu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class LoadMap extends AsyncTask<String, Integer, Bundle> {
	
	private Context context;
	private Bundle b;
	private Activity activity;
	private ArrayList<OverlayItem> overlayList;
	
	public LoadMap(Context context, Bundle b, Activity activity) {
		this.context = context;
		this.activity = activity;
		this.b = b;
		
		overlayList = new ArrayList<OverlayItem>();
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
		ArrayList<Integer> item_ids = b.getIntegerArrayList("item_ids");
		ArrayList<String> restaurant_names = b.getStringArrayList("restaurant_names");
		ArrayList<String> restaurant_addresses = b.getStringArrayList("restaurant_addresses");
		ArrayList<String> item_names = b.getStringArrayList("item_names");
		ArrayList<String> item_prices = b.getStringArrayList("item_prices");
		ArrayList<String> item_descriptions = b.getStringArrayList("item_descriptions");
		
		Geocoder gc = new Geocoder(context); //create new geocoder instance
		
		for (int i = 0; i < restaurant_names.size(); i++) {
			
			List<Address> list = null;
			String addr = restaurant_addresses.get(i);
			
			try {
				list = gc.getFromLocationName(addr, 1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (list != null && list.size() > 0) {
				Address address = list.get(0);
				
				int thisLat = (int)(address.getLatitude() * 1000000);
				int thisLon = (int)(address.getLongitude() * 1000000);
								
				GeoPoint point = new GeoPoint(thisLat, thisLon);
				OverlayItem overlayitem = new OverlayItem(point, "", "");
				
				MapFragment.itemizedOverlay.addOverlay(overlayitem);
			}
		}
		
		return null;
	}
		
}
