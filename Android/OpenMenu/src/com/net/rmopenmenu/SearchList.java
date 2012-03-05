package com.net.rmopenmenu;
import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;


public class SearchList extends ListFragment {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = this.getArguments();
		ArrayList<String> combined = b.getStringArrayList("combined");
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, combined);
		
		setListAdapter(adapter);
	}
}
