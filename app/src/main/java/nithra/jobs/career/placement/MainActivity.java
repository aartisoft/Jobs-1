package nithra.jobs.career.placement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
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
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import nithra.jobs.career.placement.Billing.BillingManager;
import nithra.jobs.career.placement.Billing.BillingProvider;
import nithra.jobs.career.placement.activity.CategoryActivity;
import nithra.jobs.career.placement.activity.EmployerHomeActivity;
import nithra.jobs.career.placement.activity.FilterActivity;
import nithra.jobs.career.placement.activity.JobDetailActivity;
import nithra.jobs.career.placement.activity.NotificationActivity;
import nithra.jobs.career.placement.activity.OldPostActivity;
import nithra.jobs.career.placement.activity.PhotoJobsLoginActivity;
import nithra.jobs.career.placement.activity.RegistrationActivity;
import nithra.jobs.career.placement.activity.SavedNotification;
import nithra.jobs.career.placement.activity.SearchActivity;
import nithra.jobs.career.placement.activity.SettingsActivity;
import nithra.jobs.career.placement.activity.Testimonial;
import nithra.jobs.career.placement.adapters.TestAdapter;
import nithra.jobs.career.placement.biling_code.AcquireFragment;
import nithra.jobs.career.placement.biling_code.MainViewController;
import nithra.jobs.career.placement.engine.DBHelper;
import nithra.jobs.career.placement.engine.LocalDB;
import nithra.jobs.career.placement.fragments.CategoryWiseFragment;
import nithra.jobs.career.placement.fragments.DistrictWiseFragment;
import nithra.jobs.career.placement.fragments.HomeFragment;
import nithra.jobs.career.placement.fragments.JobListFragment;
import nithra.jobs.career.placement.fragments.RecommendedFragment;
import nithra.jobs.career.placement.fragments.ReminderListFragment;
import nithra.jobs.career.placement.helper.AppSignatureHashHelper;
import nithra.jobs.career.placement.networking.MySingleton;
import nithra.jobs.career.placement.pojo.ArrayItem;
import nithra.jobs.career.placement.pojo.Item;
import nithra.jobs.career.placement.pojo.Jobs;
import nithra.jobs.career.placement.receivers.DailyNotifyReceiver;
import nithra.jobs.career.placement.utills.CustomViewPager;
import nithra.jobs.career.placement.utills.L;
import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static nithra.jobs.career.placement.Billing.BillingManager.BILLING_MANAGER_NOT_INITIALIZED;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnPurchaseClick, BillingProvider, InstallReferrerStateListener {

    // Remote Config keys
    private static final String LOADING_PHRASE_CONFIG_AD_KEY = "adtype";
    private static final String LOADING_PHRASE_CONFIG_VACANCY_KEY = "JOB_VACANCY_TITLE";
    private static final String LOADING_PHRASE_CONFIG_POSTEDBY_KEY = "JOB_POSTED_BY";
    private static final String LOADING_PHRASE_CONFIG_LOCATIONTAB_KEY = "Native_Job_Tab";
    private static final String LOADING_PHRASE_CONFIG_PHONE_NUMBER_KEY = "JOB_PHONE_NO_ISSHOW";
    private static final String LOADING_PHRASE_CONFIG_EMPLOYER_PART_KEY = "Is_Post_Job_App";
    private static final String DIALOG_TAG = "dialog";
    public static ViewPagerAdapter adapter;
    public static boolean listads_loaded = false;
    public static CustomViewPager viewPager;
    @SuppressLint("StaticFieldLeak")
    public static TabLayout tabLayout;
    public static int adPosition;
    public static int homePagePosition = -2;
    public static int isFirstTime;
    public static List<Jobs> sliderJSON;
    public static com.facebook.ads.NativeBannerAd nativeBannerAd;
    public static com.facebook.ads.NativeAdLayout nativeAdLayout;
    @SuppressLint("StaticFieldLeak")
    static CoordinatorLayout coordinatorLayout;
    @SuppressLint("StaticFieldLeak")
    static NativeContentAdView contadView = null;
    @SuppressLint("StaticFieldLeak")
    static NativeAppInstallAdView insadView = null;
    @SuppressLint("StaticFieldLeak")
    static FrameLayout adFrameLayout;
    static PublisherAdView adView;
    public AcquireFragment mAcquireFragment;
    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    ArrayList<Item> userDistricts, userCities;
    ArrayList<ArrayItem> userNativeLocation;
    ArrayList<String> selectedDistrict;
    ArrayList<String> selectedDistrictName;
    ImageView notifyIcon, searchIcon, profileIcon;
    RelativeLayout noti_lay;
    SharedPreference pref;
    AppUpdateManager appUpdateManager;
    LocalDB localDB;
    PopupWindow popupwindow;
    String mode = "";
    // GCM
    SQLiteDatabase myDB;
    Handler mHandler;
    Runnable mRunnable;
    Cursor notificationCursor = null;
    CallbackManager callbackManager;
    TextView notifyCount, txtToolTitle, noText;
    BroadcastReceiver mRegistrationBroadcastReceiver;
    String nativeLocationName = "", nativeLocation = "", nativeDistrict = "", whatsNew = "";
    Dialog districtDia, nativeLocationDia, infoDia, optionDialog, frstScreenDia, registerDia;
    Button button;
    SpinKitView progressBar;
    InstallReferrerClient mReferrerClient;
    String source = "", medium = "", comp = "";
    PublisherInterstitialAd interstitialAd_noti;
    Dialog exitDia = null;
    private String distId = "", distName = "";
    private int exit = 0;
    private BillingManager mBillingManager;
    private MainViewController mViewController;

    public static void load_ads_from_main(Context context, LinearLayout ads_lay) {
        try {
            if (adView != null) {
                ViewGroup parentViewGroup = (ViewGroup) adView.getParent();
                if (parentViewGroup != null) {
                    parentViewGroup.removeAllViews();
                }

                ads_lay.removeAllViews();
                ads_lay.addView(adView);
                ads_lay.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            System.out.println("Ad Exception date " + e);
        }
    }

    public static void showAd(Context context, final LinearLayout layout, final Boolean flag) {
        System.out.println("Layout called .....");

        AdSize adSize = new AdSize(320, 50);
        adView = new PublisherAdView(context);
        adView.setAdSizes(AdSize.SMART_BANNER);
        adView.setAdUnitId("/21634001759/JOBS001");//employee
        try {
            layout.addView(adView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        adView.setAdListener(new com.google.android.gms.ads.AdListener() {
            public void onAdLoaded() {
                //load_ads_from_main(Main_open.this, ads_lay);
                try {
                    System.out.println("layout visible ... ");
                    if (flag) {
                        layout.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("------------ad ok");
                super.onAdLoaded();
            }

            public void onAdFailedToLoad(int errorcode) {
                System.out.println("------------ad faild : " + errorcode);
            }
        });
        PublisherAdRequest request = new PublisherAdRequest.Builder().build();
        adView.loadAd(request);
    }


    public static void showAdaptiveAd(Context context, final LinearLayout layout, final Boolean flag) {
        System.out.println("Layout called .....");
        AdView adView = new AdView(context);
        adView.setAdUnitId(U.ADAPTIVE_BANNER_AD);
        adView.setAdSize(getAdSize(context));

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

    private static AdSize getAdSize(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        return AdSize.getCurrentOrientationBannerAdSizeWithWidth(context, adWidth);
    }

    public static void loadFaceBookBanner(Context context, final LinearLayout ad_layout) {

        final TextView tv = new TextView(context);
        tv.setBackground(context.getResources().getDrawable(R.drawable.adv_banner_sample));
        ad_layout.addView(tv);

        final com.facebook.ads.AdView adView = new com.facebook.ads.AdView(context, U.FB_BANNER, com.facebook.ads.AdSize.BANNER_HEIGHT_50);

        // Add the ad view to your activity layout
        adView.setAdListener(new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                System.out.println(adError.getErrorCode() + "load failed" + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                ViewGroup parentViewGroup = (ViewGroup) adView.getParent();
                if (parentViewGroup != null) {
                    parentViewGroup.removeAllViews();
                }
                ad_layout.setVisibility(View.VISIBLE);
                ad_layout.removeAllViews();
                ad_layout.addView(adView);
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });

        // Request an ad
        adView.loadAd();

    }

    public static void loadNativeAd(final Context context) {
        final SharedPreference pref = new SharedPreference();
        nativeBannerAd = new com.facebook.ads.NativeBannerAd(context, U.FB_LIST_NATIVE);
        pref.putInt(context, "native_add_list_fb", 0);
        nativeBannerAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
                Log.e("TAG", "Native ad finished downloading all assets.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
                Log.e("TAG", "Native ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Native ad is loaded and ready to be displayed
                Log.d("TAG", "Native ad is loaded and ready to be displayed!");
                if (nativeBannerAd == null || nativeBannerAd != ad) {
                    return;
                }
                // Inflate Native Ad into Container
                listads_loaded = true;
                pref.putInt(context, "native_add_list_fb", 1);
                inflateAd(context);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
                Log.d("TAG", "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
                Log.d("TAG", "Native ad impression logged!");
            }
        });
        nativeBannerAd.loadAd();
    }

    public static void inflateAd(Context context) {

        nativeBannerAd.unregisterView();

        nativeAdLayout = new NativeAdLayout(context);
        // Add the Ad view into the ad container.
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        View adView = inflater.inflate(R.layout.fb_native_list, null);
        nativeAdLayout.addView(adView);

        // Add the AdChoices icon
        RelativeLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(context, nativeBannerAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        AdIconView nativeAdIconView = adView.findViewById(R.id.native_icon_view);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
        sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());

        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        nativeBannerAd.registerViewForInteraction(adView, nativeAdIconView, clickableViews);
    }

    public static void load_FbaddFromMainList(final Context context, final FrameLayout add_banner) {
        SharedPreference sp = new SharedPreference();
        if (listads_loaded) {
            try {
                if (nativeAdLayout != null) {
                    ViewGroup parentViewGroup = (ViewGroup) nativeAdLayout.getParent();
                    if (parentViewGroup != null) {
                        parentViewGroup.removeAllViews();
                    }
                }
                if (sp.getInt(context, "native_add_list_fb") == 1) {
                    add_banner.setVisibility(View.VISIBLE);
                    add_banner.removeAllViews();
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10, 10, 10, 10);
                    add_banner.setLayoutParams(params);
                    add_banner.addView(nativeAdLayout);
                }
            } catch (Exception e) {
                Log.e("catch", "" + e);
            }
        }
    }

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
                            Log.e("error", "" + e);
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
                Log.e("catch", "" + e);
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
                Log.e("AdErrorResponse", "" + errorCode);
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
        adView.setPriceView(adView.findViewById(R.id.appinstall_price));
        adView.setStarRatingView(adView.findViewById(R.id.appinstall_stars));
        adView.setStoreView(adView.findViewById(R.id.appinstall_store));

        // The MediaView will display a video asset if one is present in the ad, and the first image
        // asset otherwise.
        /*MediaView mediaView = adView.findViewById(R.id.appinstall_media);
        adView.setMediaView(mediaView);*/

        // Some assets are guaranteed to be in every NativeAppInstallAd.
        ((TextView) adView.getStoreView()).setText(nativeAppInstallAd.getStore());
        ((TextView) adView.getHeadlineView()).setText(nativeAppInstallAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAppInstallAd.getBody());
        ((Button) adView.getCallToActionView()).setText("Install");
//        ((Button) adView.getCallToActionView()).setText(nativeAppInstallAd.getCallToAction());

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
        if (nativeAppInstallAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAppInstallAd.getPrice());
        }


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

    //--------------------------------------- Refferer ---------------------------------------------

    public static void load_addFromMain(Context context, FrameLayout add_banner) {
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
                Log.e("AdErrorResponse", "install");
            } else if (contadView != null) {
                add_banner.addView(contadView);
                Log.e("AdErrorResponse", "Content");
            } else {
                Log.e("AdErrorResponse", "Error");
                TextView dummyView = new TextView(context);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(MATCH_PARENT, 1);
                dummyView.setLayoutParams(params);
                add_banner.setBackgroundColor(context.getResources().getColor(R.color.white));
                add_banner.addView(dummyView);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void add_ads(Context context, List<Jobs> list) {
        SharedPreference pref = new SharedPreference();
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isAd) {
                    list.remove(i);
                    Log.e("position_ad_remove", "" + i);
                }

                if (list.get(i).isRjobs) {
                    list.remove(i);
                    Log.e("position_rjobs_remove", "" + i);
                }

                if (list.get(i).isRateus) {
                    list.remove(i);
                    Log.e("position_rateus_remove", "" + i);
                }
            }

            int vall = 0;
            for (int i = 0; i < list.size(); i++) {

                if (i == adPosition) {
                    if (pref.getInt(context, U.SH_AD_PURCHASED) == 0) {
                        Jobs jobs = new Jobs();
                        jobs.setId(0);
                        jobs.setImage("");
                        jobs.setJobtype("0");
                        jobs.setJobtitle("");
                        jobs.setJobtitleId("");
                        jobs.setEmployer("");
                        jobs.setNoofvacancy("");
                        jobs.setVerified("");
                        jobs.setDate("");
                        jobs.setDatediff(0);
                        jobs.setDescription("");
                        jobs.setDistrict("");
                        jobs.setRead(false);
                        jobs.setAd(true);
                        list.add(i, jobs);
                    }
                }

                if (i > adPosition) {
                    if (i % 30 == 0) {
                        Jobs jobs = new Jobs();
                        jobs.setId(0);
                        jobs.setImage("");
                        jobs.setJobtype("0");
                        jobs.setJobtitle("");
                        jobs.setJobtitleId("");
                        jobs.setEmployer("");
                        jobs.setNoofvacancy("");
                        jobs.setVerified("");
                        jobs.setDate("");
                        jobs.setDatediff(0);
                        jobs.setDescription("");
                        jobs.setDistrict("");
                        jobs.setRead(false);
                        jobs.setRateus(true);
                        list.add(i, jobs);
                        Log.e("position_rateus", "" + i);
                    } else if (i % 20 == 0) {
                        if (!pref.getBoolean(context, U.SH_SIGN_UP_SUCCESS)) {
                            Jobs jobs = new Jobs();
                            jobs.setId(0);
                            jobs.setImage("");
                            jobs.setJobtype("0");
                            jobs.setJobtitle("");
                            jobs.setJobtitleId("");
                            jobs.setEmployer("");
                            jobs.setNoofvacancy("");
                            jobs.setVerified("");
                            jobs.setDate("");
                            jobs.setDatediff(0);
                            jobs.setDescription("");
                            jobs.setDistrict("");
                            jobs.setRead(false);
                            jobs.setRjobs(true);
                            list.add(i, jobs);
                            Log.e("position_Rjobs", "" + i);
                        }
                    }

                    if (vall == 5) {
                        vall = 0;
                        if (pref.getInt(context, U.SH_AD_PURCHASED) == 0) {
                            Jobs jobs = new Jobs();
                            jobs.setId(0);
                            jobs.setImage("");
                            jobs.setJobtype("0");
                            jobs.setJobtitle("");
                            jobs.setJobtitleId("");
                            jobs.setEmployer("");
                            jobs.setNoofvacancy("");
                            jobs.setVerified("");
                            jobs.setDate("");
                            jobs.setDatediff(0);
                            jobs.setDescription("");
                            jobs.setDistrict("");
                            jobs.setRead(false);
                            jobs.setAd(true);
                            list.add(i, jobs);
                            Log.e("position_ad", "" + i);
                        }
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

    //----------------------------------- Initial Info Dialog --------------------------------------


    // Option Screen


    // Info Agree Screen

    public void ads_manager_interstistial() {
        interstitialAd_noti = new PublisherInterstitialAd(this);
        interstitialAd_noti.setAdUnitId("/21634001759/JOBS001");
        interstitialAd_noti.loadAd(new PublisherAdRequest.Builder().build());
        interstitialAd_noti.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                //exit_dia();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_sign_up);

        pref = new SharedPreference();
        localDB = new LocalDB(this);
        isFirstTime = 0;

        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, U.ADMOB_APP_ID);

        Log.e("androidId", "" + U.getAndroidId(MainActivity.this));

        pref = new SharedPreference();

        try {
            mode = getIntent().getStringExtra("mode");
            if (mode == null) {
                mode = "";
            }
        } catch (Exception e) {
            mode = "";
            e.printStackTrace();
        }

        Log.e("mode", "" + mode);

        if (U.isNetworkAvailable(MainActivity.this)) {
            if (pref.getInt(MainActivity.this, "referrerCheck") == 0) {
                mReferrerClient = InstallReferrerClient.newBuilder(this).build();
                System.out.println("Referrer check" + mReferrerClient.isReady());
                mReferrerClient.startConnection(this);
                System.out.println("Referrer check" + mReferrerClient.isReady());
            } else {
                System.out.println("=== ReferrerDetails Referrer Already Detected");
            }
        }

        mViewController = new MainViewController(MainActivity.this);
        if (savedInstanceState != null) {
            mAcquireFragment = (AcquireFragment) getSupportFragmentManager()
                    .findFragmentByTag(DIALOG_TAG);
        }

        mBillingManager = new BillingManager(this, mViewController.getUpdateListener());

        if (U.clr_chace(MainActivity.this)) {
            U.date_put(MainActivity.this, "clr_chace", 1);//7
            U.deleteCache(MainActivity.this);
        }

        Log.e("personal_noti", "status : " + CheckAlertService(MainActivity.this));

        /*if (pref.getInt(MainActivity.this, U.SH_AD_PURCHASED) == 0) {
            boolean contentAd = false;
            boolean installAd = false;
            int adRange = pref.getInt(MainActivity.this, U.SH_REMOTE_AD);
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
            MainActivity.refreshAd(MainActivity.this, installAd, contentAd);
        }*/

        setFBSDK();

        versionControl();

        AppSignatureHashHelper appSignatureHashHelper = new AppSignatureHashHelper(this);

        // This code requires one time to get Hash keys do comment and share key
        Log.i("kozhiihash", "HashKey: " + appSignatureHashHelper.getAppSignatures().get(0));
        System.out.println("HashKey: " + appSignatureHashHelper.getAppSignatures().get(0));

        String hashkey = "" + appSignatureHashHelper.getAppSignatures().get(0);
    }

    private void setFBSDK() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    // Initial District Screens

    private void versionControl() {
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;

        Log.e("Mode", "Mode : : " + mode);

        Log.e("USER_TYPE", "user_entry :" + pref.getString(this, U.SH_USER_TYPE));

        if (pref.getInt(this, "DB_MOVE" + U.versioncode_get(this)) == 0) {
            BackgroundMoveDbFromAsset();
        }

        if (pref.getInt(this, U.SH_FIRST_INSTALL + versionCode) == 0) {
            pref.putInt(this, U.SH_VERSION_CODE, versionCode);
            pref.putString(this, U.SH_VERSION_NAME, versionName);
            pref.putInt(this, U.SH_FIRST_INSTALL + versionCode, 1);
            firstScreen();
        } else if (pref.getString(this, U.SH_USER_TYPE).equals(U.EMPLOYEE)) {
            if (mode.equals("gcm") || pref.getInt(MainActivity.this, U.SH_RJOBS_FLAG) == 1) {
                Log.e("check_HomeActivity", "EMPLOYER");
                setHomeActivity();
            } else if (U.isNetworkAvailable(MainActivity.this) &&
                    (pref.getString(MainActivity.this, U.SH_NATIVE_LOCATION).isEmpty() || pref.getString(MainActivity.this, U.SH_NATIVE_LOCATION_NAME).isEmpty() ||
                            pref.getString(MainActivity.this, U.SH_USER_DISTRICT_ID).isEmpty())) {
                loadDistricts(SU.REGISTRATION_L);
            } else {
                Log.e("callEmployee", "called");
                callEmployee();
            }
        } else if (pref.getString(this, U.SH_USER_TYPE).equals(U.EMPLOYER)) {
            if (mode.equals("view") || mode.equals("gcm") || mode.equals("reminderNoti")) {
                Log.e("check_HomeActivity", "EMPLOYER");
                setHomeActivity();
            } else {
                firstScreen();
            }
        } else {
            firstScreen();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void setHomeActivity() {
        setContentView(R.layout.activity_main);

        if (pref.getInt(MainActivity.this, U.SH_AD_PURCHASED) == 0) {
            loadNativeAd(MainActivity.this);
        }

        if (!CheckAlertService(MainActivity.this) && pref.getString(MainActivity.this, U.SH_USER_TYPE).equals(U.EMPLOYEE)) {
            personalNotification();
        } else if (pref.getString(MainActivity.this, U.SH_USER_TYPE).equals(U.EMPLOYER)) {
            DailyNotifyReceiver receiver = new DailyNotifyReceiver();
            receiver.CancelAlarm(MainActivity.this);
        }

        MainActivity.adPositionShuffle();

        inappUpdate();

        if (pref.getInt(MainActivity.this, U.SH_AD_PURCHASED) == 0) {
            ads_manager_interstistial();


        }

        Log.e("AndroidID", U.getAndroidId(MainActivity.this));

        lastLogin();

        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(0);
        tabLayout = findViewById(R.id.tabs);
        sliderJSON = new ArrayList<>();

        progressBar = findViewById(R.id.progressBar);

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

        if (U.isNetworkAvailable(MainActivity.this)) {
            try {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();

                    }

                    @Override
                    protected String doInBackground(String... params) {

                        try {
                            remoteConfigForAd();
                        } catch (Exception e) {
                            System.out.println("remote config ad: error 1 : " + e.getMessage());
                            pref.putInt(MainActivity.this, U.SH_REMOTE_AD, 2);
                        }

                        try {
                            remoteConfigForVacancy();
                        } catch (Exception e) {
                            System.out.println("remote config Vacancy: error 1 : " + e.getMessage());
                            pref.putInt(MainActivity.this, U.SH_REMOTE_VACANCY, 2);
                        }

                        try {
                            remoteConfigForPostedby();
                        } catch (Exception e) {
                            System.out.println("remote config postedby: error 1 : " + e.getMessage());
                            pref.putInt(MainActivity.this, U.SH_REMOTE_POSTEDBY, 2);
                        }

                        try {
                            remoteConfigForPhoneNumber();
                        } catch (Exception e) {
                            System.out.println("remote config phoneNumber: error 1 : " + e.getMessage());
                            pref.putInt(MainActivity.this, U.SH_REMOTE_PHONE_NUMBER, 2);
                        }

                        try {
                            remoteConfigForEmployerPart();
                        } catch (Exception e) {
                            System.out.println("remote config EmployerPart: error 1 : " + e.getMessage());
                            pref.putInt(MainActivity.this, U.SH_REMOTE_EMPLOYER_LINK, 2);
                        }

                        try {
                            remoteConfigForLocationTab();
                        } catch (Exception e) {
                            System.out.println("remote config LocationTab: error 1 : " + e.getMessage());
                            pref.putInt(MainActivity.this, U.SH_REMOTE_LOCATION_TAB, 3);
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        super.onPostExecute(result);
                        Log.e("postExecute", "called");
                    }
                }.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setView();
        }
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

    public void setView() {

        Log.e("SetView", "called");

        setupViewPager(viewPager);

        setupTabIcons();

        gcmControl();

        setToolBar();

        gcmReceive();

        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (districtDia != null) {
            districtDia.dismiss();
            districtDia = null;
        }
        if (nativeLocationDia != null) {
            nativeLocationDia.dismiss();
            nativeLocationDia = null;
        }
        if (infoDia != null) {
            infoDia.dismiss();
            infoDia = null;
        }
        if (optionDialog != null) {
            optionDialog.dismiss();
            optionDialog = null;
        }
        if (frstScreenDia != null) {
            frstScreenDia.dismiss();
            frstScreenDia = null;
        }
        if (registerDia != null) {
            registerDia.dismiss();
            registerDia = null;
        }
        if (exitDia != null) {
            exitDia.dismiss();
            exitDia = null;
        }
    }

    @Override
    public void onInstallReferrerSetupFinished(int responseCode) {
        System.out.println("onInstallReferrerSetupFinished");
        System.out.println("InstallReferrerClient responseCode " + responseCode);

        switch (responseCode) {
            case InstallReferrerClient.InstallReferrerResponse.OK:
                pref.putInt(MainActivity.this, "referrerCheck", 1);
                System.out.println("Install Referrer conneceted... Successfully");
                System.out.println("Install Referrer conneceted..." + mReferrerClient.isReady());

                /*get response from referrer*/
                try {
                    ReferrerDetails response = mReferrerClient.getInstallReferrer();
                    if (response != null) {
                        String referrerUrl = response.getInstallReferrer();
                        long referrerClickTime = response.getReferrerClickTimestampSeconds();
                        long appInstallTime = response.getInstallBeginTimestampSeconds();
                        boolean instantExperienceLaunched = response.getGooglePlayInstantParam();

                        System.out.println("=== ReferrerDetails " + referrerUrl);
                        System.out.println("=== ReferrerDetails " + referrerClickTime);
                        System.out.println("=== ReferrerDetails " + appInstallTime);
                        System.out.println("=== ReferrerDetails " + instantExperienceLaunched);

                        if (referrerUrl != null) {
                            if (referrerUrl.length() > 0) {
                                String[] referrerList = referrerUrl.split("&");
                                for (int i = 0; i < referrerList.length; i++) {
                                    if (i == 0) {
                                        source = referrerList[i].substring(referrerList[i].indexOf("=") + 1);
                                        System.out.println("Referrer===" + source);
                                    } else if (i == 1) {
                                        medium = referrerList[i].substring(referrerList[i].indexOf("=") + 1);
                                        System.out.println("Referrer===" + medium);
                                    } else if (i == 2) {
                                        comp = referrerList[i].substring(referrerList[i].indexOf("=") + 1);
                                        System.out.println("Referrer===" + comp);
                                    }
                                }
                            }

                            /*sent data to server*/
                            if (U.isNetworkAvailable(MainActivity.this)) {
                                send(MainActivity.this, source, medium, comp);
                            }

                        } else {
                            System.out.println("=== Referrer URL NULL");
                        }

                    } else {
                        System.out.println("=== Referrer Details NULL");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                    System.out.println("=== error " + e.getMessage());
                }

                break;
            case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                System.out.println("FEATURE_NOT_SUPPORTED");
                break;
            case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                // Connection could not be established
                System.out.println("SERVICE_UNAVAILABLE");
                break;
            case InstallReferrerClient.InstallReferrerResponse.SERVICE_DISCONNECTED:
                System.out.println("SERVICE_DISCONNECTED");
                break;
            case InstallReferrerClient.InstallReferrerResponse.DEVELOPER_ERROR:
                System.out.println("DEVELOPER_ERROR");
                break;
            default:
                System.out.println("RESPONSE CODE NOT FOUND");

        }

        mReferrerClient.endConnection();
    }

    //------------------------------------- Call Employee -----------------------------------------

    public void send(final Context context, final String utm, final String utm1, final String utm2) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.REFERRERURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            L.t(context, "Referrer Error");
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
                    params.put("app", type);
                    params.put("ref", utm);
                    params.put("mm", utm1);
                    params.put("cn", utm2);
                    params.put("email", pref.getString(context, U.SH_ANDROID_ID));
                    return params;
                }
            };

            MySingleton.getInstance(context).addToRequestQue(stringRequest);

        } catch (Exception e) {

        }
    }

    @Override
    public void onInstallReferrerServiceDisconnected() {
        System.out.println("onInstallReferrerServiceDisconnected");
        mReferrerClient.startConnection(this);
    }

    public void firstScreen() {
        frstScreenDia = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        frstScreenDia.setContentView(R.layout.image_screen);
        frstScreenDia.setCancelable(false);
        LinearLayout screen = frstScreenDia.findViewById(R.id.screen_lay);
        screen.setBackgroundResource(R.drawable.entry_screen);

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                switch (pref.getString(MainActivity.this, U.SH_USER_TYPE)) {
                    case U.EMPLOYER:
                        startActivity(new Intent(MainActivity.this, EmployerHomeActivity.class));
                        finish();
                        frstScreenDia.dismiss();
                        break;
                    case U.EMPLOYEE:
                        callEmployee();
                        frstScreenDia.dismiss();
                        break;
                    case U.RESTART:
                        showEmployeeInfoDialog();
                        frstScreenDia.dismiss();
                        break;
                    default:
                        optionScreen(false);
                        break;
                }
            }
        };

        mHandler.postDelayed(mRunnable, 3500);
        frstScreenDia.show();
    }

    public void optionScreen(Boolean b) {
        optionDialog = new Dialog(MainActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        optionDialog.setContentView(R.layout.option_screen);
        optionDialog.setCancelable(b);
        if (optionDialog != null) {
            optionDialog.show();
        }
        final CardView employee_card = optionDialog.findViewById(R.id.employee_select_card);
        final CardView employer_card = optionDialog.findViewById(R.id.employer_select_card);

        employee_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEmployeeInfoDialog();
                if (frstScreenDia != null && frstScreenDia.isShowing()) {
                    frstScreenDia.dismiss();
                }
            }
        });

        employer_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEmployerInfoDialog();
                if (frstScreenDia != null && frstScreenDia.isShowing()) {
                    frstScreenDia.dismiss();
                }
            }
        });

        if (optionDialog != null) {
            optionDialog.show();
        }
    }

    //---------------------------------- viewpager -------------------------------------------------

    public void showEmployeeInfoDialog() {
        infoDia = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        infoDia.setContentView(R.layout.info_app);
        infoDia.setCancelable(false);
        final WebView infoText = infoDia.findViewById(R.id.info_text);
        final CheckBox infoAgree = infoDia.findViewById(R.id.info_agree);
        final FloatingActionButton ok = infoDia.findViewById(R.id.text_ok);
        infoText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        infoText.setBackgroundColor(Color.TRANSPARENT);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                infoText.loadUrl("file:///android_asset/welcome.html");
            }
        });

        ok.setImageBitmap(textAsBitmap(getResources().getString(R.string.ok), getResources().getDimension(R.dimen.txt_25), Color.BLACK));

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (infoAgree.isChecked()) {
                    pref.putString(MainActivity.this, U.SH_USER_TYPE, U.EMPLOYEE);

                    Log.e("USER_TYPE", "user_empe_info :" + pref.getString(MainActivity.this, U.SH_USER_TYPE));

                    if (U.isNetworkAvailable(MainActivity.this)) {
                        loadDistricts(SU.REGISTRATION_L);
                    } else {
                        setHomeActivity();
                    }

                    if (optionDialog != null && optionDialog.isShowing()) {
                        optionDialog.dismiss();
                    }
                    infoDia.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "மேலும் தொடர Checkbox-ஐ டிக் செய்து சரி-ஐ கிளிக் செய்யவும்.", Toast.LENGTH_LONG).show();
                }
            }
        });


        if (!isFinishing()) {
            infoDia.show();
        }
    }

    public void showEmployerInfoDialog() {
        infoDia = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        infoDia.setContentView(R.layout.info_app);
        infoDia.setCancelable(false);
        WebView infoText = infoDia.findViewById(R.id.info_text);
        final CheckBox infoAgree = infoDia.findViewById(R.id.info_agree);
        final FloatingActionButton ok = infoDia.findViewById(R.id.text_ok);
        infoText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        infoText.setBackgroundColor(Color.TRANSPARENT);

        infoText.loadUrl("file:///android_asset/employer_tc.html");

        ok.setImageBitmap(textAsBitmap(getResources().getString(R.string.ok), getResources().getDimension(R.dimen.txt_25), Color.BLACK));

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (infoAgree.isChecked()) {
                    pref.putString(MainActivity.this, U.SH_USER_TYPE, U.EMPLOYER);

                    Log.e("USER_TYPE", "user_empr_info :" + pref.getString(MainActivity.this, U.SH_USER_TYPE));

                    pref.putInt(MainActivity.this, U.SH_MUTE_NOTIFICATION, 1);

                    startActivity(new Intent(MainActivity.this, EmployerHomeActivity.class));
                    finish();

                    if (optionDialog != null && optionDialog.isShowing()) {
                        optionDialog.dismiss();
                    }

                    infoDia.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "மேலும் தொடர Checkbox-ஐ டிக் செய்து சரி-ஐ கிளிக் செய்யவும்.", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (!isFinishing()) {
            infoDia.show();
        }
    }

    public Bitmap textAsBitmap(String text, float textSize, int textColor) {
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

    //----------------------------------- Notification Methods -------------------------------------

    private void loadDistricts(final String param) {
        String url;
        if (param.equals(SU.REGISTRATION_L)) {
            url = SU.REGISTRATION_URL;
        } else {
            url = SU.USER_DISTRICT_URL;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        Log.e("showresponse", response);
                        switch (param) {
                            case SU.REGISTRATION_L:
                                showDistricts(response, param);
                                break;
                            case "get_ditrict":
                                showDistricts(response, param);
                                break;
                            case "set_ditrict":
                                try {
                                    jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    if (status.equals("success")) {

//                                        distId = pref.getString(MainActivity.this, U.SH_USER_DISTRICT);
//                                        distName = pref.getString(MainActivity.this, U.SH_USER_DISTRICT_NAME);
//                                        nativeLocation = pref.getString(MainActivity.this, U.SH_NATIVE_LOCATION);
//                                        nativeLocationName = pref.getString(MainActivity.this, U.SH_NATIVE_LOCATION_NAME);

                                        if (!nativeLocation.isEmpty()) {
                                            pref.putString(MainActivity.this, U.SH_NATIVE_LOCATION, nativeLocation);
                                            pref.putString(MainActivity.this, U.SH_NATIVE_LOCATION_NAME, nativeLocationName);
                                        } else {
                                            pref.putString(MainActivity.this, U.SH_NATIVE_LOCATION, "");
                                            pref.putString(MainActivity.this, U.SH_NATIVE_LOCATION_NAME, "");
                                        }

                                        if (!nativeDistrict.isEmpty()) {
                                            pref.putString(MainActivity.this, U.SH_USER_NATIVE_DISTRICT, nativeDistrict);
                                        } else {
                                            pref.putString(MainActivity.this, U.SH_USER_NATIVE_DISTRICT, "");
                                        }

                                        if (!distId.isEmpty()) {
                                            pref.putString(MainActivity.this, U.SH_USER_DISTRICT_ID, distId);
                                        } else {
                                            pref.putString(MainActivity.this, U.SH_USER_DISTRICT_ID, "");
                                        }

                                        if (!distName.isEmpty()) {
                                            pref.putString(MainActivity.this, U.SH_USER_DISTRICT_NAME, distName);
                                        } else {
                                            pref.putString(MainActivity.this, U.SH_USER_DISTRICT_NAME, "");
                                        }

                                        setHomeActivity();

                                        button.setEnabled(true);

                                        if (districtDia != null && districtDia.isShowing()) {
                                            districtDia.dismiss();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("response_error", "" + error);

                        if (districtDia != null && districtDia.isShowing()) {
                            districtDia.dismiss();
                        }

                        setHomeActivity();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                switch (param) {
                    case SU.REGISTRATION_L:
                        params.put("action", SU.REGISTRATION_L);
                        break;
                    case "get_ditrict":
                        params.put("action", "get_ditrict");
                        break;
                    case "set_ditrict":
                        params.put("action", "set_ditrict");
                        params.put("district", distId);
                        params.put("native_location", nativeLocation);
                        params.put("native_district", nativeDistrict);
                        break;
                }
                params.put("user_type", pref.getString(MainActivity.this, U.SH_USER_TYPE));
                params.put("fcm_id", pref.getString(MainActivity.this, "regId"));
                params.put("employee_id", pref.getString(MainActivity.this, U.SH_EMPLOYEE_ID));
                params.put("android_id", U.getAndroidId(MainActivity.this));
                params.put("vcode", String.valueOf(U.versioncode_get(MainActivity.this)));
                Log.e("paramsresponse", "" + params);
                return params;
            }
        };

        MySingleton.getInstance(MainActivity.this).addToRequestQue(stringRequest);
    }

    private void showDistricts(String json, String param) {
        Log.e("showresponse", "" + json);
        if (param.equals(SU.REGISTRATION_L)) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                userNativeLocation = new ArrayList<>();
                userNativeLocation.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    ArrayItem data = new ArrayItem();
                    data.setId(Integer.parseInt(jo.getString("dist_id")));
                    data.setItem(jo.getString("district"));
                    JSONArray jArray = jo.getJSONArray("areas");
                    List<Item> subList = new ArrayList<>();
                    for (int j = 0; j < jArray.length(); j++) {
                        try {
                            JSONObject obj = jArray.getJSONObject(j);
                            Item cdata = new Item();
                            cdata.setId(obj.getInt("id"));
                            cdata.setItem(obj.getString("area_name"));
                            subList.add(cdata);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    data.setList(subList);
                    userNativeLocation.add(data);
                }

                if (userNativeLocation != null && userNativeLocation.size() > 0) {
                    userCities = new ArrayList<>();
                    for (int i = 0; i < userNativeLocation.size(); i++) {
                        for (int j = 0; j < userNativeLocation.get(i).getList().size(); j++) {
                            Item data = new Item();
                            data.setId(userNativeLocation.get(i).getList().get(j).getId());
                            data.setItem(userNativeLocation.get(i).getList().get(j).getItem());
                            userCities.add(data);
                        }
                    }
                }

                loadDistricts("get_ditrict");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(json);
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                userDistricts = new ArrayList<>();
                userDistricts.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    Item data = new Item();
                    data.setId(jo.getInt("dist_id"));
                    data.setItem(jo.getString("dist"));
                    userDistricts.add(data);
                }

                if (pref.getString(MainActivity.this, U.SH_NATIVE_LOCATION).isEmpty() || pref.getString(MainActivity.this, U.SH_NATIVE_LOCATION_NAME).isEmpty() ||
                        pref.getString(MainActivity.this, U.SH_NATIVE_LOCATION).equals("0")) {
                    showNativeLocationDialog();
                } else {
                    showDistrictsDialog();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //------------------------------------ Remote Config -------------------------------------------

    public void showNativeLocationDialog() {
        nativeLocationDia = new Dialog(MainActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        nativeLocationDia.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nativeLocationDia.setContentView(R.layout.user_district_selection);
        nativeLocationDia.setCancelable(false);
        if (nativeLocationDia.getWindow() != null) {
            nativeLocationDia.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        ListView listview = nativeLocationDia.findViewById(R.id.listview);

        selectedDistrict = new ArrayList<>();
        selectedDistrictName = new ArrayList<>();

        final TextView text = nativeLocationDia.findViewById(R.id.title_text);
        text.setText("நீங்கள் வசிக்கும் நகரம்");

        noText = nativeLocationDia.findViewById(R.id.no_text);
        noText.setVisibility(View.GONE);

        button = nativeLocationDia.findViewById(R.id.submitBtn);
        button.setAlpha(0.5F);
        button.setEnabled(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text.setTextSize(25);
                text.setText(nativeLocationName);
                Animation zoomIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom_in);
                text.startAnimation(zoomIn);
                zoomIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        Handler mHandler = new Handler();
                        Runnable mRunnable = new Runnable() {
                            @Override
                            public void run() {
                                nativeLocationDia.dismiss();
                            }
                        };
                        mHandler.postDelayed(mRunnable, 2000);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                if (userNativeLocation != null && userNativeLocation.size() > 0) {
                    for (int i = 0; i < userNativeLocation.size(); i++) {
                        for (int j = 0; j < userNativeLocation.get(i).getList().size(); j++) {
                            if (nativeLocation.equals("" + userNativeLocation.get(i).getList().get(j).getId())) {
                                nativeDistrict = String.valueOf(userNativeLocation.get(i).getId());
                                Log.e("userNativeDistrict", "" + nativeDistrict);
                            }
                        }
                    }
                }
            }
        });

        //Ascending order
        Collections.sort(userCities, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getItem().compareTo(o2.getItem());
            }
        });

        final locationSelectionAdapter aadapter = new locationSelectionAdapter(
                MainActivity.this, R.layout.filter_layout, userCities);
        listview.setTextFilterEnabled(true);
        listview.setAdapter(aadapter);

        final SearchView searchview = nativeLocationDia.findViewById(R.id.searchview);
        searchview.setVisibility(View.VISIBLE);
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    aadapter.filter(newText.trim());
                } catch (Exception e) {
                    Log.e("Error", "" + e);
                }
                return true;
            }
        });

        nativeLocationDia.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                searchview.setQuery("", true);
                if (pref.getString(MainActivity.this, U.SH_USER_DISTRICT_ID).isEmpty()) {
                    showDistrictsDialog();
                } else {
                    loadDistricts("set_ditrict");
                }
            }
        });

        if (!isFinishing()) {
            nativeLocationDia.show();
        }
    }

    public void showDistrictsDialog() {
        districtDia = new Dialog(MainActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        districtDia.requestWindowFeature(Window.FEATURE_NO_TITLE);
        districtDia.setContentView(R.layout.user_district_selection);
        districtDia.setCancelable(false);
        if (districtDia.getWindow() != null) {
            districtDia.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        ListView listview = districtDia.findViewById(R.id.listview);

        SearchView searchview = districtDia.findViewById(R.id.searchview);
        searchview.setVisibility(View.GONE);

        selectedDistrict = new ArrayList<>();
        selectedDistrictName = new ArrayList<>();

        final SelectionAdapter aadapter = new SelectionAdapter(MainActivity.this, R.layout.filter_layout, userDistricts);
        listview.setTextFilterEnabled(true);
        listview.setAdapter(aadapter);

        noText = districtDia.findViewById(R.id.no_text);
        noText.setVisibility(View.GONE);

        final TextView text = districtDia.findViewById(R.id.title_text);
        text.setText("நீங்கள் வேலை தேட விரும்பும் மூன்று மாவட்டங்களை தேர்வுசெய்க");

        button = districtDia.findViewById(R.id.submitBtn);
        button.setAlpha(0.5F);
        button.setEnabled(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDistrict.isEmpty()) {
                    button.setAlpha(0.5F);
                    button.setEnabled(false);
                } else {
                    if (U.isNetworkAvailable(MainActivity.this)) {
                        button.setEnabled(false);
                        String filter;
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < selectedDistrict.size(); i++) {
                            sb.append(selectedDistrict.get(i)).append(",");
                        }
                        filter = sb.toString();
                        filter = filter.substring(0, filter.length() - 1);
                        Log.e("userLocation", "" + filter);

                        String filterName;
                        StringBuilder sb1 = new StringBuilder();
                        for (int i = 0; i < selectedDistrictName.size(); i++) {
                            sb1.append(selectedDistrictName.get(i)).append(",");
                        }
                        filterName = sb1.toString();
                        filterName = filterName.substring(0, filterName.length() - 1);
                        Log.e("userLocationName", "" + filterName);

                        distId = filter;
                        distName = filterName;
                        if (!pref.getString(MainActivity.this, U.SH_NATIVE_LOCATION).isEmpty()
                                || !pref.getString(MainActivity.this, U.SH_USER_NATIVE_DISTRICT).isEmpty()) {
                            nativeLocation = pref.getString(MainActivity.this, U.SH_NATIVE_LOCATION);
                            nativeDistrict = pref.getString(MainActivity.this, U.SH_USER_NATIVE_DISTRICT);
                        }

                        loadDistricts("set_ditrict");

                    } else {
                        L.t(MainActivity.this, U.INA);
                    }

                }
            }
        });

        if (!isFinishing()) {
            districtDia.show();
        }
    }

    public void callEmployee() {
        final Dialog welcomeDialog = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        welcomeDialog.setContentView(R.layout.intro);
        welcomeDialog.setCancelable(false);
        TextView todayQuote = welcomeDialog.findViewById(R.id.quote);
        TextView sloganText = welcomeDialog.findViewById(R.id.slogantext);
        LinearLayout topLay = welcomeDialog.findViewById(R.id.top_lay);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Aurella.ttf");
        sloganText.setTypeface(font);
        todayQuote.setTypeface(font);

        todayQuote(todayQuote, topLay);

        setHomeActivity();

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (welcomeDialog.isShowing()) {
                    welcomeDialog.dismiss();
                }
            }
        };
        mHandler.postDelayed(mRunnable, 3500);

        welcomeDialog.show();
    }

    public void todayQuote(TextView textView, LinearLayout layout) {
        DBHelper dbHelper = new DBHelper(this);

        int[] backImgArray = {R.drawable.front_screen_1, R.drawable.front_screen_2,
                R.drawable.front_screen_3, R.drawable.front_screen_4, R.drawable.front_screen_5,
                R.drawable.front_screen_6, R.drawable.front_screen_7, R.drawable.front_screen_8};

        if (pref.getString(this, U.SH_QUOTE_DATE).equals(U.currentDate())) {
            textView.setText(pref.getString(this, U.SH_TODAY_QUOTE));
            layout.setBackgroundResource(backImgArray[pref.getInt(this, U.SH_TODAY_QUOTE_IMG)]);
        } else {
            pref.putString(this, U.SH_QUOTE_DATE, U.currentDate());
            int backImg = imageShuffle();
            pref.putInt(MainActivity.this, U.SH_TODAY_QUOTE_IMG, backImg);
            layout.setBackgroundResource(backImgArray[backImg]);

            Cursor cursor = dbHelper.getQry("select * from jobsQuotes where flag='0' order by Random() limit 1");
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                dbHelper.executeSql("update jobsQuotes set flag='0' where flag='1'");
                Cursor cur = dbHelper.getQry("select * from jobsQuotes where flag='0' order by Random() limit 1");
                cur.moveToFirst();
                do {
                    String sno = cursor.getString(cursor.getColumnIndexOrThrow("sno"));
                    String quotes = cursor.getString(cursor.getColumnIndexOrThrow("quotes"));
                    textView.setText(quotes);
                    pref.putString(this, U.SH_TODAY_QUOTE, "" + quotes);
                    dbHelper.executeSql("update jobsQuotes set flag='1' where sno='" + sno + "'");
                } while (cur.moveToNext());
                cur.close();
            } else {
                do {
                    String sno = cursor.getString(cursor.getColumnIndexOrThrow("sno"));
                    String quotes = cursor.getString(cursor.getColumnIndexOrThrow("quotes"));
                    textView.setText(quotes);
                    pref.putString(this, U.SH_TODAY_QUOTE, "" + quotes);
                    dbHelper.executeSql("update jobsQuotes set flag='1' where sno='" + sno + "'");
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
    }

    public int imageShuffle() {
        ArrayList<Integer> img = new ArrayList();
        img.add(0);
        img.add(1);
        img.add(2);
        img.add(3);
        img.add(4);
        img.add(5);
        img.add(6);
        img.add(7);
        Collections.shuffle(img);
        return img.get(0);
    }

    @SuppressLint("StaticFieldLeak")
    public void BackgroundMoveDbFromAsset() {
        new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (!isFinishing()) {
//                    U.mProgress(MainActivity.this, "Loading please wait ...", false).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                TestAdapter localTestAdapter = new TestAdapter(MainActivity.this);
                localTestAdapter.createDatabase();
                localTestAdapter.open();
                localTestAdapter.close();
                pref.putInt(MainActivity.this, "DB_MOVE" + U.versioncode_get(MainActivity.this), 1);
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (!isFinishing()) {
//                    U.mProgress.dismiss();
                }
            }
        }.execute();
    }

    //------------------------------------ Toolbar Menu Fun ----------------------------------------

    private void setupTabIcons() {
        if (pref.getInt(MainActivity.this, U.SH_REMOTE_LOCATION_TAB) == 1 && pref.getString(MainActivity.this, U.SH_USER_TYPE).equals(U.EMPLOYEE)) {
            tabLayout.addTab(tabLayout.newTab().setText("உங்கள் ஊர் வேலைவாய்ப்பு"));
        }

        tabLayout.addTab(tabLayout.newTab().setText("முகப்பு"));

        if (pref.getInt(MainActivity.this, U.SH_REMOTE_LOCATION_TAB) == 2 && pref.getString(MainActivity.this, U.SH_USER_TYPE).equals(U.EMPLOYEE)) {
            tabLayout.addTab(tabLayout.newTab().setText("உங்கள் ஊர் வேலைவாய்ப்பு"));
        }

        if (pref.getString(MainActivity.this, U.SH_USER_TYPE).equals(U.EMPLOYEE)) {
            tabLayout.addTab(tabLayout.newTab().setText("பரிந்துரைக்கப்படுபவை"));
        }

        tabLayout.addTab(tabLayout.newTab().setText("மாவட்டங்கள்"));
        tabLayout.addTab(tabLayout.newTab().setText("பிரிவுகள்"));
        tabLayout.addTab(tabLayout.newTab().setText("விருப்பமானவைகள்"));
        tabLayout.addTab(tabLayout.newTab().setText("நினைவூட்டல்"));
    }

    private void setupViewPager(CustomViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        int pageNo = -1;
        if (pref.getInt(MainActivity.this, U.SH_REMOTE_LOCATION_TAB) == 1 && pref.getString(MainActivity.this, U.SH_USER_TYPE).equals(U.EMPLOYEE)) {
            adapter.addFragment("உங்கள் ஊர் வேலைவாய்ப்பு", JobListFragment.newInstance(SU.GETNLOCATIONJOBS, "", ""));
            pageNo = pageNo + 1;
            U.NLOCATION_PAGE = pageNo;
        }
        adapter.addFragment("முகப்பு", HomeFragment.newInstance());
        pageNo = pageNo + 1;
        U.HOME_PAGE = pageNo;

        if (pref.getInt(MainActivity.this, U.SH_REMOTE_LOCATION_TAB) == 2 && pref.getString(MainActivity.this, U.SH_USER_TYPE).equals(U.EMPLOYEE)) {
            adapter.addFragment("உங்கள் ஊர் வேலைவாய்ப்பு", JobListFragment.newInstance(SU.GETNLOCATIONJOBS, "", ""));
            pageNo = pageNo + 1;
            U.NLOCATION_PAGE = pageNo;
        }
        if (pref.getString(MainActivity.this, U.SH_USER_TYPE).equals(U.EMPLOYEE)) {
            adapter.addFragment("பரிந்துரைக்கப்படுபவை", RecommendedFragment.newInstance());
            pageNo = pageNo + 1;
            U.RECOMMENDED_PAGE = pageNo;
        }
        adapter.addFragment("மாவட்டங்கள்", DistrictWiseFragment.newInstance());
        pageNo = pageNo + 1;
        U.DISTRICT_PAGE = pageNo;
        adapter.addFragment("பிரிவுகள்", CategoryWiseFragment.newInstance());
        pageNo = pageNo + 1;
        U.CATEGORY_PAGE = pageNo;
        adapter.addFragment("விருப்பமானவைகள்", JobListFragment.newInstance(U.FJOBS, "", ""));
        pageNo = pageNo + 1;
        U.FAVOURITE_PAGE = pageNo;
        adapter.addFragment("நினைவூட்டல்", ReminderListFragment.newInstance());
        pageNo = pageNo + 1;
        U.REMINDER_PAGE = pageNo;
        viewPager.setAdapter(adapter);

        Log.e("pageno", " -- " + pageNo);
        Log.e("home_pageno :", "" + U.NLOCATION_PAGE + U.HOME_PAGE +
                U.RECOMMENDED_PAGE +
                U.DISTRICT_PAGE +
                U.CATEGORY_PAGE +
                U.FAVOURITE_PAGE +
                U.REMINDER_PAGE);
    }

    //------------------------------------  Navigation Menu Fun ------------------------------------

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
        notificationCursor.close();
    }

    private void extraFunctions() {
        myDB = openOrCreateDatabase("myDB", 0, null);
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + "noti_cal"
                + " (id integer NOT NULL PRIMARY KEY AUTOINCREMENT,title VARCHAR,message VARCHAR,date VARCHAR,time VARCHAR,isclose INT(4),isshow INT(4) default 0,type VARCHAR," +
                "bm VARCHAR,ntype VARCHAR,url VARCHAR);");

        updateNotificationCount();
    }

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
                            System.out.println("remoteconfig Ad: " + finalAdVal);
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            System.out.println("remoteconfig : failed 1 : " + finalAdVal);
                            pref.putInt(getApplicationContext(), U.SH_REMOTE_AD, 1);
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
            adVal = "0";
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
                            pref.putInt(getApplicationContext(), U.SH_REMOTE_VACANCY, Integer.parseInt(finalAdVal));
                            mFirebaseRemoteConfig.activateFetched();
                            System.out.println("remoteconfig vacancy:" + finalAdVal);
                        } else {
                            pref.putInt(getApplicationContext(), U.SH_REMOTE_VACANCY, 0);
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
            adVal = "0";
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
                            pref.putInt(getApplicationContext(), U.SH_REMOTE_POSTEDBY, Integer.parseInt(finalAdVal));
                            mFirebaseRemoteConfig.activateFetched();
                            System.out.println("remoteconfig postedby:" + finalAdVal);
                        } else {
                            pref.putInt(getApplicationContext(), U.SH_REMOTE_POSTEDBY, 0);
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
                            pref.putInt(getApplicationContext(), U.SH_REMOTE_PHONE_NUMBER, Integer.parseInt(finalAdVal));
                            mFirebaseRemoteConfig.activateFetched();
                            System.out.println("remoteconfig Phone_NUMBER:" + finalAdVal);
                        } else {
                            pref.putInt(getApplicationContext(), U.SH_REMOTE_PHONE_NUMBER, 1);
                        }
                    }
                });
    }

    private void remoteConfigForEmployerPart() {
        final FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_employer_part);

        String adVal = "";
        try {
            adVal = mFirebaseRemoteConfig.getString(LOADING_PHRASE_CONFIG_EMPLOYER_PART_KEY);
        } catch (Exception e) {
            adVal = "0";
        }
        pref.putInt(this, U.SH_REMOTE_EMPLOYER_LINK, Integer.parseInt(adVal));

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
                            pref.putInt(getApplicationContext(), U.SH_REMOTE_EMPLOYER_LINK, Integer.parseInt(finalAdVal));
                            mFirebaseRemoteConfig.activateFetched();
                            System.out.println("remoteconfig EmployerPart:" + finalAdVal);
                        } else {
                            pref.putInt(getApplicationContext(), U.SH_REMOTE_EMPLOYER_LINK, 0);
                        }
                    }
                });
    }

    //---------------------------------- static methods --------------------------------------------

    private void remoteConfigForLocationTab() {
        final FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_location_tab);

        String adVal = "";
        try {
            adVal = mFirebaseRemoteConfig.getString(LOADING_PHRASE_CONFIG_LOCATIONTAB_KEY);
        } catch (Exception e) {
            adVal = "0";
        }
        pref.putInt(this, U.SH_REMOTE_LOCATION_TAB, Integer.parseInt(adVal));

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
                            pref.putInt(getApplicationContext(), U.SH_REMOTE_LOCATION_TAB, Integer.parseInt(finalAdVal));
                            mFirebaseRemoteConfig.activateFetched();
                            System.out.println("remoteconfig LocationTab:" + finalAdVal);
                        } else {
                            pref.putInt(getApplicationContext(), U.SH_REMOTE_LOCATION_TAB, 0);
                        }
                    }
                });
    }

    private void setToolBar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        txtToolTitle = findViewById(R.id.txtToolTitle);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        searchIcon = findViewById(R.id.action_search);
        notifyCount = findViewById(R.id.notifications_text);
        profileIcon = findViewById(R.id.action_profile);
        notifyIcon = findViewById(R.id.action_notifications);
        noti_lay = findViewById(R.id.noti_lay);

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        searchIcon.setVisibility(View.VISIBLE);
        setProfile();
        extraFunctions();

        if (pref.getString(this, U.SH_USER_TYPE).equals(U.EMPLOYER)) {
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_settings).setVisible(false);
            menu.findItem(R.id.nav_feed_back).setVisible(false);
            menu.findItem(R.id.nav_myresume_aps).setVisible(false);
