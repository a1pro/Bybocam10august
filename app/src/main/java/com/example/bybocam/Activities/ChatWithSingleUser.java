package com.example.bybocam.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bybocam.Adapter.ChatAdapter;
import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Model.AddComentModel;
import com.example.bybocam.Model.GetChatMessagesModel;
import com.example.bybocam.Myapplication.MyApplication;
import com.example.bybocam.R;
import com.example.bybocam.URL.RetrofitClientIntance;
import com.example.bybocam.Utils.SessionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChatWithSingleUser extends AppCompatActivity {

    private static RecyclerView singlechatrecycler;
    String userName;
    SessionManager sessionManager;
    private ImageView imageBackChatMessage, pickImageToMessage, send_message;
    static String senderId, recieverId;
    static TextView homeNametitle, noDataFound;
    private EditText editTextMessage;
    private static ProgressBar progress321321231;


    /////Alert dialog

    Dialog alertDialog;
    Button dismis, clear;
    TextView dialog_title, dialog_message;
    private static final int REQUEST_GALLERY_CODE = 200;
    private int REQUEST_STORAGE_PERMISSION_RESULT = 1;
    private Uri uri;
    private File file;
    int i = 0;
    static ChatAdapter adapter;
    static List<GetChatMessagesModel.Datum> list = new ArrayList<>();
    Thread t;
    private static int abc = 0;
    final Handler handler = new Handler();

    private void updateUI() {
        hitSingleChatMessageApi();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_with_single_user);
        MyApplication.ISOPEN = true;
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        senderId = user.get(SessionManager.USER_ID);
        homeNametitle = findViewById(R.id.homeNametitle);
        progress321321231 = findViewById(R.id.progress321321231);
        imageBackChatMessage = findViewById(R.id.imageBackChatMessage);
        noDataFound = findViewById(R.id.noDataFound);
        pickImageToMessage = findViewById(R.id.pickImageToMessage);
        send_message = findViewById(R.id.send_message);
        editTextMessage = findViewById(R.id.editTextMessage);
        singlechatrecycler = findViewById(R.id.singlechatrecycler);
        progress321321231.setVisibility(View.VISIBLE);

        Intent intent = this.getIntent();
        if (intent != null) {
            recieverId = intent.getStringExtra("reciverId");
            userName = intent.getStringExtra("username");
            if (userName != null) {
                homeNametitle.setText(userName);
            }
        }
        imageBackChatMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextMessage.getText().toString().isEmpty()) {
                    Toast.makeText(ChatWithSingleUser.this, "Please enter message", Toast.LENGTH_SHORT).show();
                } else {
                    hitApiForSendingMessage(recieverId, editTextMessage.getText().toString());
                }
            }
        });
        pickImageToMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(ChatWithSingleUser.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_GRANTED) {

                        alertDialog = new Dialog(ChatWithSingleUser.this);
                        LayoutInflater inflater = ChatWithSingleUser.this.getLayoutInflater();
                        View add_menu_layout = inflater.inflate(R.layout.custom_layout_dialog, null);
                        dialog_title = add_menu_layout.findViewById(R.id.dialog_title);
                        dialog_message = add_menu_layout.findViewById(R.id.dialog_message);
                        dismis = add_menu_layout.findViewById(R.id.discard_btn);
                        clear = add_menu_layout.findViewById(R.id.cancle_btn);
                        dialog_title.setText("Choose Image");
                        dialog_message.setText("Select where you want to pick image");
                        clear.setText("Gallery");
                        dismis.setText("Camera");
                        dismis.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture, 0);
                                alertDialog.dismiss();
                            }
                        });

                        clear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                                openGalleryIntent.setType("image/*");
                                startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE);
                                alertDialog.dismiss();
                            }
                        });

                        alertDialog.setContentView(add_menu_layout);
                        alertDialog.show();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION_RESULT);
                    }
                } else {
                    alertDialog = new Dialog(ChatWithSingleUser.this);
                    LayoutInflater inflater = ChatWithSingleUser.this.getLayoutInflater();
                    View add_menu_layout = inflater.inflate(R.layout.custom_layout_dialog, null);

                    dialog_title = add_menu_layout.findViewById(R.id.dialog_title);
                    dialog_message = add_menu_layout.findViewById(R.id.dialog_message);
                    dismis = add_menu_layout.findViewById(R.id.discard_btn);
                    clear = add_menu_layout.findViewById(R.id.cancle_btn);
                    dialog_title.setText("Choose Image");
                    dialog_message.setText("Select where you want to pick image");
                    clear.setText("Gallery");
                    dismis.setText("Camera");
                    dismis.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, 0);
                            alertDialog.dismiss();
                        }
                    });

                    clear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                            openGalleryIntent.setType("image/*");
                            startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE);
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.setContentView(add_menu_layout);
                    alertDialog.show();
                }
            }
        });
        hitSingleChatMessageApi();
        adapter = new ChatAdapter(ChatWithSingleUser.this, list, senderId);
        singlechatrecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatWithSingleUser.this);
        singlechatrecycler.getRecycledViewPool().setMaxRecycledViews(0, 0);
        if (abc == 0) {
            singlechatrecycler.smoothScrollToPosition(adapter.getItemCount() - 1);
            abc = abc + 1;
        }
