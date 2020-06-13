package nithra.jobs.career.placement.activity;

import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import nithra.jobs.career.placement.MainActivity;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.fragments.JobListFragment;
import nithra.jobs.career.placement.fragments.PlaceholderFragment;
import nithra.jobs.career.placement.utills.CustomViewPager;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

public class JobDetailActivity extends AppCompatActivity {

    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */

    SectionsPagerAdapter mSectionsPagerAdapter;
    List<String> rowList;
    String point;
    int position = 0;
    String task = "", value;
    SharedPreference pref;
    String key = "";
    LinearLayout adview;
    TextView txtToolTitle;
    CustomViewPager mViewPager;
    PublisherInterstitialAd interstitialAd_noti;
    boolean isGCM = false;
    ImageView back, prev, next;
    RelativeLayout prevNextLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_job_detail);
        back = findViewById(R.id.back);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);

        prevNextLay = findViewById(R.id.prevNextLay);

        txtToolTitle = findViewById(R.id.txtToolTitle);
        pref = new SharedPreference();

        pref.putInt(this, U.SH_BACK_AD_COUNT, pref.getInt(this, U.SH_BACK_AD_COUNT) + 1);

        if (pref.getInt(this, U.SH_BACK_AD_COUNT) > 5) {
            if (pref.getInt(JobDetailActivity.this, U.SH_AD_PURCHASED) == 0) {
                ads_manager_interstistial();
            }
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();
        key = bundle.getString("key");
        task = bundle.getString("task");
        value = bundle.getString("value");
        isGCM = bundle.getBoolean("gcm");
        point = bundle.getString("point");
        position = bundle.getInt("position");

        callData();

        if (pref.getInt(this, U.SH_INFO_DIALOG) == 0) infoDialog();

        if (task.equals(U.GCM)) {
            String notifyId = bundle.getString("Notifyid");
            SQLiteDatabase myDB = openOrCreateDatabase("myDB", 0, null);
            String query = "update noti_cal set isclose='1' where id='" + notifyId + "'";
            myDB.execSQL(query);
        }

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            }
        });

        mViewPager.setOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    prev.setVisibility(View.GONE);
                    next.setVisibility(View.VISIBLE);
                } else if (position == rowList.size() - 1) {
                    prev.setVisibility(View.VISIBLE);
                    next.setVisibility(View.GONE);
                } else {
                    prev.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                }

                if (rowList.get(position).contains("-")) {
                    txtToolTitle.setText("வேலைவாய்ப்பு");
                } else {
                    txtToolTitle.setText(getResources().getString(R.string.title_activity_job_detail));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void infoDialog() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.info_dialog);
        dialog.show();
        dialog.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref.putInt(JobDetailActivity.this, U.SH_INFO_DIALOG, 1);
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.parentLay).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pref.putInt(JobDetailActivity.this, U.SH_INFO_DIALOG, 1);
                dialog.dismiss();
                return false;
            }
        });
    }

    private void callData() {
        rowList = new ArrayList<>();
        rowList = Arrays.asList(key.split(","));

        adview = findViewById(R.id.adview);

        if (pref.getInt(JobDetailActivity.this, U.SH_AD_PURCHASED) == 0) {
            MainActivity.showAd(this, adview, true);
        }

        mViewPager = findViewById(R.id.viewPager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        String vid = rowList.indexOf(point) + "".trim();
        mViewPager.setCurrentItem(Integer.parseInt(vid));
        Log.e("response_data", "" + key);
    }


    public void ads_manager_interstistial() {
        interstitialAd_noti = new PublisherInterstitialAd(this);
        interstitialAd_noti.setAdUnitId("/21634001759/JOBS001");
        interstitialAd_noti.loadAd(new PublisherAdRequest.Builder().build());
        interstitialAd_noti.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                finish();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

        });
    }

    public void showIndus() {
        if (interstitialAd_noti != null && interstitialAd_noti.isLoaded()) {
            interstitialAd_noti.show();
            pref.removeInt(this, U.SH_BACK_AD_COUNT);
        }
    }

    private void close() {
        if (pref.getInt(this, U.SH_BACK_AD_COUNT) > 5) {
            if (pref.getInt(JobDetailActivity.this, U.SH_AD_PURCHASED) == 0) {
                showIndus();
            } else {
                finish();
            }
        } else {
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
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            if (rowList.size() == 1) {
                prev.setVisibility(View.GONE);
                next.setVisibility(View.GONE);
            } else if (position == 0) {
                prev.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
            } else if (position == rowList.size() - 1) {
                prev.setVisibility(View.VISIBLE);
                next.setVisibility(View.GONE);
            } else {
                prev.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
            }

            if (rowList.get(position).contains("-")) {
                txtToolTitle.setText("வேலைவாய்ப்பு");
                return JobListFragment.newInstance(U.GROUPJOBS, rowList.get(position).trim().replace("-", ","), task);
            } else {
                txtToolTitle.setText(getResources().getString(R.string.title_activity_job_detail));
                return PlaceholderFragment.newInstance(position, task, key, value);
            }
        }

        @Override
        public int getCount() {
            return rowList.size();
        }
    }
}
