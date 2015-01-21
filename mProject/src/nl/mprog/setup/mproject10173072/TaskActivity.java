package nl.mprog.setup.mproject10173072;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

public class TaskActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task);
		
		//adding fragment in activity and calling support library 
		//to call getSupportFragmentManager
		FragmentManager fm = getSupportFragmentManager();
		Fragment frag = fm.findFragmentById(R.id.fragmentContainer);
		
		// if there is no fragment create new one
		if (frag == null){
			frag = createFragment();
			fm.beginTransaction().add(R.id.fragmentContainer, frag).commit();
		}
	}
	
	protected Fragment createFragment() {
		
		//retrieve the extra from CrimeActivity's intent 
		//and pass it into CrimeFragment.newInstance(UUID).
		Long taskId = (Long)getIntent().getSerializableExtra(TaskFragment.EXTRA_TASK_ID);
		
		return TaskFragment.newInstance(taskId);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
