package org.thecyst.mytab;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Wallet {
	
	private Context context;
	
	private SQLiteDatabase db;
	private String DB_NAME = "mytab";
	private final int DB_VERSION = 1;
	
	private String TABLE_NAME = "wallet";
	private static final String KEY_ID = "id";
    private static final String KEY_AMOUNT = "amount";
	private static final String KEY_NAME = "name";
    private static final String KEY_NOTE = "note";
    private static final String KEY_TIME = "time";
    
    private static final String AKS = "AKS";
    
    public Wallet(Context context)
	{
		this.context = context;
 
		CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(this.context);
		this.db = helper.getWritableDatabase();
	}
    
    private class CustomSQLiteOpenHelper extends SQLiteOpenHelper
	{
		public CustomSQLiteOpenHelper(Context context)
		{
			super(context, DB_NAME, null, DB_VERSION);
		}
 
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			// This string is used to create the database.  It should
			// be changed to suit your needs.
			String newTableQueryString = "CREATE TABLE " +
					TABLE_NAME + " ( " +
					KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
					KEY_AMOUNT + " INTEGER NOT NULL, " +
					KEY_NAME + " TEXT NOT NULL DEFAULT ( 'misc' ), " +
					KEY_NOTE + " TEXT DEFAULT ( 'misc' ), " +
					KEY_TIME + " DATETIME DEFAULT ( datetime( 'now' ) ) " +
					");";
			db.execSQL(newTableQueryString);
		}
 
 
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			// NOTHING TO DO HERE. THIS IS THE ORIGINAL DATABASE VERSION.
			// OTHERWISE, YOU WOULD SPECIFIY HOW TO UPGRADE THE DATABASE.			
			Log.i(AKS, "onUpgrade");
		}
		
		@Override
		public void onOpen(SQLiteDatabase db) {
			String newTableQueryString = "CREATE TABLE IF NOT EXISTS " +
					TABLE_NAME + " ( " +
					KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
					KEY_AMOUNT + " INTEGER NOT NULL, " +
					KEY_NAME + " TEXT NOT NULL DEFAULT ( 'misc' ), " +
					KEY_NOTE + " TEXT DEFAULT ( 'misc' ), " +
					KEY_TIME + " DATETIME DEFAULT ( datetime( 'now' ) ) " +
					");";
			db.execSQL(newTableQueryString);
		}
		
	}
    
    public void closeDB() {
    	db.close();
    }
    
}
