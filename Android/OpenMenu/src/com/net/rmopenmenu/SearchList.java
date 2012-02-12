package com.net.rmopenmenu;
import java.util.ArrayList;

import com.net.rmopenmenu.R;

import android.app.Fragment;
import android.app.ListActivity;
import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class SearchList extends ListFragment {
	
	private ArrayList<String> restaurant_names;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SQLiteDatabase db = new Database(getActivity().getApplicationContext()).getReadableDatabase();
		Cursor cursor = db.query("items", null, "name LIKE '%" + getTag() + "%'", null, null, null, null);
		cursor.moveToFirst();

		String s = "";
		int count = 0;
		while (!cursor.isAfterLast()) {
			count++;
			s += ("iid = " + cursor.getInt(cursor.getColumnIndex("iid")) + " OR ");
			cursor.moveToNext();
		}
		
        restaurant_names = new ArrayList<String>();

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
		
		db.close();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, restaurant_names);
		
		setListAdapter(adapter);
	}
}
