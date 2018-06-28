package nithra.jobs.career.placement.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

/**
 * Created by nithra on 5/3/18.
 */

public class SettingsActivity extends AppCompatActivity {

    Switch notiSoundSwitch,notiSwitch;
    SharedPreference pref;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.settings_lay);
        pref = new SharedPreference();
        back = findViewById(R.id.back);
        notiSoundSwitch = findViewById(R.id.switch1);
        notiSwitch = findViewById(R.id.switch2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (pref.getInt(SettingsActivity.this, U.SH_NOTIFICATION_SOUND) == 0) {
            notiSoundSwitch.setChecked(true);
        } else if (pref.getInt(SettingsActivity.this,U.SH_NOTIFICATION_SOUND) == 1) {
            notiSoundSwitch.setChecked(false);
        }

        if (pref.getInt(SettingsActivity.this,U.SH_MUTE_NOTIFICATION) == 0) {
            notiSwitch.setChecked(true);
        } else if (pref.getInt(SettingsActivity.this,U.SH_MUTE_NOTIFICATION) == 1) {
            notiSwitch.setChecked(false);
        }

        notiSoundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    pref.putInt( SettingsActivity.this,U.SH_NOTIFICATION_SOUND,0);
                    notiSoundSwitch.setChecked(true);
                } else {
                    // The toggle is disabled
                    pref.putInt( SettingsActivity.this,U.SH_NOTIFICATION_SOUND,1);
                    notiSoundSwitch.setChecked(false);
                }
            }
        });

        notiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    pref.putInt( SettingsActivity.this,U.SH_MUTE_NOTIFICATION,0);
                    notiSwitch.setChecked(true);
                } else {
                    // The toggle is disabled
                    pref.putInt( SettingsActivity.this,U.SH_MUTE_NOTIFICATION,1);
                    notiSwitch.setChecked(false);
                }
            }
        });
    }

}
