package com.example.lenovo.recipes.adapterUtile;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.recipes.DetailRecipesActivity;
import com.example.lenovo.recipes.R;

import java.util.ArrayList;

import static com.example.lenovo.recipes.MainRecipesActivity.RECIPES_ID;
import static com.example.lenovo.recipes.MainRecipesActivity.RECIPES_NAME;

public class RecipesNameAdapter extends RecyclerView.Adapter<RecipesNameAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> mNameList;
    private OnNameClick mListener;

    public interface OnNameClick {
        void onNameListItemClick(int position, String name);
    }

    public RecipesNameAdapter(Context context, ArrayList<String> name, OnNameClick clickListener) {
        mContext = context;
        mNameList = name;
        mListener = clickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.main_fragment_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.name_tv.setText(mNameList.get(i));

    }

    @Override
    public int getItemCount() {
        return mNameList.size();
    }

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView name_tv;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        name_tv = itemView.findViewById(R.id.recipes_name);
        name_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int index = getAdapterPosition();
        String name = name_tv.getText().toString();
        mListener.onNameListItemClick(index, name);
    }
}
}
