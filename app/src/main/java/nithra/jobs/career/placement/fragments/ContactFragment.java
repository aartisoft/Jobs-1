package nithra.jobs.career.placement.fragments;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.common.AccountPicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import nithra.jobs.career.placement.FragmentInterface;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.activity.RegistrationActivity;
import nithra.jobs.career.placement.pojo.Item;
import nithra.jobs.career.placement.utills.CustomEditText;
import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.TouchImageView;
import nithra.jobs.career.placement.utills.U;

import static android.support.v4.view.ViewCompat.jumpDrawablesToCurrentState;

/**
 * Created by nithra-apps on 14/11/17.
 */

public class ContactFragment extends Fragment implements FragmentInterface {

    CustomEditText edName, edEmail, edAltermob;
    SharedPreference pref;
    TextView txtMobno, nativeLocation;
    CheckBox mobileShow;
    List<Item> cities;
    ListView listview;
    Dialog dialog;
    SearchView searchView;
    ImageView profilePhoto;
    int flip = 0;
    final int PIC_CROP = 3;
    private static final int SELECT_PICTURE = 1;

    public ContactFragment() {
        // Required empty public constructor
    }

    public static ContactFragment newInstance() {
        return new ContactFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (getActivity() != null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
        return inflater.inflate(R.layout.contact_detail_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = new SharedPreference();
        cities = new ArrayList<>();
        mobileShow = view.findViewById(R.id.mobileshow);
        edName = view.findViewById(R.id.edName);
        edEmail = view.findViewById(R.id.edMail);
        txtMobno = view.findViewById(R.id.edMobno);
        edAltermob = view.findViewById(R.id.edAlternateMob);
        nativeLocation = view.findViewById(R.id.livingcity);
        profilePhoto = view.findViewById(R.id.profilephoto);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("photoURi_oncreate", pref.getString(getActivity(), U.SH_PHOTO_URI));
        if (!pref.getString(getActivity(), U.SH_PHOTO_URI).equals("")) {

            Uri photoUri = Uri.parse(pref.getString(getActivity(), U.SH_PHOTO_URI));
            Glide.with(this)
                    .load(photoUri)
                    .asBitmap()
                    .placeholder(R.drawable.user)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .into(new BitmapImageViewTarget(profilePhoto) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            profilePhoto.setImageDrawable(circularBitmapDrawable);
                        }
                    });

            flip = 1;
        } else if (pref.getBoolean(getActivity(), U.SH_SIGN_UP_SUCCESS)) {

            String photoValue = pref.getString(getActivity(), U.SH_PHOTO);
            Glide.with(this)
                    .load(SU.BASE_URL + photoValue)
                    .asBitmap()
                    .placeholder(R.drawable.user)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .into(new BitmapImageViewTarget(profilePhoto) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            profilePhoto.setImageDrawable(circularBitmapDrawable);
                        }
                    });

