package com.example.bybocam.Myapplication;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MyApplication extends Application {


    private static Context context;
    public static boolean ISOPEN=false;

    public static Context getAppContext() {
        return MyApplication.context;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        getDeviceID2();
        MyApplication.context = getApplicationContext();
    }

    // Get new Instance ID token
    private void getDeviceID() {
        String AndroidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("registration_id", AndroidId);
        editor.apply();

    }

    private void getDeviceID2() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e("Log", "getInstanceId failed", task.getException());
                            return;
                        }
                        if (task.getResult() != null) {
                            String token = task.getResult().getToken();
                            if (!token.isEmpty()) {
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("registration_id", token);
                                editor.apply();
                            }
                        } else {
                            Log.e("Log", "getInstanceId failed", task.getException());
                        }
                    }
                });

    }



}

