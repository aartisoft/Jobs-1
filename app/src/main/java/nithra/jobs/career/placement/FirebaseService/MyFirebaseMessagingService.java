package nithra.jobs.career.placement.FirebaseService;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import nithra.jobs.career.placement.MainActivity;
import nithra.jobs.career.placement.ST_Activity;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    SQLiteDatabase myDB;
    SharedPreference sharedPreference;
    int iddd;
    String msgg, titt, bmmm;
    private NotificationHelper noti;
    int isclose = 0;
    Cursor c,c1;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        myDB = openOrCreateDatabase("myDB", 0, null);
        noti = new NotificationHelper(this);
        String tablenew = "noti_cal";
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + tablenew + " (id integer NOT NULL PRIMARY KEY AUTOINCREMENT,title VARCHAR,message VARCHAR,date VARCHAR,time VARCHAR,isclose INT(4),isshow INT(4) default 0,type VARCHAR," +
                "bm VARCHAR,ntype VARCHAR,url VARCHAR);");
        sharedPreference = new SharedPreference();
        if (remoteMessage.getData().size() > 0) {

            try {
                Log.e("Data Payload: ", remoteMessage.getData().toString());
                Map<String, String> params = remoteMessage.getData();
                JSONObject object = new JSONObject(params);

                Log.e("JSON_OBJECT", object.toString());
                //JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(object);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Exception: ", e.getMessage());
            }
        }
    }

    private void handleDataMessage(JSONObject data) {
        /*Log.e(TAG, "push json: " + json.toString());*/
        /*JSONObject data = null;*/
        /*data = json.getJSONObject("data");*/
        try {
            String message = data.getString("message");
            String title = data.getString("title");
            String date = data.getString("date");
            String time = data.getString("time");
            String type = data.getString("type");
            String bm = data.getString("bm");
            String ntype = data.getString("ntype");
            String url = data.getString("url");
            String pac = data.getString("pac");
            int intent_id = (int) System.currentTimeMillis();

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
                                    noti.Notification_custom(iddd, type, title, message, url, "bt", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), MainActivity.class);
                                    break;
                                case "bi":
                                    noti.Notification_custom(iddd, type, title, message, url, "bi", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), MainActivity.class);
                                    break;
                                default:
                                    noti.Notification_bm(iddd, type, title, message, url, "bt", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), ST_Activity.class);
                                    break;
                            }
                        }
                        break;
                    }
                    case "st": {
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
                        break;
                    }
                    case "ins":
                        try {
                            bm = URLDecoder.decode(bm, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        if (sharedPreference.getInt(this, U.SH_MUTE_NOTIFICATION) == 0) {
                            noti.Notification(intent_id, type, title, message, url, "bi", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), ST_Activity.class);
                        }
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
                        if (sharedPreference.getInt(this, U.SH_MUTE_NOTIFICATION) == 0) {
                            if (ntype.equals("bt")) {
                                noti.Notification_custom(0, type, title, message, url, "bt", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), MainActivity.class);
                            } else if (ntype.equals("bi")) {
                                noti.Notification_custom(0, type, title, message, url, "bi", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), MainActivity.class);
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
                                noti.Notification_custom(0, type, title, message, url, "bt", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), MainActivity.class);
                            } else if (ntype.equals("bi")) {
                                noti.Notification_custom(0, type, title, message, url, "bi", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), MainActivity.class);
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
                                noti.Notification_custom(iddd, type, title, message, url, "bt", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), MainActivity.class);
                            } else if (ntype.equals("bi")) {
                                noti.Notification_custom(iddd, type, title, message, url, "bi", bm, sharedPreference.getInt(this, U.SH_NOTIFICATION_SOUND), MainActivity.class);
                            }
                        }
                        break;
                    }
                }
            }
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