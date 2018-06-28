package nithra.jobs.career.placement.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;

import nithra.jobs.career.placement.FragmentInterface;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.fragments.ContactFragment;
import nithra.jobs.career.placement.fragments.OthersFragment;
import nithra.jobs.career.placement.fragments.PersonalFragment;
import nithra.jobs.career.placement.fragments.QualificationFragment;
import nithra.jobs.career.placement.pojo.ArrayItem;
import nithra.jobs.career.placement.utills.FlexboxLayout;
import nithra.jobs.career.placement.utills.L;
import nithra.jobs.career.placement.utills.Pinview;
import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

/**
 * Created by nithra-apps on 27/1/18.
 */

public class ProfileEntryActivity extends AppCompatActivity {

    SharedPreference pref;
    TabLayout tabLayout;
    ViewPager viewPager;
    LinearLayout back, next;
    TextView nextText, backText;
    int notifyImg = R.drawable.profile;
    int notifyImg_light = R.drawable.profile_light;
    int current_page = 0;
    ViewPagerAdapter adapter;
    String task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        task = getIntent().getStringExtra("task");
        pref = new SharedPreference();
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
                if (fragment != null) {
                    fragment.fragmentBecameVisible();
                }
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

        if ((task.equals(U.SH_JOBLOCATION)) || (task.equals(U.SH_SKILLS))) {
            task = SU.UPDATE;
            viewPager.setCurrentItem(3);
            notifyImg = R.drawable.edit;
            notifyImg_light = R.drawable.edit_light;
        } else if (task.equals(SU.REGISTER)) {
            notifyImg = R.drawable.profile;
            notifyImg_light = R.drawable.profile_light;
        } else if (task.equals(SU.UPDATE)) {
            notifyImg = R.drawable.edit;
            notifyImg_light = R.drawable.edit_light;
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

    //-----------------------------------------  profile Submit ------------------------------------

    private void checkValuesForSubmit(String task) {
        if (pref.getString(ProfileEntryActivity.this, U.SH_NAME).equals("")) {
            viewPager.setCurrentItem(0);
            L.t(this, "Enter Your Name");
        } else if (pref.getString(ProfileEntryActivity.this, U.SH_EMAIL).equals("")) {
            viewPager.setCurrentItem(0);
            L.t(this, "Enter Your Email");
        } else if (pref.getString(ProfileEntryActivity.this, U.SH_MOBILE).equals("")) {
            viewPager.setCurrentItem(0);
            L.t(this, "Enter Your MobileNumber");
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
        } else if (pref.getString(ProfileEntryActivity.this, U.SH_PRIVACY_POLICY).equals("") ||
                pref.getString(ProfileEntryActivity.this, U.SH_PRIVACY_POLICY).equals("0")) {
            viewPager.setCurrentItem(3);
            L.t(this, "Read & Tick Privacy Policy");
        } else if (pref.getString(ProfileEntryActivity.this, U.SH_TERMS_CONDITIONS).equals("") ||
                pref.getString(ProfileEntryActivity.this, U.SH_TERMS_CONDITIONS).equals("0")) {
            viewPager.setCurrentItem(3);
            L.t(this, "Read & Tick Terms & conditions");
        } else {
            pref.putInt(ProfileEntryActivity.this, U.SH_REGISTRATION_FLAG, 1);
            pref.putString(ProfileEntryActivity.this, U.SH_REGISTRATION_TASK, task);
            finish();
        }
    }

    private void checkContactValues() {
        if (pref.getString(ProfileEntryActivity.this, U.SH_NAME).equals("")) {
            viewPager.setCurrentItem(0);
            L.t(ProfileEntryActivity.this, "Enter Your Name");
        } else if (pref.getString(ProfileEntryActivity.this, U.SH_EMAIL).equals("")) {
            viewPager.setCurrentItem(0);
            L.t(ProfileEntryActivity.this, "Enter Your Email");
        } else if (pref.getString(ProfileEntryActivity.this, U.SH_MOBILE).equals("")) {
            viewPager.setCurrentItem(0);
            L.t(ProfileEntryActivity.this, "Enter Your MobileNumber");
        } else if (pref.getString(ProfileEntryActivity.this, U.SH_GENDER).equals("")) {
            viewPager.setCurrentItem(1);
            L.t(ProfileEntryActivity.this, "Choose Your Gender");
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

}
