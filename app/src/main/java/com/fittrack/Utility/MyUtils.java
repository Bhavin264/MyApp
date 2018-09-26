package com.fittrack.Utility;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ResultReceiver;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.fittrack.Constants.Constants;
import com.fittrack.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MyUtils {

    public static final String NUMBERS_AND_LETTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String NUMBERS = "0123456789";
    public static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String CAPITAL_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static String PREFERENCE_NAME = "GLOBAL_DATA";
    public static ProgressDialog pDialog = null, pDialogSimilor = null, pDialogTournament = null;
    static Context ctx;

    // -------------------------------------------------//
    // ----------------Show Toast-----------------------//
    // -------------------------------------------------//

    public MyUtils(Context ctx) {
        super();
        MyUtils.ctx = ctx;
    }


    // -------------------------------------------------//
    // -----------------Share Preference---------------//
    // -------------------------------------------------//

    public static void makeToast(String text) {
        Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show();
    }

    public static boolean putString(String key, String value) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static String getString(String key) {
        return getString(ctx, key, "");
    }


//    public static String getLanguageString(String key) {
//        return getString(ctx, key, "" + Constants.language_en);
//    }

    public static boolean RemoveString(String key) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(key);
        return editor.commit();
    }

    public static String getString(Context context, String key,
                                   String defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, defaultValue);
    }

    public static boolean putInt(String key, int value) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public static int getInt(String key) {
        return getInt(ctx, key, -1);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getInt(key, defaultValue);
    }

    public static boolean putLong(String key, long value) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public static long getLong(String key) {
        return getLong(ctx, key, -1);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getLong(key, defaultValue);
    }

    public static boolean putFloat(String key, float value) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    public static float getFloat(String key) {
        return getFloat(ctx, key, -1);
    }

    public static float getFloat(Context context, String key, float defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getFloat(key, defaultValue);
    }

    public static boolean putBoolean(String key, boolean value) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static boolean getBoolean(String key) {
        return getBoolean(ctx, key, false);
    }

    public static boolean getBoolean(Context context, String key,
                                     boolean defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getBoolean(key, defaultValue);
    }

    //--------------Check Language---------------
