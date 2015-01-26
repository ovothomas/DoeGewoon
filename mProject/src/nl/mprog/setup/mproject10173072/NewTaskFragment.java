package nl.mprog.setup.mproject10173072;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
	private EditText mTaskTitle;
	private EditText mTaskDetails;
	private Button mTaskDateButton;
	private CheckBox mTaskCompletedCheckBox;
	private Button mAddTask;
	private Button mSendButton;
	private Task mTask;
	
	 
	// SQL Database
	private TaskDataBase mDatabase;
	Calendar calendar = Calendar.getInstance(); 
	
	private void addTask(){
		Editable taskTitle =  mTaskTitle.getText();
		Editable taskDetails = mTaskDetails.getText();
		int taskCompleted = mTaskCompletedCheckBox.isChecked() == true ? 1:0;
		String dateString = mTaskDateButton.getText().toString();
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM dd, yyyy");
		try {
			sdf.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long time = sdf.getCalendar().getTimeInMillis();
		mDatabase.createTask(taskTitle.toString(), taskDetails.toString(), time, taskCompleted);	
		Intent intent = new Intent(getActivity(), TaskListActivity.class);
		startActivity(intent);
		getActivity().finish();		 	
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		mDatabase = new TaskDataBase(getActivity());
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
		
		// since we are saving in database we cannot save the checkbox as a boolean
		// so we convert boolean to an integer to be able to save
		mTaskCompletedCheckBox = (CheckBox)view.findViewById(R.id.task_completed);
		mTaskCompletedCheckBox.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mTask.setCompleted(mTaskCompletedCheckBox.isChecked() == true ? 1 : 0);
				
			}
		});
		
		//Setting and wiring the date button to show date in custom format
		mTaskDateButton = (Button)view.findViewById(R.id.task_date);
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM dd, yyyy");
		String dateString = sdf.format(calendar.getTimeInMillis());   
		mTaskDateButton.setText(dateString);
		mTaskDateButton.setEnabled(false);
		
		// Button to add task to database
		mAddTask = (Button)view.findViewById(R.id.addButton);
		mAddTask.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
			addTask();			
			}	 
		});
		
		// a button to send task
		mSendButton = (Button)view.findViewById(R.id.send_task) ;
		mSendButton.setEnabled(false);
		/*
		mSendButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_TEXT, getTaskReport());
				intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.task_report_subject));
				startActivity(intent);
			}
		});*/
		
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
	    	 addTask();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	/*
	private String getTaskReport(){
		String isCompletedString = null;
		boolean task = mTask.getCompleted()!= 0; 
		if (task){
			isCompletedString = getString(R.string.task_report_completed);
		} else {
			isCompletedString = getString(R.string.task_report_not_completed);
		}
		
		String dateString = mTaskDateButton.getText().toString();
		SimpleDateFormat stf = new SimpleDateFormat("EEEE, MMM dd, yyyy");
		try {
			stf.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String taskreport = getString(R.string.task_report, mTask.getTaskTitle(), dateString, isCompletedString, mTask.getTaskDetails());
		
		return taskreport;	
	}*/
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		mDatabase.close();
	}
}
