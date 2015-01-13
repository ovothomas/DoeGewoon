package nl.mprog.setup.mproject10173072;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment {
	
	// variables
	private int mYear, mMonth, mDay;
	private OnDateSetListener mListener;
	
	public DatePickerFragment(OnDateSetListener listener, int year, int month, int day) {
		
		this.mDay = day;
		this.mMonth = month;
		this.mYear = year;
		this.mListener = listener;
		
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		
		// Create a new instance of Date
		return new DatePickerDialog(getActivity(), mListener, mYear, mMonth, mDay);
		
	}
	
	

}
