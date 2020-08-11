package com.example.bybocam.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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

import com.example.bybocam.Adapter.FluenceAdapter;
import com.example.bybocam.Adapter.InfluenceProfileAdapter;
import com.example.bybocam.Adapter.RecommendationRecyclerAdapter;
import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Model.GetInfluence;
import com.example.bybocam.Model.InfluenceData;
import com.example.bybocam.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class InfluenceProfileActivity extends AppCompatActivity {
    private String id;
    private List<InfluenceData> list=new ArrayList<>();
    private List<InfluenceData> list2=new ArrayList<>();
    private ProgressBar progress;
    InfluenceProfileAdapter adapter;
    private RecyclerView recycler_view;
    private ImageView filter;
    private String race_,industry_,gender_,lati,longi;
    private String Price="0";
    private ImageView imageBackSearch;
    private TextView tv_alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_influence_profile);
        progress=findViewById(R.id.progress);
        recycler_view=findViewById(R.id.recycler_view);
        tv_alert=findViewById(R.id.tv_alert);
        imageBackSearch=findViewById(R.id.imageBackSearch);
        filter=findViewById(R.id.filter);
        id=getIntent().getStringExtra("id");
        Getinfluence();


        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(InfluenceProfileActivity.this,InfluenceFilterActivity.class);
                startActivityForResult(intent,1);
            }
        });


        imageBackSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
            Call<GetInfluence> call = apiListener.GetInfluence(id);
            call.enqueue(new Callback<GetInfluence>() {
                @Override
                public void onResponse(Call<GetInfluence> call, Response<GetInfluence> response) {
                    if (response.isSuccessful()) {
                        list.clear();
                        progress.setVisibility(View.GONE);
                        GetInfluence data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {

                                list.addAll(data.getData());

                                    if (list.size()>0) {
                                        adapter = new InfluenceProfileAdapter(InfluenceProfileActivity.this, list);
                                        recycler_view.setHasFixedSize(true);
                                        GridLayoutManager gridLayoutManager = new GridLayoutManager(InfluenceProfileActivity.this, 2);
                                        recycler_view.setLayoutManager(gridLayoutManager);
                                        recycler_view.setAdapter(adapter);
                                    }


                            } else {
                                tv_alert.setVisibility(View.VISIBLE);
                                progress.setVisibility(View.GONE);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetInfluence> call, Throwable t) {
                    Toast.makeText(InfluenceProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.e("exception",e.getMessage());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (data!=null){
                race_=data.getStringExtra("race");
                gender_=data.getStringExtra("gender");
                industry_=data.getStringExtra("industry");
                Price=data.getStringExtra("price");
                lati=data.getStringExtra("latitude");
                longi=data.getStringExtra("logitude");

                GetinfluenceFilter();

//                Log.e("race",race_);
//                Log.e("gender",gender_);
//                Log.e("industry",industry_);
//                Log.e("price",Price);
//                Log.e("lat",lati);
//                Log.e("longi",longi);
            }
        }
    }

    private void GetinfluenceFilter() {
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
            Call<GetInfluence> call = apiListener.GetInfluenceFilter(id,lati
                    ,longi,Price,industry_,gender_,race_);
            call.enqueue(new Callback<GetInfluence>() {
                @Override
                public void onResponse(Call<GetInfluence> call, Response<GetInfluence> response) {
                    if (response.isSuccessful()) {
                        list2.clear();
                        getlistclear();
                        progress.setVisibility(View.GONE);
                        GetInfluence data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {

                                list2.addAll(data.getData());
                                Toast.makeText(InfluenceProfileActivity.this, ""+data.getStatus(), Toast.LENGTH_SHORT).show();

                                if (list2.size()>0) {

                                    adapter = new InfluenceProfileAdapter(InfluenceProfileActivity.this, list2);
                                    recycler_view.setHasFixedSize(true);
                                    GridLayoutManager gridLayoutManager = new GridLayoutManager(InfluenceProfileActivity.this, 2);
                                    recycler_view.setLayoutManager(gridLayoutManager);
                                    recycler_view.setAdapter(adapter);
                                }else {
                                    Toast.makeText(InfluenceProfileActivity.this, "size 0", Toast.LENGTH_SHORT).show();
                                }



                            } else {
                                Toast.makeText(InfluenceProfileActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
                                progress.setVisibility(View.GONE);
                                tv_alert.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetInfluence> call, Throwable t) {
                    Toast.makeText(InfluenceProfileActivity.this, "fsilure"+t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("failsLog",t.getMessage());
                    progress.setVisibility(View.GONE);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.e("exception",e.getMessage());
        }
    }

    private void getlistclear() {
        list.clear();
        adapter = new InfluenceProfileAdapter(InfluenceProfileActivity.this, list);
        recycler_view.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(InfluenceProfileActivity.this, 2);
        recycler_view.setLayoutManager(gridLayoutManager);
        recycler_view.setAdapter(adapter);
    }
}