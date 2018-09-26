package com.fittrack.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fittrack.App;
import com.fittrack.Constants.Api;
import com.fittrack.Constants.Constants;
import com.fittrack.Interface.ApiInterface;
import com.fittrack.Model.Profile.GsonChangePassword;
import com.fittrack.Model.Profile.GsonSaveProfile;
import com.fittrack.Model.Register.DataRegister;
import com.fittrack.Progressbar.SmoothProgressBar;
import com.fittrack.R;
import com.fittrack.Utility.Crop.CropActivity;
import com.fittrack.Utility.LogUtil;
import com.fittrack.Utility.MyUtils;
import com.fittrack.Utility.RoundedCornerImages;
import com.fittrack.activity.Home;
import com.fittrack.activity.Login;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.naver.android.helloyako.imagecrop.util.BitmapLoadUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Umesh on 8/17/2017.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {


    private String TAG = "Profile";
    private ImageView img_Profile, img_edit, img_camera, img_gallery;
    private EditText et_username, et_height, et_weight, et_mobile_number, et_old_password, et_new_password, et_confirm_password, et_email;
    private Spinner sp_gender, sp_activity;
    private RelativeLayout rl_gender, rl_activity;
    private TextView tv_edit_profile, tv_change_password, tv_birthdate;
    /*headr View*/
    private TextView tv_title;
    private ImageView img_drawer;

    ArrayList<String> gender_list = new ArrayList<>();
    ArrayList<String> activity_list = new ArrayList<>();
    ArrayList<String> activity_list_number = new ArrayList<>();

    String gender = "", activity_level = "", Image_path = "";
    private int mYear, mMonth, mDay;
    private File file, file_camera, file_output;
    private SmoothProgressBar Progressbar;
    /*permissions*/
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions = new String[]{
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    private ArrayAdapter<String> adapter_gender, adapter_activity;
    List<DataRegister> dataregister = new ArrayList<>();


    /*Dialog*/
    private DisplayMetrics metrics;
    private Dialog CustomDialog, ChangePaawordDialog;
    private ImageView img_cancel, img_close;
    private LinearLayout ll_done;
    private MultipartBody.Part fileToUpload;
    private File FilePath;
    private View view;
    private AdView mAdView;
    private LinearLayout ll_main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_profile, container, false);
        HeaderView(rootView);
        findViewById(rootView);
        OnClickListener();
        setSpinnerofGender();
        setProfileInfo();
        return rootView;
    }


    /**
     * set Profile Info
     */
    private void setProfileInfo() {
        et_username.setText(App.USER_DETAILS.getUsername());
        et_height.setText(App.USER_DETAILS.getHeight());
        et_weight.setText(App.USER_DETAILS.getWeight());
        tv_birthdate.setText(App.USER_DETAILS.getBirthdate());
        et_mobile_number.setText(App.USER_DETAILS.getMobile());
        et_height.setText(App.USER_DETAILS.getHeight());
        et_email.setText(App.USER_DETAILS.getEmail());
        gender = App.USER_DETAILS.getGender();
        activity_level = "" + App.USER_DETAILS.getActivityType();

        LogUtil.Print("Info", "gender==>" + gender + " activity==>" + activity_level);
        LogUtil.Print("emailInfo", "email==>" + App.USER_DETAILS.getEmail());

        Drawable drawable = getResources().getDrawable(R.drawable.user_placeholder_login_screen);
        int h = drawable.getIntrinsicHeight();
        int w = drawable.getIntrinsicWidth();

        if (!App.USER_DETAILS.getProfileImage().equalsIgnoreCase("")) {
            Picasso.with(getActivity()).load(App.USER_DETAILS.getProfileImage())
                    .resize(w, h)
                    .transform(new RoundedCornerImages())
                    .placeholder(R.drawable.user_placeholder_login_screen)
                    .error(R.drawable.user_placeholder_login_screen)
                    .into(img_Profile);
        } else {
            img_Profile.setImageResource(R.drawable.user_placeholder_login_screen);
        }

        for (int i = 0; i < gender_list.size(); i++) {

            if (App.USER_DETAILS.getGender().equalsIgnoreCase(gender_list.get(i).toString())) {

                sp_gender.setSelection(i);
                adapter_gender.notifyDataSetChanged();

            }
        }
        for (int i = 0; i < activity_list_number.size(); i++) {

            if (App.USER_DETAILS.getActivityType().equalsIgnoreCase(activity_list_number.get(i).toString())) {

                sp_activity.setSelection(i);
                adapter_activity.notifyDataSetChanged();
            }
        }
    }

    private void setSpinnerofGender() {

        gender_list.add(getString(R.string.text_gender));
        gender_list.add(getString(R.string.text_male));
        gender_list.add(getString(R.string.text_female));
        adapter_gender = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_dropdown_gender, R.id.tv_spinner_items, gender_list);
        sp_gender.setAdapter(adapter_gender);
        sp_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    gender = gender_list.get(i).toString();
                } else {
                    gender = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        activity_list_number.add("0");
        activity_list_number.add("1");
        activity_list_number.add("2");
        activity_list_number.add("3");
        activity_list_number.add("4");

        activity_list.add(getString(R.string.text_activity_level));
        activity_list.add(getString(R.string.text_normal));
        activity_list.add(getString(R.string.text_light));
        activity_list.add(getString(R.string.text_medium));
        activity_list.add(getString(R.string.text_active));

        adapter_activity = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_dropdown_gender, R.id.tv_spinner_items, activity_list);
        sp_activity.setAdapter(adapter_activity);
        sp_activity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    activity_level = "" + i;
                } else {
                    activity_level = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * Header view
     *
     * @param rootView
     */
    private void HeaderView(View rootView) {
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.text_Profile));
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
        ll_main = (LinearLayout) rootView.findViewById(R.id.ll_main);
        img_Profile = (ImageView) rootView.findViewById(R.id.img_Profile);
        img_edit = (ImageView) rootView.findViewById(R.id.img_edit);
        tv_edit_profile = (TextView) rootView.findViewById(R.id.tv_edit_profile);
        tv_change_password = (TextView) rootView.findViewById(R.id.tv_change_password);
        et_height = (EditText) rootView.findViewById(R.id.et_height);
        et_weight = (EditText) rootView.findViewById(R.id.et_weight);
        et_email = (EditText) rootView.findViewById(R.id.et_email);
        et_mobile_number = (EditText) rootView.findViewById(R.id.et_mobile_number);
        et_username = (EditText) rootView.findViewById(R.id.et_username);
        sp_gender = (Spinner) rootView.findViewById(R.id.sp_gender);
        tv_birthdate = (TextView) rootView.findViewById(R.id.tv_birthdate);
        sp_activity = (Spinner) rootView.findViewById(R.id.sp_activity);
        rl_gender = (RelativeLayout) rootView.findViewById(R.id.rl_gender);
        rl_activity = (RelativeLayout) rootView.findViewById(R.id.rl_activity);
        Progressbar = (SmoothProgressBar) rootView.findViewById(R.id.google_now);
        mAdView = (AdView) rootView.findViewById(R.id.adView);
        Home.getAds(mAdView);
        setListnerofAdsView();
    }

    /**
     * set Listner of adsview
     */
    private void setListnerofAdsView() {
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
//                Toast.makeText(getActivity(), "Ad is loaded!", Toast.LENGTH_SHORT).show();
                mAdView.setVisibility(View.VISIBLE);
                ll_main.setPadding(0, 0, 0, 50);

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
                ll_main.setPadding(0, 0, 0, 50);

            }
        });
    }

    /**
     * OnClickListener of Views
     */
    private void OnClickListener() {
        tv_edit_profile.setOnClickListener(this);
        tv_change_password.setOnClickListener(this);
        tv_birthdate.setOnClickListener(this);
        img_Profile.setOnClickListener(this);
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

            case R.id.tv_change_password:
                ChangePasswordDialog();
                break;

            case R.id.img_close:
                CustomDialog.dismiss();
                break;

            case R.id.img_cancel:
                ChangePaawordDialog.dismiss();
                break;

            case R.id.tv_edit_profile:

                if (et_username.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_username_empty), getResources().getString(R.string.app_name));

                } else if (sp_gender.getSelectedItemPosition() == 0) {

                    App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_gender_empty), getResources().getString(R.string.app_name));

                } else if (et_height.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_height_empty), getResources().getString(R.string.app_name));

                } else if (et_weight.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_weight_empty), getResources().getString(R.string.app_name));

                } else if (tv_birthdate.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_birthdate_empty), getResources().getString(R.string.app_name));

                } else if (et_email.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_email_empty), getResources().getString(R.string.app_name));

                } else if (!App.Utils.IsValidEmail(et_email.getText().toString())) {

                    App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_valid_email_empty), getResources().getString(R.string.app_name));

                } else if (et_mobile_number.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_mobile_number_empty), getResources().getString(R.string.app_name));

                } else if (sp_activity.getSelectedItemPosition() == 0) {

                    App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_activity_level_empty), getResources().getString(R.string.app_name));

                } else {

                    view = getActivity().getCurrentFocus();
                    if (view != null) {

                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    CallEditProfileAPI();

                }

                break;

            case R.id.img_Profile:
                Gallery_Parmission();
                break;

            case R.id.ll_done:
                if (et_old_password.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_old_password_empty), getResources().getString(R.string.app_name));

                } else if (et_new_password.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_new_password_empty), getResources().getString(R.string.app_name));

                } else if (et_confirm_password.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_confirm_password_empty), getResources().getString(R.string.app_name));

                } else if (!et_new_password.getText().toString().equals(et_confirm_password.getText().toString())) {

                    App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_password_not_match), getResources().getString(R.string.app_name));

                } else if (et_new_password.getText().length() < 6) {

                    App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_password_limit), getResources().getString(R.string.app_name));

                } else if (et_confirm_password.getText().length() < 6) {

                    App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_password_limit), getResources().getString(R.string.app_name));

                } else {

                    ChangePaawordDialog.dismiss();

                    view = getActivity().getCurrentFocus();
                    if (view != null) {

                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    CallChangePasswordApi();

                }
                break;

            case R.id.img_gallery:
                Intent i1 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i1, Constants.PHOTO_GALLERY);
                CustomDialog.dismiss();
                break;

            case R.id.img_camera:
                Intent i2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file_camera = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                file_output = new File(file_camera, "CameraContentDemo.jpeg");
                i2.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() +
                        ".provider", file_output));
