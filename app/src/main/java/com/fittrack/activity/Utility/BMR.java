package com.fittrack.activity.Utility;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fittrack.App;
import com.fittrack.Constants.Constants;
import com.fittrack.Progressbar.SmoothProgressBar;
import com.fittrack.R;
import com.fittrack.Utility.LogUtil;

import java.util.ArrayList;

public class BMR extends AppCompatActivity implements View.OnClickListener {

    /*header View*/
    private TextView tv_title;
    private ImageView img_back;
    ArrayList<String> weight_unit_list = new ArrayList<>();
    private BMR activity;
    private EditText et_age, et_weight, et_height;
    private TextView tv_reset, tv_calculate, tv_bmr, tv_weight_loss_calories, tv_weight_gain_calories, tv_maintain_calories, tv_cal_value, tv_male, tv_female;
    private Spinner sp_weight_unit;
    private SmoothProgressBar Progressbar;
    String weight_unit = "";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.RegisterActivityForBugsense(activity);
        setContentView(R.layout.activity_bmr);

        activity = BMR.this;
        HeaderView();
        findViewById();
        OnClickListener();

//        setSpinnerofWeight();

//        if (App.Utils.getString(Constants.user_id).equalsIgnoreCase("")) {
//            ll_gender.setVisibility(View.VISIBLE);
//            flag_male = true;
//            flag_female = false;
//        } else {
//            ll_gender.setVisibility(View.GONE);
//
//        }
    }

    /**
     * set Weight Spinner
     */
