package com.fittrack.activity.Utility;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fittrack.App;
import com.fittrack.Constants.Api;
import com.fittrack.Constants.Constants;
import com.fittrack.Interface.ApiInterface;
import com.fittrack.Model.Feedback.GsonFeedback;
import com.fittrack.Progressbar.SmoothProgressBar;
import com.fittrack.R;
import com.fittrack.Utility.LogUtil;
import com.fittrack.Utility.MyUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Feedback extends AppCompatActivity implements View.OnClickListener {

    private Feedback activity;
    /*header View*/
    private TextView tv_title;
    private ImageView img_back, img_add;
    private SmoothProgressBar Progressbar;
    private EditText et_description, et_subject;
    private TextView tv_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.RegisterActivityForBugsense(activity);
        setContentView(R.layout.activity_feedback);

        activity = Feedback.this;
        HeaderView();
        findViewById();
        OnClickListener();
    }

    /**
     * Header view
     */
    private void HeaderView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.text_feedback));
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setVisibility(View.VISIBLE);
        img_back.setOnClickListener(this);
    }

    /**
     * OnClickListener of Views
     */
    private void OnClickListener() {
        tv_send.setOnClickListener(this);
    }

    /**
     * findViews by Ids
     */
    private void findViewById() {

        tv_send = (TextView) findViewById(R.id.tv_send);
        et_subject = (EditText) findViewById(R.id.et_subject);
        et_description = (EditText) findViewById(R.id.et_description);
        Progressbar = (SmoothProgressBar) findViewById(R.id.google_now);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_back:
                onBackPressed();
                break;

            case R.id.tv_send:

                if (et_subject.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_subject_empty), getResources().getString(R.string.app_name));

                } else if (et_description.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_descrption_empty), getResources().getString(R.string.app_name));

                } else {

                    View view = getCurrentFocus();

                    if (view != null) {

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    CallFeedbackAPI();
                }

                break;
        }
    }

    /**
     * Feedback Api
     */
    private void CallFeedbackAPI() {
        if (App.Utils.IsInternetOn()) {

            ApiInterface request = Api.retrofit.create(ApiInterface.class);
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.message, et_description.getText().toString());
            params.put(Constants.name, et_subject.getText().toString());
            params.put(Constants.user_id, App.Utils.getString(Constants.user_id));
            params.put(Constants.access_token, App.Utils.getString(Constants.access_token));
            params.put(Constants.lang, Constants.language);
            LogUtil.Print("save_feedback_params", "" + params);

            Call<GsonFeedback> call = request.getFeedback(params);
            Progressbar.progressiveStart();
            Progressbar.setVisibility(View.VISIBLE);
            Progressbar.setIndeterminate(true);
            call.enqueue(new Callback<GsonFeedback>() {
                @Override
                public void onResponse(Call<GsonFeedback> call, Response<GsonFeedback> response) {
                    Progressbar.progressiveStop();
                    Progressbar.setVisibility(View.GONE);

                    GsonFeedback gson = response.body();
                    if (gson.getFlag().equals(1)) {
                        App.Utils.ShowAlert(activity, "" + gson.getMsg(), getResources().getString(R.string.app_name));
                        et_description.setText("");
                        et_subject.setText("");
                    } else {
                        App.Utils.ShowAlert(activity, "" + gson.getMsg(), getResources().getString(R.string.app_name));
                    }
                }
                @Override
                public void onFailure(Call<GsonFeedback> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });

        } else {
            MyUtils.ShowAlert(activity, getResources().getString(R.string.text_internet), getResources().getString(R.string.app_name));
        }

    }
}
