package nithra.jobs.career.placement.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;

import java.util.ArrayList;
import java.util.List;

import nithra.jobs.career.placement.MainActivity;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.ST_Activity;
import nithra.jobs.career.placement.adapters.MessagesAdapter;
import nithra.jobs.career.placement.pojo.Notifications;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;


public class NotificationActivity extends AppCompatActivity implements MessagesAdapter.MessageAdapterListener {

    List<Notifications> messages;
    SQLiteDatabase myDB;
    RecyclerView mRecyclerView;
    ActionModeCallback actionModeCallback;
    MessagesAdapter mAdapter;
    ActionMode actionMode;
    LinearLayout dnf, adLayout;
    SharedPreference pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_notification);
        loadNotificaion();
        pref = new SharedPreference();
        pref.putInt(this, "nactivity", 1);

        createView();
    }

    private void createView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        actionModeCallback = new ActionModeCallback();

        dnf = findViewById(R.id.dnf);
        adLayout = findViewById(R.id.adLayout);
        MainActivity.showAd(this, adLayout, true);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        if (messages != null) {
            if (messages.size() != 0) {
                mAdapter = new MessagesAdapter(this, messages, this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
                mRecyclerView.setAdapter(mAdapter);
            } else {
                dnf.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        }
    }

    private void loadNotificaion() {

        messages = new ArrayList<Notifications>();

        myDB = openOrCreateDatabase("myDB", 0, null);
        Cursor c = myDB.rawQuery("select * from noti_cal ORDER BY id DESC", null);

        if (c.moveToFirst()) {
            do {
                int id, isclose, isshow;
                String title, message, date, time, type, bm, ntype, url;

                id = c.getInt(c.getColumnIndex("id"));
                title = c.getString(c.getColumnIndex("title"));
                message = c.getString(c.getColumnIndex("message"));
                date = c.getString(c.getColumnIndex("date"));
                time = c.getString(c.getColumnIndex("time"));
                isshow = c.getInt(c.getColumnIndex("isshow"));
                isclose = c.getInt(c.getColumnIndex("isclose"));
                type = c.getString(c.getColumnIndex("type"));
                bm = c.getString(c.getColumnIndex("bm"));
                ntype = c.getString(c.getColumnIndex("ntype"));
                url = c.getString(c.getColumnIndex("url"));

                Notifications notifications = new Notifications();
                notifications.setId(id);
                notifications.setTitle(title);
                notifications.setMessage(message);
                notifications.setDate(date);
                notifications.setTime(time);
                notifications.setIsshow(isshow);
                notifications.setIsclose(isclose);
                notifications.setType(type);
                notifications.setBm(bm);
                notifications.setNtype(ntype);
                notifications.setUrl(url);
                messages.add(notifications);

            } while (c.moveToNext());
        }
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

    @Override
    public void onIconClicked(int position) {
        // toggleSelection(position);
    }

    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    @Override
    public void onIconImportantClicked(int position) {
        Notifications message = messages.get(position);
        message.setIsclose(message.getIsshow() == 0 ? 1 : 0);
        messages.set(position, message);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMessageRowClicked(int position) {
        if (mAdapter.getSelectedItemCount() > 0) {
            enableActionMode(position);
        } else {
            // read the message which removes bold from the row

            Notifications message = messages.get(position);
            message.setIsclose(1);
            messages.set(position, message);
            mAdapter.notifyDataSetChanged();

            String type, ntype;
            type = message.getType();
            ntype = message.getNtype();
            String jobid = message.getMessage();
            String title = message.getBm();
            String id = String.valueOf(message.getId());

            switch (type) {
                case "s":
                    if (ntype.equals("bi") || ntype.equals("bt")) {
                        if (jobid != null) {
                            Log.e("jobIds", "" + jobid);
                            if (jobid.contains(",")) {
                                Intent notiIntent = new Intent(NotificationActivity.this, MultipleNotificationActivity.class);
                                notiIntent.putExtra("type", type);
                                notiIntent.putExtra("jobids", jobid);
                                notiIntent.putExtra("title", title);
                                notiIntent.putExtra("Notifyid", id);
                                startActivity(notiIntent);
                            } else {
                                Intent resultIntent = new Intent(NotificationActivity.this, JobDetailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("point", 0);
                                bundle.putString("task", U.GCM);
                                bundle.putString("key", jobid);
                                bundle.putString("Notifyid", id);
                                bundle.putBoolean("gcm", true);
                                resultIntent.putExtras(bundle);
                                startActivity(resultIntent);
                            }
                        }
                    }
                    break;
                case "st":
                    startActivity(new Intent(this, ST_Activity.class).putExtra("idd", message.getId()).putExtra("nactivity", 1));
                    break;
                case "w":
                    startActivity(new Intent(this, ST_Activity.class).putExtra("id", message.getId()).putExtra("nactivity", 1));
                    break;
                case "cj":
                    callMultipleNoti(type, jobid, title, id);
                    break;
                case "fj":
                    callMultipleNoti(type, jobid, title, id);
                    break;
                case "dj":
                    callMultipleNoti(type, jobid, title, id);
                    break;
            }
        }
    }

    @Override
    public void onRowLongClicked(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }

        enableActionMode(position);
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    delete(mode);
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearSelections();

            actionMode = null;
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.resetAnimationIndex();
                    mAdapter.notifyDataSetChanged();
                }
            });
        }

    }

    private void deleteMessages() {
        mAdapter.resetAnimationIndex();
        List<Integer> selectedItemPositions =
                mAdapter.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            int dID = messages.get(selectedItemPositions.get(i)).getId();
            System.out.println("Item Removed : " + dID);
            myDB.execSQL("delete from noti_cal where id = " + dID);
            mAdapter.removeData(selectedItemPositions.get(i));
        }
        mAdapter.notifyDataSetChanged();
        if (messages.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            dnf.setVisibility(View.VISIBLE);
        }
    }

    private void delete(final ActionMode mode) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set dialog message
        alertDialogBuilder
                .setMessage("இந்த அறிவிப்புகளை நீக்க விரும்புகிறீர்களா?")
                .setCancelable(false)
                .setPositiveButton("ஆம்", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int ids) {
                        // if this button is clicked, close
                        // current activity
                        deleteMessages();
                        mode.finish();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("இல்லை", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void callMultipleNoti(String type, String jobid, String title, String Notifyid) {
        Intent notiIntent = new Intent(NotificationActivity.this, MultipleNotificationActivity.class);
        notiIntent.putExtra("type", type);
        notiIntent.putExtra("jobids", jobid);
        notiIntent.putExtra("title", title);
        notiIntent.putExtra("Notifyid", Notifyid);
        startActivity(notiIntent);
    }
}
