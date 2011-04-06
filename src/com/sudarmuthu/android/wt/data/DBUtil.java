/**
 * 
 */
package com.sudarmuthu.android.wt.data;

import static android.provider.BaseColumns._ID;
import static com.sudarmuthu.android.wt.data.DBConstants.ENTRY_DATE;
import static com.sudarmuthu.android.wt.data.DBConstants.ENTRY_DAY_SEQ;
import static com.sudarmuthu.android.wt.data.DBConstants.ENTRY_TABLE_NAME;
import static com.sudarmuthu.android.wt.data.DBConstants.ENTRY_TYPE_ID;
import static com.sudarmuthu.android.wt.data.DBConstants.ENTRY_VALUE;
import static com.sudarmuthu.android.wt.data.DBConstants.TYPES_CREATED_ON;
import static com.sudarmuthu.android.wt.data.DBConstants.TYPES_NAME;
import static com.sudarmuthu.android.wt.data.DBConstants.TYPES_TABLE_NAME;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author "Sudar Muthu (http://sudarmuthu.com)"
 *
 */
public class DBUtil {

	private static final String TAG = "DBUtil";

	/**
	 * Fetch all Exercises
	 * 
	 * @param context
	 * @return
	 */
	public static List<Exercise> fetchAllExercises(Context context) {
		String [] FROM = {_ID, TYPES_NAME, TYPES_CREATED_ON};
		SQLiteDatabase db = new DBData(context).getReadableDatabase();
		List<Exercise> exercises = new ArrayList<Exercise>(); 
		
		Cursor cursor = db.query(TYPES_TABLE_NAME, FROM, null, null, null, null, TYPES_NAME); 

		while (cursor.moveToNext()) {
			Exercise temp = new Exercise(cursor.getInt(0), cursor.getString(1), new Date(cursor.getLong(2)));
			exercises.add(temp);
		}
		
		cursor.close();
		db.close();
		return exercises;
	}
	
	/**
	 * Fetch a type by id
	 * 
	 * @param context
	 * @param typeId
	 * @return
	 */
	public static Exercise fetchExercise(Context context, int typeId) {
		String [] FROM = {_ID, TYPES_NAME, TYPES_CREATED_ON};
		String where = _ID + "=" + typeId;
		
		SQLiteDatabase db = new DBData(context).getReadableDatabase();
		
		Cursor cursor = db.query(TYPES_TABLE_NAME, FROM, where, null, null, null, TYPES_NAME); 
		
		cursor.moveToNext();
		Exercise temp = new Exercise(cursor.getInt(0), cursor.getString(1), new Date(cursor.getLong(2)));

		cursor.close();
		db.close();
		
		return temp;
	}
	
	/**
	 * Insert type into db
	 * 
	 * @param context
	 * @param typeName
	 */
	public static Exercise insertExercise(Context context, String typeName) {
		SQLiteDatabase db = new DBData(context).getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(TYPES_NAME, typeName);
		values.put(TYPES_CREATED_ON, System.currentTimeMillis());
		long id = db.insertOrThrow(TYPES_TABLE_NAME, null, values);
		
		db.close();
		
		Exercise newType = fetchExercise(context, (int) id);
		return newType;
	}

	/**
	 * Update Exercise Name
	 * 
	 * @param context
	 * @param exerciseToEdit
	 */
	public static void updateExercise(Context context, Exercise exerciseToEdit) {
		SQLiteDatabase db = new DBData(context).getWritableDatabase();
		String whereClause = _ID + "=" + exerciseToEdit.getId();
		ContentValues values = new ContentValues();
		
		values.put(TYPES_NAME, exerciseToEdit.getName());
		
		db.update(TYPES_TABLE_NAME, values, whereClause, null);
		db.close();
	}

	/**
	 * Delete a Exercise
	 * 
	 * @param context
	 * @param exerciseId
	 */
	public static void deleteExercise(Context context, int exerciseId ) {
		deleteAllEntries(context, exerciseId);
		
		SQLiteDatabase db = new DBData(context).getWritableDatabase();
		String where = _ID +  "=" + exerciseId;
		
		db.delete(TYPES_TABLE_NAME, where, null);
		db.close();

	}
	
	/**
	 * Delete all entries for an exercise
	 * 
	 * @param context
	 * @param exerciseId
	 */
	private static void deleteAllEntries(Context context, int exerciseId) {
		SQLiteDatabase db = new DBData(context).getWritableDatabase();
		
		String where = ENTRY_TYPE_ID + "=" + exerciseId;
		db.delete(ENTRY_TABLE_NAME, where, null);
		db.close();
		
	}
	
