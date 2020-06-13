package nithra.jobs.career.placement.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.networking.MySingleton;
import nithra.jobs.career.placement.pojo.Item;
import nithra.jobs.career.placement.utills.FlexboxLayout;
import nithra.jobs.career.placement.utills.L;
import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

/**
 * Created by nithra on 5/3/18.
 */

public class SettingsActivity extends AppCompatActivity {

    String distId = "", distName = "";
    private Switch notiSoundSwitch, notiSwitch;
    private SharedPreference pref;
    private FlexboxLayout fblDistrict;
    private Dialog districtDia;
    private ArrayList<Item> userDistricts;
    private ArrayList<String> selectedDistrict;
    private ArrayList<String> selectedDistrictName;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.settings_lay);
        pref = new SharedPreference();
        ImageView back = findViewById(R.id.back);
        notiSoundSwitch = findViewById(R.id.switch1);
//        notiSwitch = findViewById(R.id.switch2);

        fblDistrict = findViewById(R.id.fblDistrict);

        ImageView editDistrict = findViewById(R.id.editDistrict);

        userDistricts = new ArrayList<>();
        selectedDistrict = new ArrayList<>();
        selectedDistrictName = new ArrayList<>();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (pref.getInt(SettingsActivity.this, U.SH_NOTIFICATION_SOUND) == 0) {
            notiSoundSwitch.setChecked(true);
        } else if (pref.getInt(SettingsActivity.this, U.SH_NOTIFICATION_SOUND) == 1) {
            notiSoundSwitch.setChecked(false);
        }

//        if (pref.getInt(SettingsActivity.this, U.SH_MUTE_NOTIFICATION) == 0) {
//            notiSwitch.setChecked(true);
//        } else if (pref.getInt(SettingsActivity.this, U.SH_MUTE_NOTIFICATION) == 1) {
//            notiSwitch.setChecked(false);
//        }

        notiSoundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    pref.putInt(SettingsActivity.this, U.SH_NOTIFICATION_SOUND, 0);
                    notiSoundSwitch.setChecked(true);
                } else {
                    // The toggle is disabled
                    pref.putInt(SettingsActivity.this, U.SH_NOTIFICATION_SOUND, 1);
                    notiSoundSwitch.setChecked(false);
                }
            }
        });

