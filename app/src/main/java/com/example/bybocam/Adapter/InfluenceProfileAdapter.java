package com.example.bybocam.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bybocam.Activities.InfluencePersonActivity;
import com.example.bybocam.Model.InfluenceData;
import com.example.bybocam.R;
import com.example.bybocam.Utils.Common;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfluenceProfileAdapter extends RecyclerView.Adapter<InfluenceProfileAdapter.ViewHolder> {
    Context context;
    List<InfluenceData> list;
    public InfluenceProfileAdapter(Context context, List<InfluenceData> list) {
        this.list=list;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fluence_item3,parent,false);
        return  new InfluenceProfileAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(context).load(Common.Influencer_image_url+list.get(position).getPicture()).placeholder(R.drawable.bg_msg_from).dontAnimate().into(holder.img_fluence);




        if (list.get(position).getInfulenname()!=null){
            holder.tv_title.setText(list.get(position).getInfulenname());
        }

        if (list.get(position).getPrice()!=null){
            holder.tv_desc.setText("$"+list.get(position).getPrice());
        }

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, InfluencePersonActivity.class);
                intent.putExtra("name",list.get(position).getInfulenname());
                intent.putExtra("price",list.get(position).getPrice());
                intent.putExtra("race",list.get(position).getRace());
                intent.putExtra("industry",list.get(position).getIndustry());
                intent.putExtra("gender",list.get(position).getGender());
                intent.putExtra("location",list.get(position).getAddress());
                intent.putExtra("desc",list.get(position).getDiscription());
                intent.putExtra("iamge",list.get(position).getPicture());
                context.startActivity(intent);
            }
        });



    }


    @Override
    public int getItemCount() {
        return list.size();
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
