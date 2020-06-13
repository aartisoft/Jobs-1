package nithra.jobs.career.placement;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.widget.NestedScrollView;
import nithra.jobs.career.placement.activity.EmployerHomeActivity;
import nithra.jobs.career.placement.adapters.MyAdapter;
import nithra.jobs.career.placement.engine.LocalDB;
import nithra.jobs.career.placement.utills.CodetoTamilUtil;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class ST_Activity extends AppCompatActivity {
    SharedPreference sharedPreference;
    String str_title = "";
    WebView content_view;
    ImageView share;
    SQLiteDatabase myDB;
    LocalDB localDB;
    String tablenew = "noti_cal";
    String title, message, msgType, date, time, url, bm, ntype;
    AppCompatImageView btn_close;
    int show_id, show_ads, idd, isclose;
    NestedScrollView scrool;
    int nactivity = 0;
    java.util.List<ResolveInfo> listApp;
    PackageManager pManager;
    FloatingActionButton save;
    com.facebook.ads.InterstitialAd interstitialAd;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.st_lay);
        sharedPreference = new SharedPreference();
        localDB = new LocalDB(this);
        myDB = openOrCreateDatabase("myDB", 0, null);
        pManager = getPackageManager();
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + tablenew
                + " (id integer NOT NULL PRIMARY KEY AUTOINCREMENT,title VARCHAR,message VARCHAR,date VARCHAR,time VARCHAR,isclose INT(4),isshow INT(4) default 0,type VARCHAR," +
                "bm VARCHAR,ntype VARCHAR,url VARCHAR);");

        content_view = findViewById(R.id.web);
        scrool = findViewById(R.id.scrool);
        share = findViewById(R.id.share);
        LinearLayout addview = findViewById(R.id.ads_lay);
        save = findViewById(R.id.fab);
        int count = sharedPreference.getInt(ST_Activity.this, U.SH_SHOW_INS_AD) + 1;
        sharedPreference.putInt(ST_Activity.this, U.SH_SHOW_INS_AD, count);

        if (sharedPreference.getString(ST_Activity.this, U.SH_USER_TYPE).equals(U.EMPLOYEE)) {
            if (sharedPreference.getInt(this, U.SH_AD_PURCHASED) == 0) {
                Log.e("adCount", "" + sharedPreference.getInt(ST_Activity.this, U.SH_SHOW_INS_AD));
                MainActivity.loadFaceBookBanner(ST_Activity.this, addview);
                if (sharedPreference.getInt(ST_Activity.this, U.SH_SHOW_INS_AD) == 3) {
                    loadInterstitial();
                    Log.e("adCount", "ad_load");
                } else if (sharedPreference.getInt(ST_Activity.this, U.SH_SHOW_INS_AD) > 3) {
                    sharedPreference.putInt(ST_Activity.this, U.SH_SHOW_INS_AD, 0);
                }
            } else {
                addview.setVisibility(View.GONE);
            }
        } else {
            addview.setVisibility(View.GONE);
        }

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
        if (c.getCount() != 0) {
            idd = c.getInt(c.getColumnIndex("id"));
            title = c.getString(c.getColumnIndex("title"));
            message = c.getString(c.getColumnIndex("message"));
            msgType = c.getString(c.getColumnIndex("type"));
            date = c.getString(c.getColumnIndex("date"));
            time = c.getString(c.getColumnIndex("time"));
            isclose = c.getInt(c.getColumnIndex("isclose"));
            url = c.getString(c.getColumnIndex("url"));
            bm = c.getString(c.getColumnIndex("bm"));
            ntype = c.getString(c.getColumnIndex("ntype"));
            str_title = bm;

            if (sharedPreference.getString(ST_Activity.this, U.SH_USER_TYPE).equals(U.EMPLOYEE)) {
                save.show();
                if (localDB.isSaveNotificationExists(idd)) {
                    save.setImageBitmap(textAsBitmap(getResources().getString(R.string.delete), getResources().getDimension(R.dimen.txt_25), Color.BLACK));
                } else {
                    save.setImageBitmap(textAsBitmap(getResources().getString(R.string.save), getResources().getDimension(R.dimen.txt_25), Color.BLACK));
                }
            } else {
                save.hide();
            }

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (localDB.isSaveNotificationExists(idd)) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ST_Activity.this);
                        alertDialogBuilder
                                .setMessage("நீக்க விரும்புகிறீர்களா?")
                                .setCancelable(false)
                                .setPositiveButton("ஆம்", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int ids) {
                                        if (localDB.deleteSavedNotification(idd)) {
                                            save.setImageBitmap(textAsBitmap(getResources().getString(R.string.save), getResources().getDimension(R.dimen.txt_25), Color.BLACK));
                                        } else {
                                            save.setImageBitmap(textAsBitmap(getResources().getString(R.string.delete), getResources().getDimension(R.dimen.txt_25), Color.BLACK));
                                        }
                                    }
                                })
                                .setNegativeButton("இல்லை", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    } else {
                        if (localDB.saveNotification(idd, title, message, date, time,
                                String.valueOf(isclose), msgType, bm, ntype, url)) {
                            save.setImageBitmap(textAsBitmap(getResources().getString(R.string.delete), getResources().getDimension(R.dimen.txt_25), Color.BLACK));
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ST_Activity.this);
                            alertDialogBuilder
                                    .setTitle("அறிவிப்பு!")
                                    .setMessage("இந்த அறிவிப்பு சேமிக்கப்பட்டது.சேமித்த அறிவிப்புகளை முகப்பு பக்கத்தில் உள்ள மூன்று கோடிட்ட MenuBar - ஐ கிளிக் செய்து அதில் உள்ள சேமித்த அறிவிப்புகள் பகுதியில் பார்க்கவும்")
                                    .setCancelable(false)
                                    .setPositiveButton("தொடர", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int ids) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setNegativeButton("வெளியேறு", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                            close();
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        } else {
                            save.setImageBitmap(textAsBitmap(getResources().getString(R.string.save), getResources().getDimension(R.dimen.txt_25), Color.BLACK));
                        }
                    }
                }
            });

            content_view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });

            TextView tit_txt = findViewById(R.id.sticky);
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
                    U.custom_tabs(ST_Activity.this, url);
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

            btn_close = findViewById(R.id.btn_close);

            btn_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    close();
                }
            });

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog shareDialog = new Dialog(ST_Activity.this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
                    shareDialog.setContentView(R.layout.share_dialog);

                    ListView share_list = shareDialog.findViewById(R.id.list);
                    TextView txtnone = shareDialog.findViewById(R.id.txt_none);
                    txtnone.setVisibility(View.GONE);
                    TextView txtTitle = shareDialog.findViewById(R.id.title_text);
                    txtTitle.setText("Share Via :");

                    listApp = showAllShareApp();

                    if (listApp != null) {
                        share_list.setAdapter(new MyAdapter(ST_Activity.this, pManager, listApp));
                        share_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String[] shareString = U.substringsBetween(message, "<tt>", "</tt>");
                                if (shareString != null) {
                                    Log.e("strrr", "" + shareString.length);
                                    for (int i = 0; i < shareString.length; i++) {
                                        message = message.replace(shareString[i], "((?))" + i + "((?))");
                                        Log.e("strrrr1", message);
                                    }
                                    message = CodetoTamilUtil.convertToTamil(CodetoTamilUtil.BAMINI, Html.fromHtml(message).toString());
                                    for (int i = 0; i < shareString.length; i++) {
                                        message = message.replace("((?))" + i + "((?))", shareString[i]);
                                        Log.e("strrrr2", message);
                                    }
                                    String[] symboles = {"&#36;", "&#8242;", "&#39;"};
                                    String[] symboles1 = {"$", "′", "′"};
                                    for (int i = 0; i < symboles.length; i++) {
                                        message = message.replace(symboles[i], symboles1[i]);
                                    }
                                    share(listApp.get(position), message);
                                } else {
                                    message = CodetoTamilUtil.convertToTamil(CodetoTamilUtil.BAMINI, Html.fromHtml(message).toString());
                                    String[] symboles = {"&#36;", "&#8242;", "&#39;"};
                                    String[] symboles1 = {"$", "′", "′"};
                                    for (int i = 0; i < symboles.length; i++) {
                                        message = message.replace(symboles[i], symboles1[i]);
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
        } else {
            sharedPreference.putInt(getApplicationContext(), "Noti_add", 0);
            Intent i = new Intent(ST_Activity.this, MainActivity.class).putExtra("mode", "gcm");
            finish();
            startActivity(i);
        }
        c.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPreference.getInt(ST_Activity.this, "open" + get_curday()) == 0) {
            sharedPreference.putInt(ST_Activity.this, "open" + get_curday(), 1);
        }
    }

    public String get_curday() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return day + "/" + (month + 1) + "/" + year;
    }

    private void share(ResolveInfo appInfo, String sharefinal) {

        if (appInfo.activityInfo.packageName.equals("com.whatsapp")) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/*");
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, str_title);
            Uri uriUrl = Uri.parse("whatsapp://send?text=" + "நித்ரா வேலைவாய்ப்பு செயலி வழியாக பகிரப்பட்டது. செயலியை தரவிறக்கம் செய்ய:" + U.UTM_SOURCE + "\n\n" +
                    sharefinal + " \nகீழுள்ள லிங்கை கிளிக் செய்து இந்த இலவச ஆண்ட்ராய்டு அப்ளிகேசனை டவுன்லோடு செய்து கொள்ளுங்கள்! "
                    + U.UTM_SOURCE);
            sendIntent.setAction(Intent.ACTION_VIEW);
            sendIntent.setData(uriUrl);
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);

        } else {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, str_title);
            sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "நித்ரா வேலைவாய்ப்பு செயலி வழியாக பகிரப்பட்டது. செயலியை தரவிறக்கம் செய்ய: " + U.UTM_SOURCE + "\n\n" +
                            sharefinal + " \nகீழுள்ள லிங்கை கிளிக் செய்து இந்த இலவச ஆண்ட்ராய்டு அப்ளிகேசனை டவுன்லோடு செய்து கொள்ளுங்கள்! "
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

    public void close() {
        if (sharedPreference.getString(ST_Activity.this, U.SH_USER_TYPE).equals(U.EMPLOYEE)) {
            Log.e("adCount", "showAd  " + nactivity);
            if (nactivity == 1) {
                if (interstitialAd != null && interstitialAd.isAdLoaded()) {
                    sharedPreference.putInt(ST_Activity.this, U.SH_SHOW_INS_AD, 0);
                    Log.e("adCount", "showAd");
                    interstitialAd.show();
                    interstitialAd.setAdListener(new InterstitialAdListener() {
                        @Override
                        public void onInterstitialDisplayed(Ad ad) {

                        }

                        @Override
                        public void onInterstitialDismissed(Ad ad) {
                            finish();
                        }

                        @Override
                        public void onError(Ad ad, AdError adError) {

                        }

                        @Override
                        public void onAdLoaded(Ad ad) {

                        }

                        @Override
                        public void onAdClicked(Ad ad) {

                        }

                        @Override
                        public void onLoggingImpression(Ad ad) {

                        }
                    });
                } else {
                    finish();
                }
            } else {
                if (sharedPreference.getInt(ST_Activity.this, "Noti_add") == 1) {
                    sharedPreference.putInt(getApplicationContext(), "Noti_add", 0);
                    Intent i = new Intent(ST_Activity.this, MainActivity.class).putExtra("mode", "gcm");
                    finish();
                    startActivity(i);

                } else {
                    if (interstitialAd != null && interstitialAd.isAdLoaded()) {
                        sharedPreference.putInt(ST_Activity.this, U.SH_SHOW_INS_AD, 0);
                        Log.e("adCount", "showAd");
                        interstitialAd.show();
                        interstitialAd.setAdListener(new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(Ad ad) {

                            }

                            @Override
                            public void onInterstitialDismissed(Ad ad) {
                                finish();
                            }

                            @Override
                            public void onError(Ad ad, AdError adError) {

                            }

                            @Override
                            public void onAdLoaded(Ad ad) {

                            }

                            @Override
                            public void onAdClicked(Ad ad) {

                            }

                            @Override
                            public void onLoggingImpression(Ad ad) {

                            }
                        });
                    } else {
                        finish();
                    }

                }
            }
        } else if (sharedPreference.getString(ST_Activity.this, U.SH_USER_TYPE).equals(U.EMPLOYER)) {
            sharedPreference.putInt(getApplicationContext(), "Noti_add", 0);
            Intent i = new Intent(ST_Activity.this, EmployerHomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(i);
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

    //------------------------------------ FB Interstitial ad --------------------------------------

    public void loadInterstitial() {
        interstitialAd = new com.facebook.ads.InterstitialAd(this, U.FB_NOTI_INS_EXIT);
        interstitialAd.loadAd();
    }
}