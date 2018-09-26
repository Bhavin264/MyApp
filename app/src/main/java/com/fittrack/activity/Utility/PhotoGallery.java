package com.fittrack.activity.Utility;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.fittrack.Adapter.PhotoAdapter;
import com.fittrack.App;
import com.fittrack.Constants.Api;
import com.fittrack.Constants.Constants;
import com.fittrack.Interface.ApiInterface;
import com.fittrack.Model.PhotoGallery.DataPhoto;
import com.fittrack.Model.PhotoGallery.GsonAddPhoto;
import com.fittrack.Model.PhotoGallery.GsonPhoto;
import com.fittrack.Progressbar.SmoothProgressBar;
import com.fittrack.R;
import com.fittrack.Utility.Crop.CropActivity;
import com.fittrack.Utility.LogUtil;
import com.fittrack.Utility.MyUtils;
import com.naver.android.helloyako.imagecrop.util.BitmapLoadUtils;
import com.paginate.Paginate;

import java.io.File;
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

public class PhotoGallery extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "PhotoGallery";
    private PhotoGallery activity;
    private RecyclerView rv_photo_list;
    private TextView tv_msg, tv_add_photo;
    List<DataPhoto> dataPhotos = new ArrayList<>();
    PhotoAdapter photoAdapter;

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
    private Dialog CustomDialog, AddPhotoDialog;
    private EditText et_weight;
    private LinearLayout ll_done;
    private TextView tv_date;
    private int mYear, mMonth, mDay;
    private ImageView img_cancel, img_photo, img_camera, img_gallery, img_close;


    /*permissions*/
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions = new String[]{
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    private File file, file_camera, file_output;
    String Image_path = "";
    private MultipartBody.Part fileToUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.RegisterActivityForBugsense(activity);
        setContentView(R.layout.activity_photo_gallery);

        activity = PhotoGallery.this;
        HeaderView();
        findViewById();
        OnClickListener();
        Main();
        setUpPagination();
    }

    /**
     * set adapter
     */
    private void Main() {
        if (photoAdapter == null) {
            photoAdapter = new PhotoAdapter(activity, dataPhotos);
            rv_photo_list.setAdapter(photoAdapter);
        } else {
            rv_photo_list.setAdapter(photoAdapter);
            photoAdapter.notifyDataSetChanged();
            rv_photo_list.scrollToPosition(photoAdapter.getItemCount() - Constants.ITEM_CLICK);
        }
    }

    /**
     * set pagination to RecyclerView
     */
    private void setUpPagination() {

        if (paginate != null) {

            paginate.unbind();
        }

        paginate = Paginate.with(rv_photo_list, new Paginate.Callbacks() {
            @Override
            public void onLoadMore() {
                isLoading = true;
                CallPhotoListApi();
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
     * PhotoList Api
     */
    private void CallPhotoListApi() {
        if (App.Utils.IsInternetOn()) {

            ApiInterface request = Api.retrofit.create(ApiInterface.class);
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.user_id, App.Utils.getString(Constants.user_id));
            params.put(Constants.access_token, App.Utils.getString(Constants.access_token));
            params.put(Constants.lang, Constants.language);
            params.put(Constants.offset, offset);

            LogUtil.Print("PhotoList_params", "" + params);

            Call<GsonPhoto> call = request.getPhotoList(params);
            Progressbar.progressiveStart();
            Progressbar.setVisibility(View.VISIBLE);
            Progressbar.setIndeterminate(true);
            call.enqueue(new Callback<GsonPhoto>() {
                @Override
                public void onResponse(Call<GsonPhoto> call, Response<GsonPhoto> response) {
                    Log.d("response", response.toString());

                    Progressbar.progressiveStop();
                    Progressbar.setVisibility(View.GONE);
                    GsonPhoto gson = response.body();

                    if (gson.getFlag().equals(1)) {
                        dataPhotos.addAll(gson.getData());
                        LogUtil.Print("offset_photo==>", "" + gson.getNextOffset());
                        offset = "" + gson.getNextOffset();
                        tv_msg.setVisibility(View.GONE);
                        photoAdapter.notifyDataSetChanged();

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
                        if (dataPhotos.size() > 0) {
                            dataPhotos.clear();
                            photoAdapter.notifyDataSetChanged();
                        }
                        isLoading = false;
                        if (paginate != null)
                            paginate.setHasMoreDataToLoad(false);
                    }
                }

                @Override
                public void onFailure(Call<GsonPhoto> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });
        } else {

            MyUtils.ShowAlert(activity, getResources().getString(R.string.text_internet), getResources().getString(R.string.app_name));
        }
    }

    /**
     * Header view
     */
    private void HeaderView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.text_photo_gallery));
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
        tv_add_photo.setOnClickListener(this);
    }

    /**
     * findViews by Ids
     */
    private void findViewById() {

        rv_photo_list = (RecyclerView) findViewById(R.id.rv_photo_list);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv_photo_list.setLayoutManager(gridLayoutManager);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        Progressbar = (SmoothProgressBar) findViewById(R.id.google_now);
        tv_add_photo = (TextView) findViewById(R.id.tv_add_photo);

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
                AddPhotoDialog();
                break;

            case R.id.tv_date:
                DatePickerDialog();
                break;

            case R.id.img_cancel:
                AddPhotoDialog.dismiss();
                break;

            case R.id.img_close:
                CustomDialog.dismiss();
                break;

            case R.id.img_photo:
                Gallery_Parmission();
                break;

            case R.id.ll_done:

                if (Image_path.equalsIgnoreCase("")) {

                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_image_empty), getResources().getString(R.string.app_name));

                } else if (tv_date.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_date_empty), getResources().getString(R.string.app_name));

                } else if (et_weight.getText().toString().trim().isEmpty()) {

                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_weight_empty), getResources().getString(R.string.app_name));

                } else {

                    AddPhotoDialog.dismiss();
                    View view = getCurrentFocus();
                    if (view != null) {

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    CallAddPhotoApi();
                }
                break;

            case R.id.img_gallery:
                Intent i1 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i1, Constants.PHOTO_GALLERY);
                CustomDialog.dismiss();
                break;

            case R.id.img_camera:
                Intent i2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file_camera = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                file_output = new File(file_camera, "CameraContentDemo.jpeg");
                i2.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(activity, getApplicationContext().getPackageName() +
                        ".provider", file_output));
