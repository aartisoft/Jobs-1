package nithra.jobs.career.placement.fragments;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.activity.RegistrationActivity;
import nithra.jobs.career.placement.adapters.RelatedJobsDataAdapter;
import nithra.jobs.career.placement.bottomsheets.ShareBottomSheet;
import nithra.jobs.career.placement.engine.LocalDB;
import nithra.jobs.career.placement.listeners.EndlessRelatedJobsScrollListener;
import nithra.jobs.career.placement.networking.MySingleton;
import nithra.jobs.career.placement.pojo.Jobs;
import nithra.jobs.career.placement.utills.FlexboxLayout;
import nithra.jobs.career.placement.utills.L;
import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.TouchImageView;
import nithra.jobs.career.placement.utills.U;

/**
 * Created by nithra-apps on 14/10/17.
 */
public class PlaceholderFragment extends Fragment implements View.OnClickListener,
        ShareBottomSheet.OnShareClick, SwipeRefreshLayout.OnRefreshListener,
        AddDialogFragment.DialogListener, RelatedJobsDataAdapter.OnRelatedJobItemClick {

    //private static final String CONTACT_ASK_MESSAGE = "உங்களுக்கு Call சேவையை கொடுக்க பின்வரும் permission-களை allow செய்யவேண்டும்.";

    private static final String ARG_SECTION_NUMBER = "section_number";
    private TouchImageView imageJob;
    private TextView txtJobType;
    private TextView txtSkills;
    private TextView txtQualification;
    private TextView txtStarting;
    private TextView txtEnding;
    private TextView txtExamDate;
    private TextView txtCities;
    private TextView txtjobLocation;
    private ImageButton btnLike, btnReminder, btnReport;
    private TextView btnApply, bottombtnApply, errorText, tRelatedJobs;
    private View view;
    private int employerid, isapplied, jobid = 0;
    private LocalDB localDB;
    private String viewType, jobtype, jobtitle, jobtitleId, description, skills, employer, experience, posteddate, apply,
            phone, address, email, qualification, noofvancancy, starting, ending, website, website2,
            examdate, agelimit, howtoapply, postaladdress, salary, selectionprocess, feesdetails,
            examcentre, postedby, location, webViewBanner, joblocation, gender, maritalStatus, phpostion,
            detailViewImage, callOption, mailOption, mailWithResume, workmode;
    private String name = "", phoneno = "", comment = "", userEmail = "", task = "", key = "", value = "";
    private LinearLayout lDate, lError, postedByLay, lProgress, startDateLay, endDateLay,
            examDateLay;
    private SpinKitView progressBar;
    private NestedScrollView mNestedScrollView;
    private ImageView imgShare;
    private SharedPreference pref;
    private SwipeRefreshLayout swipeContainer;
    private LinearLayout detailLay;
    private Button networkRetry;
    private FrameLayout adLay;
    private List<Jobs> list;
    private RecyclerView relatedJobsList;
    private RelatedJobsDataAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private RelativeLayout imageLay;
    private int ROWID;
    private int click = 0;
    private List<String> candloc;
    private com.facebook.ads.NativeBannerAd nativeBannerAd;
    private com.facebook.ads.NativeAdLayout nativeAdLayout;

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber, String task,
                                                  String key, String value) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString("Task", task);
        args.putString("key", key);
        args.putString("value", value);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
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
        list = new ArrayList<>();
        candloc = new ArrayList<>();

        errorText = view.findViewById(R.id.txtError1);
        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);
        lError = view.findViewById(R.id.lError);
        mNestedScrollView = view.findViewById(R.id.mNestedScrollView);
        progressBar = view.findViewById(R.id.progressBar);
        postedByLay = view.findViewById(R.id.postedbylay);
        lProgress = view.findViewById(R.id.lProgress);
        imageLay = view.findViewById(R.id.imageLay);
        detailLay = view.findViewById(R.id.detailLay);

        relatedJobsList = view.findViewById(R.id.relatedJobsList);
        adapter = new RelatedJobsDataAdapter(this, list);
        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
        relatedJobsList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        relatedJobsList.setLayoutManager(mLayoutManager);
        relatedJobsList.setAdapter(adapter);
        relatedJobsList.setRecycledViewPool(recycledViewPool);

        pref = new SharedPreference();
        adLay = view.findViewById(R.id.frame);
        if (pref.getInt(getActivity(), U.SH_AD_PURCHASED) == 0) {
            adLay.setBackground(getActivity().getResources().getDrawable(R.drawable.adv_banner_sample));
        }

        tRelatedJobs = view.findViewById(R.id.tRelatedJobs);

        networkRetry = view.findViewById(R.id.network_retry);
        networkRetry.setOnClickListener(this);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            localDB = new LocalDB(getActivity());
            int postedbyValue = pref.getInt(getActivity(), U.SH_REMOTE_POSTEDBY);
            Log.e("vacancypostedbyValue", "" + postedbyValue);
            if (postedbyValue == 0) {
                postedByLay.setVisibility(View.GONE);
            } else if (postedbyValue == 1) {
                postedByLay.setVisibility(View.VISIBLE);
            }

            if (pref.getInt(getActivity(), U.SH_AD_PURCHASED) == 0) {
                if (U.isNetworkAvailable(getActivity())) {
//                    refreshAd(getActivity(), true, true, adLay);
                    loadNativeAd(getActivity());
                }
            }

            if (getArguments() != null) {
                task = getArguments().getString("Task");
                key = getArguments().getString("key");
                value = getArguments().getString("value");
                ROWID = getArguments().getInt(ARG_SECTION_NUMBER);
                Log.e("RowId", "" + ROWID);

                List<String> rowList = new ArrayList<>();
                rowList = Arrays.asList(key.split(","));
                swipeContainer.setVisibility(View.VISIBLE);
                try {
                    jobid = Integer.parseInt(rowList.get(ROWID));
                    load(jobid);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    errorView(U.DNF);
                }
            }
        }
    }

    private void load(int id) {

        jobid = id;

        if (U.isNetworkAvailable(getActivity())) {
            preLoad();

            loadJSON(jobid, U.SH_VIEWCOUNT, "");

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
            if (getArguments() != null) {
                if (localDB.isBookMarkExists(jobid)) {
                    if (localDB.deleteJobBookMark(jobid)) {
                        L.t(getActivity(), U.FAV_REMOVED);
                        btnLike.setImageResource(R.drawable.star_off);
                    }
                } else {
                    if (localDB.addJobBookMark(jobid, jobtitleId)) {
                        L.t(getActivity(), U.FAV_SUCCESS);
                        btnLike.setImageResource(R.drawable.star_on);
                    }
                }
            }

        } else if (v == imgShare) {
            if (viewType.equals("image")) {
                PermissionFun();
            } else {
                String share;
                try {
                    share = "Job Title : " + jobtitle + "\n" + "Description : " + description + "\n\n" + "மேலும் வேலைவாய்ப்பை பற்றி அறிந்துகொள்ள கீழே உள்ள லிங்கை கிளிக் செய்யவும்" +
                            " " + SU.BASE_URL + "deeplinking.php?id=" + jobid + "\n\n";
                } catch (Exception e) {
                    share = "";
                }
                contentShare(share);
            }
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
            addDialogFragment = new AddDialogFragment(PlaceholderFragment.this, action, ROWID, id, jobimage, title, jobtitleId, emp, date);
            if (getActivity() != null) {
                addDialogFragment.show(getActivity().getSupportFragmentManager(), "");
            }
        } else if (v == btnReport) {
            reportDialog();
        } else if (v == networkRetry) {
            load(jobid);
        } else if (v == btnApply || v == bottombtnApply) {
            if (U.isNetworkAvailable(getActivity())) {
                if (getActivity() != null) {
                    if (isapplied == 0) {
                        applyDialog("apply");
                    } else {
                        L.t(getActivity(), "You have Already Applied for this job");
                    }
                }
            } else {
                L.t(getActivity(), U.INA);
            }
        }
    }

    private void applyDialog(final String type) {
        if (getActivity() != null) {
            final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Light_NoTitleBar_Fullscreen);
            dialog.setContentView(R.layout.apply_bottom_sheet);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            TextView hintText = dialog.findViewById(R.id.hintText);
            TextView tqualification = dialog.findViewById(R.id.tqualification);
            TextView txtQualification = dialog.findViewById(R.id.txtQualification);
            TextView tskills = dialog.findViewById(R.id.tskills);
            TextView txtskills = dialog.findViewById(R.id.txtSkills);
            TextView tLocation = dialog.findViewById(R.id.tLocation);
            final TextView txtLocation = dialog.findViewById(R.id.txtLocation);

            TextView applyBtn = dialog.findViewById(R.id.applyBtn);
            applyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (U.isNetworkAvailable(getActivity())) {
                        if (type.equals("apply")) {
                            Log.e("apply_type", "" + mailOption);
                            if (!pref.getBoolean(getActivity(), U.SH_SIGN_UP_SUCCESS)) {
                                Log.e("applyCount", "three");
                                alertDialog("இந்த வேலைவாய்ப்புக்கு விண்ணப்பிக்க உங்கள் சுயவிவரத்தை பதிவு செய்ய வேண்டும்.", SU.REGISTER, dialog);
                            } else if (mailOption.equals("1")) {
                                click = 0;
                                sendApplyRequest(jobid);
                                dialog.dismiss();
                            } else if (mailWithResume.equals("1") && pref.getString(getActivity(), U.SH_RESUME).equals("")) {
                                Log.e("applyCount", "one");
                                alertDialog("இந்த வேலைவாய்ப்புக்கு விண்ணப்பிக்க உங்கள் சுயவிவரத்தை upload செய்ய வேண்டும்.", U.SH_RESUME, dialog);
                            } else {
                                click = 0;
                                sendApplyRequest(jobid);
                                dialog.dismiss();
                            }
                        } else {
                            if (getActivity() != null) {
                                if (U.isNetworkAvailable(getActivity())) {
                                    if (isapplied == 0) {
                                        loadJSON(jobid, U.SH_CALL_CONFIRMATION_COUNT, type);
                                        makeCall(type);
                                    } else {
                                        L.t(getActivity(), "Already Applied");
                                    }
                                }
                            }
                        }
                    } else {
                        L.t(getActivity(), U.INA);
                    }
                }
            });

            if (type.equals("apply")) {
                hintText.setText("இந்த வேலைவாய்ப்பில் கொடுக்கப்பட்டுள்ள இடம், கல்வித்தகுதி, அனுபவம், திறன், வயது வரம்பு இவை அனைத்திற்கும் நீங்கள் தகுதியுடையவர்களாக இருக்கும் பட்சத்தில் இப்பணிக்கு விண்ணப்பிக்கவும்.");
                applyBtn.setText("APPLY");
            } else {
                hintText.setText("இந்த வேலைவாய்ப்பில் கொடுக்கப்பட்டுள்ள இடம், கல்வித்தகுதி, அனுபவம், திறன், வயது வரம்பு இவை அனைத்திற்கும் நீங்கள் தகுதியுடையவர்களாக இருக்கும் பட்சத்தில் " + type + " என்ற எண்ணிற்கு தொடர்பு கொள்ளுங்கள்.");
                applyBtn.setText("CALL");
            }

            if (qualification.length() != 0) {
                txtQualification.setText(Html.fromHtml(qualification));
                txtQualification.setVisibility(View.VISIBLE);
            } else {
                txtQualification.setVisibility(View.GONE);
                if (tqualification != null) tqualification.setVisibility(View.GONE);
            }

            if (skills.length() != 0) {
                txtskills.setText(Html.fromHtml(skills));
                txtskills.setVisibility(View.VISIBLE);
            } else {
                txtskills.setVisibility(View.GONE);
                if (tskills != null) tskills.setVisibility(View.GONE);
            }

            if (joblocation.length() != 0) {
                txtLocation.setText(Html.fromHtml(joblocation));
                txtLocation.setVisibility(View.VISIBLE);
                txtLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringBuilder candlocitem = new StringBuilder();
                        int candloccount = candloc.size();
                        if (candloccount > 7) {
                            for (int i = 0; i < candloc.size(); i++) {
                                candlocitem.append("&nbsp; &#9930; &nbsp;").append(candloc.get(i));
                                if (i < (candloccount - 1))
                                    candlocitem.append("<br><br>");
                            }
                            txtLocation.setText(Html.fromHtml(candlocitem.toString()));
                        }
                    }
                });
            } else {
                txtLocation.setVisibility(View.GONE);
                if (tLocation != null) tLocation.setVisibility(View.GONE);
            }

            dialog.show();
        }
    }

    private void PermissionFun() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
                dialog.setContentView(R.layout.permission_dialog_layout);
                Button ok = dialog.findViewById(R.id.permission_ok);
                Button cancel = dialog.findViewById(R.id.permission_cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                TextView txt = dialog.findViewById(R.id.text);
                if (pref.getInt(getActivity(), U.SH_STORAGE_PERMISSION) == 2) {
                    txt.setText("To share image, Please enable the following permissions in App settings");
                } else {
                    txt.setText("To share image, Please grant the following permissions");
                }

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pref.getInt(getActivity(), U.SH_STORAGE_PERMISSION) == 2) {
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                            dialog.dismiss();
                        } else {
                            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 154);
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            } else {

                imageShare(getActivity(), detailViewImage);
            }
        } else {
            imageShare(getActivity(), detailViewImage);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void imageShare(final Context context, final String imgUrl) {

        String root = Environment.getExternalStorageDirectory().toString();
        final File myDir = new File(root + "/Nithra/Jobs/");
        myDir.mkdirs();

        final String fname = "imageJobShare.png";

        final ProgressDialog mProgress = new ProgressDialog(context);
        mProgress.setMessage("Processing...");
        mProgress.setCancelable(false);

        new AsyncTask<String, String, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgress.show();
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    Bitmap image = null;
                    try {
                        URL url = new URL(imgUrl);
                        image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    } catch (IOException e) {
                        System.out.println(e);
                    }

                    File file = new File(myDir, fname);
                    if (file.exists()) file.delete();
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        image.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    Log.e("catch", "" + e);
                }
                return "result";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                try {
                    File file_image = new File(myDir, fname);
                    if (file_image.exists()) {
                        Uri uri = Uri.fromFile(file_image);
                        Intent share = new Intent();
                        share.setAction(Intent.ACTION_SEND);
                        share.setType("image/*");
                        share.putExtra(Intent.EXTRA_STREAM, uri);
                        share.putExtra(Intent.EXTRA_TEXT, "நித்ரா வேலைவாய்ப்பு செயலி வழியாக பகிரப்பட்டது. செயலியை தரவிறக்கம் செய்ய: " + U.UTM_SOURCE + "\n\n");
                        share.putExtra(Intent.EXTRA_SUBJECT, "நித்ரா வேலைவாய்ப்பு செயலி");
                        startActivity(Intent.createChooser(share, "Share Card Using"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mProgress.dismiss();
            }

        }.execute();

    }

    private void contentShare(String share) {
        ShareBottomSheet newsDetailBottomsheet = new ShareBottomSheet(this, share);
        newsDetailBottomsheet.show(getChildFragmentManager(), "");
    }

    private void loadData(final int id) {
        swipeContainer.setRefreshing(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.SERVER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        if (!isDetached()) {
                            Log.e("response", "" + response);
                            setValues(response);
                            EndlessRelatedJobsScrollListener.resetState();
                            if (value != null && value.equals(SU.POPULAR_EMP)) {
                                tRelatedJobs.setVisibility(View.GONE);
                                relatedJobsList.setVisibility(View.GONE);
                            } else {
                                loadRelatedJobs(id);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("volleyError", "" + volleyError);
                errorHandling(volleyError);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (getActivity() != null) {
                    params.put("action", SU.FETCHJOBS);
                    params.put("id", "" + id);
                    params.put("user_type", pref.getString(getActivity(), U.SH_USER_TYPE));
                    params.put("employee_id", pref.getString(getActivity(), U.SH_EMPLOYEE_ID));
                    params.put("android_id", U.getAndroidId(getActivity()));
                    params.put("vcode", String.valueOf(U.versioncode_get(getActivity())));
                    Log.e("paramsresponse", "" + params);
                }
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQue(stringRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 154: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pref.putInt(getActivity(), U.SH_STORAGE_PERMISSION, 1);
                    imageShare(getActivity(), detailViewImage);
                } else {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale = shouldShowRequestPermissionRationale(permissions[0]);
                        if (!showRationale) {
                            pref.putInt(getActivity(), U.SH_STORAGE_PERMISSION, 2);
                        } else if (android.Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permissions[0])) {
                            pref.putInt(getActivity(), U.SH_STORAGE_PERMISSION, 0);
                        }
                    }
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void loadRelatedJobs(final int id) {
        swipeContainer.setRefreshing(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.SERVER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        if (!isDetached()) {
                            Log.e("response", "" + response);
                            showRelatedJobs(response);
                            assignScrollListener(id);
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
                    params.put("action", SU.RELATEDJOBS);
                    params.put("job_id", "" + id);
                    params.put("load", "0");
                    params.put("user_type", pref.getString(getActivity(), U.SH_USER_TYPE));
                    params.put("employee_id", pref.getString(getActivity(), U.SH_EMPLOYEE_ID));
                    params.put("android_id", U.getAndroidId(getActivity()));
                    params.put("vcode", String.valueOf(U.versioncode_get(getActivity())));
                    Log.e("paramsresponse", "" + params);
                }
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQue(stringRequest);
    }

    private void showRelatedJobs(String response) {
        list.clear();
        adapter.clear();
        relatedJobsList.getRecycledViewPool().clear();
        adapter.notifyDataSetChanged();
        System.out.println("Data string :" + response);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    Jobs jobs = new Jobs();
                    jobs.setId(jo.getInt("jobid"));
                    jobs.setImage(jo.getString("image"));
                    jobs.setJobtype(jo.getString("jobtype"));
                    jobs.setJobtitle(jo.getString("jobtitle"));
                    jobs.setJobtitleId(jo.getString("jobtitle_id"));
                    jobs.setEmployer(jo.getString("employer"));
                    jobs.setDistrict(jo.getString("location"));
                    jobs.setNoofvacancy(jo.getString("noofvancancy"));
                    jobs.setVerified(jo.getString("verified"));
                    jobs.setDate(jo.getString("ending"));
                    jobs.setDatediff(jo.getInt("datediff"));
                    jobs.setDescription(jo.getString("description"));
                    Cursor c1 = localDB.getQry("SELECT * FROM ReadUnreadTable where read_id='" + jo.getInt("jobid") + "'");
                    c1.moveToFirst();
                    if (c1.getCount() == 0) {
                        jobs.setRead(false);
                    } else {
                        jobs.setRead(true);
                    }
                    c1.close();

                    if (contains(list, jo.getInt("jobid"))) {
                        System.out.println("jobid-- already exists");
                    } else {
                        System.out.println("jobid--" + jo.getInt("jobid"));
                        list.add(jobs);
                    }
                }
                adapter.addAll(list);

                String data = jsonObject.getString("data");
                int vcount = jsonObject.getInt("vcount");

                relatedJobsList.getRecycledViewPool().clear();
                relatedJobsList.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            } else {
                tRelatedJobs.setVisibility(View.GONE);
                relatedJobsList.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void assignScrollListener(final int id) {
        EndlessRelatedJobsScrollListener scrollListener = new EndlessRelatedJobsScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount, final RecyclerView view) {
                lProgress.setVisibility(View.VISIBLE);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.SERVER,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println("Data string :" + response);
                                lProgress.setVisibility(View.GONE);
                                JSONObject jsonObject;
                                JSONArray jsonArray;
                                try {
                                    jsonObject = new JSONObject(response);
                                    jsonArray = jsonObject.getJSONArray("list");

                                    List<Jobs> moreJobs = new ArrayList<>();
                                    moreJobs.clear();
                                    if (jsonArray.length() == 0) {
//                                        L.t(getActivity(), "No More Jobs");
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
                                        jobs.setDistrict(jo.getString("location"));
                                        jobs.setNoofvacancy(jo.getString("noofvancancy"));
                                        jobs.setVerified(jo.getString("verified"));
                                        jobs.setDate(jo.getString("ending"));
                                        jobs.setDatediff(jo.getInt("datediff"));
                                        jobs.setDescription(jo.getString("description"));
                                        Cursor c1 = localDB.getQry("SELECT * FROM ReadUnreadTable where read_id='" + jo.getInt("jobid") + "'");
                                        c1.moveToFirst();
                                        if (c1.getCount() == 0) {
                                            jobs.setRead(false);
                                        } else {
                                            jobs.setRead(true);
                                        }

                                        if (contains(list, jo.getInt("jobid"))) {
                                            System.out.println("jobid-- already exists");
                                        } else {
                                            System.out.println("jobid--" + jo.getInt("jobid"));
                                            moreJobs.add(jobs);
                                            Log.e("noofvancancy", "" + page + " - " + jo.getString("noofvancancy") + " - " + jo.getInt("jobid"));
                                        }
                                    }


                                    list.addAll(moreJobs);

                                    adapter.addAll(list);

                                    view.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                relatedJobsList.getRecycledViewPool().clear();
                                                adapter.notifyDataSetChanged();
                                            } catch (Exception e) {
                                                System.out.println("Error : " + e.getMessage());
                                            }
                                        }
                                    });
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
                        if (getActivity() != null) {
                            params.put("action", SU.RELATEDJOBS);
                            params.put("job_id", "" + id);
                            params.put("load", String.valueOf(page));
                            params.put("user_type", pref.getString(getActivity(), U.SH_USER_TYPE));
                            params.put("employee_id", pref.getString(getActivity(), U.SH_EMPLOYEE_ID));
                            params.put("android_id", U.getAndroidId(getActivity()));
                            params.put("vcode", String.valueOf(U.versioncode_get(getActivity())));
                            Log.e("paramsresponse", "" + params);
                        }
                        return params;
                    }
                };
                MySingleton.getInstance(getContext()).addToRequestQue(stringRequest);
            }
        };
        relatedJobsList.addOnScrollListener(scrollListener);
    }

    public boolean contains(List<Jobs> list, int id) {
        for (Jobs item : list) {
            if (item.getId() == id) {
                return true;
            }
        }
        return false;
    }

    @SuppressLint("SetTextI18n")
    private void setValues(final String response) {
        if (getActivity() != null) {
            System.out.println("Data string :" + response);
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                viewType = "";
                if (status.equals("expired")) {
                    errorView(jsonObject.getString("description"));
                } else {
                    getValues(jsonObject);
                    TextView txtJobID = view.findViewById(R.id.txtJobID);
                    txtJobID.setText("" + jobid);
                    TextView txtPostedBy = view.findViewById(R.id.txtPostedBy);
                    txtPostedBy.setText(postedby.equals("null") ? "" : U.FCAPS(postedby));
                }

                if (viewType.equals("image")) {
                    detailLay.setVisibility(View.GONE);
                    imageLay.setVisibility(View.VISIBLE);
                    imageJob = view.findViewById(R.id.imageJob);

                    imgShare = view.findViewById(R.id.imggShare);
                    imgShare.setOnClickListener(this);

                    btnLike = view.findViewById(R.id.imgbtnLike);
                    btnLike.setOnClickListener(this);
                    btnLike.setImageResource(localDB.isBookMarkExists(jobid) ? R.drawable.star_on : R.drawable.star_off);

                    btnReport = view.findViewById(R.id.imgbtnReport);
                    btnReport.setOnClickListener(this);

                    btnReminder = view.findViewById(R.id.imgbtnReminder);
                    btnReminder.setOnClickListener(this);
                    btnReminder.setImageResource(localDB.isReminderExists(jobid) ? R.drawable.ic_alarm_color_png : R.drawable.ic_alarm_black_png);

                    txtJobType = view.findViewById(R.id.imgJobType);

                    setJobType();

                    Glide.with(getActivity())
                            .load(detailViewImage)
                            .asBitmap()
                            .placeholder(R.drawable.trans_back)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(new BitmapImageViewTarget(imageJob) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    imageJob.setImageBitmap(resource);
                                }
                            });
                    postLoad();

                } else if (viewType.equals("normal")) {
                    detailLay.setVisibility(View.VISIBLE);
                    imageLay.setVisibility(View.GONE);
                    btnApply = view.findViewById(R.id.btnApply);
                    btnApply.setOnClickListener(this);
                    btnApply.setVisibility(View.GONE);
                    bottombtnApply = view.findViewById(R.id.bottombtnApply);
                    bottombtnApply.setOnClickListener(this);
                    bottombtnApply.setVisibility(View.GONE);
                    imgShare = view.findViewById(R.id.imgShare);
                    imgShare.setOnClickListener(this);

                    btnLike = view.findViewById(R.id.btnLike);
                    btnLike.setOnClickListener(this);
                    btnLike.setImageResource(localDB.isBookMarkExists(jobid) ? R.drawable.star_on : R.drawable.star_off);

                    btnReport = view.findViewById(R.id.btnReport);
                    btnReport.setOnClickListener(this);

                    btnReminder = view.findViewById(R.id.btnReminder);
                    btnReminder.setOnClickListener(this);
                    btnReminder.setImageResource(localDB.isReminderExists(jobid) ? R.drawable.ic_alarm_color_png : R.drawable.ic_alarm_black_png);

                    View divider1 = view.findViewById(R.id.divider1);
                    View divider2 = view.findViewById(R.id.divider2);
                    startDateLay = view.findViewById(R.id.startDateLay);
                    endDateLay = view.findViewById(R.id.endDateLay);
                    examDateLay = view.findViewById(R.id.examDateLay);
                    FlexboxLayout txtEmail = view.findViewById(R.id.txtEmail);
                    TextView temail = view.findViewById(R.id.temail);
                    FlexboxLayout txtPhone = view.findViewById(R.id.txtPhone);
                    FlexboxLayout txtBottomPhone = view.findViewById(R.id.txtBottomPhone);
                    TextView tphone = view.findViewById(R.id.tphone);
                    TextView tBottomphone = view.findViewById(R.id.tBottomphone);
                    TextView txtAddress = view.findViewById(R.id.txtAddress);
                    TextView taddress = view.findViewById(R.id.taddress);
                    TextView txtTaluk = view.findViewById(R.id.txtTaluk);
                    TextView ttaluk = view.findViewById(R.id.ttaluk);
                    TextView txtDist = view.findViewById(R.id.txtDist);
                    TextView tdist = view.findViewById(R.id.tdist);
                    TextView tcities = view.findViewById(R.id.tcities);
                    txtCities = view.findViewById(R.id.txtCities);
                    txtSkills = view.findViewById(R.id.txtSkills);
                    TextView tskills = view.findViewById(R.id.tskills);
                    TextView txtDetail = view.findViewById(R.id.txtDetail);
                    TextView tdetail = view.findViewById(R.id.tdetail);
                    TextView txtCompanyName = view.findViewById(R.id.txtCompanyName);
                    TextView txtTitle = view.findViewById(R.id.txtTitle);
                    txtJobType = view.findViewById(R.id.txtJobType);
                    TextView txtFees = view.findViewById(R.id.txtFees);
                    TextView tfees = view.findViewById(R.id.tfees);
                    TextView txtExamCentre = view.findViewById(R.id.txtExamCentre);
                    TextView texamcentre = view.findViewById(R.id.texamcentre);
                    TextView txtSelection = view.findViewById(R.id.txtSelection);
                    TextView tselection = view.findViewById(R.id.tselection);
                    TextView txtSalary = view.findViewById(R.id.txtSalary);
                    TextView tsalary = view.findViewById(R.id.tsalary);
                    TextView txtPostalAddress = view.findViewById(R.id.txtPostalAddress);
                    TextView tpostaladdress = view.findViewById(R.id.tpostaladdress);
                    TextView txtHowToApply = view.findViewById(R.id.txtHowToApply);
                    TextView thowtoapply = view.findViewById(R.id.thowtoapply);
                    TextView txtAgeLimit = view.findViewById(R.id.txtAgeLimit);
                    TextView tagelimit = view.findViewById(R.id.tagelimit);
                    txtExamDate = view.findViewById(R.id.txtExamDate);
                    TextView texamdate = view.findViewById(R.id.texamdate);
                    TextView txtWebsite = view.findViewById(R.id.txtWebsite);
                    TextView txtWebsite2 = view.findViewById(R.id.txtWebsite2);
                    TextView twebsite = view.findViewById(R.id.twebsite);
                    TextView twebsite2 = view.findViewById(R.id.twebsite2);
                    txtStarting = view.findViewById(R.id.txtStarting);
                    TextView tstarting = view.findViewById(R.id.tstarting);
                    txtEnding = view.findViewById(R.id.txtEnding);
                    TextView tending = view.findViewById(R.id.tending);
                    TextView txtExp = view.findViewById(R.id.txtExp);
                    TextView texp = view.findViewById(R.id.texp);
                    txtQualification = view.findViewById(R.id.txtQualification);
                    TextView tqualification = view.findViewById(R.id.tqualification);
                    TextView txtNoOfVacancy = view.findViewById(R.id.txtNoOfVacancy);
                    TextView tnoofvancancy = view.findViewById(R.id.tnoofvancancy);
                    TextView twebaddress = view.findViewById(R.id.twebaddress);
                    TextView tjobLocation = view.findViewById(R.id.tjobLocation);
                    txtjobLocation = view.findViewById(R.id.txtjobLocation);
                    TextView infoText = view.findViewById(R.id.infoText);
                    TextView tgender = view.findViewById(R.id.tGender);
                    TextView txtGender = view.findViewById(R.id.txtGender);
                    TextView tmaritalStatus = view.findViewById(R.id.tmaritalStatus);
                    TextView txtMaritalStatus = view.findViewById(R.id.txtmaritalStatus);
                    TextView txtWorkmode = view.findViewById(R.id.txtWorkmode);
                    WebView txtwebaddress = view.findViewById(R.id.txtwebAddress);
                    txtwebaddress.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

                    if (!skills.isEmpty()) {
                        List<String> skillList = Arrays.asList(skills.split(","));
                        StringBuilder item = new StringBuilder();
                        int count = skillList.size();
                        for (int i = 0; i < skillList.size(); i++) {
                            item.append("&nbsp; &#9930; &nbsp;").append(skillList.get(i));
                            if (i < (count - 1)) item.append("<br><br>");
                        }
                        skills = item.toString();
                    }

                    check(gender, txtGender, tgender);

                    check(maritalStatus, txtMaritalStatus, tmaritalStatus);

                    checkWebBanner(webViewBanner, twebaddress, txtwebaddress);

                    if (starting.length() == 0 && ending.length() == 0) {
                        lDate.setVisibility(View.GONE);
                    } else {
                        lDate.setVisibility(View.VISIBLE);
                    }

                    setJobType();

                    check(employer, txtCompanyName, null);

                    check(jobtitle, txtTitle, null);

                    if (workmode.equals("1")) {
                        txtWorkmode.setText("முழு நேரம்");
                    } else if (workmode.equals("2")) {
                        txtWorkmode.setText("பகுதி நேரம்");
                    } else {
                        txtWorkmode.setText("");
                    }

                    check(website, txtWebsite, twebsite);

                    if (website.length() != 0) {
                        txtWebsite.setPaintFlags(txtWebsite.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txtWebsite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (getActivity() != null) {
                                    if (U.isNetworkAvailable(getActivity())) {
                                        if (website.substring(0, 4).equals("http")) {
                                            U.custom_tabs(getActivity(), website);
                                        } else {
                                            String web = "http://" + website;
                                            U.custom_tabs(getActivity(), web);
                                        }
                                    } else {
                                        L.t(getActivity(), U.INA);
                                    }
                                }
                            }
                        });
                    }

                    if (website2.length() != 0) {
                        txtWebsite2.setPaintFlags(txtWebsite2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txtWebsite2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // to solve url opening in drive**
                                if (getActivity() != null) {
                                    if (U.isNetworkAvailable(getActivity())) {
                                        if (website2.substring(0, 4).equals("http")) {
                                            U.custom_tabs(getActivity(), website2);
                                        } else {
                                            String web = "http://" + website2;
                                            U.custom_tabs(getActivity(), web);
                                        }
                                    } else {
                                        L.t(getActivity(), U.INA);
                                    }
                                }
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
                    String[] locArray = location.split(", ");
                    if (locArray.length > 7) {
                        location = locArray[0] + ", " + locArray[1] + ", " + locArray[2] + ", " + locArray[3] + " <u><font color='#03A9F4'> +More</font></u>";
                        final List<String> loc = new ArrayList<>(Arrays.asList(locArray));
                        txtCities.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ShowAll(loc);
                            }
                        });
                    }

                    check(location, txtCities, tcities);

                    starting = starting.contains("0000") ? "" : starting;
                    check(starting, txtStarting, tstarting);

                    ending = ending.contains("0000") ? "" : ending;
                    check(ending, txtEnding, tending);

                    if (starting.equals("") || ending.equals("")) {
                        divider1.setVisibility(View.GONE);
                        divider2.setVisibility(View.GONE);
                        examDateLay.setVisibility(View.GONE);
                    } else {
                        examDateLay.setVisibility(View.GONE);
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
                        gone(btnApply);
                        gone(txtBottomPhone);
                        gone(tBottomphone);
                        gone(bottombtnApply);
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
                        gone(tjobLocation);
                        gone(txtjobLocation);
                        gone(infoText);

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

                        if (!examdate.equals("")) {
                            examDateLay.setVisibility(View.VISIBLE);
                            divider2.setVisibility(View.VISIBLE);
                        }

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
                        visible(txtBottomPhone);
                        visible(tBottomphone);
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
                        visible(tjobLocation);
                        visible(txtjobLocation);
                        visible(infoText);

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

                        /*taddress.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = "https://www.google.com/maps?&daddr=11.3782° N, 77.8969° E";
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }
                        });*/

                        Log.e("phonee", "" + phone);
                        if (callOption.equals("1")) {
                            if (phpostion.equals("top")) {
                                txtBottomPhone.setVisibility(View.GONE);
                                tBottomphone.setVisibility(View.GONE);
                                checkPhoneNumber("", txtPhone, tphone);
                            } else if (phpostion.equals("bottom")) {
                                txtPhone.setVisibility(View.GONE);
                                tphone.setVisibility(View.GONE);
                                checkPhoneNumber("", txtBottomPhone, tBottomphone);
                            }
                        } else {
                            if (phpostion.equals("top")) {
                                txtBottomPhone.setVisibility(View.GONE);
                                tBottomphone.setVisibility(View.GONE);
                                checkPhoneNumber(phone, txtPhone, tphone);
                            } else if (phpostion.equals("bottom")) {
                                txtPhone.setVisibility(View.GONE);
                                tphone.setVisibility(View.GONE);
                                checkPhoneNumber(phone, txtBottomPhone, tBottomphone);
                            }
                        }

                        if (pref.getString(getActivity(), U.SH_USER_TYPE).equals(U.EMPLOYEE)) {
                            if (mailOption.equals("1") || mailWithResume.equals("1")) {
                                if (phpostion.equals("top")) {
                                    tphone.setVisibility(View.VISIBLE);
                                    btnApply.setVisibility(View.VISIBLE);
                                } else {
                                    tBottomphone.setVisibility(View.VISIBLE);
                                    bottombtnApply.setVisibility(View.VISIBLE);
                                }

                                if (isapplied == 0) {
                                    btnApply.setText("APPLY");
                                    bottombtnApply.setText("APPLY");
                                } else {
                                    btnApply.setText("APPLIED");
                                    bottombtnApply.setText("APPLIED");
                                }
                            }
                        }

                        Log.e("phoneeEmail", "" + email);
                        checkEmail(email, txtEmail, temail);

                        check(skills, txtSkills, tskills);

                        check(experience, txtExp, texp);

                        String[] candlocArray = joblocation.split(", ");
                        if (candlocArray.length > 7) {
                            joblocation = candlocArray[0] + ", " + candlocArray[1] + ", " + candlocArray[2] + ", " + candlocArray[3] + " <u><font color='#03A9F4'> +More</font></u>";
                            candloc.addAll(Arrays.asList(candlocArray));
                            txtjobLocation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ShowAll(candloc);
                                }
                            });
                        }
                        check(joblocation, txtjobLocation, tjobLocation);
                    }
                    postLoad();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setJobType() {
        if (getActivity() != null) {
            final int sdk = Build.VERSION.SDK_INT;
            if (Integer.parseInt(jobtype) == 1) {
                if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                    try {
                        if (isAdded())
                            txtJobType.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.bg_lightblue));
                    } catch (Exception e) {
                        Log.e("tag", "" + e);
                    }
                } else {
                    try {
                        if (isAdded())
                            txtJobType.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_lightblue));
                    } catch (Exception e) {
                        Log.e("tag", "" + e);
                    }
                }
                txtJobType.setText("தனியார்");

            } else if (Integer.parseInt(jobtype) == 2) {
                if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                    try {
                        if (isAdded())
                            txtJobType.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.bg_green));
                    } catch (Exception e) {
                        Log.e("tag", "" + e);
                    }
                } else {
                    try {
                        if (isAdded())
                            txtJobType.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_green));
                    } catch (Exception e) {
                        Log.e("tag", "" + e);
                    }
                }
                txtJobType.setText("மாநில அரசு");

            } else if (Integer.parseInt(jobtype) == 4) {
                if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                    try {
                        if (isAdded())
                            txtJobType.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.bg_green));
                    } catch (Exception e) {
                        Log.e("tag", "" + e);
                    }
                } else {
                    try {
                        if (isAdded())
                            txtJobType.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_pink));
                    } catch (Exception e) {
                        Log.e("tag", "" + e);
                    }
                }
                txtJobType.setText("கன்சல்டன்சி");

            } else {
                if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                    try {
                        if (isAdded())
                            txtJobType.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.bg_brownish_red));
                    } catch (Exception e) {
                        Log.e("tag", "" + e);
                    }
                } else {
                    try {
                        if (isAdded())
                            txtJobType.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_brownish_red));
                    } catch (Exception e) {
                        Log.e("tag", "" + e);
                    }
                }
                txtJobType.setText("மத்திய அரசு");
            }
        }
    }

    private void getValues(JSONObject jsonObject) {
        try {
            jobid = jsonObject.getInt("jobid");
            jobtype = jsonObject.getString("jobtype");
            jobtitle = jsonObject.getString("jobtitle");
            jobtitleId = jsonObject.getString("jobtitle_id");
            description = jsonObject.getString("description");
            skills = jsonObject.getString("skills");
            employerid = jsonObject.getInt("employerid");
            employer = jsonObject.getString("employer");
            location = jsonObject.getString("location");
            joblocation = jsonObject.getString("cand_location");
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
            webViewBanner = jsonObject.getString("web_view_banner");
            gender = jsonObject.getString("gender");
            maritalStatus = jsonObject.getString("marital_status");
            phpostion = jsonObject.getString("ph_postion");
            workmode = jsonObject.getString("workmode");

            viewType = jsonObject.getString("view_type");
            detailViewImage = jsonObject.getString("detail_view_image");

            callOption = jsonObject.getString("disable_call_button");
            mailOption = jsonObject.getString("enable_detail_apply");
            mailWithResume = jsonObject.getString("enable_resume_apply");

            Log.e("ReadId", "task :" + task);
            if (!task.equals(SU.GETRECENTJOBS)) {
                Cursor c1 = localDB.getQry("SELECT * FROM ReadUnreadTable where read_id='" + jobid + "'");
                c1.moveToFirst();
                if (c1.getCount() == 0) {
                    if (localDB.insertReadId(String.valueOf(jobid), jobtitleId)) {
                        Log.e("readId", "if inserted - " + jobtitleId);
                    }
                } else {
                    if (localDB.deleteReadId(jobid)) {
                        Log.e("readId", "else inserted - " + jobtitleId);
                        localDB.insertReadId(String.valueOf(jobid), jobtitleId);
                    }
                }
                c1.close();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void makeCall(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_VIEW);
        callIntent.setData(Uri.parse("tel:" + phone));
        startActivity(callIntent);
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
        lError.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        mNestedScrollView.setVisibility(View.VISIBLE);
    }

    private void check(String text, TextView textView1, TextView textView2) {
        if (text.length() != 0) {
            if (textView1 == txtSkills || textView1 == txtQualification || textView1 == txtCities || textView1 == txtjobLocation) {
                textView1.setText(Html.fromHtml(text));
            } else {
                textView1.setText(text.trim());
            }
            textView1.setVisibility(View.VISIBLE);
            if (textView2 != null) textView2.setVisibility(View.VISIBLE);
            if (textView1 == txtStarting) {
                startDateLay.setVisibility(View.VISIBLE);
            } else if (textView1 == txtEnding) {
                endDateLay.setVisibility(View.VISIBLE);
            } else if (textView1 == txtExamDate) {
                examDateLay.setVisibility(View.VISIBLE);
            }
        } else {
            textView1.setVisibility(View.GONE);
            if (textView2 != null) textView2.setVisibility(View.GONE);
            if (textView1 == txtStarting) {
                startDateLay.setVisibility(View.GONE);
            } else if (textView1 == txtEnding) {
                endDateLay.setVisibility(View.GONE);
            } else if (textView1 == txtExamDate) {
                examDateLay.setVisibility(View.GONE);
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void checkWebBanner(String text, TextView textView1, WebView textView2) {
        if (text.length() != 0) {
            WebSettings ws = textView2.getSettings();
            ws.setJavaScriptEnabled(true);
            textView2.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
            textView2.setWebViewClient(new WebViewClient() {

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    // Handle the error
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (getActivity() != null) {
                        U.custom_tabs(getActivity(), url);
                    }
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                }
            });

            if (getActivity() != null) {
                U.webview(getActivity(), text, textView2);
            }
        } else {
            textView1.setVisibility(View.GONE);
            textView2.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void checkPhoneNumber(String text, final FlexboxLayout textView2, TextView textView1) {
        Log.e("phoneeNumber", "textValue :" + text);
        textView2.removeAllViews();
        if (text.length() != 0) {
            final String[] valueArray;
            valueArray = text.split(",");
            if (valueArray.length > 0) {
                for (int i = 0; i < valueArray.length; i++) {
                    if (getActivity() != null) {
                        final TextView tv = new TextView(getActivity());
                        tv.setPadding(5, 5, 5, 5);
                        tv.setId(i);
                        if (i == (valueArray.length - 1)) {
                            tv.setText(Html.fromHtml(valueArray[i] + " <font color='blue'><u> அழைக்க &raquo;</u></font>"));
                        } else {
                            tv.setText(Html.fromHtml(valueArray[i] + " <font color='blue'><u> அழைக்க &raquo;</u></font>" + " , "));
                        }
                        tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                        final int finalI = i;
                        tv.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("InflateParams")
                            @Override
                            public void onClick(View v) {
                                if (getActivity() != null) {
                                    if (U.isNetworkAvailable(getActivity())) {
                                        loadJSON(jobid, U.SH_CALL_COUNT, valueArray[finalI]);
                                    }
                                    if (getActivity().getFragmentManager() != null) {
                                        applyDialog(valueArray[finalI]);
                                    }
                                }
                            }
                        });
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView2.addView(tv);
                            }
                        });
                    }
                }
                textView1.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
            }
        } else {
            textView1.setVisibility(View.GONE);
            textView2.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void checkEmail(String text, final FlexboxLayout textView2, TextView textView1) {
        textView2.removeAllViews();
        if (text.length() != 0) {
            final String[] valueArray = text.split(",");
            if (valueArray.length > 0) {
                for (int i = 0; i < valueArray.length; i++) {
                    if (getActivity() != null) {
                        final TextView tv = new TextView(getActivity());
                        tv.setPadding(5, 5, 5, 5);
                        tv.setId(i);
                        if (i == (valueArray.length - 1)) {
                            tv.setText(valueArray[i]);
                        } else {
                            tv.setText(valueArray[i] + " , ");
                        }
                        tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                        final int finalI = i;
                        tv.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("InflateParams")
                            @Override
                            public void onClick(View v) {
                                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                                emailIntent.setType("plain/text");
                                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{valueArray[finalI]});
                                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                            }
                        });
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView2.addView(tv);
                            }
                        });
                    }
                }
                textView1.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
            }
        } else {
            textView1.setVisibility(View.GONE);
            textView2.setVisibility(View.GONE);
        }
    }

    private void visible(View textView) {
        textView.setVisibility(View.VISIBLE);
    }

    private void gone(View textView) {
        textView.setVisibility(View.GONE);
    }

    @Override
    public void onShareItemClick(int item, String message) {
        if (item == 1) whatsAppShare(message);
        else if (item == 2) gmailShare(message);
        if (item == 3) if (getActivity() != null) U.shareText(getActivity(), message);
    }

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
        if (getActivity() != null) {
            message = message.replace("&", ",");
            if (U.isAppInstalled(getActivity(), "com.whatsapp")) {
                startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("whatsapp://send?text=" + "நித்ரா வேலைவாய்ப்பு செயலி வழியாக பகிரப்பட்டது. செயலியை தரவிறக்கம் செய்ய:" + U.UTM_SOURCE + "\n\n" +
                                message)));
            } else L.t(getActivity(), U.ANI);
        }
    }

    private void reportDialog() {
        if (getActivity() != null) {
            final Dialog report_dialog = new Dialog(getActivity());
            report_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            report_dialog.setContentView(R.layout.report_dia);
            if (report_dialog.getWindow() != null) {
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
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(edreport.getWindowToken(), 0);
                    }
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
                    if (checkedId == R.id.reportbtn6) {
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
                                                Log.e("tag", "" + e);
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
                                            Log.e("tag", "" + e);
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
        load(jobid);
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
                        errorHandling(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (getActivity() != null) {
                    switch (task) {
                        case U.SH_VIEWCOUNT:
                            params.put("action", "view");
                            params.put("jobid", "" + jobid);
                            break;
                        case U.SH_REPORT:
                            params.put("action", "feedback");
                            params.put("jobid", "" + jobid);
                            params.put("uid", U.getAndroidId(getActivity()));
                            params.put("comment", comment);
                            params.put("name", name);
                            params.put("phone_number", phoneno);
                            params.put("email", userEmail);
                            params.put("company_name", employer);
                            params.put("company_phone", phone);
                            params.put("job_title", jobtitle);
                            break;
                        case U.SH_CALL_COUNT:
                        case U.SH_CALL_CONFIRMATION_COUNT:
                            params.put("action", task);
                            params.put("jobid", "" + jobid);
                            params.put("phoneNumber", "" + comment);
                            break;

                    }
                    params.put("user_type", pref.getString(getActivity(), U.SH_USER_TYPE));
                    params.put("employee_id", pref.getString(getActivity(), U.SH_EMPLOYEE_ID));
                    params.put("android_id", U.getAndroidId(getActivity()));
                    params.put("vcode", String.valueOf(U.versioncode_get(getActivity())));
                    Log.e("paramsresponse", "" + params);
                }
                return params;
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQue(stringRequest);
    }

    private void showJSON(String json, String task) {
        Log.e("showresponse", "" + json);
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
                } else if (task.equals(U.SH_CALL_CONFIRMATION_COUNT) ||
                        task.equals(U.SH_CALL_COUNT)) {
                    load(jobid);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskAdded(int position) {
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
            load(jobid);
        }
    }

    @Override
    public void onReminderRemoved(int position) {
        load(jobid);
    }

    private void ShowAll(List<String> list) {
        if (getActivity() != null) {
            final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            }
            dialog.setContentView(R.layout.more_location);

            ImageView close_btn = dialog.findViewById(R.id.close_btn);
            close_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            ListView listView = dialog.findViewById(R.id.listview);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
            listView.setTextFilterEnabled(true);
            listView.setAdapter(adapter);

            dialog.show();
        }
    }

    @Override
    public void setOnRelatedJobItemClick(int id) {
        load(id);
        mNestedScrollView.scrollTo(0, 0);
    }

    private void sendApplyRequest(final int id) {
        swipeContainer.setRefreshing(false);
        U.mProgress(getActivity(), "Applying...", false).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.CANDIDATE_APPLY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        Log.e("response", "" + response);
                        if (localDB.addAppliedJob(String.valueOf(jobid), jobtitleId)) {
                            Log.e("AppReadId", "insert Apply :" + jobtitleId);
                            L.t(getActivity(), "Applied Successfully");
                            isapplied = 1;
                            btnApply.setText("APPLIED");
                            bottombtnApply.setText("APPLIED");
                            U.mProgress.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("volleyError", "" + volleyError);
                errorHandling(volleyError);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (getActivity() != null && click == 0) {
                    click = 1;
                    String applyMode = "";
                    if (mailOption.equals("1") && mailWithResume.equals("1")) {
                        if (pref.getString(getActivity(), U.SH_RESUME).equals("")) {
                            applyMode = "detail";
                        } else {
                            applyMode = "resume";
                        }
                    } else if (mailOption.equals("1")) {
                        applyMode = "detail";
                    } else if (mailWithResume.equals("1")) {
                        applyMode = "resume";
                    }

                    final String[] valueArray = email.split(",");

                    params.put("action", SU.APPLYJOBS);
                    params.put("job_id", "" + id);
                    params.put("employer_email_id", "" + valueArray[0]);
                    params.put("apply_mode", applyMode);
                    params.put("user_type", pref.getString(getActivity(), U.SH_USER_TYPE));
                    params.put("employee_id", pref.getString(getActivity(), U.SH_EMPLOYEE_ID));
                    params.put("android_id", U.getAndroidId(getActivity()));
                    params.put("vcode", String.valueOf(U.versioncode_get(getActivity())));
                    Log.e("paramsresponse", "" + params);
                }
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQue(stringRequest);
    }

    private void alertDialog(String msg, final String task, final Dialog adialog) {
        Log.e("applyCount", "" + msg + "  " + task);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder
                .setTitle("அறிவிப்பு!")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("தொடர", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int ids) {
                        if (U.isNetworkAvailable(getActivity())) {
                            Intent localIntent = new Intent(getActivity(), RegistrationActivity.class);
                            localIntent.putExtra("task", task);
                            startActivity(localIntent);
                        } else {
                            adialog.dismiss();
                            errorView(U.INA);
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("வெளியேறு", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //------------------------------------ Facebook Native Ads -------------------------------------

    private void loadNativeAd(final Context context) {
        final SharedPreference pref = new SharedPreference();
        // Instantiate a NativeAd object.
        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
        // now, while you are testing and replace it later when you have signed up.
        // While you are using this temporary code you will only get test ads and if you release
        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
        nativeBannerAd = new com.facebook.ads.NativeBannerAd(context, U.FB_DETAIL_NATIVE);
        nativeBannerAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
                Log.e("TAG", "Native ad finished downloading all assets.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
                Log.e("TAG", "Native ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Native ad is loaded and ready to be displayed
                Log.d("TAG", "Native ad is loaded and ready to be displayed!");
                if (nativeBannerAd == null || nativeBannerAd != ad) {
                    return;
                }
                // Inflate Native Ad into Container
                inflateAd(context);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
                Log.d("TAG", "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
                Log.d("TAG", "Native ad impression logged!");
            }
        });
        // Request an ad
        nativeBannerAd.loadAd();
    }

    private void inflateAd(Context context) {

        nativeBannerAd.unregisterView();

        nativeAdLayout = new NativeAdLayout(context);
        // Add the Ad view into the ad container.
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        View adView = inflater.inflate(R.layout.fb_native_detail, null);
        nativeAdLayout.addView(adView);
        adLay.removeAllViews();
        adLay.addView(nativeAdLayout);

        // Add the AdChoices icon
        RelativeLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(context, nativeBannerAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        AdIconView nativeAdIconView = adView.findViewById(R.id.native_icon_view);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
        sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());

        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        nativeBannerAd.registerViewForInteraction(adView, nativeAdIconView, clickableViews);
    }

    // -------------------------------------------- Google Ads -------------------------------------

    private void refreshAd(final Context context, boolean requestAppInstallAds, boolean requestContentAds, final FrameLayout add_banner) {
        if (context != null) {
            if (!requestAppInstallAds && !requestContentAds) {
                System.out.println("Ad error : " + "At least one ad format must be checked to request an ad.");
                return;
            }

            AdLoader.Builder builder = new AdLoader.Builder(context, U.JOBS_NATIVE_DETAILPAGE);

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
                                add_banner.setBackground(context.getResources().getDrawable(R.drawable.adv_banner_sample));
                                add_banner.addView(adView);
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e("tag", "" + e);
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
                                add_banner.setBackground(context.getResources().getDrawable(R.drawable.adv_banner_sample));
                                add_banner.addView(adView);
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e("tag", "" + e);
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
    }

    private void populateAppInstallAdView(NativeAppInstallAd nativeAppInstallAd, NativeAppInstallAdView adView) {
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

        /*MediaView mediaView = adView.findViewById(R.id.appinstall_media);
        adView.setMediaView(mediaView);*/

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
