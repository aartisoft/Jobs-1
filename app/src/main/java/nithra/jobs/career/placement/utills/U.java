package nithra.jobs.career.placement.utills;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import nithra.jobs.career.placement.R;

import static android.provider.Settings.System.DATE_FORMAT;
import static android.text.TextUtils.isEmpty;

/**
 * Created by arunrk on 30/5/17.
 */

public class U {

    //---------------------------------------- AD UNIT IDs ---------------------

    public static final String ADMOB_AD_UNIT_ID = "ca-app-pub-4267540560263635/9112641062";
    public static final String ADMOB_JOBS_ICON_NATIVE = "ca-app-pub-4267540560263635/5500406873";
    public static final String JOBS_NATIVE_DETAILPAGE = "ca-app-pub-4267540560263635/6414880717";
    public static final String ADMOB_JOBS_BACKPRESS_NATIVE = "ca-app-pub-4267540560263635/8539958591";
    public static final String ADMOB_APP_ID = "ca-app-pub-4267540560263635~7106995106";

    public static final String BANNER_AD = "ca-app-pub-4267540560263635/1060461504";
    public static final String INDUS_AD_CAT = "ca-app-pub-4267540560263635/6967394304";
    public static final String INDUS_AD_EXIT = "ca-app-pub-4267540560263635/4013927907";
    public static final String INDUS_AD_NOTI = "ca-app-pub-4267540560263635/9920860706";

    //---------------------------------------- shared preference keys ---------------------

    public static final String SH_OTP_SUCCESS = "sh_otp_success";
    public static final String SH_EMPLOYEE_ID = "SH_EMPLOYEE_ID";
    public static final String SH_ANDROID_ID = "SH_ANDROID_ID";
    public static final String SH_RECOMMENDED_DIA_SHOW = "SH_RECOMMENDED_DIA_SHOW";
    public static final String SH_NAME = "SH_NAME";
    public static final String SH_EMAIL = "SH_EMAIL";
    public static final String SH_MOBILE = "SH_MOBILE";
    public static final String SH_ALTERNATE_MOBILE = "SH_ALTERNATE_MOBILE";
    public static final String SH_GENDER = "SH_GENDER";
    public static final String SH_MARITAL_STATUS ="SH_MARITAL_STATUS";
    public static final String SH_DOB = "SH_DOB";
    public static final String SH_AGE = "SH_AGE";
    public static final String SH_COURSE = "SH_COURSE";
    public static final String SH_WORK_STATUS = "SH_WORK_STATUS";
    public static final String SH_SKILLS = "SH_SKILLS";
    public static final String SH_JOBLOCATION = "SH_JOBLOCATION";
    public static final String SH_OTP = "SH_OTP";
    public static final String SH_DETAILS_SHOWN = "SH_DETAILS_SHOWN";
    public static final String SH_CHANGE_MOBILE = "SH_CHANGE_MOBILE";
    public static final String SH_NATIVE_LOCATION = "SH_NATIVE_LOCATION";
    public static final String SH_ADDED_SKILLS = "SH_ADDED_SKILLS";
    public static final String SH_PRIVACY_POLICY = "SH_PRIVACY_POLICY";
    public static final String SH_COMPLETED_JSON = "SH_COMPLETED_JSON";
    public static final String SH_PHOTO = "SH_PHOTO";
    public static final String SH_PHOTO_URI = "SH_PHOTO_URI";
    public static final String SH_MOBILE_SHOWN = "SH_MOBILE_SHOWN";
    public static final String SH_SIGN_UP_SUCCESS = "SH_SIGN_UP_SUCCESS";
    public static final String SH_BLOCKED_USER = "SH_BLOCKED_USER";
    public static final String SH_TERMS_CONDITIONS = "SH_TERMS_CONDITIONS";
    public static final String SH_RJOBS_FLAG = "SH_RJOBS_FLAG";
    public static final String SH_REGISTRATION_TASK = "registrationTask";
    public static final String SH_REGISTRATION_FLAG = "registrationCheckFlag";

