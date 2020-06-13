package nithra.jobs.career.placement.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import nithra.jobs.career.placement.MainActivity;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.activity.RegistrationActivity;
import nithra.jobs.career.placement.networking.MySingleton;
import nithra.jobs.career.placement.pojo.Jobs;
import nithra.jobs.career.placement.utills.L;
import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.U;

/**
 * Created by arunrk on 24/5/17.
 */

public class RecyclerTaskAdapter extends RecyclerView.Adapter<RecyclerTaskAdapter.DataObjectHolder> {

    List<Jobs> list;
    private Fragment fragment;
    private OnReminderItemClick mListener;

    public RecyclerTaskAdapter(Fragment fragment, List<Jobs> list) {
        this.fragment = fragment;
        mListener = (OnReminderItemClick) fragment;
        this.list = list;
    }

    @NonNull
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reminder_list_item, viewGroup, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DataObjectHolder holder, final int position) {

        if (fragment.getActivity() != null) {
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
            holder.txtActionDate.setText(date + " " + ampm);

            if (list.get(position).isAd()) {

                holder.frameLayout.setVisibility(View.VISIBLE);
                holder.reminderlay.setVisibility(View.GONE);
                MainActivity.load_FbaddFromMainList(fragment.getActivity(), holder.frameLayout);

            } else if (list.get(position).isRateus()) {

                holder.reminderlay.setVisibility(View.GONE);
                holder.frameLayout.setVisibility(View.VISIBLE);
                rateus(holder.frameLayout);

            } else if (list.get(position).isRjobs()) {

                holder.reminderlay.setVisibility(View.GONE);
                holder.frameLayout.setVisibility(View.VISIBLE);
                rjobs(holder.frameLayout);

            } else {
                holder.frameLayout.setVisibility(View.GONE);
                holder.reminderlay.setVisibility(View.VISIBLE);
            }

            System.out.println("reminderDate : " + list.get(position).getActionDate());

            Glide.with(fragment.getActivity())
                    .load(list.get(position).getImage())
                    .asBitmap()
                    .placeholder(R.drawable.jobs_round_logo_black)
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
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    private void rjobs(FrameLayout frameLayout) {
        @SuppressLint("InflateParams") View view = fragment.getLayoutInflater().inflate(R.layout.rjobs_view, null);
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

    private void rateus(FrameLayout frameLayout) {
        @SuppressLint("InflateParams") View view = fragment.getLayoutInflater().inflate(R.layout.rateus_view, null);
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
                fragment.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(U.RATEUS)));
            }
        });
    }

    private void callRegistration() {
        if (U.isNetworkAvailable(fragment.getActivity())) {
            Intent intent = new Intent(fragment.getActivity(), RegistrationActivity.class);
            intent.putExtra("task", "Edit");
            fragment.startActivity(intent);
        } else {
            Toast.makeText(fragment.getActivity(), U.INA, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void showFeedBack() {
        if (fragment.getActivity() != null) {
            final Dialog dialog = new Dialog(fragment.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.feed_back);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }

            final EditText edFeed, edEmail, edphone;

            TextView txtCancel, txtSend, txtPrivacy;
            txtPrivacy = dialog.findViewById(R.id.txtPrivacy);
            txtPrivacy.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            txtPrivacy.setText(fragment.getActivity().getString(R.string.privacy_policy) + "\nPrivacy Policy");
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
                    String feed, email = "", phone = "";
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
                        if (U.isNetworkAvailable(fragment.getActivity())) {
                            sendFeed(email, feed);
                            dialog.dismiss();
                        } else L.t(fragment.getActivity(), U.INA);
                    } else {
                        L.t(fragment.getActivity(), fragment.getActivity().getResources().getString(R.string.type));
                    }

                }
            });

            if (!fragment.getActivity().isFinishing()) {
                dialog.show();
            }
        }
    }

    private void callPrivacy() {
        if (U.isNetworkAvailable(fragment.getActivity())) {
            showPrivacy(SU.PRIVACY);
        } else L.t(fragment.getActivity(), U.INA);
    }

    private void showPrivacy(String url) {
        if (fragment.getActivity() != null) {
            Dialog dialog = new Dialog(fragment.getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
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
            if (!fragment.getActivity().isFinishing()) {
                dialog.show();
            }
        }
    }

    private void sendFeed(final String email, final String feed) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.FEEDBACKURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (fragment.getActivity() != null) {
                            U.toast_center(fragment.getActivity(), "" + fragment.getActivity().getResources().getString(R.string.content_saved));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        L.t(fragment.getActivity(), error.getMessage());
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
                if (fragment.getActivity() != null) {
                    params.put("feedback", feed);
                    params.put("email", email);
                    params.put("vcode", U.getVersion(fragment.getActivity()).versionCode + "");
                    params.put("model", U.getDeviceName());
                    params.put("type", type);
                }
                return params;
            }
        };
        MySingleton.getInstance(fragment.getActivity()).addToRequestQue(stringRequest);
    }

    public interface OnReminderItemClick {
        void setOnReminderItemClickListener(int position, int action);
    }

    class DataObjectHolder extends RecyclerView.ViewHolder {

        CardView card;
        TextView txtTitle, txtDescription, txtDate, txtActionDate;
        ImageButton btnReminderEdit, btnReminderDelete;
        ImageView imgLogo;
        FrameLayout frameLayout;
        LinearLayout reminderlay;

        DataObjectHolder(View view) {
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
            // txtDescription.setTypeface(tfDescription);
        }
    }

}
