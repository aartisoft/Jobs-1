package nithra.jobs.career.placement.utills;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.browser.customtabs.CustomTabsIntent;
import nithra.jobs.career.placement.R;

import static android.text.TextUtils.isEmpty;

/**
 * Created by arunrk on 30/5/17.
 */
public class U {

    //--------------------------------------------- AD UNIT IDs ------------------------------------

    public static final String ADMOB_JOBS_ICON_NATIVE = "ca-app-pub-4267540560263635/5500406873";
    public static final String ADMOB_JOBS_BACKPRESS_NATIVE = "ca-app-pub-4267540560263635/8539958591";
    public static final String ADMOB_APP_ID = "ca-app-pub-4267540560263635~7106995106";

    public static final String ADMOB_AD_UNIT_ID = "ca-app-pub-4267540560263635/9112641062";
    public static final String JOBS_NATIVE_DETAILPAGE = "ca-app-pub-4267540560263635/6414880717";
    public static final String BANNER_AD = "ca-app-pub-4267540560263635/1060461504";
    public static final String ADAPTIVE_BANNER_AD = "ca-app-pub-4267540560263635/4647021940";
    public static final String INDUS_AD_CAT = "ca-app-pub-4267540560263635/6967394304";
    public static final String INDUS_AD_EXIT = "ca-app-pub-4267540560263635/4013927907";
    public static final String INDUS_AD_NOTI = "ca-app-pub-4267540560263635/9920860706";
    public static final String FB_LIST_NATIVE = "3043185265706166_3043186162372743";
    public static final String FB_DETAIL_NATIVE = "3043185265706166_3043188129039213";
    public static final String FB_BANNER = "3043185265706166_3047305375294155";
    public static final String FB_NOTI_INS_EXIT = "3043185265706166_3047305948627431";


    //-------------------------------------------- Test ADS ----------------------------------------

//    public static final String ADMOB_AD_UNIT_ID = "ca-app-pub-3940256099942544/1044960115";
//    public static final String JOBS_NATIVE_DETAILPAGE = "ca-app-pub-3940256099942544/2247696110";
//    public static final String BANNER_AD = "ca-app-pub-3940256099942544/6300978111";
//    public static final String ADAPTIVE_BANNER_AD = "ca-app-pub-3940256099942544/6300978111";
//    public static final String INDUS_AD_CAT = "ca-app-pub-3940256099942544/1033173712";
//    public static final String INDUS_AD_EXIT = "ca-app-pub-3940256099942544/1033173712";
//    public static final String INDUS_AD_NOTI = "ca-app-pub-3940256099942544/1033173712";

    //--------------------------------------- Facebook Ad Units ------------------------------------

//    public static final String FB_LIST_NATIVE = "3043185265706166_3043186162372743";
//    public static final String FB_DETAIL_NATIVE = "3043185265706166_3043188129039213";
//    public static final String FB_BANNER = "3043185265706166_3047305375294155";
//    public static final String FB_NOTI_INS_EXIT = "3043185265706166_3047305948627431";
//    public static final String FB_JOBS_VIEW_INS_EXIT = "3043185265706166_3047304205294272";

    //---------------------------------------- shared preference keys ------------------------------

    public static final String SH_AD_PURCHASED = "SH_AD_PURCHASED";
    public static final String SH_ALARM_SET = "SH_ALARM_SET";
    public static final String SH_ANDROID_ID = "SH_ANDROID_ID";
    public static final String SH_RECENT_SEARCH_KEYS = "SH_RECENT_SEARCH_KEYS";

    public static final String SH_USER_TYPE = "SH_USER_TYPE";
    public static final String SH_FIRST_INSTALL = "SH_FIRST_INSTALL";
    public static final String SH_VERSION_CODE = "SH_VERSION_CODE";
    public static final String SH_VERSION_NAME = "SH_VERSION_NAME";
    public static final String SH_VIEWCOUNT = "SH_VIEWCOUNT";
    public static final String SH_REPORT = "SH_REPORT";
    public static final String SH_CALL_COUNT = "call_count";
    public static final String SH_CALL_CONFIRMATION_COUNT = "call_confirm_count";

    public static final String SH_BACK_AD_COUNT = "SH_BACK_AD_COUNT";
    public static final String SH_REMOTE_AD = "SH_REMOTE_AD";
    public static final String SH_REMOTE_VACANCY = "SH_REMOTE_VACANCY";
    public static final String SH_REMOTE_POSTEDBY = "SH_REMOTE_POSTEDBY";
    public static final String SH_REMOTE_PHONE_NUMBER = "SH_REMOTE_PHONE_NUMBER";
    public static final String SH_REMOTE_EMPLOYER_LINK = "SH_REMOTE_EMPLOYER_LINK";
    public static final String SH_REMOTE_LOCATION_TAB = "SH_REMOTE_LOCATION_TAB";

