package nithra.jobs.career.placement.bottomsheets;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.View;

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

public class ShareBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {

    View view;
    CardView cardWhatsApp, cardGmail, cardShare;
    Fragment fragment;
    AppCompatActivity activity;
    OnShareClick onShareClick;
    String message;
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
    public ShareBottomSheet(AppCompatActivity activity, String message) {
        this.activity = activity;
        this.message = message;
        onShareClick = (OnShareClick) fragment;
    }

    @SuppressLint("ValidFragment")
    public ShareBottomSheet(Fragment fragment, String message) {
        this.fragment = fragment;
        this.message = message;
        onShareClick = (OnShareClick) fragment;
    }

    @SuppressLint("ValidFragment")
    public ShareBottomSheet() {
    }

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        view = View.inflate(getContext(), R.layout.share_bottom_sheet, null);

        cardWhatsApp = view.findViewById(R.id.cardWhatsApp);
        cardWhatsApp.setOnClickListener(this);

        cardGmail = view.findViewById(R.id.cardGmail);
        cardGmail.setOnClickListener(this);

        cardShare = view.findViewById(R.id.cardShare);
        cardShare.setOnClickListener(this);

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
        if (v == cardWhatsApp) onShareClick.onShareItemClick(1, message);
        else if (v == cardGmail) onShareClick.onShareItemClick(2, message);
        else if (v == cardShare) onShareClick.onShareItemClick(3, message);
    }

    public interface OnShareClick {
        void onShareItemClick(int item, String message);
    }

}
