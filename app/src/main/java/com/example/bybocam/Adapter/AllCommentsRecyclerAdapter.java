package com.example.bybocam.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bybocam.Model.GetAllCommentsModel;
import com.example.bybocam.R;
import com.example.bybocam.Utils.Common;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class AllCommentsRecyclerAdapter extends RecyclerView.Adapter<AllCommentsRecyclerAdapter.ViewHolder> {

    private GetAllCommentsModel data;
    private Context context;

    public AllCommentsRecyclerAdapter(Context context1, GetAllCommentsModel dataset) {
        this.data = dataset;
        this.context = context1;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_comments_recycler_layout, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new AllCommentsRecyclerAdapter.ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (data.getData().get(position).getUserData().size() > 0) {
            String userName = data.getData().get(position).getUserData().get(0).getUserName();
            String userImageName = data.getData().get(position).getUserData().get(0).getUserImage();
            if (userImageName != null) {
                Glide.with(context).load(Uri.parse(Common.image_base_url + userImageName)).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.all_comments_profile_image);
            }
            if (userName != null) {
                holder.username_allcomments.setText(userName);
            }
        }
        String comment = data.getData().get(position).getPostMessage();
        if (comment != null) {
            holder.username_comments.setText(comment);
        }


        String inputText = data.getData().get(position).getCreatedAt();
//        try
//        {
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateAndTime = getNewDate(inputText);
        Log.e("dateAndTime", dateAndTime);
        Date past = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            format.setTimeZone(TimeZone.getDefault());
            past = format.parse(dateAndTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("pastDate", String.valueOf(past));


        Calendar calander = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(calander.getTime());

        Log.e("currentDateandTime", currentDateandTime);
        Date now = null;
        try {
            now = sdf.parse(currentDateandTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("currentdate", String.valueOf(now));
        try { long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());

            Log.e("time", String.valueOf(seconds));
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            Log.e("time", String.valueOf(minutes));
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            Log.e("time", String.valueOf(hours));
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
            Log.e("time", String.valueOf(days));
            if (seconds <= 0) {
                holder.all_comments_time.setText(1+ " seconds ago");
            } else if (seconds < 60) {
                holder.all_comments_time.setText(seconds + " seconds ago");
                //System.out.println(seconds+" seconds ago");
            } else if (minutes < 60) {
                holder.all_comments_time.setText(minutes + " minutes ago");
                //System.out.println(minutes+" minutes ago");
            } else if (hours < 24) {
                holder.all_comments_time.setText(hours + " hours ago");
                // System.out.println(hours+" hours ago");
            } else {
                holder.all_comments_time.setText(days + " days ago");
                //System.out.println(days+" days ago");
            }
        } catch (Exception j) {
            j.printStackTrace();
        }

//        String dateAndTime=getNewDate(inputText);
//        holder.all_comments_time.setText(dateAndTime);


    }

    @Override
    public int getItemCount() {
        if (data.getData()!=null) {
            if (data.getData().size() == 0) {
                return 0;
            } else {
                return data.getData().size();
            }
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView all_comments_profile_image;
        TextView username_allcomments, username_comments, all_comments_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            all_comments_profile_image = itemView.findViewById(R.id.all_comments_profile_image);
            username_allcomments = itemView.findViewById(R.id.username_allcomments);
            username_comments = itemView.findViewById(R.id.username_comments);
            all_comments_time = itemView.findViewById(R.id.all_comments_time);
        }
    }

    public String getNewDate(String getOldDate) {

        if (getOldDate == null) {
            return "";
        }

        SimpleDateFormat oldFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        oldFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        String dueDateAsNormal = "";
        try {
            value = oldFormatter.parse(getOldDate);
            SimpleDateFormat newFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            newFormatter.setTimeZone(TimeZone.getDefault());
            dueDateAsNormal = newFormatter.format(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dueDateAsNormal;
    }
}
