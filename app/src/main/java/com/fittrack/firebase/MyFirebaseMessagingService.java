package com.fittrack.firebase;

/**
 * Created by AndroidBash on 20-Aug-16.
 */

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fittrack.App;
import com.fittrack.Constants.Constants;
import com.fittrack.R;
import com.fittrack.Utility.LogUtil;
import com.fittrack.activity.Home;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessageService";
    Bitmap bitmap;
    JSONObject customdata = new JSONObject();
    private static int SPLASH_WAIT = 850;
    Handler handler = new Handler();
    public static int notifyID = 9001;
    private PendingIntent resultPendingIntent;
    private JSONObject jsonObject;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        //

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

//            {custom_data={"push_type":"0","send_from":"1","msg":"test by abc"}, message=Message from admin}
            ActivityManager am = (ActivityManager) MyFirebaseMessagingService.this
                    .getSystemService(Activity.ACTIVITY_SERVICE);
            String packageName = am.getRunningTasks(1).get(0).topActivity
                    .getPackageName();

            if (remoteMessage != null && !App.Utils.getString(Constants.user_id).equals("")) {

                if (packageName.contains(Constants.APP_PdfKG)) {

                    String message = remoteMessage.getData().get(Constants.message);

                    Log.e("packageName!!!", packageName);

                    NotificationCompat.Builder mNotifyBuilder;
                    final NotificationManager mNotificationManager;
                    int defaults = 0;
                    defaults = defaults | Notification.DEFAULT_LIGHTS;
                    defaults = defaults | Notification.DEFAULT_VIBRATE;
                    defaults = defaults | Notification.DEFAULT_SOUND;

                    if (Build.VERSION.SDK_INT >= 21) {

                        mNotifyBuilder = new NotificationCompat.Builder(this)
                                .setContentTitle(getResources().getString(R.string.app_name))
                                .setContentText(message)
                                .setSmallIcon(R.mipmap.ic_launcher_transparent)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                                .setColor(getApplicationContext().getResources().getColor(R.color.color_splash_red));

                    } else {

                        mNotifyBuilder = new NotificationCompat.Builder(this)
                                .setContentTitle(getResources().getString(R.string.app_name))
                                .setContentText(message)
                                .setSmallIcon(R.mipmap.ic_launcher);
                    }

                    mNotifyBuilder.setDefaults(defaults);
                    mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(1, mNotifyBuilder.build());

                    handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mNotificationManager.cancel(1);
                                            }
                                        }
                            , SPLASH_WAIT);

                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(Constants.filterAction);
                    try {
                        jsonObject = new JSONObject(remoteMessage.getData().get("custom_data"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    broadcastIntent.putExtra(Constants.push_message, jsonObject.toString());
                    broadcastIntent.putExtra(Constants.message, remoteMessage.getData().get(Constants.message));
                    sendBroadcast(broadcastIntent);

                } else {
                    sendNotification(remoteMessage);
                }

            } else {

                LogUtil.Print("remoteMessage", "Null");
            }
        }
//        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Title: " + remoteMessage.getNotification().getTitle());
        }

    }


    private void sendNotification(RemoteMessage bundle) {
        try {

            customdata = new JSONObject((bundle.getData().containsKey(Constants.custom_data)) ? bundle.getData().get(Constants.custom_data) : "");

            Intent resultIntent = new Intent(this, Home.class);
            try {
                jsonObject = new JSONObject(bundle.getData().get("custom_data"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            resultIntent.putExtra(Constants.push_message, jsonObject.toString());
            resultIntent.putExtra(Constants.message, bundle.getData().get(Constants.message));

            resultPendingIntent = PendingIntent.getActivity(this, 0,
                    resultIntent, PendingIntent.FLAG_ONE_SHOT);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        NotificationCompat.Builder mNotifyBuilder = null;
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String message = bundle.getData().get(Constants.message);


        if (Build.VERSION.SDK_INT >= 21) {

            mNotifyBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText(message)
                    //.setSmallIcon(R.mipmap.ic_launcher);
                    .setSmallIcon(R.mipmap.ic_launcher_transparent)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setColor(getApplicationContext().getResources().getColor(R.color.color_splash_red));

        } else {

            mNotifyBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_launcher);

        }

        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification

        if (bundle != null) {

            mNotifyBuilder.setContentText(message);
        }

        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification

        notifyID = +1;
        App.Utils.putInt(Constants.notifyID, notifyID);

        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        Random random = new Random();
        int randomNumber = random.nextInt(9999 - 1000) + 1000;
        Log.e("RandomNumber", "" + randomNumber);
        mNotificationManager.notify(randomNumber, mNotifyBuilder.build());

    }
}


