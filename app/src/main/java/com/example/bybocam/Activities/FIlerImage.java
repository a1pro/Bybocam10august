package com.example.bybocam.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.exifinterface.media.ExifInterface;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bybocam.Adapter.ViewPagerAdapter2;
import com.example.bybocam.Editor.EditImageActivity;
import com.example.bybocam.Fragments.FilterFragment;
import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Interface.EditImageFragmentListener;
import com.example.bybocam.Interface.FilterListFragmentListener;
import com.example.bybocam.Model.AddComentModel;
import com.example.bybocam.R;
import com.example.bybocam.URL.RetrofitClientIntance;
import com.example.bybocam.Utils.BitmapUtils;
import com.example.bybocam.Utils.Common;
import com.example.bybocam.Utils.SessionManager;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FIlerImage extends AppCompatActivity implements FilterListFragmentListener, EditImageFragmentListener {

    public static final int PERMISSION_PICK_IMAGE = 101;
    ImageView img_preview, backImageFilter;
    ViewPager viewPager;
    CoordinatorLayout coordinatorLayout;
    public static Bitmap originalBitmap, filterBitmap, finalBitmap, originalBitmap1;
    FilterFragment filterFragment;
    int brightnessFinal = 0;
    float saturationFinal = 1.0f;
    float constrantFinal = 1.0f;
    SessionManager session;
    String userID;
    Retrofit retrofit;
    ProgressBar progress;
    TextView nextText;

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    Uri imageUri;
    private TextView dialog_title, dialog_message;
    private Button dismis, clear;
    private File fil1;
    private String cameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filer_image);

        Intent callingActivityIntent = this.getIntent();
        if (callingActivityIntent != null) {
            imageUri = callingActivityIntent.getData();
            cameraId = callingActivityIntent.getStringExtra("cameraId");
            if (imageUri != null) {
            } else {
                Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "callingActivityIntent is null", Toast.LENGTH_SHORT).show();
        }

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        userID = user.get(SessionManager.USER_ID);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //view

        img_preview = findViewById(R.id.image_preview);
        viewPager = findViewById(R.id.viewPager2);
        coordinatorLayout = findViewById(R.id.coordinator);
        backImageFilter = findViewById(R.id.backImageFilter);
        progress = findViewById(R.id.progress);
        nextText = findViewById(R.id.nextText);


        loadImage();

        setupViewPager(viewPager);


        backImageFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        nextText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImageToGallery();
            }
        });
    }

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap) {
        Matrix rotateRight = new Matrix();
        rotateRight.preRotate(90);

        if (android.os.Build.VERSION.SDK_INT > 13) {
            float[] mirrorY = {-1, 0, 0, 0, 1, 0, 0, 0, 1};
            rotateRight = new Matrix();
            Matrix matrixMirrorY = new Matrix();
            matrixMirrorY.setValues(mirrorY);

            rotateRight.postConcat(matrixMirrorY);

            rotateRight.preRotate(270);
        }

        final Bitmap rImg = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), rotateRight, true);
        return rImg;
    }


    private Bitmap rotateBitmap(Bitmap bitmap) {

        Matrix rotateRight = new Matrix();
        rotateRight.preRotate(90);

        if (android.os.Build.VERSION.SDK_INT > 13) {
            float[] mirrorY = {-1, 0, 0, 0, 1, 0, 0, 0, 1};
            rotateRight = new Matrix();
            Matrix matrixMirrorY = new Matrix();
            matrixMirrorY.setValues(mirrorY);

            rotateRight.postConcat(matrixMirrorY);

            rotateRight.preRotate(270);
        }

        final Bitmap rImg = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), rotateRight, true);
        return rImg;
    }

    private void loadImage() {
        try {
            if (cameraId != null) {
                if (cameraId.matches("1")) {
                    originalBitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    originalBitmap = rotateBitmap(originalBitmap1);

                } else {
                    originalBitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    originalBitmap = modifyOrientation(originalBitmap1, imageUri.getPath());

                }
            } else {
                originalBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        filterBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        img_preview.setImageBitmap(originalBitmap);
    }


    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter2 adapter = new ViewPagerAdapter2(getSupportFragmentManager());

        // adding filter list fragment
        filterFragment = new FilterFragment();
        filterFragment.setListener(this);

        adapter.addFragments(filterFragment, "");


        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBrghtnessChanges(int brightness) {
        brightnessFinal = brightness;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightness));
        img_preview.setImageBitmap(myFilter.processFilter(filterBitmap.copy(Bitmap.Config.ARGB_8888, true)));

    }

    @Override
    public void onSaturationChanges(float saturation) {
        saturationFinal = saturation;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new SaturationSubfilter(saturation));
        img_preview.setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888, true)));
    }

    @Override
    public void onConstrantsChanges(float contrast) {
        constrantFinal = contrast;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new ContrastSubFilter(contrast));
        img_preview.setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888, true)));
    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditCompleted() {
        final Bitmap bitmap = filterBitmap.copy(Bitmap.Config.ARGB_8888, true);

        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightnessFinal));
        myFilter.addSubFilter(new ContrastSubFilter(constrantFinal));
        myFilter.addSubFilter(new SaturationSubfilter(saturationFinal));
        finalBitmap = myFilter.processFilter(bitmap);
    }

    @Override
    public void onFIlterSelected(Filter filter) {
        //  resetControl();

        filterBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        img_preview.setImageBitmap(filter.processFilter(filterBitmap));
        finalBitmap = filterBitmap.copy(Bitmap.Config.ARGB_8888, true);

    }

