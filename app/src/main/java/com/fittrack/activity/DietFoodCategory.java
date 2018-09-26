package com.fittrack.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fittrack.Adapter.DietFoodAdapter;
import com.fittrack.App;
import com.fittrack.Constants.Api;
import com.fittrack.Constants.Constants;
import com.fittrack.Interface.ApiInterface;
import com.fittrack.Interface.RecyclerViewItemClickListener;
import com.fittrack.Model.FoodCategory.DataDietFood;
import com.fittrack.Model.FoodCategory.GsonDietFood;
import com.fittrack.Progressbar.SmoothProgressBar;
import com.fittrack.R;
import com.fittrack.Utility.LogUtil;
import com.fittrack.Utility.MyUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.paginate.Paginate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DietFoodCategory extends AppCompatActivity implements View.OnClickListener, RecyclerViewItemClickListener {

    private DietFoodCategory activity;
    /*header View*/
    private TextView tv_title;
    private ImageView img_back;
    /*Views*/
    private RecyclerView rv_dietfood_list;
    private TextView tv_msg;
    private SmoothProgressBar Progressbar;
    private AdView mAdView;
    /*Adapter*/
    DietFoodAdapter dietFoodAdapter;
    /*Pagination*/
    String offset = "0";
    private Paginate paginate;
    boolean isLoading = false;
    List<DataDietFood> dataDietFoodList = new ArrayList<>();
    String category_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_food_category);

        activity = DietFoodCategory.this;
        HeaderView();
        findViewById();
        OnClickListener();
        getIntentData();
        Main();
        setUpPagination();

    }
    /**
     * Get Intent values
     */
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            category_id = intent.getStringExtra(Constants.category_id);
            LogUtil.Print("DietFood_data", "id==>" + category_id);
        }
    }

    /**
     * Header view
     */
    private void HeaderView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.text_diet_food));
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
        rv_dietfood_list = (RecyclerView) findViewById(R.id.rv_dietfood_list);
        rv_dietfood_list.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        rv_dietfood_list.setItemAnimator(new DefaultItemAnimator());
        tv_msg = (TextView) findViewById(R.id.tv_msg);
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
     * set adapter
     */
    private void Main() {

        if (dietFoodAdapter == null) {
            dietFoodAdapter = new DietFoodAdapter(activity, dataDietFoodList);
            dietFoodAdapter.setOnRecyclerViewItemClickListener(this);
            rv_dietfood_list.setAdapter(dietFoodAdapter);
        } else {
            rv_dietfood_list.setAdapter(dietFoodAdapter);
            dietFoodAdapter.setOnRecyclerViewItemClickListener(this);
            dietFoodAdapter.notifyDataSetChanged();
            rv_dietfood_list.scrollToPosition(dietFoodAdapter.getItemCount() - Constants.ITEM_CLICK);
        }
    }

    /**
     * set pagination to RecyclerView
     */
    private void setUpPagination() {

        if (paginate != null) {

            paginate.unbind();
        }

        paginate = Paginate.with(rv_dietfood_list, new Paginate.Callbacks() {
            @Override
            public void onLoadMore() {
                isLoading = true;
                CallDietFoodCategoryListApi();
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean hasLoadedAllItems() {
                if (offset.equals(Constants.pagination_last_offset))
                    return true;
                else
                    return false;
            }
        }).setLoadingTriggerThreshold(2)
                .addLoadingListItem(true)
                .build();
    }

    /**
     * Call DietFoodCategoryList Api
     */
    private void CallDietFoodCategoryListApi() {
        if (App.Utils.IsInternetOn()) {

            ApiInterface request = Api.retrofit.create(ApiInterface.class);
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.user_id, App.Utils.getString(Constants.user_id));
            params.put(Constants.access_token, App.Utils.getString(Constants.access_token));
            params.put(Constants.lang, Constants.language);
            params.put(Constants.category_id, category_id);
            params.put(Constants.offset, offset);

            LogUtil.Print("DietFoodList_params", "" + params);

            Call<GsonDietFood> call = request.getDietFoodDirectoryList(params);
            Progressbar.progressiveStart();
            Progressbar.setVisibility(View.VISIBLE);
            Progressbar.setIndeterminate(true);
            call.enqueue(new Callback<GsonDietFood>() {
                @Override
                public void onResponse(Call<GsonDietFood> call, Response<GsonDietFood> response) {
                    Progressbar.progressiveStop();
                    Progressbar.setVisibility(View.GONE);

                    GsonDietFood gson = response.body();
                    if (gson.getFlag().equals(1)) {

                        dataDietFoodList.addAll(gson.getData());
                        LogUtil.Print("offset_DietFood==>", "" + gson.getNextOffset());
                        offset = "" + gson.getNextOffset();
                        tv_msg.setVisibility(View.GONE);

                        dietFoodAdapter.notifyDataSetChanged();
                        isLoading = false;
                        if (paginate != null) {
                            if (offset.equals(Constants.pagination_last_offset))
                                paginate.setHasMoreDataToLoad(false);
                            else
                                paginate.setHasMoreDataToLoad(true);
                        }
                    } else {
                        tv_msg.setVisibility(View.VISIBLE);
                        tv_msg.setText("" + gson.getMsg());

                        if (dataDietFoodList.size() > 0) {
                            dataDietFoodList.clear();
                            dietFoodAdapter.notifyDataSetChanged();
                        }
                        isLoading = false;
                        if (paginate != null)
                            paginate.setHasMoreDataToLoad(false);
                    }
                }

                @Override
                public void onFailure(Call<GsonDietFood> call, Throwable t) {
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

    @Override
    public void onItemClick(int position, int flag, View view) {
        Intent i = new Intent(activity, FoodDetails.class);
        i.putExtra(Constants.food_id, dataDietFoodList.get(position).getFoodId());
        startActivity(i);
    }
}
