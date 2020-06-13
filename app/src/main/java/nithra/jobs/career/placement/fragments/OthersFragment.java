package nithra.jobs.career.placement.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import nithra.jobs.career.placement.Interface.FragmentInterface;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.activity.RegistrationActivity;
import nithra.jobs.career.placement.networking.MySingleton;
import nithra.jobs.career.placement.pojo.ArrayItem;
import nithra.jobs.career.placement.pojo.Item;
import nithra.jobs.career.placement.utills.FlexboxLayout;
import nithra.jobs.career.placement.utills.L;
import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

import static androidx.core.view.ViewCompat.jumpDrawablesToCurrentState;

/**
 * Created by nithra-apps on 14/11/17.
 */

public class OthersFragment extends Fragment implements FragmentInterface {

    private SharedPreference pref;
    private TextView txtSkills, txtJoblocation, txtPpolicy, tctext, txtjobCat, txtTitle, uploadResumeTxt;
    private RadioGroup workStatusGroup;
    private RadioButton fresher, experienced;
    private CheckBox detailShow, ppShown, tcShown;
    private CardView resumeUploadCard;
    private ListView listview;
    private List<String> selectedSkills, selectedLocation, selectedCategory, selectedTitle;
    private List<Item> allSkills, allLocation, fileLists;
    private FlexboxLayout fblSkills, fblLocation, fblCategory, fblTitle;
    private LinearLayout listLay;
    private SkillsSelectionAdapter skilladapter;
    private LocationSelectionAdapter locationadapter;
    private CheckBox skillDetail, all;
    private Dialog allSkillsDia, allLocationDia, skillsDia, locationDia, allCategoryDia, allTitleDia, allFilesDia;
    private int selectedAll = 0;
    private FileSelectionAdapter fileAdapter;
    private ImageView uploadImage;
    private int Upload_Download = 0;
    private OnRegistrationListener onRegistrationListener;

    public OthersFragment() {
        // Required empty public constructor
    }

    public static OthersFragment newInstance() {
        return new OthersFragment();
    }

