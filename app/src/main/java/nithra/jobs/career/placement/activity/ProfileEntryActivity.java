package nithra.jobs.career.placement.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import nithra.jobs.career.placement.Interface.FragmentInterface;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.fragments.ContactFragment;
import nithra.jobs.career.placement.fragments.OthersFragment;
import nithra.jobs.career.placement.fragments.PersonalFragment;
import nithra.jobs.career.placement.fragments.QualificationFragment;
import nithra.jobs.career.placement.utills.L;
import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

/**
 * Created by nithra-apps on 27/1/18.
 */

public class ProfileEntryActivity extends AppCompatActivity
        implements ContactFragment.OnRegistrationListener,
        PersonalFragment.OnRegistrationListener,
        QualificationFragment.OnRegistrationListener,
        OthersFragment.OnRegistrationListener {

    SharedPreference pref;
    TabLayout tabLayout;
    ViewPager viewPager;
    LinearLayout back, next, progressLay;
    TextView nextText, backText, percentIndicator;
    ProgressBar progressbarPredict;
    int notifyImg = R.drawable.profile;
    int notifyImg_light = R.drawable.profile_light;
    int current_page = 0;
    ViewPagerAdapter adapter;
    String task;
    int status = 0;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        task = getIntent().getStringExtra("task");
        pref = new SharedPreference();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        registration();
    }

    //------------------------------------------ profile Entry view --------------------------------

    public void registration() {
        setContentView(R.layout.registration_lay);
        current_page = 3;
        back = findViewById(R.id.backlay);
        next = findViewById(R.id.nextlay);

        nextText = findViewById(R.id.next_text);
        backText = findViewById(R.id.back_text);

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);

        progressLay = findViewById(R.id.progressLay);
        percentIndicator = findViewById(R.id.percentIndicator);
        progressbarPredict = findViewById(R.id.progressbarPredict);

        if (pref.getBoolean(ProfileEntryActivity.this, U.SH_SIGN_UP_SUCCESS) &&
                pref.getBoolean(ProfileEntryActivity.this, U.SH_OTP_SUCCESS)) {
            progressLay.setVisibility(View.GONE);
        } else {
            progressLay.setVisibility(View.VISIBLE);
            showPercentage();
        }

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                FragmentInterface fragment = (FragmentInterface) adapter.instantiateItem(viewPager, position);
                fragment.fragmentBecameVisible();

                if (position == 3) {
                    if (task.equals(SU.REGISTER)) {
                        nextText.setText("Submit");
                    } else if (task.equals(SU.UPDATE)) {
                        nextText.setText("Update");
                    }
                } else {
                    nextText.setText("Next");
                }
                if (position == 0) {
                    backText.setText("Cancel");
                } else {
                    backText.setText("Back");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        switch (task) {
            case U.SH_JOBLOCATION:
            case U.SH_SKILLS:
            case U.SH_RESUME:
                task = SU.UPDATE;
                viewPager.setCurrentItem(3);
                notifyImg = R.drawable.edit;
                notifyImg_light = R.drawable.edit_light;
                break;
            case SU.REGISTER:
                notifyImg = R.drawable.profile;
                notifyImg_light = R.drawable.profile_light;
                break;
            case SU.UPDATE:
                notifyImg = R.drawable.edit;
                notifyImg_light = R.drawable.edit_light;
                break;
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewPager.getCurrentItem() == 0) {
                    pref.putInt(ProfileEntryActivity.this, U.SH_REGISTRATION_FLAG, 1);
                    pref.putString(ProfileEntryActivity.this, U.SH_REGISTRATION_TASK, "");
                    finish();
                } else {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewPager.getCurrentItem() == 0) {
                    checkContactValues();
                } else if (viewPager.getCurrentItem() == 1) {
                    checkPersonalValues();
                } else if (viewPager.getCurrentItem() == 2) {
                    checkQualicationValues();
                } else if (viewPager.getCurrentItem() == 3) {
                    checkValuesForSubmit(task);
                }
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    ImageView imageView = tab.getCustomView().findViewById(R.id.profile_img);
                    imageView.setImageResource(notifyImg);
                    TextView text = tab.getCustomView().findViewById(R.id.tabTitleText);
                    text.setTextColor(getResources().getColor(R.color.black));
                } else if (tab.getPosition() == 1) {
                    ImageView imageView = tab.getCustomView().findViewById(R.id.profile_img);
                    imageView.setImageResource(notifyImg);
                    TextView text = tab.getCustomView().findViewById(R.id.tabTitleText);
                    text.setTextColor(getResources().getColor(R.color.black));
                } else if (tab.getPosition() == 2) {
                    ImageView imageView = tab.getCustomView().findViewById(R.id.profile_img);
                    imageView.setImageResource(notifyImg);
                    TextView text = tab.getCustomView().findViewById(R.id.tabTitleText);
                    text.setTextColor(getResources().getColor(R.color.black));
                } else if (tab.getPosition() == 3) {
                    ImageView imageView = tab.getCustomView().findViewById(R.id.profile_img);
                    imageView.setImageResource(notifyImg);
                    TextView text = tab.getCustomView().findViewById(R.id.tabTitleText);
                    text.setTextColor(getResources().getColor(R.color.black));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0) {
                    ImageView imageView = tab.getCustomView().findViewById(R.id.profile_img);
                    imageView.setImageResource(notifyImg_light);
                    TextView text = tab.getCustomView().findViewById(R.id.tabTitleText);
                    text.setTextColor(getResources().getColor(R.color.lighest));
                } else if (tab.getPosition() == 1) {
                    ImageView imageView = tab.getCustomView().findViewById(R.id.profile_img);
                    imageView.setImageResource(notifyImg_light);
                    TextView text = tab.getCustomView().findViewById(R.id.tabTitleText);
                    text.setTextColor(getResources().getColor(R.color.lighest));
                } else if (tab.getPosition() == 2) {
                    ImageView imageView = tab.getCustomView().findViewById(R.id.profile_img);
                    imageView.setImageResource(notifyImg_light);
                    TextView text = tab.getCustomView().findViewById(R.id.tabTitleText);
                    text.setTextColor(getResources().getColor(R.color.lighest));
                } else if (tab.getPosition() == 3) {
                    ImageView imageView = tab.getCustomView().findViewById(R.id.profile_img);
                    imageView.setImageResource(notifyImg_light);
                    TextView text = tab.getCustomView().findViewById(R.id.tabTitleText);
                    text.setTextColor(getResources().getColor(R.color.lighest));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * Adding custom view to tab
     */
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setCustomView(createTabView(this, "Contact Details", 0));
        tabLayout.getTabAt(1).setCustomView(createTabView(this, "Personal Details", 1));
        tabLayout.getTabAt(2).setCustomView(createTabView(this, "Qualification Details", 2));
        tabLayout.getTabAt(3).setCustomView(createTabView(this, "Others Details", 3));
    }

    private View createTabView(Context context, String tabText, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.registration_tab_design, null, false);
        if (position == 0) {
            ImageView img = view.findViewById(R.id.profile_img);
            img.setImageResource(notifyImg);
            TextView tv = view.findViewById(R.id.tabTitleText);
            tv.setText(tabText);
            tv.setTextColor(context.getResources().getColor(R.color.black));
        } else {
            ImageView img = view.findViewById(R.id.profile_img);
            img.setImageResource(notifyImg_light);
            TextView tv = view.findViewById(R.id.tabTitleText);
            tv.setText(tabText);
            tv.setTextColor(context.getResources().getColor(R.color.lighest));
        }

        return view;
    }

    /**
     * Adding fragments to ViewPager
     *
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onPercentageChange() {
        showPercentage();
    }

    private void checkValuesForSubmit(String task) {
        if (pref.getString(ProfileEntryActivity.this, U.SH_NAME).equals("")) {
            viewPager.setCurrentItem(0);
            L.t(this, "Enter Your Name");
        } else if (pref.getString(ProfileEntryActivity.this, U.SH_EMAIL).equals("")) {
            viewPager.setCurrentItem(0);
            L.t(this, "Enter Your Email Address");
        } /*else if (!U.EmailValidation(pref.getString(ProfileEntryActivity.this, U.SH_EMAIL))) {
            viewPager.setCurrentItem(0);
            L.t(ProfileEntryActivity.this, "Enter Valid Email");
        }*/ else if (pref.getString(ProfileEntryActivity.this, U.SH_MOBILE).equals("")) {
            viewPager.setCurrentItem(0);
            L.t(this, "Enter Your MobileNumber");
        } else if (pref.getString(ProfileEntryActivity.this, U.SH_NATIVE_LOCATION).equals("")) {
            viewPager.setCurrentItem(0);
            L.t(this, "Choose Your Native Location");
        } else if (pref.getString(ProfileEntryActivity.this, U.SH_GENDER).equals("")) {
            viewPager.setCurrentItem(1);
            L.t(this, "Choose Your Gender");
        } else if (pref.getString(ProfileEntryActivity.this, U.SH_MARITAL_STATUS).equals("")) {
            viewPager.setCurrentItem(1);
            L.t(this, "Choose Your Marital Status");
        } else if (pref.getString(ProfileEntryActivity.this, U.SH_DOB).equals("")) {
            viewPager.setCurrentItem(1);
            L.t(this, "Choose Your DOB");
        } else if (pref.getString(ProfileEntryActivity.this, U.SH_COURSE).equals("")) {
            viewPager.setCurrentItem(2);
            L.t(this, "Choose Your Qualification");
        } else if (pref.getString(ProfileEntryActivity.this, U.SH_WORK_STATUS).equals("")) {
            viewPager.setCurrentItem(3);
            L.t(this, "Choose Your Work Status");
        } else if (pref.getString(ProfileEntryActivity.this, U.SH_SKILLS).equals("")) {
            viewPager.setCurrentItem(3);
            L.t(this, "Choose Your Skills");
        } else if (pref.getString(ProfileEntryActivity.this, U.SH_CATEGORY).equals("")) {
            viewPager.setCurrentItem(3);
            L.t(this, "Choose Your preferred Job Category");
        } else if (pref.getString(ProfileEntryActivity.this, U.SH_TITLE).equals("")) {
            viewPager.setCurrentItem(3);
            L.t(this, "Choose Your preferred Job Title");
        } else if (pref.getString(ProfileEntryActivity.this, U.SH_JOBLOCATION).equals("")) {
            viewPager.setCurrentItem(3);
            L.t(this, "Choose Your preferred Job Location");
        } else if (pref.getString(ProfileEntryActivity.this, U.SH_PRIVACY_POLICY).equals("") ||
                pref.getString(ProfileEntryActivity.this, U.SH_PRIVACY_POLICY).equals("0")) {
            viewPager.setCurrentItem(3);
            L.t(this, "Read & Tick Privacy Policy");
        } else if (pref.getString(ProfileEntryActivity.this, U.SH_TERMS_CONDITIONS).equals("") ||
                pref.getString(ProfileEntryActivity.this, U.SH_TERMS_CONDITIONS).equals("0")) {
            viewPager.setCurrentItem(3);
            L.t(this, "Read & Tick Terms & conditions");
        } else if (!U.isNetworkAvailable(ProfileEntryActivity.this)) {
            L.t(this, U.INA);
        } else {
            pref.putInt(ProfileEntryActivity.this, U.SH_REGISTRATION_FLAG, 1);
            pref.putString(ProfileEntryActivity.this, U.SH_REGISTRATION_TASK, task);
            finish();
        }
    }

    //-----------------------------------------  profile Submit ------------------------------------

    private void checkContactValues() {
        if (pref.getString(ProfileEntryActivity.this, U.SH_NAME).equals("")) {
            viewPager.setCurrentItem(0);
            L.t(ProfileEntryActivity.this, "Enter Your Name");
        } else if (pref.getString(ProfileEntryActivity.this, U.SH_EMAIL).equals("")) {
            viewPager.setCurrentItem(0);
            L.t(ProfileEntryActivity.this, "Enter Your Email Address");
        } /*else if (!U.EmailValidation(pref.getString(ProfileEntryActivity.this, U.SH_EMAIL))) {
            viewPager.setCurrentItem(0);
            L.t(ProfileEntryActivity.this, "Enter Valid Email");
        }*/ else if (pref.getString(ProfileEntryActivity.this, U.SH_MOBILE).equals("")) {
            viewPager.setCurrentItem(0);
            L.t(ProfileEntryActivity.this, "Enter Your MobileNumber");
        } else if (pref.getString(ProfileEntryActivity.this, U.SH_NATIVE_LOCATION).equals("")) {
            viewPager.setCurrentItem(0);
            L.t(ProfileEntryActivity.this, "Choose Your Native Location");
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    private void checkPersonalValues() {
        if (pref.getString(ProfileEntryActivity.this, U.SH_GENDER).equals("")) {
            viewPager.setCurrentItem(1);
            L.t(this, "Choose Your Gender");
        } else if (pref.getString(ProfileEntryActivity.this, U.SH_MARITAL_STATUS).equals("")) {
            viewPager.setCurrentItem(1);
            L.t(this, "Choose Your Marital Satus");
        } else if (pref.getString(ProfileEntryActivity.this, U.SH_DOB).equals("")) {
            viewPager.setCurrentItem(1);
            L.t(this, "Choose Your DOB");
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    private void checkQualicationValues() {
        if (pref.getString(ProfileEntryActivity.this, U.SH_COURSE).equals("")) {
            viewPager.setCurrentItem(2);
            L.t(this, "Choose Your Qualification");
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (RegistrationActivity.Qpopupwindow != null && RegistrationActivity.Qpopupwindow.isShowing()) {
                    RegistrationActivity.Qpopupwindow.dismiss();
                }
                if (RegistrationActivity.Spopupwindow != null && RegistrationActivity.Spopupwindow.isShowing()) {
                    RegistrationActivity.Spopupwindow.dismiss();
                }
                if (RegistrationActivity.Lpopupwindow != null && RegistrationActivity.Lpopupwindow.isShowing()) {
                    RegistrationActivity.Lpopupwindow.dismiss();
                }
                if (viewPager.getCurrentItem() == 0) {
                    pref.putInt(ProfileEntryActivity.this, U.SH_REGISTRATION_FLAG, 1);
                    pref.putString(ProfileEntryActivity.this, U.SH_REGISTRATION_TASK, "");
                    finish();
                } else {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private int predictPercentage() {
        int percentage = 0;

        if (!pref.getString(ProfileEntryActivity.this, U.SH_PHOTO).equals("")) {
            percentage++;

        }

        if (!pref.getString(ProfileEntryActivity.this, U.SH_NAME).equals("")) {
            percentage++;

        }

        if (!pref.getString(ProfileEntryActivity.this, U.SH_EMAIL).equals("")) {
            percentage++;

        }

        if (!pref.getString(ProfileEntryActivity.this, U.SH_MOBILE).equals("")) {
            percentage++;

        }

        if (!pref.getString(ProfileEntryActivity.this, U.SH_ALTERNATE_MOBILE).equals("")) {
            percentage++;

        }

        if (!pref.getString(ProfileEntryActivity.this, U.SH_NATIVE_LOCATION).equals("")) {
            percentage++;

        }

        if (!pref.getString(ProfileEntryActivity.this, U.SH_GENDER).equals("")) {
            percentage++;

        }

        if (!pref.getString(ProfileEntryActivity.this, U.SH_MARITAL_STATUS).equals("")) {
            percentage++;

        }

        if (!pref.getString(ProfileEntryActivity.this, U.SH_DOB).equals("")) {
            percentage++;

        }

        if (!pref.getString(ProfileEntryActivity.this, U.SH_AGE).equals("")) {
            percentage++;

        }

        if (!pref.getString(ProfileEntryActivity.this, U.SH_COURSE).equals("")) {
            percentage++;

        }

        if (!pref.getString(ProfileEntryActivity.this, U.SH_CERTIFIED_COURSE).equals("")) {
            percentage++;

        }

        if (!pref.getString(ProfileEntryActivity.this, U.SH_WORK_STATUS).equals("")) {
            percentage++;

        }

        if (!pref.getString(ProfileEntryActivity.this, U.SH_SKILLS).equals("")) {
            percentage++;

        } else if (!pref.getString(ProfileEntryActivity.this, U.SH_ADDED_SKILLS).equals("")) {
            percentage++;

        }

        if (!pref.getString(ProfileEntryActivity.this, U.SH_CATEGORY).equals("")) {
            percentage++;

        }

        if (!pref.getString(ProfileEntryActivity.this, U.SH_TITLE).equals("")) {
            percentage++;

        }

        if (!pref.getString(ProfileEntryActivity.this, U.SH_JOBLOCATION).equals("")) {
            percentage++;

        }

        if (!pref.getString(ProfileEntryActivity.this, U.SH_RESUME).equals("")) {
            percentage++;

        }
        return percentage;
    }

    @SuppressLint("SetTextI18n")
    public void showPercentage() {
        final int percentage = (int) (((float) predictPercentage() / 18f) * 100);
        progressbarPredict.setProgress(percentage);
        percentIndicator.setText(percentage + "% Profile Completed");
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return ContactFragment.newInstance();
            } else if (position == 1) {
                return PersonalFragment.newInstance();
            } else if (position == 2) {
                return QualificationFragment.newInstance();
            } else if (position == 3) {
                return OthersFragment.newInstance();
            }
            return null;
        }

        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            // POSITION_NONE makes it possible to reload the PagerAdapter
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return 4;
        }

    }
}
