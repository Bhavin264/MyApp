package com.fittrack.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fittrack.Adapter.ComparePhotoAdapter;
import com.fittrack.App;
import com.fittrack.Constants.Api;
import com.fittrack.Constants.Constants;
import com.fittrack.Interface.ApiInterface;
import com.fittrack.Interface.RecyclerViewItemClickListener;
import com.fittrack.Model.CompareClg.DataCompareList;
import com.fittrack.Model.CompareClg.GsonCompare;
import com.fittrack.Model.CompareClg.GsonCompareList;
import com.fittrack.Progressbar.SmoothProgressBar;
import com.fittrack.R;
import com.fittrack.Utility.Crop.CropActivity;
import com.fittrack.Utility.LogUtil;
import com.fittrack.Utility.MyUtils;
import com.fittrack.activity.ComparePhotoShare;
import com.fittrack.activity.Home;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.naver.android.helloyako.imagecrop.util.BitmapLoadUtils;
import com.paginate.Paginate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Umesh on 8/17/2017.
 */
public class MakeCompareClgFragment extends Fragment implements View.OnClickListener, RecyclerViewItemClickListener {

    private static final String TAG = "MakeCompareClgFragment";
    private String MakeCompareClgFragment = "MakeCompareClg";

    /*permissions*/
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions = new String[]{
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    private File file1, file2, file_camera, file_output;
    String Image_path1 = "", Image_path2 = "";

    /*header View*/
    private TextView tv_title;
    private ImageView img_drawer, img_share;
    private ImageView img_photo1, img_photo2;
    private TextView tv_date1, tv_date2, tv_weight1, tv_weight2, tv_congrats, tv_perfect, tv_msg, tv_compare_photo;
    /*Pagination*/
    String offset = "0";
    private Paginate paginate;
    boolean isLoading = false;
    private SmoothProgressBar Progressbar;

    /*Adapter*/
    ComparePhotoAdapter comparePhotoAdapter;
    /*Dialog*/
    private DisplayMetrics metrics;
    private Dialog CustomDialog, AddPhotoDialog;
    private EditText et_w1, et_w2;
    private LinearLayout ll_done;
    private TextView tv_d1, tv_d2;
    private ImageView img1, img2, img_camera, img_gallery, img_cancel, img_close;
    private int mYear1, mMonth1, mDay1, mYear2, mMonth2, mDay2;
    private MultipartBody.Part fileToUpload1, fileToUpload2;
    private RelativeLayout rl_bg1, rl_bg2;
    private LinearLayout ll_bg1, ll_bg2;
    private RecyclerView rv_compare_photo_list;
    List<DataCompareList> dataCompareList = new ArrayList<>();
    private View view;
    private File FilePath;
    private AdView mAdView;
    private GsonCompareList gson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_make_compare_clg, container, false);
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
        if (comparePhotoAdapter == null) {
            comparePhotoAdapter = new ComparePhotoAdapter(getActivity(), dataCompareList);
            comparePhotoAdapter.setOnRecyclerViewItemClickListener(this);
            rv_compare_photo_list.setAdapter(comparePhotoAdapter);
        } else {
            rv_compare_photo_list.setAdapter(comparePhotoAdapter);
            comparePhotoAdapter.notifyDataSetChanged();
            rv_compare_photo_list.scrollToPosition(comparePhotoAdapter.getItemCount() - Constants.ITEM_CLICK);
        }
    }

    /**
     * set pagination to RecyclerView
     */
    private void setUpPagination() {

        if (paginate != null) {

            paginate.unbind();
        }

        paginate = Paginate.with(rv_compare_photo_list, new Paginate.Callbacks() {
            @Override
            public void onLoadMore() {
                isLoading = true;
                CallComparePhotoListApi();
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
     * ComparePhotoList Api
     */
    private void CallComparePhotoListApi() {

        if (App.Utils.IsInternetOn()) {


            ApiInterface request = Api.retrofit.create(ApiInterface.class);
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.user_id, App.Utils.getString(Constants.user_id));
            params.put(Constants.access_token, App.Utils.getString(Constants.access_token));
            params.put(Constants.lang, Constants.language);
            params.put(Constants.offset, offset);

            LogUtil.Print("PhotoList_params", "" + params);

            Call<GsonCompareList> call = request.getComparePhotoList(params);
            Progressbar.progressiveStart();
            Progressbar.setVisibility(View.VISIBLE);
            Progressbar.setIndeterminate(true);
            call.enqueue(new Callback<GsonCompareList>() {
                @Override
                public void onResponse(Call<GsonCompareList> call, Response<GsonCompareList> response) {
                    Log.d("response", response.toString());

                    Progressbar.progressiveStop();
                    Progressbar.setVisibility(View.GONE);
                    gson = response.body();

                    if (gson.getFlag().equals(1)) {

                        dataCompareList.addAll(gson.getData());
                        LogUtil.Print("offset_compare_photo==>", "" + gson.getNextOffset());
                        offset = "" + gson.getNextOffset();
                        tv_msg.setVisibility(View.GONE);
                        comparePhotoAdapter.notifyDataSetChanged();

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

                        if (dataCompareList.size() > 0) {
                            dataCompareList.clear();
                            comparePhotoAdapter.notifyDataSetChanged();
                        }
                        isLoading = false;
                        if (paginate != null)
                            paginate.setHasMoreDataToLoad(false);
                    }
                }

                @Override
                public void onFailure(Call<GsonCompareList> call, Throwable t) {
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
        tv_title.setText(getResources().getString(R.string.text_make_compare));
        img_drawer = (ImageView) rootView.findViewById(R.id.img_drawer);
        img_drawer.setVisibility(View.VISIBLE);
        img_drawer.setOnClickListener(this);
        img_share = (ImageView) rootView.findViewById(R.id.img_share);
        img_share.setVisibility(View.GONE);
        img_share.setOnClickListener(this);
    }

    /**
     * OnClickListener of Views
     */
    private void OnClickListener() {
        tv_compare_photo.setOnClickListener(this);
    }

    /**
     * findViews by Ids
     *
     * @param rootView
     */
    private void findViewById(View rootView) {
        rv_compare_photo_list = (RecyclerView) rootView.findViewById(R.id.rv_compare_photo_list);
        rv_compare_photo_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_compare_photo_list.setItemAnimator(new DefaultItemAnimator());
        tv_msg = (TextView) rootView.findViewById(R.id.tv_msg);
        Progressbar = (SmoothProgressBar) rootView.findViewById(R.id.google_now);
        tv_compare_photo = (TextView) rootView.findViewById(R.id.tv_compare_photo);
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
            case R.id.tv_compare_photo:
                AddComparePhotoDialog();
                break;

            case R.id.img_cancel:
                AddPhotoDialog.dismiss();
                break;

            case R.id.tv_d1:
                DatePickerDialog1();
                break;

            case R.id.tv_d2:
                DatePickerDialog2();
                break;

            case R.id.img1:
                Gallery_Parmission(1);
                break;

            case R.id.img2:
                Gallery_Parmission(2);
                break;

            case R.id.ll_done:


                if (Image_path1.equalsIgnoreCase("")) {

                    App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_image_empty), getResources().getString(R.string.app_name));

                } else if (Image_path2.equalsIgnoreCase("")) {

                    App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_image_empty), getResources().getString(R.string.app_name));

                } else if (tv_d1.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_date_empty), getResources().getString(R.string.app_name));

                } else if (tv_d2.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_date_empty), getResources().getString(R.string.app_name));

                } else if (et_w1.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_weight_empty), getResources().getString(R.string.app_name));

                } else if (et_w2.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_weight_empty), getResources().getString(R.string.app_name));

                } else {
                    AddPhotoDialog.dismiss();
                    view = getActivity().getCurrentFocus();
                    if (view != null) {

                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    CallComparePhotoAPI();

                }
                break;
            case R.id.img_share:

                break;

        }
    }

    /**
     * ComparePhoto API
     */
    private void CallComparePhotoAPI() {
        if (App.Utils.IsInternetOn()) {

            ApiInterface request = Api.retrofit.create(ApiInterface.class);
//            if (file1 != null && file2 != null) {
//
//                RequestBody requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
//                fileToUpload1 = MultipartBody.Part.createFormData(Constants.user_before_photo, file1.getName(), requestBody1);
//
//                RequestBody requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), file2);
//                fileToUpload2 = MultipartBody.Part.createFormData(Constants.user_after_photo, file2.getName(), requestBody2);
//
//            } else {
//                App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_image_empty), getResources().getString(R.string.app_name));
//            }

            Map<String, RequestBody> params = new HashMap<String, RequestBody>();
            params.put(Constants.before_date, RequestBody.create(MediaType.parse("multipart/form-data"), tv_d1.getText().toString()));
            params.put(Constants.before_weight, RequestBody.create(MediaType.parse("multipart/form-data"), et_w1.getText().toString()));
            params.put(Constants.after_date, RequestBody.create(MediaType.parse("multipart/form-data"), tv_d2.getText().toString()));
            params.put(Constants.after_weight, RequestBody.create(MediaType.parse("multipart/form-data"), et_w2.getText().toString()));

            params.put(Constants.user_id, RequestBody.create(MediaType.parse("multipart/form-data"), App.Utils.getString(Constants.user_id)));
            params.put(Constants.access_token, RequestBody.create(MediaType.parse("multipart/form-data"), App.Utils.getString(Constants.access_token)));
            params.put(Constants.lang, RequestBody.create(MediaType.parse("multipart/form-data"), Constants.language));
            LogUtil.Print("compare_photo_params", "" + params);
            if (file1 != null && file2 != null) {
                RequestBody requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
                String key1 = String.format("%1$s\"; filename=\"photo_" + String.valueOf(1) + ".png", Constants.user_before_photo);
                params.put(key1, requestBody1);

                RequestBody requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), file2);
                String key2 = String.format("%1$s\"; filename=\"photo_" + String.valueOf(1) + ".png", Constants.user_after_photo);
                params.put(key2, requestBody2);
            } else {
                App.Utils.ShowAlert(getActivity(), getResources().getString(R.string.text_image_empty), getResources().getString(R.string.app_name));
            }

            Call<GsonCompare> call = request.getComparePhoto(params, fileToUpload1, fileToUpload2);
            Progressbar.progressiveStart();
            Progressbar.setVisibility(View.VISIBLE);
            Progressbar.setIndeterminate(true);
            call.enqueue(new Callback<GsonCompare>() {
                @Override
                public void onResponse(Call<GsonCompare> call, Response<GsonCompare> response) {

                    Progressbar.progressiveStop();
                    Progressbar.setVisibility(View.GONE);
                    GsonCompare gson = response.body();

                    if (gson.getFlag().equals(1)) {
                        App.Utils.ShowAlert(getActivity(), "" + gson.getMsg(), getResources().getString(R.string.app_name));

                        if (dataCompareList.size() > 0) {
                            DataCompareList dataCompare = new DataCompareList();
                            dataCompare.setUserComparePhotoId(gson.getData().get(0).getUserComparePhotoId());
                            dataCompare.setBeforeDate(gson.getData().get(0).getBeforeDate());
                            dataCompare.setBeforeWeight(gson.getData().get(0).getBeforeWeight());
                            dataCompare.setUserBeforePhoto(gson.getData().get(0).getUserBeforePhoto());

                            dataCompare.setAfterDate(gson.getData().get(0).getAfterDate());
                            dataCompare.setAfterWeight(gson.getData().get(0).getAfterWeight());
                            dataCompare.setUserAfterPhoto(gson.getData().get(0).getUserAfterPhoto());
                            dataCompareList.add(0, dataCompare);
                            comparePhotoAdapter.notifyDataSetChanged();
                        } else {
                            dataCompareList.clear();
                            offset = "0";
                            CallComparePhotoListApi();
                        }
//                        dataCompareList.clear();
//                        offset = "0";
//                        setUpPagination();

                    } else {
                        App.Utils.ShowAlert(getActivity(), "" + gson.getMsg(), getResources().getString(R.string.app_name));
                    }
                }

                @Override
                public void onFailure(Call<GsonCompare> call, Throwable t) {
                    Log.d("Error", "error" + t.getMessage());
                }
            });
        } else {
            MyUtils.ShowAlert(getActivity(), getResources().getString(R.string.text_internet), getResources().getString(R.string.app_name));
        }
    }

    /**
     * set runtime permission of gallery and camera
     *
     * @param i
     */
    private void Gallery_Parmission(int i) {

        if (checkPermissions()) {
            Dialog_Camera_Gallery(i);
        }
    }

    /**
     * open Dialog of gallery and camera
     *
     * @param i
     */
    private void Dialog_Camera_Gallery(final int i) {

        metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        CustomDialog = new Dialog(getActivity());
        CustomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        CustomDialog.setCancelable(true);
        CustomDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        CustomDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        CustomDialog.setContentView(R.layout.dialog_camera_gallery);

        TextView tv_title = (TextView) CustomDialog.findViewById(R.id.tv_title);
        img_camera = (ImageView) CustomDialog.findViewById(R.id.img_camera);
        img_gallery = (ImageView) CustomDialog.findViewById(R.id.img_gallery);
        img_close = (ImageView) CustomDialog.findViewById(R.id.img_close);
        img_close.setOnClickListener(this);


        img_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 1) {
                    Intent i1 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i1, Constants.PHOTO_GALLERY);
                    CustomDialog.dismiss();
                } else if (i == 2) {
                    Intent i1 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i1, Constants.PHOTO_GALLERY1);
                    CustomDialog.dismiss();
                }
            }
        });
        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 1) {
                    Intent i2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    file_camera = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    file_output = new File(file_camera, "CameraContentDemo.jpeg");
                    i2.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() +
                            ".provider", file_output));
