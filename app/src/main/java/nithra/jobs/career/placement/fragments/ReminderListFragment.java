package nithra.jobs.career.placement.fragments;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import nithra.jobs.career.placement.MainActivity;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.adapters.RecyclerTaskAdapter;
import nithra.jobs.career.placement.engine.LocalDB;
import nithra.jobs.career.placement.pojo.Jobs;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReminderListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReminderListFragment extends Fragment implements
        RecyclerTaskAdapter.OnReminderItemClick, AddDialogFragment.DialogListener {

    LocalDB localDB;
    List<Jobs> list;
    TextView txtError1;
    Button networkRetry;
    SharedPreference pref;
    private LinearLayout lError;
    private RecyclerView mRecyclerView;
    private RecyclerTaskAdapter mAdapter;

    public ReminderListFragment() {
    }

    public static ReminderListFragment newInstance() {
        return new ReminderListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reminder_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setView(view);
    }

    @SuppressLint("WrongConstant")
    private void setView(View view) {
        lError = view.findViewById(R.id.lError);
        txtError1 = view.findViewById(R.id.txtError1);
        networkRetry = view.findViewById(R.id.network_retry);
        networkRetry.setVisibility(View.GONE);
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity.homePagePosition = U.REMINDER_PAGE;
        pref = new SharedPreference();
        localDB = new LocalDB(getActivity());
        list = localDB.getReminders();
        MainActivity.add_ads(this.getActivity(), list);
        mAdapter = new RecyclerTaskAdapter(this, list);
        mRecyclerView.setAdapter(mAdapter);
        errorView();
    }

    private void errorView() {
        txtError1.setText("நினைவூட்டல்கள் எதுவும் இல்லை");
        lError.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
        mRecyclerView.setVisibility(list.size() == 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setOnReminderItemClickListener(final int position, int action) {
        final int id = list.get(position).getId();

        if (action == 1) {
            AddDialogFragment addDialogFragment;
            Jobs jobs = localDB.getItem(id);
            addDialogFragment = new AddDialogFragment(this, 1, position, id, jobs.image, jobs.jobtitle, jobs.jobtitleId, jobs.employer, jobs.date);
            if (getActivity() != null) {
                addDialogFragment.show(getActivity().getSupportFragmentManager(), "");
            }
        } else if (action == 2) {
            if (getActivity() != null) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                // set dialog message
                alertDialogBuilder
                        .setMessage("இந்த நினைவுட்டலை நீக்க விரும்புகிறீர்களா?")
                        .setCancelable(false)
                        .setPositiveButton("ஆம்", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int ids) {
                                // if this button is clicked, close
                                // current activity
                                localDB.deleteJobReminder(id);
                                refresh();
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
        }
    }

    private void refresh() {
        list.clear();
        list = localDB.getReminders();
        MainActivity.add_ads(this.getActivity(), list);
        mAdapter = new RecyclerTaskAdapter(this, list);
        mRecyclerView.setAdapter(mAdapter);
        errorView();
    }

    @Override
    public void onTaskAdded(int position) {
        refresh();
    }

    @Override
    public void onReminderRemoved(int position) {
        refresh();
    }

}
