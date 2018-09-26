package com.fittrack.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fittrack.Adapter.ViewpagerCompareAdapter;
import com.fittrack.Constants.Constants;
import com.fittrack.Model.CompareClg.DataCompareList;
import com.fittrack.Model.CompareClg.GsonCompareList;
import com.fittrack.Progressbar.SmoothProgressBar;
import com.fittrack.R;
import com.fittrack.Utility.LogUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComparePhotoShare extends AppCompatActivity implements View.OnClickListener {

    private ComparePhotoShare activity;
    /*header View*/
    private TextView tv_title;
    private ImageView img_back, img_share;
    private SmoothProgressBar Progressbar;
    private String ImagePath1 = "", str_date1 = "", str_weight1 = "", ImagePath2 = "", str_date2 = "", str_weight2 = "";
    private ImageView image1, image2;
    private TextView tv_weight1, tv_date1, tv_weight2, tv_date2;
    private LinearLayout ll_image1, ll_image2, ll_main;
    private FileOutputStream output;
    private Bitmap bitmap;
    private Bitmap loadedImage;
    private File FilePath;
    private AdView mAdView;
    private GsonCompareList gson;
    List<DataCompareList> dataCompareList = new ArrayList<>();
    private int position=0;
    private ViewPager viewpager;
    ViewpagerCompareAdapter viewpagerCompareAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_photo_share);
        activity = ComparePhotoShare.this;
        HeaderView();
        findViewById();
        OnClickListener();
        getIntentData();
        setData();
    }

    /**
     * Set Data
     */
    private void setData() {

//        Drawable drawable = activity.getResources().getDrawable(R.drawable.image_placeholder_compare);
//        int h = drawable.getIntrinsicHeight();
//        int w = drawable.getIntrinsicWidth();
//        /*For Image 1*/
//        tv_date1.setText(str_date1);
//        tv_weight1.setText(str_weight1);
//        if (!ImagePath1.equalsIgnoreCase("")) {
//
//            Picasso.with(activity).load(ImagePath1)
//                    .resize(w, h)
//                    .centerCrop()
//                    .transform(new RoundedCornerImages())
//                    .placeholder(R.drawable.image_placeholder_compare)
//                    .error(R.drawable.image_placeholder_compare)
//                    .into(image1);
//        } else {
//            image1.setImageResource(R.drawable.image_placeholder_compare);
//
//        }
//         /*For Image 1*/
//        tv_date2.setText(str_date2);
//        tv_weight2.setText(str_weight2);
//        if (!ImagePath2.equalsIgnoreCase("")) {
//            Picasso.with(activity).load(ImagePath2)
//                    .resize(w, h)
//                    .centerCrop()
//                    .transform(new RoundedCornerImages())
//                    .placeholder(R.drawable.image_placeholder_compare)
//                    .error(R.drawable.image_placeholder_compare)
//                    .into(image2);
//        } else {
//            image2.setImageResource(R.drawable.image_placeholder_compare);
//        }
    }

    /**
     * Intent Data
     */
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            ImagePath1 = intent.getStringExtra(Constants.Image1);
            str_date1 = intent.getStringExtra(Constants.date1);
            str_weight1 = intent.getStringExtra(Constants.weight1);
            ImagePath2 = intent.getStringExtra(Constants.Image2);
            str_date2 = intent.getStringExtra(Constants.date2);
            str_weight2 = intent.getStringExtra(Constants.weight2);
            position = intent.getIntExtra(Constants.position,0);

            gson = new GsonBuilder().create().fromJson(intent.getStringExtra(Constants.Imagelist), GsonCompareList.class);
            if (gson != null && gson.getData() != null) {
                dataCompareList.addAll(gson.getData());
            }

            LogUtil.Print("Imagepath1==>", " path=> " + ImagePath1 + " date=> " + str_date1 + " weight=> " + str_weight1);
            LogUtil.Print("Imagepath2==>", " path=> " + ImagePath2 + " date=> " + str_date2 + " weight=> " + str_weight2);
            LogUtil.Print("position==>", " position=> " + position + "   size " + dataCompareList.size());
            viewpagerCompareAdapter = new ViewpagerCompareAdapter(activity, dataCompareList);
            viewpager.setAdapter(viewpagerCompareAdapter);
            viewpager.setCurrentItem(position);
        }
    }

    /**
     * Header view
     */
    private void HeaderView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.text_image));
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setVisibility(View.VISIBLE);
        img_back.setOnClickListener(this);
        img_share = (ImageView) findViewById(R.id.img_share);
        img_share.setVisibility(View.VISIBLE);
        img_share.setOnClickListener(this);
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
//        image1 = (ImageView) findViewById(R.id.image1);
//        tv_date1 = (TextView) findViewById(R.id.tv_date1);
//        tv_weight1 = (TextView) findViewById(R.id.tv_weight1);
//        image2 = (ImageView) findViewById(R.id.image2);
//        tv_date2 = (TextView) findViewById(R.id.tv_date2);
//        tv_weight2 = (TextView) findViewById(R.id.tv_weight2);
//        ll_image1 = (LinearLayout) findViewById(R.id.ll_image1);
//        ll_image2 = (LinearLayout) findViewById(R.id.ll_image2);

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        ll_main = (LinearLayout) findViewById(R.id.ll_main);
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

            case R.id.img_share:
                ll_main.setBackgroundColor(getResources().getColor(R.color.color_white));
                Bitmap bitmap = takeScreenshot();
                SaveBitmap(bitmap);
                ShareChart();
                break;
        }
    }

    /**
     * TakeScreenshot Programatically
     */
    public Bitmap takeScreenshot() {
        View view = findViewById(R.id.ll_main);//your layout id
        view.getRootView();
//        View view = getWindow().getDecorView().getRootView(); // this line is for full sceen sceenshot
        view.setDrawingCacheEnabled(true);
        return view.getDrawingCache();
    }

    /**
     * SaveBitmap in gallery
     */
    private void SaveBitmap(Bitmap bitmap) {
        ll_main.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        /**
         * File imagePath in which image is saved.
         */
        FilePath = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
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
        String shareBody = "Share your Photo with your Friends...";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Photo");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

}
