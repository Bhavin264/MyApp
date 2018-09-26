package com.fittrack;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.fittrack.Constants.Constants;
import com.fittrack.Model.Register.DataRegister;
import com.fittrack.Utility.CustomFont;
import com.fittrack.Utility.GPSTracker;
import com.fittrack.Utility.MyUtils;
import com.fittrack.Utility.PacificoFont;
import com.google.gson.GsonBuilder;
import com.splunk.mint.Mint;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;


public class App extends MultiDexApplication {

    private static final String TAG = "App";
    public static Context context;
    public static MyUtils Utils;
    public static Locale locale;
    public static Typeface app_font_regular;
    public static Typeface app_font_light;
    public static Typeface app_font_bold;
    public static Typeface app_font_medium;

    public static Typeface app_neon_font;
    public static Typeface app_pacifico_font;

    public static OkHttpClient client;
    private GPSTracker gt;
    private HttpLoggingInterceptor interceptor;
    public static DataRegister USER_DETAILS;


    @SuppressWarnings("static-access")
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        context = getApplicationContext();
        Utils = new MyUtils(context);

        InitializeHttpClient();
        InitializeFont();
        /** GPS tracker for current location */
        gt = new GPSTracker(this);

        if (!App.Utils.getString(Constants.user_gson).equals(""))
            USER_DETAILS = new GsonBuilder().create().fromJson(App.Utils.getString(Constants.user_gson), DataRegister.class);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void InitializeFont() {

        CustomFont font = new CustomFont(context);
        app_font_regular = font.font_regular;
        app_font_bold = font.font_bold;
        app_font_light = font.font_light;

//        NeonFont font1 = new NeonFont(context);
//        app_neon_font = font1.app_font_neon;

        PacificoFont font2 = new PacificoFont(context);
        app_pacifico_font = font2.app_font_pacifico;

    }


    private void InitializeHttpClient() {

        client = new OkHttpClient.Builder()
                .addInterceptor(new App.BasicAuthInterceptor(Constants.AUTH_USERNAME, Constants.AUTH_PASSWORD))
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

//        client.setBasicAuth(Constants.AUTH_USERNAME, Constants.AUTH_PASSWORD);
//        client.setConnectTimeout(10 * 1000);
//        client.setMaxRetriesAndTimeout(1, 10 * 1000);
//        client.setEnableRedirects(true);
//        client.setTimeout(60 * 1000);


    }

    public static class BasicAuthInterceptor implements Interceptor {

        private String credentials;

