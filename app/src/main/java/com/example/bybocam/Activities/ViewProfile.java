package com.example.bybocam.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bybocam.BuildConfig;
import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Model.EditProfileModel;
import com.example.bybocam.Model.ViewProfileModel;
import com.example.bybocam.R;
import com.example.bybocam.URL.RetrofitClientIntance;
import com.example.bybocam.Utils.Common;
import com.example.bybocam.Utils.SessionManager;
import com.hbb20.CountryCodePicker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static androidx.core.content.FileProvider.getUriForFile;


public class ViewProfile extends AppCompatActivity {
    SessionManager session;
    String email;
    ImageView edit_profile_image, edit_photo, imageBackViewProfile;
    EditText edit_profile_user_name, edit_profile_user_phone, edit_profile_user_dateofbirth, edit_profile_user_email, fisrtname, lastname;
    Button edit_profile_submit_btn;
    String username, phone1, dateofbirth, email2;
    TextView text_flag;
    Button  bt_verfy;
    Retrofit retrofit;
    Dialog alertDialog;
    String userID;
    Calendar myCalendar;
    ProgressBar progress;
    private Uri uri;
    File file=null;
    private  final int REQUEST_GALLERY_CODE = 200;
    private int REQUEST_STORAGE_PERMISSION_RESULT = 1;
    private String firstName, lastName;

    private  String namecode="IN +91";
    private String currentPhotoPath;
    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private static final int REQUEST_GALLERY_IMAGE = 1;
    private boolean lockAspectRatio = false, setBitmapMaxWidthHeight = false;
    private int ASPECT_RATIO_X = 16, ASPECT_RATIO_Y = 9, bitmapMaxWidth = 800, bitmapMaxHeight = 800;
    private int IMAGE_COMPRESSION = 85;
    byte[] byteArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        intialUi();
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);
        userID = user.get(SessionManager.USER_ID);


        if (Common.isNetworkConnected(ViewProfile.this)) {
            viewDataHitApi();
        } else {
            progress.setVisibility(View.INVISIBLE);
            Toast.makeText(ViewProfile.this, "Check your network", Toast.LENGTH_SHORT).show();
        }

        bt_verfy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ViewProfile.this,PhoneVerificationActivity.class);
                startActivityForResult(intent,1);
            }
        });



//        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
//            @Override
//            public void onCountrySelected() {
//
//                String  code = ccp.getSelectedCountryCodeWithPlus();
//                String name=ccp.getSelectedCountryNameCode();
//
//                 namecode=name+" "+code;
//               //  Log.e("namecode",namecode);
//
//             //   Toast.makeText(ViewProfile.this, name+" "+code, Toast.LENGTH_SHORT).show();
//            }
//        });


        edit_profile_user_email.setText(email);
        myCalendar = Calendar.getInstance();
        edit_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setImage();
            }
        });