//        linearLayoutManager.setStackFromEnd(true);
        singlechatrecycler.setLayoutManager(linearLayoutManager);
        singlechatrecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        }, 20000);

    }


    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.ISOPEN = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.ISOPEN = false;
        abc = 0;
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.ISOPEN = false;
        abc = 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.ISOPEN = false;
        abc = 0;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(ChatWithSingleUser.this, "Permissions are  required to run app correctly", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void hitApiForSendingMessage(String recieverId, String message) {
        progress321321231.setVisibility(View.VISIBLE);
        Retrofit retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<AddComentModel> call = apiListener.addSingleMessagesApi(senderId, recieverId, message);
        call.enqueue(new Callback<AddComentModel>() {
            @Override
            public void onResponse(Call<AddComentModel> call, Response<AddComentModel> response) {
                if (response.isSuccessful()) {
                    AddComentModel data = response.body();
                    if (data.getCode().equalsIgnoreCase("201")) {
                        progress321321231.setVisibility(View.INVISIBLE);
                        editTextMessage.setText("");
                        hideKeyboard(ChatWithSingleUser.this);
                        abc = 0;
                        hitSingleChatMessageApi();
                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<AddComentModel> call, Throwable t) {
                Toast.makeText(ChatWithSingleUser.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                progress321321231.setVisibility(View.INVISIBLE);
            }
        });

    }

    public static void hitSingleChatMessageApi() {
        Retrofit retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<GetChatMessagesModel> call = apiListener.getSingleMessagesApi(senderId, recieverId);
        call.enqueue(new Callback<GetChatMessagesModel>() {
            @Override
            public void onResponse(Call<GetChatMessagesModel> call, Response<GetChatMessagesModel> response) {

                if (response.isSuccessful()) {
                    GetChatMessagesModel data = response.body();
                    if (data.getCode().equalsIgnoreCase("201")) {
                        list.clear();
                        for (int i = 0; i < data.getData().size(); i++) {
                            list.add(data.getData().get(i));
                        }
                        adapter = new ChatAdapter(MyApplication.getAppContext(), list, senderId);
                        singlechatrecycler.getRecycledViewPool().setMaxRecycledViews(0, 0);
                        if (list.size() != 0) {
                            if (abc == 0) {
                                singlechatrecycler.smoothScrollToPosition(adapter.getItemCount() - 1);
                                abc = abc + 1;
                            }
                        }
                        singlechatrecycler.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        progress321321231.setVisibility(View.INVISIBLE);
                        noDataFound.setVisibility(View.INVISIBLE);

                    } else {
                        noDataFound.setVisibility(View.VISIBLE);
                        Toast.makeText(MyApplication.getAppContext(), "No data found", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<GetChatMessagesModel> call, Throwable t) {
                progress321321231.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            uri = data.getData();
        }
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    //edit_profile_image.setImageURI(uri);
                    file = new File(uri.getPath());
                    String filePath = getRealPathFromURIPath(uri, ChatWithSingleUser.this);
                    file = new File(filePath);
                    if (file != null) {
                        hitApiForSendingImage(file);
                    }
                }

                break;
            case REQUEST_GALLERY_CODE:
                if (resultCode == RESULT_OK) {
                    // edit_profile_image.setImageURI(uri);
                    String filePath = getRealPathFromURIPath(uri, ChatWithSingleUser.this);
                    file = new File(uri.getPath());
                    file = new File(filePath);
                    if (file != null) {
                        hitApiForSendingImage(file);
                    }
                }
                break;
        }
    }

    private void hitApiForSendingImage(File file) {
        progress321321231.setVisibility(View.VISIBLE);
        RequestBody sender = RequestBody.create(MediaType.parse("text/plain"), senderId);
        final RequestBody reciver = RequestBody.create(MediaType.parse("text/plain"), recieverId);
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), "i");

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part userImage = MultipartBody.Part.createFormData("messageFile", file.getName(), requestFile);

        Retrofit retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<AddComentModel> call = apiListener.addFileMessagesApi(sender, reciver, type, userImage);
        call.enqueue(new Callback<AddComentModel>() {
            @Override
            public void onResponse(Call<AddComentModel> call, Response<AddComentModel> response) {
                progress321321231.setVisibility(View.INVISIBLE);
                if (response.isSuccessful()) {
                    AddComentModel data = response.body();
                    if (data.getCode().equalsIgnoreCase("201")) {
                        // Toast.makeText(ChatWithSingleUser.this, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                        hitSingleChatMessageApi();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddComentModel> call, Throwable t) {
                Toast.makeText(ChatWithSingleUser.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                progress321321231.setVisibility(View.INVISIBLE);
            }
        });

    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

}
