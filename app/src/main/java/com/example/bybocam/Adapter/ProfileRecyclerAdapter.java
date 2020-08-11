package com.example.bybocam.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.bybocam.Activities.AllComments;
import com.example.bybocam.Activities.FullImageView;
import com.example.bybocam.Activities.VideoPlay;
import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Interface.DeletePost;
import com.example.bybocam.Model.AddComentModel;
import com.example.bybocam.Model.ViewProfileOurUserModel;
import com.example.bybocam.R;
import com.example.bybocam.URL.RetrofitClientIntance;
import com.example.bybocam.Utils.Common;
import com.example.bybocam.Utils.SessionManager;
import com.example.bybocam.Utils.SimpleVideoView;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileRecyclerAdapter extends RecyclerView.Adapter {


    private ViewProfileOurUserModel dataset;
    Context context;
    int type;
    SessionManager session;
    private String userID;
    DeletePost deletePost;


    public ProfileRecyclerAdapter(Context context, ViewProfileOurUserModel data, int type, DeletePost deletePost) {
        this.context = context;
        this.dataset = data;
        this.type = type;
        this.deletePost=deletePost;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (type == 1) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.my_profile_recycler_layout, parent, false);
            return new ProfileRecyclerAdapter.ImageTypeViewHolder(listItem);
        } else if (type == 0) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.my_profile_recycler_layout2, parent, false);
            return new ProfileRecyclerAdapter.ImageTypeViewHolder(listItem);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        session = new SessionManager(context.getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        userID = user.get(SessionManager.USER_ID);
        final String imageName = dataset.getPostData().get(position).getPostImage();
        final String videoName = dataset.getPostData().get(position).getPostVideoThumbnailImg();
        final String videoUrl = dataset.getPostData().get(position).getPostVideo();
        final Integer totalLike = dataset.getPostData().get(position).getPostLikeCount();
        final Integer totalComments = dataset.getPostData().get(position).getPostCommentsCounts();
        ((ImageTypeViewHolder) holder).total_likes.setText(String.valueOf(totalLike));
        ((ImageTypeViewHolder) holder).no_of_comments.setText(String.valueOf(totalComments));
        final String likeOrNot = dataset.getPostData().get(position).getIsliked();
        if (likeOrNot.equalsIgnoreCase("0")) {
            ((ImageTypeViewHolder) holder).likeImage.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            ((ImageTypeViewHolder) holder).likeImage.setImageResource(R.mipmap.yellowheart);
        }


        ((ImageTypeViewHolder) holder).likeImage.setOnClickListener(new View.OnClickListener() {
            int likes = 0;

            @Override
            public void onClick(View view) {
                final Integer totalLike = dataset.getPostData().get(position).getPostLikeCount();
                String likeStatus21 = dataset.getPostData().get(position).getIsliked();
                String postId = dataset.getPostData().get(position).getPostId();
                Retrofit retrofit = RetrofitClientIntance.retroInit();
                ApiListener apiListener = retrofit.create(ApiListener.class);
                if (likeStatus21.equalsIgnoreCase("0")) {
                    ((ImageTypeViewHolder) holder).likeImage.setImageResource(R.mipmap.yellowheart);
                    dataset.getPostData().get(position).setIsliked("1");
                    if (likes == 0) {
                        likes = totalLike + 1;
                        ((ImageTypeViewHolder) holder).total_likes.setText(String.valueOf(likes));
                        dataset.getPostData().get(position).setPostLikeCount(likes);
                        //((ImageTypeViewHolder) holder).progress.setVisibility(View.INVISIBLE);
                    } else {
                        int total = likes + 1;
                        ((ImageTypeViewHolder) holder).total_likes.setText(String.valueOf(total));
                        dataset.getPostData().get(position).setPostLikeCount(total);
                        // ((ImageTypeViewHolder) holder).progress.setVisibility(View.INVISIBLE);
                        likes = total;
                    }
                    Call<AddComentModel> call = apiListener.postLikeAPI(userID, postId, "1");
                    call.enqueue(new Callback<AddComentModel>() {
                        @Override
                        public void onResponse(Call<AddComentModel> call, Response<AddComentModel> response) {
                            AddComentModel data = response.body();
                            if (data.getCode().equalsIgnoreCase("201")) {
                            }
                        }

                        @Override
                        public void onFailure(Call<AddComentModel> call, Throwable t) {
                            Log.e("error", "" + t.getMessage());
                        }
                    });
                } else {
                    ((ImageTypeViewHolder) holder).likeImage.setImageResource(R.drawable.ic_favorite_black_24dp);
                    dataset.getPostData().get(position).setIsliked("0");
                    if (likes == 0) {
                        int total = totalLike - 1;
                        ((ImageTypeViewHolder) holder).total_likes.setText(String.valueOf(total));
                        dataset.getPostData().get(position).setPostLikeCount(total);
                        likes = total;
                    } else {
                        int total = likes - 1;
                        ((ImageTypeViewHolder) holder).total_likes.setText(String.valueOf(total));
                        dataset.getPostData().get(position).setPostLikeCount(total);
                        likes = total;
                    }
                    Call<AddComentModel> call = apiListener.postLikeAPI(userID, postId, "0");
                    call.enqueue(new Callback<AddComentModel>() {
                        @Override
                        public void onResponse(Call<AddComentModel> call, Response<AddComentModel> response) {
                            AddComentModel data = response.body();
                            if (data.getCode().equalsIgnoreCase("201")) {
                            }
                        }

                        @Override
                        public void onFailure(Call<AddComentModel> call, Throwable t) {
                            Log.e("error", "" + t.getMessage());
                        }
                    });
                }
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (videoUrl != null) {
                    ((ImageTypeViewHolder) holder).postVideoProfile.setVisibility(View.VISIBLE);
                    Glide.with(context.getApplicationContext())
                            .load(Uri.parse(Common.image_post_url + videoName))
                            .into(new CustomTarget<Drawable>() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    ((ImageTypeViewHolder) holder).postVideoProfile.setBackground(resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }
                            });
                    ((ImageTypeViewHolder) holder).playPostVideo.setVisibility(View.VISIBLE);
                    ((ImageTypeViewHolder) holder).imageView.setVisibility(View.INVISIBLE);
                    ((ImageTypeViewHolder) holder).postVideoProfile.start(Uri.parse(Common.image_post_url + videoUrl));
                    ((ImageTypeViewHolder) holder).postVideoProfile.play();
                    ((ImageTypeViewHolder) holder).postVideoProfile.setStopSystemAudio(false);
                    ((ImageTypeViewHolder) holder).postVideoProfile.setShouldLoop(true);

                }
            }
        }, 3000);

        ((ImageTypeViewHolder) holder).commnetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postId = dataset.getPostData().get(position).getPostId();
                Intent commentIntent = new Intent(context, AllComments.class);
                Log.e("postId", postId);
                commentIntent.putExtra("postId", postId);
                context.startActivity(commentIntent);
            }
        });
        if (imageName != null) {
            ((ImageTypeViewHolder) holder).playPostVideo.setVisibility(View.INVISIBLE);
            Glide.with(context.getApplicationContext()).load(Uri.parse(Common.image_post_url + imageName))
                    .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(((ImageTypeViewHolder) holder).imageView);

        }
        if (videoName != null) {
            ((ImageTypeViewHolder) holder).playPostVideo.setVisibility(View.VISIBLE);
            Glide.with(context.getApplicationContext()).load(Uri.parse(Common.image_post_url + videoName))
                    .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(((ImageTypeViewHolder) holder).imageView);

        }
        ((ImageTypeViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FullImageView.class);
                intent.putExtra("data", imageName);
                context.startActivity(intent);
            }
        });
        ((ImageTypeViewHolder) holder).playPostVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String videoUrl = dataset.getPostData().get(position).getPostVideo();
                Intent intent = new Intent(context, VideoPlay.class);
                intent.putExtra("videoName", videoUrl);
                context.startActivity(intent);
            }
        });

        ((ImageTypeViewHolder) holder).dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePost.getpost(dataset.getPostData().get(position).getPostId());
            }
        });


    }

    public class ImageTypeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, playPostVideo, likeImage, commnetImage;
        SimpleVideoView postVideoProfile;
        LinearLayout profileLayout;
        TextView total_likes, no_of_comments;
        ProgressBar progress;
        private ImageView dots;

        public ImageTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            dots=itemView.findViewById(R.id.dots);
            progress = itemView.findViewById(R.id.progress);
            no_of_comments = itemView.findViewById(R.id.no_of_comments);
            total_likes = itemView.findViewById(R.id.total_likes);
            commnetImage = itemView.findViewById(R.id.commnetImage);
            imageView = itemView.findViewById(R.id.my_profile_reycler_image);
            likeImage = itemView.findViewById(R.id.likeImage);
            playPostVideo = itemView.findViewById(R.id.playPostVideo);
            postVideoProfile = itemView.findViewById(R.id.postVideoProfile);
            profileLayout = itemView.findViewById(R.id.profileLayout);
        }
    }


    @Override
    public int getItemCount() {
        if (dataset != null) {
            if (dataset.getPostData().size() != 0) {
                return dataset.getPostData().size();
            }
        }
        return 0;
    }
}