//        edit_photo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (ContextCompat.checkSelfPermission(ViewProfile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
//                            PackageManager.PERMISSION_GRANTED) {
//
//                        alertDialog = new Dialog(ViewProfile.this);
//                        LayoutInflater inflater = ViewProfile.this.getLayoutInflater();
//                        View add_menu_layout = inflater.inflate(R.layout.custom_layout_dialog, null);
//                        dialog_title = add_menu_layout.findViewById(R.id.dialog_title);
//                        dialog_message = add_menu_layout.findViewById(R.id.dialog_message);
//                        dismis = add_menu_layout.findViewById(R.id.discard_btn);
//                        clear = add_menu_layout.findViewById(R.id.cancle_btn);
//                        dialog_title.setText("Choose Image");
//                        dialog_message.setText("Select where you want to pick image");
//                        clear.setText("Gallery");
//                        dismis.setText("Camera");
//                        dismis.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                startActivityForResult(takePicture, 0);
//                                alertDialog.dismiss();
//                            }
//                        });
//
//                        clear.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
//                                openGalleryIntent.setType("image/*");
//                                startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE);
//                                alertDialog.dismiss();
//                            }
//                        });
//
//                        alertDialog.setContentView(add_menu_layout);
//                        alertDialog.show();
//                    } else {
//                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION_RESULT);
//                    }
//                } else {
//                    alertDialog = new Dialog(ViewProfile.this);
//                    LayoutInflater inflater = ViewProfile.this.getLayoutInflater();
//                    View add_menu_layout = inflater.inflate(R.layout.custom_layout_dialog, null);
//                    dialog_title = add_menu_layout.findViewById(R.id.dialog_title);
//                    dialog_message = add_menu_layout.findViewById(R.id.dialog_message);
//                    dismis = add_menu_layout.findViewById(R.id.discard_btn);
//                    clear = add_menu_layout.findViewById(R.id.cancle_btn);
//                    dialog_title.setText("Choose Image");
//                    dialog_message.setText("Select where you want to pick image");
//                    clear.setText("Gallery");
//                    dismis.setText("Camera");
//                    dismis.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            takePicture.putExtra(MediaStore.EXTRA_OUTPUT,uri);
//                            startActivityForResult(takePicture, 0);
//                            alertDialog.dismiss();
//                        }
//                    });
//
//                    clear.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
//                            openGalleryIntent.setType("image/*");
//                            startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE);
//                            alertDialog.dismiss();
//                        }
//                    });
//
//                    alertDialog.setContentView(add_menu_layout);
//                    alertDialog.show();
//
//                }
//            }
//        });


        edit_profile_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
        imageBackViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        edit_profile_user_dateofbirth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog=new DatePickerDialog(ViewProfile.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
    }

    private void getData() {
        if (checkConditions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(ViewProfile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {

                    if (Common.isNetworkConnected(ViewProfile.this)) {
                        progress.setVisibility(View.VISIBLE);
                        hitApi();
                    } else {
                        progress.setVisibility(View.INVISIBLE);
                        Toast.makeText(ViewProfile.this, "Check your network", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION_RESULT);
                }
            } else {
                if (Common.isNetworkConnected(ViewProfile.this)) {
                    progress.setVisibility(View.VISIBLE);
                    hitApi();
                } else {
                    progress.setVisibility(View.INVISIBLE);
                    Toast.makeText(ViewProfile.this, "Check your network", Toast.LENGTH_SHORT).show();
                }
            }
        }
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
                    Toast.makeText(ViewProfile.this, "Permissions are  required to run app correctly", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edit_profile_user_dateofbirth.setText(sdf.format(myCalendar.getTime()));
    }

    private void viewDataHitApi() {
        retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<ViewProfileModel> call = apiListener.viewProfileAPI(userID);
        call.enqueue(new Callback<ViewProfileModel>() {
            @Override
            public void onResponse(Call<ViewProfileModel> call, Response<ViewProfileModel> response) {
                if (response.isSuccessful()) {
                    ViewProfileModel data = response.body();
                    if (data != null) {
                        if (data.getCode().equalsIgnoreCase("201")) {
                            if (data.getData().get(0).getUserName() != null) {
                                edit_profile_user_name.setText(data.getData().get(0).getUserName());
                            }

                            if (data.getData().get(0).getFirstName() != null) {
                                fisrtname.setText(String.valueOf(data.getData().get(0).getFirstName()));
                            }
                            if (data.getData().get(0).getLastName() != null) {
                                lastname.setText(String.valueOf(data.getData().get(0).getLastName()));
                            }
                            if (data.getData().get(0).getPhone() != null) {
                                Object phone = data.getData().get(0).getPhone();
                                String ph = String.valueOf(phone);
                                edit_profile_user_phone.setText(ph);
                            }
                            if (data.getData().get(0).getDateOfBirth() != null) {
                                Object date = data.getData().get(0).getDateOfBirth();
                                String dateofB = String.valueOf(date);
                                edit_profile_user_dateofbirth.setText(dateofB);
                            }
                            if (data.getData().get(0).getUserImage() != null) {
                                String imageUri = Common.image_base_url + data.getData().get(0).getUserImage();
                                Uri uri = Uri.parse(imageUri);
                                Glide.with(getApplicationContext()).load(uri).into(edit_profile_image);
                            }

                            if (data.getData().get(0).getCountryCode() != null) {
                               String  countrycode = data.getData().get(0).getCountryCode();
                                text_flag.setText(countrycode);
                                //    Log.e("newcode",newcode);
                            }

                            progress.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ViewProfileModel> call, Throwable t) {
                Toast.makeText(ViewProfile.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void hitApi() {

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), userID);
        RequestBody firstName1 = RequestBody.create(MediaType.parse("text/plain"), firstName);
        RequestBody lastName1 = RequestBody.create(MediaType.parse("text/plain"), lastName);
        RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), phone1);
        RequestBody countryCode = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody dateOfBirth = RequestBody.create(MediaType.parse("text/plain"), dateofbirth);
        RequestBody userName = RequestBody.create(MediaType.parse("text/plain"), username);

        if (file != null) {
            File file1 = new File(String.valueOf(file));
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
            MultipartBody.Part userImage = MultipartBody.Part.createFormData("userImage", file1.getName(), requestFile);

            retrofit = RetrofitClientIntance.retroInit();
            final ApiListener apiListener = retrofit.create(ApiListener.class);
            Call<EditProfileModel> call = apiListener.uploadFile(userId, firstName1, lastName1, countryCode,phone, dateOfBirth, userName, userImage);
            call.enqueue(new Callback<EditProfileModel>() {
                @Override
                public void onResponse(Call<EditProfileModel> call, Response<EditProfileModel> response) {
                    if (response.isSuccessful()) {
                        EditProfileModel data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {
                                Toast.makeText(ViewProfile.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ViewProfile.this, Home.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(ViewProfile.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ViewProfile.this, "Data null", Toast.LENGTH_SHORT).show();


                        }
                    }
                    progress.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<EditProfileModel> call, Throwable t) {
                    Toast.makeText(ViewProfile.this, "Try again", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.INVISIBLE);

                }
            });

        } else {
            retrofit = RetrofitClientIntance.retroInit();
            final ApiListener apiListener = retrofit.create(ApiListener.class);
            Call<EditProfileModel> call = apiListener.editProfileClassApi(userID, firstName, lastName,namecode, phone1, dateofbirth, username);
            call.enqueue(new Callback<EditProfileModel>() {
                @Override
                public void onResponse(Call<EditProfileModel> call, Response<EditProfileModel> response) {
                    if (response.isSuccessful()) {
                        EditProfileModel data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {
                                Toast.makeText(ViewProfile.this, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ViewProfile.this, Home.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(ViewProfile.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(ViewProfile.this, "try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                    progress.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<EditProfileModel> call, Throwable t) {
                    Toast.makeText(ViewProfile.this, "Try again", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.INVISIBLE);

                }
            });

        }

    }

    private void intialUi() {
        text_flag=findViewById(R.id.text_flag);
        bt_verfy=findViewById(R.id.bt_verfy);
        session = new SessionManager(getApplicationContext());
        edit_profile_user_name = findViewById(R.id.edit_profile_user_name);
        edit_profile_user_phone = findViewById(R.id.edit_profile_user_phone);
        edit_profile_user_dateofbirth = findViewById(R.id.edit_profile_user_dateofbirth);
        edit_profile_user_email = findViewById(R.id.edit_profile_user_email);
        edit_profile_image = findViewById(R.id.edit_profile_image);
        edit_photo = findViewById(R.id.edit_photo);
        edit_profile_submit_btn = findViewById(R.id.edit_profile_submit_btn);
        imageBackViewProfile = findViewById(R.id.imageBackViewProfile);
        progress = findViewById(R.id.progress);
        fisrtname = findViewById(R.id.edit_profile_first_name);
        lastname = findViewById(R.id.edit_profile_last_name);


    }

    private boolean checkConditions() {
        progress.setVisibility(View.VISIBLE);
        firstName = fisrtname.getText().toString().trim();
        lastName = lastname.getText().toString().trim();
        username = edit_profile_user_name.getText().toString().trim();
        phone1 = edit_profile_user_phone.getText().toString().trim();
        dateofbirth = edit_profile_user_dateofbirth.getText().toString().trim();
        email2 = edit_profile_user_email.getText().toString().trim();
        if (TextUtils.isEmpty(username) && (TextUtils.isEmpty(phone1)) && edit_profile_user_dateofbirth.getText().toString().isEmpty() && (TextUtils.isEmpty(email2)) && (fisrtname.getText().toString().isEmpty()) && (lastname.getText().toString().isEmpty())) {

            Toast.makeText(ViewProfile.this, "Enter all fields", Toast.LENGTH_SHORT).show();
            progress.setVisibility(View.INVISIBLE);
            return false;
        }

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(ViewProfile.this, "Check your network", Toast.LENGTH_SHORT).show();
            progress.setVisibility(View.INVISIBLE);
            return false;
        }
        if (fisrtname.getText().toString().isEmpty()) {
            Toast.makeText(ViewProfile.this, "Enter first name", Toast.LENGTH_SHORT).show();
            progress.setVisibility(View.INVISIBLE);
            return false;
        }

        if (lastname.getText().toString().isEmpty()) {
            Toast.makeText(ViewProfile.this, "Enter last name", Toast.LENGTH_SHORT).show();
            progress.setVisibility(View.INVISIBLE);
            return false;
        }
        if (TextUtils.isEmpty(phone1)) {
            Toast.makeText(ViewProfile.this, "Enter phone number", Toast.LENGTH_SHORT).show();
            progress.setVisibility(View.INVISIBLE);
            return false;
        }
        if (edit_profile_user_dateofbirth.getText().toString().isEmpty()) {
            Toast.makeText(ViewProfile.this, "Enter date of birth", Toast.LENGTH_SHORT).show();
            progress.setVisibility(View.INVISIBLE);
            return false;
        }
        if (TextUtils.isEmpty(email2)) {
            Toast.makeText(ViewProfile.this, "Enter email", Toast.LENGTH_SHORT).show();
            progress.setVisibility(View.INVISIBLE);
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (data!=null){
                String verfynum=data.getStringExtra("verfiednumber");
                String code=data.getStringExtra("code");
                namecode=code+" "+verfynum;
                if (verfynum!=null) {
                    edit_profile_user_phone.setText(verfynum);
                }
//                if (code!=null){
//                    String newcodee=code.substring(3);
//                //    ccp.setCountryForPhoneCode(Integer.parseInt(newcodee));
//                }
            }
        }
        switch (requestCode) {

            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    try {
                        //  cropImage(getCacheImagePath(fileName));
                        Uri uri = Uri.parse(currentPhotoPath);
                        Log.e("currentPhotoPath", "" + currentPhotoPath);
                        cropImage(uri, uri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //  Toast.makeText(getContext(), fileName.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    setResultCancelled();
                }
                break;
            case REQUEST_GALLERY_IMAGE:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri sourceUri = data.getData();
                        File file = getImageFile();
                        Log.e("Source", "" + file.getAbsolutePath());
                        Uri destinationUri = Uri.fromFile(file);
                        cropImage(sourceUri, destinationUri);
                    } catch (Exception e) {
                        Log.e("ss", "Please select another image");
                    }

                } else {
                    setResultCancelled();
                }
                break;
            case UCrop.REQUEST_CROP:
                if (resultCode == RESULT_OK) {
                    handleUCropResult(data);
                    if (file != null) {
                        Glide.with(this).load(file).diskCacheStrategy(DiskCacheStrategy.ALL).into(edit_profile_image);
                        String name = System.currentTimeMillis() + "_video.jpeg";
                    }
                } else {
                    setResultCancelled();
                }
                break;
            case UCrop.RESULT_ERROR:
                final Throwable cropError = UCrop.getError(data);
                Log.e("error", "Crop error: " + cropError);
                setResultCancelled();
                break;
            default:
                setResultCancelled();
        }

    }



    private void setImage() {
        final CharSequence[] option = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewProfile.this);
        builder.setTitle("Add Photo !");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (option[i].equals("Take Photo")) {
                    takeCameraImage();
                } else if (option[i].equals("Choose from Gallery")) {
                    chooseImageFromGallery();
                } else {
                    dialog.cancel();
                }
            }
        });
        builder.show();
    }

    private void chooseImageFromGallery() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            Intent pictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            pictureIntent.setType("image/*");
                            pictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                String[] mimeTypes = new String[]{"image/jpeg", "image/png"};
                                pictureIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                            }
                            startActivityForResult(Intent.createChooser(pictureIntent, "Select Picture"), REQUEST_GALLERY_IMAGE);

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }

    private void takeCameraImage() {
        Dexter.withActivity(ViewProfile.this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File file;
                            try {
                                file = getImageFile(); // 1
                            } catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }
                            Uri uri;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) // 2
                                uri = getUriForFile(ViewProfile.this, BuildConfig.APPLICATION_ID.concat(".provider"), file);
                            else
                                uri = Uri.fromFile(file); // 3
                            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // 4
                            startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private File getImageFile () throws IOException {
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                ), "Camera"
        );
        System.out.println(storageDir.getAbsolutePath());
        if (storageDir.exists())
            System.out.println("File exists");
        else
            System.out.println("File not exists");
        File file = File.createTempFile(
                imageFileName, ".jpg", storageDir
        );
        currentPhotoPath = "file:" + file.getAbsolutePath();
        return file;
    }


    private static int getImageRotation ( final File imageFile){

        ExifInterface exif = null;
        int exifRotation = 0;

        try {
            exif = new ExifInterface(imageFile.getPath());
            exifRotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (exif == null)
            return 0;
        else
            return exifToDegrees(exifRotation);
    }

    private static int exifToDegrees ( int rotation){
        if (rotation == ExifInterface.ORIENTATION_ROTATE_90)
            return 90;
        else if (rotation == ExifInterface.ORIENTATION_ROTATE_180)
            return 180;
        else if (rotation == ExifInterface.ORIENTATION_ROTATE_270)
            return 270;

        return 0;
    }

    private static Bitmap getBitmapRotatedByDegree (Bitmap bitmap, int rotationDegree){
        Matrix matrix = new Matrix();
        matrix.preRotate(rotationDegree);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private void cropImage(Uri sourceUri, Uri destinationUri) {
//        Uri destinationUri = Uri.fromFile(new File(getActivity().getCacheDir(), queryName(getActivity().getContentResolver(), sourceUri)));
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(IMAGE_COMPRESSION);
        // options.setMaxBitmapSize(10000);


//        // applying UI theme
        options.setToolbarColor(ContextCompat.getColor(ViewProfile.this, R.color.yellow));
        options.setStatusBarColor(ContextCompat.getColor(ViewProfile.this, R.color.yellow));
        options.setActiveWidgetColor(ContextCompat.getColor(ViewProfile.this, R.color.yellow));

//
        if (lockAspectRatio)
            options.withAspectRatio(ASPECT_RATIO_X, ASPECT_RATIO_Y);
//
        if (setBitmapMaxWidthHeight)
            options.withMaxResultSize(bitmapMaxWidth, bitmapMaxHeight);
//
        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .start(ViewProfile.this);
    }

    private void handleUCropResult(Intent data) {
        if (data == null) {
            setResultCancelled();
            return;
        }
        final Uri resultUri = UCrop.getOutput(data);
        setResultOk(resultUri);
    }


    private void setResultCancelled () {
        Toast.makeText(ViewProfile.this, "Cancelled", Toast.LENGTH_SHORT).show();
    }

    private void setResultOk(Uri imagePath) {


        try {
            String path = String.valueOf(imagePath);
            file = new File(new URI(path));
            byteArray = getStreamByteFromImage(file);


        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    public static byte[] getStreamByteFromImage ( final File imageFile){

        Bitmap photoBitmap = BitmapFactory.decodeFile(imageFile.getPath());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        int imageRotation = getImageRotation(imageFile);

        if (imageRotation != 0)
            photoBitmap = getBitmapRotatedByDegree(photoBitmap, imageRotation);

        photoBitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream);

        return stream.toByteArray();
    }



}