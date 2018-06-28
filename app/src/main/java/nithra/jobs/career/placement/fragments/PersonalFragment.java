package nithra.jobs.career.placement.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Calendar;

import nithra.jobs.career.placement.FragmentInterface;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.activity.RegistrationActivity;
import nithra.jobs.career.placement.dialogfragment.DatePickerFragment;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;
import static android.support.v4.view.ViewCompat.jumpDrawablesToCurrentState;

/**
 * Created by nithra-apps on 14/11/17.
 */

public class PersonalFragment extends Fragment implements FragmentInterface {

    SharedPreference pref;
    RadioGroup genderGroup, MaritalStatus;
    RadioButton male, female, married, unMarried;
    ImageView datePicker;
    //Pinview pinDay, pinMonth, pinYear;
    TextView pinAge,pinDay,pinMonth,pinYear;
    int yearVal=0;int monthVal=0;int dayVal=0;
    LinearLayout doblay;

    public PersonalFragment() {
        // Required empty public constructor
    }

    public static PersonalFragment newInstance() {
        return new PersonalFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(getActivity()!=null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
        return inflater.inflate(R.layout.personal_details_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = new SharedPreference();

        genderGroup = view.findViewById(R.id.gendergroup);
        MaritalStatus = view.findViewById(R.id.marital_statusgroup);
        doblay = view.findViewById(R.id.doblay);

        pinDay = view.findViewById(R.id.pinview_day);
        pinMonth = view.findViewById(R.id.pinview_month);
        pinYear = view.findViewById(R.id.pinview_year);
        pinAge = view.findViewById(R.id.pinview_age);
        pinAge = view.findViewById(R.id.pinview_age);

        datePicker = view.findViewById(R.id.datepick_image);

        male = view.findViewById(R.id.male);
        female = view.findViewById(R.id.female);

        married = view.findViewById(R.id.married);
        unMarried = view.findViewById(R.id.unmarried);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonID = group.getCheckedRadioButtonId();
                View radioButton = group.findViewById(radioButtonID);
                RegistrationActivity.gender = String.valueOf(group.indexOfChild(radioButton));
                pref.putString(getActivity(), U.SH_GENDER, RegistrationActivity.gender);
            }
        });

        MaritalStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonID = group.getCheckedRadioButtonId();
                View radioButton = group.findViewById(radioButtonID);
                RegistrationActivity.maritalStatus = String.valueOf(group.indexOfChild(radioButton));
                pref.putString(getActivity(), U.SH_MARITAL_STATUS, RegistrationActivity.maritalStatus);
            }
        });


       /* pinDay.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                dayVal = Integer.parseInt(pinview.getValue());
                pinMonth.requestPinEntryFocus();
            }
        });

        pinMonth.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                monthVal = Integer.parseInt(pinview.getValue());
                pinYear.requestPinEntryFocus();
            }
        });

        pinYear.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                yearVal = Integer.parseInt(pinview.getValue());

                Log.e("check ypin log","year pin function called");
                Log.e("check ypin d",""+dayVal);
                Log.e("check ypin m",""+monthVal);
                Log.e("check ypin y",""+yearVal);

                check(dayVal,monthVal,yearVal);
            }
        });*/

        doblay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!pref.getString(getActivity(), U.SH_GENDER).equals("")) {
            String gender = pref.getString(getActivity(), U.SH_GENDER);
            if (gender.equals("0")) {
                male.setChecked(true);
                jumpDrawablesToCurrentState(male);
            } else {
                female.setChecked(true);
                jumpDrawablesToCurrentState(female);
            }
        }

        if (!pref.getString(getActivity(), U.SH_MARITAL_STATUS).equals("")) {
            String gender = pref.getString(getActivity(), U.SH_MARITAL_STATUS);
            if (gender.equals("0")) {
                unMarried.setChecked(true);
                jumpDrawablesToCurrentState(unMarried);
            } else {
                married.setChecked(true);
                jumpDrawablesToCurrentState(married);
            }
        }

        if (!pref.getString(getActivity(), U.SH_DOB).equals("")) {
            RegistrationActivity.dob = pref.getString(getActivity(), U.SH_DOB);
            String[] ageArray = RegistrationActivity.dob.split("-");
            String day = ageArray[0];
            String month = ageArray[1];
            String year = ageArray[2];

            pinYear.setText(String.valueOf(year));
            pinMonth.setText(U.s2d(Integer.parseInt(month)));
            pinDay.setText(U.s2d(Integer.parseInt(day)));
        }

        if (!pref.getString(getActivity(), U.SH_AGE).equals("")) {
            RegistrationActivity.age = pref.getString(getActivity(), U.SH_AGE);
            pinAge.setText(RegistrationActivity.age);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putInt("year", 1990);
        args.putInt("month", 0);
        args.putInt("day", 1);
        date.setArguments(args);
        date.setCallBack(ondate);
        if(getActivity()!=null) {
            date.show(getActivity().getSupportFragmentManager(), "Date Picker");
        }
    }

    /*private void check(int day, int month, int year){
        if(day!=0 && month!=0 && year!=0){
            RegistrationActivity.dob = U.s2d(day) + "-" + U.s2d(month)
                    + "-" + String.valueOf(year);
            RegistrationActivity.age = getAge(day,month,year);
            Log.e("check log","check function called");
            Log.e("check d",""+day);
            Log.e("check m",""+month);
            Log.e("check y",""+year);
            Log.e("check ageval",""+RegistrationActivity.age);

            pinAge.setText(RegistrationActivity.age);

            if (!RegistrationActivity.dob.isEmpty()) {
                pref.putString(getActivity(), U.SH_DOB, RegistrationActivity.dob);
            }else{
                pref.putString(getActivity(), U.SH_DOB, "");
            }

            if (!RegistrationActivity.age.isEmpty()) {
                pref.putString(getActivity(), U.SH_AGE, RegistrationActivity.age);
            }else{
                pref.putString(getActivity(), U.SH_AGE, "");
            }

        }else if(day == 0 && month == 0 && year== 0){
            Toast.makeText(getActivity(), "Enter your Date of Birth", Toast.LENGTH_SHORT).show();
        }else if(day == 0){
            Toast.makeText(getActivity(), "Enter your birth day", Toast.LENGTH_SHORT).show();
        }else if(month == 0){
            Toast.makeText(getActivity(), "Enter your birth month", Toast.LENGTH_SHORT).show();
        }else if(year== 0){
            Toast.makeText(getActivity(), "Enter your birth year", Toast.LENGTH_SHORT).show();
        }
    }*/

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            int month = monthOfYear+1;
            RegistrationActivity.dob =  U.s2d(dayOfMonth) + "-" + U.s2d(month) + "-" +String.valueOf(year);
            RegistrationActivity.age = getAge(dayOfMonth,month,year);

            Log.e("check picker log","picker result called");
            Log.e("check picker d",""+dayOfMonth);
            Log.e("check picker m",""+month);
            Log.e("check picker y",""+year);
            Log.e("check picker ageval",""+RegistrationActivity.age);

            yearVal = year;
            monthVal = Integer.parseInt(U.s2d(month));
            dayVal = Integer.parseInt(U.s2d(dayOfMonth));

            pinYear.setText(String.valueOf(year));
            pinMonth.setText(U.s2d(month));
            pinDay.setText(U.s2d(dayOfMonth));
            pinAge.setText(RegistrationActivity.age);

            if (!RegistrationActivity.dob.isEmpty()) {
                pref.putString(getActivity(), U.SH_DOB, RegistrationActivity.dob);
            }

            if (!RegistrationActivity.age.isEmpty()) {
                pref.putString(getActivity(), U.SH_AGE, RegistrationActivity.age);
            }
        }
    };

    private String getAge(int day, int month, int year) {

        Log.e("check age log","Age function called");
        Log.e("check age d",""+day);
        Log.e("check age m",""+month);
        Log.e("check age y",""+year);

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month-1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        Log.e("check compare age",""+today.get(Calendar.YEAR)+"-"+dob.get(Calendar.YEAR)+"="+age);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        int ageInt = age;

        Log.e("check age ageval",""+ U.s2d(ageInt));
        return  U.s2d(ageInt);
    }

    @Override
    public void fragmentBecameVisible() {

    }
}