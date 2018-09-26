package com.fittrack.activity;

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

import com.fittrack.Adapter.CommentAdapter;
import com.fittrack.App;
import com.fittrack.Constants.Api;
import com.fittrack.Constants.Constants;
import com.fittrack.Interface.ApiInterface;
import com.fittrack.Model.Forum.Comment.GsonComment;
import com.fittrack.Model.Forum.ForumDetails.ForumComments;
import com.fittrack.Model.Forum.ForumDetails.GsonForumDetails;
import com.fittrack.Progressbar.SmoothProgressBar;
import com.fittrack.R;
import com.fittrack.Utility.LogUtil;
import com.fittrack.Utility.MyUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForumDetails extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rv_comment;
    private TextView tv_msg;
    private TextView tv_topic, tv_date, tv_description, tv_comment;

    List<ForumComments> commentsList = new ArrayList<>();
    CommentAdapter commentAdapter;
    /*header View*/
    private TextView tv_title;
    private ImageView img_back, img_add;
    private SmoothProgressBar Progressbar;
    String Forum_id = "", Forum_topic = "";
    private ForumDetails activity;

    /*Dialog*/
    private DisplayMetrics metrics;
    private Dialog CustomDialog;
    private EditText et_add_comment;
    private LinearLayout ll_done;
    private ImageView img_cancel;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.RegisterActivityForBugsense(activity);
        setContentView(R.layout.activity_forum_details);

        activity = ForumDetails.this;
        HeaderView();
        findViewById();
        OnClickListener();
        getIntentData();
        CallForumDetailsApi();
        Main();

    }

    /**
     * ForumDetails Api
     */
    private void CallForumDetailsApi() {
        if (App.Utils.IsInternetOn()) {

            ApiInterface request = Api.retrofit.create(ApiInterface.class);
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.forum_id, Forum_id);
            params.put(Constants.user_id, App.Utils.getString(Constants.user_id));
            params.put(Constants.access_token, App.Utils.getString(Constants.access_token));
            params.put(Constants.lang, Constants.language);
            LogUtil.Print("forum_details_params", "" + params);

            Call<GsonForumDetails> call = request.getForumDetails(params);
            Progressbar.progressiveStart();
            Progressbar.setVisibility(View.VISIBLE);
            Progressbar.setIndeterminate(true);
            call.enqueue(new Callback<GsonForumDetails>() {
                @Override
                public void onResponse(Call<GsonForumDetails> call, Response<GsonForumDetails> response) {

                    Progressbar.progressiveStop();
                    Progressbar.setVisibility(View.GONE);
                    GsonForumDetails gson = response.body();

                    if (gson.getFlag().equals(1)) {
                        tv_description.setText(gson.getData().getDescription());
                        tv_date.setText(gson.getData().getDate());
                        tv_topic.setText(gson.getData().getForumTopic());

                        commentsList.addAll(gson.getData().getForumComments());
                        commentAdapter.notifyDataSetChanged();

                    } else {
                        App.Utils.ShowAlert(activity, "" + gson.getMsg(), getResources().getString(R.string.app_name));
                    }
                }

                @Override
                public void onFailure(Call<GsonForumDetails> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });

        } else {
            MyUtils.ShowAlert(activity, getResources().getString(R.string.text_internet), getResources().getString(R.string.app_name));
        }
    }

    /**
     * set adapter
     */
    private void Main() {

        if (commentAdapter == null) {
            commentAdapter = new CommentAdapter(activity, commentsList);
            rv_comment.setAdapter(commentAdapter);
        } else {
            rv_comment.setAdapter(commentAdapter);
            commentAdapter.notifyDataSetChanged();
//            rv_forum_list.scrollToPosition(forumAdapter.getItemCount() - Constants.ITEM_CLICK);
        }
    }

    /**
     * Get Intent values
     */
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            Forum_id = intent.getStringExtra(Constants.forum_id);
            Forum_topic = intent.getStringExtra(Constants.forum_topic);
            LogUtil.Print("Forum_data", "id==>" + Forum_id + "topic==>" + Forum_topic);
        }
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
    }

    /**
     * OnClickListener of Views
     */
    private void OnClickListener() {
        tv_comment.setOnClickListener(this);
    }

    /**
     * findViews by Ids
     */
    private void findViewById() {

        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_topic = (TextView) findViewById(R.id.tv_topic);
        tv_comment = (TextView) findViewById(R.id.tv_comment);
        rv_comment = (RecyclerView) findViewById(R.id.rv_comment);
        rv_comment.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        rv_comment.setItemAnimator(new DefaultItemAnimator());
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

            case R.id.tv_comment:
                AddCommentDialog();
                break;

            case R.id.ll_done:

                if (et_add_comment.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_comment_empty), getResources().getString(R.string.app_name));

                } else {
                    CustomDialog.dismiss();
                    View view = getCurrentFocus();
                    if (view != null) {

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    CallAddCommentApi();
                }
                break;

            case R.id.img_cancel:
                CustomDialog.dismiss();
                break;
        }
    }

    /**
     * Add Comment Api
     */
    private void CallAddCommentApi() {

        if (App.Utils.IsInternetOn()) {

            ApiInterface request = Api.retrofit.create(ApiInterface.class);
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.forum_id, Forum_id);
            params.put(Constants.comment_text, et_add_comment.getText().toString());
            params.put(Constants.user_id, App.Utils.getString(Constants.user_id));
            params.put(Constants.access_token, App.Utils.getString(Constants.access_token));
            params.put(Constants.lang, Constants.language);
            LogUtil.Print("add_comment_params", "" + params);

            Call<GsonComment> call = request.getAddComment(params);
            Progressbar.progressiveStart();
            Progressbar.setVisibility(View.VISIBLE);
            Progressbar.setIndeterminate(true);
            call.enqueue(new Callback<GsonComment>() {
                @Override
                public void onResponse(Call<GsonComment> call, Response<GsonComment> response) {

                    Progressbar.progressiveStop();
                    Progressbar.setVisibility(View.GONE);
                    GsonComment gson = response.body();

                    if (gson.getFlag().equals(1)) {
                        App.Utils.ShowAlert(activity, "" + gson.getMsg(), getResources().getString(R.string.app_name));
                        commentsList.clear();
                        CallForumDetailsApi();
                    } else {
                        App.Utils.ShowAlert(activity, "" + gson.getMsg(), getResources().getString(R.string.app_name));
                    }
                }

                @Override
                public void onFailure(Call<GsonComment> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });
        } else {
            MyUtils.ShowAlert(activity, getResources().getString(R.string.text_internet), getResources().getString(R.string.app_name));
        }
    }

    /**
     * Add WeightSheet Dailog
     */
    private void AddCommentDialog() {
        metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        CustomDialog = new Dialog(activity);
        CustomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        CustomDialog.setCancelable(true);
        CustomDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        CustomDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        CustomDialog.setContentView(R.layout.add_comment);

        et_add_comment = (EditText) CustomDialog.findViewById(R.id.et_add_comment);
        ll_done = (LinearLayout) CustomDialog.findViewById(R.id.ll_done);
        img_cancel = (ImageView) CustomDialog.findViewById(R.id.img_cancel);
        ll_done.setOnClickListener(this);
        img_cancel.setOnClickListener(this);
        CustomDialog.show();
        CustomDialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        CustomDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_dialog_rounded));

    }
}
