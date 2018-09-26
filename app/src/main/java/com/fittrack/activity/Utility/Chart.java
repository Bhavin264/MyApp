package com.fittrack.activity.Utility;

import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fittrack.App;
import com.fittrack.Constants.Api;
import com.fittrack.Constants.Constants;
import com.fittrack.Interface.ApiInterface;
import com.fittrack.Model.WeightChart.DataWeightChart;
import com.fittrack.Model.WeightChart.GsonWeightChart;
import com.fittrack.Progressbar.SmoothProgressBar;
import com.fittrack.R;
import com.fittrack.Utility.LogUtil;
import com.fittrack.Utility.MyUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Chart extends AppCompatActivity implements View.OnClickListener {

    private Chart activity;
    /*header View*/
    private TextView tv_title;
    private ImageView img_back, img_share;
    private SmoothProgressBar Progressbar;
    private LineChart lineChart;
    ArrayList<String> monthlist = new ArrayList<>();
    ArrayList<String> weight = new ArrayList<>();
    String Selected_month = "";
    String data_type = "1";
    List<DataWeightChart> dataWeightSheets = new ArrayList<>();
    private Spinner sp_month;
    private ArrayList<String> month_list = new ArrayList<>();
    private TextView tv_month, tv_year, tv_sp_month;
    ArrayList<Entry> entries = new ArrayList<>();
    private LineDataSet dataset;
    private LineData data;
    private RelativeLayout rl_month;
    private int month;
    private LinearLayout ll_chart;
    private File FilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.RegisterActivityForBugsense(activity);
        setContentView(R.layout.activity_chart);

        activity = Chart.this;
        HeaderView();
        findViewById();
        OnClickListener();
        setMonthSpinner();
        data_type = Constants.date_type_year;
        CallWeightYearlyChartApi();
    }

    /**
     * set data in Spinner
     */
    private void setMonthSpinner() {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;

        LogUtil.Print("month", "" + month);
        month_list.add(getString(R.string.text_month));
        month_list.add(getString(R.string.text_january));
        month_list.add(getString(R.string.text_february));
        month_list.add(getString(R.string.text_march));
        month_list.add(getString(R.string.text_april));
        month_list.add(getString(R.string.text_may));
        month_list.add(getString(R.string.text_june));
        month_list.add(getString(R.string.text_july));
        month_list.add(getString(R.string.text_august));
        month_list.add(getString(R.string.text_sepember));
        month_list.add(getString(R.string.text_october));
        month_list.add(getString(R.string.text_november));
        month_list.add(getString(R.string.text_december));

//        for (int i = 0; i <month_list.size() ; i++) {
//
//            if(month == month_list.get(i).length()){
//
//                LogUtil.Print("selected", "" + month_list.get(i).length());
//
//            }
//        }
        final ArrayAdapter<String> adapter_gender = new ArrayAdapter<String>(activity,
                R.layout.spinner_month_items, R.id.tv_spinner_items, month_list);
        sp_month.setAdapter(adapter_gender);
        sp_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i > 0) {
                    data_type = "2";
                    Selected_month = "" + i;
                    CallWeightYearlyChartApi();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * WeightChart Api
     */
    private void CallWeightYearlyChartApi() {
        if (App.Utils.IsInternetOn()) {

            ApiInterface request = Api.retrofit.create(ApiInterface.class);
            Map<String, String> params = new HashMap<String, String>();

            if (data_type.equalsIgnoreCase("1")) {
                params.put(Constants.month, "");
                params.put(Constants.date_type, Constants.date_type_year);
            } else if (data_type.equalsIgnoreCase("2")) {
                params.put(Constants.month, Selected_month);
                params.put(Constants.date_type, Constants.date_type_month);
            }
            params.put(Constants.user_id, App.Utils.getString(Constants.user_id));
            params.put(Constants.access_token, App.Utils.getString(Constants.access_token));
            params.put(Constants.lang, Constants.language);
            LogUtil.Print("weight_chart_params", "" + params);

            Call<GsonWeightChart> call = request.getWeightChart(params);
            Progressbar.progressiveStart();
            Progressbar.setVisibility(View.VISIBLE);
            Progressbar.setIndeterminate(true);
            call.enqueue(new Callback<GsonWeightChart>() {
                @Override
                public void onResponse(Call<GsonWeightChart> call, Response<GsonWeightChart> response) {

                    Progressbar.progressiveStop();
                    Progressbar.setVisibility(View.GONE);

                    GsonWeightChart gson = response.body();
                    if (gson.getFlag().equals(1)) {

                        if (data_type.equalsIgnoreCase("1")) {
                            dataWeightSheets.clear();
                            monthlist.clear();
                            weight.clear();
                            entries.clear();

                            dataWeightSheets.addAll(gson.getData());
                            for (int i = 0; i < dataWeightSheets.size(); i++) {
                                monthlist.add(gson.getData().get(i).getMonth());
                                weight.add(gson.getData().get(i).getWeight());
                            }
                            for (int j = 0; j <= 11; j++) {
                                entries.add(new Entry(Float.parseFloat(weight.get(j).toString()), j));
                            }

                            dataset = new LineDataSet(entries, "");
                            data = new LineData(monthlist, dataset);
                            dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                            dataset.setDrawCubic(true);
                            dataset.setDrawFilled(false);
                            dataset.setColor(getResources().getColor(R.color.color_bg_header));
                            dataset.setCircleSize(5f);
//                            dataset.setFillAlpha(10);
                            dataset.setLineWidth(3f);
                            dataset.setDrawValues(false);
                            dataset.setCircleColor(getResources().getColor(R.color.color_red));
                            dataset.getCircleHoleColor();
                            dataset.setCircleColorHole(getResources().getColor(R.color.color_red));
                            YAxis leftAxis = lineChart.getAxisLeft();
                            leftAxis.setAxisMaxValue(140f);
                            leftAxis.setAxisMinValue(0f);
                            leftAxis.setDrawGridLines(true);
                            lineChart.setData(data);
                            lineChart.setDrawGridBackground(true);
                            XAxis xAxis = lineChart.getXAxis();
                            xAxis.setDrawGridLines(true);
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            setChart();


                        } else if (data_type.equalsIgnoreCase("2")) {
                            dataWeightSheets.clear();
                            monthlist.clear();
                            weight.clear();
                            entries.clear();

                            dataWeightSheets.addAll(gson.getData());

                            LogUtil.Print("size", "" + dataWeightSheets.size());
                            tv_month.setVisibility(View.GONE);
                            tv_month.setText(gson.getMonthName());

                            for (int i = 0; i < dataWeightSheets.size(); i++) {
                                monthlist.add(gson.getData().get(i).getMonth());
                                weight.add(gson.getData().get(i).getWeight());
                            }
                            for (int j = 0; j < dataWeightSheets.size(); j++) {
                                entries.add(new Entry(Float.parseFloat(weight.get(j).toString()), j));
                            }
                            dataset = new LineDataSet(entries, "");
                            data = new LineData(monthlist, dataset);
                            dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                            dataset.setDrawCubic(true);
                            dataset.setDrawFilled(false);
                            dataset.setColor(getResources().getColor(R.color.color_bg_header));
                            dataset.setCircleSize(3f);
//                            dataset.setFillAlpha(10);
                            dataset.setLineWidth(2f);
                            dataset.setDrawValues(false);
                            dataset.setCircleColor(getResources().getColor(R.color.color_red));
                            dataset.getCircleHoleColor();
                            dataset.setCircleColorHole(getResources().getColor(R.color.color_red));
                            YAxis leftAxis = lineChart.getAxisLeft();
                            leftAxis.setAxisMaxValue(140f);
                            leftAxis.setAxisMinValue(0f);
                            leftAxis.setDrawGridLines(true);
                            lineChart.setData(data);
                            lineChart.setDrawGridBackground(true);
                            XAxis xAxis = lineChart.getXAxis();
                            xAxis.setDrawGridLines(true);
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            setChart();
                        }
                    } else {
                        App.Utils.ShowAlert(activity, "" + gson.getMsg(), getResources().getString(R.string.app_name));
                    }
                }
                @Override
                public void onFailure(Call<GsonWeightChart> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });
        } else {
            MyUtils.ShowAlert(activity, getResources().getString(R.string.text_internet), getResources().getString(R.string.app_name));
        }
    }

    private void setChart() {
        lineChart.invalidate();
        lineChart.getAxisRight().setEnabled(false);
//        lineChart.setDescriptionPosition(1f, 1f);
        lineChart.setDescription("Weight(kg)");// set the description
        lineChart.setDescriptionColor(getResources().getColor(R.color.color_red, null));
        lineChart.getAxisLeft().setDrawLabels(true);
        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.getXAxis().setDrawLabels(true);
        lineChart.getLegend().setEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setTouchEnabled(false);
        lineChart.animateY(5);
    }

    /**
     * Header view
     */
    private void HeaderView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.text_chart));
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
        tv_year.setOnClickListener(this);
        tv_sp_month.setOnClickListener(this);

    }

    /**
     * findViews by Ids
     */
    private void findViewById() {
        lineChart = (LineChart) findViewById(R.id.chart);
//        graph = (GraphView) findViewById(R.id.graph);
        Progressbar = (SmoothProgressBar) findViewById(R.id.google_now);
        sp_month = (Spinner) findViewById(R.id.sp_month);
        tv_month = (TextView) findViewById(R.id.tv_month);
        rl_month = (RelativeLayout) findViewById(R.id.rl_month);
        tv_year = (TextView) findViewById(R.id.tv_year);
        tv_sp_month = (TextView) findViewById(R.id.tv_sp_month);
        ll_chart = (LinearLayout) findViewById(R.id.ll_chart);
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

            case R.id.tv_sp_month:

                sp_month.setBackgroundResource(R.drawable.bg_round_red);
                tv_year.setBackgroundResource(R.drawable.bg_round_corner_blue);
                sp_month.setVisibility(View.VISIBLE);
                tv_sp_month.setVisibility(View.GONE);
                tv_year.setTextColor(getResources().getColor(R.color.color_bg_header, null));
                tv_sp_month.setTextColor(getResources().getColor(R.color.color_white, null));
                sp_month.setSelection(month);
                Selected_month = "" + month;
                LogUtil.Print("current_month", "" + month);
                data_type = Constants.date_type_month;
                CallWeightYearlyChartApi();
                break;

            case R.id.tv_year:
                tv_sp_month.setBackgroundResource(R.drawable.bg_round_corner_blue);
                tv_year.setBackgroundResource(R.drawable.bg_round_red);
                sp_month.setVisibility(View.GONE);
                tv_sp_month.setVisibility(View.VISIBLE);
                tv_sp_month.setTextColor(getResources().getColor(R.color.color_bg_header, null));
                tv_year.setTextColor(getResources().getColor(R.color.color_white, null));
                data_type = Constants.date_type_year;
                CallWeightYearlyChartApi();
                break;

            case R.id.img_share:
                ll_chart.setBackgroundColor(getResources().getColor(R.color.color_white));
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
        View view = findViewById(R.id.ll_chart);//your layout id
        view.getRootView();
//        View view = getWindow().getDecorView().getRootView(); // this line is for full sceen sceenshot
        view.setDrawingCacheEnabled(true);
        return view.getDrawingCache();
    }

    /**
     * SaveBitmap in gallery
     */
    private void SaveBitmap(Bitmap bitmap) {
        ll_chart.setBackgroundColor(getResources().getColor(android.R.color.transparent));
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

}
