package nithra.jobs.career.placement.FirebaseService;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import nithra.jobs.career.placement.Config;
import nithra.jobs.career.placement.MainActivity;
import nithra.jobs.career.placement.ST_Activity;
import nithra.jobs.career.placement.ServerUtilities;
import nithra.jobs.career.placement.activity.EmployerHomeActivity;
import nithra.jobs.career.placement.activity.MultipleNotificationActivity;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    SQLiteDatabase myDB;
    SharedPreference sharedPreference;
    int iddd;
    String msgg, titt, bmmm;
    int isclose = 0;
    Cursor c;
    private NotificationHelper noti;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);
        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
        System.out.println("sendRegistrationToServer: " + token);
        SharedPreference sharedPreference = new SharedPreference();
        sharedPreference.putString(this, "token", token);
        ServerUtilities.gcmpost(token, U.getAndroidId(this), U.versionname_get(this), U.versioncode_get(this), this);
        sharedPreference.putInt(this, "fcm_update", U.versioncode_get(this));
    }

    private void storeRegIdInPref(String token) {
        SharedPreference pref = new SharedPreference();
        pref.putString(this, "regId", token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        sharedPreference = new SharedPreference();

        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.e("Noti_from", remoteMessage.getFrom());
        myDB = openOrCreateDatabase("myDB", 0, null);
        noti = new NotificationHelper(this);
        String tablenew = "noti_cal";
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + tablenew + " (id integer NOT NULL PRIMARY KEY AUTOINCREMENT,title VARCHAR,message VARCHAR,date VARCHAR,time VARCHAR,isclose INT(4),isshow INT(4) default 0,type VARCHAR," +
                "bm VARCHAR,ntype VARCHAR,url VARCHAR);");

        if (remoteMessage.getData().size() > 0) {

            try {
                Log.e("Data Payload: ", remoteMessage.getData().toString());
                Map<String, String> params = remoteMessage.getData();
                JSONObject object = new JSONObject(params);
                Log.e("Noti_JSON_OBJECT", object.toString());

                if (sharedPreference.getString(this, U.SH_USER_TYPE).equals(U.EMPLOYEE)) {
                    handleDataMessageForEmployee(object);
                } else if (sharedPreference.getString(this, U.SH_USER_TYPE).equals(U.EMPLOYER)) {
                    handleDataMessageForEmployer(object);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Exception: ", e.getMessage());
            }
        }
    }

    private void handleDataMessageForEmployee(JSONObject data) {
        Log.e("Noti_Employee: ", "" + data);
        String userType;
        try {
            String message = data.getString("message");
            String title = data.getString("title");
            String date = data.getString("date");
            String time = data.getString("time");
            Log.e("Noti_time", time);
            String type = data.getString("type");
            String bm = data.getString("bm");
            String ntype = data.getString("ntype");
            String url = data.getString("url");
            String pac = data.getString("pac");
            int intent_id = (int) System.currentTimeMillis();
            try {
                userType = data.getString("usertype");
            } catch (JSONException e) {
                userType = "catch";
                e.printStackTrace();
            }
            Log.e("Noti_UserType", type);


            if (!sharedPreference.getString(getApplicationContext(), "old_msg").equals(message) || !sharedPreference.getString(getApplicationContext(), "old_tit").equals(title)) {

                sharedPreference.putString(getApplicationContext(), "old_msg", message);
                sharedPreference.putString(getApplicationContext(), "old_tit", title);
                try {
                    if (type.equals("s")) {
                    }
                } catch (Exception e) {
                    type = "no";
                }
                try {
                    if (pac.equals("s")) {

                    }
                } catch (Exception e) {
                    pac = "no";
                }
                try {
                    if (bm.equals("s")) {

                    }
                } catch (Exception e) {
                    bm = "no";
                }
                try {
                    if (ntype.equals("s")) {

                    }
                } catch (Exception e) {
                    ntype = "no";
                }
                try {
                    title = URLDecoder.decode(title, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                switch (type) {
                    case "h":
                        msgg = message;
                        titt = title;
                        try {
                            bm = URLDecoder.decode(bm, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        bmmm = bm;
                        if (ntype.equals("bt")) {
                            noti.Notification_custom(0, type, title, message, url, "bt", bm, sharedPreference
                                    .getInt(this, U.SH_NOTIFICATION_SOUND), MainActivity.class);
                        } else if (ntype.equals("bi")) {
                            noti.Notification_custom(0, type, title, message, url, "bi", bm, sharedPreference
                                    .getInt(this, U.SH_NOTIFICATION_SOUND), MainActivity.class);
                        }
                        break;
                    case "s": {
                        try {
                            bm = URLDecoder.decode(bm, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        myDB.execSQL("INSERT INTO noti_cal(title,message,date,time,isclose,type,bm,ntype,url) values " +
                                "('" + title + "','" + message + "','" + date + "','" + time + "','" + isclose + "','s','" + bm + "','" + ntype + "','" + url + "');");
                        sharedPreference.putInt(this, "typee", 0);
                        c = myDB.rawQuery("select id from noti_cal", null);
                        c.moveToLast();
                        iddd = c.getInt(0);
                        myDB.close();
                        if (sharedPreference.getInt(this, U.SH_MUTE_NOTIFICATION) == 0) {
                            switch (ntype) {
                                case "bt":
                                    noti.Notification_custom(iddd, type, title, message, url, "bt", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), MultipleNotificationActivity.class);
                                    break;
                                case "bi":
                                    noti.Notification_custom(iddd, type, title, message, url, "bi", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), MultipleNotificationActivity.class);
                                    break;
                                default:
                                    noti.Notification_bm(iddd, type, title, message, url, "bt", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), ST_Activity.class);
                                    break;
                            }
                        }
                        break;
                    }
                    case "st": {
                        if (!userType.equals(U.EMPLOYER)) {
                            msgg = message;
                            titt = title;
                            Log.e("Noti_UserType_st", userType);
                            try {
                                bm = URLDecoder.decode(bm, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            bmmm = bm;
                            myDB.execSQL("INSERT INTO noti_cal(title,message,date,time,isclose,type,bm,ntype,url) values " +
                                    "('" + title + "','" + message + "','" + date + "','" + time + "','" + isclose + "','st','" + bm + "','" + ntype + "','" + url + "');");
                            sharedPreference.putInt(this, "typee", 0);
                            c = myDB.rawQuery("select id from noti_cal", null);
                            c.moveToLast();
                            iddd = c.getInt(0);
                            myDB.close();
                            if (sharedPreference.getInt(this, U.SH_MUTE_NOTIFICATION) == 0) {
                                switch (ntype) {
                                    case "bt":
                                        noti.Notification_custom(iddd, type, title, message, url, "bt", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), ST_Activity.class);//changed
                                        break;
                                    case "bi":
                                        noti.Notification_bm(iddd, type, title, message, url, "bi", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), ST_Activity.class);
                                        break;
                                    default:
                                        noti.Notification_bm(iddd, type, title, message, url, "bt", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), ST_Activity.class);
                                        break;
                                }
                            }
                        }
                        break;
                    }
                    case "ins":
                        try {
                            bm = URLDecoder.decode(bm, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        noti.Notification(intent_id, type, title, message, url, "bi", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), ST_Activity.class);
                        break;
                    case "ap":
                        if (!appInstalledOrNot(pac)) {
                            msgg = message;
                            titt = title;
                            switch (ntype) {
                                case "n":
                                    try {
                                        bm = URLDecoder.decode(bm, "UTF-8");
                                    } catch (UnsupportedEncodingException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    noti.Notification(intent_id, type, title, message, url, "bt", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), ST_Activity.class);
                                    break;
                                case "bt":
                                    try {
                                        bm = URLDecoder.decode(bm, "UTF-8");
                                    } catch (UnsupportedEncodingException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    noti.Notification(intent_id, type, title, message, url, "bt", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), ST_Activity.class);
                                    break;
                                case "bi":
                                    try {
                                        bm = URLDecoder.decode(bm, "UTF-8");
                                    } catch (UnsupportedEncodingException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    noti.Notification(intent_id, type, title, message, url, "bi", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), ST_Activity.class);
                                    break;
                                case "w":
                                    try {
                                        bm = URLDecoder.decode(bm, "UTF-8");
                                    } catch (UnsupportedEncodingException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    noti.Notification(intent_id, type, title, message, url, "bi", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), ST_Activity.class);
                                    break;
                            }
                        }
                        break;
                    case "u":
                        sharedPreference.putInt(this, "gcmvcode", Integer.parseInt(message));
                        sharedPreference.putInt(this, "isvupdate", 1);
                        break;
                    case "cj":
                        msgg = message;
                        titt = title;
                        try {
                            bm = URLDecoder.decode(bm, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        bmmm = bm;

                        if (message != null && !message.isEmpty()) {
                            switch (message) {
                                case "homePrivate":
                                case "homePopularEmp":
                                case "homeAlljobs":
                                case "homeConsultancy":
                                case "homeFreshers":
                                case "homeCentral":
                                case "homeState":
                                    if (sharedPreference.getInt(this, U.SH_MUTE_NOTIFICATION) == 0) {
                                        if (ntype.equals("bt")) {
                                            noti.Notification_custom(0, type, title, message, url, "bt", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), MultipleNotificationActivity.class);
                                        } else if (ntype.equals("bi")) {
                                            noti.Notification_custom(0, type, title, message, url, "bi", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), MultipleNotificationActivity.class);
                                        }
                                    }
                                    break;
                                default:
                                    if (sharedPreference.getInt(this, U.SH_MUTE_NOTIFICATION) == 0) {
                                        if (ntype.equals("bt")) {
                                            noti.Notification_custom(0, type, title, message, url, "bt", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), MainActivity.class);
                                        } else if (ntype.equals("bi")) {
                                            noti.Notification_custom(0, type, title, message, url, "bi", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), MainActivity.class);
                                        }
                                    }
                                    break;
                            }
                        }
                        break;
                    case "fj":
                        msgg = message;
                        titt = title;
                        try {
                            bm = URLDecoder.decode(bm, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        bmmm = bm;
                        if (sharedPreference.getInt(this, U.SH_MUTE_NOTIFICATION) == 0) {
                            if (ntype.equals("bt")) {
                                noti.Notification_custom(0, type, title, message, url, "bt", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), MultipleNotificationActivity.class);
                            } else if (ntype.equals("bi")) {
                                noti.Notification_custom(0, type, title, message, url, "bi", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), MultipleNotificationActivity.class);
                            }
                        }
                        break;
                    case "dj": {
                        msgg = message;
                        titt = title;
                        try {
                            bm = URLDecoder.decode(bm, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        bmmm = bm;
                        myDB.execSQL("INSERT INTO noti_cal(title,message,date,time,isclose,type,bm,ntype,url) values " +
                                "('" + title + "','" + message + "','" + date + "','" + time + "','" + isclose + "','dj','" + bm + "','" + ntype + "','" + url + "');");
                        sharedPreference.putInt(this, "typee", 0);
                        c = myDB.rawQuery("select id from noti_cal", null);
                        c.moveToLast();
                        iddd = c.getInt(0);
                        myDB.close();
                        if (sharedPreference.getInt(this, U.SH_MUTE_NOTIFICATION) == 0) {
                            if (ntype.equals("bt")) {
                                noti.Notification_custom(iddd, type, title, message, url, "bt", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), MultipleNotificationActivity.class);
                            } else if (ntype.equals("bi")) {
                                noti.Notification_custom(iddd, type, title, message, url, "bi", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), MultipleNotificationActivity.class);
                            }
                        }
                        break;
                    }
                    case "rao": {
                        msgg = message;
                        titt = title;
                        bmmm = bm;
                        try {
                            bm = URLDecoder.decode(bm, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        if (sharedPreference.getString(this, U.SH_USER_TYPE).equals(U.EMPLOYEE)) {
                            if (sharedPreference.getInt(this, U.SH_AD_PURCHASED) == 0) {
                                if (ntype.equals("bt")) {
                                    noti.Notification_rao(type, 0, title, message, url, "bt", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), MainActivity.class);
                                } else if (ntype.equals("bi")) {
                                    noti.Notification_rao(type, 0, title, message, url, "bi", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), MainActivity.class);
                                }
                            }
                        }
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleDataMessageForEmployer(JSONObject data) {
        Log.e("Noti_Employer: ", "" + data);
        String userType;
        try {
            String message = data.getString("message");
            String title = data.getString("title");
            String date = data.getString("date");
            String time = data.getString("time");
            Log.e("Noti_time", time);
            String type = data.getString("type");
            String bm = data.getString("bm");
            String ntype = data.getString("ntype");
            String url = data.getString("url");
            String pac = data.getString("pac");
            try {
                userType = data.getString("usertype");
            } catch (JSONException e) {
                userType = "catch";
                e.printStackTrace();
            }
            Log.e("Noti_UserType_Employer", userType);
            int intent_id = (int) System.currentTimeMillis();

            if (userType.equals(U.EMPLOYER)) {

                Log.e("Noti_UserType if", userType);

                if (!sharedPreference.getString(getApplicationContext(), "old_msg").equals(message) || !sharedPreference.getString(getApplicationContext(), "old_tit").equals(title)) {

                    sharedPreference.putString(getApplicationContext(), "old_msg", message);
                    sharedPreference.putString(getApplicationContext(), "old_tit", title);
                    try {
                        if (type.equals("s")) {
                        }
                    } catch (Exception e) {
                        type = "no";
                    }
                    try {
                        if (pac.equals("s")) {

                        }
                    } catch (Exception e) {
                        pac = "no";
                    }
                    try {
                        if (bm.equals("s")) {

                        }
                    } catch (Exception e) {
                        bm = "no";
                    }
                    try {
                        if (ntype.equals("s")) {

                        }
                    } catch (Exception e) {
                        ntype = "no";
                    }
                    try {
                        title = URLDecoder.decode(title, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    if (type.equals("st")) {
                        Log.e("Noti_type", type + " -- " + ntype);
                        msgg = message;
                        titt = title;
                        try {
                            bm = URLDecoder.decode(bm, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        bmmm = bm;
                        myDB.execSQL("INSERT INTO noti_cal(title,message,date,time,isclose,type,bm,ntype,url) values " +
                                "('" + title + "','" + message + "','" + date + "','" + time + "','" + isclose + "','st','" + bm + "','" + ntype + "','" + url + "');");
                        sharedPreference.putInt(this, "typee", 0);
                        c = myDB.rawQuery("select id from noti_cal", null);
                        c.moveToLast();
                        iddd = c.getInt(0);
                        myDB.close();
                        switch (ntype) {
                            case "bt":
                                noti.Notification_custom(iddd, type, title, message, url, "bt", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), ST_Activity.class);//changed
                                break;
                            case "bi":
                                noti.Notification_bm(iddd, type, title, message, url, "bi", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), ST_Activity.class);
                                break;
                            default:
                                noti.Notification_bm(iddd, type, title, message, url, "bt", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), ST_Activity.class);
                                break;
                        }
                    }
                }

            } else if (userType.equals("INCOMPLETE_USER")) {
                if (!sharedPreference.getBoolean(MyFirebaseMessagingService.this, U.SH_EMP_SIGN_UP_SUCCESS) &&
                        sharedPreference.getBoolean(MyFirebaseMessagingService.this, U.SH_EMP_OTP_SUCCESS)) {
                    switch (ntype) {
                        case "bt":
                            noti.Notification_custom(iddd, type, title, message, url, "bt", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), EmployerHomeActivity.class);//changed
                            break;
                        case "bi":
                            noti.Notification_bm(iddd, type, title, message, url, "bi", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), EmployerHomeActivity.class);
                            break;
                        default:
                            noti.Notification_bm(iddd, type, title, message, url, "bt", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), EmployerHomeActivity.class);
                            break;
                    }
                }

            } else if (userType.equals("PROMO_CODE_SHARE")) {

                if (sharedPreference.getBoolean(MyFirebaseMessagingService.this, U.SH_EMP_SIGN_UP_SUCCESS) &&
                        sharedPreference.getBoolean(MyFirebaseMessagingService.this, U.SH_EMP_OTP_SUCCESS)) {
                    sharedPreference.putString(MyFirebaseMessagingService.this, U.PROMO_NOTI, "yes");
                    switch (ntype) {
                        case "bt":
                            noti.Notification_custom(iddd, type, title, message, url, "bt", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), EmployerHomeActivity.class);//changed
                            break;
                        case "bi":
                            noti.Notification_bm(iddd, type, title, message, url, "bi", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), EmployerHomeActivity.class);
                            break;
                        default:
                            noti.Notification_bm(iddd, type, title, message, url, "bt", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), EmployerHomeActivity.class);
                            break;
                    }
                }
            }
//INCOMPLETE_USER
//PROMO-CODE_SHARE
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
}