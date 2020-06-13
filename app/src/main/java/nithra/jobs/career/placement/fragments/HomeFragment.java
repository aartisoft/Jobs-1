package nithra.jobs.career.placement.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import nithra.jobs.career.placement.MainActivity;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.activity.CategoryActivity;
import nithra.jobs.career.placement.activity.FilterActivity;
import nithra.jobs.career.placement.activity.OldPostActivity;
import nithra.jobs.career.placement.activity.PhotoJobsLoginActivity;
import nithra.jobs.career.placement.activity.RegistrationActivity;
import nithra.jobs.career.placement.activity.SearchActivity;
import nithra.jobs.career.placement.activity.Testimonial;
import nithra.jobs.career.placement.utills.CirclePageIndicator;
import nithra.jobs.career.placement.utills.L;
import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

/**
 * Created by nithra on 6/3/18.
 */

public class HomeFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    public static LinearLayout frontLay, fragHolder, ads;
    SharedPreference pref;
    private CardView btnEmp;
    private LinearLayout cardPrivate, cardState, cardCentral, cardFresher, cardPopularEmp, pagerLay;
    private CardView btnjobSearch, btnjobFilter;
    private Handler mHandler;
    private Runnable mRunnable;
    private OnPurchaseClick onPurchaseClick;
    private ImageView prev, next, pagePrev, pageNext;
    private HorizontalScrollView sv;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TextView Counts_privatee, Counts_state, Counts_central;
    private ViewPager pager;
    private CirclePageIndicator indicator;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onPurchaseClick = (OnPurchaseClick) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = new SharedPreference();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_lay, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        frontLay = view.findViewById(R.id.front_lay);
        fragHolder = view.findViewById(R.id.fragment_container);
        ads = view.findViewById(R.id.ads);

        frontLay = view.findViewById(R.id.front_lay);
        fragHolder = view.findViewById(R.id.fragment_container);

        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        indicator = view.findViewById(R.id.indicator);
        pagerLay = view.findViewById(R.id.pagerLay);

        prev = view.findViewById(R.id.prev);
        next = view.findViewById(R.id.next);
        pagePrev = view.findViewById(R.id.pagePrev);
        pageNext = view.findViewById(R.id.pageNext);
        cardPrivate = view.findViewById(R.id.cardPrivate);
        cardState = view.findViewById(R.id.cardState);
        cardCentral = view.findViewById(R.id.cardCentral);
        cardFresher = view.findViewById(R.id.cardFreshers);
        cardPopularEmp = view.findViewById(R.id.cardPopularEmp);
        btnEmp = view.findViewById(R.id.btnEmp);
        btnjobSearch = view.findViewById(R.id.btnjobsearch);
        btnjobFilter = view.findViewById(R.id.btnjobfilter);
        pager = view.findViewById(R.id.pager);

        Counts_privatee = view.findViewById(R.id.Counts_privatee);
        Counts_state = view.findViewById(R.id.Counts_state);
        Counts_central = view.findViewById(R.id.Counts_central);

        sv = view.findViewById(R.id.scroll_one);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity.homePagePosition = U.HOME_PAGE;

        if (getActivity() != null) {
            if (pref.getInt(getActivity(), U.SH_AD_PURCHASED) == 0) {
                if (U.isNetworkAvailable(getActivity())) {
                    ads.setVisibility(View.VISIBLE);
                    MainActivity.showAd(getActivity(), ads, false);
                }
            }
            if (MainActivity.sliderJSON != null && MainActivity.sliderJSON.size() == 0) {
                pagerLay.setVisibility(View.GONE);
            } else if (U.isNetworkAvailable(getActivity())) {
                pagerLay.setVisibility(View.VISIBLE);
            } else {
                pagerLay.setVisibility(View.GONE);
            }

            if (pref.getString(getActivity(), U.SH_USER_TYPE).equals(U.EMPLOYER)) {
                btnEmp.setVisibility(View.GONE);
            } else {
                btnEmp.setVisibility(View.VISIBLE);
            }

            setRecyclerView();

            if (MainActivity.isFirstTime == 0) {
                MainActivity.isFirstTime = 1;
                sv.post(new Runnable() {
                    @Override
                    public void run() {
                        sv.fullScroll(View.FOCUS_RIGHT);
                        CountDownTimer otptimer = new CountDownTimer(1000, 10) {
                            public void onTick(long millis) {
                                mRecyclerView.smoothScrollToPosition(7);
                            }

                            public void onFinish() {
                                if (getActivity() != null) {
                                    sv.smoothScrollTo(0, cardPrivate.getTop());
                                    mRecyclerView.smoothScrollToPosition(0);
                                }
                            }
                        }.start();
                    }
                });
            }

            setJobsCount();
        }

        cardPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setModeFragment(U.SH_FIRST_TIME_PRIVATE, "1");
            }
        });

        cardState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setModeFragment(U.SH_FIRST_TIME_STATE, "2");
            }
        });

        cardCentral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setModeFragment(U.SH_FIRST_TIME_CENTRAL, "3");
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sv.fullScroll(View.FOCUS_RIGHT);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sv.fullScroll(View.FOCUS_LEFT);
            }
        });

        pagePrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = mLayoutManager.findFirstVisibleItemPosition();
                int pos = position - 1;
                if (pos >= 0) {
                    Log.e("prev_pos", "" + pos);
                    mRecyclerView.smoothScrollToPosition(pos);
                } else {
                    mRecyclerView.smoothScrollToPosition(0);
                }
            }
        });

        pageNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = mLayoutManager.findLastVisibleItemPosition();
                Log.e("next_pos", "" + position);
                mRecyclerView.smoothScrollToPosition(position + 1);
            }
        });

        cardFresher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFreshersJobs();
            }
        });

        cardPopularEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPopularEmployerJobs();
            }
        });

        btnjobSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });

        btnjobFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FilterActivity.class));
            }
        });

        btnEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Log.e("EmployerLink", "" + pref.getInt(getContext(), U.SH_REMOTE_EMPLOYER_LINK));
                Log.e("vacancy", "" + pref.getInt(getContext(), U.SH_REMOTE_VACANCY));
                if (pref.getInt(getContext(), U.SH_REMOTE_EMPLOYER_LINK) == 0) {
                    onPurchaseClick.onUserTypeChangeClick();
                } else {
                    try {
                        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
                        intentBuilder.setExitAnimations(getActivity(), android.R.anim.slide_in_left,
                                android.R.anim.slide_out_right);
                        intentBuilder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
                        intentBuilder.build().launchUrl(getActivity(), Uri.parse(U.EMPLOYER_POSTING_LINK));

                    } catch (ActivityNotFoundException | NullPointerException e) {
                        e.printStackTrace();
                        try {
                            Intent i = new Intent();
                            i.setAction(Intent.ACTION_VIEW);
                            i.addCategory(Intent.CATEGORY_BROWSABLE);
                            i.setData(Uri.parse(U.EMPLOYER_POSTING_LINK));
                            startActivity(i);
                        } catch (ActivityNotFoundException e1) {
                            e1.printStackTrace();
                        }
                    }
                }*/

                if (U.isNetworkAvailable(getActivity())) {
                    Intent intent = new Intent(getActivity(), PhotoJobsLoginActivity.class);
                    startActivity(intent);
                } else {
                    L.t(getActivity(), U.INA);
                }

            }
        });
    }

    private int checkFirstTime(String key) {
        int value;
        if (pref.getString(getActivity(), key).equals(U.currentDate())) {
            value = 1;
        } else {
            value = 0;
            pref.putString(getActivity(), key, U.currentDate());
        }
        return value;
    }

    private void setModeFragment(String key, String mode) {
        frontLay.setVisibility(View.GONE);
        fragHolder.setVisibility(View.VISIBLE);
        ads.setVisibility(View.GONE);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction;
        if (fragmentManager != null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            JobListFragment cat = JobListFragment.newInstance(SU.FILTER, "", "",
                    "", "", "", "" + mode, "", "",
                    "" + checkFirstTime(key));
            fragmentTransaction.replace(R.id.fragment_container, cat, "HELLO");
            fragmentTransaction.commit();
        }

        setJobsCount();
    }

    private void setRecentJobsFragment() {
        frontLay.setVisibility(View.GONE);
        fragHolder.setVisibility(View.VISIBLE);
        ads.setVisibility(View.GONE);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction;
        if (fragmentManager != null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            JobListFragment cat = JobListFragment.newInstance(SU.GETRECENTJOBS, "", "");
            fragmentTransaction.replace(R.id.fragment_container, cat, "HELLO");
            fragmentTransaction.commit();
        }
    }

    private void setDistrictFragment() {
        frontLay.setVisibility(View.GONE);
        fragHolder.setVisibility(View.VISIBLE);
        ads.setVisibility(View.GONE);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction;
        if (fragmentManager != null) {
            fragmentTransaction = fragmentManager.beginTransaction();
//            JobListFragment cat = JobListFragment.newInstance(SU.GETPERSONALISEDJOBS, "", ,
//                    "", "", "", "", "", "", "1");
            JobListFragment cat = JobListFragment.newInstance(SU.GETPERSONALISEDJOBS, "", "");
            if (fragmentTransaction != null) {
                fragmentTransaction.replace(R.id.fragment_container, cat, "HELLO");
                fragmentTransaction.commit();
            }
        }
    }

    public void showInfoDialog() {
        if (getActivity() != null) {
            final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            }
            dialog.setContentView(R.layout.info_popup_consultancy);
            WebView infoText = dialog.findViewById(R.id.info_text);
            infoText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });

            String info = "<b><meta charset=\"utf-8\"><font color=purple size=2>\n" +
                    getResources().getString(R.string.consultancy_text) + "<br><br></font></b>";

            U.webview(getActivity(), info, infoText);

            dialog.findViewById(R.id.text_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pref.putInt(getActivity(), U.SH_CONSULTANCY_INFO_SHOW, 1);
                    setModeFragment(U.SH_FIRST_TIME_CONSULTANCY, "4");
                    dialog.dismiss();
                }
            });

            dialog.findViewById(R.id.text_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    private void homeBannerClick() {
        if (U.isNetworkAvailable(getActivity())) {
            if (U.HOME_PAGE_IMAGE_REDIRECT_URL.substring(0, 4).equals("http")) {
                U.custom_tabs(getActivity(), U.HOME_PAGE_IMAGE_REDIRECT_URL);
            } else {
                String web = "http://" + U.HOME_PAGE_IMAGE_REDIRECT_URL;
                U.custom_tabs(getActivity(), web);
            }
        } else {
            L.t(getActivity(), U.INA);
        }
    }

    private void callAllJobs() {
        frontLay.setVisibility(View.GONE);
        fragHolder.setVisibility(View.VISIBLE);
        ads.setVisibility(View.GONE);

        FragmentTransaction localFragmentTransaction = null;
        if (getFragmentManager() != null) {
            localFragmentTransaction = getFragmentManager().beginTransaction();
        }
        if (localFragmentTransaction != null) {
            localFragmentTransaction.replace(R.id.fragment_container, JobListFragment.newInstance(U.ALLJOBS, "", "3"), "HELLO");
            localFragmentTransaction.commit();
        }
    }

    private void callFreshersJobs() {
        frontLay.setVisibility(View.GONE);
        fragHolder.setVisibility(View.VISIBLE);
        ads.setVisibility(View.GONE);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction;
        if (fragmentManager != null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            JobListFragment cat = JobListFragment.newInstance(SU.FILTER, "", "",
                    "", "100,101", "", "", "", "", "" + checkFirstTime(U.SH_FIRST_TIME_FRESHERS));
            if (fragmentTransaction != null) {
                fragmentTransaction.replace(R.id.fragment_container, cat, "HELLO");
                fragmentTransaction.commit();
            }
        }
    }

    private void callPopularEmployerJobs() {
        frontLay.setVisibility(View.GONE);
        fragHolder.setVisibility(View.VISIBLE);
        ads.setVisibility(View.GONE);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction;
        if (fragmentManager != null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            JobListFragment cat = JobListFragment.newInstance(SU.POPULAR_EMP, "", "");
            if (fragmentTransaction != null) {
                fragmentTransaction.replace(R.id.fragment_container, cat, "HELLO");
                fragmentTransaction.commit();
            }
        }
    }

    private void callRegistration() {
        if (U.isNetworkAvailable(getActivity())) {
            Intent intent = new Intent(getActivity(), RegistrationActivity.class);
            intent.putExtra("task", "Edit");
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), U.INA, Toast.LENGTH_SHORT).show();
        }
    }

    private void slideInFab(final FloatingActionButton mFab) {

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mFab.getLayoutParams();
        float dy = mFab.getWidth() + lp.rightMargin;

        mFab.show();
        mFab.animate()
                .setStartDelay(0)
                .setDuration(500)
                .setInterpolator(new FastOutLinearInInterpolator())
                .translationX(-lp.rightMargin)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(final Animator animation) {
                        super.onAnimationEnd(animation);
                        mHandler = new Handler();
                        mRunnable = new Runnable() {
                            @Override
                            public void run() {
                                slideOutFab(mFab);
                            }
                        };
                        mHandler.postDelayed(mRunnable, 3500);
                    }
                })
                .start();
    }

    private void slideOutFab(final FloatingActionButton mFab) {

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mFab.getLayoutParams();
        mFab.animate()
                .setStartDelay(0)
                .setDuration(500)
                .setInterpolator(new FastOutLinearInInterpolator())
                .translationX(mFab.getWidth() - lp.rightMargin)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(final Animator animation) {
                        super.onAnimationEnd(animation);
                        mFab.show();
                        mHandler = new Handler();
                        mRunnable = new Runnable() {
                            @Override
                            public void run() {
                                slideInFab(mFab);
                            }
                        };
                        mHandler.postDelayed(mRunnable, 3500);
                    }
                })
                .start();
    }

    @Override
    public void onResume() {
        super.onResume();
        setSlider();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void setRecyclerView() {
        if (getActivity() != null) {
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);
            RecyclerTaskAdapter mAdapter = new RecyclerTaskAdapter(getActivity());
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setJobsCount() {
        if (!pref.getString(getActivity(), U.SH_FIRST_TIME_PRIVATE).equals(U.currentDate())) {
            String privateJobCount = pref.getString(getActivity(), U.SH_PRIVATE_JOBS_COUNT);
            if (!privateJobCount.isEmpty()) {
                Counts_privatee.setText(privateJobCount + " +");
                Animation blinking = AnimationUtils.loadAnimation(getActivity(), R.anim.blinking);
                Counts_privatee.setAnimation(blinking);
            }
        } else {
            Counts_privatee.setVisibility(View.GONE);
        }

        if (!pref.getString(getActivity(), U.SH_FIRST_TIME_STATE).equals(U.currentDate())) {
            String stateJobCount = pref.getString(getActivity(), U.SH_STATE_JOBS_COUNT);
            if (!stateJobCount.isEmpty()) {
                Counts_state.setText(stateJobCount + " +");
                Animation blinking = AnimationUtils.loadAnimation(getActivity(), R.anim.blinking);
                Counts_state.setAnimation(blinking);
            }
        } else {
            Counts_state.setVisibility(View.GONE);
        }

        if (!pref.getString(getActivity(), U.SH_FIRST_TIME_CENTRAL).equals(U.currentDate())) {
            String centralJobCount = pref.getString(getActivity(), U.SH_CENTRAL_JOBS_COUNT);
            if (!centralJobCount.isEmpty()) {
                Counts_central.setText(centralJobCount + " +");
                Animation blinking = AnimationUtils.loadAnimation(getActivity(), R.anim.blinking);
                Counts_central.setAnimation(blinking);
            }
        } else {
            Counts_central.setVisibility(View.GONE);
        }
    }

    private void setSlider() {
        Log.e("slider", "called");
        if (getActivity() != null) {
            pagerAdapter adapter = new pagerAdapter(getChildFragmentManager());
            pager.setAdapter(adapter);
            pager.setClipToPadding(false);
            pager.setPadding(50, 0, 50, 0);
            indicator.setViewPager(pager);
        }
    }

    public interface OnPurchaseClick {
        void onPurchaseItemClick();

        void onNextPageItemClick();

        void onUserTypeChangeClick();
    }

    class pagerAdapter extends FragmentPagerAdapter {

        public pagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return HomePagerFragment.newInstance(MainActivity.sliderJSON.get(position));
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
            return MainActivity.sliderJSON.size();
        }

    }

    //---------------------------------------- Adapter ---------------------------------------------

    public class RecyclerTaskAdapter extends RecyclerView.Adapter<RecyclerTaskAdapter.DataObjectHolder> {

        Context context;
        String[] textList;
        int[] imageList;
        int[] positionList;

        RecyclerTaskAdapter(Context context) {
            this.context = context;
            if (getActivity() != null)
                if (pref.getString(getActivity(), U.SH_USER_TYPE).equals(U.EMPLOYER)) {
                    textList = new String[]{"அனைத்து வேலை\nவாய்ப்புகளையும்\nகாண", "அரசு\nபணித்தேர்வுகள்\nபற்றிய விபரங்கள்"
                            , "வேலைவாய்ப்பு\nசார்ந்த பயனுள்ள\nகுறிப்புகள்", "நீங்கள்\nபார்வையிட்ட\nவேலைவாய்ப்புகள்", "  விளம்பரத்தை  \nநீக்க\n", "நித்ராவின் மூலம்\nவேலைவாய்ப்பு\nபெற்றவர்கள்"};
                    imageList = new int[]{R.drawable.all_jobs_icon, R.drawable.govt_exam_icon, R.drawable.oldpost_icon,
                            R.drawable.recently_view_jobs, R.drawable.emp_home_icon, R.drawable.testimonial_icon};
                    positionList = new int[]{0, 3, 4, 5, 6, 7};
                } else {
                    textList = new String[]{"அனைத்து வேலை\nவாய்ப்புகளையும்\nகாண", "உங்களுக்கான\nவேலைவாய்ப்புகள்\n", "உங்கள் தகுதி,\n திறமைக்கான\nவேலைவாய்ப்புகள்", "அரசு\nபணித்தேர்வுகள்\nபற்றிய விபரங்கள்"
                            , "வேலைவாய்ப்பு\nசார்ந்த பயனுள்ள\nகுறிப்புகள்", "நீங்கள்\nபார்வையிட்ட\nவேலைவாய்ப்புகள்", "  விளம்பரத்தை  \nநீக்க\n", "நித்ராவின் மூலம்\nவேலைவாய்ப்பு\nபெற்றவர்கள்"};
                    imageList = new int[]{R.drawable.all_jobs_icon, R.drawable.preff_job_loc_icon, R.drawable.rjobs_icon, R.drawable.govt_exam_icon,
                            R.drawable.oldpost_icon, R.drawable.recently_view_jobs, R.drawable.emp_home_icon, R.drawable.testimonial_icon};
                    positionList = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
                }
        }

        @NonNull
        @Override
        public RecyclerTaskAdapter.DataObjectHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_option_lay, viewGroup, false);
            return new RecyclerTaskAdapter.DataObjectHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull final DataObjectHolder holder, final int position) {
            holder.image.setImageResource(imageList[position]);
            holder.text.setText(textList[position]);

            if (position == 0) {
                if (!pref.getString(getActivity(), U.SH_FIRST_TIME_ALL_JOBS).equals(U.currentDate())) {
                    String allJobCount = pref.getString(getActivity(), U.SH_ALL_JOBS_COUNT);
                    if (!allJobCount.isEmpty()) {
                        Animation blinking = AnimationUtils.loadAnimation(context, R.anim.blinking);
                        holder.Counts_text.setVisibility(View.VISIBLE);
                        holder.Counts_text.setText("" + allJobCount + " +");
                        holder.Counts_text.setAnimation(blinking);
                    }
                } else {
                    holder.Counts_text.setVisibility(View.GONE);
                }

            } else {
                holder.Counts_text.setVisibility(View.GONE);
            }

            holder.parentLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (positionList[position]) {
                        case 0:
                            callAllJobs();
                            checkFirstTime(U.SH_FIRST_TIME_ALL_JOBS);
                            holder.Counts_text.setVisibility(View.GONE);
                            break;
                        case 1:
//                            String district = pref.getString(getActivity(), U.SH_USER_DISTRICT_NAME);
//                            String[] districtArray = district.split(",");
//                            if (districtArray.length == 1) {
                            setDistrictFragment();
//                            } else {
//                                showDistrictDialog();
//                            }
                            break;
                        case 2:
                            if (!pref.getBoolean(getActivity(), U.SH_SIGN_UP_SUCCESS)) {
                                callRegistration();
                            } else {
                                onPurchaseClick.onNextPageItemClick();
                            }
                            break;
                        case 3:
                            startActivity(new Intent(getActivity(), CategoryActivity.class));
                            break;
                        case 4:
                            if (U.isNetworkAvailable(getActivity())) {
                                startActivity(new Intent(getActivity(), OldPostActivity.class));
                            } else {
                                Toast.makeText(getActivity(), U.INA, Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 5:
                            setRecentJobsFragment();
                            break;
                        case 6:
                            onPurchaseClick.onPurchaseItemClick();
                            break;
                        case 7:
                            if (U.isNetworkAvailable(getActivity())) {
                                startActivity(new Intent(getActivity(), Testimonial.class));
                            } else {
                                Toast.makeText(getActivity(), U.INA, Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return textList.length;
        }

        class DataObjectHolder extends RecyclerView.ViewHolder {

            ImageView image;
            TextView text, Counts_text;
            LinearLayout parentLay;

            DataObjectHolder(View view) {
                super(view);
                image = view.findViewById(R.id.image);
                text = view.findViewById(R.id.text);
                Counts_text = view.findViewById(R.id.Counts_text);
                parentLay = view.findViewById(R.id.parentLay);
            }
        }
    }
}
