package nithra.jobs.career.placement.fragments;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
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
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.activity.JobDetailActivity;
import nithra.jobs.career.placement.bottomsheets.ShareBottomSheet;
import nithra.jobs.career.placement.engine.LocalDB;
import nithra.jobs.career.placement.networking.MySingleton;
import nithra.jobs.career.placement.utills.L;
import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

import static nithra.jobs.career.placement.activity.JobDetailActivity.interstitialAd;
import static nithra.jobs.career.placement.activity.JobDetailActivity.mSectionsPagerAdapter;
import static nithra.jobs.career.placement.activity.JobDetailActivity.position;
import static nithra.jobs.career.placement.activity.JobDetailActivity.rowList;

/**
 * Created by nithra-apps on 14/10/17.
 */
public class PlaceholderFragment extends Fragment implements View.OnClickListener,
        ShareBottomSheet.OnShareClick, SwipeRefreshLayout.OnRefreshListener, AddDialogFragment.DialogListener {
    //private static final String CONTACT_ASK_MESSAGE = "உங்களுக்கு Call சேவையை கொடுக்க பின்வரும் permission-களை allow செய்யவேண்டும்.";
    public InterstitialAd indAd;
    /*
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    TextView txtJobID, txtPostedBy, txtJobType, txtDate, txtTitle, txtCompanyName, tdetail, txtDetail, txtSkills, tskills, txtDist, tdist, txtTaluk,
            ttaluk, txtAddress, txtEmail, temail, txtQualification, tqualification, txtExp, texp, txtStarting, tstarting, txtEnding, tending,
            txtWebsite, twebsite, txtWebsite2, twebsite2, txtExamDate, texamdate, txtAgeLimit, tagelimit, txtHowToApply, thowtoapply, txtPostalAddress, tpostaladdress, txtSalary,
            tsalary, txtSelection, tselection, txtFees, tfees, txtExamCentre, texamcentre, txtNoOfVacancy, tnoofvancancy,
            txtPhone, tphone, taddress, tcities, txtCities;
    ImageButton btnLike, btnReminder, btnReport;
    TextView btnApply, errorText;
    View view, divider1, divider2;
    int employerid, employeeid = 40, isapplied, jobid = 0;
    LocalDB localDB;
    String jobtype, jobtitle, description, skills, employer, experience, posteddate, apply,
            phone, address, email, qualification, noofvancancy, starting, ending, website, website2, examdate, agelimit, howtoapply, postaladdress, salary,
            selectionprocess, feesdetails, examcentre, postedby, location;
    String name, phoneno, comment, userEmail, task;
    LinearLayout lDate, lError, postedByLay;
    SpinKitView progressBar;
    NestedScrollView mNestedScrollView;
    CustomTabsClient mCustomTabsClient;
    CustomTabsSession mCustomTabsSession;
    CustomTabsServiceConnection mCustomTabsServiceConnection;
    CustomTabsIntent mCustomTabsIntent;
    final String CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";
    ImageView imgShare;
    SharedPreference pref;
    private SwipeRefreshLayout swipeContainer;
    private int ROWID = 0;
    public static int adCount = 0;
    int postedbyValue = 0;
    ImageView back, next;
    Button networkRetry;
    FrameLayout adLay;

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber, String task) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString("Task", task);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_job_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lDate = view.findViewById(R.id.lDate);

        errorText = view.findViewById(R.id.txtError1);
        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);
        lError = view.findViewById(R.id.lError);
        mNestedScrollView = view.findViewById(R.id.mNestedScrollView);
        progressBar = view.findViewById(R.id.progressBar);
        postedByLay = view.findViewById(R.id.postedbylay);
        adLay = view.findViewById(R.id.frame);
        indAd = interstitialAd;

        back = view.findViewById(R.id.back);
        next = view.findViewById(R.id.next);
        if (rowList.size() > 1) {
            back.setVisibility(View.VISIBLE);
            next.setVisibility(View.VISIBLE);
        } else {
            back.setVisibility(View.GONE);
            next.setVisibility(View.GONE);
        }
        pref = new SharedPreference();

        networkRetry = view.findViewById(R.id.network_retry);
        networkRetry.setOnClickListener(this);

        btnApply = view.findViewById(R.id.btnApply);
        btnApply.setOnClickListener(this);

        imgShare = view.findViewById(R.id.imgShare);
        imgShare.setOnClickListener(this);

        btnLike = view.findViewById(R.id.btnLike);
        btnLike.setOnClickListener(this);

        btnReport = view.findViewById(R.id.btnReport);
        btnReport.setOnClickListener(this);

        btnReminder = view.findViewById(R.id.btnReminder);
        btnReminder.setOnClickListener(this);
    }

    public void showIndus() {
        indAd.show();
        indAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                indAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        localDB = new LocalDB(getActivity());

        if (getArguments() != null) {
            ROWID = getArguments().getInt(ARG_SECTION_NUMBER);
            task = getArguments().getString("Task");
        }

        postedbyValue = pref.getInt(getContext(), U.SH_REMOTE_POSTEDBY);
        Log.e("vacancypostedbyValue", "" + postedbyValue);
        if (postedbyValue == 0) {
            postedByLay.setVisibility(View.GONE);
        } else if (postedbyValue == 1) {
            postedByLay.setVisibility(View.VISIBLE);
        }

        if (U.isNetworkAvailable(getActivity())) {
            refreshAd(getActivity(), true, false, adLay);
        }

        final int jobid = Integer.parseInt(rowList.get(getArguments().getInt(ARG_SECTION_NUMBER)).trim());
        btnLike.setImageResource(localDB.isBookMarkExists(jobid) ? R.drawable.star_on : R.drawable.star_off);
        load(ROWID);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JobDetailActivity.mViewPager.setCurrentItem(JobDetailActivity.mViewPager.getCurrentItem() - 1);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JobDetailActivity.mViewPager.setCurrentItem(JobDetailActivity.mViewPager.getCurrentItem() + 1);
            }
        });

        JobDetailActivity.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    if (rowList.size() == 1) {
                        back.setVisibility(View.GONE);
                        next.setVisibility(View.GONE);
                    } else {
                        back.setVisibility(View.GONE);
                        next.setVisibility(View.VISIBLE);
                    }
                } else if (position == rowList.size() - 1) {
                    next.setVisibility(View.GONE);
                    back.setVisibility(View.VISIBLE);
                } else {
                    next.setVisibility(View.VISIBLE);
                    back.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {
                loadJSON(jobid, U.SH_VIEWCOUNT, "");
                Cursor c1 = localDB.getQry("SELECT * FROM ReadUnreadTable where read_id='" + jobid + "'");
                c1.moveToFirst();
                if (c1.getCount() == 0) {
                    localDB.insertReadId(String.valueOf(jobid));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void load(int id) {
        if (U.isNetworkAvailable(getActivity())) {
            preLoad();
            loadData(id);
        } else {
            swipeContainer.setRefreshing(false);
            errorView(U.INA);
        }
    }

    private void preLoad() {
        progressBar.setVisibility(View.VISIBLE);
        lError.setVisibility(View.GONE);
        mNestedScrollView.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {

        if (v == btnLike) {
            final int jobid;
            if (getArguments() != null) {
                jobid = Integer.parseInt(rowList.get(getArguments().getInt(ARG_SECTION_NUMBER)).trim());
                if (localDB.isBookMarkExists(jobid)) {
                    if (localDB.deleteJobBookMark(jobid)) {
                        L.t(getActivity(), U.FAV_REMOVED);
                        btnLike.setImageResource(R.drawable.star_off);
                    }
                } else {
                    if (localDB.addJobBookMark(jobid)) {
                        L.t(getActivity(), U.FAV_SUCCESS);
                        btnLike.setImageResource(R.drawable.star_on);
                    }
                }
            }

        } else if (v == imgShare) {
            String share;
            try {
                share = "Job Title : " + jobtitle + "\n" + "Description : " + description + "\n\n" + "மேலும் வேலைவாய்ப்பை பற்றி அறிந்துகொள்ள கீழே உள்ள லிங்கை கிளிக் செய்யவும்" +
                        " " + SU.BASE_URL + "deeplinking.php?id=" + jobid + "\n\n";
            } catch (Exception e) {
                share = "";
            }
            contentShare(share);

        } else if (v == btnReminder) {
            AddDialogFragment addDialogFragment;
            int id = jobid;
            String title = jobtitle;
            String emp = employer;
            String date = ending;
            boolean b = localDB.isReminderExists(jobid);
            int action = (b) ? 1 : 0;
            String jobimage = "";
            try {
                jobimage = java.net.URLDecoder.decode(jobimage, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            addDialogFragment = new AddDialogFragment(PlaceholderFragment.this, action, position, id, jobimage, title, emp, date);
            if (getActivity() != null) {
                addDialogFragment.show(getActivity().getSupportFragmentManager(), "");
            }

        } else if (v == btnReport) {
            reportDialog();
        } else if (v == networkRetry) {
            load(ROWID);
        }
    }

    private void contentShare(String share) {
        ShareBottomSheet newsDetailBottomsheet = new ShareBottomSheet(this, share);
        newsDetailBottomsheet.show(getChildFragmentManager(), "");
    }

    public void loadData(final int position) {
        final String id = rowList.get(position);
        swipeContainer.setRefreshing(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.SERVER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        if (!isDetached()) {
                            setValues(response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                errorHandling(volleyError);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (getActivity() != null) {
                    params.put("action", SU.FETCHJOBS);
                    params.put("employeeid", String.valueOf(employeeid));
                    params.put("id", id);
                    params.put("vcode", String.valueOf(U.versioncode_get(getActivity())));
                }
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQue(stringRequest);
    }

    @SuppressLint("SetTextI18n")
    private void setValues(final String response) {
        if(getActivity()!=null) {
            System.out.println("Data string :" + response);
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                if (status.equals("expired")) {
                    errorView(jsonObject.getString("description"));
                } else {
                    try {
                        divider1 = view.findViewById(R.id.divider1);
                        divider2 = view.findViewById(R.id.divider2);
                        txtEmail = view.findViewById(R.id.txtEmail);
                        temail = view.findViewById(R.id.temail);
                        txtPhone = view.findViewById(R.id.txtPhone);
                        tphone = view.findViewById(R.id.tphone);
                        txtAddress = view.findViewById(R.id.txtAddress);
                        taddress = view.findViewById(R.id.taddress);
                        txtTaluk = view.findViewById(R.id.txtTaluk);
                        ttaluk = view.findViewById(R.id.ttaluk);
                        txtDist = view.findViewById(R.id.txtDist);
                        tdist = view.findViewById(R.id.tdist);
                        tcities = view.findViewById(R.id.tcities);
                        txtCities = view.findViewById(R.id.txtCities);
                        txtSkills = view.findViewById(R.id.txtSkills);
                        tskills = view.findViewById(R.id.tskills);
                        txtDetail = view.findViewById(R.id.txtDetail);
                        tdetail = view.findViewById(R.id.tdetail);
                        txtCompanyName = view.findViewById(R.id.txtCompanyName);
                        txtTitle = view.findViewById(R.id.txtTitle);
                        txtDate = view.findViewById(R.id.txtDate);
                        txtJobType = view.findViewById(R.id.txtJobType);
                        txtFees = view.findViewById(R.id.txtFees);
                        tfees = view.findViewById(R.id.tfees);
                        txtExamCentre = view.findViewById(R.id.txtExamCentre);
                        texamcentre = view.findViewById(R.id.texamcentre);
                        txtSelection = view.findViewById(R.id.txtSelection);
                        tselection = view.findViewById(R.id.tselection);
                        txtSalary = view.findViewById(R.id.txtSalary);
                        tsalary = view.findViewById(R.id.tsalary);
                        txtPostalAddress = view.findViewById(R.id.txtPostalAddress);
                        tpostaladdress = view.findViewById(R.id.tpostaladdress);
                        txtHowToApply = view.findViewById(R.id.txtHowToApply);
                        thowtoapply = view.findViewById(R.id.thowtoapply);
                        txtAgeLimit = view.findViewById(R.id.txtAgeLimit);
                        tagelimit = view.findViewById(R.id.tagelimit);
                        txtExamDate = view.findViewById(R.id.txtExamDate);
                        texamdate = view.findViewById(R.id.texamdate);
                        txtWebsite = view.findViewById(R.id.txtWebsite);
                        txtWebsite2 = view.findViewById(R.id.txtWebsite2);
                        twebsite = view.findViewById(R.id.twebsite);
                        twebsite2 = view.findViewById(R.id.twebsite2);
                        txtStarting = view.findViewById(R.id.txtStarting);
                        tstarting = view.findViewById(R.id.tstarting);
                        txtEnding = view.findViewById(R.id.txtEnding);
                        tending = view.findViewById(R.id.tending);
                        txtExp = view.findViewById(R.id.txtExp);
                        texp = view.findViewById(R.id.texp);
                        txtQualification = view.findViewById(R.id.txtQualification);
                        tqualification = view.findViewById(R.id.tqualification);
                        txtNoOfVacancy = view.findViewById(R.id.txtNoOfVacancy);
                        tnoofvancancy = view.findViewById(R.id.tnoofvancancy);
                        txtPostedBy = view.findViewById(R.id.txtPostedBy);
                        txtJobID = view.findViewById(R.id.txtJobID);


                        jobid = jsonObject.getInt("jobid");
                        jobtype = jsonObject.getString("jobtype");
                        jobtitle = jsonObject.getString("jobtitle");
                        description = jsonObject.getString("description");

                        txtJobID.setText("" + jobid);

                        List<String> skillList = Arrays.asList(jsonObject.getString("skills").split(","));

                        StringBuilder item = new StringBuilder();
                        int count = skillList.size();
                        for (int i = 0; i < skillList.size(); i++) {
                            item.append("&nbsp; &#9930; &nbsp;").append(skillList.get(i));
                            if (i < (count - 1)) item.append("<br><br>");
                        }

                        skills = item.toString();

                        employerid = jsonObject.getInt("employerid");
                        employer = jsonObject.getString("employer");
                        location = jsonObject.getString("location");
                        experience = jsonObject.getString("experience");
                        posteddate = jsonObject.getString("posteddate");
                        apply = jsonObject.getString("apply");
                        isapplied = jsonObject.getInt("isapplied");
                        address = jsonObject.getString("address");
                        phone = jsonObject.getString("phone");
                        email = jsonObject.getString("email");
                        qualification = jsonObject.getString("qualification");
                        noofvancancy = jsonObject.getString("noofvancancy");
                        starting = jsonObject.getString("starting");
                        ending = jsonObject.getString("enddate");
                        website = jsonObject.getString("website");
                        website2 = jsonObject.getString("website2");
                        examdate = jsonObject.getString("examdate");
                        agelimit = jsonObject.getString("agelimit");
                        howtoapply = jsonObject.getString("howtoapply");
                        postaladdress = jsonObject.getString("postaladdress");
                        salary = jsonObject.getString("salary");
                        selectionprocess = jsonObject.getString("selectionprocess");
                        feesdetails = jsonObject.getString("feesdetails");
                        examcentre = jsonObject.getString("examcentre");
                        postedby = jsonObject.getString("inby");

                        txtPostedBy.setText(postedby.equals("null") ? "" : U.FCAPS(postedby));

                        btnReminder.setImageResource(localDB.isReminderExists(jobid) ? R.drawable.ic_alarm_color_24dp : R.drawable.ic_alarm_black_24dp);

                        if (starting.length() == 0 && ending.length() == 0)
                            lDate.setVisibility(View.GONE);
                        else lDate.setVisibility(View.VISIBLE);

                        final int sdk = Build.VERSION.SDK_INT;
                        if (Integer.parseInt(jobtype) == 1) {
                            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                                try {
                                    if (isAdded())
                                        txtJobType.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.bg2));
                                } catch (Exception e) {
                                    Log.e("tag", "" + e);
                                }
                            } else {
                                try {
                                    if (isAdded())
                                        txtJobType.setBackground(getActivity().getResources().getDrawable(R.drawable.bg2));
                                } catch (Exception e) {
                                    Log.e("tag",""+e);
                                }
                            }
                            txtJobType.setText("தனியார்");

                        } else if (Integer.parseInt(jobtype) == 2) {
                            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                                try {
                                    if (isAdded())
                                        txtJobType.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.bg1));
                                } catch (Exception e) {
                                    Log.e("tag",""+e);
                                }
                            } else {
                                try {
                                    if (isAdded())
                                        txtJobType.setBackground(getActivity().getResources().getDrawable(R.drawable.bg1));
                                } catch (Exception e) {
                                    Log.e("tag",""+e);
                                }
                            }
                            txtJobType.setText("மாநில அரசு");

                        } else if (Integer.parseInt(jobtype) == 4) {
                            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                                try {
                                    if (isAdded())
                                        txtJobType.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.bg1));
                                } catch (Exception e) {
                                    Log.e("tag",""+e);
                                }
                            } else {
                                try {
                                    if (isAdded())
                                        txtJobType.setBackground(getActivity().getResources().getDrawable(R.drawable.bg4));
                                } catch (Exception e) {
                                    Log.e("tag",""+e);
                                }
                            }
                            txtJobType.setText("கன்சல்டன்சி");

                        } else {
                            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                                try {
                                    if (isAdded())
                                        txtJobType.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.bg3));
                                } catch (Exception e) {
                                    Log.e("tag",""+e);
                                }
                            } else {
                                try {
                                    if (isAdded())
                                        txtJobType.setBackground(getActivity().getResources().getDrawable(R.drawable.bg3));
                                } catch (Exception e) {
                                    Log.e("tag",""+e);
                                }
                            }
                            txtJobType.setText("மத்திய அரசு");
                        }

                        check(employer, txtCompanyName, null);

                        check(jobtitle, txtTitle, null);

                        check(ending, txtDate, null);

                        check(website, txtWebsite, twebsite);

                        if (website.length() != 0) {
                            txtWebsite.setPaintFlags(txtWebsite.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                            txtWebsite.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
                                        @Override
                                        public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                                            mCustomTabsClient = customTabsClient;
                                            mCustomTabsClient.warmup(0L);
                                            mCustomTabsSession = mCustomTabsClient.newSession(null);
                                        }

                                        @Override
                                        public void onServiceDisconnected(ComponentName name) {
                                            mCustomTabsClient = null;
                                        }
                                    };

                                    CustomTabsClient.bindCustomTabsService(getActivity(), CUSTOM_TAB_PACKAGE_NAME, mCustomTabsServiceConnection);

                                    mCustomTabsIntent = new CustomTabsIntent.Builder(mCustomTabsSession)
                                            .setShowTitle(true)
                                            .setToolbarColor(getActivity().getResources().getColor(R.color.colorPrimary))
                                            .build();
                                    mCustomTabsIntent.launchUrl(getActivity(), Uri.parse(website));
                                }
                            });
                        }

                        if (website2.length() != 0) {
                            txtWebsite2.setPaintFlags(txtWebsite2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                            txtWebsite2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // to solve url opening in drive**
                                    txtWebsite2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
                                                @Override
                                                public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                                                    mCustomTabsClient = customTabsClient;
                                                    mCustomTabsClient.warmup(0L);
                                                    mCustomTabsSession = mCustomTabsClient.newSession(null);
                                                }

                                                @Override
                                                public void onServiceDisconnected(ComponentName name) {
                                                    mCustomTabsClient = null;
                                                }
                                            };

                                            CustomTabsClient.bindCustomTabsService(getActivity(), CUSTOM_TAB_PACKAGE_NAME, mCustomTabsServiceConnection);

                                            mCustomTabsIntent = new CustomTabsIntent.Builder(mCustomTabsSession)
                                                    .setShowTitle(true)
                                                    .setToolbarColor(getActivity().getResources().getColor(R.color.colorPrimary))
                                                    .build();
                                            mCustomTabsIntent.launchUrl(getActivity(), Uri.parse(website2));
                                        }
                                    });
                                }
                            });
                        }

                        qualification = qualification.equals("false") ? "" : qualification;
                        if (qualification.length() != 0) {
                            List<String> qualificationList = Arrays.asList(qualification.split(","));

                            StringBuilder qualificationitem = new StringBuilder();
                            int qualificationcount = qualificationList.size();
                            for (int i = 0; i < qualificationList.size(); i++) {
                                qualificationitem.append("&nbsp; &#9930; &nbsp;").append(qualificationList.get(i));
                                if (i < (qualificationcount - 1))
                                    qualificationitem.append("<br><br>");
                            }

                            qualification = qualificationitem.toString();
                        }
                        check(qualification, txtQualification, tqualification);

                        check(noofvancancy, txtNoOfVacancy, tnoofvancancy);

                        check(description, txtDetail, tdetail);

                        location = location.equals("false") ? "" : location;
                        if (location.length() != 0) {
                            List<String> locationList = Arrays.asList(location.split(","));
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < locationList.size(); i++) {
                                sb.append(locationList.get(i)).append(",");
                            }
                            location = sb.toString();
                            location = location.substring(0, location.length() - 1);
                        }
                        check(location, txtCities, tcities);

                        starting = starting.contains("0000") ? "" : starting;
                        check(starting, txtStarting, tstarting);

                        ending = ending.contains("0000") ? "" : ending;
                        check(ending, txtEnding, tending);

                        if (starting.equals("") || ending.equals("")) {
                            divider1.setVisibility(View.GONE);
                            divider2.setVisibility(View.GONE);
                        }

                        check(agelimit, txtAgeLimit, tagelimit);

                        check(salary, txtSalary, tsalary);

                        if (Integer.parseInt(jobtype) == 2 || Integer.parseInt(jobtype) == 3) {
                            // for govt job
                            gone(txtEmail);
                            gone(temail);
                            gone(txtPhone);
                            gone(tphone);
                            gone(txtAddress);
                            gone(taddress);
                            gone(txtDist);
                            gone(tdist);
                            gone(txtTaluk);
                            gone(ttaluk);
                            gone(txtSkills);
                            gone(tskills);
                            gone(txtExp);
                            gone(texp);

                            visible(txtFees);
                            visible(tfees);
                            visible(txtSelection);
                            visible(tselection);
                            visible(txtPostalAddress);
                            visible(tpostaladdress);
                            visible(txtHowToApply);
                            visible(thowtoapply);
                            visible(txtExamDate);
                            visible(texamdate);
                            visible(txtWebsite2);
                            visible(twebsite2);
                            visible(txtExamCentre);
                            visible(texamcentre);

                            twebsite.setText("அதிகாரப்பூர்வ அறிவிப்பு");

                            examdate = examdate.contains("0000") ? "" : examdate;
                            check(examdate, txtExamDate, texamdate);

                            check(agelimit, txtAgeLimit, tagelimit);

                            check(howtoapply, txtHowToApply, thowtoapply);

                            check(postaladdress, txtPostalAddress, tpostaladdress);

                            check(selectionprocess, txtSelection, tselection);

                            check(feesdetails, txtFees, tfees);

                            check(website2, txtWebsite2, twebsite2);

                            check(examcentre, txtExamCentre, texamcentre);

                        } else {
                            //for privatejob job
                            visible(txtEmail);
                            visible(temail);
                            visible(txtPhone);
                            visible(tphone);
                            visible(txtAddress);
                            visible(taddress);
                            visible(txtDist);
                            visible(tdist);
                            visible(txtTaluk);
                            visible(ttaluk);
                            visible(txtSkills);
                            visible(tskills);
                            visible(txtExp);
                            visible(texp);

                            gone(txtFees);
                            gone(tfees);
                            gone(txtSelection);
                            gone(tselection);
                            gone(txtPostalAddress);
                            gone(tpostaladdress);
                            gone(txtHowToApply);
                            gone(thowtoapply);
                            gone(txtExamDate);
                            gone(texamdate);
                            gone(txtWebsite2);
                            gone(twebsite2);
                            gone(txtExamCentre);
                            gone(texamcentre);

                            twebsite.setText("இணைய முகவரி");

                            check(address, txtAddress, taddress);

                            check(phone, txtPhone, tphone);

                            txtPhone.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    makeCall(phone);
                                }
                            });

                            txtEmail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                                    emailIntent.setType("plain/text");
                                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{txtEmail.getText().toString()});
                                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                                }
                            });

                            check(email, txtEmail, temail);

                            check(item.toString(), txtSkills, tskills);

                            check(experience, txtExp, texp);

                        }

                        if (!apply.equals("no")) {
                            if (Integer.parseInt(jobtype) == 1)
                                btnApply.setVisibility(isapplied == 0 ? View.VISIBLE : View.GONE);
                        }

                        postLoad();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        errorView(U.INA);
                    }
                    mSectionsPagerAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void makeCall(String phone) {

        Intent callIntent = new Intent(Intent.ACTION_VIEW);
        callIntent.setData(Uri.parse("tel:" + phone));
        startActivity(callIntent);
        //Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        //   intent.setData(Uri.parse("tel:" + phone));
        //if (U.checkCallPermission(getActivity())) startActivity(intent);
        //  else L.t(getActivity(), "Call Not Allowed ....");
    }

    private void errorView(String text) {
        lError.setVisibility(View.VISIBLE);
        errorText.setText(text);
        if (text.equals(U.INA)) {
            networkRetry.setVisibility(View.VISIBLE);
        } else {
            networkRetry.setVisibility(View.GONE);
        }
        mNestedScrollView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void postLoad() {
        adCount++;
        lError.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        mNestedScrollView.setVisibility(View.VISIBLE);

        if (U.isNetworkAvailable(getActivity())) {
            System.out.println("internet " + adCount);
            if (indAd.isLoaded()) {
                System.out.println("loaded : " + adCount);
                if (adCount % 12 == 0) {
                    System.out.println("viewed : " + adCount);
                    showIndus();
                } else {
                    System.out.println(" else viewed : " + adCount);
                }
            } else {
                System.out.println("ad not loadded");
            }

        } else adCount = 0;
        System.out.println("ad count : " + adCount);
    }

    private void check(String text, TextView textView1, TextView textView2) {
        text.trim();
        if (text.length() != 0) {
            if (textView1 == txtSkills || textView1 == txtQualification || textView1 == txtCities)
                textView1.setText(Html.fromHtml(text));
            else textView1.setText(text.trim());
            textView1.setVisibility(View.VISIBLE);
            if (textView2 != null) textView2.setVisibility(View.VISIBLE);
        } else {
            textView1.setVisibility(View.GONE);
            if (textView2 != null) textView2.setVisibility(View.GONE);
        }
    }

    private void visible(TextView textView) {
        textView.setVisibility(View.VISIBLE);
    }

    private void gone(TextView textView) {
        textView.setVisibility(View.GONE);
    }

    @Override
    public void onShareItemClick(int item, String message) {
        if (item == 1) whatsAppShare(message);
        else if (item == 2) gmailShare(message);
        if (item == 3) if(getActivity()!=null) U.shareText(getActivity(), message);
    }

    private void gmailShare(String message) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_SUBJECT, SU.APP);
        i.putExtra(Intent.EXTRA_TEXT,
                "நித்ரா வேலைவாய்ப்பு செயலி வழியாக பகிரப்பட்டது. செயலியை தரவிறக்கம் செய்ய: "
                        + U.UTM_SOURCE + "\n\n" + message);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void whatsAppShare(String message) {
        if(getActivity()!=null) {
            message = message.replace("&", ",");
            if (U.isAppInstalled(getActivity(), "com.whatsapp")) {
                startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("whatsapp://send?text=" + "நித்ரா வேலைவாய்ப்பு செயலி வழியாக பகிரப்பட்டது. செயலியை தரவிறக்கம் செய்ய:" + U.UTM_SOURCE + "\n\n" +
                                message)));
            } else L.t(getActivity(), U.ANI);
        }
    }

    public void reportDialog() {
        if(getActivity()!=null) {
            final Dialog report_dialog = new Dialog(getActivity());
            report_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            report_dialog.setContentView(R.layout.report_dia);
            if(report_dialog.getWindow()!=null) {
                report_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            final EditText edname = report_dialog.findViewById(R.id.name);
            final EditText edmobile = report_dialog.findViewById(R.id.mobno);
            final EditText edemail = report_dialog.findViewById(R.id.email);
            final EditText edreport = report_dialog.findViewById(R.id.report);
            TextView reportTxt = report_dialog.findViewById(R.id.report_txt);
            reportTxt.setText(getResources().getString(R.string.rprt));
            edreport.setHint(getResources().getString(R.string.feed_back));
            edemail.setHint(getResources().getString(R.string.email));
            edmobile.setHint(getResources().getString(R.string.mobileno));
            edname.setHint(getResources().getString(R.string.name));
            Button submit = report_dialog.findViewById(R.id.send);
            final RadioGroup reason_grp = report_dialog.findViewById(R.id.report_group);
            ImageView closeBtn = report_dialog.findViewById(R.id.close_btn);
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    report_dialog.dismiss();
                }
            });

            edreport.setVisibility(View.GONE);
            if (pref.getBoolean(getActivity(), U.SH_SIGN_UP_SUCCESS)) {
                edname.setVisibility(View.VISIBLE);
                edmobile.setVisibility(View.VISIBLE);
                edemail.setVisibility(View.VISIBLE);

                edname.setText(pref.getString(getActivity(), U.SH_NAME));
                edmobile.setText(pref.getString(getActivity(), U.SH_MOBILE));
                edemail.setText(pref.getString(getActivity(), U.SH_EMAIL));
            } else {
                edname.setVisibility(View.GONE);
                edmobile.setVisibility(View.GONE);
                edemail.setVisibility(View.GONE);
            }

            reason_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    if (checkedId == R.id.reportbtn5) {
                        edreport.setVisibility(View.VISIBLE);
                    } else {
                        edreport.setVisibility(View.GONE);
                    }
                }
            });

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name = edname.getText().toString();
                    phoneno = edmobile.getText().toString();
                    userEmail = edemail.getText().toString();
                    comment = edreport.getText().toString();
                    if (U.isNetworkAvailable(getActivity())) {
                        if (reason_grp.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(getActivity(), "" + getActivity().getResources().getString(R.string.select_option), Toast.LENGTH_SHORT).show();
                        } else {
                            int selectedId = reason_grp.getCheckedRadioButtonId();
                            RadioButton radioButton = report_dialog.findViewById(selectedId);
                            comment = radioButton.getText().toString();
                            if (comment.equals(getActivity().getResources().getString(R.string.others))) {
                                if (edreport.getText().toString().isEmpty()) {
                                    Toast.makeText(getActivity(), "" + getActivity().getResources().getString(R.string.type), Toast.LENGTH_SHORT).show();
                                } else {
                                    comment = edreport.getText().toString();
                                    @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
                                        public void handleMessage(Message msg) {
                                            Runnable runnable = new Runnable() {
                                                public void run() {
                                                }
                                            };
                                            getActivity().runOnUiThread(runnable);
                                        }
                                    };
                                    final String finalReportValue = comment;
                                    Thread checkUpdate = new Thread() {
                                        public void run() {
                                            try {
                                                loadJSON(jobid, U.SH_REPORT, finalReportValue);
                                            } catch (Exception e) {
                                                Log.e("tag",""+e);
                                            }
                                            handler.sendEmptyMessage(0);
                                        }
                                    };
                                    checkUpdate.start();
                                    Toast.makeText(getActivity(), "" + getResources().getString(R.string.content_saved), Toast.LENGTH_SHORT).show();
                                    report_dialog.dismiss();
                                }

                            } else {
                                @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
                                    public void handleMessage(Message msg) {
                                        Runnable runnable = new Runnable() {
                                            public void run() {
                                            }
                                        };
                                        getActivity().runOnUiThread(runnable);
                                    }
                                };
                                final String finalReportValue = comment;
                                Thread checkUpdate = new Thread() {
                                    public void run() {
                                        try {
                                            loadJSON(jobid, U.SH_REPORT, finalReportValue);
                                        } catch (Exception e) {
                                            Log.e("tag",""+e);
                                        }
                                        handler.sendEmptyMessage(0);
                                    }
                                };
                                checkUpdate.start();
                                Toast.makeText(getActivity(), "" + getResources().getString(R.string.content_saved), Toast.LENGTH_SHORT).show();
                                report_dialog.dismiss();
                            }
                        }
                    } else {
                        Toast.makeText(getActivity(), U.INA, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            report_dialog.show();
        }
    }

    @Override
    public void onRefresh() {
        swipeContainer.setRefreshing(true);
        load(ROWID);
    }

    private void errorHandling(VolleyError error) {
        String e = "";
        if (error instanceof TimeoutError)
            e = "Request TimeOut";
        else if (error instanceof AuthFailureError)
            e = "AuthFailureError";
        else if (error instanceof ServerError)
            e = "ServerError";
        else if (error instanceof NetworkError)
            e = U.INA;
        else if (error instanceof ParseError)
            e = U.ERROR;
        else
            e = U.ERROR;
        errorView(e);
    }

    private void loadJSON(final int jobid, final String task, final String comment) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.GETDATAURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSON(response, task);
                        Log.e("response", "" + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getActivity(), "Request Time Out Please Try again later", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getActivity(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getActivity(), "Server Not Connected", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getActivity(), "Internet Not Available", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (task.equals(U.SH_VIEWCOUNT)) {
                    params.put("action", "view");
                    params.put("jobid", "" + jobid);
                } else if (task.equals(U.SH_REPORT)) {
                    params.put("action", "feedback");
                    params.put("jobid", "" + jobid);
                    params.put("uid", U.getAndroidId(getActivity()));
                    params.put("comment", comment);
                    params.put("name", name);
                    params.put("phone_number", phoneno);
                    params.put("email", userEmail);
                }
                return params;
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQue(stringRequest);
    }

    private void showJSON(String json, String task) {
        JSONObject jsonObject;
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                String status = jsonObject.getString("status");
                if (task.equals(U.SH_REPORT)) {
                    if (status.equals("success"))
                        L.t(getActivity(), "உங்கள் கருத்து பதிவு செய்யப்பட்டது, நன்றி");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskAdded(int position) {
        if(getActivity()!=null) {
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
            load(ROWID);
        }
    }

    @Override
    public void onReminderRemoved(int position) {
        load(ROWID);
    }

    public void refreshAd(final Context context, boolean requestAppInstallAds, boolean requestContentAds, final FrameLayout add_banner) {
        if (!requestAppInstallAds && !requestContentAds) {
            System.out.println("Ad error : " + "At least one ad format must be checked to request an ad.");
            return;
        }

        AdLoader.Builder builder = new AdLoader.Builder(context, U.ADMOB_AD_UNIT_ID);

        if (requestAppInstallAds) {
            try {
                builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
                    @SuppressLint("InflateParams")
                    @Override
                    public void onAppInstallAdLoaded(NativeAppInstallAd ad) {
                        LayoutInflater layinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        NativeAppInstallAdView adView;
                        if (layinflater != null) {
                            adView = (NativeAppInstallAdView) layinflater.inflate(R.layout.ad_app_install_banner, null);
                            populateAppInstallAdView(ad, adView);
                            add_banner.removeAllViews();
                            add_banner.addView(adView);
                        }
                    }
                });
            } catch (Exception e) {
                Log.e("tag",""+e);
            }
        }

        if (requestContentAds) {
            try {
                builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
                    @SuppressLint("InflateParams")
                    @Override
                    public void onContentAdLoaded(NativeContentAd ad) {
                        LayoutInflater layinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        NativeContentAdView adView;
                        if (layinflater != null) {
                            adView = (NativeContentAdView) layinflater.inflate(R.layout.ad_content_banner, null);
                            populateContentAdView(ad, adView);
                            add_banner.removeAllViews();
                            add_banner.addView(adView);
                        }
                    }
                });
            } catch (Exception e) {
                Log.e("tag",""+e);
            }
        }

//        VideoOptions videoOptions = new VideoOptions.Builder()
//                .setStartMuted(true)
//                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                // .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                // mRefresh.setEnabled(true);
                System.out.println("Ad error back : " + "Failed to load native ad: "
                        + errorCode);
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());

    }

    public void populateAppInstallAdView(NativeAppInstallAd nativeAppInstallAd, NativeAppInstallAdView adView) {
        VideoController vc = nativeAppInstallAd.getVideoController();

        vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            public void onVideoEnd() {
                super.onVideoEnd();
            }
        });

        adView.setHeadlineView(adView.findViewById(R.id.appinstall_headline));
        // adView.setBodyView(adView.findViewById(R.id.appinstall_body));
        adView.setCallToActionView(adView.findViewById(R.id.appinstall_call_to_action));
        adView.setIconView(adView.findViewById(R.id.appinstall_app_icon));
        adView.setPriceView(adView.findViewById(R.id.appinstall_price));
        adView.setStarRatingView(adView.findViewById(R.id.appinstall_stars));
        adView.setStoreView(adView.findViewById(R.id.appinstall_store));

        MediaView mediaView = adView.findViewById(R.id.appinstall_media);
        adView.setMediaView(mediaView);

        ((TextView) adView.getHeadlineView()).setText(nativeAppInstallAd.getHeadline());
        //   ((TextView) adView.getBodyView()).setText(nativeAppInstallAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAppInstallAd.getCallToAction());

        if (nativeAppInstallAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAppInstallAd.getPrice());
        }

        if (nativeAppInstallAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAppInstallAd.getStore());
        }

        if (nativeAppInstallAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAppInstallAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        NativeAd.Image logoImage = nativeAppInstallAd.getIcon();

        if (logoImage == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(logoImage.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(nativeAppInstallAd);
    }

    private void populateContentAdView(NativeContentAd nativeContentAd, NativeContentAdView contentAdView) {
        contentAdView.setHeadlineView(contentAdView.findViewById(R.id.contentad_headline));
        //contentAdView.setImageView(contentAdView.findViewById(R.id.contentad_image));
        // contentAdView.setBodyView(contentAdView.findViewById(R.id.contentad_body));
        contentAdView.setCallToActionView(contentAdView.findViewById(R.id.contentad_call_to_action));
        contentAdView.setLogoView(contentAdView.findViewById(R.id.contentad_logo));
        contentAdView.setAdvertiserView(contentAdView.findViewById(R.id.contentad_advertiser));

        // Some assets are guaranteed to be in every NativeContentAd.
        ((TextView) contentAdView.getHeadlineView()).setText(nativeContentAd.getHeadline());
        //  ((TextView) contentAdView.getBodyView()).setText(nativeContentAd.getBody());
        ((TextView) contentAdView.getCallToActionView()).setText(nativeContentAd.getCallToAction());
        ((TextView) contentAdView.getAdvertiserView()).setText(nativeContentAd.getAdvertiser());

//        List<NativeAd.Image> images = nativeContentAd.getImages();

        /*if (images.size() > 0) {
            ((ImageView) contentAdView.getImageView()).setImageDrawable(images.get(0).getDrawable());
        }*/

        // Some aren't guaranteed, however, and should be checked.
        NativeAd.Image logoImage = nativeContentAd.getLogo();

        if (logoImage == null) {
            contentAdView.getLogoView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) contentAdView.getLogoView()).setImageDrawable(logoImage.getDrawable());
            contentAdView.getLogoView().setVisibility(View.VISIBLE);

        }

        // Assign native ad object to the native view.
        contentAdView.setNativeAd(nativeContentAd);
    }

}