        public BasicAuthInterceptor(String user, String password) {
            this.credentials = Credentials.basic(user, password);
        }

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request authenticatedRequest = request.newBuilder()
                    .header("Authorization", credentials).build();
            return chain.proceed(authenticatedRequest);
        }
    }

    public static void RegisterActivityForBugsense(Context context) {
        try {
            Mint.initAndStartSession(context, Constants.Mint_key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }




        /*if (App.Utils.getString(Constants.language).equals("")) {
            if (Locale.getDefault().getLanguage().equalsIgnoreCase(Constants.language_en)) {
                changeLang(Constants.language_en);
                App.Utils.putString(Constants.language, Constants.language_en);
            } else if (Locale.getDefault().getLanguage().equalsIgnoreCase(Constants.language_ar)) {
                changeLang(Constants.language_ar);
                App.Utils.putString(Constants.language, Constants.language_ar);
            } else {
                changeLang(Constants.language_en);
                App.Utils.putString(Constants.language, Constants.language_en);
            }
        }
        else {
            if (App.USER_DETAILS.getLang().equalsIgnoreCase(Constants.language_en)) {
                changeLang(Constants.language_en);
                App.Utils.putString(Constants.language, Constants.language_en);
            } else if (App.USER_DETAILS.getLang().equalsIgnoreCase(Constants.language_ar)) {
                changeLang(Constants.language_ar);
                App.Utils.putString(Constants.language, Constants.language_ar);
            } else {
                changeLang(Constants.language_en);
                App.Utils.putString(Constants.language, Constants.language_en);
            }
        }*/


//    public void getGCMRegID() {
//        if (Utils.getString(Constants.gcm_registration_id).equalsIgnoreCase("")) {
//
//            GCM_reg_id = getRegistrationId(context);
//            LogUtil.Print("Preference", "GCM_regId is : " + GCM_reg_id);
//            if (GCM_reg_id.length() == 0) {
//
//                String loading = context.getResources().getString(
//                        R.string.txt_loading);
//                // mProgressDialog = ProgressDialog.show(context, "", loading,
//                // true, false);
//                new registerBackground().execute(null, null, null);
//
//                LogUtil.Print("length == 0", "GCM_regId is : " + GCM_reg_id);
//
//            } else {
//                Utils.putString(Constants.gcm_registration_id, GCM_reg_id);
//
//                LogUtil.Print("length != 0", "GCM_regId is : " + GCM_reg_id);
//                if (Utils.getString(Constants.device_id).equalsIgnoreCase("")
//                        || Utils.getString(Constants.device_id) == null) {
//                    Utils.putString(Constants.device_id, App.Utils.GetDeviceID());
//                }
//            }
//
//            gcm = GoogleCloudMessaging.getInstance(context);
//        } else {
//
//            LogUtil.Print(TAG,
//                    "getGCMRegID : else :"
//                            + Utils.getString(Constants.gcm_registration_id));
//
//
//        }
//    }


//    // ----------------------GCM---------------------------------------------------//
//    private String getRegistrationId(Context context) {
//
//        String registrationId = Utils.getString(Constants.gcm_registration_id);
//        if (registrationId.length() == 0) {
//            LogUtil.Print("gcm register not found", ".........Registration not found.");
//            return "";
//        }
//        // check if app was updated; if so, it must clear registration id to
//        // avoid a race condition if GCM sends a message
//        int registeredVersion = Integer
//                .parseInt(Utils.getString("appVersion"));
//        int currentVersion = getAppVersion(context);
//        if (registeredVersion != currentVersion || isRegistrationExpired()) {
//            LogUtil.Print("...", "App version changed or registration expired.");
//            return "";
//        }
//        return registrationId;
//    }


//    private class registerBackground extends AsyncTask<String, String, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            String msg = "";
//            try {
//
//                if (gcm == null) {
//                    gcm = GoogleCloudMessaging.getInstance(context);
//                }
//
//                GCM_reg_id = gcm.register(Constants.sender_id);
//                LogUtil.Print("", "registerBackground : gcm reg id : " + GCM_reg_id);
//                msg = "Device registered, registration id = " + GCM_reg_id;
//
//                setRegistrationId(context, GCM_reg_id);
//
//            } catch (IOException ex) {
//                msg = "Error :" + ex.getMessage();
//            }
//            return msg;
//        }
//
//        @Override
//        protected void onPostExecute(String msg) {
//            // if (mProgressDialog != null)
//            // mProgressDialog.dismiss();
//
//            if (!GCM_reg_id.equalsIgnoreCase("")) {
//
//                LogUtil.Print("", "registerBackground : onPostExecute :gcm reg id : "
//                        + GCM_reg_id);
//
//                Utils.putString(Constants.gcm_registration_id, GCM_reg_id);
//                if (Utils.getString(Constants.device_id).equalsIgnoreCase("")
//                        || Utils.getString(Constants.device_id) == null) {
//                    Utils.putString(Constants.device_id, App.Utils.GetDeviceID());
//                }
//            }
//        }
//    }

//    private void setRegistrationId(Context context, String regId) {
//
//        int appVersion = getAppVersion(context);
//
//        LogUtil.Print("...", "Saving regId on app version " + appVersion);
//
//        long expirationTime = System.currentTimeMillis()
//                + REGISTRATION_EXPIRY_TIME_MS;
//        Utils.putString(Constants.gcm_registration_id, regId);
//        Utils.putString("appVersion", String.valueOf(appVersion));
//        Utils.putString("onServerExpirationTimeMs",
//                String.valueOf(expirationTime));
//        LogUtil.Print("", "Setting registration expiry time to "
//                + new Timestamp(expirationTime));
//
//    }


}
