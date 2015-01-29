package nl.mprog.setup.mproject10173072;
/*
 * DatePickerFragment is a dialog where 
 * the user can change the date of a 
 * particular task.
 */

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment { 
	public static final String EXTRA_DATE = 
			"nl.mprog.setup.mproject10173072.date";

	// variables
	private long mDate;
	private int mYear, mMonth, mDay;
	
	// stash date in an argument bundle where the DatePickerFragment can
	// access it
	public static DatePickerFragment newInstance(long l){
		
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_DATE, l);
		DatePickerFragment fragment = new DatePickerFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		// getting the arguments
		mDate = (Long)getArguments().getSerializable(EXTRA_DATE);
		
		// Creating a Calendar to get the year, month and day of date
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(mDate);
		mYear = calendar.get(Calendar.YEAR);
		mMonth = calendar.get(Calendar.MONTH);
		mDay = calendar.get(Calendar.DAY_OF_MONTH);
		
		// initializing datepicker
		DatePickerDialog datepicker = new DatePickerDialog(getActivity(), new
				DatePickerDialog.OnDateSetListener() {
		
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					mDate = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTimeInMillis();
					sendResult(Activity.RESULT_OK);	
				}
			}, mYear, mMonth, mDay);
		return datepicker;
	}
	
	// send data result back to TaskFragment
	private void sendResult(int resultCode){
		if (getTargetFragment() == null)
			return;
		Intent i = new Intent();
		i.putExtra(EXTRA_DATE, mDate);
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
	}
}
