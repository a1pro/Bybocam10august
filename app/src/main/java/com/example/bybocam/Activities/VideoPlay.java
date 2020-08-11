package com.example.bybocam.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bybocam.R;
import com.example.bybocam.Utils.Common;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import static com.google.android.exoplayer2.Player.STATE_BUFFERING;
import static com.google.android.exoplayer2.Player.STATE_IDLE;
import static com.google.android.exoplayer2.Player.STATE_READY;


public class VideoPlay extends AppCompatActivity {


    private static String VIDEO_PATH = null;
    private static String VIDEO_PATH2 = null;
    private static String VIDEO_PATH3 = null;
    SimpleExoPlayer player;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_play);
        progress = findViewById(R.id.progress);

        if (Common.isNetworkConnected(VideoPlay.this)) {

        } else {
            Toast.makeText(VideoPlay.this, "Check your network", Toast.LENGTH_SHORT).show();
        }

        Intent callingActivityIntent = this.getIntent();
        if (callingActivityIntent != null) {
            String videoUri = callingActivityIntent.getStringExtra("videoNameProfile");
            if (videoUri != null) {
                VIDEO_PATH2 = videoUri;
                initializePlayer(VIDEO_PATH2,null);
                Log.e("VIDEO_PATH2", VIDEO_PATH2);
            }
            String videoUri1 = callingActivityIntent.getStringExtra("videoNameRecommendation");
            if (videoUri1 != null) {
                VIDEO_PATH3 = videoUri1;
                initializePlayer(VIDEO_PATH3,null);
                Log.e("VIDEO_PATH3", VIDEO_PATH3);
            }
            String videoUri2 = callingActivityIntent.getStringExtra("videoName");
            if (videoUri2 != null) {
                VIDEO_PATH = videoUri2;
                initializePlayer(null,VIDEO_PATH);
                Log.e("VIDEO_PATH", VIDEO_PATH);
            }
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
       // initializePlayer();

    }

    private void initializePlayer(String path,String postPath) {
        // Create a default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        //Initialize the player
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        //Initialize simpleExoPlayerView
        SimpleExoPlayerView simpleExoPlayerView = findViewById(R.id.exoplayer);
        simpleExoPlayerView.setPlayer(player);


        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, Util.getUserAgent(this, "CloudinaryExoplayer"));

        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        // This is the MediaSource representing the media to be played.
        Uri videoUri = Uri.parse(Common.image_post_url + postPath);
        Uri videoUri1 = Uri.parse(Common.video_base_url + path);

        if (path != null) {
            MediaSource videoSource = new ExtractorMediaSource(videoUri1,
                    dataSourceFactory, extractorsFactory, null, null);
            // Prepare the player with the source.
            player.prepare(videoSource);
            player.setPlayWhenReady(true);
        } else if (postPath != null) {

            MediaSource videoSource = new ExtractorMediaSource(videoUri,
                    dataSourceFactory, extractorsFactory, null, null);
            // Prepare the player with the source.
            player.prepare(videoSource);
            player.setPlayWhenReady(true);
        }

        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                if (isLoading) {
                    progress.setVisibility(View.VISIBLE);
                } else {
                    progress.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case STATE_BUFFERING:
                        progress.setVisibility(View.VISIBLE);
                        break;
                    case STATE_READY:
                        progress.setVisibility(View.INVISIBLE);
                        break;
                    case STATE_IDLE:
                        progress.setVisibility(View.INVISIBLE);
                        break;
                }

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });


    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}