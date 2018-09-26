package com.fittrack.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.fittrack.App;
import com.fittrack.Constants.Constants;
import com.fittrack.Progressbar.SmoothProgressBar;
import com.fittrack.R;
import com.fittrack.Utility.LogUtil;
import com.fittrack.activity.Home;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Umesh on 8/17/2017.
 */
public class BMRFragment extends Fragment implements View.OnClickListener {

    /*header View*/
    private TextView tv_title;
    private ImageView img_drawer;
    ArrayList<String> gender_list = new ArrayList<>();
    private EditText et_age, et_weight, et_height;
    private TextView tv_reset, tv_calculate, tv_bmr, tv_weight_loss_calories, tv_weight_gain_calories, tv_maintain_calories, tv_cal_value, tv_gender;
    private Spinner sp_weight_gender;
    private SmoothProgressBar Progressbar;
    String str_gender = "";
    private ImageView img_logout, img_Profile;
    private LinearLayout ll_edit;
    String str_height = "", str_weight = "";
    private LinearLayout ll_normal, ll_light, ll_medium, ll_active, ll_check, ll_gender;
    double dbl_bmr, dbl_light, dbl_medium, dbl_active, dbl_normal;

    /*Dialog*/
    private ImageView img_cancel;
    private LinearLayout ll_continue;
    private DisplayMetrics metrics;
    private Dialog CustomDialog;
    boolean flag_male, flag_female;
    private View view;
    private AdView mAdView;
    private RadioButton rb_normal, rb_light, rb_medium, rb_active;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_bmr, container, false);
        HeaderView(rootView);
        findViewById(rootView);
        OnClickListener();
        setData();
        setSpinnerofGender();
        return rootView;
    }

    /**
     * Calculate age from Birthdate
     */
    private static int CalculateAge(String birthdate) {
        LogUtil.Print("CurrentBirthdate==>", "" + birthdate);
        int age = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date1 = sdf.parse(birthdate);
            Calendar now = Calendar.getInstance();
            Calendar dob = Calendar.getInstance();
            dob.setTime(date1);
            if (dob.after(now)) {
                throw new IllegalArgumentException("Can't be born in the future");
            }
            int year1 = now.get(Calendar.YEAR);
            int year2 = dob.get(Calendar.YEAR);
            age = year1 - year2;
            int month1 = now.get(Calendar.MONTH);
            int month2 = dob.get(Calendar.MONTH);
            if (month2 > month1) {
                age--;
            } else if (month1 == month2) {
                int day1 = now.get(Calendar.DAY_OF_MONTH);
                int day2 = dob.get(Calendar.DAY_OF_MONTH);
                if (day2 > day1) {
                    age--;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LogUtil.Print("age==>", "" + age);
        return age;
    }

    /**
     * set Weight Spinner
     */
    private void setSpinnerofGender() {
        gender_list.add(getString(R.string.text_gender));
        gender_list.add(getString(R.string.text_male));
        gender_list.add(getString(R.string.text_female));
        final ArrayAdapter<String> adapter_gender = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_month_items, R.id.tv_spinner_items, gender_list);
        sp_weight_gender.setAdapter(adapter_gender);
        sp_weight_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                sp_weight_gender.setSelection(0);
                str_gender = gender_list.get(i).toString();
                if (i > 0) {
                    tv_gender.setText(str_gender);
                }
                LogUtil.Print("str_gender==>", "" +str_gender);
                if (str_gender.equalsIgnoreCase(getResources().getString(R.string.text_male))) {
                    flag_male = true;
                    flag_female = false;
                } else if (str_gender.equalsIgnoreCase(getResources().getString(R.string.text_female))) {
                    flag_male = false;
                    flag_female = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    /**
     * set Data Initially
     */
    private void setData() {

        LogUtil.Print("genderset==>", "" + App.USER_DETAILS.getGender() + " " + App.Utils.getString(Constants.gender));

        if (!App.Utils.getString(Constants.gender).equalsIgnoreCase("")) {
            et_weight.setText(App.USER_DETAILS.getWeight());
            et_height.setText(App.USER_DETAILS.getHeight());
            tv_gender.setText(App.Utils.getString(Constants.gender));
            et_age.setText(Integer.toString(CalculateAge(App.USER_DETAILS.getBirthdate())));

            if (Integer.parseInt(et_height.getText().toString()) >= 0 || Integer.parseInt(et_weight.getText().toString()) >= 0
                    || Integer.parseInt(et_age.getText().toString()) >= 0) {
                CalculateBMR();
            }
        }
    }

    /**
     * Header view
     */
    private void OnClickListener() {
        tv_calculate.setOnClickListener(this);
        tv_reset.setOnClickListener(this);
        rb_normal.setOnClickListener(this);
        rb_light.setOnClickListener(this);
        rb_medium.setOnClickListener(this);
        rb_active.setOnClickListener(this);
        ll_check.setOnClickListener(this);
//        tv_male.setOnClickListener(this);
//        tv_female.setOnClickListener(this);

    }

    /**
     * Header view
     *
     * @param rootView
     */
    private void HeaderView(View rootView) {
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.text_bmr));
        img_drawer = (ImageView) rootView.findViewById(R.id.img_drawer);
        img_drawer.setVisibility(View.VISIBLE);
        img_drawer.setOnClickListener(this);
    }

    /**
     * findViews by Ids
     *
     * @param rootView
     */
    private void findViewById(View rootView) {

        et_age = (EditText) rootView.findViewById(R.id.et_age);
        et_weight = (EditText) rootView.findViewById(R.id.et_weight);
        et_height = (EditText) rootView.findViewById(R.id.et_height);
        tv_reset = (TextView) rootView.findViewById(R.id.tv_reset);
        tv_calculate = (TextView) rootView.findViewById(R.id.tv_calculate);
        sp_weight_gender = (Spinner) rootView.findViewById(R.id.sp_weight_gender);
        tv_bmr = (TextView) rootView.findViewById(R.id.tv_bmr);
        ll_normal = (LinearLayout) rootView.findViewById(R.id.ll_normal);
        ll_light = (LinearLayout) rootView.findViewById(R.id.ll_light);
        ll_medium = (LinearLayout) rootView.findViewById(R.id.ll_medium);
        ll_active = (LinearLayout) rootView.findViewById(R.id.ll_active);
        ll_check = (LinearLayout) rootView.findViewById(R.id.ll_check);
        tv_weight_loss_calories = (TextView) rootView.findViewById(R.id.tv_weight_loss_calories);
        tv_weight_gain_calories = (TextView) rootView.findViewById(R.id.tv_weight_gain_calories);
        tv_maintain_calories = (TextView) rootView.findViewById(R.id.tv_maintain_calories);
        tv_cal_value = (TextView) rootView.findViewById(R.id.tv_cal_value);
        rb_normal = (RadioButton) rootView.findViewById(R.id.rb_normal);
        rb_light = (RadioButton) rootView.findViewById(R.id.rb_light);
        rb_medium = (RadioButton) rootView.findViewById(R.id.rb_medium);
        rb_active = (RadioButton) rootView.findViewById(R.id.rb_active);
        tv_gender = (TextView) rootView.findViewById(R.id.tv_gender);
//        ll_gender = (LinearLayout) rootView.findViewById(R.id.ll_gender);
//        tv_male = (TextView) rootView.findViewById(R.id.tv_male);
//        tv_female = (TextView) rootView.findViewById(R.id.tv_female);
        mAdView = (AdView) rootView.findViewById(R.id.adView);
        Home.getAds(mAdView);
        setListnerofAdsView();

    }

    /**
     * set Listner to AdsView
     */
    private void setListnerofAdsView() {
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
//                Toast.makeText(getActivity(), "Ad is loaded!", Toast.LENGTH_SHORT).show();
                mAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdClosed() {
//                Toast.makeText(getActivity(), "Ad is closed!", Toast.LENGTH_SHORT).show();
                mAdView.setVisibility(View.GONE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
//                Toast.makeText(getActivity(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
                mAdView.setVisibility(View.GONE);

            }

            @Override
            public void onAdLeftApplication() {
//                Toast.makeText(getActivity(), "Ad left application!", Toast.LENGTH_SHORT).show();
                mAdView.setVisibility(View.GONE);

            }

            @Override
            public void onAdOpened() {
//                Toast.makeText(getActivity(), "Ad is opened!", Toast.LENGTH_SHORT).show();
                mAdView.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_drawer:
                view = getActivity().getCurrentFocus();
                if (view != null) {

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if (Home.drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    Home.drawer_layout.closeDrawer(GravityCompat.START);
                } else {
                    Home.drawer_layout.openDrawer(GravityCompat.START);
                }
                break;

            case R.id.tv_reset:
                et_age.setText("");
                et_weight.setText("");
                et_height.setText("");
                tv_bmr.setText("");
                tv_gender.setText(getResources().getString(R.string.text_gender));
                tv_cal_value.setText("");
                tv_weight_loss_calories.setText("-500");
                tv_maintain_calories.setText("0");
                tv_weight_gain_calories.setText("500");
                rb_normal.setChecked(false);
                rb_light.setChecked(false);
                rb_medium.setChecked(false);
                rb_active.setChecked(false);
                sp_weight_gender.setSelection(0);
                break;

            case R.id.ll_check:
                CheckInfoDialog();
                break;

            case R.id.img_cancel:
                CustomDialog.dismiss();
                break;

            case R.id.ll_continue:
                CustomDialog.dismiss();
                break;

//            case R.id.tv_male:
//                flag_male = true;
//                flag_female = false;
//                tv_male.setBackgroundResource(R.drawable.bg_round_fill_green);
//                tv_female.setBackgroundResource(R.drawable.bg_round_fill_grey);
//                tv_female.setTextColor(getResources().getColor(R.color.color_bg_header, null));
//                tv_male.setTextColor(getResources().getColor(R.color.color_white, null));
//                break;

//            case R.id.tv_female:
//                flag_male = false;
//                flag_female = true;
//                tv_female.setBackgroundResource(R.drawable.bg_round_fill_green);
//                tv_male.setBackgroundResource(R.drawable.bg_round_fill_grey);
//                tv_female.setTextColor(getResources().getColor(R.color.color_white, null));
//                tv_male.setTextColor(getResources().getColor(R.color.color_bg_header, null));
//                break;

            case R.id.tv_calculate:
                CalculateBMR();
                break;

            case R.id.rb_normal:

                dbl_normal = 1.2 * dbl_bmr;
                LogUtil.Print("dbl_normal", "  " + dbl_normal);
                int normal_loss = (int) (dbl_normal - 500);
                int normal_gain = (int) (dbl_normal + 500);

                LogUtil.Print("normal_loss ", "  " + normal_loss + " gain   " + normal_gain);
                tv_weight_loss_calories.setText("" + normal_loss);
                tv_weight_gain_calories.setText("" + normal_gain);
                tv_maintain_calories.setText("" + (int) dbl_normal);
                tv_cal_value.setText("" + (int) dbl_normal + "  " + getResources().getString(R.string.text_calories));
                rb_normal.setChecked(true);
                rb_light.setChecked(false);
                rb_medium.setChecked(false);
                rb_active.setChecked(false);
                break;

            case R.id.rb_light:
                dbl_light = 1.375 * dbl_bmr;
                LogUtil.Print("dbl_light", "  " + dbl_light);

                int light_loss = (int) (dbl_light - 500);
                int light_gain = (int) (dbl_light + 500);

                LogUtil.Print("light_loss ", "  " + light_loss + " gain   " + light_gain);
                tv_weight_loss_calories.setText("" + light_loss);
                tv_weight_gain_calories.setText("" + light_gain);
                tv_maintain_calories.setText("" + (int) dbl_light);
                tv_cal_value.setText("" + (int) dbl_light + "  " + getResources().getString(R.string.text_calories));
                rb_normal.setChecked(false);
                rb_light.setChecked(true);
                rb_medium.setChecked(false);
                rb_active.setChecked(false);
                break;

            case R.id.rb_medium:

                dbl_medium = 1.55 * dbl_bmr;
                LogUtil.Print("dbl_light", "  " + dbl_light);

                int medium_loss = (int) (dbl_medium - 500);
                int medium_gain = (int) (dbl_medium + 500);

                LogUtil.Print("medium_loss ", "  " + medium_loss + " gain   " + medium_gain);
                tv_weight_loss_calories.setText("" + medium_loss);
                tv_weight_gain_calories.setText("" + medium_gain);
                tv_maintain_calories.setText("" + (int) dbl_medium);
                tv_cal_value.setText("" + (int) dbl_medium + "  " + getResources().getString(R.string.text_calories));
                rb_normal.setChecked(false);
                rb_light.setChecked(false);
                rb_medium.setChecked(true);
                rb_active.setChecked(false);
                break;

            case R.id.rb_active:

                dbl_active = 1.725 * dbl_bmr;
                LogUtil.Print("dbl_light", "  " + dbl_light);

                int active_loss = (int) (dbl_active - 500);
                int active_gain = (int) (dbl_active + 500);

                LogUtil.Print("active_loss ", "  " + active_loss + " gain   " + active_gain);
                tv_weight_loss_calories.setText("" + active_loss);
                tv_weight_gain_calories.setText("" + active_gain);
                tv_maintain_calories.setText("" + (int) dbl_active);
                tv_cal_value.setText("" + (int) dbl_active + "  " + getResources().getString(R.string.text_calories));
                rb_normal.setChecked(false);
                rb_light.setChecked(false);
                rb_medium.setChecked(false);
                rb_active.setChecked(true);
                break;
        }
    }

    /**
     * Calculate BMR
     */
    private void CalculateBMR() {

        if (et_age.getText().toString().trim().isEmpty()) {

            App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_age_empty), getResources().getString(R.string.app_name));

        } else if (sp_weight_gender.getSelectedItemPosition() == 0) {

            App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_gender_empty), getResources().getString(R.string.app_name));

        } else if (et_weight.getText().toString().trim().isEmpty()) {

            App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_height_empty), getResources().getString(R.string.app_name));

        } else if (et_height.getText().toString().trim().isEmpty()) {

            App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_weight_empty), getResources().getString(R.string.app_name));

        } else {
            view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            LogUtil.Print("gender", "" + tv_gender.getText().toString());
                    /*Formula to calculate bmr*/

//                Men: BMR'='88.362'+'(13.397'x'weight'in'kg)'+'(4.799'x'height'in'cm)'-'(5.677'x'age'in'years)'

//                Women: BMR'='447.593'+'(9.247'x'weight'in'kg)'+'(3.098'x'height'in'cm)'-'(4.330'x'
//                age'in'years)'

                    /*Multiply BMR to following number depending on activity like
                         1.2    1.375    1.55  1.725

                    * Normal:- 1.2 * BMR
                    * Light:- 1.375 * BMR
                    * Medium:-1.55 * BMR
                    * Active:-1.725 * BMR */


            if ((App.Utils.getString(Constants.gender).equalsIgnoreCase(getResources().getString(R.string.text_female))) || flag_female) {

                double d1 = (9.247 * Double.parseDouble(et_weight.getText().toString()));
                double d2 = (3.098 * Double.parseDouble(et_height.getText().toString()));
                double d3 = (4.330 * Double.parseDouble(et_age.getText().toString()));

                LogUtil.Print("dbls_female", "  " + d1 + "  " + d2 + "  " + d3);

                dbl_bmr = (447.593 + d1 + d2 - d3);
                LogUtil.Print("dbl_final_female", "  " + dbl_bmr);
                tv_bmr.setText(String.format("%.2f", dbl_bmr));

            } else if ((App.Utils.getString(Constants.gender).equalsIgnoreCase(getResources().getString(R.string.text_male))) || flag_male) {

                double d6 = (13.397 * Double.parseDouble(et_weight.getText().toString()));
                double d7 = (4.799 * Double.parseDouble(et_height.getText().toString()));
                double d8 = (5.677 * Double.parseDouble(et_age.getText().toString()));

                LogUtil.Print("dbls_male", "  " + d6 + "  " + d7 + "  " + d8 + " " + Double.parseDouble(et_weight.getText().toString()));
                dbl_bmr = (88.362 + d6 + d7 - d8);

                LogUtil.Print("dbl_final_male", "  " + dbl_bmr);
                tv_bmr.setText(String.format("%.2f", dbl_bmr));

            } else {
            }
        }
    }

    /**
     * Check Info Dialog
     */
    private void CheckInfoDialog() {
        metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        CustomDialog = new Dialog(getActivity());
        CustomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        CustomDialog.setCancelable(true);
        CustomDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        CustomDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        CustomDialog.setContentView(R.layout.bmr_info);
        img_cancel = (ImageView) CustomDialog.findViewById(R.id.img_cancel);
        ll_continue = (LinearLayout) CustomDialog.findViewById(R.id.ll_continue);
        img_cancel.setOnClickListener(this);
        ll_continue.setOnClickListener(this);

        CustomDialog.show();
        CustomDialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
