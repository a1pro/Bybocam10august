package com.example.bybocam.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bybocam.Activities.Gallary;
import com.example.bybocam.Activities.MessageScreen;
import com.example.bybocam.Activities.SearchUser;
import com.example.bybocam.Adapter.newAdapter.AllMessageRecyclerAdapter;
import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Model.GetAllMessagesModel;
import com.example.bybocam.Model.ViewProfileModel;
import com.example.bybocam.R;
import com.example.bybocam.URL.RetrofitClientIntance;
import com.example.bybocam.Utils.Common;
import com.example.bybocam.Utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AllMessagesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FloatingActionButton mFloatingActionButton;
    private Retrofit retrofit;
    private SessionManager sessionManager;
    private RecyclerView allMessagesRecyler;
    private String userID;
    private ImageView imageSerach, openCamera;
    ProgressBar progress1232;
    SwipeRefreshLayout refresh;

    public AllMessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_messages, container, false);
        sessionManager = new SessionManager(getContext().getApplicationContext());
        openCamera = view.findViewById(R.id.openCamera);
        refresh = view.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(this);
        progress1232=view.findViewById(R.id.progress1232);
        imageSerach = view.findViewById(R.id.imageSerach);
        allMessagesRecyler = view.findViewById(R.id.allMessagesRecyler);
        HashMap<String, String> user = sessionManager.getUserDetails();
        userID = user.get(SessionManager.USER_ID);
        getLocation();
        mFloatingActionButton = view.findViewById(R.id.addfloatMessage);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent messsageIntent = new Intent(getActivity(), MessageScreen.class);
                startActivity(messsageIntent);
            }
        });
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Gallary.class);
                startActivity(intent);
            }
        });
        imageSerach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchUser.class);
                startActivity(intent);
            }
        });
        hitApiForGettingAllMessages();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        hitApiForGettingAllMessages();
    }

    private void hitApiForGettingAllMessages() {
        progress1232.setVisibility(View.VISIBLE);
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
        }
        refresh.setRefreshing(false);
        retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<GetAllMessagesModel> call = apiListener.getAllMessagesApi(userID);
        call.enqueue(new Callback<GetAllMessagesModel>() {
            @Override
            public void onResponse(Call<GetAllMessagesModel> call, Response<GetAllMessagesModel> response) {

                if (response.isSuccessful()) {
                    GetAllMessagesModel data = response.body();
                    if (data.getCode().equalsIgnoreCase("201")) {
                        AllMessageRecyclerAdapter adapter = new AllMessageRecyclerAdapter(getContext(), data);
                        allMessagesRecyler.setHasFixedSize(true);
                        allMessagesRecyler.setLayoutManager(new LinearLayoutManager(getContext()));
                        allMessagesRecyler.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        if (refresh.isRefreshing()){
                            refresh.setRefreshing(false);
                        }
                        progress1232.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getContext(), "" + response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GetAllMessagesModel> call, Throwable t) {
                if (refresh.isRefreshing()) {
                    refresh.setRefreshing(false);
                }
                progress1232.setVisibility(View.GONE);
            }
        });
    }

    public void getLocation() {
        retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<ViewProfileModel> call = apiListener.viewProfileAPI(userID);
        call.enqueue(new Callback<ViewProfileModel>() {
            @Override
            public void onResponse(Call<ViewProfileModel> call, Response<ViewProfileModel> response) {
                ViewProfileModel data = response.body();
                if (data != null) {
                    if (data.getCode().equalsIgnoreCase("201")) {
                        String lat = data.getData().get(0).getLatitude();
                        String lon = data.getData().get(0).getLongitude();
                        if ((lat != null) && (lon != null)) {
                            Common.latitude = lat;
                            Common.longitude = lon;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ViewProfileModel> call, Throwable t) {

            }
        });
    }


    @Override
    public void onRefresh() {
        if (Common.isNetworkConnected(getContext())) {
            hitApiForGettingAllMessages();
        } else {
            refresh.setRefreshing(false);
            Toast.makeText(getContext(), "Please check your network", Toast.LENGTH_SHORT).show();
        }
    }
}
