package com.carehack.medivault;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

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
}