package com.example.bybocam.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bybocam.Activities.ViewProfile;
import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Model.ForgotModel1;
import com.example.bybocam.Model.LoginModel;
import com.example.bybocam.R;
import com.example.bybocam.URL.RetrofitClientIntance;
import com.example.bybocam.Utils.Common;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPassword1 extends Fragment {

    private TextView forgot_login_text;
    private Button forgotSubmit;
    private NavController navController;
    private ImageView imageBackForgotPassword1;
    private EditText forgotUserEmail;
    Retrofit retrofit;
    private String email;
    ProgressBar progress;

    public ForgotPassword1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_forgot_password1, container, false);
        initalUI(view);
        forgot_login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_forgotPassword1_to_loginFragment);
            }
        });
        forgotSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkConditions(view)) {
                    if (Common.isNetworkConnected(getContext())) {
                        progress.setVisibility(View.VISIBLE);
                        hitApi();
                    }
                    else {
                        progress.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "Check your network", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });
        imageBackForgotPassword1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_forgotPassword1_to_loginFragment);
            }
        });
        return view;
    }

    private void hitApi() {
        retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<ForgotModel1> call = apiListener.forgotoneclassAPI(email);
        call.enqueue(new Callback<ForgotModel1>() {
            @Override
            public void onResponse(Call<ForgotModel1> call, Response<ForgotModel1> response) {
                ForgotModel1 data=response.body();
                if (data!=null){
                    if (data.getCode().equalsIgnoreCase("201")) {
                        Toast.makeText(getContext(), "Otp has send in your registered email", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putString("otp", data.getOtp());
                        navController.navigate(R.id.action_forgotPassword1_to_forgotPassword2,bundle);
                        progress.setVisibility(View.INVISIBLE);

                    }
                    else {
                        Toast.makeText(getContext(), "Email is not registered", Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.INVISIBLE);
                    }
                }else {
                    Toast.makeText(getContext(), "Email is not registered", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ForgotModel1> call, Throwable t) {
                Toast.makeText(getContext(), "Try again", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void initalUI(View view) {
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        forgotSubmit=view.findViewById(R.id.forgotSubmit);
        forgot_login_text=view.findViewById(R.id.forgot_login_text);
        imageBackForgotPassword1=view.findViewById(R.id.imageBackForgotPassword1);
        forgotUserEmail=view.findViewById(R.id.forgotUserEmail);
        progress=view.findViewById(R.id.progress);
    }
    public boolean checkConditions(View view){
        email = forgotUserEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Enter email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!email.matches(Common.emailPattern)) {
            Toast.makeText(getContext(), "Enter correct email", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
