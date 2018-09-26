package com.fittrack.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fittrack.Adapter.WeightSheetAdapter;
import com.fittrack.App;
import com.fittrack.Constants.Api;
import com.fittrack.Constants.Constants;
import com.fittrack.Interface.ApiInterface;
import com.fittrack.Model.WeightSheet.DataWeightDate;
import com.fittrack.Model.WeightSheet.GsonAddWeightSheet;
import com.fittrack.Model.WeightSheet.GsonWeightsheet;
import com.fittrack.Progressbar.SmoothProgressBar;
import com.fittrack.R;
import com.fittrack.Utility.LogUtil;
import com.fittrack.Utility.MyUtils;
import com.fittrack.activity.Home;
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

/**
 * Created by Umesh on 8/17/2017.
 */
public class WeightSheetFragment extends Fragment implements View.OnClickListener {
    private RecyclerView rv_weight_sheet_list;
    private TextView tv_msg,tv_add_weight;
    List<DataWeightDate> dataWeightDates = new ArrayList<>();
    WeightSheetAdapter weightSheetAdapter;

    /*Pagination*/
    String offset = "0";
    private Paginate paginate;
    boolean isLoading = false;

    /*header View*/
    private TextView tv_title;
    private ImageView img_drawer;
    private SmoothProgressBar Progressbar;

