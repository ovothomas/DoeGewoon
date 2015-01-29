package nl.mprog.setup.mproject10173072;
/*
 * Viewpager activity the helps
 * view the listarray through sliding
 * left and right
 */
import java.util.List;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class TaskPagerActivity extends FragmentActivity {
	//viewPager
	private ViewPager mTaskViewPager;
	
	//Arraylist of tasks;
	private List<Task> mListTasks;
	private TaskDataBase mDatabase;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//instantiating viewpager and settings its content view
		mTaskViewPager = new ViewPager(this);
		mTaskViewPager.setId(R.id.viewPager);
		setContentView(mTaskViewPager);
		
		//get data from database
		mDatabase = new TaskDataBase(this);
		mListTasks = mDatabase.getAllTasks();
		
		//getting activities instance of FragmentManager
		FragmentManager fm = getSupportFragmentManager();
		mTaskViewPager.setAdapter(new FragmentStatePagerAdapter(fm){
			
			// fetch the task instance for that particular position
			// and use its ID to create and return a properly configured
			// TaskFragment
			@Override
			public Fragment getItem(int pos) {
				// TODO Auto-generated method stub
				Task task = mListTasks.get(pos);
				return TaskFragment.newInstance(task.getId());
			}
			
			// return how many items are in the arraylist
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mListTasks.size();
			}
		});
		
		Long taskId = (Long)getIntent().getSerializableExtra(TaskFragment.EXTRA_TASK_ID);
		
		// loop through the arraylist of tasks and 
		// check the id of each task. when the id matches
		// that of the intent extra, set the item to the 
		// index of that Task
		for(int x = 0; x < mListTasks.size(); x++){
			if(mListTasks.get(x).getId().equals(taskId)){
				// show this item in ViewPager
				mTaskViewPager.setCurrentItem(x);
			}
		}
		
		// Setting the title of the task on the action bar
		mTaskViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			public void onPageScrollStateChanged(int state) { }
	
			public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) { }
			public void onPageSelected(int pos) {
				Task task = mListTasks.get(pos);
				if (task.getTaskTitle() != null) {
					setTitle(task.getTaskTitle());
				}
			}
		});
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		mDatabase.close();
	}
}
