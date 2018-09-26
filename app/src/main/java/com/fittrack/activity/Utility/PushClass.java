package com.fittrack.activity.Utility;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fittrack.Fragment.ChatFragment;
import com.fittrack.R;

import org.json.JSONObject;


/**
 * Created by Umesh on 2/11/2017.
 */

public class PushClass implements View.OnClickListener {
    private String msg = "";
    String TAG = "PushClass";
    Context context;
    Dialog CustomDialog;
    JSONObject customdata = new JSONObject();
    JSONObject bundle;
    public static EditText et_DescFeedBack;
    public static RatingBar rbFeedBack;
    private DisplayMetrics metrics;
    private Dialog Pushdialog;
    private ImageView img_close;
    private TextView tv_yes, tv_no, tv_title_text;
    private Intent intent;

    public PushClass(Context context, JSONObject bundle, String msg) {
        this.context = context;
        this.bundle = bundle;
        this.msg = msg;
        ShowDialog(customdata, msg);


    }

    private void ShowDialog(final JSONObject customdata, String msg) {
        metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        Pushdialog = new Dialog(context);
        Pushdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Pushdialog.setCancelable(true);
        Pushdialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Pushdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Pushdialog.setContentView(R.layout.dialog_logout);
        img_close = (ImageView) Pushdialog.findViewById(R.id.img_close);
        tv_title_text = (TextView) Pushdialog.findViewById(R.id.tv_title_text);
        tv_yes = (TextView) Pushdialog.findViewById(R.id.tv_yes);
        tv_no = (TextView) Pushdialog.findViewById(R.id.tv_no);

        String message = msg;
        tv_title_text.setText(message);

        img_close.setOnClickListener(this);
        tv_yes.setOnClickListener(this);
        tv_no.setOnClickListener(this);

        Pushdialog.show();
        Pushdialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        Pushdialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_dialog_rounded));

    }

    private void Fragment_Call(Fragment fragment) {

        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.replace(R.id.fl_main, fragment);
        ft.addToBackStack(fragment.getTag());
        ft.commit();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                if (Pushdialog.isShowing()) {
                    Pushdialog.dismiss();
                }
                break;

            case R.id.tv_yes:
                if (Pushdialog.isShowing()) {
                    Pushdialog.dismiss();
                }
                if (customdata != null) {
                    Fragment fragment = new ChatFragment();
                    Fragment_Call(fragment);
                }
                break;

            case R.id.tv_no:
                if (Pushdialog.isShowing()) {
                    Pushdialog.dismiss();
                }
                break;
        }
    }
}
