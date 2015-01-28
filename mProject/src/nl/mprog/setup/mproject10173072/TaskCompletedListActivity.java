package nl.mprog.setup.mproject10173072;
/*
 * This activity handles the listview
 * of the task completed. The user
 * can see the tasks completed.
 */

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class TaskCompletedListActivity extends Activity implements OnItemLongClickListener{
	// variables
	private List<Task>	mCompletedListTasks;
	private TaskDataBase mDatabase;
	private TaskCompletedListAdapter mAdapter;
	private ListView mListView;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list);
		
		//configure the actionbar
		getActionBar().setTitle(R.string.task_title);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(255, 164, 9, 9)));
		
		// Initiate the views
		initViews();
		
		// open database and set listadaper to display completed
		mDatabase = new TaskDataBase(this);
		mCompletedListTasks = mDatabase.getAllCompletedTasks();
		mAdapter = new TaskCompletedListAdapter(this, mCompletedListTasks, mDatabase);
		mListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}
	
	// initiate views
	private void initViews(){
		this.mListView = (ListView)findViewById(R.id.list_task);
		this.mListView.setOnItemLongClickListener(this);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		Task task = mAdapter.getItem(position);
		showDeleteDialogConfirmation(task);
		return true;
	}
	
	public void onResume(){
		super.onResume();
		mCompletedListTasks = mDatabase.getAllCompletedTasks();
		mAdapter.setItems(mCompletedListTasks);
		mAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		mDatabase.close();
	}
	
	// show the user a dialog to delete a task
	private void showDeleteDialogConfirmation(final Task task){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("DELETE");
		alertDialogBuilder.setMessage("Sure you want to delete?  \"" + task.getTaskTitle());
		alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// delete task
				if (mDatabase != null){
					mDatabase.deleteTask(task);
					mCompletedListTasks.remove(task);
					mAdapter.setItems(mCompletedListTasks);
					mAdapter.notifyDataSetChanged();
				}
				dialog.dismiss();
				Toast.makeText(TaskCompletedListActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
			}
		});
		
		// set neutral button OK
        alertDialogBuilder.setNeutralButton(android.R.string.no, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Dismiss the dialog
                dialog.dismiss();
			}
		});
        
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
	}
}
