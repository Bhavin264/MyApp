package com.fittrack.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fittrack.App;
import com.fittrack.Constants.Api;
import com.fittrack.Constants.Constants;
import com.fittrack.Fragment.BMIFragment;
import com.fittrack.Fragment.BMRFragment;
import com.fittrack.Fragment.ChartFragment;
import com.fittrack.Fragment.ChatFragment;
import com.fittrack.Fragment.FeedbackFragment;
import com.fittrack.Fragment.FoodAvoidFragment;
import com.fittrack.Fragment.FoodFragment;
import com.fittrack.Fragment.ForumFragment;
import com.fittrack.Fragment.HomeFragment;
import com.fittrack.Fragment.MakeCompareClgFragment;
import com.fittrack.Fragment.PhotoGalleryFragment;
import com.fittrack.Fragment.ProfileFragment;
import com.fittrack.Fragment.WeightSheetFragment;
import com.fittrack.Interface.ApiInterface;
import com.fittrack.Model.GsonLogout;
import com.fittrack.Progressbar.SmoothProgressBar;
import com.fittrack.R;
import com.fittrack.Utility.LogUtil;
import com.fittrack.Utility.MyUtils;
import com.fittrack.Utility.RoundedCornerImages;
import com.fittrack.activity.Utility.PushClass;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

public class Home extends AppCompatActivity implements View.OnClickListener {

    private Home activity;
    public static DrawerLayout drawer_layout;