    public static final String SH_TODAY_QUOTE = "SH_TODAY_QUOTE";
    public static final String SH_TODAY_QUOTE_IMG = "SH_TODAY_QUOTE_IMG";
    public static final String SH_QUOTE_DATE = "SH_QUOTE_DATE";
    public static final String SH_LAST_LOGIN_DATE = "SH_LAST_LOGIN_DATE";
    public static final String SH_ASK_REGISTER_DATE = "SH_ASK_REGISTER_DATE";
    public static final String SH_USER_DISTRICT_ID = "SH_USER_DISTRICT_ID";
    public static final String SH_USER_DISTRICT_NAME = "SH_USER_DISTRICT_NAME";
    public static final String SH_USER_NATIVE_DISTRICT = "SH_USER_NATIVE_DISTRICT";
    public static final String SH_USERTYPE_SEND = "SH_USERTYPE_SEND";

    public static final String SH_FIRST_TIME_DATE = "SH_FIRST_TIME_DATE";
    public static final String SH_FIRST_TIME_ALL_JOBS = "SH_FIRST_TIME_ALL_JOBS";
    public static final String SH_FIRST_TIME_PRIVATE = "SH_FIRST_TIME_PRIVATE";
    public static final String SH_FIRST_TIME_STATE = "SH_FIRST_TIME_STATE";
    public static final String SH_FIRST_TIME_CENTRAL = "SH_FIRST_TIME_CENTRAL";
    public static final String SH_FIRST_TIME_CONSULTANCY = "SH_FIRST_TIME_CONSULTANCY";
    public static final String SH_FIRST_TIME_FRESHERS = "SH_FIRST_TIME_FRESHERS";
    public static final String SH_FIRST_TIME_RLOCATION = "SH_FIRST_TIME_RLOCATION";
    public static final String SH_FIRST_TIME_RSKILLS = "SH_FIRST_TIME_RSKILLS";
    public static final String SH_FIRST_TIME_RQUALIFICATION = "SH_FIRST_TIME_RQUALIFICATION";

    public static final String SH_SHOW_INS_AD = "SH_SHOW_INS_AD";
    public static final String SH_SHOW_SAVED_INS_AD = "SH_SHOW_SAVED_INS_AD";
    public static final String SH_ALL_JOBS_COUNT = "SH_ALL_JOBS_COUNT";
    public static final String SH_PRIVATE_JOBS_COUNT = "SH_PRIVATE_JOBS_COUNT";
    public static final String SH_STATE_JOBS_COUNT = "SH_STATE_JOBS_COUNT";
    public static final String SH_CENTRAL_JOBS_COUNT = "SH_CENTRAL_JOBS_COUNT";

    public static final String SH_NOTIFICATION_SOUND = "NotificationSound";
    public static final String SH_MUTE_NOTIFICATION = "MuteNotification";
    public static final String SH_PERSONAL_NOTIFICATION_COUNT = "SH_PERSONAL_NOTIFICATION_COUNT";
    public static final String SH_UNREG_NOTIFICATION_COUNT = "SH_UNREG_NOTIFICATION_COUNT";

    public static final String SH_REGISTER_DIA_SHOW = "SH_REGISTER_DIA_SHOW";
    public static final String SH_EMAIL_PERMISSION_DIA_SHOW = "permissionEmail";
    public static final String SH_STORAGE_PERMISSION = "photopermission";
    public static final String SH_CAMERA_PERMISSION = "photoShare";

    public static final String SH_INFO_WHATSNEW = "SH_INFO_WHATSNEW";
    public static final String SH_INFO_DIALOG = "SH_INFO_DIALOG";
    public static final String SH_FILTER_INFO_SHOW = "info_show";
    public static final String SH_CONSULTANCY_INFO_SHOW = "SH_CONSULTANCY_INFO_SHOW";
    public static final String SH_SEARCH_INFO_SHOW = "searchInfo";
    public static final String SH_QUALIFICATION_INFO_SHOW = "Qualification_info_show";
    public static final String SH_TESTIMONIAL_INFO_SHOW = "SH_TESTIMONIAL_INFO_SHOW";

    public static final String SH_GUIDE_WINDOW_1 = "guide_window1";
    public static final String SH_GUIDE_WINDOW_2 = "guide_window2";
    public static final String SH_GUIDE_WINDOW_3 = "guide_window3";
    public static final String SH_GUIDE_WINDOW_4 = "guide_window4";
    public static final String SH_GUIDE_WINDOW_5 = "guide_window5";

    //-------------------------------- Employee Registration ---------------------------------------

