package com.example.bybocam.Fragments;


import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Model.ViewProfileModel;
import com.example.bybocam.R;
import com.example.bybocam.URL.RetrofitClientIntance;
import com.example.bybocam.Utils.Common;
import com.example.bybocam.Utils.FingerprintDialog;
import com.example.bybocam.Utils.SessionManager;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.KEYGUARD_SERVICE;

public class SplashOneFragmnet extends Fragment {

    public static ImageView splash2Image;
    Context context;
    private NavController navController;
    private Animation animation;
    SessionManager session;
    private String userID;
    private Retrofit retrofit;
    FingerprintManagerCompat managerCompat;


    public SplashOneFragmnet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_splash_one_fragmnet, container, false);
        splash2Image = view.findViewById(R.id.splash2Image);
        session = new SessionManager(getActivity().getApplicationContext());
        context = container.getContext();

        HashMap<String, String> user = session.getUserDetails();
        userID = user.get(SessionManager.USER_ID);
        if (userID != null) {
            getLocation();
        }
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        animation = AnimationUtils.loadAnimation(context, R.anim.animation);
        splash2Image.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (session.isLoggedIn()) {
                    managerCompat = FingerprintManagerCompat.from(getActivity());
                    if (managerCompat.isHardwareDetected() && managerCompat.hasEnrolledFingerprints()) {
                        showFingerPrintDialog();
                    } else {
                        KeyguardManager km = (KeyguardManager) getActivity().getSystemService(KEYGUARD_SERVICE);
                        if (km.isKeyguardSecure()) {
                            Intent i = km.createConfirmDeviceCredentialIntent("Authentication required", "password");
                            startActivityForResult(i, 0);
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "No any security setup done by user(pattern or password or pin or fingerprint", Toast.LENGTH_SHORT).show();
                            if (session.checkLogin()) {
                                getActivity().finish();
                            } else {
                                navController.navigate(R.id.splashFragment);
                            }
                        }
                    }
                }
                else {
                    if (session.checkLogin()) {
                        getActivity().finish();
                    } else {
                        navController.navigate(R.id.splashFragment);
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return view;
    }

    private void showFingerPrintDialog() {

        FingerprintDialog fragment = new FingerprintDialog();
        fragment.setContext(getActivity());
        fragment.show(getActivity().getSupportFragmentManager(), "");


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                if (session.checkLogin()) {
                    getActivity().finish();
                } else {
                    navController.navigate(R.id.splashFragment);
                }
            }
        }
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

    private String gettouchid() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return preferences.getString("Touchid", null);
    }


}
