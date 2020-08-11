package com.example.bybocam.Adapter.newAdapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bybocam.Activities.AllComments;
import com.example.bybocam.Activities.FullImageView;
import com.example.bybocam.Activities.VideoPlay;
import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Model.AddComentModel;
import com.example.bybocam.Model.SearchUserModel;
import com.example.bybocam.R;
import com.example.bybocam.URL.RetrofitClientIntance;
import com.example.bybocam.Utils.Common;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class NewSearchAdapter extends RecyclerView.Adapter {
    private SearchUserModel dataset;
    private Context context;
    private String userID;
    public NewSearchAdapter(String userId,SearchUserModel data, Context context) {
        this.dataset = data;
        this.userID=userId;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_user_recycler_layout, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new NewSearchAdapter.ImageTypeViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        String videoName = dataset.getPostData().get(position).getPostVideoThumbnailImg();
        if (videoName!=null) {
            Log.e("videoName", videoName);
        }
        final String imageName = String.valueOf(dataset.getPostData().get(position).getPostImage());
        if (imageName!=null) {
            Log.e("imageName", imageName);
        }
        final String video=dataset.getPostData().get(position).getPostVideo();
        if (video!=null) {
            Log.e("video", video);
        }
        if (imageName!=null) {
            ((ImageTypeViewHolder)holder).playVideoSearch.setVisibility(View.INVISIBLE);
            Glide.with(context.getApplicationContext()).load(Uri.parse(Common.image_post_url+imageName)).centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(((ImageTypeViewHolder)holder).imageView);
            ((ImageTypeViewHolder)holder).progressBar.setVisibility(View.INVISIBLE);
        }
        if (videoName!=null){
            ((ImageTypeViewHolder)holder).imageView.setEnabled(false);
            ((ImageTypeViewHolder)holder).imageView.setClickable(false);
            ((ImageTypeViewHolder)holder).playVideoSearch.setVisibility(View.VISIBLE);
            Glide.with(context.getApplicationContext()).load(Uri.parse(Common.image_post_url+videoName)).centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(((ImageTypeViewHolder)holder).imageView);
            ((ImageTypeViewHolder)holder).progressBar.setVisibility(View.INVISIBLE);
        }
        ((ImageTypeViewHolder)holder).playVideoSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,VideoPlay.class);
                intent.putExtra("videoName",video);
                context.startActivity(intent);
            }
        });
        ((ImageTypeViewHolder)holder).imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,FullImageView.class);
                intent.putExtra("data",imageName);
                context.startActivity(intent);
            }
        });
        ((ImageTypeViewHolder)holder).no_of_comments.setText(String.valueOf(dataset.getPostData().get(position).getPostCommentsCounts()));
        ((ImageTypeViewHolder)holder).total_likes.setText(String.valueOf(dataset.getPostData().get(position).getPostLikeCount()));
        if (dataset.getPostData().get(position).getIsliked().equalsIgnoreCase("1")){
            ((ImageTypeViewHolder)holder).likeImage.setImageResource(R.mipmap.yellowheart);
        }
        else {
            ((ImageTypeViewHolder)holder).likeImage.setImageResource(R.drawable.ic_favorite_black_24dp);
        }


        ///////////////////////////////////////////////////////////////////////////////////////////////


        ((ImageTypeViewHolder)holder).likeImage.setOnClickListener(new View.OnClickListener() {
            int likes=0;
            @Override
            public void onClick(View view) {
                final Integer totalLike=dataset.getPostData().get(position).getPostLikeCount();
                String likeStatus21 =dataset.getPostData().get(position).getIsliked();
                String postId=dataset.getPostData().get(position).getPostId();
                Retrofit retrofit = RetrofitClientIntance.retroInit();
                ApiListener apiListener = retrofit.create(ApiListener.class);
                if (likeStatus21.equalsIgnoreCase("0")) {
                    dataset.getPostData().get(position).setIsliked("1");
                    if (likes==0) {
                        likes = totalLike + 1;
                        ((ImageTypeViewHolder) holder).total_likes.setText(String.valueOf(likes));
                        dataset.getPostData().get(position).setPostLikeCount(likes);

                    }
                    else {
                        int total=likes+1;
                        ((ImageTypeViewHolder) holder).total_likes.setText(String.valueOf(total));
                        dataset.getPostData().get(position).setPostLikeCount(total);
                        likes=total;
                    }
                    ((ImageTypeViewHolder)holder).likeImage.setImageResource(R.mipmap.yellowheart);
                    Call<AddComentModel> call = apiListener.postLikeAPI(userID, postId, "1");
                    call.enqueue(new Callback<AddComentModel>() {
                        @Override
                        public void onResponse(Call<AddComentModel> call, Response<AddComentModel> response) {
                            AddComentModel data = response.body();
                            if (data.getCode().equalsIgnoreCase("201")) {
                               // Toast.makeText(context, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                                ((ImageTypeViewHolder)holder).likeImage.setImageResource(R.mipmap.yellowheart);
//                                dataset.getPostData().get(position).setIsliked("1");
//                                if (likes==0) {
//                                    likes = totalLike + 1;
//                                    ((ImageTypeViewHolder) holder).total_likes.setText(String.valueOf(likes));
//                                    dataset.getPostData().get(position).setPostLikeCount(likes);
//
//                                }
//                                else {
//                                    int total=likes+1;
//                                    ((ImageTypeViewHolder) holder).total_likes.setText(String.valueOf(total));
//                                    dataset.getPostData().get(position).setPostLikeCount(total);
//                                    likes=total;
//                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<AddComentModel> call, Throwable t) {
                            Log.e("error", ""+t.getMessage());
                          //  Toast.makeText(context, "Failed to like user", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    ((ImageTypeViewHolder)holder).likeImage.setImageResource(R.drawable.ic_favorite_black_24dp);
                    dataset.getPostData().get(position).setIsliked("0");
                    if (likes==0) {
                        int total=totalLike-1;
                        ((ImageTypeViewHolder) holder).total_likes.setText(String.valueOf(total));
                        dataset.getPostData().get(position).setPostLikeCount(total);
                        likes=total;
                    }
                    else {
                        int total=likes-1;
                        ((ImageTypeViewHolder) holder).total_likes.setText(String.valueOf(total));
                        dataset.getPostData().get(position).setPostLikeCount(total);
                        likes=total;
                    }
                    Call<AddComentModel> call = apiListener.postLikeAPI(userID, postId, "0");
                    call.enqueue(new Callback<AddComentModel>() {
                        @Override
                        public void onResponse(Call<AddComentModel> call, Response<AddComentModel> response) {
                            AddComentModel data = response.body();
                            if (data.getCode().equalsIgnoreCase("201")) {
                               // Toast.makeText(context, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                                ((ImageTypeViewHolder)holder).likeImage.setImageResource(R.drawable.ic_favorite_black_24dp);
//                                dataset.getPostData().get(position).setIsliked("0");
//                                if (likes==0) {
//                                    int total=totalLike-1;
//                                    ((ImageTypeViewHolder) holder).total_likes.setText(String.valueOf(total));
//                                    dataset.getPostData().get(position).setPostLikeCount(total);
//                                    likes=total;
//                                }
//                                else {
//                                    int total=likes-1;
//                                    ((ImageTypeViewHolder) holder).total_likes.setText(String.valueOf(total));
//                                    dataset.getPostData().get(position).setPostLikeCount(total);
//                                    likes=total;
//                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<AddComentModel> call, Throwable t) {
                            Log.e("error",""+ t.getMessage());
                           // Toast.makeText(context, "Failed to like user", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        ///////////////////////////////////////////////////////////////////////////////////////////


        ((ImageTypeViewHolder) holder).commentLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postId=dataset.getPostData().get(position).getPostId();
                Intent commentIntent=new Intent(context, AllComments.class);
                Log.e("postId",postId);
                commentIntent.putExtra("postId",postId);
                context.startActivity(commentIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (dataset.getPostData().size() != 0) {
            return dataset.getPostData().size();
        }
        return 0;
    }

    public static class ImageTypeViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView,playVideoSearch,likeImage;
        ProgressBar progressBar;
        LinearLayout likeLinear,commentLinear;
        TextView total_likes,no_of_comments;


        public ImageTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.mediaStoreImageView);
            likeLinear = itemView.findViewById(R.id.likeLinear);
            commentLinear = itemView.findViewById(R.id.commentLinear);
            progressBar = itemView.findViewById(R.id.imageProgress);
            playVideoSearch=itemView.findViewById(R.id.playVideoSearch);
            likeImage = itemView.findViewById(R.id.likeImage);
            total_likes = itemView.findViewById(R.id.total_likes);
            no_of_comments=itemView.findViewById(R.id.no_of_comments);

        }
    }

}
