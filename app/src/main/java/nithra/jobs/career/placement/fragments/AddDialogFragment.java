package nithra.jobs.career.placement.fragments;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.engine.LocalDB;
import nithra.jobs.career.placement.pojo.Jobs;
import nithra.jobs.career.placement.receivers.AlarmReceiver;
import nithra.jobs.career.placement.utills.L;
import nithra.jobs.career.placement.utills.U;

public class AddDialogFragment extends DialogFragment implements View.OnClickListener {

    Button btnCancel, btnAdd, btnRemove;
    EditText edTitle, edDescription;
    LocalDB localDB;
    DialogListener dialogListener;
    int action = 0, id = 0, position = 0;
    String title = "", emp = "", lastDate = "", time = "", date = "", image = "";
    long actionDate = 0;
    TextView txtDate, txtTime;
    ImageButton btnDate, btnTime;
    int amPM = 0;
    private int mHour;
    private int mMinute;
    Calendar calendar;
    int selectedDay, selectedMonth, selectedYear, selectedHour, selectedMinute;

    public AddDialogFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public AddDialogFragment(Fragment context, int action, int position, int id, String image, String title, String emp, String date) {
        dialogListener = (DialogListener) context;
        this.action = action;
        this.position = position;
        this.id = id;
        this.image = image;
        this.title = title;
        this.emp = emp;
        this.lastDate = date;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (action == 0) getDialog().setTitle("Remind Me");
        else getDialog().setTitle("Edit Reminder");

        localDB = new LocalDB(getActivity());
        return inflater.inflate(R.layout.fragment_add_dialog, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        edTitle = view.findViewById(R.id.edTitle);
        edDescription = view.findViewById(R.id.edDescription);
        btnDate = view.findViewById(R.id.btnDate);
        btnDate.setOnClickListener(this);
        btnTime = view.findViewById(R.id.btnTime);
        btnTime.setOnClickListener(this);
        txtDate = view.findViewById(R.id.txtDate);
        txtDate.setOnClickListener(this);
        txtTime = view.findViewById(R.id.txtTime);
        txtTime.setOnClickListener(this);
        btnRemove = view.findViewById(R.id.btnRemove);
        btnRemove.setOnClickListener(this);

        if (localDB.isReminderExists(id)) {
            btnRemove.setVisibility(View.VISIBLE);
        } else {
            btnRemove.setVisibility(View.GONE);
        }

        calendar = Calendar.getInstance();
        if (action != 0) {
            btnAdd.setText("Update");
            Jobs jobs = localDB.getItem(id);
            image = jobs.image;
            title = jobs.jobtitle;
            emp = jobs.employer;
            lastDate = jobs.date;
            actionDate = jobs.actionDate;
            calendar.setTimeInMillis(actionDate);

            //set date to textview
            txtDate.setText(U.s2d(calendar.get(Calendar.DAY_OF_MONTH)) + "-" + U.s2d(calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR));

            //set time to textview
            mHour = calendar.get(Calendar.HOUR);
            mMinute = calendar.get(Calendar.MINUTE);
            String tH = U.s2d(mHour).equals("00") ? "12" : U.s2d(mHour);
            amPM = calendar.get(Calendar.AM_PM);
            txtTime.setText(tH + ":" + U.s2d(mMinute) + " " + (amPM == Calendar.AM ? "AM" : "PM"));
            btnAdd.setVisibility(View.VISIBLE);

        } else btnAdd.setVisibility(View.GONE);

        System.out.println("Read Image : " + image);
        edTitle.setText(title);
        edDescription.setText(emp);
//        edTitle.setSelection(edTitle.getText().toString().length());
//        edDescription.setSelection(edDescription.getText().toString().length());

    }

    @Override
    public void onClick(View view) {
        if (view == btnAdd) {
            String title , emp ;
            title = edTitle.getText().toString();
            emp = edDescription.getText().toString();
            if (title.equals("") && txtDate.getText().toString().equals("") && txtTime.getText().toString().equals(""))
                L.t(getActivity(), "Please Fill All Fields");
            else {
                String message;
                boolean done = false;
                actionDate = calendar.getTimeInMillis();
                if (action != 0) {
                    done = localDB.updateTask(new Jobs(id, image, title, emp, lastDate, actionDate));
                    message = "Reminder updated successfully";
                } else {
//                    Calendar current = Calendar.getInstance();
//                    if (current.compareTo(calendar) <= 0) {
//                        message = "Invalid Date/Time";
//                    } else {
//                    message = "Reminder set successfully";
//                    done = localDB.addJobReminder(new Jobs(id, image, title, emp, lastDate, actionDate));
//                    }
                    Calendar cal = Calendar.getInstance();
                    cal.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute, 0);

                    Calendar current = Calendar.getInstance();

                    Date today = current.getTime();
                    Date dateSpecified = cal.getTime();

                    if (dateSpecified.before(today)) {
                        message = "Set Reminder for Upcoming Time";
                    } else {
                        message = "Reminder set successfully";
                        done = localDB.addJobReminder(new Jobs(id, image, title, emp, lastDate, actionDate));
                    }
                }
                if (done) {
                    setAlarm(calendar, id, title, emp, image);
                    dialogListener.onTaskAdded(position);
                }
                L.t(getActivity(), message);
                dismiss();
            }
        } else if (view == btnCancel) {
            dismiss();
        } else if (view == btnDate) {
            setDate();
        } else if (view == btnTime) {
            setTime();
        } else if (view == btnRemove) {
            localDB.deleteJobReminder(id);
            dialogListener.onReminderRemoved(id);
            dismiss();
        }
    }

