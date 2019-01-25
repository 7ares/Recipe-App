package com.example.lenovo.recipes.fragmentsUtile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lenovo.recipes.NetworkUtile.InitializeRetroFit;
import com.example.lenovo.recipes.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
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
        Log.i(TAG, "create view");

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
                Log.i(TAG, "get used");
                mBeginFrom = savedInstanceState.getLong("seekTo");
                mDuration = savedInstanceState.getLong("duration");
                videoUrl = savedInstanceState.getString("videoLink");
                Log.i(TAG, videoUrl);

                if (mBeginFrom > 0 && mBeginFrom < mDuration) {
                    preparingPLayer(videoUrl);
                    player.seekTo(mBeginFrom);
                    player.setPlayWhenReady(true);
                } else
                    preparingPLayer(videoUrl);

            } else {
                Log.i(TAG, "new Create");
                if (videoUrl != null)
                    preparingPLayer(videoUrl);
                Log.i(TAG, "bundle null");
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

            Log.i(TAG, "save " + videoUrl);
            Log.e(TAG, "save instance " + "getAccess");
            Log.e(TAG, "is it work" + player.getPlayWhenReady() + "");
            Log.e(TAG, "duration" + mBeginFrom + "");
            Log.e(TAG, "Duratoin orgin" + mDuration + "");
        }

    }


    private void preparingPLayer(String video) {
        Log.i(TAG, "video");

        com.google.android.exoplayer2.upstream.DataSource.Factory
                dataSourceFactory = new DefaultDataSourceFactory(mContext, Util.getUserAgent(mContext,
                getString(R.string.app_name)));

        MediaSource tutorialVideo = new ExtractorMediaSource.
                Factory(dataSourceFactory).createMediaSource(
                Uri.parse(video));


        player.prepare(tutorialVideo);

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "Start");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "pause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "Stoped");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "destroy");
        player.release();
    }


    Player.EventListener listener = new Player.EventListener() {
        @Override
        public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {
            Log.i(TAG, "time line changed " + timeline);
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            Log.i(TAG, "tracks changed " + trackSelections);
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            Log.i(TAG, "on loading changed: " + isLoading);

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            Log.i(TAG, "on player state changed : " + playWhenReady + " : " + playbackState);
            if (playbackState == 1)
                mConstrainLayout.setVisibility(View.VISIBLE);
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
            Log.i(TAG, "on repeadt mode");
        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            Log.i(TAG, "shuffle mode");
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            Log.i(TAG, "error : " + error.getMessage());
        }

        @Override
        public void onPositionDiscontinuity(int reason) {
            Log.i(TAG, "onposition changed");
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            Log.i(TAG, "on play back ");
        }

        @Override
        public void onSeekProcessed() {
            Log.i(TAG, "on seek processed");
        }
    };

}
