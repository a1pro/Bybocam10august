package com.example.bybocam.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bybocam.Activities.InfluencePersonActivity;
import com.example.bybocam.Model.InfluenceData;
import com.example.bybocam.R;
import com.example.bybocam.Utils.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FluenceAdapter extends RecyclerView.Adapter<FluenceAdapter.ViewHolder> {
    Context context;
    List<InfluenceData> influencelist;
    int fluencesize;
    public FluenceAdapter(Context context, List<InfluenceData> influencelist, int fluencesize) {
        this.context=context;
        this.influencelist=influencelist;
        this.fluencesize=fluencesize;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fluence_item2,parent,false);
        return  new FluenceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.e("picturename",""+influencelist.get(position).getPicture());

    Glide.with(context).load(Common.Influencer_image_url+influencelist.get(position).getPicture()).placeholder(R.drawable.bg_msg_from).dontAnimate().into(holder.img_fluence);




        if (influencelist.get(position).getInfulenname()!=null){
        holder.tv_title.setText(influencelist.get(position).getInfulenname());
        }

        if (influencelist.get(position).getPrice()!=null){
            holder.tv_desc.setText("$"+influencelist.get(position).getPrice());
        }

        Log.e("price",influencelist.get(position).getPrice());

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, InfluencePersonActivity.class);
                intent.putExtra("name",influencelist.get(position).getInfulenname());
                intent.putExtra("price",influencelist.get(position).getPrice());
                intent.putExtra("race",influencelist.get(position).getRace());
                intent.putExtra("industry",influencelist.get(position).getIndustry());
                intent.putExtra("gender",influencelist.get(position).getGender());
                intent.putExtra("location",influencelist.get(position).getAddress());
                intent.putExtra("desc",influencelist.get(position).getDiscription());
                intent.putExtra("iamge",influencelist.get(position).getPicture());
                context.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return fluencesize;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title,tv_desc;
        private LinearLayout linear;
        private CircleImageView img_fluence;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linear=itemView.findViewById(R.id.linear);
            tv_desc=itemView.findViewById(R.id.tv_desc);
            tv_title=itemView.findViewById(R.id.tv_title);
            img_fluence=itemView.findViewById(R.id.img_fluence);
        }
    }
}
