package nithra.jobs.career.placement.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nithra.jobs.career.placement.MainActivity;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.networking.MySingleton;
import nithra.jobs.career.placement.pojo.Item;
import nithra.jobs.career.placement.utills.FlexboxLayout;
import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.U;

/**
 * Created by nithra on 14/2/18.
 */

public class DistrictWiseFragment extends Fragment {

    public static FlexboxLayout fblLay;
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout fragHolder, ads;
    public static List<Item> districtList;
    LinearLayout lError;
    TextView txtError1;
    SpinKitView progressBar;
    SwipeRefreshLayout swipeContainer;
    Button networkRetry;

    public DistrictWiseFragment() {
        // Required empty public constructor
    }

    public static DistrictWiseFragment newInstance() {
        return new DistrictWiseFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.category_lay, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        districtList = new ArrayList<>();
        fblLay = view.findViewById(R.id.fbl);
        fragHolder = view.findViewById(R.id.fragment_container);
        ads = view.findViewById(R.id.ads);
        progressBar = view.findViewById(R.id.progressBar);
        lError = view.findViewById(R.id.lError);
        txtError1 = view.findViewById(R.id.txtError1);
        swipeContainer = view.findViewById(R.id.swipeContainer);
        networkRetry = view.findViewById(R.id.network_retry);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity.homePagePosition = U.DISTRICT_PAGE;
        ads.setVisibility(View.VISIBLE);
        MainActivity.showAd(getActivity(), ads, false);
        load();

        swipeContainer.setColorSchemeResources(R.color.green, R.color.orange, R.color.lightblue);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                                @Override
                                                public void onRefresh() {
                                                    load();
                                                    swipeContainer.setRefreshing(false);
                                                }
                                            }
        );
        networkRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
            }
        });
    }

    public void load() {
        if (U.isNetworkAvailable(getActivity())) {
            preLoading();
            loadJSON(SU.GETJOBSDIST);
        } else {
            errorView(U.INA);
        }
    }

    private void loadJSON(final String param) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSON(response);
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
                Map<String, String> params = new HashMap<>();
                params.put("action", param);
                return params;
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQue(stringRequest);
    }

    private void showJSON(String json) {
        JSONObject jsonObject;
        JSONArray jsonArray;
        districtList.clear();
        try {
            jsonObject = new JSONObject(json);
            jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                Item catItem = new Item();
                catItem.setId(i);
                catItem.setItem(jo.getString("district"));
                catItem.setCount(jo.getString("count"));
                districtList.add(catItem);
            }
            setCategory(districtList, fblLay);
            progressBar.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
            errorView(U.INA);
        }
    }

    @SuppressLint("SetTextI18n")
    public void setCategory(List<Item> list, final FlexboxLayout fbl) {
        fbl.setVisibility(View.VISIBLE);
        fbl.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            if (getActivity() != null) {
                @SuppressLint("InflateParams") final View subchild = getActivity().getLayoutInflater().inflate(R.layout.single_chip, null);
                final TextView tv = subchild.findViewById(R.id.txt_title);
                final ImageView remove = subchild.findViewById(R.id.remove);
                remove.setVisibility(View.GONE);
                tv.setId(list.get(i).getId());
                tv.setText(list.get(i).getItem() + "  " + "(" + list.get(i).getCount() + ")");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fbl.addView(subchild);
                    }
                });
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fbl.setVisibility(View.GONE);
                        fragHolder.setVisibility(View.VISIBLE);
                        ads.setVisibility(View.GONE);
                        String distStr = "";
                        try {
                            distStr = URLEncoder.encode(districtList.get(tv.getId()).getItem(), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction;
                        if (fragmentManager != null) {
                            fragmentTransaction = fragmentManager.beginTransaction();
                            JobListFragment cat = JobListFragment.newInstance(SU.DISTRICTWISE, "", "" + distStr,
                                    "", "", "", "", "", "", "1");
                            if (fragmentTransaction != null) {
                                fragmentTransaction.replace(R.id.fragment_container, cat, "HELLO");
                                fragmentTransaction.commit();
                            }

                        }
                    }
                });
            }
        }
    }

    private void preLoading() {
        lError.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void errorView(String msg1) {
        fragHolder.setVisibility(View.GONE);
        fblLay.setVisibility(View.GONE);
        txtError1.setText(msg1);
        lError.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void errorHandling(VolleyError error) {
        String e;
        if (error instanceof TimeoutError)
            e = "Request TimeOut";
        else if (error instanceof AuthFailureError)
            e = "AuthFailureError";
        else if (error instanceof ServerError)
            e = "ServerError";
        else if (error instanceof NetworkError)
            e = U.INA;
        else if (error instanceof ParseError)
            e = U.ERROR;
        else
            e = U.ERROR;
        errorView(e);
    }

}
