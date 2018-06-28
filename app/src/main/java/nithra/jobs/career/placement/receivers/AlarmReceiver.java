package nithra.jobs.career.placement.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import nithra.jobs.career.placement.FirebaseService.NotificationHelper;
import nithra.jobs.career.placement.MainActivity;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.activity.JobDetailActivity;
import nithra.jobs.career.placement.engine.LocalDB;
import nithra.jobs.career.placement.networking.MySingleton;
import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

/**
 * Created by arunrk on 31/7/17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    LocalDB localDB;
    Context context;
    String title = "", emp = "", image = "";
    int jobid = 0;
    Bitmap bitmap = null;
    int jobtype = -1;
    String noofvancancy = "", ending = "", datediff = "", post = "";

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
                               // getnerateNotification(1);
                                //Log.e("refresh no", "1");
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
                    params.put("vcode", String.valueOf(U.versioncode_get(context)));
                    return params;
                }
            };

            MySingleton.getInstance(context).addToRequestQue(stringRequest);

            getnerateNotification(0);
            Log.e("refresh no", "0");
        } else {

        }
    }

    private void getnerateNotification(int refresh) {
        Log.e("generate fun", "" + refresh);
        SharedPreference sharedPreference = new SharedPreference();
        NotificationHelper noti = new NotificationHelper(context);
        noti.Notification_Jobs_remainder(jobid,title,emp,image,jobtype,refresh,noofvancancy,post,ending,datediff,
                sharedPreference.getInt(context, U.SH_NOTIFICATION_SOUND));
    }

    /*protected NotificationCompat.Builder buildNotification(int refresh) {

        // Create the style object with BigPictureStyle subclass.
        NotificationCompat.BigPictureStyle notiStyle = new
                NotificationCompat.BigPictureStyle();

        notiStyle.setBigContentTitle(title);
        notiStyle.setSummaryText(emp);

        System.out.println("Image url : " + image);
        try {
            //bitmap = BitmapFactory.decodeStream((InputStream) new URL(image).getContent());
            Glide.with(context)
                    .load(image)
                    .asBitmap()
                    .placeholder(R.drawable.nithra)
                    .error(R.drawable.nithra)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            bitmap = resource;
                        }
                    });
        } catch (Exception e) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.nithra);
        }

        // Add the big picture to the style.
        if (bitmap != null) notiStyle.bigPicture(bitmap);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.setFlags(resultIntent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle bundle = new Bundle();
        bundle.putString("title", "");
        bundle.putString("message", ""+jobid);
        resultIntent.putExtras(bundle);

        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(
                context,
                jobid,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                // Set Icon
                .setSmallIcon(R.drawable.ic_stat_app_notification)
                // Set Ticker Message
                .setTicker(context.getString(R.string.title_all_jobs))
                // Dismiss Notification
                .setAutoCancel(true)
                // Set PendingIntent into Notification
                .setStyle(new NotificationCompat.BigPictureStyle())

                //.setCustomContentView(getComplexImageNotificationView())
                .setContentIntent(pIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // build a complex notification, with buttons and such
            //
            builder = builder.setCustomBigContentView(getComplexImageNotificationView(refresh));
            builder = builder.setCustomContentView(getComplexNotificationView());

        } else {
            // Build a simpler notification, without buttons
            //
            builder = builder.setContentTitle(title)
                    .setContentText(emp)
                    .setSmallIcon(R.drawable.ic_stat_app_notification);
        }
        if (refresh == 1) {
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(alarmSound);
        }
        return builder;
    }

    private RemoteViews getComplexNotificationView() {
        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews notificationView = new RemoteViews(
                context.getPackageName(),
                R.layout.reminder
        );

        // Locate and set the Image into customnotificationtext.xml ImageViews
        //notificationView.setImageViewBitmap(R.id.imgLogo,bitmap);

        // Locate and set the Text into customnotificationtext.xml TextViews
        notificationView.setTextViewText(R.id.txtTitle, title);
        notificationView.setTextViewText(R.id.txtDescription, emp);

        return notificationView;
    }

    private RemoteViews getComplexImageNotificationView(int refresh) {
        final RemoteViews notificationView = new RemoteViews(context.getPackageName(), R.layout.reminder_big);

        // Using RemoteViews to bind custom layouts into Notification

        // Locate and set the Image into customnotificationtext.xml ImageViews
        if (bitmap != null) notificationView.setImageViewBitmap(R.id.imgLogo, bitmap);

        // Locate and set the Text into customnotificationtext.xml TextViews
        notificationView.setTextViewText(R.id.txtTitle, title);
        notificationView.setTextViewText(R.id.txtCompanyName, emp);
        String jtype = "";
        if (jobtype == -1)
            jtype = "";
        else if (jobtype == 0) jtype = "Govt";
        else jtype = "Private";

        if (refresh == 0) {
            notificationView.setViewVisibility(R.id.txtJobType, View.GONE);
        } else {
            notificationView.setTextViewText(R.id.txtJobType, jtype);
            notificationView.setViewVisibility(R.id.txtJobType, View.VISIBLE);
            *//*if (jobtype == 0) notificationView.setInt(R.id.txtJobType, "setBackgroundColor", R.color.colorPrimary);
            if (jobtype == 1) notificationView.setInt(R.id.txtJobType, "setBackgroundColor", R.color.blue);*//*
        }

        notificationView.setTextViewText(R.id.txtNoOfVacancy, noofvancancy);
        notificationView.setTextViewText(R.id.txtnoofvacancy, post);
        notificationView.setTextViewText(R.id.txtDate, ending);
        notificationView.setTextViewText(R.id.txtDateDiff, datediff);

        return notificationView;
    }*/

}
