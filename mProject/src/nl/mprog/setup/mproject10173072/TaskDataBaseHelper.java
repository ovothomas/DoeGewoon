package nl.mprog.setup.mproject10173072;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDataBaseHelper extends SQLiteOpenHelper {
	
	//columns task table
	public static final String TABLE_TASK = "tasks";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_TASK_ID = "_id";
	public static final String COLUMN_TASK_DETAILS = "details";
	public static final String COLUMN_TASK_DATE = "date";
	public static final String COLUMN_TASK_COMPLETED = "completed";
	public static final String DATABASE_NAME = "taskdatabase.db";
	public static final int DATABASE_VERSION = 6;
	
	// create sql table
	public static final String SQL_CREATE_TABLE_TASKS = " CREATE TABLE " + TABLE_TASK + 
			"(" + COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TITLE + 
			" TEXT NOT NULL, " + COLUMN_TASK_DETAILS + " TEXT NOT NULL,"+ COLUMN_TASK_DATE + 
			" INTEGER NOT NULL," + COLUMN_TASK_COMPLETED + " INTEGER NOT NULL" + ");";
	
	public TaskDataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLE_TASKS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_TASK);
		
		onCreate(db);	
	}
	
	public TaskDataBaseHelper(Context context, String name, CursorFactory factory,int version) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
	}

}
