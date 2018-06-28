package nithra.jobs.career.placement.adapters;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.Calendar;
import java.util.List;

import nithra.jobs.career.placement.MainActivity;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.pojo.Jobs;
import nithra.jobs.career.placement.utills.U;

/**
 * Created by arunrk on 24/5/17.
 */

public class RecyclerTaskAdapter extends RecyclerView.Adapter<RecyclerTaskAdapter.DataObjectHolder> {

    Typeface tfTitle, tfDescription;
    List<Jobs> list;
    Fragment fragment;
    OnReminderItemClick mListener;

    public interface OnReminderItemClick {
        void setOnReminderItemClickListener(int position, int action);
    }

    public RecyclerTaskAdapter(Fragment fragment, List<Jobs> list) {
        this.fragment = fragment;
        mListener = (OnReminderItemClick) fragment;
        this.list = list;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reminder_list_item, viewGroup, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {

        holder.txtTitle.setText(list.get(position).getJobtitle());
        holder.txtDescription.setText(list.get(position).getEmployer());
        holder.txtDate.setText(list.get(position).getDate());

        long dt = list.get(position).getActionDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dt);
        String date = U.convertDatetoString(calendar.getTime(), U.DATE_TIME_FORMAT_WOS);
        String ampm = calendar.get(Calendar.AM_PM) == Calendar.PM ? "PM" : "AM";

        date = U.s2d(calendar.get(Calendar.DAY_OF_MONTH)) + "-" + U.s2d(calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);

        String hour = U.s2d(calendar.get(Calendar.HOUR));
        hour = (hour.equals("00")) ? "12" : hour;
        String min = U.s2d(calendar.get(Calendar.MINUTE));
        date = date + " " + hour + ":" + min;

        if (list.get(position).isAd()) {
            MainActivity.load_addFromMain(holder.frameLayout);
            holder.frameLayout.setVisibility(View.VISIBLE);
            holder.reminderlay.setVisibility(View.GONE);
        } else {
            holder.frameLayout.setVisibility(View.GONE);
            holder.reminderlay.setVisibility(View.VISIBLE);
        }

        holder.txtActionDate.setText(date + " " + ampm);

        System.out.println("reminder image : " + list.get(position).getImage());
        Glide.with(fragment)
                .load(list.get(position).getImage())
                .asBitmap()
                .placeholder(R.drawable.nithra_round)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new BitmapImageViewTarget(holder.imgLogo) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(fragment.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.imgLogo.setImageDrawable(circularBitmapDrawable);
                    }
                });

        holder.btnReminderEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setOnReminderItemClickListener(position, 1);
            }
        });
        holder.btnReminderDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setOnReminderItemClickListener(position, 2);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {

        CardView card;
        TextView txtTitle, txtDescription, txtDate, txtActionDate;
        ImageButton btnReminderEdit, btnReminderDelete;
        ImageView imgLogo;
        FrameLayout frameLayout;
        LinearLayout reminderlay;

        public DataObjectHolder(View view) {
            super(view);

           // tfTitle = Typeface.createFromAsset(context.getAssets(), "Roboto-Bold.ttf");
           // tfTitle = Typeface.createFromAsset(context.getAssets(), "RobotoCondensed-Regular.ttf");

            card = view.findViewById(R.id.card);
            imgLogo = view.findViewById(R.id.imgLogo);
            txtTitle = view.findViewById(R.id.txtTitle);
            txtDescription = view.findViewById(R.id.txtDescription);
            txtDate = view.findViewById(R.id.txtDate);
            txtActionDate = view.findViewById(R.id.txtActionDate);
            btnReminderEdit = view.findViewById(R.id.btnReminderEdit);
            btnReminderDelete = view.findViewById(R.id.btnReminderDelete);
            frameLayout = view.findViewById(R.id.frame);
            reminderlay = view.findViewById(R.id.reminderlay);
           // txtTitle.setTypeface(tfTitle);
          //  txtDescription.setTypeface(tfDescription);

        }
    }

    public void refreshBlockOverlay(int position) {
        notifyItemChanged(position);
    }

}
