package com.net.rmopenmenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

public class MenuFragment extends Fragment {
	
	private String[] mThumbStrs;
	private boolean menu;
    
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle b1 = getArguments();
		menu = b1.getBoolean("menu");
		if (menu) {
			mThumbStrs = mThumbStrs1;
		} else {
			mThumbStrs = mThumbStrs2;
		}
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.main, container, false);
    	
    	final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		boolean databaseLoaded = prefs.getBoolean("databaseLoaded", false);
		
		TextView tv = (TextView)v.findViewById(R.id.myTextView);
				
		if (!databaseLoaded) {
			tv.setText("Building initial database.  This may take a minute.  Please wait...");
			
			TextView tv2 = (TextView)v.findViewById(R.id.myTextView2);
			tv2.setText("");
			
			LoadDatabase ld = new LoadDatabase(getActivity().getApplicationContext(), tv, tv2);
			ld.execute("http://www.project-fin.org/openmenu/sync.php");
		} else {
		}
    	
    	Bundle b1 = getArguments();
		menu = b1.getBoolean("menu");
		
		tv.setText(menu? getActivity().getString(R.string.hello) : getActivity().getString(R.string.hello2));
    	
    	GridView gridview = (GridView)v.findViewById(R.id.gridview);
	    gridview.setAdapter(new ImageAdapter(menu));
	    
	    return v;
    }
    
    private String[] mThumbStrs1 = {
            "Burger", "Pizza", "Bacon", "Cake"
    };
    
    private String[] mThumbStrs2 = {
            "Johnny Rockets", "Blue Onion Bistro", "Chipotle", "Kidd Valley"
    };
	
	public class ImageAdapter extends BaseAdapter {
		
		private LayoutInflater inflater;
		private Integer[] mThumbIds;
		
	    public ImageAdapter(boolean menu) {
	    	inflater = getActivity().getLayoutInflater();
	    	
	    	if (menu) {
	    		mThumbIds = mThumbIds1;
	    	} else {
	    		mThumbIds = mThumbIds2;
	    	}
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
				myView = inflater.inflate(R.layout.grid_item, null);
	            
	            
	        } else {
	            myView = convertView;
	        }
	        ImageButton ib = (ImageButton) myView.findViewById(R.id.grid_item_button);
	        
	        ib.setImageResource(mThumbIds[position]);

	        ib.setOnClickListener(new OnClickListener() {
		        public void onClick(View v) {
		        	Intent myIntent = new Intent(getActivity().getApplicationContext(), SearchActivity.class);

					myIntent.putExtra("query", mThumbStrs[position]);
					myIntent.putExtra("menu", menu);
					
					startActivity(myIntent);
		        }
		    });
	        
	        return myView;
	    }
	    
	    private Integer[] mThumbIds1 = {
	            R.drawable.burger, R.drawable.pizza,
	            R.drawable.bacon, R.drawable.cake
	    };
	    
	    private Integer[] mThumbIds2 = {
	            R.drawable.johnnyrockets, R.drawable.blueonionbistro,
	            R.drawable.chipotle, R.drawable.kiddvalley
	    };
	}

}