    public static final String SH_FIRST_INSTALL = "SH_FIRST_INSTALL";
    public static final String SH_VERSION_CODE = "SH_VERSION_CODE";
    public static final String SH_VERSION_NAME = "SH_VERSION_NAME";
    public static final String SH_VIEWCOUNT = "SH_VIEWCOUNT";
    public static final String SH_REPORT = "SH_REPORT";
    public static final String SH_INFO_DIALOG = "SH_INFO_DIALOG";

    public static final String SH_BACK_AD_COUNT = "SH_BACK_AD_COUNT";
    public static final String SH_REMOTE_AD = "SH_REMOTE_AD";
    public static final String SH_REMOTE_VACANCY = "SH_REMOTE_VACANCY";
    public static final String SH_REMOTE_POSTEDBY = "SH_REMOTE_POSTEDBY";
    public static final String SH_REMOTE_DISTRICT = "SH_REMOTE_DISTRICT";
    public static final String SH_REMOTE_PHONE_NUMBER = "SH_REMOTE_PHONE_NUMBER";

    public static final String SH_TODAY_QUOTE = "SH_TODAY_QUOTE";
    public static final String SH_TODAY_QUOTE_NAME = "SH_TODAY_QUOTE_NAME";
    public static final String SH_TODAY_QUOTE_IMG = "SH_TODAY_QUOTE_IMG";
    public static final String SH_QUOTE_DATE = "SH_QUOTE_DATE";
    public static final String SH_LAST_LOGIN_DATE = "SH_LAST_LOGIN_DATE";
    public static final String SH_ASK_REGISTER_DATE = "SH_ASK_REGISTER_DATE";
    public static final String SH_ASK_UPDATE_DATE = "SH_ASK_UPDATE_DATE";
    public static final String SH_APP_INFO_SHOW = "SH_APP_INFO_SHOW";

    public static final String SH_FIRST_TIME_DATE = "SH_FIRST_TIME_DATE";
    public static final String SH_FIRST_TIME_PRIVATE = "SH_FIRST_TIME_PRIVATE";
    public static final String SH_FIRST_TIME_STATE = "SH_FIRST_TIME_STATE";
    public static final String SH_FIRST_TIME_CENTRAL = "SH_FIRST_TIME_CENTRAL";
    public static final String SH_FIRST_TIME_CONSULTANCY = "SH_FIRST_TIME_CONSULTANCY";
    public static final String SH_FIRST_TIME_FRESHERS = "SH_FIRST_TIME_FRESHERS";
    public static final String SH_FIRST_TIME_RECCOMMED = "SH_FIRST_TIME_RECCOMMED";
    public static final String SH_FIRST_TIME_RLOCATION = "SH_FIRST_TIME_RLOCATION";
    public static final String SH_FIRST_TIME_RSKILLS = "SH_FIRST_TIME_RSKILLS";
    public static final String SH_FIRST_TIME_RQUALIFICATION = "SH_FIRST_TIME_RQUALIFICATION";
    public static final String SH_REGISTER_DIA_SHOW = "SH_REGISTER_DIA_SHOW";
    public static final String SH_UPDATE_DIA_SHOW = "SH_UPDATE_DIA_SHOW";
    public static final String SH_EMAIL_DIA_SHOW = "emailFetch";
    public static final String SH_SEARCH_INFO_SHOW = "searchInfo";
    public static final String SH_QUALIFICATION_INFO_SHOW = "Qualification_info_show";
    public static final String SH_EMAIL_PERMISSION_DIA_SHOW = "permissionEmail";
    public static final String SH_PHOTO_PERMISSION_DIA_SHOW = "photopermission";
    public static final String SH_NOTIFICATION_SOUND = "NotificationSound";
    public static final String SH_MUTE_NOTIFICATION = "MuteNotification";

    public static final String SH_FILTER_INFO_SHOW = "info_show";
    public static final String SH_CONSULTANCY_INFO_SHOW = "SH_CONSULTANCY_INFO_SHOW";
    public static final String SH_SKILLS_CLICKED = "skills_clicked";
    public static final String SH_LOCATION_CLICKED = "location_clicked";
    public static final String SH_CATEGORY_CLICKED = "category_clicked";
    public static final String SH_WORKMODE_CLICKED = "workmode_clicked";
    public static final String SH_EXPERIENCE_CLICKED = "experience_clicked";
    public static final String SH_QUALIFICATION_CLICKED = "qualification_clicked";
    public static final String SH_JOBMODE_CLICKED = "jobmode_clicked";

