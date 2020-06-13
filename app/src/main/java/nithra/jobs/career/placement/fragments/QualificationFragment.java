package nithra.jobs.career.placement.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import nithra.jobs.career.placement.Interface.FragmentInterface;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.activity.RegistrationActivity;
import nithra.jobs.career.placement.pojo.ArrayItem;
import nithra.jobs.career.placement.pojo.Item;
import nithra.jobs.career.placement.utills.FlexboxLayout;
import nithra.jobs.career.placement.utills.L;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

/**
 * Created by nithra-apps on 14/11/17.
 */

public class QualificationFragment extends Fragment implements FragmentInterface {

    ImageView infoIcon;
    List<String> selectedCourse, selectedCertifiedCourses;
    SharedPreference pref;
    TextView txtQualification, txtCertificationCourses;
    FlexboxLayout fbl, fblCertifiedCourses;
    TextView emptyText;
    OnRegistrationListener onRegistrationListener;

    public QualificationFragment() {
        // Required empty public constructor
    }

    public static QualificationFragment newInstance() {
        return new QualificationFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onRegistrationListener = (OnRegistrationListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getActivity() != null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
        return inflater.inflate(R.layout.qualification_details_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = new SharedPreference();
        selectedCourse = new ArrayList<>();
        selectedCertifiedCourses = new ArrayList<>();
        fbl = view.findViewById(R.id.fbl);
        fblCertifiedCourses = view.findViewById(R.id.fblCertifiedCourses);
        infoIcon = view.findViewById(R.id.info_icon);
        txtQualification = view.findViewById(R.id.txtQualification);
        txtCertificationCourses = view.findViewById(R.id.txtCertificationCourses);
        emptyText = view.findViewById(R.id.empty_text);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog();
            }
        });

