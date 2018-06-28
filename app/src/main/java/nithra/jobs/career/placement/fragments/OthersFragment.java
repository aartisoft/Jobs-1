package nithra.jobs.career.placement.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import nithra.jobs.career.placement.FragmentInterface;
import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.activity.RegistrationActivity;
import nithra.jobs.career.placement.pojo.ArrayItem;
import nithra.jobs.career.placement.pojo.Item;
import nithra.jobs.career.placement.utills.FlexboxLayout;
import nithra.jobs.career.placement.utills.L;
import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;
import static android.support.v4.view.ViewCompat.jumpDrawablesToCurrentState;

/**
 * Created by nithra-apps on 14/11/17.
 */

public class OthersFragment extends Fragment implements FragmentInterface {

    SharedPreference pref;
    TextView txtSkills, txtJoblocation, txtPpolicy, tctext;
    RadioGroup workStatusGroup;
    RadioButton fresher, experienced;
    CheckBox detailShow, ppShown, tcShown;
    ListView listview;
    List<String> selectedSkills, selectedLocation;
    List<Item> allSkills, allLocation;
    FlexboxLayout fblSkills, fblLocation;
    LinearLayout listLay;
    SkillsSelectionAdapter skilladapter;
    LocationSelectionAdapter locationadapter;
    CheckBox skillDetail, all;
    Dialog allSkillsDia, allLocationDia, skillsDia, locationDia;
    int selectedAll = 0;
    AlertDialog alertDia;

    public OthersFragment() {
        // Required empty public constructor
    }

