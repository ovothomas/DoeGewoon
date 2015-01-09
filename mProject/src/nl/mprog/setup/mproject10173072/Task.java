package nl.mprog.setup.mproject10173072;

import java.util.UUID;

public class Task {
	
	//creating members
	private UUID mId;
	private String mTaskTitle;
	
	public Task(){
		
		// generate Id's for the tasks
		mId = UUID.randomUUID(); 
		
	}

	public String getTaskTitle() {
		return mTaskTitle;
	}

	public void setTaskTitle(String taskTitle) {
		mTaskTitle = taskTitle;
	}

	public UUID getId() {
		return mId;
	}
}
