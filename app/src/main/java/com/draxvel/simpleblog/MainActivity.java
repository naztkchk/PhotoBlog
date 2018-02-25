package com.draxvel.simpleblog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.draxvel.simpleblog.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Toolbar main_tb;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_tb = findViewById(R.id.main_tb);

        setSupportActionBar(main_tb);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_action:
                Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.settings_action:
                Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.logout_action:
                logout();
                Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

    private void logout() {
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}
