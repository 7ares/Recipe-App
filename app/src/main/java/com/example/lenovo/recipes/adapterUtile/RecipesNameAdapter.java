package com.example.lenovo.recipes.adapterUtile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.recipes.DetailRecipesActivity;
import com.example.lenovo.recipes.R;
import com.example.lenovo.recipes.recipesDetail.StepsDetail;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import static com.example.lenovo.recipes.MainRecipesActivity.RECIPES_ID;
import static com.example.lenovo.recipes.MainRecipesActivity.RECIPES_NAME;

public class RecipesNameAdapter extends RecyclerView.Adapter<RecipesNameAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> mNameList;
    private OnNameClick mListener;
    private ArrayList<String> mRecipesFinalResult;

    public interface OnNameClick {
        void onNameListItemClick(int position, String name);
    }

    public RecipesNameAdapter(Context context, ArrayList<String> results, ArrayList<String> name, OnNameClick clickListener) {
        mContext = context;
        mNameList = name;
        mListener = clickListener;
        mRecipesFinalResult = results;
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
        viewHolder.mPicShow.setPlayWhenReady(true);
        preparingPLayer(mRecipesFinalResult.get(i), viewHolder.mPicShow);

    }

    @Override
    public int getItemCount() {
        return mNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name_tv;
        private SimpleExoPlayer mPicShow;
        PlayerView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name_tv = itemView.findViewById(R.id.recipes_name);
            pic = itemView.findViewById(R.id.pic_view);
            mPicShow = ExoPlayerFactory.newSimpleInstance(mContext);

            pic.setPlayer(mPicShow);


            name_tv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            String name = name_tv.getText().toString();
            mListener.onNameListItemClick(index, name);
        }
    }

    private void preparingPLayer(String video, SimpleExoPlayer player) {

        com.google.android.exoplayer2.upstream.DataSource.Factory
                dataSourceFactory = new DefaultDataSourceFactory(mContext, Util.getUserAgent(mContext,
                mContext.getString(R.string.app_name)));

        MediaSource tutorialVideo = new ExtractorMediaSource.
                Factory(dataSourceFactory).createMediaSource(
                Uri.parse(video));


        player.prepare(tutorialVideo);

    }
}
