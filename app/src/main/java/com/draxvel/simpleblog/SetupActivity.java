package com.draxvel.simpleblog;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {


    private CircleImageView setup_profile_iv;
    private Uri mainImageURI = null;
    private EditText name_et;
    private Button save_account_settings_btn;

    private ProgressBar mProgressBar;

    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        Toolbar setup_tv = findViewById(R.id.setup_tv);
        setSupportActionBar(setup_tv);

        getSupportActionBar().setTitle("Account Setup");

        mProgressBar = findViewById(R.id.setup_pb);
        setup_profile_iv = findViewById(R.id.setup_profile_iv);
        name_et = findViewById(R.id.setup_name_et);
        save_account_settings_btn = findViewById(R.id.setup_save_account_settings_btn);

        setup_profile_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Marshmello version
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    Toast.makeText(SetupActivity.this, "Marshmello sdk", Toast.LENGTH_SHORT).show();

                    if(ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED){

                        Toast.makeText(SetupActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();

                        ActivityCompat.requestPermissions(SetupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1,1)
                                .start(SetupActivity.this);
                    }
                }else{

                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1,1)
                            .start(SetupActivity.this);
                }


            }
        });

        save_account_settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = name_et.getText().toString();

                if(!TextUtils.isEmpty(userName) && mainImageURI != null){

                    mProgressBar.setVisibility(View.VISIBLE);
                    String userId = mAuth.getCurrentUser().getUid();

                    StorageReference imagePath = mStorageRef.child("profile_images").child(userId+".jpg");
                    imagePath.putFile(mainImageURI)
                             .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                 @Override
                                 public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                     if(task.isSuccessful()){

                                         Uri download_uri = task.getResult().getDownloadUrl();
                                         Toast.makeText(SetupActivity.this, "The image is uploaded", Toast.LENGTH_SHORT).show();

                                     }else {
                                         String e = task.getException().getMessage();
                                         Toast.makeText(SetupActivity.this, e, Toast.LENGTH_SHORT).show();
                                     }


                                     mProgressBar.setVisibility(View.INVISIBLE);
                                 }
                             });
                } else  Toast.makeText(SetupActivity.this, "empty info", Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageURI = result.getUri();

                setup_profile_iv.setImageURI(mainImageURI);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
