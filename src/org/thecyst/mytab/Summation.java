package org.thecyst.mytab;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Summation {

	private Context context;
	
	private SQLiteDatabase db;
	private String DB_NAME = "mytab";
	private final int DB_VERSION = 1;
	
	private String TABLE_NAME = "table_name";
	private static final String KEY_ID = "id";
	private static final String KEY_AMOUNT = "amount";
    private static final String KEY_NAME = "name";
    
    private static final String AKS = "AKS";
    
    public Summation(Context context, String tableName) {
		this.context = context;
		TABLE_NAME = tableName;
 
		CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(this.context);
		this.db = helper.getWritableDatabase();
	}
    
    private class CustomSQLiteOpenHelper extends SQLiteOpenHelper {
		public CustomSQLiteOpenHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}
 
		@Override
		public void onCreate(SQLiteDatabase db) {
			String newTableQueryString = "CREATE TABLE " +
				TABLE_NAME + " ( " +
				KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
				KEY_AMOUNT + " INTEGER NOT NULL, " +
				KEY_NAME + " TEXT NOT NULL DEFAULT ( 'misc' ) " +
				");";
			db.execSQL(newTableQueryString);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.i(AKS, "onUpgrade");
		}
		
		@Override
		public void onOpen(SQLiteDatabase db) {
			String newTableQueryString = "CREATE TABLE IF NOT EXISTS " +
				TABLE_NAME + " ( " +
				KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
				KEY_AMOUNT + " INTEGER NOT NULL, " +
				KEY_NAME + " TEXT NOT NULL DEFAULT ( 'misc' ) " +
				");";
			db.execSQL(newTableQueryString);
		}
	}
    
    public ArrayList<ArrayList<Object>> loadList() {
    	ArrayList<ArrayList<Object>> ledgers = new ArrayList<ArrayList<Object>>();
		Cursor cursor;
		try {
			String generateList = "SELECT " +
				KEY_NAME + ", " +
				KEY_AMOUNT + " FROM " +
				TABLE_NAME + " ORDER BY " +
				KEY_NAME + " ASC";
			cursor = db.rawQuery(generateList, null);
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					ArrayList<Object> dataList = new ArrayList<Object>();
					dataList.add(cursor.getString(0));
					dataList.add(cursor.getInt(1));
					ledgers.add(dataList);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		catch (SQLException e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
		return ledgers;
	}
    
    public void addRow(String name) {
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name);
		values.put(KEY_AMOUNT, 0);
		try {
			db.insert(TABLE_NAME, null, values);
		}
		catch(Exception e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
	}
    
    public void closeDB() {
    	db.close();
    }
        
}