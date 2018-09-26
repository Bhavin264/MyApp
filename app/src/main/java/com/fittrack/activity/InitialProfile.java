package com.fittrack.activity;

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
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.fittrack.Model.Register.DataRegister;
import com.fittrack.Model.Register.GsonRegister;
import com.fittrack.Progressbar.SmoothProgressBar;
import com.fittrack.R;
import com.fittrack.Utility.Crop.CropActivity;
import com.fittrack.Utility.LogUtil;
import com.fittrack.Utility.MyUtils;
import com.fittrack.Utility.RoundedCornerImages;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.naver.android.helloyako.imagecrop.util.BitmapLoadUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
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

public class InitialProfile extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "InitialProfile";
    private InitialProfile activity;
    private LinearLayout ll_login;
    private ImageView img_Profile, img_edit, img_camera, img_gallery, img_close;
    private EditText et_username, et_height, et_weight, et_mobile_number, et_password, et_confirm_password, et_email;
    private Spinner sp_gender, sp_activity;
    private RelativeLayout rl_gender, rl_activity;
    private TextView tv_register, tv_login, tv_birthdate;
    /*headr View*/
    private TextView tv_title;
    ArrayList<String> gender_list = new ArrayList<>();
    ArrayList<String> activity_list = new ArrayList<>();
    String activity_level = "", Image_path = "", Image_path1 = "";
    private int mYear, mMonth, mDay;
    private File file, file_camera, file_output;
    private SmoothProgressBar Progressbar;
    List<DataRegister> dataregister = new ArrayList<>();
    /*permissions*/
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions = new String[]{
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    /*Dialog*/
    private DisplayMetrics metrics;
    private Dialog CustomDialog;
    private MultipartBody.Part fileToUpload;
    String name = "", gender = "", id = "", email = "", gender_intent;
    private ArrayAdapter<String> adapter_gender;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_profile);


        activity = InitialProfile.this;
        HeaderView();
        findViewById();
        OnClickListener();
        setSpinnerofGender();

        getIntentData();
        setData();
    }


    /**
     * set Data Initially
     */
    private void setData() {

        et_username.setText(name);
        et_email.setText(email);
        for (int i = 0; i < gender_list.size(); i++) {

            if (gender_intent.equalsIgnoreCase(gender_list.get(i).toString())) {
                sp_gender.setSelection(i);
                adapter_gender.notifyDataSetChanged();
            }
        }


        Drawable drawable = getResources().getDrawable(R.drawable.user_placeholder_login_screen);
        int h = drawable.getIntrinsicHeight();
        int w = drawable.getIntrinsicWidth();
        Image_path1 = App.Utils.getString(Constants.profile_image);


        LogUtil.Print("image", Image_path);

        if (!App.Utils.getString(Constants.profile_image).equalsIgnoreCase("")) {
            Picasso.with(activity).load(App.Utils.getString(Constants.profile_image))
                    .resize(w, h)
                    .transform(new RoundedCornerImages())
                    .placeholder(R.drawable.user_placeholder_login_screen)
                    .error(R.drawable.user_placeholder_login_screen)
                    .into(img_Profile);
        } else {
            img_Profile.setImageResource(R.drawable.user_placeholder_login_screen);
        }

    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            name = intent.getStringExtra(Constants.name);
            gender_intent = intent.getStringExtra(Constants.gender);
            id = intent.getStringExtra(Constants.id);
            email = intent.getStringExtra(Constants.email);
            LogUtil.Print("data", "name==>" + name + "gender==>" + gender_intent + "id==>" + "" + id);
        }
    }

    private void setSpinnerofGender() {

        gender_list.add(getString(R.string.text_gender));
        gender_list.add(getString(R.string.text_male));
        gender_list.add(getString(R.string.text_female));
        adapter_gender = new ArrayAdapter<String>(activity,
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
        activity_list.add(getString(R.string.text_activity_level));
        activity_list.add(getString(R.string.text_normal));
        activity_list.add(getString(R.string.text_light));
        activity_list.add(getString(R.string.text_medium));
        activity_list.add(getString(R.string.text_active));

        final ArrayAdapter<String> adapter_activity = new ArrayAdapter<String>(activity,
                R.layout.spinner_dropdown_gender, R.id.tv_spinner_items, activity_list);
        sp_activity.setAdapter(adapter_activity);
        sp_activity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                activity_level = "" + i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    /**
     * Header view
     */
    private void HeaderView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.text_register));
    }

    /**
     * findViews by Ids
     */
    private void findViewById() {
        img_Profile = (ImageView) findViewById(R.id.img_Profile);
        img_edit = (ImageView) findViewById(R.id.img_edit);
        tv_register = (TextView) findViewById(R.id.tv_register);
        et_height = (EditText) findViewById(R.id.et_height);
        et_weight = (EditText) findViewById(R.id.et_weight);
        et_mobile_number = (EditText) findViewById(R.id.et_mobile_number);
        et_email = (EditText) findViewById(R.id.et_email);
        et_username = (EditText) findViewById(R.id.et_username);
        sp_gender = (Spinner) findViewById(R.id.sp_gender);
        tv_birthdate = (TextView) findViewById(R.id.tv_birthdate);
        sp_activity = (Spinner) findViewById(R.id.sp_activity);
        rl_gender = (RelativeLayout) findViewById(R.id.rl_gender);
        rl_activity = (RelativeLayout) findViewById(R.id.rl_activity);
        Progressbar = (SmoothProgressBar) findViewById(R.id.google_now);

        mAdView = (AdView) findViewById(R.id.adView);
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
        tv_register.setOnClickListener(this);
        tv_birthdate.setOnClickListener(this);
        img_Profile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_Profile:
                Gallery_Parmission();
                break;

            case R.id.tv_birthdate:

                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
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

            case R.id.img_cancel:
                CustomDialog.dismiss();
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
                i2.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(activity, getApplicationContext().getPackageName() +
                        ".provider", file_output));
