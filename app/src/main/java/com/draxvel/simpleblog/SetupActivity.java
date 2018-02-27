package com.draxvel.simpleblog;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {


    CircleImageView setup_profile_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);


        Toolbar setup_tv = findViewById(R.id.setup_tv);
        setSupportActionBar(setup_tv);

        getSupportActionBar().setTitle("Account Setup");


        setup_profile_iv = findViewById(R.id.setup_profile_iv);

        setup_profile_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Marshmello version
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(SetupActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(SetupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }else {
                        Toast.makeText(SetupActivity.this, "You already have permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
