package com.fittrack.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.fittrack.activity.Home;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

/**
 * Created by Umesh on 8/17/2017.
 */
public class BMIFragment extends Fragment implements View.OnClickListener {

    /*header View*/
    private TextView tv_title;
    private ImageView img_drawer;
    ArrayList<String> weight_unit_list = new ArrayList<>();
    private EditText et_age, et_weight, et_height;
    private TextView tv_reset, tv_calculate, tv_username, tv_current_bmi, tv_current_bmr, tv_weight_sheet, tv_photo_gallery, tv_chart, tv_make_compare, tv_forum,
            tv_feedback, tv_bmi_value, tv_bmi_category, tv_login, tv_share, tv_slogan;
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
    private View view;
    private AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_bmi, container, false);
        HeaderView(rootView);
        findViewById(rootView);
        OnClickListener();

        setData();

        return rootView;
    }

    private void setData() {
        if (!App.Utils.getString(Constants.gender).equalsIgnoreCase("")) {

            et_weight.setText(App.USER_DETAILS.getWeight());
            et_height.setText(App.USER_DETAILS.getHeight());

            if (Integer.parseInt(et_height.getText().toString()) >= 0 || Integer.parseInt(et_weight.getText().toString()) >= 0) {
                CalculateBMI();
            }

        }
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
//        sp_weight_unit = (Spinner) findViewById(R.id.sp_weight_unit);
        img_underweight = (ImageView) rootView.findViewById(R.id.img_underweight);
        img_normal = (ImageView) rootView.findViewById(R.id.img_normal);
        img_overweight = (ImageView) rootView.findViewById(R.id.img_overweight);
        img_obese = (ImageView) rootView.findViewById(R.id.img_obese);
        tv_bmi_value = (TextView) rootView.findViewById(R.id.tv_bmi_value);
        tv_bmi_category = (TextView) rootView.findViewById(R.id.tv_bmi_category);
        ll_check = (LinearLayout) rootView.findViewById(R.id.ll_check);
        tv_slogan = (TextView) rootView.findViewById(R.id.tv_slogan);
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

    /**
     * OnClickListener of Views
     */
    private void OnClickListener() {

        tv_reset.setOnClickListener(this);
        tv_calculate.setOnClickListener(this);
        ll_check.setOnClickListener(this);
    }

    /**
     * Header view
     *
     * @param rootView
     */
    private void HeaderView(View rootView) {
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.text_bmi));
        img_drawer = (ImageView) rootView.findViewById(R.id.img_drawer);
        img_drawer.setVisibility(View.VISIBLE);
        img_drawer.setOnClickListener(this);
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
                img_underweight.setVisibility(View.GONE);
                img_normal.setVisibility(View.GONE);
                img_overweight.setVisibility(View.GONE);
                img_obese.setVisibility(View.GONE);
                tv_bmi_category.setText("");
                tv_slogan.setText("");
                tv_bmi_value.setText("");
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
        }
    }

    /**
     * Calculate BMI
     */
    private void CalculateBMI() {
        if (et_weight.getText().toString().trim().isEmpty()) {

            App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_height_empty), getResources().getString(R.string.app_name));

        } else if (et_height.getText().toString().trim().isEmpty()) {

            App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_weight_empty), getResources().getString(R.string.app_name));

        } else {
            view = getActivity().getCurrentFocus();

            if (view != null) {

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
        CustomDialog = new Dialog(getActivity());
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

    /*0  16.5   18.5   25   40*/
    private void displayBMI(float bmi) {
        String bmiLabel = "";

        if (Float.compare(bmi, 18.5f) <= 0) {

            bmiLabel = getString(R.string.text_underweight);
            tv_bmi_category.setText(getString(R.string.text_underweight));
            tv_slogan.setText(getResources().getString(R.string.text_bmi_underweight));

            img_underweight.setVisibility(View.VISIBLE);
            img_normal.setVisibility(View.GONE);
            img_overweight.setVisibility(View.GONE);
            img_obese.setVisibility(View.GONE);

        } else if (Float.compare(bmi, 18.5f) >= 0 && Float.compare(bmi, 24.9f) <= 0) {

            bmiLabel = getString(R.string.text_normal);
            tv_bmi_category.setText(getString(R.string.text_normal));
            tv_slogan.setText(getResources().getString(R.string.text_bmi_normal));
            img_underweight.setVisibility(View.GONE);
            img_normal.setVisibility(View.VISIBLE);
            img_overweight.setVisibility(View.GONE);
            img_obese.setVisibility(View.GONE);

        } else if (Float.compare(bmi, 25.0f) >= 0 && Float.compare(bmi, 29.9f) <= 0) {

            bmiLabel = getString(R.string.text_overweight);
            tv_bmi_category.setText(getString(R.string.text_overweight));
            tv_slogan.setText(getResources().getString(R.string.text_bmi_overweight));

            img_underweight.setVisibility(View.GONE);
            img_normal.setVisibility(View.GONE);
            img_overweight.setVisibility(View.VISIBLE);
            img_obese.setVisibility(View.GONE);

        } else if (Float.compare(bmi, 30.0f) >= 0) {

            bmiLabel = getString(R.string.text_obese);
            tv_bmi_category.setText(getString(R.string.text_obese));
            tv_slogan.setText(getResources().getString(R.string.text_bmi_obuse));

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

    /**
     * Called when leaving the activity
     */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /**
     * Called before the activity is destroyed
     */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
