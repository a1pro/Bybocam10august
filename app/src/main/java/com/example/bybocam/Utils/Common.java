package com.example.bybocam.Utils;

import android.content.Context;
import android.net.ConnectivityManager;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class Common {
    public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static String image_base_url = "https://a1professionals.net/bybocam/assets/images/";
    public static String image_post_url = "https://a1professionals.net/bybocam/assets/posts/";
    public static String video_base_url = "https://a1professionals.net/bybocam/assets/videos/";
    public static String message_Image_base_url = "http://srv1.a1professionals.net/bybocam/assets/messageAsImage/";
    public static String latitude,longitude;

    public static String Influencer_image_url = "http://a1professionals.net/bybocam/assets/picture/";
    public static String RANDOM_Base_URL = "http://a1professionals.net/bybocam/assets/randomVideo/";

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo()!= null;
    }
}
