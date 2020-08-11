package com.example.bybocam.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bybocam.Adapter.ProfileRecyclerAdapter;
import com.example.bybocam.Adapter.ProfileRecyclerViewAdapter;
import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Interface.DeletePost;
import com.example.bybocam.Model.AddComentModel;
import com.example.bybocam.Model.GetAllPostModel;
import com.example.bybocam.Model.ResponseData;
import com.example.bybocam.Model.ViewProfileModel;
import com.example.bybocam.Model.ViewProfileOurUserModel;
import com.example.bybocam.Model.ViewThumbnails;
import com.example.bybocam.R;
import com.example.bybocam.URL.RetrofitClientIntance;
import com.example.bybocam.Utils.Common;
import com.example.bybocam.Utils.SessionManager;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OtherUserProfile extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    RecyclerView recyclerView, recyclerView2;
    ImageView listViewRecycler, GridViewRecycler, listViewRecycler2, GridViewRecycler2, profile_image, settingProfile,img_dot;
    LinearLayout linearLayout1, linearLayout2;
    TextView NameProfile, userEmailProfile, postText, noOfFollowersOther, noOfFollowing, no_video_found_other, textFollowers;
    Retrofit retrofit;
    String userID, email, urlVideo1, urlVideo2, urlVideo3;
    SessionManager session;
    ProgressBar progress;
    public static ProgressBar progress1;
    Uri uri1, uri2, uri3;
    static Context context;
    public String videoName1, videoName2, videoName3, videoId1, videoId2, videoId3;
    ProfileRecyclerViewAdapter adapter21, adapter22, adapter23;
    int pos;
    int vID = 0;
    String id;
    SwipeRefreshLayout refreshOther;
    ViewProfileOurUserModel data;
    private String myInt, type;
    private String reciverId,LikeStatus;

    SimpleExoPlayer player1, player2, player3;
    SimpleExoPlayerView simpleExoPlayerView;
    TextView dialog_title, dialog_message;
    Button follow,block, cancel;

    @Override
    protected void onStart() {
        super.onStart();
//        initializePlayer();
    }

    private void initializePlayer1(String videoName) {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        //Initialize the player
        player1 = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        //Initialize simpleExoPlayerView
        SimpleExoPlayerView simpleExoPlayerView = findViewById(R.id.exoplayer1);
        simpleExoPlayerView.setVisibility(View.VISIBLE);
        simpleExoPlayerView.setPlayer(player1);
        simpleExoPlayerView.setUseController(false);


        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, Util.getUserAgent(this, "CloudinaryExoplayer"));

        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        // This is the MediaSource representing the media to be played.
        Uri videoUri = Uri.parse(Common.video_base_url + videoName);
        MediaSource videoSource = new ExtractorMediaSource(videoUri,
                dataSourceFactory, extractorsFactory, null, null);

        // Prepare the player with the source.
        player1.prepare(videoSource);
        player1.setVolume(0f);
        player1.setPlayWhenReady(true);
        player1.setRepeatMode(Player.REPEAT_MODE_ALL);
    }

    private void initializePlayer2(String videoName) {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        //Initialize the player
        player2 = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        //Initialize simpleExoPlayerView
        SimpleExoPlayerView simpleExoPlayerView = findViewById(R.id.exoplayer2);
        simpleExoPlayerView.setVisibility(View.VISIBLE);
        simpleExoPlayerView.setPlayer(player2);
        simpleExoPlayerView.setUseController(false);


        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, Util.getUserAgent(this, "CloudinaryExoplayer"));

        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        // This is the MediaSource representing the media to be played.
        Uri videoUri = Uri.parse(Common.video_base_url + videoName);
        MediaSource videoSource = new ExtractorMediaSource(videoUri,
                dataSourceFactory, extractorsFactory, null, null);

        // Prepare the player with the source.
        player2.prepare(videoSource);
        player2.setVolume(0f);
        player2.setPlayWhenReady(true);
        player2.setRepeatMode(Player.REPEAT_MODE_ALL);
    }

    private void initializePlayer3(String videoName) {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        //Initialize the player
        player3 = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        //Initialize simpleExoPlayerView
        SimpleExoPlayerView simpleExoPlayerView = findViewById(R.id.exoplayer3);
        simpleExoPlayerView.setVisibility(View.VISIBLE);
        simpleExoPlayerView.setPlayer(player3);
        simpleExoPlayerView.setUseController(false);

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, Util.getUserAgent(this, "CloudinaryExoplayer"));

        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        // This is the MediaSource representing the media to be played.
        Uri videoUri = Uri.parse(Common.video_base_url + videoName);
        MediaSource videoSource = new ExtractorMediaSource(videoUri,
                dataSourceFactory, extractorsFactory, null, null);



        // Prepare the player with the source.
        player3.prepare(videoSource);
        player3.setVolume(0f);
        player3.setPlayWhenReady(true);
        player3.setRepeatMode(Player.REPEAT_MODE_ALL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);
        initialUi();
        session = new SessionManager(OtherUserProfile.this.getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);
        userID = user.get(SessionManager.USER_ID);

        Intent intent = getIntent();
        if (intent != null) {
            myInt = intent.getStringExtra("userID");
            type = intent.getStringExtra("type");
            LikeStatus=intent.getStringExtra("likestatus");

        }
        if ((myInt != null) && (type.matches("0"))) {
            if (Common.isNetworkConnected(OtherUserProfile.this)) {
//                Log.e("myint",myInt);
//                Log.e("myid",userID);
                viewDataHitApiOther();


            } else {
                progress.setVisibility(View.INVISIBLE);
                Toast.makeText(OtherUserProfile.this, "Check your network", Toast.LENGTH_SHORT).show();
            }


        } else {
            if (Common.isNetworkConnected(OtherUserProfile.this)) {


            } else {
                progress.setVisibility(View.INVISIBLE);
                Toast.makeText(OtherUserProfile.this, "Check your network", Toast.LENGTH_SHORT).show();
            }

        }


        img_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendialog();
            }
        });

        listViewRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileRecyclerAdapter adapter = new ProfileRecyclerAdapter(OtherUserProfile.this, data, 1,deletePost);
                recyclerView.setHasFixedSize(true);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(OtherUserProfile.this));
                recyclerView.setAdapter(adapter);
            }
        });

        listViewRecycler2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout2.setVisibility(View.INVISIBLE);
                linearLayout1.setVisibility(View.VISIBLE);
                ProfileRecyclerAdapter adapter = new ProfileRecyclerAdapter(OtherUserProfile.this, data, 1,deletePost);
                recyclerView.setHasFixedSize(true);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(OtherUserProfile.this));
                recyclerView.setAdapter(adapter);
            }
        });

        GridViewRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout1.setVisibility(View.INVISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);
                ProfileRecyclerAdapter adapter = new ProfileRecyclerAdapter(OtherUserProfile.this, data, 0,deletePost);
                recyclerView.setHasFixedSize(true);
                recyclerView.setNestedScrollingEnabled(false);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(OtherUserProfile.this, 2);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(adapter);
            }
        });

        GridViewRecycler2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout1.setVisibility(View.INVISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);
                ProfileRecyclerAdapter adapter = new ProfileRecyclerAdapter(OtherUserProfile.this, data, 0,deletePost);
                recyclerView.setHasFixedSize(true);
                recyclerView.setNestedScrollingEnabled(false);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(OtherUserProfile.this, 2);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(adapter);
            }
        });
        settingProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void opendialog() {
        final Dialog alertDialog = new Dialog(OtherUserProfile.this);
        LayoutInflater inflater = OtherUserProfile.this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.custom_layout_dialog2, null);

        dialog_title = add_menu_layout.findViewById(R.id.dialog_title);
        dialog_message = add_menu_layout.findViewById(R.id.dialog_message);
        cancel=add_menu_layout.findViewById(R.id.cancel_btn);
        block=add_menu_layout.findViewById(R.id.block_btn);
        follow=add_menu_layout.findViewById(R.id.follow_btn);

        if (LikeStatus!=null){
            if (LikeStatus.equalsIgnoreCase("1")){
                follow.setText("UnFollow");
            }else {
                follow.setText("Follow");
            }
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlockUserApi();
                alertDialog.cancel();
            }
        });

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LikeStatus.equalsIgnoreCase("1")){
                    HitFollowUnFollowApi("0");
                    follow.setText("Follow");

                }else {
                    HitFollowUnFollowApi("1");
                    follow.setText("UnFollow");
                }

            }
        });

        alertDialog.setContentView(add_menu_layout);
        alertDialog.show();
    }

    private void HitFollowUnFollowApi(String status) {
        retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<AddComentModel> call = apiListener.likeDislikeclassAPI(userID,reciverId,status);
        call.enqueue(new Callback<AddComentModel>() {
            @Override
            public void onResponse(Call<AddComentModel> call, Response<AddComentModel> response) {
                if (response.isSuccessful()) {
                    AddComentModel data = response.body();
                    if (data.getCode().equalsIgnoreCase("201")) {

                        Toast.makeText(OtherUserProfile.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.INVISIBLE);
                        if (LikeStatus.equalsIgnoreCase("1")){
                            LikeStatus="0";
                        }else {
                            LikeStatus="1";
                        }


                    } else {
                        progress.setVisibility(View.INVISIBLE);
                    }
                }

            }

            @Override
            public void onFailure(Call<AddComentModel> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
            }
        });

    }


    private void BlockUserApi( ) {
        retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<ResponseData> call = apiListener.BlockUser(userID,myInt);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData data = response.body();
                    if (data.getCode().equalsIgnoreCase("201")) {

                        Toast.makeText(OtherUserProfile.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.INVISIBLE);
                        finish();



                    } else {
                        progress.setVisibility(View.INVISIBLE);
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
            }
        });

    }


    DeletePost deletePost=new DeletePost() {
        @Override
        public void getpost(String postid) {

        }
    };




    private void viewDataHitApiOther() {
        if (refreshOther.isRefreshing()) {
            refreshOther.setRefreshing(false);
        }

     //   Toast.makeText(OtherUserProfile.this, ""+userID, Toast.LENGTH_SHORT).show();

        retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<ViewProfileOurUserModel> call = apiListener.viewProfileOurUserAPI(myInt, userID);
        call.enqueue(new Callback<ViewProfileOurUserModel>() {
            @Override
            public void onResponse(Call<ViewProfileOurUserModel> call, Response<ViewProfileOurUserModel> response) {
                progress.setVisibility(View.INVISIBLE);
                if (response.isSuccessful()) {
                    if (refreshOther.isRefreshing()) {
                        refreshOther.setRefreshing(false);
                    }

                    data = response.body();
                    if (data != null) {

                        if (data.getCode().equalsIgnoreCase("201")) {
                            if (data.getData().get(0).getUserName() != null) {
                                NameProfile.setText(data.getData().get(0).getUserName());

                            }

                            if (data.getData().get(0).getUserId() != null) {
                               reciverId= data.getData().get(0).getUserId();
                            }

                            if (data.getData().get(0).getLikeStatus() != null) {
                                reciverId= data.getData().get(0).getLikeStatus();
                            }

//                            if (data.getData().get(0).getLikeStatus() != null) {
//                                LikeStatus= data.getData().get(0).getLikeStatus();
//
//                            }
                            if (data.getData().get(0).getEmail() != null) {
                                userEmailProfile.setText(data.getData().get(0).getEmail());

                            }
                            if (data.getData().get(0).getUserImage() != null) {
                                String imageUri = Common.image_base_url + data.getData().get(0).getUserImage();
                                Uri uri = Uri.parse(imageUri);
                                Picasso.get().load(uri).into(profile_image);

                            }
                            Integer noOfFollowersOther1 = data.getTotalfavouriteusers();
                            if (noOfFollowersOther1 < 0) {
                                noOfFollowing.setText("0");
                            } else {
                                noOfFollowing.setText(String.valueOf(noOfFollowersOther1));
                            }
                            Integer followers = data.getFollowers();
                            if (followers < 0) {
                                noOfFollowersOther.setText("0");
                            } else {
                                noOfFollowersOther.setText(String.valueOf(followers));
                            }
                            if (data.getPostData().size() == 0) {
                                postText.setText("User have no posts");
                                postText.setVisibility(View.VISIBLE);
                            }
                            else {
                                postText.setVisibility(View.INVISIBLE);
                            }
                            ProfileRecyclerAdapter adapter = new ProfileRecyclerAdapter(OtherUserProfile.this, data, 1,deletePost);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setNestedScrollingEnabled(false);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OtherUserProfile.this, LinearLayoutManager.VERTICAL, false);
                            linearLayoutManager.setReverseLayout(true);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(adapter);

                        }
                    }
                    progress1.setVisibility(View.VISIBLE);
                    viewUserVideosOther();
                }
            }

            @Override
            public void onFailure(Call<ViewProfileOurUserModel> call, Throwable t) {
                if (refreshOther.isRefreshing()) {
                    refreshOther.setRefreshing(false);
                }
                Toast.makeText(OtherUserProfile.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void viewUserVideosOther() {
        if (refreshOther.isRefreshing()) {
            refreshOther.setRefreshing(false);
        }
        try {
            retrofit = RetrofitClientIntance.retroInit();
            final ApiListener apiListener = retrofit.create(ApiListener.class);
            Call<ViewProfileModel> call = apiListener.viewProfileAPI(myInt);
            call.enqueue(new Callback<ViewProfileModel>() {
                @Override
                public void onResponse(Call<ViewProfileModel> call, Response<ViewProfileModel> response) {
                    if (response.isSuccessful()) {
                        if (refreshOther.isRefreshing()) {
                            refreshOther.setRefreshing(false);
                        }
                        ViewProfileModel data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {
                                progress1.setVisibility(View.INVISIBLE);
                                if (data.getUserVideos().size() >= 1) {
                                    videoName1 = data.getUserVideos().get(0).getVideoName();
                                    videoId1 = data.getUserVideos().get(0).getId();
                                    initializePlayer1(videoName1);
                                } else {
                                    no_video_found_other.setVisibility(View.VISIBLE);
                                }
                                if (data.getUserVideos().size() >= 2) {
                                    videoName2 = data.getUserVideos().get(1).getVideoName();
                                    videoId2 = data.getUserVideos().get(1).getId();
                                    initializePlayer2(videoName2);
                                }
                                if (data.getUserVideos().size() >= 3) {
                                    videoName3 = data.getUserVideos().get(2).getVideoName();
                                    videoId3 = data.getUserVideos().get(2).getId();
                                    initializePlayer3(videoName3);

                                }
                            } else {
                                Toast.makeText(OtherUserProfile.this, "User have no videos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ViewProfileModel> call, Throwable t) {
                    Toast.makeText(OtherUserProfile.this, "failed to load user data", Toast.LENGTH_SHORT).show();
                    progress1.setVisibility(View.INVISIBLE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    private void initialUi() {
        img_dot=findViewById(R.id.img_dot);
        recyclerView = findViewById(R.id.myProfileRecyclerview);
        listViewRecycler = findViewById(R.id.listViewRecycler);
        GridViewRecycler = findViewById(R.id.GridViewRecycler);
        linearLayout1 = findViewById(R.id.gridlistlinear1);
        linearLayout2 = findViewById(R.id.gridlistlinear2);
        listViewRecycler2 = findViewById(R.id.listViewRecycler2);
        GridViewRecycler2 = findViewById(R.id.GridViewRecycler2);
        NameProfile = findViewById(R.id.NameProfile);
        userEmailProfile = findViewById(R.id.userEmailProfile);
        progress = findViewById(R.id.progress);
        progress1 = findViewById(R.id.progress2);
        profile_image = findViewById(R.id.profile_image);
        recyclerView2 = findViewById(R.id.profile_recyler);
        refreshOther = findViewById(R.id.refreshOther);
        refreshOther.setOnRefreshListener(OtherUserProfile.this);
        settingProfile = findViewById(R.id.settingProfile);
        postText = findViewById(R.id.postText);
        noOfFollowersOther = findViewById(R.id.noOfFollowersOther);
        textFollowers = findViewById(R.id.textFollowers);
        noOfFollowing = findViewById(R.id.noOfFollowing);
        no_video_found_other = findViewById(R.id.no_video_found_other);

    }

    @Override
    public void onRefresh() {
        if (myInt != null) {
            viewDataHitApiOther();
        } else {
            Toast.makeText(context, "Something is worng", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player1 != null) {
            player1.setRepeatMode(Player.REPEAT_MODE_OFF);
            player1.release();
            player1.stop();
            player1 = null;

        }
        if (player2 != null) {
            player2.setRepeatMode(Player.REPEAT_MODE_OFF);
            player2.release();
            player2.stop();
            player2 = null;

        }
        if (player3 != null) {
            player3.setRepeatMode(Player.REPEAT_MODE_OFF);
            player3.release();
            player3.stop();
            player3 = null;

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (player1 != null) {
            player1.setRepeatMode(Player.REPEAT_MODE_OFF);
            player1.release();
            player1.stop();
            player1 = null;

        }
        if (player2 != null) {
            player2.setRepeatMode(Player.REPEAT_MODE_OFF);
            player2.release();
            player2.stop();
            player2 = null;

        }
        if (player3 != null) {
            player3.setRepeatMode(Player.REPEAT_MODE_OFF);
            player3.release();
            player3.stop();
            player3 = null;

        }
    }
}