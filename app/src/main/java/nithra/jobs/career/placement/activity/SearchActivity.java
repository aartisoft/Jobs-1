package nithra.jobs.career.placement.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.fragments.JobListFragment;
import nithra.jobs.career.placement.pojo.Item;
import nithra.jobs.career.placement.utills.CustomEditText;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

public class SearchActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1234;
    public static int searchEmptyCount = 0;
    CustomEditText edSearch;
    String search = "";
    Toolbar toolbar;
    ImageView voiceSearch, back;
    LinearLayout textSearch;
    Dialog match_text_dialog;
    ListView textlist, listview;
    ArrayList<String> matches_text;
    RelativeLayout container, listlay, searchHistoryLay;
    SharedPreference pref;
    TagSelectionAdapter itemsAdapter;
    List<Item> list;
    LinearLayout lError, recentSearchHistory;
    Button networkRetry;
    TextView txtError1;
    SpinKitView progressBar;
    SwipeRefreshLayout swipeContainer;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_adv_search);
        pref = new SharedPreference();
        list = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar);
        back = findViewById(R.id.back);

        edSearch = findViewById(R.id.edSearch);

        swipeContainer = findViewById(R.id.swipeContainer);
        recentSearchHistory = findViewById(R.id.recent_search_history);
        searchHistoryLay = findViewById(R.id.search_history_lay);

        showRecentSearches();

        listlay = findViewById(R.id.listlay);
        listlay.setVisibility(View.GONE);
        textSearch = findViewById(R.id.text_search);
        voiceSearch = findViewById(R.id.voice_search);
        container = findViewById(R.id.container);
        progressBar = findViewById(R.id.progressBar);
        lError = findViewById(R.id.lError);
        txtError1 = findViewById(R.id.txtError1);
        networkRetry = findViewById(R.id.network_retry);
        listview = findViewById(R.id.listview);
        itemsAdapter = new TagSelectionAdapter(SearchActivity.this, R.layout.activity_adv_search, list);
        listview.setTextFilterEnabled(true);
        listview.setAdapter(itemsAdapter);

        if (pref.getInt(this, U.SH_SEARCH_INFO_SHOW) == 0) {
            pref.putInt(this, U.SH_SEARCH_INFO_SHOW, 1);
            showInfoDialog();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(edSearch.getWindowToken(), 0);
            }
        }

        loadFirebseTags();
        swipeContainer.setColorSchemeResources(R.color.green, R.color.orange, R.color.lightblue);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                                @Override
                                                public void onRefresh() {
                                                    loadFirebseTags();
                                                    swipeContainer.setRefreshing(false);
                                                }
                                            }
        );

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (container.getVisibility() == View.VISIBLE) {
                    edSearch.setText("");
                    container.setVisibility(View.GONE);
                    listlay.setVisibility(View.GONE);
                    if (list.size() == 0) {
                        loadFirebseTags();
                    }
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(edSearch.getWindowToken(), 0);
                    }
                    finish();
                }
            }
        });

        textSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSearch(edSearch.getText().toString(), 0);
            }
        });

        networkRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFirebseTags();
            }
        });

        edSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
