package com.example.bybocam.Adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.bybocam.Activities.RandomvideoplayActivity;
import com.example.bybocam.Activities.VideoPlay;
import com.example.bybocam.Model.RandomData;
import com.example.bybocam.R;
import com.example.bybocam.Utils.Common;

import java.util.List;

public class PersonalAdapter extends RecyclerView.Adapter<PersonalAdapter.ViewHolder> {
    Context context;
    List<RandomData> randomData;
    public PersonalAdapter(Context context, List<RandomData> randomData) {
        this.context=context;
        this.randomData=randomData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fluence_item,parent,false);
        return  new PersonalAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.e("randomvideo",""+randomData.get(position).getVideoImage());
        if (randomData.get(position).getVideoImage()!=null && !randomData.get(position).getVideoImage().isEmpty()){
            Glide.with(context).load(Common.RANDOM_Base_URL+randomData.get(position).getVideoImage()).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img_fluence);
        }



        if (randomData.get(position).getUserName()!=null){
            holder.tv_title.setText(randomData.get(position).getUserName());
        }

        if (randomData.get(position).getDiscriptions()!=null){
            holder.tv_desc.setText(randomData.get(position).getDiscriptions());
        }

        holder.img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String videoUrl = "http://a1professionals.net/bybocam/assets/randomVideo/"+randomData.get(position).getVideoName();
                Intent intent = new Intent(context, RandomvideoplayActivity.class);
                intent.putExtra("videoName", videoUrl);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return randomData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title,tv_desc;
        private ImageView img_fluence,img_play;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_desc=itemView.findViewById(R.id.tv_desc);
            tv_title=itemView.findViewById(R.id.tv_title);
            img_fluence=itemView.findViewById(R.id.img_fluence);
            img_play=itemView.findViewById(R.id.img_play);

        }
    }
}
