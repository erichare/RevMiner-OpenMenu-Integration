package com.net.rmopenmenu;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class FavoritesActivity extends ListActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		final SharedPreferences.Editor editor = prefs.edit();
		
		this.setTitle("OM Finder - Favorites");
		
		final boolean menu = this.getIntent().getBooleanExtra("menu", true);

		if (prefs.contains("result")) { 
			Toast.makeText(getBaseContext(), "Removed from Favorites", Toast.LENGTH_SHORT).show();
			editor.remove("result");
			
			editor.commit();
		}
		final Set<String> favorites = (menu? prefs.getStringSet("favoritemenus", null) : prefs.getStringSet("favoriterests", null));

		ArrayList<String> faves = new ArrayList<String>();
		for (Iterator<String> i = favorites.iterator(); i.hasNext();) {
			String str = i.next();
			
			faves.add(str);
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.list_item, faves);
		
		setListAdapter(adapter);
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		// Every item will launch the map
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				final String selectedItem = ((TextView) v).getText().toString();

				favorites.remove(selectedItem);
				editor.putStringSet((menu? "favoritemenus" : "favoriterests"), favorites);
				editor.putString("result", "Removed from Favorites");
				
				editor.commit();
				
				startActivity(getIntent()); finish();
			}
		});
	}
}