        txtQualification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RegistrationActivity.qualificationList.size() > 0) {
                    txtQualification.setEnabled(false);
                    showDialog();
                } else {
                    L.t(getActivity(), "Data Loading,Request one more time");
                }
            }
        });
        txtCertificationCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RegistrationActivity.qualificationList.size() > 0) {
                    txtCertificationCourses.setEnabled(false);
                    addExtraValue();
                } else {
                    L.t(getActivity(), "Data Loading,Request one more time");
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showDialog() {
        if (getActivity() != null) {
            final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            }
            dialog.setContentView(R.layout.selection_layout);
            dialog.setCancelable(true);
            Button selectBtn = dialog.findViewById(R.id.set_btn);
            TextView task = dialog.findViewById(R.id.task);
            task.setText("Qualification :");

            LinearLayout quaList = dialog.findViewById(R.id.val_list);
            quaList.removeAllViews();

            final ListView listView = dialog.findViewById(R.id.listview);
            final SearchView searchView = dialog.findViewById(R.id.searchview);
            searchView.setIconifiedByDefault(false);
            searchView.setSubmitButtonEnabled(false);
            searchView.setQueryHint("Search...");
            searchView.setQuery("", true);

            ImageView actionSearch = dialog.findViewById(R.id.action_search);
            actionSearch.setVisibility(View.GONE);

            CheckBox skildetail = dialog.findViewById(R.id.checkvalue);
            skildetail.setVisibility(View.GONE);

            CheckBox all_checkbox = dialog.findViewById(R.id.all_checkbox);
            all_checkbox.setVisibility(View.GONE);

            final TextView[] textViews = new TextView[RegistrationActivity.qualificationList.size()];
            for (int i = 0; i < RegistrationActivity.qualificationList.size(); i++) {
                if (!pref.getString(getActivity(), U.SH_COURSE).equals("")) {
                    RegistrationActivity.courses = pref.getString(getActivity(), U.SH_COURSE);
                    String[] skillsArray = RegistrationActivity.courses.split(",");
                    selectedCourse.clear();
                    selectedCourse.addAll(Arrays.asList(skillsArray));
                    emptyText.setVisibility(View.GONE);
                    setQualificationValues(fbl);
                } else {
                    selectedCourse.clear();
                }

                final TextView temp = new TextView(getActivity());
                temp.setText(RegistrationActivity.qualificationList.get(i).getItem().toUpperCase());
                temp.setId(i);
                temp.setBackgroundResource(R.drawable.bg_yellow_thick);
                temp.setTextColor(getActivity().getResources().getColor(R.color.black));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 115);
                params.setMargins(10, 10, 10, 10);
                params.weight = 1;
                temp.setLayoutParams(params);
                temp.setPadding(0, 0, 0, 0);
                temp.setTextSize(12);
                temp.setGravity(Gravity.CENTER);
                final int finalI = i;
                temp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchView.setQuery("", true);
                        for (int i = 0; i < RegistrationActivity.qualificationList.size(); i++) {
                            textViews[i].setBackgroundResource(R.drawable.bg_yellow_thick);
                        }
                        temp.setBackgroundResource(R.drawable.bg_black_line);

                        final CourseSelectionAdapter aadapter =
                                new CourseSelectionAdapter(getActivity(),
                                        R.layout.filter_layout,
                                        RegistrationActivity.qualificationList.get(temp.getId()).getList());

                        listView.setTextFilterEnabled(true);
                        listView.setAdapter(aadapter);
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                try {
                                    aadapter.filter(newText.trim());
                                } catch (Exception e) {
                                    Log.e("Error", "" + e);
                                }
                                return true;
                            }
                        });
                    }
                });
                textViews[i] = temp;
                quaList.addView(temp);
            }
            selectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchView.setQuery("", true);
                    if (selectedCourse.isEmpty()) {
                        RegistrationActivity.courses = "";
                        pref.putString(getActivity(), U.SH_COURSE, "");
                        emptyText.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Select Your Qualification Details", Toast.LENGTH_SHORT).show();
                    } else {
                        String filter;
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < selectedCourse.size(); i++) {
                            sb.append(selectedCourse.get(i)).append(",");
                        }
                        filter = sb.toString();
                        filter = filter.substring(0, filter.length() - 1);
                        RegistrationActivity.courses = filter;
                        if (!RegistrationActivity.courses.isEmpty()) {
                            pref.putString(getActivity(), U.SH_COURSE, RegistrationActivity.courses);
                        } else {
                            pref.putString(getActivity(), U.SH_COURSE, "");
                        }
                        emptyText.setVisibility(View.GONE);
                        dialog.dismiss();
                    }
                    onRegistrationListener.onPercentageChange();
                    setQualificationValues(fbl);
                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    searchView.setQuery("", true);
                    txtQualification.setEnabled(true);

                    if (!pref.getString(getActivity(), U.SH_COURSE).equals("")) {
                        RegistrationActivity.courses = pref.getString(getActivity(), U.SH_COURSE);
                        String[] skillsArray = RegistrationActivity.courses.split(",");
                        selectedCourse.clear();
                        selectedCourse.addAll(Arrays.asList(skillsArray));
                        emptyText.setVisibility(View.GONE);
                        setQualificationValues(fbl);
                    } else {
                        selectedCourse.clear();
                    }
                }
            });

            textViews[0].performClick();
            dialog.show();

            if (pref.getInt(getActivity(), U.SH_GUIDE_WINDOW_4) == 0) {
                final Dialog guideWindow = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                guideWindow.setContentView(R.layout.guide_window_search);
                TextView infoText = guideWindow.findViewById(R.id.info_text);
                infoText.setVisibility(View.GONE);
                ImageView searchImg = guideWindow.findViewById(R.id.search_img);
                searchImg.setVisibility(View.GONE);
                TextView searchText = guideWindow.findViewById(R.id.search_text);
                searchText.setVisibility(View.GONE);
                TextView selectText = guideWindow.findViewById(R.id.select_text);
                selectText.setText(getResources().getString(R.string.guide_select_qualification));
                Button ok = guideWindow.findViewById(R.id.ok_btn);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pref.putInt(getActivity(), U.SH_GUIDE_WINDOW_4, 1);
                        guideWindow.dismiss();
                    }
                });
                guideWindow.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        pref.putInt(getActivity(), U.SH_GUIDE_WINDOW_4, 1);
                    }
                });
                guideWindow.show();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void setQualificationValues(final FlexboxLayout fbl) {
        final List<ArrayItem> list = new ArrayList<>();
        if (RegistrationActivity.qualificationList != null && RegistrationActivity.qualificationList.size() > 0) {
            for (int i = 0; i < RegistrationActivity.qualificationList.size(); i++) {
                List<Item> tempList1 = new ArrayList<>();
                List<Item> temp = RegistrationActivity.qualificationList.get(i).getList();
                for (int j = 0; j < temp.size(); j++) {
                    int id = RegistrationActivity.qualificationList.get(i).getList().get(j).getId();
                    if (selectedCourse.contains("" + id)) {
                        Item data = new Item();
                        data.setId(RegistrationActivity.qualificationList.get(i).getList().get(j).getId());
                        data.setItem(RegistrationActivity.qualificationList.get(i).getList().get(j).getItem());
                        tempList1.add(data);
                    }
                }
                if (tempList1.size() > 0) {
                    ArrayItem data = new ArrayItem();
                    data.setId(RegistrationActivity.qualificationList.get(i).getId());
                    data.setItem(RegistrationActivity.qualificationList.get(i).getItem());
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
                        @Override
                        public void onClick(View v) {
                            LayoutInflater layoutin = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            @SuppressLint("InflateParams") final View popupview = layoutin.inflate(R.layout.location_popup, null);
                            RegistrationActivity.Qpopupwindow = new PopupWindow(popupview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            final FlexboxLayout fblcities = popupview.findViewById(R.id.fbl_cities);
                            final List<Item> temp = list.get(tv.getId()).getList();
                            for (int j = 0; j < temp.size(); j++) {
                                @SuppressLint("InflateParams") final View subchild = getActivity().getLayoutInflater().inflate(R.layout.single_chip, null);
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
                                                        selectedCourse.remove("" + temp.get(finalJ).getId());
                                                        RegistrationActivity.Qpopupwindow.dismiss();
                                                        if (selectedCourse.isEmpty()) {

                                                            RegistrationActivity.courses = "";
                                                            pref.putString(getActivity(), U.SH_COURSE, "");
                                                            emptyText.setVisibility(View.VISIBLE);

                                                        } else {
                                                            String filter;
                                                            StringBuilder sb = new StringBuilder();
                                                            for (int i = 0; i < selectedCourse.size(); i++) {
                                                                sb.append(selectedCourse.get(i)).append(",");
                                                            }
                                                            filter = sb.toString();
                                                            filter = filter.substring(0, filter.length() - 1);
                                                            RegistrationActivity.courses = filter;
                                                            if (!RegistrationActivity.courses.isEmpty()) {
                                                                pref.putString(getActivity(), U.SH_COURSE, RegistrationActivity.courses);
                                                            } else {
                                                                pref.putString(getActivity(), U.SH_COURSE, "");
                                                            }
                                                            emptyText.setVisibility(View.GONE);
                                                            dialog.dismiss();
                                                        }
                                                        setQualificationValues(fbl);
                                                        onRegistrationListener.onPercentageChange();
                                                    }
                                                })
                                                .setNegativeButton("இல்லை", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        RegistrationActivity.Qpopupwindow.dismiss();
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
                            RegistrationActivity.Qpopupwindow.setBackgroundDrawable(new BitmapDrawable());
                            RegistrationActivity.Qpopupwindow.setOutsideTouchable(true);
                            RegistrationActivity.Qpopupwindow.showAsDropDown(v);
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

    private void showInfoDialog() {
        if (getActivity() != null) {
            final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            }
            dialog.setContentView(R.layout.info_popup);
            WebView infoText = dialog.findViewById(R.id.info_text);
            infoText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });

            String info = "<b><meta charset=\"utf-8\"><font color=purple size=2>\n" + getResources().getString(R.string.qualification_info1)
                    + "<font color=red>&nbsp;" + getResources().getString(R.string.ex) + "</font><br>\n" + getResources().getString(R.string.qualification_info2) + "</font></b>";

            U.webview(getActivity(), info, infoText);

            dialog.findViewById(R.id.text_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }

    private void addExtraValue() {
        if (getActivity() != null) {
            final Dialog add_dialog = new Dialog(getActivity());
            add_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            add_dialog.setContentView(R.layout.add_extra_lay);
            if (add_dialog.getWindow() != null) {
                add_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            final EditText text = add_dialog.findViewById(R.id.text);
            TextView title = add_dialog.findViewById(R.id.title);
            ImageView done = add_dialog.findViewById(R.id.done);
            ImageView close = add_dialog.findViewById(R.id.close_btn);
            TextView predefinedKeywords = add_dialog.findViewById(R.id.predefined_keywords);
            predefinedKeywords.setVisibility(View.GONE);

            title.setText("Certification Courses :");

            add_dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    add_dialog.dismiss();
                }
            });

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!text.getText().toString().isEmpty()) {
                        if (selectedCertifiedCourses.contains(text.getText().toString())) {
                            L.t(getActivity(), "Already Added");
                        } else {
                            selectedCertifiedCourses.add(text.getText().toString());
                            if (!selectedCertifiedCourses.isEmpty()) {
                                RegistrationActivity.certifiedCourses = U.ListToString(selectedCertifiedCourses, "#@#");
                                pref.putString(getActivity(), U.SH_CERTIFIED_COURSE, RegistrationActivity.certifiedCourses);
                            } else {
                                pref.putString(getActivity(), U.SH_CERTIFIED_COURSE, "");
                            }
                            onRegistrationListener.onPercentageChange();
                            setCertifiedCourses(fblCertifiedCourses, selectedCertifiedCourses);
                        }
                    } else {
                        text.setError("Add Your Certified Course");
                    }
                    add_dialog.dismiss();
                }
            });

            add_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (getActivity() != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
                        }
                    }
                    txtCertificationCourses.setEnabled(true);
                }
            });

            add_dialog.show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void setCertifiedCourses(final FlexboxLayout fbl, final List<String> list) {
        fbl.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            if (getActivity() != null) {
                @SuppressLint("InflateParams") final View subchild = getActivity().getLayoutInflater()
                        .inflate(R.layout.single_chip, null);
                final TextView tv = subchild.findViewById(R.id.txt_title);
                final ImageView remove = subchild.findViewById(R.id.remove);
                remove.setVisibility(View.VISIBLE);
                tv.setId(i);
                tv.setText(list.get(i));
                tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.thick_blue));
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder
                                .setMessage("Are You Sure Want to Delete?")
                                .setCancelable(false)
                                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int ids) {
                                        list.remove(tv.getText().toString());
                                        if (!selectedCertifiedCourses.isEmpty()) {
                                            RegistrationActivity.certifiedCourses = U.ListToString(list, "#@#");
                                            pref.putString(getActivity(), U.SH_CERTIFIED_COURSE, RegistrationActivity.certifiedCourses);
                                        } else {
                                            pref.putString(getActivity(), U.SH_CERTIFIED_COURSE, "");
                                        }
                                        onRegistrationListener.onPercentageChange();
                                        setCertifiedCourses(fbl, list);
                                    }
                                })
                                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
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

    @Override
    public void onResume() {
        super.onResume();
        if (!pref.getString(getActivity(), U.SH_COURSE).equals("")) {
            RegistrationActivity.courses = pref.getString(getActivity(), U.SH_COURSE);
            String[] skillsArray = RegistrationActivity.courses.split(",");
            selectedCourse.clear();
            selectedCourse.addAll(Arrays.asList(skillsArray));
            emptyText.setVisibility(View.GONE);
            setQualificationValues(fbl);
        }
        if (!pref.getString(getActivity(), U.SH_CERTIFIED_COURSE).equals("")) {
            RegistrationActivity.certifiedCourses = pref.getString(getActivity(), U.SH_CERTIFIED_COURSE);
            String[] skillsArray = RegistrationActivity.certifiedCourses.split("#@#");
            selectedCertifiedCourses.clear();
            selectedCertifiedCourses.addAll(Arrays.asList(skillsArray));
            emptyText.setVisibility(View.GONE);
            setCertifiedCourses(fblCertifiedCourses, selectedCertifiedCourses);
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void fragmentBecameVisible() {
        if (pref.getInt(getActivity(), U.SH_QUALIFICATION_INFO_SHOW) == 0) {
            pref.putInt(getActivity(), U.SH_QUALIFICATION_INFO_SHOW, 1);
            showInfoDialog();
        }
    }

    public interface OnRegistrationListener {
        void onPercentageChange();
    }

    public class CourseSelectionAdapter extends ArrayAdapter {

        Context context;
        List<Item> list;
        LayoutInflater inflater;
        List<Item> orig;
        int count = 0;

        CourseSelectionAdapter(Context context, int resource, List<Item> list) {
            super(context, resource, list);
            this.context = context;
            this.list = list;
            orig = new ArrayList<Item>();
            orig.addAll(list);
            for (int i = 0; i < list.size(); i++) {
                if (selectedCourse.contains("" + list.get(i).getId())) {
                    count = count + 1;
                }
            }
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view = inflater.inflate(R.layout.dialog_list_item, null);

            TextView txt = view.findViewById(R.id.name);
            final CheckBox checkBox = view.findViewById(R.id.checkbox);
            txt.setText(list.get(position).getItem());
            if (selectedCourse.contains("" + list.get(position).getId())) {
//                count = count + 1;
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
                        if (count < 2) {
                            count = count + 1;
                            selectedCourse.add("" + list.get(position).getId());
                        } else {
                            checkBox.setChecked(false);
                            Toast.makeText(context, "வரம்பு மீறிவிட்டது", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        selectedCourse.remove("" + list.get(position).getId());
                        count = count - 1;
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