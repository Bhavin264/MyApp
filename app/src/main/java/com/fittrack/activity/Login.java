package com.fittrack.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
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
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fittrack.App;
import com.fittrack.Constants.Api;
import com.fittrack.Constants.Constants;
import com.fittrack.Interface.ApiInterface;
import com.fittrack.Model.ForgotPassword.GsonForgotPassword;
import com.fittrack.Model.Register.DataRegister;
import com.fittrack.Model.Register.GsonRegister;
import com.fittrack.Progressbar.SmoothProgressBar;
import com.fittrack.R;
import com.fittrack.Utility.LogUtil;
import com.fittrack.Utility.MyUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Login";
    private EditText et_email, et_password, et_email_forgot;
    private TextView tv_forgot_password, tv_login, tv_register, tv_fb, tv_skip;
    private LinearLayout ll_register;
    /*headr View*/
    private TextView tv_title;
    private Login activity;
    private SmoothProgressBar Progressbar;
    List<DataRegister> dataregister = new ArrayList<>();
    /*Dialog*/
    private DisplayMetrics metrics;
    private Dialog CustomDialog, ForgotPaawordDialog;
    private ImageView img_cancel, img_close;
    private LinearLayout ll_done;
    private CallbackManager callbackManager;
    private LoginButton tv_facebook_login;
    private Intent intent;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.RegisterActivityForBugsense(activity);
        initializeFacebook();
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();

        activity = Login.this;
        HeaderView();
        findViewById();
        OnClickListener();
        LoginWithFacebook();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.Print(TAG, "requestCode " + requestCode);
        LogUtil.Print(TAG, "resultCode " + resultCode);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Login with Facebook async
     */
    private void LoginWithFacebook() {

        callbackManager = CallbackManager.Factory.create();
        tv_facebook_login.setReadPermissions(Arrays.asList(
                "public_profile", "email"));
        tv_facebook_login.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Bundle params = new Bundle();
                        params.putString("fields", "id,name,email,gender,picture.type(large)");//picture.type(large)
                        new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    @Override
                                    public void onCompleted(GraphResponse response) {
                                        if (response != null) {
                                            String email = "";
                                            String name = "";
                                            String id = "";
                                            String gender = "";
                                            String profilePic = "";
                                            try {
                                                JSONObject data = response.getJSONObject();
                                                if (data.has("picture")) {
                                                    profilePic = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                                }
                                                if (data.has("email")) {
                                                    email = data.getString("email");
                                                }
                                                if (data.has("id")) {
                                                    id = data.getString("id");
                                                }
                                                if (data.has("name")) {
                                                    name = data.getString("name");
                                                }
                                                if (data.has("gender")) {
                                                    gender = data.getString("gender");
                                                }
                                                LogUtil.Print(TAG, "data --> " + data.toString());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            if (email.isEmpty()) {
                                                MyUtils.ShowAlert(activity, getResources().getString(R.string.text_fb_email_require), getResources().getString(R.string.app_name));
                                            } else {
//                                                {"id":"123828541674778","name":"Digni Zant","email":"dignizant.developer@gmail.com","gender":"male",
//                                                        "picture":{"data":{"is_silhouette":false,"url":"https:\/\/scontent.xx.fbcdn.net\/v\/t1.0-1\/p200x200\/20638916_100414880682811_3528492988709928112_n.jpg?oh=ff8056f7092eda325d9279f271139aa8&oe=5A4BB2DB"}}}
//                                                MyUtils.makeToast("Successfully Logged in.");
                                                App.Utils.putString(Constants.profile_image, profilePic);
                                                CheckIsFbLoginAlreadyApi(email, id, name, gender);
//                                                SocialLoginAPIRequest(email, name, id, gender, profilePic);
                                            }
                                        }
                                    }
                                }).executeAsync();
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        MyUtils.ShowAlert(activity, getResources().getString(R.string.text_server_error).replace("X", exception.getMessage()), getResources().getString(R.string.app_name));
                    }
                });
    }

    /**
     * Check Is FbLogin Already  Login in Api
     *
     * @param email
     * @param id
     * @param name
     * @param gender
     */
    private void CheckIsFbLoginAlreadyApi(final String email, final String id, final String name, final String gender) {
        if (App.Utils.IsInternetOn()) {

            ApiInterface request = Api.retrofit.create(ApiInterface.class);
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.email, email);
            params.put(Constants.fb_access_token, id);
            params.put(Constants.register_id, App.Utils.getString(Constants.gcm_registration_id));
            params.put(Constants.device_token, App.Utils.GetDeviceID());
            params.put(Constants.lang, Constants.language);
            params.put(Constants.device_type, Constants.DEVICE_TYPE_ANDROID);

            LogUtil.Print("Initial_fblogin_check__params", "" + params);
            Call<GsonRegister> call = request.getCheckFbInitialLogin(params);
            Progressbar.progressiveStart();
            Progressbar.setVisibility(View.VISIBLE);
            Progressbar.setIndeterminate(true);
            call.enqueue(new Callback<GsonRegister>() {
                @Override
                public void onResponse(Call<GsonRegister> call, Response<GsonRegister> response) {
                    Progressbar.progressiveStop();
                    Progressbar.setVisibility(View.GONE);
                    GsonRegister gson = response.body();

                    if (gson.getFlag().equals(1)) {

                        if (gson.getData().getIsFacebook().equalsIgnoreCase("1")) {

                            App.Utils.putString(Constants.user_id, "" + gson.getData().getUserId());
                            App.Utils.putString(Constants.access_token, "" + gson.getData().getAccessToken());
                            App.Utils.putString(Constants.user_gson, new Gson().toJson(gson.getData()));
                            App.Utils.putString(Constants.gender, "" + gson.getData().getGender());

                            App.USER_DETAILS = new GsonBuilder().create().fromJson(App.Utils.getString(Constants.user_gson),
                                    DataRegister.class);
                            Intent intent = new Intent(Login.this, Home.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        } else {

                            Intent intent = new Intent(Login.this, InitialProfile.class);
                            intent.putExtra(Constants.name, name);
                            intent.putExtra(Constants.id, id);
                            intent.putExtra(Constants.email, email);
                            intent.putExtra(Constants.gender, gender);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }

                    } else {

                        tv_facebook_login.setVisibility(View.VISIBLE);
                        Progressbar.progressiveStop();
                        Progressbar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<GsonRegister> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    CheckIsFbLoginAlreadyApi(email, id, name, gender);
                }
            });

        } else {

            App.Utils.ShowAlert(Login.this, getResources().getString(R.string.text_internet),
                    getResources().getString(R.string.app_name));
        }
    }

    /**
     * socialLogin API Request
     */
//    private void SocialLoginAPIRequest(String email, String name, String id, String gender, String profilePic) {
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
//}


    /**
     * Logout From Facebbok
     */
    public static void LogoutFromFacebbok() {
        try {
            LoginManager loginManager = LoginManager.getInstance();
            if (loginManager != null)
                loginManager.logOut();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * findViews by Ids
     */
    private void findViewById() {
        tv_facebook_login = (LoginButton) findViewById(R.id.tv_facebook_login);
        tv_skip = (TextView) findViewById(R.id.tv_skip);
        tv_fb = (TextView) findViewById(R.id.tv_fb);
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        tv_forgot_password = (TextView) findViewById(R.id.tv_forgot_password);
        tv_login = (TextView) findViewById(R.id.tv_login);
        ll_register = (LinearLayout) findViewById(R.id.ll_register);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_register.setPaintFlags(tv_login.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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

    public void initializeFacebook() {

        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    /**
     * OnClickListener of Views
     */
    private void OnClickListener() {
        tv_login.setOnClickListener(this);
        tv_forgot_password.setOnClickListener(this);
        ll_register.setOnClickListener(this);
        tv_skip.setOnClickListener(this);
        tv_fb.setOnClickListener(this);
    }

    /**
     * Header view
     */
    private void HeaderView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.text_login));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_login:

                if (et_email.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_email_empty), getResources().getString(R.string.app_name));

                } else if (!App.Utils.IsValidEmail(et_email.getText().toString())) {

                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_valid_email_empty), getResources().getString(R.string.app_name));

                } else if (et_password.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_password_empty), getResources().getString(R.string.app_name));

                } else if (et_password.getText().length() < 6) {

                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_password_limit), getResources().getString(R.string.app_name));

                } else {

                    View view = getCurrentFocus();

                    if (view != null) {

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    CallLoginAPI();
                }
                break;

            case R.id.ll_register:

                intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
//                finish();
                break;

            case R.id.tv_forgot_password:
                ForgotPaawordDialog();
                break;

            case R.id.tv_skip:
                intent = new Intent(Login.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

            case R.id.img_cancel:
                ForgotPaawordDialog.dismiss();
                break;

            case R.id.ll_done:
                if (et_email_forgot.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_email_empty), getResources().getString(R.string.app_name));

                } else if (!App.Utils.IsValidEmail(et_email_forgot.getText().toString())) {

                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_valid_email_empty), getResources().getString(R.string.app_name));

                } else {

                    ForgotPaawordDialog.dismiss();

                    View view = getCurrentFocus();

                    if (view != null) {

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    CallForgotPasswordApi();

                }
                break;
            case R.id.tv_facebook_login:
                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                }
                LoginWithFacebook();
                break;


            case R.id.tv_fb:
                tv_facebook_login.performClick();
                break;

        }

    }


    /**
     * ForgotPassword Api
     */
    private void CallForgotPasswordApi() {
        if (App.Utils.IsInternetOn()) {

            ApiInterface request = Api.retrofit.create(ApiInterface.class);
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.email, et_email_forgot.getText().toString());
            params.put(Constants.lang, Constants.language);
            LogUtil.Print("add_weightsheet_params", "" + params);

            Call<GsonForgotPassword> call = request.getForgotPassword(params);
            Progressbar.progressiveStart();
            Progressbar.setVisibility(View.VISIBLE);
            Progressbar.setIndeterminate(true);
            call.enqueue(new Callback<GsonForgotPassword>() {
                @Override
                public void onResponse(Call<GsonForgotPassword> call, Response<GsonForgotPassword> response) {

                    Progressbar.progressiveStop();
                    Progressbar.setVisibility(View.GONE);
                    GsonForgotPassword gson = response.body();

                    if (gson.getFlag().equals(1)) {
                        App.Utils.ShowAlert(activity, "" + gson.getMsg(), getResources().getString(R.string.app_name));
                    } else {
                        App.Utils.ShowAlert(activity, "" + gson.getMsg(), getResources().getString(R.string.app_name));
                    }
                }

                @Override
                public void onFailure(Call<GsonForgotPassword> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });
        } else {
            MyUtils.ShowAlert(activity, getResources().getString(R.string.text_internet), getResources().getString(R.string.app_name));
        }
    }

    /**
     * ForgotPaaword Dialog
     */
    private void ForgotPaawordDialog() {

        metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        ForgotPaawordDialog = new Dialog(activity);
        ForgotPaawordDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ForgotPaawordDialog.setCancelable(true);
        ForgotPaawordDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ForgotPaawordDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ForgotPaawordDialog.setContentView(R.layout.forgot_password);
        et_email_forgot = (EditText) ForgotPaawordDialog.findViewById(R.id.et_email_forgot);

        ll_done = (LinearLayout) ForgotPaawordDialog.findViewById(R.id.ll_done);
        img_cancel = (ImageView) ForgotPaawordDialog.findViewById(R.id.img_cancel);
        ll_done.setOnClickListener(this);
        img_cancel.setOnClickListener(this);
        ForgotPaawordDialog.show();
        ForgotPaawordDialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        ForgotPaawordDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_dialog_rounded));

    }

    /**
     * Login API of user
     */
    private void CallLoginAPI() {

        if (App.Utils.IsInternetOn()) {

            ApiInterface request = Api.retrofit.create(ApiInterface.class);
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.email, et_email.getText().toString());
            params.put(Constants.password, et_password.getText().toString());
            params.put(Constants.lang, Constants.language);
            params.put(Constants.device_token, App.Utils.GetDeviceID());
            params.put(Constants.register_id, App.Utils.getString(Constants.gcm_registration_id));
            params.put(Constants.device_type, Constants.DEVICE_TYPE_ANDROID);
            LogUtil.Print("login_params", "" + params);

            Call<GsonRegister> call = request.getJSONLogin(params);
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

//                    GsonRegister gson = new GsonBuilder().create().fromJson(response.toString(), GsonRegister.class);

                    if (gson.getFlag().equals(1)) {

                        dataregister.add(gson.getData());
                        App.Utils.putString(Constants.user_id, "" + gson.getData().getUserId());
                        App.Utils.putString(Constants.access_token, "" + gson.getData().getAccessToken());
                        App.Utils.putString(Constants.user_gson, new Gson().toJson(gson.getData()));
                        App.Utils.putString(Constants.gender, "" + gson.getData().getGender());

                        App.USER_DETAILS = new GsonBuilder().create().fromJson(App.Utils.getString(Constants.user_gson),
                                DataRegister.class);

                        Intent i = new Intent(Login.this, Home.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

                    } else {
                        App.Utils.ShowAlert(activity, "" + response.body().getMsg(), getResources().getString(R.string.app_name));
                    }
                }

                @Override
                public void onFailure(Call<GsonRegister> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });

        } else {

            MyUtils.ShowAlert(activity, getResources().getString(R.string.text_internet), getResources().getString(R.string.app_name));
        }

    }
}
//        tv_facebook_login.setReadPermissions(Arrays.asList("public_profile", "email", "user_photos"));
//        tv_facebook_login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//
//            @Override
//            public void onSuccess(final LoginResult loginResult) {
//                Log.d("onSuccess", "LoginResult : " + loginResult.getAccessToken().getToken());
//                GraphRequest request = GraphRequest.newMeRequest(
//                        loginResult.getAccessToken(),
//                        new GraphRequest.GraphJSONObjectCallback() {
//                            @Override
//                            public void onCompleted(JSONObject object,
//                                                    GraphResponse response) {
//                                Log.d(TAG, "response : " + response);
//                                if (response.getError() == null) {
//                                    JSONObject jsonObject;
//                                    try {
//                                        jsonObject = new JSONObject(
//                                                response.getRawResponse());
//                                        Log.d(TAG, "jsonObject_faceboook....." + jsonObject.toString());
//                                        fb_id = jsonObject
//                                                .getString("id");
//                                        firstname = jsonObject
//                                                .getString("first_name");
//                                        lastname = jsonObject
//                                                .getString("last_name");
//                                        email = jsonObject
//                                                .getString("email");
//                                        Toast.makeText(activity, "logged facebook in sucessfullly", Toast.LENGTH_SHORT).show();
//                                        if (!fb_id.equals("")) {
//                                        }
//                                        //  SocialLoginAPIRequest("facebook", email, firstname, lastname, fb_id, profilePhoto);
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                        if (!fb_id.equals("")) {
//                                        }
//                                        //    SocialLoginAPIRequest("facebook", email, firstname, lastname, fb_id, profilePhoto);
//                                    }
//                                } else {
//
//                                    LogoutFromFacebbok();
//                                }
//                            }
//                        });
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id,first_name,last_name,link,email");
//                request.setParameters(parameters);
//                request.executeAsync();
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//                Log.d(TAG, "onCancel......");
//            }
//
//            @Override
//            public void onError(FacebookException onError) {
//                // App code
//                Log.d(TAG, "onError......" + onError.getMessage());
//            }
//        });