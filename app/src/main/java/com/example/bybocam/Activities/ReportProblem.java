package com.example.bybocam.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Model.AddComentModel;
import com.example.bybocam.R;
import com.example.bybocam.URL.RetrofitClientIntance;
import com.example.bybocam.Utils.SessionManager;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReportProblem extends AppCompatActivity {

    private ImageView imageBackReport;
    private SessionManager sessionManager;
    private String userID;
    private TextView editTextMessage, editDescription;
    private Button sendReportBtn;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_problem);
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        userID = user.get(SessionManager.USER_ID);
        intialUi();
        imageBackReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        sendReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hitAddReportApi();
            }
        });
    }

    private void intialUi() {
        imageBackReport = findViewById(R.id.imageBackReport);
        editTextMessage=findViewById(R.id.editTextMessage);
        editDescription=findViewById(R.id.editDescription);
        sendReportBtn=findViewById(R.id.sendReportBtn);
    }

    private void hitAddReportApi() {
        if (editTextMessage.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter title", Toast.LENGTH_SHORT).show();
        } else if (editDescription.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter description", Toast.LENGTH_SHORT).show();
        } else {
            retrofit = RetrofitClientIntance.retroInit();
            final ApiListener apiListener = retrofit.create(ApiListener.class);
            Call<AddComentModel> call = apiListener.reportApi(userID, editTextMessage.getText().toString(),
                    editDescription.getText().toString());
            call.enqueue(new Callback<AddComentModel>() {
                @Override
                public void onResponse(Call<AddComentModel> call, Response<AddComentModel> response) {
                    if (response.isSuccessful()) {
                        AddComentModel data = response.body();
                        if (data.getCode().equalsIgnoreCase("201")) {
                            Toast.makeText(ReportProblem.this, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddComentModel> call, Throwable t) {
                    Toast.makeText(ReportProblem.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}
