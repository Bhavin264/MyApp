package com.fittrack.activity.Utility;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.fittrack.App;
import com.fittrack.Constants.Api;
import com.fittrack.Constants.Constants;
import com.fittrack.Interface.ApiInterface;
import com.fittrack.Model.GsonLogout;
import com.fittrack.Progressbar.SmoothProgressBar;
import com.fittrack.R;
import com.fittrack.Utility.LogUtil;
import com.fittrack.Utility.MyUtils;
import com.fittrack.activity.Login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BMI extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawer_layout;

    /*header View*/
    private TextView tv_title;
    private ImageView img_drawer;
    ArrayList<String> weight_unit_list = new ArrayList<>();
    private BMI activity;
    private EditText et_age, et_weight, et_height;
    private TextView tv_reset, tv_calculate, tv_username, tv_current_bmi, tv_current_bmr, tv_weight_sheet, tv_photo_gallery, tv_chart, tv_make_compare, tv_forum,
            tv_feedback, tv_bmi_value, tv_bmi_category, tv_login, tv_share;
    private Spinner sp_weight_unit;
    String weight_unit = "";
    private ImageView img_logout, img_Profile;
    private LinearLayout ll_edit, ll_check, ll_logout, ll_main;
    String str_height = "", str_weight = "";
    private ImageView img_underweight, img_normal, img_overweight, img_obese;
    private DisplayMetrics metrics;
    private Dialog CustomDialog, DialogLogout;
    /*Dialog*/
    private ImageView img_cancel, img_close;
    private LinearLayout ll_continue;
    private int i = 0;
    private Intent intent;
    private SmoothProgressBar Progressbar;
    private TextView tv_yes, tv_no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.RegisterActivityForBugsense(activity);
        setContentView(R.layout.activity_bmi);

        activity = BMI.this;
        HeaderView();
        findViewById();
        OnClickListener();
