package nithra.jobs.career.placement.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.InterstitialAd;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import nithra.jobs.career.placement.MainActivity;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.engine.LocalDB;
import nithra.jobs.career.placement.fragments.JobListFragment;
import nithra.jobs.career.placement.fragments.PlaceholderFragment;
import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

/**
 * Created by nithra on 14/2/18.
 */

public class MultipleNotificationActivity extends FragmentActivity {

    ViewPager viewPager;
    String type = "", message = "", title = "", notifyId = "";
    InterstitialAd interstitialAd;
    TextView textview;
    Fragment fragment;
    SharedPreference pref;
    SQLiteDatabase myDB;
    Dialog infodialog;
    LocalDB localDB;
    String mode = "";
    LinearLayout adview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.multiple_notification_lay);
        pref = new SharedPreference();
        localDB = new LocalDB(this);

        myDB = openOrCreateDatabase("myDB", 0, null);

        try {
            type = getIntent().getStringExtra("type");
            message = getIntent().getStringExtra("message");
            title = getIntent().getStringExtra("title");
            notifyId = getIntent().getStringExtra("Notifyid");
            mode = getIntent().getStringExtra("mode");
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewPager = findViewById(R.id.viewpager);
        adview = findViewById(R.id.adview);
        adview.setVisibility(View.GONE);
        textview = findViewById(R.id.txtTitle);
        textview.setText(title);

        if (type != null) {
            switch (type) {
                case "rj":
                    if (message != null && !message.isEmpty()) {
                        fragment = PlaceholderFragment.newInstance(0, U.GCM, message, U.GCM);
                        if (pref.getInt(MultipleNotificationActivity.this, U.SH_AD_PURCHASED) == 0) {
                            MainActivity.showAd(this, adview, true);
                        }
                    }
                    break;
                case "s":
                    if (message != null && !message.isEmpty()) {
                        String query = "update noti_cal set isclose='1' where id='" + notifyId + "'";
                        myDB.execSQL(query);
                        if (message.contains(",")) {
                            fragment = JobListFragment.newInstance(U.NOTIJOBS, message.trim(), "");
                        } else {
                            if (message.matches("[0-9]+")) {
                                fragment = PlaceholderFragment.newInstance(0, U.GCM, message, U.GCM);
                                if (pref.getInt(MultipleNotificationActivity.this, U.SH_AD_PURCHASED) == 0) {
                                    MainActivity.showAd(this, adview, true);
                                }
                            }
                        }
                    }
                    break;
                case "cj":
                    if (message != null) {
                        switch (message) {
                            case "homePrivate":
                                fragment = setModeFragment(U.SH_FIRST_TIME_PRIVATE, "1");
                                break;
                            case "homeState":
                                fragment = setModeFragment(U.SH_FIRST_TIME_STATE, "2");
                                break;
                            case "homeCentral":
                                fragment = setModeFragment(U.SH_FIRST_TIME_CENTRAL, "3");
                                break;
                            case "homeFreshers":
                                fragment = setFreshersFragment();
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
                            case "homePopularEmp":
                                fragment = JobListFragment.newInstance(SU.POPULAR_EMP, "", "");
                                break;
                        }
                    } else {
                        finish();
                    }
                    break;
                case "fj":
                    if (message != null) {
                        fragment = JobListFragment.newInstance(U.ALLJOBS, U.NOTIFICATION, message.trim());
                    } else {
                        finish();
                    }
                    break;
                case "dj":
                    if (message != null) {
                        String query1 = "update noti_cal set isclose='1' where id='" + notifyId + "'";
                        myDB.execSQL(query1);
                        fragment = JobListFragment.newInstance(SU.DISTRICTWISE_NOTIFICATION, "", "" + message.trim(),
                                "", "", "", "", "", "", "1");
                    } else {
                        finish();
                    }
                    break;
                case SU.GETPERSONALISEDJOBS:
                    Log.e("personalisedJobs", "called");
                    fragment = JobListFragment.newInstance(SU.GETPERSONALISEDJOBS, "", "");
                    break;
                case "pn":
                    if (message != null) {
                        if (message.equals(U.catArray[0])) {
                            fragment = setModeRFragment(U.RJOB, pref.getString(MultipleNotificationActivity.this, U.SH_SKILLS), pref.getString(MultipleNotificationActivity.this, U.SH_JOBLOCATION),
                                    pref.getString(MultipleNotificationActivity.this, U.SH_COURSE), pref.getString(MultipleNotificationActivity.this, U.SH_EMPLOYEE_ID),
                                    pref.getString(MultipleNotificationActivity.this, U.SH_GENDER),
                                    pref.getString(MultipleNotificationActivity.this, U.SH_MARITAL_STATUS), "எங்களால் பரிந்துரைக்கப்படுபவை", "");
                        } else if (message.equals(U.catArray[1])) {
                            fragment = setModeRFragment(U.RJOB, pref.getString(MultipleNotificationActivity.this, U.SH_SKILLS), "",
                                    "", pref.getString(MultipleNotificationActivity.this, U.SH_EMPLOYEE_ID), pref.getString(MultipleNotificationActivity.this, U.SH_GENDER),
                                    pref.getString(MultipleNotificationActivity.this, U.SH_MARITAL_STATUS), "உங்கள் திறமைக்கானவை", "");
                        } else if (message.equals(U.catArray[2])) {
                            fragment = setModeRFragment(U.RJOB, "", "", pref.getString(MultipleNotificationActivity.this, U.SH_COURSE), pref.getString(MultipleNotificationActivity.this, U.SH_EMPLOYEE_ID),
                                    pref.getString(MultipleNotificationActivity.this, U.SH_GENDER),
                                    pref.getString(MultipleNotificationActivity.this, U.SH_MARITAL_STATUS), "உங்கள் படிப்பிற்கானவை", "");
                        } else if (message.equals(U.catArray[3])) {
                            fragment = setModeRFragment(U.RJOB, "", pref.getString(MultipleNotificationActivity.this, U.SH_JOBLOCATION),
                                    "", pref.getString(MultipleNotificationActivity.this, U.SH_EMPLOYEE_ID), pref.getString(MultipleNotificationActivity.this, U.SH_GENDER),
                                    pref.getString(MultipleNotificationActivity.this, U.SH_MARITAL_STATUS), "நீங்கள் தேர்ந்தெடுத்த இடங்களில் உள்ளவை", "");
                        } else if (message.equals(U.catArray[4])) {
                            fragment = setModeRFragment(U.RJOB, pref.getString(MultipleNotificationActivity.this, U.SH_SKILLS), pref.getString(MultipleNotificationActivity.this, U.SH_JOBLOCATION),
                                    pref.getString(MultipleNotificationActivity.this, U.SH_COURSE), pref.getString(MultipleNotificationActivity.this, U.SH_EMPLOYEE_ID),
                                    pref.getString(MultipleNotificationActivity.this, U.SH_GENDER),
                                    pref.getString(MultipleNotificationActivity.this, U.SH_MARITAL_STATUS), "எங்களால் பரிந்துரைக்கப்படுபவை", U.catArray[4]);
                        } else if (message.equals(U.catArray[5])) {
                            fragment = setFragment(pref.getString(MultipleNotificationActivity.this, U.SH_SKILLS), pref.getString(MultipleNotificationActivity.this, U.SH_JOBLOCATION),
                                    pref.getString(MultipleNotificationActivity.this, U.SH_COURSE), 2);
                        } else if (message.equals(U.catArray[6])) {
                            fragment = setFragment(pref.getString(MultipleNotificationActivity.this, U.SH_SKILLS), pref.getString(MultipleNotificationActivity.this, U.SH_JOBLOCATION),
                                    pref.getString(MultipleNotificationActivity.this, U.SH_COURSE), 3);
                        }
                    } else {
                        finish();
                    }
                    break;
                case "grp_jobs":
                    fragment = JobListFragment.newInstance(U.NOTIJOBS, message.trim(), "");
                    break;
                case "upn":
                    if (message.equals(U.UnReg_catArray[0])) {
                        String district = pref.getString(MultipleNotificationActivity.this, U.SH_USER_DISTRICT_NAME);
                        fragment = JobListFragment.newInstance(SU.DISTRICTWISE, "", district, "", "", "", "", "", "", "1");
                    } else if (message.equals(U.UnReg_catArray[1])) {
                        String mKey = localDB.getReadIds();
                        if (!mKey.equals("")) {
                            fragment = JobListFragment.newInstance(SU.GETRECENTJOBS, "", "");
                        } else {
                            fragment = JobListFragment.newInstance(U.ALLJOBS, "", "3");
                        }
                    } else if (message.equals(U.UnReg_catArray[2])) {
                        if (!pref.getString(MultipleNotificationActivity.this, U.SH_RECENT_SEARCH_KEYS).equals("")) {
                            String search = pref.getString(MultipleNotificationActivity.this, U.SH_RECENT_SEARCH_KEYS).replace(",", " ").replace("+", " ");
                            fragment = JobListFragment.newInstance(U.ADVSEARCH, search, "key");
                        } else {
                            fragment = JobListFragment.newInstance(U.ALLJOBS, "", "3");
                        }
                    } else if (message.equals(U.UnReg_catArray[3])) {
                        fragment = JobListFragment.newInstance(U.ALLJOBS, "", "3");
                    } else if (message.equals(U.UnReg_catArray[4])) {
                        fragment = setModeFragment(U.SH_FIRST_TIME_PRIVATE, "1");
                    } else if (message.equals(U.UnReg_catArray[5])) {
                        fragment = setModeFragment(U.SH_FIRST_TIME_STATE, "2");
                    } else if (message.equals(U.UnReg_catArray[6])) {
                        fragment = setModeFragment(U.SH_FIRST_TIME_CENTRAL, "3");
                    }
                    break;
            }
        } else {
            finish();
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
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
        return JobListFragment.newInstance(SU.FILTER, "", "",
                "", "", "", "" + mode, "", "",
                "" + checkFirstTime(key));
    }

    public Fragment setFreshersFragment() {
        return JobListFragment.newInstance(SU.FILTER, "", "",
                "", "100,101", "", "", "", "", "" + checkFirstTime(U.SH_FIRST_TIME_FRESHERS));
    }

    public Fragment setFragment(String skills, String location, String qualification, int mode) {
        return JobListFragment.newInstance(SU.GETRJOBS, skills, location,
                qualification, "", "", "" + mode, "", "",
                "1");
    }

    public Fragment setModeRFragment(String action, String skill, String location, String qualification, String empId,
                                     String gender, String marital_status, String title, String category) {
        return JobListFragment.newInstance(action, skill, location, qualification,
                empId, gender, marital_status, 1, title);
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
                if (message != null) {
                    if (message.equals("homeConsultancy")) {
                        if (pref.getInt(MultipleNotificationActivity.this, U.SH_CONSULTANCY_INFO_SHOW) == 0) {
                            finish();
                        }
                    }
                }
            }
        });

        infodialog.show();
    }

    public void close() {
        if (pref.getString(MultipleNotificationActivity.this, U.SH_USER_TYPE).equals("")) {
            Intent i = new Intent(MultipleNotificationActivity.this, MainActivity.class);
            finish();
            startActivity(i);
        } else if (mode.equals("initial")) {
            if (pref.getString(MultipleNotificationActivity.this, U.SH_USER_TYPE).equals(U.EMPLOYER)) {
                startActivity(new Intent(MultipleNotificationActivity.this, EmployerHomeActivity.class));
                finish();
            } else {
                Intent i = new Intent(MultipleNotificationActivity.this, MainActivity.class).putExtra("mode", "gcm");
                finish();
                startActivity(i);
            }
        } else if (mode.equals("gcm")) {
            Intent i = new Intent(MultipleNotificationActivity.this, MainActivity.class).putExtra("mode", "gcm");
            finish();
            startActivity(i);
        } else if (mode.equals("reminderNoti")) {
            startActivity(new Intent(MultipleNotificationActivity.this, EmployerHomeActivity.class));
            finish();
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            close();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return fragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 1;
        }
    }

}
