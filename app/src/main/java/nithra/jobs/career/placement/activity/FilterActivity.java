package nithra.jobs.career.placement.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.networking.MySingleton;
import nithra.jobs.career.placement.pojo.Item;
import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

/**
 * Created by nithra-apps on 6/7/17.
 */

public class FilterActivity extends AppCompatActivity {

    LinearLayout jobModeLay, skillsLay, locationLay, categoryLay, workmodeLay, qualificationLay, experienceLay;
    ListView filterList;
    SearchView searchView;
    Spinner qualificationSpinner;
    List<String> selectedSkills, selectedLocation, selectedCategory, selectedQualification, selectedExperience,
            selectedWorkmode, selectedJobmode;
    List<Item> skillsList, locationList, jobCatList, qualificationList, experienceList, workmodeList, jobmodeList, courseList;
    CategorySelectionAdapter catAdapter;
    LocationSelectionAdapter locAdapter;
    SkillsSelectionAdapter SkillAdapter;
    QualificationSelectionAdapter qualificationAdapter;
    ExperienceSelectionAdapter experienceAdapter;
    WorkModeSelectionAdapter workmodeAdapter;
    JobModeSelectionAdapter jobmodeAdapter;
    SharedPreference sp;
    Button setFilter;
    ImageView infoBtn,refreshBtn;
    String skills = "", location = "", jobCat = "", jobMode = "", workMode = "", qualification = "", experience = "";
    Toolbar toolbar;
    LinearLayout adLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_layout);
        setStatusBarTranslucent(false);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        adLayout = findViewById(R.id.adview);
        setFilter = findViewById(R.id.set_btn);
        infoBtn = findViewById(R.id.info_btn);
        refreshBtn = findViewById(R.id.refresh_btn);

        jobModeLay = findViewById(R.id.job_mode);
        skillsLay = findViewById(R.id.skills);
        locationLay = findViewById(R.id.location);
        categoryLay = findViewById(R.id.category);
        workmodeLay = findViewById(R.id.workmode);
        qualificationLay = findViewById(R.id.qualification);
        experienceLay = findViewById(R.id.experience);

        filterList = findViewById(R.id.filter_list);
        searchView = findViewById(R.id.searchview);
        qualificationSpinner = findViewById(R.id.qualification_spinner);

        skillsList = new ArrayList<Item>();
        locationList = new ArrayList<Item>();
        jobCatList = new ArrayList<Item>();
        experienceList = new ArrayList<Item>();
        qualificationList = new ArrayList<Item>();
        workmodeList = new ArrayList<Item>();
        jobmodeList = new ArrayList<Item>();
        courseList = new ArrayList<Item>();

        selectedSkills = new ArrayList<String>();
        selectedCategory = new ArrayList<String>();
        selectedLocation = new ArrayList<String>();
        selectedQualification = new ArrayList<String>();
        selectedExperience = new ArrayList<String>();
        selectedWorkmode = new ArrayList<String>();
        selectedJobmode = new ArrayList<String>();

        sp = new SharedPreference();

        setupSearchView();

        loadJSON(SU.GETJOBTYPE);
        loadJSON(SU.GETSKILLS);
        loadJSON(SU.GETDISTRICT);
        loadJSON(SU.GETJOBSCAT);
        loadJSON(SU.GETEXPERIENCE);
        loadJSON(SU.GETQUALIFICATION);
        loadJSON(SU.GETWORKMODE);

        if (sp.getInt(FilterActivity.this, U.SH_FILTER_INFO_SHOW) == 0) {
            sp.putInt(FilterActivity.this, U.SH_FILTER_INFO_SHOW, 1);
            showInfoDialog();
        }

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FilterActivity.this, FilterActivity.class));
                finish();
            }
        });

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog();
            }
        });

        jobModeLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery("", true);
                sp.putInt(FilterActivity.this, U.SH_SKILLS_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_LOCATION_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_CATEGORY_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_WORKMODE_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_EXPERIENCE_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_QUALIFICATION_CLICKED, 0);

                jobModeLay.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_sqr));
                skillsLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                locationLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                categoryLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                workmodeLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                qualificationLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                experienceLay.setBackgroundColor(getResources().getColor(R.color.light_orange));

                qualificationSpinner.setVisibility(View.GONE);
                filterList.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.GONE);
                sp.putInt(FilterActivity.this, U.SH_JOBMODE_CLICKED, 1);
                jobmodeAdapter = new JobModeSelectionAdapter(FilterActivity.this, R.layout.filter_layout, jobmodeList);
                filterList.setTextFilterEnabled(true);
                filterList.setAdapter(jobmodeAdapter);
            }
        });
        skillsLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery("", true);
                sp.putInt(FilterActivity.this, U.SH_LOCATION_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_CATEGORY_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_WORKMODE_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_JOBMODE_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_EXPERIENCE_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_QUALIFICATION_CLICKED, 0);

                skillsLay.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_sqr));
                jobModeLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                locationLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                categoryLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                workmodeLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                qualificationLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                experienceLay.setBackgroundColor(getResources().getColor(R.color.light_orange));

                qualificationSpinner.setVisibility(View.GONE);
                filterList.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.VISIBLE);
                sp.putInt(FilterActivity.this, U.SH_SKILLS_CLICKED, 1);
                SkillAdapter = new SkillsSelectionAdapter(FilterActivity.this, R.layout.filter_layout, skillsList);
                filterList.setTextFilterEnabled(true);
                filterList.setAdapter(SkillAdapter);
            }
        });
        locationLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery("", true);
                sp.putInt(FilterActivity.this, U.SH_SKILLS_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_JOBMODE_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_CATEGORY_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_WORKMODE_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_EXPERIENCE_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_QUALIFICATION_CLICKED, 0);

                locationLay.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_sqr));
                skillsLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                jobModeLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                categoryLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                workmodeLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                qualificationLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                experienceLay.setBackgroundColor(getResources().getColor(R.color.light_orange));

                qualificationSpinner.setVisibility(View.GONE);
                filterList.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.VISIBLE);
                sp.putInt(FilterActivity.this, U.SH_LOCATION_CLICKED, 1);
                locAdapter = new LocationSelectionAdapter(FilterActivity.this, R.layout.filter_layout, locationList);
                filterList.setTextFilterEnabled(true);
                filterList.setAdapter(locAdapter);
            }
        });
        categoryLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery("", true);
                sp.putInt(FilterActivity.this, U.SH_SKILLS_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_JOBMODE_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_LOCATION_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_WORKMODE_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_EXPERIENCE_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_QUALIFICATION_CLICKED, 0);

                categoryLay.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_sqr));
                skillsLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                locationLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                jobModeLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                workmodeLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                qualificationLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                experienceLay.setBackgroundColor(getResources().getColor(R.color.light_orange));

                qualificationSpinner.setVisibility(View.GONE);
                filterList.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.VISIBLE);
                sp.putInt(FilterActivity.this, U.SH_CATEGORY_CLICKED, 1);
                catAdapter = new CategorySelectionAdapter(FilterActivity.this, R.layout.filter_layout, jobCatList);
                filterList.setTextFilterEnabled(true);
                filterList.setAdapter(catAdapter);
            }
        });
        workmodeLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery("", true);
                sp.putInt(FilterActivity.this, U.SH_SKILLS_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_LOCATION_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_EXPERIENCE_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_CATEGORY_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_JOBMODE_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_QUALIFICATION_CLICKED, 0);

                workmodeLay.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_sqr));
                categoryLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                skillsLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                locationLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                jobModeLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                qualificationLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                experienceLay.setBackgroundColor(getResources().getColor(R.color.light_orange));

                qualificationSpinner.setVisibility(View.GONE);
                filterList.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.GONE);
                sp.putInt(FilterActivity.this, U.SH_WORKMODE_CLICKED, 1);
                workmodeAdapter = new WorkModeSelectionAdapter(FilterActivity.this, R.layout.filter_layout, workmodeList);
                filterList.setTextFilterEnabled(true);
                filterList.setAdapter(workmodeAdapter);
            }
        });
        qualificationLay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                searchView.setQuery("", true);
                sp.putInt(FilterActivity.this, U.SH_SKILLS_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_LOCATION_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_EXPERIENCE_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_CATEGORY_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_WORKMODE_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_JOBMODE_CLICKED, 0);

                qualificationLay.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_sqr));
                categoryLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                skillsLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                locationLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                jobModeLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                workmodeLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                experienceLay.setBackgroundColor(getResources().getColor(R.color.light_orange));

                qualificationSpinner.setVisibility(View.VISIBLE);
                SpinnerAdapter adapter = new SpinnerAdapter(FilterActivity.this, qualificationList);
                qualificationSpinner.setAdapter(adapter);

                qualificationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        searchView.setQuery("",false);
                        int item = qualificationList.get(position).getId();
                        loadQualificationJSON(SU.GETCOURSE, String.valueOf(item));
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                filterList.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.VISIBLE);
                sp.putInt(FilterActivity.this, U.SH_QUALIFICATION_CLICKED, 1);
                qualificationAdapter = new QualificationSelectionAdapter(FilterActivity.this, R.layout.filter_layout, courseList);
                filterList.setTextFilterEnabled(true);
                filterList.setAdapter(qualificationAdapter);
            }
        });
        experienceLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery("", true);
                sp.putInt(FilterActivity.this, U.SH_SKILLS_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_LOCATION_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_WORKMODE_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_CATEGORY_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_JOBMODE_CLICKED, 0);
                sp.putInt(FilterActivity.this, U.SH_QUALIFICATION_CLICKED, 0);

                experienceLay.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_sqr));
                categoryLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                skillsLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                locationLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                jobModeLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                workmodeLay.setBackgroundColor(getResources().getColor(R.color.light_orange));
                qualificationLay.setBackgroundColor(getResources().getColor(R.color.light_orange));

                qualificationSpinner.setVisibility(View.GONE);
                filterList.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.VISIBLE);
                sp.putInt(FilterActivity.this, U.SH_EXPERIENCE_CLICKED, 1);
                experienceAdapter = new ExperienceSelectionAdapter(FilterActivity.this, R.layout.filter_layout, experienceList);
                filterList.setTextFilterEnabled(true);
                filterList.setAdapter(experienceAdapter);
            }
        });
        jobModeLay.performClick();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (sp.getInt(FilterActivity.this, U.SH_SKILLS_CLICKED) == 1) {
                    try {
                        SkillAdapter.filter(newText.trim());
                    } catch (Exception e) {
                        Log.e("Error", "" + e);
                    }
                }
                if (sp.getInt(FilterActivity.this, U.SH_LOCATION_CLICKED) == 1) {
                    try {
                        locAdapter.filter(newText.trim());
                    } catch (Exception e) {
                        Log.e("Error", "" + e);
                    }
                }
                if (sp.getInt(FilterActivity.this, U.SH_CATEGORY_CLICKED) == 1) {
                    try {
                        catAdapter.filter(newText.trim());
                    } catch (Exception e) {
                        Log.e("Error", "" + e);
                    }
                }
                if (sp.getInt(FilterActivity.this, U.SH_QUALIFICATION_CLICKED) == 1) {
                    try {
                        qualificationAdapter.filter(newText.trim());
                    } catch (Exception e) {
                        Log.e("Error", "" + e);
                    }
                }
                if (sp.getInt(FilterActivity.this, U.SH_EXPERIENCE_CLICKED) == 1) {
                    try {
                        experienceAdapter.filter(newText.trim());
                    } catch (Exception e) {
                        Log.e("Error", "" + e);
                    }
                }
                if (sp.getInt(FilterActivity.this, U.SH_WORKMODE_CLICKED) == 1) {
                    try {
                        workmodeAdapter.filter(newText.trim());
                    } catch (Exception e) {

                    }
                }
                if (sp.getInt(FilterActivity.this, U.SH_JOBMODE_CLICKED) == 1) {
                    try {
                        jobmodeAdapter.filter(newText.trim());
                    } catch (Exception e) {
                        Log.e("Error", "" + e);
                    }
                }
                if (sp.getInt(FilterActivity.this, U.SH_QUALIFICATION_CLICKED) == 1) {
                    try {
                        qualificationAdapter.filter(newText.trim());
                    } catch (Exception e) {
                        Log.e("Error", "" + e);
                    }
                }
                return true;
            }
        });

        setFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery("", true);
                if (selectedSkills.isEmpty()) {

                    skills = "";

                } else {
                    String filter = "";
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < selectedSkills.size(); i++) {
                        sb.append(selectedSkills.get(i) + ",");
                    }
                    filter = sb.toString();
                    filter = filter.substring(0, filter.length() - 1);
                    skills = filter;
                }

                if (selectedLocation.isEmpty()) {

                    location = "";

                } else {
                    String filter1 = "";
                    StringBuilder sb1 = new StringBuilder();
                    for (int i = 0; i < selectedLocation.size(); i++) {
                        sb1.append(selectedLocation.get(i) + ",");
                    }
                    filter1 = sb1.toString();
                    filter1 = filter1.substring(0, filter1.length() - 1);
                    location = filter1;
                }

                if (selectedCategory.isEmpty()) {

                    jobCat = "";

                } else {
                    String filter = "";
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < selectedCategory.size(); i++) {
                        sb.append(selectedCategory.get(i) + ",");
                    }
                    filter = sb.toString();
                    filter = filter.substring(0, filter.length() - 1);
                    jobCat = filter;
                }

                if (selectedQualification.isEmpty()) {

                    qualification = "";

                } else {
                    String filter = "";
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < selectedQualification.size(); i++) {
                        sb.append(selectedQualification.get(i) + ",");
                    }
                    filter = sb.toString();
                    filter = filter.substring(0, filter.length() - 1);
                    qualification = filter;
                }

                if (selectedExperience.isEmpty()) {

                    experience = "";

                } else {
                    String filter = "";
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < selectedExperience.size(); i++) {
                        sb.append(selectedExperience.get(i) + ",");
                    }
                    filter = sb.toString();
                    filter = filter.substring(0, filter.length() - 1);
                    experience = filter;
                }

                if (selectedJobmode.isEmpty()) {

                    jobMode = "";

                } else {
                    String filter = "";
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < selectedJobmode.size(); i++) {
                        sb.append(selectedJobmode.get(i) + ",");
                    }
                    filter = sb.toString();
                    filter = filter.substring(0, filter.length() - 1);
                    jobMode = filter;
                }

                if (selectedWorkmode.isEmpty()) {

                    workMode = "";

                } else {
                    String filter = "";
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < selectedWorkmode.size(); i++) {
                        sb.append(selectedWorkmode.get(i) + ",");
                    }
                    filter = sb.toString();
                    filter = filter.substring(0, filter.length() - 1);
                    workMode = filter;
                }
                Intent intent = new Intent(FilterActivity.this, FilterResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("action", SU.FILTER);
                bundle.putString("workmode", workMode);
                bundle.putString("skill", skills);
                bundle.putString("location", location);
                bundle.putString("experience", experience);
                bundle.putString("salary", "");
                bundle.putString("qualification", qualification);
                bundle.putString("mode", jobMode);
                bundle.putString("jobcat", jobCat);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            else {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setupSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryHint("Search...");
    }

    private void loadJSON(final String param) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.SERVER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSON(param, response);
                        Log.e("response", "" + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(FilterActivity.this, "Request Time Out Please Try again later", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(FilterActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(FilterActivity.this, "Server Not Connected", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(FilterActivity.this, "Internet Not Available", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(FilterActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", param);
                return params;
            }
        };

        MySingleton.getInstance(FilterActivity.this).addToRequestQue(stringRequest);
    }

    private void showJSON(String param, String json) {
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        if (param.equals(SU.GETSKILLS)) {
            try {
                jsonObject = new JSONObject(json);
                jsonArray = jsonObject.getJSONArray("list");
                skillsList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    Item data = new Item();
                    data.setId(jo.getInt("id"));
                    data.setItem(jo.getString("skills"));
                    skillsList.add(data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (param.equals(SU.GETDISTRICT)) {
            try {
                jsonObject = new JSONObject(json);
                jsonArray = jsonObject.getJSONArray("list");
                locationList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    Item data = new Item();
                    data.setId(jo.getInt("id"));
                    data.setItem(jo.getString("dist"));
                    locationList.add(data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (param.equals(SU.GETJOBSCAT)) {
            try {
                jsonObject = new JSONObject(json);
                jsonArray = jsonObject.getJSONArray("list");
                jobCatList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    Item data = new Item();
                    data.setId(jo.getInt("id"));
                    data.setItem(jo.getString("job-cat"));
                    data.setCount(jo.getString("jobCount"));
                    jobCatList.add(data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (param.equals(SU.GETEXPERIENCE)) {
            try {
                jsonObject = new JSONObject(json);
                jsonArray = jsonObject.getJSONArray("list");
                experienceList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    Item data = new Item();
                    data.setId(jo.getInt("id"));
                    data.setItem(jo.getString("experience"));
                    experienceList.add(data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (param.equals(SU.GETQUALIFICATION)) {
            try {
                jsonObject = new JSONObject(json);
                jsonArray = jsonObject.getJSONArray("list");
                qualificationList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    Item data = new Item();
                    data.setId(jo.getInt("id"));
                    data.setItem(jo.getString("qualification"));
                    qualificationList.add(data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (param.equals(SU.GETWORKMODE)) {
            try {
                jsonObject = new JSONObject(json);
                jsonArray = jsonObject.getJSONArray("list");
                workmodeList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    Item data = new Item();
                    data.setId(jo.getInt("id"));
                    data.setItem(jo.getString("workmode"));
                    workmodeList.add(data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (param.equals(SU.GETCOURSE)) {
            try {
                jsonObject = new JSONObject(json);
                jsonArray = jsonObject.getJSONArray("list");
                courseList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    Item data = new Item();
                    data.setId(jo.getInt("id"));
                    data.setItem(jo.getString("course"));
                    courseList.add(data);
                }
                qualificationAdapter = new QualificationSelectionAdapter(FilterActivity.this, R.layout.filter_layout, courseList);
                filterList.setTextFilterEnabled(true);
                filterList.setAdapter(qualificationAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (param.equals(SU.GETJOBTYPE)) {
            try {
                jsonObject = new JSONObject(json);
                jsonArray = jsonObject.getJSONArray("list");
                jobmodeList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    Item data = new Item();
                    data.setId(jo.getInt("id"));
                    data.setItem(jo.getString("english"));
                    jobmodeList.add(data);
                }
                jobmodeAdapter = new JobModeSelectionAdapter(FilterActivity.this, R.layout.filter_layout, jobmodeList);
                filterList.setTextFilterEnabled(true);
                filterList.setAdapter(jobmodeAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadQualificationJSON(final String param, final String courseid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.SERVER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSON(param, response);
                        Log.e("response", "" + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(FilterActivity.this, "Request Time Out Please Try again later", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(FilterActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(FilterActivity.this, "Server Not Connected", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(FilterActivity.this, "Internet Not Available", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(FilterActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", param);
                params.put("qualification", courseid);
                return params;
            }
        };

        MySingleton.getInstance(FilterActivity.this).addToRequestQue(stringRequest);
    }

    public class SkillsSelectionAdapter extends ArrayAdapter {

        Context context;
        List<Item> list;
        LayoutInflater inflater;
        List<Item> orig;

        public SkillsSelectionAdapter(Context context, int resource, List<Item> list) {
            super(context, resource, list);
            this.context = context;
            this.list = list;
            orig = new ArrayList<Item>();
            orig.addAll(list);
        }

        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_list_item, null);

            TextView txt = view.findViewById(R.id.name);
            CheckBox checkBox = view.findViewById(R.id.checkbox);
            txt.setText(list.get(position).getItem());
            if (selectedSkills.contains("" + list.get(position).getId())) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedSkills.add("" + list.get(position).getId());
                    } else {
                        selectedSkills.remove("" + list.get(position).getId());
                    }
                }
            });
            return view;
        }

        public void filter(String charText) {

            charText = charText.toLowerCase(Locale.getDefault());
            list.clear();
            if (charText.length() == 0) {
                list.addAll(orig);
            } else {
                for (Item postDetail : orig) {
                    if (postDetail.getItem().toLowerCase(Locale.getDefault()).contains(charText)) {
                        list.add(postDetail);
                    } else if (postDetail.getItem().toLowerCase(Locale.getDefault()).contains(charText)) {
                        list.add(postDetail);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    public class LocationSelectionAdapter extends ArrayAdapter {

        Context context;
        List<Item> list;
        LayoutInflater inflater;
        List<Item> orig;

        public LocationSelectionAdapter(Context context, int resource, List<Item> list) {
            super(context, resource, list);
            this.context = context;
            this.list = list;
            orig = new ArrayList<Item>();
            orig.addAll(list);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_list_item, null);

            TextView txt = view.findViewById(R.id.name);
            CheckBox checkBox = view.findViewById(R.id.checkbox);
            txt.setText(list.get(position).getItem());
            if (selectedLocation.contains("" + list.get(position).getId())) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedLocation.add("" + list.get(position).getId());
                    } else {
                        selectedLocation.remove("" + list.get(position).getId());
                    }
                }
            });
            return view;
        }

        public void filter(String charText) {

            charText = charText.toLowerCase(Locale.getDefault());
            list.clear();
            if (charText.length() == 0) {
                list.addAll(orig);
            } else {
                for (Item postDetail : orig) {
                    if (charText.length() != 0 && postDetail.getItem().toLowerCase(Locale.getDefault()).contains(charText)) {
                        list.add(postDetail);
                    } else if (charText.length() != 0 && postDetail.getItem().toLowerCase(Locale.getDefault()).contains(charText)) {
                        list.add(postDetail);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    public class CategorySelectionAdapter extends ArrayAdapter {

        Context context;
        List<Item> list;
        LayoutInflater inflater;
        List<Item> orig;

        public CategorySelectionAdapter(Context context, int resource, List<Item> list) {
            super(context, resource, list);
            this.context = context;
            this.list = list;
            orig = new ArrayList<Item>();
            orig.addAll(list);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_list_item, null);

            TextView txt = view.findViewById(R.id.name);
            CheckBox checkBox = view.findViewById(R.id.checkbox);
            txt.setText(list.get(position).getItem());
            if (selectedCategory.contains("" + list.get(position).getId())) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedCategory.add("" + list.get(position).getId());
                    } else {
                        selectedCategory.remove("" + list.get(position).getId());
                    }
                }
            });
            return view;
        }

        public void filter(String charText) {

            charText = charText.toLowerCase(Locale.getDefault());
            list.clear();
            if (charText.length() == 0) {
                list.addAll(orig);
            } else {
                for (Item postDetail : orig) {
                    if (charText.length() != 0 && postDetail.getItem().toLowerCase(Locale.getDefault()).contains(charText)) {
                        list.add(postDetail);
                    } else if (charText.length() != 0 && postDetail.getItem().toLowerCase(Locale.getDefault()).contains(charText)) {
                        list.add(postDetail);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    public class QualificationSelectionAdapter extends ArrayAdapter {

        Context context;
        List<Item> list;
        LayoutInflater inflater;
        List<Item> orig;

        public QualificationSelectionAdapter(Context context, int resource, List<Item> list) {
            super(context, resource, list);
            this.context = context;
            this.list = list;
            orig = new ArrayList<Item>();
            orig.addAll(list);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_list_item, null);

            TextView txt = view.findViewById(R.id.name);
            CheckBox checkBox = view.findViewById(R.id.checkbox);
            txt.setText(list.get(position).getItem());
            if (selectedQualification.contains("" + list.get(position).getId())) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedQualification.add("" + list.get(position).getId());
                    } else {
                        selectedQualification.remove("" + list.get(position).getId());
                    }
                }
            });
            return view;
        }

        public void filter(String charText) {

            charText = charText.toLowerCase(Locale.getDefault());
            list.clear();
            if (charText.length() == 0) {
                list.addAll(orig);
            } else {
                for (Item postDetail : orig) {
                    if (charText.length() != 0 && postDetail.getItem().toLowerCase(Locale.getDefault()).contains(charText)) {
                        list.add(postDetail);
                    } else if (charText.length() != 0 && postDetail.getItem().toLowerCase(Locale.getDefault()).contains(charText)) {
                        list.add(postDetail);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    public class ExperienceSelectionAdapter extends ArrayAdapter {

        Context context;
        List<Item> list;
        LayoutInflater inflater;
        List<Item> orig;

        public ExperienceSelectionAdapter(Context context, int resource, List<Item> list) {
            super(context, resource, list);
            this.context = context;
            this.list = list;
            orig = new ArrayList<Item>();
            orig.addAll(list);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_list_item, null);

            TextView txt = view.findViewById(R.id.name);
            CheckBox checkBox = view.findViewById(R.id.checkbox);
            txt.setText(list.get(position).getItem());
            if (selectedExperience.contains("" + list.get(position).getId())) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedExperience.add("" + list.get(position).getId());
                    } else {
                        selectedExperience.remove("" + list.get(position).getId());
                    }
                }
            });
            return view;
        }

        public void filter(String charText) {

            charText = charText.toLowerCase(Locale.getDefault());
            list.clear();
            if (charText.length() == 0) {
                list.addAll(orig);
            } else {
                for (Item postDetail : orig) {
                    if (charText.length() != 0 && postDetail.getItem().toLowerCase(Locale.getDefault()).contains(charText)) {
                        list.add(postDetail);
                    } else if (charText.length() != 0 && postDetail.getItem().toLowerCase(Locale.getDefault()).contains(charText)) {
                        list.add(postDetail);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    public class WorkModeSelectionAdapter extends ArrayAdapter {

        Context context;
        List<Item> list;
        LayoutInflater inflater;
        List<Item> orig;

        public WorkModeSelectionAdapter(Context context, int resource, List<Item> list) {
            super(context, resource, list);
            this.context = context;
            this.list = list;
            orig = new ArrayList<Item>();
            orig.addAll(list);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_list_item, null);

            TextView txt = view.findViewById(R.id.name);
            CheckBox checkBox = view.findViewById(R.id.checkbox);
            txt.setText(list.get(position).getItem());
            if (selectedWorkmode.contains("" + list.get(position).getId())) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedWorkmode.add("" + list.get(position).getId());
                    } else {
                        selectedWorkmode.remove("" + list.get(position).getId());
                    }
                }
            });
            return view;
        }

        public void filter(String charText) {

            charText = charText.toLowerCase(Locale.getDefault());
            list.clear();
            if (charText.length() == 0) {
                list.addAll(orig);
            } else {
                for (Item postDetail : orig) {
                    if (charText.length() != 0 && postDetail.getItem().toLowerCase(Locale.getDefault()).contains(charText)) {
                        list.add(postDetail);
                    } else if (charText.length() != 0 && postDetail.getItem().toLowerCase(Locale.getDefault()).contains(charText)) {
                        list.add(postDetail);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    public class JobModeSelectionAdapter extends ArrayAdapter {

        Context context;
        List<Item> list;
        LayoutInflater inflater;
        List<Item> orig;

        public JobModeSelectionAdapter(Context context, int resource, List<Item> list) {
            super(context, resource, list);
            this.context = context;
            this.list = list;
            orig = new ArrayList<Item>();
            orig.addAll(list);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_list_item, null);

            TextView txt = view.findViewById(R.id.name);
            CheckBox checkBox = view.findViewById(R.id.checkbox);
            txt.setText(list.get(position).getItem());
            if (selectedJobmode.contains("" + list.get(position).getId())) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedJobmode.add("" + list.get(position).getId());
                    } else {
                        selectedJobmode.remove("" + list.get(position).getId());
                    }
                }
            });
            return view;
        }

        public void filter(String charText) {

            charText = charText.toLowerCase(Locale.getDefault());
            list.clear();
            if (charText.length() == 0) {
                list.addAll(orig);
            } else {
                for (Item postDetail : orig) {
                    if (charText.length() != 0 && postDetail.getItem().toLowerCase(Locale.getDefault()).contains(charText)) {
                        list.add(postDetail);
                    } else if (charText.length() != 0 && postDetail.getItem().toLowerCase(Locale.getDefault()).contains(charText)) {
                        list.add(postDetail);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    public class SpinnerAdapter extends BaseAdapter {

        Context context;
        List<Item> list;
        LayoutInflater inflater;

        public SpinnerAdapter(Context context, List<Item> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.spinner_item, null);
            TextView txt = view.findViewById(R.id.name);
            txt.setText(list.get(position).getItem());
            return view;
        }
    }

    public void showInfoDialog(){
        final Dialog dialog = new Dialog(FilterActivity.this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
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
                getResources().getString(R.string.filter_info) + "<br><br></font></b>";

        U.webview(FilterActivity.this,info,infoText);

        dialog.findViewById(R.id.text_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}












