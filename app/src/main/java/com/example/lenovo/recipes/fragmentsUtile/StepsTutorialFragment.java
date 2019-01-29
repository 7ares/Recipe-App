package com.example.lenovo.recipes.fragmentsUtile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lenovo.recipes.NetworkUtile.InitializeRetroFit;
import com.example.lenovo.recipes.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.lenovo.recipes.DetailRecipesActivity.DEFAULT_VIDEO_KEY;
import static com.example.lenovo.recipes.DetailRecipesActivity.VIDEO_KEY;


public class StepsTutorialFragment extends Fragment {
    private static final String TAG = "tutorial";

    public static SimpleExoPlayer player;
    public static String videoUrl;


    private Long mBeginFrom;
    private Long mDuration;
    private Context mContext;

    @BindView(R.id.video_view)
    PlayerView mExoPlayerView;
    @BindView(R.id.netwrok_error_while_loading)
    ConstraintLayout mConstrainLayout;
    @BindView(R.id.reload)
    ImageView mReload;

    public StepsTutorialFragment() {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.direction_videos_fragment, container, false);
        ButterKnife.bind(this, rootView);

        player = ExoPlayerFactory.newSimpleInstance(mContext);
        player.addListener(listener);
        mExoPlayerView.setPlayer(player);

        if (getArguments() != null) {
            // use this argument when user begin switches between videos
            videoUrl = getArguments().getString(VIDEO_KEY);
            // use this argument when app first play so play this default video
            if (videoUrl == null || videoUrl.length() == 0)
                videoUrl = getArguments().getString(DEFAULT_VIDEO_KEY);
        }

        mReload.setOnClickListener(v -> {
            if (InitializeRetroFit.checkNetwork(mContext)) {
                mConstrainLayout.setVisibility(View.GONE);
                preparingPLayer(videoUrl);
            }
        });


        //check network connection is exist
        if (InitializeRetroFit.checkNetwork(mContext)) {
            if (savedInstanceState != null) {
                mBeginFrom = savedInstanceState.getLong("seekTo");
                mDuration = savedInstanceState.getLong("duration");
                videoUrl = savedInstanceState.getString("videoLink");

                if (mBeginFrom > 0 && mBeginFrom < mDuration) {
                    preparingPLayer(videoUrl);
                    player.seekTo(mBeginFrom);
                    player.setPlayWhenReady(true);
                } else
                    preparingPLayer(videoUrl);

            } else {
                if (videoUrl != null)
                    preparingPLayer(videoUrl);
            }
        }
        // display error message if connection lost
        else {
            mConstrainLayout.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("videoLink", videoUrl);
        int PLAYER_READY_TO_PLAY = 3;

        if (player.getPlaybackState() == PLAYER_READY_TO_PLAY) {
            mBeginFrom = player.getCurrentPosition();
            mDuration = player.getDuration();
            outState.putLong("seekTo", mBeginFrom);
            outState.putLong("duration", mDuration);

        }

    }


    private void preparingPLayer(String video) {
        com.google.android.exoplayer2.upstream.DataSource.Factory
                dataSourceFactory = new DefaultDataSourceFactory(mContext, Util.getUserAgent(mContext,
                getString(R.string.app_name)));

        MediaSource tutorialVideo = new ExtractorMediaSource.
                Factory(dataSourceFactory).createMediaSource(
                Uri.parse(video));


        player.prepare(tutorialVideo);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
    }


    Player.EventListener listener = new Player.EventListener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == 1)
                mConstrainLayout.setVisibility(View.VISIBLE);
        }
    };

}
