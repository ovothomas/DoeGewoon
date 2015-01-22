package nl.mprog.setup.mproject10173072;

import java.util.ArrayList;

import android.content.Context;


public class TaskLab {
	
	private static TaskLab sTaskLab;
	private Context mAppContext;
	private ArrayList<Task> mTask;
	
	private TaskLab(Context appContext) {
		mAppContext = appContext;
		mTask = new ArrayList<Task>();
	}
	
	public static TaskLab get(Context c) {
		if (sTaskLab == null) {
			sTaskLab = new TaskLab(c.getApplicationContext());
		}
		return sTaskLab;
	}
	
	public ArrayList<Task> getTask() {
		return mTask;
		}
	
	public void addTask(Task task){
		mTask.add(task);
	}
		

}
