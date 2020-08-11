package com.example.bybocam.Adapter.newAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bybocam.Activities.OtherUserProfile;
import com.example.bybocam.Model.GetAllFollowersModel;
import com.example.bybocam.Model.GetFavouriteUserModel;
import com.example.bybocam.R;
import com.example.bybocam.Utils.Common;

public class ViewAllFolowersRecyclerAdapter extends RecyclerView.Adapter<ViewAllFolowersRecyclerAdapter.ViewHolder> {


    GetFavouriteUserModel data;
    Context context;
    GetAllFollowersModel dataNew;
    int type;

    public ViewAllFolowersRecyclerAdapter(GetAllFollowersModel dataNew, GetFavouriteUserModel data, Context context, int type) {
        this.data = data;
        this.dataNew = dataNew;
        this.context = context;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.followers_recycler_layout, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new ViewAllFolowersRecyclerAdapter.ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (type==0) {
            String userImage = data.getData().get(position).getUserImage();
            if (userImage != null) {
                Glide.with(context.getApplicationContext()).load(Common.image_base_url + userImage).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profile_image_followers);
            }
            String userName = data.getData().get(position).getUserName();
            if (userName != null) {
                holder.userNameFollowers.setText(userName);
            }
            String userEmail = data.getData().get(position).getEmail();
            if (userEmail != null) {
                holder.userEmailFollowers.setText(userEmail);
            }
            String userContact = data.getData().get(position).getPhone();
            if (userContact != null) {
                holder.userContactFollowers.setText(userContact);
            }
            holder.relative_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = data.getData().get(position).getUserId();
                    Intent intent = new Intent(context, OtherUserProfile.class);
                    intent.putExtra("userID", id);
                    intent.putExtra("type", "0");
                    context.startActivity(intent);
                }
            });

        }else {
            String userImage = dataNew.getData().get(position).getUserImage();
            if (userImage != null) {
                Glide.with(context.getApplicationContext()).load(Common.image_base_url + userImage).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profile_image_followers);
            }
            String userName = dataNew.getData().get(position).getUserName();
            if (userName != null) {
                holder.userNameFollowers.setText(userName);
            }
            String userEmail = dataNew.getData().get(position).getEmail();
            if (userEmail != null) {
                holder.userEmailFollowers.setText(userEmail);
            }
            String userContact = dataNew.getData().get(position).getPhone();
            if (userContact != null) {
                holder.userContactFollowers.setText(userContact);
            }
            holder.relative_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = dataNew.getData().get(position).getUserId();
                    Intent intent = new Intent(context, OtherUserProfile.class);
                    intent.putExtra("userID", id);
                    intent.putExtra("type", "0");
                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (type == 0) {
            if (data.getData().size() == 0) {
                return 0;
            } else {
                return data.getData().size();
            }
        } else if (type == 1) {
            if (dataNew.getData().size() == 0) {
                return 0;
            } else {
                return dataNew.getData().size();
            }
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profile_image_followers;
        TextView userNameFollowers, userEmailFollowers, userContactFollowers;
        RelativeLayout relative_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image_followers = itemView.findViewById(R.id.profile_image_followers);
            userNameFollowers = itemView.findViewById(R.id.userNameFollowers);
            userEmailFollowers = itemView.findViewById(R.id.userEmailFollowers);
            userContactFollowers = itemView.findViewById(R.id.userContactFollowers);
            relative_layout = itemView.findViewById(R.id.relative_layout);
        }
    }
}
