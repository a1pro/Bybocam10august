package com.example.bybocam.Adapter.newAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bybocam.Activities.ChatWithSingleUser;
import com.example.bybocam.Model.GetAllMessagesModel;
import com.example.bybocam.R;

public class AllMessageRecyclerAdapter extends RecyclerView.Adapter<AllMessageRecyclerAdapter.ViewHolder>{


    private Context context;
    private GetAllMessagesModel data;

    public AllMessageRecyclerAdapter(Context context, GetAllMessagesModel data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_recycler_layout, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String username=data.getData().get(position).getUserName().get(0).getUserName();
        if (username!=null){
            holder.userName.setText(username);
        }
        else {
            holder.userName.setHint("username");
        }

        String message=data.getData().get(position).getMessage();
        if (message!=null){
            holder.message.setText(message);
        }
        else {
            holder.message.setHint("message");
        }
        holder.linearRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.linearRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id=data.getData().get(position).getUserName().get(0).getUserId();
                String username=data.getData().get(position).getUserName().get(0).getUserName();
                if (id!=null){
                    Intent intent=new Intent(context, ChatWithSingleUser.class);
                    intent.putExtra("reciverId",id);
                    intent.putExtra("username",username);
                    context.startActivity(intent);
                }

            }
        });
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


        private LinearLayout linearRecycler;
        private TextView userName,message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearRecycler=itemView.findViewById(R.id.linearRecycler);
            userName=itemView.findViewById(R.id.userName);
            message=itemView.findViewById(R.id.message);
        }
    }
}
