package nithra.jobs.career.placement.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import nithra.jobs.career.placement.FragmentInterface;
import nithra.jobs.career.placement.MainActivity;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.fragments.ContactFragment;
import nithra.jobs.career.placement.fragments.OthersFragment;
import nithra.jobs.career.placement.fragments.PersonalFragment;
import nithra.jobs.career.placement.fragments.QualificationFragment;
import nithra.jobs.career.placement.networking.MySingleton;
import nithra.jobs.career.placement.pojo.ArrayItem;
import nithra.jobs.career.placement.pojo.Item;
import nithra.jobs.career.placement.utills.FlexboxLayout;
import nithra.jobs.career.placement.utills.L;
import nithra.jobs.career.placement.utills.Pinview;
import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

public class RegistrationActivity extends AppCompatActivity {

    SharedPreference pref;
    LinearLayout adView;
    FlexboxLayout fbl;
    LinearLayout skillsLay, qualificationLay, locationLay, resendOTPLay, timerLay, profileEdit, changeNumber;
    TextView txtPhone, txtTimer, txtName, txtEmail, txtMobile, txtNatLocation, txtDetails, noValues;
    ImageView prophoto, ProfileDelete;
    public static String employeeId = "", photo = "", name = "", email = "", mobileno = "", altmobileno = "", mobileShown = "", nativeLocation = "",
            gender = "", dob = "", age = "", maritalStatus = "",
            courses = "",
            workStatus = "", skills = "", jobPrefferedLocation = "", detailsShown = "", ppshown = "", otp = "", tcshown = "";
    AppCompatButton btnResendOtp;
    Pinview pinview;
    SpinKitView progressBar;
    public static List<ArrayItem> qualificationList, locationList, skillsList;
    int current_page = 0;
    public static PopupWindow Qpopupwindow, Spopupwindow, Lpopupwindow;
    CountDownTimer otptimer;
    String task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        pref = new SharedPreference();
        initialPage();