    /*Dialog*/
    private DisplayMetrics metrics;
    private Dialog CustomDialog;
    private EditText et_weight;
    private LinearLayout ll_done;
    private TextView tv_date;
    private int mYear, mMonth, mDay;
    private ImageView img_cancel;
    private View view;
    private AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_weight_sheet, container, false);
        HeaderView(rootView);
        findViewById(rootView);
        OnClickListener();
        Main();
        setUpPagination();
        return rootView;
    }



    /**
     * set adapter
     */
    private void Main() {

        if (weightSheetAdapter == null) {
            weightSheetAdapter = new WeightSheetAdapter(getActivity(), dataWeightDates);
            rv_weight_sheet_list.setAdapter(weightSheetAdapter);
        } else {
            rv_weight_sheet_list.setAdapter(weightSheetAdapter);
            weightSheetAdapter.notifyDataSetChanged();
            rv_weight_sheet_list.scrollToPosition(weightSheetAdapter.getItemCount() - Constants.ITEM_CLICK);
        }
    }

    /**
     * set pagination to RecyclerView
     */
    private void setUpPagination() {

        if (paginate != null) {

            paginate.unbind();
        }

        paginate = Paginate.with(rv_weight_sheet_list, new Paginate.Callbacks() {
            @Override
            public void onLoadMore() {
                isLoading = true;
                CallWeightListApi();
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
     * WeightList API
     */
    private void CallWeightListApi() {
        if (App.Utils.IsInternetOn()) {

            ApiInterface request = Api.retrofit.create(ApiInterface.class);
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.user_id, App.Utils.getString(Constants.user_id));
            params.put(Constants.access_token, App.Utils.getString(Constants.access_token));
            params.put(Constants.lang, Constants.language);
            params.put(Constants.offset, offset);

            LogUtil.Print("WeightList_params", "" + params);

            Call<GsonWeightsheet> call = request.getWeightSheetList(params);
            Progressbar.progressiveStart();
            Progressbar.setVisibility(View.VISIBLE);
            Progressbar.setIndeterminate(true);
            call.enqueue(new Callback<GsonWeightsheet>() {
                @Override
                public void onResponse(Call<GsonWeightsheet> call, Response<GsonWeightsheet> response) {
                    Log.d("response", response.toString());

                    Progressbar.progressiveStop();
                    Progressbar.setVisibility(View.GONE);

//                    GsonRegister gson = new GsonBuilder().create().fromJson(response.toString(), GsonRegister.class);
                    GsonWeightsheet gson = response.body();

                    if (gson.getFlag().equals(1)) {

                        dataWeightDates.addAll(gson.getData());

                        LogUtil.Print("offset_weight==>", "" + gson.getNextOffset());

                        offset = "" + gson.getNextOffset();
                        tv_msg.setVisibility(View.GONE);

                        weightSheetAdapter.notifyDataSetChanged();

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

                        if (dataWeightDates.size() > 0) {
                            dataWeightDates.clear();
                            weightSheetAdapter.notifyDataSetChanged();
                        }
                        isLoading = false;
                        if (paginate != null)
                            paginate.setHasMoreDataToLoad(false);
                    }
                }

                @Override
                public void onFailure(Call<GsonWeightsheet> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });
        } else {

            MyUtils.ShowAlert(getActivity(), getResources().getString(R.string.text_internet), getResources().getString(R.string.app_name));
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
        tv_title.setText(getResources().getString(R.string.text_weight_sheet));
        img_drawer = (ImageView) rootView.findViewById(R.id.img_drawer);
        img_drawer.setVisibility(View.VISIBLE);
        img_drawer.setOnClickListener(this);
    }

    /**
     * OnClickListener of Views
     */
    private void OnClickListener() {
        tv_add_weight.setOnClickListener(this);

    }

    /**
     * findViews by Ids
     *
     * @param rootView
     */
    private void findViewById(View rootView) {

        rv_weight_sheet_list = (RecyclerView) rootView.findViewById(R.id.rv_weight_sheet_list);
        rv_weight_sheet_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_weight_sheet_list.setItemAnimator(new DefaultItemAnimator());
        tv_msg = (TextView) rootView.findViewById(R.id.tv_msg);
        Progressbar = (SmoothProgressBar) rootView.findViewById(R.id.google_now);
        tv_add_weight = (TextView) rootView.findViewById(R.id.tv_add_weight);
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

            case R.id.tv_add_weight:
                AddWeightSheetDailog();
                break;


            case R.id.tv_date:
                DatePickerDialog();
                break;

            case R.id.img_cancel:
                CustomDialog.dismiss();
                break;

            case R.id.ll_done:
                if (tv_date.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_date_empty), getResources().getString(R.string.app_name));

                } else if (et_weight.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_weight_empty), getResources().getString(R.string.app_name));

                } else {
                    CustomDialog.dismiss();
                    view = getActivity().getCurrentFocus();
                    if (view != null) {

                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    CallAddWeightSheetApi();
                }
                break;
        }
    }

    /**
     * DatePicker Dialog
     */
    private void DatePickerDialog() {
        java.util.Calendar c = java.util.Calendar.getInstance();
        mYear = c.get(java.util.Calendar.YEAR);
        mMonth = c.get(java.util.Calendar.MONTH);
        mDay = c.get(java.util.Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        tv_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    /**
     * AddWeightSheet Api
     */
    private void CallAddWeightSheetApi() {

        if (App.Utils.IsInternetOn()) {

            ApiInterface request = Api.retrofit.create(ApiInterface.class);
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.weight, et_weight.getText().toString());
            params.put(Constants.date, tv_date.getText().toString());
            params.put(Constants.user_id, App.Utils.getString(Constants.user_id));
            params.put(Constants.access_token, App.Utils.getString(Constants.access_token));
            params.put(Constants.lang, Constants.language);
            LogUtil.Print("add_weightsheet_params", "" + params);

            Call<GsonAddWeightSheet> call = request.getAddWeightSheetList(params);
            Progressbar.progressiveStart();
            Progressbar.setVisibility(View.VISIBLE);
            Progressbar.setIndeterminate(true);
            call.enqueue(new Callback<GsonAddWeightSheet>() {
                @Override
                public void onResponse(Call<GsonAddWeightSheet> call, Response<GsonAddWeightSheet> response) {

                    Progressbar.progressiveStop();
                    Progressbar.setVisibility(View.GONE);
                    GsonAddWeightSheet gson = response.body();
                    if (gson.getFlag().equals(1)) {

                        App.Utils.ShowAlert(getActivity(), "" + gson.getMsg(), getResources().getString(R.string.app_name));
                        offset = "0";
                        dataWeightDates.clear();
                        setUpPagination();

                    } else {
                        App.Utils.ShowAlert(getActivity(), "" + gson.getMsg(), getResources().getString(R.string.app_name));
                    }
                }

                @Override
                public void onFailure(Call<GsonAddWeightSheet> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });
        } else {

            MyUtils.ShowAlert(getActivity(), getResources().getString(R.string.text_internet), getResources().getString(R.string.app_name));
        }
    }

    /**
     * AddWeightSheet Dailog
     */
    private void AddWeightSheetDailog() {
        metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        CustomDialog = new Dialog(getActivity());
        CustomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        CustomDialog.setCancelable(true);
        CustomDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        CustomDialog.setContentView(R.layout.add_weightsheet);
        et_weight = (EditText) CustomDialog.findViewById(R.id.et_weight);
        tv_date = (TextView) CustomDialog.findViewById(R.id.tv_date);
        ll_done = (LinearLayout) CustomDialog.findViewById(R.id.ll_done);
        img_cancel = (ImageView) CustomDialog.findViewById(R.id.img_cancel);
        tv_date.setOnClickListener(this);
        ll_done.setOnClickListener(this);
        img_cancel.setOnClickListener(this);
        CustomDialog.show();
        CustomDialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        CustomDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_dialog_rounded));

    }
}