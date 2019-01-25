package com.example.lenovo.recipes.recipesDetail;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class RecipesDetail implements Parcelable {
    private int id, servings;
    private String name, image;
    private ArrayList<IngredientDetail> ingredients = new ArrayList<>();
    private ArrayList<StepsDetail> steps = new ArrayList<>();


    protected RecipesDetail(Parcel in) {
        id = in.readInt();
        servings = in.readInt();
        name = in.readString();
        image = in.readString();
        in.readTypedList(this.ingredients, IngredientDetail.CREATOR);
        in.readTypedList(steps , StepsDetail.CREATOR);
    }

    public static final Creator<RecipesDetail> CREATOR = new Creator<RecipesDetail>() {
        @Override
        public RecipesDetail createFromParcel(Parcel in) {
            return new RecipesDetail(in);
        }

        @Override
        public RecipesDetail[] newArray(int size) {
            return new RecipesDetail[size];
        }
    };

    public int getId() {
        return id;
    }

    public int getServings() {

        return servings;
    }

    public String getName() {

        return name;
    }

    public String getImage() {
        return image;
    }

    public ArrayList<IngredientDetail> getIngredients() {
        return ingredients;
    }

    public ArrayList<StepsDetail> getSteps() {
        return steps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(servings);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
    }
}
