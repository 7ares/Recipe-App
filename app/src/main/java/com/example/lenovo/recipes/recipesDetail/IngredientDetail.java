package com.example.lenovo.recipes.recipesDetail;

import android.os.Parcel;
import android.os.Parcelable;

public class IngredientDetail implements Parcelable {
    private String  measure , ingredient ;
    private double quantity ;

    protected IngredientDetail(Parcel in) {
        measure = in.readString();
        ingredient = in.readString();
        quantity = in.readDouble();
    }

    public static final Creator<IngredientDetail> CREATOR = new Creator<IngredientDetail>() {
        @Override
        public IngredientDetail createFromParcel(Parcel in) {
            return new IngredientDetail(in);
        }

        @Override
        public IngredientDetail[] newArray(int size) {
            return new IngredientDetail[size];
        }
    };

    public double getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(measure);
        dest.writeString(ingredient);
        dest.writeDouble(quantity);
    }
}
