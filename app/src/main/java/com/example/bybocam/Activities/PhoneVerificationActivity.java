package com.example.bybocam.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bybocam.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import java.util.concurrent.TimeUnit;

public class PhoneVerificationActivity extends AppCompatActivity {
    private EditText edit_profile_user_phone;
    private CountryCodePicker ccp;
    private ImageView imageBackViewProfile;
    private Button bt_send;
    private String TAG = "Bybocam Verification code";
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mobile;
    private EditText otp;
    private FirebaseAuth mAuth;
    private PhoneAuthCredential credential1;
    private TextView tv_mobile;
    String otp1;
    AlertDialog dialogBuilder;
    String countrycode="+91";
    private  String namecode="IN +91";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        mAuth=FirebaseAuth.getInstance();
        edit_profile_user_phone = findViewById(R.id.edit_profile_user_phone);
        ccp = findViewById(R.id.ccp);

        imageBackViewProfile = findViewById(R.id.imageBackViewProfile);
        bt_send = findViewById(R.id.bt_send);


        imageBackViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countrycode=ccp.getSelectedCountryCodeWithPlus();
                String  code = ccp.getSelectedCountryCodeWithPlus();
                String name=ccp.getSelectedCountryNameCode();

//                StringBuffer sb = new StringBuffer();
//                sb.append(Character.toChars(Integer.parseInt(ccp.getFullNumberWithPlus())));



//                Log.e("flag",""+sb);





                namecode=name+" "+code;

            }
        });
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    mobile=edit_profile_user_phone.getText().toString();
                    sendVerificationCode();
            }
        });


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                 otp1=credential.getSmsCode();
                if (otp1!=null){


                }
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                // Log.d(TAG, "onVerificationCompleted:" + credential);
                Toast.makeText(PhoneVerificationActivity.this, "Verification complete", Toast.LENGTH_SHORT).show();
                credential1=PhoneAuthProvider.getCredential(mVerificationId,otp1);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                //   Log.w(TAG, "onVerificationFailed", e);
                Toast.makeText(PhoneVerificationActivity.this, "Verification fail"+e.getMessage(), Toast.LENGTH_SHORT).show();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);
                Toast.makeText(PhoneVerificationActivity.this, "Code sent", Toast.LENGTH_SHORT).show();
                // Save verification ID and resending token so we can use them later
                showdialog();
                mVerificationId = verificationId;
                mResendToken = token;

                // ...
            }
        };

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential1) {
        mAuth.signInWithCredential(credential1)
                .addOnCompleteListener(PhoneVerificationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //verification successful we will start the profile activity
                            if (otp1!=null){
//                                                        dialogBuilder.dismiss();
                               String num= edit_profile_user_phone.getText().toString();
                               Intent intent=new Intent();
                               intent.putExtra("verfiednumber",num);
                               intent.putExtra("code",namecode);
                               setResult(RESULT_OK,intent);
                               finish();


                            }else {
                                Toast.makeText(PhoneVerificationActivity.this, "null", Toast.LENGTH_SHORT).show();
                            }


                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                        }
                    }
                });

    }

    private void sendVerificationCode() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                countrycode+mobile,      // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                PhoneVerificationActivity.this,               // Activity (for callback binding)
                mCallbacks);
    }


    private void showdialog(){
        dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.phone_item, null);
        dialogBuilder.setCancelable(false);
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        final EditText editText = (EditText) dialogView.findViewById(R.id.edt_comment);
        Button button1 = (Button) dialogView.findViewById(R.id.buttonSubmit);
        Button button2 = (Button) dialogView.findViewById(R.id.buttonCancel);
        if (otp1!=null){
            editText.setText(otp1);
        }
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DO SOMETHINGS
                signInWithPhoneAuthCredential(credential1);
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }


    private boolean validation(){
        if (otp.getText().toString().isEmpty()){
            return false;
        }
        return true;
    }

}