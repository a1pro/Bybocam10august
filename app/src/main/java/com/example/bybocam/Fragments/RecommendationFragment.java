package com.example.bybocam.Fragments;


import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bybocam.Adapter.RecommendationRecyclerAdapter;
import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Model.RecommendationModel;
import com.example.bybocam.R;
import com.example.bybocam.URL.RetrofitClientIntance;
import com.example.bybocam.Utils.Common;
import com.example.bybocam.Utils.SessionManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RecommendationFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static RecyclerView recyclerView;
    static final Integer LOCATION = 0x1;
    SessionManager session;
    static String userID;
    List<Address> addresses;
    String latitude, longtitude;
    Geocoder geocoder;
    private FusedLocationProviderClient client;
    public static ProgressBar progress;
    public static Context con;
    SwipeRefreshLayout refresh;

    public RecommendationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recommendation, container, false);
        con = container.getContext();
        intialUi(view);
        hitApiCommon();
        if ((latitude == null) && (longtitude == null)) {
            if (Common.isNetworkConnected(getContext())) {
                hitApiCommon();
            } else {
                progress.setVisibility(View.INVISIBLE);
                if (refresh.isRefreshing()) {
                    refresh.setRefreshing(false);
                }
                Toast.makeText(getContext(), "Cheak your network", Toast.LENGTH_SHORT).show();
            }
        }
        return view;
    }

    public void hitApiCommon() {
        String lat = Common.latitude;
        String lon = Common.longitude;
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
        }
        Retrofit retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<RecommendationModel> call = apiListener.recommendationclassAPI(userID, lat, lon);
        call.enqueue(new Callback<RecommendationModel>() {
            @Override
            public void onResponse(Call<RecommendationModel> call, Response<RecommendationModel> response) {
                if (response.isSuccessful()) {
                    if (refresh.isRefreshing()) {
                        refresh.setRefreshing(false);
                    }

                    RecommendationModel data = response.body();
                    progress.setVisibility(View.INVISIBLE);
                    String code = data.getCode();
                    if (code.matches("201")) {
                        for (int i = 0; i < data.getData().size(); i++) {
                            List list = new ArrayList();
                            list.add(data.getData().get(i).getUserId());
                            String image = data.getData().get(i).getUserImage();
                            if (image != null) {
                                list.add(image);
                            }
                            for (int j = 0; j < data.getData().get(i).getUserVideos().size(); j++) {
                                String videoname = data.getData().get(i).getUserVideos().get(j).getVideoThumbnailimg();
                                if (videoname != null) {
                                    list.add(videoname);
                                }
                            }

                        }

                        RecommendationRecyclerAdapter adapter = new RecommendationRecyclerAdapter(con, data);
                        recyclerView.setHasFixedSize(true);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(con, 2);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        recyclerView.setAdapter(adapter);
                    } else {
                        progress.setVisibility(View.INVISIBLE);
                      //  Toast.makeText(con, "Not any user find in your area", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RecommendationModel> call, Throwable t) {
                if (refresh.isRefreshing()) {
                    refresh.setRefreshing(false);
                }
                progress.setVisibility(View.INVISIBLE);
               // Toast.makeText(con, "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hitApi() {
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
        }
        Retrofit retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<RecommendationModel> call = apiListener.recommendationclassAPI(userID, latitude, longtitude);
        call.enqueue(new Callback<RecommendationModel>() {
            @Override
            public void onResponse(Call<RecommendationModel> call, Response<RecommendationModel> response) {
                if (response.isSuccessful()) {
                    if (refresh.isRefreshing()) {
                        refresh.setRefreshing(false);
                    }

                    RecommendationModel data = response.body();
                    progress.setVisibility(View.INVISIBLE);
                    if (data.getCode().equalsIgnoreCase("201")) {
                       // Toast.makeText(getContext(), "Successfully get users", Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < data.getData().size(); i++) {
                            List list = new ArrayList();
                            list.add(data.getData().get(i).getUserId());
                            String image = data.getData().get(i).getUserImage();
                            if (image != null) {
                                list.add(image);
                            }
                            for (int j = 0; j < data.getData().get(i).getUserVideos().size(); j++) {
                                String videoname = data.getData().get(i).getUserVideos().get(j).getVideoName();
                                if (videoname != null) {
                                    list.add(videoname);
                                }
                            }

                        }
                        RecommendationRecyclerAdapter adapter = new RecommendationRecyclerAdapter(getContext(), data);
                        recyclerView.setHasFixedSize(true);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        recyclerView.setAdapter(adapter);
                    } else {
                        progress.setVisibility(View.INVISIBLE);
                       // Toast.makeText(getContext(), "Not any user find in your area", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RecommendationModel> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
               // Toast.makeText(getContext(), "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void intialUi(View view) {
        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);
        getLocation();
        recyclerView = view.findViewById(R.id.recommendationRecyler);
        session = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        userID = user.get(SessionManager.USER_ID);
        progress = view.findViewById(R.id.progress);
        refresh = view.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(this);

    }

    private void askForPermission(String permission, Integer requestCode) {

        if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
            }
        } else {

            getLocation();
        }
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:


                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            status.startResolutionForResult(getActivity(), LOCATION);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });

    }

    private void getLocation() {
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            client.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        geocoder = new Geocoder(getActivity(), Locale.getDefault());
                        latitude = String.valueOf(location.getLatitude());
                        longtitude = String.valueOf(location.getLongitude());
                        hitApi();
                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                            String address = addresses.get(0).getAddressLine(0);
                            String area = addresses.get(0).getLocality();
                            String city = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String pasCode = addresses.get(0).getPostalCode();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        hitApi();
                    }
                }
            });
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(getActivity(), permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case 1:

                    break;
            }
            Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRefresh() {
        if ((latitude == null) && (longtitude == null)) {
            if (Common.isNetworkConnected(getContext())) {
                getLocation();
                hitApiCommon();
            } else {
                progress.setVisibility(View.INVISIBLE);
                refresh.setRefreshing(false);
                Toast.makeText(getContext(), "Cheak your network", Toast.LENGTH_SHORT).show();
            }
        } else {
            hitApi();
        }
    }
}
