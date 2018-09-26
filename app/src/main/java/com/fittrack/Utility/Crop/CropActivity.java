package com.fittrack.Utility.Crop;

/**
 * Created by chandresh on 3/12/2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.fittrack.App;
import com.fittrack.Constants.Constants;
import com.fittrack.R;
import com.naver.android.helloyako.imagecrop.view.ImageCropView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class CropActivity extends Activity {
    public static final String TAG = "CropActivity";
    boolean IS_SQUARE = false;
    private ImageCropView imageCropView;
    private int cropRatioWidth = 320;
    private int cropRatioHeight = 240;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        imageCropView = (ImageCropView) findViewById(+R.id.image);

        Intent i = getIntent();
        Uri uri = i.getData();
        IS_SQUARE = i.getBooleanExtra(Constants.FLAG_IS_SQUARE, false);

        if (i.hasExtra(Constants.IMAGE_CROP_RATIO_WIDTH))
            cropRatioWidth = i.getIntExtra(Constants.IMAGE_CROP_RATIO_WIDTH, 320);

        if (i.hasExtra(Constants.IMAGE_CROP_RATIO_HEIGHT))
            cropRatioHeight = i.getIntExtra(Constants.IMAGE_CROP_RATIO_HEIGHT, 240);

        Log.e(TAG, "uri..." + uri);

//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        int imageWidth = (int) ( (float) metrics.widthPixels / 1.5 );
//        int imageHeight = (int) ( (float) metrics.heightPixels / 1.5 );
//
//        bitmap = BitmapLoadUtils.decode(uri.toString(), imageWidth, imageHeight);
//
//        imageCropView.setImageBitmap(bitmap);

        imageCropView.setImageFilePath(App.Utils.getRealPathFromURI(uri, this));

        if (IS_SQUARE)
            imageCropView.setAspectRatio(1, 1);
        else {
            imageCropView.setAspectRatio(cropRatioWidth, cropRatioHeight);
        }

        //imageCropView.setMaxHeight(500);
        /*imageCropView.setMaxWidth(320);*/

        findViewById(R.id.tv_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "select");

                try {
                    Bitmap b = imageCropView.getCroppedImage();
                    bitmapConvertToFile(b);

                } catch (Exception e) {
                    // App.Utils.makeToast(getResources().getString(R.string.text_unaccepted_files));
                    finish();
                }
            }
        });

        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "cancel");
                setResult(RESULT_CANCELED);
                finish();
            }
        });


    }

    private boolean isPossibleCrop(int widthRatio, int heightRatio) {
        int bitmapWidth = imageCropView.getViewBitmap().getWidth();
        int bitmapHeight = imageCropView.getViewBitmap().getHeight();
        return !(bitmapWidth < widthRatio && bitmapHeight < heightRatio);
    }

    public File bitmapConvertToFile(Bitmap bitmap) {
        FileOutputStream fileOutputStream = null;
        File bitmapFile = null;
        final String file_path;
        try {

            File file = new File(Constants.App_ROOT_FOLDER, "");
            if (!file.exists()) {
                file.mkdir();
            }
            String image_name = "IMG_" + (new SimpleDateFormat("yyyyMMddHHmmss")).format(Calendar.getInstance().getTime()) + ".jpg";
            file_path = Constants.App_ROOT_FOLDER + "/" + image_name;

            bitmapFile = new File(file, image_name);
            fileOutputStream = new FileOutputStream(bitmapFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            MediaScannerConnection.scanFile(this, new String[]{bitmapFile.getAbsolutePath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
                @Override
                public void onMediaScannerConnected() {
                }
                @Override
                public void onScanCompleted(String path, Uri uri) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(CropActivity.this, "file saved", Toast.LENGTH_LONG).show();

                            Intent i = new Intent();
                            i.putExtra(Constants.image_register, file_path);
                            setResult(RESULT_OK, i);
                            finish();

                        }

                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (Exception e) {
                }
            }
        }

        return bitmapFile;
    }

}
