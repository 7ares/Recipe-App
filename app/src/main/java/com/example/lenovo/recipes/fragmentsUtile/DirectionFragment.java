package com.example.lenovo.recipes.fragmentsUtile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lenovo.recipes.R;
import com.example.lenovo.recipes.adapterUtile.DirectionAdapter;
import com.example.lenovo.recipes.recipesDetail.RecipesDetail;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DirectionFragment extends Fragment {

    private Context mContext;

    @BindView(R.id.direction_list)
    RecyclerView directionList;

    public DirectionFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.direction_fragment, container, false);
        ButterKnife.bind(this, rootView);

        // receive recipe id to display correct data
        assert getArguments() != null;
        int id = getArguments().getInt("recipesId");
        ArrayList<RecipesDetail> recipes = getArguments().getParcelableArrayList("ArrayList");
        assert recipes != null;
        DirectionAdapter mAdapter = new DirectionAdapter(mContext, recipes, id);

        // set up recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        directionList.setLayoutManager(linearLayoutManager);
        directionList.setAdapter(mAdapter);

        return rootView;
    }
}
