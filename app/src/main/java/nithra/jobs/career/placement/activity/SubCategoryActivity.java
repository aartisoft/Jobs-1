package nithra.jobs.career.placement.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import nithra.jobs.career.placement.MainActivity;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.adapters.SubCategoryAdapter;
import nithra.jobs.career.placement.engine.DBHelper;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

public class SubCategoryActivity extends AppCompatActivity {

    List<String> values;
    RecyclerView mRecyclerView;
    SubCategoryAdapter mAdapter;
    DBHelper dbHelper;
    String board;
    TextView title;
    LinearLayout adLayout;
    SharedPreference pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.recyclerview);
        values = new ArrayList<>();
        pref = new SharedPreference();

        adLayout = findViewById(R.id.adLayout);

        if (pref.getInt(SubCategoryActivity.this, U.SH_AD_PURCHASED) == 0) {
            MainActivity.showAd(this, adLayout, true);
        }

        board = getIntent().getStringExtra("board");

        dbHelper = new DBHelper(this);
        Cursor c = dbHelper.getQry("SELECT DISTINCT exam FROM govtexams where board = '" + board + "'");
        c.moveToFirst();
        do {
            String str = c.getString(c.getColumnIndexOrThrow("exam"));
            values.add(str);

        } while (c.moveToNext());
        c.close();

        createView();
    }

    private void createView() {
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title = findViewById(R.id.title);
        title.setText(board);

        mAdapter = new SubCategoryAdapter(SubCategoryActivity.this, values);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        if (values != null) {
            if (values.size() != 0) {
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mRecyclerView.setVisibility(View.GONE);
            }
        }
    }
}