    private void setAlarm(Calendar targetCal, int jobid, String title, String emp, String image) {
        if(getActivity()!=null) {
            targetCal.set(Calendar.SECOND, 0);
            targetCal.set(Calendar.MILLISECOND, 0);
            System.out.println("Alarm setAlarm : " + jobid + " --- " + title);
            System.out.println("Alarm : time : " + targetCal.getTime());

            AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
            alarmIntent.putExtra("jobid", jobid);
            alarmIntent.putExtra("title", title);
            alarmIntent.putExtra("emp", emp);
            alarmIntent.putExtra("image", image);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), jobid, alarmIntent, 0);
            if (manager != null) {
                manager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
            }
        }
    }

    private void setTime() {
        // Get Current Time
        mHour = calendar.get(Calendar.HOUR);
        mMinute = calendar.get(Calendar.MINUTE);
        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,
                new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        selectedHour = hourOfDay;
                        selectedMinute = minute;
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        amPM = calendar.get(Calendar.AM_PM) == Calendar.AM ? Calendar.AM : Calendar.PM;
                        time = U.s2d(hourOfDay) + ":" + U.s2d(minute);
                        String tH = U.s2d(calendar.get(Calendar.HOUR)).equals("00") ? "12" : U.s2d(calendar.get(Calendar.HOUR));
                        txtTime.setText(tH + ":" + U.s2d(minute) + " " + (amPM == Calendar.AM ? "AM" : "PM"));
                        if (action == 0) enableButton(date);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void setDate() {
        // Get Current Date

        if(getActivity()!=null) {
            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            selectedDay = dayOfMonth;
                            selectedMonth = monthOfYear;
                            selectedYear = year;
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, monthOfYear);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            date = U.s2d(dayOfMonth) + "-" + U.s2d((monthOfYear + 1)) + "-" + U.s2d(year);
                            txtDate.setText(date);
                            if (action == 0) enableButton(time);
                        }
                    }, mYear, mMonth, mDay);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            Date dt;
            Calendar c = Calendar.getInstance();
            c.setTime(U.convertStringtoDate(lastDate, U.DATE_TIME_FORMAT));
            c.add(Calendar.DATE, 0);
            dt = c.getTime();
            datePickerDialog.getDatePicker().setMaxDate(dt.getTime());
            datePickerDialog.show();
        }
    }

    private void enableButton(String text) {
        if (text.length() != 0) btnAdd.setVisibility(View.VISIBLE);
        else btnAdd.setVisibility(View.GONE);
    }

    public interface DialogListener {
        void onTaskAdded(int position);

        void onReminderRemoved(int position);
    }

}
