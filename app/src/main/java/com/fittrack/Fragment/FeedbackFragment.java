package com.fittrack.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.fittrack.activity.Home;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Umesh on 8/17/2017.
 */
public class FeedbackFragment extends Fragment implements View.OnClickListener {
    /*header View*/
    private TextView tv_title;
    private ImageView img_back, img_drawer;
    private SmoothProgressBar Progressbar;
    private EditText et_description, et_subject;
    private TextView tv_send;
    private View view;
    private AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_feedback, container, false);
        HeaderView(rootView);
        findViewById(rootView);
        OnClickListener();
        return rootView;
    }

    /**
     * Header view
     *
     * @param rootView
     */
    private void HeaderView(View rootView) {
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.text_feedback));
        img_drawer = (ImageView) rootView.findViewById(R.id.img_drawer);
        img_drawer.setVisibility(View.VISIBLE);
        img_drawer.setOnClickListener(this);
    }

    /**
     * OnClickListener of Views
     */
    private void OnClickListener() {
        tv_send.setOnClickListener(this);
    }

    /**
     * findViews by Ids
     *
     * @param rootView
     */
    private void findViewById(View rootView) {

        tv_send = (TextView) rootView.findViewById(R.id.tv_send);
        et_subject = (EditText) rootView.findViewById(R.id.et_subject);
        et_description = (EditText) rootView.findViewById(R.id.et_description);
        Progressbar = (SmoothProgressBar) rootView.findViewById(R.id.google_now);
        mAdView = (AdView) rootView.findViewById(R.id.adView);
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

            case R.id.tv_send:

                if (et_subject.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_subject_empty), getResources().getString(R.string.app_name));

                } else if (et_description.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_descrption_empty), getResources().getString(R.string.app_name));

                } else {

                    view = getActivity().getCurrentFocus();
                    if (view != null) {

                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
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
                        App.Utils.ShowAlert(getActivity(), "" + gson.getMsg(), getResources().getString(R.string.app_name));
                        et_description.setText("");
                        et_subject.setText("");
                    } else {
                        App.Utils.ShowAlert(getActivity(), "" + gson.getMsg(), getResources().getString(R.string.app_name));
                    }
                }

                @Override
                public void onFailure(Call<GsonFeedback> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });

        } else {
            MyUtils.ShowAlert(getActivity(), getResources().getString(R.string.text_internet), getResources().getString(R.string.app_name));
        }

    }
}