    public static final String SH_GUIDE_WINDOW_1 = "guide_window1";
    public static final String SH_GUIDE_WINDOW_2 = "guide_window2";
    public static final String SH_GUIDE_WINDOW_3 = "guide_window3";
    public static final String SH_GUIDE_WINDOW_4 = "guide_window4";
    public static final String SH_GUIDE_WINDOW_5 = "guide_window5";

    //-------------------------------- other Strings --------------------------------------------

    public static final String GCM = "GCM";
    public static final String DEEPLINK = "DEEPLINK";
    public static final String JOBCLICK = "JOBCLICK";
    public static final String SEARCHID = "SEARCHID";

    public static final String RJOB = "rjob";
    public static final String ALLJOBS = "alljobs";
    public static final String FJOBS = "fjob";
    public static final String NOTIJOBS = "notijob";
    public static final String ADVSEARCH = "ADVSEARCH";
    public static final String NOTIFICATION = "notification";
    public static final String DATE_TIME_FORMAT_WOS = "dd-MM-yyyy HH:mm";
    public static final String DATE_TIME_FORMAT = "dd-MM-yyyy";

    public static final int HOME_PAGE = 0;
    public static final int RECOMMENDED_PAGE = 1;
    public static final int DISTRICT_PAGE = 2;
    public static final int CATEGORY_PAGE = 3;
    public static final int FAVOURITE_PAGE = 4;
    public static final int REMINDER_PAGE = 5;

    public static final String DATE_FORMAT = "dd/MM/yyyy";

    public static final String UTM_SOURCE = "http://bit.ly/nithrajobsshare";

    public static final String PACKAGE = "nithra.jobs.career.placement";

    public static final String RATEUS = "market://details?id=" +PACKAGE;
    public static final String SH_RATE_US = "SH_RATE_US";
    public static final String OUR_APPS = "https://play.google.com/store/apps/developer?id=Nithra";

    public static final String INA = "இணைய இணைப்பை சரிபார்க்கவும்";
    public static final String ERROR = "Something Went Wrong";
    public static final String FAV_REMOVED = "விருப்பமானவையிலிருந்து நீக்கப்பட்டது";
    public static final String FAV_SUCCESS = "விருப்பமானவையில் சேர்க்கப்பட்டது";
    public static final String ANI = "செயலியை தரவிறக்கம் செய்யவும்";
    public static final String DNF = "தகவல் ஏதும் இல்லை";
    public static final String NO_FAV = "விருப்பமானவை ஏதும் இல்லை";
    public static final String SEARCH_NO_JOB = "நீங்கள் தேடிய வேலை வாய்ப்பு தற்போது இல்லை.அனைத்து வேலை வாய்ப்புகளையும் காண (முகப்பு) பகுதியை பயன்படுத்தவும்.";
    public static final String NO_RJOBS = "நீங்கள் பதிவு செய்த தகவல்களுக்கான (சுயவிபரம்) வேலைவாய்ப்பு தற்போது இல்லை.அனைத்து வேலை வாய்ப்புகளையும் காண (அனைத்து வேலைவாய்ப்புகளையும் காண) பகுதியை பயன்படுத்தவும்.";

    public static ProgressDialog mProgress;
    public static Bitmap scaledBitmap;
    public static ProgressDialog mProgress(Context context,String txt,Boolean aBoolean){
        mProgress = new ProgressDialog(context);
        mProgress.setMessage(txt);
        mProgress.setCancelable(aBoolean);
        return mProgress;
    }

    public static Bitmap compressImage(Context context, String imageUri) {

        String filePath = getRealPathFromURI(context,imageUri);
        //Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

        //by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
        //you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        //max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

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
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
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
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return scaledBitmap;
    }

