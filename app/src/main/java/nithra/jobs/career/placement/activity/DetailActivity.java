package nithra.jobs.career.placement.activity;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.text.Html;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;

import nithra.jobs.career.placement.MainActivity;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.adapters.MyAdapter;
import nithra.jobs.career.placement.utills.CodetoTamilUtil;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

/**
 * Created by nithra on 16/2/18.
 */

public class DetailActivity extends AppCompatActivity {

    SharedPreference sharedPreference;
    WebView content_view;
    // ImageView backdrop;Bitmap bitmap;
    InterstitialAd interstitialAd_noti;
    LinearLayout addview;
    AppCompatImageView btn_close;
    String str_title, message, url;
    NestedScrollView scrool;
    int share_val = 0;
    ImageView share;
    java.util.List<ResolveInfo> listApp;
    PackageManager pManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.st_lay);

        pManager = getPackageManager();
        sharedPreference = new SharedPreference();

        interstitialAd_noti = new InterstitialAd(this);
        interstitialAd_noti.setAdUnitId(U.INDUS_AD_NOTI);
        AdRequest notadRequest1 = new AdRequest.Builder().build();
        interstitialAd_noti.loadAd(notadRequest1);

        content_view = findViewById(R.id.web);

        scrool = findViewById(R.id.scrool);
        addview = findViewById(R.id.ads_lay);
        share = findViewById(R.id.share);
        if(U.isNetworkAvailable(DetailActivity.this)) {
            MainActivity.showAd(this, addview, true);
        }

        Bundle extras;
        extras = getIntent().getExtras();
        if (extras != null) {

            str_title = extras.getString("title");
            message = extras.getString("message");
            url = extras.getString("url");

        }


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

        String str = message.substring(0, 4);

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
                    // Utils.mProgress(DetailActivity.this, "ஏற்றுகிறது. காத்திருக்கவும் ", true).show();
                } catch (Exception e) {

                }

                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                try {
                    //  Utils.mProgress.dismiss();
                } catch (Exception e) {

                }
                super.onPageFinished(view, url);
            }
        });


        btn_close = findViewById(R.id.btn_close);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialAd_noti.isLoaded()) {
                    interstitialAd_noti.show();
                    interstitialAd_noti.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            finish();
                        }
                    });
                } else finish();
            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog shareDialog = new Dialog(DetailActivity.this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
                shareDialog.setContentView(R.layout.share_dialog);

                ListView share_list = shareDialog.findViewById(R.id.list);
                TextView txtnone = shareDialog.findViewById(R.id.txt_none);
                txtnone.setVisibility(View.GONE);
                TextView txtTitle = shareDialog.findViewById(R.id.title_text);
                txtTitle.setText("Share Via :");

                listApp = showAllShareApp();

                if (listApp != null) {
                    share_list.setAdapter(new MyAdapter(DetailActivity.this, pManager, listApp));
                    share_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String[] shareString = U.substringsBetween(message, "<tt>", "</tt>");
                            if(shareString!=null) {
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
                                String[] symboles = {"&#36;","&#8242;","&#39;"};
                                String[] symboles1 = {"$","′","′"};
                                for (int i = 0; i<symboles.length; i++){
                                    message = message.replace(symboles[i],symboles1[i]);
                                }
                                share(listApp.get(position), message);
                            } else {
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

        if (interstitialAd_noti.isLoaded()) {
            interstitialAd_noti.show();
            interstitialAd_noti.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    finish();
                }
            });
        } else {
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        adds(addview);

    }

    public void adds(final LinearLayout layout) {
        AdView adView = new AdView(DetailActivity.this);
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
    
    private java.util.List<ResolveInfo> showAllShareApp() {

        java.util.List<ResolveInfo> mApps = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        intent.setType("text/plain");
        PackageManager pManager = getPackageManager();
        mApps = pManager.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        return mApps;
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

}
