package com.draxvel.simpleblog.ui.auth;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.draxvel.simpleblog.data.source.auth.Auth;
import com.draxvel.simpleblog.ui.main.MainActivity;
import com.draxvel.simpleblog.R;
import com.draxvel.simpleblog.ui.settings.SettingsActivity;
import com.draxvel.simpleblog.ui.auth.signIn.SignInFragment;
import com.draxvel.simpleblog.ui.auth.signUp.SignUpFragment;

public class LoginActivity extends AppCompatActivity  implements ILoginView {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Auth auth = new Auth();

        if(auth.isSignIn()){
            showMainActivity();
        }else{
            replaceFragment(new SignInFragment());
        }
    }

    @Override
    public void showSignIn() {
        replaceFragment(new SignInFragment());
    }

    @Override
    public void showSignUp() {
        replaceFragment(new SignUpFragment());
    }

    @Override
    public void showForgotPassword() {
        //TODO implement forgot password
    }

    @Override
    public void showMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void showSetupActivity() {
        startActivity(new Intent(LoginActivity.this, SettingsActivity.class));
        finish();
    }

    @Override
    public void showError(String e) {
        Toast.makeText(this, e, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setVisibleProgressBar(boolean s) {

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.login_container, fragment)
                //.addToBackStack(null)
                .commit();
    }
}
