package com.example.bybocam.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bybocam.Activities.OtherUserProfile;
import com.example.bybocam.Activities.VideoPlay;
import com.example.bybocam.Fragments.RecommendationFragment;
import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Model.AddComentModel;
import com.example.bybocam.Model.RecommendationModel;
import com.example.bybocam.R;
import com.example.bybocam.URL.RetrofitClientIntance;
import com.example.bybocam.Utils.Common;
import com.example.bybocam.Utils.SessionManager;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RecommendationRecyclerAdapter extends RecyclerView.Adapter<RecommendationRecyclerAdapter.ViewHolder> {

    public RecommendationModel recommendations;
    Context context;
    String video3, video2, video1, videoName1, videoName2, videoName3;
    String id;
    SessionManager session;
    String userID;
    public boolean like = false;
    String likeStatus21;

    public RecommendationRecyclerAdapter(Context context, RecommendationModel recommendation) {
        this.recommendations = recommendation;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommedation_recycler_layout, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new RecommendationRecyclerAdapter.ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String imageName = recommendations.getData().get(position).getUserImage();
        String username = recommendations.getData().get(position).getUserName();
        final int arraySize = recommendations.getData().get(position).getUserVideos().size();
        if (arraySize == 0) {
            holder.novideofound.setVisibility(View.VISIBLE);
            holder.videoView1.setVisibility(View.INVISIBLE);
            holder.videoView2.setVisibility(View.INVISIBLE);
            holder.videoView3.setVisibility(View.INVISIBLE);
            holder.playVideoIconRecommendation1.setVisibility(View.INVISIBLE);
            holder.playVideoIconRecommendation2.setVisibility(View.INVISIBLE);
            holder.playVideoIconRecommendation3.setVisibility(View.INVISIBLE);
        }
        if (arraySize == 1) {
            video1 = recommendations.getData().get(position).getUserVideos().get(0).getVideoThumbnailimg();
            Glide.with(context.getApplicationContext()).load(Uri.parse(Common.video_base_url + video1))
                    .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(holder.videoView1);
            holder.videoView2.setVisibility(View.INVISIBLE);
            holder.videoView3.setVisibility(View.INVISIBLE);
            holder.novideofound.setVisibility(View.INVISIBLE);
            holder.playVideoIconRecommendation2.setVisibility(View.INVISIBLE);
            holder.playVideoIconRecommendation3.setVisibility(View.INVISIBLE);
        }
        if (arraySize == 2) {
            video1 = recommendations.getData().get(position).getUserVideos().get(0).getVideoThumbnailimg();
            video2 = recommendations.getData().get(position).getUserVideos().get(1).getVideoThumbnailimg();
            Glide.with(context.getApplicationContext()).load(Uri.parse(Common.video_base_url + video1))
                    .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(holder.videoView1);
            Glide.with(context.getApplicationContext()).load(Uri.parse(Common.video_base_url + video2))
                    .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(holder.videoView2);
            holder.videoView3.setVisibility(View.INVISIBLE);
            holder.novideofound.setVisibility(View.INVISIBLE);
            holder.playVideoIconRecommendation3.setVisibility(View.INVISIBLE);
        }
        if (arraySize == 3) {
            video1 = recommendations.getData().get(position).getUserVideos().get(0).getVideoThumbnailimg();
            video2 = recommendations.getData().get(position).getUserVideos().get(1).getVideoThumbnailimg();
            video3 = recommendations.getData().get(position).getUserVideos().get(2).getVideoThumbnailimg();





            Glide.with(context.getApplicationContext()).load(Uri.parse(Common.video_base_url + video1))
                    .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(holder.videoView1);
            Glide.with(context.getApplicationContext()).load(Uri.parse(Common.video_base_url + video2))
                    .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(holder.videoView2);
            Glide.with(context.getApplicationContext()).load(Uri.parse(Common.video_base_url + video3))
                    .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(holder.videoView3);
            holder.novideofound.setVisibility(View.INVISIBLE);
        }
        if (imageName != null) {
            Glide.with(context.getApplicationContext()).load(Uri.parse(Common.image_base_url + imageName)).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(holder.imageView);
        }
        if (username != null) {
            holder.username.setText(username);
        }

        final String likeStatus=recommendations.getData().get(position).getLikeStatus();
        if (likeStatus.matches("1")){
            holder.favorateImage.setImageResource(R.mipmap.yellowheart);
        }
        else {
            holder.favorateImage.setImageResource(R.drawable.ic_favorite_black_24dp);
        }

        holder.playVideoIconRecommendation1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoName1 = recommendations.getData().get(position).getUserVideos().get(0).getVideoName();
                Intent intent = new Intent(context, VideoPlay.class);
                Log.e("videoNameRecommendation", videoName1);
                intent.putExtra("videoNameRecommendation", videoName1);
                context.startActivity(intent);
            }
        });
        holder.playVideoIconRecommendation2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoName2 = recommendations.getData().get(position).getUserVideos().get(1).getVideoName();
                Intent intent = new Intent(context, VideoPlay.class);
                Log.e("videoNameRecommendation", videoName2);
                intent.putExtra("videoNameRecommendation", videoName2);
                context.startActivity(intent);
            }
        });
        holder.playVideoIconRecommendation3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoName3 = recommendations.getData().get(position).getUserVideos().get(2).getVideoName();
                Intent intent = new Intent(context, VideoPlay.class);
                Log.e("videoNameRecommendation", videoName3);
                intent.putExtra("videoNameRecommendation", videoName3);
                context.startActivity(intent);
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = recommendations.getData().get(position).getUserId();
                Intent intent=new Intent(context, OtherUserProfile.class);
                intent.putExtra("userID",id);
                intent.putExtra("type","0");
                intent.putExtra("likestatus",likeStatus);
                context.startActivity(intent);
            }
        });
        holder.favorateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecommendationFragment.progress.setVisibility(View.VISIBLE);
                likeStatus21 =recommendations.getData().get(position).getLikeStatus();
                id = recommendations.getData().get(position).getUserId();
                session = new SessionManager(context.getApplicationContext());
                HashMap<String, String> user = session.getUserDetails();
                userID = user.get(SessionManager.USER_ID);
                Log.e("userId",userID);
                Log.e("idasfgasfas",id);
                Retrofit retrofit = RetrofitClientIntance.retroInit();
                ApiListener apiListener = retrofit.create(ApiListener.class);
                if (likeStatus21.matches("1")) {
                    Call<AddComentModel> call = apiListener.likeDislikeclassAPI(userID, id, "0");
                    call.enqueue(new Callback<AddComentModel>() {
                        @Override
                        public void onResponse(Call<AddComentModel> call, Response<AddComentModel> response) {
                            if (response.isSuccessful()) {
                                AddComentModel data = response.body();
                                if (data.getCode().equalsIgnoreCase("201")) {
                                    Toast.makeText(context, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                                    holder.favorateImage.setImageResource(R.drawable.ic_favorite_black_24dp);
                                    recommendations.getData().get(position).setLikeStatus("0");
                                    RecommendationFragment.progress.setVisibility(View.INVISIBLE);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<AddComentModel> call, Throwable t) {
                            Log.e("error", t.getMessage());
                            Toast.makeText(context, "Dislike failed", Toast.LENGTH_SHORT).show();
                            RecommendationFragment.progress.setVisibility(View.INVISIBLE);
                        }
                    });
                }
                else {
                    if (likeStatus21.matches("0")){
                        Call<AddComentModel> call = apiListener.likeDislikeclassAPI(userID, id, "1");
                        call.enqueue(new Callback<AddComentModel>() {
                            @Override
                            public void onResponse(Call<AddComentModel> call, Response<AddComentModel> response) {
                                AddComentModel data = response.body();
                                if (data.getCode().equalsIgnoreCase("201")) {
                                    Toast.makeText(context, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                                    holder.favorateImage.setImageResource(R.mipmap.yellowheart);
                                    recommendations.getData().get(position).setLikeStatus("1");
                                    RecommendationFragment.progress.setVisibility(View.INVISIBLE);
                                }
                            }

                            @Override
                            public void onFailure(Call<AddComentModel> call, Throwable t) {
                                Log.e("error", t.getMessage());
                                Toast.makeText(context, "Failed to like user", Toast.LENGTH_SHORT).show();
                                RecommendationFragment.progress.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }
                }
        });

    }



    @Override
    public int getItemCount() {
        return recommendations.getData().size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, playVideoIconRecommendation1, playVideoIconRecommendation2, playVideoIconRecommendation3, favorateImage;
        ImageView videoView1, videoView2, videoView3;
        TextView username, novideofound;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profile_image_recommendation);
            videoView1 = itemView.findViewById(R.id.imageUserRecommendations1);
            videoView2 = itemView.findViewById(R.id.imageUserRecommendations2);
            videoView3 = itemView.findViewById(R.id.imageUserRecommendations3);
            username = itemView.findViewById(R.id.username);
            novideofound = itemView.findViewById(R.id.novideofound);
            playVideoIconRecommendation1 = itemView.findViewById(R.id.playVideoIconRecommendation1);
            playVideoIconRecommendation2 = itemView.findViewById(R.id.playVideoIconRecommendation2);
            playVideoIconRecommendation3 = itemView.findViewById(R.id.playVideoIconRecommendation3);
            favorateImage = itemView.findViewById(R.id.favorateImage);
        }
    }

}