    public static final String SH_OTP_SUCCESS = "sh_otp_success";
    public static final String SH_EMPLOYEE_ID = "SH_EMPLOYEE_ID";
    public static final String SH_RECOMMENDED_DIA_SHOW = "SH_RECOMMENDED_DIA_SHOW";
    public static final String SH_NAME = "SH_NAME";
    public static final String SH_EMAIL = "SH_EMAIL";
    public static final String SH_MOBILE = "SH_MOBILE";
    public static final String SH_ALTERNATE_MOBILE = "SH_ALTERNATE_MOBILE";
    public static final String SH_GENDER = "SH_GENDER";
    public static final String SH_MARITAL_STATUS = "SH_MARITAL_STATUS";
    public static final String SH_DOB = "SH_DOB";
    public static final String SH_AGE = "SH_AGE";
    public static final String SH_COURSE = "SH_COURSE";
    public static final String SH_CERTIFIED_COURSE = "SH_CERTIFIED_COURSE";
    public static final String SH_WORK_STATUS = "SH_WORK_STATUS";
    public static final String SH_SKILLS = "SH_SKILLS";
    public static final String SH_CATEGORY = "SH_CATEGORY";
    public static final String SH_TITLE = "SH_TITLE";
    public static final String SH_RESUME = "SH_RESUME";
    public static final String SH_JOBLOCATION = "SH_JOBLOCATION";
    public static final String SH_OTP = "SH_OTP";
    public static final String SH_DETAILS_SHOWN = "SH_DETAILS_SHOWN";
    public static final String SH_CHANGE_MOBILE = "SH_CHANGE_MOBILE";
    public static final String SH_NATIVE_LOCATION = "SH_NATIVE_LOCATION";
    public static final String SH_NATIVE_LOCATION_NAME = "SH_NATIVE_LOCATION_NAME";
    public static final String SH_ADDED_SKILLS = "SH_ADDED_SKILLS";
    public static final String SH_PRIVACY_POLICY = "SH_PRIVACY_POLICY";
    public static final String SH_COMPLETED_JSON = "SH_COMPLETED_JSON";
    public static final String SH_PHOTO = "SH_PHOTO";
    public static final String SH_MOBILE_SHOWN = "SH_MOBILE_SHOWN";
    public static final String SH_SIGN_UP_SUCCESS = "SH_SIGN_UP_SUCCESS";
    public static final String SH_BLOCKED_USER = "SH_BLOCKED_USER";
    public static final String SH_TERMS_CONDITIONS = "SH_TERMS_CONDITIONS";
    public static final String SH_RJOBS_FLAG = "SH_RJOBS_FLAG";

    public static final String SH_REGISTRATION_TASK = "registrationTask";
    public static final String SH_REGISTRATION_FLAG = "registrationCheckFlag";

    public static final String SH_EMAIL_DIA_SHOW = "emailFetch";

    //-------------------------------- Employer Registration ---------------------------------------

    public static final String SH_EMPLOYER_ID = "SH_EMPLOYER_ID";
    public static final String SH_EMP_OTP_SUCCESS = "SH_EMP_OTP_SUCCESS";
    public static final String SH_EMP_SIGN_UP_SUCCESS = "SH_EMP_SIGN_UP_SUCCESS";

    public static final String SH_COMPANY_ID = "SH_COMPANY_ID";
    public static final String SH_EMP_NAME = "SH_EMP_NAME";
    public static final String SH_EMP_EMAIL = "SH_EMP_EMAIL";
    public static final String SH_EMP_MOBILE = "SH_EMP_MOBILE";
    public static final String SH_PLAN_ID = "SH_PLAN_ID";
    public static final String SH_EMP_CHANGE_MOBILE = "SH_CHANGE_MOBILE";
    public static final String SH_EMP_DESIGNATION = "SH_EMP_DESIGNATION";
    public static final String SH_COMPANY_NAME_ENG = "SH_COMPANY_NAME_ENG";
    public static final String SH_COMPANY_NAME_TAM = "SH_COMPANY_NAME_TAM";
    public static final String SH_COMPANY_ADDRESS = "SH_COMPANY_ADDRESS";
    public static final String SH_GST_NO = "SH_GST_NO";
    public static final String SH_WEB_URL = "SH_WEB_URL";
    public static final String SH_PANCARD_NO = "SH_PANCARD_NO";
    public static final String SH_INDUSTRY_TYPE = "SH_INDUSTRY_TYPE";
    public static final String SH_COMPANY_PROOF_TYPE = "SH_COMPANY_PROOF_TYPE";
    public static final String SH_PERSON_PROOF_TYPE = "SH_PERSON_PROOF_TYPE";
    public static final String SH_COMPANY_ID_PROOF_TYPE = "SH_COMPANY_ID_PROOF_TYPE";
    public static final String SH_PASSWORD = "SH_PASSWORD";

