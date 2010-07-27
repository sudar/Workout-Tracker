/**
 * 
 */
package com.sudarmuthu.android.workouttracker.data;

import static android.provider.BaseColumns._ID;
import static com.sudarmuthu.android.workouttracker.data.DBConstants.ENTRY_DATE;
import static com.sudarmuthu.android.workouttracker.data.DBConstants.ENTRY_DAY_SEQ;
import static com.sudarmuthu.android.workouttracker.data.DBConstants.ENTRY_TABLE_NAME;
import static com.sudarmuthu.android.workouttracker.data.DBConstants.ENTRY_TYPE_ID;
import static com.sudarmuthu.android.workouttracker.data.DBConstants.ENTRY_VALUE;
import static com.sudarmuthu.android.workouttracker.data.DBConstants.TYPES_CREATED_ON;
import static com.sudarmuthu.android.workouttracker.data.DBConstants.TYPES_NAME;
import static com.sudarmuthu.android.workouttracker.data.DBConstants.TYPES_TABLE_NAME;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author "Sudar Muthu (sudarm@)"
 *
 */
public class DBData extends SQLiteOpenHelper {
	private static final String TAG = "DBData";
	
	private static final String DATABASE_NAME = "workouttracker.db"; 
	private static final int DATABASE_VERSION = 1;

	 private static final String CREATE_TYPES_TABLE_SQL =
		 "CREATE TABLE " + TYPES_TABLE_NAME + " (" 
		 + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
		 + TYPES_NAME + " TEXT,"
		 + TYPES_CREATED_ON + " INTEGER"
		 + " );";

	 private static final String CREATE_ENTRY_TABLE_SQL =
		 "CREATE TABLE " + ENTRY_TABLE_NAME + " (" 
		 + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
		 + ENTRY_TYPE_ID + " INTEGER,"
		 + ENTRY_DATE + " INTEGER,"
		 + ENTRY_DAY_SEQ + " INTEGER,"
		 + ENTRY_VALUE + " TEXT"
		 + " );";
	 
	/**
	 * 	 
	 * @param context
	 */
	public DBData(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// When the app is installed for the first time
		db.execSQL(CREATE_TYPES_TABLE_SQL);
		db.execSQL(CREATE_ENTRY_TABLE_SQL);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TYPES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ENTRY_TABLE_NAME);
        onCreate(db);
	}
}