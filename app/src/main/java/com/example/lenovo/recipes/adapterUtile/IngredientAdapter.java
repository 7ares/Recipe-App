package com.example.lenovo.recipes.adapterUtile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lenovo.recipes.R;
import com.example.lenovo.recipes.recipesDetail.IngredientDetail;
import com.example.lenovo.recipes.recipesDetail.RecipesDetail;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<IngredientDetail> mIngredientList;
    private ArrayList<String> mUpdateQuantity = new ArrayList<>();

    public IngredientAdapter(Context context, ArrayList<RecipesDetail> recipes, int i) {
        mContext = context;
        mIngredientList = recipes.get(i).getIngredients();
    }

    public IngredientAdapter(Context context, ArrayList<RecipesDetail> recipes, int i, ArrayList<String> quantity) {
        mContext = context;
        mIngredientList = recipes.get(i).getIngredients();
        mUpdateQuantity = quantity;
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
        if (mUpdateQuantity.size() == 0)
            viewHolder.quantity_tv.setText(String.valueOf(ingredientDetail.getQuantity()));
        else
            viewHolder.quantity_tv.setText(adjustQuantityResult(mUpdateQuantity.get(i)));
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

    private String adjustQuantityResult(String quantityValue) {
        int index = quantityValue.indexOf(".");
        if (index == 0) return quantityValue;

        String integerNumber = quantityValue.substring(0, index);
        String currentDecimalNumber = quantityValue.substring(index + 1);
        String decimalNumber;

        if (currentDecimalNumber.length() > 2)
            decimalNumber = quantityValue.substring(index + 1, index + 3);
        else if (currentDecimalNumber.length() == 2) decimalNumber = currentDecimalNumber;
        else decimalNumber = currentDecimalNumber + "0";

        int decimalResult = Integer.valueOf(decimalNumber);

        if (decimalResult < 87) {
            if (decimalResult <= 12) decimalResult = 0;
            else if (decimalResult <= 37) decimalResult = 25;
            else if (decimalResult <= 62) decimalResult = 5;
            else decimalResult = 75;

            return integerNumber + "." + String.valueOf(decimalResult);
        } else {
            int integerResult = Integer.valueOf(integerNumber) + 1;
            return String.valueOf(integerResult) + ".0";
        }
    }
}
