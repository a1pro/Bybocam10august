package com.example.bybocam.Fragments;


import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Model.AddComentModel;
import com.example.bybocam.R;
import com.example.bybocam.URL.RetrofitClientIntance;
import com.example.bybocam.Utils.Common;
import com.example.bybocam.Utils.LocationTrack;
import com.google.android.gms.location.FusedLocationProviderClient;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class SignupFragment extends Fragment {
    private EditText userEmailSignUp, userPasswordSignup, userLocationSignup, userUserNameSignup;
    private Button signupBtn;
    private TextView loginTxt;
    private NavController navController;
    static final Integer LOCATION = 0x1;
    private FusedLocationProviderClient client;
    Retrofit retrofit;
    String email, pass, userName;
    double latitude, longtitude;
    ProgressBar progressBar;
    String deviceid;


    Geocoder geocoder;
    List<Address> addresses;
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;


    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        initialUi(view);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        deviceid = (preferences.getString("registration_id", ""));
        getLocation();
        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_signupFragment_to_loginFragment);
            }
        });


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkConditions()) {
                    progressBar.setVisibility(View.VISIBLE);
                    if (Common.isNetworkConnected(getContext())) {
                        hitApi();

                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "Check your network", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });
        return view;
    }

    public void getLocation() {
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        locationTrack = new LocationTrack(getActivity());
        if (locationTrack.canGetLocation()) {


            longtitude = locationTrack.getLongitude();
            latitude = locationTrack.getLatitude();


           // Toast.makeText(getActivity().getApplicationContext(), "Longitude:" + Double.toString(longtitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();

            geocoder = new Geocoder(getActivity(), Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longtitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses!=null){
            if (addresses.size()!=0) {
                String address = addresses.get(0).getAddressLine(0);
                userLocationSignup.setText(address);
            }
            }
        } else {

            locationTrack.showSettingsAlert();
        }
    }

    private void hitApi() {
        String lat = String.valueOf(latitude);
        String lon = String.valueOf(longtitude);
        retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<AddComentModel> call = apiListener.signupclassAPI(userName, email, pass, deviceid, lat, lon);
        call.enqueue(new Callback<AddComentModel>() {
            @Override
            public void onResponse(Call<AddComentModel> call, Response<AddComentModel> response) {
                AddComentModel data = response.body();
                if (data != null) {
                    if (data.getCode().equalsIgnoreCase("201")) {
                        Toast.makeText(getContext(), "Successfully  signup", Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.action_signupFragment_to_loginFragment);
                    } else {
                        Toast.makeText(getContext(), "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }

            }

            @Override
            public void onFailure(Call<AddComentModel> call, Throwable t) {
                Toast.makeText(getContext(), "Please try again", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }


    private boolean checkConditions() {
        email = userEmailSignUp.getText().toString().trim();
        pass = userPasswordSignup.getText().toString().trim();
        userName = userUserNameSignup.getText().toString().trim();
        String location = userLocationSignup.getText().toString().trim();

        if (TextUtils.isEmpty(email) && (TextUtils.isEmpty(pass)) && (TextUtils.isEmpty(userName)) && (TextUtils.isEmpty(location))) {

            Toast.makeText(getContext(), "Enter all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Enter email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!email.matches(Common.emailPattern)) {
            Toast.makeText(getContext(), "Enter correct email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(getContext(), "Enter user name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(getContext(), "Enter password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pass.length() < 6) {
            Toast.makeText(getContext(), "Password lenth is not less than 6", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(location)) {
            Toast.makeText(getContext(), "Enter Location", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Intialize UI
    private void initialUi(View view) {
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        userEmailSignUp = view.findViewById(R.id.signupUserEmail);
        userPasswordSignup = view.findViewById(R.id.signupUserPassword);
        userLocationSignup = view.findViewById(R.id.signupUserLocation);
        userUserNameSignup = view.findViewById(R.id.signupUserName);
        signupBtn = view.findViewById(R.id.signupBtnSignup);
        loginTxt = view.findViewById(R.id.signupLoginText);
        progressBar = view.findViewById(R.id.progress);
    }

    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission((String) perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (ActivityCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (Object perms : permissionsToRequest) {
                    if (!hasPermission((String) perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel(
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }

                break;
        }

    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage("These permissions are mandatory for the application. Please allow access.")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }

}
