package com.net.rmopenmenu;
import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;


public class SearchList extends ListFragment {
	
	private ArrayList<String> restaurant_names;
	private ArrayList<String> restaurant_items;
	private ArrayList<String> restaurant_descriptions;
	private ArrayList<String> restaurant_prices;
	private ArrayList<Integer> item_ids;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = this.getArguments();
		String query = b.getString("query");
		
		SQLiteDatabase db = new Database(getActivity().getApplicationContext()).getReadableDatabase();
		Cursor cursor = db.query("items", null, "name LIKE '%" + query + "%'", null, null, null, null);
		cursor.moveToFirst();
		
		item_ids = new ArrayList<Integer>();
        restaurant_names = new ArrayList<String>();
        restaurant_items = new ArrayList<String>();
        restaurant_prices = new ArrayList<String>();
        restaurant_descriptions = new ArrayList<String>();

		while (!cursor.isAfterLast()) {
			item_ids.add(cursor.getInt(cursor.getColumnIndex("iid")));
			restaurant_items.add(cursor.getString(cursor.getColumnIndex("name")));
			restaurant_descriptions.add(cursor.getString(cursor.getColumnIndex("description")));
			restaurant_prices.add(cursor.getString(cursor.getColumnIndex("price")));
			cursor.moveToNext();
		}
		
		for (int i = 0; i < item_ids.size(); i++) {
			cursor = db.query("restaurants_items", null, "iid = " + item_ids.get(i), null, null, null, null);
			cursor.moveToFirst();
			
			int rid = cursor.getInt(cursor.getColumnIndex("rid"));
			
			cursor = db.query("restaurants", null, "rid = " + rid, null, null, null, null);
			cursor.moveToFirst();
			
			restaurant_names.add(cursor.getString(cursor.getColumnIndex("name")));
		}
		
		db.close();
		
		ArrayList<String> combined = new ArrayList<String>();
		for (int i = 0; i < item_ids.size(); i++) {
			combined.add(restaurant_names.get(i) + "\n" + restaurant_items.get(i) + "\n" + restaurant_prices.get(i));
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, combined);
		
		setListAdapter(adapter);
	}
}
