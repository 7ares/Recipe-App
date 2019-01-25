package com.example.lenovo.recipes.fragmentsUtile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lenovo.recipes.MainRecipesActivity;
import com.example.lenovo.recipes.R;
import com.example.lenovo.recipes.adapterUtile.IngredientAdapter;
import com.example.lenovo.recipes.recipesDetail.RecipesDetail;

import java.util.ArrayList;

public class IngredientFragment extends Fragment {

    private IngredientAdapter mAdapter;

    // create default constructor to can initiate an instance of this fragment
    public IngredientFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.ingredient_fragment, container, false);
        // get recipe id to display correct data
        int id = getArguments() != null ? getArguments().getInt(MainRecipesActivity.RECIPES_ID) : 0;
        ArrayList<RecipesDetail> recipes = getArguments().getParcelableArrayList("ArrayList");
        mAdapter = new IngredientAdapter(getContext(),recipes ,id);
        // setup recycler view
        RecyclerView ingredientList = rootView.findViewById(R.id.ingredient_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        ingredientList.setLayoutManager(linearLayoutManager);
        ingredientList.setAdapter(mAdapter);


        return rootView;
    }
}
