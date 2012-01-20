package com.net.rmopenmenu;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class OpenMenuActivity extends Activity {
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       TextView tv = new TextView(this);
       tv.setText("Eventually OpenMenu stuff will be here...");
       setContentView(tv);
   }
}