package nithra.jobs.career.placement.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nithra.jobs.career.placement.MainActivity;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.adapters.SubCategoryAdapter;
import nithra.jobs.career.placement.engine.DBHelper;

public class SubCategoryActivity extends AppCompatActivity {

    List<String> values;
    RecyclerView mRecyclerView;
    SubCategoryAdapter mAdapter;
    DBHelper dbHelper;
    String board;
    TextView title;
    LinearLayout adLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.recyclerview);
        values = new ArrayList<>();

        adLayout = findViewById(R.id.adLayout);
        MainActivity.showAd(this, adLayout,true);

        board = getIntent().getStringExtra("board");

        dbHelper=new DBHelper(this);
        Cursor c=dbHelper.getQry("SELECT DISTINCT exam FROM govtexams where board = '" + board +"'");
        c.moveToFirst();
        do{
            String str=c.getString(c.getColumnIndexOrThrow("exam"));
            values.add(str);

        }while(c.moveToNext());
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

        title =findViewById(R.id.title);
        title.setText(board);

        mAdapter = new SubCategoryAdapter(SubCategoryActivity.this,values);
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
