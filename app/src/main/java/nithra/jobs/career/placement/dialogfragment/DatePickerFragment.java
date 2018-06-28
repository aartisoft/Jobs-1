package nithra.jobs.career.placement.dialogfragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by arunrk on 27/5/17.
 */


public class DatePickerFragment extends DialogFragment {
    OnDateSetListener ondateSet;

    public DatePickerFragment() {
    }

    public void setCallBack(OnDateSetListener ondate) {
        ondateSet = ondate;
    }

    private int year, month, day;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DatePickerDialog dpDialog = new DatePickerDialog(getActivity(), ondateSet, year, month, day);
        dpDialog.setTitle("");
        DatePicker datePicker = dpDialog.getDatePicker();

        Calendar calendar = Calendar.getInstance();//get the current day
        datePicker.setMaxDate(calendar.getTimeInMillis());//set the current day as the max date
        return dpDialog;
    }
}
