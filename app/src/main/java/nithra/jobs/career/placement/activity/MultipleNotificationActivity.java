package nithra.jobs.career.placement.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.fragments.JobListFragment;
import nithra.jobs.career.placement.networking.MySingleton;
import nithra.jobs.career.placement.pojo.Item;
import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

/**
 * Created by nithra on 14/2/18.
 */

public class MultipleNotificationActivity extends FragmentActivity {

    ViewPager viewPager;
    String type, jobids, title, notifyId;
    InterstitialAd interstitialAd;
    TextView textview;
    Fragment fragment;
    SharedPreference pref;
    SQLiteDatabase myDB;
    Dialog infodialog;
    private List<Item> districtList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent(false);
        setContentView(R.layout.multiple_notification_lay);
        loadInDusAd();
        pref = new SharedPreference();
        myDB = openOrCreateDatabase("myDB", 0, null);

        Bundle extras;
        extras = getIntent().getExtras();
        if (extras != null) {
            type = extras.getString("type");
            jobids = extras.getString("jobids");
            title = extras.getString("title");
            notifyId = extras.getString("Notifyid");
        }


        if (type != null) {
            switch (type) {
                case "s":
                    String query = "update noti_cal set isclose='1' where id='" + notifyId + "'";
                    myDB.execSQL(query);
                    fragment = JobListFragment.newInstance(U.NOTIJOBS, jobids, "");
                    break;
                case "cj":
                    if (jobids != null) {
                        switch (jobids) {
                            case "homePrivate":
                                fragment = setModeFragment(U.SH_FIRST_TIME_PRIVATE, "1");
                                break;
                            case "homeState":
                                fragment = setModeFragment(U.SH_FIRST_TIME_STATE, "2");
                                break;
                            case "homeCentral":
                                fragment = setModeFragment(U.SH_FIRST_TIME_CENTRAL, "3");
                                break;
                            case "homeConsultancy":
                                fragment = setModeFragment(U.SH_FIRST_TIME_CONSULTANCY, "4");
                                if (pref.getInt(MultipleNotificationActivity.this, U.SH_CONSULTANCY_INFO_SHOW) == 0) {
                                    showInfoDialog();
                                }
                                break;
                            case "homeAlljobs":
                                fragment = JobListFragment.newInstance(U.ALLJOBS, "", "3");
                                break;
                        }
                    }
                    break;
                case "fj":
                    if (jobids != null) {
                        fragment = JobListFragment.newInstance(U.ALLJOBS, U.NOTIFICATION, jobids);
                    }
                    break;
                case "dj":
                    if (jobids != null) {
                        String query1 = "update noti_cal set isclose='1' where id='" + notifyId + "'";
                        myDB.execSQL(query1);
                        fragment = JobListFragment.newInstance(SU.DISTRICTWISE_NOTIFICATION, "", "" + jobids,
                                "", "", "", "", "", "", "1");
                    }
                    break;
            }
        }

        viewPager = findViewById(R.id.viewpager);
        textview = findViewById(R.id.txtTitle);
        textview.setText(title);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return fragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
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
                finish();
            }
        });
    }

    public int checkFirstTime(String key) {
        int value;
        if (pref.getString(MultipleNotificationActivity.this, key).equals(U.currentDate())) {
            value = 1;
        } else {
            value = 0;
            pref.putString(MultipleNotificationActivity.this, key, U.currentDate());
        }
        return value;
    }

    public Fragment setModeFragment(String key, String mode) {
        JobListFragment fragment = JobListFragment.newInstance(SU.FILTER, "", "",
                "", "", "", "" + mode, "", "",
                "" + checkFirstTime(key));
        return fragment;
    }

    public void showInfoDialog() {
        infodialog = new Dialog(MultipleNotificationActivity.this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
        infodialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        infodialog.setContentView(R.layout.info_popup_consultancy);
        WebView infoText = infodialog.findViewById(R.id.info_text);
        infoText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        String info = "<b><meta charset=\"utf-8\"><font color=purple size=2>\n" +
                getResources().getString(R.string.consultancy_text) + "<br><br></font></b>";

        U.webview(MultipleNotificationActivity.this, info, infoText);

        infodialog.findViewById(R.id.text_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref.putInt(MultipleNotificationActivity.this, U.SH_CONSULTANCY_INFO_SHOW, 1);
                infodialog.dismiss();
            }
        });

        infodialog.findViewById(R.id.text_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infodialog.dismiss();
            }
        });

        infodialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (jobids != null) {
                    if (jobids.equals("homeConsultancy")) {
                        if (pref.getInt(MultipleNotificationActivity.this, U.SH_CONSULTANCY_INFO_SHOW) == 0) {
                            finish();
                        }
                    }
                }
            }
        });

        infodialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (interstitialAd.isLoaded()) {
            showIndus();
        } else {
            finish();
        }
    }
}
