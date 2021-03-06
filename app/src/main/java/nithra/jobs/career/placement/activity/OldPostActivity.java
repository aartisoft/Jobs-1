package nithra.jobs.career.placement.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import nithra.jobs.career.placement.MainActivity;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.networking.MySingleton;
import nithra.jobs.career.placement.pojo.Item;
import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

/*
 * Created by nithra on 16/2/18.
 */

public class OldPostActivity extends AppCompatActivity {

    List<Item> oldPostList;
    ListView list;
    int preLast;
    OldPostAdapter adapter;
    String message;
    String title;
    ImageView closeBtn, back;
    SpinKitView progressBar;
    LinearLayout lError, adLayout;
    Button networkRetry;
    TextView txtError1, titleTxt;
    SharedPreference pref;
    LinearLayout parentLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.listview);
        pref = new SharedPreference();

        oldPostList = new ArrayList<>();
        list = findViewById(R.id.listview);
        back = findViewById(R.id.back);
        progressBar = findViewById(R.id.progressBar);
        lError = findViewById(R.id.lError);
        txtError1 = findViewById(R.id.txtError1);
        closeBtn = findViewById(R.id.close_btn);
        adLayout = findViewById(R.id.adLayout);
        titleTxt = findViewById(R.id.title);
        networkRetry = findViewById(R.id.network_retry);

        titleTxt.setText(getResources().getString(R.string.oldpost));

        load();

        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                final int lastItem = firstVisibleItem + visibleItemCount;
                if (lastItem == totalItemCount) {
                    if (preLast != lastItem) {
                        preLast = lastItem; //to avoid multiple calls for last item
                        if (U.isNetworkAvailable(OldPostActivity.this)) {
                            loadJSON("0", "" + oldPostList.get(oldPostList.size() - 1).getId());
                            adapter.notifyDataSetChanged();
                        } else {
                            preLast = 0;
                            Toast.makeText(OldPostActivity.this, U.INA, Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        networkRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
            }
        });
    }

    private void preLoading() {
        lError.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void errorView(String msg1) {
        txtError1.setText(msg1);
        lError.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
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
        errorView(e);
    }

    private void loadJSON(final String flag, final String uid) {
        String url = null;
        if (flag.equals("0")) {
            url = SU.OLD_POST_URL;
        } else if (flag.equals("1")) {
            url = SU.OLD_POST_SINGLE_URL;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSON(uid, flag, response);
                        Log.e("response", "" + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorHandling(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (flag.equals("0")) {
                    params.put("app", "nj");
                    params.put("uid", uid);
                } else if (flag.equals("1")) {
                    params.put("uid", uid);
                }
                return params;
            }
        };

        MySingleton.getInstance(OldPostActivity.this).addToRequestQue(stringRequest);
    }

    private void showJSON(String uid, String flag, String json) {
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        if (flag.equals("0")) {
            try {
                jsonArray = new JSONArray(json);
                System.out.println("Update===" + json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    Item movie = new Item();
                    String str = "";
                    try {
                        str = URLDecoder.decode(jsonObject.getString("title"), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    movie.setItem(str);
                    movie.setId(jsonObject.getInt("uid"));
                    oldPostList.add(movie);
                }
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
                errorView(U.ERROR);
            }
        } else if (flag.equals("1")) {
            try {
                jsonArray = new JSONArray(json);
                System.out.println("Update===" + json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    message = jsonObject.getString("des");
                }
                for (int i = 0; i < oldPostList.size(); i++) {
                    if (oldPostList.get(i).getId() == Integer.parseInt(uid)) {
                        title = oldPostList.get(i).getItem();
                    }
                }
                Intent intent = new Intent(OldPostActivity.this, DetailActivity.class);
                intent.putExtra("message", message);
                intent.putExtra("title", title);
                intent.putExtra("url", "");
                intent.putExtra("idd", uid);
                intent.putExtra("Noti_add", 1);
                startActivity(intent);
                parentLay.setEnabled(true);

            } catch (JSONException e) {
                e.printStackTrace();
                errorView(U.ERROR);
            }
        }
    }

    public void load() {
        preLoading();
        loadJSON("0", "0");
        if (pref.getInt(OldPostActivity.this, U.SH_AD_PURCHASED) == 0) {
            MainActivity.showAd(OldPostActivity.this, adLayout, true);
        }

        adapter = new OldPostAdapter(OldPostActivity.this, R.layout.filter_layout, oldPostList);
        list.setAdapter(adapter);
    }

    public class OldPostAdapter extends ArrayAdapter {

        public TextView title, number;
        Context context;
        List<Item> list;
        LayoutInflater inflater;

        public OldPostAdapter(Context context, int resource, List<Item> list) {
            super(context, resource, list);
            this.context = context;
            this.list = list;
        }

        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.oldpost_list_row, null);
            title = view.findViewById(R.id.title);
            number = view.findViewById(R.id.number);
            parentLay = view.findViewById(R.id.parentLay);
            int pos = position + 1;
            number.setText("" + pos);
            GradientDrawable bgShape = (GradientDrawable) number.getBackground();
            bgShape.setColor(context.getResources().getColor(R.color.skyblue_thick));
            title.setText(list.get(position).getItem());
            parentLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    parentLay.setEnabled(false);
                    if (U.isNetworkAvailable(OldPostActivity.this)) {
                        loadJSON("1", "" + oldPostList.get(position).getId());
                    } else {
                        Toast.makeText(OldPostActivity.this, U.INA, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return view;
        }

    }
}