//    public static boolean CheckLanguage() {
//
//        boolean is_language = true;
//
//        String CurrentLanguage = Locale.getDefault().getLanguage();
//
//        if (CurrentLanguage.equalsIgnoreCase(Constants.language_en)) {
//            is_language = true;
//        } else is_language = !CurrentLanguage.equalsIgnoreCase(Constants.language_ro);
//
//        return is_language;
//    }

    // -------------------------------------------------//
    // --------email address validation-----------------//
    // -------------------------------------------------//
    public static boolean IsValidEmail(String email2) {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(email2);
        check = m.matches();

        return check;
    }


    /**
     * Replace string from old to new
     *
     * @param findtxt
     * @param replacetxt
     * @param str
     * @param isCaseInsensitive
     * @return
     */
    public static String replaceAll(String findtxt, String replacetxt, String str,
                                    boolean isCaseInsensitive) {
        if (str == null) {
            return null;
        }
        if (findtxt == null || findtxt.length() == 0) {
            return str;
        }
        if (findtxt.length() > str.length()) {
            return str;
        }
        int counter = 0;
        String thesubstr = "";
        while ((counter < str.length())
                && (str.substring(counter).length() >= findtxt.length())) {
            thesubstr = str.substring(counter, counter + findtxt.length());
            if (isCaseInsensitive) {
                if (thesubstr.equalsIgnoreCase(findtxt)) {
                    str = str.substring(0, counter) + replacetxt
                            + str.substring(counter + findtxt.length());
                    // Failing to increment counter by replacetxt.length() leaves you open
                    // to an infinite-replacement loop scenario: Go to replace "a" with "aa" but
                    // increment counter by only 1 and you'll be replacing 'a's forever.
                    counter += replacetxt.length();
                } else {
                    counter++; // No match so move on to the next character from
                    // which to check for a findtxt string match.
                }
            } else {
                if (thesubstr.equals(findtxt)) {
                    str = str.substring(0, counter) + replacetxt
                            + str.substring(counter + findtxt.length());
                    counter += replacetxt.length();
                } else {
                    counter++;
                }
            }
        }
        return str;
    }


    // -------------------------------------------------//
    // --------removeLastChar----------------------------//
    // -------------------------------------------------//
    public static String removeLastChar(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    // -------------------------------------------------//
    // --------showRandomInteger------------------------//
    // -------------------------------------------------//
    public static int showRandomInteger(int aStart, int aEnd, Random aRandom) {
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        // get the range, casting to long to avoid overflow problems
        long range = (long) aEnd - (long) aStart + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * aRandom.nextDouble());
        int randomNumber = (int) (fraction + aStart);
        return randomNumber;
    }

    // -------------------------------------------------//
    // --------hide keyboard----------------------------//
    // -------------------------------------------------//
    public static void hideKeyBoard(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager
                .hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void hideSoftKeyboard(Activity activity) {


        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            LogUtil.Print("SoftwareOpen", "Software Keyboard was shown");
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } else {
            LogUtil.Print("SoftwareClose", "Software Keyboard was not shown");

        }
    }

    public static void ChangeKeyboardFocus(EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    // -------------------------------------------------//
    // --------getStorageDirectory----------------------//
    // -------------------------------------------------//
    public static String getStorageDirectory() {
        Boolean isSDPresent = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);

        String filename;

        if (isSDPresent) {
            // yes SD-card is present

            filename = Environment.getExternalStorageDirectory().getPath();

        } else {
            filename = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).getPath();

        }

        return filename;

    }

    // -------------------------------------------------//
    // --------getRealPathFromURI----------------------//
    // -------------------------------------------------//
    public static String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = ctx.getContentResolver().query(contentUri, proj, null,
                    null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    //-----------------------getpathfromURI---------------------------------//
    public static String getPathFromURI(final FragmentActivity context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        LogUtil.Print("IS_Not", "getAuthority" + uri.getAuthority());

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                String storageDefinition;

                if ("primary".equalsIgnoreCase(type)) {

                    LogUtil.Print("type...primary...", type);
                    LogUtil.Print("Path_Converted...", Environment.getExternalStorageDirectory() + "/" + split[1]);
                    return Environment.getExternalStorageDirectory() + "/" + split[1];

                } else {

                    LogUtil.Print("type...", type);
                    LogUtil.Print("Path_Converted...", Environment.getExternalStorageDirectory() + "/" + split[1]);

                    return "storage" + "/" + split[0] + "/" + split[1];
                }
            }

            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }

            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            } else {

                LogUtil.Print("IS_Not", "isExternalStorageDocument");
                LogUtil.Print("IS_Not", "isMediaDocument");
                LogUtil.Print("IS_Not", "isDownloadsDocument");
                return null;
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();

        } else {

            return null;
        }
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    // -------------------------------------------------//
    // --------ChangeFocus of keyboard---------------//
    // -------------------------------------------------//

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }



    public static String GetDateOnRequireFormat(String date,
                                                String givenformat, String resultformat) {


        String result = "";
        SimpleDateFormat sdf;
        SimpleDateFormat sdf1;
        try {
            sdf = new SimpleDateFormat(givenformat, Locale.ENGLISH);
            sdf1 = new SimpleDateFormat(resultformat, Locale.ENGLISH);
            result = sdf1.format(sdf.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            sdf = null;
            sdf1 = null;
        }
        return result;
    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public static Date addMonths(Date date, int months) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        cal.add(Calendar.MONTH, months); //minus number would decrement the months
        return cal.getTime();
    }

    public static String GetTitleDate(int flag) {
        String result = "";

        if (flag == 0) {
            result = GetDateFromTimeStamp(GetCurrentTimeStamp(), Constants.DATE_YYYY_MM_DD_FORMAT);

        } else if (flag == 1) {
            Calendar c = Calendar.getInstance(Locale.ENGLISH);
            c.add(Calendar.DATE, 1);
            Date dt = c.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_YYYY_MM_DD_FORMAT);
            result = formatter.format(dt);
        } else if (flag == 2) {
            Calendar c = Calendar.getInstance(Locale.ENGLISH);
            c.add(Calendar.DATE, 2);
            Date dt = c.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_YYYY_MM_DD_FORMAT);
            result = formatter.format(dt);
        } else if (flag == 3) {
            Calendar c = Calendar.getInstance(Locale.ENGLISH);
            c.add(Calendar.DATE, 3);
            Date dt = c.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_YYYY_MM_DD_FORMAT);
            result = formatter.format(dt);
        }

        return result;
    }

    public static boolean DateIsBeforeThanProvidedDate(String date_first, String date_second, String dateformat) {
        boolean is_before = false;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
            Date date1 = sdf.parse(date_first);
            Date date2 = sdf.parse(date_second);

            System.out.println(sdf.format(date1));
            System.out.println(sdf.format(date2));

            if (date1.compareTo(date2) > 0) {
                is_before = true;
            }


        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return is_before;
    }

    public static boolean DateIsBeforeStrickThanProvidedDate(String date_first, String date_second, String dateformat) {
        boolean is_before = false;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
            Date date1 = sdf.parse(date_first);
            Date date2 = sdf.parse(date_second);

            System.out.println(sdf.format(date1));
            System.out.println(sdf.format(date2));

            if (date1.compareTo(date2) >= 0) {
                is_before = true;
            }


        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return is_before;
    }

    public static boolean DateIsBeforeThanOrEqualProvidedDate(String date_first, String date_second, String dateformat) {
        boolean is_before = false;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
            Date date1 = sdf.parse(date_first);
            Date date2 = sdf.parse(date_second);

            System.out.println(sdf.format(date1));
            System.out.println(sdf.format(date2));

            if (date1.compareTo(date2) == 1) {
                is_before = true;
            }


        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return is_before;
    }

    public static boolean IsEqualDate(String date_first, String date_second, String dateformat) {
        boolean is_equal = false;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
            Date date1 = sdf.parse(date_first);
            Date date2 = sdf.parse(date_second);

            System.out.println(sdf.format(date1));
            System.out.println(sdf.format(date2));

            if (date1.compareTo(date2) == 0) {
                is_equal = true;
            }


        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return is_equal;
    }

    public static void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // -------------------------------------------------//
    // ------------date conversion----------------------//
    // -------------------------------------------------//

    public static void showSoftkeyboard(View view) {
        showSoftkeyboard(view, null);
    }

    // -------------------------------------------------//
    // ------------adding days to given date-------------//
    // -------------------------------------------------//

    public static void showSoftkeyboard(View view, ResultReceiver resultReceiver) {
        Configuration config = view.getContext().getResources()
                .getConfiguration();
        if (config.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            InputMethodManager imm = (InputMethodManager) view.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            if (resultReceiver != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT,
                        resultReceiver);
            } else {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }
    // -------------------------------------------------//
    // ------------adding month to given date-------------//
    // -------------------------------------------------//

    public static void startWebActivity(Context context, String url) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }
    // -------------------------------------------------//
    // ------------date conversion----------------------//
    // -------------------------------------------------//

    public static void startWebSearchActivity(Context context, String url) {
        final Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, url);
        context.startActivity(intent);
    }

    // -------------------------------------------------//
    // --------------Compare Date----------------------//
    // -------------------------------------------------//

    public static void startEmailActivity(Context context, int toResId,
                                          int subjectResId, int bodyResId) {
        startEmailActivity(context, context.getString(toResId),
                context.getString(subjectResId), context.getString(bodyResId));
    }
    // -------------------------------------------------//
    // --------------Compare Date----------------------//
    // -------------------------------------------------//

    public static void startEmailActivity(Context context, String to,
                                          String subject, String body) {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        //intent.setType("message/rfc822");
        intent.setType("text/html");
        if (!TextUtils.isEmpty(to)) {
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        }
        if (!TextUtils.isEmpty(subject)) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if (!TextUtils.isEmpty(body)) {
            intent.putExtra(Intent.EXTRA_TEXT, body);
        }

        final PackageManager pm = context.getPackageManager();
        try {
            if (pm.queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY).size() == 0) {
                intent.setType("text/plain");
            }
        } catch (Exception e) {
            Log.w("Error.", e);
        }

        context.startActivity(intent);
    }
    // -------------------------------------------------//
    // --------------Compare Date----------------------//
    // -------------------------------------------------//

    public static String getAppName() {
        return getAppName(ctx, null);
    }

    // -------------------------------------------------//
    // --------------check is equal Date----------------------//
    // -------------------------------------------------//

    public static String getAppName(Context context, String packageName) {
        String applicationName;

        if (packageName == null) {
            packageName = context.getPackageName();
        }

        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    packageName, 0);
            applicationName = context
                    .getString(packageInfo.applicationInfo.labelRes);
        } catch (Exception e) {
            Log.w("error", "Failed to get version number." + e);
            applicationName = "";
        }

        return applicationName;
    }

    // -------------------------------------------------//
    // --------------Hide Keyboard----------------------//
    // -------------------------------------------------//

    // -------------------------------------------------//
    // --------------get app VersionNumber--------------//
    // -------------------------------------------------//
    public static String getAppVersionNumber() {
        return getAppVersionNumber(ctx, null);
    }

    // -------------------------------------------------//
    // --------------Show Keyboard----------------------//
    // -------------------------------------------------//

    public static String getAppVersionNumber(Context context, String packageName) {
        String versionName;

        if (packageName == null) {
            packageName = context.getPackageName();
        }

        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    packageName, 0);
            versionName = packageInfo.versionName;
        } catch (Exception e) {
            // Log.e("Failed to get version number.",""+e);
            e.printStackTrace();
            versionName = "";
        }

        return versionName;
    }

    public static String getAppVersionCode() {
        return getAppVersionCode(ctx, null);
    }

    // -------------------------------------------------//
    // --------------Open WebBrowser--------------------//
    // -------------------------------------------------//

    public static String getAppVersionCode(Context context, String packageName) {
        String versionCode;

        if (packageName == null) {
            packageName = context.getPackageName();
        }

        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    packageName, 0);
            versionCode = Integer.toString(packageInfo.versionCode);
        } catch (Exception e) {
            Log.w("Failed ", e);
            versionCode = "";
        }

        return versionCode;
    }

    // -------------------------------------------------//
    // --------------Open search in WebBrowser--------------------//
    // -------------------------------------------------//

    // -------------------------------------------------//
    // --------------get SdkVersion---------------------//
    // -------------------------------------------------//
    public static int getSdkVersion() {
        try {
            return Build.VERSION.class.getField("SDK_INT").getInt(null);
        } catch (Exception e) {
            return 3;
        }
    }

    // -------------------------------------------------//
    // --------------Open Email Intent------------------//
    // -------------------------------------------------//

    // -------------------------------------------------//
    // --------check is app run on emulator-------------//
    // -------------------------------------------------//
    public static boolean isEmulator() {
        return Build.MODEL.equals("sdk") || Build.MODEL.equals("google_sdk");
    }

    public static float dpToPx(float dp) {
        if (ctx == null) {
            return -1;
        }
        return dp * ctx.getResources().getDisplayMetrics().density;
    }

    // -------------------------------------------------//
    // --------------get app Name-----------------------//
    // -------------------------------------------------//

    // -------------------------------------------------//
    // --------convert pixcel to dp --------------------//
    // -------------------------------------------------//
    public static float pxToDp(float px) {
        if (ctx == null) {
            return -1;
        }
        return px / ctx.getResources().getDisplayMetrics().density;
    }

    // -------------------------------------------------//
    // --------convert dp to pixcel integer-------------//
    // -------------------------------------------------//
    public static float dpToPxInt(Context context, float dp) {
        return (int) (dpToPx(dp) + 0.5f);
    }

    // -------------------------------------------------//
    // --------convert pixcel to DpCeilInt--------------//
    // -------------------------------------------------//
    public static float pxToDpCeilInt(float px) {
        return (int) (pxToDp(px) + 0.5f);
    }

    public static String geFileFromAssets(String fileName) {
        if (ctx == null || isEmpty(fileName)) {
            return null;
        }

        StringBuilder s = new StringBuilder("");
        try {
            InputStreamReader in = new InputStreamReader(ctx.getResources()
                    .getAssets().open(fileName));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // -------------------------------------------------//
    // --------------get app VersionCode----------------//
    // -------------------------------------------------//

    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }

    public static String geFileFromRaw(int resId) {
        if (ctx == null) {
            return null;
        }

        StringBuilder s = new StringBuilder();
        try {
            InputStreamReader in = new InputStreamReader(ctx.getResources()
                    .openRawResource(resId));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> geFileToListFromAssets(String fileName) {
        if (ctx == null || isEmpty(fileName)) {
            return null;
        }

        List<String> fileContent = new ArrayList<String>();
        try {
            InputStreamReader in = new InputStreamReader(ctx.getResources()
                    .getAssets().open(fileName));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                fileContent.add(line);
            }
            br.close();
            return fileContent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // -------------------------------------------------//
    // --------get list of file from raw--------------//
    // -------------------------------------------------//
    public static List<String> geFileToListFromRaw(int resId) {
        if (ctx == null) {
            return null;
        }

        List<String> fileContent = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            InputStreamReader in = new InputStreamReader(ctx.getResources()
                    .openRawResource(resId));
            reader = new BufferedReader(in);
            String line = null;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // -------------------------------------------------//
    // --------convert dp to pixcel---------------------//
    // -------------------------------------------------//

    public static String getRandomNumbersAndLetters(int length) {
        return getRandom(NUMBERS_AND_LETTERS, length);
    }

    // -------------------------------------------------//
    // --------------get random number only-------------//
    // -------------------------------------------------//
    public static String getRandomNumbers(int length) {
        return getRandom(NUMBERS, length);
    }

    // -------------------------------------------------//
    // --------------get random letter only-------------//
    // -------------------------------------------------//
    public static String getRandomLetters(int length) {
        return getRandom(LETTERS, length);
    }

    public static String getRandomCapitalLetters(int length) {
        return getRandom(CAPITAL_LETTERS, length);
    }

    // -------------------------------------------------//
    // --------geFileFromAssets-------------------------//
    // -------------------------------------------------//

    public static String getRandomLowerCaseLetters(int length) {
        return getRandom(LOWER_CASE_LETTERS, length);
    }

    public static String getRandom(String source, int length) {
        return isEmpty(source) ? null : getRandom(source.toCharArray(), length);
    }

    // -------------------------------------------------//
    // --------geFileFromRaw-------------------------//
    // -------------------------------------------------//

    public static String getRandom(char[] sourceChar, int length) {
        if (sourceChar == null || sourceChar.length == 0 || length < 0) {
            return null;
        }

        StringBuilder str = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            str.append(sourceChar[random.nextInt(sourceChar.length)]);
        }
        return str.toString();
    }

    // -------------------------------------------------//
    // --------get list of file from asset--------------//
    // -------------------------------------------------//

    public static int getRandom(int max) {
        return getRandom(0, max);
    }

    public static int getRandom(int min, int max) {
        if (min > max) {
            return 0;
        }
        if (min == max) {
            return min;
        }
        return min + new Random().nextInt(max - min);
    }

    // -------------------------------------------------//
    // ---get random number and letter combination------//
    // -------------------------------------------------//

    public static boolean shuffle(Object[] objArray) {
        if (objArray == null) {
            return false;
        }

        return shuffle(objArray, getRandom(objArray.length));
    }

    public static boolean shuffle(Object[] objArray, int shuffleCount) {
        int length;
        if (objArray == null || shuffleCount < 0
                || (length = objArray.length) < shuffleCount) {
            return false;
        }

        for (int i = 1; i <= shuffleCount; i++) {
            int random = getRandom(length - i);
            Object temp = objArray[length - i];
            objArray[length - i] = objArray[random];
            objArray[random] = temp;
        }
        return true;
    }

    public static int[] shuffle(int[] intArray) {
        if (intArray == null) {
            return null;
        }

        return shuffle(intArray, getRandom(intArray.length));
    }

    // -------------------------------------------------//
    // --------------get random capital letter----------//
    // -------------------------------------------------//

    public static int[] shuffle(int[] intArray, int shuffleCount) {
        int length;
        if (intArray == null || shuffleCount < 0
                || (length = intArray.length) < shuffleCount) {
            return null;
        }

        int[] out = new int[shuffleCount];
        for (int i = 1; i <= shuffleCount; i++) {
            int random = getRandom(length - i);
            out[i - 1] = intArray[random];
            int temp = intArray[length - i];
            intArray[length - i] = intArray[random];
            intArray[random] = temp;
        }
        return out;
    }

    // -------------------------------------------------//
    // --------------get random lowecase letter----------//
    // -------------------------------------------------//

    public static String MD5(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes());
            return new String(encodeHex(messageDigest.digest()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static char[] encodeHex(final byte[] data) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return out;
    }

    public static boolean isApplicationInBackground() {
        ActivityManager am = (ActivityManager) ctx
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            if (topActivity != null
                    && !topActivity.getPackageName().equals(
                    ctx.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    // -------------------------------------------------//
    // -----------Round Double Value--------------------//
    // -------------------------------------------------//
    public static String round(double value, int places) {
        double a = value;
        BigDecimal bd = new BigDecimal(a);
        BigDecimal res = bd.setScale(places, RoundingMode.DOWN);
        System.out.println("" + res.toPlainString());
        return res.toPlainString();
    }

    // -------------------------------------------------//
    // -----------DoubleToInt--------------------//
    // -------------------------------------------------//
    public static String DoubleToInt(String value) {
        double a = Double.valueOf(value);
        BigDecimal bd = new BigDecimal(a);

        System.out.println("" + bd.intValue());
        return "" + bd.intValue();
    }

    // -------------------------------------------------//
    // -----------GetPriceInFormat--------------------//
    // -------------------------------------------------//
    public static String ConvertPriceInFormat(String value) {
        double a = Double.valueOf(value);
        BigDecimal bd = new BigDecimal(a);

        System.out.println("" + bd.intValue());
        return "$" + bd.intValue();
    }

    // -------------------------------------------------//
    // -----------Show Custome Dialog-------------------//
    // -------------------------------------------------//
    public static void ShowAlert(Context ctx, String msg, String title) {
        try {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    ctx);

            // set title
            alertDialogBuilder.setTitle(title);

            // set dialog message
            alertDialogBuilder
                    .setMessage("" + msg)
                    .setCancelable(false)
                    .setPositiveButton(ctx.getResources().getString(R.string.text_ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String GetDeviceID() {
        // TODO Auto-generated method stub
        String id = Settings.Secure.getString(ctx.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return id;
    }

    public static boolean isActivityRunning(Context context, String actvity_name) {

        boolean isActivityFound;

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> activitys = activityManager.getRunningTasks(Integer.MAX_VALUE);
        isActivityFound = false;
        for (int i = 0; i < activitys.size(); i++) {
            if (activitys.get(i).topActivity.toString().equalsIgnoreCase("ComponentInfo{com.sikkaa/com.sikkaa." + actvity_name + "}")) {
                isActivityFound = true;
            }
        }
        return isActivityFound;
    }

    // -------------------------------------------------//
    // --------------encode string with MD5-------------//
    // -------------------------------------------------//

    // -------------------------------------------------//
    // -----------isAppInForeground---------------------//
    // -------------------------------------------------//
    public static boolean isForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskInfo = am.getRunningTasks(1);

        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
        return componentInfo.getPackageName().equals("com.sikkaa");
    }

    public static void setTotalHeightofListView(ListView listView, Context ctx) {

        ListAdapter mAdapter = listView.getAdapter();

        int totalHeight = 0;

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);

            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),

                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            totalHeight += mView.getMeasuredHeight();
            Log.w("HEIGHT" + i, String.valueOf(totalHeight));

        }


        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();


    }

    // -------------------------------------------------//
    // -----------isApplicationInBackground-------------//
    // -------------------------------------------------//

    public static void ClearAllPreferences() {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();

    }

    // -------------------------------------------------//
    // -----------GetDistance------------//
    // -------------------------------------------------//
    public static double GetDistance(double lat1, double lon1, double lat2,
                                     double lon2) {
        // TODO Auto-generated method stub
        android.location.Location locationA = new android.location.Location("a");
        locationA.setLatitude(lat1);
        locationA.setLongitude(lon1);
        android.location.Location LocationB = new android.location.Location("b");
        LocationB.setLatitude(lat2);
        LocationB.setLongitude(lon2);
        // return in mi
        // return locationA.distanceTo(LocationB) * 0.000621371;
        // ---------------------
        // return in mitter
        return locationA.distanceTo(LocationB);
    }

    public static double RoundDouble(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static boolean ISappInstalledOrNot(String uri) {
        PackageManager pm = ctx.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public static String GetCurrentTimeStamp() {
        Long tsLong = System.currentTimeMillis();
        String ts = tsLong.toString();
        return ts;
    }

    public static String GetDefaultTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        try {
            return "" + tz.getID();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public static String GetCurrentNanoTime() {
        Long tsLong = System.nanoTime();
        String ts = tsLong.toString();
        return ts;
    }

    public static String GetDateFromTimeStamp(String milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(milliSeconds));
        return formatter.format(calendar.getTime());
    }

    public static double roundDouble(double Rval, int numberOfDigitsAfterDecimal) {
        double p = (float) Math.pow(10, numberOfDigitsAfterDecimal);
        Rval = Rval * p;
        double tmp = Math.floor(Rval);
        System.out.println("~~~~~~tmp~~~~~" + tmp);
        return tmp / p;
    }


    // -------------------------------------------------//
    // -----------GetDeviceID---------------------------//
    // -------------------------------------------------//

    public static void showProgressDialog(Context context) {
        pDialog = new ProgressDialog(context);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);
//        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        pDialog.setMessage(context.getResources().getString(R.string.txt_loading));
        pDialog.show();
    }

    // -------------------------------------------------//
    // -----------isActivityRunning---------------------//
    // -------------------------------------------------//

    public static void dismissProgressDialog() {
        if (pDialog != null) {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }

    public static void stopRefreshing(SwipeRefreshLayout mSwipeContainer) {
        if (mSwipeContainer != null) {
            if (mSwipeContainer.isRefreshing())
                mSwipeContainer.setRefreshing(false);
        }
    }
// -------------------------------------------------//
    // -----------setTotalHeightofListView---------------------//
    // -------------------------------------------------//

    public static boolean isRefreshing(SwipeRefreshLayout mSwipeContainer) {
        boolean isRefreshing = false;
        if (mSwipeContainer != null) {
            if (mSwipeContainer.isRefreshing())
                isRefreshing = true;
        }
        return isRefreshing;
    }


    // -------------------------------------------------//
    // -----------Clear All Preferences-----------------//
    // -------------------------------------------------//

    public static void showSimillorProgressDialog(Context context) {
        pDialogSimilor = new ProgressDialog(context);
        pDialogSimilor.setCanceledOnTouchOutside(false);
        pDialogSimilor.setCancelable(false);
//        pDialogSimilor.setMessage(context.getResources().getString(R.string.txt_loading));
        pDialogSimilor.show();
    }

    public static void dismissSimilorProgressDialog() {
        if (pDialogSimilor != null) {

            if (pDialogSimilor.isShowing())
                pDialogSimilor.dismiss();
        }
    }

    public static void showTournamentDialog(Context context) {
        pDialogTournament = new ProgressDialog(context);
        pDialogTournament.setCanceledOnTouchOutside(false);
        pDialogTournament.setCancelable(false);
//        pDialogTournament.setMessage(context.getResources().getString(R.string.txt_loading));
        pDialogTournament.show();
    }


    // -------------------------------------------------//
    // -----------appInstalledOrNot---------------------//
    // -------------------------------------------------//

    public static void dismissTournamentDialog() {
        if (pDialogTournament != null) {

            if (pDialogTournament.isShowing())
                pDialogTournament.dismiss();
        }
    }

    // -------------------------------------------------//
    // -----------GetCurrentTimeStamp---------------------//
    // -------------------------------------------------//

    public static void StartRefreshing(SwipeRefreshLayout mSwipeContainer) {

        if (mSwipeContainer != null) {
            mSwipeContainer.setRefreshing(true);

        }
    }


    // -------------------------------------------------//
    // -----------GetDefaultTimeZone---------------------//
    // -------------------------------------------------//

    public static void StopRefreshing(SwipeRefreshLayout mSwipeContainer) {
        if (mSwipeContainer != null) {
            if (mSwipeContainer.isRefreshing()) {
                mSwipeContainer.setRefreshing(false);
            }
        }
    }

    // -------------------------------------------------//
    // -----------GetCurrentNanoTime---------------------//
    // -------------------------------------------------//

    public static void startGoogleMapNavigationActivity(Context context, double source_lat, double source_lng, double dest_lat, double dest_lng) {
        double lat2, lng2;


        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?" + "saddr=" + source_lat
                        + "," + source_lat + "&daddr=" + dest_lat + "," + dest_lng));
        intent.setClassName("com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity");
        context.startActivity(intent);

    }

    // -------------------------------------------------//
    // ------------ GetDateFromTimeStamp----------------------//
    // -------------------------------------------------//

    public static boolean CopyTextToClipboard(String text) {
        try {
            int sdk = Build.VERSION.SDK_INT;
            if (sdk < Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(text);
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText(ctx.getResources().getString(R.string.app_name), text);
                clipboard.setPrimaryClip(clip);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // -------------------------------------------------//
    // -----------roundDouble---------------------------//
    // -------------------------------------------------//

    public static boolean checkPermission(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * show Permissions Dialog
     *
     * @param activity
     * @param permissions
     * @param REQUEST_CODE
     */
    public static void showPermissionsDialog(Activity activity, String[] permissions, int REQUEST_CODE) {
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE);
    }

    /**
     * Make Native Call
     *
     * @param activity
     */
    public static void makeNativeCall(Activity activity, String mobileNo) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobileNo));
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        activity.startActivity(intent);
    }

//    public static void getAddressFromLocation(final double latitude, final double longitude,
//                                              final Context context, final Handler handler) {
//        final String TAG = "getAddressFromLocation";
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//                String result = null;
//                Bundle bundle = new Bundle();
//                try {
//                    List<Address> addressList = geocoder.getFromLocation(
//                            latitude, longitude, 1);
//                    if (addressList != null && addressList.size() > 0) {
//                        Address address = addressList.get(0);
//                        StringBuilder sb = new StringBuilder();
//                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
//
//                            if (i == 0) {
//                                if (address.getAddressLine(0) != null) {
//                                    if (!address.getAddressLine(0).equalsIgnoreCase("null")) {
//                                        sb.append(address.getAddressLine(0)).append(" ");
//                                    }
//                                }
//                                break;
//                            }
//                        }
//
//                        if (address.getLocality() != null) {
//                            if (!address.getLocality().equalsIgnoreCase("null")) {
//                                //sb.append(address.getLocality()).append(" ");
//                                bundle.putString(Constants.state, address.getLocality());
//                            }
//                        }
//                        if (address.getPostalCode() != null) {
//                            if (!address.getPostalCode().equalsIgnoreCase("null")) {
//                                //sb.append(address.getPostalCode()).append(" ");
//                                bundle.putString(Constants.postal_code, address.getPostalCode());
//                            }
//                        }
//
//                        if (address.getCountryName() != null) {
//                            if (!address.getCountryName().equalsIgnoreCase("null")) {
//                                //sb.append(address.getCountryName()).append(" ");
//                                bundle.putString(Constants.country, address.getAdminArea());
//                            }
//                        }
//
//                        result = sb.toString();
//                    }
//                } catch (IOException e) {
//                    Log.e(TAG, "Unable connect to Geocoder", e);
//                } finally {
//                    Message message = Message.obtain();
//                    message.setTarget(handler);
//                    if (result != null) {
//                        message.what = 1;
//
//                        bundle.putString(Constants.addressline_1, result);
//
//                        message.setData(bundle);
//                    } else {
//                        message.what = 1;
//                        bundle.putString(Constants.addressline_1, "");
//                        bundle.putString(Constants.postal_code, "");
//                        bundle.putString(Constants.country, "");
//                        bundle.putString(Constants.state, "");
//
//
//                        message.setData(bundle);
//                    }
//                    message.sendToTarget();
//                }
//            }
//        };
//        thread.start();
//
//    }

    /*-----------Service Call is running or not------------------*/
    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    // -------------------------------------------------//
    // --------internet coonection states---------------//
    // -------------------------------------------------//
    public final boolean IsInternetOn() {

        boolean connected = false;
        final ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            connected = true;
        } else if (netInfo != null && netInfo.isConnected()
                && cm.getActiveNetworkInfo().isAvailable()) {
            connected = true;
        } else if (netInfo != null && netInfo.isConnected()) {
            try {
                URL url = new URL("http://www.google.com");
                HttpURLConnection urlc = (HttpURLConnection) url
                        .openConnection();
                urlc.setConnectTimeout(3000);
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                    connected = true;
                }
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    connected = true;
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan
                    connected = true;
                }
            } else {
                // not connected to the internet
                connected = false;
            }

        }
        return connected;

    }

//    public void runTask() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
//        builder.setTitle(ctx.getResources().getString(R.string.app_name));
//        builder.setMessage(ctx.getResources().getString(R.string.text_internet));
//
//
//        AlertDialog dialog = builder.create();
//        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                runTask();
//            }
//        });
//
//        dialog.show();
//
//    }

    public String getKeyHash() {

        PackageInfo info;
        String something = "";
        try {
            info = ctx.getPackageManager().getPackageInfo(Constants.APP_PdfKG,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                something = new String(Base64.encode(md.digest(),
                        Base64.DEFAULT));
                // String something = new
                // String(Base64.encodeBytes(md.digest()));
                System.out.print("hash key : ========= " + something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            // Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            // Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            // Log.e("exception", e.toString());
        }
        return something;
    }

    // -------------------------------------------------//
    // --------IS GPS on states---------------//
    // -------------------------------------------------//
    public boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) ctx
                .getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    // -------------------------------------------------//
    // --------phone number validation-----------------//
    // -------------------------------------------------//
    public boolean IsValidPhoneNumber(String phone) {
        boolean check;
        Pattern p;
        Matcher m;

        String PHONE_STRING = "^[+]?[0-9]{10,13}$";

        p = Pattern.compile(PHONE_STRING);

        m = p.matcher(phone);
        check = m.matches();

        return check;
    }

    // -------------------------------------------------//
    // --------password >6 char validation--------------//
    // -------------------------------------------------//
    @SuppressWarnings("unused")
    private boolean IsValidPassword(String pass) {
        return pass != null && pass.length() > 6;
    }

    // -------------------------------------------------//
    // --------min no of char validation--------------//
    // -------------------------------------------------//
    @SuppressWarnings("unused")
    private boolean IsMinChar(String text, int min) {
        return text != null && text.length() >= min;
    }


// -------------------------------------------------//
    //---------Open gogole map navigation activity---------//
    // -------------------------------------------------//

    // -------------------------------------------------//
    // --------max no of char validation--------------//
    // -------------------------------------------------//
    @SuppressWarnings("unused")
    private boolean IsMaxChar(String text, int max) {
        return text != null && text.length() <= max;
    }


    // -------------------------------------------------//
    //---------Copy text to clipboard---------------//
    // -------------------------------------------------//

    // -------------------------------------------------//
    // --------edittext empty or not validation---------//
    // -------------------------------------------------//
    @SuppressWarnings("unused")
    private boolean IsEmpty(EditText et) {
        return et.getText().toString().equalsIgnoreCase("");
    }

    // -------------------------------------------------//
    // --------getRealPathFromURI----------------------//
    // -------------------------------------------------//
    @SuppressWarnings("deprecation")
    public String getRealPathFromURI(Uri contentUri, Activity activity) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.managedQuery(contentUri, proj, null,
                    null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }

    // -------------------------------------------------//
    // -----------compressImage--------------------------//
    // -------------------------------------------------//
    public Bitmap compressImage(String imagepath) {

        String filePath = imagepath;
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

        // by setting this field as true, the actual bitmap pixels are not
        // loaded in the memory. Just the bounds are loaded. If
        // you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        // max Height and width values of the compressed image is taken as
        // 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        // width and height values are set maintaining the aspect ratio of the
        // image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        // setting inSampleSize value allows to load a scaled down version of
        // the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth,
                actualHeight);

        // inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

        // this options allow android to claim the bitmap memory if it runs low
        // on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            // load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
                    Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
                middleY - bmp.getHeight() / 2, new Paint(
                        Paint.FILTER_BITMAP_FLAG));

        // check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = imagepath;
        try {
            out = new FileOutputStream(filename);

            // write the compressed bitmap at the destination specified by
            // filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return scaledBitmap;

    }

    public int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    // -------------------------------------------------//
    // -----------get getAddressFromLocation------------//
    // -------------------------------------------------//

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = ctx.getContentResolver().query(contentUri, null, null,
                null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public Bitmap RotateImageAfterPick(String sourcepath) {
        int rotate = 0;
        Bitmap mbitmap = compressImage(sourcepath);
        try {
            File imageFile = new File(sourcepath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        Bitmap bitmap = Bitmap.createBitmap(mbitmap, 0, 0, mbitmap.getWidth(),
                mbitmap.getHeight(), matrix, true);
        return bitmap;
    }


}
