package nl.mprog.setup.mproject10173072;
/*
 * This activity creates the table for the database
 * It also contains methods to retrieve all the
 * tasks from the database table, delete a task,
 * or update a task.
 */
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TaskDataBase {
	
	//for logging
	private static final String TAG = "TaskDatabase";
	private SQLiteDatabase mDatabase;
	private TaskDataBaseHelper mTaskDataBaseHelper;
	private Context context;
	private String[] mAllColumns = {
			TaskDataBaseHelper.COLUMN_TASK_ID, TaskDataBaseHelper.COLUMN_TITLE, TaskDataBaseHelper.COLUMN_TASK_DETAILS, 
			TaskDataBaseHelper.COLUMN_TASK_DATE, TaskDataBaseHelper.COLUMN_TASK_COMPLETED};

	public TaskDataBase(Context context){
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
	public TaskDataBase open() throws SQLException{
		mDatabase = mTaskDataBaseHelper.getWritableDatabase();
		return this;
	}
	
	// close database
	public void close(){
		mTaskDataBaseHelper.close();
	}
	
	// add new set of values to 
	public Task createTask(String title, String details, long date, int completed){
		
		//Create row's data
		ContentValues initialValues = new ContentValues();
		initialValues.put(TaskDataBaseHelper.COLUMN_TITLE, title);
		initialValues.put(TaskDataBaseHelper.COLUMN_TASK_DETAILS, details);
		initialValues.put(TaskDataBaseHelper.COLUMN_TASK_DATE, date);
		initialValues.put(TaskDataBaseHelper.COLUMN_TASK_COMPLETED, completed);
		
		long insertId = mDatabase
				.insert(TaskDataBaseHelper.TABLE_TASK, null, initialValues);
		Cursor cursor = mDatabase.query(TaskDataBaseHelper.TABLE_TASK, mAllColumns,
				TaskDataBaseHelper.COLUMN_TASK_ID+ " = " + insertId, null, null,
				null, null);
		cursor.moveToFirst();
		Task newTask = cursorToTask(cursor);
		
		// close cursor
		cursor.close();
		return newTask;
	}
	
	// function to deletask in the database
	public void deleteTask(Task task){
		long id = task.getId();
		System.out.println("the deleted company has the id: " + id);
		mDatabase.delete(TaskDataBaseHelper.TABLE_TASK, TaskDataBaseHelper.COLUMN_TASK_ID
				+ " = " + id, null);
	}
	
	// getting all the tasks
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

			// close the cursor
			cursor.close();
		}
		return listTask;
	}
	
	// get uncompleted Tasks
	public List<Task> getUncompletedTask(){
		List<Task> listUncompletedTask = new ArrayList<Task>();
		
		Cursor cursor = mDatabase.query(TaskDataBaseHelper.TABLE_TASK, mAllColumns, 
				TaskDataBaseHelper.COLUMN_TASK_COMPLETED + " = ?",
				new String[] { "0" }, null, null, null);
		if (cursor != null){
			cursor.moveToFirst();
			while (!cursor.isAfterLast()){
				Task task = cursorToTask(cursor);
				listUncompletedTask.add(task);
				cursor.moveToNext();	
			}
			
			// make sure to close cursor
			cursor.close();
		}
		return listUncompletedTask;
	}
				
	// get all the tasks
	public List<Task> getAllCompletedTasks(){
		List<Task> completedTasks = new ArrayList<Task>();
		Cursor cursor = mDatabase.query(TaskDataBaseHelper.TABLE_TASK, mAllColumns,
				TaskDataBaseHelper.COLUMN_TASK_COMPLETED + " = ?",	
				new String[] { "1" }, null, null, null);
		if (cursor != null){
			cursor.moveToFirst();
			while (!cursor.isAfterLast()){
				Task task = cursorToTask(cursor);
				completedTasks.add(task);
				cursor.moveToNext();	
			}
			
			// make sure to close cursor
			cursor.close();
		}
		return completedTasks;		
	}
	
	// get completed tasks
	public List<Date> getCountCompleted(){
		List<Date> getCountDate = new ArrayList<Date>();
		
		Cursor cursor = mDatabase.rawQuery("SELECT COUNT( " + TaskDataBaseHelper.COLUMN_TASK_DATE + "), " +  TaskDataBaseHelper.COLUMN_TASK_DATE +  " FROM " + TaskDataBaseHelper.TABLE_TASK + " WHERE " 
		+ TaskDataBaseHelper.COLUMN_TASK_COMPLETED + " = ? GROUP BY " + TaskDataBaseHelper.COLUMN_TASK_DATE, new String[] { "1" });
		
		if (cursor != null){
			cursor.moveToFirst();
			while (!cursor.isAfterLast()){
				Date date  = cursorToDate(cursor);
				getCountDate.add(date);
				cursor.moveToNext();	
			}
			
			// make sure to close cursor
			cursor.close();
		}
		return getCountDate;	
	}
	
	// getting a specific tasks
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
	
	// updating a specific tasks
	public boolean updateTaskById(Long id, String title, String details, long date, int taskCompleted) {
		String where = TaskDataBaseHelper.COLUMN_TASK_ID + "=" + id;
		ContentValues newValues = new ContentValues();
		newValues.put(TaskDataBaseHelper.COLUMN_TITLE, title);
		newValues.put(TaskDataBaseHelper.COLUMN_TASK_DETAILS, details);
		newValues.put(TaskDataBaseHelper.COLUMN_TASK_DATE, date);
		newValues.put(TaskDataBaseHelper.COLUMN_TASK_COMPLETED, taskCompleted);
		
		// Insert it into the database.
		return mDatabase.update(TaskDataBaseHelper.TABLE_TASK, newValues, where, null) != 0;
	}
	
	// cursor and add a task to a row
	protected Task cursorToTask(Cursor cursor) {
		Task task = new Task();
		task.setId(cursor.getLong(0));
		task.setTaskTitle(cursor.getString(1));
		task.setTaskDetails(cursor.getString(2));
		task.setTaskDate(cursor.getLong(3));
		task.setCompleted(cursor.getInt(4));
		return task;
	}	
	
	// cursor and add date to a row
	protected Date cursorToDate(Cursor cursor){
		Date date = new Date();
		date.setCount(cursor.getInt(0));
		date.setTaskDate(cursor.getLong(1));
		return date;
	}
}
