package com.net.rmopenmenu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	
    private static final String RESTAURANTS_TABLE_CREATE = "CREATE TABLE restaurants (rid INTEGER PRIMARY KEY, name TEXT, address TEXT, city TEXT, state TEXT, country TEXT)";
    private static final String ITEMS_TABLE_CREATE = "CREATE TABLE items (iid INTEGER PRIMARY KEY, name TEXT, description TEXT, price TEXT)";
    private static final String RESTAURANTS_ITEMS_TABLE_CREATE = "CREATE TABLE restaurants_items (rid INTEGER REFERENCES restaurants(rid), iid INTEGER REFERENCES items(iid), UNIQUE(rid, iid) ON CONFLICT REPLACE)";
   // private static final String RESTAURANTS_ITEMS_TABLE_ALTER = "ALTER TABLE restaurants_items ADD rid CONSTRAINT UNIQUE (rid, iid)";
    
    public Database(Context context) {
        super(context, "MENU_LOCAL", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RESTAURANTS_TABLE_CREATE);
        db.execSQL(ITEMS_TABLE_CREATE);
        db.execSQL(RESTAURANTS_ITEMS_TABLE_CREATE);
    //    db.execSQL(RESTAURANTS_ITEMS_TABLE_ALTER);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}
