package nithra.jobs.career.placement.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import nithra.jobs.career.placement.MainActivity;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.activity.RegistrationActivity;
import nithra.jobs.career.placement.networking.MySingleton;
import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

public class RecommendedFragment extends Fragment {
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout ads, fragHolder, frontLay;
    AlertDialog alertDialog;
    int flag = 0;
    SharedPreference pref;
    SpinKitView progressBar;
    TextView txtError1;
    View view;
    Button networkRetry;
    private TextView btnBrowse;
    private Button btnEdit;
    private String btnStatus = "";
    private RelativeLayout header;
    private LinearLayout lError, cardLocation, cardQualification, cardRecommend, cardSkills;
    private ImageView logo, block;
    private String profileStatus = "";
    private SwipeRefreshLayout swipeContainer;
    private WebView infotext;

    public static RecommendedFragment newInstance() {
        return new RecommendedFragment();
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
    }

    public View onCreateView(@NonNull LayoutInflater LayoutInflater, ViewGroup ViewGroup, Bundle paramBundle) {
        view = LayoutInflater.inflate(R.layout.recommended_lay, ViewGroup, false);
        pref = new SharedPreference();
        return view;
    }

    public void onViewCreated(@NonNull View paramView, @Nullable Bundle paramBundle) {
        super.onViewCreated(paramView, paramBundle);
        frontLay = paramView.findViewById(R.id.front_lay);
        fragHolder = paramView.findViewById(R.id.fragment_container);
        ads = paramView.findViewById(R.id.ads);
        header = paramView.findViewById(R.id.header);
        lError = paramView.findViewById(R.id.lError);
        progressBar = paramView.findViewById(R.id.progressBar);
        btnEdit = paramView.findViewById(R.id.btnEdit);
        infotext = paramView.findViewById(R.id.txtStatus);
        logo = paramView.findViewById(R.id.logo);
        block = paramView.findViewById(R.id.block);
        cardRecommend = paramView.findViewById(R.id.cardRecommand);
        cardLocation = paramView.findViewById(R.id.cardLocation);
        cardSkills = paramView.findViewById(R.id.cardSkills);
        cardQualification = paramView.findViewById(R.id.cardQualification);
        btnBrowse = paramView.findViewById(R.id.btnBrowse);
        txtError1 = paramView.findViewById(R.id.txtError1);
        networkRetry = paramView.findViewById(R.id.network_retry);
        swipeContainer = paramView.findViewById(R.id.swipeContainer);
    }

