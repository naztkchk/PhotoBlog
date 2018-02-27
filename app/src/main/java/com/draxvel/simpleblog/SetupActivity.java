package com.draxvel.simpleblog;

import android.Manifest;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {


    private CircleImageView photo_iv;
    private EditText name_et;
    private Button save_btn;
    private ProgressBar mProgressBar;

    private Uri mainImageURI = null;
    private String userId;
    private boolean isProfileChanged;


    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private FirebaseFirestore mFirebaseFirestore;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mFirebaseFirestore = FirebaseFirestore.getInstance();

        //toolbar
        Toolbar setup_tv = findViewById(R.id.setup_tv);
        setSupportActionBar(setup_tv);
        getSupportActionBar().setTitle("Account Setup");


        initView();
        initListener();

        userId = mAuth.getCurrentUser().getUid();

        mProgressBar.setVisibility(View.VISIBLE);
        save_btn.setEnabled(false);

        loadStoredInfo();
    }

    private void initView() {
        name_et = findViewById(R.id.setup_name_et);
        photo_iv = findViewById(R.id.setup_profile_iv);
        mProgressBar = findViewById(R.id.setup_pb);
        save_btn = findViewById(R.id.setup_save_account_settings_btn);
    }

    private void initListener() {

        photo_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picPhoto();
            }
        });


        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInfo();
            }
        });
    }

    private void loadStoredInfo() {
        mFirebaseFirestore.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        String name = task.getResult().getString("name");
                        String image = task.getResult().getString("image");
                        mainImageURI = Uri.parse(image);
                        name_et.setText(name);

                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.placeholder(R.mipmap.default_image);

                        Glide
                                .with(SetupActivity.this)
                                .setDefaultRequestOptions(placeholderRequest)
                                .load(image)
                                .into(photo_iv);

                    }

                }else{
                    String e = task.getException().getMessage();
                    Toast.makeText(SetupActivity.this, "firestore retrieve  error: "+e, Toast.LENGTH_SHORT).show();
                }

                mProgressBar.setVisibility(View.INVISIBLE);
                save_btn.setEnabled(true);
            }
        });
    }

    private void picPhoto() {
        //Marshmello version
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            Toast.makeText(SetupActivity.this, "Marshmello sdk", Toast.LENGTH_SHORT).show();

            if(ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(SetupActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();

                ActivityCompat.requestPermissions(SetupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }else {
                imagePicker();
            }
        }else{

            imagePicker();
        }
    }

    private void saveInfo() {

        final String userName = name_et.getText().toString();

        mProgressBar.setVisibility(View.VISIBLE);

        if (isProfileChanged) {

            if (!TextUtils.isEmpty(userName) && mainImageURI != null) {

                userId = mAuth.getCurrentUser().getUid();

                StorageReference imagePath = mStorageRef.child("profile_images").child(userId + ".jpg");
                imagePath.putFile(mainImageURI)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                if (task.isSuccessful()) {

                                    storageFirestore(task, userName);

                                } else {
                                    String e = task.getException().getMessage();
                                    Toast.makeText(SetupActivity.this, "image error: " + e, Toast.LENGTH_SHORT).show();

                                    mProgressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
            } else
                Toast.makeText(SetupActivity.this, "empty info", Toast.LENGTH_SHORT).show();

        }else {
            storageFirestore(null, userName);
        }
    }

    private void storageFirestore(Task<UploadTask.TaskSnapshot> task, String userName) {

        Uri download_uri;

        if(task !=null){
            download_uri = task.getResult().getDownloadUrl();
        } else {
             download_uri = mainImageURI;
        }

        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", userName);
        userMap.put("image", download_uri.toString());

        mFirebaseFirestore.collection("Users").document(userId).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                if(task.isSuccessful()){

                    Toast.makeText(SetupActivity.this, "The user settings are updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SetupActivity.this, MainActivity.class));

                }else {

                    String e = task.getException().getMessage();
                    Toast.makeText(SetupActivity.this, "firestore error: "+e, Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.INVISIBLE);

                }
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void imagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(SetupActivity.this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageURI = result.getUri();

                photo_iv.setImageURI(mainImageURI);
                isProfileChanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