            if (photoValue.equals("")) {
                flip = 0;
            } else {
                flip = 1;
            }

        } else {

            flip = 0;
        }

        for (int i = 0; i < RegistrationActivity.locationList.size(); i++) {
            for (int j = 0; j < RegistrationActivity.locationList.get(i).getList().size(); j++) {
                Item data = new Item();
                data.setId(RegistrationActivity.locationList.get(i).getList().get(j).getId());
                data.setItem(RegistrationActivity.locationList.get(i).getList().get(j).getItem());
                cities.add(data);
                if (pref.getString(getActivity(), U.SH_NATIVE_LOCATION).equals("" + RegistrationActivity.locationList.get(i).getList().get(j).getId())) {
                    nativeLocation.setText(RegistrationActivity.locationList.get(i).getList().get(j).getItem());
                }
            }
        }

        edName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                RegistrationActivity.name = edName.getText().toString();
                if (!RegistrationActivity.name.isEmpty()) {
                    pref.putString(getActivity(), U.SH_NAME, RegistrationActivity.name);
                } else {
                    pref.putString(getActivity(), U.SH_NAME, "");
                }

            }
        });

        edEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                RegistrationActivity.email = edEmail.getText().toString();
                if (!RegistrationActivity.email.isEmpty()) {
                    pref.putString(getActivity(), U.SH_EMAIL, RegistrationActivity.email);
                } else {
                    pref.putString(getActivity(), U.SH_EMAIL, "");
                }

            }
        });

        edAltermob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                RegistrationActivity.altmobileno = edAltermob.getText().toString();
                if (!RegistrationActivity.altmobileno.isEmpty()) {
                    pref.putString(getActivity(), U.SH_ALTERNATE_MOBILE, RegistrationActivity.altmobileno);
                } else {
                    pref.putString(getActivity(), U.SH_ALTERNATE_MOBILE, "");
                }
            }
        });

        if (getActivity() != null) {
            if (pref.getInt(getActivity(), U.SH_EMAIL_DIA_SHOW) == 0) {
                final Dialog guideWindow = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                guideWindow.setContentView(R.layout.guide_window_mail);
                TextView searchText = guideWindow.findViewById(R.id.search_text);
                searchText.setText(getResources().getString(R.string.guide_email_change));
                Button ok = guideWindow.findViewById(R.id.ok_btn);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pref.putInt(getActivity(), U.SH_EMAIL_DIA_SHOW, 1);
                        guideWindow.dismiss();
                    }
                });
                guideWindow.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        pref.putInt(getActivity(), U.SH_EMAIL_DIA_SHOW, 1);
                        emailPermissionFun();
                    }
                });
                guideWindow.show();
            }
        }

        mobileShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    RegistrationActivity.mobileShown = "1";
                } else {
                    RegistrationActivity.mobileShown = "0";
                }
                pref.putString(getActivity(), U.SH_MOBILE_SHOWN, RegistrationActivity.mobileShown);
            }
        });

        nativeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowNativeLocationDialog();
            }
        });

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flip == 0) {
                    GalleryPermissionFun();
                } else {
                    reselectimg();
                }
            }
        });

        edEmail.setDrawableClickListener(new CustomEditText.DrawableClickListener() {

            public void onClick(DrawablePosition target) {
                switch (target) {
                    case RIGHT:
                        emailPermissionFun();
                        break;
                    default:
                        break;
                }
            }

        });
    }

    @SuppressLint("SetTextI18n")
    public void ShowNativeLocationDialog() {
        if (getActivity() != null) {
            dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.custom_dialog);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            // searchView
            searchView = dialog.findViewById(R.id.searchview);
            searchView.setQueryHint("Search...");

            TextView headertext = dialog.findViewById(R.id.text_header);
            headertext.setText("Choose your Living City :");

            LinearLayout bottomLay = dialog.findViewById(R.id.bottom_lay);
            bottomLay.setVisibility(View.GONE);

            listview = dialog.findViewById(R.id.listview);
            //Ascending order
            Collections.sort(cities, new Comparator<Item>() {
                @Override
                public int compare(Item o1, Item o2) {
                    return o1.getItem().compareTo(o2.getItem());
                }
            });

            final SkillsSelectionAdapter SkillAdapter = new SkillsSelectionAdapter(getActivity(), R.layout.filter_layout, cities);
            listview.setTextFilterEnabled(true);
            listview.setAdapter(SkillAdapter);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    try {
                        SkillAdapter.filter(newText.trim());
                    } catch (Exception e) {
                        Log.e("Error", "" + e);
                    }
                    return true;
                }
            });

            dialog.show();
        }
    }

    public class SkillsSelectionAdapter extends ArrayAdapter {

        Context context;
        List<Item> list;
        LayoutInflater inflater;
        List<Item> orig;

        public SkillsSelectionAdapter(Context context, int resource, List<Item> list) {
            super(context, resource, list);
            this.context = context;
            this.list = list;
            orig = new ArrayList<Item>();
            orig.addAll(list);
        }

        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_list_item, null);

            TextView txt = view.findViewById(R.id.name);
            CheckBox checkBox = view.findViewById(R.id.checkbox);
            checkBox.setVisibility(View.GONE);
