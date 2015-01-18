package nl.mprog.setup.mproject10173072;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TaskDataBaseAdapter {
	
	// Constants and data
	//for logging
	private static final String TAG = "TaskDataBaseAdapter";
	
	// Creating Database Fields and variables 
	public static final String KEY_ROWID = "_id";
	public static final String COL_ROWID = "0";
	
	// setting up database fields
	public static final String KEY_TITLE = "title";
	public static final String KEY_TASKID = "task_id";
	public static final String KEY_DATECOMPLETED = "datecompleted";
	public static final String KEY_TASKDESCRIPTION = "taskdescription";
	public static final String KEY_TIMECOMPLETED = "timecompleted";
	public static final String KEY_ISCOMPLETED = "iscompleted";
	
	//setting up field numbers
	public static final int COL_TITLE = 1;
	public static final int COL_TASKID = 2;
	public static final int COL_DATECOMPLETED = 3;
	public static final int COL_TASKDESCRIPTION= 4;
	public static final int COL_TIMECOMPLETED = 5;
	public static final int COL_ISCOMPLETED = 6;
	
	public static final String[] ALL_KEYS = new String[]{KEY_ROWID, KEY_TITLE, KEY_TASKID,
		KEY_DATECOMPLETED, KEY_TASKDESCRIPTION, KEY_TIMECOMPLETED, KEY_ISCOMPLETED};
	
	// creating the data's characteristics
	public static final String DATABASE_NAME = "TaskDatabase";
	public static final String DATABASE_TABLE = "mainTable";
	
	// Database version
	public static final int DATABASE_VERSION = 1;
	
	public static final String  DATABASE_CREATE_SQL =
			"create table" + DATABASE_TABLE + "(" + KEY_ROWID + " integer primary key autoincrement, " 
			+ KEY_TITLE + " text not null," + KEY_TASKID + " integer not null, " + KEY_DATECOMPLETED + "integer not null," +
			KEY_TASKDESCRIPTION  + "text not null" + KEY_TIMECOMPLETED + " text not null" + KEY_ISCOMPLETED + "" ;
	
	// context of application who uses us
	private final Context context;
	
	private DatabaseHelper mDBHelper;
	private SQLiteDatabase mDatabase;
	
	public TaskDataBaseAdapter(Context context){
		this.context = context;
		mDBHelper = new DatabaseHelper(context);
		
		//open database
		try{
			open();
		} catch (SQLException e){
			Log.e(TAG, "SQLException on openning database " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	// open database
	public TaskDataBaseAdapter open() throws SQLException{
		mDatabase = mDBHelper.getWritableDatabase();
		return this;
	}
	
	// close database
	public void close(){
		mDBHelper.close();
	}
	
	// add new set of values to 
	public long createTask(String title, int taskId, int dateComp, String taskDescrip, int timeComp, boolean taskComp){
		
		//Create row's data
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_TASKID, taskId);
		initialValues.put(KEY_DATECOMPLETED, dateComp);
		initialValues.put(KEY_TASKDESCRIPTION, taskDescrip);
		initialValues.put(KEY_TIMECOMPLETED, timeComp);
		initialValues.put(KEY_ISCOMPLETED, taskComp);
		/*
		 
		long insertId = mDatabase.insert(DATABASE_TABLE, null, initialValues);
		
		Cursor cursor = mDatabase.query(DATABASE_TABLE, ALL_KEYS, KEY_ROWID +  " = " + insertId, null, null, null, null);				
		cursor.moveToFirst();
		Task newTask = cursorToTask(cursor);
		cursor.close();*/
		return mDatabase.insert(DATABASE_TABLE, null, initialValues); 
		
	}
	
	public boolean delete(long rowId){
		String where = KEY_ROWID + "=" + rowId;
		return mDatabase.delete(DATABASE_TABLE, where, null) != 0;	
	}
	
	/*
	protected Task cursorToTask(Cursor cursor){
		
		Task task = new Task();
		task.setId(cursor.getLong(columnIndex))
		return task;
		
	}*/
	
	
	public void deleteAll(){
		Cursor cursor = getAllRows();
		long rowId = cursor.getColumnIndexOrThrow(KEY_ROWID);
		if (cursor.moveToFirst()){
			do{
				delete(cursor.getLong((int) rowId));
			} while (cursor.moveToNext());
		}
		cursor.close();
	}
	
	// Return all data in the database
	public Cursor getAllRows(){
		String where = null;
		Cursor cursor = mDatabase.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, null, null);
		
		if ( cursor != null){
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	// Get a specific row(by rowId)
	public Cursor getRow(long rowId){
		String where = KEY_ROWID + " = " + rowId;
		Cursor cursor =  mDatabase.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, null, null);
		
		if (cursor != null){
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public boolean updateRow(long rowId,String title, int taskId, int dateComp, String taskDescrip, int timeComp, boolean taskComp){
		String where = KEY_ROWID + "=" + rowId;
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_TITLE, title);
		newValues.put(KEY_TASKID, taskId);
		newValues.put(KEY_DATECOMPLETED, dateComp);
		newValues.put(KEY_TASKDESCRIPTION, taskDescrip);
		newValues.put(KEY_TIMECOMPLETED, timeComp);
		newValues.put(KEY_ISCOMPLETED, taskComp);
		
		// insert in database
		return mDatabase.update(DATABASE_TABLE, newValues, where, null) != 0 ;
		
	}
	
	
	private static class DatabaseHelper extends SQLiteOpenHelper{
		
		DatabaseHelper(Context context){
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DATABASE_CREATE_SQL);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading application's database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data!");
			
			// Destroy old database
			_db.execSQL("DROP TABLE IF EXISTS" + DATABASE_TABLE);
			
			// Recreate new database
			onCreate(_db);	
		}
	}
}