//        notiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    // The toggle is enabled
//                    pref.putInt(SettingsActivity.this, U.SH_MUTE_NOTIFICATION, 0);
//                    notiSwitch.setChecked(true);
//                } else {
//                    // The toggle is disabled
//                    pref.putInt(SettingsActivity.this, U.SH_MUTE_NOTIFICATION, 1);
//                    notiSwitch.setChecked(false);
//                }
//            }
//        });
        if (!pref.getString(SettingsActivity.this, U.SH_USER_DISTRICT_NAME).equals("")) {
            setValues(U.SH_USER_DISTRICT_NAME, fblDistrict);
        }

        editDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (U.isNetworkAvailable(SettingsActivity.this)) {
                    loadDistricts("get_ditrict");
                } else {
                    L.t(SettingsActivity.this, U.INA);
                }
            }
        });

    }

    //--------------------------------- Edit_District ----------------------------------------------

    private void loadDistricts(final String param) {
        U.mProgress(SettingsActivity.this, "Loading...", false).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.USER_DISTRICT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        U.mProgress.dismiss();
                        Log.e("showresponse", response);
                        switch (param) {
                            case "get_ditrict":
                                showDistricts(response);
                                break;
                            case "set_ditrict": {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    if (status.equals("success")) {
                                        button.setEnabled(true);
                                        if (!distId.isEmpty()) {
                                            pref.putString(SettingsActivity.this, U.SH_USER_DISTRICT_ID, distId);
                                        } else {
                                            pref.putString(SettingsActivity.this, U.SH_USER_DISTRICT_ID, "");
                                        }
                                        if (!distName.isEmpty()) {
                                            pref.putString(SettingsActivity.this, U.SH_USER_DISTRICT_NAME, distName);
                                        } else {
                                            pref.putString(SettingsActivity.this, U.SH_USER_DISTRICT_NAME, "");
                                        }

                                        setValues(U.SH_USER_DISTRICT_NAME, fblDistrict);

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorHandling(error);
                        if (districtDia != null && districtDia.isShowing()) {
                            districtDia.dismiss();
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                switch (param) {
                    case "get_ditrict":
                        params.put("action", "get_ditrict");
                        break;
                    case "set_ditrict":
                        params.put("action", "set_ditrict");
                        params.put("user_type", pref.getString(SettingsActivity.this, U.SH_USER_TYPE));
                        params.put("district", pref.getString(SettingsActivity.this, U.SH_USER_DISTRICT_ID));
                        params.put("category", "");
                        params.put("job_title", "");
                        break;
                }
                params.put("user_type", pref.getString(SettingsActivity.this, U.SH_USER_TYPE));
                params.put("fcm_id", pref.getString(SettingsActivity.this, "regId"));
                params.put("employee_id", pref.getString(SettingsActivity.this, U.SH_EMPLOYEE_ID));
                params.put("android_id", U.getAndroidId(SettingsActivity.this));
                params.put("vcode", String.valueOf(U.versioncode_get(SettingsActivity.this)));
                Log.e("paramsresponse", "" + params);
                return params;
            }
        };

        MySingleton.getInstance(SettingsActivity.this).addToRequestQue(stringRequest);
    }

    private void errorHandling(VolleyError error) {
        String e;
        if (error instanceof TimeoutError || error instanceof AuthFailureError || error instanceof ServerError)
            e = U.SERVER_ERROR;
        else if (error instanceof NetworkError)
            e = U.INA;
        else if (error instanceof ParseError)
            e = U.ERROR;
        else
            e = U.ERROR;
        L.t(SettingsActivity.this, e);
    }

    private void showDistricts(String json) {
        Log.e("showresponse_Districts", "" + json);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            userDistricts = new ArrayList<>();
            userDistricts.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                Item data = new Item();
                data.setId(jo.getInt("dist_id"));
                data.setItem(jo.getString("dist"));
                userDistricts.add(data);
            }
            showDistrictsDialog();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showDistrictsDialog() {
        districtDia = new Dialog(SettingsActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        districtDia.requestWindowFeature(Window.FEATURE_NO_TITLE);
        districtDia.setContentView(R.layout.user_district_selection);
        districtDia.setCancelable(true);
        if (districtDia.getWindow() != null) {
            districtDia.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        ListView listview = districtDia.findViewById(R.id.listview);

        SearchView searchview = districtDia.findViewById(R.id.searchview);
        searchview.setVisibility(View.GONE);

        TextView noText = districtDia.findViewById(R.id.no_text);
        noText.setVisibility(View.GONE);

        selectedDistrict.clear();
        selectedDistrictName.clear();

        if (!pref.getString(SettingsActivity.this, U.SH_USER_DISTRICT_ID).equals("")) {
            String[] array = pref.getString(SettingsActivity.this, U.SH_USER_DISTRICT_ID).split(",");
            selectedDistrict.clear();
            selectedDistrict.addAll(Arrays.asList(array));
        }

        if (!pref.getString(SettingsActivity.this, U.SH_USER_DISTRICT_NAME).equals("")) {
            String[] array = pref.getString(SettingsActivity.this, U.SH_USER_DISTRICT_NAME).split(",");
            selectedDistrictName.clear();
            selectedDistrictName.addAll(Arrays.asList(array));
        }

        final SelectionAdapter aadapter = new SelectionAdapter(SettingsActivity.this, R.layout.filter_layout, userDistricts);
        listview.setTextFilterEnabled(true);
        listview.setAdapter(aadapter);

        TextView text = districtDia.findViewById(R.id.title_text);
        text.setText("வேலை தேட விரும்பும் மூன்று மாவட்டங்களை தேர்வுசெய்க");

        button = districtDia.findViewById(R.id.submitBtn);
        button.setAlpha(0.5F);
        button.setEnabled(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDistrict.isEmpty()) {
                    button.setAlpha(0.5F);
                    button.setEnabled(false);
                } else {
                    button.setEnabled(false);

                    pref.putString(SettingsActivity.this, U.SH_USER_DISTRICT_ID, "");
                    pref.putString(SettingsActivity.this, U.SH_USER_DISTRICT_NAME, "");

                    String filter;
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < selectedDistrict.size(); i++) {
                        sb.append(selectedDistrict.get(i)).append(",");
                    }
                    filter = sb.toString();
                    filter = filter.substring(0, filter.length() - 1);
                    Log.e("userLocation", "" + filter);

                    String filterName;
                    StringBuilder sb1 = new StringBuilder();
                    for (int i = 0; i < selectedDistrictName.size(); i++) {
                        sb1.append(selectedDistrictName.get(i)).append(",");
                    }
                    filterName = sb1.toString();
                    filterName = filterName.substring(0, filterName.length() - 1);
                    Log.e("userLocationName", "" + filterName);

                    distId = filter;
                    distName = filterName;

                    if (U.isNetworkAvailable(SettingsActivity.this)) {
                        loadDistricts("set_ditrict");

                        if (districtDia != null && districtDia.isShowing()) {
                            districtDia.dismiss();
                        }
                    } else {
                        L.t(SettingsActivity.this, U.INA);
                    }
                }
            }
        });

        if (!isFinishing()) {
            districtDia.show();
        }
    }

    public void setValues(String key, final FlexboxLayout fbl) {
        fbl.removeAllViews();
        String value = pref.getString(SettingsActivity.this, key);
        final String[] array = value.split(",");

        if (array.length > 0) {
            for (int i = 0; i < array.length; i++) {
                @SuppressLint("InflateParams") final View subchild = getLayoutInflater().inflate(R.layout.single_chip, null);
                final TextView tv = subchild.findViewById(R.id.txt_title);
                final ImageView remove = subchild.findViewById(R.id.remove);
                remove.setVisibility(View.GONE);
                tv.setId(i);
                tv.setText(array[i]);
                tv.setTextColor(ContextCompat.getColor(SettingsActivity.this, R.color.thick_blue));
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fbl.addView(subchild);
                    }
                });
            }
        }
    }

    public class SelectionAdapter extends ArrayAdapter {

        Context context;
        List<Item> list;
        LayoutInflater inflater;
        List<Item> orig;

        SelectionAdapter(Context context, int resource, List<Item> list) {
            super(context, resource, list);
            this.context = context;
            this.list = list;
            orig = new ArrayList<Item>();
            orig.addAll(list);
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_list_item, null);

            TextView txt = view.findViewById(R.id.name);
            final CheckBox checkBox = view.findViewById(R.id.checkbox);
            txt.setText(list.get(position).getItem());

            Log.e("districtSelection", selectedDistrict + " -->> " + list.get(position).getId());
            if (selectedDistrict.contains("" + list.get(position).getId())) {
                checkBox.setChecked(true);
                button.setAlpha(1F);
                button.setEnabled(true);
            } else {
                checkBox.setChecked(false);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                    } else {
                        checkBox.setChecked(true);
                    }
                }
            });

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (selectedDistrict.size() < 3) {
                            button.setAlpha(1F);
                            button.setEnabled(true);
                            selectedDistrict.add("" + list.get(position).getId());
                            selectedDistrictName.add("" + list.get(position).getItem());
                        } else {
                            Toast.makeText(context, "நீங்கள் ஏற்கனவே மூன்று மாவட்டங்களை தேர்வு செய்துவிட்டீர்கள்", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }
                    } else {
                        selectedDistrict.remove("" + list.get(position).getId());
                        selectedDistrictName.remove("" + list.get(position).getItem());
                        if (selectedDistrict.size() == 0) {
                            button.setAlpha(0.5F);
                            button.setEnabled(false);
                        }
                    }
                }
            });
            return view;
        }

    }
}
