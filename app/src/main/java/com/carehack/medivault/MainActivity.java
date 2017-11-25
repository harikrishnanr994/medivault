package com.carehack.medivault;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    enum Window{HOME, PENDING}
    BottomNavigationView bottomBar;
    FragmentManager fragmentManager;
    private FirebaseDatabase firebase;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebase=FirebaseDatabase.getInstance();
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
        final SharedPreferences sharedPreferences=getSharedPreferences(Utils.pref, MODE_PRIVATE);
        ref=firebase.getReference();
        ref.child("Shared Key").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String phone=sharedPreferences.getString("phone", null);

                if(!dataSnapshot.hasChild(phone))
                {
                    Log.v(">>",dataSnapshot.toString());
                    ref.child("Shared Key").child(phone).setValue(Utils.randomSequenceGenerator(15));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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