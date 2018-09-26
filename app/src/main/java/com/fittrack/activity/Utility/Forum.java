package com.fittrack.activity.Utility;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fittrack.Adapter.ForumAdapter;
import com.fittrack.App;
import com.fittrack.Constants.Api;
import com.fittrack.Constants.Constants;
import com.fittrack.Interface.ApiInterface;
import com.fittrack.Interface.RecyclerViewItemClickListener;
import com.fittrack.Model.Forum.DataForum;
import com.fittrack.Model.Forum.GsonAddForum;
import com.fittrack.Model.Forum.GsonForum;
import com.fittrack.Progressbar.SmoothProgressBar;
import com.fittrack.R;
import com.fittrack.Utility.LogUtil;
import com.fittrack.Utility.MyUtils;
import com.fittrack.activity.ForumDetails;
import com.paginate.Paginate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Forum extends AppCompatActivity implements View.OnClickListener, RecyclerViewItemClickListener {

    private Forum activity;
    private RecyclerView rv_forum_list;
    private TextView tv_msg;
    List<DataForum> dataForumList = new ArrayList<>();
    ForumAdapter forumAdapter;

    /*Pagination*/
    String offset = "0";
    private Paginate paginate;
    boolean isLoading = false;

    /*header View*/
    private TextView tv_title;
    private ImageView img_back, img_add;
    private SmoothProgressBar Progressbar;

    /*Dialog*/
    private DisplayMetrics metrics;
    private Dialog CustomDialog;
    private LinearLayout ll_done;
    private EditText et_add_topic, et_add_title, et_descriptiom;
    private ImageView img_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.RegisterActivityForBugsense(activity);
        setContentView(R.layout.activity_forum);

        activity = Forum.this;
        HeaderView();
        findViewById();
        OnClickListener();
        Main();
        setUpPagination();

    }
    /**
     * Header view
     */
    private void HeaderView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.text_forum));
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setVisibility(View.VISIBLE);
        img_back.setOnClickListener(this);
        img_add = (ImageView) findViewById(R.id.img_add);
        img_add.setVisibility(View.VISIBLE);
        img_add.setOnClickListener(this);
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

        rv_forum_list = (RecyclerView) findViewById(R.id.rv_forum_list);
        rv_forum_list.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        rv_forum_list.setItemAnimator(new DefaultItemAnimator());
        tv_msg = (TextView) findViewById(R.id.tv_msg);
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

            case R.id.img_add:
                AddForumDialog();
                break;

            case R.id.ll_done:

                if (et_add_topic.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_topic_empty), getResources().getString(R.string.app_name));

                } else if (et_add_title.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_title_empty), getResources().getString(R.string.app_name));

                } else {

                    CustomDialog.dismiss();
                    View view = getCurrentFocus();
                    if (view != null) {

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    CallAddForumApi();
                }
                break;

            case R.id.img_cancel:
                CustomDialog.dismiss();
                break;

        }
    }


    /**
     * Add Forum Dialog
     */
    private void CallAddForumApi() {
        if (App.Utils.IsInternetOn()) {

            ApiInterface request = Api.retrofit.create(ApiInterface.class);
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.forum_topic, et_add_topic.getText().toString());
            params.put(Constants.forum_title, et_add_title.getText().toString());
            params.put(Constants.description, et_descriptiom.getText().toString());
            params.put(Constants.user_id, App.Utils.getString(Constants.user_id));
            params.put(Constants.access_token, App.Utils.getString(Constants.access_token));
            params.put(Constants.lang, Constants.language);
            LogUtil.Print("add_forum_params", "" + params);

            Call<GsonAddForum> call = request.getAddForum(params);
            Progressbar.progressiveStart();
            Progressbar.setVisibility(View.VISIBLE);
            Progressbar.setIndeterminate(true);
            call.enqueue(new Callback<GsonAddForum>() {
                @Override
                public void onResponse(Call<GsonAddForum> call, Response<GsonAddForum> response) {

                    Progressbar.progressiveStop();
                    Progressbar.setVisibility(View.GONE);
                    GsonAddForum gson = response.body();
                    if (gson.getFlag().equals(1)) {

                        App.Utils.ShowAlert(activity, "" + gson.getMsg(), getResources().getString(R.string.app_name));
                        offset = "0";
                        dataForumList.clear();
                        setUpPagination();

                    } else {
                        App.Utils.ShowAlert(activity, "" + gson.getMsg(), getResources().getString(R.string.app_name));
                    }
                }

                @Override
                public void onFailure(Call<GsonAddForum> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });

        } else {

            MyUtils.ShowAlert(activity, getResources().getString(R.string.text_internet), getResources().getString(R.string.app_name));
        }
    }

    /**
     * Add Forum Dialog
     */
    private void AddForumDialog() {
        metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        CustomDialog = new Dialog(activity);
        CustomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        CustomDialog.setCancelable(true);
        CustomDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        CustomDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        CustomDialog.setContentView(R.layout.add_forum);
        et_add_topic = (EditText) CustomDialog.findViewById(R.id.et_add_topic);
        et_add_title = (EditText) CustomDialog.findViewById(R.id.et_add_title);
        et_descriptiom = (EditText) CustomDialog.findViewById(R.id.et_descriptiom);
        ll_done = (LinearLayout) CustomDialog.findViewById(R.id.ll_done);
        img_cancel = (ImageView) CustomDialog.findViewById(R.id.img_cancel);

        ll_done.setOnClickListener(this);
        img_cancel.setOnClickListener(this);
        CustomDialog.show();
        CustomDialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * set adapter
     */
    private void Main() {

        if (forumAdapter == null) {
            forumAdapter = new ForumAdapter(activity, dataForumList);
            forumAdapter.setOnRecyclerViewItemClickListener(this);
            rv_forum_list.setAdapter(forumAdapter);
        } else {
            rv_forum_list.setAdapter(forumAdapter);
            forumAdapter.setOnRecyclerViewItemClickListener(this);
            forumAdapter.notifyDataSetChanged();
            rv_forum_list.scrollToPosition(forumAdapter.getItemCount() - Constants.ITEM_CLICK);
        }
    }

    /**
     * set pagination to RecyclerView
     */
    private void setUpPagination() {

        if (paginate != null) {

            paginate.unbind();
        }

        paginate = Paginate.with(rv_forum_list, new Paginate.Callbacks() {
            @Override
            public void onLoadMore() {
                isLoading = true;
                CallForumListApi();
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
     * ForumList Api
     */
    private void CallForumListApi() {

        if (App.Utils.IsInternetOn()) {


            ApiInterface request = Api.retrofit.create(ApiInterface.class);

            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.user_id, App.Utils.getString(Constants.user_id));
            params.put(Constants.access_token, App.Utils.getString(Constants.access_token));
            params.put(Constants.lang, Constants.language);
            params.put(Constants.offset, offset);

            LogUtil.Print("ForumList_params", "" + params);

            Call<GsonForum> call = request.getForumList(params);
            Progressbar.progressiveStart();
            Progressbar.setVisibility(View.VISIBLE);
            Progressbar.setIndeterminate(true);
            call.enqueue(new Callback<GsonForum>() {
                @Override
                public void onResponse(Call<GsonForum> call, Response<GsonForum> response) {

                    Progressbar.progressiveStop();
                    Progressbar.setVisibility(View.GONE);

                    GsonForum gson = response.body();

                    if (gson.getFlag().equals(1)) {

                        dataForumList.addAll(gson.getData());

                        LogUtil.Print("offset_Forum==>", "" + gson.getNextOffset());
                        offset = "" + gson.getNextOffset();
                        tv_msg.setVisibility(View.GONE);

                        forumAdapter.notifyDataSetChanged();
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

                        if (dataForumList.size() > 0) {
                            dataForumList.clear();
                            forumAdapter.notifyDataSetChanged();
                        }
                        isLoading = false;
                        if (paginate != null)
                            paginate.setHasMoreDataToLoad(false);
                    }
                }

                @Override
                public void onFailure(Call<GsonForum> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });
        } else {

            MyUtils.ShowAlert(activity, getResources().getString(R.string.text_internet), getResources().getString(R.string.app_name));
        }
    }

    @Override
    public void onItemClick(int position, int flag, View view) {

        Intent i = new Intent(activity, ForumDetails.class);
        i.putExtra(Constants.forum_id, dataForumList.get(position).getForumId());
        i.putExtra(Constants.forum_topic, dataForumList.get(position).getForumTopic());
//        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }
}
