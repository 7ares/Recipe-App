package com.example.lenovo.recipes.NetworkUtile;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.lenovo.recipes.recipesDetail.RecipesDetail;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class InitializeRetroFit {

    public static Retrofit getClient() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();

        return new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public interface RecipesAPI {
        @GET("/topher/2017/May/59121517_baking/baking.json")
        Call<ArrayList<RecipesDetail>> getRecipesDetail();
    }

    // check network stat if it online or not
    public static boolean checkNetwork(Context context) {
        boolean isNetworkConnected = false;

        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connManager != null) {
            networkInfo = connManager.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) isNetworkConnected = true;

        return isNetworkConnected;
    }

}
