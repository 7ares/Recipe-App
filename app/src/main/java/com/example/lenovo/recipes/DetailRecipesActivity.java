package com.example.lenovo.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.lenovo.recipes.IdlingResource.SimpleIdlingResource;
import com.example.lenovo.recipes.NetworkUtile.InitializeRetroFit;
import com.example.lenovo.recipes.adapterUtile.RecipesNameAdapter;
import com.example.lenovo.recipes.fragmentsUtile.DirectionFragment;
import com.example.lenovo.recipes.fragmentsUtile.IngredientFragment;
import com.example.lenovo.recipes.fragmentsUtile.StepsTutorialFragment;
import com.example.lenovo.recipes.recipesDetail.RecipesDetail;
import com.example.lenovo.recipes.recipesDetail.StepsDetail;
import com.example.lenovo.recipes.widgetUtiles.RecipesWidget;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static com.example.lenovo.recipes.MainRecipesActivity.RECIPES_ID;
import static com.example.lenovo.recipes.MainRecipesActivity.RECIPES_NAME;
import static com.example.lenovo.recipes.MainRecipesActivity.UPDATE_WIDGET;
import static com.example.lenovo.recipes.MainRecipesActivity.mIdlingResource;

import static com.example.lenovo.recipes.NetworkUtile.InitializeRetroFit.getClient;

public class DetailRecipesActivity extends AppCompatActivity {

    public static String TAG = "DetailRecipesActivity";

    private ArrayList<RecipesDetail> mRecipesDetail = new ArrayList<>();
    public static final String DEFAULT_VIDEO_KEY = "videoUrl";
    public static final String VIDEO_KEY = "video";
    private Bundle mSendId;
    private boolean mIsShowIngredientsBtnOn = true;
    private boolean mIsShowStepsBtnOn = false;

    private String mDefaultDisplayedVideo;
    private String mRecipesName;

    private DirectionFragment mDirectionFragment = new DirectionFragment();
    private IngredientFragment mIngredientFragment = new IngredientFragment();
    private StepsTutorialFragment mStepsTutorialFragment = new StepsTutorialFragment();

    public static ArrayList<StepsDetail> videos;

    @BindView(R.id.show_ingredient_list)
    Button btn_showIngredients;
    @BindView(R.id.show_setps)
    Button btn_showSteps;
    @BindView(R.id.ingredient_btn_on)
    View v_showIngredients_on;
    @BindView(R.id.direction_btn_on)
    View v_direction_on;
    SimpleIdlingResource simpleIdlingResource;

    public IdlingResource getIdle() {
        if (simpleIdlingResource == null)
            simpleIdlingResource = new SimpleIdlingResource();
        return simpleIdlingResource;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipes);
        ButterKnife.bind(this);
        Log.i(TAG, "onCreate");
        getIdle();
        simpleIdlingResource.setIdleState(false);

