package com.example.lenovo.recipes.adapterUtile;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.recipes.DetailRecipesActivity;
import com.example.lenovo.recipes.R;
import com.example.lenovo.recipes.recipesDetail.RecipesDetail;
import com.example.lenovo.recipes.recipesDetail.StepsDetail;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static com.example.lenovo.recipes.DetailRecipesActivity.VIDEO_KEY;


public class DirectionAdapter extends RecyclerView.Adapter<DirectionAdapter.ViewHolder> {
    String TAG = "DirectionAdapter";
    private Context mContext;
    private ArrayList<StepsDetail> mStepsList;


    public DirectionAdapter(Context context, ArrayList<RecipesDetail> recipes, int i) {
        mContext = context;
        mStepsList = recipes.get(i).getSteps();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.direction_fragment_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        StepsDetail stepsDetail = mStepsList.get(i);
        int mStepId = stepsDetail.getId();

        viewHolder.mVideoLink = stepsDetail.getVideoURL();

        // if this step contain tutorial video so we show button to make user able to see it
        // if not make play btn invisible
        viewHolder.isPlayBtnVisible = viewHolder.mVideoLink.length() > 0;

        String shortDescription = stepsDetail.getShortDescription();
        String fullDescription = stepsDetail.getDescription();


        if (shortDescription != null && shortDescription.length() > 0)
            // set step title
            viewHolder.stepShortDescription_tv.setText(mStepsList.get(i).getShortDescription());
        if (fullDescription != null && fullDescription.length() > 0) {
            // i already define the step number so i don't need it again
            int index = fullDescription.indexOf(" ");
            String description = fullDescription.substring(index);
            // set step description
            viewHolder.fullDescription_tv.setText(description);
        }
        // set step number
        viewHolder.numberOfStep_tv.setText(String.valueOf(mStepId));

        viewHolder.hideDetail_imageBtn.setOnClickListener(v -> hideDetail(viewHolder));
        viewHolder.showDetail_imageBtn.setOnClickListener(v -> showDetail(viewHolder));
        viewHolder.play_btn.setOnClickListener(v -> {

            Intent intent = new Intent(mContext, DetailRecipesActivity.class);
            intent.putExtra(VIDEO_KEY, viewHolder.mVideoLink);
            mContext.startActivity(intent);


        });
    }

    @Override
    public int getItemCount() {
        return mStepsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.short_description)
        TextView stepShortDescription_tv;
        @BindView(R.id.description)
        TextView fullDescription_tv;
        @BindView(R.id.show_detail)
        ImageView showDetail_imageBtn;
        @BindView(R.id.hide_detail)
        ImageView hideDetail_imageBtn;
        @BindView(R.id.step_number)
        TextView numberOfStep_tv;
        @BindView(R.id.play_video_btn)
        Button play_btn;

        private String mVideoLink;
        private  boolean isPlayBtnVisible;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    // show all detail about step (description and video if exist)
    private void showDetail(ViewHolder holder) {
        if (holder.isPlayBtnVisible) holder.play_btn.setVisibility(View.VISIBLE);
        holder.fullDescription_tv.setVisibility(View.VISIBLE);
        holder.hideDetail_imageBtn.setVisibility(View.VISIBLE);
        holder.showDetail_imageBtn.setVisibility(GONE);
    }

    // show short description about step title only
    private void hideDetail(ViewHolder holder) {
        holder.fullDescription_tv.setVisibility(GONE);
        holder.hideDetail_imageBtn.setVisibility(GONE);
        holder.showDetail_imageBtn.setVisibility(View.VISIBLE);
        holder.play_btn.setVisibility(GONE);
    }
}
