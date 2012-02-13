package com.net.rmopenmenu;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;


public class SearchList extends ListFragment {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = this.getArguments();
		ArrayList<Integer> item_ids = b.getIntegerArrayList("item_ids");
		ArrayList<String> restaurant_names = b.getStringArrayList("restaurant_names");
		ArrayList<String> restaurant_addresses = b.getStringArrayList("restaurant_addresses");
		ArrayList<String> item_names = b.getStringArrayList("item_names");
		ArrayList<String> item_prices = b.getStringArrayList("item_prices");
		ArrayList<String> item_descriptions = b.getStringArrayList("item_descriptions");
		
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
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, combined);
		
		setListAdapter(adapter);
	}
}
