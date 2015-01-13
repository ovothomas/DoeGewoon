package nl.mprog.setup.mproject10173072;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class TaskPagerActivity extends FragmentActivity {
	//viewPager
	private ViewPager mTaskViewPager;
	//Arraylist of tasks
	private ArrayList<Task> mTasks;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//instantiating viewpager and settings its content view
		mTaskViewPager = new ViewPager(this);
		mTaskViewPager.setId(R.id.viewPager);
		setContentView(mTaskViewPager);
		
		// retrieving tasks from TaskStorage
		mTasks = TaskStorage.get(this).getTasks();
		
		//getting activities instance of FragmentManager
		FragmentManager fm = getSupportFragmentManager();
		mTaskViewPager.setAdapter(new FragmentStatePagerAdapter(fm){

			/*
			 * Adding fragments returned to activity and helping
			 * ViewPager identify the fragment's view so that they can
			 * be placed correctly with FragmentStatePagerAdapter
			 */
			
			// fetch the task instance for that particular position
			// and use its ID to create and return a properly configured
			// TaskFragment
			
			@Override
			public Fragment getItem(int pos) {
				// TODO Auto-generated method stub
				Task task = mTasks.get(pos);
				return TaskFragment.newInstance(task.getId());
			}
			
			// return how many items are in the arraylist
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mTasks.size();
			}
			
		});
		
		UUID taskId = (UUID)getIntent().getSerializableExtra(TaskFragment.EXTRA_TASK_ID);
		// loop through the arraylist of tasks and 
		// check the id of each task. when the id matches
		// that of the intent extra, set the item to the 
		// index of that Task
		for(int x = 0; x < mTasks.size(); x++){
			if(mTasks.get(x).getId().equals(taskId)){
				// show this item in ViewPager
				mTaskViewPager.setCurrentItem(x);
			}
		}
		
		/*
		 * Implement OnpageChangeListener
		 */
	}
}
