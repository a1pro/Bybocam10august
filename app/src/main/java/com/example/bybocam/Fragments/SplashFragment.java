package com.example.bybocam.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.bybocam.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SplashFragment extends Fragment {

    private Button logiBtn, signUpBtn;
    private NavController navController;
    public static ImageView imageView;


    public SplashFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        intialUi(view);

        // Go to login fragment
        logiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_splashFragment_to_loginFragment);
            }
        });

        //Go to signup fragment
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_splashFragment_to_signupFragment);
            }
        });
        return view;

    }

    // Initialize UI
    private void intialUi(View view) {
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        logiBtn = view.findViewById(R.id.splashLoginButton);
        signUpBtn = view.findViewById(R.id.splashSignupButton);
        imageView = view.findViewById(R.id.splashImageLogo);
    }

}