    public static final String SH_BLOCKED_EMPLOYER = "SH_BLOCKED_EMPLOYER";
    public static final String SH_EMP_COMPLETED_JSON = "SH_EMP_COMPLETED_JSON";

    public static final String SH_COMPANY_LOGO_URI = "SH_COMPANY_LOGO_URI";
    public static final String SH_COMPANY_LOGO = "SH_COMPANY_LOGO";

    public static final String SH_PERSON_PROOF_URI = "SH_PERSON_PROOF_URI";
    public static final String SH_PERSON_PROOF = "SH_PERSON_PROOF";

    public static final String SH_COMPANY_PROOF_URI = "SH_COMPANY_PROOF_URI";
    public static final String SH_COMPANY_PROOF = "SH_COMPANY_PROOF";

    public static final String SH_COMPANY_ID_PROOF_URI = "SH_COMPANY_ID_PROOF_URI";
    public static final String SH_COMPANY_ID_PROOF = "SH_COMPANY_ID_PROOF";

    public static final String SH_DATA = "SH_DATA";

    //-------------------------------- Ranjith -----------------------------------------------
    public static final String PROMO_TAB = "PROMO_TAB";
    public static final String EMPLOYER_STATUS = "EMPLOYER_STATUS";
    public static final String EMPLOYER_PROMO_CODE = "EMPLOYER_PROMO_CODE";
    public static final String EARNING_COMPANY_ID = "EARNING_COMPANY_ID";
    public static final String PROMO_PLAN_OPEN = "PROMO_PLAN_OPEN";
    public static final String PROMO_REFRESH = "PROMO_REFRESH";
    public static final String RESTART_TO_EMPLOYEE = "RESTART_TO_EMPLOYEE";
    public static final String RESTART = "RESTART";
    public static final String PROMO_NOTI = "PROMO_NOTI";

    //-------------------------------- Photo Jobs -----------------------------------------------

    public static final String SH_PJ_MOBILE = "SH_PHOTO_MOBILE";
    public static final String SH_PJ_USERID = "SH_PJ_USERID";
    public static final String SH_PJ_OTP_SUCCESS = "SH_PJ_OTP_SUCCESS";
    public static final String SH_PJ_SIGN_UP_SUCCESS = "SH_PJ_SIGN_UP_SUCCESS";

    //-------------------------------- other Strings --------------------------------------------

    public static final String GCM = "GCM";
    public static final String DEEPLINK = "DEEPLINK";
    public static final String JOBCLICK = "JOBCLICK";
    public static final String SEARCHID = "SEARCHID";

    public static final String GET_SHORTLISTED = "getShortlisted";
    public static final String TITLEWISE = "TITLEWISE";

    public static final String PERSON_PROOF_CAPTION = "Person Proof";
    public static final String COMPANY_ID_CAPTION = "Company ID Proof";

    public static final String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnUrBqF3PxEnC6c8HG5dB11cFIBfl3lRWnKMIWrg4qNYz2x2Of9pOqBvj7NUuB8YYrjzMbLNXYXaJhGtY8bHzRJNjxnByGypk4RYEWzzrvnX251Io/QYie/EGv4psxDNbwqH+Vvw2WtBaNbXdAPWknwrIb7Bv9xov3wPPBt7NoTeh269JNQUw89C+EL5csCB1ttxxA0Kde1jv/D6X6LcYdO+p08fDvROZodxtav6dkyt+VK6cH+IS5a5WsxSFSmf/U8U00Clrmi9wkQJLRJkbZ9qGbMVjxaRifxHWxyct4Wqi7d28m1YV6p6DN0Rv3c+x7dOHEedZvNXPwObGN1DpKwIDAQAB";
    public static final String PURCHASE_KEY = "removeads";
    //public static final String PURCHASE_KEY = "";
    public static final String RJOB = "rjob";
    public static final String ALLJOBS = "alljobs";
    public static final String FJOBS = "fjob";
    public static final String VIEWAPPLICANTS = "view_applicants";
    public static final String NOTIJOBS = "notijob";
    public static final String GROUPJOBS = "groupjob";
    public static final String ADVSEARCH = "ADVSEARCH";
    public static final String NOTIFICATION = "notification";
    public static final String DATE_TIME_FORMAT_WOS = "dd-MM-yyyy HH:mm";
    public static final String DATE_TIME_FORMAT = "dd-MM-yyyy";

    public static final String LIVE = "live";
    public static final String WAITING = "waiting";
    public static final String HISTORY = "history";
    public static final String REPOST = "repost";
    public static final String DRAFT = "draft";

    //        public static final String HOME_PAGE_IMAGE_URL = "https://s3.ap-south-1.amazonaws.com/nithra-jobs/php-testing/detailjobs/detailjobs_3.webp";
    public static final String HOME_PAGE_IMAGE_URL = "https://s3.ap-south-1.amazonaws.com/nithra-images/Jobs_Image/appad/job_dynamic_label.webp";
    public static final String HOME_PAGE_IMAGE_REDIRECT_URL = "https://s3.ap-south-1.amazonaws.com/nithra-images/Jobs_Image/appad/job_dynamic_url.html";