//                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
                    startActivityForResult(i2, Constants.PHOTO_CAMERA);
                    CustomDialog.dismiss();
                } else if (i == 2) {
                    Intent i2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    file_camera = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    file_output = new File(file_camera, "CameraContentDemo.jpeg");
                    i2.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() +
                            ".provider", file_output));
//                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
                    startActivityForResult(i2, Constants.PHOTO_CAMERA1);
                    CustomDialog.dismiss();
                }
            }
        });

        CustomDialog.show();
        CustomDialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        CustomDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_dialog_rounded));

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("requestCode", "" + requestCode);
/*Gallery pic 1*/
        if (requestCode == Constants.PHOTO_GALLERY) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String filePath = BitmapLoadUtils.getPathFromUri(getActivity(), uri);
                Uri filePathUri = Uri.parse(filePath);

                if (filePathUri != null) {
                    Image_path1 = filePathUri.toString();
                    Log.e("imagepath_gallery1", filePathUri.toString());

                    beginCrop(filePathUri, Constants.RESULT_CROP_GALLERY);
                } else {
                    MyUtils.ShowAlert(getActivity(), getResources().getString(R.string.File_not_Found),
                            getResources().getString(R.string.app_name));
                }
            }

        } else if (requestCode == Constants.RESULT_CROP_GALLERY) {

            if (resultCode == RESULT_OK) {
                if (Image_path1 != null) {
                    Image_path1 = data.getStringExtra(Constants.image_register);
                    Log.e("crop_imagepath_gallery1", Image_path1);
                    Bitmap bitmapImage = BitmapFactory.decodeFile(Image_path1);
                    BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.image_placeholder_compare);
                    Glide.with(getActivity()).load(Image_path1)
                            .error(R.drawable.image_placeholder_compare)
                            .into(img1);
                    file1 = new File(Image_path1);

                } else {
                    MyUtils.ShowAlert(getActivity(), getResources().getString(R.string.File_not_Found),
                            getResources().getString(R.string.app_name));
                }
            }
        }
