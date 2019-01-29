package com.example.lenovo.recipes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lenovo.recipes.IdlingResource.SimpleIdlingResource;
import com.example.lenovo.recipes.NetworkUtile.InitializeRetroFit;
import com.example.lenovo.recipes.adapterUtile.RecipesNameAdapter;
import com.example.lenovo.recipes.recipesDetail.RecipesDetail;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.lenovo.recipes.NetworkUtile.InitializeRetroFit.getClient;


public class MainRecipesActivity extends AppCompatActivity implements RecipesNameAdapter.OnNameClick {

    public static final String RECIPES_ID = "recipesId";
    public static final String RECIPES_NAME = "recipesName";
    public static final String UPDATE_WIDGET = "recently watched";

    public static ArrayList<RecipesDetail> recipes;
    private RecipesNameAdapter mAdapter;
    private ArrayList<String> mRecipesName = new ArrayList<>();

    // get reference to all views
    @BindView(R.id.loading_message)
    TextView mLoadingMessage_tv;
    @BindView(R.id.loading_progress)
    ProgressBar mLoadingProgressBar;
    @Nullable
    @BindView(R.id.chocolate)
    ImageView mRecipesImage;
    @BindView(R.id.recipes_name_list)
    RecyclerView namesList;
    @Nullable
    @BindView(R.id.land_layout)
    RelativeLayout relativeLayout;

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

        // get idle resource instance for test
        getIdleResource();

        // show message that make user now the connection is down
        if (!InitializeRetroFit.checkNetwork(this)) {
            View view = findViewById(R.id.network_error_message);
            view.setVisibility(View.VISIBLE);
            mLoadingProgressBar.setVisibility(View.GONE);
            mLoadingMessage_tv.setVisibility(View.GONE);
        }

        // fetching data from server
        InitializeRetroFit.RecipesAPI recipesAPI = getClient().create(InitializeRetroFit.RecipesAPI.class);
        Call<ArrayList<RecipesDetail>> recipesList = recipesAPI.getRecipesDetail();
        // make test waite until background work finished
        mIdlingResource.setIdleState(false);

        recipesList.enqueue(new Callback<ArrayList<RecipesDetail>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<RecipesDetail>> call, @NonNull final Response<ArrayList<RecipesDetail>> response) {
                recipes = response.body();

                //get recipes names
                if (recipes != null && recipes.size() > 0) {
                    for (int n = 0; n < recipes.size(); n++) {
                        mRecipesName.add(recipes.get(n).getName());
                    }
                    // set up recyclerView
                    GridLayoutManager gridLayoutManager;
                    // handel screen size Variety
                    if (relativeLayout != null || mRecipesImage != null)
                        gridLayoutManager = new GridLayoutManager(MainRecipesActivity.this, 2);
                    else
                        gridLayoutManager = new GridLayoutManager(MainRecipesActivity.this, 1);

                    namesList.setLayoutManager(gridLayoutManager);

                    mAdapter = new RecipesNameAdapter(MainRecipesActivity.this, mRecipesName, MainRecipesActivity.this);
                    // make progress bar invisible when data is ready to be displayed on screen
                    mLoadingProgressBar.setVisibility(View.GONE);
                    mLoadingMessage_tv.setVisibility(View.GONE);

                    namesList.setAdapter(mAdapter);
                    // test continue it's work
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

    // open detail activity and send some extra data
    @Override
    public void onNameListItemClick(int position, String name) {

        Intent sendRecipesPosition = new Intent(this, DetailRecipesActivity.class);

        sendRecipesPosition.putExtra(RECIPES_ID, position);
        sendRecipesPosition.putExtra(RECIPES_NAME, name);
        //  sendRecipesPosition.putParcelableArrayListExtra("ArrayList", recipes);
        sendRecipesPosition.putExtra("ArrayList", recipes);

        startActivity(sendRecipesPosition);
    }



   /* public int getScreenOrientation()
    {
        Display screenOrientation = getWindowManager().getDefaultDisplay();
        int orientation ;
        if(screenOrientation.getWidth()==screenOrientation.getHeight()){
            orientation = Configuration.ORIENTATION_SQUARE;
            } else{
            if(screenOrientation.getWidth() < screenOrientation.getHeight()){
                orientation = Configuration.ORIENTATION_PORTRAIT;


            }else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;

            }
        }
        return orientation;
    }
*/

    // send position of the recipe to show correct detail
    /*@Override
    public void onNameClick(int position, String name) {

        Toast.makeText(this, "position clicked = " + position, Toast.LENGTH_LONG).show();


    }*/

}
