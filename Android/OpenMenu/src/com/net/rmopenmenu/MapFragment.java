package com.net.rmopenmenu;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
		
		Drawable drawable = getActivity().getApplicationContext().getResources().getDrawable(R.drawable.pin);
		itemizedOverlay = new OMOverlay(drawable);
		overlays = mapView.getOverlays();
		
		locOverlay = new MyLocationOverlay(getActivity().getApplicationContext(), mapView);
		locOverlay.enableMyLocation();
				
		LoadMap lm = new LoadMap(getActivity().getApplicationContext(), b, getActivity());
		lm.execute("http://www.project-fin.org/openmenu/sync.php");
		
        return fragmentView;
	}
}
