package com.example.bybocam.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.bybocam.Activities.VideoPlay;
import com.example.bybocam.Model.ViewProfileModel;
import com.example.bybocam.Model.ViewThumbnails;
import com.example.bybocam.R;
import com.example.bybocam.Utils.Common;
import com.example.bybocam.Utils.SimpleVideoView;



public class ProfileRecyclerViewAdapter extends RecyclerView.Adapter<ProfileRecyclerViewAdapter.ViewHolder> {


    private ViewThumbnails[] listdata;
    Context context;
    int position;
    OnItemClickListener mItemClickListener;
    ViewProfileModel data;
    String accessType;
    String name;


    public ProfileRecyclerViewAdapter(ViewThumbnails[] listdata, ViewProfileModel data, String type) {
        this.listdata = listdata;
        this.data = data;
        this.accessType = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_recycler_layout, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new ProfileRecyclerViewAdapter.ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        if (Integer.parseInt(accessType)==0) {
            holder.editVideo.setVisibility(View.VISIBLE);
        } else {
            if (Integer.parseInt(accessType)==1) {
                holder.editVideo.setVisibility(View.INVISIBLE);

            }
        }
        name = data.getUserVideos().get(position).getVideoName();

        holder.videoView.setStopSystemAudio(false);
        holder.videoView.start(Uri.parse(Common.video_base_url + name));
        holder.videoView.setShouldLoop(true);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Glide.with(context.getApplicationContext())
                        .load(listdata[position].getUri())
                        .into(new CustomTarget<Drawable>() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                holder.videoView.setBackground(resource);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
                if (name!=null) {
                    holder.videoThumbnailProflie.setVisibility(View.INVISIBLE);
                    holder.videoView.setVisibility(View.VISIBLE);
                    holder.playVideoIcon.setVisibility(View.VISIBLE);
                    if (Integer.parseInt(accessType) == 0) {
                        holder.editVideo.setVisibility(View.VISIBLE);
                    }


                }

                holder.videoView.play();
            }
        }, 200);

        Glide.with(context.getApplicationContext()).load(listdata[position].getUri()).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(holder.videoThumbnailProflie);
        holder.playVideoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == 0) {
                    String name1 = data.getUserVideos().get(0).getVideoName();
                    if (name1 != null) {
                        Intent intent = new Intent(context, VideoPlay.class);
                        intent.putExtra("videoNameProfile", name1);
                        context.startActivity(intent);
                    }
                }
                if (position == 1) {
                     String name2 = data.getUserVideos().get(1).getVideoName();
                    if (name2 != null) {
                        Intent intent = new Intent(context, VideoPlay.class);
                        intent.putExtra("videoNameProfile", name2);
                        context.startActivity(intent);
                    }
                }
                if (position == 2) {
                     String name3 = data.getUserVideos().get(2).getVideoName();
                    if (name3 != null) {
                        Intent intent = new Intent(context, VideoPlay.class);
                        intent.putExtra("videoNameProfile", name3);
                        context.startActivity(intent);
                    }
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        SimpleVideoView videoView;
        public ImageView playVideoIcon, editVideo, videoThumbnailProflie;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            videoView = itemView.findViewById(R.id.simplevideoprofile);
            playVideoIcon = itemView.findViewById(R.id.playVideoIcon);
            editVideo = itemView.findViewById(R.id.editVideo);
            videoThumbnailProflie = itemView.findViewById(R.id.videoThumbnailProflie);
            position = getAdapterPosition();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}