    /*header View*/
    public static ImageView img_drawer;
    private TextView tv_username, tv_current_bmi, tv_current_bmr, tv_weight_sheet, tv_photo_gallery, tv_chart, tv_make_compare, tv_forum,
            tv_feedback, tv_login, tv_share, tv_home, tv_food, tv_chat, tv_food_avoid;
    private ImageView img_logout, img_Profile;
    private LinearLayout ll_edit, ll_logout, ll_main, ll_guest;
    private DisplayMetrics metrics;
    private Dialog CustomDialog, DialogLogout;
    /*Dialog*/
    private ImageView img_close;
    private Intent intent;
    private SmoothProgressBar Progressbar;
    private TextView tv_yes, tv_no, tv_title_text;
    private FrameLayout fl_main;
    private FragmentManager mFragmentManager;
    /*Push*/
    BroadcastReceiver receiver;
    private FragmentTransaction ft;
    IntentFilter filter = new IntentFilter();
    private JSONObject custom_data;
    private String msg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.banner_ad_unit_id));
        activity = Home.this;
        findViewById();
        OnClickListener();
        setProfilePhoto();
        getHomeFragment();
        GetNotification();
        filter.addAction(Constants.filterAction);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, final Intent intent) {
                Log.e("onReceive", " onReceive");
                if (intent.getExtras() != null) {
                    String jsonString = intent.getStringExtra(Constants.push_message);
                    msg = intent.getStringExtra(Constants.message);
                    try {
                        custom_data = new JSONObject(jsonString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("onReceive_custom_data", "" + custom_data.toString());
                    Log.e("onReceive_msg", "" + msg);
                }
                new PushClass(Home.this, custom_data, msg);
            }
        };

        AppCenter.start(getApplication(), "19e52bbd-7203-4faa-9631-247e3d21e4ed", Analytics.class, Crashes.class);

    }

    /**
     * Get Notification
     */
    private void GetNotification() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String jsonString = intent.getStringExtra(Constants.push_message);
            msg = intent.getStringExtra(Constants.message);
            try {
                custom_data = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("custom_data", "" + custom_data.toString());
            Log.e("msg", "" + msg);
        }
        if (custom_data != null) {
            CallActivityForSalonPush();
        }

    }

    /**
     * CAll this method from push
     */
    private void CallActivityForSalonPush() {

        Fragment fragment = new ChatFragment();
        Fragment_Replace(fragment);

    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(receiver, filter);

    }

    @Override
    protected void onPause() {
        if (receiver != null)
            unregisterReceiver(receiver);
        super.onPause();
    }
    /**
     * common method for load ads in adsview
     */
    public static void getAds(AdView mAdView) {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    /**
     * get Home Fragment initially
     */
    private void getHomeFragment() {
        drawer_layout.closeDrawer(GravityCompat.START);
        Fragment fragment = new HomeFragment();
        Fragment_Replace(fragment);
    }

    /**
     * setProfilePhoto at drawer
     */
    private void setProfilePhoto() {

        if (!App.Utils.getString(Constants.user_id).equalsIgnoreCase("")) {
            Drawable drawable = activity.getResources().getDrawable(R.drawable.user_placeholder_login_screen);
            int h = drawable.getIntrinsicHeight();
            int w = drawable.getIntrinsicWidth();
            ll_main.setVisibility(View.VISIBLE);
            tv_username.setText(App.USER_DETAILS.getUsername());
            ll_logout.setVisibility(View.VISIBLE);
            ll_guest.setVisibility(View.GONE);

            LogUtil.Print("img", "" + App.USER_DETAILS.getProfileImage());
            if (!App.USER_DETAILS.getProfileImage().equalsIgnoreCase("")) {
                Picasso.with(activity).load(App.USER_DETAILS.getProfileImage())
                        .resize(w, h)
                        .centerCrop()
                        .transform(new RoundedCornerImages())
                        .placeholder(R.drawable.user_placeholder_login_screen)
                        .error(R.drawable.user_placeholder_login_screen)
                        .into(img_Profile);
            } else {
                img_Profile.setImageResource(R.drawable.user_placeholder_login_screen);
            }
        } else {
            ll_logout.setVisibility(View.GONE);
            ll_main.setVisibility(View.GONE);
            ll_guest.setVisibility(View.VISIBLE);
        }
    }

    /**
     * findViews by Ids
     */
    private void findViewById() {

        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        fl_main = (FrameLayout) findViewById(R.id.fl_main);
        /*drawer*/
        img_logout = (ImageView) findViewById(R.id.img_logout);
        img_Profile = (ImageView) findViewById(R.id.img_Profile);
        tv_username = (TextView) findViewById(R.id.tv_username);
        Progressbar = (SmoothProgressBar) findViewById(R.id.google_now);
        tv_home = (TextView) findViewById(R.id.tv_home);
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
        ll_logout = (LinearLayout) findViewById(R.id.ll_logout);
        ll_main = (LinearLayout) findViewById(R.id.ll_main);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_food = (TextView) findViewById(R.id.tv_food);
        tv_chat = (TextView) findViewById(R.id.tv_chat);
        tv_food_avoid = (TextView) findViewById(R.id.tv_food_avoid);
        ll_guest = (LinearLayout) findViewById(R.id.ll_guest);
    }

    /**
     * OnClickListener of Views
     */
    private void OnClickListener() {

        tv_home.setOnClickListener(this);
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
        tv_login.setOnClickListener(this);
        tv_share.setOnClickListener(this);
        tv_food.setOnClickListener(this);
        tv_chat.setOnClickListener(this);
        tv_food_avoid.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_home:
                drawer_layout.closeDrawer(GravityCompat.START);
                Fragment fragment = new HomeFragment();
                Fragment_Replace(fragment);
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

            case R.id.tv_food:
                SideMenuSelection(11);
                break;

            case R.id.tv_food_avoid:
                SideMenuSelection(12);
                break;

            case R.id.tv_chat:
                SideMenuSelection(13);
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
                intent = new Intent(Home.this, Login.class);
                startActivity(intent);
                finish();
                break;
        }
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
            Fragment fragment = new BMIFragment();
            Fragment_Replace(fragment);
//            intent = new Intent(activity, BMI.class);
//            startActivity(intent);
        } else if (i == 1) {

            /* Current BMR */
            drawer_layout.closeDrawers();
            Fragment fragment = new BMRFragment();
            Fragment_Replace(fragment);

        } else if (i == 2) {

            /* Weight Sheet */
            drawer_layout.closeDrawers();
            if (App.Utils.getString(Constants.user_id).equalsIgnoreCase("")) {
                AlertDialog();

            } else {
                Fragment fragment = new WeightSheetFragment();
                Fragment_Replace(fragment);
            }

        } else if (i == 3) {

            /* Photo Galllery */
            drawer_layout.closeDrawers();
            if (App.Utils.getString(Constants.user_id).equalsIgnoreCase("")) {
                AlertDialog();
            } else {
                Fragment fragment = new PhotoGalleryFragment();
                Fragment_Replace(fragment);
            }

        } else if (i == 4) {

            /* Chart */
            drawer_layout.closeDrawers();
            if (App.Utils.getString(Constants.user_id).equalsIgnoreCase("")) {
                AlertDialog();
            } else {
                drawer_layout.closeDrawers();
                Fragment fragment = new ChartFragment();
                Fragment_Replace(fragment);
            }

        } else if (i == 5) {

            /* make compare college */
            drawer_layout.closeDrawers();
            if (App.Utils.getString(Constants.user_id).equalsIgnoreCase("")) {
                AlertDialog();
            } else {
                Fragment fragment = new MakeCompareClgFragment();
                Fragment_Replace(fragment);
            }

        } else if (i == 6) {

            /* Forum */
            drawer_layout.closeDrawers();
            if (App.Utils.getString(Constants.user_id).equalsIgnoreCase("")) {
                AlertDialog();

            } else {
                Fragment fragment = new ForumFragment();
                Fragment_Replace(fragment);
            }

        } else if (i == 7) {

            /* Feedback */
            drawer_layout.closeDrawers();
            if (App.Utils.getString(Constants.user_id).equalsIgnoreCase("")) {
                AlertDialog();

            } else {
                Fragment fragment = new FeedbackFragment();
                Fragment_Replace(fragment);
            }

        } else if (i == 8) {

            /* Logout */
            Dialog_Logout();

        } else if (i == 9) {

            /* Edit Profile */
            drawer_layout.closeDrawers();
            if (App.Utils.getString(Constants.user_id).equalsIgnoreCase("")) {
                AlertDialog();
            } else {
                Fragment fragment = new ProfileFragment();
                Fragment_Replace(fragment);
            }
        } else if (i == 10) {

            /* Share */
            drawer_layout.closeDrawers();

            String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
            }
