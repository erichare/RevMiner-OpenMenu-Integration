package com.net.rmopenmenu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapFragment extends Fragment {
	private View fragmentView;
	LinearLayout linearLayout;
	MapView mapView;
	List<Overlay> mapOverlays;
	Drawable drawable;
	OMOverlay itemizedOverlay;
	MapController mapController;
	MyLocationOverlay locOverlay;
	Geocoder gc;
	
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (fragmentView == null) {
			fragmentView = inflater.inflate(R.layout.map, container, false);
		}
		
		Bundle b = this.getArguments();
		ArrayList<Integer> item_ids = b.getIntegerArrayList("item_ids");
		ArrayList<String> restaurant_names = b.getStringArrayList("restaurant_names");
		ArrayList<String> restaurant_addresses = b.getStringArrayList("restaurant_addresses");
		ArrayList<String> item_names = b.getStringArrayList("item_names");
		ArrayList<String> item_prices = b.getStringArrayList("item_prices");
		ArrayList<String> item_descriptions = b.getStringArrayList("item_descriptions");
		
		mapView = (MapView) fragmentView.findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		
		mapController = mapView.getController();
		
		mapOverlays = mapView.getOverlays();
		drawable = this.getResources().getDrawable(R.drawable.pin);
		itemizedOverlay = new OMOverlay(drawable);
		locOverlay = new MyLocationOverlay(this.getActivity().getApplicationContext(), mapView);
		locOverlay.enableMyLocation();
		
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity().getApplicationContext());
		int lat = prefs.getInt("lat", 0);
		int lon = prefs.getInt("lon", 0);
		
		gc = new Geocoder(this.getActivity().getApplicationContext()); //create new geocoder instance
		
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
				
				itemizedOverlay.addOverlay(overlayitem);
				mapOverlays.add(itemizedOverlay);
				mapController.animateTo(new GeoPoint(lat, lon));
			}
		}
		
		mapOverlays.add(locOverlay);
		mapController.setZoom(15);
		
        return fragmentView;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		locOverlay.disableMyLocation();
	}
}