    public static boolean exists(String URLName) {
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con =
                    (HttpURLConnection) new URL(URLName).openConnection();
            con.setRequestMethod("HEAD");
            Log.e("urlExists", "exists");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("urlExists", "not_exists");
            return false;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onRegistrationListener = (OnRegistrationListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (getActivity() != null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
        return inflater.inflate(R.layout.other_details_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = new SharedPreference();
        fileLists = new ArrayList<>();
        uploadResumeTxt = view.findViewById(R.id.uploadResumeTxt);
        uploadImage = view.findViewById(R.id.uploadImage);
        resumeUploadCard = view.findViewById(R.id.resumeUploadCard);
        txtSkills = view.findViewById(R.id.txtSkills);
        txtjobCat = view.findViewById(R.id.txtjobCat);
        txtJoblocation = view.findViewById(R.id.txtJoblocation);
        txtTitle = view.findViewById(R.id.txtjobTitle);
        detailShow = view.findViewById(R.id.detailshow);
        ppShown = view.findViewById(R.id.privacypolicy);
        tcShown = view.findViewById(R.id.tc);
        workStatusGroup = view.findViewById(R.id.workstatus_group);
        fresher = view.findViewById(R.id.fresher);
        experienced = view.findViewById(R.id.experienced);
        txtPpolicy = view.findViewById(R.id.privacypolicytext);
        txtPpolicy.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tctext = view.findViewById(R.id.tctext);
        tctext.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        fblSkills = view.findViewById(R.id.fbl_skills);
        fblLocation = view.findViewById(R.id.fbl_location);
        fblCategory = view.findViewById(R.id.fbl_category);
        fblTitle = view.findViewById(R.id.fbl_title);

        selectedSkills = new ArrayList<>();
        selectedLocation = new ArrayList<>();
        selectedCategory = new ArrayList<>();
        selectedTitle = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        allSkills = loadAllSkills();
        allLocation = loadAllLocation();

        //for title process
        if (RegistrationActivity.titleList.size() != 0) {
            RegistrationActivity.titleList_Sub.clear();
            RegistrationActivity.titleList_Sub.addAll(RegistrationActivity.titleList);
        }

        workStatusGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonID = group.getCheckedRadioButtonId();
                View radioButton = group.findViewById(radioButtonID);
                RegistrationActivity.workStatus = String.valueOf(group.indexOfChild(radioButton));
                pref.putString(getActivity(), U.SH_WORK_STATUS, RegistrationActivity.workStatus);
                onRegistrationListener.onPercentageChange();
            }
        });

        detailShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    RegistrationActivity.detailsShown = "1";
                } else {
                    RegistrationActivity.detailsShown = "0";
                }
                pref.putString(getActivity(), U.SH_DETAILS_SHOWN, RegistrationActivity.detailsShown);
            }
        });

        ppShown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    RegistrationActivity.ppshown = "1";
                } else {
                    RegistrationActivity.ppshown = "0";
                }
                pref.putString(getActivity(), U.SH_PRIVACY_POLICY, RegistrationActivity.ppshown);
            }
        });

        tcShown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    RegistrationActivity.tcshown = "1";
                } else {
                    RegistrationActivity.tcshown = "0";
                }
                pref.putString(getActivity(), U.SH_TERMS_CONDITIONS, RegistrationActivity.tcshown);
            }
        });

        txtSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RegistrationActivity.skillsList.size() > 0) {
                    txtSkills.setEnabled(false);
                    ShowSkillsDialog();
                } else {
                    L.t(getActivity(), "Data Loading,Request one more time");
                }
            }
        });

        txtjobCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RegistrationActivity.jobcategoryList.size() > 0) {
                    txtjobCat.setEnabled(false);
                    wholeCategoryDialog();
                } else {
                    L.t(getActivity(), "Data Loading,Request one more time");
                }
            }
        });

        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (U.isNetworkAvailable(getActivity())) {
                    if (!pref.getString(getActivity(), U.SH_CATEGORY).equals("")) {
                        if (RegistrationActivity.jobcategoryList.size() > 0) {
                            txtTitle.setEnabled(false);
                            loadJSON(SU.GETCATTITLE, 0);
                        }
                    } else {
                        L.t(getActivity(), "Choose Job Category to select preferred Job Title");
                    }
                } else {
                    L.t(getActivity(), U.INA);
                }
            }
        });

        txtJoblocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RegistrationActivity.locationList.size() > 0) {
                    txtJoblocation.setEnabled(false);
                    ShowLocationDialog();
                } else {
                    L.t(getActivity(), "Data Loading,Request one more time");
                }
            }
        });

        txtPpolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (U.isNetworkAvailable(getActivity())) {
                    showPrivacy(SU.JOBS_PRIVACY);
                } else L.t(getActivity(), U.INA);
            }
        });

        tctext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (U.isNetworkAvailable(getActivity())) {
                    showPrivacy(SU.JOBS_TC);
                } else L.t(getActivity(), U.INA);
            }
        });

        resumeUploadCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!pref.getString(getActivity(), U.SH_RESUME).equals("")) {
                    reselectPDF();
                    Log.e("PDF", "present");
                } else {
                    Upload_Download = 0;
                    Log.e("PDF", "absent");
                    PermissionFun();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!pref.getString(getActivity(), U.SH_WORK_STATUS).equals("")) {
            String workStatus = pref.getString(getActivity(), U.SH_WORK_STATUS);
            if (workStatus.equals("0")) {
                fresher.setChecked(true);
                jumpDrawablesToCurrentState(fresher);
            } else {
                experienced.setChecked(true);
                jumpDrawablesToCurrentState(experienced);
            }
        }

        if (!pref.getString(getActivity(), U.SH_DETAILS_SHOWN).equals("")) {
            String detailsShown = pref.getString(getActivity(), U.SH_DETAILS_SHOWN);
            if (detailsShown.equals("0")) detailShow.setChecked(false);
            else detailShow.setChecked(true);
            jumpDrawablesToCurrentState(detailShow);
        } else {
            pref.putString(getActivity(), U.SH_DETAILS_SHOWN, "1");
            detailShow.setChecked(true);
            jumpDrawablesToCurrentState(detailShow);
        }

        if (!pref.getString(getActivity(), U.SH_PRIVACY_POLICY).equals("")) {
            pref.putString(getActivity(), U.SH_PRIVACY_POLICY, "1");
            String pShown = pref.getString(getActivity(), U.SH_PRIVACY_POLICY);
            if (pShown.equals("0")) ppShown.setChecked(false);
            else ppShown.setChecked(true);
            jumpDrawablesToCurrentState(ppShown);
        } else {
            pref.putString(getActivity(), U.SH_PRIVACY_POLICY, "1");
            ppShown.setChecked(true);
            jumpDrawablesToCurrentState(ppShown);
        }

        if (!pref.getString(getActivity(), U.SH_TERMS_CONDITIONS).equals("")) {
            pref.putString(getActivity(), U.SH_TERMS_CONDITIONS, "1");
            String pShown = pref.getString(getActivity(), U.SH_TERMS_CONDITIONS);
            if (pShown.equals("0")) tcShown.setChecked(false);
            else tcShown.setChecked(true);
            jumpDrawablesToCurrentState(tcShown);
        } else {
            pref.putString(getActivity(), U.SH_TERMS_CONDITIONS, "1");
            tcShown.setChecked(true);
            jumpDrawablesToCurrentState(tcShown);
        }

        if (!pref.getString(getActivity(), U.SH_SKILLS).equals("")) {
            RegistrationActivity.skills = pref.getString(getActivity(), U.SH_SKILLS);
            if (!RegistrationActivity.skills.equals("0")) {
                String[] skillsArray = RegistrationActivity.skills.split(",");
                selectedSkills.clear();
                selectedSkills.addAll(Arrays.asList(skillsArray));
                setSkillsValues(fblSkills);
            }
        }

        if (!pref.getString(getActivity(), U.SH_JOBLOCATION).equals("")) {
            RegistrationActivity.jobPrefferedLocation = pref.getString(getActivity(), U.SH_JOBLOCATION);
            String[] locationArray = RegistrationActivity.jobPrefferedLocation.split(",");
            selectedLocation.clear();
            selectedLocation.addAll(Arrays.asList(locationArray));
            setLocationValues(fblLocation);
        }

        if (!pref.getString(getActivity(), U.SH_CATEGORY).equals("")) {
            RegistrationActivity.category = pref.getString(getActivity(), U.SH_CATEGORY);
            String[] categoryArray = RegistrationActivity.category.split(",");
            selectedCategory.clear();
            selectedCategory.addAll(Arrays.asList(categoryArray));
            Log.e("CatArray", "" + selectedCategory);
            setCategoryValues(fblCategory);
        }

        if (!pref.getString(getActivity(), U.SH_TITLE).equals("")) {
            Log.e("cattitle", "" + pref.getString(getActivity(), U.SH_TITLE));
            RegistrationActivity.cattitle = pref.getString(getActivity(), U.SH_TITLE);
            String[] titleArray = RegistrationActivity.cattitle.split(",");
            selectedTitle.clear();
            selectedTitle.addAll(Arrays.asList(titleArray));
            setTitleValues(fblTitle);
        }

        if (!pref.getString(getActivity(), U.SH_RESUME).equals("")) {
            RegistrationActivity.resumePdf = pref.getString(getActivity(), U.SH_RESUME);
            String pdfPath = RegistrationActivity.resumePdf.substring(RegistrationActivity.resumePdf.lastIndexOf('/') + 1);
            Log.e("pdfPath", "Path --" + pdfPath);
            uploadResumeTxt.setText(pdfPath);
            Log.e("PDF", "present -- onResume");
            uploadImage.setImageResource(R.drawable.ic_draft);
        } else {
            uploadResumeTxt.setText("Upload Your Resume");
            Log.e("PDF", "absent -- onResume");
            uploadImage.setImageResource(R.drawable.ic_cloud_upload);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void fragmentBecameVisible() {

    }

    //------------------------------------------- Resume upload & download -------------------------

    private void PermissionFun() {
        if (getActivity() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
                    dialog.setContentView(R.layout.permission_dialog_layout);
                    dialog.setCancelable(false);
                    TextView txt = dialog.findViewById(R.id.text);

                    if (pref.getInt(getActivity(), U.SH_STORAGE_PERMISSION) == 2) {
                        txt.setText(getResources().getString(R.string.resume_permission_settings_text));
                    } else {
                        txt.setText(getResources().getString(R.string.resume_permission_text));
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
                            if (pref.getInt(getActivity(), U.SH_STORAGE_PERMISSION) == 2) {
                                Intent intent = new Intent();
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                                dialog.dismiss();
                            } else {
                                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 154);
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.show();
                } else {
                    if (Upload_Download == 0) {
                        uploadResume();
                    } else {
                        openResume();
                    }
                }
            } else {
                if (Upload_Download == 0) {
                    uploadResume();
                } else {
                    openResume();
                }
            }
        }
    }

    private void uploadResume() {
        resumeUploadCard.setEnabled(false);
        fileLists.clear();
        Search_Dir(Environment.getExternalStorageDirectory());
        showFilesDia();
    }

    private void Search_Dir(File dir) {
        File[] FileList = dir.listFiles();
        if (FileList != null) {
            for (File file : FileList) {
                if (file.isDirectory()) {
                    Search_Dir(file);
                } else {
                    if (file.getName().endsWith(".pdf") || file.getName().endsWith(".doc") || file.getName().endsWith(".docx")) {

                        long filesize1 = file.length() / 1024;
                        Log.e("pdfFiles1", file.length() + " -->> " + filesize1);
                        long filesize2 = filesize1 / 1024;
                        Log.e("pdfFiles2", file.length() + " -->> " + filesize2);

                        Item item = new Item();
                        if (filesize2 != 0) {
                            item.setId(2);
                            item.setSize(filesize2);
                        } else {
                            item.setId(1);
                            item.setSize(filesize1);
                        }
                        item.setItem(file.getName());
                        item.setCount(file.getAbsolutePath());
                        fileLists.add(item);

                    }
                }

            }
        }
    }

    private boolean copyFile(String filePath) {
        File sourceLocation = new File(filePath);

        File root = Environment.getExternalStorageDirectory();
        File mydir = new File(root + "/Nithra/Jobs/Resume");
        mydir.mkdirs();

        String copyFileName = filePath.substring(filePath.lastIndexOf('/'));//Create file name by picking download file name from URL

        File targetLocation = new File(Environment.getExternalStorageDirectory()
                + "/Nithra/Jobs/Resume", copyFileName);

        Log.e("sourceLocation", "sourceLocation: " + sourceLocation);
        Log.e("targetLocation", "targetLocation: " + targetLocation);

        if (sourceLocation.exists()) {

            InputStream in = null;
            try {
                in = new FileInputStream(sourceLocation);
                OutputStream out = new FileOutputStream(targetLocation);

                byte[] buf = new byte[1024];
                int len;
                try {
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }

                    in.close();
                    out.close();

                    Log.e("Location", "Copy file successful.");

                    RegistrationActivity.resumePdf = Environment.getExternalStorageDirectory() + "/Nithra/Jobs/Resume/" + copyFileName;

                    return true;

                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }

        } else {
            Log.e("Location", "Copy file failed. Source file missing.");
        }

        return true;
    }

    @SuppressLint("SetTextI18n")
    private void showFilesDia() {
        allFilesDia = new Dialog(getActivity());
        allFilesDia.setContentView(R.layout.custom_dialog);

        Log.e("fileList", "size :" + fileLists.size());

        // searchView
        final SearchView searchView = allFilesDia.findViewById(R.id.searchview);
        searchView.setQueryHint("Search...");

        TextView headertext = allFilesDia.findViewById(R.id.text_header);
        headertext.setText("Choose your Resume :");

        LinearLayout bottomLay = allFilesDia.findViewById(R.id.bottom_lay);
        bottomLay.setVisibility(View.GONE);
        allFilesDia.findViewById(R.id.set_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery("", true);
            }
        });
        allFilesDia.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery("", true);
                allFilesDia.dismiss();
            }
        });

        allFilesDia.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                searchView.setQuery("", true);
                resumeUploadCard.setEnabled(true);
            }
        });

        listview = allFilesDia.findViewById(R.id.listview);
        //Ascending order
        Collections.sort(fileLists, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getItem().compareTo(o2.getItem());
            }
        });

        fileAdapter = new FileSelectionAdapter(getActivity(), R.layout.filter_layout, fileLists, searchView, listview);
        listview.setTextFilterEnabled(true);
        listview.setAdapter(fileAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    fileAdapter.filter(newText.trim());
                } catch (Exception e) {
                    Log.e("Error", "" + e);
                }
                return true;
            }
        });

        allFilesDia.show();
    }

    private void openResume() {
        String resumePath = "";
        if (pref.getString(getActivity(), U.SH_RESUME).equals("")) {
            uploadImage.setImageResource(R.drawable.ic_cloud_upload);
            uploadResumeTxt.setText("Upload Your Resume");
            Log.e("PDF", "absent -- openResume");
            onRegistrationListener.onPercentageChange();
        } else if (pref.getString(getActivity(), U.SH_RESUME).contains("resumeuploads/" + pref.getString(getActivity(), U.SH_MOBILE))) {
            if (!pref.getString(getActivity(), U.SH_RESUME).contains("https")) {
                resumePath = SU.BASE_URL + pref.getString(getActivity(), U.SH_RESUME);
            } else {
                resumePath = pref.getString(getActivity(), U.SH_RESUME);
            }

            Log.e("resumePath", "" + resumePath);
            String fn = resumePath.substring(resumePath.lastIndexOf('/') + 1);
            File file = new File(Environment.getExternalStorageDirectory()
                    + "/Nithra/Jobs/Resume/" + fn);
            Log.e("Open_filename", "" + file.getAbsolutePath());
            if (file.exists()) {
                Uri path = Uri.fromFile(file);
                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pdfOpenintent.setDataAndType(path, "application/*");
                try {
                    startActivity(pdfOpenintent);
                    Log.e("openresumePath", "" + path);
                } catch (ActivityNotFoundException e) {
                    L.t(getActivity(), "No PDF Found");
                    Log.e("exists_filename_catch", "" + e);
                }
            } else {
                if (U.isNetworkAvailable(getActivity())) {
                    if (exists(resumePath)) {
                        new DownloadFile().execute(resumePath);
                        Log.e("pdfUrl", "" + resumePath);
                    } else {
                        Log.e("not_exists_filename", "pdfUrl not exists");
                        L.t(getActivity(), "No PDF Found");
                    }
                } else {
                    L.t(getActivity(), U.INA);
                }
            }
        } else if (pref.getString(getActivity(), U.SH_RESUME).contains("resume/" + pref.getString(getActivity(), U.SH_MOBILE))) {
            if (!pref.getString(getActivity(), U.SH_RESUME).contains("https")) {
                resumePath = SU.BASE_URL + pref.getString(getActivity(), U.SH_RESUME);
            } else {
                resumePath = pref.getString(getActivity(), U.SH_RESUME);
            }

            Log.e("resumePath", "" + resumePath);
            String fn = resumePath.substring(resumePath.lastIndexOf('/') + 1);
            File file = new File(Environment.getExternalStorageDirectory()
                    + "/Nithra/Jobs/Resume/" + fn);
            Log.e("Open_filename", "" + file.getAbsolutePath());
            if (file.exists()) {
                Uri path = Uri.fromFile(file);
                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pdfOpenintent.setDataAndType(path, "application/*");
                try {
                    startActivity(pdfOpenintent);
                    Log.e("openresumePath", "" + path);
                } catch (ActivityNotFoundException e) {
                    L.t(getActivity(), "No PDF Found");
                    Log.e("exists_filename_catch", "" + e);
                }
            } else {
                if (U.isNetworkAvailable(getActivity())) {
                    if (exists(resumePath)) {
                        new DownloadFile().execute(resumePath);
                        Log.e("pdfUrl", "" + resumePath);
                    } else {
                        Log.e("not_exists_filename", "pdfUrl not exists");
                        L.t(getActivity(), "No PDF Found");
                    }
                } else {
                    L.t(getActivity(), U.INA);
                }
            }
        } else {
            resumePath = pref.getString(getActivity(), U.SH_RESUME);
            File file = new File(resumePath);
            Log.e("filename", "" + file.getPath());
            if (file.exists()) {
                Uri path = Uri.fromFile(file);
                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pdfOpenintent.setDataAndType(path, "application/*");
                try {
                    startActivity(pdfOpenintent);
                } catch (ActivityNotFoundException e) {
                    Log.e("exists_filename_catch", "" + e);
                    L.t(getActivity(), "No PDF Found");
                }
            }
        }
    }

    private void deleteResume(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory()
                + "/Nithra/Jobs/Resume/" + fileName);
        if (file.exists()) {
            Log.e("filename", "" + fileName);
            file.delete();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 154: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pref.putInt(getActivity(), U.SH_STORAGE_PERMISSION, 1);
                    if (Upload_Download == 0) {
                        uploadResume();
                    } else {
                        PermissionFun();
                    }
                } else {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale = shouldShowRequestPermissionRationale(permissions[0]);
                        if (!showRationale) {
                            pref.putInt(getActivity(), U.SH_STORAGE_PERMISSION, 2);
                        } else if (android.Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permissions[0])) {
                            pref.putInt(getActivity(), U.SH_STORAGE_PERMISSION, 0);
                        }
                    }
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void reselectPDF() {
        final CharSequence[] items = {"View Resume", "Change Resume", "Remove"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Please Select");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("View Resume")) {
                    dialog.dismiss();
                    Upload_Download = 1;
                    PermissionFun();
                } else if (items[item].equals("Change Resume")) {
                    dialog.dismiss();
                    Upload_Download = 0;
                    PermissionFun();
                } else if (items[item].equals("Remove")) {
                    deleteResume(uploadResumeTxt.getText().toString());
                    uploadImage.setImageResource(R.drawable.ic_cloud_upload);
                    uploadResumeTxt.setText("Upload Your Resume");
                    Log.e("PDF", "absent -- reselectPDF");
                    pref.putString(getActivity(), U.SH_RESUME, "");
                    dialog.dismiss();
                    onRegistrationListener.onPercentageChange();
                }
            }
        });
        builder.show();
    }

    private void showPrivacy(String url) {
        if (getActivity() != null) {
            Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
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
            dialog.show();
        }
    }

    private List<Item> loadAllSkills() {
        List<Item> allSkills = new ArrayList<>();
        if (RegistrationActivity.skillsList != null && RegistrationActivity.skillsList.size() > 0) {
            for (int i = 0; i < RegistrationActivity.skillsList.size(); i++) {
                for (int j = 0; j < RegistrationActivity.skillsList.get(i).getList().size(); j++) {
                    Item data = new Item();
                    data.setId(RegistrationActivity.skillsList.get(i).getList().get(j).getId());
                    data.setItem(RegistrationActivity.skillsList.get(i).getList().get(j).getItem());
                    allSkills.add(data);
                }
            }
        }
        return allSkills;
    }

    @SuppressLint("SetTextI18n")
    private void ShowSkillsDialog() {
        if (getActivity() != null) {
            skillsDia = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            if (skillsDia.getWindow() != null) {
                skillsDia.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            }
            skillsDia.setCancelable(true);
            skillsDia.setContentView(R.layout.selection_layout);
            final Button selectBtn = skillsDia.findViewById(R.id.set_btn);
            final ScrollView scrollview = skillsDia.findViewById(R.id.scrollview);
            TextView task = skillsDia.findViewById(R.id.task);
            task.setText("Choose your skills:");

            final TextView[] textViews = new TextView[RegistrationActivity.skillsList.size()];

            LinearLayout LocationList = skillsDia.findViewById(R.id.val_list);
            LocationList.removeAllViews();

            CheckBox all_checkbox = skillsDia.findViewById(R.id.all_checkbox);
            all_checkbox.setVisibility(View.GONE);

            final SearchView searchView = skillsDia.findViewById(R.id.searchview);
            searchView.setIconifiedByDefault(false);
            searchView.setSubmitButtonEnabled(false);
            searchView.setQueryHint("Search...");
            searchView.setQuery("", true);

            final ImageView actionSearch = skillsDia.findViewById(R.id.action_search);
            actionSearch.setVisibility(View.VISIBLE);
            actionSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wholeSkillsDialog();
                }
            });

            TextView suggest = skillsDia.findViewById(R.id.suggest);
            suggest.setVisibility(View.VISIBLE);
            suggest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null) {
                        final Dialog suggestDia = new Dialog(getActivity());
                        suggestDia.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        suggestDia.setContentView(R.layout.extra_skills_lay);
                        if (suggestDia.getWindow() != null) {
                            suggestDia.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        }
                        final EditText localEditText = suggestDia.findViewById(R.id.user_input);
                        final ListView localListView = suggestDia.findViewById(R.id.list);
                        List localArrayList = new ArrayList();
                        localArrayList.addAll(allSkills);
                        final suggestionAdapter localsuggestionAdapter = new suggestionAdapter(getActivity(), R.layout.extra_skills_lay, localEditText, localListView, localArrayList);
                        localListView.setAdapter(localsuggestionAdapter);
                        localListView.setVisibility(View.GONE);
                        localEditText.addTextChangedListener(new TextWatcher() {
                            public void afterTextChanged(Editable paramAnonymous2Editable) {
                            }

                            public void beforeTextChanged(CharSequence paramAnonymous2CharSequence, int paramAnonymous2Int1, int paramAnonymous2Int2, int paramAnonymous2Int3) {
                            }

                            public void onTextChanged(CharSequence paramAnonymous2CharSequence, int paramAnonymous2Int1, int paramAnonymous2Int2, int paramAnonymous2Int3) {
                                try {
                                    localListView.setVisibility(View.VISIBLE);
                                    localsuggestionAdapter.filter("" + paramAnonymous2CharSequence);
                                } catch (Exception localException) {
                                    Log.e("Error", "" + localException);
                                }
                            }
                        });
                        Button localButton = suggestDia.findViewById(R.id.send_btn);
                        suggestDia.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
                            public void onClick(View paramAnonymous2View) {
                                suggestDia.dismiss();
                            }
                        });
                        localEditText.addTextChangedListener(new TextWatcher() {
                            public void afterTextChanged(Editable paramAnonymous2Editable) {
                            }

                            public void beforeTextChanged(CharSequence paramAnonymous2CharSequence, int paramAnonymous2Int1, int paramAnonymous2Int2, int paramAnonymous2Int3) {
                            }

                            public void onTextChanged(CharSequence paramAnonymous2CharSequence, int paramAnonymous2Int1, int paramAnonymous2Int2, int paramAnonymous2Int3) {
                                if (paramAnonymous2Int3 == 0) {
                                    localListView.setVisibility(View.GONE);
                                }
                            }
                        });
                        localButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View paramAnonymous2View) {
                                String str1 = localEditText.getText().toString();
                                Log.e("valuee", "" + str1);
                                if (!str1.isEmpty()) {
                                    for (int i = 0; i < RegistrationActivity.skillsList.size(); i++) {
                                        final List localList = RegistrationActivity.skillsList.get(i).getList();
                                        for (int j = 0; j < localList.size(); j++) {
                                            String str2 = RegistrationActivity.skillsList.get(i).getList().get(j).getItem();
                                            if (str1.equals("" + str2)) {
                                                if (getActivity() != null) {
                                                    AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
                                                    final int k = j;
                                                    final int m = i;
                                                    localBuilder.setMessage("நீங்கள் குறிப்பிட்ட திறன் எங்கள் தொகுப்பில் " + RegistrationActivity.skillsList.get(i).getItem() + " என்ற பகுதியில் உள்ளது.இந்த திறனை தேர்வு செய்ய விரும்புகிறீர்களா?")
                                                            .setCancelable(false)
                                                            .setPositiveButton("ஆம்", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface paramAnonymous3DialogInterface, int paramAnonymous3Int) {
                                                                    if (OthersFragment.this.selectedSkills.contains("" + ((Item) localList.get(k)).getId())) {
                                                                        Toast.makeText(getActivity(), "" + getActivity().getResources().getString(R.string.already_added), Toast.LENGTH_SHORT).show();
                                                                    } else {
                                                                        pref.putString(OthersFragment.this.getActivity(), U.SH_ADDED_SKILLS, "");
                                                                        suggestDia.dismiss();
                                                                        skillDetail.setChecked(false);
                                                                        selectedSkills.add("" + ((Item) localList.get(k)).getId());
                                                                        skilladapter.notifyDataSetChanged();
                                                                        textViews[m].performClick();
                                                                        scrollview.scrollTo(0, textViews[m].getTop());
                                                                    }
                                                                }
                                                            }).setNegativeButton("இல்லை", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int paramAnonymous3Int) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                    AlertDialog alertDia = localBuilder.create();
                                                    alertDia.show();
                                                }
                                            }
                                        }
                                        pref.putString(getActivity(), U.SH_ADDED_SKILLS, str1);
                                        suggestDia.dismiss();
                                    }
                                } else {
                                    pref.putString(getActivity(), U.SH_ADDED_SKILLS, "");
                                    suggestDia.dismiss();
                                }
                                onRegistrationListener.onPercentageChange();
                            }
                        });
                        suggestDia.show();
                    }
                }
            });

            listLay = skillsDia.findViewById(R.id.list_lay);
            listLay.setVisibility(View.VISIBLE);
            listview = skillsDia.findViewById(R.id.listview);
            listview.setVisibility(View.VISIBLE);

            for (int i = 0; i < RegistrationActivity.skillsList.size(); i++) {
                if (!pref.getString(getActivity(), U.SH_SKILLS).equals("")) {
                    RegistrationActivity.skills = pref.getString(getActivity(), U.SH_SKILLS);
                    if (!RegistrationActivity.skills.equals("0")) {
                        String[] skillsArray = RegistrationActivity.skills.split(",");
                        selectedSkills.clear();
                        selectedSkills.addAll(Arrays.asList(skillsArray));
                        setSkillsValues(fblSkills);
                    }
                } else {
                    selectedSkills.clear();
                }
                final TextView temp = new TextView(getActivity());
                temp.setText(RegistrationActivity.skillsList.get(i).getItem().toUpperCase());
                temp.setId(i);
                temp.setBackgroundColor(getResources().getColor(R.color.light_orange));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 115);
                params.setMargins(10, 10, 10, 10);
                params.weight = 1;
                temp.setLayoutParams(params);
                temp.setPadding(0, 0, 0, 0);
                temp.setTextSize(12);
                temp.setGravity(Gravity.CENTER);
                temp.setBackgroundResource(R.drawable.bg_yellow_thick);
                temp.setTextColor(getActivity().getResources().getColor(R.color.black));
                temp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchView.setQuery("", true);
                        for (int i = 0; i < RegistrationActivity.skillsList.size(); i++) {
                            textViews[i].setBackgroundResource(R.drawable.bg_yellow_thick);
                        }
                        temp.setBackgroundResource(R.drawable.bg_black_line);

                        //Ascending order
                        Collections.sort(RegistrationActivity.skillsList.get(temp.getId()).getList(), new Comparator<Item>() {
                            @Override
                            public int compare(Item o1, Item o2) {
                                return o1.getItem().compareTo(o2.getItem());
                            }
                        });

                        skilladapter = new SkillsSelectionAdapter(getActivity(), R.layout.filter_layout,
                                RegistrationActivity.skillsList.get(temp.getId()).getList(), searchView);
                        listview.setTextFilterEnabled(true);
                        listview.setAdapter(skilladapter);

                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                try {
                                    skilladapter.filter(newText.trim());
                                } catch (Exception e) {
                                    Log.e("Error", "" + e);
                                }
                                return true;
                            }
                        });
                    }
                });
                textViews[i] = temp;
                LocationList.addView(temp);
            }
            selectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("done", "clicked");
                    searchView.setQuery("", true);
                    if (!pref.getString(OthersFragment.this.getActivity(), U.SH_SKILLS).equals("0")) {
                        if (OthersFragment.this.selectedSkills.isEmpty()) {
                            RegistrationActivity.skills = "";
                            pref.putString(getActivity(), U.SH_SKILLS, "");
                            Toast.makeText(getActivity(), "Select Any Skills", Toast.LENGTH_SHORT).show();
                        } else {
                            String filter;
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < selectedSkills.size(); i++) {
                                sb.append(selectedSkills.get(i)).append(",");
                            }
                            filter = sb.toString();
                            filter = filter.substring(0, filter.length() - 1);
                            RegistrationActivity.skills = filter;
                            if (!RegistrationActivity.skills.isEmpty()) {
                                pref.putString(getActivity(), U.SH_SKILLS, RegistrationActivity.skills);
                            } else {
                                pref.putString(getActivity(), U.SH_SKILLS, "");
                            }
                            skillsDia.dismiss();
                        }
                        setSkillsValues(fblSkills);
                    } else {
                        setSkillsValues(fblSkills);
                        skillsDia.dismiss();
                    }

                    onRegistrationListener.onPercentageChange();
                }
            });
            skillsDia.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    searchView.setQuery("", true);
                    txtSkills.setEnabled(true);
                    if (!pref.getString(getActivity(), U.SH_SKILLS).equals("")) {
                        RegistrationActivity.skills = pref.getString(getActivity(), U.SH_SKILLS);
                        if (!RegistrationActivity.skills.equals("0")) {
                            String[] skillsArray = RegistrationActivity.skills.split(",");
                            selectedSkills.clear();
                            selectedSkills.addAll(Arrays.asList(skillsArray));
                            setSkillsValues(fblSkills);
                        }
                    } else {
                        selectedSkills.clear();
                    }
                }
            });
            textViews[0].performClick();

            skillDetail = skillsDia.findViewById(R.id.checkvalue);
            skillDetail.setText("Do not Consider Skills");
            skillDetail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedSkills.clear();
                        listview.setAlpha(0.5F);
                        pref.putString(getActivity(), U.SH_SKILLS, "0");
                        skilladapter.notifyDataSetChanged();
                    } else {
                        listview.setAlpha(1.0F);
                        listLay.setVisibility(View.VISIBLE);
                        pref.putString(getActivity(), U.SH_SKILLS, "");
                        skilladapter.notifyDataSetChanged();
                    }
                }
            });
            if (pref.getString(getActivity(), U.SH_SKILLS).equals("0")) {
                skillDetail.setChecked(true);
            }

            skillsDia.show();

            if (pref.getInt(getActivity(), U.SH_GUIDE_WINDOW_2) == 0) {
                final Dialog guideWindow = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                guideWindow.setContentView(R.layout.guide_window_search);
                TextView infoText = guideWindow.findViewById(R.id.info_text);
                infoText.setText("கொடுக்கப்பட்ட திறமையிலிருந்து நீங்கள் முன்னுரிமை அளிக்கும் பத்து திறன்களை தேர்வு செய்ய வேண்டும்.");
                TextView searchText = guideWindow.findViewById(R.id.search_text);
                searchText.setText(getResources().getString(R.string.guide_all_skills));
                TextView selectText = guideWindow.findViewById(R.id.select_text);
                selectText.setText(getResources().getString(R.string.guide_select_skills));
                Button ok = guideWindow.findViewById(R.id.ok_btn);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pref.putInt(getActivity(), U.SH_GUIDE_WINDOW_2, 1);
                        guideWindow.dismiss();
                    }
                });
                guideWindow.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        pref.putInt(getActivity(), U.SH_GUIDE_WINDOW_2, 1);
                    }
                });
                guideWindow.show();
            }

        }
    }

    //-------------------------------------------- pp & tc -----------------------------------------

    @SuppressLint("SetTextI18n")
    private void wholeSkillsDialog() {
        if (getActivity() != null) {
            allSkillsDia = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            allSkillsDia.requestWindowFeature(Window.FEATURE_NO_TITLE);
            allSkillsDia.setCancelable(false);
            allSkillsDia.setContentView(R.layout.custom_dialog);
            if (allSkillsDia.getWindow() != null) {
                allSkillsDia.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            // searchView
            final SearchView searchView = allSkillsDia.findViewById(R.id.searchview);
            searchView.setQueryHint("Search...");

            TextView headertext = allSkillsDia.findViewById(R.id.text_header);
            headertext.setText("Choose your Skills :");

            LinearLayout bottomLay = allSkillsDia.findViewById(R.id.bottom_lay);
            bottomLay.setVisibility(View.VISIBLE);
            allSkillsDia.findViewById(R.id.set_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchView.setQuery("", true);
                    if (selectedSkills.isEmpty()) {
                        RegistrationActivity.skills = "";
                        pref.putString(getActivity(), U.SH_SKILLS, "");
                        Toast.makeText(getActivity(), "Select Any Skills", Toast.LENGTH_SHORT).show();
                    } else {
                        String filter;
                        StringBuilder sb = new StringBuilder();
                        Log.e("selectedskillsArray", "" + selectedSkills);
                        for (int i = 0; i < selectedSkills.size(); i++) {
                            sb.append(selectedSkills.get(i)).append(",");
                        }
                        filter = sb.toString();
                        filter = filter.substring(0, filter.length() - 1);
                        RegistrationActivity.skills = filter;
                        if (!RegistrationActivity.skills.isEmpty()) {
                            pref.putString(getActivity(), U.SH_SKILLS, RegistrationActivity.skills);
                        } else {
                            pref.putString(getActivity(), U.SH_SKILLS, "");
                        }
                        allSkillsDia.dismiss();
                        skilladapter.notifyDataSetChanged();
                        if (skillsDia != null && skillsDia.isShowing()) {
                            skillsDia.dismiss();
                        }
                    }
                    onRegistrationListener.onPercentageChange();
                    setSkillsValues(fblSkills);
                }
            });
            allSkillsDia.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (skillsDia != null && skillsDia.isShowing()) {
                        skillsDia.dismiss();
                    }
                    ShowSkillsDialog();
                    searchView.setQuery("", true);
                    allSkillsDia.dismiss();
                }
            });

            allSkillsDia.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    searchView.setQuery("", true);

                    if (!pref.getString(getActivity(), U.SH_SKILLS).equals("")) {
                        RegistrationActivity.skills = pref.getString(getActivity(), U.SH_SKILLS);
                        if (!RegistrationActivity.skills.equals("0")) {
                            String[] skillsArray = RegistrationActivity.skills.split(",");
                            selectedSkills.clear();
                            selectedSkills.addAll(Arrays.asList(skillsArray));
                            setSkillsValues(fblSkills);
                        }
                    } else {
                        selectedSkills.clear();
                    }
                }
            });

            listview = allSkillsDia.findViewById(R.id.listview);
            //Ascending order
            Collections.sort(allSkills, new Comparator<Item>() {
                @Override
                public int compare(Item o1, Item o2) {
                    return o1.getItem().compareTo(o2.getItem());
                }
            });

            final SkillsSelectionAdapter SkillAdapter = new SkillsSelectionAdapter(getActivity(), R.layout.filter_layout, allSkills, searchView);
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

            allSkillsDia.show();
        }
    }

    //------------------------------------------- skills -------------------------------------------

    @SuppressLint("SetTextI18n")
    private void setSkillsValues(final FlexboxLayout fbl) {
        final List<ArrayItem> list = new ArrayList<>();
        for (int i = 0; i < RegistrationActivity.skillsList.size(); i++) {
            List<Item> tempList1 = new ArrayList<>();
            List<Item> temp = RegistrationActivity.skillsList.get(i).getList();
            for (int j = 0; j < temp.size(); j++) {
                int id = RegistrationActivity.skillsList.get(i).getList().get(j).getId();
                if (selectedSkills.contains("" + id)) {
                    Item data = new Item();
                    data.setId(RegistrationActivity.skillsList.get(i).getList().get(j).getId());
                    data.setItem(RegistrationActivity.skillsList.get(i).getList().get(j).getItem());
                    tempList1.add(data);
                }
            }
            if (tempList1.size() > 0) {
                ArrayItem data = new ArrayItem();
                data.setId(RegistrationActivity.skillsList.get(i).getId());
                data.setItem(RegistrationActivity.skillsList.get(i).getItem());
                data.setList(tempList1);
                list.add(data);
            }
        }

        fbl.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            if (getActivity() != null) {
                @SuppressLint("InflateParams") final View subchild = getActivity().getLayoutInflater().inflate(R.layout.single_chip, null);
                final TextView tv = subchild.findViewById(R.id.txt_title);
                final ImageView remove = subchild.findViewById(R.id.remove);
                remove.setVisibility(View.GONE);
                tv.setId(i);
                tv.setText(list.get(i).getItem() + "  +");
                tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.thick_blue));
                tv.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("InflateParams")
                    @Override
                    public void onClick(View v) {
                        LayoutInflater layoutin = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        @SuppressLint("InflateParams") final View popupview;
                        if (layoutin != null) {
                            popupview = layoutin.inflate(R.layout.location_popup, null);
                            RegistrationActivity.Spopupwindow = new PopupWindow(popupview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            final FlexboxLayout fblcities = popupview.findViewById(R.id.fbl_cities);
                            final List<Item> temp = list.get(tv.getId()).getList();
                            for (int j = 0; j < temp.size(); j++) {
                                final View subchild = getActivity().getLayoutInflater().inflate(R.layout.single_chip, null);
                                TextView tv = subchild.findViewById(R.id.txt_title);
                                final ImageView remove = subchild.findViewById(R.id.remove);
                                tv.setId(j);
                                tv.setText(temp.get(j).getItem());
                                tv.setBackgroundResource(R.drawable.chip_selected);
                                tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                                final int finalJ = j;
                                remove.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                        alertDialogBuilder
                                                .setMessage("நீக்க விரும்புகிறீர்களா?")
                                                .setCancelable(false)
                                                .setPositiveButton("ஆம்", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int ids) {
                                                        selectedSkills.remove("" + temp.get(finalJ).getId());
                                                        RegistrationActivity.Spopupwindow.dismiss();
                                                        if (selectedSkills.isEmpty()) {
                                                            RegistrationActivity.skills = "";
                                                            pref.putString(getActivity(), U.SH_SKILLS, "");
                                                        } else {
                                                            String filter;
                                                            StringBuilder sb = new StringBuilder();
                                                            for (int i = 0; i < selectedSkills.size(); i++) {
                                                                sb.append(selectedSkills.get(i)).append(",");
                                                            }
                                                            filter = sb.toString();
                                                            filter = filter.substring(0, filter.length() - 1);
                                                            RegistrationActivity.skills = filter;
                                                            if (!RegistrationActivity.skills.isEmpty()) {
                                                                pref.putString(getActivity(), U.SH_SKILLS, RegistrationActivity.skills);
                                                            } else {
                                                                pref.putString(getActivity(), U.SH_SKILLS, "");
                                                            }
                                                        }
                                                        onRegistrationListener.onPercentageChange();
                                                        setSkillsValues(fblSkills);
                                                    }
                                                })
                                                .setNegativeButton("இல்லை", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        RegistrationActivity.Spopupwindow.dismiss();
                                                    }
                                                });
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();
                                    }
                                });
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        fblcities.addView(subchild);
                                    }
                                });
                            }
                            RegistrationActivity.Spopupwindow.setBackgroundDrawable(new BitmapDrawable());
                            RegistrationActivity.Spopupwindow.setOutsideTouchable(true);
                            RegistrationActivity.Spopupwindow.showAsDropDown(v);
                        }
                    }
                });
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fbl.addView(subchild);
                    }
                });
            }
        }
    }

    private List<Item> loadAllLocation() {
        List<Item> allLocation = new ArrayList<>();
        for (int i = 0; i < RegistrationActivity.locationList.size(); i++) {
            for (int j = 0; j < RegistrationActivity.locationList.get(i).getList().size(); j++) {
                Item data = new Item();
                data.setId(RegistrationActivity.locationList.get(i).getList().get(j).getId());
                data.setItem(RegistrationActivity.locationList.get(i).getList().get(j).getItem());
                allLocation.add(data);
            }
        }
        Log.e("location", "" + RegistrationActivity.locationList);
        return allLocation;
    }

    @SuppressLint("SetTextI18n")
    private void wholeLocationDialog() {
        if (getActivity() != null) {
            allLocationDia = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            allLocationDia.requestWindowFeature(Window.FEATURE_NO_TITLE);
            allLocationDia.setCancelable(false);
            allLocationDia.setContentView(R.layout.custom_dialog);
            if (allLocationDia.getWindow() != null) {
                allLocationDia.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            // searchView
            final SearchView searchView = allLocationDia.findViewById(R.id.searchview);
            searchView.setQueryHint("Search...");

            TextView headertext = allLocationDia.findViewById(R.id.text_header);
            headertext.setText("Choose your Job Location :");

            LinearLayout bottomLay = allLocationDia.findViewById(R.id.bottom_lay);
            bottomLay.setVisibility(View.VISIBLE);
            allLocationDia.findViewById(R.id.set_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchView.setQuery("", true);
                    if (selectedLocation.isEmpty()) {
                        RegistrationActivity.jobPrefferedLocation = "";
                        pref.putString(getActivity(), U.SH_JOBLOCATION, "");
                        Toast.makeText(getActivity(), "Select your preferred Job Location", Toast.LENGTH_SHORT).show();

                    } else {
                        String filter;
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < selectedLocation.size(); i++) {
                            sb.append(selectedLocation.get(i)).append(",");
                        }
                        filter = sb.toString();
                        filter = filter.substring(0, filter.length() - 1);
                        RegistrationActivity.jobPrefferedLocation = filter;
                        if (!RegistrationActivity.jobPrefferedLocation.isEmpty()) {
                            pref.putString(getActivity(), U.SH_JOBLOCATION, RegistrationActivity.jobPrefferedLocation);
                        } else {
                            pref.putString(getActivity(), U.SH_JOBLOCATION, "");
                        }
                        allLocationDia.dismiss();
                        locationadapter.notifyDataSetChanged();
                        if (locationDia != null && locationDia.isShowing()) {
                            locationDia.dismiss();
                        }
                    }
                    onRegistrationListener.onPercentageChange();
                    setLocationValues(fblLocation);
                }
            });
            allLocationDia.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (locationDia != null && locationDia.isShowing()) {
                        locationDia.dismiss();
                    }
                    ShowLocationDialog();
                    searchView.setQuery("", true);
                    allLocationDia.dismiss();
                }
            });

            allLocationDia.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    searchView.setQuery("", true);
                    if (!pref.getString(getActivity(), U.SH_JOBLOCATION).equals("")) {
                        RegistrationActivity.jobPrefferedLocation = pref.getString(getActivity(), U.SH_JOBLOCATION);
                        String[] skillsArray = RegistrationActivity.jobPrefferedLocation.split(",");
                        selectedLocation.clear();
                        selectedLocation.addAll(Arrays.asList(skillsArray));
                        setLocationValues(fblLocation);
                    } else {
                        selectedLocation.clear();
                    }
                }
            });

            listview = allLocationDia.findViewById(R.id.listview);
            //Ascending order
            Collections.sort(allLocation, new Comparator<Item>() {
                @Override
                public int compare(Item o1, Item o2) {
                    return o1.getItem().compareTo(o2.getItem());
                }
            });

            final LocationSelectionAdapter locationadapter = new LocationSelectionAdapter(getActivity(), R.layout.filter_layout, 0, allLocation, searchView);
            listview.setTextFilterEnabled(true);
            listview.setAdapter(locationadapter);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    try {
                        locationadapter.filter(newText.trim());
                    } catch (Exception e) {
                        Log.e("Error", "" + e);
                    }
                    return true;
                }
            });

            allLocationDia.show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void setLocationValues(final FlexboxLayout fbl) {
        final List<ArrayItem> list = new ArrayList<>();
        for (int i = 0; i < RegistrationActivity.locationList.size(); i++) {
            List<Item> tempList1 = new ArrayList<>();
            List<Item> temp = RegistrationActivity.locationList.get(i).getList();
            for (int j = 0; j < temp.size(); j++) {
                int id = RegistrationActivity.locationList.get(i).getList().get(j).getId();
                if (selectedLocation.contains("" + id)) {
                    Item data = new Item();
                    data.setId(RegistrationActivity.locationList.get(i).getList().get(j).getId());
                    data.setItem(RegistrationActivity.locationList.get(i).getList().get(j).getItem());
                    tempList1.add(data);
                }
            }
            if (tempList1.size() > 0) {
                ArrayItem data = new ArrayItem();
                data.setId(RegistrationActivity.locationList.get(i).getId());
                data.setItem(RegistrationActivity.locationList.get(i).getItem());
                data.setList(tempList1);
                list.add(data);
            }
        }

        fbl.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            if (getActivity() != null) {
                @SuppressLint("InflateParams") final View subchild = getActivity().getLayoutInflater().inflate(R.layout.single_chip, null);
                final TextView tv = subchild.findViewById(R.id.txt_title);
                final ImageView remove = subchild.findViewById(R.id.remove);
                remove.setVisibility(View.GONE);
                tv.setId(i);
                tv.setText(list.get(i).getItem() + "  +");
                tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.thick_blue));
                tv.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("InflateParams")
                    @Override
                    public void onClick(View v) {
                        LayoutInflater layoutin = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        @SuppressLint("InflateParams") final View popupview;
                        if (layoutin != null) {
                            popupview = layoutin.inflate(R.layout.location_popup, null);
                            RegistrationActivity.Lpopupwindow = new PopupWindow(popupview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            final FlexboxLayout fblcities = popupview.findViewById(R.id.fbl_cities);
                            final List<Item> temp = list.get(tv.getId()).getList();
                            for (int j = 0; j < temp.size(); j++) {
                                final View subchild = getActivity().getLayoutInflater().inflate(R.layout.single_chip, null);
                                TextView tv = subchild.findViewById(R.id.txt_title);
                                final ImageView remove = subchild.findViewById(R.id.remove);
                                tv.setId(j);
                                tv.setText(temp.get(j).getItem());
                                tv.setBackgroundResource(R.drawable.chip_selected);
                                tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                                final int finalJ = j;
                                remove.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                        alertDialogBuilder
                                                .setMessage("நீக்க விரும்புகிறீர்களா?")
                                                .setCancelable(false)
                                                .setPositiveButton("ஆம்", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int ids) {
                                                        selectedLocation.remove("" + temp.get(finalJ).getId());
                                                        RegistrationActivity.Lpopupwindow.dismiss();
                                                        if (selectedLocation.isEmpty()) {
                                                            RegistrationActivity.jobPrefferedLocation = "";
                                                            pref.putString(getActivity(), U.SH_JOBLOCATION, "");
                                                        } else {
                                                            String filter;
                                                            StringBuilder sb = new StringBuilder();
                                                            for (int i = 0; i < selectedLocation.size(); i++) {
                                                                sb.append(selectedLocation.get(i)).append(",");
                                                            }
                                                            filter = sb.toString();
                                                            filter = filter.substring(0, filter.length() - 1);
                                                            RegistrationActivity.jobPrefferedLocation = filter;
                                                            if (!RegistrationActivity.jobPrefferedLocation.isEmpty()) {
                                                                pref.putString(getActivity(), U.SH_JOBLOCATION, RegistrationActivity.jobPrefferedLocation);
                                                            } else {
                                                                pref.putString(getActivity(), U.SH_JOBLOCATION, "");
                                                            }
                                                        }
                                                        onRegistrationListener.onPercentageChange();
                                                        setLocationValues(fblLocation);
                                                    }
                                                })
                                                .setNegativeButton("இல்லை", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        RegistrationActivity.Lpopupwindow.dismiss();
                                                    }
                                                });
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();
                                    }
                                });
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        fblcities.addView(subchild);
                                    }
                                });
                            }
                            RegistrationActivity.Lpopupwindow.setBackgroundDrawable(new BitmapDrawable());
                            RegistrationActivity.Lpopupwindow.setOutsideTouchable(true);
                            RegistrationActivity.Lpopupwindow.showAsDropDown(v);
                        }
                    }
                });
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fbl.addView(subchild);
                    }
                });
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void ShowLocationDialog() {
        if (getActivity() != null) {
            locationDia = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            if (locationDia.getWindow() != null) {
                locationDia.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            }
            locationDia.setContentView(R.layout.selection_layout);
            locationDia.setCancelable(true);
            final Button selectBtn = locationDia.findViewById(R.id.set_btn);
            TextView task = locationDia.findViewById(R.id.task);
            task.setText("Preferred location :");

            LinearLayout LocationList = locationDia.findViewById(R.id.val_list);
            LocationList.removeAllViews();

            final SearchView searchView = locationDia.findViewById(R.id.searchview);
            searchView.setIconifiedByDefault(false);
            searchView.setSubmitButtonEnabled(false);
            searchView.setQueryHint("Search...");
            searchView.setQuery("", true);

            final ImageView actionSearch = locationDia.findViewById(R.id.action_search);
            actionSearch.setVisibility(View.VISIBLE);
            actionSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wholeLocationDialog();
                }
            });

            TextView suggest = locationDia.findViewById(R.id.suggest);
            suggest.setVisibility(View.GONE);

            listLay = locationDia.findViewById(R.id.list_lay);
            listLay.setVisibility(View.VISIBLE);
            listview = locationDia.findViewById(R.id.listview);
            listview.setVisibility(View.VISIBLE);

            skillDetail = locationDia.findViewById(R.id.checkvalue);
            skillDetail.setVisibility(View.GONE);

            final TextView[] textViews = new TextView[RegistrationActivity.locationList.size()];
            for (int i = 0; i < RegistrationActivity.locationList.size(); i++) {
                if (!pref.getString(getActivity(), U.SH_JOBLOCATION).equals("")) {
                    RegistrationActivity.jobPrefferedLocation = pref.getString(getActivity(), U.SH_JOBLOCATION);
                    String[] skillsArray = RegistrationActivity.jobPrefferedLocation.split(",");
                    selectedLocation.clear();
                    selectedLocation.addAll(Arrays.asList(skillsArray));
                    setLocationValues(fblLocation);
                } else {
                    selectedLocation.clear();
                }
                final TextView temp = new TextView(getActivity());
                temp.setText(RegistrationActivity.locationList.get(i).getItem().toUpperCase());
                temp.setId(i);
                temp.setBackgroundColor(getResources().getColor(R.color.light_orange));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 115);
                params.setMargins(10, 10, 10, 10);
                params.weight = 1;
                temp.setLayoutParams(params);
                temp.setPadding(0, 0, 0, 0);
                temp.setTextSize(12);
                temp.setGravity(Gravity.CENTER);
                temp.setBackgroundResource(R.drawable.bg_yellow_thick);
                temp.setTextColor(getActivity().getResources().getColor(R.color.black));
                temp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchView.setQuery("", true);
                        for (int i = 0; i < RegistrationActivity.locationList.size(); i++) {
                            textViews[i].setBackgroundResource(R.drawable.bg_yellow_thick);
                        }
                        temp.setBackgroundResource(R.drawable.bg_black_line);

                        //Ascending order
                        Collections.sort(RegistrationActivity.locationList.get(temp.getId()).getList(), new Comparator<Item>() {
                            @Override
                            public int compare(Item o1, Item o2) {
                                return o1.getItem().compareTo(o2.getItem());
                            }
                        });

                        locationadapter = new LocationSelectionAdapter(getActivity(), R.layout.filter_layout, temp.getId(),
                                RegistrationActivity.locationList.get(temp.getId()).getList(), searchView);
                        listview.setTextFilterEnabled(true);
                        listview.setAdapter(locationadapter);

                        all = locationDia.findViewById(R.id.all_checkbox);
                        all.setVisibility(View.GONE);
                        selectedAll = 1;
                        all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    selectedAll = 0;
                                    Log.e("selectedAll", "" + selectedAll);
                                    if (selectedAll == 0) {
                                        for (int k = 0; k < RegistrationActivity.locationList.get(temp.getId()).getList().size(); k++) {
                                            if (!selectedLocation.contains("" + RegistrationActivity.locationList.get(temp.getId()).getList().get(k).getId())) {
                                                if (selectedLocation.size() < 10) {
                                                    selectedLocation.add("" + RegistrationActivity.locationList.get(temp.getId()).getList().get(k).getId());
                                                } else {
                                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                                    alertDialogBuilder
                                                            .setMessage("நீங்கள் வேலை செய்ய விரும்பும் பத்து இடங்களை தேர்வு செய்து விட்டீர்கள்.")
                                                            .setCancelable(false)
                                                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int ids) {
                                                                    dialog.dismiss();
                                                                }
                                                            });
                                                    AlertDialog exceedDia = alertDialogBuilder.create();
                                                    exceedDia.show();
                                                    break;
                                                }
                                            }
                                            locationadapter.notifyDataSetChanged();
                                        }
                                    }
                                } else {
                                    if (selectedAll == 0) {
                                        for (int j = 0; j < RegistrationActivity.locationList.get(temp.getId()).getList().size(); j++) {
                                            selectedLocation.remove("" + RegistrationActivity.locationList.get(temp.getId()).getList().get(j).getId());
                                        }
                                        Log.e("Location_set", "" + selectedLocation);
                                        locationadapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        });

                        ArrayList localArrayList = new ArrayList();
                        for (int i = 0; i < RegistrationActivity.locationList.get(temp.getId()).getList().size(); i++) {
                            localArrayList.add("" + RegistrationActivity.locationList.get(temp.getId()).getList().get(i).getId());
                        }
                        if (selectedLocation.containsAll(localArrayList)) {
                            all.setChecked(true);
                        } else {
                            all.setChecked(false);
                        }
                        locationadapter.notifyDataSetChanged();

                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                try {
                                    locationadapter.filter(newText.trim());
                                } catch (Exception e) {
                                    Log.e("Error", "" + e);
                                }
                                return true;
                            }

                        });
                    }
                });
                textViews[i] = temp;
                LocationList.addView(temp);

            }
            selectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchView.setQuery("", true);
                    if (selectedLocation.isEmpty()) {
                        RegistrationActivity.jobPrefferedLocation = "";
                        pref.putString(getActivity(), U.SH_JOBLOCATION, "");
                        Toast.makeText(getActivity(), "Select your preferred Job Location", Toast.LENGTH_SHORT).show();
                    } else {
                        String filter;
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < selectedLocation.size(); i++) {
                            sb.append(selectedLocation.get(i)).append(",");
                        }
                        filter = sb.toString();
                        filter = filter.substring(0, filter.length() - 1);
                        RegistrationActivity.jobPrefferedLocation = filter;
                        if (!RegistrationActivity.jobPrefferedLocation.isEmpty()) {
                            pref.putString(getActivity(), U.SH_JOBLOCATION, RegistrationActivity.jobPrefferedLocation);
                        } else {
                            pref.putString(getActivity(), U.SH_JOBLOCATION, "");
                        }
                        locationDia.dismiss();
                    }
                    onRegistrationListener.onPercentageChange();
                    setLocationValues(fblLocation);
                }
            });
            locationDia.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    searchView.setQuery("", true);
                    txtJoblocation.setEnabled(true);
                    if (!pref.getString(getActivity(), U.SH_JOBLOCATION).equals("")) {
                        RegistrationActivity.jobPrefferedLocation = pref.getString(getActivity(), U.SH_JOBLOCATION);
                        String[] skillsArray = RegistrationActivity.jobPrefferedLocation.split(",");
                        selectedLocation.clear();
                        selectedLocation.addAll(Arrays.asList(skillsArray));
                        setLocationValues(fblLocation);
                    } else {
                        selectedLocation.clear();
                    }
                }
            });
            textViews[0].performClick();
            locationDia.show();

            if (pref.getInt(getActivity(), U.SH_GUIDE_WINDOW_3) == 0) {
                final Dialog guideWindow = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                guideWindow.setContentView(R.layout.guide_window_search);
                TextView infoText = guideWindow.findViewById(R.id.info_text);
                infoText.setText("தங்களுக்கு வேலை செய்ய விருப்பமான பத்து இடங்களை மட்டும் தேர்வு செய்யவும்.");
                TextView searchText = guideWindow.findViewById(R.id.search_text);
                searchText.setText(getResources().getString(R.string.guide_all_location));
                TextView selectText = guideWindow.findViewById(R.id.select_text);
                selectText.setText(getResources().getString(R.string.guide_select_location));
                Button ok = guideWindow.findViewById(R.id.ok_btn);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pref.putInt(getActivity(), U.SH_GUIDE_WINDOW_3, 1);
                        guideWindow.dismiss();
                    }
                });
                guideWindow.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        pref.putInt(getActivity(), U.SH_GUIDE_WINDOW_3, 1);
                    }
                });
                guideWindow.show();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void wholeCategoryDialog() {
        if (getActivity() != null) {
            allCategoryDia = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            allCategoryDia.requestWindowFeature(Window.FEATURE_NO_TITLE);
            allCategoryDia.setCancelable(true);
            allCategoryDia.setContentView(R.layout.custom_dialog);
            if (allCategoryDia.getWindow() != null) {
                allCategoryDia.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            // searchView
            final SearchView searchView = allCategoryDia.findViewById(R.id.searchview);
            searchView.setQueryHint("Search...");

            TextView headertext = allCategoryDia.findViewById(R.id.text_header);
            headertext.setText("Choose your Job Category :");

            LinearLayout bottomLay = allCategoryDia.findViewById(R.id.bottom_lay);
            bottomLay.setVisibility(View.VISIBLE);
            allCategoryDia.findViewById(R.id.set_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchView.setQuery("", true);
                    Log.e("selectedCatArray", "" + selectedCategory);
                    if (selectedCategory.isEmpty()) {
                        RegistrationActivity.category = "";
                        pref.putString(getActivity(), U.SH_CATEGORY, "");

                        setCategoryValues(fblCategory);

                        selectedTitle.clear();
                        RegistrationActivity.cattitle = "";
                        pref.putString(getActivity(), U.SH_TITLE, "");
                        setTitleValues(fblTitle);

                        onRegistrationListener.onPercentageChange();
                        Toast.makeText(getActivity(), "Select Your preferred Job Category", Toast.LENGTH_SHORT).show();
                    } else {
                        String filter;
                        StringBuilder sb = new StringBuilder();
                        Log.e("selectedCatArray1", "" + selectedCategory);

                        for (int i = 0; i < selectedCategory.size(); i++) {
                            sb.append(selectedCategory.get(i)).append(",");
                        }

                        filter = sb.toString();
                        filter = filter.substring(0, filter.length() - 1);
                        RegistrationActivity.category = filter;
                        if (!RegistrationActivity.category.isEmpty()) {
                            pref.putString(getActivity(), U.SH_CATEGORY, RegistrationActivity.category);
                        } else {
                            pref.putString(getActivity(), U.SH_CATEGORY, "");
                        }
                        onRegistrationListener.onPercentageChange();
                        setCategoryValues(fblCategory);
                        //-------------------
                        if (RegistrationActivity.jobcategoryList.size() > 0) {
                            loadJSON(SU.GETCATTITLE, 1);
                        }
                        //--------------------
                        allCategoryDia.dismiss();
                    }
                }
            });
            allCategoryDia.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchView.setQuery("", true);
                    allCategoryDia.dismiss();
                }
            });

            allCategoryDia.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    searchView.setQuery("", true);
                    txtjobCat.setEnabled(true);

                    if (!pref.getString(getActivity(), U.SH_CATEGORY).equals("")) {
                        RegistrationActivity.category = pref.getString(getActivity(), U.SH_CATEGORY);
                        String[] categoryArray = RegistrationActivity.category.split(",");
                        selectedCategory.clear();
                        selectedCategory.addAll(Arrays.asList(categoryArray));
                        Log.e("CatArray", "" + selectedCategory);
                        setCategoryValues(fblCategory);
                    } else {
                        selectedCategory.clear();
                    }
                }
            });

            listview = allCategoryDia.findViewById(R.id.listview);
            //Ascending order
            Collections.sort(RegistrationActivity.jobcategoryList, new Comparator<Item>() {
                @Override
                public int compare(Item o1, Item o2) {
                    return o1.getItem().compareTo(o2.getItem());
                }
            });

            if (!pref.getString(getActivity(), U.SH_CATEGORY).equals("")) {
                RegistrationActivity.category = pref.getString(getActivity(), U.SH_CATEGORY);
                String[] categoryArray = RegistrationActivity.category.split(",");
                selectedCategory.clear();
                selectedCategory.addAll(Arrays.asList(categoryArray));
                Log.e("CatArray", "" + selectedCategory);
                setCategoryValues(fblCategory);
            } else {
                selectedCategory.clear();
            }

            final CategorySelectionAdapter categoryAdapter = new CategorySelectionAdapter(getActivity(), R.layout.filter_layout, RegistrationActivity.jobcategoryList);
            listview.setTextFilterEnabled(true);
            listview.setAdapter(categoryAdapter);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    try {
                        categoryAdapter.filter(newText.trim());
                    } catch (Exception e) {
                        Log.e("Error", "" + e);
                    }
                    return true;
                }
            });

            allCategoryDia.show();
        }
    }

    //------------------------------------------- Location -----------------------------------------

    @SuppressLint("SetTextI18n")
    private void wholeTitleDialog() {
        if (getActivity() != null) {
            allTitleDia = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            allTitleDia.requestWindowFeature(Window.FEATURE_NO_TITLE);
            allTitleDia.setCancelable(true);
            allTitleDia.setContentView(R.layout.custom_dialog);
            if (allTitleDia.getWindow() != null) {
                allTitleDia.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            // searchView
            final SearchView searchView = allTitleDia.findViewById(R.id.searchview);
            searchView.setQueryHint("Search...");

            TextView headertext = allTitleDia.findViewById(R.id.text_header);
            headertext.setText("Choose your Job Title :");

            LinearLayout bottomLay = allTitleDia.findViewById(R.id.bottom_lay);
            bottomLay.setVisibility(View.VISIBLE);
            allTitleDia.findViewById(R.id.set_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchView.setQuery("", true);
                    if (selectedTitle.isEmpty()) {
                        RegistrationActivity.cattitle = "";
                        pref.putString(getActivity(), U.SH_TITLE, "");
                        setTitleValues(fblTitle);
                        Toast.makeText(getActivity(), "Select Your preferred Job Title", Toast.LENGTH_SHORT).show();
                    } else {
                        String filter;
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < selectedTitle.size(); i++) {
                            sb.append(selectedTitle.get(i)).append(",");
                        }
                        filter = sb.toString();
                        filter = filter.substring(0, filter.length() - 1);
                        RegistrationActivity.cattitle = filter;
                        if (!RegistrationActivity.cattitle.isEmpty()) {
                            pref.putString(getActivity(), U.SH_TITLE, RegistrationActivity.cattitle);
                        } else {
                            pref.putString(getActivity(), U.SH_TITLE, "");
                        }
                        setTitleValues(fblTitle);
                        allTitleDia.dismiss();
                    }
                    onRegistrationListener.onPercentageChange();
                }
            });
            allTitleDia.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchView.setQuery("", true);
                    allTitleDia.dismiss();
                }
            });

            allTitleDia.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    searchView.setQuery("", true);
                    txtTitle.setEnabled(true);

                    if (!pref.getString(getActivity(), U.SH_TITLE).equals("")) {
                        RegistrationActivity.cattitle = pref.getString(getActivity(), U.SH_TITLE);
                        String[] titleArray = RegistrationActivity.cattitle.split(",");
                        selectedTitle.clear();
                        selectedTitle.addAll(Arrays.asList(titleArray));
                        setTitleValues(fblTitle);
                    } else {
                        selectedTitle.clear();
                    }
                }
            });

            listview = allTitleDia.findViewById(R.id.listview);

            //Ascending order
            Collections.sort(RegistrationActivity.titleList_Sub, new Comparator<Item>() {
                @Override
                public int compare(Item o1, Item o2) {
                    return o1.getItem().compareTo(o2.getItem());
                }
            });

            if (!pref.getString(getActivity(), U.SH_TITLE).equals("")) {
                RegistrationActivity.cattitle = pref.getString(getActivity(), U.SH_TITLE);
                String[] titleArray = RegistrationActivity.cattitle.split(",");
                selectedTitle.clear();
                selectedTitle.addAll(Arrays.asList(titleArray));
                setTitleValues(fblTitle);
            } else {
                selectedTitle.clear();
            }

            final TitleSelectionAdapter titleAdapter = new TitleSelectionAdapter(getActivity(), R.layout.filter_layout, RegistrationActivity.titleList_Sub);
            listview.setTextFilterEnabled(true);
            listview.setAdapter(titleAdapter);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    try {
                        titleAdapter.filter(newText.trim());
                    } catch (Exception e) {
                        Log.e("Error", "" + e);
                    }
                    return true;
                }
            });

            allTitleDia.show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void setCategoryValues(final FlexboxLayout fbl) {
        fbl.removeAllViews();
        for (int i = 0; i < RegistrationActivity.jobcategoryList.size(); i++) {
            int id = RegistrationActivity.jobcategoryList.get(i).getId();
            if (selectedCategory.contains("" + id)) {
                if (getActivity() != null) {
                    @SuppressLint("InflateParams") final View subchild = getActivity().getLayoutInflater().inflate(R.layout.single_chip, null);
                    final TextView tv = subchild.findViewById(R.id.txt_title);
                    final ImageView remove = subchild.findViewById(R.id.remove);
                    tv.setId(i);
                    tv.setText(RegistrationActivity.jobcategoryList.get(i).getItem());
                    tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.thick_blue));
                    final int finalI = i;
                    remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                            alertDialogBuilder
                                    .setMessage("நீக்க விரும்புகிறீர்களா?")
                                    .setCancelable(false)
                                    .setPositiveButton("ஆம்", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int ids) {
                                            selectedCategory.remove("" + RegistrationActivity.jobcategoryList.get(finalI).getId());
                                            if (selectedCategory.isEmpty()) {
                                                RegistrationActivity.category = "";
                                                pref.putString(getActivity(), U.SH_CATEGORY, "");

                                                setCategoryValues(fblCategory);

                                                selectedTitle.clear();
                                                RegistrationActivity.cattitle = "";
                                                pref.putString(getActivity(), U.SH_TITLE, "");
                                                setTitleValues(fblTitle);
                                                onRegistrationListener.onPercentageChange();
                                            } else {
                                                String filter;
                                                StringBuilder sb = new StringBuilder();
                                                for (int i = 0; i < selectedCategory.size(); i++) {
                                                    sb.append(selectedCategory.get(i)).append(",");
                                                }
                                                filter = sb.toString();
                                                filter = filter.substring(0, filter.length() - 1);
                                                RegistrationActivity.category = filter;
                                                if (!RegistrationActivity.category.isEmpty()) {
                                                    pref.putString(getActivity(), U.SH_CATEGORY, RegistrationActivity.category);
                                                } else {
                                                    pref.putString(getActivity(), U.SH_CATEGORY, "");
                                                }
                                                onRegistrationListener.onPercentageChange();
                                                setCategoryValues(fblCategory);
                                                //-------------------
                                                if (RegistrationActivity.jobcategoryList.size() > 0) {
                                                    loadJSON(SU.GETCATTITLE, 1);
                                                }
                                                //--------------------
                                            }
                                        }
                                    })
                                    .setNegativeButton("இல்லை", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    });
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fbl.addView(subchild);
                        }
                    });
                }
            }
        }
    }

    private void setTitleValues(final FlexboxLayout fbl) {
        fbl.removeAllViews();
        Log.e("titles", "called :" + RegistrationActivity.titleList_Sub.size());
        for (int i = 0; i < RegistrationActivity.titleList_Sub.size(); i++) {
            int id = RegistrationActivity.titleList_Sub.get(i).getId();
            if (selectedTitle.contains("" + id)) {
                if (getActivity() != null) {
                    @SuppressLint("InflateParams") final View subchild = getActivity().getLayoutInflater().inflate(R.layout.single_chip, null);
                    final TextView tv = subchild.findViewById(R.id.txt_title);
                    final ImageView remove = subchild.findViewById(R.id.remove);
                    tv.setId(i);
                    tv.setText(RegistrationActivity.titleList_Sub.get(i).getItem());
                    Log.e("cattitle_text", "" + RegistrationActivity.titleList_Sub.get(i).getItem());
                    tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.thick_blue));
                    final int finalI = i;
                    remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                            alertDialogBuilder
                                    .setMessage("நீக்க விரும்புகிறீர்களா?")
                                    .setCancelable(false)
                                    .setPositiveButton("ஆம்", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int ids) {
                                            selectedTitle.remove("" + RegistrationActivity.titleList_Sub.get(finalI).getId());
                                            if (selectedTitle.isEmpty()) {
                                                RegistrationActivity.cattitle = "";
                                                pref.putString(getActivity(), U.SH_TITLE, "");
                                            } else {
                                                String filter;
                                                StringBuilder sb = new StringBuilder();
                                                for (int i = 0; i < selectedTitle.size(); i++) {
                                                    sb.append(selectedTitle.get(i)).append(",");
                                                }
                                                filter = sb.toString();
                                                filter = filter.substring(0, filter.length() - 1);
                                                RegistrationActivity.cattitle = filter;
                                                if (!RegistrationActivity.cattitle.isEmpty()) {
                                                    pref.putString(getActivity(), U.SH_TITLE, RegistrationActivity.cattitle);
                                                } else {
                                                    pref.putString(getActivity(), U.SH_TITLE, "");
                                                }
                                            }
                                            onRegistrationListener.onPercentageChange();
                                            setTitleValues(fblTitle);
                                        }
                                    })
                                    .setNegativeButton("இல்லை", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    });
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fbl.addView(subchild);
                        }
                    });
                }
            }
        }
    }

    private void loadJSON(final String param, final int flag) {
        U.mProgress(getActivity(), "please Wait...", true).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SU.REGISTRATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("responseRegister", "" + param + " --" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("list");
                            RegistrationActivity.titleList_Sub.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                if (!jo.getString("english").equals("")) {
                                    Item data = new Item();
                                    data.setId(jo.getInt("id"));
                                    data.setItem(jo.getString("english"));
                                    RegistrationActivity.titleList_Sub.add(data);
                                    Log.e("title", "  " + i + "  " + jo.getString("english"));
                                } else {
                                    Item data = new Item();
                                    data.setId(jo.getInt("id"));
                                    data.setItem(jo.getString("tamil"));
                                    RegistrationActivity.titleList_Sub.add(data);
                                    Log.e("title", "  " + i + "  " + jo.getString("tamil"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (flag == 0) {
                            wholeTitleDialog();
                        } else {
                            removeCatTitle();
                        }

                        U.mProgress.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        U.mProgress.dismiss();
                        if (getActivity() != null) {
                            errorHandling(error);
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (getActivity() != null) {
                    params.put("action", param);
                    params.put("category", pref.getString(getActivity(), U.SH_CATEGORY));
                    params.put("user_type", pref.getString(getActivity(), U.SH_USER_TYPE));
                    params.put("employee_id", pref.getString(getActivity(), U.SH_EMPLOYEE_ID));
                    params.put("android_id", U.getAndroidId(getActivity()));
                    params.put("vcode", String.valueOf(U.versioncode_get(getActivity())));
                    Log.e("paramsresponse", "" + params);
                }
                return params;
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQue(stringRequest);
    }

    private void errorHandling(VolleyError error) {
        String e;
        if (error instanceof TimeoutError || error instanceof AuthFailureError || error instanceof ServerError)
            e = U.SERVER_ERROR;
        else if (error instanceof NetworkError)
            e = U.INA;
        else if (error instanceof ParseError)
            e = U.ERROR;
        else
            e = U.ERROR;
        L.t(getActivity(), e);
    }

    //--------------------------------------------- category ---------------------------------------

    private void removeCatTitle() {
        List<String> allTitles = loadAllTittles();
        List<String> temp = new ArrayList<>();
        for (int i = 0; i < allTitles.size(); i++) {
            String id = allTitles.get(i);
            for (int j = 0; j < selectedTitle.size(); j++) {
                if (id.equals(selectedTitle.get(j))) {
                    temp.add(selectedTitle.get(j));
                }
            }
        }
        selectedTitle.clear();
        selectedTitle.addAll(temp);
        onRegistrationListener.onPercentageChange();
        setTitleValues(fblTitle);
    }

    private List<String> loadAllTittles() {
        List<String> allTitles = new ArrayList<>();
        for (int i = 0; i < RegistrationActivity.titleList_Sub.size(); i++) {
            allTitles.add("" + RegistrationActivity.titleList_Sub.get(i).getId());
        }
        Log.e("title", "" + allTitles);
        return allTitles;
    }

    public interface OnRegistrationListener {
        void onPercentageChange();
    }

    public class FileSelectionAdapter extends ArrayAdapter {

        Context context;
        List<Item> list;
        LayoutInflater inflater;
        List<Item> orig;
        SearchView searchView;
        ListView listView;
        int count = 0;

        FileSelectionAdapter(Context context, int resource, List<Item> list,
                             SearchView searchView, ListView listview) {
            super(context, resource, list);
            this.context = context;
            this.list = list;
            this.searchView = searchView;
            orig = new ArrayList<Item>();
            this.listView = listview;
            orig.addAll(list);
        }

        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @SuppressLint("SetTextI18n")
        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view = inflater.inflate(R.layout.dialog_list_item, null);

            TextView txt = view.findViewById(R.id.name);
            TextView size = view.findViewById(R.id.size);
            size.setVisibility(View.VISIBLE);
            final CheckBox checkBox = view.findViewById(R.id.checkbox);

            txt.setTextSize(15);
            txt.setText(list.get(position).getItem());

            if (list.get(position).getId() == 2) {
                size.setText("Size : " + list.get(position).getSize() + " MB");
            } else {
                size.setText("Size : " + list.get(position).getSize() + " KB");
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                    } else {
                        checkBox.setChecked(true);
                    }
                }
            });

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (count == 0) {
                            if (list.get(position).getId() == 2 && list.get(position).getSize() < 2) {
                                if (copyFile(list.get(position).getCount())) {
                                    count = 1;
                                    listView.setAlpha(0.5F);
                                    listView.setClickable(false);
                                    uploadImage.setImageResource(R.drawable.ic_draft);
                                    uploadResumeTxt.setText(list.get(position).getItem());
                                    pref.putString(getActivity(), U.SH_RESUME, RegistrationActivity.resumePdf);
                                    onRegistrationListener.onPercentageChange();
                                    allFilesDia.dismiss();
                                } else {
                                    L.t(getActivity(), "File not Supported");
                                }

                            } else if (list.get(position).getId() == 1) {

                                if (copyFile(list.get(position).getCount())) {
                                    count = 1;
                                    listView.setAlpha(0.5F);
                                    listView.setClickable(false);
                                    uploadImage.setImageResource(R.drawable.ic_draft);
                                    uploadResumeTxt.setText(list.get(position).getItem());
                                    pref.putString(getActivity(), U.SH_RESUME, RegistrationActivity.resumePdf);
                                    onRegistrationListener.onPercentageChange();
                                    allFilesDia.dismiss();
                                } else {
                                    L.t(getActivity(), "File not Supported");
                                }

                            } else {
                                checkBox.setChecked(false);
                                L.t(getActivity(), "Resume File size should be less than 2 MB");
                            }

                        } else {
                            checkBox.setChecked(false);
                        }
                    }
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

    @SuppressLint("StaticFieldLeak")
    private class DownloadFile extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            U.mProgress(getActivity(), "Resume Downloading...", false).show();
        }

        @Override
        protected String doInBackground(String... Url) {
            try {
                URL url = new URL(Url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                // Detect the file lenghth
                int fileLength = connection.getContentLength();

                // Download the file
                InputStream input = new BufferedInputStream(url.openStream());

                File root = Environment.getExternalStorageDirectory();
                File mydir = new File(root + "/Nithra/Jobs/Resume");
                mydir.mkdirs();

                // Save the downloaded file
                File fileLocation = new File(Environment.getExternalStorageDirectory()
                        + "/Nithra/Jobs/Resume");

                String downloadFileName = Url[0].substring(Url[0].lastIndexOf('/'));//Create file name by picking download file name from URL

                File outputFile = new File(fileLocation, downloadFileName);//Create Output file in Main File

                FileOutputStream output = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                byte[] data = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // Publish the progress
                    publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
                // Close connection
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
                String downloadFileName = Url[0].substring(Url[0].lastIndexOf('/'));
                deleteResume(downloadFileName);
                Log.e("catch", "" + e);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            if (progress[0] == 100) {
                U.mProgress.dismiss();
                openResume();
            }
        }
    }

    public class SkillsSelectionAdapter extends ArrayAdapter {

        Context context;
        List<Item> list;
        LayoutInflater inflater;
        List<Item> orig;
        SearchView searchView;

        public SkillsSelectionAdapter(Context context, int resource, List<Item> list, SearchView searchView) {
            super(context, resource, list);
            this.context = context;
            this.list = list;
            this.searchView = searchView;
            orig = new ArrayList<>();
            orig.addAll(list);
        }

        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint({"ViewHolder", "InflateParams"}) final View view = inflater.inflate(R.layout.dialog_list_item, null);

            TextView txt = view.findViewById(R.id.name);
            final CheckBox checkBox = view.findViewById(R.id.checkbox);
            RelativeLayout checkboxLay = view.findViewById(R.id.checkboxLay);
            txt.setText(list.get(position).getItem());
            if (selectedSkills.contains("" + list.get(position).getId())) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                    } else {
                        checkBox.setChecked(true);
                    }

                }
            });

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (selectedSkills.size() < 10) {
                            if (skillDetail.isChecked()) {
                                skillDetail.setChecked(false);
                            }
                            selectedSkills.add("" + list.get(position).getId());
                        } else if (selectedSkills.size() == 10 || selectedSkills.size() > 10) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                            alertDialogBuilder
                                    .setMessage("நீங்கள் ஏற்கனவே உங்களுக்கான பத்து திறன்களை தேர்வு செய்து விட்டீர்கள்.")
                                    .setCancelable(false)
                                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int ids) {
                                            notifyDataSetChanged();
                                            dialog.dismiss();
                                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                            if (imm != null) {
                                                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                                            }
                                        }
                                    });
                            AlertDialog exceedDia = alertDialogBuilder.create();
                            exceedDia.show();
                        }

                    } else {
                        selectedSkills.remove("" + list.get(position).getId());
                    }
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

    public class suggestionAdapter extends ArrayAdapter {
        Context context;
        LayoutInflater inflater;
        List<Item> list;
        List<Item> orig;
        ListView suggestionlist;
        EditText userInput;

        public suggestionAdapter(Context context, int resource, EditText editText, ListView listView, List<Item> List) {
            super(context, resource, List);
            this.context = context;
            list = List;
            orig = new ArrayList();
            orig.addAll(List);
            userInput = editText;
            suggestionlist = listView;
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

        @NonNull
        public View getView(final int position, View paramView, @NonNull ViewGroup paramViewGroup) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View view = inflater.inflate(R.layout.group_view, null);
            TextView txt = view.findViewById(R.id.text);
            txt.setText(list.get(position).getItem());
            view.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                public void onClick(View paramAnonymousView) {
                    userInput.setText("" + list.get(position).getItem());
                    suggestionlist.setVisibility(View.GONE);
                }
            });
            return view;
        }

        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }
    }

    public class LocationSelectionAdapter extends ArrayAdapter {

        Context context;
        List<Item> list;
        LayoutInflater inflater;
        List<Item> orig;
        int distId = 0;
        SearchView searchView;

        public LocationSelectionAdapter(Context context, int resource, int distId, List<Item> list, SearchView searchView) {
            super(context, resource, list);
            this.context = context;
            this.list = list;
            this.distId = distId;
            this.searchView = searchView;
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
            final View view = inflater.inflate(R.layout.dialog_list_item, null);

            TextView txt = view.findViewById(R.id.name);
            final CheckBox checkBox = view.findViewById(R.id.checkbox);
            txt.setText(list.get(position).getItem());

            if (selectedLocation.contains("" + list.get(position).getId())) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                    } else {
                        checkBox.setChecked(true);
                    }

                }
            });

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (selectedLocation.size() < 10) {
                            selectedLocation.add("" + list.get(position).getId());
                            ArrayList localArrayList = new ArrayList();
                            for (int i = 0; i < RegistrationActivity.locationList.get(distId).getList().size(); i++) {
                                localArrayList.add("" + RegistrationActivity.locationList.get(distId).getList().get(i).getId());
                            }
                            if (selectedLocation.containsAll(localArrayList)) {
                                all.setChecked(true);
                            } else {
                                all.setChecked(false);
                            }
                        } else if (selectedLocation.size() == 10 || selectedLocation.size() > 10) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                            alertDialogBuilder
                                    .setMessage("நீங்கள் வேலை செய்ய விரும்பும் பத்து இடங்களை தேர்வு செய்து விட்டீர்கள்.")
                                    .setCancelable(false)
                                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int ids) {
                                            notifyDataSetChanged();
                                            dialog.dismiss();
                                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                            if (imm != null) {
                                                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                                            }
                                        }
                                    });
                            AlertDialog exceedDia = alertDialogBuilder.create();
                            exceedDia.show();
                        }
                    } else {
                        selectedLocation.remove("" + list.get(position).getId());
                        selectedAll = 1;
                        Log.e("selectedAll", "" + selectedAll);
                        all.setChecked(false);
                        notifyDataSetChanged();
                    }
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

    public class CategorySelectionAdapter extends ArrayAdapter {

        Context context;
        List<Item> list;
        LayoutInflater inflater;
        List<Item> orig;

        public CategorySelectionAdapter(Context context, int resource, List<Item> list) {
            super(context, resource, list);
            this.context = context;
            this.list = list;
            orig = new ArrayList<>();
            orig.addAll(list);
        }

        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint({"ViewHolder", "InflateParams"}) View view = inflater.inflate(R.layout.dialog_list_item, null);

            TextView txt = view.findViewById(R.id.name);
            final CheckBox checkBox = view.findViewById(R.id.checkbox);
            txt.setText(list.get(position).getItem());
            if (selectedCategory.contains("" + list.get(position).getId())) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                    } else {
                        checkBox.setChecked(true);
                    }

                }
            });
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedCategory.add("" + list.get(position).getId());
                    } else {
                        selectedCategory.remove("" + list.get(position).getId());
                    }
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

    public class TitleSelectionAdapter extends ArrayAdapter {

        Context context;
        List<Item> list;
        LayoutInflater inflater;
        List<Item> orig;

        public TitleSelectionAdapter(Context context, int resource, List<Item> list) {
            super(context, resource, list);
            this.context = context;
            this.list = list;
            orig = new ArrayList<>();
            orig.addAll(list);
        }

        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint({"ViewHolder", "InflateParams"}) View view = inflater.inflate(R.layout.dialog_list_item, null);

            TextView txt = view.findViewById(R.id.name);
            final CheckBox checkBox = view.findViewById(R.id.checkbox);
            txt.setText(list.get(position).getItem());
            if (selectedTitle.contains("" + list.get(position).getId())) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                    } else {
                        checkBox.setChecked(true);
                    }

                }
            });
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedTitle.add("" + list.get(position).getId());
                    } else {
                        selectedTitle.remove("" + list.get(position).getId());
                    }
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
