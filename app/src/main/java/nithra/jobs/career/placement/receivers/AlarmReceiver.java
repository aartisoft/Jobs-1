package nithra.jobs.career.placement.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import nithra.jobs.career.placement.FirebaseService.NotificationHelper;
import nithra.jobs.career.placement.engine.LocalDB;
import nithra.jobs.career.placement.networking.MySingleton;
import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

/**
 * Created by vanmathi on 31/7/17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    LocalDB localDB;
    Context context;
    String title = "", emp = "", image = "";
    int jobid = 0;
    int jobtype = -1;
    String noofvancancy = "", ending = "", datediff = "", post = "";
    SharedPreference pref;

    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context = context;
        Bundle extras = intent.getExtras();
        if (extras != null) {
            jobid = extras.getInt("jobid");
            title = extras.getString("title");
            emp = extras.getString("emp");
            image = extras.getString("image");
        }
        localDB = new LocalDB(context);
        pref = new SharedPreference();
        if (localDB.isReminderExists(jobid)) {
            localDB.deleteJobReminder(jobid);
            System.out.println("Alarm Receiver : " + jobid + " --- " + title);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.SERVER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {
                            System.out.println("showresponse" + response);
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                if (!status.equals("in-active")) {
                                    jsonObject.getString("jobtitle");
                                    jsonObject.getString("employer");
                                    jsonObject.getString("image");
                                    jobtype = jsonObject.getInt("jobtype");
                                    noofvancancy = jsonObject.getString("noofvancancy");
                                    if (noofvancancy.length() != 0) {
                                        post = "பணியிடங்கள்";
                                    } else {
                                        noofvancancy = "";
                                        post = "";
                                    }
                                    ending = jsonObject.getString("ending");
                                    datediff = jsonObject.getString("datediff");
                                    datediff = datediff + " days left";
                                    System.out.println("Notification data : " + noofvancancy);

                                    getnerateNotification();
                                    Log.e("refresh no", "0");
                                }

                            } catch (JSONException e) {
                                jobtype = 0;
                                noofvancancy = "";
                                ending = "";
                                datediff = "";
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    System.out.println("Notification Error : " + volleyError.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("action", SU.GETDATA);
                    params.put("id", jobid + "");
                    System.out.println("Notification job id : " + jobid);
                    params.put("user_type", pref.getString(context, U.SH_USER_TYPE));
                    params.put("employee_id", pref.getString(context, U.SH_EMPLOYEE_ID));
                    params.put("android_id", U.getAndroidId(context));
                    params.put("vcode", String.valueOf(U.versioncode_get(context)));
                    Log.e("paramsresponse", "" + params);
                    return params;
                }
            };
            MySingleton.getInstance(context).addToRequestQue(stringRequest);
        }
    }

    private void getnerateNotification() {
        Log.e("generate fun", "" + 0);
        SharedPreference sharedPreference = new SharedPreference();
        NotificationHelper noti = new NotificationHelper(context);
        noti.Notification_Jobs_remainder(jobid, title, emp, image, jobtype, 0, noofvancancy, post, ending, datediff,
                sharedPreference.getInt(context, U.SH_NOTIFICATION_SOUND));
    }

}
