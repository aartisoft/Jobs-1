package nithra.jobs.career.placement.bottomsheets;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import nithra.jobs.career.placement.R;


/**
 * Created by ARUNRK THOMAS on 4/27/2017.
 */

public class SortBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {

    View view;
    CardView cardDateAsc, cardDateDesc, cardToday, cardTwoDay, cardOneWeek, cardVacancyAsc, cardVacancyDesc;
    Fragment fragment;
    AppCompatActivity activity;
    OnSortClick onSortClick;
    int message, mark = 0;
    TextView txtDescending, txtAscending, txtToday, txtTwoDay, txtOneWeek, txtVacancyAsc, txtVacancyDesc;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };

    @SuppressLint("ValidFragment")
    public SortBottomSheet(AppCompatActivity activity) {
        this.activity = activity;
        onSortClick = (OnSortClick) fragment;
    }

    @SuppressLint("ValidFragment")
    public SortBottomSheet(Fragment fragment, int mark) {
        this.fragment = fragment;
        this.mark = mark;
        onSortClick = (OnSortClick) fragment;
    }

    @SuppressLint("ValidFragment")
    public SortBottomSheet() {
    }

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        view = View.inflate(getContext(), R.layout.sort_bottom_sheet, null);

        txtDescending = view.findViewById(R.id.txtDescending);
        txtAscending = view.findViewById(R.id.txtAscending);
        txtToday = view.findViewById(R.id.txttoday);
        txtTwoDay = view.findViewById(R.id.txttwodays);
        txtOneWeek = view.findViewById(R.id.txtoneweek);
        txtVacancyAsc = view.findViewById(R.id.txtvacancyasc);
        txtVacancyDesc = view.findViewById(R.id.txtvacancydesc);

        cardDateAsc = view.findViewById(R.id.cardDateAsc);
        cardDateAsc.setOnClickListener(this);

        cardDateDesc = view.findViewById(R.id.cardDateDesc);
        cardDateDesc.setOnClickListener(this);

        cardToday = view.findViewById(R.id.cardtoday);
        cardToday.setOnClickListener(this);

        cardTwoDay = view.findViewById(R.id.cardtwodays);
        cardTwoDay.setOnClickListener(this);

        cardOneWeek = view.findViewById(R.id.cardoneweek);
        cardOneWeek.setOnClickListener(this);

        cardVacancyAsc = view.findViewById(R.id.cardvacancyasc);
        cardVacancyAsc.setOnClickListener(this);

        cardVacancyDesc = view.findViewById(R.id.cardvacancydesc);
        cardVacancyDesc.setOnClickListener(this);

        if (mark == 2)
            txtDescending.setTextColor(fragment.getResources().getColor(R.color.colorPrimary));
        else if (mark == 1)
            txtAscending.setTextColor(fragment.getResources().getColor(R.color.colorPrimary));
        else if (mark == 4)
            txtToday.setTextColor(fragment.getResources().getColor(R.color.colorPrimary));
        else if (mark == 5)
            txtTwoDay.setTextColor(fragment.getResources().getColor(R.color.colorPrimary));
        else if (mark == 6)
            txtOneWeek.setTextColor(fragment.getResources().getColor(R.color.colorPrimary));
        else if (mark == 7)
            txtVacancyAsc.setTextColor(fragment.getResources().getColor(R.color.colorPrimary));
        else if (mark == 8)
            txtVacancyDesc.setTextColor(fragment.getResources().getColor(R.color.colorPrimary));


        dialog.setContentView(view);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (v == cardDateAsc) onSortClick.onSortItemClick(1, 1);
        else if (v == cardDateDesc) onSortClick.onSortItemClick(2, 2);
        else if (v == cardToday) onSortClick.onSortItemClick(4, 4);
        else if (v == cardTwoDay) onSortClick.onSortItemClick(5, 5);
        else if (v == cardOneWeek) onSortClick.onSortItemClick(6, 6);
        else if (v == cardVacancyAsc) onSortClick.onSortItemClick(7, 7);
        else if (v == cardVacancyDesc) onSortClick.onSortItemClick(8, 8);
    }

    public interface OnSortClick {
        void onSortItemClick(int item, int message);
    }

}
