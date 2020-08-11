package com.example.bybocam.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bybocam.Adapter.BusinessAdapter;
import com.example.bybocam.Adapter.FluenceAdapter;
import com.example.bybocam.Adapter.PersonalAdapter;
import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Model.GetInfluence;
import com.example.bybocam.Model.GetRandom;
import com.example.bybocam.Model.InfluenceData;
import com.example.bybocam.Model.RandomData;
import com.example.bybocam.Model.ResponseData;
import com.example.bybocam.R;
import com.example.bybocam.Utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ShoutLoudVideos extends AppCompatActivity {
    private RecyclerView recyclerview_personal,recyclerview_fluence,recycler_view_business;
    private PersonalAdapter adapter;
    private BusinessAdapter businessAdapter;
    private FluenceAdapter fluenceAdapter;
    private ProgressBar progress;
    SessionManager session;
    private TextView tv_alert;
    private String UserID;
    private List<InfluenceData> influencelist=new ArrayList<>();
    private List<RandomData> randomData=new ArrayList<>();
    int fluencesize=0;
    private TextView tv_seeall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shout_loud_videos);
        session = new SessionManager(ShoutLoudVideos.this.getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        //  email = user.get(SessionManager.KEY_EMAIL);
        UserID = user.get(SessionManager.USER_ID);
      //  Log.e("userid",""+UserID);
        init();
        GetRandom();
        Getinfluence();
        setBusinessAdapter();

        tv_seeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ShoutLoudVideos.this,InfluenceProfileActivity.class);
                intent.putExtra("id",UserID);
                startActivity(intent);
            }
        });
    }

    private void setBusinessAdapter() {
        businessAdapter = new BusinessAdapter(ShoutLoudVideos.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShoutLoudVideos.this, LinearLayoutManager.HORIZONTAL, false);
        recycler_view_business.setLayoutManager(linearLayoutManager);
        recycler_view_business.setAdapter(businessAdapter);
    }

    private void init() {
        tv_seeall=findViewById(R.id.tv_seeall);
        tv_alert=findViewById(R.id.tv_alert);
        progress=findViewById(R.id.progress);
        recyclerview_personal=findViewById(R.id.recyclerview_personal);
        recyclerview_fluence=findViewById(R.id.recyclerview_fluence);
        recycler_view_business=findViewById(R.id.recycler_view_business);
    }


    private void Getinfluence() {
        try {

            progress.setVisibility(View.VISIBLE);

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit2 = new Retrofit.Builder()
                    .baseUrl("https://a1professionals.net/bybocam/api/")
                    .client(client)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            final ApiListener apiListener = retrofit2.create(ApiListener.class);
            Call<GetInfluence> call = apiListener.GetInfluence(UserID);
            call.enqueue(new Callback<GetInfluence>() {
                @Override
                public void onResponse(Call<GetInfluence> call, Response<GetInfluence> response) {
                    if (response.isSuccessful()) {
                        influencelist.clear();
                        progress.setVisibility(View.GONE);
                        GetInfluence data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {

                                influencelist.addAll(data.getData());
                                if (influencelist.size()>3){
                                    fluencesize=3;
                                    fluenceAdapter = new FluenceAdapter(ShoutLoudVideos.this,influencelist,fluencesize);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShoutLoudVideos.this, LinearLayoutManager.HORIZONTAL, false);
                                    recyclerview_fluence.setLayoutManager(linearLayoutManager);
                                    recyclerview_fluence.setAdapter(fluenceAdapter);
                                }else {
                                    fluencesize=influencelist.size();
                                    fluenceAdapter = new FluenceAdapter(ShoutLoudVideos.this,influencelist,fluencesize);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShoutLoudVideos.this, LinearLayoutManager.HORIZONTAL, false);
                                    recyclerview_fluence.setLayoutManager(linearLayoutManager);
                                    recyclerview_fluence.setAdapter(fluenceAdapter);
                                }



                            } else {
                                progress.setVisibility(View.GONE);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetInfluence> call, Throwable t) {
                    Toast.makeText(ShoutLoudVideos.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.e("exception",e.getMessage());
        }
    }





    private void GetRandom() {
        try {

            progress.setVisibility(View.VISIBLE);

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit2 = new Retrofit.Builder()
                    .baseUrl("https://a1professionals.net/bybocam/api/")
                    .client(client)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            final ApiListener apiListener = retrofit2.create(ApiListener.class);
            Call<GetRandom> call = apiListener.GetRAndomVideo(UserID);
            call.enqueue(new Callback<GetRandom>() {
                @Override
                public void onResponse(Call<GetRandom> call, Response<GetRandom> response) {
                    if (response.isSuccessful()) {
                        randomData.clear();
                        progress.setVisibility(View.GONE);
                        GetRandom data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {

                                randomData.addAll(data.getData());

                                if (randomData.size()>0) {
                                    adapter = new PersonalAdapter(ShoutLoudVideos.this, randomData);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShoutLoudVideos.this, LinearLayoutManager.HORIZONTAL, false);
                                    recyclerview_personal.setLayoutManager(linearLayoutManager);
                                    recyclerview_personal.setAdapter(adapter);
                                }else {
                                    tv_alert.setVisibility(View.VISIBLE);
                                   // Toast.makeText(ShoutLoudVideos.this, "0 ====", Toast.LENGTH_SHORT).show();

                                }


                            } else {
                                tv_alert.setVisibility(View.VISIBLE);
                          //      Toast.makeText(ShoutLoudVideos.this, ""+data.getStatus(), Toast.LENGTH_SHORT).show();
                                progress.setVisibility(View.GONE);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetRandom> call, Throwable t) {
                    Toast.makeText(ShoutLoudVideos.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.e("random_exception",e.getMessage()+"==="+e.getLocalizedMessage());
        }
    }
}