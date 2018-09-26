package com.fittrack.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.fittrack.Adapter.CategoryAdapter;
import com.fittrack.App;
import com.fittrack.Constants.Api;
import com.fittrack.Constants.Constants;
import com.fittrack.Interface.ApiInterface;
import com.fittrack.Interface.RecyclerViewItemClickListener;
import com.fittrack.Model.FoodCategory.DataFoodCategory;
import com.fittrack.Model.FoodCategory.GsonFoodCategory;
import com.fittrack.Progressbar.SmoothProgressBar;
import com.fittrack.R;
import com.fittrack.Utility.LogUtil;
import com.fittrack.Utility.MyUtils;
import com.fittrack.activity.DietFoodCategory;
import com.fittrack.activity.Home;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Umesh on 9/9/2017.
 */
public class FoodFragment extends Fragment implements View.OnClickListener, RecyclerViewItemClickListener {
    /*header View*/
    private TextView tv_title;
    private ImageView img_back, img_drawer;
    private SmoothProgressBar Progressbar;
    private View view;
    private AdView mAdView;
    private RecyclerView rv_food_list;
    private TextView tv_msg;
    List<DataFoodCategory> foodCategoryList = new ArrayList<>();
    /*Adapter*/
    CategoryAdapter categoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_food, container, false);
        HeaderView(rootView);
        findViewById(rootView);
        OnClickListener();
        CallFoodCategoryListApi();
        Main();
        return rootView;
    }

    /**
     * set adapter
     */
    private void Main() {

        if (categoryAdapter == null) {
            categoryAdapter = new CategoryAdapter(getActivity(), foodCategoryList);
            categoryAdapter.setOnRecyclerViewItemClickListener(this);
            rv_food_list.setAdapter(categoryAdapter);
        } else {
            rv_food_list.setAdapter(categoryAdapter);
            categoryAdapter.setOnRecyclerViewItemClickListener(this);
            categoryAdapter.notifyDataSetChanged();
            rv_food_list.scrollToPosition(categoryAdapter.getItemCount() - Constants.ITEM_CLICK);
        }
    }

    /**
     * Header view
     *
     * @param rootView
     */
    private void HeaderView(View rootView) {
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.text_food_categories));
        img_drawer = (ImageView) rootView.findViewById(R.id.img_drawer);
        img_drawer.setVisibility(View.VISIBLE);
        img_drawer.setOnClickListener(this);
    }

    /**
     * OnClickListener of Views
     */
    private void OnClickListener() {
    }

    /**
     * findViews by Ids
     *
     * @param rootView
     */
    private void findViewById(View rootView) {
        rv_food_list = (RecyclerView) rootView.findViewById(R.id.rv_food_list);
        rv_food_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_food_list.setItemAnimator(new DefaultItemAnimator());
        tv_msg = (TextView) rootView.findViewById(R.id.tv_msg);
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

    /**
     * Call FoodCategoryList Api
     */
    private void CallFoodCategoryListApi() {

        if (App.Utils.IsInternetOn()) {

            ApiInterface request = Api.retrofit.create(ApiInterface.class);
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.user_id, App.Utils.getString(Constants.user_id));
            params.put(Constants.access_token, App.Utils.getString(Constants.access_token));
            params.put(Constants.lang, Constants.language);

            LogUtil.Print("FoodCategoryList_params", "" + params);

            Call<GsonFoodCategory> call = request.getFoodCategoryList(params);
            Progressbar.progressiveStart();
            Progressbar.setVisibility(View.VISIBLE);
            Progressbar.setIndeterminate(true);
            call.enqueue(new Callback<GsonFoodCategory>() {
                @Override
                public void onResponse(Call<GsonFoodCategory> call, Response<GsonFoodCategory> response) {

                    Progressbar.progressiveStop();
                    Progressbar.setVisibility(View.GONE);
                    GsonFoodCategory gson = response.body();

                    if (gson.getFlag().equals(1)) {
                        foodCategoryList.clear();
                        foodCategoryList.addAll(gson.getData());
                        categoryAdapter.notifyDataSetChanged();

                    } else {
                        tv_msg.setVisibility(View.VISIBLE);
                        tv_msg.setText("" + gson.getMsg());
                    }
                }

                @Override
                public void onFailure(Call<GsonFoodCategory> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });
        } else {

            MyUtils.ShowAlert(getActivity(), getResources().getString(R.string.text_internet), getResources().getString(R.string.app_name));
        }
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


        }
    }

    @Override
    public void onItemClick(int position, int flag, View view) {
        Intent i = new Intent(getActivity(), DietFoodCategory.class);
        i.putExtra(Constants.category_id, foodCategoryList.get(position).getCategoryId());
        startActivity(i);

    }
}

