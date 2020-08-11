package com.example.bybocam.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bybocam.Adapter.newAdapter.NewMessageScreenAdapter;
import com.example.bybocam.Adapter.newAdapter.ViewAllFolowersRecyclerAdapter;
import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Model.GetAllFollowersModel;
import com.example.bybocam.Model.GetFavouriteUserModel;
import com.example.bybocam.R;
import com.example.bybocam.URL.RetrofitClientIntance;
import com.example.bybocam.Utils.SessionManager;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ViewAllFollowers extends AppCompatActivity {

    RecyclerView view_follower_recycler;
    ImageView imageBackFollowers;
    GetFavouriteUserModel data;
    SessionManager sessionManager;
    ProgressBar progress;
    String userID,type;
    GetAllFollowersModel dataNew;
    private TextView title_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_followers);
        intialUi();
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        userID = user.get(SessionManager.USER_ID);
        Intent i=this.getIntent();{
            if (i!=null){
                type=i.getStringExtra("type");
            }
            if (type.equalsIgnoreCase("follower")){
                hitFollowerAPI(userID);
                title_tv.setText("Followers");
            }
            else {
                hitFollowerApi(userID);
                title_tv.setText("Following");
            }
        }

        imageBackFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void hitFollowerAPI(String userID) {
        progress.setVisibility(View.VISIBLE);
        Retrofit retrofit = RetrofitClientIntance.retroInit();
        ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<GetAllFollowersModel> call = apiListener.getAllFollowersApi(userID);
        call.enqueue(new Callback<GetAllFollowersModel>() {
            @Override
            public void onResponse(Call<GetAllFollowersModel> call, Response<GetAllFollowersModel> response) {
                progress.setVisibility(View.INVISIBLE);
                if (response.isSuccessful()) {
                    dataNew = response.body();
                    if (dataNew.getCode().equalsIgnoreCase("201")) {
                        ViewAllFolowersRecyclerAdapter mViewAllFolowersRecyclerAdapter = new ViewAllFolowersRecyclerAdapter(dataNew,data, ViewAllFollowers.this,1);
                        view_follower_recycler.setHasFixedSize(true);
                        view_follower_recycler.setLayoutManager(new LinearLayoutManager(ViewAllFollowers.this));
                        view_follower_recycler.setAdapter(mViewAllFolowersRecyclerAdapter);
                    }
                }
            }
            @Override
            public void onFailure(Call<GetAllFollowersModel> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
                Log.e("error",""+t.getMessage());
                Toast.makeText(ViewAllFollowers.this, "Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hitFollowerApi(String id) {
        progress.setVisibility(View.VISIBLE);
        Retrofit retrofit = RetrofitClientIntance.retroInit();
        ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<GetFavouriteUserModel> call = apiListener.likedUserclassAPI(id);
        call.enqueue(new Callback<GetFavouriteUserModel>() {
            @Override
            public void onResponse(Call<GetFavouriteUserModel> call, Response<GetFavouriteUserModel> response) {
                progress.setVisibility(View.INVISIBLE);
                if (response.isSuccessful()) {
                    data = response.body();
                    if (data.getCode().equalsIgnoreCase("201")) {
                        ViewAllFolowersRecyclerAdapter mViewAllFolowersRecyclerAdapter = new ViewAllFolowersRecyclerAdapter(dataNew,data, ViewAllFollowers.this,0);
                        view_follower_recycler.setHasFixedSize(true);
                        view_follower_recycler.setLayoutManager(new LinearLayoutManager(ViewAllFollowers.this));
                        view_follower_recycler.setAdapter(mViewAllFolowersRecyclerAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetFavouriteUserModel> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
                Log.e("error",""+t.getMessage());
                Toast.makeText(ViewAllFollowers.this, "Please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void intialUi() {
        progress=findViewById(R.id.progress);
        title_tv=findViewById(R.id.title_tv);
        view_follower_recycler=findViewById(R.id.view_follower_recycler);
        imageBackFollowers=findViewById(R.id.imageBackFollowers);
    }
}