    public static OthersFragment newInstance() {
        return new OthersFragment();
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
        return inflater.inflate(R.layout.other_details_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = new SharedPreference();

        txtSkills = view.findViewById(R.id.txtSkills);
        txtJoblocation = view.findViewById(R.id.txtJoblocation);
        detailShow = view.findViewById(R.id.detailshow);
        ppShown = view.findViewById(R.id.privacypolicy);
        tcShown = view.findViewById(R.id.tc);
        workStatusGroup = view.findViewById(R.id.workstatus_group);
        fresher = view.findViewById(R.id.fresher);
        experienced = view.findViewById(R.id.experienced);
        ppShown = view.findViewById(R.id.privacypolicy);
        txtPpolicy = view.findViewById(R.id.privacypolicytext);
        txtPpolicy.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tctext = view.findViewById(R.id.tctext);
        tctext.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        fblSkills = view.findViewById(R.id.fbl_skills);
        fblLocation = view.findViewById(R.id.fbl_location);

        selectedSkills = new ArrayList<>();
        selectedLocation = new ArrayList<>();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        allSkills = loadAllSkills();
        allLocation = loadAllLocation();

        workStatusGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonID = group.getCheckedRadioButtonId();
                View radioButton = group.findViewById(radioButtonID);
                RegistrationActivity.workStatus = String.valueOf(group.indexOfChild(radioButton));
                pref.putString(getActivity(), U.SH_WORK_STATUS, RegistrationActivity.workStatus);
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
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!pref.getString(getActivity(), U.SH_WORK_STATUS).equals("")) {
            String workStatus = pref.getString(getActivity(), U.SH_WORK_STATUS);
            if (workStatus.equals("0")) {fresher.setChecked(true);
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
                String skillsArray[] = RegistrationActivity.skills.split(",");
                selectedSkills.clear();
                selectedSkills.addAll(Arrays.asList(skillsArray));
                setSkillsValues(fblSkills);
            }
        }

        if (!pref.getString(getActivity(), U.SH_JOBLOCATION).equals("")) {
            RegistrationActivity.jobPrefferedLocation = pref.getString(getActivity(), U.SH_JOBLOCATION);
            String skillsArray[] = RegistrationActivity.jobPrefferedLocation.split(",");
            selectedLocation.clear();
            selectedLocation.addAll(Arrays.asList(skillsArray));
            setLocationValues(fblLocation);
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

    //-------------------------------------------- pp & tc -----------------------------------------

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

    //------------------------------------------- skills -------------------------------------------

    public List<Item> loadAllSkills() {
        List<Item> allSkills = new ArrayList<>();
        for (int i = 0; i < RegistrationActivity.skillsList.size(); i++) {
            for (int j = 0; j < RegistrationActivity.skillsList.get(i).getList().size(); j++) {
                Item data = new Item();
                data.setId(RegistrationActivity.skillsList.get(i).getList().get(j).getId());
                data.setItem(RegistrationActivity.skillsList.get(i).getList().get(j).getItem());
                allSkills.add(data);
            }
        }
        return allSkills;
    }

    @SuppressLint("SetTextI18n")
    public void ShowSkillsDialog() {
        if (getActivity() != null) {
            skillsDia = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            if(skillsDia.getWindow()!=null) {
                skillsDia.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            }
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
                        if(suggestDia.getWindow()!=null) {
                            suggestDia.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        }
                        final EditText localEditText = suggestDia.findViewById(R.id.user_input);
                        final ListView localListView = suggestDia.findViewById(R.id.list);
                        List localArrayList = new ArrayList();
                        localArrayList.addAll(allSkills);
                        final OthersFragment.suggestionAdapter localsuggestionAdapter = new OthersFragment.suggestionAdapter(getActivity(), R.layout.extra_skills_lay, localEditText, localListView, localArrayList);
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
                                                        public void onClick(DialogInterface paramAnonymous3DialogInterface, int paramAnonymous3Int) {
                                                            alertDia.dismiss();
                                                        }
                                                    });
                                                    alertDia = localBuilder.create();
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
                        String skillsArray[] = RegistrationActivity.skills.split(",");
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
                temp.setBackgroundResource(R.drawable.rect_yellow);
                temp.setTextColor(getActivity().getResources().getColor(R.color.black));
                temp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchView.setQuery("", true);
                        for (int i = 0; i < RegistrationActivity.skillsList.size(); i++) {
                            textViews[i].setBackgroundResource(R.drawable.rect_yellow);
                        }
                        temp.setBackgroundResource(R.drawable.rounded_sqr);

                        //Ascending order
                        Collections.sort(RegistrationActivity.skillsList.get(temp.getId()).getList(), new Comparator<Item>() {
                            @Override
                            public int compare(Item o1, Item o2) {
                                return o1.getItem().compareTo(o2.getItem());
                            }
                        });

                        skilladapter = new SkillsSelectionAdapter(getActivity(), R.layout.filter_layout,
                                RegistrationActivity.skillsList.get(temp.getId()).getList());
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
                            Toast.makeText(getActivity(), "Select any skills", Toast.LENGTH_SHORT).show();
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
                }
            });
            skillsDia.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    searchView.setQuery("", true);
                    txtSkills.setEnabled(true);
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

    @SuppressLint("SetTextI18n")
    public void wholeSkillsDialog() {
        if(getActivity()!=null) {
            allSkillsDia = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            allSkillsDia.requestWindowFeature(Window.FEATURE_NO_TITLE);
            allSkillsDia.setCancelable(true);
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
                        Toast.makeText(getActivity(), "Select any skills", Toast.LENGTH_SHORT).show();
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
                        allSkillsDia.dismiss();
                        skilladapter.notifyDataSetChanged();
                        if (skillsDia != null && skillsDia.isShowing()) {
                            skillsDia.dismiss();
                        }
                    }
                    setSkillsValues(fblSkills);
                }
            });
            allSkillsDia.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchView.setQuery("", true);
                    allSkillsDia.dismiss();
                }
            });

            allSkillsDia.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    searchView.setQuery("", true);
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

            final SkillsSelectionAdapter SkillAdapter = new SkillsSelectionAdapter(getActivity(), R.layout.filter_layout, allSkills);
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

    @SuppressLint("SetTextI18n")
    private void setSkillsValues(final FlexboxLayout fbl) {
        final List<ArrayItem> list = new ArrayList<>();
        Log.e("removeId_set", "" + selectedSkills);
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

    @SuppressLint("SetTextI18n")
    public void ShowLocationDialog() {
        if (getActivity() != null) {
            locationDia = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            if(locationDia.getWindow()!=null) {
                locationDia.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            }
            locationDia.setContentView(R.layout.selection_layout);
            final Button selectBtn = locationDia.findViewById(R.id.set_btn);
            TextView task = locationDia.findViewById(R.id.task);
            task.setText("Preffered location :");

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
                    String skillsArray[] = RegistrationActivity.jobPrefferedLocation.split(",");
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
                temp.setBackgroundResource(R.drawable.rect_yellow);
                temp.setTextColor(getActivity().getResources().getColor(R.color.black));
                temp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchView.setQuery("", true);
                        for (int i = 0; i < RegistrationActivity.locationList.size(); i++) {
                            textViews[i].setBackgroundResource(R.drawable.rect_yellow);
                        }
                        temp.setBackgroundResource(R.drawable.rounded_sqr);

                        //Ascending order
                        Collections.sort(RegistrationActivity.locationList.get(temp.getId()).getList(), new Comparator<Item>() {
                            @Override
                            public int compare(Item o1, Item o2) {
                                return o1.getItem().compareTo(o2.getItem());
                            }
                        });

                        locationadapter = new LocationSelectionAdapter(getActivity(), R.layout.filter_layout, temp.getId(),
                                RegistrationActivity.locationList.get(temp.getId()).getList());
                        listview.setTextFilterEnabled(true);
                        listview.setAdapter(locationadapter);

                        all = locationDia.findViewById(R.id.all_checkbox);
                        all.setVisibility(View.VISIBLE);
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
                                                selectedLocation.add("" + RegistrationActivity.locationList.get(temp.getId()).getList().get(k).getId());
                                            }
                                            locationadapter.notifyDataSetChanged();
                                        }
                                    }
                                } else {
                                    if (selectedAll == 0) {
                                        for (int j = 0; j < RegistrationActivity.locationList.get(temp.getId()).getList().size(); j++) {
                                            if (selectedLocation.contains("" + RegistrationActivity.locationList.get(temp.getId()).getList().get(j).getId())) {
                                                selectedLocation.remove("" + RegistrationActivity.locationList.get(temp.getId()).getList().get(j).getId());
                                            }
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
                        Toast.makeText(getActivity(), "Select any location", Toast.LENGTH_SHORT).show();
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
                    setLocationValues(fblLocation);
                }
            });
            locationDia.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    searchView.setQuery("", true);
                    txtJoblocation.setEnabled(true);
                }
            });
            textViews[0].performClick();
            locationDia.show();

            if (pref.getInt(getActivity(), U.SH_GUIDE_WINDOW_3) == 0) {
                final Dialog guideWindow = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                guideWindow.setContentView(R.layout.guide_window_search);
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

    //------------------------------------------- Location -----------------------------------------

    public List<Item> loadAllLocation() {
        List<Item> allLocation = new ArrayList<>();
        for (int i = 0; i < RegistrationActivity.locationList.size(); i++) {
            for (int j = 0; j < RegistrationActivity.locationList.get(i).getList().size(); j++) {
                Item data = new Item();
                data.setId(RegistrationActivity.locationList.get(i).getList().get(j).getId());
                data.setItem(RegistrationActivity.locationList.get(i).getList().get(j).getItem());
                allLocation.add(data);
            }
        }
        return allLocation;
    }

    @SuppressLint("SetTextI18n")
    public void wholeLocationDialog() {
        if(getActivity()!=null) {
            allLocationDia = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            allLocationDia.requestWindowFeature(Window.FEATURE_NO_TITLE);
            allLocationDia.setCancelable(true);
            allLocationDia.setContentView(R.layout.custom_dialog);
            if(allLocationDia.getWindow()!=null) {
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
                        if (locationDia != null && locationDia.isShowing()) {
                            locationDia.dismiss();
                        }
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
                    setLocationValues(fblLocation);
                }
            });
            allLocationDia.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchView.setQuery("", true);
                    allLocationDia.dismiss();
                }
            });

            allLocationDia.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    searchView.setQuery("", true);
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

            final LocationSelectionAdapter locationadapter = new LocationSelectionAdapter(getActivity(), R.layout.filter_layout, 0, allLocation);
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
        Log.e("removeId_set", "" + selectedLocation);
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
            if(getActivity()!=null){
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

    public class SkillsSelectionAdapter extends ArrayAdapter {

        Context context;
        List<Item> list;
        LayoutInflater inflater;
        List<Item> orig;

        public SkillsSelectionAdapter(Context context, int resource, List<Item> list) {
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
            CheckBox checkBox = view.findViewById(R.id.checkbox);
            txt.setText(list.get(position).getItem());
            if (selectedSkills.contains("" + list.get(position).getId())) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (skillDetail.isChecked()) {
                            skillDetail.setChecked(false);
                        }
                        selectedSkills.add("" + list.get(position).getId());
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
            context = context;
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
            View view = inflater.inflate(R.layout.group_view, null);
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

        public LocationSelectionAdapter(Context context, int resource, int distId, List<Item> list) {
            super(context, resource, list);
            this.context = context;
            this.list = list;
            this.distId = distId;
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

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
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

}
