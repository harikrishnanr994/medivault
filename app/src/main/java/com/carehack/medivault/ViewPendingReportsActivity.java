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
}
