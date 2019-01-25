package com.example.lenovo.recipes.NetworkUtile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.lenovo.recipes.recipesDetail.RecipesDetail;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.lenovo.recipes.NetworkUtile.InitializeRetroFit.getClient;

public class FetchRecipesData {
    final static String TAG = "FETCH DATA";
    private static ArrayList<RecipesDetail> recipes = new ArrayList<>();


    public static ArrayList<RecipesDetail> getRecipesDetailList() {
        Log.i(TAG, "fetch data");
        InitializeRetroFit.RecipesAPI recipesAPI = getClient().create(InitializeRetroFit.RecipesAPI.class);
        Call<ArrayList<RecipesDetail>> recipesList = recipesAPI.getRecipesDetail();

        recipesList.clone().enqueue(new Callback<ArrayList<RecipesDetail>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<RecipesDetail>> call, @NonNull final Response<ArrayList<RecipesDetail>> response) {
                recipes = response.body();
                Log.i(TAG, "Success");
                Log.i(TAG , recipes.size()+"");
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<RecipesDetail>> call, @NonNull Throwable t) {
                Log.i(TAG, t.getMessage());
            }
        });
        Log.i(TAG, recipes.size() + "");
        return recipes;
    }
}
