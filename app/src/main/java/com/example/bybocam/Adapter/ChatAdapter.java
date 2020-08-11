package com.example.bybocam.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
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
import com.example.bybocam.Activities.FullImageView;
import com.example.bybocam.Model.GetChatMessagesModel;
import com.example.bybocam.R;
import com.example.bybocam.Utils.Common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context context;
    List<GetChatMessagesModel.Datum> data;
    String senderId2121;
    int pos = 0;
    int pos2 = 0;
    int pos3 = 0;

    public ChatAdapter(Context context, List<GetChatMessagesModel.Datum> data, String senderId) {
        this.context = context;
        this.data = data;
        this.senderId2121 = senderId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlechat_recycler_layout, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        String senderId = data.get(position).getSenderId();
        String time = data.get(position).getCreatedAt();
        String message = data.get(position).getMessage();
        Object messaegFile = data.get(position).getMessageFile();

        final String dateAndTime = getNewDate(time);
        String newFormate2 = getNewDate2(time);

        Date past = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            format.setTimeZone(TimeZone.getDefault());
            past = format.parse(dateAndTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar calander = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(calander.getTime());


        Date now = null;
        try {
            now = sdf.parse(currentDateandTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
            if (seconds <= 0) {
                if (pos == 0) {
                    holder.messageDay.setVisibility(View.VISIBLE);
                    holder.messageDay.setText("Today");
                    pos = 58585885;
                } else {
                    holder.messageDay.setVisibility(View.GONE);
                }
            } else if (seconds < 60) {
                if (pos == 0) {
                    holder.messageDay.setVisibility(View.VISIBLE);
                    holder.messageDay.setText("Today");
                    pos2 = 47747447;
                } else {
                    holder.messageDay.setVisibility(View.GONE);
                }
            } else if (minutes < 60) {
                if (pos == 0) {
                    holder.messageDay.setVisibility(View.VISIBLE);
                    holder.messageDay.setText("Today");
                    pos = 8787878;
                } else {
                    holder.messageDay.setVisibility(View.GONE);
                }
            } else if (hours < 24) {
                if (pos == 0) {
                    holder.messageDay.setVisibility(View.VISIBLE);
                    holder.messageDay.setText("Today");
                    pos = 77787877;
                } else {
                    holder.messageDay.setVisibility(View.GONE);
                }
            } else {
                if (pos2 == 0) {
                    if (String.valueOf(days).equalsIgnoreCase("1")) {
                        if (pos2 == 0) {
                            holder.messageDay.setVisibility(View.VISIBLE);
                            holder.messageDay.setText("Yesterday");
                            pos2 = 787877;
                        } else {
                            holder.messageDay.setVisibility(View.GONE);
                        }
                    } else {
                        if (pos3 == 0) {
                            holder.messageDay.setVisibility(View.VISIBLE);
                            holder.messageDay.setText(String.valueOf(days) + " days ago");
                            pos3 = 78787888;
                        } else {
                            holder.messageDay.setVisibility(View.GONE);
                        }

                    }
                }
                else {
                    holder.messageDay.setVisibility(View.GONE);
                }
            }
        } catch (Exception j) {
            j.printStackTrace();
        }
        if (message != null) {
            if (senderId.equalsIgnoreCase(senderId2121)) {
                holder.rigthRelative.setVisibility(View.VISIBLE);
                holder.rigthMessage.setVisibility(View.VISIBLE);
                holder.rigthtimeOfMessage.setVisibility(View.VISIBLE);
                holder.rightmessageImage.setVisibility(View.GONE);
                holder.leftRelative.setVisibility(View.GONE);
                holder.lefttimeOfMessage.setVisibility(View.GONE);
                holder.rigthtimeOfMessage.setText(newFormate2);
                holder.rigthMessage.setText(message);
            } else {
                holder.leftRelative.setVisibility(View.VISIBLE);
                holder.leftMessage.setVisibility(View.VISIBLE);
                holder.lefttimeOfMessage.setVisibility(View.VISIBLE);
                holder.leftmessageImage.setVisibility(View.GONE);
                holder.rigthRelative.setVisibility(View.GONE);
                holder.rigthtimeOfMessage.setVisibility(View.GONE);
                holder.lefttimeOfMessage.setText(newFormate2);
                holder.leftMessage.setText(message);
            }
        } else if (messaegFile != null) {
            if (senderId.equalsIgnoreCase(senderId2121)) {
                holder.rigthtimeOfMessage.setText(newFormate2);
                holder.rigthRelative.setVisibility(View.VISIBLE);
                holder.leftRelative.setVisibility(View.GONE);
                holder.rightmessageImage.setVisibility(View.VISIBLE);
                holder.rigthMessage.setVisibility(View.GONE);
                Glide.with(context).load(Uri.parse(Common.message_Image_base_url + messaegFile)).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.rightmessageImage);
            } else {
                holder.lefttimeOfMessage.setText(newFormate2);
                holder.rigthRelative.setVisibility(View.GONE);
                holder.leftRelative.setVisibility(View.VISIBLE);
                holder.leftMessage.setVisibility(View.GONE);
                holder.leftmessageImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(Uri.parse(Common.message_Image_base_url + messaegFile)).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.leftmessageImage);
            }
        }
        holder.leftmessageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageName=data.get(position).getMessageFile();
                Intent intent=new Intent(context, FullImageView.class);
                intent.putExtra("imageName",imageName);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.rightmessageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageName=data.get(position).getMessageFile();
                Intent intent=new Intent(context, FullImageView.class);
                intent.putExtra("imageName",imageName);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (data.size() > 0) {
            return data.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout leftRelative, rigthRelative;
        private ImageView rightmessageImage, leftmessageImage;
        private TextView leftMessage, rigthMessage, lefttimeOfMessage, rigthtimeOfMessage, messageDay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leftRelative = itemView.findViewById(R.id.leftRelative);
            rigthRelative = itemView.findViewById(R.id.rigthRelative);
            leftMessage = itemView.findViewById(R.id.leftMessage);
            messageDay = itemView.findViewById(R.id.messageDay);

            rightmessageImage = itemView.findViewById(R.id.rightmessageImage);
            leftmessageImage = itemView.findViewById(R.id.leftmessageImage);

            rigthMessage = itemView.findViewById(R.id.rigthMessage);
            lefttimeOfMessage = itemView.findViewById(R.id.lefttimeOfMessage);
            rigthtimeOfMessage = itemView.findViewById(R.id.rigthtimeOfMessage);
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

    public String getNewDate2(String getOldDate) {

        if (getOldDate == null) {
            return "";
        }

        SimpleDateFormat oldFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        oldFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        String dueDateAsNormal = "";
        try {
            value = oldFormatter.parse(getOldDate);
            SimpleDateFormat newFormatter = new SimpleDateFormat("hh:mm a");

            newFormatter.setTimeZone(TimeZone.getDefault());
            dueDateAsNormal = newFormatter.format(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dueDateAsNormal;
    }
}