    public static final String EMPLOYER_POSTING_LINK = "https://s3.ap-south-1.amazonaws.com/nithra-jobs/url/Job_Post_App.html";

    public static final String HOW_TO_USE_URL = "https://nithrajobs.com/job_youtube.php";
    public static final String HOW_TO_POST_URL = "https://nithrajobs.com/job_youtube_posting.php";
    public static final String EMPLOYER = "EMPLOYER";
    public static final String EMPLOYEE = "EMPLOYEE";
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String UTM_SOURCE = "http://bit.ly/nithrajobsshare";
    public static final String UTM_PROMO = "https://bit.ly/2WvHKmE";
    public static final String UTM_EVENT = "https://bit.ly/2MaVCwK";
    public static final String SH_RATE_US = "SH_RATE_US";
    public static final String OUR_APPS = "https://play.google.com/store/apps/developer?id=Nithra";
    public static final String INA = "இணைய இணைப்பை சரிபார்க்கவும்";
    public static final String ERROR = "Something Went Wrong";
    public static final String SERVER_ERROR = "தொழில்நுட்ப கோளாறு ஏற்பட்டுள்ளது, சிறிது நேரத்திற்கு பின்னர் முயற்சி செய்யவும்.";
    public static final String FAV_REMOVED = "விருப்பமானவையிலிருந்து நீக்கப்பட்டது";
    public static final String FAV_SUCCESS = "விருப்பமானவையில் சேர்க்கப்பட்டது";
    public static final String SHORTLIST_REMOVED = "Removed from Shortlisted";
    public static final String SHORTLIST_SUCCESS = "Added to Shortlisted";
    public static final String ANI = "செயலியை தரவிறக்கம் செய்யவும்";
    public static final String DNF = "தகவல் ஏதும் இல்லை";
    public static final String NO_FAV = "விருப்பமானவை ஏதும் இல்லை";
    public static final String SEARCH_NO_JOB = "நீங்கள் தேடிய வேலை வாய்ப்பு தற்போது இல்லை.அனைத்து வேலை வாய்ப்புகளையும் காண (முகப்பு) பகுதியை பயன்படுத்தவும்.";
    public static final String NO_RJOBS = "நீங்கள் பதிவு செய்த தகவல்களுக்கான (சுயவிபரம்) வேலைவாய்ப்பு தற்போது இல்லை.அனைத்து வேலை வாய்ப்புகளையும் காண (அனைத்து வேலைவாய்ப்புகளையும் காண) பகுதியை பயன்படுத்தவும்.";
    private static final String PACKAGE = "nithra.jobs.career.placement";
    public static final String RATEUS = "market://details?id=" + PACKAGE;
    public static int HOME_PAGE = -1;
    public static int RECOMMENDED_PAGE = -1;
    public static int DISTRICT_PAGE = -1;
    public static int CATEGORY_PAGE = -1;
    public static int FAVOURITE_PAGE = -1;
    public static int REMINDER_PAGE = -1;
    public static int NLOCATION_PAGE = -1;
    public static String[] catArray = {"reccommend", "skills", "qualification", "location", "todaysjob", "state", "central"};

    public static String[] bmArray = {"நீங்கள் எதிர்பார்த்து காத்திருந்த வேலைவாய்ப்பு தகவல்கள் !",
            "உங்களின் திறனுக்கான வேலைவாய்ப்பை காண வேண்டுமா ?",
            "நீங்கள் படித்த படிப்பிற்கான வேலைவாய்ப்பு தகவல்கள் !",
            "நீங்கள் விரும்பிய ஊரில் வேலைவாய்ப்பு தகவல்கள் !",
            "உங்களுக்காக பதிவிடப்பட்ட இன்றைய வேலைவாய்ப்புகள் !",
            "தமிழக அரசுப் பணியில் சேர உங்களுக்கான வேலைவாய்ப்பு தகவல்கள் !",
            "மத்திய அரசுப் பணியில் சேர உங்களுக்கான வேலைவாய்ப்பு தகவல்கள் !"};

    public static String[] UnReg_catArray = {"District", "RecentlyViewedJobs",
            "SearchRelatedJobs", "AllJobs", "PrivateJobs", "stateJobs", "centralJobs"};

