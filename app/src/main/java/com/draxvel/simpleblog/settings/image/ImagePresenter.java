package com.draxvel.simpleblog.settings.image;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ImagePresenter{

    private Context context;
    private Activity activity;

    private IImageView iImageView;

    public ImagePresenter(Context context, Activity activity){
        this.context = context;
        this.activity = activity;

        this.iImageView = (IImageView) activity;
    }

    public void chooseImage() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if(ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                iImageView.showError("Permission Denied");

                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }else {
                imagePicker();
            }

        }else{
            imagePicker();
        }
    }

    private void imagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(activity);
    }
}
