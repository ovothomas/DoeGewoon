package nl.mprog.setup.mproject10173072;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

public class TaskStorage {
	
	public static TaskStorage sTaskStorage;
	
	//Arraylist for task to be stored in
	private ArrayList<Task> mTasks;
	
	
	private TaskStorage(Context appContext){
		
		// filling my arraylist with random task.
		mTasks = new ArrayList<Task>();
		for (int i = 1; i < 20; i++){
			Task context = new Task();
			context.setTaskTitle("Task #" + i);
			mTasks.add(context);
		}
	}
	
	// return the task from the arraylist
	public ArrayList<Task> getTasks(){
		return mTasks;
	}
	
	//return the task with that particular id
	public Task getTask(UUID id){
		for (Task task : mTasks){
			if(task.getId().equals(id))
				return task;
		}
		return null;
	}
	public static TaskStorage get(Context context){
		if(sTaskStorage == null){
			sTaskStorage = new TaskStorage(context.getApplicationContext());
		}
		return sTaskStorage;
	}

}