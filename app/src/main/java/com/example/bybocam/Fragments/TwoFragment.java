package com.example.bybocam.Fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.bybocam.Activities.FIlerImage;
import com.example.bybocam.Activities.Gallary;
import com.example.bybocam.R;
import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitEventCallback;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraView;
import java.io.File;
import java.io.FileOutputStream;


public class TwoFragment extends Fragment {
    private CameraView camera;
    private ImageView record_image;
    private final static int READ_EXTERNAL_STORAGE_PERMISSION_RESULT = 0;
    private ImageView rotate_camera,flash_camera;
    //Handler
    File image = null;
    boolean is_flash_on=false;


    public TwoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        camera = view.findViewById(R.id.camera);
        rotate_camera=view.findViewById(R.id.rotate_camera);
        flash_camera=view.findViewById(R.id.flash_camera);
        chechReadExternalstorage();

        record_image = view.findViewById(R.id.record_image);
        record_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gallary.nextTxt.setVisibility(View.VISIBLE);
                camera.captureImage(new CameraKitEventCallback<CameraKitImage>() {
                    @Override
                    public void callback(CameraKitImage cameraKitImage) {
                        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "Bybocams");
                        if (!folder.exists())
                            folder.mkdirs();
                        image = new File(folder.getAbsoluteFile() + File.separator + "photo.jpg");
                        if (image.exists())
                            image.delete();

                        FileOutputStream outputStream = null;
                        try {
                            outputStream = new FileOutputStream(image.getPath(), false);
                            outputStream.write(cameraKitImage.getJpeg());
                            outputStream.flush();
                            outputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("exception", "==" + e.getMessage());
                        }
                    }
                });
            }
        });

        rotate_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RotateCamera();
            }
        });

        flash_camera.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                if(is_flash_on){
                    is_flash_on=false;
                    camera.setFlash(0);
                    flash_camera.setImageDrawable(getResources().getDrawable(R.drawable.ic_flash_off_black_24dp));

                }else {
                    is_flash_on=true;
                    camera.setFlash(CameraKit.Constants.FLASH_TORCH);
                    flash_camera.setImageDrawable(getResources().getDrawable(R.drawable.ic_flash_off_black_24dp));
                }
            }
        });


        Gallary.nextTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), FIlerImage.class);
                intent.setData(Uri.fromFile(image));
                startActivity(intent);
            }
        });


        return view;
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        camera.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }


    @Override
    public void onStart() {
        super.onStart();
        camera.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        camera.start();
    }

    @Override
    public void onPause() {
        camera.stop();
        super.onPause();
    }

    @Override
    public void onStop() {
        camera.stop();
        super.onStop();
    }

    private void chechReadExternalstorage() {


        if (Build.VERSION.SDK_INT >= 23) {
            if ((getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) || (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)) {
                //   Log.v(TAG,"Permission is granted1");
            } else {

                //   Log.v(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_RESULT);
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //  Log.v(TAG,"Permission is granted1");
            //  return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_PERMISSION_RESULT:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }else {
                    Toast.makeText(getContext(), "Please allow permission", Toast.LENGTH_SHORT).show();
                    //    finish();
                }
                break;
            default:
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void RotateCamera(){
        camera.toggleFacing();
    }





}