//    private void resetControl() {
//        if (editImageFragment != null) {
//            editImageFragment.resetControls();
//        }
//        brightnessFinal = 0;
//        saturationFinal = 1.0f;
//        constrantFinal = 1.0f;
//    }


    private void hitApi() {
        final Dialog alertDialog = new Dialog(FIlerImage.this);
        LayoutInflater inflater = getLayoutInflater();
        final View add_menu_layout = inflater.inflate(R.layout.custom_layout_dialog, null);

        dialog_title = add_menu_layout.findViewById(R.id.dialog_title);
        dialog_message = add_menu_layout.findViewById(R.id.dialog_message);
        dismis = add_menu_layout.findViewById(R.id.discard_btn);
        clear = add_menu_layout.findViewById(R.id.cancle_btn);
        dialog_title.setText(R.string.uploadImage);
        dialog_message.setText("Upload selected Image in your profile");
        dismis.setText("Cancel");
        clear.setText("Upload Image");
        dismis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
                Toast.makeText(FIlerImage.this, "Uploading....", Toast.LENGTH_SHORT).show();
                uploadImage();
            }
        });

        alertDialog.setContentView(add_menu_layout);

        alertDialog.show();
    }

    private void uploadImage() {
        try {
            progress.setVisibility(View.VISIBLE);
            RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), userID);
            RequestBody postType = RequestBody.create(MediaType.parse("text/plain"), "0");
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fil1);
            MultipartBody.Part userImage = MultipartBody.Part.createFormData("postVideoImg", fil1.getName(), requestFile);

            retrofit = RetrofitClientIntance.retroInit();
            final ApiListener apiListener = retrofit.create(ApiListener.class);
            Call<AddComentModel> call = apiListener.addPostImage(userId, postType, userImage);
            call.enqueue(new Callback<AddComentModel>() {
                @Override
                public void onResponse(Call<AddComentModel> call, Response<AddComentModel> response) {
                    AddComentModel data = response.body();
                    if (data.getCode().equalsIgnoreCase("201")) {
                        Toast.makeText(FIlerImage.this, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(FIlerImage.this, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    progress.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<AddComentModel> call, Throwable t) {
                    Toast.makeText(FIlerImage.this, "Failed to uplaod image", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.INVISIBLE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PERMISSION_PICK_IMAGE) {
            Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this, data.getData(), 800, 800);

            // clear bitmap memory
            originalBitmap.recycle();
            finalBitmap.recycle();
            filterBitmap.recycle();

            originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            filterBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
            finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
            img_preview.setImageBitmap(originalBitmap);
            bitmap.recycle();

            // render selected image thumbnails
            filterFragment.displayThumbnails(originalBitmap);
        }

    }

    private void saveImageToGallery() {

        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            String name = System.currentTimeMillis() + "_profile.jpeg";
                            final String path = BitmapUtils.insertImage(getContentResolver(), finalBitmap, name, null);
                            if (!TextUtils.isEmpty(path)) {
                                fil1 = new File(FIlerImage.this.getCacheDir(), name);
                                try {
                                    fil1.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

//Convert bitmap to byte array
                                Bitmap bitmap = finalBitmap;
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 90 /*ignored for PNG*/, bos);
                                byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
                                FileOutputStream fos = null;
                                try {
                                    fos = new FileOutputStream(fil1);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    fos.write(bitmapdata);
                                    fos.flush();
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (Common.isNetworkConnected(FIlerImage.this)) {
                                    Intent intent = new Intent(FIlerImage.this, EditImageActivity.class);
                                    String nameorimagesa = fil1.getAbsolutePath();
                                    Uri uri = Uri.parse(nameorimagesa);
                                    intent.setData(uri);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(FIlerImage.this, "Check your network", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Snackbar snackbar = Snackbar
                                        .make(coordinatorLayout, "Unable to save image!", Snackbar.LENGTH_LONG);

                                snackbar.show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Permissions are not granted!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }
}
