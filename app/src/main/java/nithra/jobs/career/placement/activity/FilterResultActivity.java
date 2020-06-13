package nithra.jobs.career.placement.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.fragments.JobListFragment;

public class FilterResultActivity extends AppCompatActivity {

    Toolbar toolbar;
    String skill = "", location = "", qualification = "", experience = "", salary = "", mode = "", jobcat = "", action = "", workMode = "";
    LinearLayout adLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.filter_result_lay);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();
        action = bundle.getString("action");
        skill = bundle.getString("skill");
        location = bundle.getString("location");
        qualification = bundle.getString("qualification");
        experience = bundle.getString("experience");
        salary = bundle.getString("salary");
        mode = bundle.getString("mode");
        Log.e("jobmode", mode);
        workMode = bundle.getString("workmode");
        jobcat = bundle.getString("jobcat");
        adLayout = findViewById(R.id.adview);
        getSupportFragmentManager().beginTransaction().add(R.id.container, JobListFragment.newInstance(action, skill, location, qualification, experience, salary, mode, jobcat, workMode, "1"), "").commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
