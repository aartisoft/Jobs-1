package nithra.jobs.career.placement.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import nithra.jobs.career.placement.MainActivity;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.adapters.TestAdapter;
import nithra.jobs.career.placement.engine.DBHelper;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

/**
 * Created by nithra-apps on 6/11/17.
 */

public class SplashScreenActivity extends AppCompatActivity {

    Handler mHandler;
    Runnable mRunnable;
    SharedPreference pref;
    LinearLayout topLay;
    //    TextView authorname;
    Typeface font;
    TextView sloganText, todayQuote;
    public int[] backImgArray = {R.drawable.front_screen_1, R.drawable.front_screen_2,
            R.drawable.front_screen_3, R.drawable.front_screen_4, R.drawable.front_screen_5,
            R.drawable.front_screen_6, R.drawable.front_screen_7, R.drawable.front_screen_8};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent(true);
        setContentView(R.layout.intro);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        pref = new SharedPreference();

        todayQuote = findViewById(R.id.quote);
        sloganText = findViewById(R.id.slogantext);
        topLay = findViewById(R.id.top_lay);
//        authorname = ((TextView) findViewById(R.id.authorname));
        font = Typeface.createFromAsset(getAssets(), "fonts/Aurella.ttf");
        sloganText.setTypeface(this.font);
        todayQuote.setTypeface(this.font);
//        authorname.setTypeface(this.font);
        if (pref.getInt(this, "DB_MOVE" + U.versioncode_get(this)) == 0) {
            new BackgroundMoveDbFromAsset().execute();
        } else {
            todayQuote();
        }

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();
            }
        };
        mHandler.postDelayed(mRunnable, 3500);
    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            else {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }
    }

    public void todayQuote() {
        DBHelper dbHelper = new DBHelper(this);
        if (pref.getString(this, U.SH_QUOTE_DATE).equals(U.currentDate())) {
            todayQuote.setText(pref.getString(this, U.SH_TODAY_QUOTE));
            topLay.setBackgroundResource(backImgArray[pref.getInt(this, U.SH_TODAY_QUOTE_IMG)]);
//            authorname.setText("-" + pref.getString(this, U.SH_TODAY_QUOTE_NAME));
        } else {
            pref.putString(this, U.SH_QUOTE_DATE, U.currentDate());
            int backImg = imageShuffle();
            pref.putInt(SplashScreenActivity.this, U.SH_TODAY_QUOTE_IMG, backImg);
            topLay.setBackgroundResource(backImgArray[backImg]);

            Cursor cursor = dbHelper.getQry("select * from jobsQuotes where flag='0' order by Random() limit 1");
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                dbHelper.executeSql("update jobsQuotes set flag='0' where flag='1'");
                Cursor cur = dbHelper.getQry("select * from jobsQuotes where flag='0' order by Random() limit 1");
                cur.moveToFirst();
                do {
                    String sno = cursor.getString(cursor.getColumnIndexOrThrow("sno"));
                    String quotes = cursor.getString(cursor.getColumnIndexOrThrow("quotes"));
//                    String str7 = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    todayQuote.setText(quotes);
//                    authorname.setText("-" + str7);
                    pref.putString(this, U.SH_TODAY_QUOTE, "" + quotes);
//                    pref.putString(this, U.SH_TODAY_QUOTE_NAME, "" + str7);
                    dbHelper.executeSql("update jobsQuotes set flag='1' where sno='" + sno + "'");
                } while (cur.moveToNext());
                cur.close();
            } else {
                do {
                    String sno = cursor.getString(cursor.getColumnIndexOrThrow("sno"));
                    String quotes = cursor.getString(cursor.getColumnIndexOrThrow("quotes"));
//                    String str4 = cursor.getString(cursor.getColumnIndexOrThrow("name"));
//                    authorname.setText("-" + str4);
                    todayQuote.setText(quotes);
                    pref.putString(this, U.SH_TODAY_QUOTE, "" + quotes);
//                    pref.putString(this, U.SH_TODAY_QUOTE_NAME, "" + str4);
                    dbHelper.executeSql("update jobsQuotes set flag='1' where sno='" + sno + "'");
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
    }

    public int imageShuffle() {
        ArrayList<Integer> img = new ArrayList();
        img.add(0);
        img.add(1);
        img.add(2);
        img.add(3);
        img.add(4);
        img.add(5);
        img.add(6);
        img.add(7);
        Collections.shuffle(img);
        return img.get(0);
    }

    public class BackgroundMoveDbFromAsset extends AsyncTask<String, String, String> {
        public BackgroundMoveDbFromAsset() {
        }

        protected String doInBackground(String... paramVarArgs) {
            TestAdapter localTestAdapter = new TestAdapter(SplashScreenActivity.this);
            localTestAdapter.createDatabase();
            localTestAdapter.open();
            localTestAdapter.close();
            SplashScreenActivity.this.pref.putInt(SplashScreenActivity.this, "DB_MOVE" + U.versioncode_get(SplashScreenActivity.this), 1);
            return null;
        }

        protected void onPostExecute(String paramString) {
            if (!SplashScreenActivity.this.isFinishing()) {
                U.mProgress.dismiss();
            }
            todayQuote();
        }

        protected void onPreExecute() {
            if (!SplashScreenActivity.this.isFinishing()) {
                U.mProgress(SplashScreenActivity.this, "Loading please wait ...", Boolean.valueOf(false)).show();
            }
        }
    }

}