/*Gallery pic 2*/
        if (requestCode == Constants.PHOTO_GALLERY1) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String filePath = BitmapLoadUtils.getPathFromUri(getActivity(), uri);
                Uri filePathUri = Uri.parse(filePath);

                if (filePathUri != null) {
                    Image_path2 = filePathUri.toString();
                    Log.e("imagepath_gallery2", filePathUri.toString());

                    beginCrop(filePathUri, Constants.RESULT_CROP_GALLERY1);
                } else {
                    MyUtils.ShowAlert(getActivity(), getResources().getString(R.string.File_not_Found),
                            getResources().getString(R.string.app_name));
                }
            }

        } else if (requestCode == Constants.RESULT_CROP_GALLERY1) {

            if (resultCode == RESULT_OK) {
                if (Image_path2 != null) {
                    Image_path2 = data.getStringExtra(Constants.image_register);
                    Log.e("crop_imagepath_gallery2", Image_path2);
                    Bitmap bitmapImage = BitmapFactory.decodeFile(Image_path2);
                    BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.image_placeholder_compare);
                    Glide.with(getActivity()).load(Image_path2)
                            .error(R.drawable.image_placeholder_compare)
                            .into(img2);
                    file2 = new File(Image_path2);

                } else {
                    MyUtils.ShowAlert(getActivity(), getResources().getString(R.string.File_not_Found),
                            getResources().getString(R.string.app_name));
                }
            }
        }
        /*camera pic 1 */
        if (requestCode == Constants.PHOTO_CAMERA) {
            if (resultCode == RESULT_OK) {

                Uri tempUri = Uri.fromFile(file_output);
                Image_path1 = file_camera.getAbsolutePath();
                LogUtil.Print("imagepath_camera", Image_path1);

                if (tempUri != null) {
                    beginCrop(tempUri, Constants.RESULT_CROP_CAMERA);
                } else {
                    MyUtils.ShowAlert(getActivity(), getResources().getString(R.string.File_not_Found),
                            getResources().getString(R.string.app_name));
                }
            }

        } else if (requestCode == Constants.RESULT_CROP_CAMERA
                && resultCode == RESULT_OK) {
            try {
                if (Image_path1 != null) {
                    Image_path1 = data.getStringExtra(Constants.image_register);
                    Log.e("crop_imagepath_camera1", Image_path1);

                    Glide.with(getActivity()).load(Image_path1)
                            .error(R.drawable.image_placeholder_compare)
                            .into(img1);
                    Bitmap bitmapImage = BitmapFactory.decodeFile(Image_path1);
                    file1 = new File(Image_path1);
                } else {
                    MyUtils.ShowAlert(getActivity(), getResources().getString(R.string.File_not_Found),
                            getResources().getString(R.string.app_name));
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                MyUtils.ShowAlert(getActivity(), getResources().getString(R.string.File_not_Found), getResources().getString(R.string.app_name));
            }

        }
/*camera pic 2*/
        if (requestCode == Constants.PHOTO_CAMERA1) {
            if (resultCode == RESULT_OK) {

                Uri tempUri = Uri.fromFile(file_output);
                Image_path2 = file_camera.getAbsolutePath();
                LogUtil.Print("imagepath_camera1", Image_path1);

                if (tempUri != null) {
                    beginCrop(tempUri, Constants.RESULT_CROP_CAMERA1);
                } else {
                    MyUtils.ShowAlert(getActivity(), getResources().getString(R.string.File_not_Found),
                            getResources().getString(R.string.app_name));
                }
            }

        } else if (requestCode == Constants.RESULT_CROP_CAMERA1
                && resultCode == RESULT_OK) {
            try {
                if (Image_path2 != null) {
                    Image_path2 = data.getStringExtra(Constants.image_register);
                    Log.e("crop_imagepath_camera2", Image_path2);

                    Glide.with(getActivity()).load(Image_path2)
                            .error(R.drawable.image_placeholder_compare)
                            .into(img2);
                    Bitmap bitmapImage = BitmapFactory.decodeFile(Image_path2);
                    file2 = new File(Image_path2);
                } else {
                    MyUtils.ShowAlert(getActivity(), getResources().getString(R.string.File_not_Found),
                            getResources().getString(R.string.app_name));
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                MyUtils.ShowAlert(getActivity(), getResources().getString(R.string.File_not_Found), getResources().getString(R.string.app_name));
            }

        }
    }

    /**
     * for beginCrop
     *
     * @param uri
     * @param resultCrop
     */
    private void beginCrop(Uri uri, int resultCrop) {

        File file = null;
        file = new File(Constants.App_ROOT_FOLDER);
        file.mkdirs();

        file = new File(Constants.App_ROOT_FOLDER + "/" + Constants.IMAGE_FILE_NAME_PREFIX.replace("X", MyUtils.GetCurrentNanoTime()));

        System.out.println("file::" + file);
        Uri destination = Uri.fromFile(file);
        // Crop.of(source, destination).asSquare().start(getActivity());
        startCrop(uri, resultCrop);

    }

    /**
     * for startCrop
     *
     * @param imageUri
     * @param resultCrop
     */
    private void startCrop(Uri imageUri, int resultCrop) {

        LogUtil.Print(TAG, "imageUri..." + imageUri);
        Intent intent = new Intent(getActivity(), CropActivity.class);
        intent.setData(imageUri);
        intent.putExtra(Constants.FLAG_IS_SQUARE, false);
        intent.putExtra(Constants.IMAGE_CROP_RATIO_WIDTH, Constants.DEFAULT_PICTURE_CROP_RATIO_WIDTH);
        intent.putExtra(Constants.IMAGE_CROP_RATIO_HEIGHT, Constants.DEFAULT_PICTURE_CROP_RATIO_HEIGHT);
        startActivityForResult(intent, resultCrop);

    }

    /**
     * DatePicker Dialog1
     */
    private void DatePickerDialog1() {
        java.util.Calendar c = java.util.Calendar.getInstance();
        mYear1 = c.get(java.util.Calendar.YEAR);
        mMonth1 = c.get(java.util.Calendar.MONTH);
        mDay1 = c.get(java.util.Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        tv_d1.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear1, mMonth1, mDay1);

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    /**
     * DatePicker Dialog2
     */
    private void DatePickerDialog2() {
        java.util.Calendar c = java.util.Calendar.getInstance();
        mYear2 = c.get(java.util.Calendar.YEAR);
        mMonth2 = c.get(java.util.Calendar.MONTH);
        mDay2 = c.get(java.util.Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        tv_d2.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear2, mMonth2, mDay2);

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    /**
     * AddComparePhoto Dialog
     */
    private void AddComparePhotoDialog() {
        metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        AddPhotoDialog = new Dialog(getActivity());
        AddPhotoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        AddPhotoDialog.setCancelable(true);
        AddPhotoDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        AddPhotoDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        AddPhotoDialog.setContentView(R.layout.compare_photo);

        img1 = (ImageView) AddPhotoDialog.findViewById(R.id.img1);
        et_w1 = (EditText) AddPhotoDialog.findViewById(R.id.et_w1);
        tv_d1 = (TextView) AddPhotoDialog.findViewById(R.id.tv_d1);

        img2 = (ImageView) AddPhotoDialog.findViewById(R.id.img2);
        et_w2 = (EditText) AddPhotoDialog.findViewById(R.id.et_w2);
        tv_d2 = (TextView) AddPhotoDialog.findViewById(R.id.tv_d2);
        ll_done = (LinearLayout) AddPhotoDialog.findViewById(R.id.ll_done);
        img_cancel = (ImageView) AddPhotoDialog.findViewById(R.id.img_cancel);

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        tv_d1.setOnClickListener(this);
        tv_d2.setOnClickListener(this);

        ll_done.setOnClickListener(this);
        img_cancel.setOnClickListener(this);
        AddPhotoDialog.show();
        AddPhotoDialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        AddPhotoDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_dialog_rounded));

    }

    /**
     * checkPermissions of gallery and camera
     */
    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getActivity(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
        public void onItemClick(int position, int flag, View view) {
        
        Intent intent = new Intent(getActivity(), ComparePhotoShare.class);
        intent.putExtra(Constants.Image1, dataCompareList.get(position).getUserBeforePhoto());
        intent.putExtra(Constants.weight1, dataCompareList.get(position).getBeforeWeight());
        intent.putExtra(Constants.date1, dataCompareList.get(position).getBeforeDate());
        intent.putExtra(Constants.Image2, dataCompareList.get(position).getUserAfterPhoto());
        intent.putExtra(Constants.weight2, dataCompareList.get(position).getAfterWeight());
        intent.putExtra(Constants.date2, dataCompareList.get(position).getAfterDate());
        intent.putExtra(Constants.Imagelist , new Gson().toJson(gson));
        intent.putExtra(Constants.position, position);
        startActivity(intent);

    }

    private Bitmap takeScreenshot(View view) {
        view.getRootView();
//        View view = getWindow().getDecorView().getRootView(); // this line is for full sceen sceenshot
        view.setDrawingCacheEnabled(true);
        return view.getDrawingCache();
    }
    /**
     * SaveBitmap in gallery
     */
    private void SaveBitmap(Bitmap bitmap) {
        view.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        /**
         * File imagePath in which image is saved.
         */
        FilePath = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(FilePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("Log", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("Log", e.getMessage(), e);
        }
    }

    /**
     * Share your Chart
     */
    private void ShareChart() {
        Uri uri = Uri.fromFile(FilePath);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        String shareBody = "Share your Chart with your Friends...";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Chart");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MULTIPLE_PERMISSIONS: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    img1.performClick();
//                    img2.performClick();
//
//                    // permissions granted.
//                } else {
//                    // permissions not granted.
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                    builder.setCancelable(false);
//                    builder.setTitle(getResources().getString(R.string.app_name));
//                    builder.setMessage(getResources().getString(R.string.text_save_profile_picture_msg));
//                    builder.setNegativeButton(getResources().getString(R.string.text_skip), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    builder.setPositiveButton(getResources().getString(R.string.text_continue), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Gallery_Parmission(i);
//                            dialog.dismiss();
//                        }
//                    });
//                    AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
//                    dialog.show();
//                }
//                return;
//            }
//        }
//    }

}



