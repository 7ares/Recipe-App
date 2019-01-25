package com.example.lenovo.recipes.adapterUtile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lenovo.recipes.DetailRecipesActivity;
import com.example.lenovo.recipes.MainRecipesActivity;
import com.example.lenovo.recipes.R;
import com.example.lenovo.recipes.recipesDetail.IngredientDetail;
import com.example.lenovo.recipes.recipesDetail.RecipesDetail;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<IngredientDetail> mIngredientList;

    public IngredientAdapter(Context context, ArrayList<RecipesDetail> recipes, int i) {
        mContext = context;
        mIngredientList = recipes.get(i).getIngredients();
    }

    @NonNull
    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        int layout = R.layout.ingredients_fragments_item;
        View view = layoutInflater.inflate(layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.ViewHolder viewHolder, int i) {
        IngredientDetail ingredientDetail = mIngredientList.get(i);

        if (ingredientDetail.getIngredient() != null)
            viewHolder.ingredient_tv.setText(ingredientDetail.getIngredient());

        if (ingredientDetail.getMeasure() != null)
            viewHolder.measureUnite_tv.setText(ingredientDetail.getMeasure());
        viewHolder.quantity_tv.setText(String.valueOf(ingredientDetail.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return mIngredientList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.measure)
        TextView measureUnite_tv;
        @BindView(R.id.quantity)
        TextView quantity_tv;
        @BindView(R.id.ingredient)
        TextView ingredient_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
