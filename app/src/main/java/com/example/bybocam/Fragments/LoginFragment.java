package com.example.bybocam.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bybocam.Activities.Home;
import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Model.LoginModel;
import com.example.bybocam.R;
import com.example.bybocam.URL.RetrofitClientIntance;
import com.example.bybocam.Utils.Common;
import com.example.bybocam.Utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class LoginFragment extends Fragment {
    private EditText userEmail, userPassword;
    private Button loginBtn;
    private TextView signUpTxt, loginForgotPassword;
    private NavController navController;
    ProgressBar progressBar;
    Retrofit retrofit;
    String email, pass;
    SessionManager session;
    String deviceid;

    public LoginFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        session = new SessionManager(getActivity().getApplicationContext());
        intialUi(view);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        deviceid = (preferences.getString("registration_id", ""));
        Log.e("registration_id", deviceid);
        signUpTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_loginFragment_to_signupFragment);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chechConditions()) {
                    if (Common.isNetworkConnected(getContext())) {
                        progressBar.setVisibility(View.VISIBLE);
                        hitApi();

                    }
                    else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "Check your network", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        loginForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_loginFragment_to_forgotPassword1);
            }
        });

        return view;
    }

    private void hitApi() {
        retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<LoginModel> call = apiListener.loginclassAPI(email, pass, "1", deviceid);
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                LoginModel data = response.body();
                if (data != null) {
                    if (data.getCode().equalsIgnoreCase("201")) {
                        Toast.makeText(getContext(), "Login successfully", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        String lat=data.getData().get(0).getLatitude();
                        String lon=data.getData().get(0).getLatitude();
                        String image=data.getData().get(0).getUserImage();
                        String usename=data.getData().get(0).getUserName();
                   //     Toast.makeText(getContext(), ""+data.getData().get(0).getUserImage(), Toast.LENGTH_SHORT).show();
                        session.createLoginSession(pass, email, data.getData().get(0).getUserId(),lat,lon
                                ,usename,image);
                        Intent homeIntent = new Intent(getContext(), Home.class);
                        startActivity(homeIntent);
                        getActivity().finish();
                    } else {
                      Toast.makeText(getContext(), ""+data.getStatus(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                }

            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private boolean chechConditions() {
        email = userEmail.getText().toString().trim();
        pass = userPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email) && (TextUtils.isEmpty(pass))) {

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
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(getContext(), "Enter password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Intialize UI
    private void intialUi(View view) {
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        userEmail = view.findViewById(R.id.loginUserEmail);
        userPassword = view.findViewById(R.id.loginUserPassword);
        loginBtn = view.findViewById(R.id.loginBtnLogin);
        signUpTxt = view.findViewById(R.id.loginSignupText);
        loginForgotPassword = view.findViewById(R.id.loginForgotPassword);
        progressBar = view.findViewById(R.id.progress);
    }


}
