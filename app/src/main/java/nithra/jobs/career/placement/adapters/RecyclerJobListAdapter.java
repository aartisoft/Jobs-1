package nithra.jobs.career.placement.adapters;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.UnsupportedEncodingException;
import java.util.List;

import nithra.jobs.career.placement.MainActivity;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.bottomsheets.ShareBottomSheet;
import nithra.jobs.career.placement.engine.LocalDB;
import nithra.jobs.career.placement.pojo.Jobs;
import nithra.jobs.career.placement.utills.L;
import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.U;

/**
 * Created by arunrk on 24/5/17.
 */

public class RecyclerJobListAdapter extends RecyclerView.Adapter<RecyclerJobListAdapter.DataObjectHolder> {

    List<Jobs> list;
    Fragment context;
    Typeface tfTitle, tfSub;
    OnJobItemClick onJobItemClick;
    LocalDB localDB;
    Animation blinking;

    public void refresh() {
        notifyDataSetChanged();
    }

    public void clear() {
        if (this.list != null) {
            int size = this.list.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    this.list.remove(0);
                }

                this.notifyItemRangeRemoved(0, size);
            }
        }
    }

    public void addAll(List<Jobs> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public interface OnJobItemClick {
        void setOnJobItemClick(int id, int position);

        void setOnJobDeleteItemClick(int id, int position);

        void setOnJobReminderClick(int position);
    }

    public RecyclerJobListAdapter(Fragment context, List<Jobs> list) {
        this.context = context;
        onJobItemClick = (OnJobItemClick) context;
        localDB = new LocalDB(context.getActivity());
        this.list = list;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.joblist, viewGroup, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        String image = "";
        try {
            image = java.net.URLDecoder.decode(list.get(position).getImage(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        blinking = AnimationUtils.loadAnimation(context.getActivity(), R.anim.blinking);

        Glide.with(context)
                .load(image)
                .asBitmap()
                .placeholder(R.drawable.nithra_round)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new BitmapImageViewTarget(holder.imgLogo) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.imgLogo.setImageDrawable(circularBitmapDrawable);
                    }
                });

        if (list.get(position).isAd()) {
            Log.e("adPosition", "" + position + "--" + list.get(position).isAd());
            MainActivity.load_addFromMain(holder.frameLayout);
            holder.jobLay.setVisibility(View.GONE);
            holder.divider.setVisibility(View.GONE);
            holder.frameLayout.setVisibility(View.VISIBLE);
        } else {
            holder.divider.setVisibility(View.VISIBLE);
            holder.jobLay.setVisibility(View.VISIBLE);
            holder.frameLayout.setVisibility(View.GONE);
        }

        if (list.get(position).getJobtype().equals("1")) {
            holder.txtJobType.setBackgroundColor(context.getResources().getColor(R.color.blue));
            holder.txtJobType.setText(context.getResources().getString(R.string.privatejob));
        } else if (list.get(position).getJobtype().equals("2")) {
            holder.txtJobType.setBackgroundColor(context.getResources().getColor(R.color.thick_green));
            holder.txtJobType.setText(context.getResources().getString(R.string.state_govt));
        } else if (list.get(position).getJobtype().equals("4")) {
            holder.txtJobType.setBackgroundColor(context.getResources().getColor(R.color.toast));
            holder.txtJobType.setText(context.getResources().getString(R.string.consultancy));
        } else {
            holder.txtJobType.setBackgroundColor(context.getResources().getColor(R.color.browish_red));
            holder.txtJobType.setText(context.getResources().getString(R.string.central_govt));
        }

        if (list.get(position).getVerified().equals("0")) {
            holder.verifiedLogo.setVisibility(View.GONE);
        } else {
            holder.verifiedLogo.setVisibility(View.VISIBLE);
        }

        holder.txtTitle.setText(list.get(position).getJobtitle());
        holder.txtTitle.setTypeface(tfTitle);
        if (list.get(position).isRead) {
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.l));
        }

        holder.txtCompanyName.setText(list.get(position).getEmployer());
        holder.txtCompanyName.setTypeface(tfSub);
        String vacancy = list.get(position).getNoofvacancy();
        if (vacancy != null) {
            if (vacancy.equals("")) vacancy = "";
            else if (vacancy.equals("0")) vacancy = "";
        } else vacancy = "";
        holder.txtnoofvacancy.setVisibility(vacancy.equals("") ? View.GONE : View.VISIBLE);
        holder.txtNoOfVacancy.setText(vacancy);
        holder.txtNoOfVacancy.setTypeface(tfSub);

        holder.txtDate.setText(list.get(position).getDate());
        holder.txtDate.setTypeface(tfSub);

        int datediff = list.get(position).getDatediff();
        String diff = datediff > 1 ? datediff + " days left" : datediff + " day left";
        if (datediff == 0) diff = "Today";
        else if (datediff == 1) diff = "Tomorrow";
        else if (datediff > 1) diff = datediff + " days left";
        holder.txtDateDiff.setText(diff);
        holder.txtDateDiff.setTypeface(tfSub);
        if (diff.equals("Today")) {
            holder.txtDateDiff.startAnimation(blinking);
            // holder.txtDateDiff.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }

        holder.btnLike.setImageResource(localDB.isBookMarkExists(list.get(position).getId()) ? R.drawable.star_on : R.drawable.star_off);

        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = list.get(position).getId();
                if (localDB.isBookMarkExists(id)) {
                    if (localDB.deleteJobBookMark(id)) {
                        onJobItemClick.setOnJobDeleteItemClick(id, position);
                        L.t(context.getActivity(), U.FAV_REMOVED);
                        holder.btnLike.setImageResource(R.drawable.star_off);
                    }
                } else {
                    if (localDB.addJobBookMark(id)) {
                        L.t(context.getActivity(), U.FAV_SUCCESS);
                        holder.btnLike.setImageResource(R.drawable.star_on);
                    }
                }
            }
        });

        holder.btnReminder.setImageResource(localDB.isReminderExists(list.get(position).getId()) ? R.drawable.ic_alarm_color_24dp : R.drawable.ic_alarm_black_24dp);

        holder.btnReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onJobItemClick.setOnJobReminderClick(position);
            }
        });

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String share = "";
                try {
                    share = "Job Title : " + list.get(position).getJobtitle() + "\n" + "Description : " + list.get(position).getDescription() + "\n\n" + "மேலும் வேலைவாய்ப்பை பற்றி அறிந்துகொள்ள கீழே உள்ள லிங்கை கிளிக் செய்யவும்" +
                            " " + SU.BASE_URL + "deeplinking.php?id=" + list.get(position).getId() + "\n\n";
                } catch (Exception e) {
                    share = "";
                }
                contentShare(share);
            }
        });

        holder.jobLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c1 = localDB.getQry("SELECT * FROM ReadUnreadTable where read_id='" + list.get(position).getId() + "'");
                c1.moveToFirst();
                if (c1.getCount() == 0) {
                    localDB.insertReadId(String.valueOf(list.get(position).getId()));
                }
                onJobItemClick.setOnJobItemClick(list.get(position).getId(), position);
                c1.close();
            }
        });

    }

    public void refreshBlockOverlay(int position) {
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {

        LinearLayout layout, background, jobLay;
        ImageView imgLogo, verifiedLogo;
        ImageButton btnLike, btnReminder, btnShare;
        View divider;
        FrameLayout frameLayout;
        TextView txtTitle, txtCompanyName, txtNoOfVacancy, txtSkills, txtDate, txtJobType, txtDateDiff, txtnoofvacancy;

        public DataObjectHolder(View view) {
            super(view);

            divider = view.findViewById(R.id.divider);
            frameLayout = view.findViewById(R.id.frame);
            layout = view.findViewById(R.id.layout);
            jobLay = view.findViewById(R.id.joblay);
            background = view.findViewById(R.id.background);
            imgLogo = view.findViewById(R.id.imgLogo);
            verifiedLogo = view.findViewById(R.id.verified_logo);
            btnLike = view.findViewById(R.id.btnLike);
            btnReminder = view.findViewById(R.id.btnReminder);
            btnShare = view.findViewById(R.id.btnShare);
            txtJobType = view.findViewById(R.id.txtJobType);
            txtTitle = view.findViewById(R.id.txtTitle);
            txtCompanyName = view.findViewById(R.id.txtCompanyName);
            txtNoOfVacancy = view.findViewById(R.id.txtNoOfVacancy);
            txtnoofvacancy = view.findViewById(R.id.txtnoofvacancy);
            txtSkills = view.findViewById(R.id.txtSkills);
            txtDate = view.findViewById(R.id.txtDate);
            txtDateDiff = view.findViewById(R.id.txtDateDiff);
            tfTitle = Typeface.createFromAsset(context.getActivity().getAssets(), "fonts/roboto-bold.ttf");
            tfSub = Typeface.createFromAsset(context.getActivity().getAssets(), "fonts/roboto-medium.ttf");
        }
    }

    private void contentShare(String share) {
        ShareBottomSheet newsDetailBottomsheet = null;
        if (newsDetailBottomsheet == null) {
            newsDetailBottomsheet = new ShareBottomSheet(context, share);
            newsDetailBottomsheet.show(context.getFragmentManager(), "");
        }
    }
}
