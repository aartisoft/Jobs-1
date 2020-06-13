package nithra.jobs.career.placement.fragments;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.common.AccountPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import nithra.jobs.career.placement.MainActivity;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.activity.FilterActivity;
import nithra.jobs.career.placement.activity.JobDetailActivity;
import nithra.jobs.career.placement.activity.PopularEmployersDetailActivity;
import nithra.jobs.career.placement.activity.SearchActivity;
import nithra.jobs.career.placement.adapters.RecyclerJobListAdapter;
import nithra.jobs.career.placement.bottomsheets.ShareBottomSheet;
import nithra.jobs.career.placement.bottomsheets.SortBottomSheet;
import nithra.jobs.career.placement.engine.LocalDB;
import nithra.jobs.career.placement.networking.MySingleton;
import nithra.jobs.career.placement.pojo.Jobs;
import nithra.jobs.career.placement.utills.L;
import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

public class JobListFragment extends Fragment implements RecyclerJobListAdapter.OnJobItemClick,
        View.OnClickListener, SortBottomSheet.OnSortClick, SwipeRefreshLayout.OnRefreshListener,
        AddDialogFragment.DialogListener, ShareBottomSheet.OnShareClick {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TASK = "task";
    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final String SKILL = "skill";
    private static final String LOCATION = "location";
    private static final String QUALIFICATION = "qualification";
    private static final String EXPERIENCE = "experience";
    private static final String SALARY = "salary";
    private static final String JOBCAT = "jobcat";
    private static final String MODE = "mode";
    private static final String WORKMODE = "workmode";
    private static final String FIRSTTIME = "firstTime";
    public boolean loading = true;
    private RecyclerView mRecyclerView;
    private List<Jobs> list, moreJobs;
    private RecyclerJobListAdapter mAdapter;
    private TextView txtFound, txtError1, txtFilter, txtSort;
    private ImageView moveUpBtn;
    private LinearLayout lProgress, lError, header;
    private ImageView imgLoading;
    private String data = "";
    private int vcount = 0;
    private LocalDB localDB;
    private String fav = "";
    private SpinKitView progressBar;
    private CardView footer;
    private int order;
    private SharedPreference pref;
    private boolean contentAd = false, installAd = false;
    private int adRange = 1, vacancy = 0;
    private Spinner emailIdspinner;
    private int phnNo = 0;
    private int firstTime;
    private Button networkRetry;
    private String mTask;
    private String mKey;
    private String mValue;
    private String mSkill;
    private String mLocation;
    private String mQualification;
    private String mExperience;
    private String mSalary;
    private String mJobcat;
    private String mWorkmode;
    private String mfirstTime;
    private String mRtitle;
    private String mMode;
    private String mEmployeeId;
    private String mGender;
    private String mMaritalStatus;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeContainer;
    private int lastFirstVisible = -1;
    private int lastVisibleCount = -1;
    private int lastItemCount = -1;
    private String selectedEmailId;
    private String versionCode;
    private int visibleThreshold = 5;
    private int currentPage = 0;
    private int previousTotalItemCount = 0;
    private int startingPageIndex = 0;
    private Boolean LastItem = true;
    private int aRelatedJobsPage = 0;

    public JobListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param task Parameter 1.
     * @param key  Parameter 2.
     * @return A new instance of fragment JobListFragment.
     */
    public static JobListFragment newInstance(String task, String key, String value) {
        JobListFragment fragment = new JobListFragment();
        Bundle args = new Bundle();
        args.putString(TASK, task);
        args.putString(KEY, key);
        args.putString(VALUE, value);
        fragment.setArguments(args);
        return fragment;
    }

    public static JobListFragment newInstance(String task, String skill, String location,
                                              String qualification, String empId,
                                              String gender, String marital_status,
                                              int firstTime, String title) {
        JobListFragment fragment = new JobListFragment();
        Bundle args = new Bundle();
        args.putString("task", task);
        args.putString("skill", skill);
        args.putString("location", location);
        args.putString("qualification", qualification);
        args.putString("employee_id", empId);
        args.putString("gender", gender);
        args.putString("marital_status", marital_status);
        args.putString("title", title);
        args.putString(FIRSTTIME, "" + firstTime);
        fragment.setArguments(args);
        return fragment;
    }

    public static JobListFragment newInstance(String task, String skill, String location,
                                              String qualification, String experience, String salary,
                                              String mode, String jobcat, String workmode, String firstTime) {
        JobListFragment fragment = new JobListFragment();
        Bundle args = new Bundle();
        args.putString(TASK, task);
        args.putString(SKILL, skill);
        args.putString(LOCATION, location);
        args.putString(QUALIFICATION, qualification);
        args.putString(EXPERIENCE, experience);
        args.putString(SALARY, salary);
        args.putString(MODE, mode);
        args.putString(JOBCAT, jobcat);
        args.putString(WORKMODE, workmode);
        args.putString(FIRSTTIME, firstTime);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = new SharedPreference();
        order = 3;
        if (getArguments() != null) {
            mTask = getArguments().getString(TASK);
            mKey = getArguments().getString(KEY);
            mValue = getArguments().getString(VALUE);
            if (mTask.equals(SU.FILTER) || mTask.equals(SU.CATEGORY) || mTask.equals(SU.DISTRICTWISE) || mTask.equals(SU.DISTRICTWISE_NOTIFICATION) || mTask.equals(SU.GETRJOBS)) {
                mSkill = getArguments().getString(SKILL);
                mLocation = getArguments().getString(LOCATION);
                mQualification = getArguments().getString(QUALIFICATION);
                mExperience = getArguments().getString(EXPERIENCE);
                mSalary = getArguments().getString(SALARY);
                mMode = getArguments().getString(MODE);
                mJobcat = getArguments().getString(JOBCAT);
                mWorkmode = getArguments().getString(WORKMODE);
                mfirstTime = getArguments().getString(FIRSTTIME);
            }
            if (mTask.equals(U.RJOB)) {
                mSkill = getArguments().getString("skill");
                mLocation = getArguments().getString("location");
                mQualification = getArguments().getString("qualification");
                mEmployeeId = getArguments().getString("employee_id");
                mGender = getArguments().getString("gender");
                mMaritalStatus = getArguments().getString("marital_status");
                mfirstTime = getArguments().getString(FIRSTTIME);
                mRtitle = getArguments().getString("title");
            }
            if (mTask.equals(U.FJOBS)) {
                setHasOptionsMenu(true);
            }
            if (mTask.equals(U.ALLJOBS)) {
                try {
                    order = Integer.parseInt(mValue);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            if (getActivity() != null) {
                if (pref.getInt(getActivity(), U.SH_AD_PURCHASED) == 0) {
                    if (mTask.equals(U.GROUPJOBS) && mValue.equals(SU.POPULAR_EMP)) {
                        //jobs from same company so Ad need not to be show
                    } else {
                        MainActivity.loadNativeAd(getActivity());
                    }
                }
            }

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (mTask.equals(U.FJOBS)) menu.clear();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        adRange = pref.getInt(getContext(), U.SH_REMOTE_AD);
        if (adRange == 1) {
            contentAd = true;
            installAd = false;
        } else if (adRange == 2) {
            contentAd = false;
            installAd = true;
        } else if (adRange == 3) {
            contentAd = true;
            installAd = true;
        }
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);
        footer = view.findViewById(R.id.footer);
        progressBar = view.findViewById(R.id.progressBar);
        lProgress = view.findViewById(R.id.lProgress);
        lError = view.findViewById(R.id.lError);
        header = view.findViewById(R.id.header);
        imgLoading = view.findViewById(R.id.imgLoading);
        networkRetry = view.findViewById(R.id.network_retry);
        networkRetry.setOnClickListener(this);
        vacancy = pref.getInt(getContext(), U.SH_REMOTE_VACANCY);
        Log.e("vacancy", "" + vacancy);
        if (vacancy == 0) {
            header.setVisibility(View.GONE);
        } else if (vacancy == 1) {
            header.setVisibility(View.VISIBLE);
        }
        txtError1 = view.findViewById(R.id.txtError1);
        txtFilter = view.findViewById(R.id.txtFilter);
        txtFilter.setOnClickListener(this);
        txtSort = view.findViewById(R.id.txtSort);
        txtSort.setOnClickListener(this);
        moveUpBtn = view.findViewById(R.id.moveUpBtn);
        moveUpBtn.setOnClickListener(this);
        txtFound = view.findViewById(R.id.txtFound);
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerJobListAdapter(this, list, mTask, mValue);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void hiddenSearchFileter() {
        switch (mTask) {
            case SU.FILTER:
            case SU.CATEGORY:
            case SU.DISTRICTWISE:
                footer.setVisibility(View.VISIBLE);
                txtFilter.setVisibility(View.GONE);
                break;
            case SU.DISTRICTWISE_NOTIFICATION:
            case SU.APPLIEDJOBS:
            case SU.GETPERSONALISEDJOBS:
                footer.setVisibility(View.GONE);
                break;
            case U.FJOBS:
                footer.setVisibility(View.GONE);
                break;
            case U.NOTIJOBS:
                footer.setVisibility(View.GONE);
                break;
            case U.GROUPJOBS:
                footer.setVisibility(View.GONE);
                break;
            case U.RJOB:
                footer.setVisibility(View.GONE);
                break;
            case U.ALLJOBS:
                if (mKey.equals(U.NOTIFICATION)) {
                    footer.setVisibility(View.GONE);
                } else {
                    footer.setVisibility(View.VISIBLE);
                }
                break;
            case U.ADVSEARCH:
                footer.setVisibility(View.VISIBLE);
                txtSort.setVisibility(View.VISIBLE);
                txtFilter.setVisibility(View.GONE);
                break;
            case SU.GETRECENTJOBS:
                footer.setVisibility(View.VISIBLE);
                txtSort.setVisibility(View.VISIBLE);
                txtFilter.setVisibility(View.GONE);
                break;
            case SU.GETRJOBS:
                footer.setVisibility(View.GONE);
                break;
            case SU.POPULAR_EMP:
                footer.setVisibility(View.GONE);
                break;
            default:
                footer.setVisibility(View.VISIBLE);
                txtSort.setVisibility(View.VISIBLE);
                txtFilter.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mTask.equals(U.FJOBS)) {
            if (MainActivity.viewPager != null) {
                MainActivity.homePagePosition = U.FAVOURITE_PAGE;
            }
        }
        localDB = new LocalDB(getActivity());

        list = new ArrayList<>();
        if (getActivity() != null) {
            versionCode = String.valueOf(U.versioncode_get(getActivity()));
        }

        Glide.with(getActivity())
                .load(R.drawable.loading)
                .asGif()
                .crossFade()
                .into(imgLoading);

        load();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisible = 0;
                if (layoutManager != null) {
                    firstVisible = layoutManager.findFirstVisibleItemPosition();
                }
                int visibleCount = 0;
                if (layoutManager != null) {
                    visibleCount = Math.abs(firstVisible - layoutManager.findLastVisibleItemPosition());
                }
                int itemCount = recyclerView.getAdapter().getItemCount();
                if (firstVisible != lastFirstVisible || visibleCount != lastVisibleCount || itemCount != lastItemCount) {
                    lastFirstVisible = firstVisible;
                    lastVisibleCount = visibleCount;
                    lastItemCount = itemCount;
                }
                if (lastFirstVisible > 10) {
                    moveUpBtn.setVisibility(View.VISIBLE);
                } else {
                    moveUpBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private void load() {
        list.clear();
        resetState();
        if (mTask.equals(U.FJOBS)) {
            fav = localDB.getBookMarks().toString().replace("[", "").replace("]", "").replace(", ", ",");
            Log.e("fav", fav);
        }
        if (mTask.equals(SU.GETRECENTJOBS)) {
            mKey = localDB.getReadIds();
        }
        hiddenSearchFileter();
        if (U.isNetworkAvailable(getActivity())) {
            preLoading();
            loadJSON(mTask);
        } else {
            swipeContainer.setRefreshing(false);
            errorView(U.INA);
        }
    }

    private void resetState() {
        currentPage = startingPageIndex;
        previousTotalItemCount = 0;
        loading = true;
        LastItem = true;
    }

    @SuppressLint("SetTextI18n")
    private void errorView(String msg1) {
        if (mTask.equals(U.GROUPJOBS)) {
            txtFound.setText("");
            if (getActivity() != null) {
                header.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            }
        } else {
            txtFound.setText("0 Jobs Found");
        }
        txtError1.setText(msg1);
        lProgress.setVisibility(View.GONE);
        lError.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        if (order != 3 && (mTask.equals(U.ALLJOBS) || mTask.equals(SU.FILTER)
                || mTask.equals(U.ADVSEARCH) || mTask.equals(SU.CATEGORY)
                || mTask.equals(SU.DISTRICTWISE) || mTask.equals(SU.GETRECENTJOBS))) {
            if (msg1.equals(U.INA)) {
                footer.setVisibility(View.GONE);
            } else {
                footer.setVisibility(View.VISIBLE);
            }
        } else {
            footer.setVisibility(View.GONE);
        }
        if ((mTask.equals(U.FJOBS) && msg1.equals(U.NO_FAV)) || mTask.equals(SU.APPLIEDJOBS)) {
            networkRetry.setVisibility(View.GONE);
        } else if (mTask.equals(U.ADVSEARCH) && msg1.equals(U.SEARCH_NO_JOB)) {
            networkRetry.setVisibility(View.GONE);
        } else {
            networkRetry.setVisibility(View.VISIBLE);
        }
    }

    private void preLoading() {
        txtFound.setText("");
        footer.setVisibility(View.GONE);
        lError.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void loadJSON(final String mTask) {
        swipeContainer.setRefreshing(false);
        final String url;
        if (mTask.equals(U.RJOB) || mTask.equals(SU.GETRJOBS)) {
            url = SU.RECOMMENDJOBS;
        } else {
            url = SU.SERVER;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        Log.e("showResponse", "" + response);
                        Handler mHandler = new Handler();
                        Runnable mRunnable = new Runnable() {
                            @Override
                            public void run() {
                                showJSON(response);
                                assignScrollListener();
                            }
                        };
                        mHandler.postDelayed(mRunnable, 3500);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorHandling(error);
                        Log.e("Error", "" + error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                System.out.println("Current task : " + mTask);
                Map<String, String> params = new HashMap<>();
                if (getActivity() != null) {
                    if (mTask.equals(U.RJOB)) {
                        params.put("action", SU.GETRJOBS);
                        params.put("employee_id", mEmployeeId);
                        params.put("skill", mSkill);
                        params.put("gender", mGender);
                        params.put("marital_status", mMaritalStatus);
                        params.put("job-preferred-location", mLocation);
                        params.put("qualification", mQualification);
                    } else if (mTask.equals(SU.GETRJOBS)) {
                        params.put("action", SU.GETRJOBS);
                        params.put("skill", mSkill);
                        params.put("location", mLocation);
                        params.put("qualification", mQualification);
                        params.put("experience", "");
                        params.put("salary", "");
                        params.put("mode", mMode);
                        params.put("job-cat", "");
                        params.put("workmode", "");
                        params.put("first_time", "1");
                    } else if (mTask.equals(U.ALLJOBS)) {
                        if (pref.getString(getActivity(), U.SH_FIRST_TIME_DATE).equals(U.currentDate())) {
                            firstTime = 1;
                        } else {
                            firstTime = 0;
                            pref.putString(getActivity(), U.SH_FIRST_TIME_DATE, U.currentDate());
                        }
                        params.put("first_time", "" + firstTime);
                        params.put("action", SU.GETJOBS);
                    } else if (mTask.equals(U.ADVSEARCH)) {
                        params.put("action", SU.GETJOBS);
                        params.put(mValue, mKey);
                    } else if (mTask.equals(SU.FILTER) || mTask.equals(SU.CATEGORY) || (mTask.equals(SU.DISTRICTWISE)) || (mTask.equals(SU.DISTRICTWISE_NOTIFICATION))) {
                        params.put("action", SU.SEARCH);
                        params.put("skill", mSkill);
                        if (mTask.equals(SU.DISTRICTWISE) || mTask.equals(SU.DISTRICTWISE_NOTIFICATION)) {
                            params.put("district_wise", mLocation);
                        } else {
                            params.put("district_wise", mLocation);//location
                        }
                        params.put("qualification", mQualification);
                        params.put("experience", mExperience);
                        params.put("salary", mSalary);
                        params.put("mode", mMode);
                        params.put("job-cat", mJobcat);
                        params.put("workmode", mWorkmode);
                        params.put("first_time", mfirstTime);
                    } else if (mTask.equals(U.FJOBS)) {
                        params.put("action", SU.FAVJOBS);
                        params.put("fav", fav);
                        params.put("isCompanyList", "no");
                    } else if (mTask.equals(U.NOTIJOBS)) {
                        params.put("action", SU.FAVJOBS);
                        params.put("fav", mKey);
                        params.put("isCompanyList", "no");
                    } else if (mTask.equals(U.GROUPJOBS)) {
                        params.put("action", SU.FAVJOBS);
                        params.put("fav", mKey);
                        params.put("isCompanyList", "yes");
                    } else if (mTask.equals(SU.POPULAR_EMP)) {
                        params.put("action", SU.POPULAR_EMP);
                    } else if (mTask.equals(SU.GETRECENTJOBS)) {
                        params.put("action", SU.FAVJOBS);
                        params.put("fav", mKey);
                    } else if (mTask.equals(SU.GETPERSONALISEDJOBS)) {
                        params.put("action", SU.GETPERSONALISEDJOBS);
                        params.put("job_title", "");
                        params.put("job-preferred-location", "" + pref.getString(getActivity(), U.SH_JOBLOCATION));
                        params.put("skill", "" + pref.getString(getActivity(), U.SH_SKILLS));
                        params.put("qualification", "" + pref.getString(getActivity(), U.SH_COURSE));
                        params.put("district_wise", "" + pref.getString(getActivity(), U.SH_USER_DISTRICT_NAME));
                        params.put("applied_jobs_job_title", "" + localDB.getAppliedTitleIds());
                        params.put("recently_viwed_jobs_job_title", "" + localDB.getReadTitleIds());
                        params.put("key", "" + pref.getString(getActivity(), U.SH_RECENT_SEARCH_KEYS).replace(",", " ").replace("+", " "));
                    } else if (mTask.equals(SU.APPLIEDJOBS)) {
                        params.put("action", SU.APPLIEDJOBS);
                    } else if (mTask.equals(SU.GETNLOCATIONJOBS)) {
                        params.put("action", SU.GETNLOCATIONJOBS);
                        params.put("native_location", pref.getString(getActivity(), U.SH_NATIVE_LOCATION));
                        params.put("native_district", pref.getString(getActivity(), U.SH_USER_NATIVE_DISTRICT));
                    } else if (mTask.equals(SU.APPLIEDRELATEDJOBS)) {
                        params.put("action", SU.APPLIEDRELATEDJOBS);
                    }
                    params.put("load", String.valueOf(0));
                    params.put("order", String.valueOf(order));
                    params.put("vcode", versionCode);
                    params.put("user_type", pref.getString(getActivity(), U.SH_USER_TYPE));
                    params.put("employee_id", pref.getString(getActivity(), U.SH_EMPLOYEE_ID));
                    params.put("android_id", U.getAndroidId(getActivity()));
                    params.put("ad_purchase", "" + pref.getInt(getActivity(), U.SH_AD_PURCHASED));

                    Log.e("paramsresponse", url + " -- " + params);
                }
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQue(stringRequest);
    }

    @SuppressLint("SetTextI18n")
    private void showJSON(String json) {
        list.clear();
        mAdapter.clear();
        mRecyclerView.getRecycledViewPool().clear();
        mAdapter.notifyDataSetChanged();
        System.out.println("Data string :" + json);
        JSONObject jsonObject;
        JSONArray jsonArray;
        int flag = 0;
        if (getActivity() != null) {
            try {
                jsonObject = new JSONObject(json);
                //check profile status to show RJOB
                if (mTask.equals(U.RJOB)) {
                    String status = jsonObject.getString("status");
                    switch (status) {
                        case "completed":
                            pref.putBoolean(getActivity(), U.SH_OTP_SUCCESS, true);
                            pref.putBoolean(getActivity(), U.SH_SIGN_UP_SUCCESS, true);
                            pref.putBoolean(getActivity(), U.SH_BLOCKED_USER, false);
                            flag = 0;
                            break;
                        case "user_blocked":
                            flag = 1;
                            pref.putBoolean(getActivity(), U.SH_OTP_SUCCESS, false);
                            pref.putBoolean(getActivity(), U.SH_SIGN_UP_SUCCESS, false);
                            pref.putBoolean(getActivity(), U.SH_BLOCKED_USER, true);
                            setIntent();
                            break;
                        case "New_user":
                            pref.putBoolean(getActivity(), U.SH_OTP_SUCCESS, false);
                            pref.putBoolean(getActivity(), U.SH_SIGN_UP_SUCCESS, false);
                            pref.putBoolean(getActivity(), U.SH_BLOCKED_USER, false);
                            flag = 1;
                            setIntent();
                            break;
                        case "In-complete":
                            pref.putBoolean(getActivity(), U.SH_BLOCKED_USER, false);
                            flag = 1;
                            setIntent();
                            break;
                    }
                } else {
                    flag = 0;
                }
                if (flag == 0) {
                    jsonArray = jsonObject.getJSONArray("list");
                    if (jsonArray.length() == 0) {
                        switch (mTask) {
                            case U.FJOBS:
                                errorView(U.NO_FAV);
                                break;
                            case U.RJOB:
                                errorView(U.NO_RJOBS);
                                break;
                            case U.ADVSEARCH:
                                errorView(U.SEARCH_NO_JOB);
                                loadJSONSearchWord();
                                SearchActivity.searchEmptyCount = 1 + SearchActivity.searchEmptyCount;
                                Log.e("searchEmptyCount", "" + SearchActivity.searchEmptyCount);
                                if (SearchActivity.searchEmptyCount > 3) {
                                    SearchActivity.searchEmptyCount = 0;
                                    searchEmptyDia();
                                }
                                break;
                            case U.NOTIJOBS:
                                errorView("Jobs may Expired");
                                break;
                            case U.GROUPJOBS:
                                errorView("Jobs may Expired");
                                break;
                            case SU.APPLIEDJOBS:
//                                errorView("No Applied Jobs Found");
                                loadAppliedRelevantJobs();
                                break;
                            default:
                                errorView(U.DNF);
                                break;
                        }
                        txtFound.setText("0 Jobs Found");
                    }

                    if (mTask.equals(U.ADVSEARCH)) {
                        if (mKey != null && !mKey.equals("")) {
                            if (pref.getString(getActivity(), U.SH_RECENT_SEARCH_KEYS).equals("")) {
                                pref.putString(getActivity(), U.SH_RECENT_SEARCH_KEYS, mKey.trim());
                            } else {
                                String recentSearch = pref.getString(getActivity(), U.SH_RECENT_SEARCH_KEYS);
                                String[] recentSearchArray = recentSearch.split(",");
                                ArrayList<String> recentSearchList = new ArrayList<>(Arrays.asList(recentSearchArray));
                                if (recentSearchList.size() == 3) {
                                    if (!recentSearchList.contains(mKey.trim())) {
                                        recentSearchList.remove(0);
                                        recentSearchList.add(mKey.trim());
                                    }
                                } else {
                                    if (!recentSearchList.contains(mKey.trim())) {
                                        recentSearchList.add(mKey.trim());
                                    }
                                }

                                String filter;
                                StringBuilder sb = new StringBuilder();
                                for (int i = 0; i < recentSearchList.size(); i++) {
                                    sb.append(recentSearchList.get(i)).append(",");
                                }
                                filter = sb.toString();
                                filter = filter.substring(0, filter.length() - 1);
                                String ss = filter;
                                if (!ss.isEmpty()) {
                                    pref.putString(getActivity(), U.SH_RECENT_SEARCH_KEYS, ss);
                                } else {
                                    pref.putString(getActivity(), U.SH_RECENT_SEARCH_KEYS, "");
                                }
                            }
                        }
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        Jobs jobs = new Jobs();
                        jobs.setId(jo.getInt("jobid"));
                        jobs.setImage(jo.getString("image"));
                        jobs.setJobtype(jo.getString("jobtype"));
                        jobs.setJobtitle(jo.getString("jobtitle"));
                        jobs.setJobtitleId(jo.getString("jobtitle_id"));
                        jobs.setEmployer(jo.getString("employer"));
                        jobs.setNoofvacancy(jo.getString("noofvancancy"));
                        jobs.setVerified(jo.getString("verified"));
                        jobs.setDate(jo.getString("ending"));
                        jobs.setDatediff(jo.getInt("datediff"));
                        jobs.setDescription(jo.getString("description"));
                        jobs.setDistrict(jo.getString("location"));
                        jobs.setViewType(jo.getString("view_type"));
                        jobs.setGrpJobId(jo.getString("group_job_id"));
                        jobs.setPopularEmpContent(jo.getString("popular_employer_string"));
                        jobs.setSerialNumber(jo.getString("serial_number"));
                        if (pref.getString(getActivity(), U.SH_USER_TYPE).equals(U.EMPLOYEE) && !mTask.equals(SU.APPLIEDJOBS)) {
                            if (jo.getString("view_type").equals("single_detail")) {
                                jobs.setTag(setValidTag(Integer.parseInt(jo.getString("jobtitle_id")),
                                        jo.getString("jobtitle"),
                                        jo.getString("location"),
                                        jo.getString("district_tamil"),
                                        jo.getString("description")));
                            } else {
                                Log.e("setTag", "else");
                                jobs.setTag("");
                            }
                        }
                        Cursor c1 = localDB.getQry("SELECT * FROM ReadUnreadTable where read_id='" + jo.getInt("jobid") + "'");
                        c1.moveToFirst();
                        if (c1.getCount() == 0) {
                            jobs.setRead(false);
                        } else {
                            jobs.setRead(true);
                        }
                        if (contains(list, jo.getString("serial_number"))) {
                            System.out.println("jobid-- already exists");
                        } else {
                            list.add(jobs);
                        }
                        c1.close();
                    }
                    /*if (getActivity() != null) {
                        MainActivity.add_ads(getActivity(), list);
                    }*/
                    mAdapter.addAll(list);
                    mRecyclerView.smoothScrollToPosition(0);
                    data = jsonObject.getString("data");
                    vcount = jsonObject.getInt("vcount");
                    if (!data.equals("false")) {
                        switch (mTask) {
                            case U.RJOB:
                                txtFound.setText(mRtitle);
                                break;
                            case U.GROUPJOBS:
                                if (mValue.equals(SU.POPULAR_EMP)) {
                                    header.setVisibility(View.GONE);
                                } else if (getActivity() != null) {
                                    header.setVisibility(View.VISIBLE);
                                    header.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                                    txtFound.setTextColor(getActivity().getResources().getColor(R.color.white));
                                    txtFound.setText(list.get(0).getEmployer());
                                    txtFound.setGravity(Gravity.CENTER);
                                }
                                break;
                            case SU.CATEGORY:
                                if (CategoryWiseFragment.categoryList != null && CategoryWiseFragment.categoryList.size() != 0) {
                                    for (int i = 0; i < CategoryWiseFragment.categoryList.size(); i++) {
                                        if (CategoryWiseFragment.categoryList.get(i).getId() == Integer.parseInt(mJobcat)) {
                                            txtFound.setText(CategoryWiseFragment.categoryList.get(i).getItem()
                                                    + "  " + "(" + CategoryWiseFragment.categoryList.get(i).getCount() + ")");
                                        }
                                    }
                                }
                                break;
                            case SU.DISTRICTWISE:
                                try {
                                    String afterDecode = URLDecoder.decode(mLocation, "UTF-8");
                                    if (DistrictWiseFragment.districtList != null && DistrictWiseFragment.districtList.size() != 0) {
                                        for (int i = 0; i < DistrictWiseFragment.districtList.size(); i++) {
                                            if (DistrictWiseFragment.districtList.get(i).getItem().equals(afterDecode)) {
                                                txtFound.setText(DistrictWiseFragment.districtList.get(i).getItem()
                                                        + "  " + "(" + DistrictWiseFragment.districtList.get(i).getCount() + ")");
                                            }
                                        }
                                    } else {
                                        txtFound.setText(afterDecode);
                                    }
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case SU.POPULAR_EMP:
                                header.setVisibility(View.GONE);
                                break;
                            case SU.DISTRICTWISE_NOTIFICATION:
                                header.setVisibility(View.GONE);
                                break;
                            default:
                                if (vcount == 1)
                                    txtFound.setText(vcount + " Vacancy from " + Arrays.asList(data.split(",")).size() + " Jobs");
                                else if (vcount == 0)
                                    txtFound.setText(Arrays.asList(data.split(",")).size() + " Jobs Found");
                                else
                                    txtFound.setText(vcount + " Vacancies from " + Arrays.asList(data.split(",")).size() + " Jobs");
                                break;
                        }
                        mRecyclerView.getRecycledViewPool().clear();
                        mAdapter.notifyDataSetChanged();
                        postLoading();
                    }
                }

            } catch (JSONException e) {
                Log.e("tag", "" + e);
            }
        }
    }

    private void assignScrollListener() {
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount, final RecyclerView view) {
                lProgress.setVisibility(View.VISIBLE);
                String url;
                if (mTask.equals(U.RJOB) || mTask.equals(SU.GETRJOBS)) {
                    url = SU.RECOMMENDJOBS;
                } else {
                    url = SU.SERVER;
                }
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (getActivity() != null) {
                                    Log.e("showResponse", "" + response);
                                    lProgress.setVisibility(View.GONE);
                                    JSONObject jsonObject;
                                    JSONArray jsonArray;
                                    try {
                                        jsonObject = new JSONObject(response);
                                        jsonArray = jsonObject.getJSONArray("list");
                                        moreJobs = new ArrayList<>();
                                        moreJobs.clear();
                                        if (jsonArray.length() == 0) {

                                            if (mTask.equals(SU.APPLIEDJOBS)) {
                                                loadAppliedRelevantJobs();
                                            } else {
                                                if (LastItem) {
                                                    LastItem = false;
                                                    Jobs jobs = new Jobs();
                                                    jobs.setId(0);
                                                    jobs.setImage("");
                                                    jobs.setJobtype("0");
                                                    jobs.setJobtitle("");
                                                    jobs.setJobtitleId("");
                                                    jobs.setEmployer("");
                                                    jobs.setNoofvacancy("");
                                                    jobs.setVerified("");
                                                    jobs.setDate("");
                                                    jobs.setDatediff(0);
                                                    jobs.setViewType("LastItem");
                                                    jobs.setDescription("");
                                                    jobs.setDistrict("");
                                                    jobs.setSerialNumber("");
                                                    jobs.setRead(false);
                                                    jobs.setAd(true);
                                                    moreJobs.add(jobs);
                                                }
                                            }
                                        }
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jo = jsonArray.getJSONObject(i);
                                            Jobs jobs = new Jobs();
                                            jobs.setId(jo.getInt("jobid"));
                                            jobs.setImage(jo.getString("image"));
                                            jobs.setJobtype(jo.getString("jobtype"));
                                            jobs.setJobtitle(jo.getString("jobtitle"));
                                            jobs.setJobtitleId(jo.getString("jobtitle_id"));
                                            jobs.setEmployer(jo.getString("employer"));
                                            jobs.setNoofvacancy(jo.getString("noofvancancy"));
                                            jobs.setVerified(jo.getString("verified"));
                                            jobs.setDate(jo.getString("ending"));
                                            jobs.setDatediff(jo.getInt("datediff"));
                                            jobs.setDescription(jo.getString("description"));
                                            jobs.setDistrict(jo.getString("location"));
                                            jobs.setViewType(jo.getString("view_type"));
                                            jobs.setGrpJobId(jo.getString("group_job_id"));
                                            jobs.setPopularEmpContent(jo.getString("popular_employer_string"));
                                            jobs.setSerialNumber(jo.getString("serial_number"));
                                            if (pref.getString(getActivity(), U.SH_USER_TYPE).equals(U.EMPLOYEE) && !mTask.equals(SU.APPLIEDJOBS)) {
                                                if (jo.getString("view_type").equals("single_detail")) {
                                                    jobs.setTag(setValidTag(Integer.parseInt(jo.getString("jobtitle_id")),
                                                            jo.getString("jobtitle"),
                                                            jo.getString("location"),
                                                            jo.getString("district_tamil"),
                                                            jo.getString("description")));
                                                } else {
                                                    jobs.setTag("");
                                                }
                                            }
                                            Cursor c1 = localDB.getQry("SELECT * FROM ReadUnreadTable where read_id='" + jo.getInt("jobid") + "'");
                                            c1.moveToFirst();
                                            if (c1.getCount() == 0) {
                                                jobs.setRead(false);
                                            } else {
                                                jobs.setRead(true);
                                            }
                                            if (contains(list, jo.getString("serial_number"))) {
                                                System.out.println("jobid-- already exists");
                                            } else {
                                                moreJobs.add(jobs);
                                            }
                                            c1.close();
                                        }
                                        list.addAll(moreJobs);
                                    /*if (getActivity() != null) {
                                        MainActivity.add_ads(getActivity(), list);
                                    }*/
                                        mAdapter.addAll(list);
                                        view.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    // mAdapter.notifyItemRangeInserted(curSize, list.size() - 1);
                                                    mRecyclerView.getRecycledViewPool().clear();
                                                    mAdapter.notifyDataSetChanged();
                                                } catch (Exception e) {
                                                    System.out.println("Error : " + e.getMessage());
                                                }
                                            }
                                        });
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                lProgress.setVisibility(View.GONE);
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        if (getActivity() != null) {
                            if (mTask.equals(U.RJOB)) {
                                params.put("action", SU.GETRJOBS);
                                params.put("employee_id", mEmployeeId);
                                params.put("skill", mSkill);
                                params.put("gender", mGender);
                                params.put("marital_status", mMaritalStatus);
                                params.put("job-preferred-location", mLocation);
                                params.put("qualification", mQualification);
                                params.put("first_time", mfirstTime);
                            } else if (mTask.equals(SU.GETRJOBS)) {
                                params.put("action", SU.GETRJOBS);
                                params.put("skill", mSkill);
                                params.put("location", mLocation);
                                params.put("qualification", mQualification);
                                params.put("experience", "");
                                params.put("salary", "");
                                params.put("mode", mMode);
                                params.put("job-cat", "");
                                params.put("workmode", "");
                                params.put("first_time", "1");
                            } else if (mTask.equals(U.ALLJOBS)) {
                                params.put("action", SU.GETJOBS);
                                params.put("first_time", "" + firstTime);
                            } else if (mTask.equals(U.ADVSEARCH)) {
                                System.out.println("scroll view listener : called : " + mKey);
                                params.put("action", SU.GETJOBS);
                                params.put(mValue, mKey);
                            } else if (mTask.equals(SU.FILTER) || mTask.equals(SU.CATEGORY) || mTask.equals(SU.DISTRICTWISE) || mTask.equals(SU.DISTRICTWISE_NOTIFICATION)) {
                                params.put("action", SU.SEARCH);
                                params.put("skill", mSkill);
                                if (mTask.equals(SU.DISTRICTWISE) || mTask.equals(SU.DISTRICTWISE_NOTIFICATION)) {
                                    params.put("district_wise", mLocation);
                                } else {
                                    params.put("district_wise", mLocation);
                                }
                                params.put("qualification", mQualification);
                                params.put("experience", mExperience);
                                params.put("salary", mSalary);
                                params.put("mode", mMode);
                                params.put("job-cat", mJobcat);//category
                                params.put("workmode", mWorkmode);
                                params.put("first_time", mfirstTime);
                            } else if (mTask.equals(U.FJOBS)) {
                                params.put("action", SU.FAVJOBS);
                                params.put("fav", fav);
                                params.put("isCompanyList", "no");
                            } else if (mTask.equals(U.NOTIJOBS)) {
                                params.put("action", SU.FAVJOBS);
                                params.put("fav", mKey);
                                params.put("isCompanyList", "no");
                            } else if (mTask.equals(U.GROUPJOBS)) {
                                params.put("action", SU.FAVJOBS);
                                params.put("fav", mKey);
                                params.put("isCompanyList", "yes");
                            } else if (mTask.equals(SU.POPULAR_EMP)) {
                                params.put("action", SU.POPULAR_EMP);
                            } else if (mTask.equals(SU.GETRECENTJOBS)) {
                                params.put("action", SU.FAVJOBS);
                                params.put("fav", mKey);
                            } else if (mTask.equals(SU.GETPERSONALISEDJOBS)) {
                                params.put("action", SU.GETPERSONALISEDJOBS);
                                params.put("job_title", "");
                                params.put("job-preferred-location", "" + pref.getString(getActivity(), U.SH_JOBLOCATION));
                                params.put("skill", "" + pref.getString(getActivity(), U.SH_SKILLS));
                                params.put("qualification", "" + pref.getString(getActivity(), U.SH_COURSE));
                                params.put("district_wise", "" + pref.getString(getActivity(), U.SH_USER_DISTRICT_NAME));
                                params.put("applied_jobs_job_title", "" + localDB.getAppliedTitleIds());
                                params.put("recently_viwed_jobs_job_title", "" + localDB.getReadTitleIds());
                                params.put("key", "" + pref.getString(getActivity(), U.SH_RECENT_SEARCH_KEYS).replace(",", " ").replace("+", " "));
                            } else if (mTask.equals(SU.GETNLOCATIONJOBS)) {
                                params.put("action", SU.GETNLOCATIONJOBS);
                                params.put("native_location", pref.getString(getActivity(), U.SH_NATIVE_LOCATION));
                                params.put("native_district", pref.getString(getActivity(), U.SH_USER_NATIVE_DISTRICT));
                            } else if (mTask.equals(SU.APPLIEDJOBS)) {
                                params.put("action", SU.APPLIEDJOBS);
                            } else if (mTask.equals(SU.APPLIEDRELATEDJOBS)) {
                                params.put("action", SU.APPLIEDRELATEDJOBS);
                            }

                            params.put("load", String.valueOf(page));
                            params.put("order", String.valueOf(order));
                            params.put("vcode", versionCode);
                            params.put("user_type", pref.getString(getActivity(), U.SH_USER_TYPE));
                            params.put("employee_id", pref.getString(getActivity(), U.SH_EMPLOYEE_ID));
                            params.put("android_id", U.getAndroidId(getActivity()));
                            params.put("ad_purchase", "" + pref.getInt(getActivity(), U.SH_AD_PURCHASED));

                            Log.e("paramsresponse", "" + params);
                        }
                        return params;
                    }
                };
                MySingleton.getInstance(getContext()).addToRequestQue(stringRequest);
            }
        };
        mRecyclerView.addOnScrollListener(scrollListener);
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

    private void loadAppliedRelevantJobs() {
        lProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.SERVER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("showResponse", "" + response);
                        JSONObject jsonObject;
                        JSONArray jsonArray;
                        try {
                            jsonObject = new JSONObject(response);
                            jsonArray = jsonObject.getJSONArray("list");
                            moreJobs = new ArrayList<>();
                            moreJobs.clear();
                            if (jsonArray.length() == 0) {

                                if (list.size() == 0) {
                                    errorView("No Applied Jobs Found");
                                } else {
                                    if (LastItem) {
                                        LastItem = false;
                                        Jobs jobs = new Jobs();
                                        jobs.setId(0);
                                        jobs.setImage("");
                                        jobs.setJobtype("0");
                                        jobs.setJobtitle("");
                                        jobs.setJobtitleId("");
                                        jobs.setEmployer("");
                                        jobs.setNoofvacancy("");
                                        jobs.setVerified("");
                                        jobs.setDate("");
                                        jobs.setDatediff(0);
                                        jobs.setViewType("LastItem");
                                        jobs.setDescription("");
                                        jobs.setDistrict("");
                                        jobs.setSerialNumber("");
                                        jobs.setRead(false);
                                        jobs.setAd(true);
                                        moreJobs.add(jobs);
                                    }
                                }

                                lProgress.setVisibility(View.GONE);

                            } else {

                                if (aRelatedJobsPage == 0) {
                                    Jobs jobs = new Jobs();
                                    jobs.setId(0);
                                    jobs.setImage("");
                                    jobs.setJobtype("0");
                                    jobs.setJobtitle("விண்ணப்பித்தவைக்கு தொடர்புடைய வேலைவாய்ப்புகள் பின்வருமாறு :");
                                    jobs.setJobtitleId("");
                                    jobs.setEmployer("");
                                    jobs.setNoofvacancy("");
                                    jobs.setVerified("");
                                    jobs.setDate("");
                                    jobs.setDatediff(0);
                                    jobs.setViewType("LastItem");
                                    jobs.setDescription("");
                                    jobs.setDistrict("");
                                    jobs.setSerialNumber("");
                                    jobs.setRead(false);
                                    jobs.setAd(true);
                                    moreJobs.add(jobs);
                                }

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jo = jsonArray.getJSONObject(i);
                                    Jobs jobs = new Jobs();
                                    jobs.setId(jo.getInt("jobid"));
                                    jobs.setImage(jo.getString("image"));
                                    jobs.setJobtype(jo.getString("jobtype"));
                                    jobs.setJobtitle(jo.getString("jobtitle"));
                                    jobs.setJobtitleId(jo.getString("jobtitle_id"));
                                    jobs.setEmployer(jo.getString("employer"));
                                    jobs.setNoofvacancy(jo.getString("noofvancancy"));
                                    jobs.setVerified(jo.getString("verified"));
                                    jobs.setDate(jo.getString("ending"));
                                    jobs.setDatediff(jo.getInt("datediff"));
                                    jobs.setDescription(jo.getString("description"));
                                    jobs.setDistrict(jo.getString("location"));
                                    jobs.setViewType(jo.getString("view_type"));
                                    jobs.setGrpJobId(jo.getString("group_job_id"));
                                    jobs.setPopularEmpContent(jo.getString("popular_employer_string"));
                                    jobs.setSerialNumber(jo.getString("serial_number"));
                                    if (jo.getString("view_type").equals("single_detail")) {
                                        jobs.setTag(setValidTag(Integer.parseInt(jo.getString("jobtitle_id")),
                                                jo.getString("jobtitle"),
                                                jo.getString("location"),
                                                jo.getString("district_tamil"),
                                                jo.getString("description")));
                                    } else {
                                        jobs.setTag("");
                                    }
                                    Cursor c1 = localDB.getQry("SELECT * FROM ReadUnreadTable where read_id='" + jo.getInt("jobid") + "'");
                                    c1.moveToFirst();
                                    if (c1.getCount() == 0) {
                                        jobs.setRead(false);
                                    } else {
                                        jobs.setRead(true);
                                    }
                                    if (contains(list, jo.getString("serial_number"))) {
                                        System.out.println("jobid-- already exists");
                                    } else {
                                        moreJobs.add(jobs);
                                    }
                                    c1.close();
                                }

                                aRelatedJobsPage++;

                                if (list.size() == 0) {
                                    postLoading();
                                }

                            }

                            list.addAll(moreJobs);
                            mAdapter.addAll(list);
                            mRecyclerView.getRecycledViewPool().clear();
                            mAdapter.notifyDataSetChanged();
                            lProgress.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        lProgress.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", SU.APPLIEDRELATEDJOBS);
                params.put("load", String.valueOf(aRelatedJobsPage));
                params.put("order", String.valueOf(order));
                params.put("vcode", versionCode);
                params.put("user_type", pref.getString(getActivity(), U.SH_USER_TYPE));
                params.put("employee_id", pref.getString(getActivity(), U.SH_EMPLOYEE_ID));
                params.put("android_id", U.getAndroidId(getActivity()));
                params.put("ad_purchase", "" + pref.getInt(getActivity(), U.SH_AD_PURCHASED));
                Log.e("paramsresponse", "" + params);
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQue(stringRequest);
    }

    private void postLoading() {
        hiddenSearchFileter();
        lError.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void setOnJobItemClick(String id, int position, String task) {
        if (U.isNetworkAvailable(getActivity())) {
            if (task.equals(SU.POPULAR_EMP)) {
                Intent intent = new Intent(getActivity(), PopularEmployersDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("key", data);
                bundle.putString("task", SU.POPULAR_EMP);
                bundle.putString("value", SU.POPULAR_EMP);
                bundle.putString("point", id);
                bundle.putInt("position", position);
                intent.putExtra("detailValue", list.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            } else if (task.equals("ViewAllJobs")) {
                Intent homeIntent = new Intent(getActivity(), MainActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                homeIntent.putExtra("type", "cj");
                homeIntent.putExtra("message", "home");
                homeIntent.putExtra("mode", "gcm");
                homeIntent.putExtra("title", "");
                homeIntent.putExtra("idd", "");
                homeIntent.putExtra("Noti_add", 1);
                startActivity(homeIntent);
                if (getActivity() != null) {
                    getActivity().finish();
                }
            } else {
                Intent intent = new Intent(getActivity(), JobDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("key", data);
                bundle.putString("task", mTask);
                bundle.putString("value", mValue);
                bundle.putString("point", id);
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        } else L.t(getActivity(), U.INA);
    }

    @Override
    public void setOnJobReminderClick(int position) {
        AddDialogFragment addDialogFragment;
        int id = list.get(position).getId();
        String title = list.get(position).getJobtitle();
        String titleId = list.get(position).getJobtitleId();
        String emp = list.get(position).getEmployer();
        String date = list.get(position).getDate();
        boolean b = localDB.isReminderExists(list.get(position).getId());
        int action = (b) ? 1 : 0;
        String image = "";
        try {
            image = java.net.URLDecoder.decode(list.get(position).getImage(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        addDialogFragment = new AddDialogFragment(this, action, position, id, image, title, titleId, emp, date);
        if (getActivity() != null) {
            addDialogFragment.show(getActivity().getSupportFragmentManager(), "");
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setOnJobDeleteItemClick(int id, int position) {
        if (mTask.equals(U.FJOBS)) {
            list.remove(position);
            /*if (getActivity() != null) {
                MainActivity.add_ads(getActivity(), list);
            }*/
            mRecyclerView.getRecycledViewPool().clear();
            mAdapter.notifyDataSetChanged();
            if (list.size() != 0) {
                fav = localDB.getBookMarks().toString().replace("[", "").replace("]", "")
                        .replace(", ", ",");
                data = fav;
                List<String> item = Arrays.asList(data.split(","));
                txtFound.setText(item.size() + " Jobs Found");
            } else errorView(U.NO_FAV);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == txtFilter) {
            startActivity(new Intent(getActivity(), FilterActivity.class));
        } else if (view == txtSort) {
            new SortBottomSheet(this, order).show(getChildFragmentManager(), "");
        } else if (view == moveUpBtn) {
            mRecyclerView.setAdapter(mAdapter);
        } else if (view == networkRetry) {
            load();
        }
    }

    @Override
    public void onSortItemClick(int item, int message) {
        if (item == 1) {
            list.clear();
            order = 2;
            load();
        } else if (item == 2) {
            list.clear();
            order = 1;
            load();
        } else if (item == 4) {
            list.clear();
            order = 4;
            load();
        } else if (item == 5) {
            list.clear();
            order = 5;
            load();
        } else if (item == 6) {
            list.clear();
            order = 6;
            load();
        } else if (item == 7) {
            list.clear();
            order = 7;
            load();
        } else if (item == 8) {
            list.clear();
            order = 8;
            load();
        }
        mRecyclerView.getRecycledViewPool().clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        swipeContainer.setRefreshing(true);
        aRelatedJobsPage = 0;
        list.clear();
        resetState();
        order = 3;
        load();
    }

    @Override
    public void onTaskAdded(int position) {
        // L.t(getActivity(), position +"");
        if (getActivity() != null) {
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
            notificationIntent.addCategory("android.intent.category.DEFAULT");
            PendingIntent broadcast = PendingIntent.getBroadcast(getActivity(), 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.SECOND, 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (alarmManager != null) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
                }
            }
            mAdapter.refreshBlockOverlay(position);
        }
    }

    @Override
    public void onReminderRemoved(int position) {
        load();
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (getActivity() != null) {
            if (pref.getInt(getActivity(), U.SH_AD_PURCHASED) == 0) {
                if (mTask.equals(U.GROUPJOBS) && mValue.equals(SU.POPULAR_EMP)) {
                    //jobs from same company so Ad need not to be show
                } else {
//                MainActivity.refreshAd(getActivity(), installAd, contentAd);
                    MainActivity.loadNativeAd(getActivity());
                }
            }
        }*/
        mRecyclerView.getRecycledViewPool().clear();
        mAdapter.notifyDataSetChanged();
        if (pref.getInt(getActivity(), U.SH_RJOBS_FLAG) == 1) {
            setIntent();
        }
    }

    public boolean contains(List<Jobs> list, String id) {

        for (Jobs item : list) {
            if (item.getSerialNumber().equals(id)) {
                return true;
            }
        }
        return false;
    }

    private void searchEmptyDia() {
        if (getActivity() != null) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.search_empty_feed_lay);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            TextView callTxt = dialog.findViewById(R.id.call_txt);
            TextView feedTxt = dialog.findViewById(R.id.feed_txt);
            emailIdspinner = dialog.findViewById(R.id.emailids);
            final LinearLayout calLay = dialog.findViewById(R.id.call_lay);
            final LinearLayout feedLay = dialog.findViewById(R.id.feed_lay);
            ImageView closeBtn = dialog.findViewById(R.id.close_btn);
            TextView txtSend = dialog.findViewById(R.id.txtSend);
            final EditText edFeed = dialog.findViewById(R.id.edFeed);
            final EditText edphone = dialog.findViewById(R.id.edphone);
            Button btnOk = dialog.findViewById(R.id.btnOk);
            btnOk.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    dialog.dismiss();
                }
            });
            callTxt.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(edFeed.getWindowToken(), 0);
                    }
                    calLay.setVisibility(View.VISIBLE);
                    feedLay.setVisibility(View.GONE);
                }
            });
            feedTxt.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    calLay.setVisibility(View.GONE);
                    feedLay.setVisibility(View.VISIBLE);
                    emailPermissionFun();
                }
            });
            closeBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    dialog.dismiss();
                }
            });
            txtSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (emailIdspinner.getAdapter() == null) {
                        selectedEmailId = "";
                    } else {
                        selectedEmailId = emailIdspinner.getSelectedItem().toString();
                    }
                    String str2 = edFeed.getText().toString();
                    String str3 = edphone.getText().toString();
                    try {
                        str2 = URLEncoder.encode(edFeed.getText().toString(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        L.t(getActivity(), U.ERROR);
                    }
                    if (str2.length() != 0) {
                        if (!str3.isEmpty()) {
                            str2 = str2 + "   PhnNO: " + str3;
                        }
                        if (U.isNetworkAvailable(getActivity())) {
                            sendFeed(selectedEmailId, str2);
                            dialog.dismiss();
                        }
                    } else {
                        L.t(getActivity(), getResources().getString(R.string.type));
                    }
                }
            });
            this.phnNo = this.pref.getInt(getContext(), U.SH_REMOTE_PHONE_NUMBER);
            Log.e("phnNo", "" + this.phnNo);
            if (this.phnNo == 0) {
                calLay.setVisibility(View.GONE);
                feedLay.setVisibility(View.VISIBLE);
                callTxt.setVisibility(View.GONE);
            } else if (this.phnNo == 1) {
                callTxt.performClick();
            }
            dialog.show();
        }
    }

    private void emailPermissionFun() {
        if (getActivity() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getActivity().checkSelfPermission(android.Manifest.permission.GET_ACCOUNTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
                    dialog.setContentView(R.layout.permission_dialog_layout);
                    dialog.setCancelable(false);
                    TextView text = dialog.findViewById(R.id.text);
                    text.setText(getResources().getString(R.string.email_permission_text));
                    Button ok = dialog.findViewById(R.id.permission_ok);
                    Button cancel = dialog.findViewById(R.id.permission_cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (pref.getInt(getActivity(), U.SH_EMAIL_PERMISSION_DIA_SHOW) == 2) {
                                Intent intent = new Intent();
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                                dialog.dismiss();
                            } else {
                                requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS}, 153);
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.show();
                } else {
//                        getmaildia();
                    chooseAccount();
                }
            } else {
                InputMethodManager imm1 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm1 != null) {
                    imm1.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
                getmaildia();
            }
        }
    }

    private List<String> getaccount() {
        List<String> mailid = new ArrayList<>();
        mailid.clear();
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(getActivity()).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                mailid.add(possibleEmail);
            }
        }
        for (int i = 1; i < mailid.size(); i++) {
            String a1 = mailid.get(i);
            String a2 = mailid.get(i - 1);
            if (a1.equals(a2)) {
                mailid.remove(a1);
            }
        }
        return mailid;
    }

    private void getmaildia() {
        if (getActivity() != null) {
            final List<String> mailid = getaccount();
            if (!mailid.isEmpty()) {
                ArrayAdapter<String> itemsAdapter =
                        new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mailid);
                emailIdspinner.setAdapter(itemsAdapter);
            }
        }
    }

    private void chooseAccount() {
        Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
                false, null, null, null, null);
        startActivityForResult(intent, 190);
    }

    private void setIntent() {
        pref.putInt(getActivity(), U.SH_RJOBS_FLAG, 0);
        Intent homeIntent = new Intent(getActivity(), MainActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("type", "cj");
        homeIntent.putExtra("message", "homeRjobs");
        homeIntent.putExtra("mode", "gcm");
        homeIntent.putExtra("title", "");
        homeIntent.putExtra("idd", "");
        homeIntent.putExtra("Noti_add", 1);
        startActivity(homeIntent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getActivity() != null) {
            if (requestCode == 190 && resultCode == Activity.RESULT_OK) {
                String selectedEmailId = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                final List<String> mailid = new ArrayList<>();
                mailid.add(selectedEmailId);
                if (!mailid.isEmpty()) {
                    ArrayAdapter<String> itemsAdapter =
                            new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mailid);
                    emailIdspinner.setAdapter(itemsAdapter);
                }
            }
        }
    }

    private void sendFeed(final String email, final String feed) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.FEEDBACKURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        U.toast_center(getActivity(), "" + getResources().getString(R.string.content_saved));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        L.t(getActivity(), error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                String type = "";
                try {
                    type = URLEncoder.encode(SU.APP, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Map<String, String> params = new HashMap<>();
                if (getActivity() != null) {
                    String feedback = "Search Feedback : " + feed;
                    params.put("feedback", feedback);
                    params.put("email", email);
                    params.put("vcode", U.getVersion(getActivity()).versionCode + "");
                    params.put("model", U.getDeviceName());
                    params.put("type", type);
                }
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQue(stringRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 153: {
                if (getActivity() != null) {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        pref.putInt(getActivity(), U.SH_EMAIL_PERMISSION_DIA_SHOW, 1);
                        InputMethodManager imm1 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm1 != null) {
                            imm1.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        }
//                    getmaildia();
                        chooseAccount();
                    } else {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            boolean showRationale = shouldShowRequestPermissionRationale(permissions[0]);
                            if (!showRationale) {
                                pref.putInt(getActivity(), U.SH_EMAIL_PERMISSION_DIA_SHOW, 2);
                            } else if (Manifest.permission.GET_ACCOUNTS.equals(permissions[0])) {
                                pref.putInt(getActivity(), U.SH_EMAIL_PERMISSION_DIA_SHOW, 0);
                            }
                        }
                    }
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void loadJSONSearchWord() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.GETDATAURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSONSearchWord(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (getActivity() != null) {
                    params.put("action", "searchword");
                    params.put("word", "" + mKey);
                    params.put("inby", "" + U.getAndroidId(getActivity()));
                    params.put("email", pref.getString(getActivity(), U.SH_EMAIL));
                    params.put("user_type", pref.getString(getActivity(), U.SH_USER_TYPE));
                    params.put("mobile1", pref.getString(getActivity(), U.SH_MOBILE));
                    params.put("employee_id", pref.getString(getActivity(), U.SH_EMPLOYEE_ID));
                    params.put("android_id", U.getAndroidId(getActivity()));
                    params.put("vcode", versionCode);
                    Log.e("paramsresponse", "" + params);
                }
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQue(stringRequest);
    }

    //------------------------- search word to server --------------------

    private void showJSONSearchWord(String json) {
        JSONObject jsonObject;
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                String status = jsonObject.getString("status");
                Log.e("tag", status);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onShareItemClick(int item, String message) {
        if (item == 1) whatsAppShare(message);
        else if (item == 2) gmailShare(message);
        if (item == 3) if (getActivity() != null) U.shareText(getActivity(), message);
    }

    //-------------------------- share ------------------------------------

    private void gmailShare(String message) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_SUBJECT, SU.APP);
        i.putExtra(Intent.EXTRA_TEXT,
                "நித்ரா வேலைவாய்ப்பு செயலி வழியாக பகிரப்பட்டது. செயலியை தரவிறக்கம் செய்ய: "
                        + U.UTM_SOURCE + "\n\n" + message);
        try {
            startActivity(Intent.createChooser(i, "Send Mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There are No Email Clients Installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void whatsAppShare(String message) {
        message = message.replace("&", ",");
        if (getActivity() != null) {
            if (U.isAppInstalled(getActivity(), "com.whatsapp")) {
                startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("whatsapp://send?text=" + "நித்ரா வேலைவாய்ப்பு செயலி வழியாக பகிரப்பட்டது. செயலியை தரவிறக்கம் செய்ய:" + U.UTM_SOURCE + "\n\n" +
                                message)));
            } else L.t(getActivity(), U.ANI);
        }
    }

    private String setValidTag(int titleId, String titleName, String location, String district, String description) {
        String tagName = "";
        int weight = 0;
        if (getActivity() != null) {

            final String[] districtArray = district.split(",");
            final String[] locationArray = location.split(",");

            if (localDB.isAppliedTitleExists(titleId)) {
                weight++;
            }
            if (localDB.isReadTitleExists(titleId)) {
                weight++;
            }
            if (localDB.isBookMarkTitleExists(titleId)) {
                weight++;
            }

            String loc = pref.getString(getActivity(), U.SH_USER_DISTRICT_NAME);
            final String[] locArray = loc.split(",");

            for (String s2 : locArray) {
                for (String s : districtArray) {
                    if (s2.equals(s)) {
                        weight++;
                    }
                }
            }

            if (!pref.getString(getActivity(), U.SH_RECENT_SEARCH_KEYS).equals("")) {
                String searchKeys = pref.getString(getActivity(), U.SH_RECENT_SEARCH_KEYS);
                final String[] desArray = searchKeys.split(",");
                for (String s : desArray) {
                    if (description.contains(s)) {
                        weight++;
                    }
                    for (String s1 : districtArray) {
                        if (s.equals(s1)) {
                            weight++;
                        }
                    }
                    if (titleName.equals(s)) {
                        weight++;
                    }
                }
            }

            for (String s : locationArray) {
                if (pref.getString(getActivity(), U.SH_NATIVE_LOCATION_NAME).equals(s)) {
                    weight++;
                }
            }

            if (weight >= 5) {
                tagName = "Suggested";
            } else if (weight >= 1) {
                tagName = "More Relevent";
            } else {
                tagName = "None";
            }
        }
        return tagName;
    }

    public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

        RecyclerView.LayoutManager mLayoutManager;

        EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
            this.mLayoutManager = layoutManager;
        }

        public EndlessRecyclerViewScrollListener(GridLayoutManager layoutManager) {
            this.mLayoutManager = layoutManager;
            visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        }

        public EndlessRecyclerViewScrollListener(StaggeredGridLayoutManager layoutManager) {
            this.mLayoutManager = layoutManager;
            visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        }

        public int getLastVisibleItem(int[] lastVisibleItemPositions) {
            int maxSize = 0;
            for (int i = 0; i < lastVisibleItemPositions.length; i++) {
                if (i == 0) {
                    maxSize = lastVisibleItemPositions[i];
                } else if (lastVisibleItemPositions[i] > maxSize) {
                    maxSize = lastVisibleItemPositions[i];
                }
            }
            return maxSize;
        }

        // This happens many times a second during a scroll, so be wary of the code you place here.
        // We are given a few useful parameters to help us work out if we need to load some more data,
        // but first we check if we are waiting for the previous load to finish.
        @Override
        public void onScrolled(RecyclerView view, int dx, int dy) {
            int lastVisibleItemPosition = 0;
            int totalItemCount = mLayoutManager.getItemCount();

            if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
                // get maximum element within the list
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
            } else if (mLayoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            } else if (mLayoutManager instanceof LinearLayoutManager) {
                lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            }

            // If it’s still loading, we check to see if the dataset count has
            // changed, if so we conclude it has finished loading and update the current page
            // number and total item count.
            if (loading && (totalItemCount > previousTotalItemCount)) {
                loading = false;
                previousTotalItemCount = totalItemCount;
            }

            // If it isn’t currently loading, we check to see if we have breached
            // the visibleThreshold and need to reload more data.
            // If we do need to reload some more data, we execute onLoadMore to fetch the data.
            // threshold should reflect how many total columns there are too
            if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
                currentPage++;
                onLoadMore(currentPage, totalItemCount, view);
                loading = true;
            }
        }

        // Call whenever performing new searches
        public void resetState() {
            currentPage = startingPageIndex;
            previousTotalItemCount = 0;
            loading = true;
        }

        // Defines the process for actually loading more data based on page
        public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);
    }

}