//            menu.findItem(R.id.nav_purchase).setVisible(false);
            menu.findItem(R.id.nav_notification).setVisible(false);
            menu.findItem(R.id.nav_saved_notification).setVisible(false);
            menu.findItem(R.id.nav_exit).setVisible(false);
            profileIcon.setVisibility(View.GONE);
            noti_lay.setVisibility(View.GONE);
        } else {
            profileIcon.setVisibility(View.VISIBLE);
            noti_lay.setVisibility(View.VISIBLE);
        }

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
                LayoutInflater layoutin = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View popupview = layoutin.inflate(R.layout.popup, null);
                popupwindow = new PopupWindow(popupview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                LinearLayout paymentDetails = popupview.findViewById(R.id.payment_details);
                LinearLayout paymentHistory = popupview.findViewById(R.id.payment_history);
                ImageView img1 = popupview.findViewById(R.id.img1);
                ImageView img2 = popupview.findViewById(R.id.img2);
                TextView txt1 = popupview.findViewById(R.id.txt1);
                TextView txt2 = popupview.findViewById(R.id.txt2);
                img1.setImageResource(R.drawable.ic_search_black_24dp);
                img2.setImageResource(R.drawable.ic_filter_list_white_24dp);
                txt1.setText("  Job Search  ");
                txt2.setText("  Job Filter  ");
                paymentDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupwindow.dismiss();
                        startActivity(new Intent(MainActivity.this, SearchActivity.class));

                    }
                });
                paymentHistory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupwindow.dismiss();
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(MainActivity.this, FilterActivity.class));
                            }
                        });
                    }
                });
                popupwindow.setBackgroundDrawable(new BitmapDrawable());
                popupwindow.setOutsideTouchable(true);
                popupwindow.showAsDropDown(v);
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

    private void makeCall(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_VIEW);
        callIntent.setData(Uri.parse("tel:" + phone));
        startActivity(callIntent);
    }

    //---------------------------------- Last login update -----------------------------------------

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
            profileStatus = getResources().getString(R.string.signup);
        }

        txtprofile.setText(profileStatus);

        if (pref.getString(MainActivity.this, U.SH_USER_TYPE).equals(U.EMPLOYER)) {
            profileLink.setVisibility(View.GONE);
        }

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
            viewPager.setCurrentItem(U.HOME_PAGE);
        } else if (id == R.id.nav_old_post) {
            if (U.isNetworkAvailable(MainActivity.this)) {
                startActivity(new Intent(MainActivity.this, OldPostActivity.class));
            } else {
                Toast.makeText(this, U.INA, Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_exam_details) {
            startActivity(new Intent(MainActivity.this, CategoryActivity.class));//OldPostActivity
        } else if (id == R.id.nav_search_id) {
            final Dialog searchDia = new Dialog(this, R.style.Base_Theme_AppCompat_Dialog_Alert);
            searchDia.setContentView(R.layout.search_byid);
            searchDia.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            final EditText userInput = searchDia.findViewById(R.id.user_input);
            userInput.requestFocus();

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
            if (imm != null) {
                imm.hideSoftInputFromWindow(userInput.getWindowToken(), 0);
            }
            searchDia.show();
        }

        // others
        else if (id == R.id.nav_purchase) {
            if (U.isNetworkAvailable(MainActivity.this)) {
                purchasedialog();
            } else {
                Toast.makeText(this, U.INA, Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_notification) {
            startActivity(new Intent(this, NotificationActivity.class));
        } else if (id == R.id.nav_saved_notification) {
            startActivity(new Intent(this, SavedNotification.class));
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
        } else if (id == R.id.nav_testimonial) {
            if (U.isNetworkAvailable(this)) {
                startActivity(new Intent(MainActivity.this, Testimonial.class));
            } else L.t(this, U.INA);
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
                            "\n" + U.UTM_SOURCE /* +"\n" + U.getAndroidId(MainActivity.this)*/);
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
        } else if (id == R.id.nav_how_to_use) {
            if (U.isNetworkAvailable(MainActivity.this)) {
                /*Intent yt_play = new Intent(Intent.ACTION_VIEW, Uri.parse(U.HOW_TO_USE_URL));
                Intent chooser = Intent.createChooser(yt_play, "Open With");
                if (yt_play.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                }*/
                U.custom_tabs(MainActivity.this, U.HOW_TO_USE_URL);
            } else {
                L.t(MainActivity.this, U.INA);
            }
        } else if (id == R.id.nav_exit) {
            if (pref.getString(MainActivity.this, U.SH_USER_TYPE).equals(U.EMPLOYEE)) {
                if (pref.getInt(MainActivity.this, U.SH_AD_PURCHASED) == 0) {
                    if (U.isNetworkAvailable(MainActivity.this)) {
                        /*if (interstitialAd != null && interstitialAd.isLoaded()) {*/
                        showIndus();
                       /* } else {
                            thankYouMessage();
                        }*/
                    }
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
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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

    private void callPrivacy() {
        if (U.isNetworkAvailable(this)) {
            showPrivacy(SU.PRIVACY);
        } else L.t(this, U.INA);
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

    public void lastLogin() {
        if (pref.getString(MainActivity.this, U.SH_LAST_LOGIN_DATE).equals(U.currentDate())) {
            callRegisterDia();
            loadSliderJSON();
        } else {
            if (U.isNetworkAvailable(MainActivity.this)) {
                loadJSON(U.currentDate());
            }
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
                        errorHandling(error);
                        setView();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "last_login");
                params.put("eid", pref.getString(MainActivity.this, U.SH_EMPLOYEE_ID));
                params.put("androidid", U.getAndroidId(MainActivity.this));
                params.put("vcode", "" + U.versioncode_get(MainActivity.this));
                Log.e("paramresponse", "" + params);
                return params;
            }

        };

        MySingleton.getInstance(MainActivity.this).addToRequestQue(stringRequest);
    }

    // -------------------------------------- GCM --------------------------------------------------

    private void errorHandling(VolleyError error) {
        String e;
        if (error instanceof TimeoutError || error instanceof AuthFailureError || error instanceof ServerError)
            e = U.SERVER_ERROR;
        else if (error instanceof NetworkError)
            e = U.INA;
        else if (error instanceof ParseError)
            e = U.ERROR;
        else
            e = U.ERROR;
        L.t(MainActivity.this, e);
    }

    private void showJSON(String cDate, String json) {

        Log.e("showresponse_vcode", "" + json);
        pref.putString(MainActivity.this, U.SH_LAST_LOGIN_DATE, "" + cDate);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            int vcode = jsonObject.getInt("v_code");

            String liveJobCount = jsonObject.getString("live_count");
            String privateJobCount = jsonObject.getString("live_private_jobs");
            String stateJobCount = jsonObject.getString("livestate_jobs");
            String centralJobCount = jsonObject.getString("live_central_jobs");

            pref.putString(MainActivity.this, U.SH_ALL_JOBS_COUNT, liveJobCount);
            pref.putString(MainActivity.this, U.SH_PRIVATE_JOBS_COUNT, privateJobCount);
            pref.putString(MainActivity.this, U.SH_STATE_JOBS_COUNT, stateJobCount);
            pref.putString(MainActivity.this, U.SH_CENTRAL_JOBS_COUNT, centralJobCount);

            loadSliderJSON();

            if (vcode > U.versioncode_get(MainActivity.this)) {
                whatsNew = jsonObject.getString("description");
                pref.putString(MainActivity.this, U.SH_INFO_WHATSNEW, whatsNew);
            } else {
                callRegisterDia();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void callRegisterDia() {
        if (pref.getString(MainActivity.this, U.SH_USER_TYPE).equals(U.EMPLOYEE) &&
                !pref.getBoolean(MainActivity.this, U.SH_SIGN_UP_SUCCESS)) {

            if (pref.getInt(MainActivity.this, U.SH_REGISTER_DIA_SHOW) == 0) {

                pref.putInt(MainActivity.this, U.SH_REGISTER_DIA_SHOW, 1);
                pref.putString(MainActivity.this, U.SH_ASK_REGISTER_DATE, U.addDaysInCal(3));

            } else if (pref.getInt(MainActivity.this, U.SH_REGISTER_DIA_SHOW) == 1) {
                Date date1 = U.convertStringtoDate(pref.getString(MainActivity.this, U.SH_ASK_REGISTER_DATE), U.DATE_FORMAT);
                Date date2 = U.convertStringtoDate(U.currentDate(), U.DATE_FORMAT);
                if (date2.equals(date1) || date2.after(date1)) {
                    askToRegister();
                }
            }
        }
    }

    private void askToRegister() {
        try {
            registerDia = new Dialog(MainActivity.this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
            if (registerDia.getWindow() != null) {
                registerDia.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            registerDia.setContentView(R.layout.ask_register_dia);
            registerDia.setCancelable(false);

            TextView no = registerDia.findViewById(R.id.never);
            Button ok = registerDia.findViewById(R.id.text_ok);
            WebView infoText = registerDia.findViewById(R.id.info_text);
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

            registerDia.findViewById(R.id.text_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    registerDia.dismiss();
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registerDia.dismiss();
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
                        registerDia.dismiss();
                    } else {
                        Toast.makeText(MainActivity.this, U.INA, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            registerDia.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface registerDia) {
                    pref.putInt(MainActivity.this, U.SH_REGISTER_DIA_SHOW, 1);
                    pref.putString(MainActivity.this, U.SH_ASK_REGISTER_DATE, U.addDaysInCal(3));
                    Log.e("REGISTER_DATE", "" + U.addDaysInCal(3));
                }
            });
            if (registerDia != null && !isFinishing()) {
                registerDia.show();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void askToUpdate(String description) {
        final Dialog dialog = new Dialog(MainActivity.this);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        dialog.setContentView(R.layout.alert_update_lay);
        dialog.setCancelable(false);
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
                inappUpdate();
                dialog.dismiss();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        if (!isFinishing()) {
            dialog.show();
        }
    }

    public void inappUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(MainActivity.this);

        com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                Log.e("inapp", "inapp_called");
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    Log.e("inapp", "inapp_Condition_called");
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo,
                                AppUpdateType.IMMEDIATE,
                                MainActivity.this,
                                200);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e("inapp", "inapp_Condition_failed");
                }
            }
        });
    }

    //----------------------------------------- Personal Notification ------------------------------

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (resultCode != RESULT_OK) {
                Log.e("Resultcode:", "" + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.

                String description = pref.getString(MainActivity.this, U.SH_INFO_WHATSNEW);
                if (description.equals("")) {
                    askToUpdate(pref.getString(MainActivity.this, U.SH_INFO_WHATSNEW));
                }
            }
        }
    }

    private void gcmControl() {
        pref.putString(getApplicationContext(), U.SH_ANDROID_ID, U.getAndroidId(MainActivity.this));
        gcmreg_fun();
    }

    //------------------------------------- Instestirial Ad ----------------------------------------

    public void gcmreg_fun() {
        if (U.isNetworkAvailable(MainActivity.this)) {
//            if (pref.getInt(getApplicationContext(), "notcunt") == 0) {

            smallestWidth();

            Log.e("gcmreg_fun", "gcmreg_fun------called");

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
                    Log.e("gcmpost_update2", "gcmpost_update2------called");
                }

            } else {
                if (pref.getInt(MainActivity.this, "fcm_update") < U.versioncode_get(MainActivity.this)) {
                    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                    new gcmpost_update1().execute(refreshedToken);
                    Log.e("gcmpost_update1", "gcmpost_update1------called");
                }
            }
            pref.putInt(getApplicationContext(), "vcode", BuildConfig.VERSION_CODE);
//                pref.putInt(getApplicationContext(), "notcunt", 1);
//            }
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

    //------------------------------------ FaceBook Banner Ads -------------------------------------

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
                    case "cj":
                        if (message != null && !message.isEmpty()) {
                            switch (message) {
                                case "home":
                                    setPage(U.HOME_PAGE);
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
                                case "homeTestimonial":
                                    if (U.isNetworkAvailable(MainActivity.this)) {
                                        startActivity(new Intent(MainActivity.this, Testimonial.class));
                                    } else {
                                        Toast.makeText(this, U.INA, Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case "jobs_search":
                                    if (U.isNetworkAvailable(MainActivity.this)) {
                                        startActivity(new Intent(MainActivity.this, SearchActivity.class));
                                    } else {
                                        Toast.makeText(this, U.INA, Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case "jobs_filter":
                                    if (U.isNetworkAvailable(MainActivity.this)) {
                                        startActivity(new Intent(MainActivity.this, FilterActivity.class));
                                    } else {
                                        Toast.makeText(this, U.INA, Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case "photoJobs":
                                    Intent photoJobs = new Intent(MainActivity.this, PhotoJobsLoginActivity.class);
                                    startActivity(photoJobs);
                                    break;
                            }
                        }
                        break;
                    case "rao":
                        if (U.isNetworkAvailable(MainActivity.this)) {
                            purchasedialog();
                        } else {
                            Toast.makeText(this, U.INA, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "pn":
                        if (message != null && !message.isEmpty()) {
                            if (message.equals(U.catArray[1])) {
                                if (pref.getString(MainActivity.this, U.SH_SKILLS).equals("0")) {
                                    setPage(U.RECOMMENDED_PAGE);
                                }
                            } else if (message.equals(U.catArray[3])) {
                                if (pref.getString(MainActivity.this, U.SH_JOBLOCATION).isEmpty()) {
                                    setPage(U.RECOMMENDED_PAGE);
                                }
                            }
                        }
                        break;
                }
            }
        }
    }

    //------------------------------------ Facebook Native Ads -------------------------------------

    public boolean CheckAlertService(Context context) {
        Intent i = new Intent(context, DailyNotifyReceiver.class);
        return (PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_NO_CREATE) != null);
    }

    public void personalNotification() {
        DailyNotifyReceiver alarm = new DailyNotifyReceiver();
        try {
            if (pref.getInt(MainActivity.this, U.SH_PERSONAL_NOTIFICATION_COUNT) == 4) {
                int shuffleTime = U.numberShuffle(17, 23);
                alarm.SetAlarm(MainActivity.this, shuffleTime);
                Log.e("infoshuffleTime_today", "" + shuffleTime);
            } else {
                int shuffleTime = U.numberShuffle(6, 23);
                alarm.SetAlarm(MainActivity.this, shuffleTime);
                Log.e("infoshuffleTime_other", "" + shuffleTime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("info", "personal notification called");
    }


    //------------------------------------ adv native static ad ------------------------------------

    public void showIndus() {
        if (interstitialAd_noti != null && interstitialAd_noti.isLoaded()) {
            interstitialAd_noti.show();
        } else {
            thankYouMessage();
        }


       /* interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                thankYouMessage();
            }
        });*/
    }

    public void purchasedialog() {
        final Dialog purchaseDia = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        purchaseDia.setContentView(R.layout.purchase_dialog_lay);
        Button purchase = purchaseDia.findViewById(R.id.purchase);
        Button cuscare = purchaseDia.findViewById(R.id.cuscare);
        TextView review = purchaseDia.findViewById(R.id.review);
        TextView title = purchaseDia.findViewById(R.id.title);
        LinearLayout expLay = purchaseDia.findViewById(R.id.exp_lay);

        if (pref.getInt(MainActivity.this, U.SH_AD_PURCHASED) != 0) {
            title.setVisibility(View.GONE);
            purchase.setVisibility(View.GONE);
            review.setText("நீங்கள் Purchase செய்து நித்ரா வேலைவாய்ப்பு செயலியை விளம்பரங்கள் இல்லாமல் பயன்படுத்திக்கொண்டிருக்கிறீர்கள்.");
        }

        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (U.isNetworkAvailable(MainActivity.this)) {
                    if (U.isNetworkAvailable(MainActivity.this)) {
                        onPurchaseButtonClicked();
                    } else {
                        U.toast_center(MainActivity.this, U.INA);
                    }
                    purchaseDia.dismiss();
                } else {
                    L.t(MainActivity.this, U.INA);
                }
            }
        });
        cuscare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerCaredialog();
            }
        });
        purchaseDia.show();
    }

    public void customerCaredialog() {
        final Dialog cusCareDia = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        cusCareDia.setContentView(R.layout.listview);
        ListView list = cusCareDia.findViewById(R.id.listview);
        ImageView back = cusCareDia.findViewById(R.id.back);
        TextView titleTxt = cusCareDia.findViewById(R.id.title);
        titleTxt.setText("            FAQ");
        List<Item> data = new ArrayList<>();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cusCareDia.dismiss();
            }
        });

        data.add(new Item(0, "நான், ஏன் ஆயுள் சந்தா Purchase செய்ய வேண்டும்?",
                "நித்ரா வேலைவாய்ப்பு செயலியில் ஆயுள் சந்தா Purchase செய்து கொள்வதால் விளம்பரங்கள் (Banner, Full Screen Ads)  அனைத்து " +
                        "பகுதிகளில் இருந்தும் முற்றிலுமாக நீக்கப்படும்."));

        data.add(new Item(0, "Premium Subscription (ஆயுள் சந்தா) செய்வதற்கு நான் எதற்காக E-mail ID-ஐ Play store-ல் Sign-in செய்ய வேண்டும்? " +
                "அதற்கான அவசியம் என்ன?",
                "நீங்கள் ஆயுள் சந்தா-வினை Purchase செய்துள்ள தகவல்களை Sign-in செய்து சேமித்து வைப்பதன் வாயிலாக " +
                        "உங்களுடைய ஸ்மார்ட் போனை நீங்கள் மாற்றினாலோ அல்லது நித்ரா வேலைவாய்ப்பு செயலியை Uninstall செய்து விட்டு திரும்பவும் Install செய்யும் போது  " +
                        "Subscription-னை பெறலாம். தவறுதலாக போனையோ அல்லது நித்ரா வேலைவாய்ப்பு செயலியோ Premium Subscription வசதியை இழக்கும் போது அதனை " +
                        "மீண்டும் செயல்பாட்டிற்கு கொண்டு வர மிகப் பயனுள்ளதாக இருக்கும்."
        ));

        data.add(new Item(0, "நான் எனது மொபைல் போனை தொலைத்துவிட்டேன் அல்லது எனது மொபைல் போனை மாற்றிவிட்டேன். ஆயுள் " +
                "சந்தா வசதியை எனது புது போனில் பயன்படுத்துவதற்கான வாய்ப்புகள் உள்ளதா?",
                "நிச்சயமாக உள்ளது. நீங்கள் ஆயுள் சந்தா Purchase செய்ய பயன்படுத்திய Google E-mail ID-ஐ புதிய மொபைலில்  " +
                        "Sign-in செய்வதன் வாயிலாக  Premium Plan வசதியை Restore செய்து கொள்ளலாம்."
        ));

        data.add(new Item(0, "நான் இரண்டு ஆண்ட்ராய்டு மொபைல் போனை பயன்படுத்திக்கொண்டிருக்கிறேன். ஒரு தடவை ஆயுள் சந்தா-ஐ  " +
                "Purchase செய்து இரண்டு மொபைல் போனிலும் பயன்படுத்தலாமா?",
                "தாராளமாக பயன்படுத்திக்கொள்ளலாம். நீங்கள் இரண்டு மொபைல் போனிலும் ஆயுள் சந்தா செய்ய பயன்படுத்திய " +
                        "Google E-mail ID-ஐ Sign-in செய்வதன் வாயிலாகப் பெறலாம்."
        ));

        data.add(new Item(0, "எனது Credit/Debit கார்டு பற்றிய தகவல்களை கொடுத்து Purchase செய்கிறேன். உண்மையிலேயே இது பாதுகாப்பானதாக இருக்குமா?",
                "நிச்சயமாக மிகவும் பாதுகாப்பான முறையாகத்தான் இது இருக்கிறது. நீங்கள்  Google வழியாக கட்டணமுறை மேற்கொள்வதால் உங்களது " +
                        "பணபரிவர்த்தனைகளையோ, Credit/Debit கார்டு தகவல்களையோ யாரும் அணுக முடியாது."));

        data.add(new Item(0, "Payment முறையில் என்னுடைய Debit கார்டு தகவல்களை சேர்க்கமுடியவில்லை. “Correct this card info or try a different card” என்ற Error message " +
                "வந்து கொண்டே இருக்கிறது? இப்போது நான் என்ன செய்ய வேண்டும்?",
                "பொதுவாகவே Debit கார்டானது உள்நாட்டு (Domestic) பணபரிவர்த்தனைக்காக மட்டுமே பயன்பாட்டில் கொடுக்கப்பட்டிருக்கும். நீங்கள் உங்கள் வங்கியை" +
                        " அணுகி சர்வதேச பயன்பாட்டிற்கான கார்டாக (International) மாற்றிக்கொண்டு பணபரிவர்த்தனையை மேற்கொள்ளலாம்."));

        data.add(new Item(1, "நான் ஆயுள் சந்தாவினை Purchase செய்துவிட்டேன். ஆனாலும், எனக்கு ஆயுள் சந்தா வசதி activate " +
                "ஆகவில்லை. இப்போது நான் என்ன செய்ய வேண்டும்?",
                "நீங்கள் Google-ல் Purchase செய்ததற்கான Order-ஐ எங்களின் jobs@nithra.mobi என்ற மின்னஞ்சல் முகவரிக்கு அனுப்பினால் விரைவாக " +
                        "கவனத்தில் கொண்டு சரிசெய்யப்படும்."
        ));

        data.add(new Item(2, "ஆயுள் சந்தா சார்ந்த  சந்தேகங்கள் அல்லது பிரச்சனைகள் ", "ஆயுள் சந்தா சார்ந்த  சந்தேகங்கள் அல்லது பிரச்சனைகளுக்கு தொடர்பு கொள்ள வேண்டிய தொலைப்பேசி எண் : " +
                "9942267855 (நிறுவனம் இயங்கும் நேரம் : 9.30AM - 5.00PM IST இணையதள முகவரி : https://nithra.mobi/"
        ));

        CustomerCareAdapter adapter = new CustomerCareAdapter(MainActivity.this, R.layout.listview, data);
        list.setAdapter(adapter);

        cusCareDia.show();
    }

    @Override
    public void onPurchaseItemClick() {
        if (U.isNetworkAvailable(MainActivity.this)) {
            purchasedialog();
        } else {
            Toast.makeText(this, U.INA, Toast.LENGTH_SHORT).show();
        }
    }

    //----------------------------------------- Ad Position ----------------------------------------

    @Override
    public void onNextPageItemClick() {
        setPage(U.RECOMMENDED_PAGE);
    }

    @Override
    public void onUserTypeChangeClick() {
        showEmployerInfoDialog();
    }

    //---------------------------------------- Purchase Functions ----------------------------------

    public void onPurchaseButtonClicked() {
        Log.d("onPurchaseButtonClicked", "Purchase button clicked.");
        if (mAcquireFragment == null) {
            mAcquireFragment = new AcquireFragment();
        }

        if (!isAcquireFragmentShown()) {
            mAcquireFragment.show(getSupportFragmentManager(), DIALOG_TAG);
            if (mBillingManager != null
                    && mBillingManager.getBillingClientResponseCode()
                    > BILLING_MANAGER_NOT_INITIALIZED) {
                mAcquireFragment.onManagerReady(this);
            }
        }
    }

    public boolean isAcquireFragmentShown() {
        return mAcquireFragment != null && mAcquireFragment.isVisible();
    }

    public void showRefreshedUi() {
        updateUi();
        if (mAcquireFragment != null) {
            mAcquireFragment.refreshUI();
        }
    }

    /**
     * Update UI to reflect model
     */
    @UiThread
    private void updateUi() {
        if (pref.getInt(MainActivity.this, U.SH_AD_PURCHASED) == 1) {
            if (HomeFragment.ads != null) {
                HomeFragment.ads.setVisibility(View.GONE);
            }
            if (pref.getInt(MainActivity.this, "add_remove_isshow") == 1) {
                pref.putInt(MainActivity.this, "add_remove_isshow", 2);
                appRestart();
            }
        } else {
            pref.putInt(MainActivity.this, "add_remove_isshow", 1);

        }
    }

    @Override
    public BillingManager getBillingManager() {
        return mBillingManager;
    }

    @Override
    public boolean isPremiumPurchased() {
        return mViewController.isPremiumPurchased();
    }

    @Override
    public boolean isGoldMonthlySubscribed() {
        return mViewController.isGoldMonthlySubscribed();
    }

    @Override
    public boolean isTankFull() {
        return mViewController.isTankFull();
    }

    public void onBillingManagerSetupFinished() {
        if (mAcquireFragment != null) {
            mAcquireFragment.onManagerReady(this);
        }
    }

    public void appRestart() {
        // Intent i = new Intent(MainActivity.this, MainActivity.class);
        Intent i = getIntent();
        i.putExtra("mode", "");
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

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
        exitDia = new Dialog(MainActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        exitDia.setContentView(R.layout.image_screen);
        exitDia.setCancelable(false);
        if (!isFinishing()) {
            exitDia.show();
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing() && exitDia.isShowing()) {
                    finish();
                }
                finish();
            }
        }, 2000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawer != null) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                } else if (MainActivity.homePagePosition == U.CATEGORY_PAGE) {
                    if (CategoryWiseFragment.fragHolder.getVisibility() == View.VISIBLE) {
                        CategoryWiseFragment.fragHolder.removeAllViews();
                        CategoryWiseFragment.fragHolder.setVisibility(View.GONE);
                        CategoryWiseFragment.fblLay.setVisibility(View.VISIBLE);
                        if (pref.getInt(MainActivity.this, U.SH_AD_PURCHASED) == 0) {
                            CategoryWiseFragment.ads.setVisibility(View.VISIBLE);
                        }
                    } else {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                    }
                } else if (MainActivity.homePagePosition == U.DISTRICT_PAGE) {
                    if (DistrictWiseFragment.fragHolder.getVisibility() == View.VISIBLE) {
                        DistrictWiseFragment.fragHolder.removeAllViews();
                        DistrictWiseFragment.fragHolder.setVisibility(View.GONE);
                        DistrictWiseFragment.fblLay.setVisibility(View.VISIBLE);
                        if (pref.getInt(MainActivity.this, U.SH_AD_PURCHASED) == 0) {
                            if (U.isNetworkAvailable(MainActivity.this)) {
                                DistrictWiseFragment.ads.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                    }
                } else if (MainActivity.homePagePosition == U.RECOMMENDED_PAGE) {
                    if (RecommendedFragment.fragHolder.getVisibility() == View.VISIBLE) {
                        RecommendedFragment.fragHolder.removeAllViews();
                        RecommendedFragment.fragHolder.setVisibility(View.GONE);
                        RecommendedFragment.frontLay.setVisibility(View.VISIBLE);
                        if (pref.getInt(MainActivity.this, U.SH_AD_PURCHASED) == 0) {
                            if (U.isNetworkAvailable(MainActivity.this)) {
                                RecommendedFragment.ads.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                    }
                } else if (MainActivity.homePagePosition == U.HOME_PAGE) {
                    if (HomeFragment.fragHolder.getVisibility() == View.VISIBLE) {
                        HomeFragment.fragHolder.removeAllViews();
                        HomeFragment.fragHolder.setVisibility(View.GONE);
                        HomeFragment.frontLay.setVisibility(View.VISIBLE);
                        if (pref.getInt(MainActivity.this, U.SH_AD_PURCHASED) == 0) {
                            if (U.isNetworkAvailable(MainActivity.this)) {
                                HomeFragment.ads.setVisibility(View.VISIBLE);
                            }
                        }
                    } else if (viewPager != null && viewPager.getCurrentItem() == 0) {
                        //check
                        Log.e("mode_onKey", "" + mode);
                        if (mode.equals("reminderNoti")) {
                            startActivity(new Intent(MainActivity.this, EmployerHomeActivity.class));
                            finish();
                        } else if (mode.equals("view")) {
                            finish();
                        } else if (pref.getInt(getApplicationContext(), U.SH_RATE_US) == 0) {
                            showRateUs();
                        } else {
                            exit();
                        }
                    } else {
                        if (viewPager != null) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                        }
                    }
                } else if (viewPager != null && viewPager.getCurrentItem() == 0) {
                    //check
                    Log.e("mode_onKey", "" + mode);
                    if (mode.equals("reminderNoti")) {
                        startActivity(new Intent(MainActivity.this, EmployerHomeActivity.class));
                        finish();
                    } else if (mode.equals("view")) {
                        finish();
                    } else if (pref.getInt(getApplicationContext(), U.SH_RATE_US) == 0) {
                        showRateUs();
                    } else {
                        Log.e("exit", "called");
                        exit();
                    }
                } else {
                    if (viewPager != null) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                    }
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if (exit != 0) {
            if (pref.getString(MainActivity.this, U.SH_USER_TYPE).equals(U.EMPLOYEE)) {
                Log.e("exit emp", "called");
                if (pref.getInt(MainActivity.this, U.SH_AD_PURCHASED) == 0) {
                    if (U.isNetworkAvailable(MainActivity.this)) {
                        /* if (interstitialAd != null && interstitialAd.isLoaded()) {*/
                        Log.e("exit ins", "called");
                        showIndus();
                        /*} else {
                            thankYouMessage();
                        }*/
                    } else {
                        thankYouMessage();
                    }
                } else {
                    thankYouMessage();
                }
            } else if (pref.getString(this, U.SH_USER_TYPE).equals(U.EMPLOYER)) {
                if (mode.equals("view") || mode.equals("gcm")) {
                    startActivity(new Intent(MainActivity.this, EmployerHomeActivity.class));
                    finish();
                } else {
                    finish();
                }
            }
        } else {
            exit = exit + 1;
            L.t(MainActivity.this, "Press one more time to exit");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pref.getString(MainActivity.this, U.SH_USER_TYPE).equals(U.EMPLOYEE)) {
            if (navigationView != null && notifyCount != null) {
                setProfile();
                updateNotificationCount();
            }
        }

        if (pref.getInt(MainActivity.this, U.SH_RJOBS_FLAG) == 1) {
            pref.putInt(MainActivity.this, U.SH_RJOBS_FLAG, 0);
            Log.e("adapter_onRes", "called");
            setupViewPager(viewPager);
            viewPager.setCurrentItem(U.RECOMMENDED_PAGE);
            TabLayout.Tab tab = tabLayout.getTabAt(U.RECOMMENDED_PAGE);
            if (tab != null) {
                tab.select();
            }
        } else {
            Intent in = getIntent();
            Uri data = in.getData();
            if (data != null) {
                String v = data.toString().substring(data.toString().indexOf("id=") + 3);
                if (v.length() != 0) {
                    Log.e("Deep_Data----", "" + v);
                    Log.e("check_HomeActivity", "onResume");
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
    }

    //---------------------------------------- Common Methods --------------------------------------

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

    private void loadSliderJSON() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.SERVER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("showResponse", "" + response);
                        showSliderJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", "" + error);
                        setView();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", SU.GETPERSONALISEDJOBS);
                params.put("job_title", "");
                params.put("job-preferred-location", "" + pref.getString(MainActivity.this, U.SH_JOBLOCATION));
                params.put("skill", "" + pref.getString(MainActivity.this, U.SH_SKILLS));
                params.put("qualification", "" + pref.getString(MainActivity.this, U.SH_COURSE));
                params.put("district_wise", "" + pref.getString(MainActivity.this, U.SH_USER_DISTRICT_NAME));
                params.put("applied_jobs_job_title", "" + localDB.getAppliedTitleIds());
                params.put("recently_viwed_jobs_job_title", "" + localDB.getReadTitleIds());
                params.put("key", "" + pref.getString(MainActivity.this, U.SH_RECENT_SEARCH_KEYS).replace(",", " ").replace("+", " "));
                params.put("load", String.valueOf(-1));
                params.put("order", "3");
                params.put("vcode", String.valueOf(U.versioncode_get(MainActivity.this)));
                params.put("employee_id", pref.getString(MainActivity.this, U.SH_EMPLOYEE_ID));
                params.put("android_id", U.getAndroidId(MainActivity.this));
                params.put("ad_purchase", "" + pref.getInt(MainActivity.this, U.SH_AD_PURCHASED));
                params.put("user_type", pref.getString(MainActivity.this, U.SH_USER_TYPE));

                Log.e("paramsresponse", "" + params);
                return params;
            }
        };
        MySingleton.getInstance(MainActivity.this).addToRequestQue(stringRequest);
    }

    @SuppressLint("SetTextI18n")
    private void showSliderJSON(String json) {
        sliderJSON.clear();
        System.out.println("Data string :" + json);
        JSONObject jsonObject;
        JSONArray jsonArray;
        try {
            jsonObject = new JSONObject(json);
            jsonArray = jsonObject.getJSONArray("list");
            if (jsonArray.length() == 0) {

                setView();

            } else {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    Jobs jobs = new Jobs();
                    jobs.setId(jo.getInt("jobid"));
                    jobs.setImage(jo.getString("image"));
                    jobs.setJobtype(jo.getString("jobtype"));
                    jobs.setJobtitle(jo.getString("jobtitle"));
                    jobs.setJobtitleId(jo.getString("jobtitle_id"));
                    jobs.setEmployer(jo.getString("employer"));
                    jobs.setNoofvacancy(jo.getString("noofvancancy"));
                    jobs.setVerified(jo.getString("verified"));
                    jobs.setDate(jo.getString("ending"));
                    jobs.setDatediff(jo.getInt("datediff"));
                    jobs.setDescription(jo.getString("description"));
                    jobs.setDistrict(jo.getString("location"));
                    jobs.setViewType(jo.getString("view_type"));
                    jobs.setGrpJobId(jo.getString("group_job_id"));
                    jobs.setPopularEmpContent(jo.getString("popular_employer_string"));
                    jobs.setSerialNumber(jo.getString("serial_number"));
                    jobs.setAdditionalTag(jo.getString("additonal_label"));

                    Cursor c1 = localDB.getQry("SELECT * FROM ReadUnreadTable where read_id='" + jo.getInt("jobid") + "'");
                    c1.moveToFirst();
                    if (c1.getCount() == 0) {
                        jobs.setRead(false);
                    } else {
                        jobs.setRead(true);
                    }
                    c1.close();

                    sliderJSON.add(jobs);
                }

                setView();
            }
        } catch (JSONException e) {
            Log.e("tag", "" + e);
        }
    }

    public class locationSelectionAdapter extends ArrayAdapter {

        Context context;
        List<Item> list;
        LayoutInflater inflater;
        List<Item> orig;

        locationSelectionAdapter(Context context, int resource, List<Item> list) {
            super(context, resource, list);
            this.context = context;
            this.list = list;
            orig = new ArrayList<Item>();
            orig.addAll(list);
        }

        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_list_item, null);

            TextView txt = view.findViewById(R.id.name);
            final CheckBox checkBox = view.findViewById(R.id.checkbox);
            txt.setText(list.get(position).getItem());
            if (!nativeLocation.isEmpty() && nativeLocation.equals(String.valueOf(list.get(position).getId()))) {
                checkBox.setChecked(true);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                    } else {
                        checkBox.setChecked(true);
                    }
                }
            });

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        nativeLocation = String.valueOf(list.get(position).getId());
                        nativeLocationName = String.valueOf(list.get(position).getItem());
                        if (!pref.getString(MainActivity.this, U.SH_USER_DISTRICT_ID).isEmpty()) {
                            distId = pref.getString(MainActivity.this, U.SH_USER_DISTRICT_ID);
                        }
                        button.setAlpha(1F);
                        button.setEnabled(true);
                        notifyDataSetChanged();
                    } else {
                        nativeLocation = "";
                        nativeLocationName = "";
                        button.setAlpha(0.5F);
                        button.setEnabled(false);
                        notifyDataSetChanged();
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

            if (list.size() == 0) {
                noText.setVisibility(View.VISIBLE);
            } else {
                noText.setVisibility(View.GONE);
            }

            notifyDataSetChanged();
        }
    }

    public class SelectionAdapter extends ArrayAdapter {

        Context context;
        List<Item> list;
        LayoutInflater inflater;
        List<Item> orig;

        SelectionAdapter(Context context, int resource, List<Item> list) {
            super(context, resource, list);
            this.context = context;
            this.list = list;
            orig = new ArrayList<Item>();
            orig.addAll(list);
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_list_item, null);

            TextView txt = view.findViewById(R.id.name);
            final CheckBox checkBox = view.findViewById(R.id.checkbox);
            txt.setText(list.get(position).getItem());

            if (selectedDistrict.contains("" + list.get(position).getId())) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                    } else {
                        checkBox.setChecked(true);
                    }
                }
            });

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (selectedDistrict.size() < 3) {
                            button.setAlpha(1F);
                            button.setEnabled(true);
                            selectedDistrict.add("" + list.get(position).getId());
                            selectedDistrictName.add("" + list.get(position).getItem());
                        } else {
                            Toast.makeText(context, "நீங்கள் ஏற்கனவே மூன்று மாவட்டங்களை தேர்வு செய்துவிட்டீர்கள்", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }
                    } else {
                        selectedDistrict.remove("" + list.get(position).getId());
                        selectedDistrictName.remove("" + list.get(position).getItem());
                        if (selectedDistrict.size() == 0) {
                            button.setAlpha(0.5F);
                            button.setEnabled(false);
                        }
                    }
                }
            });
            return view;
        }

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<String> mFragmentTitleList = new ArrayList<>();
        private final List<Fragment> mFragmentList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
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

        private void addFragment(String title, Fragment fragment) {
            mFragmentTitleList.add(title);
            mFragmentList.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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

    public class CustomerCareAdapter extends ArrayAdapter {

        public TextView title, ans;
        public int[] colorArray = {R.color.skyblue_thick, R.color.voilet, R.color.thick_green, R.color.pink,
                R.color.light_green, R.color.colorPrimary, R.color.green, R.color.browish_red};
        Context context;
        List<Item> list;
        LayoutInflater inflater;
        LinearLayout textLay;
        Button btn;

        public CustomerCareAdapter(Context context, int resource, List<Item> list) {
            super(context, resource, list);
            this.context = context;
            this.list = list;
        }

        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.cus_care_lay, null);
            btn = view.findViewById(R.id.btn);
            title = view.findViewById(R.id.title);
            title.setText(list.get(position).getItem());

            ans = view.findViewById(R.id.ans);
            ans.setText(list.get(position).getCount());

            textLay = view.findViewById(R.id.text_lay);
            textLay.setBackgroundColor(context.getResources().getColor(colorArray[position]));

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (list.get(position).getId() == 1) {
                        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                        emailIntent.setType("plain/text");
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Regarding Nithra Jobs Subscription");
                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"jobs@nithra.mobi"});
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    } else if (list.get(position).getId() == 2) {
                        makeCall("9942267855");
                    }
                }
            });

            if (list.get(position).getId() == 1) {
                btn.setText("EMAIL US");
                btn.setBackgroundColor(context.getResources().getColor(colorArray[position]));
            } else if (list.get(position).getId() == 2) {
                btn.setText("CALL US");
                btn.setBackgroundColor(context.getResources().getColor(colorArray[position]));
            } else {
                btn.setVisibility(View.GONE);
            }
            return view;
        }

    }
}