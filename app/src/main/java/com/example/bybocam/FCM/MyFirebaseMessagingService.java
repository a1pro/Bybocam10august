package com.example.bybocam.FCM;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;

import com.example.bybocam.Activities.ChatWithSingleUser;
import com.example.bybocam.Activities.MainActivity;
import com.example.bybocam.Myapplication.MyApplication;
import com.example.bybocam.R;
import com.example.bybocam.Utils.Myreciever;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.json.JSONObject;
import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCMService";
    private String message;


    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {

        Log.e("Notification", "Notification"+remoteMessage);
        try {
            if (remoteMessage.getData() != null) {
                Log.e(TAG, "1response==" + remoteMessage.getData().toString());
                Map<String, String> data = remoteMessage.getData();
                Log.e(TAG, "2response==" + data.toString());
                if (data.containsKey("message")) {
                    String storedData = data.get("message");
                    Log.e(TAG, "2response==" + storedData);
                    JSONObject jsonObject = new JSONObject(storedData);
                    String title = jsonObject.getString("title");
                    message = jsonObject.getString("body");
                    Log.e("fjhgkfjhkj","fjhgkfjhkj ");
                    Intent intent = new Intent(this, Myreciever.class);
                    if ( MyApplication.ISOPEN) {
                        intent.putExtra("type","1");
                    }
                    else {
                        showNotification(title,message);
                        intent.putExtra("type","0");
                    }
                    sendBroadcast(intent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        Log.e("Notification", "Notification");
    }


    private void showNotification(String title,String msg) {
        Intent intent = null;
        intent = new Intent(this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int value5 = getRandomNumberInRange(0, 99999);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, value5 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo))
                .setSound(defaultSoundUri)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setPriority(Notification.PRIORITY_MAX).setChannelId("1")
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("1", "Bybocam",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(channel);
        }

        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = notificationBuilder.setContentTitle("Bybocam")
                    .setContentText(msg)
                    .setTicker("New Message Alert!")
                    .setSmallIcon(R.mipmap.logo)
                    .setAutoCancel(true)
                    .setChannelId(String.valueOf(1))
                    .setContentIntent(pendingIntent).build();
        } else {
            notification = notificationBuilder.setContentTitle("Bybocam")
                    .setContentText(msg)
                    .setTicker("New Message Alert!")
                    .setSmallIcon(R.mipmap.logo)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent).build();
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private static int getRandomNumberInRangeeee(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
