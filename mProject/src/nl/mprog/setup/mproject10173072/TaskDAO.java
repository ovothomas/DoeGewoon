package nl.mprog.setup.mproject10173072;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TaskDAO {
	
	//for logging
	private static final String TAG = "TaskDAO";
	
	private SQLiteDatabase mDatabase;
	private TaskDataBaseHelper mTaskDataBaseHelper;
	private Context context;
	private String[] mAllColumns = {
			TaskDataBaseHelper.COLUMN_TASK_ID, TaskDataBaseHelper.COLUMN_TITLE, TaskDataBaseHelper.COLUMN_TASK_DETAILS, TaskDataBaseHelper.COLUMN_TASK_DATE};

	public TaskDAO(Context context){
		this.context = context;
		mTaskDataBaseHelper = new TaskDataBaseHelper(context);
		
		//open database
		try{
			open();
		} catch (SQLException e){
			Log.e(TAG, "SQLException on openning database " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	// open database
	public TaskDAO open() throws SQLException{
		mDatabase = mTaskDataBaseHelper.getWritableDatabase();
		return this;
	}
	
	// close database
	public void close(){
		mTaskDataBaseHelper.close();
	}
	
	// add new set of values to 
	public Task createTask(String title, String details, long date){
		
		//Create row's data
		ContentValues initialValues = new ContentValues();
		initialValues.put(TaskDataBaseHelper.COLUMN_TITLE, title);
		initialValues.put(TaskDataBaseHelper.COLUMN_TASK_DETAILS, details);
		initialValues.put(TaskDataBaseHelper.COLUMN_TASK_DATE, date);
		

		long insertId = mDatabase
				.insert(TaskDataBaseHelper.TABLE_TASK, null, initialValues);
	
		Cursor cursor = mDatabase.query(TaskDataBaseHelper.TABLE_TASK, mAllColumns,
				TaskDataBaseHelper.COLUMN_TASK_ID+ " = " + insertId, null, null,
				null, null);
		cursor.moveToFirst();
		Task newTask = cursorToTask(cursor);
		cursor.close();
		return newTask;
		
	}
	
	public void deleteTask(Task task){
		long id = task.getId();
		System.out.println("the deleted company has the id: " + id);
		mDatabase.delete(TaskDataBaseHelper.TABLE_TASK, TaskDataBaseHelper.COLUMN_TASK_ID
				+ " = " + id, null);
	}
	
	public List<Task> getAllTasks(){
		List<Task> listTask = new ArrayList<Task>();
		
		Cursor cursor = mDatabase.query(TaskDataBaseHelper.TABLE_TASK, mAllColumns, null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Task task = cursorToTask(cursor);
				listTask.add(task);
				cursor.moveToNext();
			}

			// make sure to close the cursor
			cursor.close();
		}
		return listTask;
		
	}
	
	public Task getTaskById(Long id) {
		Cursor cursor = mDatabase.query(TaskDataBaseHelper.TABLE_TASK, mAllColumns,
				TaskDataBaseHelper.COLUMN_TASK_ID + " = ?",
				new String[] { String.valueOf(id) }, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}

		Task task = cursorToTask(cursor);
		return task;
	}
	
	public boolean updateTaskById(Long id, String title, String details, long date) {
		String where = TaskDataBaseHelper.COLUMN_TASK_ID + "=" + id;
		ContentValues newValues = new ContentValues();
		newValues.put(TaskDataBaseHelper.COLUMN_TITLE, title);
		newValues.put(TaskDataBaseHelper.COLUMN_TASK_DETAILS, details);
		newValues.put(TaskDataBaseHelper.COLUMN_TASK_DATE, date);
		// Insert it into the database.
		return mDatabase.update(TaskDataBaseHelper.TABLE_TASK, newValues, where, null) != 0;
	}
	
	
	protected Task cursorToTask(Cursor cursor) {
		Task task = new Task();
		task.setId(cursor.getLong(0));
		task.setTaskTitle(cursor.getString(1));
		task.setTaskDetails(cursor.getString(2));
		task.setTaskDate(cursor.getLong(3));
		
		return task;
	}	 	 
}
