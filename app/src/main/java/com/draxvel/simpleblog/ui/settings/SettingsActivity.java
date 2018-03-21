package com.draxvel.simpleblog.ui.settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.draxvel.simpleblog.ui.main.MainActivity;
import com.draxvel.simpleblog.R;
import com.draxvel.simpleblog.util.imagePicker.IImageView;
import com.draxvel.simpleblog.util.imagePicker.ImagePresenter;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity implements ISettingsView, IImageView{

    private CircleImageView photo_iv;
    private EditText name_et;
    private Button save_btn;
    private ProgressBar mProgressBar;

    private SettingsPresenter settingsPresenter;
    private ImagePresenter imagePresenter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();
        initListener();
        initPresenter();

        mProgressBar.setVisibility(View.VISIBLE);
        save_btn.setEnabled(false);
    }

    private void initView() {
        //toolbar
        Toolbar setup_tv = findViewById(R.id.setup_tv);
        setSupportActionBar(setup_tv);
        getSupportActionBar().setTitle("Account Setup");

        name_et = findViewById(R.id.setup_name_et);
        photo_iv = findViewById(R.id.setup_profile_iv);
        mProgressBar = findViewById(R.id.setup_pb);
        mProgressBar.getProgressDrawable().setColorFilter(
               getResources().getColor(R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.SRC_IN);
        save_btn = findViewById(R.id.setup_save_account_settings_btn);
    }

    private void initListener() {

        photo_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePresenter.chooseImage();
            }
        });


        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = name_et.getText().toString();
                settingsPresenter.updateInfo(name);
            }
        });
    }

    private void initPresenter() {
        settingsPresenter = new SettingsPresenter(this);
        imagePresenter = new ImagePresenter(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                settingsPresenter.setMainImageURI(result.getUri());
                settingsPresenter.setProfileChanged(true);
                photo_iv.setImageURI(result.getUri());

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                showError("Error in request code: \"+ result.getError().getMessage().toString()");
            }
        }
    }

    @Override
    public void setName(String name) {
        name_et.setText(name);
    }

    @Override
    public void setImage(String image) {
        RequestOptions placeholderRequest = new RequestOptions();
        placeholderRequest.placeholder(R.mipmap.default_image);

        Glide
                .with(SettingsActivity.this)
                .setDefaultRequestOptions(placeholderRequest)
                .load(image)
                .into(photo_iv);
    }

    @Override
    public void setEnabledSaveBtn(boolean b) {
        save_btn.setEnabled(b);
    }

    @Override
    public void showError(String e) {
        Toast.makeText(SettingsActivity.this, e, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setVisibleProgressBar(boolean s) {

        Log.i("sss", "setVisibleProgressBar");
        if(s){
            mProgressBar.setVisibility(View.VISIBLE);
            save_btn.setEnabled(false);
        }
        else{
            mProgressBar.setVisibility(View.INVISIBLE);
            save_btn.setEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showMainActivity();
    }

    @Override
    public void showMainActivity() {
        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
        finish();
    }
}
