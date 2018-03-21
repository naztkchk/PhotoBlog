package com.draxvel.simpleblog.ui.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.draxvel.simpleblog.R;
import com.draxvel.simpleblog.data.source.auth.Auth;
import com.draxvel.simpleblog.data.source.usersData.IUsers;
import com.draxvel.simpleblog.data.source.usersData.Users;
import com.draxvel.simpleblog.ui.auth.LoginActivity;
import com.draxvel.simpleblog.ui.main.account.AccountFragment;
import com.draxvel.simpleblog.ui.main.home.HomeFragment;
import com.draxvel.simpleblog.ui.main.notification.NotificationFragment;
import com.draxvel.simpleblog.ui.newpost.NewPostActivity;
import com.draxvel.simpleblog.ui.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    private Toolbar main_tb;

    private FloatingActionButton add_post_fab;
    private BottomNavigationView main_bnv;

    private HomeFragment homeFragment;
    private NotificationFragment notificationFragment;
    private AccountFragment accountFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_tb = findViewById(R.id.main_tb);
        setSupportActionBar(main_tb);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        add_post_fab = findViewById(R.id.add_post_fab);
        main_bnv = findViewById(R.id.main_bnv);


        //FRAGMENTS
        homeFragment = new HomeFragment();
        notificationFragment = new NotificationFragment();
        accountFragment = new AccountFragment();

        replaceFragment(homeFragment);

        main_bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.home_action:
                    replaceFragment(homeFragment);
                    return true;

                    case R.id.notification_action:
                    replaceFragment(notificationFragment);
                    return true;

                    case R.id.account_action:
                    replaceFragment(accountFragment);
                    return true;

                    default:return false;
                }
            }
        });

        add_post_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewPostActivity.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Users users = new Users();
        users.isCurrentUserExists(new IUsers.CurrentUserExistsCallback() {
            @Override
            public void onNotExists() {
                startSettingsActivity();
                finish();
            }
        });
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
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                finish();
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
        Auth auth = new Auth();
        auth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    private void startSettingsActivity(){
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