        // we need to initialize fragment only at first time we create activity
        if (savedInstanceState == null) {
// get extra info from intent
            int mRecipesId = getIntent().getIntExtra("recipesId", -1);
            mRecipesName = getIntent().getStringExtra(RECIPES_NAME);
            mRecipesDetail = getIntent().getParcelableArrayListExtra("ArrayList");

// set name of recipes in Actionbar
            Objects.requireNonNull(getSupportActionBar()).setTitle(mRecipesName);
// display the recent recipes that get visited in the widget
            Intent updateWidget = new Intent(this, RecipesWidget.class);
            updateWidget.putExtra(RECIPES_ID, mRecipesId);
            updateWidget.setAction(UPDATE_WIDGET);
            sendBroadcast(updateWidget);

           /* InitializeRetroFit.RecipesAPI recipesAPI = getClient().create(InitializeRetroFit.RecipesAPI.class);
            Call<ArrayList<RecipesDetail>> recipesList = recipesAPI.getRecipesDetail();
            simpleIdlingResource.setIdleState(false);
            recipesList.enqueue(new Callback<ArrayList<RecipesDetail>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<RecipesDetail>> call, @NonNull final Response<ArrayList<RecipesDetail>> response) {
                    recipes = response.body();
                    // get URL for default tutorial video
                    ArrayList<StepsDetail> videos = recipes.get(mRecipesId).getSteps();
                    for (int i = 0; i < videos.size(); i++) {
                        String tutorialUrl = videos.get(i).getVideoURL();
                        if (tutorialUrl != null && tutorialUrl.length() > 0) {
                            mDefaultDisplayedVideo = tutorialUrl;
                            break;
                        }
                        }
                    simpleIdlingResource.setIdleState(true);
                }


                @Override
                public void onFailure(@NonNull Call<ArrayList<RecipesDetail>> call, @NonNull Throwable t) {
                    Log.i("hj", t.getMessage());
                    simpleIdlingResource.setIdleState(true);
                }

            });
*/
            Log.i("ArrayList", mRecipesId + "");

            Log.i("ArrayList", mRecipesDetail.get(0).getName() + "");
            Log.i("ArrayList", mRecipesDetail.get(0).getIngredients().size() + "");


            ArrayList<StepsDetail> videos = mRecipesDetail.get(mRecipesId).getSteps();
            Log.i("ArrayList", videos.size() + "");
            for (int i = 0; i < videos.size(); i++) {
                String tutorialUrl = videos.get(i).getVideoURL();
                if (tutorialUrl != null && tutorialUrl.length() > 0) {
                    mDefaultDisplayedVideo = tutorialUrl ;
                    break;
                }
            }
            // send id of recipe in bundle as argument to fragment to can handel correct recipe
            mSendId = new Bundle();
            mSendId.putInt(RECIPES_ID, mRecipesId);
            mSendId.putParcelableArrayList("ArrayList", mRecipesDetail);
            mIngredientFragment.setArguments(mSendId);

            // display default video
            Bundle sendVideoUrl = new Bundle();
            sendVideoUrl.putString(DEFAULT_VIDEO_KEY, mDefaultDisplayedVideo);
            mStepsTutorialFragment.setArguments(sendVideoUrl);

            // handle visibility of indicator(view)to which fragment is shown now
            v_direction_on.setVisibility(GONE);
            v_showIngredients_on.setVisibility(View.VISIBLE);

            //  initialize ingredient fragment by default
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.recipes_detail_container, mIngredientFragment)
                    .add(R.id.recipes_video_container, mStepsTutorialFragment)
                    .commit();
        }

        // handel configuration changes
        if (savedInstanceState != null) {

            mSendId = savedInstanceState.getBundle(RECIPES_ID);
            mRecipesName = savedInstanceState.getString(RECIPES_NAME);
            mIsShowIngredientsBtnOn = savedInstanceState.getBoolean("ingredient_on");
            mIsShowStepsBtnOn = savedInstanceState.getBoolean("steps_on");
            mRecipesDetail = savedInstanceState.getParcelableArrayList("ArrayList");

            Objects.requireNonNull(getSupportActionBar()).setTitle(mRecipesName);

            if (mIsShowStepsBtnOn) {
                v_showIngredients_on.setVisibility(GONE);
                v_direction_on.setVisibility(View.VISIBLE);
            } else {
                v_showIngredients_on.setVisibility(View.VISIBLE);
                v_direction_on.setVisibility(View.GONE);
            }
        }
    }

    // store the data that we need if configuration changed
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("ingredient_on", mIsShowIngredientsBtnOn);
        outState.putBoolean("steps_on", mIsShowStepsBtnOn);
        outState.putBundle(RECIPES_ID, mSendId);
        outState.putString(RECIPES_NAME, mRecipesName);
        outState.putParcelableArrayList("ArrayList", mRecipesDetail);
    }

    // handel replace of fragments
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.recipes_detail_container, fragment)
                .commit();
        if (fragment.equals(mDirectionFragment)) mDirectionFragment.setArguments(mSendId);
        else mIngredientFragment.setArguments(mSendId);
    }

    // handel change videos
    public void changeVideo(String videoUri) {
        Log.i(TAG, "change video### " + videoUri);

        StepsTutorialFragment fragment = new StepsTutorialFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.recipes_video_container, fragment)
                .commit();

        Bundle sendUri = new Bundle();
        sendUri.putString(VIDEO_KEY, videoUri);
        fragment.setArguments(sendUri);
        Log.i(TAG, "Commit");
    }

    // show ingredient fragment
    @OnClick(R.id.show_ingredient_list)
    void showIngredients() {

        if (!mIsShowIngredientsBtnOn) {
            isClicked(v_showIngredients_on);
            replaceFragment(mIngredientFragment);
        }
    }

    // show steps Fragment
    @OnClick(R.id.show_setps)
    void showSteps() {
        if (!mIsShowStepsBtnOn) {
            isClicked(v_direction_on);
            replaceFragment(mDirectionFragment);

        }
    }

    // handle switch indicator  between fragments
    private void isClicked(View view) {
        switch (view.getId()) {
            case R.id.direction_btn_on:
                if (v_direction_on.getVisibility() != View.VISIBLE) {
                    v_direction_on.setVisibility(View.VISIBLE);
                    v_showIngredients_on.setVisibility(GONE);
                    mIsShowIngredientsBtnOn = false;
                    mIsShowStepsBtnOn = true;

                }
                break;

            case R.id.ingredient_btn_on:
                if (v_showIngredients_on.getVisibility() != View.VISIBLE) {
                    v_showIngredients_on.setVisibility(View.VISIBLE);
                    v_direction_on.setVisibility(GONE);
                    mIsShowStepsBtnOn = false;
                    mIsShowIngredientsBtnOn = true;
                }
                break;

        }

    }

    // handel intent if my activity is the root of stack
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String videoUri = intent.getStringExtra("video");
        changeVideo(videoUri);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "RESUME");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "start");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "stop");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("TAG", "destroy");
    }

}