//            String sharePlaystore = "https://www.google.co.in";
//            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//            sharingIntent.setType("text/plain");
//            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share the Application");
//            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sharePlaystore);
//            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        } else if (i == 11) {

            /* Food Directories */
            drawer_layout.closeDrawers();
            if (App.Utils.getString(Constants.user_id).equalsIgnoreCase("")) {
                AlertDialog();
            } else {
                Fragment fragment = new FoodFragment();
                Fragment_Replace(fragment);
            }
        } else if (i == 12) {

            /* Food Avoid */
            drawer_layout.closeDrawers();
            if (App.Utils.getString(Constants.user_id).equalsIgnoreCase("")) {
                AlertDialog();
            } else {
                Fragment fragment = new FoodAvoidFragment();
                Fragment_Replace(fragment);
            }
        } else if (i == 13) {

            /* Chat with Krira */
            drawer_layout.closeDrawers();
            if (App.Utils.getString(Constants.user_id).equalsIgnoreCase("")) {
                AlertDialog();
            } else {
                Fragment fragment = new ChatFragment();
                Fragment_Replace(fragment);
            }
        }
    }

    /**
     * Alert Dialog
     */
    private void AlertDialog() {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            // set title
            alertDialogBuilder.setTitle(getResources().getString(R.string.app_name));
            // set dialog message
            alertDialogBuilder
                    .setMessage(getResources().getString(R.string.text_login_valid))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.text_ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            intent = new Intent(activity, Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Dialog for Logout
     */
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
        tv_title_text = (TextView) DialogLogout.findViewById(R.id.tv_title_text);
        tv_yes = (TextView) DialogLogout.findViewById(R.id.tv_yes);
        tv_no = (TextView) DialogLogout.findViewById(R.id.tv_no);
        tv_title_text.setText(getResources().getString(R.string.text_are_you_sure_to_logout));
        img_close.setOnClickListener(this);
        tv_yes.setOnClickListener(this);
        tv_no.setOnClickListener(this);

        DialogLogout.show();
        DialogLogout.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        DialogLogout.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_dialog_rounded));

    }

    /**
     * Api of Logout
     */
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

            call.enqueue(new Callback<GsonLogout>() {
                @Override
                public void onResponse(Call<GsonLogout> call, Response<GsonLogout> response) {
                    if (response.body().getFlag().equals(1)) {

                        String reg_id = App.Utils.getString(Constants.gcm_registration_id);
                        App.Utils.ClearAllPreferences();
                        App.Utils.putString(Constants.user_id, "");
                        drawer_layout.closeDrawers();
                        Login.LogoutFromFacebbok();

                        intent = new Intent(activity, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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

    /**
     * method for replacement of fragment
     *
     * @param fragment
     */
    public void Fragment_Replace(Fragment fragment) {
        mFragmentManager = getSupportFragmentManager();
        ft = mFragmentManager.beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.replace(R.id.fl_main, fragment);
        ft.addToBackStack(fragment.getTag());
        ft.commit();

    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            if (mFragmentManager.getBackStackEntryCount() == 0) {
                // and then you define a method allowBackPressed with the logic to allow back pressed or not
                if (mFragmentManager.findFragmentById(R.id.fl_main) instanceof HomeFragment) {
                    finish();
                } else
                    super.onBackPressed();
            } else {
                if (mFragmentManager.findFragmentById(R.id.fl_main) instanceof BMIFragment) {
                    getHomeFragment();
                } else if (mFragmentManager.findFragmentById(R.id.fl_main) instanceof BMRFragment) {
                    getHomeFragment();
                } else if (mFragmentManager.findFragmentById(R.id.fl_main) instanceof WeightSheetFragment) {
                    getHomeFragment();
                } else if (mFragmentManager.findFragmentById(R.id.fl_main) instanceof PhotoGalleryFragment) {
                    getHomeFragment();
                } else if (mFragmentManager.findFragmentById(R.id.fl_main) instanceof ChartFragment) {
                    getHomeFragment();
                } else if (mFragmentManager.findFragmentById(R.id.fl_main) instanceof MakeCompareClgFragment) {
                    getHomeFragment();
                } else if (mFragmentManager.findFragmentById(R.id.fl_main) instanceof ForumFragment) {
                    getHomeFragment();
                } else if (mFragmentManager.findFragmentById(R.id.fl_main) instanceof FeedbackFragment) {
                    getHomeFragment();
                } else if (mFragmentManager.findFragmentById(R.id.fl_main) instanceof ProfileFragment) {
                    getHomeFragment();
                } else if (mFragmentManager.findFragmentById(R.id.fl_main) instanceof HomeFragment) {
                    finish();
                } else
                    mFragmentManager.popBackStack();
            }
        }
    }

}
