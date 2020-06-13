/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nithra.jobs.career.placement.FirebaseService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import nithra.jobs.career.placement.MainActivity;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.activity.MultipleNotificationActivity;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

/**
 * Helper class to manage notification channels, and create notifications.
 */
public class NotificationHelper extends ContextWrapper {

    public static final String PRIMARY_CHANNEL = "default";
    NotificationChannel chan1 = null;
    Context context;
    SharedPreference pref;
    private NotificationManager manager;

    public NotificationHelper(Context ctx) {
        super(ctx);
        context = ctx;
        pref = new SharedPreference();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chan1 = new NotificationChannel(PRIMARY_CHANNEL, "Primary Channel", NotificationManager.IMPORTANCE_DEFAULT);
            chan1.setLightColor(Color.GREEN);
            chan1.setShowBadge(true);
            chan1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getManager().createNotificationChannel(chan1);
        }

    }

    //Intent to only url
    public void Notification(int id, String type, String title, String body1, String imgg, String style, String bm, int sund_chk1, Class activity) {
        try {
            Uri mUri;
            if (sund_chk1 == 0) {
                mUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            } else {
                mUri = null;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder nb;
                nb = new Notification.Builder(context, PRIMARY_CHANNEL)
                        .setContentTitle(title)
                        .setSound(mUri)
                        .setContentText("")
                        .setContentIntent(resultPendingIntent1(bm))
                        .setSmallIcon(getSmallIcon())
                        .setColor(Color.parseColor("#6460AA"))
                        .setLargeIcon(LargeIcon(imgg))
                        .setGroup("" + title)
                        .setStyle(bigimg1(title, "Nithra jobs", imgg))
                        .setAutoCancel(true);
                notify(id, nb);
            } else {
                Notification myNotification;
                myNotification = new NotificationCompat.Builder(context)
                        .setSound(mUri)
                        .setSmallIcon(getSmallIcon())
                        .setColor(Color.parseColor("#6460AA"))
                        .setLargeIcon(getlogo1())
                        .setAutoCancel(true)
                        .setPriority(2)
                        .setContentIntent(resultPendingIntent1(bm))
                        .setContentTitle(title)
                        .setGroup("" + title)
                        .setContentText("")
                        .setStyle(bigimg(title, "Nithra jobs", imgg))
                        .build();
                notify(id, myNotification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Intent to only Activity
    public void Notification_bm(int id, String type, String title, String body, String imgg, String style, String bm, int sund_chk1, Class activity) {
        try {
            Uri mUri;
            if (sund_chk1 == 0) {
                mUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            } else {
                mUri = null;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder nb;
                if (style.equals("bt")) {

                    nb = new Notification.Builder(context, PRIMARY_CHANNEL)
                            .setContentTitle("Nithra jobs")
                            .setSound(mUri)
                            .setContentText("")
                            .setContentIntent(resultPendingIntent(type, bm, body, id, activity))
                            .setSmallIcon(getSmallIcon())
                            .setColor(Color.parseColor("#6460AA"))
                            .setLargeIcon(LargeIcon(imgg))
                            .setGroup("" + title)
                            .setStyle(bigtext1("Nithra jobs", "Nithra jobs", bm))
                            .setAutoCancel(true);
                } else {
                    nb = new Notification.Builder(context, PRIMARY_CHANNEL)
                            .setContentTitle(bm)
                            .setSound(mUri)
                            .setContentText("")
                            .setContentIntent(resultPendingIntent(type, bm, body, id, activity))
                            .setSmallIcon(getSmallIcon())
                            .setColor(Color.parseColor("#6460AA"))
                            .setLargeIcon(LargeIcon(imgg))
                            .setGroup("" + title)
                            .setStyle(bigimg1("Nithra jobs", bm, imgg))
                            .setAutoCancel(true);
                }

                notify(id, nb);
            } else {
                Notification myNotification;
                if (style.equals("bt")) {
                    myNotification = new NotificationCompat.Builder(context)
                            .setSound(mUri)
                            .setSmallIcon(getSmallIcon())
                            .setColor(Color.parseColor("#6460AA"))
                            .setLargeIcon(getlogo1())
                            .setAutoCancel(true)
                            .setPriority(2)
                            .setContentIntent(resultPendingIntent(type, bm, body, id, activity))
                            .setContentTitle("Nithra jobs")
                            .setContentText("")
                            .setGroup("" + title)
                            .setStyle(bigtext("Nithra jobs", "Nithra jobs", bm))
                            .build();
                    notify(id, myNotification);
                } else {
                    myNotification = new NotificationCompat.Builder(context)
                            .setSound(mUri)
                            .setSmallIcon(getSmallIcon())
                            .setColor(Color.parseColor("#6460AA"))
                            .setLargeIcon(getlogo1())
                            .setAutoCancel(true)
                            .setPriority(2)
                            .setContentIntent(resultPendingIntent(type, bm, body, id, activity))
                            .setContentTitle(bm)
                            .setContentText("")
                            .setGroup("" + title)
                            .setStyle(bigimg("Nithra jobs", bm, imgg))
                            .build();
                    notify(id, myNotification);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Notification_custom(int id, String type, String titlee, String body, String imgg, String style, String bm, int sund_chk1, Class activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_shown_st);
                contentView.setImageViewResource(R.id.image, getlogo());
                contentView.setTextViewText(R.id.title, bm);
                Notification.Builder mBuilder = null;
                if (style.equals("bt")) {
                    mBuilder = new Notification.Builder(context, PRIMARY_CHANNEL)
                            .setSmallIcon(getSmallIcon())
                            .setColor(Color.parseColor("#6460AA"))
                            .setGroup("" + titlee)
                            .setCustomContentView(contentView);
                } else {
                    RemoteViews expandView = new RemoteViews(getPackageName(), R.layout.notification_shown_bi);
                    expandView.setImageViewResource(R.id.image, getlogo());
                    expandView.setTextViewText(R.id.title, bm);
                    expandView.setImageViewBitmap(R.id.imgg, LargeIcon(imgg));
                    mBuilder = new Notification.Builder(context, PRIMARY_CHANNEL)
                            .setSmallIcon(getSmallIcon())
                            .setGroup("" + titlee)
                            .setColor(Color.parseColor("#6460AA"))
                            .setCustomContentView(contentView)
                            .setCustomBigContentView(expandView);
                }
                Notification notification = mBuilder.build();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    notification.priority |= Notification.PRIORITY_MAX;
                }
                if (sund_chk1 == 0) {
                    notification.defaults |= Notification.DEFAULT_SOUND;
                } else {
                    // notification.sound = mUri;
                }

                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notification.flags |= Notification.FLAG_SHOW_LIGHTS;
                notification.contentIntent = resultPendingIntent(type, bm, body, id, activity);
                getManager().notify(id, notification);
            } else {
                RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_shown_st);
                contentView.setImageViewResource(R.id.image, getlogo());
                contentView.setTextViewText(R.id.title, bm);
                NotificationCompat.Builder mBuilder = null;
                if (style.equals("bt")) {
                    mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(getSmallIcon())
                            .setColor(Color.parseColor("#6460AA"))
                            .setGroup("" + titlee)
                            .setContent(contentView);
                } else {
                    RemoteViews expandView = new RemoteViews(getPackageName(), R.layout.notification_shown_bi);
                    expandView.setImageViewResource(R.id.image, getlogo());
                    expandView.setTextViewText(R.id.title, bm);
                    expandView.setImageViewBitmap(R.id.imgg, LargeIcon(imgg));
                    mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(getSmallIcon())
                            .setColor(Color.parseColor("#6460AA"))
                            .setGroup("" + titlee)
                            .setContent(contentView)
                            .setCustomBigContentView(expandView);
                }

                Notification notification = mBuilder.build();
                if (sund_chk1 == 0) {
                    notification.defaults |= Notification.DEFAULT_SOUND;
                } else {
                    // notification.sound = mUri;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    notification.priority |= Notification.PRIORITY_MAX;
                }
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notification.flags |= Notification.FLAG_SHOW_LIGHTS;
                notification.contentIntent = resultPendingIntent(type, bm, body, id, activity);
                getManager().notify(id, notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Notification_rao(String type, int id, String title, String body, String imgg, String style, String bm, int sund_chk1, Class activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_shown_st);
                contentView.setImageViewResource(R.id.image, getlogo());
                contentView.setTextViewText(R.id.title, bm);
                Notification.Builder mBuilder = null;
                if (style.equals("bt")) {
                    mBuilder = new Notification.Builder(context, PRIMARY_CHANNEL)
                            .setSmallIcon(getSmallIcon())
                            .setColor(Color.parseColor("#6460AA"))
                            .setGroup("" + title)
                            .setCustomContentView(contentView);
                } else {
                    RemoteViews expandView = new RemoteViews(getPackageName(), R.layout.notification_shown_bi);
                    expandView.setImageViewResource(R.id.image, getlogo());
                    expandView.setTextViewText(R.id.title, bm);
                    expandView.setImageViewBitmap(R.id.imgg, LargeIcon(imgg));
                    mBuilder = new Notification.Builder(context, PRIMARY_CHANNEL)
                            .setSmallIcon(getSmallIcon())
                            .setGroup("" + title)
                            .setColor(Color.parseColor("#6460AA"))
                            .setCustomContentView(contentView)
                            .setCustomBigContentView(expandView);
                }
                Notification notification = mBuilder.build();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    notification.priority |= Notification.PRIORITY_MAX;
                }
                if (sund_chk1 == 0) {
                    notification.defaults |= Notification.DEFAULT_SOUND;
                } else {
                    // notification.sound = mUri;
                }
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notification.flags |= Notification.FLAG_SHOW_LIGHTS;
                notification.contentIntent = resultPendingIntent(type, bm, body, id, activity);
                getManager().notify(id, notification);
            } else {
                RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_shown_st);
                contentView.setImageViewResource(R.id.image, getlogo());
                contentView.setTextViewText(R.id.title, bm);
                NotificationCompat.Builder mBuilder = null;
                if (style.equals("bt")) {
                    mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(getSmallIcon())
                            .setColor(Color.parseColor("#6460AA"))
                            .setGroup("" + title)
                            .setContent(contentView);
                } else {
                    RemoteViews expandView = new RemoteViews(getPackageName(), R.layout.notification_shown_bi);
                    expandView.setImageViewResource(R.id.image, getlogo());
                    expandView.setTextViewText(R.id.title, bm);
                    expandView.setImageViewBitmap(R.id.imgg, LargeIcon(imgg));
                    mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(getSmallIcon())
                            .setColor(Color.parseColor("#6460AA"))
                            .setGroup("" + title)
                            .setContent(contentView)
                            .setCustomBigContentView(expandView);
                }

                Notification notification = mBuilder.build();
                if (sund_chk1 == 0) {
                    notification.defaults |= Notification.DEFAULT_SOUND;
                } else {
                    // notification.sound = mUri;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    notification.priority |= Notification.PRIORITY_MAX;
                }
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notification.flags |= Notification.FLAG_SHOW_LIGHTS;
                notification.contentIntent = resultPendingIntent(type, bm, body, id, activity);
                getManager().notify(id, notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void notify(int id, Notification.Builder notification) {
        getManager().notify(id, notification.build());
    }

    public void notify(int id, Notification myNotification) {
        getManager().notify(id, myNotification);
    }

    private int getSmallIcon() {
        return R.drawable.ic_stat_app_notification;
    }

    private int getlogo() {
        return R.mipmap.ic_launcher;
    }

    private Bitmap getlogo1() {
        return BitmapFactory.decodeResource(getResources(), getlogo());
    }

    private Bitmap LargeIcon(String url) {
        Bitmap remote_picture = BitmapFactory.decodeResource(getResources(), getlogo());

        if (url.equals("personalNotification")) {
            remote_picture = null;
        } else {
            if (url.length() > 5) {
                try {
                    remote_picture = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
                } catch (IOException e) {
                    e.printStackTrace();
                    remote_picture = BitmapFactory.decodeResource(getResources(), getlogo());
                }
            } else {
                remote_picture = BitmapFactory.decodeResource(getResources(), getlogo());
            }
        }
        return remote_picture;
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public NotificationCompat.BigTextStyle bigtext(String Title, String Summary, String bigText) {
        return new NotificationCompat.BigTextStyle()
                .setBigContentTitle(Title)
                .setSummaryText(Summary)
                .bigText(bigText);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public Notification.BigTextStyle bigtext1(String Title, String Summary, String bigText) {
        return new Notification.BigTextStyle()
                .setBigContentTitle(Title)
                .setSummaryText(Summary)
                .bigText(bigText);
    }

    public NotificationCompat.BigPictureStyle bigimg(String Title, String Summary, String imgg) {
        return new NotificationCompat.BigPictureStyle()
                .setBigContentTitle(Title)
                .setSummaryText(Summary)
                .bigPicture(LargeIcon(imgg));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public Notification.BigPictureStyle bigimg1(String Title, String Summary, String imgg) {
        return new Notification.BigPictureStyle()
                .setBigContentTitle(Title)
                .setSummaryText(Summary)
                .bigPicture(LargeIcon(imgg));
    }

    public PendingIntent resultPendingIntent(String type, String titt, String msgg, int idd, Class activity) {
        Intent intent = set_intent(context, idd, type, titt, msgg, activity);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        stackBuilder.addParentStack(activity);

        stackBuilder.addNextIntent(intent);
        return stackBuilder.getPendingIntent((int) System.currentTimeMillis(), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public PendingIntent resultPendingIntent1(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        stackBuilder.addNextIntent(intent);
        return stackBuilder.getPendingIntent((int) System.currentTimeMillis(), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public Intent set_intent(Context context, int iddd, String type, String titt, String msgg, Class activity) {
        Intent intent;
        intent = new Intent(context, activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("type", type);
        intent.putExtra("message", msgg);
        intent.putExtra("mode", "gcm");
        intent.putExtra("title", titt);
        intent.putExtra("idd", iddd);
        intent.putExtra("Noti_add", 1);
        return intent;
    }

    public void Notification_Jobs_remainder(int id, String title, String emp, String imgg, int jobtype, int refresh,
                                            String noofvancancy, String post, String ending, String datediff,
                                            int sund_chk1) {

        Intent notiIntent = new Intent(context, MultipleNotificationActivity.class);
        notiIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notiIntent.putExtra("type", "rj");
        notiIntent.putExtra("message", "" + id);
        notiIntent.putExtra("title", "Reminder Alert !!");
        notiIntent.putExtra("idd", "");
        notiIntent.putExtra("Noti_add", 1);
        if (pref.getString(context, U.SH_USER_TYPE).equals(U.EMPLOYEE)) {
            notiIntent.putExtra("mode", "gcm");
        } else if (pref.getString(context, U.SH_USER_TYPE).equals(U.EMPLOYER)) {
            notiIntent.putExtra("mode", "reminderNoti");
        } else {
            notiIntent.putExtra("mode", "initial");
        }

        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(
                context,
                id,
                notiIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.reminder);
                contentView.setTextViewText(R.id.txtTitle, title);
                contentView.setTextViewText(R.id.txtDescription, emp);
                Notification.Builder mBuilder = null;

                RemoteViews expandView = new RemoteViews(getPackageName(), R.layout.reminder_big);
                expandView.setTextViewText(R.id.txtTitle, title);
                expandView.setTextViewText(R.id.txtCompanyName, emp);

                String jtype = "";
                switch (jobtype) {
                    case 1:
                        jtype = context.getResources().getString(R.string.privatejob);
                        break;
                    case 2:
                        jtype = context.getResources().getString(R.string.state_govt);
                        break;
                    case 4:
                        jtype = context.getResources().getString(R.string.consultancy);
                        break;
                    default:
                        jtype = context.getResources().getString(R.string.central_govt);
                        break;
                }

                if (refresh == 0) {
                    expandView.setViewVisibility(R.id.txtJobType, View.GONE);
                } else {
                    expandView.setTextViewText(R.id.txtJobType, jtype);
                    expandView.setViewVisibility(R.id.txtJobType, View.VISIBLE);
                }

                expandView.setImageViewBitmap(R.id.imgLogo, LargeIcon(imgg));

                if (noofvancancy.equals("") || noofvancancy.equals("0")) {
                    expandView.setTextViewText(R.id.txtNoOfVacancy, "");
                    expandView.setTextViewText(R.id.txtnoofvacancy, "");
                } else {
                    expandView.setTextViewText(R.id.txtNoOfVacancy, noofvancancy);
                    expandView.setTextViewText(R.id.txtnoofvacancy, post);
                }
                expandView.setTextViewText(R.id.txtDate, ending);
                expandView.setTextViewText(R.id.txtDateDiff, datediff);

                mBuilder = new Notification.Builder(context, PRIMARY_CHANNEL)
                        .setSmallIcon(getSmallIcon())
                        .setGroup("" + title)
                        .setColor(Color.parseColor("#6460AA"))
                        .setCustomContentView(contentView)
                        .setCustomBigContentView(expandView);

                Notification notification = mBuilder.build();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    notification.priority |= Notification.PRIORITY_MAX;
                }
                if (sund_chk1 == 0) {
                    notification.defaults |= Notification.DEFAULT_SOUND;
                } else {
                    // notification.sound = mUri;
                }

                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notification.flags |= Notification.FLAG_SHOW_LIGHTS;
                notification.contentIntent = pIntent;
                getManager().notify(id, notification);
            } else {
                RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.reminder);
                contentView.setTextViewText(R.id.txtTitle, title);
                contentView.setTextViewText(R.id.txtDescription, emp);
                NotificationCompat.Builder mBuilder = null;

                RemoteViews expandView = new RemoteViews(getPackageName(), R.layout.reminder_big);
                expandView.setTextViewText(R.id.txtTitle, title);
                expandView.setTextViewText(R.id.txtCompanyName, emp);

                String jtype = "";
                switch (jobtype) {
                    case 1:
                        jtype = context.getResources().getString(R.string.privatejob);
                        break;
                    case 2:
                        jtype = context.getResources().getString(R.string.state_govt);
                        break;
                    case 4:
                        jtype = context.getResources().getString(R.string.consultancy);
                        break;
                    default:
                        jtype = context.getResources().getString(R.string.central_govt);
                        break;
                }

                if (refresh == 0) {
                    expandView.setViewVisibility(R.id.txtJobType, View.GONE);
                } else {
                    expandView.setTextViewText(R.id.txtJobType, jtype);
                    expandView.setViewVisibility(R.id.txtJobType, View.VISIBLE);
                }

                expandView.setImageViewBitmap(R.id.imgLogo, LargeIcon(imgg));

                if (noofvancancy.equals("") || noofvancancy.equals("0")) {
                    expandView.setTextViewText(R.id.txtNoOfVacancy, "");
                    expandView.setTextViewText(R.id.txtnoofvacancy, "");
                } else {
                    expandView.setTextViewText(R.id.txtNoOfVacancy, noofvancancy);
                    expandView.setTextViewText(R.id.txtnoofvacancy, post);
                }
                expandView.setTextViewText(R.id.txtDate, ending);
                expandView.setTextViewText(R.id.txtDateDiff, datediff);

                mBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(getSmallIcon())
                        .setColor(Color.parseColor("#6460AA"))
                        .setGroup("" + title)
                        .setContent(contentView)
                        .setCustomBigContentView(expandView);


                Notification notification = mBuilder.build();
                if (sund_chk1 == 0) {
                    notification.defaults |= Notification.DEFAULT_SOUND;
                } else {
                    // notification.sound = mUri;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    notification.priority |= Notification.PRIORITY_MAX;
                }
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notification.flags |= Notification.FLAG_SHOW_LIGHTS;
                notification.contentIntent = pIntent;
                getManager().notify(id, notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void personalNotification_with_Internet(int id, String bm, String title, String emp, String imgg, int jobtype, int refresh,
                                                   String noofvancancy, String post, String ending, String datediff,
                                                   int sund_chk1, String category, String notiType) {

        Intent resultIntent;
        if (bm.equals(U.catArray[1]) || bm.equals(U.catArray[3])) {
            if (pref.getString(context, U.SH_SKILLS).equals("0") || pref.getString(context, U.SH_JOBLOCATION).isEmpty()) {
                resultIntent = new Intent(context, MainActivity.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Bundle bundle = new Bundle();
                bundle.putString("type", notiType);
                bundle.putString("title", bm);
                bundle.putString("idd", "");
                bundle.putString("mode", "gcm");
                bundle.putString("message", category);
                resultIntent.putExtras(bundle);
            } else {
                resultIntent = new Intent(context, MultipleNotificationActivity.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                resultIntent.putExtra("type", notiType);
                resultIntent.putExtra("message", category);
                resultIntent.putExtra("title", bm);
                resultIntent.putExtra("Notifyid", "" + id);
                resultIntent.putExtra("mode", "gcm");
            }
        } else {
            resultIntent = new Intent(context, MultipleNotificationActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            resultIntent.putExtra("type", notiType);
            resultIntent.putExtra("message", category);
            resultIntent.putExtra("title", bm);
            resultIntent.putExtra("Notifyid", "" + id);
            resultIntent.putExtra("mode", "gcm");
        }

        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(
                context,
                id,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.personal_noti);
                contentView.setTextViewText(R.id.txtTitle, title);
                contentView.setTextViewText(R.id.txtDescription, emp);
                Notification.Builder mBuilder = null;

                RemoteViews expandView = new RemoteViews(getPackageName(), R.layout.personal_noti_big);
                expandView.setTextViewText(R.id.txtTitle, title);
                expandView.setTextViewText(R.id.txtCompanyName, emp);

                String jtype = "";
                switch (jobtype) {
                    case 1:
                        jtype = context.getResources().getString(R.string.privatejob);
                        expandView.setInt(R.id.txtJobType, "setBackgroundColor", context.getResources().getColor(R.color.blue));
                        break;
                    case 2:
                        jtype = context.getResources().getString(R.string.state_govt);
                        expandView.setInt(R.id.txtJobType, "setBackgroundColor", context.getResources().getColor(R.color.thick_green));
                        break;
                    case 4:
                        jtype = context.getResources().getString(R.string.consultancy);
                        expandView.setInt(R.id.txtJobType, "setBackgroundColor", context.getResources().getColor(R.color.toast));
                        break;
                    default:
                        jtype = context.getResources().getString(R.string.central_govt);
                        expandView.setInt(R.id.txtJobType, "setBackgroundColor", context.getResources().getColor(R.color.browish_red));
                        break;
                }

//                if (refresh == 0) {
                expandView.setTextViewText(R.id.txtJobType, jtype);
                expandView.setViewVisibility(R.id.txtJobType, View.VISIBLE);
//                } else {
//                    expandView.setTextViewText(R.id.txtJobType, jtype);
//                    expandView.setViewVisibility(R.id.txtJobType, View.VISIBLE);
//                }

                expandView.setImageViewBitmap(R.id.imgLogo, LargeIcon(imgg));

                if (noofvancancy.equals("") || noofvancancy.equals("0")) {
                    expandView.setTextViewText(R.id.txtNoOfVacancy, "");
                    expandView.setTextViewText(R.id.txtnoofvacancy, "");
                } else {
                    expandView.setTextViewText(R.id.txtNoOfVacancy, noofvancancy);
                    expandView.setTextViewText(R.id.txtnoofvacancy, post);
                }

                expandView.setTextViewText(R.id.txtDate, ending);
                expandView.setTextViewText(R.id.txtDateDiff, datediff);

                mBuilder = new Notification.Builder(context, PRIMARY_CHANNEL)
                        .setSmallIcon(getSmallIcon())
                        .setGroup("" + title)
                        .setColor(Color.parseColor("#6460AA"))
                        .setCustomContentView(contentView)
                        .setCustomBigContentView(expandView);

                Notification notification = mBuilder.build();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    notification.priority |= Notification.PRIORITY_MAX;
                }
                if (sund_chk1 == 0) {
                    notification.defaults |= Notification.DEFAULT_SOUND;
                } else {
                    // notification.sound = mUri;
                }

                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notification.flags |= Notification.FLAG_SHOW_LIGHTS;
                notification.contentIntent = pIntent;
                getManager().notify(id, notification);
            } else {
                RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.reminder);
                contentView.setTextViewText(R.id.txtTitle, title);
                contentView.setTextViewText(R.id.txtDescription, emp);
                NotificationCompat.Builder mBuilder = null;

                RemoteViews expandView = new RemoteViews(getPackageName(), R.layout.reminder_big);
                expandView.setTextViewText(R.id.txtTitle, title);
                expandView.setTextViewText(R.id.txtCompanyName, emp);

                String jtype = "";
                switch (jobtype) {
                    case 1:
                        jtype = context.getResources().getString(R.string.privatejob);
                        expandView.setInt(R.id.txtJobType, "setBackgroundColor", context.getResources().getColor(R.color.blue));
                        break;
                    case 2:
                        jtype = context.getResources().getString(R.string.state_govt);
                        expandView.setInt(R.id.txtJobType, "setBackgroundColor", context.getResources().getColor(R.color.thick_green));
                        break;
                    case 4:
                        jtype = context.getResources().getString(R.string.consultancy);
                        expandView.setInt(R.id.txtJobType, "setBackgroundColor", context.getResources().getColor(R.color.toast));
                        break;
                    default:
                        jtype = context.getResources().getString(R.string.central_govt);
                        expandView.setInt(R.id.txtJobType, "setBackgroundColor", context.getResources().getColor(R.color.browish_red));
                        break;
                }

//                if (refresh == 0) {
                expandView.setViewVisibility(R.id.txtJobType, View.VISIBLE);
                expandView.setTextViewText(R.id.txtJobType, jtype);
//                } else {
//                    expandView.setTextViewText(R.id.txtJobType, jtype);
//                    expandView.setViewVisibility(R.id.txtJobType, View.VISIBLE);
//                }

                expandView.setImageViewBitmap(R.id.imgLogo, LargeIcon(imgg));

                if (noofvancancy.equals("") || noofvancancy.equals("0")) {
                    expandView.setTextViewText(R.id.txtNoOfVacancy, "");
                    expandView.setTextViewText(R.id.txtnoofvacancy, "");
                } else {
                    expandView.setTextViewText(R.id.txtNoOfVacancy, noofvancancy);
                    expandView.setTextViewText(R.id.txtnoofvacancy, post);
                }

                expandView.setTextViewText(R.id.txtDate, ending);
                expandView.setTextViewText(R.id.txtDateDiff, datediff);

                mBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(getSmallIcon())
                        .setColor(Color.parseColor("#6460AA"))
                        .setGroup("" + title)
                        .setContent(contentView)
                        .setCustomBigContentView(expandView);


                Notification notification = mBuilder.build();
                if (sund_chk1 == 0) {
                    notification.defaults |= Notification.DEFAULT_SOUND;
                } else {
                    // notification.sound = mUri;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    notification.priority |= Notification.PRIORITY_MAX;
                }
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notification.flags |= Notification.FLAG_SHOW_LIGHTS;
                notification.contentIntent = pIntent;
                getManager().notify(id, notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void personalNotification_without_Internet(int id, String type, String titlee, String body, String imgg, String style, String bm, int sund_chk1, Class activity, String category) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_shown_st);
                contentView.setImageViewResource(R.id.image, getlogo());
                contentView.setTextViewText(R.id.title, bm);
                Notification.Builder mBuilder = null;
                if (style.equals("bt")) {
                    mBuilder = new Notification.Builder(context, PRIMARY_CHANNEL)
                            .setSmallIcon(getSmallIcon())
                            .setColor(Color.parseColor("#6460AA"))
                            .setGroup("" + titlee)
                            .setCustomContentView(contentView);
                } else {
                    RemoteViews expandView = new RemoteViews(getPackageName(), R.layout.notification_shown_bi);
                    expandView.setImageViewResource(R.id.image, getlogo());
                    expandView.setTextViewText(R.id.title, bm);
                    expandView.setImageViewBitmap(R.id.imgg, LargeIcon(imgg));
                    mBuilder = new Notification.Builder(context, PRIMARY_CHANNEL)
                            .setSmallIcon(getSmallIcon())
                            .setGroup("" + titlee)
                            .setColor(Color.parseColor("#6460AA"))
                            .setCustomContentView(contentView)
                            .setCustomBigContentView(expandView);
                }
                Notification notification = mBuilder.build();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    notification.priority |= Notification.PRIORITY_MAX;
                }
                if (sund_chk1 == 0) {
                    notification.defaults |= Notification.DEFAULT_SOUND;
                } else {
                    // notification.sound = mUri;
                }

                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notification.flags |= Notification.FLAG_SHOW_LIGHTS;
                notification.contentIntent = resultPendingIntent(type, bm, category, id, activity);
                getManager().notify(id, notification);

            } else {
                RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_shown_st);
                contentView.setImageViewResource(R.id.image, getlogo());
                contentView.setTextViewText(R.id.title, bm);
                NotificationCompat.Builder mBuilder = null;
                if (style.equals("bt")) {
                    mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(getSmallIcon())
                            .setColor(Color.parseColor("#6460AA"))
                            .setGroup("" + titlee)
                            .setContent(contentView);
                } else {
                    RemoteViews expandView = new RemoteViews(getPackageName(), R.layout.notification_shown_bi);
                    expandView.setImageViewResource(R.id.image, getlogo());
                    expandView.setTextViewText(R.id.title, bm);
                    expandView.setImageViewBitmap(R.id.imgg, LargeIcon(imgg));
                    mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(getSmallIcon())
                            .setColor(Color.parseColor("#6460AA"))
                            .setGroup("" + titlee)
                            .setContent(contentView)
                            .setCustomBigContentView(expandView);
                }

                Notification notification = mBuilder.build();
                if (sund_chk1 == 0) {
                    notification.defaults |= Notification.DEFAULT_SOUND;
                } else {
                    // notification.sound = mUri;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    notification.priority |= Notification.PRIORITY_MAX;
                }
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notification.flags |= Notification.FLAG_SHOW_LIGHTS;
                notification.contentIntent = resultPendingIntent(type, bm, category, id, activity);
                getManager().notify(id, notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