//                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
                startActivityForResult(i2, Constants.PHOTO_CAMERA);
                CustomDialog.dismiss();
                break;

            case R.id.img_share:
                Bitmap bitmap = takeScreenshot();
                SaveBitmap(bitmap);
                ShareChart();
                break;

            case R.id.tv_birthdate:

                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                tv_birthdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

                break;
        }
    }

    /**
     * TakeScreenshot Programatically
     */
    public Bitmap takeScreenshot() {
//        View view = findViewById(R.id.ll_chart);//your layout id
        View view = getActivity().getWindow().getDecorView().getRootView(); // this line is for full sceen sceenshot
        view.getRootView();
        view.setDrawingCacheEnabled(true);
        return view.getDrawingCache();
    }

    /**
     * SaveBitmap in gallery
     */
    private void SaveBitmap(Bitmap bitmap) {
        /**
         * File imagePath in which image is saved.
         */
        FilePath = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(FilePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("Log", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("Log", e.getMessage(), e);
        }
    }

    /**
     * Share your Chart
     */
    private void ShareChart() {
        Uri uri = Uri.fromFile(FilePath);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        String shareBody = "Share your Chart with your Friends...";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Chart");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void CallEditProfileAPI() {

        if (App.Utils.IsInternetOn()) {

            ApiInterface request = Api.retrofit.create(ApiInterface.class);
//            if (file != null) {
//                RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file);
//                fileToUpload = MultipartBody.Part.createFormData(Constants.profile_image, file.getName(), requestBody1);
//            }
            Map<String, RequestBody> params = new HashMap<String, RequestBody>();
            params.put(Constants.username, RequestBody.create(MediaType.parse("multipart/form-data"), et_username.getText().toString()));
            params.put(Constants.gender, RequestBody.create(MediaType.parse("multipart/form-data"), gender));
            params.put(Constants.height, RequestBody.create(MediaType.parse("multipart/form-data"), et_height.getText().toString()));
            params.put(Constants.weight, RequestBody.create(MediaType.parse("multipart/form-data"), et_weight.getText().toString()));
            params.put(Constants.birthdate, RequestBody.create(MediaType.parse("multipart/form-data"), tv_birthdate.getText().toString()));
            params.put(Constants.email, RequestBody.create(MediaType.parse("multipart/form-data"), et_email.getText().toString()));
            params.put(Constants.mobile, RequestBody.create(MediaType.parse("multipart/form-data"), et_mobile_number.getText().toString()));
            params.put(Constants.activity_type, RequestBody.create(MediaType.parse("multipart/form-data"), activity_level));
            params.put(Constants.user_id, RequestBody.create(MediaType.parse("multipart/form-data"), App.Utils.getString(Constants.user_id)));
            params.put(Constants.access_token, RequestBody.create(MediaType.parse("multipart/form-data"), App.Utils.getString(Constants.access_token)));
            params.put(Constants.lang, RequestBody.create(MediaType.parse("multipart/form-data"), Constants.language));
            LogUtil.Print("save_profile_params", "" + params + "user_id==>" + App.USER_DETAILS.getUserId());
            if (file != null) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                String key = String.format("%1$s\"; filename=\"photo_" + String.valueOf(1) + ".png", Constants.profile_image);
                params.put(key, requestBody);
            }
            Call<GsonSaveProfile> call = request.getSaveProfile(params, fileToUpload);
            Progressbar.progressiveStart();
            Progressbar.setVisibility(View.VISIBLE);
            Progressbar.setIndeterminate(true);
            call.enqueue(new Callback<GsonSaveProfile>() {
                @Override
                public void onResponse(Call<GsonSaveProfile> call, Response<GsonSaveProfile> response) {

                    Progressbar.progressiveStop();
                    Progressbar.setVisibility(View.GONE);
                    GsonSaveProfile gson = response.body();

                    if (gson.getFlag().equals(1)) {

                        dataregister.add(gson.getData());
//                        App.Utils.ShowAlert(getActivity(), "" + gson.getMsg(), getResources().getString(R.string.app_name));
                        App.Utils.putString(Constants.user_id, "" + gson.getData().getUserId());
                        App.Utils.putString(Constants.access_token, "" + gson.getData().getAccessToken());
                        App.Utils.putString(Constants.user_gson, new Gson().toJson(gson.getData()));
                        App.Utils.putString(Constants.gender, "" + gson.getData().getGender());

                        App.USER_DETAILS = new GsonBuilder().create().fromJson(App.Utils.getString(Constants.user_gson),
                                DataRegister.class);

                        Intent i = new Intent(getActivity(), Home.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    } else {
                        App.Utils.ShowAlert(getActivity(), "" + gson.getMsg(), getResources().getString(R.string.app_name));
                    }
                }

                @Override
                public void onFailure(Call<GsonSaveProfile> call, Throwable t) {
                    Log.d("Error", "error" + t.getMessage());
                }
            });
        } else {
            MyUtils.ShowAlert(getActivity(), getResources().getString(R.string.text_internet), getResources().getString(R.string.app_name));
        }


//        if (App.Utils.IsInternetOn()) {
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(Api.BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(App.client)
//                    .build();
//
//            ApiInterface request = retrofit.create(ApiInterface.class);
//
//            Map<String, RequestBody> params = new HashMap<String, RequestBody>();
//            params.put(Constants.username, RequestBody.create(MediaType.parse("multipart/form-data"), et_username.getText().toString()));
//            params.put(Constants.gender, RequestBody.create(MediaType.parse("multipart/form-data"), gender));
//            params.put(Constants.height, RequestBody.create(MediaType.parse("multipart/form-data"), et_height.getText().toString()));
//            params.put(Constants.weight, RequestBody.create(MediaType.parse("multipart/form-data"), et_weight.getText().toString()));
//            params.put(Constants.birthdate, RequestBody.create(MediaType.parse("multipart/form-data"), tv_birthdate.getText().toString()));
//            params.put(Constants.email, RequestBody.create(MediaType.parse("multipart/form-data"), et_email.getText().toString()));
//            params.put(Constants.mobile, RequestBody.create(MediaType.parse("multipart/form-data"), et_mobile_number.getText().toString()));
//            params.put(Constants.activity_type, RequestBody.create(MediaType.parse("multipart/form-data"), activity_level));
//            params.put(Constants.user_id, RequestBody.create(MediaType.parse("multipart/form-data"), App.USER_DETAILS.getUserId()));
//            params.put(Constants.access_token, RequestBody.create(MediaType.parse("multipart/form-data"), App.USER_DETAILS.getAccessToken()));
//            params.put(Constants.lang, RequestBody.create(MediaType.parse("multipart/form-data"), Constants.language));
//            if(file!=null) {
//                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                String key = String.format("%1$s\"; filename=\"photo_" + String.valueOf(1) + ".png", Constants.profile_image);
//                params.put(key, requestBody);
//            }
//            LogUtil.Print("save_profile_params", "" + params);
//
//            Call<GsonSaveProfile> call = request.getSaveProfile(params);
//           // Progressbar.progressiveStart();
//           // Progressbar.setVisibility(View.VISIBLE);
//            //Progressbar.setIndeterminate(true);
//
//           /* try {
//                GsonSaveProfile response=call.execute().body();
//                if (response.getFlag().equals(1)) {
//                    App.Utils.ShowAlert(activity, "" + response.getMsg(), getResources().getString(R.string.app_name));
//
//
//                } else {
//                    App.Utils.ShowAlert(activity, "" + response.getMsg(), getResources().getString(R.string.app_name));
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }*/
//            call.enqueue(new Callback<GsonSaveProfile>() {
//                @Override
//                public void onResponse(Call<GsonSaveProfile> call, Response<GsonSaveProfile> response) {
//
//                    Progressbar.progressiveStop();
//                    Progressbar.setVisibility(View.GONE);
//
//                    if (response.body().getFlag().equals(1)) {
//                        App.Utils.ShowAlert(activity, "" + response.body().getMsg(), getResources().getString(R.string.app_name));
//
//
//                    } else {
//                        App.Utils.ShowAlert(activity, "" + response.body().getMsg(), getResources().getString(R.string.app_name));
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<GsonSaveProfile> call, Throwable t) {
//                    Log.d("Error", "error" + t.getMessage());
//                }
//            });
//        } else {
//            MyUtils.ShowAlert(activity, getResources().getString(R.string.text_internet), getResources().getString(R.string.app_name));
//        }
    }

    /**
     * ChangePassword Api
     */
    private void CallChangePasswordApi() {
        if (App.Utils.IsInternetOn()) {

            ApiInterface request = Api.retrofit.create(ApiInterface.class);
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.current_password, et_old_password.getText().toString());
            params.put(Constants.new_password, et_confirm_password.getText().toString());
            params.put(Constants.user_id, App.Utils.getString(Constants.user_id));
            params.put(Constants.access_token, App.Utils.getString(Constants.access_token));
            params.put(Constants.lang, Constants.language);
            LogUtil.Print("add_weightsheet_params", "" + params);

            Call<GsonChangePassword> call = request.getChangePasswordList(params);
            Progressbar.progressiveStart();
            Progressbar.setVisibility(View.VISIBLE);
            Progressbar.setIndeterminate(true);
            call.enqueue(new Callback<GsonChangePassword>() {
                @Override
                public void onResponse(Call<GsonChangePassword> call, Response<GsonChangePassword> response) {

                    Progressbar.progressiveStop();
                    Progressbar.setVisibility(View.GONE);
                    GsonChangePassword gson = response.body();

                    if (gson.getFlag().equals(1)) {
                        App.Utils.ShowAlert(getActivity(), "" + gson.getMsg(), getResources().getString(R.string.app_name));
                        Intent i = new Intent(getActivity(), Login.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

                    } else {
                        App.Utils.ShowAlert(getActivity(), "" + gson.getMsg(), getResources().getString(R.string.app_name));
                    }
                }

                @Override
                public void onFailure(Call<GsonChangePassword> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });

        } else {
            MyUtils.ShowAlert(getActivity(), getResources().getString(R.string.text_internet), getResources().getString(R.string.app_name));
        }
    }

    /**
     * AddWeightSheet Dailog
     */
    private void ChangePasswordDialog() {
        metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        ChangePaawordDialog = new Dialog(getActivity());
        ChangePaawordDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ChangePaawordDialog.setCancelable(true);
        ChangePaawordDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ChangePaawordDialog.setContentView(R.layout.change_password);
        et_old_password = (EditText) ChangePaawordDialog.findViewById(R.id.et_old_password);
        et_new_password = (EditText) ChangePaawordDialog.findViewById(R.id.et_new_password);
        et_confirm_password = (EditText) ChangePaawordDialog.findViewById(R.id.et_confirm_password);

        ll_done = (LinearLayout) ChangePaawordDialog.findViewById(R.id.ll_done);
        img_cancel = (ImageView) ChangePaawordDialog.findViewById(R.id.img_cancel);
        ll_done.setOnClickListener(this);
        img_cancel.setOnClickListener(this);
        ChangePaawordDialog.show();
        ChangePaawordDialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        ChangePaawordDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_dialog_rounded));

    }

    /**
     * set runtime permission of gallery and camera
     */
    private void Gallery_Parmission() {

        if (checkPermissions()) {
            Dialog_Camera_Gallery();
        }
    }

    /**
     * checkPermissions of gallery and camera
     */
    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getActivity(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    img_Profile.performClick();
                    // permissions granted.
                } else {
                    // permissions not granted.
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(false);
                    builder.setTitle(getResources().getString(R.string.app_name));
                    builder.setMessage(getResources().getString(R.string.text_save_profile_picture_msg));
                    builder.setNegativeButton(getResources().getString(R.string.text_skip), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton(getResources().getString(R.string.text_continue), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Gallery_Parmission();
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
                    dialog.show();
                }
                return;
            }
        }
    }

    /**
     * open Dialog of gallery and camera
     */
    private void Dialog_Camera_Gallery() {

        metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        CustomDialog = new Dialog(getActivity());
        CustomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        CustomDialog.setCancelable(true);
        CustomDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        CustomDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        CustomDialog.setContentView(R.layout.dialog_camera_gallery);

        TextView tv_title = (TextView) CustomDialog.findViewById(R.id.tv_title);
        img_camera = (ImageView) CustomDialog.findViewById(R.id.img_camera);
        img_gallery = (ImageView) CustomDialog.findViewById(R.id.img_gallery);
        img_close = (ImageView) CustomDialog.findViewById(R.id.img_close);
        img_close.setOnClickListener(this);
        img_camera.setOnClickListener(this);
        img_gallery.setOnClickListener(this);
        CustomDialog.show();
        CustomDialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("requestCode", "" + requestCode);

        if (requestCode == Constants.PHOTO_GALLERY) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String filePath = BitmapLoadUtils.getPathFromUri(getActivity(), uri);
                Uri filePathUri = Uri.parse(filePath);

                if (filePathUri != null) {
                    Image_path = filePathUri.toString();
                    Log.e("imagepath_gallery", filePathUri.toString());

                    beginCrop(filePathUri, Constants.RESULT_CROP_GALLERY);
                } else {
                    MyUtils.ShowAlert(getActivity(), getResources().getString(R.string.File_not_Found),
                            getResources().getString(R.string.app_name));
                }
            }

        } else if (requestCode == Constants.RESULT_CROP_GALLERY) {

            if (resultCode == RESULT_OK) {
                if (Image_path != null) {
                    Image_path = data.getStringExtra(Constants.image_register);
                    Log.e("crop_imagepath_gallery", Image_path);
                    Bitmap bitmapImage = BitmapFactory.decodeFile(Image_path);
                    BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.user_placeholder_login_screen);
                    Glide.with(getActivity()).load(Image_path)
                            .error(R.drawable.user_placeholder_login_screen)
                            .into(img_Profile);
                    file = new File(Image_path);

                } else {
                    MyUtils.ShowAlert(getActivity(), getResources().getString(R.string.File_not_Found),
                            getResources().getString(R.string.app_name));
                }
            }
        }
        if (requestCode == Constants.PHOTO_CAMERA) {
            if (resultCode == RESULT_OK) {

                Uri tempUri = Uri.fromFile(file_output);
                Image_path = file_camera.getAbsolutePath();
                LogUtil.Print("imagepath_camera", Image_path);

                if (tempUri != null) {
                    beginCrop(tempUri, Constants.RESULT_CROP_CAMERA);
                } else {
                    MyUtils.ShowAlert(getActivity(), getResources().getString(R.string.File_not_Found),
                            getResources().getString(R.string.app_name));
                }
            }

        } else if (requestCode == Constants.RESULT_CROP_CAMERA
                && resultCode == RESULT_OK) {
            try {
                if (Image_path != null) {
                    Image_path = data.getStringExtra(Constants.image_register);
                    Log.e("crop_imagepath_camera", Image_path);

                    Glide.with(getActivity()).load(Image_path)
                            .error(R.drawable.user_placeholder_login_screen)
                            .into(img_Profile);
                    Bitmap bitmapImage = BitmapFactory.decodeFile(Image_path);
                    file = new File(Image_path);
                } else {
                    MyUtils.ShowAlert(getActivity(), getResources().getString(R.string.File_not_Found),
                            getResources().getString(R.string.app_name));
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                MyUtils.ShowAlert(getActivity(), getResources().getString(R.string.File_not_Found), getResources().getString(R.string.app_name));
            }

        }
    }

    /**
     * for beginCrop
     *
     * @param uri
     * @param resultCrop
     */
    private void beginCrop(Uri uri, int resultCrop) {

        File file = null;
        file = new File(Constants.App_ROOT_FOLDER);
        file.mkdirs();

        file = new File(Constants.App_ROOT_FOLDER + "/" + Constants.IMAGE_FILE_NAME_PREFIX.replace("X", MyUtils.GetCurrentNanoTime()));

        System.out.println("file::" + file);
        Uri destination = Uri.fromFile(file);
        // Crop.of(source, destination).asSquare().start(getActivity());
        startCrop(uri, resultCrop);

    }

    /**
     * for startCrop
     *
     * @param imageUri
     * @param resultCrop
     */
    private void startCrop(Uri imageUri, int resultCrop) {

        LogUtil.Print(TAG, "imageUri..." + imageUri);
        Intent intent = new Intent(getActivity(), CropActivity.class);
        intent.setData(imageUri);
        intent.putExtra(Constants.FLAG_IS_SQUARE, false);
        intent.putExtra(Constants.IMAGE_CROP_RATIO_WIDTH, Constants.DEFAULT_REGISTER_PICTURE_CROP_RATIO_WIDTH);
        intent.putExtra(Constants.IMAGE_CROP_RATIO_HEIGHT, Constants.DEFAULT_REGISTER_PICTURE_CROP_RATIO_HEIGHT);
        startActivityForResult(intent, resultCrop);

    }
}

