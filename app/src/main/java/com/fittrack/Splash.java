package com.fittrack;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.WindowManager;

import com.fittrack.activity.Home;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Splash extends AppCompatActivity {

    private static final int SPLASH_WAIT = 3000;
    private static final String TAG = "Splash";
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.RegisterActivityForBugsense(Splash.this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        BGThread();
        GetKeyHash();
    }

    /**
     * GetKeyHash
     */
    private void GetKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.fittrack", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                System.out.println(TAG + "KeyHash:" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


    /**
     * splash thread upto 3s
     */
    private void BGThread() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


//                if (App.Utils.getString(Constants.user_id).equalsIgnoreCase("")) {
//                    Intent i = new Intent(Splash.this, Login.class);
//                    startActivity(i);
//                    finish();
//                } else {

                    Intent i = new Intent(Splash.this, Home.class);
                    startActivity(i);
                    finish();

//                }
            }
        }, SPLASH_WAIT);
    }
}
