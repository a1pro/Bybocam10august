package com.example.bybocam.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bybocam.Interface.UnblockInterface;
import com.example.bybocam.Model.BlockedData;
import com.example.bybocam.R;
import com.example.bybocam.Utils.Common;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlockedUserAdapter extends RecyclerView.Adapter<BlockedUserAdapter.ViewHolder> {
    Context context;
    List<BlockedData> list;
    UnblockInterface unblockInterface;
    public BlockedUserAdapter(Context context, List<BlockedData> list, UnblockInterface unblockInterface) {
        this.context=context;
        this.list=list;
        this.unblockInterface=unblockInterface;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_item, parent, false);
        return new BlockedUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (list.get(position).getUserName()!=null){
            holder.tv_name.setText(list.get(position).getUserName());
        }
        if (list.get(position).getEmail()!=null){
            holder.tv_email.setText(list.get(position).getEmail());
        }
        if (list.get(position).getPhone()!=null){
            holder.tv_no.setText(list.get(position).getPhone());
        }
        if (list.get(position).getUserImage()!=null && !list.get(position).getUserImage().isEmpty()){
            Glide.with(context).load(Common.image_base_url+list.get(position).getUserImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img_user);
        }

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unblockInterface.DoUnblock(list.get(position).getUserId());
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView img_user;
        private LinearLayout linear;
        private TextView tv_name,tv_email,tv_no;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linear=itemView.findViewById(R.id.linear);
            img_user=itemView.findViewById(R.id.img_user);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_email=itemView.findViewById(R.id.tv_email);
            tv_no=itemView.findViewById(R.id.tv_no);

        }
    }
}