    public void onActivityCreated(@Nullable Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        MainActivity.homePagePosition = U.RECOMMENDED_PAGE;

        if (getActivity() != null)
            if (pref.getInt(getActivity(), U.SH_AD_PURCHASED) == 0) {
                if (U.isNetworkAvailable(getActivity())) {
                    ads.setVisibility(View.VISIBLE);
                    MainActivity.showAd(getActivity(), ads, false);
                }
            }

        load();

        swipeContainer.setColorSchemeResources(R.color.green, R.color.orange, R.color.lightblue);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                                @Override
                                                public void onRefresh() {
                                                    load();
                                                    swipeContainer.setRefreshing(false);
                                                }
                                            }
        );

        cardRecommend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (getActivity() != null) {
                    setModeFragment(U.SH_FIRST_TIME_RLOCATION, U.RJOB, pref.getString(getActivity(), U.SH_SKILLS), pref.getString(getActivity(), U.SH_JOBLOCATION),
                            pref.getString(getActivity(), U.SH_COURSE), pref.getString(getActivity(), U.SH_EMPLOYEE_ID), pref.getString(getActivity(), U.SH_GENDER),
                            pref.getString(getActivity(), U.SH_MARITAL_STATUS), "எங்களால் பரிந்துரைக்கப்படுபவை");
                }
            }
        });

        cardLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (getActivity() != null) {
                    if (pref.getString(getActivity(), U.SH_JOBLOCATION).isEmpty()) {
                        showAlertDialog(getActivity().getResources().getString(R.string.select_location), U.SH_JOBLOCATION);
                    } else {
                        setModeFragment(U.SH_FIRST_TIME_RLOCATION, U.RJOB, "", pref.getString(getActivity(), U.SH_JOBLOCATION), "", pref.getString(getActivity(), U.SH_EMPLOYEE_ID),
                                pref.getString(getActivity(), U.SH_GENDER), pref.getString(getActivity(), U.SH_MARITAL_STATUS), "நீங்கள் தேர்ந்தெடுத்த இடங்களில் உள்ளவை");
                    }
                }
            }
        });

        cardSkills.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (getActivity() != null) {
                    if (pref.getString(getActivity(), U.SH_SKILLS).equals("0")) {
                        showAlertDialog(getActivity().getResources().getString(R.string.select_skills), U.SH_SKILLS);
                    } else {
                        setModeFragment(U.SH_FIRST_TIME_RSKILLS, U.RJOB, pref.getString(getActivity(), U.SH_SKILLS), "", "", pref.getString(getActivity(), U.SH_EMPLOYEE_ID),
                                pref.getString(getActivity(), U.SH_GENDER), pref.getString(getActivity(), U.SH_MARITAL_STATUS), "உங்கள் திறமைக்கானவை");

                    }
                }
            }
        });

        cardQualification.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (getActivity() != null) {
                    setModeFragment(U.SH_FIRST_TIME_RQUALIFICATION, U.RJOB, "", "", pref.getString(getActivity(), U.SH_COURSE), pref.getString(getActivity(), U.SH_EMPLOYEE_ID),
                            pref.getString(getActivity(), U.SH_GENDER), pref.getString(getActivity(), U.SH_MARITAL_STATUS), "உங்கள் படிப்பிற்கானவை");
                }
            }
        });

        btnBrowse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                callAllJobs();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (U.isNetworkAvailable(getActivity())) {
                    Intent localIntent = new Intent(getActivity(), RegistrationActivity.class);
                    localIntent.putExtra("task", "Edit");
                    startActivity(localIntent);
                } else {
                    errorView(U.INA);
                }
            }
        });

        networkRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
            }
        });
    }

    public int checkFirstTime(String key) {
        int value;
        if (pref.getString(getActivity(), key).equals(U.currentDate())) {
            value = 1;
        } else {
            value = 0;
            pref.putString(getActivity(), key, U.currentDate());
        }
        return value;
    }

    public void setModeFragment(String key, String action, String skill, String location, String qualification, String empId,
                                String gender, String marital_status, String title) {
        frontLay.setVisibility(View.GONE);
        fragHolder.setVisibility(View.VISIBLE);
        ads.setVisibility(View.GONE);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction;
        if (fragmentManager != null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            JobListFragment cat = JobListFragment.newInstance(action, skill, location, qualification,
                    empId, gender, marital_status, checkFirstTime(key), title);
            if (fragmentTransaction != null) {
                fragmentTransaction.replace(R.id.fragment_container, cat, "HELLO");
                fragmentTransaction.commit();
            }
        }
    }

    public void showAlertDialog(String msg, final String task) {
        if (getActivity() != null) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
            localBuilder.setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton("ஆம்", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                            if (U.isNetworkAvailable(getActivity())) {
                                Intent localIntent = new Intent(getActivity(), RegistrationActivity.class);
                                localIntent.putExtra("task", task);
                                startActivity(localIntent);
                            } else {
                                errorView(U.INA);
                            }
                        }
                    }).setNegativeButton("இல்லை", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                    alertDialog.dismiss();
                }
            });
            alertDialog = localBuilder.create();
            alertDialog.show();
        }
    }

    public void callAllJobs() {
        frontLay.setVisibility(View.GONE);
        fragHolder.setVisibility(View.VISIBLE);
        ads.setVisibility(View.GONE);
        FragmentTransaction localFragmentTransaction = null;
        if (getFragmentManager() != null) {
            localFragmentTransaction = getFragmentManager().beginTransaction();
        }
        if (localFragmentTransaction != null) {
            localFragmentTransaction.replace(R.id.fragment_container, JobListFragment.newInstance(U.ALLJOBS, "", "3"), "HELLO");
            localFragmentTransaction.commit();
        }
    }

    private void errorView(String paramString) {
        txtError1.setText(paramString);
        lError.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        header.setVisibility(View.GONE);
        fragHolder.setVisibility(View.GONE);
        frontLay.setVisibility(View.GONE);
    }

    private void preLoading() {
        lError.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        fragHolder.setVisibility(View.GONE);
        frontLay.setVisibility(View.GONE);
    }

    private void errorHandling(VolleyError error) {
        String e;
        if (error instanceof TimeoutError || error instanceof AuthFailureError || error instanceof ServerError)
            e = U.SERVER_ERROR;
        else if (error instanceof NetworkError)
            e = U.INA;
        else if (error instanceof ParseError)
            e = U.ERROR;
        else
            e = U.ERROR;
        errorView(e);
    }

    public void load() {
        if (getActivity() != null) {
            if (U.isNetworkAvailable(getActivity())) {
                preLoading();
                checkUserAvailability(pref.getString(getActivity(), U.SH_MOBILE));
            } else {
                errorView(U.INA);
            }
        }
    }

    private void checkUserAvailability(final String mobileno) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.REGISTRATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (getActivity() != null) {
                            progressBar.setVisibility(View.GONE);
                            Log.e("checkResponse", "" + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");

                                switch (status) {
                                    case "No Mobile Number":
                                        pref.putBoolean(getActivity(), U.SH_BLOCKED_USER, false);
                                        profileStatus = getResources().getString(R.string.rjobs_text);
                                        btnStatus = getResources().getString(R.string.signup);
                                        setProfileStatus("", profileStatus, btnStatus);
                                        break;
                                    case "user_blocked":
                                        pref.putBoolean(getActivity(), U.SH_BLOCKED_USER, true);
                                        profileStatus = getResources().getString(R.string.blocked_user) + getResources().getString(R.string.user_query);
                                        btnStatus = getResources().getString(R.string.retry);
                                        setProfileStatus(U.SH_BLOCKED_USER, profileStatus, btnStatus);
                                        break;
                                    case "completed":
                                        pref.putBoolean(getActivity(), U.SH_BLOCKED_USER, false);
                                        pref.putString(getActivity(), U.SH_COMPLETED_JSON, jsonObject.toString());
                                        pref.putBoolean(getActivity(), U.SH_SIGN_UP_SUCCESS, true);
                                        if (pref.getBoolean(getActivity(), U.SH_OTP_SUCCESS) && pref.getBoolean(getActivity(), U.SH_SIGN_UP_SUCCESS)) {
                                            frontLay.setVisibility(View.VISIBLE);
                                            fragHolder.setVisibility(View.GONE);
                                            header.setVisibility(View.GONE);
                                            ads.setVisibility(View.VISIBLE);
                                        } else {
                                            profileStatus = getResources().getString(R.string.rjobs_text);
                                            btnStatus = getResources().getString(R.string.signup);
                                            setProfileStatus("", profileStatus, btnStatus);
                                        }
                                        break;
                                    case "In-complete":
                                        pref.putBoolean(getActivity(), U.SH_BLOCKED_USER, false);
                                        pref.putString(getActivity(), U.SH_EMPLOYEE_ID, jsonObject.getString("employee_id"));
                                        if (pref.getBoolean(getActivity(), U.SH_OTP_SUCCESS)) {
                                            profileStatus = getResources().getString(R.string.fill_blanks);
                                            btnStatus = getResources().getString(R.string.contiue);
                                        } else {
                                            profileStatus = getResources().getString(R.string.rjobs_text);
                                            btnStatus = getResources().getString(R.string.signup);
                                        }
                                        setProfileStatus("", profileStatus, btnStatus);
                                        break;
                                    case "New_user":
                                        pref.putBoolean(getActivity(), U.SH_BLOCKED_USER, false);
                                        pref.putString(getActivity(), U.SH_EMPLOYEE_ID, jsonObject.getString("employee_id"));
                                        profileStatus = getResources().getString(R.string.rjobs_text);
                                        btnStatus = getResources().getString(R.string.signup);
                                        setProfileStatus("", profileStatus, btnStatus);
                                        break;
                                    case "invalid_user":
                                        pref.putBoolean(getActivity(), U.SH_BLOCKED_USER, false);
                                        pref.putBoolean(getActivity(), U.SH_SIGN_UP_SUCCESS, false);
                                        pref.putBoolean(getActivity(), U.SH_OTP_SUCCESS, false);
                                        profileStatus = getResources().getString(R.string.profile_expired);
                                        btnStatus = getResources().getString(R.string.signup);
                                        setProfileStatus(U.SH_BLOCKED_USER, profileStatus, btnStatus);
                                        break;
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorHandling(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (getActivity() != null) {
                    params.put("action", SU.CHECK);
                    params.put("employee_id", "" + pref.getString(getActivity(), U.SH_EMPLOYEE_ID));
                    params.put("send_otp", "0");
                    params.put("mobile1", mobileno);
                    params.put("user_type", pref.getString(getActivity(), U.SH_USER_TYPE));
                    params.put("android_id", U.getAndroidId(getActivity()));
                    params.put("vcode", String.valueOf(U.versioncode_get(getActivity())));
                    Log.e("paramsresponse", "" + params);
                }
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQue(stringRequest);
    }

    public void setProfileStatus(String status, String profileStatus, String btnStatus) {

        if (status.equals(U.SH_BLOCKED_USER)) {
            btnEdit.setVisibility(View.GONE);
            logo.setVisibility(View.GONE);
            block.setVisibility(View.VISIBLE);
        } else {
            block.setVisibility(View.GONE);
            logo.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.VISIBLE);
        }

        lError.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        infotext.setBackgroundColor(Color.TRANSPARENT);
        header.setVisibility(View.VISIBLE);

        frontLay.setVisibility(View.GONE);
        fragHolder.setVisibility(View.GONE);
        ads.setVisibility(View.GONE);

        String info = "<b><meta charset=\"utf-8\"><font color=black size=2>\n" +
                profileStatus + "<br><br></font></b>";

        if (getActivity() != null) {
            U.webview(getActivity(), info, infotext);
        }

        btnEdit.setText(btnStatus);
    }

    public void onResume() {
        super.onResume();

        if (flag == 0) {
            if (U.isNetworkAvailable(getActivity())) {
                if (pref.getBoolean(getActivity(), U.SH_SIGN_UP_SUCCESS) && pref.getBoolean(getActivity(), U.SH_OTP_SUCCESS)) {
                    flag = 1;
                    frontLay.setVisibility(View.VISIBLE);
                    fragHolder.setVisibility(View.GONE);
                    header.setVisibility(View.GONE);
                    ads.setVisibility(View.VISIBLE);

                } else if (pref.getBoolean(getActivity(), U.SH_BLOCKED_USER)) {

                    profileStatus = getResources().getString(R.string.blocked_user) + getResources().getString(R.string.user_query);
                    btnStatus = getResources().getString(R.string.retry);
                    setProfileStatus(U.SH_BLOCKED_USER, profileStatus, btnStatus);

                } else if (pref.getBoolean(getActivity(), U.SH_OTP_SUCCESS)) {

                    profileStatus = getResources().getString(R.string.fill_blanks);
                    btnStatus = getResources().getString(R.string.contiue);
                    setProfileStatus("", profileStatus, btnStatus);

                } else if (pref.getString(getActivity(), U.SH_MOBILE).equals("") || !pref.getBoolean(getActivity(), U.SH_OTP_SUCCESS)) {

                    profileStatus = getResources().getString(R.string.rjobs_text);
                    btnStatus = getResources().getString(R.string.signup);
                    setProfileStatus("", profileStatus, btnStatus);
                }

            } else {
                errorView(U.INA);
            }
        }

    }
}

