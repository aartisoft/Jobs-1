package nithra.jobs.career.placement.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import nithra.jobs.career.placement.Interface.FragmentInterface;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.activity.RegistrationActivity;
import nithra.jobs.career.placement.dialogfragment.DatePickerFragment;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

import static androidx.core.view.ViewCompat.jumpDrawablesToCurrentState;

/**
 * Created by nithra-apps on 14/11/17.
 */

public class PersonalFragment extends Fragment implements FragmentInterface {

    SharedPreference pref;
    RadioGroup genderGroup, MaritalStatus;
    RadioButton male, female, married, unMarried;
    ImageView datePicker;
    TextView age;
    int yearVal = 0;
    int monthVal = 0;
    int dayVal = 0;
    LinearLayout doblay;
    EditText d1, m1, y1;
    OnRegistrationListener onRegistrationListener;
    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            int month = monthOfYear + 1;
            RegistrationActivity.dob = U.s2d(dayOfMonth) + "-" + U.s2d(month) + "-" + year;
            RegistrationActivity.age = getAge(dayOfMonth, month, year);

            Log.e("check picker log", "picker result called");
            Log.e("check picker d", "" + dayOfMonth);
            Log.e("check picker m", "" + month);
            Log.e("check picker y", "" + year);
            Log.e("check picker ageval", "" + RegistrationActivity.age);

            yearVal = year;
            monthVal = Integer.parseInt(U.s2d(month));
            dayVal = Integer.parseInt(U.s2d(dayOfMonth));

            d1.setText(U.s2d(dayOfMonth));
            m1.setText(U.s2d(month));
            y1.setText(String.valueOf(year));

            age.setText(RegistrationActivity.age);

            if (!RegistrationActivity.dob.isEmpty()) {
                pref.putString(getActivity(), U.SH_DOB, RegistrationActivity.dob);
            }

            if (!RegistrationActivity.age.isEmpty()) {
                pref.putString(getActivity(), U.SH_AGE, RegistrationActivity.age);
            }

            onRegistrationListener.onPercentageChange();
        }
    };

    public PersonalFragment() {
        // Required empty public constructor
    }

    public static PersonalFragment newInstance() {
        return new PersonalFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onRegistrationListener = (OnRegistrationListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (getActivity() != null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }

        if (getActivity() != null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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

        age = view.findViewById(R.id.pinview_age);

        datePicker = view.findViewById(R.id.datepick_image);

        male = view.findViewById(R.id.male);
        female = view.findViewById(R.id.female);

        married = view.findViewById(R.id.married);
        unMarried = view.findViewById(R.id.unmarried);
        d1 = view.findViewById(R.id.d1);
        m1 = view.findViewById(R.id.m1);
        y1 = view.findViewById(R.id.y1);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getActivity().getCurrentFocus();
        if (view == null) {
            view = new View(getActivity());
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonID = group.getCheckedRadioButtonId();
                View radioButton = group.findViewById(radioButtonID);
                RegistrationActivity.gender = String.valueOf(group.indexOfChild(radioButton));
                pref.putString(getActivity(), U.SH_GENDER, RegistrationActivity.gender);
                onRegistrationListener.onPercentageChange();
            }
        });

        MaritalStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonID = group.getCheckedRadioButtonId();
                View radioButton = group.findViewById(radioButtonID);
                RegistrationActivity.maritalStatus = String.valueOf(group.indexOfChild(radioButton));
                pref.putString(getActivity(), U.SH_MARITAL_STATUS, RegistrationActivity.maritalStatus);
                onRegistrationListener.onPercentageChange();
            }
        });

        d1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (d1.length() == 2) {
                    setFocusTo(m1);
                }
            }
        });

        m1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (m1.length() == 2) {
                    setFocusTo(y1);
                }
            }
        });

        y1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (y1.length() == 4) {
                    try {
                        if (d1.getText().toString().isEmpty() || m1.getText().toString().isEmpty()) {
                            y1.setText("");
                            d1.requestFocus();
                            U.toast_center(getActivity(), "Kindly Enter correct values");
                        } else {
                            int dayOfMonth = Integer.parseInt(d1.getText().toString());
                            int month = Integer.parseInt(m1.getText().toString());
                            int year = Integer.parseInt(y1.getText().toString());
                            RegistrationActivity.dob = U.s2d(dayOfMonth) + "-" + U.s2d(month) + "-" + year;

                            Log.e("validate", "" + validate(RegistrationActivity.dob));
                            if (validate(RegistrationActivity.dob)) {
                                RegistrationActivity.age = getAge(dayOfMonth, month, year);
                                age.setText(RegistrationActivity.age);

                                if (!RegistrationActivity.dob.isEmpty()) {
                                    pref.putString(getActivity(), U.SH_DOB, RegistrationActivity.dob);
                                }

                                if (!RegistrationActivity.age.isEmpty()) {
                                    pref.putString(getActivity(), U.SH_AGE, RegistrationActivity.age);
                                }

                                onRegistrationListener.onPercentageChange();

                            } else {
                                U.toast_center(getActivity(), "Invalid DOB! Kindly Enter correct values");
                            }
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        y1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    if (y1.length() == 0) {
                        setFocusTo(m1);
                    }
                }
                return false;
            }
        });
        m1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    if (m1.length() == 0) {
                        setFocusTo(d1);
                    }
                }
                return false;
            }
        });

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
            Log.e("value", day + month + year);

            d1.setText(U.s2d(Integer.parseInt(day)));
            m1.setText(U.s2d(Integer.parseInt(month)));
            y1.setText(String.valueOf(year));
        }

        if (!pref.getString(getActivity(), U.SH_AGE).equals("")) {
            RegistrationActivity.age = pref.getString(getActivity(), U.SH_AGE);
            age.setText(RegistrationActivity.age);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(d1.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(m1.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(y1.getWindowToken(), 0);
        }
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putInt("year", 1990);
        args.putInt("month", 0);
        args.putInt("day", 1);
        date.setArguments(args);
        date.setCallBack(ondate);
        if (getActivity() != null) {
            date.show(getActivity().getSupportFragmentManager(), "Date Picker");
        }
    }

    private String getAge(int day, int month, int year) {

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month - 1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        Log.e("check compare age", "" + today.get(Calendar.YEAR) + "-" + dob.get(Calendar.YEAR) + "=" + age);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        int ageInt = age;

        return U.s2d(ageInt);
    }

    private boolean validate(final String date) {
        int currentyear = Calendar.getInstance().get(Calendar.YEAR);
        String[] ageArray = date.split("-");
        String day = ageArray[0];
        String month = ageArray[1];
        int year = Integer.parseInt(ageArray[2]);
        if (day.equals("31") &&
                (month.equals("4") || month.equals("6") || month.equals("9") ||
                        month.equals("11") || month.equals("04") || month.equals("06") ||
                        month.equals("09"))) {
            return false; // only 1,3,5,7,8,10,12 has 31 days
        } else if (month.equals("2") || month.equals("02")) {
            //leap year
            if (year % 4 == 0) {
                return !day.equals("30") && !day.equals("31");//return true
            } else {
                return !day.equals("29") && !day.equals("30") && !day.equals("31");//return true
            }
        } else return Integer.parseInt(day) <= 31 && Integer.parseInt(month) <= 12 && year < currentyear;
    }

    @Override
    public void fragmentBecameVisible() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(d1.getWindowToken(), 0);
        }
    }

    private void setFocusTo(EditText view) {
        if (getActivity() != null) {
            view.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    public interface OnRegistrationListener {
        void onPercentageChange();
    }
}