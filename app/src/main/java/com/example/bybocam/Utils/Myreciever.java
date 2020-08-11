package com.example.bybocam.Utils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import static com.example.bybocam.Activities.ChatWithSingleUser.hitSingleChatMessageApi;


public class Myreciever extends BroadcastReceiver {

    String type=null;

    @SuppressLint("ResourceType")
    @Override
    public void onReceive(Context context, Intent intent) {
        String type=intent.getStringExtra("type");
        if (type!=null) {
            if (type.equalsIgnoreCase("1")) {
                hitSingleChatMessageApi();
            }

        }
    }
}
