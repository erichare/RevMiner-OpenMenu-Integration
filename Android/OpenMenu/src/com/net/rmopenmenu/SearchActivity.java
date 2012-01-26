package com.net.rmopenmenu;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		
		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		ArrayList<String> restaurant_names = new ArrayList<String>();

		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);

			SQLiteDatabase db = new Database(getBaseContext()).getReadableDatabase();
			Cursor cursor = db.query("items", null, "name LIKE '%" + query + "%'", null, null, null, null);
			cursor.moveToFirst();

			String s = "";
			int count = 0;
			while (!cursor.isAfterLast()) {
				count++;
				s += ("iid = " + cursor.getInt(cursor.getColumnIndex("iid")) + " OR ");
				cursor.moveToNext();
			}

			if (count > 0) {
				s = s.substring(0, s.length() - 4);
				count = 0;

				cursor = db.query("restaurants_items", null, s, null, null, null, null);
				cursor.moveToFirst();
	
				s = "";
				while (!cursor.isAfterLast()) {
					count++;
					s += ("rid = " + cursor.getInt(cursor.getColumnIndex("rid")) + " OR ");
					cursor.moveToNext();
				}
	
				if (count > 0) {
					s = s.substring(0, s.length() - 4);

					cursor = db.query("restaurants", null, s, null, null, null, null);
					cursor.moveToFirst();
		
					while (!cursor.isAfterLast()) {
						restaurant_names.add(cursor.getString(cursor.getColumnIndex("name")));
						cursor.moveToNext();
					}
				}
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, restaurant_names);

			setListAdapter(adapter);
			
			ListView lv = getListView();
			lv.setTextFilterEnabled(true);

			// Every item will launch the map
			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, final View v, int position, long id) {
					Thread thread = new Thread() {
						@Override
						public void run() {
							Intent myIntent = new Intent(getBaseContext(), OMMapActivity.class);
							myIntent.putExtra("restaurant", ((TextView) v).getText().toString());
							
							startActivity(myIntent);
						}
					};
					thread.start();
				}
			});

			db.close();
		}
	}

}
