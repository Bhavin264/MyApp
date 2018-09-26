package com.fittrack.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fittrack.Adapter.ViewPagerAdapter;
import com.fittrack.Constants.Constants;
import com.fittrack.Model.PhotoGallery.DataPhoto;
import com.fittrack.Model.PhotoGallery.GsonPhoto;
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

public class Photo extends AppCompatActivity implements View.OnClickListener {

    private Photo activity;
    /*header View*/
    private TextView tv_title;
    private ImageView img_back, img_share;
    private SmoothProgressBar Progressbar;
    private String ImagePath = "", str_date = "", str_weight = "";
    private ImageView image;
    private TextView tv_weight, tv_date;
    private LinearLayout ll_image;
    private FileOutputStream output;
    private Bitmap bitmap;
    private Bitmap loadedImage;
    private AdView mAdView;
    private ViewPager viewpager;
    private int position=0;
    private GsonPhoto gson;
    List<DataPhoto> dataPhotos = new ArrayList<>();
    ArrayList<String> PhotoList = new ArrayList<>();
    private ViewPagerAdapter pagerAdapter;
    private File FilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        activity = Photo.this;
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
//
//        Drawable drawable = activity.getResources().getDrawable(R.drawable.image_placeholder_big);
//        int h = drawable.getIntrinsicHeight();
//        int w = drawable.getIntrinsicWidth();
//        tv_date.setText(str_date);
//        tv_weight.setText(str_weight);
//        if (!ImagePath.equalsIgnoreCase("")) {
//            Picasso.with(activity).load(ImagePath)
//                    .resize(w, h)
//                    .centerCrop()
//                    .transform(new RoundedCornerImages())
//                    .placeholder(R.drawable.image_placeholder_big)
//                    .error(R.drawable.image_placeholder_big)
//                    .into(image);
//        } else {
//            image.setImageResource(R.drawable.image_placeholder_big);
//        }
    }

    /**
     * Intent Data
     */
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            ImagePath = intent.getStringExtra(Constants.Image);
            str_date = intent.getStringExtra(Constants.date);
            str_weight = intent.getStringExtra(Constants.weight);
            position = intent.getIntExtra(Constants.position,0);

            gson = new GsonBuilder().create().fromJson(intent.getStringExtra(Constants.Imagelist), GsonPhoto.class);
            if (gson != null && gson.getData() != null) {
                dataPhotos.addAll(gson.getData());
            }

            for (int i = 0; i < dataPhotos.size(); i++) {
                PhotoList.add(dataPhotos.get(i).getUserPhoto());
            }
            LogUtil.Print("Imagepath==>", " path=> " + ImagePath + " date=> " + str_date + " weight=> " + str_weight);
            LogUtil.Print("position==>", " position=> " + position + "   size " + dataPhotos.size());
            pagerAdapter = new ViewPagerAdapter(activity, dataPhotos);
            viewpager.setAdapter(pagerAdapter);
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
//        image = (ImageView) findViewById(R.id.image);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_weight = (TextView) findViewById(R.id.tv_weight);
        ll_image = (LinearLayout) findViewById(R.id.ll_image);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        mAdView = (AdView)findViewById(R.id.adView);
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

                ll_image.setBackgroundColor(ContextCompat.getColor(activity, R.color.color_white));
                Bitmap bitmap = takeScreenshot();
                SaveBitmap(bitmap);
                ShareChart();


//                if (!ImagePath.equalsIgnoreCase("")) {
//                    Picasso.with(getApplicationContext()).load(ImagePath).into(new Target() {
//                        @Override
//                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                            Intent i = new Intent(Intent.ACTION_SEND);
//                            i.setType("image/*");
//                            i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
//                            startActivity(Intent.createChooser(i, "Share Image"));
//                        }
//
//                        @Override
//                        public void onBitmapFailed(Drawable errorDrawable) {
//                        }
//
//                        @Override
//                        public void onPrepareLoad(Drawable placeHolderDrawable) {
//                        }
//                    });
//                } else{
//                    App.Utils.ShowAlert(activity, getResources().getString(R.string.text_image_share_empty) , getResources().getString(R.string.app_name));
//
//               }
                break;
        }
    }
    /**
     * TakeScreenshot Programatically
     */
    public Bitmap takeScreenshot() {

        ll_image.getRootView();
//        View view = getWindow().getDecorView().getRootView(); // this line is for full sceen sceenshot
        ll_image.setDrawingCacheEnabled(true);
        return ll_image.getDrawingCache();
    }

    /**
     * SaveBitmap in gallery
     */
    private void SaveBitmap(Bitmap bitmap) {
        ll_image.setBackgroundColor(getResources().getColor(android.R.color.transparent));
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
        String shareBody = "Share your Chart with your Friends...";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Chart");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            /**
             * File imagePath in which image is saved.
             */
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}