	/**
	 * Fetch Entires based on type
	 * 
	 * @param context
	 * @param typeId
	 * @return
	 */
	public static List<Entry> fetchEntries(Context context, int typeId, String orderBy) {
		
		List<Entry> entires = new ArrayList<Entry>(); 
		SQLiteDatabase db = new DBData(context).getWritableDatabase();
		
		Cursor cursor = fetchEntriesFromDB(db, typeId, orderBy);

		while (cursor.moveToNext()) {
			Entry temp = new Entry(cursor.getInt(0),
								   cursor.getInt(1), 
								   new Date(cursor.getLong(2) * 1000),	
								   cursor.getInt(3),
								   cursor.getString(4));								   
			entires.add(temp);
		}
		
		cursor.close();
		db.close();
		return entires;
	}
	
	/**
	 * Fetch Entries based on type and return an JSON array
	 * 
	 * @param mContext
	 * @return
	 */
	public static JSONArray fetchEntriesInJSON(Context context, int typeId) {

		JSONArray entires = new JSONArray(); 
		SQLiteDatabase db = new DBData(context).getWritableDatabase();
		
		Cursor cursor = fetchEntriesFromDB(db, typeId, ENTRY_DATE);
		
		while (cursor.moveToNext()) {
			JSONArray entry = new JSONArray();
			entry.put(cursor.getLong(2) * 1000);
			entry.put(Integer.parseInt(cursor.getString(4)));
			entires.put(entry);
		}
		
		cursor.close();
		db.close();
		
		Log.d(TAG, entires.toString());
		
		return entires;
	}

	/**
	 * Fetch Entries from DB
	 * 
	 * @param db
	 * @param typeId
	 * @return
	 */
	private static Cursor fetchEntriesFromDB(SQLiteDatabase db, int typeId, String orderBy) {
		String [] FROM = {_ID, ENTRY_TYPE_ID, ENTRY_DATE, ENTRY_DAY_SEQ, ENTRY_VALUE};
		String where = ENTRY_TYPE_ID + "=" + typeId;
		
		Cursor cursor = db.query(ENTRY_TABLE_NAME, FROM, where, null, null, null, orderBy);

		return cursor;
	}

	/**
	 * Fetch entries by date
	 * 
	 * @param mContext
	 * @param typeId
	 * @return
	 */
	public static List<Entry> fetchEntriesByDate(Context context, int typeId, String orderBy) {
		
		SQLiteDatabase db = new DBData(context).getWritableDatabase();
		List<Entry> entires = new ArrayList<Entry>(); 
		
		Cursor cursor = fetchEntriesByDateFromDB(db, typeId, orderBy);

		while (cursor.moveToNext()) {
			Entry temp = new Entry(cursor.getInt(0),
								   cursor.getInt(1), 
								   new Date(cursor.getLong(2) * 1000),	
								   cursor.getInt(3),
								   cursor.getString(4));								   
			entires.add(temp);
		}
		
		cursor.close();
		db.close();
		return entires;
	}
	
	/**
	 * Fetch Entries by Date in JSON
	 * 
	 * @param context
	 * @param typeId
	 * @return
	 */
	public static JSONArray fetchEntriesByDateInJSON(Context context, int typeId) {
		
		SQLiteDatabase db = new DBData(context).getWritableDatabase();
		JSONArray entires = new JSONArray(); 
		
		Cursor cursor = fetchEntriesByDateFromDB(db, typeId, ENTRY_DATE);
		
		while (cursor.moveToNext()) {
			JSONArray entry = new JSONArray();
			entry.put(cursor.getLong(2) * 1000);
			entry.put(Integer.parseInt(cursor.getString(4)));
			entires.put(entry);
		}
		
		cursor.close();
		db.close();
		return entires;
	}
	
	/**
	 * Fetch Entries by Date from DB
	 * 
	 * @param db
	 * @param typeId
	 * @return
	 */
	private static Cursor fetchEntriesByDateFromDB(SQLiteDatabase db, int typeId, String orderBy) {
		String [] FROM = {_ID, ENTRY_TYPE_ID, ENTRY_DATE, "MAX(" + ENTRY_DAY_SEQ + ")", "SUM(" + ENTRY_VALUE + ") AS " + ENTRY_VALUE};
		String where = ENTRY_TYPE_ID + "=" + typeId;
		String groupBy = "date(" + ENTRY_DATE + ", 'unixepoch')";

		Cursor cursor = db.query(ENTRY_TABLE_NAME, FROM, where, null, groupBy, null, orderBy); 

		return cursor;
	}
	
