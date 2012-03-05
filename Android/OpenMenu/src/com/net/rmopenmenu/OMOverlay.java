package com.net.rmopenmenu;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class OMOverlay extends ItemizedOverlay<OverlayItem> {
	
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context context;

	public OMOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		this.context = context;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	@Override
	protected boolean onTap(int index) {
		OverlayItem overlay = mOverlays.get(index);
		String query = overlay.getTitle();
		
		Intent myIntent = new Intent(context, SearchActivity.class);

		myIntent.putExtra("query", query);
		myIntent.putExtra("menu", false);
		
		context.startActivity(myIntent);
		
		return true;
	}

}
