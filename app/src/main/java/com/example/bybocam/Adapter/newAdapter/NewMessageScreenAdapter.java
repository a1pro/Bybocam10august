package com.example.bybocam.Adapter.newAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bybocam.Model.GetFavouriteUserModel;
import com.example.bybocam.R;

import java.util.ArrayList;
import java.util.List;

public class NewMessageScreenAdapter extends RecyclerView.Adapter<NewMessageScreenAdapter.VIewHolder>  {


    GetFavouriteUserModel data;
    Context context;
    public static List<String> usernames=new ArrayList<>();
    public static List<String> userIds=new ArrayList<>();
    public NewMessageScreenAdapter(GetFavouriteUserModel data1, Context context) {
        this.data = data1;
        this.context = context;
    }

    @NonNull
    @Override
    public VIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_laytout_recycler, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new NewMessageScreenAdapter.VIewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final VIewHolder holder, final int position) {
        holder.checkBox.setText(data.getData().get(position).getUserName());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (holder.checkBox.isChecked()==true){
                    String id=data.getData().get(position).getUserId();
                    userIds.add(id);
                    String text= String.valueOf(compoundButton.getText());
                    usernames.add(text);
                }
                if (holder.checkBox.isChecked()==false){
                    String text= String.valueOf(compoundButton.getText());
                    usernames.remove(text);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (data!=null) {
            if (data.getCode().equalsIgnoreCase("201")) {
                if (data.getData().size() > 0) {
                    return data.getData().size();
                } else {
                    return 0;
                }
            }
            else {
                return 0;
            }
        }
        else {
            return 0;
        }
    }

    public class VIewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;
        public VIewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox=itemView.findViewById(R.id.cheackBoxName);
        }
    }
}
