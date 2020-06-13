package nithra.jobs.career.placement.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import nithra.jobs.career.placement.MainActivity;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.activity.RegistrationActivity;
import nithra.jobs.career.placement.bottomsheets.ShareBottomSheet;
import nithra.jobs.career.placement.engine.LocalDB;
import nithra.jobs.career.placement.networking.MySingleton;
import nithra.jobs.career.placement.pojo.Jobs;
import nithra.jobs.career.placement.utills.L;
import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

/**
 * Created by arunrk on 24/5/17.
 */
public class RecyclerJobListAdapter extends RecyclerView.Adapter<RecyclerJobListAdapter.DataObjectHolder> {

    private List<Jobs> list;
    private Fragment context;
    private Typeface tfTitle, tfSub;
    private OnJobItemClick onJobItemClick;
    private LocalDB localDB;
    private String mtask = "";
    private String mValue = "";
    private SharedPreference pref;

    public RecyclerJobListAdapter(Fragment context, List<Jobs> list, String task, String value) {
        this.context = context;
        mtask = "";
        mValue = "";
        onJobItemClick = (OnJobItemClick) context;
        localDB = new LocalDB(context.getActivity());
        pref = new SharedPreference();
        this.list = list;
        this.mtask = task;
        Log.e("task_adapter", "" + mtask);
        this.mValue = value;
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    public void clear() {
        if (this.list != null) {
            int size = this.list.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    list.remove(0);
                }

                this.notifyItemRangeRemoved(0, size);
            }
        }
    }

    public void addAll(List<Jobs> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DataObjectHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.joblist, viewGroup, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DataObjectHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (context.getActivity() != null) {

            switch (list.get(position).getViewType()) {
                case "ad":
                    chooselay(holder, holder.frameLayout);
                    holder.frameLayout.setBackgroundResource(R.drawable.adv_banner_sample);
                    holder.frameLayout.setMinimumHeight(150);
                    MainActivity.load_FbaddFromMainList(context.getActivity(), holder.frameLayout);
                    holder.divider.setVisibility(View.VISIBLE);
                    break;
                case "rate_us":
                    chooselay(holder, holder.frameLayout);
                    rateus(holder.frameLayout);
                    break;
                case "rjobs":
                    chooselay(holder, holder.frameLayout);
                    rjobs(holder.frameLayout);
                    break;
                case "group_detail":
                    chooselay(holder, holder.grpCompanyJobLay);
                    setGroupJob(holder, position);
                    break;
                case "single_detail":
                    chooselay(holder, holder.jobLay);
                    setSingleJob(holder, position);
                    break;
                case "single_image":
                    chooselay(holder, holder.imgJobLay);
                    setImageJob(holder, position, "single_image");
                    break;
                case "group_image":
                    chooselay(holder, holder.imgJobLay);
                    setImageJob(holder, position, "group_image");
                    break;
                case "nithra_ad":
                    chooselay(holder, holder.imgJobLay);
                    setImageJob(holder, position, "nithra_ad");
                    break;
                case "LastItem":
                    chooselay(holder, holder.lastItemLay);
                    if (mtask.equals(SU.GETNLOCATIONJOBS) || mtask.equals(SU.FILTER) || mtask.equals(U.ADVSEARCH)) {
                        holder.lastItemLay.setBackgroundColor(context.getResources().getColor(R.color.thick_green));
                        holder.lastItemLay.setPadding(5, 10, 5, 10);
                        holder.lastItemTxt.setTextSize(15);
                        holder.lastItemTxt.setTextColor(context.getResources().getColor(R.color.white));
                        holder.lastItemTxt.setText("முகப்பு பக்கத்திற்கு செல்ல !!");
                    } else if (list.get(position).getJobtitle().equals("")) {
                        holder.lastItemTxt.setText("மேலும் தகவல்கள் இல்லை");
                    } else {
                        holder.lastItemTxt.setTextSize(15);
                        holder.lastItemTxt.setText(list.get(position).getJobtitle());
                    }
                    break;
            }

            holder.lastItemLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mtask.equals(SU.GETNLOCATIONJOBS) || mtask.equals(SU.FILTER)
                            || mtask.equals(U.ADVSEARCH)) {
                        try {
                            onJobItemClick.setOnJobItemClick(String.valueOf(list.get(position).getId()), position, "ViewAllJobs");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public void refreshBlockOverlay(int position) {
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    private void setSingleJob(final DataObjectHolder holder, final int position) {

        Animation blinking = AnimationUtils.loadAnimation(context.getActivity(), R.anim.blinking);

        String image = "";
        try {
            image = java.net.URLDecoder.decode(list.get(position).getImage(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Glide.with(context.getActivity())
                .load(image)
                .asBitmap()
                .placeholder(R.drawable.jobs_round_logo_black)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new BitmapImageViewTarget(holder.imgLogo) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        if (context.getActivity() != null) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getActivity().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            holder.imgLogo.setImageDrawable(circularBitmapDrawable);
                        }
                    }
                });

        holder.txtTitle.setText(list.get(position).getJobtitle());
        holder.txtTitle.setTypeface(tfTitle);

        switch (list.get(position).getJobtype()) {
            case "1":
                holder.txtJobType.setBackgroundColor(context.getResources().getColor(R.color.blue));
                holder.txtJobType.setText(context.getResources().getString(R.string.privatejob));
                break;
            case "2":
                holder.txtJobType.setBackgroundColor(context.getResources().getColor(R.color.thick_green));
                holder.txtJobType.setText(context.getResources().getString(R.string.state_govt));
                break;
            case "4":
                holder.txtJobType.setBackgroundColor(context.getResources().getColor(R.color.toast));
                holder.txtJobType.setText(context.getResources().getString(R.string.consultancy));
                break;
            default:
                holder.txtJobType.setBackgroundColor(context.getResources().getColor(R.color.browish_red));
                holder.txtJobType.setText(context.getResources().getString(R.string.central_govt));
                break;
        }

        if (list.get(position).getVerified().equals("0")) {
            holder.verifiedLogo.setVisibility(View.GONE);
        } else {
            holder.verifiedLogo.setVisibility(View.VISIBLE);
        }

        if (context.getActivity() != null) {
            if ((pref.getString(context.getActivity(), U.SH_USER_TYPE).equals(U.EMPLOYER)) ||
                    (mtask.equals(U.GROUPJOBS) && mValue.equals(SU.GETPERSONALISEDJOBS)) ||
                    (mtask.equals(SU.GETPERSONALISEDJOBS)) || (mtask.equals(SU.APPLIEDJOBS))) {
                holder.tagImg.setVisibility(View.GONE);
            } else {
                if (list.get(position).getTag() != null) {
                    switch (list.get(position).getTag()) {
                        case "Suggested":
                            holder.tagImg.setVisibility(View.VISIBLE);
                            holder.tagImg.setImageResource(R.drawable.ic_thumb_up);
                            break;
                        case "More Relevent":
                            holder.tagImg.setVisibility(View.VISIBLE);
                            holder.tagImg.setImageResource(R.drawable.ic_thumb_up_outline);
                            break;
                        case "None":
                            holder.tagImg.setVisibility(View.GONE);
                            break;
                    }

                    holder.tagImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            switch (list.get(position).getTag()) {
                                case "Suggested":
                                    L.t(context.getActivity(), "உங்களுக்கான வேலைவாய்ப்பு !!");
                                    break;
                                case "More Relevent":
                                    L.t(context.getActivity(), "உங்களின் ஏதேனும் ஒரு தகவலுடன் பொருந்தக்கூடிய வேலைவாய்ப்பு !!");
                                    break;
                                case "None":
                                    break;
                            }
                        }
                    });
                }
            }
        }

        if (list.get(position).isRead) {
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.light_grey));
        }

        holder.txtCompanyName.setText(list.get(position).getEmployer());
        holder.txtCompanyName.setTypeface(tfSub);

        String location;
        location = list.get(position).getDistrict().equals("false") ? "" : list.get(position).getDistrict();
        if (location.isEmpty()) {
            holder.txtCompanyLocation.setVisibility(View.GONE);
        } else {
            holder.txtCompanyLocation.setVisibility(View.VISIBLE);
            holder.txtCompanyLocation.setText(location);
            holder.txtCompanyLocation.setTypeface(tfSub);
        }

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
        }

        holder.btnLike.setImageResource(localDB.isBookMarkExists(list.get(position).getId()) ? R.drawable.star_on : R.drawable.star_off);

        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = list.get(position).getId();
                String titleId = list.get(position).getJobtitleId();
                if (localDB.isBookMarkExists(id)) {
                    if (localDB.deleteJobBookMark(id)) {
                        onJobItemClick.setOnJobDeleteItemClick(id, position);
                        L.t(context.getActivity(), U.FAV_REMOVED);
                        holder.btnLike.setImageResource(R.drawable.star_off);
                    }
                } else {
                    if (localDB.addJobBookMark(id, titleId)) {
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
                String share;
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
                try {
                    onJobItemClick.setOnJobItemClick(String.valueOf(list.get(position).getId()), position, mtask);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setGroupJob(final DataObjectHolder holder, final int position) {

        Animation blinking = AnimationUtils.loadAnimation(context.getActivity(), R.anim.blinking);

        String image = "";
        try {
            image = java.net.URLDecoder.decode(list.get(position).getImage(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Glide.with(context.getActivity())
                .load(image)
                .asBitmap()
                .placeholder(R.drawable.jobs_round_logo_black)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new BitmapImageViewTarget(holder.imgLogo) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        try {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            holder.grpimgLogo.setImageDrawable(circularBitmapDrawable);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

        switch (list.get(position).getJobtype()) {
            case "1":
                holder.grpJobType.setBackgroundColor(context.getResources().getColor(R.color.blue));
                holder.grpJobType.setText(context.getResources().getString(R.string.privatejob));
                break;
            case "2":
                holder.grpJobType.setBackgroundColor(context.getResources().getColor(R.color.thick_green));
                holder.grpJobType.setText(context.getResources().getString(R.string.state_govt));
                break;
            case "4":
                holder.grpJobType.setBackgroundColor(context.getResources().getColor(R.color.toast));
                holder.grpJobType.setText(context.getResources().getString(R.string.consultancy));
                break;
            default:
                holder.grpJobType.setBackgroundColor(context.getResources().getColor(R.color.browish_red));
                holder.grpJobType.setText(context.getResources().getString(R.string.central_govt));
                break;
        }

        if (list.get(position).getVerified().equals("0")) {
            holder.grpverifiedLogo.setVisibility(View.GONE);
        } else {
            holder.grpverifiedLogo.setVisibility(View.VISIBLE);
        }

        if (list.get(position).isRead) {
            holder.grpCompanyJobLay.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            holder.grpCompanyJobLay.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        holder.grpCompanyName.setText(list.get(position).getEmployer());
        holder.grpCompanyName.setTypeface(tfSub);

        String location;
        location = list.get(position).getDistrict().equals("false") ? "" : list.get(position).getDistrict();
        if (location.isEmpty()) {
            holder.grpCompanyLocation.setVisibility(View.GONE);
        } else {
            holder.grpCompanyLocation.setVisibility(View.VISIBLE);
            holder.grpCompanyLocation.setText(location);
            holder.grpCompanyLocation.setTypeface(tfSub);
        }

        String vacancy = list.get(position).getNoofvacancy();
        if (vacancy != null) {
            if (vacancy.equals("")) vacancy = "";
            else if (vacancy.equals("0")) vacancy = "";
        } else vacancy = "";
        holder.grpnoofvacancy.setVisibility(vacancy.equals("") ? View.GONE : View.VISIBLE);
        holder.grpNoOfVacancy.setText(vacancy);
        holder.grpNoOfVacancy.setTypeface(tfSub);

        holder.grpDate.setText(list.get(position).getDate());
        holder.grpDate.setTypeface(tfSub);

        int datediff = list.get(position).getDatediff();
        String diff = datediff > 1 ? datediff + " days left" : datediff + " day left";
        if (datediff == 0) diff = "Today";
        else if (datediff == 1) diff = "Tomorrow";
        else if (datediff > 1) diff = datediff + " days left";
        holder.grpDateDiff.setText(diff);
        holder.grpDateDiff.setTypeface(tfSub);
        if (diff.equals("Today")) {
            holder.grpDateDiff.startAnimation(blinking);
        }

        List<String> titleList = Arrays.asList(list.get(position).getJobtitle().split(", "));
        int count = titleList.size();
        StringBuilder item = new StringBuilder();
        for (int i = 0; i < titleList.size(); i++) {
            if (i > 3) {
                int remaining = count - 4;
                item.append("&nbsp; <font color='blue'><u> + ").append(remaining).append(" More</u> &#9758;</font><br>");
                break;
            } else {
                if (i == 0) {
                    item.append("&nbsp;").append(i + 1).append(".&nbsp;").append(titleList.get(i));//&#10147;
                } else {
                    item.append("<br>");
                    item.append("&nbsp;").append(i + 1).append(".&nbsp;").append(titleList.get(i));//&#10003;
                }
            }
        }
        holder.grpTitles.setText(Html.fromHtml(item.toString()));

        holder.grpCompanyJobLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context.getActivity() != null) {
                    onJobItemClick.setOnJobItemClick((list.get(position).getGrpJobId().replace(",", "-")), position, mtask);
                }
            }
        });

        holder.viewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context.getActivity() != null) {
                    onJobItemClick.setOnJobItemClick((list.get(position).getGrpJobId().replace(",", "-")), position, mtask);
                }
            }
        });
    }

    private void setImageJob(final DataObjectHolder holder, final int position, final String task) {
        String image = list.get(position).getDescription();
        Glide.with(context.getActivity())
                .load(image)
                .asBitmap()
                .placeholder(R.drawable.pre_loader)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new BitmapImageViewTarget(holder.imgLogo) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        holder.imageJob.setImageBitmap(resource);
                    }
                });

        holder.imageJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context.getActivity() != null) {
                    switch (task) {
                        case "single_image":
                            onJobItemClick.setOnJobItemClick(String.valueOf(list.get(position).getId()), position, mtask);
                            break;
                        case "group_image":
                            if (context.getActivity() != null) {
                                onJobItemClick.setOnJobItemClick((list.get(position).getGrpJobId().replace(",", "-")), position, mtask);
                            }
                            break;
                        case "nithra_ad":
                            if (U.isNetworkAvailable(context.getActivity())) {
                                U.custom_tabs(context.getActivity(), list.get(position).getJobtitle());
                            } else {
                                L.t(context.getActivity(), U.INA);
                            }
                            break;
                    }
                }
            }
        });
    }

    private void contentShare(String share) {
        ShareBottomSheet newsDetailBottomsheet = new ShareBottomSheet(context, share);
        if (context.getFragmentManager() != null) {
            newsDetailBottomsheet.show(context.getFragmentManager(), "");
        }
    }

    private void rateus(FrameLayout frameLayout) {
        @SuppressLint("InflateParams") View view = context.getLayoutInflater().inflate(R.layout.rateus_view, null);
        frameLayout.removeAllViews();
        frameLayout.addView(view);
        Button feedback = view.findViewById(R.id.feedback);
        Button rateus = view.findViewById(R.id.rateus);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFeedBack();
            }
        });
        rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(U.RATEUS)));
            }
        });
    }

    private void rjobs(FrameLayout frameLayout) {
        @SuppressLint("InflateParams") View view = context.getLayoutInflater().inflate(R.layout.rjobs_view, null);
        frameLayout.removeAllViews();
        frameLayout.addView(view);
        Button register = view.findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callRegistration();
            }
        });
    }

    private void callRegistration() {
        if (U.isNetworkAvailable(context.getActivity())) {
            Intent intent = new Intent(context.getActivity(), RegistrationActivity.class);
            intent.putExtra("task", "Edit");
            context.startActivity(intent);
        } else {
            Toast.makeText(context.getActivity(), U.INA, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void showFeedBack() {
        if (context.getActivity() != null) {
            final Dialog dialog = new Dialog(context.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.feed_back);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }

            final EditText edFeed, edEmail, edphone;

            TextView txtCancel, txtSend, txtPrivacy;
            txtPrivacy = dialog.findViewById(R.id.txtPrivacy);
            txtPrivacy.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            txtPrivacy.setText(context.getActivity().getString(R.string.privacy_policy) + "\nPrivacy Policy");
            txtPrivacy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callPrivacy();
                }
            });

            txtCancel = dialog.findViewById(R.id.txtCancel);
            txtSend = dialog.findViewById(R.id.txtSend);

            edEmail = dialog.findViewById(R.id.edEmail);
            edFeed = dialog.findViewById(R.id.edFeed);
            edphone = dialog.findViewById(R.id.edphone);

            txtCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            txtSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String feed, email, phone;
                    email = edEmail.getText().toString();
                    phone = edphone.getText().toString();

                    try {
                        feed = URLEncoder.encode(edFeed.getText().toString(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        feed = "";
                    }

                    if (feed.length() != 0) {
                        if (!phone.isEmpty()) {
                            feed = feed + "   PhnNO: " + phone;
                        }
                        if (U.isNetworkAvailable(context.getActivity())) {
                            sendFeed(email, feed);
                            dialog.dismiss();
                        } else L.t(context.getActivity(), U.INA);
                    } else {
                        L.t(context.getActivity(), context.getActivity().getResources().getString(R.string.type));
                    }

                }
            });

            if (context.getActivity() != null) {
                if (!context.getActivity().isFinishing()) {
                    dialog.show();
                }
            }
        }
    }

    private void callPrivacy() {
        if (U.isNetworkAvailable(context.getActivity())) {
            showPrivacy(SU.PRIVACY);
        } else L.t(context.getActivity(), U.INA);
    }

    private void showPrivacy(String url) {
        if (context.getActivity() != null) {
            Dialog dialog = new Dialog(context.getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.setContentView(R.layout.privacy);

            final ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
            final WebView webView = dialog.findViewById(R.id.webView);
            webView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    if (progress == 100) {
                        progressBar.setVisibility(View.GONE);
                        webView.setVisibility(View.VISIBLE);
                    }
                }
            });
            webView.loadUrl(url);
            webView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return true;
                }
            });
            if (!context.getActivity().isFinishing()) {
                dialog.show();
            }
        }
    }

    private void sendFeed(final String email, final String feed) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.FEEDBACKURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (context.getActivity() != null) {
                            U.toast_center(context.getActivity(), "" + context.getActivity().getResources().getString(R.string.content_saved));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        L.t(context.getActivity(), error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                String type = "";
                try {
                    type = URLEncoder.encode(SU.APP, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Map<String, String> params = new HashMap<>();
                if (context.getActivity() != null) {
                    params.put("feedback", feed);
                    params.put("email", email);
                    params.put("vcode", U.getVersion(context.getActivity()).versionCode + "");
                    params.put("model", U.getDeviceName());
                    params.put("type", type);
                }
                return params;
            }
        };
        MySingleton.getInstance(context.getActivity()).addToRequestQue(stringRequest);
    }

    private void chooselay(final DataObjectHolder holder, View view) {
        holder.jobLay.setVisibility(View.GONE);
        holder.grpCompanyJobLay.setVisibility(View.GONE);
        holder.imgJobLay.setVisibility(View.GONE);
        holder.divider.setVisibility(View.GONE);
        holder.frameLayout.setVisibility(View.GONE);
        holder.lastItemLay.setVisibility(View.GONE);

        view.setVisibility(View.VISIBLE);
    }

    public interface OnJobItemClick {
        void setOnJobItemClick(String id, int position, String task);

        void setOnJobDeleteItemClick(int id, int position);

        void setOnJobReminderClick(int position);
    }

    class DataObjectHolder extends RecyclerView.ViewHolder {

        LinearLayout layout, background, jobLay, grpCompanyJobLay, imgJobLay, lastItemLay;
        ImageView imgLogo, verifiedLogo, grpverifiedLogo, grpimgLogo, imageJob, tagImg;
        ImageButton btnLike, btnReminder, btnShare;
        View divider;
        FrameLayout frameLayout;
        TextView txtTitle, txtCompanyName, txtCompanyLocation, txtNoOfVacancy,
                txtSkills, txtDate, txtJobType, txtDateDiff, txtnoofvacancy;
        TextView grpCompanyName, grpCompanyLocation, grpNoOfVacancy, grpDate,
                grpJobType, grpDateDiff, grpnoofvacancy, grpTitles, lastItemTxt;

        LinearLayout viewAllBtn;

        DataObjectHolder(View view) {
            super(view);

            divider = view.findViewById(R.id.divider);
            frameLayout = view.findViewById(R.id.frame);
            layout = view.findViewById(R.id.layout);
            jobLay = view.findViewById(R.id.joblay);
            grpCompanyJobLay = view.findViewById(R.id.grpCompanyJobLay);
            imgJobLay = view.findViewById(R.id.imgJobLay);
            tagImg = view.findViewById(R.id.tagImg);
            lastItemLay = view.findViewById(R.id.lastItemLay);
            lastItemTxt = view.findViewById(R.id.lastItemTxt);
            background = view.findViewById(R.id.background);
            imgLogo = view.findViewById(R.id.imgLogo);
            imageJob = view.findViewById(R.id.imageJob);
            verifiedLogo = view.findViewById(R.id.verified_logo);
            grpverifiedLogo = view.findViewById(R.id.grpverified_logo);
            btnLike = view.findViewById(R.id.btnLike);
            btnReminder = view.findViewById(R.id.btnReminder);
            btnShare = view.findViewById(R.id.btnShare);
            txtJobType = view.findViewById(R.id.txtJobType);
            txtTitle = view.findViewById(R.id.txtTitle);
            txtCompanyName = view.findViewById(R.id.txtCompanyName);
            txtCompanyLocation = view.findViewById(R.id.txtCompanyLocation);
            txtNoOfVacancy = view.findViewById(R.id.txtNoOfVacancy);
            txtnoofvacancy = view.findViewById(R.id.txtnoofvacancy);
            txtSkills = view.findViewById(R.id.txtSkills);
            txtDate = view.findViewById(R.id.txtDate);
            txtDateDiff = view.findViewById(R.id.txtDateDiff);
            grpimgLogo = view.findViewById(R.id.grpimgLogo);
            grpTitles = view.findViewById(R.id.grpTitles);
            viewAllBtn = view.findViewById(R.id.viewAllBtn);

            grpCompanyName = view.findViewById(R.id.grpCompanyName);
            grpCompanyLocation = view.findViewById(R.id.grpCompanyLocation);
            grpNoOfVacancy = view.findViewById(R.id.grpNoOfVacancy);
            grpDate = view.findViewById(R.id.grpDate);
            grpJobType = view.findViewById(R.id.grpJobType);
            grpDateDiff = view.findViewById(R.id.grpDateDiff);
            grpnoofvacancy = view.findViewById(R.id.grpnoofvacancy);

            if (context.getActivity() != null) {
                tfTitle = Typeface.createFromAsset(context.getActivity().getAssets(), "fonts/roboto-bold.ttf");
                tfSub = Typeface.createFromAsset(context.getActivity().getAssets(), "fonts/roboto-medium.ttf");
            }
        }
    }
}