//                itemsAdapter.getFilter().filter(cs);
//                container.setVisibility(View.GONE);
                try {
                    if (cs.length() == 0) {
                        listlay.setVisibility(View.GONE);
                    } else {
                        listlay.setVisibility(View.VISIBLE);
                    }
                    itemsAdapter.filter("" + cs);
                    container.setVisibility(View.GONE);
                } catch (Exception e) {
                    Log.e("Error", "" + e);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        edSearch.setDrawableClickListener(new CustomEditText.DrawableClickListener() {

            public void onClick(DrawablePosition target) {
                switch (target) {
                    case RIGHT:
                        listlay.setVisibility(View.GONE);
                        edSearch.setText("");
                        container.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }

        });

        edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //do something
                    callSearch(edSearch.getText().toString(), 0);
                    return true;
                }
                return false;
            }
        });

        voiceSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (isConnected()) {
                        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                        startActivityForResult(intent, REQUEST_CODE);
                    } else {
                        Toast.makeText(getApplicationContext(), U.INA, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = null;
        if (cm != null) {
            net = cm.getActiveNetworkInfo();
        }
        return net != null && net.isAvailable() && net.isConnected();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            match_text_dialog = new Dialog(SearchActivity.this);
            match_text_dialog.setContentView(R.layout.dialog_matches_frag);
            match_text_dialog.setTitle("Select Matching Text");
            textlist = match_text_dialog.findViewById(R.id.list);
            matches_text = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, matches_text);
            textlist.setAdapter(adapter);
            textlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, JobListFragment.newInstance(U.ADVSEARCH, matches_text.get(position), "key"), "JobListFragment").commit();
                    edSearch.setText(matches_text.get(position));
                    edSearch.setSelection(matches_text.get(position).length());
                    match_text_dialog.dismiss();
                }
            });
            if (!SearchActivity.this.isFinishing()) {
                match_text_dialog.show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void showInfoDialog() {
        final Dialog dialog = new Dialog(SearchActivity.this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        dialog.setContentView(R.layout.info_popup);
        WebView infoText = dialog.findViewById(R.id.info_text);
        infoText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        String info = "<b><meta charset=\"utf-8\"><font color=purple size=2>\n" +
                getResources().getString(R.string.guide_search) + "<br><br></font></b>";

        U.webview(SearchActivity.this, info, infoText);

        dialog.findViewById(R.id.text_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void callSearch(String searchWord, int mode) {
        try {
            search = URLEncoder.encode(searchWord, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        listlay.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);
        String keyValue;
        if (mode == 1) {
            keyValue = "firebase_key";
        } else {
            keyValue = "key";
        }
        if (search.length() != 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, JobListFragment.newInstance(U.ADVSEARCH, search, keyValue), "JobListFragment").commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, JobListFragment.newInstance(U.ADVSEARCH, "", keyValue), "JobListFragment").commit();
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(edSearch.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (container.getVisibility() == View.VISIBLE) {
                edSearch.setText("");
                container.setVisibility(View.GONE);
                listlay.setVisibility(View.GONE);
                if (list.size() == 0) {
                    loadFirebseTags();
                }
            } else {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(edSearch.getWindowToken(), 0);
                }
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void loadFirebseTags() {
        // Get a reference to our posts
        if (U.isNetworkAvailable(SearchActivity.this)) {

            preLoading();

            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
            DatabaseReference ref = mDatabase.getReference();

            ref.child("key_word_db").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String prevChildKey) {
                    String title = (String) dataSnapshot.child("key_word").getValue();
                    String key = dataSnapshot.getKey();
                    Item item = new Item();
                    item.setItem(title);
                    item.setCount(key);
                    list.add(item);
                    itemsAdapter = new TagSelectionAdapter(SearchActivity.this, R.layout.activity_adv_search, list);
                    listview.setAdapter(itemsAdapter);
                    postLoading();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String prevChildKey) {
                    String title = (String) dataSnapshot.child("key_word").getValue();
                    Log.e("title", title);
                    String key = dataSnapshot.getKey();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getCount().equals(key)) {
                            Item item = new Item();
                            item.setItem(title);
                            item.setCount(key);
                            list.set(i, item);
                        }
                    }
                    itemsAdapter = new TagSelectionAdapter(SearchActivity.this, R.layout.activity_adv_search, list);
                    listview.setAdapter(itemsAdapter);
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    String title = (String) dataSnapshot.child("key_word").getValue();

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getItem().equals(title)) {
                            list.remove(i);
                        }
                    }
                    itemsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String prevChildKey) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    errorview(U.ERROR);
                }
            });

        } else {
            errorview(U.INA);
        }
    }

    public void preLoading() {
        itemsAdapter = new TagSelectionAdapter(SearchActivity.this, R.layout.activity_adv_search, list);
        listview.setAdapter(itemsAdapter);
        progressBar.setVisibility(View.VISIBLE);
        listview.setVisibility(View.VISIBLE);
        lError.setVisibility(View.GONE);
        swipeContainer.setEnabled(false);
    }

    public void postLoading() {
        progressBar.setVisibility(View.GONE);
        listview.setVisibility(View.VISIBLE);
        lError.setVisibility(View.GONE);
        swipeContainer.setEnabled(false);
    }

    public void errorview(String msg) {
        listview.setVisibility(View.GONE);
        lError.setVisibility(View.VISIBLE);
        swipeContainer.setEnabled(true);
        txtError1.setText(msg);
        progressBar.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    public void showRecentSearches() {
        if (!pref.getString(SearchActivity.this, U.SH_RECENT_SEARCH_KEYS).equals("")) {
            searchHistoryLay.setVisibility(View.VISIBLE);
            String ss = pref.getString(SearchActivity.this, U.SH_RECENT_SEARCH_KEYS);
            String[] categoryArray = ss.split(",");
            ArrayList<String> recentList = new ArrayList<>(Arrays.asList(categoryArray));
            for (int i = recentList.size(); i > 0; i--) {
                final TextView tv = new TextView(this);
                tv.setText(" " + recentList.get(i - 1).replace("+", " "));
                tv.setTextSize(15);
                tv.setPadding(5, 15, 5, 5);
                tv.setTextColor(getResources().getColor(R.color.black));
                tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_history), null, null, null);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edSearch.setText(tv.getText().toString().trim());
                        edSearch.setSelection(tv.getText().toString().trim().length());
                        textSearch.performClick();
                    }
                });
                recentSearchHistory.addView(tv);
            }
        } else {
            searchHistoryLay.setVisibility(View.GONE);
        }
    }

    public class TagSelectionAdapter extends ArrayAdapter {

        Context context;
        List<Item> list;
        LayoutInflater inflater;
        List<Item> orig;

        TagSelectionAdapter(Context context, int resource, List<Item> list) {
            super(context, resource, list);
            this.context = context;
            this.list = list;
            orig = new ArrayList<>();
            orig.addAll(list);
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_list_item, null);

            TextView txt = view.findViewById(R.id.name);
            txt.setText(list.get(list.size() - 1 - position).getItem());
            CheckBox checkBox = view.findViewById(R.id.checkbox);
            checkBox.setVisibility(View.GONE);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String value = list.get(list.size() - 1 - position).getItem();
                    edSearch.setText(value);
                    edSearch.setSelection(value.length());
                    callSearch(value, 1);
                }
            });

            return view;
        }

        public void filter(String charText) {

            charText = charText.toLowerCase(Locale.getDefault());
            list.clear();
            if (charText.length() == 0) {
                list.addAll(orig);
            } else {
                for (Item postDetail : orig) {
                    if (postDetail.getItem().toLowerCase(Locale.getDefault()).contains(charText)) {
                        list.add(postDetail);
                    } else if (postDetail.getItem().toLowerCase(Locale.getDefault()).contains(charText)) {
                        list.add(postDetail);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
}