    public static String[] UnReg_bmArray = {
            "நீங்கள் தேர்வு செய்த மாவட்டத்தில் உள்ள வேலைவாய்ப்பு தகவல்கள் !",
            "சமீபத்தில் நீங்கள் பார்வையிட்ட வேலைவாய்ப்பு தகவல்கள் !",
            "நீங்கள் எதிர்பார்த்து காத்திருந்த வேலைவாய்ப்பு தகவல்கள் !",
            "தற்போது அறிவிக்கப்பட்டுள்ள அனைத்து வகையான வேலைவாய்ப்பு தகவல்களின் தொகுப்பு !",
            "தனியார் துறையில் உள்ள பல புதிய வேலைவாய்ப்பு தகவல்கள் !",
            "தமிழக அரசுப் பணியில் சேர உங்களுக்கான வேலைவாய்ப்பு தகவல்கள் !",
            "மத்திய அரசுப் பணியில் சேர உங்களுக்கான வேலைவாய்ப்பு தகவல்கள் !"};

    public static ProgressDialog mProgress;
    public static Dialog shareDialog;
    public static java.util.List<ResolveInfo> listApp;
    private static Bitmap scaledBitmap = null;

    public static ProgressDialog mProgress(Context context, String txt, Boolean aBoolean) {
        mProgress = new ProgressDialog(context);
        mProgress.setMessage(txt);
        mProgress.setCancelable(aBoolean);
        return mProgress;
    }

    public static boolean panCardValidation(String value) {
        String ss = value.trim();
        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
        Matcher matcher = pattern.matcher(ss);
        return matcher.matches();
    }

    public static boolean GSTValidation(String value) {
        String ss = value.trim();
        Pattern pattern = Pattern.compile("[0-9]{2}[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9A-Za-z]{1}[Z]{1}[0-9a-zA-Z]{1}");
        Matcher matcher = pattern.matcher(ss);
        return matcher.matches();
    }

    public static boolean EmailValidation(String value) {
        String ss = value.trim();
        Pattern pattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
        Matcher matcher = pattern.matcher(ss);
        Log.e("email", "" + ss + matcher.matches());
        return matcher.matches();
    }