        qualificationList = new ArrayList<ArrayItem>();
        skillsList = new ArrayList<ArrayItem>();
        locationList = new ArrayList<ArrayItem>();
        task = getIntent().getStringExtra("task");
        loadJSON(SU.REGISTRATION_S);

    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            else {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }
    }

    private void preLoad() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void errorHandling(VolleyError error) {
        String e = "";
        if (error instanceof TimeoutError)
            e = "Request TimeOut";
        else if (error instanceof AuthFailureError)
            e = "AuthFailureError";
        else if (error instanceof ServerError)
            e = "ServerError";
        else if (error instanceof NetworkError || error instanceof NoConnectionError)
            e = U.INA;
        else if (error instanceof ParseError)
            e = U.ERROR;
        else
            e = U.ERROR;
        L.t(RegistrationActivity.this, e);
    }

    //------------------------------------------ Initial page --------------------------------------

    public void initialPage() {
        setContentView(R.layout.activity_sign_up);
        current_page = 1;
        progressBar = findViewById(R.id.progressBar);
        preLoad();
    }

    //------------------------------------------ mobile number view --------------------------------

    public void mobileNoVerification(final String action) {
        setContentView(R.layout.mobile_number);
        current_page = 2;
        final EditText userInput = findViewById(R.id.user_input);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        final Button continueBtn = findViewById(R.id.submitBtn);
        ImageView closeBtn = findViewById(R.id.close_icon);
        adView = findViewById(R.id.ads);

        if (!action.equals(SU.CHANGE_MOBILE_NUMBER)) {
            userInput.setText("" + pref.getString(RegistrationActivity.this, U.SH_MOBILE));
            userInput.setSelection(pref.getString(RegistrationActivity.this, U.SH_MOBILE).length());
        }

        if (pref.getInt(RegistrationActivity.this, U.SH_RECOMMENDED_DIA_SHOW) == 0) {
            pref.putInt(RegistrationActivity.this, U.SH_RECOMMENDED_DIA_SHOW, 1);
            final Dialog dialog = new Dialog(RegistrationActivity.this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.info_popup);
            WebView infoText = dialog.findViewById(R.id.info_text);
            infoText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });

            String info = "<b><meta charset=\"utf-8\"><font color=purple size=2>\n" +
                    getResources().getString(R.string.rjobs_intro) + "<br><br></font></b>";

            U.webview(RegistrationActivity.this, info, infoText);

            dialog.findViewById(R.id.text_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userInput.getText().toString().equals("")) {
                    userInput.setError("Enter your Mobile Number");
                } else if (userInput.length() < 10) {
                    Toast.makeText(RegistrationActivity.this, "Enter your 10 digit MobileNo.", Toast.LENGTH_SHORT).show();
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(userInput.getWindowToken(), 0);
                    }

                    final Dialog d = new Dialog(RegistrationActivity.this, R.style.Base_Theme_AppCompat_Dialog_Alert);
                    d.setContentView(R.layout.mobileno_confirm_lay);
                    TextView textTitle = d.findViewById(R.id.txt_title);
                    textTitle.setText(userInput.getText().toString());
                    Button yes = d.findViewById(R.id.yes);
                    yes.setText("CONTINUE");
                    Button no = d.findViewById(R.id.no);
                    no.setText("EDIT");
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (U.isNetworkAvailable(RegistrationActivity.this)) {
                                if (action.equals(SU.CHANGE_MOBILE_NUMBER)) {
                                    mobileno = userInput.getText().toString();
                                    if(mobileno.equals(pref.getString(RegistrationActivity.this,U.SH_MOBILE))){
                                        Toast.makeText(RegistrationActivity.this, ""+getResources().getString(R.string.repeatno), Toast.LENGTH_LONG).show();
                                    }else{
                                        pref.putString(RegistrationActivity.this, U.SH_CHANGE_MOBILE, mobileno);
                                        loadJSON(SU.CHANGE_MOBILE_NUMBER);
                                    }
                                } else {
                                    mobileno = userInput.getText().toString();
                                    pref.putString(RegistrationActivity.this, U.SH_MOBILE, mobileno);
                                    checkUserAvailability(mobileno);
                                }
                                d.dismiss();
                            } else {
                                L.t(RegistrationActivity.this, U.INA);
                            }
                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.dismiss();
                        }
                    });
                    d.show();
                }
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref.getBoolean(RegistrationActivity.this, U.SH_SIGN_UP_SUCCESS) && pref.getBoolean(RegistrationActivity.this, U.SH_OTP_SUCCESS)) {
                    ViewProfile();
                } else {
                    finish();
                }
            }
        });
    }

    //-----------------------------------------  Blocked User --------------------------------------

    public void blockedUser() {
        setContentView(R.layout.user_block_lay);
        current_page = 3;
        pref.putBoolean(RegistrationActivity.this, U.SH_OTP_SUCCESS, false);
        pref.putBoolean(RegistrationActivity.this, U.SH_SIGN_UP_SUCCESS, false);
        pref.putBoolean(RegistrationActivity.this, U.SH_BLOCKED_USER, true);
    }

    //-----------------------------------------  OTP Verification ----------------------------------

    private void setOtpView(final String action) {
        setStatusBarTranslucent(true);
        setContentView(R.layout.otp_layout);
        current_page = 4;
        resendOTPLay = findViewById(R.id.resend_otp_lay);
        timerLay = findViewById(R.id.timer_lay);
        txtTimer = findViewById(R.id.timer);
        otptimer = new CountDownTimer(90000, 1000) {

            public void onTick(long millis) {
                timerLay.setVisibility(View.VISIBLE);
                resendOTPLay.setVisibility(View.GONE);
                @SuppressLint("DefaultLocale") String hms = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                txtTimer.setText(hms);
            }

            public void onFinish() {
                timerLay.setVisibility(View.GONE);
                resendOTPLay.setVisibility(View.VISIBLE);
            }

        }.start();

        pinview = findViewById(R.id.pinview);
        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                RegistrationActivity.otp = pinview.getValue();
                if (!RegistrationActivity.otp.isEmpty()) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(pinview.getWindowToken(), 0);
                    }
                    makeOtpProcess(action, otp);
                } else {
                    L.t(RegistrationActivity.this, "Enter OTP");
                }
            }
        });
        pinview.performClick();

        btnResendOtp = findViewById(R.id.btnResendOtp);
        btnResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (action.equals(SU.CHANGE_MOBILE_NUMBER)) {
                    loadJSON(SU.CHANGE_MOBILE_NUMBER);
                } else {
                    checkUserAvailability(pref.getString(RegistrationActivity.this, U.SH_MOBILE));
                }
            }
        });

        txtPhone = findViewById(R.id.phoneno);
        if (action.equals(SU.OTP)) {
            txtPhone.setText(pref.getString(this, U.SH_MOBILE));
        } else if (action.equals(SU.CHANGE_MOBILE_NUMBER)) {
            txtPhone.setText(pref.getString(this, U.SH_CHANGE_MOBILE));
        }
    }

    private void makeOtpProcess(final String action, final String otp) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.REGISTRATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (action.equals(SU.OTP)) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");

                                if (status.equals("OTP mismatch")) {
                                    L.t(RegistrationActivity.this, "Enter Correct OTP");
                                    pinview.clearValue();

                                } else if (status.equals("OTP verified")) {
                                    pref.putBoolean(RegistrationActivity.this, U.SH_OTP_SUCCESS, true);
                                    L.t(RegistrationActivity.this, "OTP verified");
                                    if (pref.getBoolean(RegistrationActivity.this, U.SH_SIGN_UP_SUCCESS)) {
                                        Intent intent = new Intent(RegistrationActivity.this, ProfileEntryActivity.class);
                                        intent.putExtra("task", SU.UPDATE);
                                        startActivity(intent);

//                                        registration(SU.UPDATE);
                                    } else {
                                        Intent intent = new Intent(RegistrationActivity.this, ProfileEntryActivity.class);
                                        intent.putExtra("task", SU.REGISTER);
                                        startActivity(intent);

//                                        registration(SU.REGISTER);
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                pref.putBoolean(RegistrationActivity.this, U.SH_OTP_SUCCESS, false);
                                L.t(RegistrationActivity.this, U.INA);
                            }
                        } else if (action.equals(SU.CHANGE_MOBILE_NUMBER)) {
                            JSONObject jsonObject = null;
                            Log.e("change_mno.", response);
                            try {
                                jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");

                                if (status.equals("OTP mismatch")) {
                                    L.t(RegistrationActivity.this, "Enter Correct OTP");
                                    pinview.clearValue();

                                } else if (status.equals("invalid_user")) {
                                    L.t(RegistrationActivity.this, "" + getResources().getString(R.string.profile_expired));
                                    pref.putBoolean(RegistrationActivity.this, U.SH_OTP_SUCCESS, false);
                                    pref.putBoolean(RegistrationActivity.this, U.SH_SIGN_UP_SUCCESS, false);
                                    clearSPValues();
                                    mobileNoVerification("invalid_user");

                                } else if (status.equals("mobile_changed")) {
                                    pref.putBoolean(RegistrationActivity.this, U.SH_OTP_SUCCESS, true);
                                    L.t(RegistrationActivity.this, "Mobile Number Successfully Changed");
                                    pref.putString(RegistrationActivity.this, U.SH_COMPLETED_JSON, jsonObject.toString());
                                    getValues(jsonObject);
                                    if (pref.getBoolean(RegistrationActivity.this, U.SH_SIGN_UP_SUCCESS)) {
                                        Intent intent = new Intent(RegistrationActivity.this, ProfileEntryActivity.class);
                                        intent.putExtra("task", SU.UPDATE);
                                        startActivity(intent);

//                                        registration(SU.UPDATE);
                                    } else {
                                        Intent intent = new Intent(RegistrationActivity.this, ProfileEntryActivity.class);
                                        intent.putExtra("task", SU.REGISTER);
                                        startActivity(intent);

//                                        registration(SU.REGISTER);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                pref.putBoolean(RegistrationActivity.this, U.SH_OTP_SUCCESS, false);
                                L.t(RegistrationActivity.this, U.INA);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pref.putBoolean(RegistrationActivity.this, U.SH_OTP_SUCCESS, false);
                        errorHandling(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (action.equals(SU.OTP)) {
                    params.put("action", SU.OTP);
                    params.put("mobile1", pref.getString(RegistrationActivity.this, U.SH_MOBILE));
                    params.put("employee_id", pref.getString(RegistrationActivity.this, U.SH_EMPLOYEE_ID));
                    params.put("otp", otp);
                } else if (action.equals(SU.CHANGE_MOBILE_NUMBER)) {
                    params.put("action", SU.OTP);
                    params.put("action1", SU.CHANGE_MOBILE_NUMBER);
                    params.put("mobile_new", pref.getString(RegistrationActivity.this, U.SH_CHANGE_MOBILE));
                    params.put("employee_id", pref.getString(RegistrationActivity.this, U.SH_EMPLOYEE_ID));
                    params.put("otp", otp);
                }
                return params;
            }

        };

        MySingleton.getInstance(RegistrationActivity.this).addToRequestQue(stringRequest);
    }

    //-----------------------------------------  Registration Values -------------------------------

    private void checkUserAvailability(final String mobileno) {
        System.out.println("Current mobile : " + mobileno);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.REGISTRATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("showresponse", "" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("No Mobile Number")) {

                                pref.putBoolean(RegistrationActivity.this, U.SH_BLOCKED_USER, false);
                                L.t(RegistrationActivity.this, "No Mobile Number");
                                mobileNoVerification("");

                            } else if (status.equals("user_blocked")) {

                                blockedUser();

                            } else if (status.equals("completed")) {

                                pref.putBoolean(RegistrationActivity.this, U.SH_BLOCKED_USER, false);
                                pref.putString(RegistrationActivity.this, U.SH_COMPLETED_JSON, jsonObject.toString());
                                getValues(jsonObject);
                                pref.putBoolean(RegistrationActivity.this, U.SH_SIGN_UP_SUCCESS, true);
                                pref.putString(RegistrationActivity.this, U.SH_PHOTO_URI, "");
                                pref.putInt(RegistrationActivity.this, U.SH_EMAIL_DIA_SHOW, 1);

                                if (pref.getBoolean(RegistrationActivity.this, U.SH_OTP_SUCCESS) && pref.getBoolean(RegistrationActivity.this, U.SH_SIGN_UP_SUCCESS)) {
                                    ViewProfile();
                                } else {
                                    setOtpView(SU.OTP);
                                }

                            } else if (status.equals("In-complete")) {

                                pref.putBoolean(RegistrationActivity.this, U.SH_BLOCKED_USER, false);
                                pref.putString(RegistrationActivity.this, U.SH_EMPLOYEE_ID, jsonObject.getString("employee_id"));
                                if (pref.getBoolean(RegistrationActivity.this, U.SH_OTP_SUCCESS)) {
                                    Intent intent = new Intent(RegistrationActivity.this, ProfileEntryActivity.class);
                                    intent.putExtra("task", SU.REGISTER);
                                    startActivity(intent);
                                } else {
                                    setOtpView(SU.OTP);
                                }

                            } else if (status.equals("New_user")) {
                                pref.putBoolean(RegistrationActivity.this, U.SH_BLOCKED_USER, false);
                                pref.putString(RegistrationActivity.this, U.SH_EMPLOYEE_ID, jsonObject.getString("employee_id"));
                                setOtpView(SU.OTP);

                            } else if (status.equals("invalid_user")) {
                                pref.putBoolean(RegistrationActivity.this, U.SH_BLOCKED_USER, false);
                                pref.putBoolean(RegistrationActivity.this, U.SH_SIGN_UP_SUCCESS, false);
                                pref.putBoolean(RegistrationActivity.this, U.SH_OTP_SUCCESS, false);
                                clearSPValues();
                                mobileNoVerification("invalid_user");
                                L.t(RegistrationActivity.this, "" + getResources().getString(R.string.profile_expired));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            L.t(RegistrationActivity.this, U.INA);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorHandling(error);
                        finish();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", SU.CHECK);
                params.put("fcmid", pref.getString(RegistrationActivity.this, "regId"));
                params.put("employee_id", "" + pref.getString(RegistrationActivity.this, U.SH_EMPLOYEE_ID));
                Log.e("empId", "" + pref.getString(RegistrationActivity.this, U.SH_EMPLOYEE_ID));
                if (pref.getBoolean(RegistrationActivity.this, U.SH_OTP_SUCCESS)) {
                    params.put("send_otp", "0");
                } else {
                    params.put("send_otp", "1");
                }
                params.put("mobile1", mobileno);
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQue(stringRequest);
    }

    private void loadJSON(final String param) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.REGISTRATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSON(param, response);
                        Log.e("responseRegister", "" + param + " --" + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            L.t(RegistrationActivity.this, "Request Time Out Please Try again later");
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(RegistrationActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(RegistrationActivity.this, "Server Not Connected", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(RegistrationActivity.this, "Internet Not Available", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(RegistrationActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (param.equals(SU.REGISTER) || param.equals(SU.UPDATE)) {
                    params.put("action", param);
                    if (pref.getString(RegistrationActivity.this, U.SH_PHOTO).contains(pref.getString(RegistrationActivity.this, U.SH_MOBILE) + ".webp")) {
                        Bitmap bmp = getBitmapFromURL(SU.BASE_URL + pref.getString(RegistrationActivity.this, U.SH_PHOTO));
                        pref.putString(RegistrationActivity.this, U.SH_PHOTO, getStringImage(bmp));
                        params.put("photo", pref.getString(RegistrationActivity.this, U.SH_PHOTO));
                    } else {
                        params.put("photo", pref.getString(RegistrationActivity.this, U.SH_PHOTO));
                    }
                    params.put("name", pref.getString(RegistrationActivity.this, U.SH_NAME));
                    params.put("email", pref.getString(RegistrationActivity.this, U.SH_EMAIL));
                    params.put("mobile1", pref.getString(RegistrationActivity.this, U.SH_MOBILE));
                    params.put("mobile2", pref.getString(RegistrationActivity.this, U.SH_ALTERNATE_MOBILE));
                    params.put("native_location", pref.getString(RegistrationActivity.this, U.SH_NATIVE_LOCATION));
                    params.put("mobile_shown_employer", pref.getString(RegistrationActivity.this, U.SH_MOBILE_SHOWN));
                    params.put("gender", pref.getString(RegistrationActivity.this, U.SH_GENDER));
                    params.put("marital_status", pref.getString(RegistrationActivity.this, U.SH_MARITAL_STATUS));
                    String dobArray[] = pref.getString(RegistrationActivity.this, U.SH_DOB).split("-");
                    String dobval = dobArray[2] + "-" + dobArray[1] + "-" + dobArray[0];
                    params.put("dob", dobval);
                    params.put("age", pref.getString(RegistrationActivity.this, U.SH_AGE));
                    params.put("course", pref.getString(RegistrationActivity.this, U.SH_COURSE));
                    params.put("work_status", pref.getString(RegistrationActivity.this, U.SH_WORK_STATUS));
                    params.put("skills", pref.getString(RegistrationActivity.this, U.SH_SKILLS));
                    params.put("skill_string", pref.getString(RegistrationActivity.this, U.SH_ADDED_SKILLS));
                    params.put("job-preferred-location", pref.getString(RegistrationActivity.this, U.SH_JOBLOCATION));
                    params.put("detail_shown_employer", pref.getString(RegistrationActivity.this, U.SH_DETAILS_SHOWN));
                    params.put("employee_id", pref.getString(RegistrationActivity.this, U.SH_EMPLOYEE_ID));
                    params.put("vcode", String.valueOf(U.versioncode_get(RegistrationActivity.this)));
                } else if (param.equals(SU.CHANGE_MOBILE_NUMBER)) {
                    params.put("action", param);
                    params.put("mobile_new", pref.getString(RegistrationActivity.this, U.SH_CHANGE_MOBILE));
                    params.put("employee_id", pref.getString(RegistrationActivity.this, U.SH_EMPLOYEE_ID));
                } else {
                    params.put("action", param);
                }
                Log.e("params", ""+params);
                return params;
            }
        };

        MySingleton.getInstance(RegistrationActivity.this).addToRequestQue(stringRequest);
    }

    private void showJSON(String param, String json) {
        Log.e("showresponse", "" + json);
        if (param.equals(SU.REGISTRATION_Q)) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                qualificationList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    ArrayItem data = new ArrayItem();
                    data.setId(jo.getInt("id"));
                    data.setItem(jo.getString(SU.QUALIFICATION));
                    JSONArray jArray = jo.getJSONArray("course");
                    List<Item> subList = new ArrayList<>();
                    for (int j = 0; j < jArray.length(); j++) {
                        try {
                            JSONObject obj = jArray.getJSONObject(j);
                            Item cdata = new Item();
                            cdata.setId(obj.getInt("id"));
                            cdata.setItem(obj.getString("course"));
                            subList.add(cdata);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    data.setList(subList);
                    qualificationList.add(data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            loadJSON(SU.REGISTRATION_L);
        }
        if (param.equals(SU.REGISTRATION_S)) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                skillsList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    ArrayItem data = new ArrayItem();
                    data.setId(jo.getInt("id"));
                    data.setItem(jo.getString("field"));
                    JSONArray jArray = jo.getJSONArray("skills");
                    List<Item> subList = new ArrayList<>();
                    for (int j = 0; j < jArray.length(); j++) {
                        try {
                            JSONObject obj = jArray.getJSONObject(j);
                            Item cdata = new Item();
                            cdata.setId(obj.getInt("id"));
                            cdata.setItem(obj.getString("skills"));
                            subList.add(cdata);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    data.setList(subList);
                    skillsList.add(data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            loadJSON(SU.REGISTRATION_Q);

        }
        if (param.equals(SU.REGISTRATION_L)) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                locationList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    ArrayItem data = new ArrayItem();
                    data.setId(i);
                    data.setItem(jo.getString("district"));
                    JSONArray jArray = jo.getJSONArray("areas");
                    List<Item> subList = new ArrayList<>();
                    for (int j = 0; j < jArray.length(); j++) {
                        try {
                            JSONObject obj = jArray.getJSONObject(j);
                            Item cdata = new Item();
                            cdata.setId(obj.getInt("id"));
                            cdata.setItem(obj.getString("area_name"));
                            subList.add(cdata);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    data.setList(subList);
                    locationList.add(data);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (pref.getString(RegistrationActivity.this, U.SH_MOBILE).equals("") || !pref.getBoolean(RegistrationActivity.this, U.SH_OTP_SUCCESS)) {
                mobileNoVerification("");
            } else if ((task.equals(U.SH_JOBLOCATION)) || (task.equals(U.SH_SKILLS))) {
                Intent intent = new Intent(RegistrationActivity.this, ProfileEntryActivity.class);
                intent.putExtra("task", task);
                startActivity(intent);
            } else {
                checkUserAvailability(pref.getString(RegistrationActivity.this, U.SH_MOBILE));
            }
        }
        if (param.equals(SU.REGISTER)) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(json);
                String status = jsonObject.getString("status");

                if (status.equals("Registration_complete")) {
                    pref.putBoolean(RegistrationActivity.this, U.SH_SIGN_UP_SUCCESS, true);
                    pref.putString(RegistrationActivity.this, U.SH_PHOTO_URI, "");
                    pref.putString(RegistrationActivity.this, U.SH_COMPLETED_JSON, jsonObject.toString());
                    getValues(jsonObject);
                    pref.putInt(RegistrationActivity.this, U.SH_RJOBS_FLAG, 1);
                    finish();

                } else if (status.equals("invalid_user")) {
                    L.t(RegistrationActivity.this, "" + getResources().getString(R.string.profile_expired));
                    pref.putBoolean(RegistrationActivity.this, U.SH_OTP_SUCCESS, false);
                    pref.putBoolean(RegistrationActivity.this, U.SH_SIGN_UP_SUCCESS, false);
                    clearSPValues();
                    mobileNoVerification("invalid_user");

                } else if (status.equals("No Mobile Number")) {
                    L.t(RegistrationActivity.this, "No Mobile Number");
                    mobileNoVerification("");

                }

            } catch (JSONException e) {
                e.printStackTrace();
                pref.putBoolean(RegistrationActivity.this, U.SH_SIGN_UP_SUCCESS, false);
            }
        }
        if (param.equals(SU.UPDATE)) {
            Log.e("update_response", "" + json);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(json);
                String status = jsonObject.getString("status");

                if (status.equals("Registration_complete")) {
                    L.t(RegistrationActivity.this, "Profile Updated Successfully");
                    pref.putString(RegistrationActivity.this, U.SH_PHOTO_URI, "");
                    pref.putString(RegistrationActivity.this, U.SH_COMPLETED_JSON, jsonObject.toString());
                    getValues(jsonObject);
                    pref.putInt(RegistrationActivity.this, U.SH_RJOBS_FLAG, 1);
                    finish();

                } else if (status.equals("invalid_user")) {
                    L.t(RegistrationActivity.this, "" + getResources().getString(R.string.profile_expired));
                    pref.putBoolean(RegistrationActivity.this, U.SH_OTP_SUCCESS, false);
                    pref.putBoolean(RegistrationActivity.this, U.SH_SIGN_UP_SUCCESS, false);
                    clearSPValues();
                    mobileNoVerification("invalid_user");

                } else if (status.equals("No Mobile Number")) {
                    L.t(RegistrationActivity.this, "No Mobile Number");
                    mobileNoVerification("");

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (param.equals(SU.CHANGE_MOBILE_NUMBER)) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(json);
                String status = jsonObject.getString("status");
                if (status.equals("otp_sent")) {
                    setOtpView(SU.CHANGE_MOBILE_NUMBER);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //----------------------------------------  Values Handling ------------------------------------

    private void getValues(JSONObject jsonObject) {
        try {
            photo = jsonObject.getString("photo");
            employeeId = jsonObject.getString("employee_id");
            name = jsonObject.getString("name");
            email = jsonObject.getString("email");
            mobileno = jsonObject.getString("mobile1");
            altmobileno = jsonObject.getString("mobile2");
            nativeLocation = jsonObject.getString("native_location");
            mobileShown = jsonObject.getString("mobile-shown-employer");
            gender = jsonObject.getString("gender");
            maritalStatus = jsonObject.getString("marital_status");
            String dobArray[] = jsonObject.getString("dob").split("-");
            String dobval = dobArray[2] + "-" + dobArray[1] + "-" + dobArray[0];
            dob = dobval;
            age = jsonObject.getString("age");
            courses = jsonObject.getString("course");
            workStatus = jsonObject.getString("work_status");
            skills = jsonObject.getString("skills");
            jobPrefferedLocation = jsonObject.getString("job-preferred-location");
            detailsShown = jsonObject.getString("detail-shown-employer");
            otp = jsonObject.getString("otp");

            pref.putString(RegistrationActivity.this, U.SH_ADDED_SKILLS, jsonObject.getString("skill_string"));
            pref.putString(RegistrationActivity.this, U.SH_PHOTO, photo);
            pref.putString(RegistrationActivity.this, U.SH_EMPLOYEE_ID, employeeId);
            pref.putString(RegistrationActivity.this, U.SH_NAME, name);
            pref.putString(RegistrationActivity.this, U.SH_EMAIL, email);
            pref.putString(RegistrationActivity.this, U.SH_MOBILE, mobileno);
            pref.putString(RegistrationActivity.this, U.SH_ALTERNATE_MOBILE, altmobileno);
            pref.putString(RegistrationActivity.this, U.SH_NATIVE_LOCATION, nativeLocation);
            pref.putString(RegistrationActivity.this, U.SH_MOBILE_SHOWN, mobileShown);
            pref.putString(RegistrationActivity.this, U.SH_GENDER, gender);
            pref.putString(RegistrationActivity.this, U.SH_MARITAL_STATUS, maritalStatus);
            pref.putString(RegistrationActivity.this, U.SH_DOB, dob);
            pref.putString(RegistrationActivity.this, U.SH_AGE, age);
            pref.putString(RegistrationActivity.this, U.SH_COURSE, courses);
            pref.putString(RegistrationActivity.this, U.SH_WORK_STATUS, workStatus);
            pref.putString(RegistrationActivity.this, U.SH_SKILLS, skills);
            pref.putString(RegistrationActivity.this, U.SH_JOBLOCATION, jobPrefferedLocation);
            pref.putString(RegistrationActivity.this, U.SH_DETAILS_SHOWN, detailsShown);
            pref.putString(RegistrationActivity.this, U.SH_OTP, otp);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void clearSPValues() {
        employeeId = "";
        name = "";
        email = "";
        mobileno = "";
        altmobileno = "";
        nativeLocation = "";
        mobileShown = "";
        gender = "";
        maritalStatus = "";
        dob = "";
        age = "";
        courses = "";
        workStatus = "";
        skills = "";
        jobPrefferedLocation = "";
        detailsShown = "";
        otp = "";

        pref.putString(RegistrationActivity.this, U.SH_ADDED_SKILLS, "");
        pref.putString(RegistrationActivity.this, U.SH_EMPLOYEE_ID, "");
        pref.putString(RegistrationActivity.this, U.SH_NAME, "");
        pref.putString(RegistrationActivity.this, U.SH_EMAIL, "");
        pref.putString(RegistrationActivity.this, U.SH_MOBILE, "");
        pref.putString(RegistrationActivity.this, U.SH_ALTERNATE_MOBILE, "");
        pref.putString(RegistrationActivity.this, U.SH_NATIVE_LOCATION, "");
        pref.putString(RegistrationActivity.this, U.SH_MOBILE_SHOWN, "");
        pref.putString(RegistrationActivity.this, U.SH_GENDER, "");
        pref.putString(RegistrationActivity.this, U.SH_MARITAL_STATUS, "");
        pref.putString(RegistrationActivity.this, U.SH_DOB, "");
        pref.putString(RegistrationActivity.this, U.SH_AGE, "");
        pref.putString(RegistrationActivity.this, U.SH_COURSE, "");
        pref.putString(RegistrationActivity.this, U.SH_WORK_STATUS, "");
        pref.putString(RegistrationActivity.this, U.SH_SKILLS, "");
        pref.putString(RegistrationActivity.this, U.SH_JOBLOCATION, "");
        pref.putString(RegistrationActivity.this, U.SH_DETAILS_SHOWN, "");
        pref.putString(RegistrationActivity.this, U.SH_OTP, "");

    }

    //------------------------------------------- View Profile --------------------------------------

    private void ViewProfile() {
        setStatusBarTranslucent(true);
        setContentView(R.layout.view_profile_lay);
        current_page = 5;
        pref.putString(RegistrationActivity.this, U.SH_PHOTO_URI, "");
        if (!pref.getString(RegistrationActivity.this, U.SH_COMPLETED_JSON).equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(pref.getString(RegistrationActivity.this, U.SH_COMPLETED_JSON));
                getValues(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        txtName = findViewById(R.id.name);
        txtEmail = findViewById(R.id.email);
        txtMobile = findViewById(R.id.Mobileno);
        txtDetails = findViewById(R.id.details);
        txtNatLocation = findViewById(R.id.nativelocation);
        noValues = findViewById(R.id.novalues);
        prophoto = findViewById(R.id.photo);
        fbl = findViewById(R.id.fbl);
        skillsLay = findViewById(R.id.skills_lay);
        qualificationLay = findViewById(R.id.qualification_lay);
        locationLay = findViewById(R.id.location_lay);
        profileEdit = findViewById(R.id.profile_edit);
        changeNumber = findViewById(R.id.number_change);
        ProfileDelete = findViewById(R.id.profile_delete);
        String genderStr = "";
        String maritalStatusStr = "";
        String agestr = "";
        String detailsStr = "";

        if (pref.getInt(RegistrationActivity.this, U.SH_GUIDE_WINDOW_1) == 0) {
            final Dialog guideWindow = new Dialog(RegistrationActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
            guideWindow.setContentView(R.layout.guide_window_viewprofile);
            TextView searchText = guideWindow.findViewById(R.id.search_text);
            searchText.setText(getResources().getString(R.string.guide_update_profile));
            Button ok = guideWindow.findViewById(R.id.ok_btn);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pref.putInt(RegistrationActivity.this, U.SH_GUIDE_WINDOW_1, 1);
                    guideWindow.dismiss();
                }
            });
            guideWindow.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    pref.putInt(RegistrationActivity.this, U.SH_GUIDE_WINDOW_1, 1);
                }
            });
            guideWindow.show();
        }

        if (!pref.getString(RegistrationActivity.this, U.SH_NAME).equals("")) {
            String name = pref.getString(RegistrationActivity.this, U.SH_NAME);
            txtName.setText(name);
        }

        if (!pref.getString(RegistrationActivity.this, U.SH_PHOTO_URI).equals("")) {

            if (!isFinishing()) {
                Uri photoUri = Uri.parse(pref.getString(RegistrationActivity.this, U.SH_PHOTO_URI));
                Glide.with(this)
                        .load(photoUri)
                        .asBitmap()
                        .placeholder(R.drawable.user)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .centerCrop()
                        .into(new BitmapImageViewTarget(prophoto) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                prophoto.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            }

        } else if (pref.getBoolean(RegistrationActivity.this, U.SH_SIGN_UP_SUCCESS)) {

            if (!isFinishing()) {
                String photoValue = pref.getString(RegistrationActivity.this, U.SH_PHOTO);
                Glide.with(this)
                        .load(SU.BASE_URL + photoValue)
                        .asBitmap()
                        .placeholder(R.drawable.user)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .centerCrop()
                        .into(new BitmapImageViewTarget(prophoto) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                prophoto.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            }
        }

        if (!pref.getString(RegistrationActivity.this, U.SH_NATIVE_LOCATION).equals("")) {
            for (int i = 0; i < RegistrationActivity.locationList.size(); i++) {
                for (int j = 0; j < RegistrationActivity.locationList.get(i).getList().size(); j++) {
                    if (pref.getString(RegistrationActivity.this, U.SH_NATIVE_LOCATION).equals("" + RegistrationActivity.locationList.get(i).getList().get(j).getId())) {
                        txtNatLocation.setVisibility(View.VISIBLE);
                        txtNatLocation.setText(RegistrationActivity.locationList.get(i).getList().get(j).getItem());
                    }
                }
            }
        } else {
            txtNatLocation.setVisibility(View.GONE);
        }

        if (!pref.getString(RegistrationActivity.this, U.SH_EMAIL).equals("")) {
            txtEmail.setText(pref.getString(RegistrationActivity.this, U.SH_EMAIL));
        }

        if (!pref.getString(RegistrationActivity.this, U.SH_MOBILE).equals("")) {
            if (!pref.getString(RegistrationActivity.this, U.SH_ALTERNATE_MOBILE).equals("")) {
                txtMobile.setText(pref.getString(RegistrationActivity.this, U.SH_MOBILE) + " | " + pref.getString(RegistrationActivity.this, U.SH_ALTERNATE_MOBILE));
            } else {
                txtMobile.setText(pref.getString(RegistrationActivity.this, U.SH_MOBILE));
            }
        }

        if (!pref.getString(RegistrationActivity.this, U.SH_GENDER).equals("")) {
            String gender = pref.getString(RegistrationActivity.this, U.SH_GENDER);
            if (gender.equals("0")) genderStr = "Male";
            else genderStr = "Female";
            detailsStr = genderStr;
        }

        if (!pref.getString(RegistrationActivity.this, U.SH_MARITAL_STATUS).equals("")) {
            String maritalStatus = pref.getString(RegistrationActivity.this, U.SH_MARITAL_STATUS);
            if (maritalStatus.equals("0")) maritalStatusStr = "UnMarried";
            else maritalStatusStr = "Married";
            if (!genderStr.equals("")) {
                detailsStr = genderStr + " | " + maritalStatusStr;
            } else {
                detailsStr = maritalStatusStr;
            }
        }

        if (!pref.getString(RegistrationActivity.this, U.SH_AGE).equals("")) {
            agestr = pref.getString(RegistrationActivity.this, U.SH_AGE);
            detailsStr = agestr;
            if (!genderStr.equals("")) {
                detailsStr = genderStr + " | " + agestr;
            }
            if (!maritalStatusStr.equals("")) {
                detailsStr = maritalStatusStr + " | " + agestr;
            }
            if (!genderStr.equals("") && !maritalStatusStr.equals("")) {
                detailsStr = genderStr + " | " + maritalStatusStr + " | " + agestr;
            }
        }

        txtDetails.setText("" + detailsStr);

        skillsLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pref.getString(RegistrationActivity.this, U.SH_SKILLS).equals("")) {
                    RegistrationActivity.skills = pref.getString(RegistrationActivity.this, U.SH_SKILLS);
                    String skillsArray[] = RegistrationActivity.skills.split(",");
                    List<String> selectedSkills = new ArrayList<>();
                    for (int i = 0; i < skillsArray.length; i++) {
                        selectedSkills.add(skillsArray[i]);
                    }
                    noValues.setVisibility(View.GONE);
                    fbl.setVisibility(View.VISIBLE);
                    setProfileValues(RegistrationActivity.skillsList, selectedSkills, fbl);
                } else {
                    fbl.setVisibility(View.GONE);
                    noValues.setVisibility(View.VISIBLE);
                    noValues.setText("Edit profile to add your skills");
                }
            }
        });

        qualificationLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pref.getString(RegistrationActivity.this, U.SH_COURSE).equals("")) {
                    RegistrationActivity.courses = pref.getString(RegistrationActivity.this, U.SH_COURSE);
                    String qualificationArray[] = RegistrationActivity.courses.split(",");
                    List<String> selectedqualification = new ArrayList<>();
                    for (int i = 0; i < qualificationArray.length; i++) {
                        selectedqualification.add(qualificationArray[i]);
                    }
                    fbl.setVisibility(View.VISIBLE);
                    noValues.setVisibility(View.GONE);
                    setProfileValues(RegistrationActivity.qualificationList, selectedqualification, fbl);
                } else {
                    fbl.setVisibility(View.GONE);
                    noValues.setVisibility(View.VISIBLE);
                    noValues.setText("Edit profile to add your Qualification");
                }
            }
        });

        locationLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pref.getString(RegistrationActivity.this, U.SH_JOBLOCATION).equals("")) {
                    RegistrationActivity.jobPrefferedLocation = pref.getString(RegistrationActivity.this, U.SH_JOBLOCATION);
                    String locationArray[] = RegistrationActivity.jobPrefferedLocation.split(",");
                    List<String> selectedLocation = new ArrayList<>();
                    for (int i = 0; i < locationArray.length; i++) {
                        selectedLocation.add(locationArray[i]);
                    }
                    fbl.setVisibility(View.VISIBLE);
                    noValues.setVisibility(View.GONE);
                    setProfileValues(RegistrationActivity.locationList, selectedLocation, fbl);
                } else {
                    fbl.setVisibility(View.GONE);
                    noValues.setVisibility(View.VISIBLE);
                    noValues.setText("Edit profile to add your Location");
                }

            }
        });

        skillsLay.performClick();

        profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, ProfileEntryActivity.class);
                intent.putExtra("task", SU.UPDATE);
                startActivity(intent);
            }
        });

        changeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobileNoVerification(SU.CHANGE_MOBILE_NUMBER);
            }
        });

        ProfileDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d = new Dialog(RegistrationActivity.this, R.style.Base_Theme_AppCompat_Dialog_Alert);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                d.setContentView(R.layout.delete_profile_lay);
                Button yes = d.findViewById(R.id.yes);
                Button no = d.findViewById(R.id.no);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteProfile();
                        d.dismiss();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                d.show();
            }
        });
    }

    //------------------------------------------- Delete Profile -----------------------------------

    private void deleteProfile() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.REGISTRATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("profile deleted")) {
                                pref.putBoolean(RegistrationActivity.this, U.SH_BLOCKED_USER, false);
                                pref.putString(RegistrationActivity.this, U.SH_COMPLETED_JSON, "");
                                clearSPValues();
                                pref.putBoolean(RegistrationActivity.this, U.SH_SIGN_UP_SUCCESS, false);
                                pref.putBoolean(RegistrationActivity.this, U.SH_OTP_SUCCESS, false);
                                pref.putString(RegistrationActivity.this, U.SH_PHOTO_URI, "");
                                pref.putInt(RegistrationActivity.this, U.SH_EMAIL_DIA_SHOW, 0);
                                Toast.makeText(RegistrationActivity.this, "Profile Deleted Successfully", Toast.LENGTH_SHORT).show();
                                pref.putInt(RegistrationActivity.this, U.SH_RJOBS_FLAG, 1);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            L.t(RegistrationActivity.this, U.INA);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorHandling(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", SU.DELETE);
                params.put("employee_id", pref.getString(RegistrationActivity.this, U.SH_EMPLOYEE_ID));
                return params;
            }

        };

        MySingleton.getInstance(RegistrationActivity.this).addToRequestQue(stringRequest);
    }

    //------------------------------------------- others -------------------------------------------

    private void setProfileValues(List<ArrayItem> list, List<String> selectedList, final FlexboxLayout fbl) {
        final List<ArrayItem> templist = new ArrayList<ArrayItem>();
        for (int i = 0; i < list.size(); i++) {
            List<Item> tempList1 = new ArrayList<Item>();
            List<Item> temp = list.get(i).getList();
            for (int j = 0; j < temp.size(); j++) {
                int id = list.get(i).getList().get(j).getId();
                if (selectedList.contains("" + id)) {
                    Item data = new Item();
                    data.setId(list.get(i).getList().get(j).getId());
                    data.setItem(list.get(i).getList().get(j).getItem());
                    tempList1.add(data);
                }
            }
            if (tempList1.size() > 0) {
                ArrayItem data = new ArrayItem();
                data.setId(list.get(i).getId());
                data.setItem(list.get(i).getItem());
                data.setList(tempList1);
                templist.add(data);
            }
        }

        fbl.removeAllViews();
        for (int i = 0; i < templist.size(); i++) {
            final View subchild = getLayoutInflater().inflate(R.layout.single_chip, null);
            final TextView tv = subchild.findViewById(R.id.txt_title);
            final ImageView remove = subchild.findViewById(R.id.remove);
            remove.setVisibility(View.GONE);
            tv.setId(i);
            tv.setText(templist.get(i).getItem() + "  +");
            tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater layoutin = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View popupview = layoutin.inflate(R.layout.location_popup, null);
                    final PopupWindow popupwindow = new PopupWindow(popupview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    final FlexboxLayout fblcities = popupview.findViewById(R.id.fbl_cities);
                    final List<Item> temp = templist.get(tv.getId()).getList();
                    for (int j = 0; j < temp.size(); j++) {
                        final View subchild = getLayoutInflater().inflate(R.layout.single_chip, null);
                        TextView tv = subchild.findViewById(R.id.txt_title);
                        final ImageView remove = subchild.findViewById(R.id.remove);
                        remove.setVisibility(View.GONE);
                        tv.setId(j);
                        tv.setText(temp.get(j).getItem());
                        tv.setBackgroundResource(R.drawable.chip_selected);
                        tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fblcities.addView(subchild);
                            }
                        });
                    }
                    popupwindow.setBackgroundDrawable(new BitmapDrawable());
                    popupwindow.setOutsideTouchable(true);
                    popupwindow.showAsDropDown(v);
                }
            });
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fbl.addView(subchild);
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (RegistrationActivity.Qpopupwindow != null && RegistrationActivity.Qpopupwindow.isShowing()) {
                    RegistrationActivity.Qpopupwindow.dismiss();
                }
                if (RegistrationActivity.Spopupwindow != null && RegistrationActivity.Spopupwindow.isShowing()) {
                    RegistrationActivity.Spopupwindow.dismiss();
                }
                if (RegistrationActivity.Lpopupwindow != null && RegistrationActivity.Lpopupwindow.isShowing()) {
                    RegistrationActivity.Lpopupwindow.dismiss();
                }
                if (current_page == 0) {
                    finish();
                } else if (current_page == 1) {
                    finish();
                } else if (current_page == 2) {
                    if (pref.getBoolean(RegistrationActivity.this, U.SH_SIGN_UP_SUCCESS) && pref.getBoolean(RegistrationActivity.this, U.SH_OTP_SUCCESS)) {
                        ViewProfile();
                    } else {
                        finish();
                    }
                } else if (current_page == 3) {
                    finish();
                } else if (current_page == 4) {
                    if (pref.getBoolean(RegistrationActivity.this, U.SH_SIGN_UP_SUCCESS) && pref.getBoolean(RegistrationActivity.this, U.SH_OTP_SUCCESS)) {
                        ViewProfile();
                    } else {
                        finish();
                    }
                } else if (current_page == 5) {
                    finish();
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.WEBP, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pref.getInt(RegistrationActivity.this, U.SH_REGISTRATION_FLAG) == 1) {
            if (!pref.getString(RegistrationActivity.this, U.SH_REGISTRATION_TASK).equals("")) {
                initialPage();
                loadJSON(pref.getString(RegistrationActivity.this, U.SH_REGISTRATION_TASK));
            } else {
                if (pref.getBoolean(RegistrationActivity.this, U.SH_SIGN_UP_SUCCESS) && pref.getBoolean(RegistrationActivity.this, U.SH_OTP_SUCCESS)) {
                    ViewProfile();
                } else {
                    finish();
                }
            }
            pref.putInt(RegistrationActivity.this, U.SH_REGISTRATION_FLAG, 0);
            pref.putString(RegistrationActivity.this, U.SH_REGISTRATION_TASK, "");
        }
    }

    public void expandLayout(final List<ArrayItem> templist, int checkposition, final FlexboxLayout fbl, int checkexpand) {

        //----------------code for setProfileValues expand--------------------
//        fbl.removeAllViews();
//        final TextView[] textViews = new TextView[templist.size()];
//        for (int i = 0; i < templist.size(); i++) {
//            final View subchild = getLayoutInflater().inflate(R.layout.single_chip, null);
//            final TextView tv = ((TextView) subchild.findViewById(R.id.txt_title));
//            final ImageView remove = (ImageView) subchild.findViewById(R.id.remove);
//            remove.setVisibility(View.GONE);
//            tv.setId(i);
//            tv.setText(templist.get(i).getItem() + "  +");
//            tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
//            tv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(textViews[tv.getId()].getText().toString().trim().contains("+")){
//                        expandLayout(templist,tv.getId(),fbl,0);
//                    }else if(textViews[tv.getId()].getText().toString().trim().contains("-")){
//                        expandLayout(templist,tv.getId(),fbl,1);
//                    }
//                }
//            });
//            textViews[i] = tv;
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    fbl.addView(subchild);
//                }
//            });
//        }


        fbl.removeAllViews();
        final TextView[] textViews1 = new TextView[templist.size()];
        for (int i = 0; i < templist.size(); i++) {
            final View subchild = getLayoutInflater().inflate(R.layout.single_chip, null);
            final TextView tv1 = subchild.findViewById(R.id.txt_title);
            final ImageView remove1 = subchild.findViewById(R.id.remove);
            remove1.setVisibility(View.GONE);
            tv1.setId(i);
            tv1.setText(templist.get(i).getItem() + "  +");
            tv1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
            tv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textViews1[tv1.getId()].getText().toString().trim().contains("+")) {
                        expandLayout(templist, tv1.getId(), fbl, 0);
                    } else if (textViews1[tv1.getId()].getText().toString().trim().contains("-")) {
                        expandLayout(templist, tv1.getId(), fbl, 1);
                    }
                }
            });
            textViews1[i] = tv1;
            fbl.addView(subchild);

            if (checkexpand == 0) {
                if (tv1.getId() == checkposition) {
                    textViews1[i].setText(templist.get(checkposition).getItem() + "  -");
                    final List<Item> temp = templist.get(checkposition).getList();
                    for (int j = 0; j < temp.size(); j++) {
                        final View subchild1 = getLayoutInflater().inflate(R.layout.single_chip, null);
                        TextView tv2 = subchild1.findViewById(R.id.txt_title);
                        final ImageView remove2 = subchild1.findViewById(R.id.remove);
                        remove2.setVisibility(View.GONE);
                        tv2.setId(j);
                        tv2.setText(temp.get(j).getItem());
                        tv2.setBackgroundResource(R.drawable.chip_selected);
                        tv2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                        fbl.addView(subchild1);
                    }
                }
            } else {
                fbl.removeAllViews();
                final TextView[] textViews = new TextView[templist.size()];
                for (int k = 0; k < templist.size(); k++) {
                    final View subchild2 = getLayoutInflater().inflate(R.layout.single_chip, null);
                    final TextView tv2 = subchild2.findViewById(R.id.txt_title);
                    final ImageView remove2 = subchild2.findViewById(R.id.remove);
                    remove2.setVisibility(View.GONE);
                    tv2.setId(k);
                    tv2.setText(templist.get(k).getItem() + "  +");
                    tv2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
                    tv2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (textViews[tv2.getId()].getText().toString().trim().contains("+")) {
                                expandLayout(templist, tv2.getId(), fbl, 0);
                            } else if (textViews[tv2.getId()].getText().toString().trim().contains("-")) {
                                expandLayout(templist, tv2.getId(), fbl, 1);
                            }
                        }
                    });
                    textViews[k] = tv2;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fbl.addView(subchild2);
                        }
                    });
                }
            }
        }
    }
}