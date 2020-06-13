package nithra.jobs.career.placement.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.networking.MySingleton;
import nithra.jobs.career.placement.utills.L;
import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

public class Event_List extends AppCompatActivity {
    ImageView back, filter;
    TextView txtcat, txtorder, txtError1;
    ListView mRecyclerView;
    LinearLayout lProgress, lError;
    ArrayList<HashMap<String, Object>> event_list;
    ArrayList<HashMap<String, Object>> cat_list;
    int preLast;
    Button network_retry;
    int stop_load = 0, end1 = 0;
    int first_size = 0;
    int Last;
    int preLast_id = 0;
    int last_size = 0;
    int order_by = 1;
    int cat_id = 0;
    int media_id = 0;
    CustomListAdapter adapter;
    FilterListAdapter filter_adapter;
    LayoutInflater inflater;
    SharedPreference sp = new SharedPreference();
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_event_list);
        back = findViewById(R.id.back);
        filter = findViewById(R.id.filter);
        txtcat = findViewById(R.id.txtcat);
        txtorder = findViewById(R.id.txtorder);
        lProgress = findViewById(R.id.lProgress);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        lError = findViewById(R.id.lError);
        network_retry = findViewById(R.id.network_retry);
        txtError1 = findViewById(R.id.txtError1);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        event_list = new ArrayList<HashMap<String, Object>>();
        cat_list = new ArrayList<HashMap<String, Object>>();

        adapter = new CustomListAdapter(this, event_list);
        mRecyclerView.setAdapter(adapter);

        checkPromoStatus();
        network_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (U.isNetworkAvailable(Event_List.this)) {
                    checkPromoStatus();
                } else {
                    txtError1.setText(U.INA);
                    lError.setVisibility(View.VISIBLE);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_info("பிரிவுகள்");
            }
        });

        txtorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_info("வரிசையாக");
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_info("Media Type");
            }
        });
    }

    private void CategoryStatus(int type) {
        U.mProgress(Event_List.this, "Loading...", false).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.EVENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("showresponse_emp", "" + response);
                        JSONArray jArray = null;
                        try {
                            JSONObject jsonObject = null;
                            jArray = new JSONArray(response);
                            JSONObject json_data = null;

                            cat_list.clear();
                            if (jArray.length() > 0) {
                                json_data = jArray.getJSONObject(0);
                                String strr = response.replaceAll("\"", "");

                                if (strr.contains("status:no data")) {

                                } else {

                                    HashMap<String, Object> state1 = new HashMap<String, Object>();
                                    state1.put("id", "0");
                                    if (type == 1) {
                                        state1.put("category_name", "All Categories");
                                    } else {
                                        state1.put("category_name", "No Filter");
                                    }

                                    cat_list.add(state1);
                                    for (int i = 0; i < jArray.length(); i++) {

                                        json_data = jArray.getJSONObject(i);
                                        HashMap<String, Object> state = new HashMap<String, Object>();
                                        state.put("id", json_data.getString("id"));
                                        state.put("category_name", json_data.getString("category_name"));
                                        cat_list.add(state);
                                    }
                                }
                            }

                            U.mProgress.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            U.mProgress.dismiss();
                            L.t(Event_List.this, U.ERROR);
                            finish();
                        }


                        filter_adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorHandling(error);
                        U.mProgress.dismiss();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (type == 1) {
                    params.put("action", "get_cat");
                } else {
                    params.put("action", "get_media");
                }


                Log.e("paramsresponse", "" + params);
                return params;
            }
        };

        MySingleton.getInstance(Event_List.this).addToRequestQue(stringRequest);

    }


    private void PayStatus() {
        U.mProgress(Event_List.this, "Loading...", false).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.EVENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("showresponse_emp", "" + response);
                        JSONArray jArray = null;
                        try {
                            JSONObject jsonObject = null;
                            jArray = new JSONArray(response);
                            JSONObject json_data = null;

                            cat_list.clear();
                            if (jArray.length() > 0) {
                                json_data = jArray.getJSONObject(0);
                                String strr = response.replaceAll("\"", "");

                                if (strr.contains("status:no data")) {

                                } else {
                                    HashMap<String, Object> state1 = new HashMap<String, Object>();
                                    state1.put("id", "0");
                                    state1.put("category_name", "All Categories");
                                    cat_list.add(state1);
                                    for (int i = 0; i < jArray.length(); i++) {

                                        json_data = jArray.getJSONObject(i);
                                        HashMap<String, Object> state = new HashMap<String, Object>();
                                        state.put("id", json_data.getString("id"));
                                        state.put("category_name", json_data.getString("category_name"));
                                        cat_list.add(state);
                                    }
                                }
                            }

                            U.mProgress.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            U.mProgress.dismiss();
                            L.t(Event_List.this, U.ERROR);
                            finish();
                        }


                        filter_adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorHandling(error);
                        U.mProgress.dismiss();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "get_cat");

                Log.e("paramsresponse", "" + params);
                return params;
            }
        };

        MySingleton.getInstance(Event_List.this).addToRequestQue(stringRequest);

    }

    private void checkPromoStatus() {
        U.mProgress(Event_List.this, "Loading...", false).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.EVENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("showresponse_emp", "" + response);
                        JSONArray jArray = null;
                        try {
                            JSONObject jsonObject = null;
                            jArray = new JSONArray(response);
                            JSONObject json_data = null;

                            event_list.clear();
                            if (jArray.length() > 0) {
                                json_data = jArray.getJSONObject(0);
                                String strr = response.replaceAll("\"", "");

                                if (strr.contains("status:no data")) {
                                    end1 = 1;
                                } else {
                                    end1 = 0;
                                    for (int i = 0; i < jArray.length(); i++) {

                                        json_data = jArray.getJSONObject(i);
                                        if (event_list.size() != 0) {
                                            if (!event_list.get((event_list.size() - 1)).get("id").toString().equals("" + json_data.getString("id"))) {

                                                HashMap<String, Object> state = new HashMap<String, Object>();
                                                state.put("id", json_data.getString("id"));
                                                state.put("cat_id", json_data.getString("cat_id"));
                                                state.put("media_id", json_data.getString("media_id"));
                                                state.put("ename", json_data.getString("ename"));
                                                state.put("oname", json_data.getString("oname"));
                                                state.put("sname", json_data.getString("sname"));
                                                state.put("pay", json_data.getString("pay"));
                                                state.put("images", json_data.getString("images"));
                                                state.put("event_link", json_data.getString("event_link"));
                                                state.put("others", json_data.getString("others"));
                                                state.put("edate", json_data.getString("edate"));
                                                state.put("time", json_data.getString("time"));
                                                state.put("is_delete", json_data.getString("is_delete"));
                                                state.put("insta_url", json_data.getString("insta_url"));
                                                state.put("is_dnd", json_data.getString("is_dnd"));
                                                state.put("is_active", json_data.getString("is_active"));
                                                state.put("is_verfiy", json_data.getString("is_verfiy"));
                                                state.put("cip", json_data.getString("cip"));
                                                state.put("cby", json_data.getString("cby"));
                                                state.put("cdate", json_data.getString("cdate"));
                                                state.put("mip", json_data.getString("mip"));
                                                state.put("mdate", json_data.getString("mdate"));
                                                state.put("mby", json_data.getString("mby"));
                                                state.put("media_name", json_data.getString("media_name"));
                                                state.put("cat_name", json_data.getString("cat_name"));
                                                event_list.add(state);
                                            }
                                        } else {
                                            HashMap<String, Object> state = new HashMap<String, Object>();
                                            state.put("id", json_data.getString("id"));
                                            state.put("cat_id", json_data.getString("cat_id"));
                                            state.put("media_id", json_data.getString("media_id"));
                                            state.put("ename", json_data.getString("ename"));
                                            state.put("oname", json_data.getString("oname"));
                                            state.put("sname", json_data.getString("sname"));
                                            state.put("pay", json_data.getString("pay"));
                                            state.put("images", json_data.getString("images"));
                                            state.put("event_link", json_data.getString("event_link"));
                                            state.put("others", json_data.getString("others"));
                                            state.put("edate", json_data.getString("edate"));
                                            state.put("time", json_data.getString("time"));
                                            state.put("is_delete", json_data.getString("is_delete"));
                                            state.put("insta_url", json_data.getString("insta_url"));
                                            state.put("is_dnd", json_data.getString("is_dnd"));
                                            state.put("is_active", json_data.getString("is_active"));
                                            state.put("is_verfiy", json_data.getString("is_verfiy"));
                                            state.put("cip", json_data.getString("cip"));
                                            state.put("cby", json_data.getString("cby"));
                                            state.put("cdate", json_data.getString("cdate"));
                                            state.put("mip", json_data.getString("mip"));
                                            state.put("mdate", json_data.getString("mdate"));
                                            state.put("mby", json_data.getString("mby"));
                                            state.put("media_name", json_data.getString("media_name"));
                                            state.put("cat_name", json_data.getString("cat_name"));
                                            event_list.add(state);
                                        }


                                    }
                                    preLast_id = event_list.size();
                                }
                            }

                            U.mProgress.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            U.mProgress.dismiss();
                            L.t(Event_List.this, U.ERROR);
                            finish();
                        }

                        if (end1 == 0) {

                            if (event_list.size() > 4) {
                                if (jArray != null) {
                                    preLast = preLast + jArray.length();
                                }
                                adapter.notifyDataSetChanged();

                                mRecyclerView.setOnScrollListener(new scrollListener());
                            } else {
                                adapter.notifyDataSetChanged();
                            }
                        }


                        if (event_list.size() == 0) {
                            lError.setVisibility(View.VISIBLE);
                        } else {
                            lError.setVisibility(View.GONE);
                        }

                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorHandling(error);
                        U.mProgress.dismiss();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", SU.GET_EVENT);
                params.put("limit", "" + preLast_id);
                params.put("cat_id", "" + cat_id);
                params.put("media_id", "" + media_id);
                params.put("order_by", "" + order_by);
                Log.e("paramsresponse", "" + params);
                return params;
            }
        };

        MySingleton.getInstance(Event_List.this).addToRequestQue(stringRequest);

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
        L.t(Event_List.this, e);
    }

    private void load_filtter_below() {
        U.mProgress(Event_List.this, "Loading...", false).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.EVENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("showresponse_emp", "" + response);
                        JSONArray jArray = null;
                        try {
                            JSONObject jsonObject = null;
                            jArray = new JSONArray(response);
                            JSONObject json_data = null;

                            if (jArray.length() > 0) {
                                json_data = jArray.getJSONObject(0);
                                String strr = response.replaceAll("\"", "");

                                if (strr.contains("status:no data")) {
                                    end1 = 1;
                                } else {
                                    end1 = 0;
                                    for (int i = 0; i < jArray.length(); i++) {

                                        json_data = jArray.getJSONObject(i);
                                        if (event_list.size() != 0) {
                                            if (!event_list.get((event_list.size() - 1)).get("id").toString().equals("" + json_data.getString("id"))) {

                                                HashMap<String, Object> state = new HashMap<String, Object>();
                                                state.put("id", json_data.getString("id"));
                                                state.put("cat_id", json_data.getString("cat_id"));
                                                state.put("media_id", json_data.getString("media_id"));
                                                state.put("ename", json_data.getString("ename"));
                                                state.put("oname", json_data.getString("oname"));
                                                state.put("sname", json_data.getString("sname"));
                                                state.put("pay", json_data.getString("pay"));
                                                state.put("images", json_data.getString("images"));
                                                state.put("event_link", json_data.getString("event_link"));
                                                state.put("others", json_data.getString("others"));
                                                state.put("edate", json_data.getString("edate"));
                                                state.put("time", json_data.getString("time"));
                                                state.put("is_delete", json_data.getString("is_delete"));
                                                state.put("insta_url", json_data.getString("insta_url"));
                                                state.put("is_dnd", json_data.getString("is_dnd"));
                                                state.put("is_active", json_data.getString("is_active"));
                                                state.put("is_verfiy", json_data.getString("is_verfiy"));
                                                state.put("cip", json_data.getString("cip"));
                                                state.put("cby", json_data.getString("cby"));
                                                state.put("cdate", json_data.getString("cdate"));
                                                state.put("mip", json_data.getString("mip"));
                                                state.put("mdate", json_data.getString("mdate"));
                                                state.put("mby", json_data.getString("mby"));
                                                state.put("media_name", json_data.getString("media_name"));
                                                state.put("cat_name", json_data.getString("cat_name"));
                                                event_list.add(state);
                                            }
                                        } else {
                                            HashMap<String, Object> state = new HashMap<String, Object>();
                                            state.put("id", json_data.getString("id"));
                                            state.put("cat_id", json_data.getString("cat_id"));
                                            state.put("media_id", json_data.getString("media_id"));
                                            state.put("ename", json_data.getString("ename"));
                                            state.put("oname", json_data.getString("oname"));
                                            state.put("sname", json_data.getString("sname"));
                                            state.put("pay", json_data.getString("pay"));
                                            state.put("images", json_data.getString("images"));
                                            state.put("event_link", json_data.getString("event_link"));
                                            state.put("others", json_data.getString("others"));
                                            state.put("edate", json_data.getString("edate"));
                                            state.put("time", json_data.getString("time"));
                                            state.put("is_delete", json_data.getString("is_delete"));
                                            state.put("insta_url", json_data.getString("insta_url"));
                                            state.put("is_dnd", json_data.getString("is_dnd"));
                                            state.put("is_active", json_data.getString("is_active"));
                                            state.put("is_verfiy", json_data.getString("is_verfiy"));
                                            state.put("cip", json_data.getString("cip"));
                                            state.put("cby", json_data.getString("cby"));
                                            state.put("cdate", json_data.getString("cdate"));
                                            state.put("mip", json_data.getString("mip"));
                                            state.put("mdate", json_data.getString("mdate"));
                                            state.put("mby", json_data.getString("mby"));
                                            state.put("media_name", json_data.getString("media_name"));
                                            state.put("cat_name", json_data.getString("cat_name"));
                                            event_list.add(state);
                                        }


                                    }
                                    preLast_id = event_list.size();
                                }
                            }

                            U.mProgress.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            U.mProgress.dismiss();
                            L.t(Event_List.this, U.ERROR);
                            finish();
                        }
                        last_size = event_list.size();
                        if (event_list.size() > 4) {
                            if (first_size == last_size) {
                                stop_load = 1;
                            }
                        }
                        if (end1 == 0) {

                            adapter.notifyDataSetChanged();
                            if (jArray != null) {
                                preLast = preLast + jArray.length();
                                if (jArray.length() == 5) {
                                    mRecyclerView.setOnScrollListener(new scrollListener());
                                }
                            }


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorHandling(error);
                        U.mProgress.dismiss();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", SU.GET_EVENT);
                params.put("limit", "" + preLast_id);
                params.put("cat_id", "" + cat_id);
                params.put("media_id", "" + media_id);
                params.put("order_by", "" + order_by);

                Log.e("paramsresponse", "" + params);
                return params;
            }
        };

        MySingleton.getInstance(Event_List.this).addToRequestQue(stringRequest);

    }

    private String convertTime(String time) {

        String[] separated = time.split(":");
        int HOUR = Integer.parseInt(separated[0]);
        String MIN = separated[1];
        String AM_PM = "AM";
        if (HOUR >= 12) {
            HOUR = HOUR - 12;
            AM_PM = "PM";
        } else {
            AM_PM = "AM";

        }
        if (HOUR == 0) {
            HOUR = 12;
        }
        String SOUND = String.valueOf(HOUR);
        if (String.valueOf(HOUR).length() == 1) {
            SOUND = ("0" + HOUR);
        } else {
            SOUND = String.valueOf(HOUR);
        }
        return SOUND + ":" + MIN + " " + AM_PM;
    }

    public void Filter_info(String title) {
        final Dialog no_datefun = new Dialog(Event_List.this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
        no_datefun.setContentView(R.layout.dialog_event_filter);
        no_datefun.setCancelable(true);
        no_datefun.setCanceledOnTouchOutside(false);

        TextView cat_type_txt = no_datefun.findViewById(R.id.cat_type_txt);
        ListView sort_list = no_datefun.findViewById(R.id.sort_list);

        cat_type_txt.setText("" + title);
        if (title.equals("பிரிவுகள்")) {
            CategoryStatus(1);
        } else if (title.equals("Media Type")) {
            CategoryStatus(2);
        } else {
            cat_list.clear();
            HashMap<String, Object> state = new HashMap<String, Object>();
            state.put("id", "0");
            state.put("category_name", "தேதி ஏறுவரிசையில்");
            cat_list.add(state);
            state.put("id", "1");
            state.put("category_name", "தேதி இறங்குவரிசையில்");

            cat_list.add(state);
        }

        filter_adapter = new FilterListAdapter(Event_List.this, cat_list);
        sort_list.setAdapter(filter_adapter);

        sort_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                stop_load = 0;
                end1 = 0;
                first_size = 0;
                preLast_id = 0;
                last_size = 0;

                if (title.equals("பிரிவுகள்")) {
                    cat_id = Integer.parseInt(cat_list.get(position).get("id").toString());

                } else if (title.equals("Media Type")) {
                    media_id = Integer.parseInt(cat_list.get(position).get("id").toString());

                } else {

                    order_by = Integer.parseInt(cat_list.get(position).get("id").toString());
                }

                checkPromoStatus();
                no_datefun.dismiss();
            }
        });

        no_datefun.show();


    }

    public class scrollListener implements AbsListView.OnScrollListener {

        public scrollListener() {
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            if (preLast != 0) {
                if (preLast > Last) {

                    if ((firstVisibleItem) == (totalItemCount - (visibleItemCount))) {
                        Last = preLast;
                        System.out.println("filter_val - " + "last_id" + " : " + "" + preLast_id);


                        if (stop_load == 0) {
                            load_filtter_below();
                        }
                        first_size = event_list.size();

                    }

                }
            }

        }
    }

    public class CustomListAdapter extends BaseAdapter {

        ArrayList<HashMap<String, Object>> players;


        public CustomListAdapter(Context context,
                                 ArrayList<HashMap<String, Object>> players) {
            this.players = players;
        }

        @Override
        public int getCount() {
            return players.size();
        }

        @Override
        public Object getItem(int location) {
            return players.get(location);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {

            if (inflater == null)
                inflater = (LayoutInflater) Event_List.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (view == null)
                view = inflater.inflate(R.layout.event_view_item, null);

            TextView event_name = view.findViewById(R.id.event_name);
            TextView common_edit_txt = view.findViewById(R.id.common_view_txt);
            TextView org_name_txt = view.findViewById(R.id.org_name_txt);
            TextView specker_name_txt = view.findViewById(R.id.specker_name_txt);
            TextView category_name_txt = view.findViewById(R.id.category_name_txt);
            TextView event_time_txt = view.findViewById(R.id.event_time_txt);
            TextView event_date_txt = view.findViewById(R.id.event_date_txt);
            TextView amt_type_txt = view.findViewById(R.id.amt_type_txt);
            ImageView logo_img = view.findViewById(R.id.logo_img);

            event_name.setText("" + players.get(position).get("ename").toString());
            org_name_txt.setText("" + players.get(position).get("oname").toString());
            specker_name_txt.setText("" + players.get(position).get("sname").toString());
            category_name_txt.setText("" + players.get(position).get("cat_name").toString());
            event_time_txt.setText("" + convertTime(players.get(position).get("time").toString()));
            event_date_txt.setText("" + U.timeToDateFormate(players.get(position).get("edate").toString()));
            if (players.get(position).get("pay").toString().equals("1")) {
                amt_type_txt.setText("PAID");
                amt_type_txt.setBackgroundResource(R.drawable.home_design);
            } else {
                amt_type_txt.setText("FREE");
                amt_type_txt.setBackgroundResource(R.drawable.home_design_green);
            }


            // height_name_txt.setText("" + players.get(position).get("gov_con").toString());
            //added_name_txt.setText("" + players.get(position).get("cby").toString());

            Glide.with(Event_List.this)
                    .load(players.get(position).get("images").toString())
                    .asBitmap()
                    .placeholder(R.drawable.ic_male)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(logo_img);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sp.putString(Event_List.this, "Event_name", "" + players.get(position).get("ename").toString());
                    sp.putString(Event_List.this, "Event_img", "" + players.get(position).get("images").toString());
                    sp.putString(Event_List.this, "Event_organizer", "" + players.get(position).get("oname").toString());
                    sp.putString(Event_List.this, "Event_speaker", "" + players.get(position).get("sname").toString());
                    sp.putString(Event_List.this, "Event_category", "" + players.get(position).get("cat_name").toString());
                    sp.putString(Event_List.this, "Event_media", "" + players.get(position).get("media_name").toString());
                    sp.putString(Event_List.this, "Event_link", "" + players.get(position).get("event_link").toString());
                    sp.putString(Event_List.this, "Event_date", "" + U.timeToDateFormate(players.get(position).get("edate").toString()));
                    sp.putString(Event_List.this, "Event_time", "" + players.get(position).get("time").toString());
                    sp.putString(Event_List.this, "Event_pay", "" + players.get(position).get("pay").toString());
                    sp.putString(Event_List.this, "Event_other", "" + players.get(position).get("others").toString());
                    startActivity(new Intent(Event_List.this, View_Event.class));
                }
            });
            return view;
        }

    }

    public class FilterListAdapter extends BaseAdapter {

        ArrayList<HashMap<String, Object>> players;


        public FilterListAdapter(Context context,
                                 ArrayList<HashMap<String, Object>> players) {
            this.players = players;
        }

        @Override
        public int getCount() {
            return players.size();
        }

        @Override
        public Object getItem(int location) {
            return players.get(location);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {

            if (inflater == null)
                inflater = (LayoutInflater) Event_List.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (view == null)
                view = inflater.inflate(R.layout.filter_view_item, null);

            TextView event_name = view.findViewById(R.id.txt_filter);


            event_name.setText("" + players.get(position).get("category_name").toString());
            return view;
        }

    }
}
