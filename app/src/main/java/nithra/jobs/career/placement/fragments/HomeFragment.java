package nithra.jobs.career.placement.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import nithra.jobs.career.placement.MainActivity;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.activity.CategoryActivity;
import nithra.jobs.career.placement.activity.OldPostActivity;
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
    TextView btnposting, btnGovtExams, btnOldpost, btnBrowse;
    LinearLayout cardPrivate, cardState, cardCentral, cardConsultancy, cardFresher;
    CustomTabsClient mCustomTabsClient;
    CustomTabsSession mCustomTabsSession;
    CustomTabsServiceConnection mCustomTabsServiceConnection;
    CustomTabsIntent mCustomTabsIntent;
    final String CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        pref = new SharedPreference();
        frontLay = view.findViewById(R.id.front_lay);
        fragHolder = view.findViewById(R.id.fragment_container);
        ads = view.findViewById(R.id.ads);
        cardPrivate = view.findViewById(R.id.cardPrivate);
        cardState = view.findViewById(R.id.cardState);
        cardCentral = view.findViewById(R.id.cardCentral);
        cardConsultancy = view.findViewById(R.id.cardConsultancy);
        cardFresher = view.findViewById(R.id.cardFreshers);
        btnBrowse = view.findViewById(R.id.btnBrowse);
        btnposting = view.findViewById(R.id.btnposting);
        btnGovtExams = view.findViewById(R.id.govtExams);
        btnOldpost = view.findViewById(R.id.oldpost);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity.homePagePosition = U.HOME_PAGE;

        if (U.isNetworkAvailable(getActivity())) {
            ads.setVisibility(View.VISIBLE);
            MainActivity.showAd(getActivity(), ads, false);
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
        cardConsultancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref.getInt(getActivity(), U.SH_CONSULTANCY_INFO_SHOW) == 0) {
                    showInfoDialog();
                } else {
                    setModeFragment(U.SH_FIRST_TIME_CONSULTANCY, "4");
                }

            }
        });
        cardFresher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFreshersJobs();
            }
        });

        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAllJobs();
            }
        });

        btnposting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employerPost();
            }
        });

        btnGovtExams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CategoryActivity.class));
            }
        });
        btnOldpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (U.isNetworkAvailable(getActivity())) {
                    startActivity(new Intent(getActivity(), OldPostActivity.class));
                } else {
                    Toast.makeText(getActivity(), U.INA, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public int checkFirstTime(String key) {
        int value;
        if (pref.getString(getActivity(), key).equals(U.currentDate())) {
            value = 1;
        } else {
            value = 0;
            pref.putString(getActivity(), key, U.currentDate());
        }
        return value;
    }

    public void setModeFragment(String key, String mode) {
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

    public void employerPost() {
        if (U.isNetworkAvailable(getActivity())) {
            mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
                @Override
                public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                    mCustomTabsClient = customTabsClient;
                    mCustomTabsClient.warmup(0L);
                    mCustomTabsSession = mCustomTabsClient.newSession(null);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    mCustomTabsClient = null;
                }
            };

            CustomTabsClient.bindCustomTabsService(getActivity(), CUSTOM_TAB_PACKAGE_NAME, mCustomTabsServiceConnection);

            mCustomTabsIntent = new CustomTabsIntent.Builder(mCustomTabsSession)
                    .setShowTitle(true)
                    .setToolbarColor(getActivity().getResources().getColor(R.color.colorPrimary))
                    .build();
            mCustomTabsIntent.launchUrl(getActivity(), Uri.parse(SU.JOBS_POSTING));
        } else {
            L.t(getActivity(), U.INA);
        }
    }

    public void callAllJobs() {
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

    public void callFreshersJobs() {
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

    @Override
    public void onResume() {
        super.onResume();
    }

}