//    private void setSpinnerofWeight() {
//
//        weight_unit_list.add(getString(R.string.text_kg));
////        weight_unit_list.add(getString(R.string.text_pound));
//
//        final ArrayAdapter<String> adapter_gender = new ArrayAdapter<String>(activity,
//                R.layout.spinner_dropdown_gender, R.id.tv_spinner_items, weight_unit_list);
//        sp_weight_unit.setAdapter(adapter_gender);
//        sp_weight_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                weight_unit = weight_unit_list.get(i).toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//    }

    /**
     * Header view
     */
    private void OnClickListener() {
        tv_calculate.setOnClickListener(this);
        tv_reset.setOnClickListener(this);
        ll_normal.setOnClickListener(this);
        ll_light.setOnClickListener(this);
        ll_medium.setOnClickListener(this);
        ll_active.setOnClickListener(this);
        ll_check.setOnClickListener(this);
//        tv_male.setOnClickListener(this);
//        tv_female.setOnClickListener(this);

    }

    private void HeaderView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.text_bmr));
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setVisibility(View.VISIBLE);
        img_back.setOnClickListener(this);
    }

    /**
     * findViews by Ids
     */
    private void findViewById() {

        et_age = (EditText) findViewById(R.id.et_age);
        et_weight = (EditText) findViewById(R.id.et_weight);
        et_height = (EditText) findViewById(R.id.et_height);
        tv_reset = (TextView) findViewById(R.id.tv_reset);
        tv_calculate = (TextView) findViewById(R.id.tv_calculate);
//        sp_weight_unit = (Spinner) findViewById(R.id.sp_weight_unit);
        tv_bmr = (TextView) findViewById(R.id.tv_bmr);
        ll_normal = (LinearLayout) findViewById(R.id.ll_normal);
        ll_light = (LinearLayout) findViewById(R.id.ll_light);
        ll_medium = (LinearLayout) findViewById(R.id.ll_medium);
        ll_active = (LinearLayout) findViewById(R.id.ll_active);
        ll_check = (LinearLayout) findViewById(R.id.ll_check);
        tv_weight_loss_calories = (TextView) findViewById(R.id.tv_weight_loss_calories);
        tv_weight_gain_calories = (TextView) findViewById(R.id.tv_weight_gain_calories);
        tv_maintain_calories = (TextView) findViewById(R.id.tv_maintain_calories);
        tv_cal_value = (TextView) findViewById(R.id.tv_cal_value);
//        ll_gender = (LinearLayout) findViewById(R.id.ll_gender);
//        tv_male = (TextView) findViewById(R.id.tv_male);
//        tv_female = (TextView) findViewById(R.id.tv_female);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_back:
                onBackPressed();
                break;

            case R.id.tv_reset:
                et_age.setText("");
                et_weight.setText("");
                et_height.setText("");
                tv_bmr.setText("");
                tv_cal_value.setText("");
                tv_weight_loss_calories.setText("");
                tv_maintain_calories.setText("");
                tv_weight_gain_calories.setText("");
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
//
//            case R.id.tv_female:
//                flag_male = false;
//                flag_female = true;
//                tv_female.setBackgroundResource(R.drawable.bg_round_fill_green);
//                tv_male.setBackgroundResource(R.drawable.bg_round_fill_grey);
//                tv_female.setTextColor(getResources().getColor(R.color.color_white, null));
//                tv_male.setTextColor(getResources().getColor(R.color.color_bg_header, null));
//                break;

            case R.id.tv_calculate:

                if (et_age.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_age_empty), getResources().getString(R.string.app_name));

                } else if (et_weight.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_height_empty), getResources().getString(R.string.app_name));

                } else if (et_height.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_weight_empty), getResources().getString(R.string.app_name));

                } else {
                    View view = getCurrentFocus();
                    if (view != null) {

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    LogUtil.Print("gender", "" + App.Utils.getString(Constants.gender));
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
                break;

            case R.id.ll_normal:

                dbl_normal = 1.2 * dbl_bmr;

                LogUtil.Print("dbl_normal", "  " + dbl_normal);

                int normal_loss = (int) (dbl_normal - 300);
                int normal_gain = (int) (dbl_normal + 300);

                LogUtil.Print("normal_loss ", "  " + normal_loss + " gain   " + normal_gain);
                tv_weight_loss_calories.setText("" + normal_loss);
                tv_weight_gain_calories.setText("" + normal_gain);
                tv_maintain_calories.setText("" + (int) dbl_normal);
                tv_cal_value.setText("" + (int) dbl_normal + "  " + getResources().getString(R.string.text_calories));

                break;

            case R.id.ll_light:

                dbl_light = 1.375 * dbl_bmr;
                LogUtil.Print("dbl_light", "  " + dbl_light);

                int light_loss = (int) (dbl_light - 300);
                int light_gain = (int) (dbl_light + 300);

                LogUtil.Print("light_loss ", "  " + light_loss + " gain   " + light_gain);
                tv_weight_loss_calories.setText("" + light_loss);
                tv_weight_gain_calories.setText("" + light_gain);
                tv_maintain_calories.setText("" + (int) dbl_light);
                tv_cal_value.setText("" + (int) dbl_light + "  " + getResources().getString(R.string.text_calories));
                break;

            case R.id.ll_medium:

                dbl_medium = 1.55 * dbl_bmr;
                LogUtil.Print("dbl_light", "  " + dbl_light);

                int medium_loss = (int) (dbl_medium - 300);
                int medium_gain = (int) (dbl_medium + 300);

                LogUtil.Print("medium_loss ", "  " + medium_loss + " gain   " + medium_gain);
                tv_weight_loss_calories.setText("" + medium_loss);
                tv_weight_gain_calories.setText("" + medium_gain);
                tv_maintain_calories.setText("" + (int) dbl_medium);
                tv_cal_value.setText("" + (int) dbl_medium + "  " + getResources().getString(R.string.text_calories));
                break;

            case R.id.ll_active:

                dbl_active = 1.725 * dbl_bmr;
                LogUtil.Print("dbl_light", "  " + dbl_light);

                int active_loss = (int) (dbl_active - 300);
                int active_gain = (int) (dbl_active + 300);

                LogUtil.Print("active_loss ", "  " + active_loss + " gain   " + active_gain);
                tv_weight_loss_calories.setText("" + active_loss);
                tv_weight_gain_calories.setText("" + active_gain);
                tv_maintain_calories.setText("" + (int) dbl_active);
                tv_cal_value.setText("" + (int) dbl_active + "  " + getResources().getString(R.string.text_calories));
                break;
        }
    }

    /**
     * Check Info Dialog
     */
    private void CheckInfoDialog() {
        metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        CustomDialog = new Dialog(activity);
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