    public static  String getRealPathFromURI(Context context,String contentURI) {
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

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;      }       final float totalPixels = width * height;       final float totalReqPixelsCap = reqWidth * reqHeight * 2;       while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
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
        return pInfo.versionCode;
    }

    public static void toast_center(Context context, String str) {
        Toast toast = Toast.makeText(context, "" + str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static String getAndroidId(Context context) {
        try{
            return Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }catch (Exception e) {
            return "";
        }
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean appInstalledOrNot(String uri,Context context) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
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

    public static String email_get(Context context) {
        SharedPreference sharedPreference = new SharedPreference();
        String email = sharedPreference.getString(context, "email");

        if (email.equals("")) {

            AccountManager am = AccountManager.get(context);
            // Account[] accounts =
            // AccountManager.get(getBaseContext()).getAccounts();
            Account[] accounts = am.getAccountsByType("com.google");

            if (accounts.length > 0) {
                email = accounts[0].name;
            }

            System.out.println("email ==============" + email);
        }
        return email;
    }

    public static void shareText(Context context, String message){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT,
                "நித்ரா வேலைவாய்ப்பு செயலி வழியாக பகிரப்பட்டது. செயலியை தரவிறக்கம் செய்ய: " + U.UTM_SOURCE + "\n\n" +
                        message);
        context.startActivity(Intent.createChooser(intent, "Share"));
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (activity.getCurrentFocus() != null && inputManager != null) {
                inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                inputManager.hideSoftInputFromInputMethod(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public static void hideSoftKeyboard(View view) {
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } else return false;
    }

    public static String s2d(int num) {
        return (num < 10 ? "0" : "") + num;
    }

    public static Date convertStringtoDate(String dateTime, String format){

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

    public static String convertDatetoString(Date dateString){
        String d = "";
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        d =  formatter.format(dateString);
        return d;
    }

    public static String convertDatetoString(Date dateString, String format) {
        String d = "";
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        d = formatter.format(dateString);
        return d;
    }

    public static Calendar convertDateToCalendar(Date date){
        Calendar cal = null;
        try {
            cal = Calendar.getInstance();
            cal.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cal;
    }

    public static String addDaysInCal(int value){
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, value);
        final Date laterDate = c.getTime();
        return dateFormat.format(laterDate);
    }

    public static void P(String s, String s1) {
        System.out.println(s + " --- " + s1);
    }

    public static void PQ(String selectQuery) {
        System.out.println("Current query : " +selectQuery);
    }

    public static String FCAPS(String str) {
        if (str == null) str = "";
        return str.length() !=0 ? str.substring(0, 1).toUpperCase() + str.substring(1) : "";
    }

    public static void date_put(Context context,String str, int val) {
        Calendar calendar = Calendar.getInstance();
        long next_hour = calendar.getTimeInMillis() + val * DateUtils.DAY_IN_MILLIS;

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/M/yyyy");
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
        return list.toArray(new String [list.size()]);
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static Boolean clr_chace(Context context) {
        Boolean aBoolean = false;
        SharedPreference sharedPreference = new SharedPreference();
        Calendar calendar = Calendar.getInstance();
        long next_hour = calendar.getTimeInMillis();

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/M/yyyy");
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
                ;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("clr_chace : error");
        }


        if (sharedPreference.getString(context, "clr_chace").equals("")) {

            aBoolean = true;
            System.out.println("clr_chace : " + aBoolean);
        } else {
            if (date_today.compareTo(date_app_update) >= 0) {
                aBoolean = true;
                System.out.println("clr_chace : " + aBoolean);
            }
        }
        return aBoolean;
    }

    public static void webview(Context context,String str, WebView webView) {
        String bodyFont = "<style> body{font-size:" + context.getResources().getString(R.string.fontsize) + "px; LINE-HEIGHT:" + context.getResources().getString(R.string.lineHeight) + "px;}</style>";

        String exp = "<!DOCTYPE html><head>" + bodyFont + " </head><body> <div align=justify>  <font face=Bamini color=#FFFFFF>" + str + "</font>  </div></body></html>";

        exp = exp.replaceAll("<font face=bamini>", "<font  color=#000000>");

        webView.loadDataWithBaseURL("file:///android_asset/images/", exp, "text/html", "utf-8", null);
    }

    public static String currentDate(){
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat(U.DATE_FORMAT);
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

}

