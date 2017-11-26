package com.carehack.medivault;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewPendingReportsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    PendingReportAdapter pendingReportAdapter;
    List<DataClass> reportList = new ArrayList<>();
    FloatingActionButton floatingActionButton;
    private DatabaseReference mRef;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    ProgressBar progressBar;
    TextView textView;
    String name;
    public static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reports);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences(Utils.pref, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        name = sharedPreferences.getString("name","");
        recyclerView =  findViewById(R.id.recyclerview);
        progressBar =  findViewById(R.id.progressbar);
        textView = findViewById(R.id.textview);
        mRef = FirebaseDatabase.getInstance().getReference();

        pendingReportAdapter = new PendingReportAdapter(reportList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pendingReportAdapter);
        prepareData();
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewPendingReportsActivity.this,NotUserMainActivity.class));
            }
        });
    }

    private void prepareData() {
        mRef.child("Pending Reports").child("By Name").child(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postDataSnapshot : dataSnapshot.getChildren())
                {
                    String name = postDataSnapshot.child("Name").getValue(String.class);
                    String phone = postDataSnapshot.child("Phone").getValue(String.class);
                    DataClass dataClass = new DataClass();
                    dataClass.setTitle(name);
                    dataClass.setSubtitle(phone);
                    reportList.add(dataClass);
                }
                progressBar.setVisibility(View.GONE);
                floatingActionButton.setVisibility(View.VISIBLE);
                if(reportList.size()!=0)
                {
                    recyclerView.setVisibility(View.VISIBLE);
                    pendingReportAdapter.notifyDataSetChanged();
                }
                else
                {
                    textView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                Intent intent = new Intent(ViewPendingReportsActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_CODE  && resultCode  == RESULT_OK) {
                DataClass dataClass = (DataClass) data.getSerializableExtra("data");
                reportList.remove(dataClass);
                pendingReportAdapter.notifyDataSetChanged();
            }
        } catch (Exception ex) {
            Toast.makeText(ViewPendingReportsActivity.this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }
}
