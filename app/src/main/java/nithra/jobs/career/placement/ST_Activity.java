package nithra.jobs.career.placement;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.zxing.common.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import nithra.jobs.career.placement.adapters.MyAdapter;
import nithra.jobs.career.placement.utills.CodetoTamilUtil;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

import static android.text.TextUtils.isEmpty;


public class ST_Activity extends AppCompatActivity {

    SharedPreference sharedPreference;
    String str_title = "";
    WebView content_view;
    ImageView share;
    String ismark = "";
    SQLiteDatabase myDB;
    String tablenew = "noti_cal";
    String title, message, msgType, date, time;
    int isvalided = 0;
    Bitmap bitmap;
    String str_msg;
    InterstitialAd interstitialAd_noti;
    LinearLayout addview;
    AppCompatImageView btn_close;
    int show_id, show_ads;
    NestedScrollView scrool;
    int nactivity = 0;
    java.util.List<ResolveInfo> listApp;
    PackageManager pManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.st_lay);
        sharedPreference = new SharedPreference();
        myDB = openOrCreateDatabase("myDB", 0, null);
        pManager = getPackageManager();
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + tablenew
                + " (id integer NOT NULL PRIMARY KEY AUTOINCREMENT,title VARCHAR,message VARCHAR,date VARCHAR,time VARCHAR,isclose INT(4),isshow INT(4) default 0,type VARCHAR," +
                "bm VARCHAR,ntype VARCHAR,url VARCHAR);");

        myDB.execSQL("CREATE TABLE IF NOT EXISTS share_noti (id integer NOT NULL PRIMARY KEY AUTOINCREMENT,title VARCHAR,sahre_msg VARCHAR,date VARCHAR,time VARCHAR);");

        //backdrop = (ImageView) findViewById(R.id.backdrop);

        interstitialAd_noti = new InterstitialAd(this);
        interstitialAd_noti.setAdUnitId(U.INDUS_AD_NOTI);
        AdRequest notadRequest1 = new AdRequest.Builder().build();
        interstitialAd_noti.loadAd(notadRequest1);

        content_view = (WebView) findViewById(R.id.web);
        scrool = (NestedScrollView) findViewById(R.id.scrool);
        share = (ImageView) findViewById(R.id.share);
        addview = (LinearLayout) findViewById(R.id.ads_lay);
        MainActivity.showAd(this, addview,true);

        Bundle extras;
        extras = getIntent().getExtras();
        if (extras != null) {
            int idd = extras.getInt("idd");
            int adss = extras.getInt("Noti_add");
            nactivity = extras.getInt("nactivity");

            if (idd != 0) {
                show_id = idd;
            } else {
                show_id = sharedPreference.getInt(getApplicationContext(), "Noti_id");
            }

            if (adss != 0) {
                show_ads = adss;
                sharedPreference.putInt(getApplicationContext(), "Noti_add", show_ads);
            } else {
                show_ads = sharedPreference.getInt(getApplicationContext(), "Noti_add");
            }
        }
        String query = "update " + tablenew + " set isclose='1' where id='" + show_id + "'";
        myDB.execSQL(query);
        U.PQ(query);
        Cursor c = myDB.rawQuery("select * from " + tablenew + " where id =" + show_id + " ", null);
        c.moveToFirst();

        title = c.getString(c.getColumnIndex("bm"));
        message = c.getString(c.getColumnIndex("message"));
        msgType = c.getString(c.getColumnIndex("type"));
        date = c.getString(c.getColumnIndex("date"));
        time = c.getString(c.getColumnIndex("time"));
        int isclose = c.getInt(c.getColumnIndex("isclose"));
        String url = c.getString(c.getColumnIndex("url"));
        str_title = title;

        content_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        TextView tit_txt = (TextView) findViewById(R.id.sticky);
        tit_txt.setText(str_title);

        WebSettings ws = content_view.getSettings();
        ws.setJavaScriptEnabled(true);
        String bodyFont = "<style> body { font-size:20px; } table { font-size:20px; <font face='bamini' > }</style>"
                + "<style> @font-face { font-family:'bamini'; src: url('file:///android_asset/baamini.ttf') } </style>";
        String summary = "<!DOCTYPE html> <html><head>" + bodyFont + " </head> <body >"
                + "<br>" + message + "</body></html>";
        String str = "";
        try {
            str = message.substring(0, 4);
        } catch (Exception e) {
            str = "";
        }

        if (str.equals("http")) {
            content_view.loadUrl(message);
        } else {
            content_view.loadDataWithBaseURL("", summary, "text/html", "utf-8", null);
        }

        content_view.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // Handle the error

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //view.loadUrl(url);

                Intent i = new Intent();
                i.setAction(Intent.ACTION_VIEW);
                i.addCategory(Intent.CATEGORY_BROWSABLE);
                i.setData(Uri.parse(url));
                startActivity(i);

                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                try {
                    // Utils.mProgress(ST_Activity.this, "ஏற்றுகிறது. காத்திருக்கவும் ", true).show();
                } catch (Exception e) {

                }

                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                try {
                    //    Utils.mProgress.dismiss();
                } catch (Exception e) {

                }
                super.onPageFinished(view, url);
            }
        });

        btn_close = (AppCompatImageView) findViewById(R.id.btn_close);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nactivity == 1) {
                    if (interstitialAd_noti.isLoaded()) {
                        interstitialAd_noti.show();
                        interstitialAd_noti.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                finish();
                            }
                        });
                    } else finish();
                } else {
                    if (sharedPreference.getInt(ST_Activity.this, "Noti_add") == 1) {
                        if (interstitialAd_noti.isLoaded()) {
                            interstitialAd_noti.show();

                            interstitialAd_noti.setAdListener(new AdListener() {
                                @Override
                                public void onAdClosed() {
                                    Intent i = new Intent(ST_Activity.this, MainActivity.class);
                                    finish();
                                    startActivity(i);
                                }
                            });
                        } else {
                            sharedPreference.putInt(getApplicationContext(), "Noti_add", 0);
                            Intent i = new Intent(ST_Activity.this, MainActivity.class);
                            finish();
                            startActivity(i);
                        }
                    } else {
                        finish();
                    }
                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog shareDialog = new Dialog(ST_Activity.this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
                shareDialog.setContentView(R.layout.share_dialog);

                ListView share_list = (ListView) shareDialog.findViewById(R.id.list);
                TextView txtnone = (TextView) shareDialog.findViewById(R.id.txt_none);
                txtnone.setVisibility(View.GONE);
                TextView txtTitle = (TextView) shareDialog.findViewById(R.id.title_text);
                txtTitle.setText("Share Via :");

                listApp = showAllShareApp();

                if (listApp != null) {
                    share_list.setAdapter(new MyAdapter(ST_Activity.this, pManager, listApp));
                    share_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String[] shareString = U.substringsBetween(message, "<tt>", "</tt>");
                            if(shareString!=null) {
                                Log.e("strrr",""+shareString.length);
                                for (int i = 0; i < shareString.length; i++) {
                                    message = message.replace(shareString[i], "((?))" + i + "((?))");
                                    Log.e("strrrr1", message);
                                }
                                message = CodetoTamilUtil.convertToTamil(CodetoTamilUtil.BAMINI, Html.fromHtml(message).toString());
                                for (int i = 0; i < shareString.length; i++) {
                                    message = message.replace("((?))" + i + "((?))", shareString[i]);
                                    Log.e("strrrr2", message);
                                }
                                String[] symboles = {"&#36;","&#8242;","&#39;"};
                                String[] symboles1 = {"$","′","′"};
                                for (int i = 0; i<symboles.length; i++){
                                    message = message.replace(symboles[i],symboles1[i]);
                                }
                                share(listApp.get(position), message);
                            }else{
                                message = CodetoTamilUtil.convertToTamil(CodetoTamilUtil.BAMINI, Html.fromHtml(message).toString());
                                String[] symboles = {"&#36;","&#8242;","&#39;"};
                                String[] symboles1 = {"$","′","′"};
                                for (int i = 0; i<symboles.length; i++){
                                    message = message.replace(symboles[i],symboles1[i]);
                                }
                                share(listApp.get(position), message);
                            }
                            shareDialog.dismiss();
                        }
                    });
                }
                shareDialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (nactivity == 1) {
            if (interstitialAd_noti.isLoaded()) {
                interstitialAd_noti.show();
                interstitialAd_noti.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        finish();
                    }
                });
            } else finish();
        } else {
            if (sharedPreference.getInt(ST_Activity.this, "Noti_add") == 1) {
                if (interstitialAd_noti.isLoaded()) {
                    interstitialAd_noti.show();

                    interstitialAd_noti.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            Intent i = new Intent(ST_Activity.this, MainActivity.class);
                            finish();
                            startActivity(i);
                        }
                    });
                } else {
                    sharedPreference.putInt(getApplicationContext(), "Noti_add", 0);
                    Intent i = new Intent(ST_Activity.this, MainActivity.class);
                    finish();
                    startActivity(i);
                }
            } else {
                //   finish();
                sharedPreference.putInt(getApplicationContext(), "Noti_add", 0);
                Intent i = new Intent(ST_Activity.this, MainActivity.class);
                finish();
                startActivity(i);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPreference.getInt(ST_Activity.this, "open" + get_curday()) == 0) {
            sharedPreference.putInt(ST_Activity.this, "open" + get_curday(), 1);
            //open_dia();
        }
        if (sharedPreference.getInt(ST_Activity.this, "Noti_add") == 1) {
            adds(addview);
        } else {
            // MainActivity.load_addFromMain(ST_Activity.this, addview);
        }
    }

    public String get_curday() {
        Calendar cal = Calendar.getInstance();

        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return day + "/" + (month + 1) + "/" + year;
    }

    public void adds(final LinearLayout layout) {
        AdView adView = new AdView(ST_Activity.this);
        adView.setAdUnitId(U.BANNER_AD);
        adView.setAdSize(AdSize.SMART_BANNER);
        try {
            layout.removeAllViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
        layout.addView(adView);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                layout.setVisibility(View.VISIBLE);
            }
        });
        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);
    }

    public void slideInFab(final AppCompatImageView mFab) {

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mFab.getLayoutParams();
        float dy = mFab.getWidth() + lp.rightMargin;
       /* if (mFab.getTranslationX() != dy) {
            return;
        }*/

        mFab.setVisibility(View.VISIBLE);
        mFab.animate()
                .setStartDelay(0)
                .setDuration(200)
                .setInterpolator(new FastOutLinearInInterpolator())
                .translationX(-lp.rightMargin)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(final Animator animation) {
                        super.onAnimationEnd(animation);

                    }
                })
                .start();

    }

    public void slideOutFab(final AppCompatImageView mFab) {

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mFab.getLayoutParams();
       /* if (mFab.getTranslationX() != 0f) {
            return;
        }*/
        mFab.animate()
                .setStartDelay(0)
                .setDuration(200)
                .setInterpolator(new FastOutLinearInInterpolator())
                .translationX(mFab.getWidth() - lp.rightMargin)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(final Animator animation) {
                        super.onAnimationEnd(animation);
                        mFab.setVisibility(View.INVISIBLE);
                    }
                })
                .start();
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    private void share(ResolveInfo appInfo, String sharefinal) {

        if (appInfo.activityInfo.packageName.equals("com.whatsapp")) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/*");
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, str_title);
            Uri uriUrl = Uri.parse("whatsapp://send?text=" + "நித்ரா வேலைவாய்ப்பு செயலி வழியாக பகிரப்பட்டது. செயலியை தரவிறக்கம் செய்ய:" + U.UTM_SOURCE + "\n\n" +
                    sharefinal + " \nகீழுள்ள லிங்கை கிளிக் செய்து இந்த இலவச ஆன்டிராய்டு அப்ளிகேசனை டவுன்லோடு செய்து கொள்ளுங்கள்! "
                    + U.UTM_SOURCE);
            sendIntent.setAction(Intent.ACTION_VIEW);
            sendIntent.setData(uriUrl);
            sendIntent.setComponent(new ComponentName(appInfo.activityInfo.packageName, appInfo.activityInfo.name));
            startActivity(sendIntent);

        } else {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, str_title);
            sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "நித்ரா வேலைவாய்ப்பு செயலி வழியாக பகிரப்பட்டது. செயலியை தரவிறக்கம் செய்ய: " + U.UTM_SOURCE + "\n\n" +
                            sharefinal + " \nகீழுள்ள லிங்கை கிளிக் செய்து இந்த இலவச ஆன்டிராய்டு அப்ளிகேசனை டவுன்லோடு செய்து கொள்ளுங்கள்! "
                            + U.UTM_SOURCE);
            sendIntent.setComponent(new ComponentName(appInfo.activityInfo.packageName, appInfo.activityInfo.name));
            sendIntent.setType("text/*");
            startActivity(sendIntent);
        }
    }

    private java.util.List<ResolveInfo> showAllShareApp() {

        java.util.List<ResolveInfo> mApps = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        intent.setType("text/plain");
        PackageManager pManager = getPackageManager();
        mApps = pManager.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        return mApps;
    }
}