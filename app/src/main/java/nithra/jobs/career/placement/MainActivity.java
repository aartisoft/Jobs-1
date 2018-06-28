package nithra.jobs.career.placement;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nithra.jobs.career.placement.activity.CategoryActivity;
import nithra.jobs.career.placement.activity.JobDetailActivity;
import nithra.jobs.career.placement.activity.MultipleNotificationActivity;
import nithra.jobs.career.placement.activity.NotificationActivity;
import nithra.jobs.career.placement.activity.OldPostActivity;
import nithra.jobs.career.placement.activity.RegistrationActivity;
import nithra.jobs.career.placement.activity.SearchActivity;
import nithra.jobs.career.placement.activity.SettingsActivity;
import nithra.jobs.career.placement.engine.LocalDB;
import nithra.jobs.career.placement.fragments.CategoryWiseFragment;
import nithra.jobs.career.placement.fragments.DistrictWiseFragment;
import nithra.jobs.career.placement.fragments.HomeFragment;
import nithra.jobs.career.placement.fragments.JobListFragment;
import nithra.jobs.career.placement.fragments.RecommendedFragment;
import nithra.jobs.career.placement.fragments.ReminderListFragment;
import nithra.jobs.career.placement.networking.MySingleton;
import nithra.jobs.career.placement.pojo.Jobs;
import nithra.jobs.career.placement.utills.CustomViewPager;
import nithra.jobs.career.placement.utills.L;
import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {

    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    View view;
    static CoordinatorLayout coordinatorLayout;

    static NativeContentAdView contadView = null;
    static NativeAppInstallAdView insadView = null;
    static FrameLayout adFrameLayout;
    public static ViewPagerAdapter adapter;

    ImageView notifyIcon, employerIcon, searchIcon, profileIcon;
    SharedPreference pref;
    LocalDB localDB;

    boolean contentAd = false, installAd = false;

    LinearLayout adLayout;
    public static InterstitialAd interstitialAd;

    // GCM
    SQLiteDatabase myDB;
    Bundle extras;

    Handler mHandler;
    Runnable mRunnable;

    Cursor notificationCursor = null;
    CallbackManager callbackManager;

    // Remote Config keys
    private static final String LOADING_PHRASE_CONFIG_AD_KEY = "adtype";
    private static final String LOADING_PHRASE_CONFIG_VACANCY_KEY = "JOB_VACANCY_TITLE";
    private static final String LOADING_PHRASE_CONFIG_POSTEDBY_KEY = "JOB_POSTED_BY";
    private static final String LOADING_PHRASE_CONFIG_PHONE_NUMBER_KEY = "JOB_PHONE_NO_ISSHOW";
    private static final String LOADING_PHRASE_CONFIG_DISTRICT_KEY = "district_isshow";

    int adRange = 1;
    TextView notifyCount, txtToolTitle;
    public static CustomViewPager viewPager;
    public static TabLayout tabLayout;
    public static int adPosition;
    BroadcastReceiver mRegistrationBroadcastReceiver;
    public static int homePagePosition = U.HOME_PAGE;
    Dialog backdia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        pref = new SharedPreference();
        localDB = new LocalDB(this);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, U.ADMOB_APP_ID);

        pref = new SharedPreference();
        adRange = pref.getInt(MainActivity.this, U.SH_REMOTE_AD);
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

        if (U.clr_chace(MainActivity.this)) {
            U.date_put(MainActivity.this, "clr_chace", 7);
            U.deleteCache(MainActivity.this);
        }

        setFBSDK();
        versionControl();
        setHomeActivity();

        if (pref.getInt(MainActivity.this, U.SH_APP_INFO_SHOW) == 0) {
            pref.putInt(MainActivity.this, U.SH_APP_INFO_SHOW, 1);
            showInfoDialog();
        } else {
            lastLogin();
        }
    }

    private void setFBSDK() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    private void versionControl() {
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;

        if (pref.getInt(this, U.SH_FIRST_INSTALL + versionCode) == 0) {
            pref.putInt(this, U.SH_VERSION_CODE, versionCode);
            pref.putString(this, U.SH_VERSION_NAME, versionName);
            pref.putInt(this, U.SH_FIRST_INSTALL + versionCode, 1);

          /*try {
                dbHelper.createDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
            dbHelper.close();
            dbHelper.openDataBase();
            dbHelper.close();*/
        }
    }

    private void setHomeActivity() {
        setContentView(R.layout.activity_main);

        try {
            remoteConfigForAd();
        } catch (Exception e) {
            System.out.println("remote config ad: error 1 : " + e.getMessage());
            pref.putInt(this, U.SH_REMOTE_AD, 2);
        }

        try {
            remoteConfigForVacancy();
        } catch (Exception e) {
            System.out.println("remote config Vacancy: error 1 : " + e.getMessage());
            pref.putInt(this, U.SH_REMOTE_VACANCY, 2);
        }

        try {
            remoteConfigForPostedby();
        } catch (Exception e) {
            System.out.println("remote config postedby: error 1 : " + e.getMessage());
            pref.putInt(this, U.SH_REMOTE_POSTEDBY, 2);
        }

        try {
            remoteConfigForPhoneNumber();
        } catch (Exception e) {
            System.out.println("remote config phoneNumber: error 1 : " + e.getMessage());
            pref.putInt(this, U.SH_REMOTE_PHONE_NUMBER, 2);
        }

        try {
            remoteConfigForDistrictWise();
        } catch (Exception e) {
            System.out.println("remote config DistrictWise: error 1 : " + e.getMessage());
            pref.putInt(this, U.SH_REMOTE_DISTRICT, 2);
        }

        adLayout = findViewById(R.id.adview);
        loadInDusAd();

        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(0);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        setupTabIcons();

        gcmControl();
        extraFunctions();
        setToolBar();
        loadBackPressInterstitial();
        gcmReceive();

        if (pref.getInt(MainActivity.this, U.SH_GUIDE_WINDOW_5) == 0) {
            final Dialog guideWindow = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
            guideWindow.setContentView(R.layout.guide_window_home);
            ImageView searchIcon = guideWindow.findViewById(R.id.search_icon);
            searchIcon.setVisibility(View.VISIBLE);
            ImageView profileIcon = guideWindow.findViewById(R.id.profile_icon);
            profileIcon.setVisibility(View.VISIBLE);
            TextView searchText = guideWindow.findViewById(R.id.search_text);
            searchText.setText(getResources().getString(R.string.guide_profile_icon));
            Button ok = guideWindow.findViewById(R.id.ok_btn);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    guideWindow.dismiss();
                }
            });
            guideWindow.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    guideWindow();
                }
            });
            guideWindow.show();
        }

        viewPager.setOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                TabLayout.Tab tab = tabLayout.getTabAt(position);
                if (tab != null) {
                    tab.select();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });

    }

    public void guideWindow() {
        final Dialog guideWindow = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        guideWindow.setContentView(R.layout.guide_window_home);
        ImageView searchIcon = guideWindow.findViewById(R.id.search_icon);
        searchIcon.setVisibility(View.GONE);
        ImageView profileIcon = guideWindow.findViewById(R.id.profile_icon);
        profileIcon.setVisibility(View.VISIBLE);
        TextView searchText = guideWindow.findViewById(R.id.search_text);
        searchText.setText(getResources().getString(R.string.guide_profile_search));
        Button ok = guideWindow.findViewById(R.id.ok_btn);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.putInt(MainActivity.this, U.SH_GUIDE_WINDOW_5, 1);
                guideWindow.dismiss();
            }
        });
        guideWindow.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                pref.putInt(MainActivity.this, U.SH_GUIDE_WINDOW_5, 1);
            }
        });
        guideWindow.show();
    }

    //---------------------------------- viewpager -------------------------------------------------

    private void setupTabIcons() {
        tabLayout.addTab(tabLayout.newTab().setText("முகப்பு"));
        tabLayout.addTab(tabLayout.newTab().setText("பரிந்துரைக்கப்படுபவை"));
        if (pref.getInt(MainActivity.this, U.SH_REMOTE_DISTRICT) == 1) {
            tabLayout.addTab(tabLayout.newTab().setText("மாவட்டங்கள்"));
        }
        tabLayout.addTab(tabLayout.newTab().setText("பிரிவுகள்"));
        tabLayout.addTab(tabLayout.newTab().setText("விருப்பமானவைகள்"));
        tabLayout.addTab(tabLayout.newTab().setText("நினைவூட்டல்"));
    }

    private void setupViewPager(CustomViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment("முகப்பு", HomeFragment.newInstance());
        adapter.addFragment("பரிந்துரைக்கப்படுபவை", RecommendedFragment.newInstance());
        if (pref.getInt(MainActivity.this, U.SH_REMOTE_DISTRICT) == 1) {
            adapter.addFragment("மாவட்டங்கள்", DistrictWiseFragment.newInstance());
        }
        adapter.addFragment("பிரிவுகள்", CategoryWiseFragment.newInstance());
        adapter.addFragment("விருப்பமானவைகள்", JobListFragment.newInstance(U.FJOBS, "", ""));
        adapter.addFragment("நினைவூட்டல்", ReminderListFragment.newInstance());
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<String> mFragmentTitleList = new ArrayList<>();
        private final List<Fragment> mFragmentList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mFragmentTitleList.size();
        }

        public void addFragment(String title, Fragment fragment) {
            mFragmentTitleList.add(title);
            mFragmentList.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    //------------------------------------ click events --------------------------------------------

    @Override
    public void onClick(View view) {

    }

    //------------------------------------- Instestirial Ad ----------------------------------------

    private void loadInDusAd() {
        //INDUS ADD
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(U.INDUS_AD_EXIT);
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
        // ---- END ---
    }

    public void showIndus() {
        interstitialAd.show();
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                thankYouMessage();
            }
        });
    }

    //----------------------------------- Notification Methods -------------------------------------

    private void updateNotificationCount() {
        notificationCursor = myDB.rawQuery("select * from noti_cal where isclose='0'", null);
        int notificationCount = notificationCursor.getCount();
        if (notificationCount == 0) {
            notifyCount.setVisibility(View.GONE);
        } else if (notificationCount > 9) {
            notifyCount.setText("9+");
        } else {
            notifyCount.setText("" + notificationCount);
        }
        invalidateOptionsMenu();
    }

    private void extraFunctions() {
        myDB = openOrCreateDatabase("myDB", 0, null);
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + "noti_cal"
                + " (id integer NOT NULL PRIMARY KEY AUTOINCREMENT,title VARCHAR,message VARCHAR,date VARCHAR,time VARCHAR,isclose INT(4),isshow INT(4) default 0,type VARCHAR," +
                "bm VARCHAR,ntype VARCHAR,url VARCHAR);");
    }

    //------------------------------------ Remote Config -------------------------------------------

    private void remoteConfigForAd() {
        final FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(false)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_ad);

        String adVal = "";
        try {
            adVal = mFirebaseRemoteConfig.getString(LOADING_PHRASE_CONFIG_AD_KEY);
        } catch (Exception e) {
            System.out.println("remote config : error 2 : " + e.getMessage());
            adVal = "1";
        }
        pref.putInt(this, U.SH_REMOTE_AD, Integer.parseInt(adVal));

        long cacheExpiration = 3600; // 1 hour in seconds.
        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
        // retrieve values from the service.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        final String finalAdVal = adVal;
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            System.out.println("remote config : success 1 : " + finalAdVal);
                            //  Toast.makeText(MainActivity.this, "Succeeded " + finalAdVal, Toast.LENGTH_SHORT).show();
                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            System.out.println("remote config : failed 1 : " + finalAdVal);
                            pref.putInt(getApplicationContext(), U.SH_REMOTE_AD, 1);
                            //  Toast.makeText(MainActivity.this, "Failed " + finalAdVal, Toast.LENGTH_SHORT).show();
                        }
                        //displayWelcomeMessage();
                    }
                });
    }

    private void remoteConfigForVacancy() {
        final FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_vacancy);

        String adVal = "";
        try {
            adVal = mFirebaseRemoteConfig.getString(LOADING_PHRASE_CONFIG_VACANCY_KEY);
        } catch (Exception e) {
            adVal = "1";
        }
        pref.putInt(this, U.SH_REMOTE_VACANCY, Integer.parseInt(adVal));

        long cacheExpiration = 3600; // 1 hour in seconds.
        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
        // retrieve values from the service.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        final String finalAdVal = adVal;
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(MainActivity.this, "Succeeded " + finalAdVal, Toast.LENGTH_SHORT).show();
                            pref.putInt(getApplicationContext(), U.SH_REMOTE_VACANCY, Integer.parseInt(finalAdVal));
                            mFirebaseRemoteConfig.activateFetched();
                            System.out.println("remoteconfig vacancy:" + finalAdVal);
                        } else {
                            pref.putInt(getApplicationContext(), U.SH_REMOTE_VACANCY, 1);
                            // Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void remoteConfigForPostedby() {
        final FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_postedby);

        String adVal = "";
        try {
            adVal = mFirebaseRemoteConfig.getString(LOADING_PHRASE_CONFIG_POSTEDBY_KEY);
        } catch (Exception e) {
            adVal = "1";
        }
        pref.putInt(this, U.SH_REMOTE_POSTEDBY, Integer.parseInt(adVal));

        long cacheExpiration = 3600; // 1 hour in seconds.
        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
        // retrieve values from the service.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        final String finalAdVal = adVal;
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(MainActivity.this, "Succeeded " + finalAdVal, Toast.LENGTH_SHORT).show();
                            pref.putInt(getApplicationContext(), U.SH_REMOTE_POSTEDBY, Integer.parseInt(finalAdVal));
                            mFirebaseRemoteConfig.activateFetched();
                            System.out.println("remoteconfig postedby:" + finalAdVal);
                        } else {
                            pref.putInt(getApplicationContext(), U.SH_REMOTE_POSTEDBY, 1);
                            // Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void remoteConfigForPhoneNumber() {
        final FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_phone_number);

        String adVal = "";
        try {
            adVal = mFirebaseRemoteConfig.getString(LOADING_PHRASE_CONFIG_PHONE_NUMBER_KEY);
        } catch (Exception e) {
            adVal = "1";
        }
        pref.putInt(this, U.SH_REMOTE_PHONE_NUMBER, Integer.parseInt(adVal));

        long cacheExpiration = 3600; // 1 hour in seconds.
        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
        // retrieve values from the service.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        final String finalAdVal = adVal;
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(MainActivity.this, "Succeeded " + finalAdVal, Toast.LENGTH_SHORT).show();
                            pref.putInt(getApplicationContext(), U.SH_REMOTE_PHONE_NUMBER, Integer.parseInt(finalAdVal));
                            mFirebaseRemoteConfig.activateFetched();
                            System.out.println("remoteconfig Phone_NUMBER:" + finalAdVal);
                        } else {
                            pref.putInt(getApplicationContext(), U.SH_REMOTE_PHONE_NUMBER, 1);
                            // Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void remoteConfigForDistrictWise() {
        final FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_district);

        String adVal = "";
        try {
            adVal = mFirebaseRemoteConfig.getString(LOADING_PHRASE_CONFIG_DISTRICT_KEY);
        } catch (Exception e) {
            adVal = "1";
        }
        pref.putInt(this, U.SH_REMOTE_DISTRICT, Integer.parseInt(adVal));

        long cacheExpiration = 3600; // 1 hour in seconds.
        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
        // retrieve values from the service.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        final String finalAdVal = adVal;
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(MainActivity.this, "Succeeded " + finalAdVal, Toast.LENGTH_SHORT).show();
                            pref.putInt(getApplicationContext(), U.SH_REMOTE_DISTRICT, Integer.parseInt(finalAdVal));
                            mFirebaseRemoteConfig.activateFetched();
                            System.out.println("remoteconfig DISTRICT_WISE:" + finalAdVal);
                        } else {
                            pref.putInt(getApplicationContext(), U.SH_REMOTE_DISTRICT, 1);
                            // Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //------------------------------------ Toolbar Menu Fun ----------------------------------------

    private void setToolBar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        txtToolTitle = findViewById(R.id.txtToolTitle);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        employerIcon = findViewById(R.id.action_employer);
        searchIcon = findViewById(R.id.action_search);
        notifyCount = findViewById(R.id.notifications_text);
        profileIcon = findViewById(R.id.action_profile);
        notifyIcon = findViewById(R.id.action_notifications);

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setProfile();

        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (U.isNetworkAvailable(MainActivity.this)) {
                    Intent localIntent = new Intent(MainActivity.this, RegistrationActivity.class);
                    localIntent.putExtra("task", "Edit");
                    startActivity(localIntent);
                } else {
                    Toast.makeText(MainActivity.this, U.INA, Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });

        employerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employerInfo();
            }
        });

        notifyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor cv1 = myDB.rawQuery("select * from noti_cal", null);
                cv1.moveToFirst();

                if (cv1.getCount() == 0) {
                    Intent intent2 = new Intent(MainActivity.this, NotificationActivity.class);
                    startActivity(intent2);
                } else {
                    Intent intent2 = new Intent(MainActivity.this, NotificationActivity.class);
                    startActivity(intent2);
                }

            }
        });
    }

    private void employerInfo() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.employer_info);

        String firstText = "உங்கள் நிறுவனத்திற்கு ஆட்கள் தேவை எனில் இந்த அப்ளிகேஷனில் உங்கள் வேலைவாய்ப்பு தகவலைப் பதிவிட எங்களது அலைபேசி எண் : <u><font color='red'>9942267855</font></u> -க்கு\n" +
                "தொடர்பு கொள்ளவும்";

        TextView txtInfo = dialog.findViewById(R.id.txtInfo);
        String text = "<b>" + firstText + "</b>";
        txtInfo.setText(Html.fromHtml(text));
        final String call = "9942267855";

        txtInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCall(call);
            }
        });

        if (!isFinishing()) {
            dialog.show();
        }

        dialog.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void makeCall(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_VIEW);
        callIntent.setData(Uri.parse("tel:" + phone));
        startActivity(callIntent);
    }

    //------------------------------------  Navigation Menu Fun ------------------------------------

    private void setProfile() {
        String profileStatus = "";
        View navHeaderView = navigationView.getHeaderView(0);
        TextView txtprofile = navHeaderView.findViewById(R.id.txtProfile);
        LinearLayout profileLink = navHeaderView.findViewById(R.id.profile_link);
        TextView txtApp = navHeaderView.findViewById(R.id.txtApp);
        txtApp.setText("Version : " + pref.getString(this, U.SH_VERSION_NAME) + " FCM : " + pref.getInt(this, "isvalid"));
        if (pref.getBoolean(MainActivity.this, U.SH_SIGN_UP_SUCCESS) && pref.getBoolean(MainActivity.this, U.SH_OTP_SUCCESS)) {
            profileStatus = getResources().getString(R.string.view_profile);
        } else if (pref.getBoolean(MainActivity.this, U.SH_BLOCKED_USER)) {
            profileStatus = getResources().getString(R.string.blocked);
        } else if (pref.getBoolean(MainActivity.this, U.SH_OTP_SUCCESS)) {
            profileStatus = getResources().getString(R.string.complete_registration);
        } else if (pref.getString(MainActivity.this, U.SH_MOBILE).equals("")) {
            profileStatus = getResources().getString(R.string.signup);
        } else if (!pref.getBoolean(MainActivity.this, U.SH_OTP_SUCCESS)) {
//            profileStatus = getResources().getString(R.string.otp_pending);
            profileStatus = getResources().getString(R.string.signup);
        }

        txtprofile.setText(profileStatus);

        profileLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (U.isNetworkAvailable(MainActivity.this)) {
                    Intent localIntent = new Intent(MainActivity.this, RegistrationActivity.class);
                    localIntent.putExtra("task", "Edit");
                    startActivity(localIntent);
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    Toast.makeText(MainActivity.this, U.INA, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            //home
            txtToolTitle.setText(R.string.app_name);
            viewPager.setCurrentItem(0);
        } else if (id == R.id.nav_old_post) {
            if (U.isNetworkAvailable(MainActivity.this)) {
                startActivity(new Intent(MainActivity.this, OldPostActivity.class));
            } else {
                Toast.makeText(this, U.INA, Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_exam_details) {
            startActivity(new Intent(MainActivity.this, CategoryActivity.class));//OldPostActivity
        } else if (id == R.id.nav_employer) {
            employerInfo();
        } else if (id == R.id.nav_user_post) {
            if (U.isNetworkAvailable(MainActivity.this)) {
                userPost();
            } else {
                Toast.makeText(this, U.INA, Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_search_id) {
            final Dialog searchDia = new Dialog(this, R.style.Base_Theme_AppCompat_Dialog_Alert);
            searchDia.setContentView(R.layout.search_byid);
            searchDia.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            final EditText userInput = searchDia.findViewById(R.id.user_input);
            searchDia.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    searchDia.dismiss();
                }
            });
            searchDia.findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (userInput.getText().toString().equals("")) {
                        userInput.setError("Enter JobID");
                    } else {
                        callDetail(userInput.getText().toString(), U.SEARCHID);
                        searchDia.dismiss();
                    }
                }
            });
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(userInput.getWindowToken(), 0);
            searchDia.show();
        }

        // others
        else if (id == R.id.nav_notification) {
            startActivity(new Intent(this, NotificationActivity.class));
        } else if (id == R.id.nav_tc) {
            showPrivacy("file:///android_asset/tc.html");
        } else if (id == R.id.nav_rate) {
            // rating
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(U.RATEUS)));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        } else if (id == R.id.nav_feed_back) {
            if (U.isNetworkAvailable(this)) showFeedBack();
            else L.t(this, U.INA);
        } else if (id == R.id.nav_share) {
            //share app
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT,
                    "நித்ரா வேலைவாய்ப்பு செயலி : " + U.UTM_SOURCE + "\n\n" +
                            "மத்திய, மாநில மற்றும் தனியார் வேலைகள் என அனைத்து வகையான வேலைவாய்ப்பு தகவல்களையும்  தனித்தனி பிரிவுகளாக கொண்ட ஒரே செயலி. உங்களது கல்வி, திறன் மற்றும் விருப்பமான இடம் ஆகியவற்றை கொண்டு உங்களுக்கான வேலைவாய்ப்பு தகவல்களை எளிதில் தெரிந்து கொள்ளுவதற்கு உதவும்  பரிந்துரைக்கப்பட்ட பகுதி.\n" +
                            "\n" +
                            "மாவட்டம் வாரியாக உங்களது ஊரில் உள்ள அனைத்து வகையான வேலைவாய்ப்புகளையும் தெரிந்து கொள்ளும் சிறப்பு வசதி. தினந்தோறும் பல புதிய வேலைவாய்ப்பு தகவல்களை உடனுக்குடன் அளிக்கும் சிறந்த செயலி. இனி வேலையை தேடி நீங்கள் செல்ல வேண்டாம் , வேலை உங்களை தேடி உங்களது கையில் எங்கள் நித்ரா வேலைவாய்ப்பு செயலி வழியாக…\n" +
                            "\n" +
                            "உடனே இலவசமாக பதிவிறக்கம் செய்ய இங்கே கிளிக் செய்யுங்கள்.." +
                            "\n" + U.UTM_SOURCE);
            startActivity(Intent.createChooser(intent, "Share"));

        } else if (id == R.id.nav_myresume_aps) {
            if (U.appInstalledOrNot("com.nithra.resume", MainActivity.this)) {
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.nithra.resume");
                startActivity(intent);
            } else {
                String url = "https://play.google.com/store/apps/details?id=com.nithra.resume&referrer=utm_source%3DVia_Jobs_App_Share";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        } else if (id == R.id.nav_our_aps) {
            //our apps
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(U.OUR_APPS)));
        } else if (id == R.id.nav_privacy) {
            callPrivacy();
        } else if (id == R.id.nav_exit) {
            if (U.isNetworkAvailable(MainActivity.this)) {
                if (interstitialAd.isLoaded()) {
                    showIndus();
                } else {
                    thankYouMessage();
                }
            } else {
                thankYouMessage();
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showRateUs() {
        pref.putInt(this, U.SH_RATE_US, 1);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rate_us);

        TextView txtRateUs, txtFeedBack;
        txtRateUs = dialog.findViewById(R.id.txtRateUs);
        txtFeedBack = dialog.findViewById(R.id.txtFeedBack);

        ImageView imgRating = dialog.findViewById(R.id.imgRating);

        imgRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(U.RATEUS)));
                dialog.dismiss();
            }
        });

        txtRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(U.RATEUS)));
                dialog.dismiss();
            }
        });

        txtFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                showFeedBack();
            }
        });

        if (!isFinishing()) {
            dialog.show();
        }
    }

    private void showFeedBack() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.feed_back);

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        final EditText edFeed, edEmail, edphone;

        TextView txtCancel, txtSend, txtTitle, txtPrivacy;
        txtPrivacy = dialog.findViewById(R.id.txtPrivacy);
        txtPrivacy.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        txtPrivacy.setText(getString(R.string.privacy_policy) + "\nPrivacy Policy");
        txtPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPrivacy();
            }
        });

        txtTitle = dialog.findViewById(R.id.txtTitle);
        txtCancel = dialog.findViewById(R.id.txtCancel);
        txtSend = dialog.findViewById(R.id.txtSend);

        edEmail = dialog.findViewById(R.id.edEmail);
        edFeed = dialog.findViewById(R.id.edFeed);
        edphone = dialog.findViewById(R.id.edphone);

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        txtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String feed = "", email = "", phone = "";
                email = edEmail.getText().toString();
                phone = edphone.getText().toString();

                try {
                    feed = URLEncoder.encode(edFeed.getText().toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    feed = "";
                }

                if (feed.length() != 0) {
                    if (!phone.isEmpty()) {
                        feed = feed + "   PhnNO: " + phone;
                    }
                    if (U.isNetworkAvailable(getApplicationContext())) {
                        sendFeed(email, feed);
                        dialog.dismiss();
                    } else L.t(getApplicationContext(), U.INA);
                } else {
                    L.t(getApplicationContext(), getResources().getString(R.string.type));
                }

            }
        });

        if (!isFinishing()) {
            dialog.show();
        }
    }

    private void callPrivacy() {
        if (U.isNetworkAvailable(this)) {
            showPrivacy(SU.PRIVACY);
        } else L.t(this, U.INA);
    }

    private void sendFeed(final String email, final String feed) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.FEEDBACKURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        U.toast_center(getApplicationContext(), "" + getResources().getString(R.string.content_saved));

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        L.t(MainActivity.this, error.getMessage());
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("feedback", feed);
                params.put("email", email);
                params.put("vcode", U.getVersion(getApplicationContext()).versionCode + "");
                params.put("model", U.getDeviceName());
                params.put("type", type);

                return params;
            }
        };
        MySingleton.getInstance(MainActivity.this).addToRequestQue(stringRequest);
    }

    private void sendJob(final String name, final String details) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.GETDATAURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("sendJob", "" + response);
                        try {
                            JSONArray jArray = new JSONArray(response);
                            JSONObject jsonObject = jArray.getJSONObject(0);
                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {
                                U.toast_center(getApplicationContext(), "" + getResources().getString(R.string.user_jobs_test));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            L.t(MainActivity.this, U.INA);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        L.t(MainActivity.this, error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                String type = "";
                try {
                    type = URLEncoder.encode(details, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", SU.USER_JOB_POST);
                params.put("uid", U.getAndroidId(MainActivity.this));
                params.put("employee_id", pref.getString(MainActivity.this, U.SH_EMPLOYEE_ID));
                params.put("name", name);
                params.put("email", pref.getString(MainActivity.this, U.SH_EMAIL));
                params.put("phone_number", pref.getString(MainActivity.this, U.SH_MOBILE));
                params.put("message", type);
                return params;
            }
        };
        MySingleton.getInstance(MainActivity.this).addToRequestQue(stringRequest);
    }

    private void showPrivacy(String url) {

        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.privacy);

        final ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
        final WebView webView = dialog.findViewById(R.id.webView);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                }
            }
        });
        webView.loadUrl(url);
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
        if (!isFinishing()) {
            dialog.show();
        }
    }

    private void userPost() {
        final Dialog report_dialog = new Dialog(MainActivity.this);
        report_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        report_dialog.setContentView(R.layout.user_post_lay);
        report_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView reportTxt = report_dialog.findViewById(R.id.report_txt);
        reportTxt.setText("" + getResources().getString(R.string.post_job));
        final EditText edname = report_dialog.findViewById(R.id.name);
        edname.setHint("" + getResources().getString(R.string.name));
        final EditText edreport = report_dialog.findViewById(R.id.report);
        edreport.setHint("" + getResources().getString(R.string.user_post_hint));
        Button submit = report_dialog.findViewById(R.id.send);
        ImageView closeBtn = report_dialog.findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report_dialog.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = edname.getText().toString();
                String comment = edreport.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(MainActivity.this, "" + getResources().getString(R.string.nametype), Toast.LENGTH_SHORT).show();
                } else if (comment.equals("")) {
                    Toast.makeText(MainActivity.this, "" + getResources().getString(R.string.user_post_error), Toast.LENGTH_SHORT).show();
                } else {
                    sendJob(name, comment);
                    report_dialog.dismiss();
                }
            }
        });
        if (!isFinishing()) {
            report_dialog.show();
        }
    }

    //----------------------------------- Activity Results -----------------------------------------

    @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    //---------------------------------- static methods --------------------------------------------

    public static void showAd(Context context, final LinearLayout layout, final Boolean flag) {
        System.out.println("Layout called .....");
        AdView adView = new AdView(context);
        adView.setAdUnitId(U.BANNER_AD);
        adView.setAdSize(AdSize.SMART_BANNER);

        try {
            layout.addView(adView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                try {
                    System.out.println("layout visible ... ");
                    if (flag) {
                        layout.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);
    }

    //---------------------------------- Last login update -----------------------------------------

    public void lastLogin() {
        if (pref.getString(MainActivity.this, U.SH_LAST_LOGIN_DATE).equals(U.currentDate())) {
            callRegisterDia();
        } else {
            loadJSON(U.currentDate());
        }
    }

    private void loadJSON(final String cDate) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.GETDATAURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSON(cDate, response);
                        Log.e("showresponse", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            L.t(MainActivity.this, "Request Time Out Please Try again later");
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(MainActivity.this, "Server Not Connected", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(MainActivity.this, "Internet Not Available", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(MainActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "last_login");
                params.put("eid", pref.getString(MainActivity.this, U.SH_EMPLOYEE_ID));
                params.put("androidid", U.getAndroidId(MainActivity.this));
                params.put("vcode", "" + U.versioncode_get(MainActivity.this));
                return params;
            }
        };

        MySingleton.getInstance(MainActivity.this).addToRequestQue(stringRequest);
    }

    private void showJSON(String cDate, String json) {

        Log.e("showresponse_vcode", "" + json);
        pref.putString(MainActivity.this, U.SH_LAST_LOGIN_DATE, "" + cDate);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            int vcode = jsonObject.getInt("v_code");
            Log.e("showresponse_vcode", "" + vcode);
            if (vcode > U.versioncode_get(MainActivity.this)) {

                callUpdateDia(jsonObject.getString("description"));

            } else {

                callRegisterDia();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void callRegisterDia() {
        if (!pref.getBoolean(MainActivity.this, U.SH_SIGN_UP_SUCCESS)) {

            if (pref.getInt(MainActivity.this, U.SH_REGISTER_DIA_SHOW) == 0) {

                pref.putInt(MainActivity.this, U.SH_REGISTER_DIA_SHOW, 1);
                pref.putString(MainActivity.this, U.SH_ASK_REGISTER_DATE, U.addDaysInCal(3));
                Log.e("REGISTER_DATE", "" + U.addDaysInCal(3));

            } else if (pref.getInt(MainActivity.this, U.SH_REGISTER_DIA_SHOW) == 1) {
                Date date1 = U.convertStringtoDate(pref.getString(MainActivity.this,
                        U.SH_ASK_REGISTER_DATE), U.DATE_FORMAT);
                Date date2 = U.convertStringtoDate(U.currentDate(), U.DATE_FORMAT);
                if (date2.equals(date1) || date2.after(date1)) {
                    askToRegister();
                }
            }
        }
    }

    public void callUpdateDia(String msg) {
        if (pref.getInt(MainActivity.this, U.SH_UPDATE_DIA_SHOW) == 0) {
            askToUpdate(msg);
        } else if (pref.getInt(MainActivity.this, U.SH_UPDATE_DIA_SHOW) == 1) {
            Date date1 = U.convertStringtoDate(pref.getString(MainActivity.this,
                    U.SH_ASK_UPDATE_DATE), U.DATE_FORMAT);
            Date date2 = U.convertStringtoDate(U.currentDate(), U.DATE_FORMAT);
            if (date2.equals(date1) || date2.after(date1)) {
                askToUpdate(msg);
            }
        }
    }

    private void askToRegister() {
        final Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.ask_register_dia);
        dialog.setCancelable(false);

        TextView no = dialog.findViewById(R.id.never);
        Button ok = dialog.findViewById(R.id.text_ok);
        WebView infoText = dialog.findViewById(R.id.info_text);
        infoText.setBackgroundColor(Color.TRANSPARENT);

        infoText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        String info = "<b><meta charset=\"utf-8\"><font color=white size=2>\n" +
                getResources().getString(R.string.rjobs_intro) + "<br><br></font></b>";

        U.webview(MainActivity.this, info, infoText);

        dialog.findViewById(R.id.text_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (U.isNetworkAvailable(MainActivity.this)) {
                    pref.putInt(MainActivity.this, U.SH_RECOMMENDED_DIA_SHOW, 1);
                    Intent localIntent = new Intent(MainActivity.this, RegistrationActivity.class);
                    localIntent.putExtra("task", "Edit");
                    startActivity(localIntent);
                    dialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, U.INA, Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                pref.putInt(MainActivity.this, U.SH_REGISTER_DIA_SHOW, 1);
                pref.putString(MainActivity.this, U.SH_ASK_REGISTER_DATE, U.addDaysInCal(3));
                Log.e("REGISTER_DATE", "" + U.addDaysInCal(3));
            }
        });
        if (!isFinishing()) {
            dialog.show();
        }
    }

    private void askToUpdate(String description) {
        final Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.alert_update_lay);
        Button later = dialog.findViewById(R.id.later);
        WebView infoText = dialog.findViewById(R.id.info_text);
        Button update = dialog.findViewById(R.id.updateBtn);
        ImageView closeBtn = dialog.findViewById(R.id.close_btn);

        String info = "<b><meta charset=\"utf-8\"><font color=black size=3>\n" +
                description + "<br><br></font></b>";

        U.webview(MainActivity.this, info, infoText);

        infoText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(U.RATEUS)));
                dialog.dismiss();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                pref.putString(MainActivity.this, U.SH_ASK_UPDATE_DATE, U.addDaysInCal(7));
                Log.e("UPDATE_DATE", "" + U.addDaysInCal(7));
                pref.putInt(MainActivity.this, U.SH_UPDATE_DIA_SHOW, 1);
            }
        });
        if (!isFinishing()) {
            dialog.show();
        }
    }

    public void showInfoDialog() {
        final Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.info_app);
        WebView infoText = dialog.findViewById(R.id.info_text);
        FloatingActionButton ok = dialog.findViewById(R.id.text_ok);
        infoText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        infoText.setBackgroundColor(Color.TRANSPARENT);

        infoText.loadUrl("file:///android_asset/welcome.html");

        ok.setImageBitmap(textAsBitmap(getResources().getString(R.string.ok), getResources().getDimension(R.dimen.txt_25), Color.BLACK));

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastLogin();
                dialog.dismiss();
            }
        });

        if (!isFinishing()) {
            dialog.show();
        }
    }

    public void showRegisterDialog() {
        final Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.first_register_lay);
        Button ok = dialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent localIntent = new Intent(MainActivity.this, RegistrationActivity.class);
                localIntent.putExtra("task", "Edit");
                startActivity(localIntent);
                dialog.dismiss();
            }
        });

        if (!isFinishing()) {
            dialog.show();
        }
    }

    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        Typeface currentTypeFace = paint.getTypeface();
        Typeface bold = Typeface.create(currentTypeFace, Typeface.BOLD);
        paint.setTypeface(bold);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    // -------------------------------------- GCM --------------------------------------------------

    private void gcmControl() {
        pref.putString(getApplicationContext(), U.SH_ANDROID_ID, U.getAndroidId(MainActivity.this));
        gcmreg_fun();
    }

    public void gcmReceive() {
        Bundle extras;
        extras = getIntent().getExtras();
        if (extras != null) {
            String type = extras.getString("type");
            String message = extras.getString("message");
            String title = extras.getString("title");
            String Notifyid = "" + extras.getInt("idd", 0);

            if (type != null) {
                switch (type) {
                    case "h":
                        break;
                    case "rj":
                        callDetail(message, U.GCM);
                        break;
                    case "s":
                        if (message != null) {
                            if (message.contains(",")) {
                                Intent notiIntent = new Intent(MainActivity.this, MultipleNotificationActivity.class);
                                notiIntent.putExtra("type", type);
                                notiIntent.putExtra("jobids", message);
                                notiIntent.putExtra("title", title);
                                notiIntent.putExtra("Notifyid", Notifyid);
                                startActivity(notiIntent);
                            } else {
                                Intent resultIntent = new Intent(MainActivity.this, JobDetailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("point", 0);
                                bundle.putString("task", U.GCM);
                                bundle.putString("key", message);
                                bundle.putString("Notifyid", Notifyid);
                                bundle.putBoolean("gcm", true);
                                resultIntent.putExtras(bundle);
                                startActivity(resultIntent);
                            }
                        }
                        break;
                    case "cj":
                        if (message != null) {
                            switch (message) {
                                case "homePrivate":
                                    callMultipleNoti(type, message, title, Notifyid);
                                    break;
                                case "homeState":
                                    callMultipleNoti(type, message, title, Notifyid);
                                    break;
                                case "homeCentral":
                                    callMultipleNoti(type, message, title, Notifyid);
                                    break;
                                case "homeConsultancy":
                                    callMultipleNoti(type, message, title, Notifyid);
                                    break;
                                case "homeAlljobs":
                                    callMultipleNoti(type, message, title, Notifyid);
                                    break;
                                case "homeRjobs":
                                    setPage(U.RECOMMENDED_PAGE);
                                    break;
                                case "homeDistrict":
                                    setPage(U.DISTRICT_PAGE);
                                    break;
                                case "homeCategory":
                                    setPage(U.CATEGORY_PAGE);
                                    break;
                                case "homeGovtExams":
                                    startActivity(new Intent(MainActivity.this, CategoryActivity.class));
                                    break;
                                case "homeOldpost":
                                    if (U.isNetworkAvailable(MainActivity.this)) {
                                        startActivity(new Intent(MainActivity.this, OldPostActivity.class));
                                    } else {
                                        Toast.makeText(this, U.INA, Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                            }
                        }
                        break;
                    case "fj":
                        if (message != null) {
                            callMultipleNoti(type, message, title, Notifyid);
                        }
                        break;
                    case "dj":
                        if (message != null) {
                            callMultipleNoti(type, message, title, Notifyid);
                        }
                        break;
                }
            }
        }
    }

    public void gcmreg_fun() {
        if (U.isNetworkAvailable(MainActivity.this)) {
            if (pref.getInt(getApplicationContext(), "notcunt") == 0) {

                smallestWidth();

                mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                            FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                        }
                    }
                };

                if (pref.getInt(MainActivity.this, "isvalid") == 0) {
                    if (pref.getString(this, "token").length() > 0) {
                        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                        new gcmpost_update2().execute(refreshedToken);
                    }

                } else {
                    if (pref.getInt(MainActivity.this, "fcm_update") < U.versioncode_get(MainActivity.this)) {
                        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                        new gcmpost_update1().execute(refreshedToken);
                    }
                }
                pref.putInt(getApplicationContext(), "vcode", BuildConfig.VERSION_CODE);
                pref.putInt(getApplicationContext(), "notcunt", 1);

            }

        }
    }

    public void smallestWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        float scaleFactor = metrics.density;

        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;

        float smallestWidth = Math.min(widthDp, heightDp);

        System.out.println("Width Pixels : " + widthPixels);
        System.out.println("Height Pixels : " + heightPixels);
        System.out.println("Dots per inch : " + metrics.densityDpi);
        System.out.println("Scale Factor : " + scaleFactor);
        System.out.println("Smallest Width : " + smallestWidth);

        pref.putString(getApplicationContext(), "smallestWidth", smallestWidth + "");
        pref.putString(getApplicationContext(), "widthPixels", widthPixels + "");
        pref.putString(getApplicationContext(), "heightPixels", heightPixels + "");
        pref.putString(getApplicationContext(), "density", metrics.densityDpi + "");

    }

    @SuppressLint("StaticFieldLeak")
    private class gcmpost_update1 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strr) {
            ServerUtilities.gcmupdate(MainActivity.this, U.versionname_get(MainActivity.this), U.versioncode_get(MainActivity.this), strr[0]);
            return "";
        }

        @Override
        protected void onPostExecute(String onlineVersions) {
            super.onPostExecute(onlineVersions);
            SharedPreference sharedPreference = new SharedPreference();
            sharedPreference.putInt(MainActivity.this, "fcm_update", U.versioncode_get(MainActivity.this));
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class gcmpost_update2 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strr) {
            ServerUtilities.gcmpost(strr[0], U.getAndroidId(MainActivity.this), U.versionname_get(MainActivity.this),
                    U.versioncode_get(MainActivity.this), MainActivity.this);
            return "";
        }

        @Override
        protected void onPostExecute(String onlineVersions) {
            super.onPostExecute(onlineVersions);
            SharedPreference sharedPreference = new SharedPreference();
            sharedPreference.putInt(MainActivity.this, "fcm_update", U.versioncode_get(MainActivity.this));
        }
    }

    //---------------------------------------- common Methods --------------------------------------

    public void setPage(final int no) {
        viewPager.setCurrentItem(no);
        tabLayout.setScrollX(tabLayout.getWidth());
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        TabLayout.Tab tab = tabLayout.getTabAt(no);
                        if (tab != null) {
                            tab.select();
                        }
                    }
                }, 100);
    }

    public void thankYouMessage() {
        setStatusBarTranslucent(true);
        final Dialog exitDia = new Dialog(MainActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        exitDia.setContentView(R.layout.thankyou);
        exitDia.setCancelable(false);
        if (!isFinishing()) {
            exitDia.show();
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                exitDia.dismiss();
                finish();
            }
        }, 2000);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (drawer != null) {
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else if (MainActivity.homePagePosition == U.CATEGORY_PAGE) {
                        if (CategoryWiseFragment.fragHolder.getVisibility() == View.VISIBLE) {
                            CategoryWiseFragment.fragHolder.setVisibility(View.GONE);
                            CategoryWiseFragment.fblLay.setVisibility(View.VISIBLE);
                            CategoryWiseFragment.ads.setVisibility(View.VISIBLE);
                        } else {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                        }
                    } else if (MainActivity.homePagePosition == U.DISTRICT_PAGE) {
                        if (DistrictWiseFragment.fragHolder.getVisibility() == View.VISIBLE) {
                            DistrictWiseFragment.fragHolder.setVisibility(View.GONE);
                            DistrictWiseFragment.fblLay.setVisibility(View.VISIBLE);
                            DistrictWiseFragment.ads.setVisibility(View.VISIBLE);
                        } else {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                        }
                    } else if (MainActivity.homePagePosition == U.RECOMMENDED_PAGE) {
                        if (RecommendedFragment.fragHolder.getVisibility() == View.VISIBLE) {
                            RecommendedFragment.fragHolder.setVisibility(View.GONE);
                            RecommendedFragment.frontLay.setVisibility(View.VISIBLE);
                            RecommendedFragment.ads.setVisibility(View.VISIBLE);
                        } else {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                        }
                    } else if (MainActivity.homePagePosition == U.HOME_PAGE) {
                        if (HomeFragment.fragHolder.getVisibility() == View.VISIBLE) {
                            HomeFragment.fragHolder.setVisibility(View.GONE);
                            HomeFragment.frontLay.setVisibility(View.VISIBLE);
                            HomeFragment.ads.setVisibility(View.VISIBLE);
                        } else if (pref.getInt(getApplicationContext(), U.SH_RATE_US) == 0) {
                            showRateUs();
                        } else {
                            backdia.show();
                        }
                    } else {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                    }
                }

                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
        // unregisterReceiver(mHandleMessageReceiverdic);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setProfile();
        updateNotificationCount();
        if (pref.getInt(MainActivity.this, U.SH_RJOBS_FLAG) == 1) {
            pref.putInt(MainActivity.this, U.SH_RJOBS_FLAG, 0);
            setupViewPager(viewPager);
            viewPager.setCurrentItem(U.RECOMMENDED_PAGE);
            TabLayout.Tab tab = tabLayout.getTabAt(U.RECOMMENDED_PAGE);
            tab.select();
        }

        Intent in = getIntent();
        Uri data = in.getData();
        Log.e("Deep_Data", "" + data);
        if (data != null) {
            String v = data.toString().substring(data.toString().indexOf("id=") + 3);
            if (v.length() != 0) {
                Log.e("Deep_Data----", "" + v);
                setHomeActivity();
                callDetail(v, U.DEEPLINK);
                data = null;
                // clear intent
                Intent intent = getIntent();
                intent.replaceExtras(new Bundle());
                intent.setAction("");
                intent.setData(data);
                intent.setFlags(0);
            }
        }
    }

    private void callDetail(String jobid, String task) {
        Intent resultIntent = new Intent(MainActivity.this, JobDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("point", 0);
        bundle.putString("task", task);
        bundle.putString("key", jobid);
        bundle.putBoolean("gcm", true);
        if (task.equals(U.DEEPLINK)) {
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        resultIntent.putExtras(bundle);
        startActivity(resultIntent);
    }

    public void callMultipleNoti(String type, String jobid, String title, String Notifyid) {
        Intent notiIntent = new Intent(MainActivity.this, MultipleNotificationActivity.class);
        notiIntent.putExtra("type", type);
        notiIntent.putExtra("jobids", jobid);
        notiIntent.putExtra("title", title);
        notiIntent.putExtra("Notifyid", "" + Notifyid);
        startActivity(notiIntent);
    }

    //------------------------- adv native static ad -----------------------------------------------

    public static void refreshAd(final Context context, boolean requestAppInstallAds, boolean requestContentAds) {
        if (!requestAppInstallAds && !requestContentAds) {
            System.out.println("Ad error : " + "At least one ad format must be checked to request an ad.");
            return;
        }

        //  mRefresh.setEnabled(false);

        AdLoader.Builder builder = new AdLoader.Builder(context, U.ADMOB_AD_UNIT_ID);

        if (requestAppInstallAds) {
            try {
                builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
                    @Override
                    public void onAppInstallAdLoaded(NativeAppInstallAd ad) {
                        LayoutInflater inflater = null;
                        View view = null;
//                        NativeAppInstallAdView adView = null;
                        try {
                            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        } catch (Exception e) {

                        }
                        if (inflater == null) insadView = null;
                        else {
                            view = inflater.inflate(R.layout.ad_app_install, null);
                            insadView = (NativeAppInstallAdView) view;
                            populateAppInstallAdViewFlag(ad, insadView);
                        }
//                        frameLayout.removeAllViews();
//                        if (insadView != null) frameLayout.addView(insadView);
//                        else frameLayout.removeAllViews();

                    }
                });
            } catch (Exception e) {

            }
        }

        if (requestContentAds) {
            try {
                builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
                    @Override
                    public void onContentAdLoaded(NativeContentAd ad) {
                        LayoutInflater inflater = null;
//                       NativeContentAdView adView = null;
                        View view = null;
                        try {
                            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        } catch (Exception e) {
                        }
                        if (inflater == null) contadView = null;
                        else {
                            view = inflater.inflate(R.layout.ad_content, null);
                            contadView = (NativeContentAdView) view;
                            populateContentAdViewFlag(ad, contadView);
                        }
//                       frameLayout.removeAllViews();
//                       if (contadView != null)
//                           frameLayout.addView(contadView);
//                       else frameLayout.removeAllViews();
                    }
                });
            } catch (Exception e) {

            }
        }

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(true)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                // .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                // mRefresh.setEnabled(true);
                System.out.println("Ad error : " + "Failed to load native ad: "
                        + errorCode);
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());

        // mVideoStatus.setText("");
    }

    public static void populateContentAdViewFlag(NativeContentAd nativeContentAd, NativeContentAdView adView) {
        // mVideoStatus.setText("Video status: Ad does not contain a video asset.");

        //  mRefresh.setEnabled(true);

        adView.setHeadlineView(adView.findViewById(R.id.contentad_headline));
        adView.setImageView(adView.findViewById(R.id.contentad_image));
        adView.setBodyView(adView.findViewById(R.id.contentad_body));

//        adView.findViewById(R.id.imgAd).setBackgroundResource(jobtype == 1 ? R.drawable.ic_ad_badgeblue : R.drawable.ic_ad_badge_color_primary);

//        adView.findViewById(R.id.contentad_call_to_action).setBackgroundColor(jobtype == 1 ? context.getResources().getColor(R.color.blue) : context.getResources().getColor(R.color.colorPrimary));

        adView.setCallToActionView(adView.findViewById(R.id.contentad_call_to_action));
        adView.setLogoView(adView.findViewById(R.id.contentad_logo));
        adView.setAdvertiserView(adView.findViewById(R.id.contentad_advertiser));

        // Some assets are guaranteed to be in every NativeContentAd.
        ((TextView) adView.getHeadlineView()).setText(nativeContentAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeContentAd.getBody());
        ((TextView) adView.getCallToActionView()).setText(nativeContentAd.getCallToAction());
        ((TextView) adView.getAdvertiserView()).setText(nativeContentAd.getAdvertiser());

        List<NativeAd.Image> images = nativeContentAd.getImages();

        if (images.size() > 0) {
            ((ImageView) adView.getImageView()).setImageDrawable(images.get(0).getDrawable());
        }

        // Some aren't guaranteed, however, and should be checked.
        NativeAd.Image logoImage = nativeContentAd.getLogo();

        if (logoImage == null) {
            adView.getLogoView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getLogoView()).setImageDrawable(logoImage.getDrawable());
            adView.getLogoView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeContentAd);
    }

    public static void populateAppInstallAdViewFlag(NativeAppInstallAd nativeAppInstallAd, NativeAppInstallAdView adView) {
        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        //  VideoController vc = nativeAppInstallAd.getVideoController();

        // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
        // VideoController will call methods on this object when events occur in the video
        // lifecycle.
        //   vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
        //   public void onVideoEnd() {
        // Publishers should allow native ads to complete video playback before refreshing
        // or replacing them with another ad in the same UI location.
        // mRefresh.setEnabled(true);
        //  mVideoStatus.setText("Video status: Video playback has ended.");
        //          super.onVideoEnd();
        //      }
        //  });

        adView.setHeadlineView(adView.findViewById(R.id.appinstall_headline));
        adView.setBodyView(adView.findViewById(R.id.appinstall_body));
        adView.setCallToActionView(adView.findViewById(R.id.appinstall_call_to_action));
        adView.setIconView(adView.findViewById(R.id.appinstall_app_icon));
        //adView.setPriceView(adView.findViewById(R.id.appinstall_price));
        adView.setStarRatingView(adView.findViewById(R.id.appinstall_stars));
        //adView.setStoreView(adView.findViewById(R.id.appinstall_store));

        // The MediaView will display a video asset if one is present in the ad, and the first image
        // asset otherwise.
        MediaView mediaView = adView.findViewById(R.id.appinstall_media);
        adView.setMediaView(mediaView);

        // Some assets are guaranteed to be in every NativeAppInstallAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAppInstallAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAppInstallAd.getBody());
        ((Button) adView.getCallToActionView()).setText("Install");
        //((Button) adView.getCallToActionView()).setText(nativeAppInstallAd.getCallToAction());

        // Apps can check the VideoController's hasVideoContent property to determine if the
        // NativeAppInstallAd has a video asset.
        //  if (vc.hasVideoContent()) {
          /*  mVideoStatus.setText(String.format(Locale.getDefault(),
                    "Video status: Ad contains a %.2f:1 video asset.",
                    vc.getAspectRatio()));*/
        //   } else {
        //  mRefresh.setEnabled(true);
        //  mVideoStatus.setText("Video status: Ad does not contain a video asset.");
        //  }

        // These assets aren't guaranteed to be in every NativeAppInstallAd, so it's important to
        // check before trying to display them.
       /* if (nativeAppInstallAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAppInstallAd.getPrice());
        }*/


        NativeAd.Image logoImage = nativeAppInstallAd.getIcon();

        if (logoImage == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(logoImage.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }


       /* if (nativeAppInstallAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAppInstallAd.getStore());
        }*/

      /*  if (nativeAppInstallAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAppInstallAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }*/

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAppInstallAd);
    }

    public static void load_addFromMain(FrameLayout add_banner) {
        adFrameLayout = add_banner;
        try {
            if (insadView != null) {
                ViewGroup parentViewGroup = (ViewGroup) insadView.getParent();
                if (parentViewGroup != null) {
                    parentViewGroup.removeAllViews();
                }
            }
            if (contadView != null) {
                ViewGroup parentViewGroup = (ViewGroup) contadView.getParent();
                if (parentViewGroup != null) {
                    parentViewGroup.removeAllViews();
                }
            }
            add_banner.setVisibility(View.VISIBLE);
            add_banner.removeAllViews();

            if (insadView != null) {
                add_banner.addView(insadView);
                System.out.println("///////////////////================= INSTALL:-");
            } else if (contadView != null) {
                add_banner.addView(contadView);
                System.out.println("///////////////////================= content:-");
            } else {
                System.out.println("///////////////////================= ERROR:-");
                add_banner.removeAllViews();
            }

        } catch (Exception e) {

        }

    }

    //----------------------------------------- Ad Position ----------------------------------------

    public static void add_ads(List<Jobs> list) {
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isAd) {
                    list.remove(i);
                }
            }
            int vall = 0;
            for (int i = 0; i < list.size(); i++) {
                if (i == adPosition) {
                    Jobs jobs = new Jobs();
                    jobs.setId(0);
                    jobs.setImage("");
                    jobs.setJobtype("0");
                    jobs.setJobtitle("");
                    jobs.setEmployer("");
                    jobs.setNoofvacancy("");
                    jobs.setVerified("");
                    jobs.setDate("");
                    jobs.setDatediff(0);
                    jobs.setDescription("");
                    jobs.setRead(false);
                    jobs.setAd(true);
                    list.add(i, jobs);
                }
                if (i > adPosition) {
                    if (vall == 5) {
                        vall = 0;
                        Jobs jobs = new Jobs();
                        jobs.setId(0);
                        jobs.setImage("");
                        jobs.setJobtype("0");
                        jobs.setJobtitle("");
                        jobs.setEmployer("");
                        jobs.setNoofvacancy("");
                        jobs.setVerified("");
                        jobs.setDate("");
                        jobs.setDatediff(0);
                        jobs.setDescription("");
                        jobs.setRead(false);
                        jobs.setAd(true);
                        list.add(i, jobs);
                    } else {
                        vall = vall + 1;
                    }
                }

            }
        }
    }

    public static void adPositionShuffle() {
        ArrayList<Integer> adPositionList = new ArrayList();
        adPositionList.add(1);
        adPositionList.add(2);
        adPositionList.add(3);
        Collections.shuffle(adPositionList);
        adPosition = adPositionList.get(0);
    }

    //-------------------- Adv Ad ------------------------------------------------------------------

    public void loadBackPressInterstitial() {
        backdia = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        backdia.setContentView(R.layout.exit_lay);
        Button ok = backdia.findViewById(R.id.ok);
        Button no = backdia.findViewById(R.id.no);
        FrameLayout frameLay = backdia.findViewById(R.id.frame_lay);
        if (U.isNetworkAvailable(MainActivity.this)) {
            refreshAdBack(MainActivity.this, false, true, frameLay);
        }
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backdia.dismiss();
                if (U.isNetworkAvailable(MainActivity.this)) {
                    if (interstitialAd.isLoaded()) {
                        showIndus();
                    } else {
                        thankYouMessage();
                    }
                } else {
                    thankYouMessage();
                }

            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backdia.dismiss();
            }
        });
    }

    public void refreshAdBack(final Context context, boolean requestAppInstallAds, boolean requestContentAds, final FrameLayout add_banner) {
        if (!requestAppInstallAds && !requestContentAds) {
            System.out.println("Ad error : " + "At least one ad format must be checked to request an ad.");
            return;
        }

        AdLoader.Builder builder = new AdLoader.Builder(context, U.ADMOB_JOBS_BACKPRESS_NATIVE);

        if (requestAppInstallAds) {
            try {
                builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
                    @Override
                    public void onAppInstallAdLoaded(NativeAppInstallAd ad) {
                        LayoutInflater layinflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                        NativeAppInstallAdView adView = (NativeAppInstallAdView) layinflater.inflate(R.layout.ad_app_install_logo, null);
                        populateAppInstallAdView(ad, adView);
                        add_banner.removeAllViews();
                        add_banner.addView(adView);
                    }
                });
            } catch (Exception e) {

            }
        }

        if (requestContentAds) {
            try {
                builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
                    @Override
                    public void onContentAdLoaded(NativeContentAd ad) {
                        LayoutInflater layinflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                        NativeContentAdView adView = (NativeContentAdView) layinflater.inflate(R.layout.ad_app_content_logo, null);
                        populateContentAdView(ad, adView);
                        add_banner.removeAllViews();
                        add_banner.addView(adView);
                    }
                });
            } catch (Exception e) {

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
        adView.setBodyView(adView.findViewById(R.id.appinstall_body));
        adView.setCallToActionView(adView.findViewById(R.id.appinstall_call_to_action));
        adView.setIconView(adView.findViewById(R.id.appinstall_app_icon));
        adView.setPriceView(adView.findViewById(R.id.appinstall_price));
        adView.setStarRatingView(adView.findViewById(R.id.appinstall_stars));
        adView.setStoreView(adView.findViewById(R.id.appinstall_store));

        MediaView mediaView = adView.findViewById(R.id.appinstall_media);
        adView.setMediaView(mediaView);

        ((TextView) adView.getHeadlineView()).setText(nativeAppInstallAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAppInstallAd.getBody());
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
        contentAdView.setImageView(contentAdView.findViewById(R.id.contentad_image));
        contentAdView.setBodyView(contentAdView.findViewById(R.id.contentad_body));
        contentAdView.setCallToActionView(contentAdView.findViewById(R.id.contentad_call_to_action));
        contentAdView.setLogoView(contentAdView.findViewById(R.id.contentad_logo));
        contentAdView.setAdvertiserView(contentAdView.findViewById(R.id.contentad_advertiser));

        // Some assets are guaranteed to be in every NativeContentAd.
        ((TextView) contentAdView.getHeadlineView()).setText(nativeContentAd.getHeadline());
        ((TextView) contentAdView.getBodyView()).setText(nativeContentAd.getBody());
        ((TextView) contentAdView.getCallToActionView()).setText(nativeContentAd.getCallToAction());
        ((TextView) contentAdView.getAdvertiserView()).setText(nativeContentAd.getAdvertiser());

        List<NativeAd.Image> images = nativeContentAd.getImages();

        if (images.size() > 0) {
            ((ImageView) contentAdView.getImageView()).setImageDrawable(images.get(0).getDrawable());
        }

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