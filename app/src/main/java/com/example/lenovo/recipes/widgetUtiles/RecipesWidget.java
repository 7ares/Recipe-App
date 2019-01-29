package com.example.lenovo.recipes.widgetUtiles;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.example.lenovo.recipes.DetailRecipesActivity;
import com.example.lenovo.recipes.NetworkUtile.InitializeRetroFit;
import com.example.lenovo.recipes.R;
import com.example.lenovo.recipes.recipesDetail.RecipesDetail;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.lenovo.recipes.NetworkUtile.InitializeRetroFit.getClient;


/**
 * Implementation of App Widget functionality.
 */
public class RecipesWidget extends AppWidgetProvider {
    static String GAT = "widget provider";
    private Context mContext;

    public static final String UPDATE_WIDGET_ACTION = "recently watched";
    public static final String DISPLAY_NEXT_RECIPE_ACTION = "display next";
    public static final String DISPLAY_PREV_RECIPE_ACTION = "display_prev";

    public static ArrayList<RecipesDetail> mRecipesDetail;
    public static int mRecipesListIndex = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        mContext = context;

        if (intent != null) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context,
                    RecipesWidget.class));

            // handle intents depending on specified action
            switch (Objects.requireNonNull(intent.getAction())) {
                // display next recipes
                case DISPLAY_NEXT_RECIPE_ACTION:
                    mRecipesListIndex = intent.getIntExtra("next", 0);
                    if (mRecipesListIndex < 3) mRecipesListIndex++;
                    else mRecipesListIndex = 0;
                    onUpdate(context, appWidgetManager, appWidgetIds);
                    break;

                // display previous recipes
                case DISPLAY_PREV_RECIPE_ACTION:
                    mRecipesListIndex = intent.getIntExtra("prev", 0);
                    if (mRecipesListIndex > 0) mRecipesListIndex--;
                    else mRecipesListIndex = 3;
                    onUpdate(context, appWidgetManager, appWidgetIds);
                    break;

                // display recent watched recipes
                case UPDATE_WIDGET_ACTION:
                    mRecipesListIndex = intent.getIntExtra("recipesId", 0);
                    onUpdate(context, appWidgetManager, appWidgetIds);
                    break;
            }

            // update widget
            // onUpdate(context, appWidgetManager, appWidgetIds);

        }
    }


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int index, String name) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipes_widget);

        if (isConnectionExist(context)) {

            views.setViewVisibility(R.id.widget_prev_recipes_btn, View.VISIBLE);
            views.setViewVisibility(R.id.widget_next_recipes_btn, View.VISIBLE);
            views.setViewVisibility(R.id.error_image, View.GONE);

            // set recipes name in widget
            views.setTextViewText(R.id.widget_recipes_name, name);

            // make user able to navigate recipes ingredient from widget
            Intent nextRecipe = new Intent(context, RecipesWidget.class);
            nextRecipe.setAction(DISPLAY_NEXT_RECIPE_ACTION);
            nextRecipe.putExtra("next", mRecipesListIndex);

            PendingIntent displayNextPendingIntent = PendingIntent.getBroadcast(context, 0, nextRecipe,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            Intent prevRecipe = new Intent(context, RecipesWidget.class);
            prevRecipe.setAction(DISPLAY_PREV_RECIPE_ACTION);
            prevRecipe.putExtra("prev", mRecipesListIndex);

            PendingIntent displayPrevPendingIntent = PendingIntent.getBroadcast(context, 0, prevRecipe, PendingIntent.FLAG_UPDATE_CURRENT);

            // open detail activity by clicking on recipes name
            Intent openRecipeDetail = new Intent(context, DetailRecipesActivity.class);
            openRecipeDetail.setAction("WIDGET");
            openRecipeDetail.putExtra("recipesId", index);
            openRecipeDetail.putExtra("recipesName", name);
            openRecipeDetail.putParcelableArrayListExtra("recipeArray", mRecipesDetail);

            PendingIntent showDetail = PendingIntent.getActivity(context, 0, openRecipeDetail, PendingIntent.FLAG_CANCEL_CURRENT);

            // handle clicking on each view that is clickable
            views.setOnClickPendingIntent(R.id.widget_next_recipes_btn, displayNextPendingIntent);
            views.setOnClickPendingIntent(R.id.widget_prev_recipes_btn, displayPrevPendingIntent);
            views.setOnClickPendingIntent(R.id.widget_recipes_name, showDetail);

            // Set the ListViewWidgetService intent to act as the adapter for the ListView
            Intent intent = new Intent(context, ListViewWidgetService.class);
            intent.putExtra("id", mRecipesListIndex);

            // update ListView data
            appWidgetManager.notifyAppWidgetViewDataChanged
                    (appWidgetId, R.id.widget_ingredient_detail);

            // set adapter to connect data with ListView
            views.setRemoteAdapter(R.id.widget_ingredient_detail, intent);

        } else {
            views.setViewVisibility(R.id.widget_prev_recipes_btn, View.GONE);
            views.setViewVisibility(R.id.widget_next_recipes_btn, View.GONE);
            views.setViewVisibility(R.id.error_image, View.VISIBLE);

        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        mContext = context;

        if (isConnectionExist(context))
            fetchRecipesData(appWidgetManager, appWidgetIds);
        else
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(mContext, appWidgetManager, appWidgetId, -1, "");
            }
    }

    private void fetchRecipesData(AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        InitializeRetroFit.RecipesAPI recipesAPI = getClient().create(InitializeRetroFit.RecipesAPI.class);
        Call<ArrayList<RecipesDetail>> recipesList = recipesAPI.getRecipesDetail();

        recipesList.enqueue(new Callback<ArrayList<RecipesDetail>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<RecipesDetail>> call, @NonNull Response<ArrayList<RecipesDetail>> response) {
                mRecipesDetail = response.body();
                prepareData(mRecipesListIndex, appWidgetManager, appWidgetIds);
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<RecipesDetail>> call, @NonNull Throwable t) {
                Log.e(GAT, "error while get data from server : " + t.getMessage());
            }
        });
    }


    private void prepareData(int index, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        String name = mRecipesDetail.get(index).getName();
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(mContext, appWidgetManager, appWidgetId, index, name);
        }
    }

    private static boolean isConnectionExist(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connManager != null) {
            networkInfo = connManager.getActiveNetworkInfo();
        }

        return networkInfo != null && networkInfo.isConnected();
    }
}

