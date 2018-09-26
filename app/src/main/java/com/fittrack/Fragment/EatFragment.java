package com.fittrack.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fittrack.Adapter.FoodEatAdapter;
import com.fittrack.App;
import com.fittrack.Constants.Api;
import com.fittrack.Constants.Constants;
import com.fittrack.Interface.ApiInterface;
import com.fittrack.Model.FoodAvoid.DataFoodEat;
import com.fittrack.Model.FoodAvoid.GsonFoodAvoid;
import com.fittrack.Progressbar.SmoothProgressBar;
import com.fittrack.R;
import com.fittrack.Utility.LogUtil;
import com.fittrack.Utility.MyUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Umesh on 9/13/2017.
 */
public class EatFragment extends Fragment {

    private RecyclerView rv_food_eat_list;
    private TextView tv_msg;
    private SmoothProgressBar Progressbar;
    List<DataFoodEat> FoodEatList = new ArrayList<>();
    /*Adapter*/
    FoodEatAdapter foodEatAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_eat, container, false);
        findViewById(rootView);
        OnClickListener();
        CallFoodAvoidApi();
        Main();
        return rootView;
    }

    /**
     * set adapter
     */
    private void Main() {
        if (foodEatAdapter == null) {
            foodEatAdapter = new FoodEatAdapter(getActivity(), FoodEatList);
            rv_food_eat_list.setAdapter(foodEatAdapter);
        } else {
            rv_food_eat_list.setAdapter(foodEatAdapter);
            foodEatAdapter.notifyDataSetChanged();
            rv_food_eat_list.scrollToPosition(foodEatAdapter.getItemCount() - Constants.ITEM_CLICK);
        }
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
        rv_food_eat_list = (RecyclerView) rootView.findViewById(R.id.rv_food_eat_list);
        rv_food_eat_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_food_eat_list.setItemAnimator(new DefaultItemAnimator());
        tv_msg = (TextView) rootView.findViewById(R.id.tv_msg);
        Progressbar = (SmoothProgressBar) rootView.findViewById(R.id.google_now);
    }

    /**
     * Call FoodAvoid Api
     */
    private void CallFoodAvoidApi() {
        if (App.Utils.IsInternetOn()) {
            ApiInterface request = Api.retrofit.create(ApiInterface.class);
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.user_id, App.Utils.getString(Constants.user_id));
            params.put(Constants.access_token, App.Utils.getString(Constants.access_token));
            params.put(Constants.lang, Constants.language);
            params.put(Constants.food_avoid_id, Constants.login_type_1);

            LogUtil.Print("FoodAvoid_params", "" + params);
            Call<GsonFoodAvoid> call = request.getFoodAvoid(params);
            Progressbar.progressiveStart();
            Progressbar.setVisibility(View.VISIBLE);
            Progressbar.setIndeterminate(true);
            call.enqueue(new Callback<GsonFoodAvoid>() {
                @Override
                public void onResponse(Call<GsonFoodAvoid> call, Response<GsonFoodAvoid> response) {
                    Progressbar.progressiveStop();
                    Progressbar.setVisibility(View.GONE);
                    GsonFoodAvoid gson = response.body();

                    if (gson.getFlag().equals(1)) {

                        FoodEatList.addAll(gson.getData().getDataFoodEat());
                        foodEatAdapter.notifyDataSetChanged();
                    } else {

                        MyUtils.ShowAlert(getActivity(), "" + gson.getMsg(), getResources().getString(R.string.app_name));
                    }
                }

                @Override
                public void onFailure(Call<GsonFoodAvoid> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });
        } else {
            MyUtils.ShowAlert(getActivity(), getResources().getString(R.string.text_internet), getResources().getString(R.string.app_name));
        }

    }


}
