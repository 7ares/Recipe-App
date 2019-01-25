package com.example.lenovo.recipes.widgetUtiles;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.lenovo.recipes.R;
import com.example.lenovo.recipes.recipesDetail.IngredientDetail;

import java.util.ArrayList;

import static com.example.lenovo.recipes.widgetUtiles.RecipesWidget.mRecipesDetail;
import static com.example.lenovo.recipes.widgetUtiles.RecipesWidget.mRecipesListIndex;

public class ListViewWidgetService extends RemoteViewsService {
    public static String TAG = "ListView ";

    private ArrayList<IngredientDetail> mIngredientList;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewRemoteViewFactory(this.getApplicationContext());

    }

    class ListViewRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
        private Context mContext;

        public ListViewRemoteViewFactory(Context applicationContext) {
            mContext = applicationContext;
        }

        @Override
        public void onCreate() {
            Log.i(TAG, "on create");
        }

        @Override
        public void onDataSetChanged() {
            Log.i(TAG, " set change :" + mRecipesListIndex);
            if (mRecipesDetail != null)
                mIngredientList = RecipesWidget.mRecipesDetail.get(mRecipesListIndex).getIngredients();
        }

        @Override
        public void onDestroy() {
            Log.i(TAG, "DESTROY");
        }

        @Override
        public int getCount() {
            Log.i(TAG, "COUNT");
            if (mIngredientList != null)
                return mIngredientList.size();
            else return 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (mIngredientList != null) {
                IngredientDetail ingredientDetails = mIngredientList.get(position);
                String ingredient = ingredientDetails.getIngredient();
                String measure = ingredientDetails.getMeasure();
                double quantity = ingredientDetails.getQuantity();

                RemoteViews views = new RemoteViews(mContext.getPackageName(),
                        R.layout.widget_ingredient_list_items);
                views.setTextViewText(R.id.widget_ingredient, ingredient);
                views.setTextViewText(R.id.widget_measure, measure);
                views.setTextViewText(R.id.widget_quantity, String.valueOf(quantity));

                Log.i(TAG, ingredient);

                return views;
            } else
                return null;
        }

        @Override
        public RemoteViews getLoadingView() {
            Log.i(TAG, "LOADING DATA");
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}
