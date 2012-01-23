package com.net.rmopenmenu;

import java.io.IOException;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class OMMapActivity extends MapActivity {
	
	LinearLayout linearLayout;
	MapView mapView;
	List<Overlay> mapOverlays;
	Drawable drawable;
	OMOverlay itemizedOverlay;
	MapController mapController;
	Geocoder gc;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		
		String restaurant = getIntent().getStringExtra("restaurant");
		
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		
		mapController = mapView.getController();
		
		mapOverlays = mapView.getOverlays();
		drawable = this.getResources().getDrawable(R.drawable.pin);
		itemizedOverlay = new OMOverlay(drawable);
		
		gc = new Geocoder(this); //create new geocoder instance
		
		List<Address> list = null;
		
		Log.v("Rest", restaurant);
		try {
			list = gc.getFromLocationName(restaurant, 1);
			Log.v("Testtttt", list.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (list.size() > 0) {
			Address address = list.get(0);
			
			int lat = (int)(address.getLatitude() * 1000000);
			int lon = (int)(address.getLongitude() * 1000000);
			
			Log.v("Test", lat + " " + lon);
			
			GeoPoint point = new GeoPoint(lat, lon);
			OverlayItem overlayitem = new OverlayItem(point, "", "");
			
			itemizedOverlay.addOverlay(overlayitem);
			mapOverlays.add(itemizedOverlay);
			mapController.setZoom(18);
			mapController.animateTo(point);
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}

}
