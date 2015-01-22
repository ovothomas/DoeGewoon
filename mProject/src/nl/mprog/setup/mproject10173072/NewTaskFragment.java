package nl.mprog.setup.mproject10173072;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class NewTaskFragment extends Fragment {
	
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
	private Button mTaskTimeButton;
	private Button mAddTask;
	private int mYear, mMonth, mDay;
	// SQL Database
	private TaskDAO mDatabase;
	Calendar calendar = Calendar.getInstance(); 

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		mDatabase = new TaskDAO(getActivity());
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
		//Wiring up the edittext to respond to user input
		mTaskTitle = (EditText)view.findViewById(R.id.task_title);
		//Setting and wiring completed checkbox
		mTaskCompletedCheckBox = (CheckBox)view.findViewById(R.id.task_completed);
		//mTaskCompletedCheckBox.setChecked(mTask.isCompleted());
		mTaskCompletedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				mTask.setCompleted(isChecked);
			}
		});
		
		//Setting and wiring the date button to show date in custom format
		mTaskDateButton = (Button)view.findViewById(R.id.task_date);
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM dd, yyyy");
		String dateString = sdf.format(calendar.getTimeInMillis());   
		mTaskDateButton.setText(dateString);
		//mTaskDateButton.setText(DateFormat.format("EEEE, MMM dd, yyyy", mYear));
		mTaskDateButton.setEnabled(false);
		
		//setting up timeButton to show the time the task was made
		mTaskTimeButton = (Button)view.findViewById(R.id.task_time);
		SimpleDateFormat stf = new SimpleDateFormat("hh:mm");
		String timeString = stf.format(calendar.getTimeInMillis());
		
		mTaskTimeButton.setText(timeString);
		mTaskTimeButton.setEnabled(false);
		
		mAddTask = (Button)view.findViewById(R.id.addButton);
		mAddTask.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Editable taskTitle =  mTaskTitle.getText();
				Editable taskDetails = mTaskDetails.getText();
				
				String dateString = mTaskDateButton.getText().toString();
				SimpleDateFormat stf = new SimpleDateFormat("EEEE, MMM dd, yyyy");
				try {
					stf.parse(dateString);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				long time = stf.getCalendar().getTimeInMillis();
				
				Task createdTask = mDatabase.createTask(taskTitle.toString(), taskDetails.toString(), time);
				
				
				List<Task> getListTask = mDatabase.getAllTasks();
				Log.d(TAG, "added company : " + createdTask.getTaskTitle());
				Log.d(TAG1, "Lenght list: " + getListTask.size());
				Intent intent = new Intent(getActivity(), TaskListActivity.class);
				startActivity(intent);
				getActivity().finish();		 
			}	 
		});
		
		return view;
	}
	
	// Attach arguments bundle to a fragment
	public static NewTaskFragment newInstance(Long l){
		
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_TASK_ID, l);
		
		// create Fragment instance of that particular id
		NewTaskFragment fragment = new NewTaskFragment();
		fragment.setArguments(args);
		
		return fragment;	
	}

	// using the actionbar to navigate to the homepage
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	    	if (NavUtils.getParentActivityName(getActivity()) != null){
	    	NavUtils.navigateUpFromSameTask(getActivity());
	    	//Long taskId = (Long)getArguments().getSerializable(EXTRA_TASK_ID);
	    	//String taskTitle =  mTaskTitle.getText().toString();
			//String taskDetails = mTaskDetails.getText().toString(); 
			//mDatabase.updateTaskById(taskId, taskTitle, taskDetails);
	    	}
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		mDatabase.close();
	}
}
