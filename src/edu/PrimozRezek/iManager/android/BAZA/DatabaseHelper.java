package edu.PrimozRezek.iManager.android.BAZA;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public  class DatabaseHelper extends SQLiteOpenHelper 
{	

	public static final  String TAG="DatabaseHelper";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "DB_iManager";
	private static final String DATABASE_CREATE =
		"create table "+DBAdapterKoledar.TABLE+" ("+DBAdapterKoledar._ID+" integer primary key autoincrement, "
		+ DBAdapterKoledar.DATUM+" TEXT, " +DBAdapterKoledar.NASLOV+" TEXT, "+DBAdapterKoledar.OPIS+" TEXT );";

	DatabaseHelper(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, 
			int newVersion) 
	{
		Log.w(TAG, "Upgrading database from version " + oldVersion 
				+ " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS "+DBAdapterKoledar.TABLE);
		onCreate(db);
	}
}
