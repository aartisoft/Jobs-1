package nithra.jobs.career.placement.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import nithra.jobs.career.placement.MainActivity;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.fragments.PlaceholderFragment;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

public class JobDetailActivity extends AppCompatActivity{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    public static SectionsPagerAdapter mSectionsPagerAdapter;
    public static List<String> rowList;
    int point = 0;
    public static int position = 0;
    String task = "";
    SharedPreference pref;
    String key = "";
    public static int adCount = 0;
    LinearLayout adview;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public static ViewPager mViewPager;
    Toolbar toolbar;
    public static InterstitialAd interstitialAd;
    boolean isGCM = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent(false);
        setContentView(R.layout.activity_job_detail);
        toolbar = findViewById(R.id.toolbar);
        pref = new SharedPreference();
        loadInDusAd();
        pref.putInt(this, U.SH_BACK_AD_COUNT, pref.getInt(this, U.SH_BACK_AD_COUNT) + 1);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();
        key = bundle.getString("key");
        task = bundle.getString("task");
        Log.e("task",task);
        isGCM = bundle.getBoolean("gcm");
        point = bundle.getInt("point");
        position = bundle.getInt("position");
        System.out.println("Deeplinking not called");

        if (pref.getInt(this, U.SH_INFO_DIALOG) == 0) infoDialog();

        if(task.equals(U.GCM)){
            String notifyId = bundle.getString("Notifyid");

            SQLiteDatabase myDB = openOrCreateDatabase("myDB", 0, null);
            String query = "update noti_cal set isclose='1' where id='" + notifyId + "'";
            myDB.execSQL(query);

        }
        callData();
    }

    private void infoDialog() {
        pref.putInt(this, U.SH_INFO_DIALOG, 1);
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.info_dialog);
                dialog.show();
                dialog.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
            }
        });
    }

    private void callData() {
        rowList = new ArrayList<>();
        rowList = Arrays.asList(key.split(","));

        adview = findViewById(R.id.adview);

        MainActivity.showAd(this, adview,true);
        mViewPager = findViewById(R.id.viewPager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        String vid = rowList.indexOf(point + "") + "".trim();
        mViewPager.setCurrentItem(Integer.parseInt(vid));
    }

    public void loadInDusAd() {
        //INDUS ADD
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(U.INDUS_AD_CAT);
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
        // ---- END ---
    }

    public void showIndus() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
            pref.removeInt(this, U.SH_BACK_AD_COUNT);
            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }
            });
        }
    }

    private void close() {
        if (isGCM) {
            showIndus();
            finish();
        } else {
            if (pref.getInt(this, U.SH_BACK_AD_COUNT) > 5) showIndus();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adCount = 0;
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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position,task);
        }

        @Override
        public int getCount() {
            return rowList.size();
        }
    }

}
