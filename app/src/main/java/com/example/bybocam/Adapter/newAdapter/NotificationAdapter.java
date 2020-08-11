package com.example.bybocam.Adapter.newAdapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bybocam.Model.GetAllNotificationsModel;
import com.example.bybocam.R;
import com.example.bybocam.Utils.Common;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    private Context context;
    private GetAllNotificationsModel data;

    public NotificationAdapter(Context context, GetAllNotificationsModel data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_recycler_layout, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String profileImage=data.getData().get(position).getUserData().get(0).getUserImage();
        String userName=data.getData().get(position).getUserData().get(0).getUserName();
        String title=data.getData().get(position).getTitle();
        String description=data.getData().get(position).getDescription();
        if (profileImage!=null){
            Glide.with(context).load(Uri.parse(Common.image_base_url+profileImage)).centerCrop().into(holder.profile_image);
        }
        if (userName!=null){
            holder.nameofuser.setText(userName);
        }
        if (title!=null){
            holder.titleofnotifcation.setText(title);
        }
        if (description!=null){
            holder.titleofnotifcation.setText(description);
        }

    }

    @Override
    public int getItemCount() {
        if (data!=null){
            if (data.getData().size()==0){
                return 0;
            }
            else {
                return data.getData().size();
            }
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private CircleImageView profile_image;
        private TextView nameofuser,titleofnotifcation,descriptionofnotifcation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameofuser=itemView.findViewById(R.id.nameofuser);
            titleofnotifcation=itemView.findViewById(R.id.titleofnotifcation);
            descriptionofnotifcation=itemView.findViewById(R.id.descriptionofnotifcation);
            profile_image=itemView.findViewById(R.id.profile_image);
        }
    }
}
