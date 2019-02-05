package com.example.lenovo.recipes;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.lenovo.recipes.fragmentsUtile.DirectionFragment;
import com.example.lenovo.recipes.fragmentsUtile.IngredientFragment;
import com.example.lenovo.recipes.fragmentsUtile.ServingNumberPickerDialog;
import com.example.lenovo.recipes.fragmentsUtile.StepsTutorialFragment;
import com.example.lenovo.recipes.recipesDetail.IngredientDetail;
import com.example.lenovo.recipes.recipesDetail.StepsDetail;
import com.example.lenovo.recipes.widgetUtiles.RecipesWidget;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static com.example.lenovo.recipes.MainRecipesActivity.*;
import static com.example.lenovo.recipes.MainRecipesActivity.RECIPES_ID;
import static com.example.lenovo.recipes.MainRecipesActivity.RECIPES_NAME;
import static com.example.lenovo.recipes.MainRecipesActivity.UPDATE_WIDGET;
import static com.example.lenovo.recipes.widgetUtiles.RecipesWidget.mRecipesDetail;

public class DetailRecipesActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

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


    @BindView(R.id.show_ingredient_list)
    Button btn_showIngredients;
    @BindView(R.id.show_setps)
    Button btn_showSteps;
    @BindView(R.id.ingredient_btn_on)
    View v_showIngredients_on;
    @BindView(R.id.direction_btn_on)
    View v_direction_on;

    private int mRecipesId;
    private static int numberServings;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipes);
        ButterKnife.bind(this);

        // we need to initialize fragment only at first time we create activity
        if (savedInstanceState == null) {
            if (Objects.equals(getIntent().getAction(), "WIDGET")) {
                // get extra info from intent
                mRecipesId = getIntent().getIntExtra("recipesId", -1);
                mRecipesName = getIntent().getStringExtra("recipesName");
                if (recipes == null)
                    recipes = getIntent().getParcelableArrayListExtra("recipeArray");
            } else {
                // get extra info from intent
                mRecipesId = getIntent().getIntExtra("recipesId", -1);
                mRecipesName = getIntent().getStringExtra(RECIPES_NAME);

                // display the recent recipes that get visited in the widget
                Intent updateWidget = new Intent(this, RecipesWidget.class);
                updateWidget.putExtra(RECIPES_ID, mRecipesId);
                updateWidget.setAction(UPDATE_WIDGET);
                sendBroadcast(updateWidget);
            }

            numberServings = recipes.get(mRecipesId).getServings();
            getIngredientQuantityForOne();

            // set name of recipes in Actionbar
            Objects.requireNonNull(getSupportActionBar()).setTitle(mRecipesName);

            ArrayList<StepsDetail> videos = recipes.get(mRecipesId).getSteps();

            for (int i = 0; i < videos.size(); i++) {
                String tutorialUrl = videos.get(i).getVideoURL();
                if (tutorialUrl != null && tutorialUrl.length() > 0) {
                    mDefaultDisplayedVideo = tutorialUrl;
                    break;
                }
            }
            // send id of recipe in bundle as argument to fragment to can handel correct recipe
            mSendId = new Bundle();
            mSendId.putInt(RECIPES_ID, mRecipesId);
            mSendId.putParcelableArrayList("ArrayList", recipes);
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
        StepsTutorialFragment fragment = new StepsTutorialFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.recipes_video_container, fragment)
                .commit();

        Bundle sendUri = new Bundle();
        sendUri.putString(VIDEO_KEY, videoUri);
        fragment.setArguments(sendUri);
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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Objects.equals(intent.getAction(), "WIDGET")) {
            mRecipesId = getIntent().getIntExtra("recipesId", -1);
            mRecipesName = getIntent().getStringExtra("recipesName");

            Objects.requireNonNull(getSupportActionBar()).setTitle(mRecipesName);
            ArrayList<StepsDetail> videos = recipes.get(mRecipesId).getSteps();

            for (int i = 0; i < videos.size(); i++) {
                String tutorialUrl = videos.get(i).getVideoURL();
                if (tutorialUrl != null && tutorialUrl.length() > 0) {
                    mDefaultDisplayedVideo = tutorialUrl;
                    break;
                }
            }

            // send id of recipe in bundle as argument to fragment to can handel correct recipe
            mSendId = new Bundle();
            mSendId.putInt(RECIPES_ID, mRecipesId);
            mSendId.putParcelableArrayList("ArrayList", recipes);

            // display default video
            Bundle sendVideoUrl = new Bundle();
            sendVideoUrl.putString(DEFAULT_VIDEO_KEY, mDefaultDisplayedVideo);

            // handle visibility of indicator(view)to which fragment is shown now
            v_direction_on.setVisibility(GONE);
            v_showIngredients_on.setVisibility(View.VISIBLE);

            mIngredientFragment = new IngredientFragment();
            mStepsTutorialFragment = new StepsTutorialFragment();
            mIngredientFragment.setArguments(mSendId);
            mStepsTutorialFragment.setArguments(sendVideoUrl);

            //  initialize ingredient fragment by default
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.recipes_detail_container, mIngredientFragment)
                    .replace(R.id.recipes_video_container, mStepsTutorialFragment)
                    .commit();


        } else {
            String videoUri = intent.getStringExtra("video");
            changeVideo(videoUri);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent openMainActivity = new Intent(this, MainRecipesActivity.class);
            startActivity(openMainActivity);
            finish();
        } else {
            showNumberPicker();
        }
        return super.onOptionsItemSelected(item);
    }

    public void showNumberPicker() {
        ServingNumberPickerDialog newFragment = new ServingNumberPickerDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("value", numberServings);
        newFragment.setArguments(bundle);
        newFragment.setListener(this);
        newFragment.show(getSupportFragmentManager(), "time picker");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.serving_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.spinner);

        TextView servingText = (TextView) item.getActionView();
        servingText.setTextColor(getResources().getColor(R.color.colorAccent));
        servingText.setText(String.valueOf(numberServings) + " Servings");
        servingText.setTextSize(16);

        return true;
    }

    ArrayList<String> ingredientQuantityForMany = new ArrayList<>();

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        oldVal = numberServings;
        numberServings = newVal;
        invalidateOptionsMenu();

        if (newVal != oldVal) {
            ingredientQuantityForMany.clear();
            for (int i = 0; i < ingredientQuantityForOne.size(); i++) {
                ingredientQuantityForMany.add(String.valueOf(ingredientQuantityForOne.get(i) * newVal));
            }

            Bundle bundle = new Bundle();
            bundle.putStringArrayList("IngredientQuantity", ingredientQuantityForMany);
            bundle.putInt(RECIPES_ID, mRecipesId);
            bundle.putParcelableArrayList("ArrayList", recipes);

            mIngredientFragment = new IngredientFragment();
            mIngredientFragment.setArguments(bundle);
            v_direction_on.setVisibility(GONE);
            v_showIngredients_on.setVisibility(View.VISIBLE);
            mIsShowStepsBtnOn = false;
            mIsShowIngredientsBtnOn = true;

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.recipes_detail_container, mIngredientFragment)
                    .commit();
        }
    }

    ArrayList<Double> ingredientQuantityForOne = new ArrayList<>();

    public void getIngredientQuantityForOne() {

        ingredientQuantityForOne.clear();
        ArrayList<IngredientDetail> ingredientDetails = recipes.get(mRecipesId).getIngredients();
        for (int i = 0; i < ingredientDetails.size(); i++) {
            ingredientQuantityForOne.add(ingredientDetails.get(i).getQuantity() / numberServings);
        }
    }

}