	/**
	 * Group by Max
	 * 
	 * @param mContext
	 * @param int1
	 * @return
	 */
	public static List<Entry> fetchEntriesByMax(Context context, int typeId, String orderBy) {
		SQLiteDatabase db = new DBData(context).getWritableDatabase();
		List<Entry> entires = new ArrayList<Entry>(); 
		
		Cursor cursor = fetchEntriesByMaxFromDB(db, typeId, orderBy);
		
		while (cursor.moveToNext()) {
			Entry temp = new Entry(cursor.getInt(0),
								   cursor.getInt(1), 
								   new Date(cursor.getLong(2) * 1000),	
								   cursor.getInt(3),
								   cursor.getString(4));								   
			entires.add(temp);
		}
		
		cursor.close();
		db.close();
		return entires;
	}

	/**
	 * Fetch Entries by Max in JSON
	 * 
	 * @param context
	 * @param typeId
	 * @return
	 */
	public static JSONArray fetchEntriesByMaxInJSON(Context context, int typeId) {
		
		SQLiteDatabase db = new DBData(context).getWritableDatabase();
		JSONArray entires = new JSONArray(); 
		
		Cursor cursor = fetchEntriesByMaxFromDB(db, typeId, ENTRY_DATE);
		
		while (cursor.moveToNext()) {
			JSONArray entry = new JSONArray();
			entry.put(cursor.getLong(2) * 1000);
			entry.put(Integer.parseInt(cursor.getString(4)));
			entires.put(entry);
		}
		
		cursor.close();
		db.close();
		return entires;
	}
	
	/**
	 * Fetch Entries by Max from db
	 * 
	 * @param db
	 * @param typeId
	 * @return
	 */
	private static Cursor fetchEntriesByMaxFromDB(SQLiteDatabase db, int typeId, String orderBy) {
		String [] FROM = {_ID, ENTRY_TYPE_ID, ENTRY_DATE, ENTRY_DAY_SEQ, "MAX(" + ENTRY_VALUE + ") AS " + ENTRY_VALUE};
		String where = ENTRY_TYPE_ID + "=" + typeId;
		String groupBy = "date(" + ENTRY_DATE + ", 'unixepoch')";
		
		Cursor cursor = db.query(ENTRY_TABLE_NAME, FROM, where, null, groupBy, null, orderBy); 
		return cursor;
	}
	
	/**
	 * Insert entry into db
	 * 
	 * @param context
	 * @param entry
	 */
	public static void insertEntry(Context context, Entry entry) {
		SQLiteDatabase db = new DBData(context).getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(ENTRY_TYPE_ID, entry.getTypeId());
		values.put(ENTRY_DAY_SEQ, entry.getDaySeq());
		values.put(ENTRY_VALUE, entry.getValue());
		values.put(ENTRY_DATE, (entry.getDate().getTime() / 1000));

		db.insertOrThrow(ENTRY_TABLE_NAME, null, values);		
		db.close();
	}

	/**
	 * Update Entry
	 * 
	 * @param mContext
	 * @param entryToEdit
	 */
	public static void updateEntry(Context context, Entry entry) {
		SQLiteDatabase db = new DBData(context).getWritableDatabase();
		String whereClause = _ID + "=" + entry.getId();
		ContentValues values = new ContentValues();
		
		values.put(ENTRY_VALUE, entry.getValue());
		values.put(ENTRY_DATE, (entry.getDate().getTime() / 1000));
		
		db.update(ENTRY_TABLE_NAME, values, whereClause, null);
		db.close();
	}
	
	/**
	 * Delete entry
	 * 
	 * @param mContext
	 * @param id
	 */
	public static void deleteEntry(Context context, int id) {
		SQLiteDatabase db = new DBData(context).getWritableDatabase();
		String where = _ID +  "=" + id;
		
		db.delete(ENTRY_TABLE_NAME, where, null);
		db.close();
	}
	
	/**
	 * Get the max day seq for a day
	 * 
	 * @param context
	 * @param date
	 * @param typeId 
	 * @return
	 */
	public static int getMaxDaySeq(Context context, String date, int typeId) {
		SQLiteDatabase db = new DBData(context).getReadableDatabase();
		int maxDaySeq = 0;
		String query = "SELECT MAX( " + ENTRY_DAY_SEQ + ") FROM " + ENTRY_TABLE_NAME + " where date(" + ENTRY_DATE + ", 'unixepoch') = '" + date + "' AND " + ENTRY_TYPE_ID + "=" + typeId ;
		
		Log.v("DBUtil", "Query: " + query);
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToNext();
		
		maxDaySeq = cursor.getInt(0);
		
		cursor.close();
		db.close();
		
		return maxDaySeq; 
	}
	
	/**
	 * Convert Date to String
	 * @param date
	 * @return
	 */
	public static String dateToString(Date dateValue) {
		return dateToString(dateValue, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * Convert Date to String
	 * 
	 * @param format If format is specified
	 * @param date
	 * @return
	 */
	public static String dateToString(Date dateValue, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format); // set the format to sql date time
		return dateFormat.format(dateValue);
	}
}