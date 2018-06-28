package nithra.jobs.career.placement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import nithra.jobs.career.placement.networking.MySingleton;
import nithra.jobs.career.placement.utills.L;
import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;


public class Referrer extends BroadcastReceiver {

    SharedPreference pref;
    Context context;
    String source = "";
    String medium = "";
    String comp = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        pref = new SharedPreference();
        this.context = context;
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String referrerString = extras.getString("referrer");
            String[] referrerList = new String[0];
            try {
                referrerList = referrerString.split("&");
            }catch (Exception e) {

            }
            for (int i = 0; i < referrerList.length; i++) {
                if (i == 0) {
                    source = referrerList[i].substring(referrerList[i].indexOf("=") + 1);
                } else if (i == 1) {
                    medium = referrerList[i].substring(referrerList[i].indexOf("=") + 1);
                } else if (i == 2) {
                    comp = referrerList[i].substring(referrerList[i].indexOf("=") + 1);
                }
            }

       if (U.isNetworkAvailable(context)) {
                send(context, source, medium, comp);
            }

        }
    }

    public void send(final Context context, final String utm, final String utm1, final String utm2) {
        try {

            MainActivity homeActivity = new MainActivity();

            String email = "";
            try {
                email = pref.getString(context, U.SH_ANDROID_ID);
            } catch (Exception e) {
                e.printStackTrace();
            }

         //   System.out.println("REFERER : ---- " + SU.APP + " --- " + utm + " --- " + utm1 + " --- " + utm2 + " --- " + email);


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
 }