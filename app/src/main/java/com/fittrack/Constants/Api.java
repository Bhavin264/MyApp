package com.fittrack.Constants;

import com.fittrack.App;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Umesh on 7/6/2017.
 */
public class Api {
    /*local*/
//    public static String BASE_URL = "http://192.168.0.9/fittrack/api/";
    /*Live*/
    public static String BASE_URL = "http://dignizant.com/fittrack/api/";

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(App.client)
            .build();
}

//
//    public static OkHttpClient client = new OkHttpClient.Builder()
//            .addInterceptor(new App.BasicAuthInterceptor("fittrack", "jd3Z=fn[\"2#^su$Y"))
//            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
//            .readTimeout(60, TimeUnit.SECONDS)
//            .connectTimeout(60, TimeUnit.SECONDS)
//            .build();









