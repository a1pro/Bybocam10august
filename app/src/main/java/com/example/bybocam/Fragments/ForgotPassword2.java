package com.example.bybocam.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Model.AddComentModel;
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
public class ForgotPassword2 extends Fragment {


    private EditText forgot2UserEmail,forgot2UserNewPassword,forgot2UserConfirmPassword;
    private Button forgot2Submit;
    private NavController navController;
    private ImageView imageBackForgotPassword2;
    private String otp,newPass,confirmPass;
    String myInt;
    Retrofit retrofit;
    ProgressBar progress;
    public ForgotPassword2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_forgot_password2, container, false);
        intialUi(view);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
             myInt = bundle.getString("otp");
            Log.e("otpValue", myInt);
        }
        forgot2Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkCondition()){
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
        imageBackForgotPassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_forgotPassword2_to_forgotPassword1);
            }
        });
        return  view;
    }

    private void hitApi() {
        retrofit= RetrofitClientIntance.retroInit();
        final ApiListener apiListener=retrofit.create(ApiListener.class);
        Call<AddComentModel> call=apiListener.forgotTwoclassAPI(otp,newPass,confirmPass);
        call.enqueue(new Callback<AddComentModel>() {
            @Override
            public void onResponse(Call<AddComentModel> call, Response<AddComentModel> response) {
                AddComentModel data=response.body();
                if (data!=null){
                    if (data.getCode().equalsIgnoreCase("201")){
                        Toast.makeText(getContext(), "Password change successfully", Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.action_forgotPassword2_to_loginFragment);
                        progress.setVisibility(View.INVISIBLE);
                    }
                    else {
                        Toast.makeText(getContext(), "Invalid OTP", Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.INVISIBLE);
                    }
                }
                else {
                    Toast.makeText(getContext(), "Try again", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<AddComentModel> call, Throwable t) {
                Toast.makeText(getContext(), "Try again", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.INVISIBLE);
            }
        });
    }

    private boolean checkCondition() {
        otp=forgot2UserEmail.getText().toString().trim();
        newPass=forgot2UserNewPassword.getText().toString().trim();
        confirmPass=forgot2UserConfirmPassword.getText().toString();
        if (TextUtils.isEmpty(otp) && (TextUtils.isEmpty(newPass)) && (TextUtils.isEmpty(confirmPass))) {

            Toast.makeText(getContext(), "Enter all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(otp)) {
            Toast.makeText(getContext(), "Enter email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(newPass)) {
            Toast.makeText(getContext(), "Enter user name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(confirmPass)) {
            Toast.makeText(getContext(), "Enter password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (newPass.length() < 6) {
            Toast.makeText(getContext(), "Password lenth is not less than 6", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void intialUi(View view) {
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        forgot2UserEmail=view.findViewById(R.id.forgot2UserOTP);
        forgot2UserNewPassword=view.findViewById(R.id.forgot2UserNewPassword);
        forgot2UserConfirmPassword=view.findViewById(R.id.forgot2UserConfirmPassword);
        forgot2Submit=view.findViewById(R.id.forgot2Submit);
        imageBackForgotPassword2=view.findViewById(R.id.imageBackForgotPassword2);
        progress=view.findViewById(R.id.progress);
    }
}
