package com.draxvel.simpleblog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class NewPostActivity extends AppCompatActivity {

    private Toolbar new_post_tb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        new_post_tb = findViewById(R.id.new_post_tb);

        setSupportActionBar(new_post_tb);

        getSupportActionBar().setTitle(R.string.add_new_post);
    }
}
