package com.example.bybocam.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bybocam.Adapter.newAdapter.NotificationAdapter;
import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Model.GetAllNotificationsModel;
import com.example.bybocam.R;
import com.example.bybocam.URL.RetrofitClientIntance;
import com.example.bybocam.Utils.Common;
import com.example.bybocam.Utils.SessionManager;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NotificationScreen extends AppCompatActivity {

    private ImageView imageBackNotification;
    private RecyclerView notification_Recycler;
    SessionManager sessionManager;
    String userId;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_screen);
        intialUi();
        imageBackNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void intialUi() {
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get(SessionManager.USER_ID);
        imageBackNotification = findViewById(R.id.imageBackNotification);
        notification_Recycler = findViewById(R.id.notification_Recycler);
        progress = findViewById(R.id.progress);
        if (Common.isNetworkConnected(NotificationScreen.this)) {
            hitGetAllNotificationAPI();
        }
    }

    private void hitGetAllNotificationAPI() {
        Retrofit retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<GetAllNotificationsModel> call = apiListener.getAllNotificationApi(userId);
        call.enqueue(new Callback<GetAllNotificationsModel>() {
            @Override
            public void onResponse(Call<GetAllNotificationsModel> call, Response<GetAllNotificationsModel> response) {
                if (response.isSuccessful()) {
                    GetAllNotificationsModel data = response.body();
                    if (data.getCode().equalsIgnoreCase("201")) {
                        NotificationAdapter adapter = new NotificationAdapter(NotificationScreen.this, data);
                        notification_Recycler.setHasFixedSize(true);
                        notification_Recycler.setHasFixedSize(true);
                        notification_Recycler.setLayoutManager(new LinearLayoutManager(NotificationScreen.this));
                        if (data.getData().size() != 0) {
                            notification_Recycler.smoothScrollToPosition(adapter.getItemCount() - 1);
                        }
                        adapter.notifyDataSetChanged();
                        notification_Recycler.setAdapter(adapter);
                        progress.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetAllNotificationsModel> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
                Toast.makeText(NotificationScreen.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
