package edu.PrimozRezek.iManager.android.BAZA;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class DBAdapterKoledar implements BaseColumns {
	
	public static final  String TABLE="Dogodki";
	public static final  String TAG="DBAdapterKoledar";
	public static final String DATUM = "d_datum";
	public static final String NASLOV = "n_naslov";
	public static final String OPIS = "o_opis";
	
	public static final  int POS__ID=0;
	public static final  int POS_DATUM=1;
	public static final  int POS_NASLOV=2;
	public static final  int POS_OPIS=3;

	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DBAdapterKoledar(Context ctx) 
	{
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
		
	}


	//---opens the database---
	public DBAdapterKoledar open() throws SQLException 
	{
		db = DBHelper.getWritableDatabase();
		return this;
	}

	//---closes the database---    
	public void close() 
	{
		DBHelper.close();
	}

	//---insert a Koledar
	public long insertDogodek(Koledar k) 
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(DATUM, k.getDatum().toString());
		initialValues.put(NASLOV, k.getNaslovDogodka()); 
		initialValues.put(OPIS, k.getOpisDogodka()); 
		return db.insert(TABLE, null, initialValues);
	}

	//---deletes a particular title---
	public boolean deleteDogodek(long rowId) 
	{
		return db.delete(TABLE, _ID + "=" + rowId, null) > 0;
	}

	//---retrieves all the titles---
	public Cursor getAll() 
	{
		return db.query(TABLE, new String[] {
				_ID,       //POS__ID=0;
				DATUM,      //1
				NASLOV,		//2
				OPIS},      //3
				null, 		
				null, 
				null, 
				null, 
				null);
	}

	//---retrieves a particular title---
	public Cursor getDogodek(long rowId) throws SQLException 
	{
		Cursor mCursor =
			db.query(true, TABLE, new String[] {
					_ID, 
					DATUM,
					NASLOV,
					OPIS}, 
					_ID + "=" + rowId, 
					null,
					null, 
					null, 
					null, 
					null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	//---update---
	public boolean updateDogodek(Koledar tmp) 
	{
		ContentValues args = new ContentValues();
		args.put(DATUM, tmp.getDatum().toString());
		args.put(NASLOV, tmp.getNaslovDogodka());
		args.put(OPIS, tmp.getOpisDogodka());
		return db.update(TABLE, args, 
				_ID + "=" + tmp.getDbID(), null) > 0;
	}


}
