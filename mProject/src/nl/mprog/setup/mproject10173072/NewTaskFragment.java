package nl.mprog.setup.mproject10173072;
/*
 * This Activity is opened when a task
 * is being freshly created. Some button
 * are disabled as it is not important
 * in this particular fragment
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
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
	
	// member variable for Task
	private EditText mTaskTitle;
	private EditText mTaskDetails;
	private Button mTaskDateButton;
	private Button mAddTask;
	private Button mSendButton;
	private CheckBox mTaskCompletedCheckBox;
	
	// SQL Database
	private TaskDataBase mDatabase;
	Calendar calendar = Calendar.getInstance(); 
	
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
		
		// In the new TaskFragment the user cannot complete his task so
		// the button is disabled
		mTaskCompletedCheckBox = (CheckBox)view.findViewById(R.id.task_completed);
		mTaskCompletedCheckBox.setEnabled(false);
		
		// Setting and wiring the date button to show date in custom format
		// when the task is being created at first. Button shows only the date
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
		
		return view;
	}
	
	// Attach arguments bundle to a fragment
	// contains the id of a task so the viewpager
	// will make that particular task
	public static NewTaskFragment newInstance(Long l){
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_TASK_ID, l);
		
		// create Fragment instance of that particular id
		NewTaskFragment fragment = new NewTaskFragment();
		fragment.setArguments(args);
		return fragment;	
	}
	
	// function to add a task to database
	// by getting the necessary information
	// formating the date and converting 
	// ischecked from a boolean to an integer
	private void addTask(){
		Editable taskTitle =  mTaskTitle.getText();
		Editable taskDetails = mTaskDetails.getText();
		int taskCompleted = mTaskCompletedCheckBox.isChecked() == true ? 1:0;
		String dateString = mTaskDateButton.getText().toString();
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM dd, yyyy");
		try {
			sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long time = sdf.getCalendar().getTimeInMillis();
		mDatabase.createTask(taskTitle.toString(), taskDetails.toString(), time, taskCompleted);	
		getActivity().finish();		 	
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		mDatabase.close();
	}
}
