package com.net.rmopenmenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.TabHost;

import com.net.rmopenmenu.SearchActivity.TabsAdapter;

public class LoadList extends AsyncTask<String, Integer, Bundle> {
	
	private Context context;
	private boolean menu;
	private Activity activity;
	
    TabHost mTabHost;
    ViewPager  mViewPager;
    TabsAdapter mTabsAdapter;
	
	public LoadList(Context context, boolean menu, Activity activity) {
		this.context = context;
		this.menu = menu;
		this.activity = activity;
	}

	@Override
	protected Bundle doInBackground(String... params) {
		return load(params[0]);
	}
	
	protected void onProgressUpdate(Integer... progress) {
    }

    protected void onPostExecute(Bundle b) {    
    	
		mTabHost = (TabHost)activity.findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mViewPager = (ViewPager)activity.findViewById(R.id.pager);

        mTabsAdapter = new TabsAdapter((FragmentActivity)activity, mTabHost, mViewPager);

        mTabsAdapter.addTab(mTabHost.newTabSpec("list").setIndicator("List"),
                SearchList.class, b);
        mTabsAdapter.addTab(mTabHost.newTabSpec("map").setIndicator("Map"),
                MapFragment.class, b);

        if (SearchActivity.savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(SearchActivity.savedInstanceState.getString("tab"));
        }
	}
    
  
	
	public Bundle load(String query) {
		SQLiteDatabase db = new Database(context).getReadableDatabase();
		ArrayList<Integer> item_ids = new ArrayList<Integer>();
		ArrayList<String> restaurant_names = new ArrayList<String>();
		ArrayList<String> restaurant_addresses = new ArrayList<String>();
		ArrayList<String> item_names = new ArrayList<String>();
		ArrayList<String> item_prices = new ArrayList<String>();
		ArrayList<String> item_descriptions = new ArrayList<String>();
		if (menu) {
			Cursor cursor = db.query("items", null, "name LIKE '%" + query + "%'", null, null, null, null);
			cursor.moveToFirst();
				
			while (!cursor.isAfterLast()) {
				item_ids.add(cursor.getInt(cursor.getColumnIndex("iid")));
				item_names.add(cursor.getString(cursor.getColumnIndex("name")));
				item_descriptions.add(cursor.getString(cursor.getColumnIndex("description")));
				String price = cursor.getString(cursor.getColumnIndex("price"));
				if (price.equals("0.00")) {
					price = "Unknown Price";
				}
				item_prices.add(price);
				cursor.moveToNext();
			}
			
			for (int i = 0; i < item_ids.size(); i++) {
				cursor = db.query("restaurants_items", null, "iid = " + item_ids.get(i), null, null, null, null);
				cursor.moveToFirst();
				
				int rid = cursor.getInt(cursor.getColumnIndex("rid"));
				
				cursor = db.query("restaurants", null, "rid = " + rid, null, null, null, null);
				cursor.moveToFirst();
				
				restaurant_names.add(cursor.getString(cursor.getColumnIndex("name")));
				restaurant_addresses.add(cursor.getString(cursor.getColumnIndex("address")));
			}
		} else {
			Cursor cursor = db.query("restaurants", null, "name LIKE '%" + query + "%'", null, null, null, null);
			cursor.moveToFirst();
	        
	        int rid = cursor.getInt(cursor.getColumnIndex("rid"));
	        String restaurant_name = cursor.getString(cursor.getColumnIndex("name"));
	        String restaurant_address = cursor.getString(cursor.getColumnIndex("address"));

	        cursor = db.query("restaurants_items", null, "rid = " + rid, null, null, null, null);
			cursor.moveToFirst();
			
			while (!cursor.isAfterLast()) {
				item_ids.add(cursor.getInt(cursor.getColumnIndex("iid")));
				cursor.moveToNext();
			}

			for (int i = 0; i < item_ids.size(); i++) {
				cursor = db.query("items", null, "iid = " + item_ids.get(i), null, null, null, null);
				cursor.moveToFirst();
				
				restaurant_names.add(restaurant_name);
				restaurant_addresses.add(restaurant_address);
				
				item_names.add(cursor.getString(cursor.getColumnIndex("name")));
				String price = cursor.getString(cursor.getColumnIndex("price"));
				if (price.equals("0.00")) {
					price = "Unknown Price";
				}
				item_prices.add(price);
				item_descriptions.add(cursor.getString(cursor.getColumnIndex("description")));
			}
		}
		
		db.close();

        Bundle b = new Bundle();
        b.putString("query", query);
        b.putBoolean("menu", menu);
        b.putIntegerArrayList("item_ids", item_ids);
        b.putStringArrayList("restaurant_names", restaurant_names);
        b.putStringArrayList("restaurant_addresses", restaurant_addresses);
        b.putStringArrayList("item_names", item_names);
        b.putStringArrayList("item_prices", item_prices);
        b.putStringArrayList("item_descriptions", item_descriptions);
		
    	ArrayList<Item> item_list = new ArrayList<Item>();

		for (int i = 0; i < item_ids.size(); i++) {
			Item item = new Item(item_ids.get(i), restaurant_names.get(i), restaurant_addresses.get(i), item_names.get(i), item_prices.get(i), item_descriptions.get(i), true);
			item_list.add(item);
		}
		
		Collections.sort(item_list);
			
		ArrayList<String> combined = new ArrayList<String>();
		for (Iterator<Item> i = item_list.iterator(); i.hasNext();) {
			Item item = i.next();
			combined.add(item.restaurant_name + "\n\n" + item.item_name + (item.item_description.equals("") ? "" : "\n" + item.item_description) + (item.item_price.equals("Unknown Price")? "" : "\n" + item.item_price));;
		}
		
		b.putStringArrayList("combined", combined);
        
		return b;
	}
		
}
