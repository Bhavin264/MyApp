package com.fittrack.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.fittrack.Adapter.ChatAdapter;
import com.fittrack.App;
import com.fittrack.Constants.Api;
import com.fittrack.Constants.Constants;
import com.fittrack.Interface.ApiInterface;
import com.fittrack.Model.Chat.DataChat;
import com.fittrack.Model.Chat.GsonChat;
import com.fittrack.Model.Chat.GsonSendMsg;
import com.fittrack.Progressbar.SmoothProgressBar;
import com.fittrack.R;
import com.fittrack.Utility.LogUtil;
import com.fittrack.Utility.MyUtils;
import com.fittrack.activity.Home;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.paginate.Paginate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Umesh on 9/9/2017.
 */
public class ChatFragment extends Fragment implements View.OnClickListener {
    /*header View*/
    private TextView tv_title;
    private ImageView img_back, img_drawer;
    private SmoothProgressBar Progressbar;

    private View view;
    private AdView mAdView;
    private RecyclerView rv_chat_list;
    private TextView tv_msg, tv_send;
    private TextView et_msg;

    /*Pagination*/
    String offset = "0";
    private Paginate paginate;
    boolean isLoading = false;
    List<DataChat> dataChats = new ArrayList<>();

    /*Adapter*/
    ChatAdapter chatAdapter;
    String timezoneID = "";
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeView;
//    int counter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        HeaderView(rootView);
        findViewById(rootView);
        OnClickListener();
        getCurrentTimeZone();
        Main();
        setRecyclerViewScrollListener();
        CallChatListApi();
//        setUpPagination();
        return rootView;
    }

    /**
     * get CurrentTimeZone Id
     */
    private void getCurrentTimeZone() {
        timezoneID = TimeZone.getDefault().getID();
        System.out.println(timezoneID);
        LogUtil.Print("timezoneID", "" + timezoneID);
    }
    /**
     * set adapter
     */
    private void Main() {

        if (chatAdapter == null) {
            chatAdapter = new ChatAdapter(getActivity(), dataChats);
            rv_chat_list.setAdapter(chatAdapter);
        } else {
            rv_chat_list.setAdapter(chatAdapter);
            chatAdapter.notifyDataSetChanged();
            rv_chat_list.scrollToPosition(chatAdapter.getItemCount() - Constants.ITEM_CLICK);
        }
    }

    /**
     * detect lis reach at top of list to refresh list
     */
    private void setRecyclerViewScrollListener() {
        rv_chat_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoading) {
                    if (mLayoutManager.findFirstVisibleItemPosition() == 0) {
                        if (!offset.equals("-1")) {
                            swipeView.performClick();
                        }
                    }
                }
            }
        });
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(false);
                if (!isLoading) {
                    if (mLayoutManager.findFirstVisibleItemPosition() == 0) {
                        if (!offset.equals("-1")) {
                            swipeView.setRefreshing(true);
                            isLoading = true;
//                            offset = "0";
                            CallChatListApi();
                        } else {
                            swipeView.setRefreshing(false);
                            isLoading = false;

                        }
                    }
                }
            }
        });
    }

    /**
     * Header view
     *
     * @param rootView
     */
    private void HeaderView(View rootView) {
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.text_chat));
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

        rv_chat_list = (RecyclerView) rootView.findViewById(R.id.rv_chat_list);
        rv_chat_list.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mLayoutManager.setReverseLayout(false);
        mLayoutManager.setStackFromEnd(true);
        rv_chat_list.setLayoutManager(mLayoutManager);
        rv_chat_list.setItemAnimator(new DefaultItemAnimator());
        tv_msg = (TextView) rootView.findViewById(R.id.tv_msg);
        et_msg = (TextView) rootView.findViewById(R.id.et_msg);
        tv_send = (TextView) rootView.findViewById(R.id.tv_send);
        Progressbar = (SmoothProgressBar) rootView.findViewById(R.id.google_now);
        swipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);

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
     * set pagination to RecyclerView
     */
    private void setUpPagination() {

        if (paginate != null) {

            paginate.unbind();
        }

        paginate = Paginate.with(rv_chat_list, new Paginate.Callbacks() {
            @Override
            public void onLoadMore() {
                isLoading = true;
                CallChatListApi();
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
     *
     */
    private void CallChatListApi() {

        if (App.Utils.IsInternetOn()) {

            ApiInterface request = Api.retrofit.create(ApiInterface.class);
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.user_id, App.Utils.getString(Constants.user_id));
            params.put(Constants.access_token, App.Utils.getString(Constants.access_token));
            params.put(Constants.lang, Constants.language);
            params.put(Constants.timezone, timezoneID);
            params.put(Constants.offset, offset);
            LogUtil.Print("ChatList_params", "" + params);

            Call<GsonChat> call = request.getChatList(params);
            if (dataChats.size() == 0) {
                Progressbar.progressiveStart();
                Progressbar.setVisibility(View.VISIBLE);
                Progressbar.setIndeterminate(true);
            } else {
                Progressbar.progressiveStop();
                Progressbar.setVisibility(View.GONE);
            }

            call.enqueue(new Callback<GsonChat>() {
                @Override
                public void onResponse(Call<GsonChat> call, Response<GsonChat> response) {
                    Progressbar.progressiveStop();
                    Progressbar.setVisibility(View.GONE);
                    swipeView.setRefreshing(false);

                    GsonChat gson = response.body();
                    if (gson.getFlag().equals(1)) {

                        if (offset.equals("0")) {
                            dataChats.clear();
                        }
//                        if (offset.equals("0")) {
//                            LogUtil.Print("chat_msgId", gson.getData().get(0).getMessageId());
//                            counter = Integer.parseInt(gson.getData().get(0).getMessageId());
//                        }
                        dataChats.addAll(gson.getData());
                        LogUtil.Print("offset_chat==>", "" + gson.getNextOffset());
                        offset = "" + gson.getNextOffset();
                        final SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FULL_FORMAT, Locale.ENGLISH);
                        Collections.sort(dataChats, new Comparator<DataChat>() {
                            @Override
                            public int compare(DataChat o1, DataChat o2) {
                                return Integer.parseInt(o1.getMessageId()) - Integer.parseInt(o2.getMessageId());
                            }
                        });
                        tv_msg.setVisibility(View.GONE);
                        chatAdapter.notifyDataSetChanged();
                        isLoading = false;

                    } else {
                        tv_msg.setVisibility(View.VISIBLE);
                        tv_msg.setText("" + gson.getMsg());
                        isLoading = false;
                        if (dataChats.size() > 0) {
                            dataChats.clear();
                            chatAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GsonChat> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    isLoading = false;
                    swipeView.setRefreshing(false);
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

            case R.id.tv_send:
                if (et_msg.getText().toString().isEmpty()) {

                    App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_message_empty),
                            getResources().getString(R.string.app_name));
                } else {
//                    LogUtil.Print("chat_count++", "" + counter);
                    ServiceCallForSendMessage();

                }
                break;


        }
    }

    /**
     * Service Call For Send Message
     */
    private void ServiceCallForSendMessage() {

        if (App.Utils.IsInternetOn()) {

            ApiInterface request = Api.retrofit.create(ApiInterface.class);
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.user_id, App.Utils.getString(Constants.user_id));
            params.put(Constants.access_token, App.Utils.getString(Constants.access_token));
            params.put(Constants.lang, Constants.language);
            params.put(Constants.message, et_msg.getText().toString());

            LogUtil.Print("Sendmsg_params", "" + params);

            Call<GsonSendMsg> call = request.getSendMsg(params);
            Progressbar.progressiveStart();
            Progressbar.setVisibility(View.VISIBLE);
            Progressbar.setIndeterminate(true);
            call.enqueue(new Callback<GsonSendMsg>() {
                @Override
                public void onResponse(Call<GsonSendMsg> call, Response<GsonSendMsg> response) {
                    Progressbar.progressiveStop();
                    Progressbar.setVisibility(View.GONE);
                    GsonSendMsg gson = response.body();

                    if (gson.getFlag().equals(1)) {
                        tv_msg.setVisibility(View.GONE);
//                        addSentLastMsg();
//                        LogUtil.Print("chat_counter", "" + counter);
                        DataChat dataMessage = new DataChat();
                        dataMessage.setMessage(gson.getData().getMessage());
                        dataMessage.setMessageId(gson.getData().getMessageId());
                        dataMessage.setSendFrom(gson.getData().getSendFrom());
                        dataMessage.setDateAdded(gson.getData().getDateAdded());
                        dataMessage.setTimeCompare(gson.getData().getTimeCompare());

                        dataChats.add(dataMessage);
                        chatAdapter.notifyDataSetChanged();
                        scrollToBottom();

                        et_msg.setText("");
                        et_msg.setHint(getResources().getString(R.string.text_enter_here));

                    } else {
                        MyUtils.ShowAlert(getActivity(), "" + gson.getMsg(), getResources().getString(R.string.app_name));
                    }
                }

                @Override
                public void onFailure(Call<GsonSendMsg> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });
        } else {
            MyUtils.ShowAlert(getActivity(), getResources().getString(R.string.text_internet), getResources().getString(R.string.app_name));
        }
    }

    /**
     * add Sent Last Msg
     */
    private void addSentLastMsg() {

//        LogUtil.Print("chat_counter", "" + counter);
        DataChat dataMessage = new DataChat();
        dataMessage.setMessage(et_msg.getText().toString().trim());
//        dataMessage.setMessageId("" + counter);
        dataMessage.setSendFrom("0");
        dataChats.add(dataMessage);
        chatAdapter.notifyDataSetChanged();
        scrollToBottom();
    }

    /**
     * scroll list to focus bottom that means latest msg
     */
    public void scrollToBottom() {
        rv_chat_list.scrollToPosition(chatAdapter.getItemCount() - 1);
    }


}

