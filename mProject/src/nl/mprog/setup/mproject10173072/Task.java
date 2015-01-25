package nl.mprog.setup.mproject10173072;

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
	private long mTaskDate;
	private int mCompleted;
	private String mTaskDetails;
	
	public Task(){
		
	}
	
	@Override
	public String toString(){
		return mTaskTitle;
	}

	public long getTaskDate() {
		return mTaskDate;
	}

	public void setTaskDate(long taskDate) {
		mTaskDate = taskDate;
	}

	public int getCompleted() {
		return mCompleted;
	}

	public void setCompleted(int completed) {
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
