package com.carehack.medivault;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    enum Window{HOME, PENDING}
    BottomNavigationView bottomBar;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");
        bottomBar = findViewById(R.id.bottomNavigationView);
        fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.dashboard_frag_lay, new HomeFragment())
                .commit();
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(bottomBar.getSelectedItemId() != item.getItemId()){
                    if(item.getItemId() == R.id.navigation_menu_home)
                        showWindow(Window.HOME);
                    else if(item.getItemId() == R.id.navigation_menu_pending)
                        showWindow(Window.PENDING);

                    return true;
                }
                return false;
            }
        });
    }
    private void showWindow(Window window) {
        switch (window){
            case HOME:
                fragmentManager.beginTransaction()
                        .replace(R.id.dashboard_frag_lay, new HomeFragment())
                        .commit();
                break;
            case PENDING:
                fragmentManager.beginTransaction()
                        .replace(R.id.dashboard_frag_lay, new PendingFragment())
                        .commit();
                break;

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        SharedPreferences sharedPreferences = getSharedPreferences(Utils.pref, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (item.getItemId()) {
            case R.id.log_out:
                editor.clear();
                editor.commit();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return true;
        }
        return false;
    }
}