package com.example.bybocam.Adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.bybocam.Activities.AllComments;
import com.example.bybocam.Activities.FullImageView;
import com.example.bybocam.Activities.LikedPostsActivity;
import com.example.bybocam.Activities.RandomvideoplayActivity;
import com.example.bybocam.Interface.UnLikePostInterface;
import com.example.bybocam.Model.LikedPostData;
import com.example.bybocam.R;
import com.example.bybocam.Utils.Common;

import java.util.List;

public class LikedPostAdapter extends RecyclerView.Adapter<LikedPostAdapter.ViewHolder> {
    Context context;
    UnLikePostInterface unLikePostInterface;
    List<LikedPostData> list;
    public LikedPostAdapter(Context context, List<LikedPostData> list, UnLikePostInterface unLikePostInterface) {
        this.context=context;
        this.unLikePostInterface=unLikePostInterface;
        this.list=list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_profile_recycler_layout2, parent, false);
        return new LikedPostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


            holder.no_of_comments.setText(""+list.get(position).getPostCommentsCounts());


            holder.total_likes.setText(""+list.get(position).getPostLikeCount());

            if (list.get(position).getPostType()!=null){
               if (list.get(position).getPostType().equalsIgnoreCase("0")){
                   holder.playPostVideo.setVisibility(View.GONE);
                   if (list.get(position).getPostImage()!=null){
                       Glide.with(context).load(Common.image_post_url+list.get(position).getPostImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.my_profile_reycler_image);

                   }
               }else {
                   holder.playPostVideo.setVisibility(View.VISIBLE);
                   if (list.get(position).getPostVideoThumbnailImg()!=null){
                       Glide.with(context).load(Common.image_post_url+list.get(position).getPostVideoThumbnailImg()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.my_profile_reycler_image);

                   }
               }
            }


            if (list.get(position).getPostType()!=null){
                if (list.get(position).getPostType().equalsIgnoreCase("0")){
                    holder.my_profile_reycler_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(context, FullImageView.class);
                            intent.putExtra("likeimage",list.get(position).getPostImage());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    });
                }
            }

            holder.commnetImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Intent intent = new Intent(context, AllComments.class);
                        intent.putExtra("postId", list.get(position).getPostId());
                        context.startActivity(intent);
                }
            });




            holder.no_of_comments.setText(""+list.get(position).getPostCommentsCounts());


        if (list.get(position).getLikeStatus()!=null){
            if (list.get(position).getLikeStatus().equalsIgnoreCase("1")){
                holder.likeImage.setImageDrawable(context.getResources().getDrawable(R.mipmap.yellowheart));
            }else {
                holder.likeImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
            }

        }

        if (list.get(position).getLikeStatus()!=null){
            if (list.get(position).getLikeStatus().equalsIgnoreCase("1")){
                holder.likeImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        unLikePostInterface.Dounlike(list.get(position).getPostId());
                    }
                });

            }
        }

        holder.playPostVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url=Common.image_post_url+list.get(position).getPostVideo();
                Intent intent = new Intent(context, RandomvideoplayActivity.class);
                intent.putExtra("videoName", url);
                context.startActivity(intent);
            }
        });




    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView my_profile_reycler_image,likeImage,commnetImage,playPostVideo;
        private TextView total_likes,no_of_comments;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            my_profile_reycler_image=itemView.findViewById(R.id.my_profile_reycler_image);
            likeImage=itemView.findViewById(R.id.likeImage);
            commnetImage=itemView.findViewById(R.id.commnetImage);
            playPostVideo=itemView.findViewById(R.id.playPostVideo);
            total_likes=itemView.findViewById(R.id.total_likes);
            no_of_comments=itemView.findViewById(R.id.no_of_comments);
        }
    }
}
