package com.net.rmopenmenu;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

public class MapFragment extends Fragment {
	private View fragmentView;
	static OMOverlay itemizedOverlay;
	static MapView mapView;
	MapController mapController;
	static MyLocationOverlay locOverlay;
	static List<Overlay> overlays;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (fragmentView == null) {
			fragmentView = inflater.inflate(R.layout.map, container, false);
		}
		
		Bundle b = this.getArguments();
		
		mapView = (MapView) fragmentView.findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		
		mapController = mapView.getController();
						
		mapController.setZoom(15);
		mapController.setCenter(new GeoPoint(47662150, -122313237));
		
		Drawable drawable = getActivity().getApplicationContext().getResources().getDrawable(R.drawable.pin);
		itemizedOverlay = new OMOverlay(drawable);
		overlays = mapView.getOverlays();
		
		locOverlay = new MyLocationOverlay(getActivity().getApplicationContext(), mapView);
		locOverlay.enableMyLocation();
				
		LoadMap lm = new LoadMap(getActivity().getApplicationContext(), b, getActivity());
		lm.execute("http://www.project-fin.org/openmenu/sync.php");
		
        return fragmentView;
	}
	
	/**
	 * This method returns the distance from the user's current location to a given point
	 * It returns this in BigDecimal format for ease of processing
	 * 
	 * @param point A GeoPoint corresponding to the location under consideration
	 * 
	 * @return A BigDecimal representing the distance to this point in miles
	 */
	public static double distanceBetween(GeoPoint point1, GeoPoint point2) {

		// Define two location variables to process
		Location loc1 = new Location("");
		Location loc2 = new Location("");

		// This method is valid so long as the location is not the default and not null
		if (point1 != null && point2 != null) {

			// Compute the latitude and longitude of the two points
			// Add these values to our location variable
			loc1.setLatitude((float)(point1.getLatitudeE6()*1E-6));
			loc1.setLongitude((float)(point1.getLongitudeE6()*1E-6));
			loc2.setLatitude((float)(point2.getLatitudeE6()*1E-6));
			loc2.setLongitude((float)(point2.getLongitudeE6()*1E-6));

			// Return this value in miles rounded
			return(loc1.distanceTo(loc2) * 0.000621371192);
		} else {

			// Return -1 if the location was not valid
			return -1;
		}
	}
}