//            txt.setTextColor(context.getResources().getColor(R.color.skyblue_thick));
            txt.setText(list.get(position).getItem());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nativeLocation.setText(list.get(position).getItem());
                    RegistrationActivity.nativeLocation = "" + list.get(position).getId();
                    pref.putString(getActivity(), U.SH_NATIVE_LOCATION, RegistrationActivity.nativeLocation);
                    searchView.setQuery("", true);
                    if (getActivity() != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                        }
                    }
                    dialog.dismiss();
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

    @Override
    public void onResume() {
        super.onResume();
        if (!pref.getString(getActivity(), U.SH_NAME).equals("")) {
            edName.setText(pref.getString(getActivity(), U.SH_NAME));
        }

        if (!pref.getString(getActivity(), U.SH_EMAIL).equals("")) {
            edEmail.setText(pref.getString(getActivity(), U.SH_EMAIL));
        }

        if (!pref.getString(getActivity(), U.SH_MOBILE).equals("")) {
            txtMobno.setText(pref.getString(getActivity(), U.SH_MOBILE));
        }

        if (!pref.getString(getActivity(), U.SH_ALTERNATE_MOBILE).equals("")) {
            edAltermob.setText(pref.getString(getActivity(), U.SH_ALTERNATE_MOBILE));
        }

        if (!pref.getString(getActivity(), U.SH_MOBILE_SHOWN).equals("")) {
            String detailsShown = pref.getString(getActivity(), U.SH_MOBILE_SHOWN);
            if (detailsShown.equals("0")) mobileShow.setChecked(false);
            else mobileShow.setChecked(true);
            jumpDrawablesToCurrentState(mobileShow);
        } else {
            pref.putString(getActivity(), U.SH_MOBILE_SHOWN, "1");
            mobileShow.setChecked(true);
            jumpDrawablesToCurrentState(mobileShow);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void emailPermissionFun() {
        if (getActivity() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getActivity().checkSelfPermission(android.Manifest.permission.GET_ACCOUNTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
                    dialog.setContentView(R.layout.permission_dialog_layout);
                    dialog.setCancelable(false);
                    TextView text = dialog.findViewById(R.id.text);
                    text.setText(getResources().getString(R.string.email_permission_text));
                    Button ok = dialog.findViewById(R.id.permission_ok);
                    Button cancel = dialog.findViewById(R.id.permission_cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (pref.getInt(getActivity(), U.SH_EMAIL_PERMISSION_DIA_SHOW) == 2) {
                                Intent intent = new Intent();
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                                dialog.dismiss();
                            } else {
                                requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS}, 153);
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.show();
                } else {
//                getmaildia();
                    chooseAccount();
                }
            } else {
                getmaildia();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void imgviewdia() {
        if(getActivity()!=null) {
            final Dialog viewimgdia = new Dialog(getActivity(), android.R.style.Theme_NoTitleBar_Fullscreen);
            viewimgdia.setContentView(R.layout.viewimage);
            ImageView img_vimgback;
            final TouchImageView img_zview;
            img_zview = viewimgdia.findViewById(R.id.img_zview);
            img_vimgback = viewimgdia.findViewById(R.id.img_vimgback);

            img_vimgback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewimgdia.dismiss();
                }
            });

            if (!pref.getString(getActivity(), U.SH_PHOTO_URI).equals("")) {

                String photoUri = pref.getString(getActivity(), U.SH_PHOTO_URI);
                Glide.with(this)
                        .load(photoUri)
                        .asBitmap()
                        .placeholder(R.drawable.user)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .centerCrop()
                        .into(img_zview);

            } else if (pref.getBoolean(getActivity(), U.SH_SIGN_UP_SUCCESS)) {

                String photoValue = pref.getString(getActivity(), U.SH_PHOTO);
                Glide.with(this)
                        .load(SU.BASE_URL + photoValue)
                        .asBitmap()
                        .placeholder(R.drawable.user)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .centerCrop()
                        .into(img_zview);
            }
            viewimgdia.show();
        }
    }

    public void reselectimg() {
        final CharSequence[] items = {"View picture", "Choose from Gallery", "Remove Picture"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Please Select");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("View picture")) {
                    dialog.dismiss();
                    imgviewdia();
                } else if (items[item].equals("Choose from Gallery")) {
                    GalleryPermissionFun();
                } else if (items[item].equals("Remove Picture")) {
                    profilePhoto.setImageResource(R.drawable.user);
                    flip = 0;
                    pref.putString(getActivity(), U.SH_PHOTO_URI, "");
                    pref.putString(getActivity(), U.SH_PHOTO, "");
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        int SELECT_FILE = 1;
        startActivityForResult(galleryIntent, SELECT_FILE);
    }

    public void GalleryPermissionFun() {
        if (getActivity() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
                    dialog.setContentView(R.layout.permission_dialog_layout);
                    dialog.setCancelable(false);
                    TextView txt = dialog.findViewById(R.id.text);
                    if (pref.getInt(getActivity(), U.SH_PHOTO_PERMISSION_DIA_SHOW) == 2) {

                        txt.setText(getResources().getString(R.string.storage_permission_settings_text));
                    } else {

                        txt.setText(getResources().getString(R.string.storage_permission_text));
                    }
                    Button ok = dialog.findViewById(R.id.permission_ok);
                    Button cancel = dialog.findViewById(R.id.permission_cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (pref.getInt(getActivity(), U.SH_PHOTO_PERMISSION_DIA_SHOW) == 2) {
                                Intent intent = new Intent();
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                                dialog.dismiss();
                            } else {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 154);
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.show();
                } else {

                    galleryIntent();

                }
            } else {
                galleryIntent();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            pref.putString(getActivity(), U.SH_PHOTO, "");
            pref.putString(getActivity(), U.SH_PHOTO_URI, "");

            String root = Environment.getExternalStorageDirectory().toString();
            File mydir = new File(root + "/Nithra/Jobs");
            mydir.mkdirs();
            String fname = "ProfilePhoto.jpg";
            final File file = new File(mydir, fname);
            Intent intent1 = new Intent("com.android.camera.action.CROP");
            intent1.setType("image/*");
            intent1.setData(data.getData()); // Uri to the image you want to crop
            intent1.putExtra("scale", true);
            intent1.putExtra("circleCrop", "");
            intent1.putExtra("return-data", false);
            intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(intent1, PIC_CROP);

        } else if (requestCode == PIC_CROP && resultCode == Activity.RESULT_OK) {

            File root = Environment.getExternalStorageDirectory();
            File fil = new File(root.getAbsolutePath() + "/Nithra/Jobs/ProfilePhoto.jpg");
            String fi = fil.getPath();
            Bitmap bmp = U.compressImage(getActivity(), String.valueOf(fi));
            bmp = CompressResizeImage(bmp);
            RegistrationActivity.photo = bmp == null ? "" : getStringImage(bmp);
            pref.putString(getActivity(), U.SH_PHOTO, RegistrationActivity.photo);
            pref.putString(getActivity(), U.SH_PHOTO_URI, fi);
            Log.e("photoURi", pref.getString(getActivity(), U.SH_PHOTO_URI));
            Glide.with(this)
                    .load(fi)
                    .asBitmap()
                    .placeholder(R.drawable.user)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .into(new BitmapImageViewTarget(profilePhoto) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            profilePhoto.setImageDrawable(circularBitmapDrawable);
                        }
                    });
            flip = 1;
        } else if (requestCode == 190 && resultCode == Activity.RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            edEmail.setText(accountName);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 153: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pref.putInt(getActivity(), U.SH_EMAIL_PERMISSION_DIA_SHOW, 1);
//                    getmaildia();
                    chooseAccount();
                } else {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale = shouldShowRequestPermissionRationale(permissions[0]);
                        if (!showRationale) {
                            pref.putInt(getActivity(), U.SH_EMAIL_PERMISSION_DIA_SHOW, 2);
                        } else if (Manifest.permission.GET_ACCOUNTS.equals(permissions[0])) {
                            pref.putInt(getActivity(), U.SH_EMAIL_PERMISSION_DIA_SHOW, 0);
                        }
                    }
                }
            }
            break;
            case 154: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pref.putInt(getActivity(), U.SH_PHOTO_PERMISSION_DIA_SHOW, 1);
                    galleryIntent();
                } else {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale = shouldShowRequestPermissionRationale(permissions[0]);
                        if (!showRationale) {
                            pref.putInt(getActivity(), U.SH_PHOTO_PERMISSION_DIA_SHOW, 2);
                        } else if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permissions[0])) {
                            pref.putInt(getActivity(), U.SH_PHOTO_PERMISSION_DIA_SHOW, 0);
                        }
                    }
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @SuppressLint("SetTextI18n")
    public void getmaildia() {
        final List<String> mailid = getaccount();
        if (!mailid.isEmpty() && getActivity() != null) {
            final Dialog diagmail = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
            diagmail.setContentView(R.layout.share_dialog);
            ListView list_getmail = diagmail.findViewById(R.id.list);
            TextView txtnone = diagmail.findViewById(R.id.txt_none);
            TextView txtTitle = diagmail.findViewById(R.id.title_text);
            txtTitle.setText("Continue with :");
            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mailid);
            list_getmail.setAdapter(itemsAdapter);

            list_getmail.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    String Slecteditem = mailid.get(position);
                    edEmail.setText(Slecteditem);
                    diagmail.dismiss();
                }
            });

            txtnone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edEmail.setText("");
                    diagmail.dismiss();
                }
            });

            diagmail.show();
        }
    }

    public List<String> getaccount() {
        List<String> mailid = new ArrayList<>();
        mailid.clear();
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(getActivity()).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                mailid.add(possibleEmail);
            }
        }

        for (int i = 1; i < mailid.size(); i++) {
            String a1 = mailid.get(i);
            String a2 = mailid.get(i - 1);
            if (a1.equals(a2)) {
                mailid.remove(a1);
            }
        }
        return mailid;
    }

    private void chooseAccount() {
        Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
                false, null, null, null, null);
        startActivityForResult(intent, 190);
    }

    //--------------------------profile photo ---------------------------------

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.WEBP, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public Bitmap CompressResizeImage(Bitmap bm) {
        int bmWidth = bm.getWidth();
        int bmHeight = bm.getHeight();
        int ivWidth = profilePhoto.getWidth();

        int new_height = (int) Math.floor((double) bmHeight * ((double) ivWidth / (double) bmWidth));
        Bitmap newbitMap = Bitmap.createScaledBitmap(bm, ivWidth, new_height, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        newbitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();

        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    @Override
    public void fragmentBecameVisible() {
    }
}