    private static void share(ResolveInfo appInfo, String sharefinal, Context context) {


        if (appInfo.activityInfo.packageName.equals("com.whatsapp")) {
            sharefinal = sharefinal.replaceAll("%", "%25");
            sharefinal = sharefinal.replaceAll("&", "%26");
            sharefinal = sharefinal.replaceAll("\\+", "%2B");
            sharefinal = sharefinal.replaceAll("#", "%23");
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/*");
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
            Uri uriUrl = Uri.parse("whatsapp://send?text=" + sharefinal);
            sendIntent.setAction(Intent.ACTION_VIEW);
            sendIntent.setData(uriUrl);
            sendIntent.setPackage("com.whatsapp");
            //sendIntent.setComponent(new ComponentName(appInfo.activityInfo.packageName, appInfo.activityInfo.name));
            context.startActivity(sendIntent);

        } else {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
            sendIntent.putExtra(
                    Intent.EXTRA_TEXT, "" + sharefinal);
            sendIntent.setComponent(new ComponentName(appInfo.activityInfo.packageName, appInfo.activityInfo.name));
            sendIntent.setType("text/*");
            context.startActivity(sendIntent);
        }
    }

    @SuppressLint("WrongConstant")
    private static java.util.List<ResolveInfo> showAllShareApp(Context context) {

        java.util.List<ResolveInfo> mApps = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        intent.setType("text/plain");
        PackageManager pManager = context.getPackageManager();
        mApps = pManager.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        return mApps;
    }

    public static Dialog shareDialog(final Context context, String result) {
        shareDialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
        shareDialog.setContentView(R.layout.share_dialog);

        ListView share_list = shareDialog.findViewById(R.id.list);

        listApp = showAllShareApp(context);

        if (listApp != null) {
            share_list.setAdapter(new MyAdapter(context));
            final String finalResult = result;
            share_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    share(listApp.get(position), finalResult, context);
                    shareDialog.dismiss();
                }
            });
        }
        return shareDialog;
    }

    public static String timeToDateFormate(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MMM-yyyy";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static void custom_tabs(Context context, String url) {
        try {
            CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
            intentBuilder.setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            intentBuilder.setToolbarColor(context.getResources().getColor(R.color.colorPrimary));
            intentBuilder.build().launchUrl(context, Uri.parse(url));
        } catch (ActivityNotFoundException | NullPointerException e) {
            e.printStackTrace();
            if (!url.substring(0, 4).equals("http")) {
                url = "http://" + url;
            }
            try {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_VIEW);
                i.addCategory(Intent.CATEGORY_BROWSABLE);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public static Bitmap compressImage(Context context, String imageUri) {

        String filePath = getRealPathFromURI(context, imageUri);
        //Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

        //by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
        //you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        //max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 0;
        float maxWidth = 0;
        float imgRatio = 0;
        float maxRatio = 0;

        try {
            maxHeight = 816.0f;
            maxWidth = 612.0f;
            imgRatio = actualWidth / actualHeight;
            maxRatio = maxWidth / maxHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }

        //width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        //setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

        //inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

        //this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            //load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        //check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return scaledBitmap;
    }

    public static String getRealPathFromURI(Context context, String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static String versionname_get(Context context) {
        PackageInfo pInfo = null;

        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return pInfo.versionName;
    }

    public static int versioncode_get(Context context) {
        PackageInfo pInfo = null;

        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return pInfo != null ? pInfo.versionCode : 0;
    }

    public static void toast_center(Context context, String str) {
        Toast toast = Toast.makeText(context, "" + str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static String time_convert(String time) {
        time = time.replaceAll(" ", "");
        time = time.replaceAll("am", "");
        time = time.replaceAll("pm", "");
        String[] strr = time.split("\\:");
        return "" + U.am_pm1(Integer.parseInt("" + strr[0]), Integer.parseInt("" + strr[1]));
    }

    private static String am_pm1(int hur, int min) {
        String AM_PM = "AM";
        if (hur >= 12) {
            hur = hur - 12;
            AM_PM = "PM";
        } else {
            AM_PM = "AM";
        }
        if (hur == 0) {
            hur = 12;
        }
        return U.pad("" + hur) + ":" + U.pad("" + min) + " " + AM_PM;
    }

    private static String pad(String str) {
        if (str.length() == 1) {
            str = "0" + str;
        }
        return str;
    }

    public static String getAndroidId(Context context) {
        try {
            return Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean appInstalledOrNot(String uri, Context context) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public static String getDeviceName() {

        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String Brand = Build.BRAND;
        String Product = Build.PRODUCT;

        return manufacturer + "-" + model + "-" + Brand + "-" + Product;
    }

    public static PackageInfo getVersion(Context context) {
        PackageInfo pInfo = null;

        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pInfo;
    }

    public static void shareText(Context context, String message) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT,
                "நித்ரா வேலைவாய்ப்பு செயலி வழியாக பகிரப்பட்டது. செயலியை தரவிறக்கம் செய்ய: " + U.UTM_SOURCE + "\n\n" +
                        message);
        context.startActivity(Intent.createChooser(intent, "Share"));
    }

    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = null;
            if (connectivityManager != null) {
                activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            }
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } else return false;
    }

    public static String s2d(int num) {
        return (num < 10 ? "0" : "") + num;
    }

    public static Date convertStringtoDate(String dateTime, String format) {

        Date date = null;

        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.ENGLISH);

        try {
            date = formatter.parse(dateTime);
            System.out.println("convert date : 1" + date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String changeDateFormats(String dateString) {
        String strDate = "";
        try {
            //create SimpleDateFormat object with source string date format
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            //parse the string into Date object
            Date date = sdfSource.parse(dateString);

            //create SimpleDateFormat object with desired date format
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfDestination = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

            //parse the date into another format
            strDate = sdfDestination.format(date);

            System.out.println("Converted date is : " + strDate);

        } catch (ParseException pe) {
            System.out.println("Parse Exception : " + pe);
        }
        return strDate;
    }

    public static String convertDatetoString(Date dateString, String format) {
        String d;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(format);
        d = formatter.format(dateString);
        return d;
    }

    public static String addDaysInCal(int value) {
        try {
            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            Date date = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, value);
            final Date laterDate = c.getTime();
            return dateFormat.format(laterDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void PQ(String selectQuery) {
        System.out.println("Current query : " + selectQuery);
    }

    public static String FCAPS(String str) {
        if (str == null) str = "";
        return str.length() != 0 ? str.substring(0, 1).toUpperCase() + str.substring(1) : "";
    }

    public static byte[] fileToByteArray(String fileName) {
        File file = new File(fileName);
        //init array with file length
        byte[] bytesArray = new byte[(int) file.length()];

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            try {
                fis.read(bytesArray); //read file into bytes[]
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bytesArray;
    }

    public static String ListToString(List<String> list) {
        String filter;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(",");
        }
        filter = sb.toString();
        filter = filter.substring(0, filter.length() - 1);
        return filter;
    }

    public static String ListToString(List<String> list, String symbol) {
        String filter;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(symbol);
        }
        filter = sb.toString();
        filter = filter.substring(0, filter.length() - symbol.length());
        Log.e("values_list", "" + filter);
        return filter;
    }

    public static void date_put(Context context, String str, int val) {
        Calendar calendar = Calendar.getInstance();
        long next_hour = calendar.getTimeInMillis() + val * DateUtils.DAY_IN_MILLIS;

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf1 = new SimpleDateFormat("dd/M/yyyy");
        Date results = new Date(next_hour);
        String formatted = sdf1.format(results);

        StringTokenizer st2 = new StringTokenizer(formatted, "/");
        int rep_day = Integer.parseInt(st2.nextToken());
        int rep_month = Integer.parseInt(st2.nextToken());
        int rep_year = Integer.parseInt(st2.nextToken());

        rep_month = rep_month - 1;

        String strdate = rep_day + "/" + rep_month + "/" + rep_year;

        SharedPreference sharedPreference = new SharedPreference();
        sharedPreference.putString(context, str, strdate);
    }

    public static String[] substringsBetween(final String str, final String open, final String close) {
        if (str == null || isEmpty(open) || isEmpty(close)) {
            return null;
        }
        final int strLen = str.length();
        if (strLen == 0) {
            return new String[0];
        }
        final int closeLen = close.length();
        final int openLen = open.length();
        final List<String> list = new ArrayList<>();
        int pos = 0;
        while (pos < strLen - closeLen) {
            int start = str.indexOf(open, pos);
            if (start < 0) {
                break;
            }
            start += openLen;
            final int end = str.indexOf(close, start);
            if (end < 0) {
                break;
            }
            list.add(str.substring(start, end));
            pos = end + closeLen;
        }
        if (list.isEmpty()) {
            return null;
        }
        return list.toArray(new String[list.size()]);
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else
            return dir != null && dir.isFile() && dir.delete();
    }

    public static Boolean clr_chace(Context context) {
        Boolean aBoolean = false;
        SharedPreference sharedPreference = new SharedPreference();
        Calendar calendar = Calendar.getInstance();
        long next_hour = calendar.getTimeInMillis();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf1 = new SimpleDateFormat("dd/M/yyyy");
        Date result = new Date(next_hour);
        String formatted = sdf1.format(result);

        StringTokenizer st2 = new StringTokenizer(formatted, "/");
        int rep_day = Integer.parseInt(st2.nextToken());
        int rep_month = Integer.parseInt(st2.nextToken());
        int rep_year = Integer.parseInt(st2.nextToken());

        rep_month = rep_month - 1;

        String today_date = rep_day + "/" + rep_month + "/" + rep_year;

        Date date_today = null, date_app_update = null;

        try {
            date_today = sdf1.parse(today_date);
            if (!sharedPreference.getString(context, "clr_chace").equals("")) {
                date_app_update = sdf1.parse(sharedPreference.getString(context, "clr_chace"));
            } else {
                date_app_update = sdf1.parse(today_date);
            }

        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("clr_chace : error");
        }


        if (sharedPreference.getString(context, "clr_chace").equals("")) {
            aBoolean = true;
        } else {
            if (date_today != null && date_today.compareTo(date_app_update) >= 0) {
                aBoolean = true;
            }
        }
        return aBoolean;
    }

    public static void webview(Context context, String str, WebView webView) {
        String bodyFont = "<style> body{font-size:" + context.getResources().getString(R.string.fontsize) + "px; LINE-HEIGHT:" + context.getResources().getString(R.string.lineHeight) + "px;}</style>";

        String exp = "<!DOCTYPE html><head>" + bodyFont + " </head><body> <div align=justify>  <font face=Bamini color=#FFFFFF>" + str + "</font>  </div></body></html>";

        exp = exp.replaceAll("<font face=bamini>", "<font  color=#000000>");

        webView.loadDataWithBaseURL("file:///android_asset/images/", exp, "text/html", "utf-8", null);
    }

    public static String currentDate() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat(U.DATE_FORMAT);
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    public static int numberShuffle(int start, int stop) {
        List<Integer> adPositionList = new ArrayList();
        for (int i = start; i <= stop; i++) {
            adPositionList.add(i);
        }
        Collections.shuffle(adPositionList);
        Log.e("shuffle_position", "" + adPositionList.get(0));
        return adPositionList.get(0);
    }

    static class MyAdapter extends BaseAdapter {

        PackageManager pm;
        Context context;

        public MyAdapter(Context context) {
            this.context = context;
            this.pm = context.getPackageManager();
        }


        @Override
        public int getCount() {
            return listApp.size();
        }

        @Override
        public Object getItem(int position) {
            return listApp.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.layout_share_app, parent, false);
                holder.ivLogo = convertView.findViewById(R.id.iv_logo);
                holder.tvAppName = convertView.findViewById(R.id.tv_app_name);
                holder.tvPackageName = convertView.findViewById(R.id.tv_app_package_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ResolveInfo appInfo = listApp.get(position);
            holder.ivLogo.setImageDrawable(appInfo.loadIcon(pm));
            holder.tvAppName.setText(appInfo.loadLabel(pm));
            holder.tvPackageName.setText(appInfo.activityInfo.packageName);

            return convertView;
        }
    }

    static class ViewHolder {
        ImageView ivLogo;
        TextView tvAppName;
        TextView tvPackageName;
    }
}

