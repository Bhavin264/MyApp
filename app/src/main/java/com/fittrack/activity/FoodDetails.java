package com.fittrack.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fittrack.App;
import com.fittrack.Constants.Api;
import com.fittrack.Constants.Constants;
import com.fittrack.Interface.ApiInterface;
import com.fittrack.Model.FoodCategory.GsonFoodDetails;
import com.fittrack.Progressbar.SmoothProgressBar;
import com.fittrack.R;
import com.fittrack.Utility.LogUtil;
import com.fittrack.Utility.MyUtils;
import com.fittrack.Utility.RoundedCornerImages;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodDetails extends AppCompatActivity implements View.OnClickListener {
    private FoodDetails activity;
    /*header View*/
    private TextView tv_title;
    private ImageView img_back;
    private String food_id = "";
    private SmoothProgressBar Progressbar;
    private AdView mAdView;
    private ImageView img_food;
    private TextView tv_food_name, tv_serving_size, tv_calories, tv_total_fat, tv_sat_fat, tv_poly_fat, tv_mono_fat, tv_total_fiber, tv_dietry_fiber,
            tv_sugar, tv_protein, tv_cholesterol, tv_sodium, tv_potassium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        activity = FoodDetails.this;
        HeaderView();
        findViewById();
        OnClickListener();
        getIntentData();
        CallFoodDetailApi();
    }

    /**
     * Get Intent values
     */
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            food_id = intent.getStringExtra(Constants.food_id);
            LogUtil.Print("food_id", "id==>" + food_id);
        }
    }

    /**
     * Header view
     */
    private void HeaderView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.text_Details));
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setVisibility(View.VISIBLE);
        img_back.setOnClickListener(this);
    }

    /**
     * OnClickListener of Views
     */
    private void OnClickListener() {
    }

    /**
     * findViews by Ids
     */
    private void findViewById() {
        img_food = (ImageView) findViewById(R.id.img_food);
        tv_food_name = (TextView) findViewById(R.id.tv_food_name);
        tv_serving_size = (TextView) findViewById(R.id.tv_serving_size);
        tv_calories = (TextView) findViewById(R.id.tv_calories);
        tv_total_fat = (TextView) findViewById(R.id.tv_total_fat);
        tv_sat_fat = (TextView) findViewById(R.id.tv_sat_fat);
        tv_poly_fat = (TextView) findViewById(R.id.tv_poly_fat);
        tv_mono_fat = (TextView) findViewById(R.id.tv_mono_fat);
        tv_total_fiber = (TextView) findViewById(R.id.tv_total_fiber);
        tv_dietry_fiber = (TextView) findViewById(R.id.tv_dietry_fiber);
        tv_sugar = (TextView) findViewById(R.id.tv_sugar);
        tv_protein = (TextView) findViewById(R.id.tv_protein);
        tv_cholesterol = (TextView) findViewById(R.id.tv_cholesterol);
        tv_sodium = (TextView) findViewById(R.id.tv_sodium);
        tv_potassium = (TextView) findViewById(R.id.tv_potassium);
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
     * Call Food Detail Api
     */
    private void CallFoodDetailApi() {

        if (App.Utils.IsInternetOn()) {
            ApiInterface request = Api.retrofit.create(ApiInterface.class);
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.user_id, App.Utils.getString(Constants.user_id));
            params.put(Constants.access_token, App.Utils.getString(Constants.access_token));
            params.put(Constants.lang, Constants.language);
            params.put(Constants.food_id, food_id);
            LogUtil.Print("FoodDetails_params", "" + params);

            Call<GsonFoodDetails> call = request.getFoodDetailsList(params);
            Progressbar.progressiveStart();
            Progressbar.setVisibility(View.VISIBLE);
            Progressbar.setIndeterminate(true);
            call.enqueue(new Callback<GsonFoodDetails>() {
                @Override
                public void onResponse(Call<GsonFoodDetails> call, Response<GsonFoodDetails> response) {
                    Progressbar.progressiveStop();
                    Progressbar.setVisibility(View.GONE);

                    GsonFoodDetails gson = response.body();

                    if (gson.getFlag().equals(1)) {

                        Drawable drawable = activity.getResources().getDrawable(R.drawable.image_placeholder_food_details);
                        int h = drawable.getIntrinsicHeight();
                        int w = drawable.getIntrinsicWidth();

                        if (!gson.getData().getFoodImage().equalsIgnoreCase("")) {
                            Picasso.with(activity).load(gson.getData().getFoodImage())
                                    .resize(w, h)
                                    .centerCrop()
                                    .transform(new RoundedCornerImages())
                                    .placeholder(R.drawable.image_placeholder_food_details)
                                    .error(R.drawable.image_placeholder_food_details)
                                    .into(img_food);
                        } else {
                            img_food.setImageResource(R.drawable.image_placeholder_food_details);
                        }
                        tv_food_name.setText(gson.getData().getFoodTitle());
                        tv_serving_size.setText(gson.getData().getServiceSize());
                        tv_calories.setText(gson.getData().getCalories());
                        tv_total_fat.setText(gson.getData().getTotalFat());
                        tv_sat_fat.setText(gson.getData().getSaturatedFat());
                        tv_poly_fat.setText(gson.getData().getPolyunsaturatedFat());
                        tv_mono_fat.setText(gson.getData().getMonounsaturatedFat());
                        tv_total_fiber.setText(gson.getData().getTotalCarbohydrate());
                        tv_dietry_fiber.setText(gson.getData().getDietaryFiber());
                        tv_sugar.setText(gson.getData().getSugar());
                        tv_protein.setText(gson.getData().getProtein());
                        tv_cholesterol.setText(gson.getData().getCholesterol());
                        tv_sodium.setText(gson.getData().getSodium());
                        tv_potassium.setText(gson.getData().getPotassium());

                    } else {
                        MyUtils.ShowAlert(activity, "" + gson.getMsg(), getResources().getString(R.string.app_name));
                    }
                }

                @Override
                public void onFailure(Call<GsonFoodDetails> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });
        } else {
            MyUtils.ShowAlert(activity, getResources().getString(R.string.text_internet), getResources().getString(R.string.app_name));
        }

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
        }
    }
}
