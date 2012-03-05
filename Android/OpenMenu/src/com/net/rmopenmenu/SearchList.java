package com.net.rmopenmenu;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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


public class SearchList extends ListFragment {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = this.getArguments();
		ArrayList<String> combined = b.getStringArrayList("combined");
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, combined);
		
		setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		final String selectedItem = ((TextView) v).getText().toString();
		boolean menu = (selectedItem.endsWith("\n\n")? false : true);
		Toast.makeText(getActivity().getApplicationContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
		
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		String favorites = prefs.getString((menu? "favoritemenus" : "favoriterests"), "");
		favorites += (",,, " + selectedItem.replace("\n\n", ""));
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString((menu? "favoritemenus" : "favoriterests"), favorites);
		
		editor.commit();
	}
}
