package com.draxvel.simpleblog.newpost;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.draxvel.simpleblog.main.MainActivity;
import com.draxvel.simpleblog.R;
import com.draxvel.simpleblog.imagePicker.ImagePresenter;
import com.theartofdev.edmodo.cropper.CropImage;

public class NewPostActivity extends AppCompatActivity implements INewPostView{

    private Toolbar new_post_tb;

    private ImageView new_post_iv;
    private EditText new_post_et;
    private Button new_post_btn;
    private ProgressBar mProgressBar;

    private ImagePresenter imagePresenter;
    private NewPostPresenter newPostPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        initView();
        initListener();
        initPresenter();
    }

    private void initView() {
        new_post_tb = findViewById(R.id.new_post_tb);
        setSupportActionBar(new_post_tb);
        getSupportActionBar().setTitle(R.string.add_new_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new_post_iv = findViewById(R.id.new_post_iv);
        new_post_et = findViewById(R.id.new_post_et);
        new_post_btn = findViewById(R.id.new_post_btn);
        mProgressBar = findViewById(R.id.new_post_pb);
    }

    private void initListener() {
        new_post_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePresenter.chooseImage();
            }
        });

        new_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String desc = new_post_et.getText().toString();
                newPostPresenter.publishPost(desc);
            }
        });
    }

    private void initPresenter() {
        newPostPresenter = new NewPostPresenter(this);
        imagePresenter = new ImagePresenter(getApplicationContext(), this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                newPostPresenter.setMainImageURI(result.getUri());
                new_post_iv.setImageURI(result.getUri());

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                showError("Error in request code: \"+ result.getError().getMessage().toString()");
            }
        }
    }

    @Override
    public void showError(String e) {
        Toast.makeText(NewPostActivity.this, e, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setVisibleProgressBar(boolean s) {
        if(s){
            mProgressBar.setVisibility(View.VISIBLE);
        }else{
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showMainActivity() {
        startActivity(new Intent(NewPostActivity.this, MainActivity.class));
        finish();
    }
}
