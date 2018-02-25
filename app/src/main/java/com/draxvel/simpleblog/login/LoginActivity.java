package com.draxvel.simpleblog.login;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.draxvel.simpleblog.MainActivity;
import com.draxvel.simpleblog.R;
import com.draxvel.simpleblog.login.signIn.SignInFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser == null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.login_container, new SignInFragment())
                    //.addToBackStack(null)
                    .commit();
        }

    }

    public void showMain(){
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }
}