//                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
                startActivityForResult(i2, Constants.PHOTO_CAMERA);
                CustomDialog.dismiss();
                break;

            case R.id.tv_register:

                if (et_username.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_username_empty), getResources().getString(R.string.app_name));

                } else if (sp_gender.getSelectedItemPosition() == 0) {

                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_gender_empty), getResources().getString(R.string.app_name));

                } else if (et_height.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_height_empty), getResources().getString(R.string.app_name));

                } else if (et_weight.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_weight_empty), getResources().getString(R.string.app_name));

                } else if (tv_birthdate.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_birthdate_empty), getResources().getString(R.string.app_name));

                } else if (et_email.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_email_empty), getResources().getString(R.string.app_name));

                } else if (!App.Utils.IsValidEmail(et_email.getText().toString())) {

                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_valid_email_empty), getResources().getString(R.string.app_name));

                } else if (et_mobile_number.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_mobile_number_empty), getResources().getString(R.string.app_name));

                } else if (sp_activity.getSelectedItemPosition() == 0) {

                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_activity_level_empty), getResources().getString(R.string.app_name));

                } else {

                    View view = getCurrentFocus();

                    if (view != null) {

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    CallSocialFbLoginAPI();

                }
                break;

        }

    }

    /**
     * Call SocialFbLogin API
     */
    private void CallSocialFbLoginAPI() {

        if (App.Utils.IsInternetOn()) {

            ApiInterface request = Api.retrofit.create(ApiInterface.class);
            Map<String, RequestBody> params = new HashMap<String, RequestBody>();
            params.put(Constants.username, RequestBody.create(MediaType.parse("multipart/form-data"), et_username.getText().toString()));
            params.put(Constants.gender, RequestBody.create(MediaType.parse("multipart/form-data"), gender));
            params.put(Constants.height, RequestBody.create(MediaType.parse("multipart/form-data"), et_height.getText().toString()));
            params.put(Constants.weight, RequestBody.create(MediaType.parse("multipart/form-data"), et_weight.getText().toString()));
            params.put(Constants.birthdate, RequestBody.create(MediaType.parse("multipart/form-data"), tv_birthdate.getText().toString()));
            params.put(Constants.email, RequestBody.create(MediaType.parse("multipart/form-data"), et_email.getText().toString()));
            params.put(Constants.mobile, RequestBody.create(MediaType.parse("multipart/form-data"), et_mobile_number.getText().toString()));
            params.put(Constants.activity_type, RequestBody.create(MediaType.parse("multipart/form-data"), activity_level));
            params.put(Constants.lang, RequestBody.create(MediaType.parse("multipart/form-data"), Constants.language));
            params.put(Constants.device_token, RequestBody.create(MediaType.parse("multipart/form-data"), App.Utils.GetDeviceID()));
            params.put(Constants.register_id, RequestBody.create(MediaType.parse("multipart/form-data"), App.Utils.getString(Constants.gcm_registration_id)));
            params.put(Constants.device_type, RequestBody.create(MediaType.parse("multipart/form-data"), Constants.language));
            params.put(Constants.fb_access_token, RequestBody.create(MediaType.parse("multipart/form-data"), id));
            params.put(Constants.login_type, RequestBody.create(MediaType.parse("multipart/form-data"), Constants.login_type_1));

            if (file != null) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                String key = String.format("%1$s\"; filename=\"photo_" + String.valueOf(1) + ".png", Constants.profile_image);
                params.put(key, requestBody);
            } else {
                params.put(Constants.profile_image, RequestBody.create(MediaType.parse("multipart/form-data"), Image_path1));
            }
            LogUtil.Print("register_params", "" + params);

            Call<GsonRegister> call = request.getLoginWithFacebook(params, fileToUpload);
            Progressbar.progressiveStart();
            Progressbar.setVisibility(View.VISIBLE);
            Progressbar.setIndeterminate(true);
            call.enqueue(new Callback<GsonRegister>() {
                @Override
                public void onResponse(Call<GsonRegister> call, Response<GsonRegister> response) {
                    Log.d("response", response.toString());

                    Progressbar.progressiveStop();
                    Progressbar.setVisibility(View.GONE);

                    GsonRegister gson = response.body();
                    if (gson.getFlag().equals(1)) {

                        App.Utils.putString(Constants.user_id, "" + gson.getData().getUserId());
                        App.Utils.putString(Constants.access_token, "" + gson.getData().getAccessToken());
                        App.Utils.putString(Constants.gender, "" + gson.getData().getGender());
                        App.Utils.putString(Constants.user_gson, new Gson().toJson(gson.getData()));

                        App.USER_DETAILS = new GsonBuilder().create().fromJson(App.Utils.getString(Constants.user_gson),
                                DataRegister.class);

                        Intent i = new Intent(activity, Home.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

                    } else {
                        App.Utils.ShowAlert(activity, "" + gson.getMsg(), getResources().getString(R.string.app_name));
                    }
                }

                @Override
                public void onFailure(Call<GsonRegister> call, Throwable t) {
                    Log.d("Error", "error" + t.getMessage());
                }
            });

        } else {
            MyUtils.ShowAlert(activity, getResources().getString(R.string.text_internet), getResources().getString(R.string.app_name));
        }


//        if (App.Utils.IsInternetOn()) {
//
//            ApiInterface request = Api.retrofit.create(ApiInterface.class);
//            Map<String, String> params = new HashMap<String, String>();
//            params.put(Constants.username, name);
//            params.put(Constants.gender, gender);
//            params.put(Constants.birthdate, "");
//            params.put(Constants.email, email);
//            params.put(Constants.mobile, "");
//            params.put(Constants.profile_image, profilePic);
//            params.put(Constants.lang, Constants.language);
//            params.put(Constants.fb_access_token, id);
//            params.put(Constants.device_token, App.Utils.GetDeviceID());
//            params.put(Constants.register_id, App.Utils.getString(Constants.gcm_registration_id));
//            params.put(Constants.device_type, Constants.language);
//            params.put(Constants.login_type, Constants.login_type_1);
//
//            LogUtil.Print("social_login_params", "" + params);
//
//            Call<GsonRegister> call = request.getFacebookLogin(params);
//            Progressbar.progressiveStart();
//            Progressbar.setVisibility(View.VISIBLE);
//            Progressbar.setIndeterminate(true);
//            call.enqueue(new Callback<GsonRegister>() {
//                @Override
//                public void onResponse(Call<GsonRegister> call, Response<GsonRegister> response) {
//                    Log.d("response", response.toString());
//
//                    Progressbar.progressiveStop();
//                    Progressbar.setVisibility(View.GONE);
//
////                    GsonRegister gson = new GsonBuilder().create().fromJson(response.toString(), GsonRegister.class);
////                    GsonRegister jsonResponse = response.body();
//
//                    GsonRegister gson = response.body();
//                    if (gson.getFlag().equals(1)) {
//
//                        App.Utils.putString(Constants.user_id, "" + gson.getData().getUserId());
//                        App.Utils.putString(Constants.access_token, "" + gson.getData().getAccessToken());
//                        App.Utils.putString(Constants.user_gson, new Gson().toJson(gson.getData()));
//                        App.Utils.putString(Constants.gender, "" + gson.getData().getGender());
//
//                        App.USER_DETAILS = new GsonBuilder().create().fromJson(App.Utils.getString(Constants.user_gson),
//                                DataRegister.class);
//
//                        Intent i = new Intent(activity, Home.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(i);
//
//                    } else {
//                        App.Utils.ShowAlert(activity, "" + gson.getMsg(), getResources().getString(R.string.app_name));
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<GsonRegister> call, Throwable t) {
//                    Log.d("Error", "error" + t.getMessage());
//                }
//            });
//
//        } else {
//
//            MyUtils.ShowAlert(activity, getResources().getString(R.string.text_internet), getResources().getString(R.string.app_name));
//        }
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
     * open Dialog of gallery and camera
     */
    private void Dialog_Camera_Gallery() {

        metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        CustomDialog = new Dialog(activity);
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
        img_gallery.setOnClickListener(this);
        img_camera.setOnClickListener(this);
        CustomDialog.show();
        CustomDialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        CustomDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_dialog_rounded));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("requestCode", "" + requestCode);

        if (requestCode == Constants.PHOTO_GALLERY) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String filePath = BitmapLoadUtils.getPathFromUri(activity, uri);
                Uri filePathUri = Uri.parse(filePath);

                if (filePathUri != null) {
                    Image_path = filePathUri.toString();
                    Log.e("imagepath_gallery", filePathUri.toString());

                    beginCrop(filePathUri, Constants.RESULT_CROP_GALLERY);
                } else {
                    MyUtils.ShowAlert(activity, getResources().getString(R.string.File_not_Found), getResources().getString(R.string.app_name));
                }
            }

        } else if (requestCode == Constants.RESULT_CROP_GALLERY) {

            if (resultCode == RESULT_OK) {
                if (Image_path != null) {
                    Image_path = data.getStringExtra(Constants.image_register);
                    Log.e("crop_imagepath_gallery", Image_path);
                    Bitmap bitmapImage = BitmapFactory.decodeFile(Image_path);
                    BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.user_placeholder_login_screen);
                    Glide.with(activity).load(Image_path)
                            .error(R.drawable.user_placeholder_login_screen)
                            .into(img_Profile);
                    file = new File(Image_path);

                } else {
                    MyUtils.ShowAlert(activity, getResources().getString(R.string.File_not_Found),
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
                    MyUtils.ShowAlert(activity, getResources().getString(R.string.File_not_Found),
                            getResources().getString(R.string.app_name));
                }
            }

        } else if (requestCode == Constants.RESULT_CROP_CAMERA
                && resultCode == RESULT_OK) {
            try {
                if (Image_path != null) {
                    Image_path = data.getStringExtra(Constants.image_register);
                    Log.e("crop_imagepath_camera", Image_path);

                    Glide.with(activity).load(Image_path)
                            .error(R.drawable.user_placeholder_login_screen)
                            .into(img_Profile);
                    Bitmap bitmapImage = BitmapFactory.decodeFile(Image_path);
                    file = new File(Image_path);
                } else {
                    MyUtils.ShowAlert(activity, getResources().getString(R.string.File_not_Found),
                            getResources().getString(R.string.app_name));
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                MyUtils.ShowAlert(activity, getResources().getString(R.string.File_not_Found), getResources().getString(R.string.app_name));
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
        Intent intent = new Intent(activity, CropActivity.class);
        intent.setData(imageUri);
        intent.putExtra(Constants.FLAG_IS_SQUARE, false);
        intent.putExtra(Constants.IMAGE_CROP_RATIO_WIDTH, Constants.DEFAULT_REGISTER_PICTURE_CROP_RATIO_WIDTH);
        intent.putExtra(Constants.IMAGE_CROP_RATIO_HEIGHT, Constants.DEFAULT_REGISTER_PICTURE_CROP_RATIO_HEIGHT);
        startActivityForResult(intent, resultCrop);

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
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
     * checkPermissions of gallery and camera
     */
    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(activity, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


}
