package nl.mprog.setup.mproject10173072;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class TaskFragment extends Fragment {
	
	public static final String TAG = "AddTaskActivity";
	public static final String TAG1 = "Listtask";
	public static final String EXTRA_TASK_ID =
			"nl.mprog.setup.mproject10173072.task_id";
	public static final String DPDIALOG_DATE = "date";
	public static final String DPDIALOG_TIME = "time";
	public static final int REQUEST_DATE = 0;
	public static final int REQUEST_TIME = 1;
	
	//member variable for Task
	private Task mTask;
	private EditText mTaskTitle;
	private EditText mTaskDetails;
	private Button mTaskDateButton;
	private CheckBox mTaskCompletedCheckBox;
	private Button mAddTask;
	private Button mSendButton;
	
	// SQL Database
	private TaskDataBase mDatabase;
	
	private void updateDate(){
		mTaskDateButton.setText(DateFormat.format("EEEE, MMM dd, yyyy",mTask.getTaskDate()));
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		// get id of task to create fragment
		Long taskId = (Long)getArguments().getSerializable(EXTRA_TASK_ID);
		mDatabase = new TaskDataBase(getActivity());
		mTask = mDatabase.getTaskById(taskId);
		
		//Enabling the app icon as Up button 
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);	
	}
	
	// Inflate the layout view
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_task, parent, false);
		
		// Wiring up EditText to respond to user input
		mTaskDetails = (EditText)view.findViewById(R.id.task_details);
		mTaskDetails.setText(mTask.getTaskDetails());
		
		//Wiring up the edittext to respond to user input
		mTaskTitle = (EditText)view.findViewById(R.id.task_title);
		mTaskTitle.setText(mTask.getTaskTitle());
		
		//Setting and wiring completed checkbox
		mTaskCompletedCheckBox = (CheckBox)view.findViewById(R.id.task_completed);
		mTaskCompletedCheckBox.setChecked(mTask.getCompleted() == 1 ? true : false);
		mTaskCompletedCheckBox.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mTask.setCompleted(mTaskCompletedCheckBox.isChecked() == true ? 1 : 0);
				Log.d(TAG, "added company : " + mTask.getCompleted());
			}
		});
		 
		//Setting and wiring the date button to show date in custom format
		mTaskDateButton = (Button)view.findViewById(R.id.task_date);
		updateDate();
		mTaskDateButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager datepicker = getActivity().getSupportFragmentManager();
				DatePickerFragment dialog = DatePickerFragment.newInstance(mTask.getTaskDate());
				dialog.setTargetFragment(TaskFragment.this, REQUEST_DATE);
				dialog.show(datepicker, DPDIALOG_DATE);
			}});
	
		// add task to database button
		mAddTask = (Button)view.findViewById(R.id.addButton);
		mAddTask.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				 update();
			}	 
		});
		
		// send the task by email or sms. your own choosing
		mSendButton = (Button)view.findViewById(R.id.send_task) ;
		mSendButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_TEXT, getTaskReport());
				intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.task_report_subject));
				startActivity(intent);
			}
		});
		
		return view;
	}
	
	// Attach arguments bundle to a fragment
	public static TaskFragment newInstance(Long l){
		
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_TASK_ID, l);
		
		// create Fragment instance of that particular id
		TaskFragment fragment = new TaskFragment();
		fragment.setArguments(args);
		
		return fragment;
		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) return;
		if (requestCode == REQUEST_DATE) {
			long date = data.getLongExtra(DatePickerFragment.EXTRA_DATE, 0);
			mTask.setTaskDate(date);
			updateDate();
		} 
	}
	
	// using the actionbar to navigate to the homepage
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	    	if (NavUtils.getParentActivityName(getActivity()) != null){
	    	NavUtils.navigateUpFromSameTask(getActivity());
	    	update();
	        return true;
	        }
	    }
	   return super.onOptionsItemSelected(item);
	}
	
	// function to update the task 
	private void update(){
		
		// getting the variables to store
		Long taskId = (Long)getArguments().getSerializable(EXTRA_TASK_ID);
    	String taskTitle =  mTaskTitle.getText().toString();
		String taskDetails = mTaskDetails.getText().toString();
		int taskCompleted = mTask.getCompleted();
		String dateString = mTaskDateButton.getText().toString();
		SimpleDateFormat stf = new SimpleDateFormat("EEEE, MMM dd, yyyy");
		try {
			stf.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long date = stf.getCalendar().getTimeInMillis();
		mDatabase.updateTaskById(taskId, taskTitle, taskDetails, date, taskCompleted );
		Intent intent = new Intent(getActivity(), TaskListActivity.class);
		startActivity(intent);
		getActivity().finish();	
	}
	
	// getting needed information to be able to send
	private String getTaskReport(){
		String isCompletedString = null;
		
		// see if the task is completed or not
		boolean task = mTask.getCompleted()!= 0; 
		if (task){
			isCompletedString = getString(R.string.task_report_completed);
		} else {
			isCompletedString = getString(R.string.task_report_not_completed);
		}
		
		// getting the date
		String dateString = mTaskDateButton.getText().toString();
		SimpleDateFormat stf = new SimpleDateFormat("EEEE, MMM dd, yyyy");
		try {
			stf.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// string report to send
		String taskreport = getString(R.string.task_report, mTask.getTaskTitle(), dateString, isCompletedString, mTask.getTaskDetails());
		
		return taskreport;
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		mDatabase.close();
	}
}
