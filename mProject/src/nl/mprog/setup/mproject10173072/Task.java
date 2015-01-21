package nl.mprog.setup.mproject10173072;

import java.util.Date;
import java.util.UUID;

public class Task {
	
	/*
	 * This is an instance of a single task when it 
	 * is created. Task has unique identifier, a title,
	 * an editbox in which the task details will be
	 * stored, date and if the task is completed.
	 */
	
	//creating members
	private Long mId;
	private String mTaskTitle;
	private Date mTaskDate;
	private boolean mCompleted;
	private String mTaskDetails;
	
	public Task(){
		
		// generate Id's for the tasks
		mTaskDate = new Date();
	}
	
	@Override
	public String toString(){
		return mTaskTitle;
	}

	public Date getTaskDate() {
		return mTaskDate;
	}

	public void setTaskDate(Date taskDate) {
		mTaskDate = taskDate;
	}

	public boolean isCompleted() {
		return mCompleted;
	}

	public void setCompleted(boolean completed) {
		mCompleted = completed;
	}

	public void setId(Long id) {
		mId = id;
	}

	public String getTaskTitle() {
		return mTaskTitle;
	}

	public void setTaskTitle(String taskTitle) {
		mTaskTitle = taskTitle;
	}

	public Long getId() {
		return mId;
	}

	public String getTaskDetails() {
		return mTaskDetails;
	}

	public void setTaskDetails(String taskDetails) {
		mTaskDetails = taskDetails;
	}
}