//                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
                startActivityForResult(i2, Constants.PHOTO_CAMERA);
                CustomDialog.dismiss();
                break;

        }
    }

    /**
     * AddPhoto Api
     */
    private void CallAddPhotoApi() {
        if (App.Utils.IsInternetOn()) {

            ApiInterface request = Api.retrofit.create(ApiInterface.class);

            Map<String, RequestBody> params = new HashMap<String, RequestBody>();
            params.put(Constants.date, RequestBody.create(MediaType.parse("multipart/form-data"), tv_date.getText().toString()));
            params.put(Constants.weight, RequestBody.create(MediaType.parse("multipart/form-data"), et_weight.getText().toString()));
            params.put(Constants.user_id, RequestBody.create(MediaType.parse("multipart/form-data"), App.Utils.getString(Constants.user_id)));
            params.put(Constants.access_token, RequestBody.create(MediaType.parse("multipart/form-data"), App.Utils.getString(Constants.access_token)));
            params.put(Constants.lang, RequestBody.create(MediaType.parse("multipart/form-data"), Constants.language));
            LogUtil.Print("add_photo_params", "" + params);
            if (file != null) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                String key = String.format("%1$s\"; filename=\"photo_" + String.valueOf(1) + ".png", Constants.user_photo);
                params.put(key, requestBody);
            }

//            if(file!=null) {
//                RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
//                String key = String.format("%1$s\"; filename=\"photo_" + String.valueOf(1) + ".png", Constants.user_photo);
//                params.put(key, requestBody);
//            }

            Call<GsonAddPhoto> call = request.getAddPhoto(params, fileToUpload);
            Progressbar.progressiveStart();
            Progressbar.setVisibility(View.VISIBLE);
            Progressbar.setIndeterminate(true);
            call.enqueue(new Callback<GsonAddPhoto>() {
                @Override
                public void onResponse(Call<GsonAddPhoto> call, Response<GsonAddPhoto> response) {
                    Progressbar.progressiveStop();
                    Progressbar.setVisibility(View.GONE);
                    GsonAddPhoto gson = response.body();

                    if (gson.getFlag().equals(1)) {
                        App.Utils.ShowAlert(activity, "" + gson.getMsg(), getResources().getString(R.string.app_name));
                        if (dataPhotos.size() > 0) {
                            DataPhoto addPhoto = new DataPhoto();
                            addPhoto.setUserPhotoId(gson.getData().getUserPhotoId());
                            addPhoto.setDate(gson.getData().getDate());
                            addPhoto.setWeight(gson.getData().getWeight());
                            addPhoto.setUserPhoto(gson.getData().getUserPhoto());
                            dataPhotos.add(0, addPhoto);
                            photoAdapter.notifyDataSetChanged();
                        } else {
                            dataPhotos.clear();
                            offset = "0";
                            CallPhotoListApi();
                        }

                    } else {
                        App.Utils.ShowAlert(activity, "" + gson.getMsg(), getResources().getString(R.string.app_name));
                    }
                }

                @Override
                public void onFailure(Call<GsonAddPhoto> call, Throwable t) {
                    Log.d("Error", "error" + t.getMessage());
                }
            });
        } else {
            MyUtils.ShowAlert(activity, getResources().getString(R.string.text_internet), getResources().getString(R.string.app_name));
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
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
     * AddPhoto Dialog
     */
    private void AddPhotoDialog() {
        metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        AddPhotoDialog = new Dialog(activity);
        AddPhotoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        AddPhotoDialog.setCancelable(true);
        AddPhotoDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        AddPhotoDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        AddPhotoDialog.setContentView(R.layout.add_image);
        et_weight = (EditText) AddPhotoDialog.findViewById(R.id.et_weight);
        tv_date = (TextView) AddPhotoDialog.findViewById(R.id.tv_date);
        ll_done = (LinearLayout) AddPhotoDialog.findViewById(R.id.ll_done);
        img_cancel = (ImageView) AddPhotoDialog.findViewById(R.id.img_cancel);
        img_photo = (ImageView) AddPhotoDialog.findViewById(R.id.img_photo);

        img_photo.setOnClickListener(this);
        tv_date.setOnClickListener(this);
        ll_done.setOnClickListener(this);
        img_cancel.setOnClickListener(this);
        AddPhotoDialog.show();
        AddPhotoDialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    /**
     * set runtime permission of gallery and camera
     */
    private void Gallery_Parmission() {

        if (checkPermissions()) {
            Dialog_Camera_Gallery();
        }
    }

    /**
     * open Dialog of gallery and camera
     */
    private void Dialog_Camera_Gallery() {

        metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        CustomDialog = new Dialog(activity);
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
        img_gallery.setOnClickListener(this);
        img_camera.setOnClickListener(this);
        CustomDialog.show();
        CustomDialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("requestCode", "" + requestCode);

        if (requestCode == Constants.PHOTO_GALLERY) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String filePath = BitmapLoadUtils.getPathFromUri(activity, uri);
                Uri filePathUri = Uri.parse(filePath);
                if (filePathUri != null) {
                    Image_path = filePathUri.toString();
                    Log.e("imagepath_gallery", filePathUri.toString());
                    beginCrop(filePathUri, Constants.RESULT_CROP_GALLERY);
                } else {
                    MyUtils.ShowAlert(activity, getResources().getString(R.string.File_not_Found),
                            getResources().getString(R.string.app_name));
                }
            }

        } else if (requestCode == Constants.RESULT_CROP_GALLERY) {

            if (resultCode == RESULT_OK) {
                if (Image_path != null) {
                    Image_path = data.getStringExtra(Constants.image_register);
                    Log.e("crop_imagepath_gallery", Image_path);
                    Bitmap bitmapImage = BitmapFactory.decodeFile(Image_path);
                    BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.user_placeholder_login_screen);
                    Glide.with(activity).load(Image_path)
                            .error(R.drawable.user_placeholder_login_screen)
                            .into(img_photo);
                    file = new File(Image_path);

                } else {
                    MyUtils.ShowAlert(activity, getResources().getString(R.string.File_not_Found),
                            getResources().getString(R.string.app_name));
                }
            }
        }
        if (requestCode == Constants.PHOTO_CAMERA) {
            if (resultCode == RESULT_OK) {

                Uri tempUri = Uri.fromFile(file_output);
                Image_path = file_camera.getAbsolutePath();
                LogUtil.Print("imagepath_camera", Image_path);

                if (tempUri != null) {
                    beginCrop(tempUri, Constants.RESULT_CROP_CAMERA);
                } else {
                    MyUtils.ShowAlert(activity, getResources().getString(R.string.File_not_Found),
                            getResources().getString(R.string.app_name));
                }
            }

        } else if (requestCode == Constants.RESULT_CROP_CAMERA
                && resultCode == RESULT_OK) {
            try {
                if (Image_path != null) {
                    Image_path = data.getStringExtra(Constants.image_register);
                    Log.e("crop_imagepath_camera", Image_path);

                    Glide.with(activity).load(Image_path)
                            .error(R.drawable.user_placeholder_login_screen)
                            .into(img_photo);
                    Bitmap bitmapImage = BitmapFactory.decodeFile(Image_path);
                    file = new File(Image_path);
                } else {
                    MyUtils.ShowAlert(activity, getResources().getString(R.string.File_not_Found),
                            getResources().getString(R.string.app_name));
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                MyUtils.ShowAlert(activity, getResources().getString(R.string.File_not_Found), getResources().getString(R.string.app_name));
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
        Intent intent = new Intent(activity, CropActivity.class);
        intent.setData(imageUri);
        intent.putExtra(Constants.FLAG_IS_SQUARE, false);
        intent.putExtra(Constants.IMAGE_CROP_RATIO_WIDTH, Constants.DEFAULT_PICTURE_CROP_RATIO_WIDTH);
        intent.putExtra(Constants.IMAGE_CROP_RATIO_HEIGHT, Constants.DEFAULT_PICTURE_CROP_RATIO_HEIGHT);
        startActivityForResult(intent, resultCrop);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    img_photo.performClick();
                    // permissions granted.
                } else {
                    // permissions not granted.
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setCancelable(false);
                    builder.setTitle(getResources().getString(R.string.app_name));
                    builder.setMessage(getResources().getString(R.string.text_save_profile_picture_msg));
                    builder.setNegativeButton(getResources().getString(R.string.text_skip), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton(getResources().getString(R.string.text_continue), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Gallery_Parmission();
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
                    dialog.show();
                }
                return;
            }
        }
    }

    /**
     * checkPermissions of gallery and camera
     */
    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(activity, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
}
