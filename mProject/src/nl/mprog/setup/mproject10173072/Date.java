package nl.mprog.setup.mproject10173072;

/*
 *  This is an object created for my database
 *  to be able to get the total number of
 *  distinct dates in my database.
 */

public class Date {
	private long mTaskDate;
	private int mCount;
	
	public Date(){
	}

	public long getTaskDate() {
		return mTaskDate;
	}

	public void setTaskDate(long taskDate) {
		mTaskDate = taskDate;
	}

	public int getCount() {
		return mCount;
	}

	public void setCount(int count) {
		mCount = count;
	}

}