//        setSpinnerofWeight();
        setProfilePhoto();

    }



    private void setProfilePhoto() {

        if (!App.Utils.getString(Constants.user_id).equalsIgnoreCase("")) {
            ll_main.setVisibility(View.VISIBLE);
            ll_logout.setVisibility(View.GONE);
            if (!App.USER_DETAILS.getProfileImage().equalsIgnoreCase("")) {
                tv_username.setText(App.USER_DETAILS.getUsername());
                Glide.with(activity).load(App.USER_DETAILS.getProfileImage())
                        .error(R.drawable.user_placeholder_login_screen)
                        .into(img_Profile);
            } else {
                img_Profile.setImageResource(R.drawable.user_placeholder_login_screen);
            }
        } else {
            ll_logout.setVisibility(View.VISIBLE);
            ll_main.setVisibility(View.GONE);
        }
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
     * findViews by Ids
     */
    private void findViewById() {
        et_age = (EditText) findViewById(R.id.et_age);
        et_weight = (EditText) findViewById(R.id.et_weight);
        et_height = (EditText) findViewById(R.id.et_height);
        tv_reset = (TextView) findViewById(R.id.tv_reset);
        tv_calculate = (TextView) findViewById(R.id.tv_calculate);
//        sp_weight_unit = (Spinner) findViewById(R.id.sp_weight_unit);
        img_underweight = (ImageView) findViewById(R.id.img_underweight);
        img_normal = (ImageView) findViewById(R.id.img_normal);
        img_overweight = (ImageView) findViewById(R.id.img_overweight);
        img_obese = (ImageView) findViewById(R.id.img_obese);
        tv_bmi_value = (TextView) findViewById(R.id.tv_bmi_value);
        tv_bmi_category = (TextView) findViewById(R.id.tv_bmi_category);
        /*drawer*/
        img_logout = (ImageView) findViewById(R.id.img_logout);
        img_Profile = (ImageView) findViewById(R.id.img_Profile);
        tv_username = (TextView) findViewById(R.id.tv_username);
        Progressbar = (SmoothProgressBar) findViewById(R.id.google_now);
        tv_current_bmi = (TextView) findViewById(R.id.tv_current_bmi);
        tv_current_bmr = (TextView) findViewById(R.id.tv_current_bmr);
        tv_weight_sheet = (TextView) findViewById(R.id.tv_weight_sheet);
        tv_photo_gallery = (TextView) findViewById(R.id.tv_photo_gallery);
        tv_chart = (TextView) findViewById(R.id.tv_chart);
        tv_make_compare = (TextView) findViewById(R.id.tv_make_compare);
        tv_forum = (TextView) findViewById(R.id.tv_forum);
        tv_feedback = (TextView) findViewById(R.id.tv_feedback);
        tv_share = (TextView) findViewById(R.id.tv_share);
        ll_edit = (LinearLayout) findViewById(R.id.ll_edit);
        ll_check = (LinearLayout) findViewById(R.id.ll_check);
        ll_logout = (LinearLayout) findViewById(R.id.ll_logout);
        ll_main = (LinearLayout) findViewById(R.id.ll_main);
        tv_login = (TextView) findViewById(R.id.tv_login);
    }

    /**
     * OnClickListener of Views
     */
    private void OnClickListener() {

        tv_reset.setOnClickListener(this);
        tv_calculate.setOnClickListener(this);
        tv_current_bmi.setOnClickListener(this);
        tv_current_bmr.setOnClickListener(this);
        tv_weight_sheet.setOnClickListener(this);
        tv_photo_gallery.setOnClickListener(this);
        tv_chart.setOnClickListener(this);
        tv_make_compare.setOnClickListener(this);
        tv_forum.setOnClickListener(this);
        tv_feedback.setOnClickListener(this);
        img_Profile.setOnClickListener(this);
        img_logout.setOnClickListener(this);
        ll_edit.setOnClickListener(this);
        ll_check.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        tv_share.setOnClickListener(this);

    }

    /**
     * Header view
     */
    private void HeaderView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.text_bmi));
        img_drawer = (ImageView) findViewById(R.id.img_drawer);
        img_drawer.setVisibility(View.VISIBLE);
        img_drawer.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_drawer:
                View view = getCurrentFocus();
                if (view != null) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawer(GravityCompat.START);
                } else {
                    drawer_layout.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.tv_reset:

                et_age.setText("");
                et_weight.setText("");
                et_height.setText("");
                break;

            case R.id.tv_calculate:

                CalculateBMI();
                break;

            case R.id.ll_check:
                CheckInfoDilaog();
                break;

            case R.id.img_cancel:
                CustomDialog.dismiss();
                break;

            case R.id.ll_continue:
                CustomDialog.dismiss();
                break;

            case R.id.tv_current_bmi:
                SideMenuSelection(0);
                break;

            case R.id.tv_current_bmr:
                SideMenuSelection(1);
                break;
            case R.id.tv_weight_sheet:

                SideMenuSelection(2);
                break;

            case R.id.tv_photo_gallery:
                SideMenuSelection(3);
                break;

            case R.id.tv_chart:
                SideMenuSelection(4);
                break;

            case R.id.tv_make_compare:
                SideMenuSelection(5);
                break;

            case R.id.tv_forum:
                SideMenuSelection(6);
                break;

            case R.id.tv_feedback:
                SideMenuSelection(7);
                break;

            case R.id.img_logout:
                SideMenuSelection(8);
                break;

            case R.id.ll_edit:
                SideMenuSelection(9);
                break;

            case R.id.tv_share:
                SideMenuSelection(10);
                break;

            case R.id.img_close:
                drawer_layout.closeDrawers();
                if (DialogLogout.isShowing()) {
                    DialogLogout.dismiss();
                }
                break;

            case R.id.tv_yes:
                drawer_layout.closeDrawers();
                if (DialogLogout.isShowing()) {
                    DialogLogout.dismiss();
                }
                CallLogoutApi();
                break;

            case R.id.tv_no:
                drawer_layout.closeDrawers();
                if (DialogLogout.isShowing()) {
                    DialogLogout.dismiss();
                }
                break;

            case R.id.tv_login:
                drawer_layout.closeDrawers();
                intent = new Intent(BMI.this, Login.class);
                startActivity(intent);
                finish();
                break;


        }
    }
    /**
     * Calculate BMI
     */
    private void CalculateBMI() {
        if (et_weight.getText().toString().trim().isEmpty()) {

            App.Utils.ShowAlert(activity, getResources().getString(R.string.text_height_empty), getResources().getString(R.string.app_name));

        } else if (et_height.getText().toString().trim().isEmpty()) {

            App.Utils.ShowAlert(activity, getResources().getString(R.string.text_weight_empty), getResources().getString(R.string.app_name));

        } else {
            View view1 = getCurrentFocus();

            if (view1 != null) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
            }

            str_weight = et_weight.getText().toString();
            str_height = et_height.getText().toString();

            if (str_height != null && !"".equals(str_height)
                    && str_weight != null && !"".equals(str_weight)) {
                float heightValue = Float.parseFloat(str_height) / 100;
                float weightValue = Float.parseFloat(str_weight);

                float bmi = weightValue / (heightValue * heightValue);

                displayBMI(bmi);
            }
        }
    }

    /**
     * Check Info Dialog
     */
    private void CheckInfoDilaog() {
        metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        CustomDialog = new Dialog(activity);
        CustomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        CustomDialog.setCancelable(true);
        CustomDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        CustomDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        CustomDialog.setContentView(R.layout.bmi_info);

        img_cancel = (ImageView) CustomDialog.findViewById(R.id.img_cancel);
        ll_continue = (LinearLayout) CustomDialog.findViewById(R.id.ll_continue);
        img_cancel.setOnClickListener(this);
        ll_continue.setOnClickListener(this);

//                CustomDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_popup_bmi));
        CustomDialog.show();
        CustomDialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * Side menu Selections
     *
     * @param i
     */
    private void SideMenuSelection(int i) {

        if (i == 0) {

            /* Current BMI */
            drawer_layout.closeDrawers();

        } else if (i == 1) {

            /* Current BMR */
            drawer_layout.closeDrawers();
            intent = new Intent(BMI.this, BMR.class);
            startActivity(intent);

        } else if (i == 2) {

            /* Weight Sheet */
            drawer_layout.closeDrawers();
            if (App.Utils.getString(Constants.user_id).equalsIgnoreCase("")) {
                App.Utils.ShowAlert(activity, getResources().getString(R.string.text_login_valid), getResources().getString(R.string.app_name));

            } else {
                intent = new Intent(BMI.this, WeightSheet.class);
                startActivity(intent);
            }

        } else if (i == 3) {

            /* Photo Galllery */
            drawer_layout.closeDrawers();
            if (App.Utils.getString(Constants.user_id).equalsIgnoreCase("")) {
                App.Utils.ShowAlert(activity, getResources().getString(R.string.text_login_valid), getResources().getString(R.string.app_name));

            } else {
                intent = new Intent(BMI.this, PhotoGallery.class);
                startActivity(intent);
            }

        } else if (i == 4) {

            /* Chart */
            if (App.Utils.getString(Constants.user_id).equalsIgnoreCase("")) {
                App.Utils.ShowAlert(activity, getResources().getString(R.string.text_login_valid), getResources().getString(R.string.app_name));

            } else {
                drawer_layout.closeDrawers();
                intent = new Intent(BMI.this, Chart.class);
                startActivity(intent);
            }

        } else if (i == 5) {

            /* make compare college */
            drawer_layout.closeDrawers();
            if (App.Utils.getString(Constants.user_id).equalsIgnoreCase("")) {
                App.Utils.ShowAlert(activity, getResources().getString(R.string.text_login_valid), getResources().getString(R.string.app_name));

            } else {
                intent = new Intent(BMI.this, MakeCompareClg.class);
                startActivity(intent);
            }

        } else if (i == 6) {

            /* Forum */
            drawer_layout.closeDrawers();
            if (App.Utils.getString(Constants.user_id).equalsIgnoreCase("")) {
                App.Utils.ShowAlert(activity, getResources().getString(R.string.text_login_valid), getResources().getString(R.string.app_name));

            } else {
                intent = new Intent(BMI.this, Forum.class);
                startActivity(intent);
            }

        } else if (i == 7) {

            /* Feedback */
            drawer_layout.closeDrawers();
            if (App.Utils.getString(Constants.user_id).equalsIgnoreCase("")) {
                App.Utils.ShowAlert(activity, getResources().getString(R.string.text_login_valid), getResources().getString(R.string.app_name));

            } else {
                intent = new Intent(BMI.this, Feedback.class);
                startActivity(intent);
            }

        } else if (i == 8) {

            /* Logout */
            Dialog_Logout();

        } else if (i == 9) {

            /* Edit Profile */
            drawer_layout.closeDrawers();
            if (App.Utils.getString(Constants.user_id).equalsIgnoreCase("")) {
                App.Utils.ShowAlert(activity, getResources().getString(R.string.text_login_valid), getResources().getString(R.string.app_name));

            } else {
                intent = new Intent(BMI.this, Profile.class);
                startActivity(intent);
            }
        } else if (i == 10) {

            /* Share */
            drawer_layout.closeDrawers();
            String sharePlaystore = "https://www.google.co.in";
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share the Application");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sharePlaystore);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        }
    }

    private void Dialog_Logout() {
        metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        DialogLogout = new Dialog(activity);
        DialogLogout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogLogout.setCancelable(true);
        DialogLogout.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        DialogLogout.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        DialogLogout.setContentView(R.layout.dialog_logout);
        img_close = (ImageView) DialogLogout.findViewById(R.id.img_close);
        tv_yes = (TextView) DialogLogout.findViewById(R.id.tv_yes);
        tv_no = (TextView) DialogLogout.findViewById(R.id.tv_no);
        img_close.setOnClickListener(this);
        tv_yes.setOnClickListener(this);
        tv_no.setOnClickListener(this);

        DialogLogout.show();
        DialogLogout.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    private void CallLogoutApi() {
        if (App.Utils.IsInternetOn()) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Api.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(App.client)
                    .build();
            ApiInterface request = retrofit.create(ApiInterface.class);
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.user_id, App.Utils.getString(Constants.user_id));
            params.put(Constants.access_token, App.Utils.getString(Constants.access_token));
            params.put(Constants.device_token, App.Utils.GetDeviceID());
            params.put(Constants.lang, Constants.language);
            LogUtil.Print("logout_params", "" + params);

            Call<GsonLogout> call = request.getLogout(params);
            Progressbar.progressiveStart();
            Progressbar.setVisibility(View.VISIBLE);
            Progressbar.setIndeterminate(true);
            call.enqueue(new Callback<GsonLogout>() {
                @Override
                public void onResponse(Call<GsonLogout> call, Response<GsonLogout> response) {

                    Progressbar.progressiveStop();
                    Progressbar.setVisibility(View.GONE);

                    if (response.body().getFlag().equals(1)) {
                        String reg_id = App.Utils.getString(Constants.gcm_registration_id);
                        App.Utils.ClearAllPreferences();
                        App.Utils.putString(Constants.user_id, "");
                        drawer_layout.closeDrawers();
                        intent = new Intent(BMI.this, Login.class);
                        startActivity(intent);
                        App.Utils.putString(Constants.gcm_registration_id, reg_id);

                    } else {
                        App.Utils.ShowAlert(activity, "" + response.body().getMsg(), getResources().getString(R.string.app_name));
                    }
                }

                @Override
                public void onFailure(Call<GsonLogout> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });

        } else {
            MyUtils.ShowAlert(activity, getResources().getString(R.string.text_internet), getResources().getString(R.string.app_name));
        }
    }

    /*0  16.5   18.5   25   40*/
    private void displayBMI(float bmi) {
        String bmiLabel = "";

        if (Float.compare(bmi, 18.5f) <= 0) {

            bmiLabel = getString(R.string.text_underweight);
            tv_bmi_category.setText(getString(R.string.text_underweight));

            img_underweight.setVisibility(View.VISIBLE);
            img_normal.setVisibility(View.GONE);
            img_overweight.setVisibility(View.GONE);
            img_obese.setVisibility(View.GONE);

        } else if (Float.compare(bmi, 18.5f) >= 0 && Float.compare(bmi, 24.9f) <= 0) {

            bmiLabel = getString(R.string.text_normal);
            tv_bmi_category.setText(getString(R.string.text_normal));

            img_underweight.setVisibility(View.GONE);
            img_normal.setVisibility(View.VISIBLE);
            img_overweight.setVisibility(View.GONE);
            img_obese.setVisibility(View.GONE);

        } else if (Float.compare(bmi, 25.0f) >= 0 && Float.compare(bmi, 29.9f) <= 0) {

            bmiLabel = getString(R.string.text_overweight);
            tv_bmi_category.setText(getString(R.string.text_overweight));

            img_underweight.setVisibility(View.GONE);
            img_normal.setVisibility(View.GONE);
            img_overweight.setVisibility(View.VISIBLE);
            img_obese.setVisibility(View.GONE);

        } else if (Float.compare(bmi, 30.0f) >= 0) {

            bmiLabel = getString(R.string.text_obese);
            tv_bmi_category.setText(getString(R.string.text_obese));

            img_underweight.setVisibility(View.GONE);
            img_normal.setVisibility(View.GONE);
            img_overweight.setVisibility(View.GONE);
            img_obese.setVisibility(View.VISIBLE);

        } else {
            bmiLabel = getString(R.string.text_obese);
            tv_bmi_category.setText(getString(R.string.text_obese));

            img_underweight.setVisibility(View.GONE);
            img_normal.setVisibility(View.GONE);
            img_overweight.setVisibility(View.GONE);
            img_obese.setVisibility(View.VISIBLE);

        }

        bmiLabel = bmi + "\n\n" + bmiLabel;
        tv_bmi_value.setText(String.format("%.1f", bmi));
        LogUtil.Print("BMI==>", "" + bmiLabel);

    }
}
