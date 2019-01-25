package com.example.lenovo.recipes;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.example.lenovo.recipes.IdlingResource.SimpleIdlingResource;
import com.example.lenovo.recipes.NetworkUtile.InitializeRetroFit;
import com.example.lenovo.recipes.adapterUtile.RecipesNameAdapter;
import com.example.lenovo.recipes.recipesDetail.RecipesDetail;
import com.example.lenovo.recipes.recipesDetail.StepsDetail;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.lenovo.recipes.NetworkUtile.InitializeRetroFit.getClient;


public class MainRecipesActivity extends AppCompatActivity implements RecipesNameAdapter.OnNameClick {

    private ArrayList<RecipesDetail> recipes;

    private RecipesNameAdapter mAdapter;
    private ArrayList<String> mRecipesName = new ArrayList<>();
    public static final String RECIPES_ID = "recipesId";
    public static final String RECIPES_NAME = "recipesName";
    public static final String UPDATE_WIDGET = "recently watched";

    public static int screenHeight;
    public static int screenWidth;
    // get reference to all views
    @BindView(R.id.recipes_name_list)
    RecyclerView namesList;
    public static boolean isTabletLayout;
    public static SimpleIdlingResource mIdlingResource;

    @Nullable
    @VisibleForTesting
    public static IdlingResource getIdleResource() {
        if (mIdlingResource == null)
            mIdlingResource = new SimpleIdlingResource();
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recipes);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0f);

        getIdleResource();

        // show message that make user now the connection is down
        if (!InitializeRetroFit.checkNetwork(this)) {
            View view = findViewById(R.id.network_error_message);
            view.setVisibility(View.VISIBLE);
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;


        InitializeRetroFit.RecipesAPI recipesAPI = getClient().create(InitializeRetroFit.RecipesAPI.class);
        Call<ArrayList<RecipesDetail>> recipesList = recipesAPI.getRecipesDetail();
        mIdlingResource.setIdleState(false);
        recipesList.enqueue(new Callback<ArrayList<RecipesDetail>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<RecipesDetail>> call, @NonNull final Response<ArrayList<RecipesDetail>> response) {
                recipes = response.body();
                ArrayList<String> finalRecipesResult = new ArrayList<>();
                int index;

                //get names
                if (recipes != null && recipes.size() > 0) {
                    for (int n = 0; n < recipes.size(); n++) {
                        mRecipesName.add(recipes.get(n).getName());
                        index = recipes.get(n).getSteps().size() - 1;
                        finalRecipesResult.add(recipes.get(n).getSteps().get(index).getVideoURL());
                    }
                    LinearLayoutManager linearLayoutManager;
                    if (!isTabletLayout)
                        linearLayoutManager = new LinearLayoutManager
                                (MainRecipesActivity.this, LinearLayoutManager.VERTICAL, false);
                    else
                        linearLayoutManager = new LinearLayoutManager(MainRecipesActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    namesList.setLayoutManager(linearLayoutManager);
                    mAdapter = new RecipesNameAdapter(MainRecipesActivity.this, finalRecipesResult ,mRecipesName, MainRecipesActivity.this);
                    namesList.setAdapter(mAdapter);
                    mIdlingResource.setIdleState(true);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<RecipesDetail>> call, @NonNull Throwable t) {
                Log.i("hj", t.getMessage());
                mIdlingResource.setIdleState(true);
            }

        });
    }

    // click this button if connection restored again
    public void refresh(View v) {
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onNameListItemClick(int position, String name) {

        Intent sendRecipesPosition = new Intent(this, DetailRecipesActivity.class);

        sendRecipesPosition.putExtra(RECIPES_ID, position);
        sendRecipesPosition.putExtra(RECIPES_NAME, name);
        sendRecipesPosition.putParcelableArrayListExtra("ArrayList", (ArrayList<? extends Parcelable>) recipes);
        sendRecipesPosition.putExtra("ArrayList", recipes);
        startActivity(sendRecipesPosition);
    }


    // send position of the recipe to show correct detail
    /*@Override
    public void onNameClick(int position, String name) {

        Toast.makeText(this, "position clicked = " + position, Toast.LENGTH_LONG).show();


    }*/

}
