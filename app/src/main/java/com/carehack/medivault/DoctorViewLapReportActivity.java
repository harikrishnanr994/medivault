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
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DoctorViewLapReportActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LabReportsRVAdapter hospitalAdapter;
    ArrayList<DataClass> reportList = new ArrayList<>();
    FloatingActionButton floatingActionButton;
    private DatabaseReference mRef;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    ProgressBar progressBar;
    TextView textView;
    String name,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_lap_report);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Lab Report");
        phone = getIntent().getStringExtra("phone");
        sharedPreferences = getSharedPreferences(Utils.pref, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        name = sharedPreferences.getString("name","");
        recyclerView =  findViewById(R.id.recyclerview);
        progressBar =  findViewById(R.id.progressbar);
        textView = findViewById(R.id.textview);
        mRef = FirebaseDatabase.getInstance().getReference();

        hospitalAdapter = new LabReportsRVAdapter(reportList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(hospitalAdapter);
        prepareData();
    }

    private void prepareData() {
        mRef.child("Lab Reports").child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postDataSnapshot : dataSnapshot.getChildren())
                {
                    String name = postDataSnapshot.child("Report").getValue(String.class);
                    DataClass dataClass = new DataClass();
                    dataClass.setTitle(name);
                    reportList.add(dataClass);
                }
                progressBar.setVisibility(View.GONE);
                if(reportList.size()!=0)
                {
                    recyclerView.setVisibility(View.VISIBLE);
                    hospitalAdapter.notifyDataSetChanged();
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
}
